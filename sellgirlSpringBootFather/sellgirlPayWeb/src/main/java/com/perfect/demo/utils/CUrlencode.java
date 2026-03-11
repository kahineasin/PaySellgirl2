package com.perfect.demo.utils;


import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;

public class CUrlencode {
    public static String urlEncode(String url) throws Exception {
        String[] uppercase = new String[0XFF + 1];
        String[] lowercase = new String[0XFF + 1];
        for (int i = 0; i <= 0XFF; i++) {
            uppercase[i] = "%" + String.format("%02x", i);
            lowercase[i] = uppercase[i];
            uppercase[i] = uppercase[i].toUpperCase();
        }
        return StringUtils.replaceEach(URLEncoder.encode(url, "utf-8"), uppercase, lowercase);
    }
}
