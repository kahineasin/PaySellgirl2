package com.sellgirl.sellgirlPayWeb.configuration;

import com.perfect.demo.interceptor.AuthorizeInterceptor;
import com.perfect.demo.interceptor.PFDayUrlInterceptor;
import com.sellgirl.sellgirlPayWeb.shop.AntiSpiderInterceptor;
import com.sellgirl.sellgirlPayWeb.shop.LoginInterceptor;
import com.sellgirl.sellgirlPayWeb.shop.ResourceInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
/*import com.perfect.logistics.configuration.pojo.DeftConfig;
import com.perfect.logistics.configuration.pojo.ErrorPageConfig;
import com.perfect.logistics.initialize.WebInitializeService;
import com.perfect.logistics.interceptor.AuthorizeInterceptor;
import com.perfect.logistics.interceptor.DSHolderInterceptor;
import com.perfect.logistics.interceptor.ErrorPageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;*/
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SpringMVCConfigure /*    @Autowired(required = false)
    private DeftConfig deftConfig;
    @Autowired(required = false)
    private ErrorPageConfig errorPageConfig;
    @Autowired(required = false)
    private WebInitializeService webInitializeService;*/implements WebMvcConfigurer {

    @Autowired
    private AntiSpiderInterceptor antiSpiderInterceptor;


    @Bean
    public ErrorPageFilter errorPageFilter() {
        return new ErrorPageFilter();
    }

    @Bean
    public FilterRegistrationBean<ErrorPageFilter> errorPageFilterRegistration() {
        FilterRegistrationBean<ErrorPageFilter> registration = new FilterRegistrationBean<ErrorPageFilter>();
        registration.setFilter(errorPageFilter());
        registration.setEnabled(false);
        return registration;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        // 默认错误页面拦截器处理
//        //InterceptorRegistration error = registry.addInterceptor(new ErrorPageInterceptor(deftConfig, errorPageConfig));
//        // 排除配置
//        //error.excludePathPatterns(deftConfig.getStaticPrefix() + "**");
//        // 拦截配置
//        //error.addPathPatterns("/**");
////        List<HandlerInterceptor> intercepter = webInitializeService.initWebMVCInterceptor();
        List<HandlerInterceptor> interceptors = new ArrayList<>();
        interceptors.add(antiSpiderInterceptor);
        interceptors.add(new LoginInterceptor());
        interceptors.add(new AuthorizeInterceptor());
        interceptors.add(new PFDayUrlInterceptor());
        interceptors.add(new ResourceInterceptor());
        for (HandlerInterceptor interceptor : interceptors) {
            InterceptorRegistration addInterceptor = registry.addInterceptor(interceptor);
            // 排除配置
            //"/assets/","/.well-known/","/common.js","/data-books.js"
//            addInterceptor.excludePathPatterns("/static/**");
            addInterceptor.excludePathPatterns(
            		"/static/**",
            		"/assets/**","/.well-known/**",
            		"/sg/js/**",
            		"/demo/**",
            		"/common.js","/announcement.js","/data-books.js",
            		"/data-resources.js","/product/*.js",
            		"/bookImg/**","/resourceImg/**","/error"
            		);
            // 拦截配置
            addInterceptor.addPathPatterns("/**");
            
//            org.springframework.util.PathMatcher pm=new org.springframework.util.AntPathMatcher();
//            pm..match(null, null)
//            addInterceptor.pathMatcher(pm);
        } 
        
        
    }

   /* @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
//        registry.addResourceHandler(deftConfig.getStaticPrefix()+ "**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }*/
}

