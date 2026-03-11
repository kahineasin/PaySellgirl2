package com.sellgirl.sgJavaMvcHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.alibaba.fastjson.JSON;

import com.sellgirl.sgJavaHelper.SGAction;
import com.sellgirl.sgJavaHelper.PFDataColumn;
import com.sellgirl.sgJavaHelper.SGFunc;
import com.sellgirl.sgJavaHelper.PFModelConfig;
import com.sellgirl.sgJavaHelper.PFModelConfigCollection;
import com.sellgirl.sgJavaHelper.SGSqlFieldTypeEnum;
import com.sellgirl.sgJavaHelper.StoreColumn;
//import com.sellgirl.sgJavaHelper.StoreColumn;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class PFGridColumnCollection extends ArrayList<PFGridColumn>
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected PFModelConfigCollection _modelConfig;
    public PFGrid Grid;
    /// <summary>
    /// 增加列
    /// </summary>
    /// <param name="text">表头文字</param>
    /// <param name="render">渲染方法 参数:列,行(当为TreeListItem时是它的Data),值(当设置了DataIndex);返回:显示值</param>
    /// <returns></returns>
    public PFGridColumn Add(String text, SGFunc<PFGridColumn, Object, Object, String> render)
    {
    	PFGridColumn col = new PFGridColumn();
    	col.SetText(text);
    	col.Render=render;
        this.add(col);
        return col;
    }
    public PFGridColumn Add(String dataIndex)
    {
    	PFGridColumn col = new PFGridColumn();
    	col.DataIndex = dataIndex;
    	col.Text = dataIndex ;
        if (_modelConfig != null)
        {
        	PFModelConfig config = _modelConfig.Get(col.DataIndex);
            if (config != null)//为了页面可以修改列宽,所以应该像现在这样Add就加上配置
            {
                col.Text = config.FieldText;

                if (!SGDataHelper.StringIsNullOrWhiteSpace(config.FieldWidth)) { col.set_width( config.FieldWidth); }
                if (!config.getVisible()) { col.set_visible(false); }

                //PFGridColumn.SetStyleByDataType(col,SGDataHelper.GetPFTypeByClass( config.FieldType));
                PFGridColumn.SetStyleByDataType(col,SGSqlFieldTypeEnum.InitByClass( config.FieldType));
                //Boolean isPercent = config.FieldType == typeof(PFPercent);
                //if (config.FieldType == typeof(decimal) || config.FieldType == typeof(int) || isPercent)
                //{
                //    col.SetStyle("text-align:right;padding-right: 9px");
                //    col.Render = (c, r,v) =>
                //    {
                //        var rr = SGDataHelper.Thousandth(v);
                //        if (isPercent)
                //        {
                //            rr += " %";
                //        }
                //        return rr;
                //    };
                //    col.SetClassName("col-r");
                //}
            }
        }
        //if (//setWidthByHeaderWord && 
        //    SGDataHelper.StringIsNullOrWhiteSpace(col.Width))//设置为中文后进入这里才有意义
        //{
        //    //var w = SGDataHelper.GetWordsWidth(col.Text,null,null,36);
        //    var w = SGDataHelper.GetWordsWidth(col.Text);
        //    if (w != null) { col.Width = w; }

        //}
        //if (!SGDataHelper.StringIsNullOrWhiteSpace(col.Width))
        //{
        //    col.Width = (decimal.Parse(col.Width.Replace("px", "")) + 36) + "px";//36是padding左右的值
        //}
        //if (Count < 1)
        //{
        //    col.IsFirstColumn=true;
        //}
        this.add(col);
        return col;
    }
    public PFGridColumn Add(String dataIndex,Field field,PFModelConfig modelConfig)
    {
    	PFGridColumn col = this.Add(dataIndex);
    	
        col._propertyInfo=field;
        if (modelConfig != null) { 
	      	//PFGridColumn.SetStyleByDataType(col,SGDataHelper.GetPFTypeByClass(modelConfig.FieldType) );
	      	PFGridColumn.SetStyleByDataType(col,SGSqlFieldTypeEnum.InitByClass(modelConfig.FieldType) );
	      	if(!SGDataHelper.StringIsNullOrWhiteSpace(modelConfig.FieldText)) {
	      		col.Text=modelConfig.FieldText;
	      	}
	  	}
        return col;
    }
//    public PFGridColumn Add(PFDataColumn dc, SGFunc<PFGridColumn, Object, Object, String> render = null)
    public PFGridColumn Add(PFDataColumn dc, SGFunc<PFGridColumn, Object, Object, String> render )
    {
    	StoreColumn sc = new StoreColumn(dc, _modelConfig.get(dc.getKey()));
    	PFGridColumn gc = new PFGridColumn(sc);
        if (render != null) { gc.Render = render; }
        this.add(gc);
        return gc;
    }
    /// <summary>
    /// 增加操作列
    /// </summary>
    /// <returns></returns>
    public PFGridColumn AddOperateColumn()
    {
    	PFGridColumn col = new PFGridColumn();
    	col.SetText("操作");
    	col.Render=(c, r, v) ->
        {
//            return SGDataHelper.FormatString(
//"<input type='button' name='btn_edit' value='编辑' onclick='$pf.fireEvent(""{0}"",""rowEdit"",[{1}])' />"+
//"<input type='button' name='btn_delete' value='删除'  onclick='$pf.fireEvent(""{0}"",""rowDelete"",[{1}])' />"+
//, Grid.GetId(), Newtonsoft.Json.JsonConvert.SerializeObject(r));
            return SGDataHelper.FormatString(
"<input type='button' name='btn_edit' value='编辑' onclick='$pf.fireEvent(\"{0}\",\"rowEdit\",[{1}])' />"+
"<input type='button' name='btn_delete' value='删除'  onclick='$pf.fireEvent(\"{0}\",\"rowDelete\",[{1}])' />"
, Grid.GetId(),JSON.toJSONString(r));
        };
        this.add(col);
        return col;
    }
    public PFGridColumn AddOperateColumn(SGAction<PFGridOperateColumnConfig,Object,Object> configSGAction)
    {
    	PFGridOperateColumnConfig config = new PFGridOperateColumnConfig();
        configSGAction.go(config,null,null);
        PFGridColumn col = new PFGridColumn();
        
        	col.Text = config.ColumnHead;
            col.Render = (c, r, v) ->
            {
                return SGDataHelper.FormatString(
"<input type='button' name='{2}' value='{3}' onclick='$pf.fireEvent(\"{0}\",\"{2}\",[{1}])' />"
, Grid.GetId(),JSON.toJSONString(r), config.ColumnName, config.ColumnText);
            };
        
        this.add(col);
        return col;
    }

    public void SetModelConfig(String name, String fullName)
    {
        _modelConfig = SGDataHelper.GetModelConfig(name, fullName);
    }
    public void SetModelConfig(String names)
    {
        _modelConfig = SGDataHelper.GetMultiModelConfig(names);
    }
}
