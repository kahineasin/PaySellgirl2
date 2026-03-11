//package com.sellgirl.sellgirlPayWeb;
//
//
//import org.mybatis.spring.annotation.MapperScan;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan;
////import com.sellgirl.sgJavaHelper.PFURLClassLoader;
//import com.sellgirl.sgJavaHelper.UnicodeReader;
//import com.sellgirl.sgJavaHelper.config.PFDataHelper;
//import sun.misc.Resource;
//import sun.misc.URLClassPath;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.net.URL;
//import java.security.AccessControlContext;
//import java.security.AccessController;
//import java.util.ArrayList;
//import java.util.Enumeration;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.jar.Attributes;
//import java.util.jar.JarEntry;
//import java.util.jar.JarFile;
//import java.util.jar.Manifest;
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
// * 测试手动加载class(不是jar里)
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
//public class App05 {
//    private static final Logger LOGGER = LoggerFactory.getLogger(App05.class);
//
//    public static void main(String[] args) throws Exception {
////        JarFile jf=new JarFile("\\C:\\Users\\1011712002\\AppData\\Local\\JetBrains\\IntelliJIdea2022.2\\captureAgent\\debugger-agent.jar");
////
////        if(null == jf) {
////            LOGGER.info("class" + 1 + " is null");
////        }else {
////            LOGGER.info("class" + 1 + " is not null");
////        }
//
//        URL baseUrl=new URL("file:D:\\eclipse_release\\javaDemo4\\mysql-connector-java-8.0.23");
//        ArrayList<URL> urls = new ArrayList<URL>();
//        //urls.add(new URL("D:/eclipse_release/javaDemo4/javaDemo4-0.0.1-jar-with-dependencies.jar"));//Exception in thread "main" java.net.MalformedURLException: unknown protocol: d
//        urls.add(new URL("file:D:\\eclipse_release\\javaDemo4\\mysql-connector-java-8.0.23"));
//        PFURLClassLoader ucl = new PFURLClassLoader(urls.toArray(new URL[urls.size()]));
//        File file = new File("D:\\eclipse_release\\javaDemo4\\mysql-connector-java-8.0.23\\com\\mysql\\cj\\jdbc\\Driver.class");
//
////        String text = "";
////        try (BufferedReader out = new BufferedReader(new UnicodeReader(new FileInputStream(file), PFDataHelper.encoding))) {
////            String b = null;
////            int cnt = 0;
////            while ((b = out.readLine()) != null) {
////////				text+=b;
//////				text += (b + "\r\n");
////
////                if (cnt > 0) {
////                    text += "\r\n";
////                }
////                text += b;
////                cnt++;
////            }
////            out.close();
////        } catch (Exception e) {
////            //WriteError(e);
////        }
////        LOGGER.info(text);
//
//        Class<?> cls = ucl.loadClass3("com.mysql.cj.jdbc.Driver", file,baseUrl);
//        //Class<?> cls = ucl.loadClass2("com.mysql.cj.jdbc.Driver", file);//Exception in thread "main" java.lang.ClassFormatError: Incompatible magic value 0 in class file com/mysql/cj/jdbc/Driver
//
//        if (null == cls) {
//            LOGGER.info("cls" + 1 + " is null");
//        } else {
//            LOGGER.info("cls" + 1 + " is not null");
//        }
//    }
//}
//
