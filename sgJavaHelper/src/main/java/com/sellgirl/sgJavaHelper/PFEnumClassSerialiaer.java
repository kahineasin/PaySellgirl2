package com.sellgirl.sgJavaHelper;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class PFEnumClassSerialiaer extends JsonSerializer<PFEnumClass>{

	@Override
	public void serialize(PFEnumClass value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		// TODO Auto-generated method stub
		gen.writeString(value.getText());
	}
}
