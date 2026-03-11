package com.sellgirl.sgJavaHelper;

import java.io.IOException;
import java.lang.reflect.Type;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

public class PFEnumClassConvert implements ObjectSerializer {

	@Override
	public
    void write(JSONSerializer serializer, //
            Object object, //
            Object fieldName, //
            Type fieldType, //
            int features) throws IOException{
		
		if(object!=null) {
			serializer.write(((PFEnumClass)object).getText());
		}
	}
}
