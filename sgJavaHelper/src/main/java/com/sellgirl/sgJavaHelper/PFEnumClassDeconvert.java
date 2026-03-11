package com.sellgirl.sgJavaHelper;

import java.lang.reflect.Type;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

//import com.alibaba.fastjson.deserializer.JSONDeserializer;
//import com.alibaba.fastjson.serializer.ObjectDeserializer;

public class PFEnumClassDeconvert implements ObjectDeserializer {
//
//	@Override
//	public
//    void write(JSONSerializer serializer, //
//            Object object, //
//            Object fieldName, //
//            Type fieldType, //
//            int features) throws IOException{
//		
//		if(object!=null) {
//			serializer.write(((PFEnumClass)object).getText());
//		}
//	}
//
//	@Override
//	public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
//		// TODO Auto-generated method stub
//		T r= (T) PFEnumClass.EnumParse((Class)type, parser.toString());
//		return null;
//	}
//
//	@Override
//	public int getFastMatchToken() {
//		// TODO Auto-generated method stub
//		return 0;
//	}    

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
    	String json = parser.parseObject(String.class);
		//T r= (T) PFEnumClass.EnumParse((Class)type, json);
		T r= (T)PFEnumClass.EnumParse((Class<PFEnumClass>)type, json);
		return r;
//        List<Object> list = new ArrayList<Object>();
//        parser.parseArray(String.class, list);
//        if (type instanceof GenericArrayType) {
//            Type componentType = ((GenericArrayType) type).getGenericComponentType();
//            if (componentType instanceof TypeVariable) {
//                TypeVariable<?> componentVar = (TypeVariable<?>) componentType;
//                componentType = componentVar.getBounds()[0];
//            }
//
//            //List<Object> list = new ArrayList<Object>();
//            parser.parseArray(componentType, list);
//            Class<?> componentClass;
//            if (componentType instanceof Class) {
//                componentClass = (Class<?>) componentType;
//                Object[] array = (Object[]) Array.newInstance(componentClass, list.size());
//                list.toArray(array);
//                return (T) array;
//            } else {
//                return (T) list.toArray();
//            }
//
//        }
//        
//        return (T) parser.parse(fieldName);
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
