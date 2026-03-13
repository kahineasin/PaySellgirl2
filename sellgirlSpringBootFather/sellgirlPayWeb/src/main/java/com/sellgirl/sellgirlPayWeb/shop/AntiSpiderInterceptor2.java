//package com.sellgirl.sellgirlPayWeb.shop;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sellgirl.sellgirlPayWeb.oAuth.DeviceFingerprintUtil;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.PrintWriter;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.atomic.AtomicLong;
//
///**
// * 防爬虫，IP 设备指纹检测
// * 此版本是用spring的cache类，需要引用
//<!-- Caffeine 缓存 -->
//<dependency>
//    <groupId>org.springframework.boot</groupId>
//    <artifactId>spring-boot-starter-cache</artifactId>
//</dependency>
//<dependency>
//    <groupId>com.github.ben-manes.caffeine</groupId>
//    <artifactId>caffeine</artifactId>
//</dependency>
// */
//@Component
//public class AntiSpiderInterceptor2 implements HandlerInterceptor {
//
//    @Autowired
//    private CacheManager cacheManager;
//
//    @Value("${antispider.ip.window-seconds:5}")
//    private int ipWindowSeconds;
//
//    @Value("${antispider.ip.max-count:10}")
//    private int ipMaxCount;
//
//    @Value("${antispider.ip.block-minutes:60}")
//    private int ipBlockMinutes;
//
//    @Value("${antispider.device.window-hours:1}")
//    private int deviceWindowHours;
//
//    @Value("${antispider.device.max-count:50}")
//    private int deviceMaxCount;
//
//    @Value("${antispider.device.block-minutes:30}")
//    private int deviceBlockMinutes;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        // 获取IP
//        String ip = getClientIp(request);
//
//        // 获取IP封禁缓存
//        Cache ipBlockCache = cacheManager.getCache("ipBlockCache");
//        if (ipBlockCache != null && ipBlockCache.get(ip) != null) {
//            return blockResponse(response, "您的IP已被临时封禁，请稍后再试", false);
//        }
//
//        // IP计数缓存
//        Cache ipCache = cacheManager.getCache("ipCache");
//        if (ipCache != null) {
//            AtomicLong counter = ipCache.get(ip, AtomicLong::new);
//            long count = counter.incrementAndGet();
//            if (count == 1) {
//                // 第一次访问，设置过期（缓存已通过配置自动过期）
//            }
//            if (count > ipMaxCount) {
//                // 封禁IP
//                ipBlockCache.put(ip, true);
//                ipCache.evict(ip); // 移除计数
//                return blockResponse(response, "操作过于频繁，IP已被封禁" + ipBlockMinutes + "分钟", false);
//            }
//        }
//
//        // 计算设备指纹（后端）
//        String deviceFp = DeviceFingerprintUtil.generateFingerprint(request);
//
//        // 设备封禁缓存
//        Cache deviceBlockCache = cacheManager.getCache("deviceBlockCache");
//        if (deviceBlockCache != null && deviceBlockCache.get(deviceFp) != null) {
//            return blockResponse(response, "操作频繁，请完成验证", true);
//        }
//
//        // 设备计数缓存
//        Cache deviceCache = cacheManager.getCache("deviceCache");
//        if (deviceCache != null) {
//            AtomicLong counter = deviceCache.get(deviceFp, AtomicLong::new);
//            long count = counter.incrementAndGet();
//            if (count > deviceMaxCount) {
//                // 设备封禁
//                deviceBlockCache.put(deviceFp, true);
//                deviceCache.evict(deviceFp);
//                return blockResponse(response, "操作过于频繁，请完成验证", true);
//            }
//        }
//
//        return true;
//    }
//
//    private String getClientIp(HttpServletRequest request) {
//        // 与之前相同
//        String ip = request.getHeader("X-Forwarded-For");
//        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//        }
//        if (ip != null && ip.contains(",")) {
//            ip = ip.split(",")[0].trim();
//        }
//        return ip;
//    }
//
//    private boolean blockResponse(HttpServletResponse response, String message, boolean needCaptcha) throws Exception {
//        response.setContentType("application/json;charset=UTF-8");
//        response.setStatus(429);
//        PrintWriter out = response.getWriter();
//        Map<String, Object> result = new HashMap<>();
//        result.put("code", 429);
//        result.put("message", message);
//        result.put("needCaptcha", needCaptcha);
//        out.print(objectMapper.writeValueAsString(result));
//        out.flush();
//        return false;
//    }
//}