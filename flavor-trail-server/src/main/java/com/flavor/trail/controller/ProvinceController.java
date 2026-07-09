package com.flavor.trail.controller;

import com.flavor.trail.common.Result;
import com.flavor.trail.entity.User;
import com.flavor.trail.service.FoodService;
import com.flavor.trail.vo.ProvinceVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "省份管理")
@RestController
@RequestMapping("/api/provinces")
public class ProvinceController {

    private final FoodService foodService;

    public ProvinceController(FoodService foodService) {
        this.foodService = foodService;
    }

    @Operation(summary = "获取省份列表")
    @GetMapping
    public Result<List<ProvinceVO>> getProvinces(@AuthenticationPrincipal User user) {
        return Result.success(foodService.getProvinces(user.getId()));
    }
}