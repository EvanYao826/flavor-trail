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
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(unique = true)
    private String username;

    private String passwordHash;

    private String nickname;

    private String avatarUrl;

    @TableField(unique = true)
    private String phone;

    @Builder.Default
    private Integer gender = 0;

    @Builder.Default
    private Integer status = 1;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updatedAt;
}