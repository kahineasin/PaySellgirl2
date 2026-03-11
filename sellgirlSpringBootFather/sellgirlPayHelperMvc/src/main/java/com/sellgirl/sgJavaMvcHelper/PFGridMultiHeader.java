package com.sellgirl.sgJavaMvcHelper;

import java.util.ArrayList;
import java.util.List;

import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.TreeListItemT;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
//import com.sellgirl.sgJavaHelper.mvc.PFGrid;
//import com.sellgirl.sgJavaHelper.mvc.PFGridColumn;
//import com.sellgirl.sgJavaHelper.mvc.PFGridMultiHeader;
//import com.sellgirl.sgJavaHelper.mvc.params;
//import com.sellgirl.sgJavaHelper.mvc.ref;
//import com.sellgirl.sgJavaHelper.mvc.region;
//import com.sellgirl.sgJavaHelper.mvc.var;

/// <summary>
/// 多表头
/// </summary>
public class PFGridMultiHeader extends TreeListItemT<PFGridMultiHeader>
{
  //#region Field
  private String _text;
  private Integer _columnSpan;
  private Integer _rowSpan;
  private Boolean _visible = true;
  public String getText() {
	return _text;
}

public void setText(String _text) {
	this._text = _text;
}

public Boolean getVisible() {
	return _visible;
}

public void setVisible(Boolean _visible) {
	this._visible = _visible;
}

private String _width;
  private String _dataIndex;
  //private Boolean _isFirstColumn;
  //#endregion

 // #region Property

  //public Boolean Visible { get { return _visible; } private set { _visible = value; } }
  //public String Text { get { return _text; } set { _text = value; } }
  //#endregion

  //#region Ctor
  public PFGridMultiHeader() { }

  /// <summary>
  ///     初始化,同时给表头赋值
  /// </summary>
  /// <param name="text">表头的文字</param>
  public PFGridMultiHeader(String text)
      
  {
	  this();
      _text = text;
  }
  public PFGridMultiHeader(PFGridColumn c)
  {
	  this();
      _text = c.Text;
      //Visible = c.Visible; 
      setVisible(c.get_visible());
      _width = c.get_width(); _dataIndex = c.DataIndex;
      // _isFirstColumn = c.IsFirstColumn;
  }
  //#endregion

  /// <summary>
  ///     增加子元素
  /// </summary>
  /// <param name="childrens">子元素,支持多个</param>
  /// <returns></returns>
  public PFGridMultiHeader AddChildren( PFGridMultiHeader... childrens)
  {
      if (childrens != null)
      {
          for (PFGridMultiHeader c : childrens)
          {
             // Children.Add(c);
        	  //_children.add(c);
              Children.add(c);
          }
      }
      return this;
  }

  /// <summary>
  ///     增加子元素
  /// </summary>
  /// <param name="childrens">子元素,支持多个,要求TableCell类型</param>
  /// <returns></returns>
  public PFGridMultiHeader AddChildren( PFGridColumn... childrens)
  {
      if (childrens != null)
      {
          for (PFGridColumn c : childrens)
          {
//              //Children.Add(new PFGridMultiHeader(c.Text) { Visible = c.Visible , _width =c.Width, _isFirstColumn=c.IsFirstColumn });
//              Children.Add(new PFGridMultiHeader(c));
        	  //_children.add(new PFGridMultiHeader(c));
        	  Children.add(new PFGridMultiHeader(c));
          }
      }
      return this;
  }

  /// <summary>
  ///     渲染到writer
  /// </summary>
  /// <param name="writer"></param>
  /// <param name="style">表头样式</param>
  public String Html(PFGrid grid)
  {
      //if (_children == null) { return ""; }
      if (Children == null) { return ""; }

      String selectTh = "";
      int depth = GetDepth() - 1;
      String selectThRowSpan = depth > 1 ? ("rowspan=" + depth) : "";
      switch (grid.GetSelectMode())
      {
          case Single:
              selectTh = SGDataHelper.FormatString("<th {0}>选择</th>", selectThRowSpan);
              break;
          case Multi:
              selectTh = SGDataHelper.FormatString("<th {0}><input type=\"checkbox\" class=\"pf-row-select-all\">选择</th>", selectThRowSpan);
              break;
          default:
              break;
      }
      String html = SGDataHelper.FormatString("<thead><tr  class='{0}' >{1}",
          grid.GetHeadClass(),
          selectTh
          );
      SGRef<String> htmlRef=new SGRef<String>(html); 
      RenderListToWriter(htmlRef, grid,this.GetChildren(), depth,null);//第一层的header是不输出的,所以要减1
      html += "</tr></thead>";
      return html;
  }

