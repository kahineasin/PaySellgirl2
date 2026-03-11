package com.sellgirl.sellgirlPayWeb.pay.service;


import com.sellgirl.sellgirlPayWeb.pay.config.WechatPayConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
//import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

//@Slf4j
@Service
public class WechatPayNativeService {

    @Autowired
    private NativePayService nativePayService;

    @Value("${wechat.pay.app-id}")
    private String appId;

    @Value("${wechat.pay.mch-id}")
    private String mchId;

    @Value("${wechat.pay.notify-url}")
    private String notifyUrl;

    /**
     * 创建Native支付订单，返回二维码链接 code_url
     */
    public String createNativeOrder(String outTradeNo, Integer totalFee, String description) {
        PrepayRequest request = new PrepayRequest();
        request.setAppid(appId);
        request.setMchid(mchId);
        request.setDescription(description);
        request.setOutTradeNo(outTradeNo);
        request.setNotifyUrl(notifyUrl);

        Amount amount = new Amount();
        amount.setTotal(totalFee); // 单位：分
        amount.setCurrency("CNY");
        request.setAmount(amount);

        // 调用接口
        PrepayResponse response = nativePayService.prepay(request);
        String codeUrl = response.getCodeUrl();  // 二维码链接
        //log.info("创建Native订单成功，outTradeNo={}, codeUrl={}", outTradeNo, codeUrl);
        SGDataHelper.getLog().print(SGDataHelper.FormatString("创建Native订单成功，outTradeNo={}, codeUrl={}", outTradeNo, codeUrl));
        return codeUrl;
    }
}