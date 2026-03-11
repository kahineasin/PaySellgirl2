package com.sellgirl.sgJavaHelper;


import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

/**
 * 
 * yaml转换工具
 * https://blog.csdn.net/qq_16127313/article/details/130374351
 * 
 * 用法:
 * 1. Map<String, String> a = SGYamlHelper.loadMap("D:\\xxx.yml");
 * 2. ClassA a=SGYamlHelper.loadType("D:\\xxx.yml", ClassA.class);
 * 
 * @author 
 * @version [版本号, 2023年4月25日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public final class SGYamlHelper
{
    private static YAMLMapper yamlMapper = new YAMLMapper();
    
    private static JavaPropsMapper javaPropsMapper = new JavaPropsMapper();
    
    private SGYamlHelper()
    {
        super();
    }

    public static <T> T loadType(String filePath, Class<T> clazz) throws Exception{
//    	if(ignoreUnknown) {
//    	    return SGYamlHelper.toObjectIgnoreUnknown( SGDataHelper.ReadFileToString(filePath), clazz);
//    	}else {
    	    return SGYamlHelper.toObject( SGDataHelper.ReadFileToString(filePath), clazz);
//    	}
    }
    public static <T> T loadTypeIgnoreUnknown(String filePath, Class<T> clazz) throws Exception{
//    	if(ignoreUnknown) {
    	    return SGYamlHelper.toObjectIgnoreUnknown( SGDataHelper.ReadFileToString(filePath), clazz);
//    	}else {
//    	    return SGYamlHelper.toObject( SGDataHelper.ReadFileToString(filePath), clazz);
//    	}
    }
    public static  Map<String, String> loadMap(String filePath) throws IOException{
	    return SGYamlHelper.yamlToMap( SGDataHelper.ReadFileToString(filePath));
    }
    
    /**
     * yaml转properties
     * 
     * @param yamlContent
     * @return
     * @throws IOException
     */
    public static Properties yamlToProperties(String yamlContent)
        throws IOException
    {
        JsonNode jsonNode = yamlMapper.readTree(yamlContent);
        return javaPropsMapper.writeValueAsProperties(jsonNode);
    }
    
    /**
     * yaml转Map<String, String>
     * 
     * @param yamlContent
     * @return
     * @throws IOException
     */
    public static Map<String, String> yamlToMap(String yamlContent)
        throws IOException
    {
        JsonNode jsonNode = yamlMapper.readTree(yamlContent);
        return javaPropsMapper.writeValueAsMap(jsonNode);
    }
    
    /**
     * yaml转properties字符串
     * 
     * @param yamlContent
     * @return
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    public static String yamlToPropText(String yamlContent)
        throws JsonMappingException, JsonProcessingException
    {
        JsonNode jsonNode = yamlMapper.readTree(yamlContent);
        return javaPropsMapper.writeValueAsString(jsonNode);
    }
    
    /**
     * yaml转Json字符串
     * 
     * @param yamlContent
     * @return
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    public static String yamlToJson(String yamlContent)
        throws JsonMappingException, JsonProcessingException
    {
        JsonNode jsonNode = yamlMapper.readTree(yamlContent);
        return jsonNode.toPrettyString();
    }
    

    /**
    * 将yaml字符串转成类对象
    * @param yamlStr 字符串
    * @param clazz 目标类
    * @param <T> 泛型
    * @return 目标类
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
    */
    public static <T> T toObject(String yamlStr, Class<T> clazz) throws Exception{
    	//这样不方便, 不yaml上的字段比Type多时, 会报错, 需要在类上加@JsonIgnoreProperties(ignoreUnknown = true)
    	YAMLFactory yamlFactory=new YAMLFactory();
//    	yamlFactory.configure(YAMLGenerator.Feature., false)
    ObjectMapper mapper = new ObjectMapper(yamlFactory);
//    mapper.ign
    mapper.findAndRegisterModules();
    try {
    return mapper.readValue(yamlStr, clazz);
    } catch (JsonProcessingException e) {
    e.printStackTrace();
    }
    return null;
    }

    public static <T> T toObjectIgnoreUnknown(String yamlStr, Class<T> clazz) throws Exception{

    	T row=clazz.getConstructor().newInstance();
    	Map<String,String> map=SGYamlHelper.yamlToMap(yamlStr);
    	SGDataHelper.EachObjectPropertyType(row,new SGAction<String, Field, PFModelConfig>(){

			@Override
			public void go(String t1, Field t2, PFModelConfig t3) {
				if(map.containsKey(t1)) {
					try {
						t2.set(row, row);
					} catch (Exception e) {
					}
				}
			}
    		
    	});
    	return row;
    }

    /**
    * 将类对象转yaml字符串
    * @param object 对象
    * @return yaml字符串
    */
    public static
    String toYaml(Object object){
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    mapper.findAndRegisterModules();
//    JsonMapper.builder().
    //mapper.builder().disable(null);
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
    StringWriter stringWriter = new StringWriter();
    try {
    mapper.writeValue(stringWriter, object);
    return stringWriter.toString();
    } catch (IOException e) {
    e.printStackTrace();
    }
    return null;
    }

    /**
    * （此方法非必要）
    * json 2 yaml
    * @param jsonStr json
    * @return yaml
    * @throws JsonProcessingException Exception
    */
    public static String json2Yaml(String jsonStr) throws JsonProcessingException {
    JsonNode jsonNode = new ObjectMapper().readTree(jsonStr);
    return new YAMLMapper().writeValueAsString(jsonNode);
    }

/**
 * Properties 转换工具
 * 
 * @author 00fly
 *
 */
public static class PropertiesUtils
{
    private static YAMLMapper yamlMapper = new YAMLMapper();
    
    private static JavaPropsMapper javaPropsMapper = new JavaPropsMapper();
    
    /**
     * properties对象转Json字符串
     * 
     * @param properties对象
     * @return
     * @throws IOException
     */
    public static String propertiesToJson(Properties properties)
        throws IOException
    {
        JsonNode jsonNode = javaPropsMapper.readPropertiesAs(properties, JsonNode.class);
        return jsonNode.toPrettyString();
    }
    
    /**
     * properties对象转yaml
     * 
     * @param properties对象
     * @return
     * @throws IOException
     */
    public static String propertiesToYaml(Properties properties)
        throws IOException
    {
        JsonNode jsonNode = javaPropsMapper.readPropertiesAs(properties, JsonNode.class);
        return yamlMapper.writeValueAsString(jsonNode);
    }
    
    /**
     * properties字符串转Json字符串
     * 
     * @param propText
     * @return
     * @throws IOException
     */
    public static String propTextToJson(String propText)
        throws IOException
    {
        JsonNode jsonNode = javaPropsMapper.readTree(propText);
        return jsonNode.toPrettyString();
    }
    
    /**
     * properties字符串转yaml
     * 
     * @param propText
     * @return
     * @throws IOException
     */
    public static String propTextToYaml(String propText)
        throws IOException
    {
        JsonNode jsonNode = javaPropsMapper.readTree(propText);
        return yamlMapper.writeValueAsString(jsonNode);
    }
    
    private PropertiesUtils()
    {
        super();
    }
}


}

