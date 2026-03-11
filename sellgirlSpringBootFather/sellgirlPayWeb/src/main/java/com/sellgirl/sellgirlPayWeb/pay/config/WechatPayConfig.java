package com.sellgirl.sellgirlPayWeb.pay.config;

//package com.example.demo.config;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
//import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

//@Slf4j
@Configuration
//@Component
public class WechatPayConfig {

    @Value("${wechat.pay.app-id}")
    private String appId;

    @Value("${wechat.pay.mch-id}")
    private String mchId;

    @Value("${wechat.pay.api-v3-key}")
    private String apiV3Key;

    @Value("${wechat.pay.mch-serial-no}")
    private String mchSerialNo;

    @Value("${wechat.pay.private-key-path}")
    private Resource privateKeyResource;

    @Bean
    public Config wechatPayConfig2() throws Exception {
        // 读取私钥文件内容
        InputStreamReader reader = new InputStreamReader(privateKeyResource.getInputStream(), StandardCharsets.UTF_8);
        String privateKey = FileCopyUtils.copyToString(reader);

        // 构建自动更新平台证书的配置
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(mchId)
                .privateKey(privateKey)
                .merchantSerialNumber(mchSerialNo)
                .apiV3Key(apiV3Key)
                .build();
    }

    @Bean
    public NativePayService nativePayService(Config config) {
        return new NativePayService.Builder().config(config).build();
    }
}