//package com.sellgirl.sellgirlPayWeb.configuration;
//
//import java.util.Properties;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
//
//@Configuration
//public class MyConfig extends SimpleUrlHandlerMapping{
//
//    @Bean
//    public SimpleUrlHandlerMapping simpleUrlHandlerMapping(){
//
//        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
//        simpleUrlHandlerMapping.setOrder(Integer.MAX_VALUE - 2);
//        Properties properties = new Properties();
//        //properties.setProperty("simpleUrl","mappingTest2");
//        //properties.setProperty("2016","2015");
////        properties.setProperty("/2016","/2015");//能运行，但不能访问
//        //properties.setProperty("2016","/2015");//能运行，但不能访问
//        //properties.setProperty("2016","/2015");
//        //properties.setProperty("/2016","PrincessDayController");
//        properties.setProperty("/2016","PrincessController");
//        simpleUrlHandlerMapping.setMappings(properties);
//		
//		//设置该handlermapping的优先级为1，否则会被默认的覆盖，导致访问无效
//        //simpleUrlHandlerMapping.setOrder(1);
//		
//        return simpleUrlHandlerMapping;
//    }
//}