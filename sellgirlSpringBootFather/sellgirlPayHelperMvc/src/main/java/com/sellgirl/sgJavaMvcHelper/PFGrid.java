package com.sellgirl.sgJavaMvcHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sellgirl.sgJavaHelper.*;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.ISGConfigMapper;

/// <summary>
/// 表格组件--wxj
/// </summary>
public class PFGrid extends PFComponent
{
    private List<PFGridColumn> _columns = new ArrayList<PFGridColumn>();
    //private String _title;
    private List<?> _model;
    private PFGridMultiHeader _header;
    private String _itemClass = "datagriditem";
    private String _headClass = "datagridhead";
    private SelectMode _selectMode=SelectMode.None;
    private Boolean _closeTree = false;
    /// <summary>
    /// 支持树型List<TreeListItem>
    /// </summary>
    /// <param name="model"></param>
    public void SetModel(List<?> model)
    {
        _model = model;
    }
    /// <summary>
    /// DataTable不支持树型
    /// </summary>
    /// <param name="model"></param>
    public void SetModel(SGDataTable model)
    {
        List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < model.Rows.size(); i++)
        {
        	PFDataRow row = model.Rows.get(i);
        	List<PFDataColumn> arr = row.getCol();
            HashMap<String, Object> r = new HashMap<String, Object>();
            for (int j = 0; j < arr.size(); j++)
            {
                //var c = model.Columns[j].ColumnName;
                //r.Add(c, arr[j]);
            	PFDataColumn c = model.Columns.get(j);
                r.put(c.getKey(), c.getValue());
            }
            result.add(r);
        }
        _model = result;
    }
    public void SetSelectMode(SelectMode selectMode)
    {
        _selectMode = selectMode;
    }
    public SelectMode GetSelectMode()
    {
        return _selectMode;
    }
