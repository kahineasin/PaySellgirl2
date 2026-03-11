////package com.sellgirl.pfHelperNotSpring;
////
////import java.io.IOException;
////import com.fasterxml.jackson.core.JsonParser;
////import com.fasterxml.jackson.core.JsonProcessingException;
////import com.fasterxml.jackson.databind.DeserializationContext;
////import com.fasterxml.jackson.databind.JsonDeserializer;
////
////
/////**
//// * http://www.voidcn.com/article/p-unpkalpk-bye.html
//// * @author Administrator
//// *
//// */
////public class PFDateDeserialiaer  extends JsonDeserializer<PFDate>{
////    private Class<?> targetClass;
////
////    public PFDateDeserialiaer() {
////    }
////
//////
//////	@Override
//////	public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//////		// TODO Auto-generated method stub
//////		gen.writeString(value.getText());
//////	}
////
////    public PFDateDeserialiaer(Class<?> targetClass) {
////        this.targetClass = targetClass;
////    }
////
////	@Override
////	public PFDate deserialize(JsonParser p, DeserializationContext ctxt)
////			throws IOException, JsonProcessingException {
////
////		PFEnumClass clazz=null;
////		try {
////			clazz = (PFEnumClass)targetClass.newInstance();
////		} catch (InstantiationException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (IllegalAccessException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////        //Now I have an instance of the annotated class I can populate the fields via reflection
////        return clazz;
//////		try
//////		{
//////	        Type superClass = getClass().getGenericSuperclass();
//////	        Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
//////			//Object a =new T();
//////			Object a=PFEnumClass.EnumParse(type, p.getText());
//////			return a;
////////			if(jsonParser!=null&&StringUtils.isNotEmpty(jsonParser.getText())){
////////				return format.parse(jsonParser.getText());
////////			}else {
////////				return null;
////////			}
////// 
//////		}
//////		catch(Exception e)
//////		{
//////			System.out.println(e.getMessage());
//////			throw new RuntimeException(e);
//////		}
////	}
//////    @Override
//////    public JsonDeserializer<?> createContextual(DeserializationContext ctxt,
//////            BeanProperty property) throws JsonMappingException {
//////        //gets the class type of the annotated class
//////        targetClass = ctxt.getContextualType().getRawClass();
//////        //this new JsonApiDeserializer will be cached
//////        return new JsonApiDeserializer(targetClass);
//////    }
////}
//package com;
//

