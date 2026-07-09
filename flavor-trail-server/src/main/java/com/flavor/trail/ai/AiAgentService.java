package com.flavor.trail.ai;

import com.flavor.trail.ai.router.IntentRouter;
import com.flavor.trail.ai.router.IntentType;
import com.flavor.trail.ai.tools.FoodSearchTool;
import com.flavor.trail.ai.tools.WeatherTool;
import com.flavor.trail.ai.tools.NutritionTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AiAgentService {

    private final IntentRouter intentRouter;
    private final FoodSearchTool foodSearchTool;
    private final WeatherTool weatherTool;
    private final NutritionTool nutritionTool;

    public AiAgentService(IntentRouter intentRouter, FoodSearchTool foodSearchTool,
                          WeatherTool weatherTool, NutritionTool nutritionTool) {
        this.intentRouter = intentRouter;
        this.foodSearchTool = foodSearchTool;
        this.weatherTool = weatherTool;
        this.nutritionTool = nutritionTool;
    }

    public Map<String, Object> process(String userInput, String city) {
        IntentType intent = intentRouter.classify(userInput);
        Map<String, Object> result = new HashMap<>();
        result.put("intent", intent.name());

        switch (intent) {
            case WEATHER:
                Map<String, Object> weather = weatherTool.getWeather(city != null ? city : "北京");
                result.put("weather", weather);
                break;
            case INGREDIENT:
                result.put("foods", foodSearchTool.searchByIngredient(extractIngredients(userInput)));
                break;
            case NUTRITION:
                result.put("nutrition", nutritionTool.getNutrition(extractFoodName(userInput)));
                break;
            case CHAT:
            default:
                result.put("response", "我是你的美食助手，请问有什么可以帮您？");
                break;
        }

        return result;
    }

    private java.util.List<String> extractIngredients(String input) {
        return java.util.List.of("番茄", "鸡蛋", "猪肉");
    }

    private String extractFoodName(String input) {
        return input;
    }
}