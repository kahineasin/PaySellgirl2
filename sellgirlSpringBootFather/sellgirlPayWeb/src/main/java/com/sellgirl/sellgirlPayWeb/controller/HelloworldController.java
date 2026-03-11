package com.sellgirl.sellgirlPayWeb.controller;

import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.sellgirl.sellgirlPayMq.PFMqHelper;
import com.sellgirl.sellgirlPayMq.model.CompareCnt;
import com.sellgirl.sellgirlPayMq.producer.*;
import com.sellgirl.sellgirlPayService.ServiceTest;
import com.sellgirl.sellgirlPayWeb.apiController.CrmController;
import com.sellgirl.sellgirlPayWeb.model.test.TestBindPModel;

import com.sellgirl.sgJavaHelper.AbstractApiResult;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sellgirl.sellgirlPayMq.producer.TestMqProduct2;

@RestController
public class HelloworldController {

//	@GetMapping("/")
	@GetMapping("/api/demo")
	String index() {
		String str = "<b style=\"color:blue\">Hello Spring Boot,包含的方法有:</b><br />";
		str += "<a style=\"color:orange\" href=\"/swagger-ui.html\">swagger Api文档</a><br />";
		// List<Method[]> mdsList=new ArrayList<Method[]>();
		Map<String, Method[]> mdsList = new HashMap<String, Method[]>();
		mdsList.put("crm1", CrmController.class.getDeclaredMethods());

		Iterator<String> iter = mdsList.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			Method[] value = mdsList.get(key);

			for (Method method : value) {
				String p = method.getName().toLowerCase();
				if (p.equals("main") || p.equals("index") || p.equals("addviewcontrollers")) {
					continue;
				}
				str += String.format("<a href=\"/%s/%s\">%s</a>", key, p, method.getName()) + "<span>,</span><br />";
			}
		}

		return str;
	}

	@GetMapping("/webtest")
	public String WebTest() {
		ServiceTest daoTest=new ServiceTest();
		return daoTest.showService()+ ":我是Web!"; 
	}

    @GetMapping(value = "/showerror")
    public AbstractApiResult<?> ShowError() {
        return AbstractApiResult.error("错误");
    }
    @GetMapping(value = "/showsuccess")
    public AbstractApiResult<?> ShowSuccess() {
        return AbstractApiResult.success();
    }
    @GetMapping(value = "/showsuccessdata")
    public AbstractApiResult<?> ShowSuccessData() {
        return AbstractApiResult.success(new String[] {"aa","bb"});
    }
    @GetMapping(value = "/sendmq")
    public AbstractApiResult<?> SendMq() {

    	PFMqHelper.BuildProducer("sendmq1 , "+GetDateString()+"");
    	
        return AbstractApiResult.success();
    }
    @GetMapping(value = "/sendmq2")
    public AbstractApiResult<?> SendMq2() {

    	PFMqHelper.BuildProducer("sendmq2 , "+GetDateString()+"",
    			new TestMqProduct2());
    	
        return AbstractApiResult.success();
    }


    @GetMapping(value = "/sendrocketmq")
    public AbstractApiResult<?> SendRocketMq() {

    	PFMqHelper.BuildProducer("SendRocketMq , "+GetDateString()+"",
    			new TestRocketMqProduct());
    	
        return AbstractApiResult.success();
    }
    
    @GetMapping(value = "/sendalimq")
    public AbstractApiResult<?> SendAliMq() {

    	PFMqHelper.BuildProducer("SendAliMq , "+GetDateString()+"",
    			new TestAliMqProduct());
    	
        return AbstractApiResult.success();
    }
    
    @GetMapping(value = "/sendhyzlupdatemq")
    public AbstractApiResult<?> SendHyzlUpdateMq() {

    	PFMqHelper.BuildProducer("SendHyzlUpdateMq , "+GetDateString()+"",
    			new HyzlUpdateProduct());
    	
        return AbstractApiResult.success();
    }
    
    @GetMapping(value = "/sendpfemailmq")
    public AbstractApiResult<?> SendPFEmailMq() {

    	List<CompareCnt> list = MonthDataCompareCntProducer.Product("bonus");    	
    	System.out.println(JSON.toJSONString(list));
        return AbstractApiResult.success();
    }

    /*
     * 为了便于跨域调用，使用get方式
     */
    @GetMapping(value = "/shutdownsystem")
    public AbstractApiResult<?> ShutdownSystem() {
    	try {
			Runtime.getRuntime().exec("shutdown /s");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return AbstractApiResult.success();
    }

    @GetMapping(value = "/gettest")
    public AbstractApiResult<?> GetTest() {
        return AbstractApiResult.success("get");
    }
    @GetMapping(value = "/gettestbycode")
    public AbstractApiResult<?> GetTestByCode(String code) {
        return AbstractApiResult.success("get:"+code);
    }
    
    @PostMapping(value = "/posttest")
    public AbstractApiResult<?> PostTest() {
        return AbstractApiResult.success("post");
    }
    @PostMapping(value = "/posttestbycode")
    public AbstractApiResult<?> PostTestByCode(String code) {
        return AbstractApiResult.success("post:"+code);
    }
    
/**
 * http://localhost:28303/TestBindP
 * @return
 */
    @GetMapping(value = "/TestBindP")
    public AbstractApiResult<?> TestBindP() {
    	return AbstractApiResult.success(new TestBindPModel(SGDate.Now()));
    }
    /**
     * http://localhost:28303/TestBindP2?date=2022-02-17%2000%3A00%3A00
     * 
     * 这样会用PFDate的无参数构造函数来构造date
     * 就算存在public PFDate(String s),也不会用来构造
     * @param date
     * @return
     */
    @GetMapping(value = "/TestBindP2")
    public AbstractApiResult<?> TestBindP2(SGDate date) {
    	return AbstractApiResult.success(new TestBindPModel(date));
    }
    /**
     * 推荐此种写法
     * 
     * http://localhost:28303/TestBindP3?date=2022-02-17%2000%3A00%3A00
     * 
     * 这样会用public PFDate(String s)构造函数来构造date
     * @param date
     * @return
     */
    @GetMapping(value = "/TestBindP3")
    public AbstractApiResult<?> TestBindP3(@RequestParam(value = "date", required = true)SGDate date) {
    	return AbstractApiResult.success(new TestBindPModel(date));
    }

    
    private String GetDateString() {
    	return SGDataHelper.ObjectToDateString(Calendar.getInstance(), SGDataHelper.DateFormat);
    }
    
}
