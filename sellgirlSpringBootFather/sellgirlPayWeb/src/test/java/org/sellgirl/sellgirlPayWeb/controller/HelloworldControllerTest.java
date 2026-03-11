//package org.sellgirl.sellgirlPayWeb.controller;
//
////import static org.junit.Assert.*;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
////import org.springframework.test.web.servlet.MvcResult;
////import org.front.server.Application;
////import org.front.server.web.control.TestController;
//import org.hamcrest.Matchers;
//import org.junit.Before;
//import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
////import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import com.alibaba.fastjson.JSON;
//import com.sellgirl.sellgirlPayWeb.SellgirlPayWebApplication;
//import com.sellgirl.sellgirlPayWeb.configuration.PFConfigMapper;
//
//import com.sellgirl.sgJavaHelper.HostType;
//import com.sellgirl.sgJavaHelper.IPFConfigMapper;
//import com.sellgirl.sgJavaHelper.PFEmailListener;
////import pf.java.pfHelper.PFEmailListener;
//import com.sellgirl.sgJavaHelper.PFEmailManager;
//import com.sellgirl.sgJavaHelper.PFEmailSend;
////import pf.java.pfHelper.PFListenNewEmailTask;
//import com.sellgirl.sgJavaHelper.PFListenNewEmailTask;
//import com.sellgirl.sgJavaHelper.config.PFDataHelper;
////import test.java.pfHelper.model.PFConfigTestMapper;
//
//@RunWith(SpringJUnit4ClassRunner.class)
////@SpringApplicationConfiguration(classes = MockServletContext.class)//这个测试单个controller，不建议使用
////@SpringBootTest
//@SpringBootTest(classes = SellgirlPayWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class HelloworldControllerTest {
//    @Autowired
//    private WebApplicationContext context;
//    private MockMvc mockMvc;
//    
//    @Before
//    public void setUp() throws Exception {
// //       mvc = MockMvcBuilders.standaloneSetup(new TestController()).build();
//    	mockMvc = MockMvcBuilders.webAppContextSetup(context).build();//建议使用这种
//    }
//	@Test
//	public void testWebTest() throws Exception {		
//			   //MvcResult result = 
//					mockMvc.perform(MockMvcRequestBuilders.get("/webtest")
//					.contentType(MediaType.APPLICATION_JSON_UTF8)
//	                .param("lat", "123.123").param("lon", "456.456")
//	                .accept(MediaType.APPLICATION_JSON))
//				    .andExpect(MockMvcResultMatchers.status().isOk())// 模拟向testRest发送get请求
//				    .andDo(MockMvcResultHandlers.print())
//				    .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("我是Dao!:我是Service!:我是Web!")))
//				     .andReturn();
//			   //System.out.println(result.getResponse().getContentAsString());
//					//fail("Not yet implemented");
//	}
//
//
//	@Test
//	public void testShowError() throws Exception {
//		  mockMvc.perform(MockMvcRequestBuilders.get("/showerror")
//				.contentType(MediaType.APPLICATION_JSON_UTF8)
//             .param("lat", "123.123").param("lon", "456.456")
//             .accept(MediaType.APPLICATION_JSON))
//			    .andExpect(MockMvcResultMatchers.status().isOk())// 模拟向testRest发送get请求
//			    .andDo(MockMvcResultHandlers.print())
//			    .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("\"success\":false")))
//			     .andReturn();
//	}
//
//	@Test
//	public void testShowSuccess() throws Exception {
//		  mockMvc.perform(MockMvcRequestBuilders.get("/showsuccess")
//				.contentType(MediaType.APPLICATION_JSON_UTF8)
//          .param("lat", "123.123").param("lon", "456.456")
//          .accept(MediaType.APPLICATION_JSON))
//			    .andExpect(MockMvcResultMatchers.status().isOk())// 模拟向testRest发送get请求
//			    .andDo(MockMvcResultHandlers.print())
//			    .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("\"success\":true")))
//			     .andReturn();
//	}
//
//	@Test
//	public void testShowSuccessData() throws Exception {
//		 mockMvc.perform(MockMvcRequestBuilders.get("/showsuccessdata")
//				.contentType(MediaType.APPLICATION_JSON_UTF8)
//       .param("lat", "123.123").param("lon", "456.456")
//       .accept(MediaType.APPLICATION_JSON))
//			    .andExpect(MockMvcResultMatchers.status().isOk())// 模拟向testRest发送get请求
//			    .andDo(MockMvcResultHandlers.print())
//			    .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("\"success\":true,\"data\":[\"aa\",\"bb\"]")))
//			     .andReturn();
//	}
//
//	@Test
//	public void testSendMq() throws Exception {
//		  mockMvc.perform(MockMvcRequestBuilders.get("/sendmq")
//				.contentType(MediaType.APPLICATION_JSON_UTF8)
//           .accept(MediaType.APPLICATION_JSON))
//			    .andExpect(MockMvcResultMatchers.status().isOk())// 模拟向testRest发送get请求
//			    .andDo(MockMvcResultHandlers.print())
//			    .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("\"success\":true")))
//			     .andReturn();
//	}
//	
//	@Test
//	public void testSendMq2() throws Exception {
//		  mockMvc.perform(MockMvcRequestBuilders.get("/sendmq2")
//				.contentType(MediaType.APPLICATION_JSON_UTF8)
//         .accept(MediaType.APPLICATION_JSON))
//			    .andExpect(MockMvcResultMatchers.status().isOk())// 模拟向testRest发送get请求
//			    .andDo(MockMvcResultHandlers.print())
//			    .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("\"success\":true")))
//			     .andReturn();
//	}
//
//	@Test
//	public void testSendHyzlUpdateMq() throws Exception {
//		  mockMvc.perform(MockMvcRequestBuilders.get("/sendhyzlupdatemq")
//				.contentType(MediaType.APPLICATION_JSON_UTF8)
//       .accept(MediaType.APPLICATION_JSON))
//			    .andExpect(MockMvcResultMatchers.status().isOk())// 模拟向testRest发送get请求
//			    .andDo(MockMvcResultHandlers.print())
//			    .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("\"success\":true")))
//			     .andReturn();
//	}
//
//	@Test
//	public void testSendPFEmailMq() throws Exception {
//		  mockMvc.perform(MockMvcRequestBuilders.get("/sendpfemailmq")
//				.contentType(MediaType.APPLICATION_JSON_UTF8)
//       .accept(MediaType.APPLICATION_JSON))
//			    .andExpect(MockMvcResultMatchers.status().isOk())// 模拟向testRest发送get请求
//			    .andDo(MockMvcResultHandlers.print())
//			    .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("\"success\":true")))
//			     .andReturn();
//		  //String aa="aa";
//	}
//
//	@SuppressWarnings("unused")
//	@Test
//public void testEmailListen() {
//
//	String appKey = "pfTransferTask";
//	PFEmailListener emailListener=PFEmailListener.init(new PFEmailManager(HostType.PERFECT.getProperties(), /* PFEmailSend.EMAIL_OWNER_ADDR_HOST, */
//			PFEmailSend.EMAIL_OWNER_ADDR, PFEmailSend.EMAIL_OWNER_ADDR_PASS));
//	PFListenNewEmailTask _emailListenTask = new PFListenNewEmailTask("appKeyEmailListener",
//			emailListener,
//			email -> {
//				System.out.println(email.getSubject());
//			}, (email// ,task
//			) -> {
//				return email.getSubject() != null && email.getSubject().indexOf("pfTransferTaskTest") == 0;// 这里不要用>-1,否则可能把自动回复的邮件也当作是了
//			}, 
//			//true
//			false
//			);
//	_emailListenTask.Start();
//	boolean b=true;
//	while(b) {
//		String aa="aa";
//	}
//}
//	@Test
//	public void testGetClassByInteface() {
//		 List<Class<?>> jdbcCls=PFDataHelper.getAllAssignedClass(PFConfigMapper.class, IPFConfigMapper.class);
//		 System.out.println(JSON.toJSONString(jdbcCls));
//		try {
//			//PFDataHelper.getClassesTest();
//			//PFDataHelper.getClassesTest(SystemUser.class);
//			
//			
////			List<Class<?>> jdbcCls = null;
////			// PFDataHelper.getClasses(SystemUser.class);
////			// System.out.println(JSON.toJSONString(jdbcCls));
////
////			File f = new File(
////					"D:\\\\eclipse_workspace_sellgirlPay\\sellgirlPay\\sellgirlPayHelper\\target\\classes\\pf\\java\\pfHelper\\model");
////			jdbcCls = PFDataHelper.getClasses(f, "pf.java.pfHelper.model");
////			System.out.println(JSON.toJSONString(jdbcCls));
////			f = new File(
////					"D:\\\\eclipse_workspace_sellgirlPay\\sellgirlPay\\sellgirlPayHelper\\target\\pfHelper-0.0.1-SNAPSHOT.jar!\\pf\\java\\pfHelper\\model");
////			jdbcCls = PFDataHelper.getClasses(f, "pf.java.pfHelper.model");
////			System.out.println(JSON.toJSONString(jdbcCls));
////			
////
////			f = new File(
////					"D:/eclipse_workspace_sellgirlPay/sellgirlPay/sellgirlPayHelper/target/classes/pf/java/pfHelper/model");
////			jdbcCls = PFDataHelper.getClasses(f, "pf.java.pfHelper.model");
////			System.out.println(f.exists());//true
////			f = new File(
////					"D:/eclipse_workspace_sellgirlPay/sellgirlPay/sellgirlPayHelper/target/pfHelper-0.0.1-SNAPSHOT.jar!/pf/java/pfHelper/model");
////			jdbcCls = PFDataHelper.getClasses(f, "pf.java.pfHelper.model");
////			System.out.println(f.exists());//true
////			
////
////		      ClassLoader classloader = Thread.currentThread().getContextClassLoader();
////		      InputStream is = classloader.getSystemResourceAsStream("pf/java/pfHelper/model");
////		      java.net.URL url = classloader.getResource("pf/java/pfHelper/model");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//}
