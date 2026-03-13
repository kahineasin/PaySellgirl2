package com.sellgirl.sellgirlPayWeb.oAuth;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class DeviceFingerprintUtil {

    /**
     * 从请求中提取特征生成简易设备指纹
     * 特征包括：User-Agent, Accept, Accept-Language, Accept-Encoding, Connection, 屏幕参数（如果有），时区等
     */
    public static String generateFingerprint(HttpServletRequest request) {
        List<String> components = new ArrayList<>();

        // User-Agent
        components.add(request.getHeader("User-Agent"));

        // Accept
        components.add(request.getHeader("Accept"));

        // Accept-Language
        components.add(request.getHeader("Accept-Language"));

        // Accept-Encoding
        components.add(request.getHeader("Accept-Encoding"));

        // Connection
        components.add(request.getHeader("Connection"));

        // Upgrade-Insecure-Requests
        components.add(request.getHeader("Upgrade-Insecure-Requests"));

        // 如果前端通过JS传递了屏幕参数（可以通过Cookie或自定义头），可以加入
        // 这里不依赖前端，所以忽略屏幕参数

        // 时区（从 Accept-Language 或 Cookie 获取？这里简单用默认）
        // 也可以尝试从 Cookie 中获取时区（如果前端设置过）

        // 将非空特征用 | 连接
        String raw = StringUtils.collectionToDelimitedString(components, "|");
        // 计算MD5作为指纹
        return DigestUtils.md5Hex(raw);
    }
}