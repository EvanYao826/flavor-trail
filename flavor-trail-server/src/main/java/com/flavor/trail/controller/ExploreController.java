package com.flavor.trail.controller;

import com.flavor.trail.common.Result;
import com.flavor.trail.entity.User;
import com.flavor.trail.service.ExploreService;
import com.flavor.trail.vo.ExploreProgressVO;
import com.flavor.trail.vo.ExploreStatsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "探索进度")
@RestController
@RequestMapping("/api/explore")
public class ExploreController {

    private final ExploreService exploreService;

    public ExploreController(ExploreService exploreService) {
        this.exploreService = exploreService;
    }

    @Operation(summary = "获取探索进度")
    @GetMapping("/progress")
    public Result<List<ExploreProgressVO>> getProgress(@AuthenticationPrincipal User user) {
        return Result.success(exploreService.getProgress(user.getId()));
    }

    @Operation(summary = "获取探索统计")
    @GetMapping("/stats")
    public Result<ExploreStatsVO> getStats(@AuthenticationPrincipal User user) {
        return Result.success(exploreService.getStats(user.getId()));
    }
}