package com.sellgirl.sgJavaHelper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

//import pf.java.pfHelper.PFEnumClass;

/**
 * 使用方法:@JSONField(name = "createDate", serializeUsing = PFCalendarMonthConvert.class)  
 * 实测com.alibaba.fastjson.JSON.toString()时有效
 * @author Administrator
 *
 */
public class PFCalendarMonthConvert implements ObjectSerializer {

	@Override
	public
    void write(JSONSerializer serializer, //
            Object object, //
            Object fieldName, //
            Type fieldType, //
            int features) throws IOException{
		
		if(object!=null) {
			serializer.write(new SGDate((Calendar)object).toString(SGDataHelper.MonthFormat));
		}
	}
}
