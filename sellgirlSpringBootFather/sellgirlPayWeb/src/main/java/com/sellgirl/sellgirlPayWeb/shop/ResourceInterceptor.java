package com.sellgirl.sellgirlPayWeb.shop;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.perfect.demo.extend.collection.FastMap;
import com.perfect.demo.utils.CUrlencode;
import com.perfect.demo.utils.HMAC_SHA1;
import com.perfect.demo.utils.HttpUtils;
import com.sellgirl.sellgirlPayWeb.configuration.pojo.RetCode;
import com.sellgirl.sellgirlPayWeb.oAuth.FormsAuth;
import com.sellgirl.sellgirlPayWeb.product.ResourceController;
import com.sellgirl.sgJavaHelper.SGAllowAnonymous;
import com.sellgirl.sgJavaHelper.model.SystemUser;
import com.sellgirl.sgJavaSpringHelper.config.SGDataHelper;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
/*import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;*/
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.annotation.Annotation;
import java.net.URLEncoder;
import java.util.*;

/**
 * shop资源拦截器
 *
 * @author shadow
 */
public class ResourceInterceptor implements HandlerInterceptor {
    //private static final String secertKey = "WdfwefDfweffsfdsfsfwefsfsd#dsdfewfds";
    private static final String secertKey = "SdwefdafdadqeERdwfe#dsdfewfdsfsffd";
    //private static final String rootBaseUrl = "http://snsapi.sellgirl.com:9090/";
    private static final String rootBaseUrl = "http://snsapi.sellgirl.com:9093/";
    //private static final String extendBaseUrl = "/Unicom/SNSAPI/User/ValidateToken";
    private static final String extendBaseUrl = "/UnicomCRM/SNSAPI/User/ValidateToken";

	public static boolean isAllowAll(HandlerMethod handlerMethod) {
//        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 获取方法上的注解
        SGAllowAnonymous allowAll = handlerMethod.getMethod().getAnnotation(SGAllowAnonymous.class);
        // 如果方法上的注解为空 则获取类的注解
        if (allowAll == null) {
            allowAll = handlerMethod.getMethod().getDeclaringClass().getAnnotation(SGAllowAnonymous.class);
        }

        if (allowAll != null){
        	return true;
        }     
        return false;
	}
	
//	public static boolean hasAttr(HandlerMethod handlerMethod,Class cls) {
////      HandlerMethod handlerMethod = (HandlerMethod) handler;
//      // 获取方法上的注解
//		Annotation allowAll = handlerMethod.getMethod().getAnnotation(cls);
//      // 如果方法上的注解为空 则获取类的注解
//      if (allowAll == null) {
//          allowAll = handlerMethod.getMethod().getDeclaringClass().getAnnotation(cls);
//      }
//
//      if (allowAll != null){
//      	return true;
//      }     
//      return false;
//	}
    /**
     * 控制器方法被调用之前
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return  false表示我不放行，后续也不需要其他Interceptor再判断了；
     *                 true表示我不需要拦截，继续走后续拦截。
     *                 即是想通过，必须所有拦截器都返回true。
     *                 所有拦截器只管自己的拦截，不可强制放行跳过其他拦截器。
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
//    	boolean b=true;
//    	if(b) {
//    		return true;
//    	}
        try {
//            if(isExcludePath(request.getRequestURI())) {
//            	return true;
//            }
            
        	if(null!=handler&&handler instanceof HandlerMethod) {
        		HandlerMethod method=(HandlerMethod)handler;
        		if(isAllowAll(method)) {
        			return true;
        		}

    			Object controller = method.getBean();
    //判断是否为登录接口实现类
    			if(controller instanceof ResourceController){
    				SystemUser user=FormsAuth.GetUserExData(SystemUser.class);
    				if(null!=user&&user.isInvited) {
    					//不拦截
    					return true;
    				}
    				
    				//sellgirl好像暂时没有使用session的方式, 现在时使用cookie+cache
//    				Object user = request.getSession().getAttribute("user");
//    				if(user == null){
//    					log.debug("-----------未登录访问   跳回登录页面----");
//    					request.getRequestDispatcher("/home/loginUI").forward(request, response); 
//    					return false;   
//    				}
    			}else {
    				//ResourceController之外的不处理
    				return true;
    			}
        	}
//            //"/procedure/" 为API请求的头
//            if(!request.getRequestURI().startsWith("/crm/")){
//                return true;
//            }
//            if((boolean)this.authorize(request)){
//                return true;
//            }
        }catch (Exception e){
            //throw e;
            //API 异常操作写在这里
            HttpUtils.responseJsonData(response,new FastMap<>().add("resultCode", RetCode.UNAUTHORIZED_STATUS)
                    .add("resultMsg", StringUtils.isEmpty(e.getMessage()) ? "未知错误" : e.getMessage())
                    .add("data", new HashMap<>()).done());

            //HttpUtils.responseJsonData(response,new HashMap());
        }
//        System.out.print("asserts not pass:  ");
//        System.out.println(request.getRequestURI());
//        return false;
        
        //宽松策略，默认放行
        return true;
    }
    /**
     * 匹配多的放前面，提高性能
     * 
     * 匹配目标：
/.well-known/appspecific/com.chrome.devtools.json
/assets/css/bootstrap.min.css
/assets/css/bootstrap-icons.min.css
/common.js
/data-books.js
/assets/js/bootstrap.bundle.min.js
     */
    private static String[] exclude=new String[] {"/assets/","/.well-known/","/common.js","/data-books.js"};
    public static boolean isExcludePath(String path) {
    	for(String i:exclude) {
    		if(path.startsWith(i)) {
    			return true;
    		}
    	}
    	return false;
    }

