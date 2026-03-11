package com.perfect.demo.interceptor;

import com.perfect.demo.extend.collection.FastMap;
import com.perfect.demo.utils.HttpUtils;
import com.sellgirl.sellgirlPayWeb.configuration.pojo.RetCode;

import com.sellgirl.sgJavaHelper.SGDate;
/*import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;*/
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 为了防扫黄,把日期加入url里
 * @author Administrator
 *测试地址:https://localhost:44303/20210107/getmobiledeskimgname
 */
public class PFDayUrlInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		try {
			if (handler instanceof HandlerMethod) {
				HandlerMethod handlerMethod = (HandlerMethod) handler;
				// 获取方法上的注解
				PFDayUrl dayUrl = handlerMethod.getMethod().getAnnotation(PFDayUrl.class);
				// 如果方法上的注解为空 则获取类的注解
				if (dayUrl == null) {
					// dayUrl =
					// handlerMethod.getMethod().getDeclaringClass().getAnnotation(PFDayUrl.class);//这样拿的是父级控制器
					dayUrl = handlerMethod.getBean().getClass().getAnnotation(PFDayUrl.class);
				}
				if (dayUrl != null) {
					String url = request.getRequestURI();
					String pattern = dayUrl.value();
					Pattern p = Pattern.compile(pattern);
					Matcher m = p.matcher(url);
					//String aa = "aa";
					if (m.find()) {
						String ym = m.group(1);
						if (ym.equals(SGDate.Now().toString("yyyyMMdd"))) {
							return true;
						}
					}
					// throw e;
					// API 异常操作写在这里
					HttpUtils.responseJsonData(response, new FastMap<>().add("resultCode", RetCode.UNAUTHORIZED_STATUS)
							.add("resultMsg", "地址不存在").add("data", new HashMap<>()).done());
					return false;
					// String aa=m.group(1);
					// 获得GetMapper上的日参数
					// RequestMapping requestMap =
					// handlerMethod.getMethod().getDeclaringClass().getAnnotation(RequestMapping.class);//这样拿的是父级控制器
//                	RequestMapping requestMap = handlerMethod.getBean().getClass().getAnnotation(RequestMapping.class);
//                	
				}
			}
		} catch (Exception e) {
			// throw e;
			// API 异常操作写在这里
			HttpUtils.responseJsonData(response,
					new FastMap<>().add("resultCode", RetCode.UNAUTHORIZED_STATUS)
							.add("resultMsg", StringUtils.isEmpty(e.getMessage()) ? "未知错误" : e.getMessage())
							.add("data", new HashMap<>()).done());

			// HttpUtils.responseJsonData(response,new HashMap());
		}
		return true;
	}



}
