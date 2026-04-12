package com.sellgirl.sellgirlPayWeb.pay;


import com.alibaba.fastjson.JSONObject;
import com.sellgirl.sellgirlPayWeb.pay.model.OrderStatus;
import com.sellgirl.sellgirlPayWeb.pay.model.vipOrderCreate;
import com.sellgirl.sellgirlPayWeb.pay.service.OrderService;
//import com.sellgirl.sellgirlPayWeb.pay.service.WechatPayNativeService;
import com.sellgirl.sellgirlPayWeb.pay.service.ZPayNativeService;
import com.sellgirl.sellgirlPayWeb.user.YJQueryController;
import com.sellgirl.sellgirlPayService.pay.model.PayPlan;
import com.sellgirl.sgJavaHelper.SGAllowAnonymous;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGHttpHelper;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.SGRequestResult;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.config.SGDataHelper.LocalDataType;
import com.sellgirl.sgJavaHelper.model.SystemUser;

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
import org.springframework.web.servlet.ModelAndView;

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
@Controller
//@RequestMapping("/pay3")
public class Pay3Controller extends YJQueryController {

	private String TAG="Pay3Controller";
    @Autowired
    private ZPayNativeService zPayNativeService;

    @Autowired
    private OrderService orderService;

    long newId=0;
//    String productName = "";//商品名
//    String money = "";//价格
    String pid = "2026031219321114";//商户id
//    String notifyUrl="www.bdhome.xyz/pay2/notify";
//    String returnUrl="www.bdhome.xyz/pay2/payPage";
//    String signStr2;
    String signType = "MD5";//签名类型
    String key = "uXXOQLIbgs3PasUjBgWp3vKkCmPzc8iN";//商户密钥
    String payType = "alipay";//支付类型
    private boolean isTest=true;

    
    /**
     * //amount应该固定,不应该前端传过来-- TODO 
     * @param plan
     * @param amount
     * @return
     */
    @GetMapping("/pay3payPage")
//  @GetMapping("/pay3")
  public ModelAndView payPage(PayPlan plan//,String amount
		  ) {
  	//newId=getNewId();
//  	productName=plan.name();
  	String amount=null;
  	switch(plan) {
  	case point5:
  		amount="5";
  		break;
  	case point15:
  		amount="10";
  		break;
  	case point50:
  		amount="30";
  		break;
  	case resource_monthly:
  		amount="30";
  		break;
  	case resource_yearly:
  		amount="300";
  		break;
  	case ebook_monthly:
  		amount="15";
  		break;
  	case ebook_yearly:
  		amount="150";
  		break;
  	default:
  		amount="30";
  		break;
  	}
  	vipOrderCreate order=new vipOrderCreate();
  	order.setAmount(new BigDecimal(amount));
  	order.setVip_type(plan.ordinal());
  	order.setStatus(OrderStatus.待支付.ordinal());
  	order.setCreate_time(SGDate.Now());
  	order.setUser_id(Long.valueOf(this.GetUserId()));
      long orderId=orderService.insertvipOrder(order);
      boolean success=0<=orderId;
      String err=null;
      if(!success) {
    	  err="新增订单失败,请重新支付";
//    	  ViewData.put("generatePayFailed",true);
//    	  ViewData.put("generatePayMsg","新增订单失败,请重新支付");
//    	  return this.View("Product/vip");
      }else {

          if(isTest) {
//          	int pid=1,
    		  String name=plan.name();
    		  String money=amount;
    		  String out_trade_no=String.valueOf(orderId);
//    		  String trade_no,
    		  String param="aa";
//    		  String trade_status,
//    		  String type="",
//    		  String sign,
//    		  String sign_type
          	String notify=this.zPayNativeService.getZPayNotifyUrlDemo(
          			//pid, 
          			name, money, out_trade_no, 
          			//trade_no,
          			param//, 
          			//trade_status, 
//          			type
          			);
          	System.out.println("------------notify demo-----------");
          	System.out.println(notify);
          }
          // 1. 构建请求参数，调用第三方接口
          String thirdPartyUrl =zPayNativeService.createNativeOrder(orderId,plan,amount);

          if(isTest) {
        	System.out.println("------------thirdPartyUrl-----------");
        	System.out.println(thirdPartyUrl);
          }
          	SGRequestResult r=SGHttpHelper.HttpGet(thirdPartyUrl, "");
          	if(r.success) {
      		
      			return this.RedirectToUrl(thirdPartyUrl);
          	}else {
          		success=false;
          		err="新增订单2失败,请重新支付";
          	}
  
      }
      switch(plan) {
      case point5:
      case point15:
      case point50:
    	  ViewData.put("generatePayFailed",!success);
    	  ViewData.put("generatePayMsg",err);
    	  return this.View("Product/pay");
      case resource_monthly:
      case resource_yearly:
      case ebook_monthly:
      case ebook_yearly:
    	  default:
        	  ViewData.put("generatePayFailed",!success);
        	  ViewData.put("generatePayMsg",err);
        	  return this.View("Product/vip");
      }
      
  }
    private long getNewId() {
    	String s=SGDataHelper.ReadLocalTxt("newOrderId.txt", LocalDataType.System);
    	long id=Long.valueOf(s);
    	id++;
    	SGDataHelper.WriteLocalTxt(String.valueOf(id),"newOrderId.txt", LocalDataType.System);
    	return id;
    }

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
//    @GetMapping("/notify")
////  @RequestMapping("/notify")
//  public String payNotify(int pid,
//		  String name,
//		  String money,
//		  String out_trade_no,
//		  String trade_no,
//		  String param,
//		  String trade_status,
//		  String type,
//		  String sign,
//		  String sign_type
//		  ) {
//    	
//    	SGRef<String> r=new SGRef<String>();
//    	if(!ZPayNativeService.isSignCorrect(pid, name, money, out_trade_no, trade_no, param, trade_status, type, sign, sign_type,r)) {
//    		SGDataHelper.getLog().print("------------sign wrong-----------------");
//    		SGDataHelper.getLog().print(pid);
//    		SGDataHelper.getLog().print(name);
//    		SGDataHelper.getLog().print(money);
//    		SGDataHelper.getLog().print(out_trade_no);
//    		SGDataHelper.getLog().print(trade_no);
//    		SGDataHelper.getLog().print(param);
//    		SGDataHelper.getLog().print(trade_status);
//    		SGDataHelper.getLog().print(type);
//    		SGDataHelper.getLog().print(sign);
//    		SGDataHelper.getLog().print(sign_type);
//    	    return "success";
//    	}
//    	
//    	if(null!=trade_status&&"TRADE_SUCCESS".equals(trade_status))
//    	{
//    		this.orderService.updateOrderPaid(Long.valueOf(out_trade_no),trade_no);
//    	      return "success";
//    	}
////      // 实际开发中需要验证签名、解密资源、更新订单状态
////      // 这里简化处理：打印回调数据，并假设支付成功
//////      log.info("收到支付回调：{}", notifyData);
////  	SGDataHelper.getLog().print(
////		SGDataHelper.FormatString("收到支付回调：{0}",notifyData)
////  	);
//
//      // TODO 解析回调，获取 out_trade_no，验证并更新订单状态
//      // 示例：orderService.markPaid(outTradeNo);
//
//      // 必须返回SUCCESS，否则微信会持续通知
//      return "success";
//  }
//
//    /**
//     * 查询订单支付状态（供前端轮询）
//     */
//    @GetMapping("/status/{outTradeNo}")
//    public Map<String, Object> getPayStatus(@PathVariable String outTradeNo) {
//        OrderStatus status = orderService.GetvipOrderStatusById(Long.valueOf( outTradeNo));
//        Map<String, Object> result = new HashMap<>();
//        result.put("outTradeNo", outTradeNo);
//        result.put("status", status); // 0-未支付，1-已支付，-1-不存在
//        return result;
//    }

