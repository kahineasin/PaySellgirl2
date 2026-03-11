package com.sellgirl.sgJavaHelper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

//import com.alibaba.fastjson.util.ParameterizedTypeImpl;

/** 
 * Represents a generic type {@code T}. Java doesn't yet provide a way to
 * represent generic types, so this class does. Forces clients to create a
 * subclass of this class which enables retrieval the type information even at
 * runtime.
 *
 * <p>For example, to create a type literal for {@code List<String>}, you can
 * create an empty anonymous inner class:
 *
 * <pre>
 * TypeReference&lt;List&lt;String&gt;&gt; list = new TypeReference&lt;List&lt;String&gt;&gt;() {};
 * </pre>
 * This syntax cannot be used to create type literals that have wildcard
 * parameters, such as {@code Class<?>} or {@code List<? extends CharSequence>}.
 */
public class PFTypeReference<T> {
    static ConcurrentMap<Type, Type> classTypeCache
            = new ConcurrentHashMap<Type, Type>(16, 0.75f, 1);

    protected final Type type;//可能是Class,也可能是ParameterizedTypeImpl
    //protected final Class<T> tClass;
    

    /**
     * Constructs a new type literal. Derives represented class from type
     * parameter.
     *
     * <p>Clients create an empty anonymous subclass. Doing so embeds the type
     * parameter in the anonymous class's type hierarchy so we can reconstitute it
     * at runtime despite erasure.
     */
    protected PFTypeReference(){
        Type superClass = getClass().getGenericSuperclass();

        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        //tClass=(Class<T>)type;
    }

    /**
     * @since 1.2.9
     * @param actualTypeArguments
     */
    protected PFTypeReference(Type... actualTypeArguments){
        Class<?> thisClass = this.getClass();
        Type superClass = thisClass.getGenericSuperclass();

        ParameterizedType argType = (ParameterizedType) ((ParameterizedType) superClass).getActualTypeArguments()[0];
        Type rawType = argType.getRawType();
        Type[] argTypes = argType.getActualTypeArguments();

        int actualIndex = 0;
        for (int i = 0; i < argTypes.length; ++i) {
            if (argTypes[i] instanceof TypeVariable) {
                argTypes[i] = actualTypeArguments[actualIndex++];
                if (actualIndex >= actualTypeArguments.length) {
                    break;
                }
            }
        }

        Type key = new PFParameterizedTypeImpl(argTypes, thisClass, rawType);
        Type cachedType = classTypeCache.get(key);
        if (cachedType == null) {
            classTypeCache.putIfAbsent(key, key);
            cachedType = classTypeCache.get(key);
        }

        type = cachedType;
        //tClass=(Class<T>)type;
    }
    
    /**
     * Gets underlying {@code Type} instance.
     */
    public Type getType() {
        return type;
    }
//    public Class<T> getClass() {
//        return (Class)type;
//    }
//    public Boolean IsTypeOf(Object value) {
////    	Class<T> bb=(Class<T>)type;
//    	Class<T> bb=(Class<T>)(((ParameterizedType)type).getRawType());
//    	return bb.isAssignableFrom(value.getClass());
////    	return value.getClass().isAssignableFrom(bb);
//    }

	
    /**
     * 是否value的类型(参照Class.isInstance())
     * 总结:由于不能拿到value的T(当value是泛型时,如ArrayList<String>),所以其实此方法还不如直接用 xx instanceof List<?>,反正只能判断外层
     * @param value
     * @return
     */
    public Boolean isInstance(Object value) {
//    	Class<T> bb=(Class<T>)type;
    	if(type instanceof ParameterizedType) {
    		//这里有两种情况,value是泛型,或非泛型,但用value.getClass好像不能获得T.
        	@SuppressWarnings("unchecked")
			Class<T> bb=(Class<T>)(((ParameterizedType)type).getRawType());
        	return bb.isAssignableFrom(value.getClass());
//        	return value.getClass().isAssignableFrom(bb);
    	}else if(type instanceof Class){
//        	Class<T> bb=(Class<T>)type;
//        	return bb.isAssignableFrom(value.getClass());
    		return ((Class<?>)type).isInstance(value);
    	}
    	return false;
    }
    /**
     * type是否可被t分配(即type是否t的父级类型)
     * @param t
     * @return
     */
    public Boolean isAssignableFrom(Type t) {
    	Class<?> tCls=null;
    	if(t instanceof ParameterizedType) {
    		tCls=(Class<?>)(((ParameterizedType)t).getRawType());
    	}else if(t instanceof Class){
    		tCls= ((Class<?>)t);
    	}
    	Class<?> sCls=null;
    	if(type instanceof ParameterizedType) {
    		sCls=(Class<?>)(((ParameterizedType)type).getRawType());
    	}else if(type instanceof Class){
    		sCls= ((Class<?>)type);
    	}
    	if(sCls!=null&&tCls!=null) {
    		return sCls.isAssignableFrom(tCls);
    	}    	
    	return false;
    }

    public final static Type LIST_STRING = new PFTypeReference<List<String>>() {}.getType();
}

