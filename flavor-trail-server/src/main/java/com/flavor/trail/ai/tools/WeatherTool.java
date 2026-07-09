package com.flavor.trail.ai.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class WeatherTool {

    public Map<String, Object> getWeather(String city) {
        log.info("Getting weather for city: {}", city);

        return Map.of(
                "city", city,
                "temperature", 25,
                "weather", "晴",
                "humidity", 60,
                "suggestion", "今天天气不错，适合吃清爽的菜品"
        );
    }
}