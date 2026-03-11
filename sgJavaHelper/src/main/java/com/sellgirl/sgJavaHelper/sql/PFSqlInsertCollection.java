package com.sellgirl.sgJavaHelper.sql;

import java.util.HashMap;
import java.util.Iterator;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class PFSqlInsertCollection extends BaseSqlUpdateCollection {

//	@Override
//	protected PFSqlType sqlType() {
//		return PFSqlType.MySql;
//	}
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -3924459189626116895L;
	//private IList<String> _updateFields;
    public PFSqlInsertCollection()
    {
    	super();
    }
    public PFSqlInsertCollection(Object model,  String... names
        )
    {
    	super(model, names);
    }

    /// <summary>
    /// 格式如:name1,name2,...
    /// </summary>
    /// <returns></returns>
    public String ToKeysSql()
    {
        int count = 0;
        String s1 = "";
        
 	   Iterator<HashMap.Entry<String, SqlUpdateItem>> iter = this.entrySet().iterator();
 	   while(iter.hasNext()){
 		  HashMap.Entry<String, SqlUpdateItem> it=iter.next();
 		  String key =SGDataHelper.FormatString("{0}{1}{2}", GetFieldQuotationCharacterL(), it.getKey(), GetFieldQuotationCharacterR());
           s1 += count == 0 ? key : ("," + key);
           count++;
 	   }

        s1 += "";
        return s1;
    }
    /// <summary>
    /// 格式如:'value1','value2',time1,int1
    /// </summary>
    /// <returns></returns>
    public String ToValuesSql()
    {
        int count = 0;
        String s2 = "";
  	   Iterator<HashMap.Entry<String, SqlUpdateItem>> iter = this.entrySet().iterator();
  	   while(iter.hasNext()){
  		 HashMap.Entry<String, SqlUpdateItem> it=iter.next();
           s2 += count == 0 ? "" : ",";

           SqlUpdateItem v = it.getValue();
           //s2 += GetFormatValue(v.Value, v.VType);
           //s2 += GetFormatValue(v.Value, v.VType,v.VPFType);
           s2 += GetFormatValue(v);
           count++;
  	   }
        s2 += "";
        return s2;
    }
}
