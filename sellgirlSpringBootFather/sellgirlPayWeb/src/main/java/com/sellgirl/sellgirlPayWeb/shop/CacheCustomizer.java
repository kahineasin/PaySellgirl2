//package com.sellgirl.sellgirlPayWeb.shop;
//
//import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
//import org.springframework.cache.caffeine.CaffeineCacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import com.github.benmanes.caffeine.cache.Caffeine;
//import java.util.concurrent.TimeUnit;
//
//@Configuration
//public class CacheCustomizer {
//
//    @Bean
//    public CacheManagerCustomizer<CaffeineCacheManager> cacheManagerCustomizer() {
//        return cacheManager -> {
//            cacheManager.registerCustomCache("ipCache",
//                    Caffeine.newBuilder()
//                            .maximumSize(10000)
//                            .expireAfterWrite(5, TimeUnit.SECONDS)
//                            .build());
//            cacheManager.registerCustomCache("ipBlockCache",
//                    Caffeine.newBuilder()
//                            .maximumSize(10000)
//                            .expireAfterWrite(60, TimeUnit.MINUTES)
//                            .build());
//            cacheManager.registerCustomCache("deviceCache",
//                    Caffeine.newBuilder()
//                            .maximumSize(20000)
//                            .expireAfterWrite(1, TimeUnit.HOURS)
//                            .build());
//            cacheManager.registerCustomCache("deviceBlockCache",
//                    Caffeine.newBuilder()
//                            .maximumSize(20000)
//                            .expireAfterWrite(30, TimeUnit.MINUTES)
//                            .build());
//        };
//    }
//}