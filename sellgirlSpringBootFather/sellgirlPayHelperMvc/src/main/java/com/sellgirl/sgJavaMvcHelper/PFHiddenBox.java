package com.sellgirl.sgJavaMvcHelper;

import java.util.function.Function;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/// <summary>
/// Text组件--wxj
/// </summary>
public class PFHiddenBox extends PFTextBox
{
  public <TModel, TProperty>  String Html(HtmlHelperT<TModel> htmlHelper, Function<TModel, TProperty> expression)
  {
	  String name=expression.toString();
      return SGDataHelper.FormatString("<input class=\"txt_333_12\" id=\"{0}\" name=\"{0}\" {1} type=\"hidden\" value=\"\">", name,GetHtmlAttributesString());
//    Map<String,Object> htmlAttributes = GetHtmlAttributes();
//      return htmlHelper.TextBoxFor(expression, htmlAttributes);
  }
    
//    public  String Html<TModel, TProperty>(HtmlHelper<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression)
//    {
//        //var config = GetModelPropertyConfig(expression);
//        //SetByModelPropertyConfig(config);
//        var htmlAttributes = GetHtmlAttributes();
//        return htmlHelper.TextBoxFor(expression, htmlAttributes);
//    }
    public  String Html(HtmlHelper htmlHelper, String name, Object value)
    {
    	return SGDataHelper.FormatString("<input class=\"txt_333_12\" id=\"{0}\" name=\"{0}\" {1} type=\"hidden\" value=\"{2}\">", name,GetHtmlAttributesString(),value);
//        var htmlAttributes = GetHtmlAttributes();
//        return htmlHelper.TextBox(name, value, htmlAttributes);
    }

}
