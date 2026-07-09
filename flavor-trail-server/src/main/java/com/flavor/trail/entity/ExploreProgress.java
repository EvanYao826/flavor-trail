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
@TableName("explore_progress")
public class ExploreProgress {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long provinceId;

    @Builder.Default
    private Integer isExplored = 0;

    @Builder.Default
    private Integer foodViewedCount = 0;

    private LocalDateTime firstExploredAt;

    private LocalDateTime lastExploredAt;

    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updatedAt;
}