    /**
     * 三方支付回调的页面
     * @param pid
     * @param name
     * @param money
     * @param out_trade_no
     * @param trade_no
     * @param param
     * @param trade_status
     * @param type
     * @param sign
     * @param sign_type
     * @return
     */
	@RequestMapping(value = { "/pay3paid.html" })//2级路径导致样式错
	@SGAllowAnonymous
    public ModelAndView PaidReturn(long pid,
  		  String name,
  		  String money,
  		  String out_trade_no,
  		  String trade_no,
  		  String param,
  		  String trade_status,
  		  String type,
  		  String sign,
  		  String sign_type//,PayPlan plan,Integer amount
  		  )
    {
		try {
		boolean b=false;
		SGRef<String> r=new SGRef();
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
//    	    return "success";
    		b=false;
    	}else {
    		b=true;
    	}
//  	  return View(new LoginerBase(),"Product/register");
  	ModelAndView result=new ModelAndView();
//  	result.addObject("username","cuowu");
  	result.addObject("paidSuccess",b);
  	result.addObject("outTradeNo",Long.valueOf(out_trade_no));
  	result.addObject("amount",money);
//  	result.addObject("Html", new HtmlHelperT<TModel>(ViewData));
  	result.setViewName("Product/paid");

	long orderId=Long.valueOf(out_trade_no);
	PayPlan plan=orderService.GetvipOrderVipTypeById(orderId);
    switch(plan) {
    case point5:
    case point15:
    case point50:
    	result.addObject("successMsg","成功充值"+plan.name().substring(5)+"积分");
      	result.addObject("returnUrl","/pay.html");
  	   break;
    case resource_monthly:
    case resource_yearly:
    case ebook_monthly:
    case ebook_yearly:
  	  default:
      	result.addObject("successMsg","支付成功！您已成为VIP会员。");
      	result.addObject("returnUrl","/vip.html");
      	break;
    }
//    //payNotify中更新了cache
//    SystemUser user=this.GetSystemUser();
//  	result.addObject("isVip",user.isVip);
//  	result.addObject("point",user.point);
  	return result;
		}catch(Throwable e) {
			SGDataHelper.getLog().writeException(e,TAG);
		}
		return null;
    }
}