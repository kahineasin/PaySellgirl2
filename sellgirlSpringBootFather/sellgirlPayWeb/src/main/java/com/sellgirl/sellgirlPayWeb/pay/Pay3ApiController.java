package com.sellgirl.sellgirlPayWeb.pay;


import com.alibaba.fastjson.JSONObject;
import com.sellgirl.sellgirlPayWeb.pay.model.OrderStatus;
import com.sellgirl.sellgirlPayWeb.pay.model.vipOrderCreate;
import com.sellgirl.sellgirlPayWeb.pay.service.OrderService;
//import com.sellgirl.sellgirlPayWeb.pay.service.WechatPayNativeService;
import com.sellgirl.sellgirlPayWeb.pay.service.ZPayNativeService;
import com.sellgirl.sellgirlPayWeb.user.service.UserService;
import com.sellgirl.sellgirlPayWeb.user.YJQueryController;
import com.sellgirl.sellgirlPayWeb.user.model.PayPlan;
import com.sellgirl.sellgirlPayWeb.user.model.User;
import com.sellgirl.sgJavaHelper.SGAllowAnonymous;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGHttpHelper;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.SGRequestResult;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.config.SGDataHelper.LocalDataType;

//import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 测试zpay支付，
 * 对应static/demo/product3.html
 */
//@Slf4j
@RestController
@RequestMapping("/api/pay3")
@SGAllowAnonymous
public class Pay3ApiController extends YJQueryController {

    @Autowired
    private ZPayNativeService zPayNativeService;
	@Autowired private UserService userService;

