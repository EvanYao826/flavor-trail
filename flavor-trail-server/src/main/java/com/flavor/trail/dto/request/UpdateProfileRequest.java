package com.flavor.trail.dto.request;

import lombok.Data;

@Data
public class UpdateProfileRequest {

    private String nickname;

    private String avatarUrl;

    private Integer gender;
}