package pf.java.pfHelper.mvc;

import java.util.function.Function;

import com.sellgirl.sellgirlPayWeb.webHelper.HtmlHelper;
import com.sellgirl.sellgirlPayWeb.webHelper.HtmlHelperT;

//
//import pf.java.pfHelper.HtmlHelper;
//import pf.java.pfHelper.HtmlHelperT;
import com.sellgirl.sgJavaHelper.PFModelConfig;
import com.sellgirl.sgJavaSpringHelper.config.SGDataHelper;

/// <summary>
/// Text组件--wxj
/// </summary>
public class PFTextBox extends PFComponent
{
    private Boolean _required;
    private Boolean _readonly;
    protected PFModelConfig _modelConfig;

    protected Boolean GetRequired() {  return _required; }
    protected Boolean GetReadonly() { return _readonly; } 
    public PFTextBox() 
    {
    	super();
        AddClass("txt_333_12");
    }
//    public //virtual
//    <TModel, TProperty> void SetPropertyFor(HtmlHelperT<TModel> htmlHelper, Expression<Func<TModel, TProperty>> expression)
//    {
//        _modelConfig = GetModelPropertyConfig(htmlHelper,expression);
//        if (_modelConfig != null)
//        {
//            SetByModelPropertyConfig(_modelConfig);
//        }
//    }
    public //virtual
    void SetPropertyFor(HtmlHelper htmlHelper, String expression)
    {
        _modelConfig = GetModelPropertyConfig(htmlHelper, expression);
        if (_modelConfig != null)
        {
            SetByModelPropertyConfig(_modelConfig);
        }
    }
    public void SetReadonly(Boolean ro)
    {
        _readonly = ro;
        if (ro) { SetHtmlAttribute("readonly", "readonly"); }
        else { RemoveHtmlAttribute("readonly"); }
    }
    public void SetReadonly()
    {
    	SetReadonly(true);
    }
    public void SetRequired(Boolean required )
    {
        _required = required;
        if (required) { SetHtmlAttribute("required", "required"); }
        else { RemoveHtmlAttribute("required"); }
    }
    public void SetRequired()
    {
    	SetRequired(true);
    }
    public void SetMinLength(int length)
    {
        SetHtmlAttribute("minlength", length);
    }
    public void SetMaxLength(int length)
    {
        SetHtmlAttribute("maxlength", length);
    }
    public void SetPlaceholder(String placeholder)
    {
        SetHtmlAttribute("placeholder", placeholder);
    }

  public <TModel, TProperty>  String Html(HtmlHelperT<TModel> htmlHelper, Function<TModel, TProperty> expression)
  {
	  String name=expression.toString();
      return SGDataHelper.FormatString("<input class=\"txt_333_12\" id=\"{0}\" name=\"{0}\" {1} type=\"text\" value=\"\">", name,GetHtmlAttributesString());
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
    	return SGDataHelper.FormatString("<input class=\"txt_333_12\" id=\"{0}\" name=\"{0}\" {1} type=\"text\" value=\"{2}\">", name,GetHtmlAttributesString(),value);
//        var htmlAttributes = GetHtmlAttributes();
//        return htmlHelper.TextBox(name, value, htmlAttributes);
    }

    //#region Protected
    protected  void SetByModelPropertyConfig(PFModelConfig config)
    {
        if (config != null)
        {
            if (config.Required) { SetRequired(); }
            //if (!String.IsNullOrWhiteSpace(config.FieldWidth)) { SetStyle("width:"+ config.FieldWidth+"pt"); }
            if (!SGDataHelper.StringIsNullOrWhiteSpace(config.FieldWidth)) { SetStyle("width:" + config.FieldWidth); }
        }
    } 
   //#endregion
}
