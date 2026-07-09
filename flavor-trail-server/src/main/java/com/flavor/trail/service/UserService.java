package com.flavor.trail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flavor.trail.dto.request.LoginRequest;
import com.flavor.trail.dto.request.RegisterRequest;
import com.flavor.trail.dto.request.UpdateProfileRequest;
import com.flavor.trail.entity.User;
import com.flavor.trail.vo.LoginVO;
import com.flavor.trail.vo.UserInfoVO;

public interface UserService extends IService<User> {

    LoginVO register(RegisterRequest request);

    LoginVO login(LoginRequest request);

    UserInfoVO getProfile(Long userId);

    void updateProfile(Long userId, UpdateProfileRequest request);

    User getById(Long userId);
}