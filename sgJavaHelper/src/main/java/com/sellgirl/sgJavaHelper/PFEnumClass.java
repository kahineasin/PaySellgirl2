package com.sellgirl.sgJavaHelper;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.sgEnum.IPFEnum;

/**
 * 这个类好像还没有方法可以处理 int转PFEnumClass
 * 
 * 序列化方式:
 *     @JSONField(name = "UserType", serializeUsing = PFEnumClassConvert.class,deserializeUsing=PFEnumClassDeconvert.class)
    @JsonSerialize(using = PFEnumClassSerialiaer.class)
    @JsonDeserialize(using = PFEnumClassDeserialiaer.class)
    public UserTypeClass UserType ;
 * @author Administrator
 *
 */
public abstract class PFEnumClass implements IPFEnum, Serializable
//<T extends IPFEnumClass>
//implements IPFEnumClass
//implements Comparable<PFEnumClass>
{
/**
	 * 
	 */
	private static final long serialVersionUID = -8210278420113831487L;

	//	private int _value;
//	private String _text;
	//private Map<Integer,String> _values;
	protected Map<Integer,String> _values;
	
	/*
	 * 根据所有子类的名保存所有enum的可能值,便于知道enum一共有多少种选项
	 */
	protected static Map<String,Map<Integer,String>> _AllTypeValues=new HashMap<String,Map<Integer,String>>();

	protected PFEnumClass() {
		
	}
	protected PFEnumClass(String text,Integer value) {
		_values=new HashMap<Integer,String>();
		_values.put(value,text);
		
		String typeName=this.getClass().getName();
		if(!_AllTypeValues.containsKey(typeName)) {
			_AllTypeValues.put(typeName, new HashMap<Integer,String>());
		}
		_AllTypeValues.get(typeName).put(value,text);
		
		//String aa="aa";
//		_value=value;
//		_text=text;		
	}
	
	public int getValue() {
		int result=0;
		Iterator<Integer> iter = _values.keySet().iterator();
		  while(iter.hasNext()){
		   result|=iter.next();
		  }
		return result;
	}

	public String getText() {
		return String.join(",", _values.values());
	}
	@Override
	public String toString() {
		return getText();
	} 
	//这种写法有可能会改变静态常量字段的值
//	public <T extends PFEnumClass> T Or(T other) {
//		_values.put(other.getValue(),other.getText());
//		return PFDataHelper.ObjectAs(this);
//	}
	/**
	 * 注意，如果子类想调用此方法，一定要用无参构造函数才行
	 * @param <T>
	 * @param other
	 * @return
	 */
	public <T extends PFEnumClass> T Or(T other)   {
		try {
			//T r=(T)this.getClass().newInstance();
			T r=SGDataHelper.ObjectAs(this.getClass().getConstructor().newInstance());
			r._values=new HashMap<Integer,String>();
			
			   Iterator<Entry<Integer,String>> iter = this._values.entrySet().iterator();
			   while(iter.hasNext()){
				   Entry<Integer,String> key=iter.next();
				   r._values.put(key.getKey(),key.getValue());
			   }
			    iter = other._values.entrySet().iterator();
			   while(iter.hasNext()){
				   Entry<Integer,String> key=iter.next();
				   r._values.put(key.getKey(),key.getValue());
			   }
			   return r;
		} catch (IllegalAccessException|IllegalArgumentException|InstantiationException|InvocationTargetException|NoSuchMethodException|SecurityException e) {
			e.printStackTrace();
		}
		return null;
//		_values.put(other.getValue(),other.getText());
//		return PFDataHelper.ObjectAs(this);
	}
	
	public <T extends PFEnumClass> Boolean HasFlag(T other) {
		return SGDataHelper.EnumHasFlag(getValue(),other.getValue());
	}
	public <T extends PFEnumClass> Boolean AnyFlag(T other) {
		return SGDataHelper.EnumAnyFlag(getValue(),other.getValue());
	}
	public String[] GetAllTexts() {
		String typeName=this.getClass().getName();
		return _AllTypeValues.get(typeName).values().toArray(new String[_AllTypeValues.get(typeName).size()]);
	}

	public static <T extends PFEnumClass> T EnumParse(Class<T> cl,String text) {
		try {
			//return PFDataHelper.ObjectAs(cl.getField(text).get(cl.newInstance())); //cl.newInstance()报错
			return SGDataHelper.ObjectAs(cl.getField(text).get(null));
		} catch (Exception e) {
		}
		return null;
		
//		String typeName=cl.getName();
//		Integer value=new Integer(0);
//		Map<Integer,String> values=_AllTypeValues.get(typeName);
//		Iterator<Map.Entry<Integer,String>> iter = values.entrySet().iterator();
//		  while(iter.hasNext()){
//			  Map.Entry<Integer,String> tmpEntry=iter.next();
//			  if(tmpEntry.getValue().equals(text)) {
//				  value=tmpEntry.getKey();
//				  break;
//			  }
//		  }
//		  
//		try {
//			return (T)cl.getDeclaredConstructor(String.class,int.class).newInstance(text,value.intValue());
//		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
//				| NoSuchMethodException | SecurityException e) {
//		    PFDataHelper.WriteError(e);
//		}
//		return null;
	}

	/**
	 * int转PFEnumClass
	 * @param <T>
	 * @param cl
	 * @param v
	 * @return
	 */
	public static <T extends PFEnumClass> T EnumParseByInt(Class<T> cl,int v) {
		try {
			String typeName=cl.getName();
			Map<Integer, String> m= _AllTypeValues.get(typeName);
			//_AllTypeValues不一定有值(因为PFEnumClass的字段不是一开始就会初始化的)
			if(null==m) {
				Field[] fields=cl.getFields();
				for(Field f:fields) {
					T r=SGDataHelper.ObjectAs(f.get(null));
					if(r.getValue()==v) {
						return r;
					}
				}
			}else {
				 m= _AllTypeValues.get(typeName);
					Iterator<Entry<Integer, String>> iter = m.entrySet().iterator();
					  while(iter.hasNext()){
						   Entry<Integer, String> key=iter.next();
						  if(v==key.getKey()) {
								return SGDataHelper.ObjectAs(cl.getField(key.getValue()).get(null));
						  }
					  }
			}
		} catch (Exception e) {
		}
		return null;
		
	}
	

//	public  PFEnumClass EnumParse(String text) {
//		try {
//			return PFDataHelper.ObjectAs(this.getClass().getField(text).get(this));
//		} catch (Exception e) {
//		}
//		return null;
//
//	}
	
//	@Override
//	public int compareTo(PFEnumClass o) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
	@Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof PFEnumClass) {
        	PFEnumClass anotherString = (PFEnumClass)anObject;
            return this.getValue()==anotherString.getValue();
        }
        return false;
    }
}