//    public void SetTitle(String title)
//    {
//        _title = title;
//    }

    public PFGridMultiHeader MultiHeader()
    {
        return _header = new PFGridMultiHeader();
    }
    public String GetHeadClass()
    {
        return _headClass;
    }
    public void CloseTree()
    {
        _closeTree = true;
    }

    public <TModel>  void ColumnsFor(SGAction<PFGridColumnCollectionT<TModel>,Object,Object> action)
    {
    	PFGridColumnCollectionT<TModel> cs = new PFGridColumnCollectionT<TModel>();
    	cs.Grid=this;
        action.go(cs,null,null);
        //cs.ForEach(_columns.add);
        for(PFGridColumn i : cs) {
        	_columns.add(i);
        }
    }
    public void ColumnsFor(String modelConfigName, SGAction<PFGridColumnCollection,Object,Object> action)
    {
        //var cs = new PFGridColumnCollection { Grid = this };
    	PFGridColumnCollection cs = new PFGridColumnCollection();
    	cs.Grid=this;
        if (!SGDataHelper.StringIsNullOrWhiteSpace(modelConfigName)) { cs.SetModelConfig(modelConfigName); }
        action.go(cs,null,null);
        //cs.ForEach(_columns.Add);
        for(PFGridColumn i : cs) {
        	_columns.add(i);
        }
    }
    public  void ColumnsForAll(String modelConfigName)
    {
        //var cs = new PFGridColumnCollection { Grid = this };
    	PFGridColumnCollection cs = new PFGridColumnCollection();
    	cs.Grid=this;
        if (!SGDataHelper.StringIsNullOrWhiteSpace(modelConfigName)) { cs.SetModelConfig(modelConfigName); }

        if (_model != null && _model.size() > 0)
        {
            if (_model.get(0) instanceof ITreeListItem)
            {
            	Object data = ((ITreeListItem)_model.get(0)).GetData();
                if (null == data)
                {
                    data =_model.get(0);
                }
                SGDataHelper.EachObjectProperty(data, (i, name, value) ->
                {
                    ////旧版只有一句?待验证--benjamin todo 20191014
                    //cs.Add(name);

                	PFGridColumn c = cs.Add(name);
//                    if (value != null) { PFGridColumn.SetStyleByDataType(c,SGDataHelper.GetPFTypeByClass(value.getClass()) ); }
                    if (value != null) { PFGridColumn.SetStyleByDataType(c,SGSqlFieldTypeEnum.InitByClass(value.getClass()) ); }
                });
            }
            else if (_model.get(0) instanceof Map)//新版才有这段代码?待验证--benjamin todo 20191014
            {
            	//Map<String, Object> dict = (Map<String, Object>)_model.get(0);
            	Map<String, Object> dict =SGDataHelper.<Map<String, Object>>ObjectAs(_model.get(0)) ;
         	   Iterator<Entry<String, Object>> iter = dict.entrySet().iterator();
        	   while(iter.hasNext()){
        		   Entry<String, Object> key=iter.next();
        		   PFGridColumn c = cs.Add(key.getKey());
                   Object value = key.getValue();
                   //if (value != null) { PFGridColumn.SetStyleByDataType(c, SGDataHelper.GetPFTypeByClass(value.getClass())); }
                   if (value != null) { PFGridColumn.SetStyleByDataType(c, SGSqlFieldTypeEnum.InitByClass(value.getClass())); }
        	   }
//                foreach (var key in dict.Keys)
//                {
//                    var c = cs.Add(key);
//                    var value = dict[key];
//                    if (value != null) { PFGridColumn.SetStyleByDataType(c, value.GetType()); }
//                }
            }else {
//                SGDataHelper.EachObjectProperty(_model.get(0), (i, name, value) ->
//                {
//                	PFGridColumn c = cs.Add(name);
//                    if (value != null) { PFGridColumn.SetStyleByDataType(c,SGDataHelper.GetPFTypeByClass(value.getClass()) ); }
//                });
                SGDataHelper.EachObjectPropertyType(_model.get(0), (name, field, modelConfig) ->
                {
                	PFGridColumn c = cs.Add(name);
                	c._propertyInfo=field;
                    if (modelConfig != null) { 
                    	//PFGridColumn.SetStyleByDataType(c,SGDataHelper.GetPFTypeByClass(modelConfig.FieldType) );
                    	PFGridColumn.SetStyleByDataType(c,SGSqlFieldTypeEnum.InitByClass(modelConfig.FieldType) );
                    	if(!SGDataHelper.StringIsNullOrWhiteSpace(modelConfig.FieldText)) {
                        	c.Text=modelConfig.FieldText;
                    	}
                	}
                });
            }
        }
        cs.forEach(a->_columns.add(a));
    }
    public  void Columns(List<StoreColumn> columns) {
    	Columns(columns,true);
    }
    /// <summary>
    /// 生成header和columns
    /// </summary>
    /// <param name="columns"></param>
    /// <param name="generateColumn">是否生成column</param>
    public  void Columns(List<StoreColumn> columns, Boolean generateColumn)//,String modelConfigName=null)
    {
        //var modelConfig= SGDataHelper.GetMultiModelConfig(modelConfigName);
    	PFGridMultiHeader header = MultiHeader();

        for(StoreColumn i :columns) {
            AppendHeader(header,i, generateColumn);
        }
//        columns.ForEach(a =>
//        {
//            AppendHeader(header, a, generateColumn);
//            //var c = new PFGridColumn(a);
//            //if (a.Children.Any())
//            //{
//            //    var h = new PFGridMultiHeader(a.title);
//            //    h.AddChildren(a.Children.Select(b => new PFGridColumn (b) ).ToArray());
//            //    header.AddChildren(h);
//            //}else
//            //{
//            //    _columns.Add(c);
//            //    header.AddChildren(c);
//            //}
//        });
    }
    public  void ClearColumns()
    {
        _columns.clear();
    }
    public  void SetColumn(String dataIndex,SGAction<PFGridColumn,Object,Object> action)
    {
    	//var c = SGDataHelper.ListFirstOrDefault(_columns, a -> a.DataIndex.equals(dataIndex));
    	PFGridColumn c = SGDataHelper.ListFirstOrDefault(_columns,a -> a.DataIndex.equals(dataIndex));
        if (c != null) { action.go(c,null,null); }            
    }
