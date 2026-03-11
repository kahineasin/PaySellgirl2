package com.sellgirl.sellgirlPayWeb.apiController;

import com.sellgirl.sgJavaHelper.express.PFExpressHelper;
import com.sellgirl.sgJavaHelper.express.PFFormula;
import com.sellgirl.sgJavaHelper.express.PFFormulaItem;
import com.sellgirl.sgJavaHelper.express.PFFormulaItemCollection;
import com.sellgirl.sgJavaSpringHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.PFPathTest;
import com.sellgirl.sgJavaHelper.AbstractApiResult;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sample")
public class SampleApiController {

    @GetMapping(value = "/test")
	@CrossOrigin
    public AbstractApiResult<?> test(){
    	return AbstractApiResult.success(PFPathTest.getTest());
    	//return AbstractApiResult.success(pf.java.pfHelper.config.PFDataHelper.GetAppConfig());
    }
    @PostMapping(value = "/eval")
	@CrossOrigin
    public AbstractApiResult<?> SellgirlPay(String code){
		//PFExpressHelper ex=new PFExpressHelper();
		PFExpressHelper ex=PFExpressHelper.popular();
		//&运算
		ex.addCustomFormula( new PFFormula(
				new PFFormulaItemCollection(
						PFFormulaItem.variable(),
						PFFormulaItem.mark("&&"),
						PFFormulaItem.variable()) ,
				(a,b,c)->{
					return SGDataHelper.ObjectToInt(a)>0&&SGDataHelper.ObjectToInt(b)>0;
					}
				,100
				));
		ex.addCustomFormula( new PFFormula(
				new PFFormulaItemCollection(
						PFFormulaItem.variable(),
						PFFormulaItem.mark("||"),
						PFFormulaItem.variable()) ,
				(a,b,c)->{
					return SGDataHelper.ObjectToInt(a)>0||SGDataHelper.ObjectToInt(b)>0;
					}
				,100
				));
		//标签运算
		Map<String,String> tags=new HashMap<String,String>();
		tags.put("ATTRITUBE_U_06_001","5");
		tags.put("ATTRITUBE_U_07_001","5");
		ex.addCustomFormula( new PFFormula(
				new PFFormulaItemCollection(
						PFFormulaItem.variable(),
						PFFormulaItem.mark(","),
						PFFormulaItem.variable()) ,
				(a,b,c)->{
					return tags.containsKey(a.toString()) ;
					}
				,50
				));
		
		Object r=null;
		r=ex.eval(code);
    	return AbstractApiResult.success(r);
    }
    
}
