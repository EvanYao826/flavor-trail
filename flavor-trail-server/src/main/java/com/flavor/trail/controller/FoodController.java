package com.flavor.trail.controller;

import com.flavor.trail.common.Result;
import com.flavor.trail.entity.User;
import com.flavor.trail.service.FoodService;
import com.flavor.trail.service.RankingService;
import com.flavor.trail.vo.CollectResultVO;
import com.flavor.trail.vo.FoodVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "美食管理")
@RestController
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService foodService;
    private final RankingService rankingService;

    public FoodController(FoodService foodService, RankingService rankingService) {
        this.foodService = foodService;
        this.rankingService = rankingService;
    }

    @Operation(summary = "获取省份美食列表")
    @GetMapping("/province/{provinceId}")
    public Result<List<FoodVO>> getProvinceFoods(@PathVariable Long provinceId,
                                                 @RequestParam(defaultValue = "1") int pageNum,
                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                 @AuthenticationPrincipal User user) {
        return Result.success(foodService.getProvinceFoods(provinceId, user.getId(), pageNum, pageSize));
    }

    @Operation(summary = "获取美食详情")
    @GetMapping("/{id}")
    public Result<FoodVO> getFoodDetail(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return Result.success(foodService.getFoodDetail(id, user.getId()));
    }

    @Operation(summary = "搜索美食")
    @GetMapping("/search")
    public Result<List<FoodVO>> searchFoods(@RequestParam String keyword, @AuthenticationPrincipal User user) {
        return Result.success(foodService.searchFoods(keyword, user.getId()));
    }

    @Operation(summary = "收藏/取消收藏")
    @PostMapping("/{id}/collect")
    public Result<CollectResultVO> toggleCollect(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return Result.success(foodService.toggleCollect(id, user.getId()));
    }

    @Operation(summary = "记录浏览")
    @PostMapping("/{id}/view")
    public Result<Void> recordView(@PathVariable Long id, @AuthenticationPrincipal User user) {
        foodService.recordView(id, user.getId());
        return Result.success();
    }

    @Operation(summary = "美食排行榜")
    @GetMapping("/ranking")
    public Result<List<FoodVO>> getRanking(@RequestParam(defaultValue = "collect") String type,
                                           @AuthenticationPrincipal User user) {
        return Result.success(rankingService.getRanking(type, user.getId()));
    }

    @Operation(summary = "我的收藏")
    @GetMapping("/favorites")
    public Result<List<FoodVO>> getFavorites(@AuthenticationPrincipal User user,
                                             @RequestParam(defaultValue = "1") int pageNum,
                                             @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(List.of());
    }
}