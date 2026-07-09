package com.flavor.trail.ai.router;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IntentRouter {

    public IntentType classify(String userInput) {
        String input = userInput.toLowerCase().trim();

        if (input.contains("天气") || input.contains("温度") || input.contains("今天吃什么") ||
            input.contains("推荐") || input.contains("热") || input.contains("冷") ||
            input.contains("季节") || input.contains("夏天") || input.contains("冬天")) {
            log.info("Intent classified as WEATHER: {}", userInput);
            return IntentType.WEATHER;
        }

        if (input.contains("食材") || input.contains("冰箱") || input.contains("有") ||
            input.contains("什么") && (input.contains("做") || input.contains("菜")) ||
            input.contains("搭配") || input.contains("怎么做")) {
            log.info("Intent classified as INGREDIENT: {}", userInput);
            return IntentType.INGREDIENT;
        }

        if (input.contains("营养") || input.contains("热量") || input.contains("卡路里") ||
            input.contains("蛋白质") || input.contains("脂肪") || input.contains("健康")) {
            log.info("Intent classified as NUTRITION: {}", userInput);
            return IntentType.NUTRITION;
        }

        log.info("Intent classified as CHAT: {}", userInput);
        return IntentType.CHAT;
    }
}