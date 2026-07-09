package com.flavor.trail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flavor.trail.common.BusinessException;
import com.flavor.trail.entity.Food;
import com.flavor.trail.entity.Province;
import com.flavor.trail.entity.UserFavorite;
import com.flavor.trail.mapper.FoodMapper;
import com.flavor.trail.mapper.ProvinceMapper;
import com.flavor.trail.mapper.UserFavoriteMapper;
import com.flavor.trail.service.ExploreService;
import com.flavor.trail.service.FoodService;
import com.flavor.trail.vo.CollectResultVO;
import com.flavor.trail.vo.FoodVO;
import com.flavor.trail.vo.ProvinceVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements FoodService {

    private final ProvinceMapper provinceMapper;
    private final UserFavoriteMapper userFavoriteMapper;
    private final ExploreService exploreService;
    private final ObjectMapper objectMapper;

    public FoodServiceImpl(ProvinceMapper provinceMapper, UserFavoriteMapper userFavoriteMapper,
                           ExploreService exploreService, ObjectMapper objectMapper) {
        this.provinceMapper = provinceMapper;
        this.userFavoriteMapper = userFavoriteMapper;
        this.exploreService = exploreService;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<ProvinceVO> getProvinces(Long userId) {
        List<Province> provinces = provinceMapper.selectList(null);
        return provinces.stream()
                .map(p -> ProvinceVO.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .code(p.getCode())
                        .description(p.getDescription())
                        .sortOrder(p.getSortOrder())
                        .isExplored(false)
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<FoodVO> getProvinceFoods(Long provinceId, Long userId, int pageNum, int pageSize) {
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Food::getProvinceId, provinceId)
                .eq(Food::getStatus, 1)
                .orderByDesc(Food::getCollectCount);

        Page<Food> page = new Page<>(pageNum, pageSize);
        IPage<Food> foodPage = page(page, wrapper);

        return foodPage.getRecords().stream()
                .map(f -> convertToVO(f, userId))
                .collect(Collectors.toList());
    }

    @Override
    public FoodVO getFoodDetail(Long foodId, Long userId) {
        Food food = getById(foodId);
        if (food == null || food.getStatus() != 1) {
            throw BusinessException.notFound();
        }
        return convertToVO(food, userId);
    }

    @Override
    public List<FoodVO> searchFoods(String keyword, Long userId) {
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Food::getStatus, 1)
                .and(w -> w.like(Food::getName, keyword)
                        .or()
                        .like(Food::getDescription, keyword));

        List<Food> foods = list(wrapper);
        return foods.stream()
                .map(f -> convertToVO(f, userId))
                .collect(Collectors.toList());
    }

    @Override
    public CollectResultVO toggleCollect(Long foodId, Long userId) {
        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavorite::getUserId, userId)
                .eq(UserFavorite::getFoodId, foodId);

        UserFavorite existing = userFavoriteMapper.selectOne(wrapper);

        if (existing != null) {
            userFavoriteMapper.deleteById(existing.getId());
            lambdaUpdate()
                    .setSql("collect_count = collect_count - 1")
                    .eq(Food::getId, foodId)
                    .update();
            return CollectResultVO.builder()
                    .isCollected(false)
                    .collectCount(getById(foodId).getCollectCount() - 1)
                    .build();
        } else {
            UserFavorite favorite = UserFavorite.builder()
                    .userId(userId)
                    .foodId(foodId)
                    .build();
            userFavoriteMapper.insert(favorite);
            lambdaUpdate()
                    .setSql("collect_count = collect_count + 1")
                    .eq(Food::getId, foodId)
                    .update();
            return CollectResultVO.builder()
                    .isCollected(true)
                    .collectCount(getById(foodId).getCollectCount() + 1)
                    .build();
        }
    }

    @Override
    public void recordView(Long foodId, Long userId) {
        Food food = getById(foodId);
        if (food == null) {
            return;
        }

        lambdaUpdate()
                .setSql("view_count = view_count + 1")
                .eq(Food::getId, foodId)
                .update();

        exploreService.updateProgress(userId, food.getProvinceId());
    }

    @Override
    public List<FoodVO> getFavorites(Long userId, int pageNum, int pageSize) {
        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavorite::getUserId, userId)
                .orderByDesc(UserFavorite::getCreatedAt);

        Page<UserFavorite> page = new Page<>(pageNum, pageSize);
        IPage<UserFavorite> favPage = userFavoriteMapper.selectPage(page, wrapper);

        return favPage.getRecords().stream()
                .map(fav -> {
                    Food food = getById(fav.getFoodId());
                    return food != null ? convertToVO(food, userId) : null;
                })
                .filter(v -> v != null)
                .collect(Collectors.toList());
    }

    private FoodVO convertToVO(Food food, Long userId) {
        Province province = provinceMapper.selectById(food.getProvinceId());

        List<String> ingredients = parseJsonArray(food.getIngredients());
        List<String> steps = parseJsonArray(food.getStepsJson());
        List<String> tags = parseJsonArray(food.getTags());

        LambdaQueryWrapper<UserFavorite> favWrapper = new LambdaQueryWrapper<>();
        favWrapper.eq(UserFavorite::getUserId, userId)
                .eq(UserFavorite::getFoodId, food.getId());
        boolean isCollected = userFavoriteMapper.selectCount(favWrapper) > 0;

        return FoodVO.builder()
                .id(food.getId())
                .provinceId(food.getProvinceId())
                .provinceName(province != null ? province.getName() : "")
                .name(food.getName())
                .description(food.getDescription())
                .ingredients(ingredients)
                .steps(steps)
                .coverUrl(food.getCoverUrl())
                .videoUrl(food.getVideoUrl())
                .tags(tags)
                .viewCount(food.getViewCount())
                .likeCount(food.getLikeCount())
                .collectCount(food.getCollectCount())
                .avgRating(food.getAvgRating())
                .isCollected(isCollected)
                .build();
    }

    private List<String> parseJsonArray(String json) {
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            log.warn("Failed to parse JSON array: {}", json);
            return new ArrayList<>();
        }
    }
}