package com.flavor.trail.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollectResultVO {

    private Boolean isCollected;

    private Integer collectCount;
}