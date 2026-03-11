package com.sellgirl.sgJavaMvcHelper;

import java.util.Iterator;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.sellgirl.sgJavaHelper.AbstractApiResult;
import com.sellgirl.sgJavaHelper.PFJsonData;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class PFBaseWebController {
    protected  String GetModelConfig() {
    	return "yjquery";
    }
    protected PFJsonData rdto;
//	private ModelAndView ViewData=new ModelAndView();
	public PFViewDataDictionary ViewData=new PFViewDataDictionary();
//    public PFBaseWebController() {
//    	ViewData.put("modelConfig", SGDataHelper.GetMultiModelConfig(GetModelConfig()));
//    }
    public <TModel> ModelAndView View(TModel model,String viewName) {
    	ViewData.SetModel(model);
    	ModelAndView result= GetView();
//    	result.addObject("Model",model);
    	result.addObject("Html", new HtmlHelperT<TModel>(ViewData));
    	result.setViewName(viewName);
    	return result;
    }
    public ModelAndView View(String viewName) {
    	ModelAndView result=GetView();
    	result.addObject("Html",new HtmlHelper(ViewData));
    	result.setViewName(viewName);
    	return result;
    }
    public <TModel> ModelAndView View(TModel model) {
    	ViewData.SetModel(model);
    	ModelAndView result=GetView();
    	result.addObject("Html",new HtmlHelperT<TModel>(ViewData));
    	return result;
    }
    public ModelAndView View() {
    	ModelAndView result=GetView();
    	result.addObject("Html",new HtmlHelper(ViewData));
    	return GetView();
    }	    
    public ModelAndView Json(PFJsonData jsonData) {
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());  
        mav.addObject("Result", jsonData.Result);  
        mav.addObject("Url", jsonData.Url);  
        return mav;  
    }
    public ModelAndView Json(AbstractApiResult<?> jsonData) {
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());  
        mav.addObject("code", jsonData.getCode());  
        mav.addObject("success", jsonData.getSuccess());  
        mav.addObject("data", jsonData.getData());  
        return mav;  
    }
    private ModelAndView GetView() {
    	ViewData.put("modelConfig", SGDataHelper.GetMultiModelConfig(GetModelConfig()));
    	ModelAndView result=new ModelAndView();

  	  
    	Iterator<String> iter = ViewData.keySet().iterator();
    	  while(iter.hasNext()){
    	   String key=iter.next();
    	   Object value = ViewData.get(key);
    	   result.addObject(key,value);
    	   //System.out.println(key+" "+value);
    	  }    	
    	//如果要完全模仿C#的做法,就如下写:
    	//result.addObject("ViewData",ViewData); 
    	  
//    	  if(!result.getModelMap().containsKey("Html")) {
//    		  result.addObject("Html",new HtmlHelper());
//    	  }
//      	  if(!ViewData.containsKey("Html")) {
//      		  ViewData.put("Html",new HtmlHelper());
//      	  }
    	return result;
    }
    protected ModelAndView RedirectToAction(String actionName, String controllerName) {
    	return new ModelAndView("redirect:/"+controllerName+"/"+actionName);
    }
    protected ModelAndView RedirectToUrl(String url) {
    	return new ModelAndView("redirect:"+url);
    }
}
