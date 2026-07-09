package com.flavor.trail.service;

import com.flavor.trail.vo.ExploreProgressVO;
import com.flavor.trail.vo.ExploreStatsVO;

import java.util.List;

public interface ExploreService {

    List<ExploreProgressVO> getProgress(Long userId);

    ExploreStatsVO getStats(Long userId);

    void updateProgress(Long userId, Long provinceId);
}