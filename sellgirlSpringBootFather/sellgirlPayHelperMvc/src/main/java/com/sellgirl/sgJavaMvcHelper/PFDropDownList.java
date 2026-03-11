package com.sellgirl.sgJavaMvcHelper;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
//import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import com.sellgirl.sgJavaHelper.PFModelConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class PFDropDownList extends PFTextBox
{
    private PFSelectList _selectList;
    //private List<Pair<String, String>> _selectList;
    
    //private String _optionLabel;//C#中的下拉的optionLabel实际上是<option value="">请选择</option> .我觉得不如直接放到_selectList更好
    private String _selectedValue ;
    private String _selectedText ;
    public PFDropDownList()
    {
    	super();
    }
//    @Override
//    public <TModel, TProperty> void SetPropertyFor(HtmlHelperT<TModel> htmlHelper, Function<TModel, TProperty> expression)
//    {
//        super.SetPropertyFor(htmlHelper, expression);
//        //var t = typeof(TProperty);
//        var t = SGDataHelper.GetNonnullType(typeof(TProperty));
//        var v = PFMVCHelper.ExpressionValueFor(htmlHelper, expression);
//        if (v != null)
//        {
//            if (t.IsEnum)
//            {
//                _selectedValue = ((int)v).ToString();
//            }
//            else
//            {
//                _selectedValue = v.ToString();
//            }
//        }
//        if (t.IsEnum)
//        {
//            SetPFSelectList(SGDataHelper.EnumToKVList(t));
//        }
//
//        //_selectedValue = (PFMVCHelper.ExpressionValueFor(htmlHelper,expression)??"").ToString();
//        //_selectedText = (PFMVCHelper.ExpressionValueFor(htmlHelper, expression) ?? "").ToString();//这里用text,是因为即便是enum,ToString后肯定是需要显示的值
//    }
    public void SetSelectList(PFSelectList selectList)
    {
        _selectList = selectList;
    }
    public <TValue> void SetSelectList( List<Pair<String,TValue>> selectList)
    {
    	_selectList=new PFSelectList();
    	for(Pair<String, TValue> i : selectList) {
    		_selectList.add(new PFSelectListItem() {{
    			Value=i.getKey();
    			Text=i.getValue().toString();
    		}});
    	}
        //_selectList = selectList;
    }
//    public void SetSelectList<TKey, TValue>(IEnumerable<KeyValuePair<TKey, TValue>> list)
//    {
//        var selectList = new PFSelectList(list, "Key", "Value");
//        _selectList = selectList;
//    }
//    public void SetOptionLabel(String optionLabel)
//    {
//        _optionLabel = optionLabel;
//    }
    public  void SetSelectedValue(String selectedValue)
    {
        _selectedValue = selectedValue;
    }
    public  void SetSelectedText(String selectedText)
    {
        _selectedText = selectedText;
    }
    public void SetDisabled()
    {
        SetHtmlAttribute("disabled", "disabled");
    }
    /// <summary>
    /// 自扩展的可编辑下拉
    /// </summary>
    public void SetEditable()
    {
        SetHtmlAttribute("editable", "editable");
    }
    private String DoGetReadonlyHtml(HtmlHelper htmlHelper,PFSelectList selectedList,String name)
    {
        //var name = ExpressionHelper.GetExpressionText(expression);
    	PFSelectListItem selectedItem = selectedList == null ? null :SGDataHelper.ListFirstOrDefault(selectedList, a -> a.Selected) ;
        String text = selectedItem == null ? "" : selectedItem.Text;
        String value = selectedItem == null ? "" : selectedItem.Value;
        String s = htmlHelper.Hidden(name, value) + super.Html(htmlHelper, name + "_txt", text);
//        return com.sellgirl.sgJavaHelperMvc.MvcHtmlString.Create(s);
        return s;
    }
//    @Override
//    public <TModel, TProperty> String Html(HtmlHelperT<TModel> htmlHelper, Function<TModel, TProperty> expression)
//    {
//    	PFSelectList selectedList = GetSelectedList();
//        if (GetReadonly())//这种ReadOnly不好,暂使用前端的$pf.disableField()
//        {
//            //var name = ExpressionHelper.GetExpressionText(expression);
//      	  String name=expression.toString();
//            return DoGetReadonlyHtml(htmlHelper,selectedList,name);
//
//            //var selectedItem = selectedList == null ? null : selectedList.FirstOrDefault(a => a.Selected);
//            //var text = selectedItem == null ? "" : selectedItem.Text;
//            //var value = selectedItem == null ? "" : selectedItem.Value;
//            //String s = htmlHelper.Hidden(name, value).ToString() + base.Html(htmlHelper, name + "_txt", text).ToString();
//            //return MvcHtmlString.Create(s);
//
//            //////return base.Html(htmlHelper,expression);//只读时如果直接返回textbox,就不能实现text映射value的方式了
//            ////var name=ExpressionHelper.GetExpressionText(expression);
//            ////var text= (PFMVCHelper.ExpressionValueFor(htmlHelper, expression) ?? "").ToString();//这里用text,是因为即便是enum,ToString后肯定是需要显示的值
//            //////var text = selectedList.Where(a => a.Selected).Select(a => a.Text).FirstOrDefault();
//            //////var value= selectedList.Where(a => a.Selected).Select(a => a.Value).FirstOrDefault();
//            ////var value = _selectList==null?"": _selectList.Where(a => a.Text==text).Select(a => a.Value).FirstOrDefault();
//            ////String s = htmlHelper.Hidden(name, value).ToString()+base.Html(htmlHelper,name+"_txt", text).ToString();
//            ////return MvcHtmlString.Create(s);
//        }
//        else
//        {
//            //var config = GetModelPropertyConfig(htmlHelper, expression);
//            //SetByModelPropertyConfig(config);
//        	Map<String, Object>  htmlAttributes = GetHtmlAttributes();
//            if (selectedList == null)//为空时用DropDownListFor会报错--benjamin20190927
//            {
//                return htmlHelper.DropDownListFor(expression, new PFSelectList(), _optionLabel, htmlAttributes);
//            }
//            return htmlHelper.DropDownListFor(expression, selectedList, _optionLabel, htmlAttributes);
//        }
//    }
    @Override
    public  String Html(HtmlHelper htmlHelper, String name, Object value)
    {
    	PFSelectList selectedList = GetSelectedList();
        if (GetReadonly()) {
            //return base.Html(htmlHelper, name, value);
            return DoGetReadonlyHtml(htmlHelper, selectedList, name);
        }
        Map<String, Object> htmlAttributes = GetHtmlAttributes();
        String sAttr = "";
 	   Iterator<Entry<String, Object>> iter = htmlAttributes.entrySet().iterator();
 	   while(iter.hasNext()){
 		   Entry<String, Object> key=iter.next();
           sAttr += SGDataHelper.FormatString(" {0}='{1}'", key.getKey(), key.getValue());
 	   }
//        SGDataHelper.EachObjectProperty(htmlAttributes, (i,k,v)->{
//            sAttr += SGDataHelper.FormatString(" {0}='{1}'", k, v);
//        });
//        foreach (var attr in htmlAttributes)
//        {
//            sAttr += String.Format(" {0}='{1}'", attr.Key, attr.Value);
//        }
        String result = SGDataHelper.FormatString("<select id='{0}' name='{1}' {2} >", GetId()==null ? name:GetId(), name, sAttr);
        //var selectedList = GetSelectedList();
        if (selectedList != null)
        {
            for (PFSelectListItem selectItem : selectedList)
            {
                result += SGDataHelper.FormatString("<option value='{0}' {2} >{1}</option>", selectItem.Value, selectItem.Text, selectItem.Selected ? "selected='selected'" : "");
            }
        }
        result += "</select>";
//        return MvcHtmlString.Create(result);
        return result;
        //return htmlHelper.DropDownList(name, GetSelectedList(), (String)value, htmlAttributes);//有bug,无论如果设置不了已选值
    }
    protected PFSelectList GetSelectedList()
    {
        if ((_selectedValue != null || _selectedText != null) && _selectList != null)
        {
            if (_selectedValue != null)
            {
                return NewPFSelectList(_selectedValue);
            }
            else if (_selectedText != null)
            {
                for (PFSelectListItem i : _selectList)
                {
                    if (i.Text == _selectedText)
                    {
                        return NewPFSelectList(i.Value);
                    }
                }
            }

        }
        return _selectList;
    }
    private PFSelectList NewPFSelectList(String selectValue)
    {
//        //return new PFSelectList(_selectList, "Value", "Text", i.Value);//这样的PFSelectList多了一个层级
//        var tList =SGDataHelper.ListSelect(_selectList,a -> new Pair<Object, Object>(a.Value, a.Text)).ToList();
//        return new PFSelectList(tList, "Key", "Value", selectValue);
////        PFSelectList r=new PFSelectList();
        return _selectList.TClone();
    }

    //#region Protected
    @Override
    protected  void SetByModelPropertyConfig(PFModelConfig config)
    {
        super.SetByModelPropertyConfig(config);

        if (config != null)
        {
            if (!SGDataHelper.StringIsNullOrWhiteSpace(config.FieldWidth))
            {
                int width =Integer.parseInt(config.FieldWidth.replace("px", "")) + 20;//下拉控件后有个下健头,所以要宽一点
                SetStyle("width:" + width + "px");
            }
        }
    }
    //#endregion
}