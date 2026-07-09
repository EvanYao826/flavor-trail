package com.flavor.trail.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ChatMessageVO {

    private Long id;

    private Long sessionId;

    private String role;

    private String content;

    private Map<String, Object> metadata;

    private LocalDateTime createdAt;
}