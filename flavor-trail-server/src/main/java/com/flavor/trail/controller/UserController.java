package com.flavor.trail.controller;

import com.flavor.trail.common.Result;
import com.flavor.trail.dto.request.UpdatePasswordRequest;
import com.flavor.trail.dto.request.UpdateProfileRequest;
import com.flavor.trail.entity.User;
import com.flavor.trail.service.UserService;
import com.flavor.trail.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "获取个人信息")
    @GetMapping("/profile")
    public Result<UserInfoVO> getProfile(@AuthenticationPrincipal User user) {
        return Result.success(userService.getProfile(user.getId()));
    }

    @Operation(summary = "修改个人信息")
    @PutMapping("/profile")
    public Result<Void> updateProfile(@AuthenticationPrincipal User user,
                                      @RequestBody UpdateProfileRequest request) {
        userService.updateProfile(user.getId(), request);
        return Result.success();
    }

    @Operation(summary = "修改密码")
    @PutMapping("/password")
    public Result<Void> updatePassword(@AuthenticationPrincipal User user,
                                       @Valid @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(user.getId(), request.getOldPassword(), request.getNewPassword());
        return Result.success();
    }
}