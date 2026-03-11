package com.sellgirl.sellgirlPayWeb.apiController;

import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.sellgirl.sgJavaHelper.SGRequestResult;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.AbstractApiResult;
import com.sellgirl.sgJavaHelper.SGHttpHelper;
import com.sellgirl.sgJavaHelper.WxJsCode2SessionResult;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pay")
public class Pay2Controller {

	/**
	 * 这方法不知道以前用来干嘛的？以前测试支付时测试了一半？
	 * @param code
	 * @return
	 */
    @GetMapping(value = "/sellgirlpay")
    public AbstractApiResult<?> SellgirlPay(String code){
		String wxAppId="";
    	String url="https://api.weixin.qq.com/sns/jscode2session?appid="+wxAppId+"&secret=e967c93628f514772a0976ee7b52e8fc&js_code=" + code+"&grant_type=authorization_code";
    	SGRequestResult r1=SGHttpHelper.HttpGet(url, null);
    	if(!r1.success) {return null;}
    	
    	//Class<AbstractApiResult<WxJsCode2SessionResult>> cl=(AbstractApiResult<WxJsCode2SessionResult>).getClass();
    	WxJsCode2SessionResult or1=JSON.parseObject(r1.content ,WxJsCode2SessionResult.class);
    	return or1;
//    	return AbstractApiResult.success(or1.getData().getOpenid());
    	
//    	String orderno="WxOrder_"+PFDataHelper.GetNewUniqueHashId();
//    	double money=0.01;
    }

    @GetMapping(value = "/checkapiparamtype")
    public AbstractApiResult<?> CheckApiParamType(
    		//@CalendarFormat(pattern = "yyyy-MM-dd")Calendar cal//, 报错:Failed to instantiate [java.util.Calendar]: Is it an abstract class?; nested exception is java.lang.InstantiationException
    		//@DateTimeFormat(pattern = "yyyy-MM-dd")Date date  //可以调用,但date是null值
    		//@DateTimeFormat(pattern = "yyyy-MM-dd")String date//可以调用,但date是null值
    		//@RequestParam@DateTimeFormat(pattern = "yyyy-MM-dd")String date//可以调用,有值
    		@RequestParam@DateTimeFormat(pattern = "yyyy-MM-dd")Date date//可以调用,有值
    		){
    	//System.out.println((new PFDate(cal)).toString());
    	//System.out.println((new PFDate(date)).toString());
    	System.out.println(date);
    	return AbstractApiResult.success();
//    	return AbstractApiResult.success(or1.getData().getOpenid());
    	
//    	String orderno="WxOrder_"+PFDataHelper.GetNewUniqueHashId();
//    	double money=0.01;
    }
    /**
     * testok 前端json:  ["aaa"]    //注意这样是不行的 {a:["aaa"]}
     * @param a
     * @return
     */
    @PostMapping(value = "/checkapibodyarray")
    public AbstractApiResult<?> CheckApiBodyArray(
    		@RequestBody String [] a
    		){
    	String json=JSON.toJSONString(a);
    	System.out.println(json);
    	return AbstractApiResult.success(json);
    }
    /**
     * testok 前端url:  https://localhost:44303/pay/checkapiarray?a=aaa%2Cbbb    // %2C为","的转码
     * @param a
     * @return
     */
    @PostMapping(value = "/checkapiarray")
    public AbstractApiResult<?> CheckApiArray(
    		@RequestParam String [] a
    		){
    	String json=JSON.toJSONString(a);
    	System.out.println(json);
    	return AbstractApiResult.success(json);
    }
    
}
