package com.flavor.trail.ai.tools;

import com.flavor.trail.entity.Food;
import com.flavor.trail.mapper.FoodMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FoodSearchTool {

    private final FoodMapper foodMapper;

    public FoodSearchTool(FoodMapper foodMapper) {
        this.foodMapper = foodMapper;
    }

    public List<Food> searchByIngredient(List<String> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return List.of();
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ingredients.size(); i++) {
            if (i > 0) {
                sb.append(" OR ");
            }
            sb.append("ingredients LIKE '%").append(ingredients.get(i)).append("%'");
        }

        List<Food> foods = foodMapper.selectList(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper.<Food>query()
                .eq(Food::getStatus, 1)
                .apply(sb.toString())
                .last("LIMIT 10"));

        log.info("Found {} foods matching ingredients: {}", foods.size(), ingredients);
        return foods;
    }

    public Food getFoodDetail(Long foodId) {
        return foodMapper.selectById(foodId);
    }
}