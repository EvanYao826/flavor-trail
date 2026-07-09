package com.flavor.trail.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoVO {

    private Long id;

    private String username;

    private String nickname;

    private String avatarUrl;

    private String phone;

    private Integer gender;
}