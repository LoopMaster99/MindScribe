package com.developmentprep.journalApp.service;

import com.developmentprep.journalApp.api.response.WeatherResponse;
import com.developmentprep.journalApp.cache.AppCache;
import com.developmentprep.journalApp.constants.Placeholders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {
    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final AppCache appCache;
    private final RedisService redisService;

    public WeatherResponse getWeather(String city) {
        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
        if (weatherResponse != null) {
            return weatherResponse;
        }

        try {
            String finalAPI = appCache.appCache.get(AppCache.keys.WEATHER_API.toString())
                    .replace(Placeholders.CITY, city)
                    .replace(Placeholders.API_KEY, apiKey);

            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null,
                    WeatherResponse.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                WeatherResponse body = response.getBody();
                redisService.set("weather_of_" + city, body, 300l);
                return body;
            }

            log.warn("Weather API returned non-successful status: {}", response.getStatusCode());
            return null;
        } catch (Exception e) {
            log.error("Failed to fetch weather for city: {}", city, e);
            return null;
        }
    }
}