    public Object authorize(HttpServletRequest request) throws Exception {
        try {
            String authorizations = request.getHeader("Authorization");
            Map<String, Object> map = new TreeMap<>();
            if(StringUtils.isEmpty(authorizations)){
                throw new Exception("请输入认证字段");
            }
            String[] authorizationStr = authorizations.split(",");
            String token = "";
            for(String s : authorizationStr){
                String[] split = s.trim().split("=");
                if(split[0].equalsIgnoreCase("appid")) {
                    map.put("appid", split[1].trim());
                }else if(split[0].equalsIgnoreCase("appkey")){
                    map.put("appkey", split[1].trim());
                }else if(split[0].equalsIgnoreCase("token")){
                    token = split[1].trim();
                }
            }
            if(StringUtils.isEmpty(map.get("appid")) || StringUtils.isEmpty(map.get("appkey"))){
                throw new Exception("格式不对或请补上认证信息");
            }
            if(StringUtils.isEmpty(token)) {
                throw new Exception("格式不对或请补上token字段");
            }
            map.put("signature_algorithm", "HMACSHA1");
            map.put("timestamp", System.currentTimeMillis());
            map.put("nonce", getRandomLetter());
            //生成签名字符串
            String signatureString = getSignatureString("GET", map);
            //使用密钥根据签名字符串进行签名
            String genHMAC = HMAC_SHA1.genHMAC(signatureString, secertKey+"&"+token);
            //使用appid、appkey、时间戳、签名算法名称、随机字符串生成认证字符串
            String authorizingString = getAuthorizingString(map, genHMAC, token);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            URIBuilder uriBuilder = new URIBuilder(rootBaseUrl+extendBaseUrl);

            // 根据带参数的URI对象构建GET请求对象
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.setHeader("Authorization", authorizingString);
            // 执行请求
            CloseableHttpResponse response = httpClient.execute(httpGet);
            //转为json
            String rStr=EntityUtils.toString(response.getEntity(), "UTF-8");
            JSONObject js=JSON.parseObject(rStr);
            if(js.containsKey("Success")&&SGDataHelper.ObjectToBool(js.get("Success"))==true){
                return true;
            }else{
                throw new Exception("code:"+js.get("StatusCode").toString()+"message:"+js.get("Message").toString());
            }
/*            //返回字符串转为Document
            Document document = DocumentHelper.parseText(EntityUtils.toString(response.getEntity(), "UTF-8"));
            Element element = document.getRootElement().element("Success");
            Element element1 = document.getRootElement().element("Message");
            if (null != element) {
                if (element.getText().equalsIgnoreCase("true")) {
                    return true;
                }
            }*/
            //throw new Exception(RetCode.UNAUTHORIZED_STATUS, element1.getText());
            //throw new Exception("没权限");
        } catch (Exception e) {
            throw e;
            //return BizErrorEx.transform(e);
        }
    }

    /**
     * 获取10位大写字母随机数(每天相同)
     *
     * @return
     */
    private static String getRandomLetter() {
        StringBuffer sb = new StringBuffer();
        Calendar now = Calendar.getInstance();
        int i1 = now.get(Calendar.YEAR) + now.get(Calendar.MONTH) + now.get(Calendar.DAY_OF_MONTH) ;
        Random random = new Random(i1);
        for (int i = 0; i < 10; i++) {
            long round = Math.round(random.nextDouble() * 25 + 65);
            sb.append(String.valueOf((char) round));
        }
        return sb.toString();
    }

    /**
     * 获取签名字符串
     *
     * @param method GET 或者 POST
     * @param map
     * @return
     * @throws Exception
     */
    private static String getSignatureString(String method, Map<String, Object> map) throws Exception {
        StringBuffer sign = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!StringUtils.isEmpty(entry.getKey())) {
                sign.append("&" + entry.getKey() + "=" + entry.getValue());
            }
        }
        if (StringUtils.isEmpty(sign)) {
            throw new Exception();
        }
        String signature = method + "&" + CUrlencode.urlEncode(extendBaseUrl) + sign.toString();
        return signature;
    }

    /**
     * 使用appid、appkey、时间戳、签名算法名称、随机字符串生成认证字符串
     *
     * @param map
     * @param signature HMAC-SHA1方式进行签名得到的Base64字符串
     * @param token
     * @return
     * @throws Exception
     */
    private static String getAuthorizingString(Map<String, Object> map, String signature, String token) throws Exception {
        String str = "OAuth appid=\"" + map.get("appid") + "\",appkey=\"" + map.get("appkey") + "\",signature_algorithm=\"" +
                map.get("signature_algorithm") + "\",timestamp=\"" + map.get("timestamp") + "\",nonce=\"" + map.get("nonce") + "\",signature=\"" + URLEncoder.encode(signature, "UTF-8") + "\"";
        if (!StringUtils.isEmpty(token)) {
            str += ",token=\"" + CUrlencode.urlEncode(token) + "\"";
        }
        return str;
    }

    /**
     * 获得token
     *
     * @return
     */
    @SuppressWarnings("unused")
	private static String generateToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
