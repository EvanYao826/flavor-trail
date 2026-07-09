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
@TableName("chat_session")
public class ChatSession {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String title;

    @Builder.Default
    private String type = "general";

    @Builder.Default
    private Integer status = 1;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updatedAt;
}