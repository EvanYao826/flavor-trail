package com.flavor.trail.service;

import com.flavor.trail.vo.FoodVO;

import java.util.List;

public interface RankingService {

    List<FoodVO> getRanking(String type, Long userId);

    void updateRanking();
}