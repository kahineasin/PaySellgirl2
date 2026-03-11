package com.sellgirl.sgJavaHelper.sql;

import java.io.Serializable;
import java.lang.reflect.Field;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sellgirl.sgJavaHelper.IPFSqlFieldTypeConverter;
import com.sellgirl.sgJavaHelper.SGSqlFieldTypeEnum;

/**
 * 继承Serializable是为了使用flink在平台上运行的时候
 * @author Administrator
 *
 */
public class SqlUpdateItem implements Serializable , IPFSqlUpdateField {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String Key ;
    public Object Value ;
    public Class<?> VType;
    
//    /**
//     * @deprecated 改用srcDataPFType
//     */
//    @JSONField(name = "VPFType", serializeUsing = PFEnumClassConvert.class,deserializeUsing=PFEnumClassDeconvert.class)
//    @Deprecated
//    private PFSqlFieldType VPFType;    
    public Integer srcDataType;
	private SGSqlFieldTypeEnum srcDataPFType;
	
	public String dstDataTypeName;
    public String dstDataTypeLowercaseName;
    public Integer dstDataType;
	private SGSqlFieldTypeEnum dstDataPFType;
    /**
     * 为了不用每次从VType判断转换方法,用此委托保存转换(目的是提高效率).
     */
    @JsonIgnore
    public IPFSqlFieldTypeConverter convertFrom=null;
	@JsonIgnore
    public IPFSqlFieldTypeConverter convertTo=null;
	
	public Field PInfo ;
    
	@Override
	public String getKey() {
		return Key;
	}
	@Override
	public Object getValue() {
		return Value;
	}
	@Override
    public Integer getSrcDataType() {
		return srcDataType;
	}
	@Override
	public void setSrcDataType(Integer srcDataType) {
		this.srcDataType = srcDataType;
	}
	@Override
	public SGSqlFieldTypeEnum getSrcDataPFType() {
//		if(null==srcDataPFType&&null!=VPFType) {//为了兼容旧代码
//			return VPFType.toPFEnum();
//		}
		return srcDataPFType;
	}
//	@Deprecated
//	public PFSqlFieldType getVPFType() {
//		if(null==VPFType&&null!=srcDataPFType) {//为了兼容旧代码
//			return PFSqlFieldType.initByPFEnum(srcDataPFType);
//		}
//		return VPFType;
//	}
	@Override
	public void setSrcDataPFType(SGSqlFieldTypeEnum type) {
		srcDataPFType=type;
	}
	@Override
	public String getDstDataTypeName() {
		return dstDataTypeName;
	}
	@Override
	public String getDstDataTypeLowercaseName() {
		return this.dstDataTypeLowercaseName;
	}
	@Override
	public void setDstDataTypeName(String dstDataTypeName) {
		this.dstDataTypeName = dstDataTypeName;
		if(null!=dstDataTypeName) {dstDataTypeLowercaseName=dstDataTypeName.toLowerCase();}
		else {dstDataTypeLowercaseName=null;}
	}
	@Override
    public Integer getDstDataType() {
		return dstDataType;
	}
	@Override
	public void setDstDataType(Integer dstDataType) {
		this.dstDataType = dstDataType;
	}
	@Override
    public SGSqlFieldTypeEnum getDstDataPFType() {
		return dstDataPFType;
	}
	@Override
	public void setDstDataPFType(SGSqlFieldTypeEnum dstDataPFType) {
		this.dstDataPFType = dstDataPFType;
	}
	@Override
    public IPFSqlFieldTypeConverter getConvertFrom() {
		return convertFrom;
	}
	@Override
	public void setConvertFrom(IPFSqlFieldTypeConverter convertFrom) {
		this.convertFrom = convertFrom;
	}
	@Override
	public IPFSqlFieldTypeConverter getConvertTo() {
		return convertTo;
	}
	@Override
	public void setConvertTo(IPFSqlFieldTypeConverter convertTo) {
		this.convertTo = convertTo;
	}
}
