package com.sellgirl.sellgirlPayWeb.shop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sellgirl.sellgirlPayWeb.oAuth.DeviceFingerprintUtil;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGCaching;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 防爬虫，IP 设备指纹检测
 *
 */
@Component
public class AntiSpiderInterceptor implements HandlerInterceptor {

//    @Autowired
//    private CacheManager cacheManager;

    @Value("${antispider.ip.window-seconds:5}")
    private Integer ipWindowSeconds;

//    @Value("${antispider.ip.windowSeconds}")
//    public Integer getIpWindowSeconds() {
//		return ipWindowSeconds;
//	}
//
//	public void setIpWindowSeconds(Integer ipWindowSeconds) {
//		this.ipWindowSeconds = ipWindowSeconds;
//	}

	@Value("${antispider.ip.max-count:10}")
    private int ipMaxCount;

    @Value("${antispider.ip.block-minutes:60}")
    private int ipBlockMinutes;

    @Value("${antispider.device.window-hours:1}")
    private int deviceWindowHours;

    @Value("${antispider.device.max-count:50}")
    private int deviceMaxCount;

    @Value("${antispider.device.block-minutes:30}")
    private int deviceBlockMinutes;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取IP
//    	Integer aa=ipWindowSeconds;

    	if(null!=handler&&handler instanceof HandlerMethod) {
    		HandlerMethod method=(HandlerMethod)handler;
    		if(ResourceInterceptor.isAllowAll(method)) {
    			return true;
    		}
    	}
        String ip = getClientIp(request);
        if(null!=ip) {
            System.out.println(SGDate.Now()+"-ip("+ip+")-----"+request.getRequestURI());
        }else {
        	System.out.println(SGDate.Now()+"-----"+request.getRequestURI());
        }
        

//        // 获取IP封禁缓存
//        Cache ipBlockCache = cacheManager.getCache("ipBlockCache");
//        if (ipBlockCache != null && ipBlockCache.get(ip) != null) {
//            return blockResponse(response, "您的IP已被临时封禁，请稍后再试", false);
//        }
        if(null!=SGCaching.Get("ipBlockCache_"+ip)) {
            return blockResponse(response, "您的IP已被临时封禁，请稍后再试", false);
        }

        // IP计数缓存
//        Cache ipCache = cacheManager.getCache("ipCache");
        Object ipCache=SGCaching.Get("ipCache_"+ip);
        if (ipCache != null) {
//            AtomicLong counter = ipCache.get(ip, AtomicLong::new);
            AtomicLong counter = (AtomicLong)ipCache;
            long count = counter.incrementAndGet();
            if (count == 1) {
                // 第一次访问，设置过期（缓存已通过配置自动过期）
            }
            if (count > ipMaxCount) {
                // 封禁IP
//                ipBlockCache.put(ip, true);
                SGCaching.Set("ipBlockCache_"+ip, true,ipBlockMinutes*60);
//                ipCache.evict(ip); // 移除计数
                SGCaching.Remove("ipCache_"+ip);;
                return blockResponse(response, "操作过于频繁，IP已被封禁" + ipBlockMinutes + "分钟", false);
            }
        }else {
            SGCaching.Set("ipCache_"+ip, new AtomicLong(1),ipWindowSeconds);
        }

        // 计算设备指纹（后端）
        String deviceFp = DeviceFingerprintUtil.generateFingerprint(request);

        // 设备封禁缓存
//        Cache deviceBlockCache = cacheManager.getCache("deviceBlockCache");
        Object deviceBlockCache=SGCaching.Get("deviceBlockCache_"+deviceFp);
        if (deviceBlockCache != null //&& deviceBlockCache.get(deviceFp) != null
        		) {
            return blockResponse(response, "操作频繁，请完成验证", true);
        }

        // 设备计数缓存
//        Cache deviceCache = cacheManager.getCache("deviceCache");
        Object deviceCache=SGCaching.Get("deviceCache_"+deviceFp);
        if (deviceCache != null) {
//            AtomicLong counter = deviceCache.get(deviceFp, AtomicLong::new);
            AtomicLong counter = (AtomicLong)deviceCache;
            long count = counter.incrementAndGet();
            if (count > deviceMaxCount) {
                // 设备封禁
//                deviceBlockCache.put(deviceFp, true);
                SGCaching.Set("deviceBlockCache_"+deviceFp, true,this.deviceBlockMinutes*60);
//                deviceCache.evict(deviceFp);
                SGCaching.Remove("deviceCache_"+deviceFp);;
                return blockResponse(response, "操作过于频繁，请完成验证", true);
            }
        }else {
            SGCaching.Set("deviceCache_"+deviceFp, new AtomicLong(1),deviceWindowHours*60*60);
        }

        return true;
    }

    private String getClientIp(HttpServletRequest request) {
        // 与之前相同
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    private boolean blockResponse(HttpServletResponse response, String message, boolean needCaptcha) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(429);
        PrintWriter out = response.getWriter();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 429);
        result.put("message", message);
        result.put("needCaptcha", needCaptcha);
        out.print(objectMapper.writeValueAsString(result));
        out.flush();
        return false;
    }
}