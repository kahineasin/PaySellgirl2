//package com.sellgirl.sellgirlPayWeb;
//
//
//import org.mybatis.spring.annotation.MapperScan;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan;
//import com.sellgirl.sgJavaHelper.PFURLClassLoader;
//
//import java.io.File;
//import java.net.URL;
//import java.util.ArrayList;
////import com.sellgirl.sellgirlPayMq.*;
//
//
////import com.sellgirl.sellgirlPayWeb.configuration.Inject001;
////import com.sellgirl.sellgirlPayWeb.configuration.InjectHelper;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.beans.factory.annotation.Value;
////import org.springframework.boot.CommandLineRunner;
////import org.springframework.context.annotation.Bean;
//
////import com.sellgirl.sellgirlPayMq.MonthDataCompareCntConsumer;
////import com.sellgirl.sellgirlPayWeb.controller.PrincessDayController;
////import com.sellgirl.sellgirlPayMq.PFMqHelper;
//
///**
// * 测试手动加载class(jar里)
// *
// * 测试通过
// */
//@SpringBootApplication
//@MapperScan("com.sellgirl.sellgirlPayDao")
//@ComponentScan(basePackages = {
//		"pf.java.pfHelper.config",
//		"com.sellgirl.sellgirlPayWeb.configuration",
//		"com.sellgirl.sellgirlPayWeb.aop",
//		//"com.sellgirl.*",
//		"com.sellgirl.sellgirlPayWeb.model.game",
//		"com.sellgirl.sellgirlPayWeb.model.xbox",
//		//,"com.sellgirl.sellgirlPayMq.consumer"
//		"com.sellgirl.sellgirlPayDao",
//		"com.sellgirl.sellgirlPayDao.*",
//		"com.sellgirl.sellgirlPayService",
//		"com.sellgirl.sellgirlPayWeb.service",
//		"com.sellgirl.sellgirlPayWeb.service.*",
//		"com.sellgirl.sellgirlPayWeb.apiController",
//		"com.sellgirl.sellgirlPayWeb.controller",
//		})
//public class App06 {
//    private static final Logger LOGGER = LoggerFactory.getLogger(App06.class);
//
//    public static void main(String[] args) throws Exception {
//
//        URL url=new URL("jar:file:/D:/eclipse_release/javaDemo4/mysql-connector-java-8.0.23.jar!/com/mysql/cj/jdbc/Driver.class");
//        URL baseUrl=new URL("jar:file:/D:/eclipse_release/javaDemo4/mysql-connector-java-8.0.23.jar!/");
//        ArrayList<URL> urls = new ArrayList<URL>();
//      urls.add(baseUrl);
//        PFURLClassLoader ucl = new PFURLClassLoader(urls.toArray(new URL[urls.size()]));
//     Class<?> cls = ucl.loadClass4("com.mysql.cj.jdbc.Driver", url,baseUrl);
//
//        if (null == cls) {
//            LOGGER.info("cls" + 1 + " is null");
//        } else {
//            LOGGER.info("cls" + 1 + " is not null");
//            Object obj=cls.getDeclaredConstructor().newInstance();
//            if (null == obj) {
//                LOGGER.info("obj" + 1 + " is null");
//            } else {
//                LOGGER.info("obj" + 1 + " is not null");
//            }
//        }
//    }
//}
//
