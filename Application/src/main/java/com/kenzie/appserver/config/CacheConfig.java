package com.kenzie.appserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.EnableCaching;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheStore userCache() {
        return new CacheStore(30, TimeUnit.MINUTES); // Modify the cache expiration time as needed
    }


    @Bean
    public CacheStore petCache() {
        return new CacheStore(30, TimeUnit.MINUTES); // Modify the cache expiration time as needed
    }
}
