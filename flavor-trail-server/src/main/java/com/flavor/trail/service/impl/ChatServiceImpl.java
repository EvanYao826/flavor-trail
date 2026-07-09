package com.flavor.trail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flavor.trail.ai.AiAgentService;
import com.flavor.trail.common.BusinessException;
import com.flavor.trail.dto.request.CreateSessionRequest;
import com.flavor.trail.dto.request.SendMessageRequest;
import com.flavor.trail.entity.ChatMessage;
import com.flavor.trail.entity.ChatSession;
import com.flavor.trail.mapper.ChatMessageMapper;
import com.flavor.trail.mapper.ChatSessionMapper;
import com.flavor.trail.service.ChatService;
import com.flavor.trail.vo.ChatMessageVO;
import com.flavor.trail.vo.ChatSessionVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatServiceImpl extends ServiceImpl<ChatSessionMapper, ChatSession> implements ChatService {

    private final ChatMessageMapper chatMessageMapper;
    private final ObjectMapper objectMapper;
    private final AiAgentService aiAgentService;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public ChatServiceImpl(ChatMessageMapper chatMessageMapper, ObjectMapper objectMapper,
                          AiAgentService aiAgentService) {
        this.chatMessageMapper = chatMessageMapper;
        this.objectMapper = objectMapper;
        this.aiAgentService = aiAgentService;
    }

    @Override
    public ChatSessionVO createSession(Long userId, CreateSessionRequest request) {
        ChatSession session = ChatSession.builder()
                .userId(userId)
                .title("新会话")
                .type(request.getType())
                .build();

        save(session);
        log.info("Created chat session: userId={}, sessionId={}", userId, session.getId());

        return ChatSessionVO.builder()
                .id(session.getId())
                .title(session.getTitle())
                .type(session.getType())
                .updatedAt(session.getUpdatedAt())
                .messageCount(0)
                .build();
    }

    @Override
    public List<ChatSessionVO> getSessions(Long userId, int pageNum, int pageSize) {
        LambdaQueryWrapper<ChatSession> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatSession::getUserId, userId)
                .orderByDesc(ChatSession::getUpdatedAt);

        Page<ChatSession> page = new Page<>(pageNum, pageSize);
        IPage<ChatSession> sessionPage = page(page, wrapper);

        return sessionPage.getRecords().stream()
                .map(this::convertToSessionVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatMessageVO> getMessages(Long sessionId, Long userId) {
        ChatSession session = getById(sessionId);
        if (session == null || !session.getUserId().equals(userId)) {
            throw BusinessException.notFound();
        }

        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getSessionId, sessionId)
                .orderByAsc(ChatMessage::getCreatedAt);

        List<ChatMessage> messages = chatMessageMapper.selectList(wrapper);
        return messages.stream()
                .map(this::convertToMessageVO)
                .collect(Collectors.toList());
    }

    @Override
    public SseEmitter sendMessage(Long sessionId, Long userId, SendMessageRequest request) {
        ChatSession session = getById(sessionId);
        if (session == null || !session.getUserId().equals(userId)) {
            throw BusinessException.notFound();
        }

        ChatMessage userMessage = ChatMessage.builder()
                .sessionId(sessionId)
                .userId(userId)
                .role("user")
                .content(request.getContent())
                .build();
        chatMessageMapper.insert(userMessage);

        SseEmitter emitter = new SseEmitter(300000L);

        executorService.execute(() -> {
            try {
                String aiResponse = aiAgentService.generateResponse(request.getContent(), request.getCity());

                StringBuilder accumulated = new StringBuilder();
                for (int i = 0; i < aiResponse.length(); i++) {
                    accumulated.append(aiResponse.charAt(i));
                    emitter.send(SseEmitter.event()
                            .name("message")
                            .data("{\"delta\":\"" + escapeJson(aiResponse.charAt(i)) + "\",\"done\":false}"));
                    Thread.sleep(50);
                }

                ChatMessage aiMessage = ChatMessage.builder()
                        .sessionId(sessionId)
                        .userId(userId)
                        .role("assistant")
                        .content(aiResponse)
                        .build();
                chatMessageMapper.insert(aiMessage);

                session.setUpdatedAt(LocalDateTime.now());
                updateById(session);

                emitter.send(SseEmitter.event()
                        .name("finish")
                        .data("{\"sessionId\":" + sessionId + ",\"messageId\":" + aiMessage.getId() + ",\"done\":true}"));

                emitter.complete();
            } catch (IOException e) {
                emitter.completeWithError(e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                emitter.completeWithError(e);
            } catch (Exception e) {
                log.error("AI response error", e);
                try {
                    emitter.send(SseEmitter.event()
                            .name("message")
                            .data("{\"delta\":\"抱歉，我现在有点忙，请稍后再试。\",\"done\":false}"));
                    emitter.send(SseEmitter.event()
                            .name("finish")
                            .data("{\"sessionId\":" + sessionId + ",\"done\":true}"));
                    emitter.complete();
                } catch (IOException ex) {
                    emitter.completeWithError(ex);
                }
            }
        });

        return emitter;
    }

    private String escapeJson(char c) {
        if (c == '"') return "\\\"";
        if (c == '\\') return "\\\\";
        if (c == '\n') return "\\n";
        if (c == '\r') return "\\r";
        if (c == '\t') return "\\t";
        return String.valueOf(c);
    }

    @Override
    public void deleteSession(Long sessionId, Long userId) {
        ChatSession session = getById(sessionId);
        if (session == null || !session.getUserId().equals(userId)) {
            throw BusinessException.notFound();
        }

        LambdaQueryWrapper<ChatMessage> msgWrapper = new LambdaQueryWrapper<>();
        msgWrapper.eq(ChatMessage::getSessionId, sessionId);
        chatMessageMapper.delete(msgWrapper);

        removeById(sessionId);
        log.info("Deleted chat session: userId={}, sessionId={}", userId, sessionId);
    }

    private ChatSessionVO convertToSessionVO(ChatSession session) {
        LambdaQueryWrapper<ChatMessage> msgWrapper = new LambdaQueryWrapper<>();
        msgWrapper.eq(ChatMessage::getSessionId, session.getId());
        int messageCount = (int) chatMessageMapper.selectCount(msgWrapper);

        return ChatSessionVO.builder()
                .id(session.getId())
                .title(session.getTitle())
                .type(session.getType())
                .updatedAt(session.getUpdatedAt())
                .messageCount(messageCount)
                .build();
    }

    private ChatMessageVO convertToMessageVO(ChatMessage message) {
        Map<String, Object> metadata = null;
        if (message.getMetadata() != null && !message.getMetadata().isEmpty()) {
            try {
                metadata = objectMapper.readValue(message.getMetadata(), new TypeReference<Map<String, Object>>() {});
            } catch (Exception e) {
                log.warn("Failed to parse metadata: {}", message.getMetadata());
            }
        }

        return ChatMessageVO.builder()
                .id(message.getId())
                .sessionId(message.getSessionId())
                .role(message.getRole())
                .content(message.getContent())
                .metadata(metadata)
                .createdAt(message.getCreatedAt())
                .build();
    }
}