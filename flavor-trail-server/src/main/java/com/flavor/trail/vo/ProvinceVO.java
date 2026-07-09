package com.flavor.trail.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProvinceVO {

    private Long id;

    private String name;

    private String code;

    private String description;

    private Integer sortOrder;

    private Boolean isExplored;
}