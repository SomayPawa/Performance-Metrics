package com.somay.url_shortener.scheduler;

import com.somay.url_shortener.Repository.UrlRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ClickCountScheduler {

    private final StringRedisTemplate redisTemplate;
    private final UrlRepository urlRepository;

    public ClickCountScheduler(
            StringRedisTemplate redisTemplate,
            UrlRepository urlRepository
    ) {
        this.redisTemplate = redisTemplate;
        this.urlRepository = urlRepository;
    }

    @Scheduled(fixedRate = 300000) // 5 minutes
    public void syncClickCounts() {

        System.out.println("Syncing Redis counters to MySQL...");

        Set<String> keys = redisTemplate.keys("click:*");

        if (keys == null || keys.isEmpty()) {
            return;
        }

        for (String key : keys) {

            String shortCode =
                    key.replace("click:", "");

            String value =
                    redisTemplate.opsForValue().get(key);

            if (value == null) {
                continue;
            }

            Long count = Long.parseLong(value);

            urlRepository.addClickCount(
                    shortCode,
                    count
            );

            redisTemplate.delete(key);
        }
    }
}