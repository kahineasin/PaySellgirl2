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
    /**
     * 总结此字段是:[多个thymeleaf模板共用的变量]
     * 但springboot是多用户多电脑单实例controller,所以绝对不能用...
     * 
     * 此字段的值是在controller的多个方法内共用的, 且用户多次请求共用, 
     * 差不多等于cache了把..
     * 比如在 controller.detail中ViewData.put(pwd)
     * 那么在 controller.index中ViewData.get(pwd) 都会有
     * 风险在于, 如果前端js这么写 const lockData=[[${lockData}]]; 就危险了
     * 因为本来以为后端 if(false){ViewData.put(lockData)} 是安全的, 
     * 实际却暴露了true时的数据
     * 直接 new ModelAndView 会更安全
     * 
     * 慎用此方法,想用cache可以试试此字段, 购买信息之类别用或者慎用
     * 
     * 但其实再想想, 在C# mvc那种多页面共用表头的情况, 其实是有用的
     * 比如多个页面的头部都显示用户名, 那viewData设置用户名, 其实多个页面生成模板时要用到
     * 
     * 最终决定:(错,绝对别用,因为controller单实例)
     * 1. 需要多页面表头模板共用的属性(比如用户名)可以使用ViewData (错的!!)
     * 2. controller内某个方法自己使用的值, 不要使用 ViewData
     * 3. 单次请求内有效的值, 不要使用ViewData, 典型错误用法if(b){ViewData.put}
     * 4. 如果你不是想用cache, 别用此字段
     * 5. ViewData和controller的生命周期密切相关, 最好别用
     * 
     * 单次使用的值, 这么加:
     * ModelAndView r=View("xx.html");
     * r.addObject("aa",aa); //这样更安全
     * 
     * @deprecated spring是单controller实例, 所以是全体用户共用的, 此字段用作thymeleaf模板绝对有问题; 若要用作cache拼接userId到key
     */
    @Deprecated
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

    @Deprecated
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
