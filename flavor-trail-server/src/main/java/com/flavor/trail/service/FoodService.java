package com.flavor.trail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flavor.trail.entity.Food;
import com.flavor.trail.vo.CollectResultVO;
import com.flavor.trail.vo.FoodVO;
import com.flavor.trail.vo.ProvinceVO;

import java.util.List;

public interface FoodService extends IService<Food> {

    List<ProvinceVO> getProvinces(Long userId);

    List<FoodVO> getProvinceFoods(Long provinceId, Long userId, int pageNum, int pageSize);

    FoodVO getFoodDetail(Long foodId, Long userId);

    List<FoodVO> searchFoods(String keyword, Long userId);

    CollectResultVO toggleCollect(Long foodId, Long userId);

    void recordView(Long foodId, Long userId);

    List<FoodVO> getFavorites(Long userId, int pageNum, int pageSize);
}