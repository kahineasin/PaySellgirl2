package com.sellgirl.sgJavaHelper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

//import pf.java.pfHelper.PFEnumClass;

public class PFCalendarConvert implements ObjectSerializer {

	@Override
	public
    void write(JSONSerializer serializer, //
            Object object, //
            Object fieldName, //
            Type fieldType, //
            int features) throws IOException{
		
		if(object!=null) {
			serializer.write(new SGDate((Calendar)object).toString());
		}
	}
}