  //#region Private
  /// <summary>
  ///     渲染List到writer
  /// </summary>
  /// <param name="writer"></param>
  /// <param name="lists">表头同一层的List</param>
  /// <param name="style">表头样式</param>
  /// <param name="maxDepth">最大深度</param>
  /// <param name="currentDepth">当前深度</param>
  private void RenderListToWriter(SGRef<String> writer, PFGrid grid, List<PFGridMultiHeader> lists, int maxDepth, Integer currentDepth )
  {
	  if(currentDepth==null) {
		  currentDepth=1;
	  }
      //因为header内部是td,可以认为首尾已经各有一个tr
      if (lists != null)
      {
          //显示下一层
    	  ArrayList<PFGridMultiHeader> next = new ArrayList<PFGridMultiHeader>();
          //显示头
          for (PFGridMultiHeader i : lists)
          {
              i.RenderHeaderToWrite( writer, grid, maxDepth, currentDepth);

              if (i.GetChildren()!= null) next.addAll(i.GetChildren());//为了免于遍历两次,放到这里--wxj20180713
          }
          ////显示下一层
          //var next = new List<PFGridMultiHeader>();
          //foreach (var i in lists)
          //{
          //    if (i.Children != null) next.AddRange(i.Children);
          //}
          //if (next.Count > 0)
          if (!next.isEmpty())
          {
        	  writer.SetValue(writer.GetValue()+"</tr>"+SGDataHelper.FormatString("<tr class='{0}'>", grid.GetHeadClass()));
              //writer += "</tr>";
              //writer += SGDataHelper.FormatString("<tr class='{0}'>", grid.GetHeadClass());
              RenderListToWriter( writer, grid, next, maxDepth, ++currentDepth);
          }
      }
  }



  /// <summary>
  ///     渲染表头td到writer
  /// </summary>
  /// <param name="writer"></param>
  /// <param name="maxDepth">最大深度</param>
  /// <param name="currentDepth">当前深度</param>
  private void RenderHeaderToWrite(SGRef<String>  writer, PFGrid grid, int maxDepth, int currentDepth)
  {
      if (SGDataHelper.StringIsNullOrWhiteSpace(_text)) return;
      //if (Children != null && Children.Count > 1)
      if (GetChildren() != null && GetChildren().size() > 0)//大于0才是对的--wxj20180906
      {
          _columnSpan = GetAllLeafCount(a -> a.getVisible()); //GetAllChildrenCount();//
      }
      else if (maxDepth > currentDepth)
      {
          _rowSpan = maxDepth - currentDepth + 1;
      }
      ArrayList<String> styles = new ArrayList<String>();
      if (!getVisible()) { styles.add("display:none"); }
      if (//(!_isFirstColumn)&&
          (!SGDataHelper.StringIsNullOrWhiteSpace(_width)))
      {
          styles.add("width:" + _width);
          styles.add("min-width:" + _width);//th的width是不会撑开table的,但min-width可以
      };
      //if (!String.IsNullOrWhiteSpace(_width)) { styles.Add("width:"+_width); }//现在的树组件,如果只有一列不设置宽度时,那列的样式就会很长,另外,树型列的宽度也不应该设置.所以暂时让TreeGrid自己控制列宽吧
      String style = (!styles.isEmpty()) ? SGDataHelper.FormatString("style ='{0}' ", String.join(";", styles)) : "";
      String dataIndex = SGDataHelper.StringIsNullOrWhiteSpace(_dataIndex) ? "" : ("field-name=" + _dataIndex);
      writer.SetValue(writer.GetValue()+ SGDataHelper.FormatString("<th {1} {2} {3} {4} >{0}</th>", _text, _columnSpan > 1 ? ("colspan=" + _columnSpan) : "", _rowSpan > 1 ? ("rowspan=" + _rowSpan) : "", style, dataIndex));
//      writer += SGDataHelper.FormatString("<th {1} {2} {3} {4} >{0}</th>", _text, _columnSpan > 1 ? ("colspan=" + _columnSpan) : "", _rowSpan > 1 ? ("rowspan=" + _rowSpan) : "", style, dataIndex);
  }


  //#endregion
}