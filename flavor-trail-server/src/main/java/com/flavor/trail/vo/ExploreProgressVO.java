package com.flavor.trail.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExploreProgressVO {

    private Long provinceId;

    private String provinceName;

    private Boolean isExplored;

    private Integer foodViewedCount;
}