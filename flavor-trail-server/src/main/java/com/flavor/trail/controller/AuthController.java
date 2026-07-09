package com.flavor.trail.controller;

import com.flavor.trail.common.Result;
import com.flavor.trail.dto.request.LoginRequest;
import com.flavor.trail.dto.request.RegisterRequest;
import com.flavor.trail.entity.User;
import com.flavor.trail.service.UserService;
import com.flavor.trail.vo.LoginVO;
import com.flavor.trail.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户认证")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<LoginVO> register(@Valid @RequestBody RegisterRequest request) {
        return Result.success(userService.register(request));
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(userService.login(request));
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/profile")
    public Result<UserInfoVO> getProfile(@AuthenticationPrincipal User user) {
        return Result.success(userService.getProfile(user.getId()));
    }
}