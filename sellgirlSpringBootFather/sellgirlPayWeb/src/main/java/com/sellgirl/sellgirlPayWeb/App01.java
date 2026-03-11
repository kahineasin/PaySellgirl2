package com.sellgirl.sellgirlPayWeb;


import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

//import org.apache.catalina.Context;
//import org.apache.catalina.connector.Connector;
//import org.apache.tomcat.util.descriptor.web.SecurityCollection;
//import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
//import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
//import com.sellgirl.sellgirlPayMq.*;

//import com.sellgirl.sellgirlPayWeb.configuration.Inject001;
//import com.sellgirl.sellgirlPayWeb.configuration.InjectHelper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;

//import com.sellgirl.sellgirlPayMq.MonthDataCompareCntConsumer;
//import com.sellgirl.sellgirlPayWeb.controller.PrincessDayController;
//import com.sellgirl.sellgirlPayMq.PFMqHelper;

/**
 * 测试在嵌套jar中加载类,不解压缩
 * @author 1011712002
 *
 */
@SpringBootApplication
@MapperScan("com.sellgirl.sellgirlPayDao")
@ComponentScan(basePackages = {
		"pf.java.pfHelper.config",
		"com.sellgirl.sellgirlPayWeb.configuration",
		"com.sellgirl.sellgirlPayWeb.aop",
		//"com.sellgirl.*",
		"com.sellgirl.sellgirlPayWeb.model.game",
		"com.sellgirl.sellgirlPayWeb.model.xbox",
		//,"com.sellgirl.sellgirlPayMq.consumer"
		"com.sellgirl.sellgirlPayDao",
		"com.sellgirl.sellgirlPayDao.*",
		"com.sellgirl.sellgirlPayService",
		"com.sellgirl.sellgirlPayWeb.service",
		"com.sellgirl.sellgirlPayWeb.service.*",
		"com.sellgirl.sellgirlPayWeb.apiController",
		"com.sellgirl.sellgirlPayWeb.controller",
		})
public class App01 {
	private static final Logger LOGGER = LoggerFactory.getLogger(App01.class);

	public static void main(String[] args) throws MalformedURLException {
		//SpringApplication.run(SellgirlPayWebApplication.class, args);

		App01.getClass(new URL("jar:file:/D:/eclipse_release/javaDemo4/mysql-connector-java-8.0.23.jar!/"), 1);//class1 is not null
//		App01.getClass(new URL("jar:file:/D:/eclipse_release/javaDemo4/mysql-connector-java-8.0.233.jar!/"), 2);//class2 is null
//		App01.getClass(new URL("jar:file:/D:/eclipse_release/javaDemo4/javaDemo4-0.0.1-jar-with-dependencies.jar!/jar/mysql-connector-java-8.0.23.jar!/"), 3);//class3 is null
//		App01.getClass(new URL(new URL("jar:file:/D:/eclipse_release/javaDemo4/javaDemo4-0.0.1-jar-with-dependencies.jar!/"),
//				"jar:file:/jar/mysql-connector-java-8.0.23.jar!/"), 4);
//		App01.getClass(new URL(new URL("jar:file:/D:/eclipse_release/javaDemo4/javaDemo4-0.0.1-jar-with-dependencies.jar!/"),
//				"jar:file:/D:/eclipse_release/javaDemo4/javaDemo4-0.0.1-jar-with-dependencies.jar!/jar/mysql-connector-java-8.0.23.jar!/"), 4);
	}

