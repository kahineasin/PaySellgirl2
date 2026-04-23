package com.sellgirl.sellgirlPayWeb;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TimeZone;

//import org.apache.catalina.Context;
//import org.apache.catalina.connector.Connector;
//import org.apache.tomcat.util.descriptor.web.SecurityCollection;
//import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
//import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import com.sellgirl.sellgirlPayWeb.configuration.AppKey;
import com.sellgirl.sgJavaHelper.AES;
import com.sellgirl.sgJavaHelper.HostType;
import com.sellgirl.sgJavaHelper.SGEmailSend;
//import com.sellgirl.sellgirlPayMq.*;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.file.SGEncryptByte;



@SpringBootApplication
@MapperScan("com.sellgirl.sellgirlPayDao")
@ComponentScan(basePackages = {
//		"pf.java.pfHelper.config",
		"com.sellgirl.sgJavaSpringHelper.config",
		"com.sellgirl.sellgirlPayWeb.configuration",
		"com.sellgirl.sellgirlPayWeb.aop",
		//"com.sellgirl.*",
		"com.sellgirl.sellgirlPayWeb.model.game",
		"com.sellgirl.sellgirlPayWeb.model.xbox",
		//,"com.sellgirl.sellgirlPayMq.consumer"
		"com.sellgirl.sellgirlPayDao",
		"com.sellgirl.sellgirlPayDao.*",
//		"com.sellgirl.sellgirlPayService",
		"com.sellgirl.sellgirlPayWeb.service",
		"com.sellgirl.sellgirlPayWeb.service.*",
		"com.sellgirl.sellgirlPayWeb.apiController",
		"com.sellgirl.sellgirlPayWeb.controller",
		
		"com.sellgirl.sellgirlPayWeb.user.service",
//		"com.sellgirl.sellgirlPayWeb.pay.service",
		"com.sellgirl.sellgirlPayWeb.product",
		"com.sellgirl.sellgirlPayWeb.user",
		"com.sellgirl.sellgirlPayWeb.pay",
		
//		"com.sellgirl.sellgirlPayWeb.pay.config",
//		"com.sellgirl.sellgirlPayWeb.pay.service",
//		"com.sellgirl.sellgirlPayWeb.pay",
		
		"com.sellgirl.sgJavaMvcHelper.config",
		"com.sellgirl.sellgirlPayWeb.oAuth",
		"com.sellgirl.sellgirlPayWeb.shop"
		})
public class SellgirlPayWebApplication {

//    @Value("${test1}")
//    private  String test1;
//    @Value("${test2:002d}")
//    private  String test2;
//    @Autowired
//    private Inject001 inject001;
	public static void main(String[] args) {
		
    	TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
    	TimeZone.setDefault(timeZone);
    	
		initSG();
		SpringApplication.run(SellgirlPayWebApplication.class, args);
//		Inject001 inject00101=InjectHelper.inject001;
//		String aa="aa";
	}
	private static void initSG() {
//		File dumpFile = new File("D:\\gitee\\secretKey\\paySellgirl\\key.yml");
//		AppKey appKey=(AppKey)SGDataHelper.map2Object((Map<String, Object>)Yaml.load(dumpFile),AppKey.class);
//		
////		String aa=SGDataHelper.ReadFileToString("D:\\gitee\\secretKey\\paySellgirl\\key.yml");
////		AppKey appKey=Yaml.loadType(aa,AppKey.class);


	try {
//		SGEmailSend.EMAIL_OWNER_ADDR_HOST = "";
//		SGEmailSend.EMAIL_OWNER_ADDR = AES.AESDecryptDemo("u8k/Cz5Z9ddjUvNuTeXVVA==",
//				SGDataHelper.decodeBase64(key));
//		SGEmailSend.EMAIL_OWNER_ADDR_PASS = AES.AESDecryptDemo("9Y4YkBmOw1mlrmmx4QHz8wK5E7/ZhZxuvoll2MmCvVc=",
//				SGDataHelper.decodeBase64(key));
//		SGEmailSend.EMAIL_OWNER_ADDR_HOST_PROPERTY = HostType.SELLGIRL.getProperties();

//		SGEmailSend.EMAIL_OWNER_ADDR_HOST=null;//"smtp.qq.com";
		SGEmailSend.EMAIL_OWNER_ADDR="2557667040@qq.com";
		SGEmailSend.EMAIL_OWNER_ADDR_PASS="ctmglvmrtpuddjaj";
		SGEmailSend.EMAIL_OWNER_ADDR_HOST_PROPERTY= HostType.TENCENT2.getProperties();
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
//    public static String getKey() throws IOException {
//
//    	File encFile = new File("d:\\github\\1\\2\\b.jpg");
//		FileInputStream fis = new FileInputStream(encFile);
//		return SGEncryptByte.DecryptByteToString(fis, SGEncryptByte.DEFAULT_BUFFER_SIZE, 0x123456);
//
//    }
	public static final String key = "a2FoaW5lYXNpbjEyMzQ1Ng==";

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

