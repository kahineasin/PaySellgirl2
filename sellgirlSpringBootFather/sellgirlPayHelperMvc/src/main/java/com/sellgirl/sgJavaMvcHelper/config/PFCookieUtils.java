package com.sellgirl.sgJavaMvcHelper.config;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/**
     * Cookie是自动区分用户，自动绑定访问的用户，所以key用固定的就行了，但容量小(且必须注入)
     * Cache是服务器的cache, 所以需要用户名做key,但能存放更大的数据
     * 
 * Created by qhong on 2018/10/15 15:46
 **/
@Component
public class PFCookieUtils {

    public static final int COOKIE_MAX_AGE = 7 * 24 * 3600;
    public static final int COOKIE_HALF_HOUR = 30 * 60;


    private static HttpServletResponse response;

    @Autowired
    private HttpServletResponse response2;

    private static HttpServletRequest request;

    @Autowired
    private HttpServletRequest request2;

    @PostConstruct
    public void beforeInit() {
        request=request2;
        response=response2;
    }


    /**
     * 根据Cookie名称得到Cookie对象，不存在该对象则返回Null
     *
     * @param request
     * @param name
     * @return
     */
    public static Cookie getCookie(String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies==null||cookies.length<1) {
            return null;
        }
        Cookie cookie = null;
        for (Cookie c : cookies) {
            if (name.equals(c.getName())) {
                cookie = c;
                break;
            }
        }
        return cookie;
    }

    /**
     * 根据Cookie名称直接得到Cookie值
     *
     * @param request
     * @param name
     * @return
     */
    public static String getCookieValue(String name) {
        Cookie cookie = getCookie(name);
        if(cookie != null){
            return SGDataHelper.getURLDecoderString(cookie.getValue());
        }
        return null;
    }

    /**
     * 移除cookie
     * @param request
     * @param response
     * @param name 这个是名称，不是值
     */
    public static void removeCookie(String name) {
        if (null == name) {
            return;
        }
        Cookie cookie = getCookie(name);
        if(null != cookie){
        	if(SGDataHelper.CookieDomain!=null) {
        		cookie.setDomain(SGDataHelper.CookieDomain);//C#中是用FormsAuthentication.CookieDomain,但不确定意义
        	}
            cookie.setPath("/");
            cookie.setValue("");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    /**
     * 添加一条新的Cookie，可以指定过期时间(单位：秒)
     *
     * @param response
     * @param name
     * @param value
     * @param maxValue
     */
    public static void setCookie(String name,
                                 String value, int maxValue) {
        if (name==null||name==""//StringUtils.isBlank(name)
        		) {
            return;
        }
        if (null == value) {
            value = "";
        }
        
        Cookie cookie = new Cookie(name, SGDataHelper.getURLEncoderString(value));
        cookie.setPath("/");
        if (maxValue != 0) {
            cookie.setMaxAge(maxValue);
        } else {
            cookie.setMaxAge(COOKIE_HALF_HOUR);
        }
        response.addCookie(cookie);
//        try {//如果有这里的代码,登陆后跳转会报错 Cannot call sendRedirect() after the response has been committed--benjamin 20191105
//            response.flushBuffer();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 添加一条新的Cookie，默认30分钟过期时间
     * Cookie时用户区别的，自动绑定访问的用户，所以name用固定的就行了，但容量小
     * Cache就服务器的cache, 所以需要用户名做key,但能存放更大的数据
     *
     * @param response
     * @param name
     * @param value
     */
    public static void setCookie(String name,
                                 String value) {
        setCookie(name, value, COOKIE_HALF_HOUR);
    }

    /**
     * 将cookie封装到Map里面
     * @param request
     * @return
     */
    public static Map<String,Cookie> getCookieMap(){
        Map<String,Cookie> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length>1){
            for(Cookie cookie : cookies){
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }

}