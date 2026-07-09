package com.flavor.trail.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("province")
public class Province {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    @TableField(unique = true)
    private String code;

    private String description;

    @Builder.Default
    private Integer sortOrder = 0;
}