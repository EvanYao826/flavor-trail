package com.flavor.trail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flavor.trail.entity.Food;
import com.flavor.trail.entity.UserFavorite;
import com.flavor.trail.mapper.FoodMapper;
import com.flavor.trail.mapper.UserFavoriteMapper;
import com.flavor.trail.service.RankingService;
import com.flavor.trail.vo.FoodVO;
import com.flavor.trail.service.FoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RankingServiceImpl implements RankingService {

    private static final String RANKING_KEY = "food:ranking";

    private final FoodMapper foodMapper;
    private final FoodService foodService;
    private final RedisTemplate<String, Object> redisTemplate;

    public RankingServiceImpl(FoodMapper foodMapper, FoodService foodService,
                              RedisTemplate<String, Object> redisTemplate) {
        this.foodMapper = foodMapper;
        this.foodService = foodService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<FoodVO> getRanking(String type, Long userId) {
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Food::getStatus, 1);

        if ("collect".equalsIgnoreCase(type)) {
            wrapper.orderByDesc(Food::getCollectCount);
        } else {
            wrapper.orderByDesc(Food::getViewCount);
        }

        wrapper.last("LIMIT 20");
        List<Food> foods = foodMapper.selectList(wrapper);

        return foods.stream()
                .map(f -> foodService.getFoodDetail(f.getId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public void updateRanking() {
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Food::getStatus, 1);
        List<Food> foods = foodMapper.selectList(wrapper);

        foods.forEach(food -> {
            redisTemplate.opsForZSet().add(RANKING_KEY, food.getId().toString(), food.getCollectCount());
        });

        log.info("Ranking updated: {} foods", foods.size());
    }
}