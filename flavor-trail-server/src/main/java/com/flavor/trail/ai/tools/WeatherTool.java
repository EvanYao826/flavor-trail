package com.flavor.trail.ai.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class WeatherTool {

    private final RestTemplate restTemplate;

    @Value("${weather.api.key:}")
    private String weatherApiKey;

    public WeatherTool() {
        this.restTemplate = new RestTemplate();
    }

    public Map<String, Object> getWeather(String city) {
        log.info("Getting weather for city: {}", city);

        if (weatherApiKey != null && !weatherApiKey.isEmpty()) {
            return fetchRealWeather(city);
        }

        return generateMockWeather(city);
    }

    private Map<String, Object> fetchRealWeather(String city) {
        try {
            String url = String.format("https://restapi.amap.com/v3/weather/weatherInfo?city=%s&key=%s",
                    encodeCity(city), weatherApiKey);

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && "1".equals(response.get("status"))) {
                Map<String, Object> result = new HashMap<>();
                result.put("city", city);

                @SuppressWarnings("unchecked")
                java.util.List<Map<String, Object>> lives = (java.util.List<Map<String, Object>>) response.get("lives");
                if (lives != null && !lives.isEmpty()) {
                    Map<String, Object> live = lives.get(0);
                    result.put("temperature", Integer.parseInt(live.get("temperature").toString()));
                    result.put("weather", live.get("weather").toString());
                    result.put("humidity", Integer.parseInt(live.get("humidity").toString()));
                    result.put("suggestion", live.get("advice").toString());
                }

                return result;
            }
        } catch (Exception e) {
            log.warn("Failed to fetch real weather for {}: {}", city, e.getMessage());
        }

        return generateMockWeather(city);
    }

    private Map<String, Object> generateMockWeather(String city) {
        String[] weatherConditions = {"晴", "多云", "阴", "小雨", "中雨", "大雨", "雪"};
        String condition = weatherConditions[(int) (Math.random() * weatherConditions.length)];
        int temp = 10 + (int) (Math.random() * 25);

        String suggestion;
        if (temp > 30) {
            suggestion = "天气炎热，注意防暑，适合吃清凉解暑的菜品";
        } else if (temp < 15) {
            suggestion = "天气较冷，注意保暖，适合吃暖身的菜品";
        } else if (condition.contains("雨")) {
            suggestion = "有雨天气，出门带伞，适合在家做些家常菜";
        } else {
            suggestion = "天气不错，适合外出品尝美食";
        }

        return Map.of(
                "city", city,
                "temperature", temp,
                "weather", condition,
                "humidity", 40 + (int) (Math.random() * 40),
                "suggestion", suggestion
        );
    }

    private String encodeCity(String city) {
        try {
            return java.net.URLEncoder.encode(city, "UTF-8");
        } catch (Exception e) {
            return city;
        }
    }
}