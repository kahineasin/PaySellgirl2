package com.sellgirl.sgJavaSpringHelper;

import org.dom4j.Element;

import com.sellgirl.sgJavaHelper.PFModelConfig;

public class SqlCreateTableItem extends PFModelConfig{

    public SqlCreateTableItem() { }
    public SqlCreateTableItem(PFModelConfig modelConfig) 
    { 
    	super(modelConfig);
    }
	public SqlCreateTableItem(Element fieldNode, String dataSet) {
		super(fieldNode, dataSet);
		// TODO Auto-generated constructor stub
	}
//
//	public String GetFieldNameA() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