	private static void getClass(URL u9, int idx) {
		ArrayList<URL> urls = new ArrayList<URL>();
		urls.add(u9);
		URLClassLoader ucl = new URLClassLoader(urls.toArray(new URL[urls.size()]));
		Class<?> dClass9 = null;
		try {
			dClass9 = ucl.loadClass("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
		}
		if(null == dClass9) {
			LOGGER.info("class" + idx + " is null");
		}else {
			LOGGER.info("class" + idx + " is not null");
			}
//		LOGGER.info("class" + idx + " is null:");
//		LOGGER.info(String.valueOf(null == dClass9));
	}
//	private static void getClass2(URL u9, int idx) {
//		ArrayList<URL> urls = new ArrayList<URL>();
//		urls.add(u9);
//		URLClassLoader ucl = new LaunchedURLClassLoader(urls.toArray(new URL[urls.size()]));
//		Class<?> dClass9 = null;
//		try {
//			dClass9 = ucl.loadClass("com.mysql.cj.jdbc.Driver");
//		} catch (Exception e) {
//		}
//		if(null == dClass9) {
//			LOGGER.info("class" + idx + " is null");
//		}else {
//			LOGGER.info("class" + idx + " is not null");
//			}
////		LOGGER.info("class" + idx + " is null:");
////		LOGGER.info(String.valueOf(null == dClass9));
//	}
//    @Bean
//	CommandLineRunner init() {
//		return (args) -> {
//			
//			//Mq测试
//		    //PFMqHelper.ListenMq();
//			new com.sellgirl.sgJavaHelper.config.PFDataHelper(pf.java.pfHelper.config.PFDataHelper.GetAppConfig().toNotSpring());
//		    //PFMqHelper.BuildConsumer(new MonthDataCompareCntConsumer());
//		};
//	}
    
//    /**
//     * http重定向到https
//     * @return
//     */
//    @Bean
//    public TomcatServletWebServerFactory servletContainer() {
//       TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//          @Override
//          protected void postProcessContext(Context context) {
//             SecurityConstraint constraint = new SecurityConstraint();
//             constraint.setUserConstraint("CONFIDENTIAL");
//             SecurityCollection collection = new SecurityCollection();
//             collection.addPattern("/*");
//             constraint.addCollection(collection);
//             context.addConstraint(constraint);
//          }
//       };
//       tomcat.addAdditionalTomcatConnectors(httpConnector());
//       return tomcat;
//    }
//    /**
//     * https
//     * @return
//     */
//    @Bean
//    public Connector httpConnector() {
//       Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//       connector.setScheme("http");
//       //Connector监听的http的端口号
//       connector.setPort(28303);
//       connector.setSecure(false);
//       //监听到http的端口号后转向到的https的端口号
//       connector.setRedirectPort(44303);
//       return connector;
//    }
    
//    @Bean
//    public SimpleUrlHandlerMapping sampleServletMapping() {
//        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
//        mapping.setOrder(Integer.MAX_VALUE - 2);
//        Properties urlProperties = new Properties();
//        //urlProperties.put("/index", "myController");
//        urlProperties.put("/2016/*", "/2015");
//
//        mapping.setMappings(urlProperties);
//
//        return mapping;
//    }
//    @Bean
//	CommandLineRunner asyncRoute() {
//		return (args) -> {
//			//failed
//////			PFDataHelper.GetClassByInterface(interfaceCls)
////
////			
//////			RequestMappingHandlerMapping requestMappingHandlerMapping = SpringContextHolder.getBean(RequestMappingHandlerMapping.class);
////			Map<String, RequestMappingHandlerMapping>  requestMappingHandlerMappingMap = (Map<String, RequestMappingHandlerMapping>) PFDataHelper.GetClassByInterface(RequestMappingHandlerMapping.class);
////			   Iterator<Entry<String, RequestMappingHandlerMapping>> requestMappingHandlerMappingIter = requestMappingHandlerMappingMap.entrySet().iterator();
//////			   while(iter.hasNext()){
//////				   Entry<T1,T2> key=iter.next();
//////			   }
////			   Entry<String, RequestMappingHandlerMapping> key=requestMappingHandlerMappingIter.next();
////			   RequestMappingHandlerMapping requestMappingHandlerMapping=key.getValue();
////			   
////			   requestMappingHandlerMapping.set
////			   
////			Class<?> entry = PrincessDayController.class;
////			
////			Method method_name = ReflectionUtils.findMethod(entry, "t2", HttpServletRequest.class, HttpServletResponse.class);
////			
////			final String[] arr = patterns;
////			
////			PatternsRequestCondition patterns = new PatternsRequestCondition(arr);
////			
////			RequestMappingInfo mapping_info = new RequestMappingInfo("name", patterns, null, null, null, null, null, null);
////			
////			requestMappingHandlerMapping.map.registerMapping(mapping_info, entry.newInstance(), method_name);
//
//			
////			//no effect
////			RequestMapping anno= PrincessDayController.class.getAnnotation(RequestMapping.class);
////			System.out.println(anno.value()[0]);
////			anno.value()[0]="/2016";
////			//Mq测试
////		    //PFMqHelper.ListenMq();
////		    PFMqHelper.BuildConsumer(new MonthDataCompareCntConsumer());
//		};
//	}
}

