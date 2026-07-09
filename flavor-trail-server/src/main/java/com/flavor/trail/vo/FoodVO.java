package com.flavor.trail.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FoodVO {

    private Long id;

    private Long provinceId;

    private String provinceName;

    private String name;

    private String description;

    private List<String> ingredients;

    private List<String> steps;

    private String coverUrl;

    private String videoUrl;

    private List<String> tags;

    private Integer viewCount;

    private Integer likeCount;

    private Integer collectCount;

    private Float avgRating;

    private Boolean isCollected;
}