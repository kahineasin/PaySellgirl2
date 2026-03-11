package com.sellgirl.sgJavaMvcHelper;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.sellgirl.sgJavaHelper.PFModelConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class PFLabel extends PFComponent
{
    private boolean _required=false;
    protected PFModelConfig _modelConfig;
    /**
     * 有冒号
     */
    private boolean _hasColon=true;
     
    public void SetRequired(Boolean required)
    {
        _required = required;
    }
//    public  void SetPropertyFor<TModel, TProperty>(HtmlHelper htmlHelper, Expression<Func<TModel, TProperty>> expression)
//    {
//        _modelConfig = GetModelPropertyConfig(htmlHelper,expression);
//        if (_modelConfig != null)
//        {
//            _required = _modelConfig.Required;
//        }
//    }
    public  void SetPropertyFor(HtmlHelper htmlHelper, String expression)
    {
        _modelConfig = GetModelPropertyConfig(htmlHelper, expression);
        if (_modelConfig != null)
        {
            _required = _modelConfig.Required;
        }
    }
//    public <TModel, TProperty> MvcHtmlString Html(HtmlHelperT<TModel> htmlHelper, Function<TModel, TProperty> expression)
//    {
//        if (_modelConfig != null)
//        {
//            var result = htmlHelper.LabelFor(expression, _modelConfig.FieldText);
//            var h = GetHtmlByModelConfig(result.ToHtmlString(), _modelConfig);
//            var htmlAttributes = GetHtmlAttributes();
//            h = AppendAttr(h, htmlAttributes);
//            return MvcHtmlString.Create(h);
//        }
//        return htmlHelper.LabelFor(expression);
//    }
    public MvcHtmlString Html(HtmlHelper htmlHelper, String expression)
    {
        String label = expression;
        if (_modelConfig != null) { label = _modelConfig.FieldText; }
        //var h = htmlHelper.Label(label).ToHtmlString();
        String h=SGDataHelper.FormatString("<label for=\"{0}\">{1}{2}</label>",expression, label,_hasColon?"：":"");
        h = GetHtmlByModelConfig(h, _modelConfig);
        Map<String, Object> htmlAttributes = GetHtmlAttributes();
        h = AppendAttr(h, htmlAttributes);
        return MvcHtmlString.Create(h);         
    }

    //#region Private
    private String AppendToLast(String html,  String s)//追加内容
    {
        //return Regex.Replace(html, "(<.*>)(.*)(<\\/.*>)", "$1$2" + s + "$3");
        return html.replaceAll("(<.*>)(.*)(<\\/.*>)", "$1$2" + s + "$3");
    }
    private String AppendAttr(String html, Map<String, Object> attributes)//追加内容
    {
        String s = "";
 	   Iterator<Entry<String, Object>> iter = attributes.entrySet().iterator();
 	   while(iter.hasNext()){
 		   Entry<String, Object> key=iter.next();
           s += " " + key.getKey() + "=" + key.getValue();
 	   }
//        foreach(var attr in attributes)
//        {
//            s += " " + attr.Key + "=" + attr.Value;
//        }
        //return Regex.Replace(html, "^(<)([^>]+)(>.*)", "$1$2" + s + "$3");
        return html.replaceAll("^(<)([^>]+)(>.*)", "$1$2" + s + "$3");
    }
    private String GetHtmlByModelConfig(String html, PFModelConfig config)
    {
        if (config != null)
        {
            String ex = "";
            if (_required) { ex += "<span style='color:red'>*</span>"; }
            //ex += "：";
            return AppendToLast(html, ex);
        }
        return html;
    }
    //#endregion
}
