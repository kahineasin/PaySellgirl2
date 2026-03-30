package com.sellgirl.sellgirlPayWeb.pay;


import com.alibaba.fastjson.JSONObject;
import com.sellgirl.sellgirlPayWeb.pay.model.OrderStatus;
import com.sellgirl.sellgirlPayWeb.pay.model.vipOrder;
import com.sellgirl.sellgirlPayWeb.pay.model.vipOrderCreate;
import com.sellgirl.sellgirlPayWeb.pay.service.OrderService;
//import com.sellgirl.sellgirlPayWeb.pay.service.WechatPayNativeService;
import com.sellgirl.sellgirlPayWeb.pay.service.ZPayNativeService;
import com.sellgirl.sellgirlPayWeb.user.service.UserService;
import com.sellgirl.sellgirlPayWeb.user.YJQueryController;
import com.sellgirl.sellgirlPayWeb.user.model.PayPlan;
import com.sellgirl.sellgirlPayWeb.user.model.User;
import com.sellgirl.sellgirlPayWeb.user.model.UserQuery;
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

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 总体过程:
 * 1. /pay3payPage 使用户跳转到zpay支付页面, 用户扫码
 * 2. zpay服务器回调我的/api/pay3/notify  (注意此接口不要试图使用用户的cookie,因为没有)
 * 3. zpay使用户跳转回我的/pay3paid.html, 页面间歇查询订单状态, 订单完成就跳转到profile.html
 * 4. profile.html 控制器内查询point并更新用户cache
 * 
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

    /**
     * 微信支付结果回调
     */
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
//    		this.orderService.updateOrderPaid(orderId,trade_no);
//    		PayPlan vip=orderService.GetvipOrderVipTypeById(orderId);
    		vipOrder vipOrder=orderService.GetOnevipOrder(orderId);
    		PayPlan vip=vipOrder.getVip_type();
//    		User user=this.userService.getUser(this.GetUserName());
    		UserQuery q=new UserQuery();
    		q.setUserId(vipOrder.getUser_id());
    		User user=this.userService.getUser(q);

		    if(PayPlan.point5==vip||PayPlan.point15==vip||PayPlan.point50==vip) {
		    	int point=5;
		        switch(vip) {
		        case point5:
		      		point=5;
		      		break;
		        case point15:
		      		point=15;
		      		break;
		        case point50:
		      		point=50;
		      		break;
		      	  default:
		      		point=5;
		      		break;
		        }
		    	userService.addUserPoint(vipOrder.getUser_id(),point);

//		    	//这里的cookie是zpay服务器,所以在这处理没有用
//	    		SystemUser sysUser=this.GetSystemUser();
//	    		sysUser.point+=point;
//	    		this.SetSystemUser(sysUser);
		    }else {
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
	    		this.userService.updateUserVip(vipOrder.getUser_id(), user.isVip1(), user.getVip1_expire(), user.isVip2(), user.getVip2_expire());
//		    	//这里的cookie是zpay服务器,所以在这处理没有用
//	    		SystemUser sysUser=this.GetSystemUser();
//	    		sysUser.isVip=true;
//	    		this.SetSystemUser(sysUser);

		    }  
		  //paid页面根据订单状态跳转到profile页并根据cache更新前端store
		    //所以这里要先更新完cache在变更订单状态
    		this.orderService.updateOrderPaid(orderId,trade_no);
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