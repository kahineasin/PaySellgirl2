package com.sellgirl.sgJavaMvcHelper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.sellgirl.sgJavaHelper.*;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
//import com.sellgirl.sgJavaHelper.mvc.PFGridColumn.TextAlign;

/// <summary>
/// 表格列
/// </summary>
public class PFGridColumn
{

  private Map<String, String> _style = new HashMap<String, String>();//(注意这里的样式仅对tbody生效,不生成到thead)
  private List<String> _className = new ArrayList<String>();
  public String Text ;
  public String DataIndex ;

  private String _width;
  //private Boolean _isFirstColumn;
  public String get_width() {
      return _width;
}
public void set_width(String width) {
	try {
	    BigDecimal num = new BigDecimal(_width);
        _width = num.toString() + "px";
	}catch(Exception e) {
        _width = width;
	}

    if (!SGDataHelper.StringIsNullOrWhiteSpace(_width)) { SetStyle("width", _width); }
    else { RemoveStyle("width"); }
}

  private Boolean _visible = true;
  public Boolean get_visible() {
	  return _visible;
}
public void set_visible(Boolean visible) {
    if (visible)
    {
        RemoveStyle("display");
    }
    else
    {
        SetStyle("display", "none");
    }
    _visible = visible;
}

//private PropertyInfo _propertyInfo = null;
  public Field _propertyInfo =null;
  //public PFModelConfig _modelConfig=null;

  public PFGridColumn() { }
  public PFGridColumn(StoreColumn c)
  {
      this.DataIndex = c.data;
      this.Text = c.title==null ? c.data:c.title;
      this.set_width(c.width);
      this.set_visible( c.get_visible());
      if (!this.get_visible()) { SetClassName("col-h"); }
      if (!SGDataHelper.StringIsNullOrWhiteSpace(c.dataType))
      {
          //Class<?> dataType = SGDataHelper.GetTypeByString(c.dataType);
          //PFSqlFieldType pfDataType = SGDataHelper.GetPFTypeByString(c.dataType);
    	  SGSqlFieldTypeEnum pfDataType = SGSqlFieldTypeEnum.InitByString(c.dataType);
          SetStyleByDataType(this, pfDataType);
          //Boolean isPercent = dataType == typeof(PFPercent);
          //if (dataType == typeof(BigDecimal) || dataType == typeof(int) || isPercent)
          //{
          //    SetStyle("text-align:right;padding-right: 9px");
          //    Render = (cc, r, v) =>
          //    {
          //        var rr = SGDataHelper.Thousandth(v);
          //        if (isPercent)
          //        {
          //            rr += " %";
          //        }
          //        return rr;
          //    };
          //    SetClassName("col-r");
          //}
      }
  }
  /// <summary>
  /// 为了减少反射的消耗--benjamin20190710
  /// </summary>
  /// <param name="nullAction"></param>
  /// <returns></returns>
  public Field GetOrSetPropertyInfo(Function<Object,Field> nullAction)
  {
      if (_propertyInfo == null) { _propertyInfo = nullAction.apply(null); }
      return _propertyInfo;
  }
//  public static void SetStyleByDataType(PFGridColumn c, Class<?> dataType)
//  {
//      Boolean isPercent = dataType.equals(PFPercent.class);
//      if (dataType == typeof(BigDecimal) || dataType == typeof(int) || isPercent)
//      {
//          c.SetStyle("text-align:right;padding-right: 9px");
//          c.Render = (cc, r, v) =>
//          {
//              var rr = SGDataHelper.Thousandth(v);
//              if (isPercent)
//              {
//                  rr += " %";
//              }
//              return rr;
//          };
//          c.SetClassName("col-r");
//      }
//  }
  public static void SetStyleByDataType(PFGridColumn c, SGSqlFieldTypeEnum dataType)
  {
      Boolean isPercent =SGSqlFieldTypeEnum.Percent.equals(dataType);
      if (SGSqlFieldTypeEnum.BigDecimal.equals(dataType) 
    		  ||SGSqlFieldTypeEnum.Int.equals(dataType) || isPercent)
      {
          c.SetStyle("text-align:right;padding-right: 9px");
          c.Render = (cc, r, v) ->
          {
              String rr = SGDataHelper.Thousandth(v);
              if (isPercent)
              {
                  rr += " %";
              }
              return rr;
          };
          c.SetClassName("col-r");
      }
  }
  /// <summary>
  /// 设置列样式
  /// </summary>
  /// <param name="style"></param>
  /// <returns></returns>
  public PFGridColumn SetStyle(String style)
  {
      String[] list = style.split(";");
      for(String i : list)
      {
    	  String[] kv = i.split(":");
          if (_style.containsKey(kv[0]))
          {
              _style.remove(kv[0]);
          }
          _style.put(kv[0], kv[1]);
          if (kv[0] == "width")
          {
              _width = kv[1];
          }
      }
      return this;
  }
  /// <summary>
  /// 设置列class
  /// </summary>
  /// <param name="style"></param>
  /// <returns></returns>
  public PFGridColumn SetClassName(String className)
  {
//      var list = className.Split(new char[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
	  String[]  list = className.split(" ");
      for(String i : list)
      {
          if (_className.indexOf(i) > -1)
          {
              _className.remove(i);
          }
          _className.add(i);
      }
      return this;
  }
  public PFGridColumn SetTextAlign(TextAlign ta)
  {
      _className.remove("col-r");
      switch (ta)
      {
          case Center:
              SetStyle("text-align:center");
              break;
          case Right:
              SetStyle("text-align:right;padding-right: 9px");
              _className.add("col-r");
              break;
          case Left:
          default:
              SetStyle("text-align:left");
              break;
      }
      return this;
  }
  public enum TextAlign
  {
      Left ,//= 1,
      Center,// = 2,
      Right// = 3
  }
  private void RemoveStyle(String key)
  {
      if (_style.containsKey(key)) { _style.remove(key); }
  }
  private void SetStyle(String key, String value)
  {
      if (!_style.containsKey(key))
      {
//          _style.add(key, value);
          _style.put(key, value);
      }
      else
      {
          //_style[key] = value;
          _style.put(key, value);
      }
  }
  /// <summary>
  /// 列样式
  /// </summary>
  public String GetStyle(Boolean withAttr )
  {
	  if(withAttr==null) {withAttr=true;}
      String s = (!_style.isEmpty()) ? String.join(";",SGDataHelper.MapSelect(_style,(k,v) -> k + ":" + v)) : "";
      if (SGDataHelper.StringIsNullOrWhiteSpace(s)) { return ""; }
      return withAttr ? SGDataHelper.FormatString("style='{0} '", s) : s;
  }
  /// <summary>
  /// 列class
  /// </summary>
  public String GetClassName()
  {
      //if(this.DataIndex== "jsjf")
      //{
      //    var aa = "aa";
      //}
      return (!_className.isEmpty()) ? SGDataHelper.FormatString("class='{0} '",
          String.join(" ", _className)
          ) : "";
  }
  public PFGridColumn SetText(String text)
  {
      Text = text;
      return this;
  }
  //public void IsFirstColumn() {
  //    _isFirstColumn = true;
  //}
  //public Boolean IsFirstColumn { get{ return _isFirstColumn; }set{ _isFirstColumn = value; } }
  /// <summary>
  /// 列,行,值,返回值
  /// </summary>
  public SGFunc<PFGridColumn, Object, Object, String> Render ;
}