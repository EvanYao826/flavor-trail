package com.flavor.trail.ai;

import com.flavor.trail.ai.router.IntentRouter;
import com.flavor.trail.ai.router.IntentType;
import com.flavor.trail.ai.tools.FoodSearchTool;
import com.flavor.trail.ai.tools.WeatherTool;
import com.flavor.trail.ai.tools.NutritionTool;
import com.flavor.trail.vo.FoodVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class AiAgentService {

    private final IntentRouter intentRouter;
    private final FoodSearchTool foodSearchTool;
    private final WeatherTool weatherTool;
    private final NutritionTool nutritionTool;

    private static final List<String> COMMON_INGREDIENTS = List.of(
            "番茄", "鸡蛋", "猪肉", "牛肉", "羊肉", "鸡肉", "鱼", "虾",
            "土豆", "白菜", "黄瓜", "胡萝卜", "青椒", "茄子", "豆腐",
            "米饭", "面条", "面粉", "洋葱", "大蒜", "生姜", "辣椒",
            "酱油", "盐", "糖", "醋", "料酒", "油", "葱"
    );

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
                result.put("response", generateWeatherRecommendation(weather, userInput));
                break;
            case INGREDIENT:
                List<String> ingredients = extractIngredients(userInput);
                List<FoodVO> foods = foodSearchTool.searchByIngredient(ingredients);
                result.put("foods", foods);
                result.put("response", generateIngredientRecommendation(ingredients, foods));
                break;
            case NUTRITION:
                String foodName = extractFoodName(userInput);
                Map<String, Object> nutrition = nutritionTool.getNutrition(foodName);
                result.put("nutrition", nutrition);
                result.put("response", generateNutritionResponse(foodName, nutrition));
                break;
            case CHAT:
            default:
                result.put("response", generateChatResponse(userInput));
                break;
        }

        return result;
    }

    public String generateResponse(String userInput, String city) {
        Map<String, Object> result = process(userInput, city);
        return result.get("response").toString();
    }

    private String generateWeatherRecommendation(Map<String, Object> weather, String userInput) {
        String city = weather.get("city").toString();
        Integer temp = (Integer) weather.get("temperature");
        String condition = weather.get("weather").toString();

        StringBuilder response = new StringBuilder();
        response.append(String.format("%s当前天气%s，温度%d℃。", city, condition, temp));

        if (temp > 30) {
            response.append("天气炎热，推荐吃清爽解暑的菜品，比如凉拌菜、凉皮、绿豆汤等。");
        } else if (temp < 10) {
            response.append("天气寒冷，适合吃暖身暖胃的菜品，比如火锅、羊肉汤、炖菜等。");
        } else if (condition.contains("雨") || condition.contains("雪")) {
            response.append("天气不好，适合在家做些简单温馨的家常菜。");
        } else {
            response.append("天气不错，可以尝试做一些新鲜的时令菜品。");
        }

        return response.toString();
    }

    private String generateIngredientRecommendation(List<String> ingredients, List<FoodVO> foods) {
        if (foods.isEmpty()) {
            return String.format("抱歉，没有找到使用[%s]的菜品，试试其他食材吧！", String.join("、", ingredients));
        }

        StringBuilder response = new StringBuilder();
        response.append(String.format("使用[%s]可以做这些菜：\n", String.join("、", ingredients)));
        for (int i = 0; i < Math.min(5, foods.size()); i++) {
            FoodVO food = foods.get(i);
            response.append(String.format("%d. %s - %s\n", i + 1, food.getName(), food.getDescription()));
        }
        response.append("想了解哪个菜品的做法？");

        return response.toString();
    }

    private String generateNutritionResponse(String foodName, Map<String, Object> nutrition) {
        return String.format("%s的营养信息：\n" +
                "热量：%.1f千卡\n" +
                "蛋白质：%.1f克\n" +
                "脂肪：%.1f克\n" +
                "碳水化合物：%.1f克\n" +
                "膳食纤维：%.1f克\n" +
                "健康评分：%.1f分",
                foodName,
                nutrition.get("calories"),
                nutrition.get("protein"),
                nutrition.get("fat"),
                nutrition.get("carbohydrates"),
                nutrition.get("fiber"),
                nutrition.get("healthScore"));
    }

    private String generateChatResponse(String userInput) {
        if (userInput.contains("你好") || userInput.contains("hi") || userInput.contains("hello")) {
            return "你好！我是寻味中国的美食助手，请问有什么可以帮您？\n您可以问我：\n- 今天天气怎么样，适合吃什么\n- 我有番茄和鸡蛋，能做什么菜\n- 宫保鸡丁有什么营养";
        } else if (userInput.contains("谢谢") || userInput.contains("感谢")) {
            return "不客气！祝您用餐愉快！";
        } else if (userInput.contains("再见") || userInput.contains("拜拜")) {
            return "再见！下次再来寻味中国！";
        }

        return "我是你的美食助手，您可以问我关于美食推荐、食材搭配、营养信息等问题。\n比如：\n- 今天吃什么\n- 四川有什么特色菜\n- 红烧肉怎么做";
    }

    private List<String> extractIngredients(String input) {
        List<String> found = new ArrayList<>();
        String lowerInput = input.toLowerCase();

        for (String ingredient : COMMON_INGREDIENTS) {
            if (input.contains(ingredient)) {
                found.add(ingredient);
            }
        }

        Pattern pattern = Pattern.compile("([\\u4e00-\\u9fa5]+)");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String match = matcher.group(1);
            if (match.length() >= 2 && !found.contains(match)) {
                found.add(match);
            }
        }

        return found;
    }

    private String extractFoodName(String input) {
        String[] keywords = {"热量", "营养", "卡路里", "蛋白质", "脂肪", "健康"};
        for (String keyword : keywords) {
            input = input.replace(keyword, "").trim();
        }
        return input.length() > 0 ? input : "未知菜品";
    }
}