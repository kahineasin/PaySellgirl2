package com.sellgirl.sellgirlPayWeb.pay.service;


import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.sellgirl.sellgirlPayWeb.user.model.PayPlan;
import com.sellgirl.sgJavaHelper.SGHttpHelper;
//import com.sellgirl.sellgirlPayWeb.user.UserController.PayPlan;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.SGRequestResult;
//import com.sellgirl.sellgirlPayWeb.pay.config.WechatPayConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

//import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

//https://member.z-pay.cn/member/doc.html
//@Slf4j
@Service
public class ZPayNativeService {

//    @Autowired
//    private NativePayService nativePayService;
//
//    @Value("${wechat.pay.app-id}")
//    private String appId;
//
//    @Value("${wechat.pay.mch-id}")
//    private String mchId;
//
//    @Value("${wechat.pay.notify-url}")
//    private String notifyUrl;

//    /**
//     * 创建Native支付订单，返回二维码链接 code_url
//     */
//    public String createNativeOrder(String outTradeNo, Integer totalFee, String description) {
//        PrepayRequest request = new PrepayRequest();
//        request.setAppid(appId);
//        request.setMchid(mchId);
//        request.setDescription(description);
//        request.setOutTradeNo(outTradeNo);
//        request.setNotifyUrl(notifyUrl);
//
//        Amount amount = new Amount();
//        amount.setTotal(totalFee); // 单位：分
//        amount.setCurrency("CNY");
//        request.setAmount(amount);
//
//        // 调用接口
//        PrepayResponse response = nativePayService.prepay(request);
//        String codeUrl = response.getCodeUrl();  // 二维码链接
//        //log.info("创建Native订单成功，outTradeNo={}, codeUrl={}", outTradeNo, codeUrl);
//        SGDataHelper.getLog().print(SGDataHelper.FormatString("创建Native订单成功，outTradeNo={}, codeUrl={}", outTradeNo, codeUrl));
//        return codeUrl;
//    }
//    long newId=0;
    private String submitApi="https://z-pay.cn/submit.php";
    private String productName = "";//商品名
    private String money = "";//价格
    private long pid =2026031219321114L;// "2026031219321114";//商户id
    @Value("${zpay.notifyUrl}")
    private String notifyUrl="";
    @Value("${zpay.returnUrl}")
    private String returnUrl="";
	//    String signStr2;
    private String signType = "MD5";//签名类型
    private static String key = "uXXOQLIbgs3PasUjBgWp3vKkCmPzc8iN";//商户密钥
    private static String payType = "alipay";//支付类型
    public String createNativeOrder(long orderId,PayPlan plan,String amount) {

//        String url = "https://z-pay.cn";//支付地址
//        String pid = "2026031219321114";//商户id
//        String outTradeNo =String.valueOf( newId);//商户单号
        String outTradeNo =String.valueOf( orderId);//商户单号
//        String notifyUrl = "";//异步通知
//        String returnUrl = "";//跳转地址
//        String name = "";//商品名
//        String money = "";//价格
//        String signType = "MD5";//签名类型
//        String key = "uXXOQLIbgs3PasUjBgWp3vKkCmPzc8iN";//商户密钥
 
        //参数存入 map
        Map<String,String> sign = new HashMap<>();
        sign.put("pid",String.valueOf(pid));
        sign.put("type",payType);
        sign.put("out_trade_no",outTradeNo);
        sign.put("notify_url",notifyUrl);
        sign.put("return_url",returnUrl);
//        sign.put("name",productName);
//        sign.put("name",SGDataHelper.getURLEncoderString( plan.toString()));
        sign.put("name",plan.toString());
//        sign.put("money",money);
//        sign.put("money",String.valueOf(amount));
        sign.put("money",amount);
 
        //根据key升序排序
        sign = sortByKey(sign);
 
        String signStr = "";
 
        //遍历map 转成字符串
        for(Map.Entry<String,String> m :sign.entrySet()){
            signStr += m.getKey() + "=" +m.getValue()+"&";
        }
 
        //去掉最后一个 &
        signStr = signStr.substring(0,signStr.length()-1);
 
        //最后拼接上KEY
        signStr += key;
 
        //转为MD5
        signStr = DigestUtils.md5DigestAsHex(signStr.getBytes());
 
        sign.put("sign_type",signType);
        sign.put("sign",signStr);
//        signStr2=signStr;

        StringBuilder sb=new StringBuilder();
        for(Map.Entry<String,String> m :sign.entrySet()){
//            signStr += m.getKey() + "=" +m.getValue()+"&";
            sb.append(m.getKey() + "=" +m.getValue()+"&");
        }
        String r=sb.toString();
        r = r.substring(0,r.length()-1);
        return submitApi+"?"+r;
//        System.out.println("<form id='paying' action='"+url+"/submit.php' method='post'>");
//        for(Map.Entry<String,String> m :sign.entrySet()){
//            System.out.println("<input type='hidden' name='"+m.getKey()+"' value='"+m.getValue()+"'/>");
//        }
//        System.out.println("<input type='submit' value='正在跳转'>");
//        System.out.println("</form>");
//        System.out.println("<script>document.forms['paying'].submit();</script>");
    }
    public static <K extends Comparable<? super K>, V > Map<K, V> sortByKey(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
 
        map.entrySet().stream()
                .sorted(Map.Entry.<K, V>comparingByKey()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }
    private static String getSign(long pid,
    		  String name,
      		  String money,
      		  String out_trade_no,
      		  String trade_no,
      		  String param,
      		  String trade_status,
      		  String type) {

        //参数存入 map
        Map<String,String> signMap = new HashMap<>();
        signMap.put("pid",String.valueOf(pid));
        signMap.put("name",name);
        signMap.put("money",money);
        signMap.put("out_trade_no",out_trade_no);
        signMap.put("trade_no",trade_no);
        if(null!=param) {
        signMap.put("param",param);
        }
        signMap.put("trade_status",trade_status);
        signMap.put("type",type);
//        signMap.put("notify_url",notifyUrl);
//        signMap.put("return_url",returnUrl);
 
        //根据key升序排序
        signMap = sortByKey(signMap);
 
        String signStr = "";
 
        //遍历map 转成字符串
        for(Map.Entry<String,String> m :signMap.entrySet()){
            signStr += m.getKey() + "=" +m.getValue()+"&";
        }
 
        //去掉最后一个 &
        signStr = signStr.substring(0,signStr.length()-1);
 
        //最后拼接上KEY
        signStr += key;
 
        //转为MD5
        signStr = DigestUtils.md5DigestAsHex(signStr.getBytes());
    	return signStr;
    }
    public static boolean isSignCorrect(long pid,
  		  String name,
  		  String money,
  		  String out_trade_no,
  		  String trade_no,
  		  String param,
  		  String trade_status,
  		  String type,//alipay
  		  String sign,
  		  String sign_type,SGRef<String> r
    		) {

//        //参数存入 map
//        Map<String,String> signMap = new HashMap<>();
//        signMap.put("pid",String.valueOf(pid));
//        signMap.put("name",name);
//        signMap.put("money",money);
//        signMap.put("out_trade_no",out_trade_no);
//        signMap.put("trade_no",trade_no);
//        signMap.put("param",param);
//        signMap.put("trade_status",trade_status);
//        signMap.put("type",type);
////        signMap.put("notify_url",notifyUrl);
////        signMap.put("return_url",returnUrl);
// 
//        //根据key升序排序
//        signMap = sortByKey(signMap);
// 
//        String signStr = "";
// 
//        //遍历map 转成字符串
//        for(Map.Entry<String,String> m :signMap.entrySet()){
//            signStr += m.getKey() + "=" +m.getValue()+"&";
//        }
// 
//        //去掉最后一个 &
//        signStr = signStr.substring(0,signStr.length()-1);
// 
//        //最后拼接上KEY
//        signStr += key;
// 
//        //转为MD5
//        signStr = DigestUtils.md5DigestAsHex(signStr.getBytes());
    	String signStr=getSign(pid, name, money, out_trade_no, trade_no, param, trade_status, payType);
        r.SetValue(signStr);
        return sign.equals(signStr);
    }
    public String getZPayNotifyUrlDemo(
    		//int pid,
    		  String name,
      		  String money,
      		  String out_trade_no,
      		  //String trade_no,
      		  String param//,
      		  //String trade_status,
//      		  String type//,//alipay
//      		  String sign,
//      		  String sign_type
      		  ) {

  		  String trade_no=out_trade_no;
  		  String trade_status="TRADE_SUCCESS";
    	String notify=SGDataHelper.FormatString(
notifyUrl+"?pid={0}&name={1}&money={2}&out_trade_no={3}&trade_no={4}&param={5}&trade_status={6}&type={7}&sign={8}&sign_type={9}"
    			,
    			 pid,
      		   name,
        		   money,
        		   out_trade_no,
        		   out_trade_no,//trade_no,
        		   param,
        		   trade_status,
        		   payType,//type,//alipay
        		   getSign(pid, name, money, out_trade_no, trade_no, param, trade_status, payType),
        		   "MD5"
    			);
    	return notify;
    }
    private boolean isTest=true;

    /**
     * 查询3方订单
     * https://zpayz.cn/api.php?act=order&pid={商户ID}&key={商户密钥}&out_trade_no={商户订单号}
     * 
     * @param orderId
     * @return
     */
	public boolean getOrder(long orderId) {
		String refundUrl=SGDataHelper.FormatString(
"https://zpayz.cn/api.php?act=order&pid={0}&key={1}&out_trade_no={2}", 
pid,key,orderId
				);

//		String refundUrl=
//"https://zpayz.cn/api.php?act=refund"
//				;
		if(isTest) {
		SGDataHelper.getLog().print("-----------getOrder---------------");
		SGDataHelper.getLog().print(refundUrl);
		}
//		HashMap<String,String> body=new HashMap<String,String>();
//		body.put("pid",String.valueOf(pid) );
//		body.put("key",key );
//		body.put("trade_no",trade_no );
//		body.put("out_trade_no",String.valueOf(orderId) );
//		body.put("money",money);
		SGRequestResult r= SGHttpHelper.HttpGet(refundUrl,
				""
//				"{\"out_trade_no\":\""+orderId+"\"}"//这格式可以
//				JSON.toJSONString(body)
				);
		if(isTest) {
		SGDataHelper.getLog().print(r.content);
		if(null!=r.error) {
		SGDataHelper.getLog().print(r.error);
		}
		}
		return r.success;
	}
	/**
	 * 退款
	 * https://zpayz.cn/api.php?act=refund
	 * 
	 * @param orderId
	 * @param trade_no
	 * @param money
	 * @return
	 */
	public boolean refund(long orderId,String trade_no,String money) {
//		String refundUrl=SGDataHelper.FormatString(
//"https://zpayz.cn/api.php?act=refund&pid={0}&key={1}&trade_no={2}&out_trade_no={3}&money={4}", 
//pid,key,trade_no,orderId,money
//				);

		String refundUrl=
"https://zpayz.cn/api.php?act=refund"
				;
		if(isTest) {
		SGDataHelper.getLog().print("-----------refund---------------");
		SGDataHelper.getLog().print(refundUrl);
		}
		HashMap<String,String> body=new HashMap<String,String>();
		body.put("pid",String.valueOf(pid) );
		body.put("key",key );
		body.put("trade_no",trade_no );
		body.put("out_trade_no",String.valueOf(orderId) );
		body.put("money",money);
		SGRequestResult r= SGHttpHelper.HttpPost(refundUrl, 
//				"{\"out_trade_no\":\""+orderId+"\"}"//这格式可以
				JSON.toJSONString(body)
				);
		if(isTest) {
		SGDataHelper.getLog().print(r.content);
		if(null!=r.error) {
		SGDataHelper.getLog().print(r.error);
		}
		}
		return r.success;
	}

public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
}