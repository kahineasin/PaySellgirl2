package com.sellgirl.sgJavaHelper.sql;

import org.dom4j.Element;

import com.sellgirl.sgJavaHelper.IPFSqlField;
import com.sellgirl.sgJavaHelper.PFModelConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class SqlCreateTableItem extends PFModelConfig {

    public SqlCreateTableItem() { }
    public SqlCreateTableItem(PFModelConfig modelConfig) 
    { 
    	super(modelConfig);
    }
	public SqlCreateTableItem(Element fieldNode, String dataSet) {
		super(fieldNode, dataSet);
		// TODO Auto-generated constructor stub
	}
	public SqlCreateTableItem(IPFSqlField f) {
		//this.PropertyName = src.PropertyName;
		//this.DataSet = f.DataSet;
		this.FieldId =String.valueOf(f.getFieldIdx());
		this.FieldName = f.getFieldName();
		this.LowerFieldName = f.getFieldName().toLowerCase();
		this.FieldText = f.getColumnDescription();
		//this.FieldType = f.getFieldType();
		this.FieldType=SGDataHelper.GetTypeByString( f.getFieldType());
		this.Precision = f.getPrecision();
		this.FieldSqlLength = f.getFieldSqlLength();
		this.FieldDescription = f.getColumnDescription();
		//this.FieldWidth = src.FieldWidth;
		//this.setVisible(src.getVisible());
		this.Required = f.getIsRequired();
		//this.HasChinese = src.HasChinese;
		// return TransExpV2<PFModelConfig, PFModelConfig>.Trans(this);
	}
//
//	public String GetFieldNameA() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
