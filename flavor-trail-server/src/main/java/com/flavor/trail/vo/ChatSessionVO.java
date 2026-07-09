package com.flavor.trail.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatSessionVO {

    private Long id;

    private String title;

    private String type;

    private LocalDateTime updatedAt;

    private Integer messageCount;
}