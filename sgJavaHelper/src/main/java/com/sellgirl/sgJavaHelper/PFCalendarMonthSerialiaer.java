package com.sellgirl.sgJavaHelper;

import java.io.IOException;
import java.util.Calendar;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class PFCalendarMonthSerialiaer extends JsonSerializer<Calendar>{

	@Override
	public void serialize(Calendar value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		// TODO Auto-generated method stub
		gen.writeString((new SGDate((Calendar)value)).toString(SGDataHelper.MonthFormat));
	}
}
