package com.flavor.trail.ai.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class NutritionTool {

    public Map<String, Object> getNutrition(String foodName) {
        log.info("Getting nutrition for food: {}", foodName);

        return Map.of(
                "foodName", foodName,
                "protein", 20.5,
                "fat", 15.3,
                "carbohydrate", 30.2,
                "calorie", 350,
                "description", "这是一份估算数据，实际营养成分可能有所不同"
        );
    }
}