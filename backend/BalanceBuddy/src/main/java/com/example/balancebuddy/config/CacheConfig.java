package com.example.balancebuddy.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Objects;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(){
        return new ConcurrentMapCacheManager("dailyUserProgress");
    }

    // Update user progress cache by clearing it at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void clearCache() {
        CacheManager cacheManager = cacheManager();
        if (cacheManager.getCache("dailyUserProgress") != null) {
            Objects.requireNonNull(cacheManager.getCache("dailyUserProgress")).clear();
        }
    }
}
