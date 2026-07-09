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
@TableName("food")
public class Food {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long provinceId;

    private String name;

    private String description;

    private String ingredients;

    private String stepsJson;

    private String coverUrl;

    private String videoUrl;

    private String tags;

    @Builder.Default
    private Integer viewCount = 0;

    @Builder.Default
    private Integer likeCount = 0;

    @Builder.Default
    private Integer collectCount = 0;

    @Builder.Default
    private Float avgRating = 0f;

    @Builder.Default
    private Integer status = 1;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}