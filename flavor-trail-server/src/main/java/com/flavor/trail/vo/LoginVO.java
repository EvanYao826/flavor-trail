package com.flavor.trail.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginVO {

    private String token;

    private UserInfoVO userInfo;
}