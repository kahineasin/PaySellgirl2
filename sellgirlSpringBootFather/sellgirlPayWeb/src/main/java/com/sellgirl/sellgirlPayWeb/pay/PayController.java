//package com.sellgirl.sellgirlPayWeb.pay;
//
//
//import com.sellgirl.sellgirlPayWeb.pay.service.OrderService;
//import com.sellgirl.sellgirlPayWeb.pay.service.WechatPayNativeService;
//import com.sellgirl.sgJavaHelper.config.SGDataHelper;
//
////import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
///**
// * 未申请到微信商户，暂时测试不了
// * 测试微信支付，
// * 对应static/demo/product.html
// */
////@Slf4j
//@RestController
//@RequestMapping("/api/pay")
//public class PayController {
//
//    @Autowired
//    private WechatPayNativeService wechatPayNativeService;
//
//    @Autowired
//    private OrderService orderService;
//
//    /**
//     * 创建Native支付订单，返回二维码code_url
//     */
//    @PostMapping("/native")
//    public Map<String, Object> createNativeOrder(@RequestBody Map<String, String> params) {
//        String description = params.get("description");  // 商品描述
//        Integer totalFee = Integer.valueOf(params.get("totalFee")); // 金额（分）
//
//        // 生成商户订单号（简单示例，实际应该用更严谨的规则）
//        String outTradeNo = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 24);
//
//        // 创建本地订单记录
//        orderService.createOrder(outTradeNo);
//
//        // 调用微信支付Native下单
//        String codeUrl = wechatPayNativeService.createNativeOrder(outTradeNo, totalFee, description);
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("success", true);
//        result.put("codeUrl", codeUrl);
//        result.put("outTradeNo", outTradeNo);
//        return result;
//    }
//
//    /**
//     * 微信支付结果回调
//     */
//    @PostMapping("/notify")
//    public String payNotify(@RequestBody String notifyData) {
//        // 实际开发中需要验证签名、解密资源、更新订单状态
//        // 这里简化处理：打印回调数据，并假设支付成功
////        log.info("收到支付回调：{}", notifyData);
//    	SGDataHelper.getLog().print(
//		SGDataHelper.FormatString("收到支付回调：{0}",notifyData)
//    	);
//
//        // TODO 解析回调，获取 out_trade_no，验证并更新订单状态
//        // 示例：orderService.markPaid(outTradeNo);
//
//        // 必须返回SUCCESS，否则微信会持续通知
//        return "SUCCESS";
//    }
//
//    /**
//     * 查询订单支付状态（供前端轮询）
//     */
//    @GetMapping("/status/{outTradeNo}")
//    public Map<String, Object> getPayStatus(@PathVariable String outTradeNo) {
//        int status = orderService.getPayStatus(outTradeNo);
//        Map<String, Object> result = new HashMap<>();
//        result.put("outTradeNo", outTradeNo);
//        result.put("status", status); // 0-未支付，1-已支付，-1-不存在
//        return result;
//    }
//}