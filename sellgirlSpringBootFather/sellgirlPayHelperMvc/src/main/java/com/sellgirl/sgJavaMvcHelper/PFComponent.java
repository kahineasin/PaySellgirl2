package com.sellgirl.sgJavaMvcHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
//import com.sellgirl.sgJavaHelper.*;
//import com.sellgirl.sgJavaHelperMvc.HtmlHelper;
import com.sellgirl.sgJavaHelper.PFModelConfig;
import com.sellgirl.sgJavaHelper.PFModelConfigCollection;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/// <summary>
/// 组件基类(注意:在此增加代码要慎重)--wxj
/// </summary>
public abstract class PFComponent
{
    private String _id;
    private Map<String, String> _style = new HashMap<String, String>();
    private Map<String, Object> _htmlAttributes = new HashMap<String, Object>();
    private List<String> _classes = new ArrayList<String>();
    public PFComponent()
    {
    }
    public void SetId(String id){
        _id = id;
    }
    public String GetId()
    {
        return _id;
    }
    public void SetStyle(String style)
    {
        String[] list = style.split(";");
        for (String i :list)
        {
            String[] kv = i.split(":");
            if (_style.containsKey(kv[0]))
            {
                _style.remove(kv[0]);
            }
            _style.put(kv[0], kv[1]);
        }
    }
    public void AutoWidth()
    {
        SetStyle("width:auto");
    }
    public void SetHtmlAttributes(Object htmlAttributes)
    {
        Field[] list = htmlAttributes.getClass().getFields();
//        var list = htmlAttributes.GetType().GetProperties().Where(p => p.PropertyType.IsPublic && p.CanRead
//        //&& p.CanWrite 
//        && (p.PropertyType.IsValueType || p.PropertyType == typeof(String)));
        for(Field p :list)
        {
            if(_htmlAttributes.containsKey(p.getName())){
                _htmlAttributes.remove(p.getName());
            }
            try {
				_htmlAttributes.put(p.getName(),p.get(htmlAttributes));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				SGDataHelper.WriteError(new Throwable(),e);
				//e.printStackTrace();
			}
        }
    }
    public void AddClass(String sClass)
    {
        _classes.add(sClass);
    }
    public void RemoveClass(String sClass)
    {
        _classes.remove(sClass);
    }
    public void SetClass(String classes)
    {
        _classes = Arrays.asList(classes.split(" "));
    }

    //#region Protected
    protected void SetHtmlAttribute(String key, Object value)
    {
        if (_htmlAttributes.containsKey(key)) { _htmlAttributes.remove(key); }
        _htmlAttributes.put(key, value);
    }
    protected void RemoveHtmlAttribute(String key)
    {
        if (_htmlAttributes.containsKey(key)) { _htmlAttributes.remove(key); }
    }
    protected// virtual //java中没有virtual,
    Map<String, Object> GetHtmlAttributes()
    {

        if (!_style.isEmpty()) { SetHtmlAttribute("style", String.join(";",SGDataHelper.MapSelect(_style, (k,v)->k+":"+v))); }
        if (!_classes.isEmpty()) { SetHtmlAttribute("class", String.join(" ", _classes)); }
        if (!SGDataHelper.StringIsNullOrWhiteSpace(_id)) { SetHtmlAttribute("id",_id ); }
        return _htmlAttributes;
    }
    protected String GetHtmlAttributesString()
    {
    	Map<String, Object>  htmlAttributes = GetHtmlAttributes();
    	String sAttr = "";
    	Iterator<String> iter = htmlAttributes.keySet().iterator();
    	  while(iter.hasNext()){
    		   String key=iter.next();
    		   Object value = htmlAttributes.get(key);
    		   sAttr +=SGDataHelper.FormatString(" {0}='{1}'",key,value);
//    		   System.out.println(key+" "+value);
    		  }
//        foreach(var attr in htmlAttributes)
//        {
//            sAttr += string.Format(" {0}='{1}'",attr.Key,attr.Value);
//        }
        return sAttr;
    }
//    protected <TModel, TProperty> PFModelConfig GetModelPropertyConfig(HtmlHelper htmlHelper, Expression<Function<TModel, TProperty>> expression)
//    {
//        var modelType = typeof(TModel);
//        //var modelConfig = SGDataHelper.GetModelConfig(modelType.Name, modelType.FullName);
//        //if (modelConfig == null)
//        //{
//        //    modelConfig = htmlHelper.ViewData["modelConfig"] as PFModelConfigCollection;
//        //}
//        var modelConfig = SGDataHelper.MergeModelConfig(
//            SGDataHelper.GetModelConfig(modelType.Name, modelType.FullName),
//            htmlHelper.ViewData["modelConfig"] as PFModelConfigCollection
//            );
//        if (modelConfig == null) { return null; }
//        var propertyName = ExpressionHelper.GetExpressionText(expression);
//        var config = modelConfig[propertyName];
//        return config;
//    }

    protected PFModelConfig GetModelPropertyConfig(HtmlHelper htmlHelper, String expression)
    {
        PFModelConfigCollection modelConfig = PFModelConfigCollection.class.cast(htmlHelper.ViewData.get("modelConfig"))  ;
        if (modelConfig == null) { return null; }
        //var propertyName = ExpressionHelper.GetExpressionText(expression);
        String propertyName=expression;
        PFModelConfig config = modelConfig.get(propertyName);
        return config;
    }
    //#endregion
}