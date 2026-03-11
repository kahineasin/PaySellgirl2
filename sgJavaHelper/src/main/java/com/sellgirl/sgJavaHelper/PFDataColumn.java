package com.sellgirl.sgJavaHelper;

import java.util.HashMap;
import java.util.Map;


public class PFDataColumn implements Cloneable{
    String key;
    Object value;
    private Class<?> _type;
    //private PFSqlFieldType _pfType;
    private SGSqlFieldTypeEnum _pfType;
    private int sqlType;
    public int getSqlType() {
		return sqlType;
	}
	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}
	public Map<String,Object> ExtendedProperties=new HashMap<String,Object>();

    private PFDataColumn() {
    }
    public PFDataColumn(String k, Object v) {
        key = k;
        value = v;
    }
    public PFDataColumn(String k, Class<?> type) {
        key = k;
        _type = type;
        _pfType=SGSqlFieldTypeEnum.InitByClass(type);
    }
 
    public String getKey() {
        return key;
    }
 
    public Object getValue() {
        return value;
    }
 
    public void setKey(String key) {
        this.key = key;
    }
 
    public void setValue(Object value) {
        this.value = value;
    }
    public Class<?> getDataType() {
        return _type;
    }
    public void setDataType(Class<?> t) {
         _type=t;
    }
    public void setPFDataType(SGSqlFieldTypeEnum pfType) {
         _pfType=pfType;
    }
    public SGSqlFieldTypeEnum getPFDataType() {
        return _pfType;
    }
    public PFDataColumn Apply(PFDataColumn o)
    {
    	this.key=o.key;
    	this.value=o.value;
    	this._type=o._type;
    	this._pfType=o._pfType;
    	return this;
    }
    public PFDataColumn TClone()
    {
        //return new PFDataColumn(this.getKey(),this.getValue());
    	PFDataColumn r=new PFDataColumn();
    	return r.Apply(this);
    	
    }
    @Override
    public Object clone()
    {
        return TClone();
    }
}
