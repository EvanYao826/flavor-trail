package com.flavor.trail.controller;

import com.flavor.trail.common.Result;
import com.flavor.trail.dto.request.CreateSessionRequest;
import com.flavor.trail.dto.request.SendMessageRequest;
import com.flavor.trail.entity.User;
import com.flavor.trail.service.ChatService;
import com.flavor.trail.vo.ChatMessageVO;
import com.flavor.trail.vo.ChatSessionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Tag(name = "AI对话")
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @Operation(summary = "创建会话")
    @PostMapping("/sessions")
    public Result<ChatSessionVO> createSession(@AuthenticationPrincipal User user,
                                               @RequestBody CreateSessionRequest request) {
        return Result.success(chatService.createSession(user.getId(), request));
    }

    @Operation(summary = "获取会话列表")
    @GetMapping("/sessions")
    public Result<List<ChatSessionVO>> getSessions(@AuthenticationPrincipal User user,
                                                   @RequestParam(defaultValue = "1") int pageNum,
                                                   @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(chatService.getSessions(user.getId(), pageNum, pageSize));
    }

    @Operation(summary = "获取会话消息")
    @GetMapping("/sessions/{id}/messages")
    public Result<List<ChatMessageVO>> getMessages(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return Result.success(chatService.getMessages(id, user.getId()));
    }

    @Operation(summary = "发送消息（SSE流式）")
    @PostMapping(value = "/sessions/{id}/send", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sendMessage(@PathVariable Long id,
                                  @AuthenticationPrincipal User user,
                                  @Valid @RequestBody SendMessageRequest request) {
        return chatService.sendMessage(id, user.getId(), request);
    }

    @Operation(summary = "删除会话")
    @DeleteMapping("/sessions/{id}")
    public Result<Void> deleteSession(@PathVariable Long id, @AuthenticationPrincipal User user) {
        chatService.deleteSession(id, user.getId());
        return Result.success();
    }
}