    @Autowired
    private OrderService orderService;
//
//    long newId=0;
//    String productName = "";//商品名
//    String money = "";//价格
//    String pid = "2026031219321114";//商户id
////    String notifyUrl="www.bdhome.xyz/pay2/notify";
////    String returnUrl="www.bdhome.xyz/pay2/payPage";
////    String signStr2;
//    String signType = "MD5";//签名类型
//    String key = "uXXOQLIbgs3PasUjBgWp3vKkCmPzc8iN";//商户密钥
//    String payType = "alipay";//支付类型
//    @GetMapping("/payPage")
////    @GetMapping("/pay3")
//    public ResponseEntity<?> payPage(PayPlan plan,String amount) {
//    	//newId=getNewId();
//    	productName="product1";
//    	money="0.01";
//    	
//    	vipOrderCreate order=new vipOrderCreate();
//    	order.setAmount(new BigDecimal(amount));
//    	order.setVip_type(plan.ordinal());
//    	order.setStatus(OrderStatus.待支付.ordinal());
//    	order.setCreate_time(SGDate.Now());
//    	order.setUser_id(Long.valueOf(this.GetUserId()));
//        long orderId=orderService.insertvipOrder(order);
//        if(0>orderId) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .contentType(MediaType.TEXT_HTML)
//                    .body("<html>...新增订单失败...</html>");
//        }
//        
//        // 1. 构建请求参数，调用第三方接口
//        String thirdPartyUrl =zPayNativeService.createNativeOrder(orderId,plan,amount);
//        
//        if(true) {
////        	SGRequestResult r=SGHttpHelper.HttpPost(thirdPartyUrl, "");
//        	SGRequestResult r=SGHttpHelper.HttpGet(thirdPartyUrl, "");
//        	if(r.success) {
//        	System.out.println(thirdPartyUrl);
////            return ResponseEntity
////            		.status(HttpStatus.INTERNAL_SERVER_ERROR)
////                    .contentType(MediaType.TEXT_HTML)                    
////                    .body(r.content);
//    		return ResponseEntity.status(HttpStatus.FOUND)
//    		.location(URI.create(thirdPartyUrl))
//    		.build();
//        	}else {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .contentType(MediaType.TEXT_HTML)
//                        .body("<html>...新增订单2失败...</html>");
//        	}
//        }
////        SGDataHelper.HttpPost(prompt, responseBody);
////        ResponseEntity<String> response = restTemplate.getForEntity(thirdPartyUrl, String.class);
//
////        String url = thirdPartyUrl;
//        HttpHeaders pheaders = new HttpHeaders();
////        headers.setContentType(MediaType.APPLICATION_JSON);
//        pheaders.setContentType(MediaType.TEXT_HTML);
////        JSONObject jsonObject = new JSONObject();
////        jsonObject.put("idOrNumber", userno);
////        String data = jsonObject.toJSONString();
//        HttpEntity<String> httpEntity = new HttpEntity<>("", pheaders);
//        RestTemplate restTemplate = new RestTemplate();
////        restTemplate.getRequestFactory().createRequest(null, null)
//        
////        String param=SGDataHelper.FormatString(
////        		"name={0}&money={1}&out_trade_no={2}&notify_url={3}&pid={4}&param={5}&return_url={6}&sign={7}&sign_type={8}&type={9}",
////        		productName,
////        		"0.01",
////        		newId,
////        		notifyUrl,
////        		pid,
////        		"256G",
////        		returnUrl,
////        		signStr2,
////        		signType,
////        		"alipay"
////        		);
//        ResponseEntity<String> response = restTemplate.exchange(thirdPartyUrl, HttpMethod.POST, httpEntity, String.class);
////        ResponseEntity<String> response = restTemplate.getForEntity(thirdPartyUrl, String.class);
////        ResponseEntity<String> response = restTemplate.postForEntity(thirdPartyUrl,null, String.class);
////        ResponseEntity<String> response = restTemplate.getForEntity(thirdPartyUrl,null, String.class);
//
//        // 2. 判断响应内容类型
//        HttpHeaders headers = response.getHeaders();
//        String body = response.getBody();
//        MediaType contentType = headers.getContentType();
//
//        if (contentType != null && contentType.includes(MediaType.TEXT_HTML)) {
//            // 返回HTML页面给浏览器
//            return ResponseEntity.ok()
//                    .contentType(MediaType.TEXT_HTML)
//                    .body(body);
//        } else {
////            // 3. 非HTML响应（如JSON错误），记录错误，返回自定义错误页面
////            log.error("支付接口返回非HTML: {}", body);
//            // 可以返回一个错误视图，或者用ResponseEntity返回自定义JSON
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .contentType(MediaType.TEXT_HTML)
//                    .body("<html>...错误信息...</html>");
//        }
//    }
//    private long getNewId() {
//    	String s=SGDataHelper.ReadLocalTxt("newOrderId.txt", LocalDataType.System);
//    	long id=Long.valueOf(s);
//    	id++;
//    	SGDataHelper.WriteLocalTxt(String.valueOf(id),"newOrderId.txt", LocalDataType.System);
//    	return id;
//    }
//
////    /**
////     * 创建Native支付订单，返回二维码code_url
////     */
////    @PostMapping("/native")
////    public Map<String, Object> createNativeOrder(@RequestBody Map<String, String> params) {
////        String description = params.get("description");  // 商品描述
////        Integer totalFee = Integer.valueOf(params.get("totalFee")); // 金额（分）
////
////        // 生成商户订单号（简单示例，实际应该用更严谨的规则）
////        String outTradeNo = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 24);
////
////        // 创建本地订单记录
////        orderService.createOrder(outTradeNo);
////
////        // 调用微信支付Native下单
////        String codeUrl = wechatPayNativeService.createNativeOrder(outTradeNo, totalFee, description);
////
////        Map<String, Object> result = new HashMap<>();
////        result.put("success", true);
////        result.put("codeUrl", codeUrl);
////        result.put("outTradeNo", outTradeNo);
////        return result;
////    }