//    private void AppendHeader(PFGridMultiHeader p, StoreColumn a) {
//    	AppendHeader(p,a,true);
//    }
    private void AppendHeader(PFGridMultiHeader p, StoreColumn a, Boolean generateColumn )
    {
        if (!a.GetChildren().isEmpty())
        {
        	PFGridMultiHeader h = new PFGridMultiHeader(a.title);
            a.GetChildren().forEach(b ->
            {
                AppendHeader(h, b, generateColumn);
            });
            //h.AddChildren(a.Children.Select(b => new PFGridColumn(b)).ToArray());
            p.AddChildren(h);
        }
        else
        {
        	PFGridColumn c = new PFGridColumn(a);
            if (generateColumn)
            {
                _columns.add(c);
            }
            p.AddChildren(c);
        }
        //cs.ForEach(a => {
        //    var c = new PFGridColumn(a);
        //    p..Add(c);
        //    if (a.Children.Any())
        //    {
        //        AppendChildColumn(c, a.Children);
        //    }
        //});
    }
    private Object GetCellText(Object row, PFGridColumn c)
    {
        Object val = null;
        if (!SGDataHelper.StringIsNullOrWhiteSpace(c.DataIndex))
        {
        	try {
                if (row instanceof PFDataRow)
                {
                    val = ((PFDataRow)row).getColumn(c.DataIndex);
                }
                else if (row instanceof Map)
                {
                    //val = ((Map<String,Object>)row).get(c.DataIndex);
                    val =SGDataHelper.<Map<String,Object>>ObjectAs(row).get(c.DataIndex);
                }
                else
                {
                    //var pi = row.GetType().GetProperty(c.DataIndex);
                	Field pi=null;
                	if(c._propertyInfo!=null) {
                		pi=c._propertyInfo;
                	}else {//else可能没用了，因为生产columns在先，应该有值。考虑删除else 。现时有用，除非把这部分移到生成列的时候执行
                        pi = c.GetOrSetPropertyInfo((a) -> {
    						try {
    							return row.getClass().getDeclaredField(c.DataIndex);
    						} catch (NoSuchFieldException | SecurityException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
    						return null;
    					});
                        if(pi!=null) {
                            pi.setAccessible(true);
                            //其实在这里设置text已经太迟了，因为此时header都生成完了
//                            PFSqlFieldAttribute fieldType=pi.getAnnotation(PFSqlFieldAttribute.class);
//                            if(fieldType!=null) {
//                                c.SetText(fieldType.FieldText());                    
//                            }
                            
//                            if(fieldType!=null&&c._modelConfig==null) {
//                                c._modelConfig=new PFModelConfig();
//                                c._modelConfig.FieldText=fieldType.FieldText();
//                                if(!SGDataHelper.StringIsNullOrWhiteSpace(fieldType.FieldDescription())){
//                                    c._modelConfig.FieldDescription=fieldType.FieldDescription();
//                                }
//                            }
                        }
                	}
                    if(pi!=null) {
                        val = pi.get(row);
                    }
                }
        	}catch(Exception e) {
        		//String aa="aa";
        	}
        }
        if (c.Render != null)
        {
            val = c.Render.go(c, row, val);
        }

        return val;
    }
    private String RowToHtml(Object row)
    {
    	StringBuilder sb = new StringBuilder();
        sb.append(SGDataHelper.FormatString("<tr class='{0}' >", _itemClass));
        _columns.forEach(c ->
        {
            Object val;
            val = GetCellText(row, c);
            //sb.append(SGDataHelper.FormatString("<td {1}>{0}</td>", val, c.GetStyle());

            //这个是新版?待验证--benjamin todo
            String style = "";
            if (c.get_visible() == false) { style = " style='display:none' "; }//好像有col-h的class代表隐藏--benjamin20191014
            sb.append(SGDataHelper.FormatString("<td {1} {2}>{0}</td>", val, c.GetClassName(), style));//样式加到表头
            //yjquery2018项目中是这句,应该是旧版?
            //sb.append(SGDataHelper.FormatString("<td >{0}</td>", val);//样式加到表头

        });
        sb.append("</tr>");
        return sb.toString();
    }
    private String GetClassByTreeMatrixNetLine(TreeMatrixNet net)
    {
        if (net.HasFlag(TreeMatrixNet.Up) && net.HasFlag(TreeMatrixNet.Right) && net.HasFlag(TreeMatrixNet.Down))
        {
            //return "tree-tr-linearea-urd";
            return "linearea-urd";
        }
        if (net.HasFlag(TreeMatrixNet.Up) && net.HasFlag(TreeMatrixNet.Down))
        {
            //return "tree-tr-linearea-ud";
            return "linearea-ud";
        }
        if (net.HasFlag(TreeMatrixNet.Up) && net.HasFlag(TreeMatrixNet.Right))
        {
            //return "tree-tr-linearea-ur";
            return "linearea-ur";
        }
        return "";
    }
    private String TreeCellToHtml(ITreeListItem row, int rowIdx, int level, PFGridColumn column, TreeMatrix matrix, Boolean isFirstColumn)
    {
    	StringBuilder sb = new StringBuilder();
        Object val = GetCellText(row.GetData(), column);

        if (isFirstColumn)
        {
            //var css = "tree-tr-hitarea tree-tr-hitarea-expanded";
            String css = _closeTree ? "hitarea hitarea-closed" : "hitarea hitarea-expanded";
            String line = "";
            for (int j = 0; j < level; j++)
            {
                //line += SGDataHelper.FormatString("<div class='{0} {1}'></div>", "tree-tr-linearea ", GetClassByTreeMatrixNetLine(matrix.GetNetLine(j, rowIdx)));
                line += SGDataHelper.FormatString("<div class='{0} {1}'></div>", "linearea ", GetClassByTreeMatrixNetLine(matrix.GetNetLine(j, rowIdx)));
            }
            val = line + SGDataHelper.FormatString("<div class='{0}'></div>", css) + val;

            //前面的GetCellText已经有调用render了，这里就不用了--benjamin20190705
            ////sb.append(SGDataHelper.FormatString("<td {1}  onclick='$pf.expandTree(this)'>{0}</td>", val, column.GetStyle());
            //if (column.Render != null)
            //{
            //    sb.append(SGDataHelper.FormatString("<td>{0}</td>", column.Render(column, row, (String)val)) ;
            //}
            //else
            //{
            sb.append(SGDataHelper.FormatString("<td>{0}</td>", val));//样式不要加到tbody上,否则页面太大了,onclick事件改到pfTreeTable的init上
            //}

        }
        else
        {
            //if (column.Render != null)
            //{
            //    //sb.append(SGDataHelper.FormatString("<td>{0}</td>", column.Render(column, row, (String)val));//样式不要加到tbody上,否则页面太大了
            //    sb.append(SGDataHelper.FormatString("<td {1}>{0}</td>", column.Render(column, row, (String)val), column.GetClassName());//样式不要加到tbody上,否则页面太大了,加上类名的页面大小多了1/23,但为了数字列右对齐
            //}
            //else
            //{
            //sb.append(SGDataHelper.FormatString("<td {1}>{0}</td>", val, column.GetStyle());
            sb.append(SGDataHelper.FormatString("<td {1}>{0}</td>", val, column.GetClassName()));//样式不要加到tbody上,否则页面太大了
            //}
        }
        return sb.toString();
    }
    public String TreeRowToHtml(ITreeListItem row, int rowIdx, int depth, TreeMatrix matrix)//, int level = 0)
    {
    	StringBuilder sb = new StringBuilder();
        int level = depth - 1;
        //sb.append(SGDataHelper.FormatString("<tr class='{0}' expanded='expanded' level='{1}' >", _itemClass, level);
        sb.append(SGDataHelper.FormatString("<tr class='{0}' level='{1}' {2} {3}>",
            _itemClass,
            level,
            _closeTree ? "" : "expanded='expanded'",
            _closeTree && level > 0 ? "style='display:none'" : ""));

        //选择列
        String selectTh = "";
        //var depth = GetDepth() - 1;
        //var selectThRowSpan = depth > 1 ? ("rowspan=" + _rowSpan) : "";
        switch (GetSelectMode())
        {
            case Single:
            case Multi:
                selectTh = "<td><input type=\"checkbox\" class=\"pf-row-select\"></td>";
                break;
            default:
                break;
        }
        sb.append(selectTh);

        //暂时固定第一列为树型列--
        int i = 0;
        if (_header != null)
        {
        	int[] iArr=new int[] {i};
            _header.EachLeaf(h ->
            {
                PFGridColumn column = SGDataHelper.ListFirst(_columns,c -> c.Text == h.getText());
                sb.append(TreeCellToHtml(row, rowIdx, level, column, matrix, (_header.FirstLeaf(a -> (a).getVisible())).getText().equals(column.Text)));
                if (h.getVisible()) { iArr[0]++; }
            });
            i=iArr[0];
        }
        else
        {
            _columns.forEach(column ->
            {
                sb.append(TreeCellToHtml(row, rowIdx, level, column, matrix, column == _columns.get(0)));
            });
        }
        sb.append("</tr>");

        return sb.toString();
    }

    //#region For DataTable Or IList
    /// <summary>
    /// 注意:Model为DataTable时没必要写自动生成列的方法,因为在PFGrid.ColumnsFor方法里循环Table生成也是很简单的
    /// </summary>
    /// <param name="htmlHelper"></param>
    /// <returns></returns>
    public String Html(HtmlHelper htmlHelper)
    {

    	StringBuilder sb = new StringBuilder();
        ////特性
    	Map<String, Object> htmlAttributes = GetHtmlAttributes();
    	String sAttributes = "";
 	   Iterator<Entry<String, Object>> iter = htmlAttributes.entrySet().iterator();
 	   while(iter.hasNext()){
 		   Entry<String, Object> key=iter.next();
           sAttributes += SGDataHelper.FormatString(" {0}='{1}' ", key.getKey(), key.getValue());
 	   }
//        foreach (var a in htmlAttributes)
//        {
//            sAttributes += SGDataHelper.FormatString(" {0}='{1}' ", a.Key, a.Value);
//        }

 	  ISGConfigMapper configMapper = SGDataHelper.GetConfigMapper();
 	 SGPathConfig pathConfig = configMapper.GetPathConfig();


        if (htmlHelper.ViewData.get("hasPFGridCss") == null)
        {
////            sb.append(SGDataHelper.FormatString("<link href=\"/{0}/PFGrid.css\" rel=\"stylesheet\" />", pathConfig.getCssPath()));
//            sb.append(SGDataHelper.FormatString("<link rel=\"stylesheet\" th:href=\"@{/{0}/PFGrid.css}\" />", pathConfig.getCssPath()));//not ok 注意这里不能用thymeleaf写法
//            sb.append(SGDataHelper.FormatString("<link rel=\"stylesheet\" href=\"/{0}/PFGrid.css\" />", pathConfig.getCssPath()));//ok
            sb.append(SGDataHelper.FormatString("<link rel=\"stylesheet\" href=\"{0}/PFGrid/PFGrid.css\" />", pathConfig.getCssPath()));//去掉前面的斜线,便于使用网络图
//            sb.append(SGDataHelper.FormatString("<link rel=\"stylesheet\" href=\"/css/login.css\" />", pathConfig.getCssPath())); //ok
            
        	//想在模版遍历CssPath得到head,但这种方式不行，因为head比body的grid先执行
        	//htmlHelper.CssPath.add(SGDataHelper.FormatString("/{0}/PFGrid.css", pathConfig.getCssPath()));
            
        	htmlHelper.ViewData.put("hasPFGridCss",true);
        }
        //#region 标题

        //#endregion 标题

        //#region 主体table

        sb.append(SGDataHelper.FormatString("<table {0}>", sAttributes));

        Boolean hasData = _model != null && _model.size() > 0;
        //#region 表头
        if (hasData)
        {
            if (_header != null)
            {
                sb.append(_header.Html(this));
            }
            else
            {
                sb.append(SGDataHelper.FormatString("<thead><tr class='{0}'>", _headClass));
                _columns.forEach(c ->
                {
                    String style = c.GetStyle(false);
                    if (!SGDataHelper.StringIsNullOrWhiteSpace(c.get_width()))
                    {
                        style += ";min-width:" + c.get_width();
                    }
                    if (!SGDataHelper.StringIsNullOrWhiteSpace(style))
                    {
                        style = SGDataHelper.FormatString("style='{0} '", style);
                    }
                    sb.append(SGDataHelper.FormatString("<th {1} field-name='{2}' >{0}</th>", c.Text, style, c.DataIndex));
                    //var style = "";
                    //if (!c.Visible) { style += "display:none;"; }
                    //if (c.Width != null) { style += "width:" + c.Width + ";"; }
                    //if (!SGDataHelper.StringIsNullOrWhiteSpace(style)) { style = SGDataHelper.FormatString("style='{0}'", style); }
                    //sb.append(SGDataHelper.FormatString("<th {1}  >{0}</th>", c.Text, style);

                });
                sb.append("</tr></thead>");
            }
        }
        //#endregion 表头
        sb.append("<tbody>");
        if (hasData)
        {
            //#region 行内容
            int i = 0;
            Boolean isTree = false;
            for( i=0;i<_model.size();i++) {
            	Object row=_model.get(i);
            	if(row instanceof ITreeListItem) {
                    isTree = true;
                    if (!_columns.isEmpty())
                    {
                        String style = "text-align:left;padding-left:5px;padding-right:5px;white-space:nowrap;";
                        if (_header != null)
                        {
                        	
                           SGDataHelper.ListFirst(_columns,c ->{
                        	   Boolean b=c.Text.equals(_header.FirstLeaf(a -> true).getText());
                        	   return b;
                           }).SetStyle(style);
                        }
                        else
                        {
                        	SGDataHelper.ListFirst(_columns,a -> a.get_visible()).SetStyle(style);
                        }
                    }
                    break;
            	}
            }
            i = 0;

            if (isTree)
            {
//            	TreeMatrix matrix = new TreeMatrix(_model);
            	TreeMatrix matrix = new TreeMatrix(SGDataHelper.ObjectAs(_model) );
                for(int j=0;j<_model.size();j++) {

                	Object row=_model.get(j);
                	if(row instanceof ITreeListItem) {
                    	ITreeListItem tRow =(ITreeListItem) row;
                        sb.append(TreeRowToHtml(tRow, i, 1, matrix));
                        i++;
                        int[] iArr=new int[] {i};
                        tRow.EachChild((a, b) ->
                        {
                            //sb.append(TreeRowToHtml(tRow, i, b, matrix));
                            sb.append(TreeRowToHtml(a, iArr[0], b, matrix));
                            iArr[0]++;
                        });
                        i=iArr[0];
                	}
                }
//                foreach (var row in _model)
//                {
//                    var tRow = row as TreeListItem;
//                    sb.append(TreeRowToHtml(tRow, i, 1, matrix));
//                    i++;
//                    tRow.EachChild((a, b) =>
//                    {
//                        //sb.append(TreeRowToHtml(tRow, i, b, matrix));
//                        sb.append(TreeRowToHtml(a, i, b, matrix));
//                        i++;
//                    });
//                }
            }
            else
            {
                for(int j=0;j<_model.size();j++) {
                    sb.append(RowToHtml(_model.get(j)));
                }
//                foreach (var row in _model)
//                {
//                    sb.append(RowToHtml(row));
//                }
            }

            //#endregion 行内容

        }
        else
        {
            sb.append(SGDataHelper.FormatString("<tr><td>{0}</td></tr>", "无相关数据"));
            //sb.append(SGDataHelper.FormatString("<table style='width: 100%;'><tr class='{0}'><td style='text-align:center;color:red'>无相关数据</td></tr></table>", _itemClass);
        }
        sb.append("</tbody>");
        sb.append("</table>");

        //#endregion 主体table


        //return MvcHtmlString.Create(sb.ToString());
        return sb.toString();
    }
    //#endregion
    public enum SelectMode
    {
        None ,//= 0,
        Single,// = 1,
        Multi// = 2
    }
}
