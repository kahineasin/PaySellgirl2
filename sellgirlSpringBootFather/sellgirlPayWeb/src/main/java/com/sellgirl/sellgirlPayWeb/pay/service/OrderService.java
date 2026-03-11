package com.sellgirl.sellgirlPayWeb.pay.service;


import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderService {

    // 模拟订单存储：key=商户订单号，value=支付状态（0-未支付，1-已支付）
    private final Map<String, Integer> orderMap = new ConcurrentHashMap<>();

    public String createOrder(String outTradeNo) {
        orderMap.put(outTradeNo, 0); // 初始状态未支付
        return outTradeNo;
    }

    public void markPaid(String outTradeNo) {
        orderMap.put(outTradeNo, 1);
    }

    public int getPayStatus(String outTradeNo) {
        return orderMap.getOrDefault(outTradeNo, -1);
    }
}