    /**
     * 微信支付结果回调
     */
//    @PostMapping("/notify")
////    @RequestMapping("/notify")
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
    @GetMapping("/notify")
//  @RequestMapping("/notify")
  public String payNotify(long pid,
		  String name,
		  String money,
		  String out_trade_no,
		  String trade_no,
		  String param,
		  String trade_status,
		  String type,
		  String sign,
		  String sign_type
		  ) {
    	
    	SGRef<String> r=new SGRef<String>();
    	if(!ZPayNativeService.isSignCorrect(pid, name, money, out_trade_no, trade_no, param, trade_status, type, sign, sign_type,r)) {
    		SGDataHelper.getLog().print("------------sign wrong-----------------");
    		SGDataHelper.getLog().print(pid);
    		SGDataHelper.getLog().print(name);
    		SGDataHelper.getLog().print(money);
    		SGDataHelper.getLog().print(out_trade_no);
    		SGDataHelper.getLog().print(trade_no);
    		SGDataHelper.getLog().print(param);
    		SGDataHelper.getLog().print(trade_status);
    		SGDataHelper.getLog().print(type);
    		SGDataHelper.getLog().print(sign);
    		SGDataHelper.getLog().print(sign_type);
    		SGDataHelper.getLog().print("should be: "+r.GetValue());
    	    return "success";
    	}
    	
    	if(null!=trade_status&&"TRADE_SUCCESS".equals(trade_status))
    	{
    		long orderId=Long.valueOf(out_trade_no);
    		this.orderService.updateOrderPaid(orderId,trade_no);
    		PayPlan vip=orderService.GetvipOrderVipTypeById(orderId);
    		User user=this.userService.getUser(this.GetUserName());
			SGDate now=SGDate.Now();
    		if(PayPlan.resource_monthly==vip) {
    			user.setVip1(true);
    			if(null==user.getVip1_expire()||0<now.compareTo(user.getVip1_expire())) {
    				user.setVip1_expire(now.AddMonths(1));
    			}else {
    				user.setVip1_expire(user.getVip1_expire().AddMonths(1));
    			}
    		}else if(PayPlan.resource_yearly==vip) {
    			user.setVip1(true);
    			if(null==user.getVip1_expire()||0<now.compareTo(user.getVip1_expire())) {
    				user.setVip1_expire(now.AddYears(1).AddMonths(3));
    			}else {
    				user.setVip1_expire(user.getVip1_expire().AddYears(1).AddMonths(3));
    			}
    		}else if(PayPlan.ebook_monthly==vip) {
    			user.setVip2(true);
    			if(null==user.getVip2_expire()||0<now.compareTo(user.getVip2_expire())) {
    				user.setVip2_expire(now.AddMonths(1));
    			}else {
    				user.setVip2_expire(user.getVip2_expire().AddMonths(1));
    			}
    		}else if(PayPlan.ebook_yearly==vip) {
    			user.setVip2(true);
    			if(null==user.getVip2_expire()||0<now.compareTo(user.getVip2_expire())) {
    				user.setVip2_expire(now.AddYears(1).AddMonths(3));
    			}else {
    				user.setVip2_expire(user.getVip2_expire().AddYears(1).AddMonths(3));
    			}
    		}
    		this.userService.updateUserVip(user.getUserId(), user.isVip1(), user.getVip1_expire(), user.isVip2(), user.getVip2_expire());
    	      return "success";
    	}
//      // 实际开发中需要验证签名、解密资源、更新订单状态
//      // 这里简化处理：打印回调数据，并假设支付成功
////      log.info("收到支付回调：{}", notifyData);
//  	SGDataHelper.getLog().print(
//		SGDataHelper.FormatString("收到支付回调：{0}",notifyData)
//  	);

      // TODO 解析回调，获取 out_trade_no，验证并更新订单状态
      // 示例：orderService.markPaid(outTradeNo);

      // 必须返回SUCCESS，否则微信会持续通知
      return "success";
  }

    /**
     * 查询订单支付状态（供前端轮询）
     */
    @GetMapping("/status/{outTradeNo}")
    @SGAllowAnonymous
    public Map<String, Object> getPayStatus(@PathVariable String outTradeNo) {
        OrderStatus status = orderService.GetvipOrderStatusById(Long.valueOf( outTradeNo));
        Map<String, Object> result = new HashMap<>();
        result.put("outTradeNo", outTradeNo);
        result.put("status", status.ordinal()); // 0-未支付，1-已支付，-1-不存在
        return result;
    }
}