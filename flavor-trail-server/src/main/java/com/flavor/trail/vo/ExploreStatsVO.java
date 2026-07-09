package com.flavor.trail.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExploreStatsVO {

    private Integer exploredCount;

    private Integer totalCount;

    private Float percentage;
}