package com.sellgirl.sellgirlPayWeb.apiController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.sellgirl.sellgirlPayWeb.model.*;
import com.sellgirl.sgJavaSpringHelper.config.SGDataHelper;
import com.sellgirl.sgJavaSpringHelper.config.SGDataHelper.LocalDataType;

import java.nio.file.Paths;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.*;

/**
 * 未测试通过,前端EBusinessAppLocal的vue报跨域问题
 * @author Administrator
 *
 */
@RestController
//@RequestMapping("/sample")
public class YJQueryLocalController {

    @PostMapping(value = "/Bonus/Home/GetSfOfAllFgs")
	@CrossOrigin
    public PFJsonDataLocalT<?> GetSfOfAllFgs(){
    	String s=SGDataHelper.ReadLocalTxt(Paths.get("YJQueryLocal", "GetSfOfAllFgs.txt").toString(), LocalDataType.System);
    	PFJsonDataLocal r=new PFJsonDataLocal(JSON.parseObject(s, new TypeReference<ArrayList<NameValue>>(){}));    	
    	
    	//PFJsonDataLocalT<?> r=JSON.parseObject(s, new TypeReference<PFJsonDataLocalT<ArrayList<NameValue>>>(){});
    	return r;
    }
    /**
     * http://testebusiness.perfect99.com:88/Bonus/PerformanceAnalysis/GetMonth
     * @return
     */
    @PostMapping(value = "/Bonus/PerformanceAnalysis/GetMonth")
	@CrossOrigin
    public PFJsonDataLocalT<?> GetMonth(){
    	String s=SGDataHelper.ReadLocalTxt(Paths.get("YJQueryLocal", "GetMonth.txt").toString(), LocalDataType.System);
    	PFJsonDataLocal r=new PFJsonDataLocal(JSON.parseObject(s, new TypeReference<ArrayList<NameValue>>(){}));    	
    	
    	//PFJsonDataLocalT<?> r=JSON.parseObject(s, new TypeReference<PFJsonDataLocalT<ArrayList<NameValue>>>(){});
    	return r;
    }
//    public static class PFJsonDataLocalT<T>{
//    	public T Data ;
//    	public Boolean Result=true;
//    	public PFJsonDataLocalT(T data) {
//    		Data=data;
//    	}
//    }
//    public static class PFJsonDataLocal extends PFJsonDataLocalT<Object>{
//    	public PFJsonDataLocal(Object data) {
//    		super(data);
//    	}
//    }
//    public static class NameValue
//    {
//        public String Name ;
//        public String Value ;
//    }
}
