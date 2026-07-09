package com.flavor.trail.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("chat_message")
public class ChatMessage {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long sessionId;

    private Long userId;

    private String role;

    private String content;

    private String metadata;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}