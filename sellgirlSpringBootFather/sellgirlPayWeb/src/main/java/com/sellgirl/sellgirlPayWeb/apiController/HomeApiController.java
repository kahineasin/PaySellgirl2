package com.sellgirl.sellgirlPayWeb.apiController;

//import com.sellgirl.sgJavaHelper.PFPathTest;
import com.sellgirl.sgJavaHelper.AbstractApiResult;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/sample")
public class HomeApiController {

    @GetMapping(value = "/alive")
	@CrossOrigin
    public boolean alive(){
    	//return AbstractApiResult.success(PFPathTest.getTest());
    	return true;
    }
    @GetMapping(value = "/config")
	@CrossOrigin
    public AbstractApiResult<?> appConfig(){
    	//return AbstractApiResult.success(PFPathTest.getTest());
    	return AbstractApiResult.success(com.sellgirl.sgJavaSpringHelper.config.SGDataHelper.GetAppConfig());
    }
    
}
