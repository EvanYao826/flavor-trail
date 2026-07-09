package com.flavor.trail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flavor.trail.dto.request.CreateSessionRequest;
import com.flavor.trail.dto.request.SendMessageRequest;
import com.flavor.trail.entity.ChatSession;
import com.flavor.trail.vo.ChatMessageVO;
import com.flavor.trail.vo.ChatSessionVO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface ChatService extends IService<ChatSession> {

    ChatSessionVO createSession(Long userId, CreateSessionRequest request);

    List<ChatSessionVO> getSessions(Long userId, int pageNum, int pageSize);

    List<ChatMessageVO> getMessages(Long sessionId, Long userId);

    SseEmitter sendMessage(Long sessionId, Long userId, SendMessageRequest request);

    void deleteSession(Long sessionId, Long userId);
}