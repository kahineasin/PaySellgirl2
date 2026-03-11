package com.sellgirl.sgJavaHelper;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class SGDateSerialiaer extends JsonSerializer<SGDate>{

	@Override
	public void serialize(SGDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		// TODO Auto-generated method stub
		gen.writeString(value.toString());
	}
}
