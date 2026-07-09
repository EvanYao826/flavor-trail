package com.flavor.trail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flavor.trail.common.BusinessException;
import com.flavor.trail.dto.request.LoginRequest;
import com.flavor.trail.dto.request.RegisterRequest;
import com.flavor.trail.dto.request.UpdateProfileRequest;
import com.flavor.trail.entity.User;
import com.flavor.trail.mapper.UserMapper;
import com.flavor.trail.security.JwtTokenUtil;
import com.flavor.trail.service.UserService;
import com.flavor.trail.vo.LoginVO;
import com.flavor.trail.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public UserServiceImpl(PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public LoginVO register(RegisterRequest request) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        if (count(wrapper) > 0) {
            throw new BusinessException(2001, "用户名已存在");
        }

        User user = User.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname() != null ? request.getNickname() : request.getUsername())
                .build();

        save(user);
        log.info("User registered: {}", user.getUsername());

        return buildLoginVO(user);
    }

    @Override
    public LoginVO login(LoginRequest request) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        User user = getOne(wrapper);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BusinessException(2002, "用户名或密码错误");
        }

        if (user.getStatus() != 1) {
            throw new BusinessException(1003, "账号已禁用");
        }

        log.info("User logged in: {}", user.getUsername());
        return buildLoginVO(user);
    }

    @Override
    public UserInfoVO getProfile(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw BusinessException.notFound();
        }
        return UserInfoVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .phone(user.getPhone())
                .gender(user.getGender())
                .build();
    }

    @Override
    public void updateProfile(Long userId, UpdateProfileRequest request) {
        User user = getById(userId);
        if (user == null) {
            throw BusinessException.notFound();
        }

        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }

        updateById(user);
    }

    @Override
    public User getById(Long userId) {
        return super.getById(userId);
    }

    private LoginVO buildLoginVO(User user) {
        String token = jwtTokenUtil.generateToken(user.getId(), user.getUsername());
        UserInfoVO userInfo = UserInfoVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .phone(user.getPhone())
                .gender(user.getGender())
                .build();
        return LoginVO.builder().token(token).userInfo(userInfo).build();
    }
}