package com.sellgirl.sgJavaHelper.sql;

import java.io.Serializable;

import com.sellgirl.sgJavaHelper.IPFSqlFieldTypeConverter;
import com.sellgirl.sgJavaHelper.SGSqlFieldTypeEnum;

/**
 * sql更新的字段,便于做一些格式转换
 * 
 * 转换过程： 
 * ConvertFrom: srcType的原库以srcPFType保存到Value 
 * ConvertTo: Value保存入dstType的目标库
 * 
 * 初始化可以参考这样:
                        SqlUpdateItem updateItem = new SqlUpdateItem();
                        updateItem.Key = fieldName;
                        updateItem.setDstDataType(dstMd.getColumnType(mdIdx));
                        updateItem.setDstDataTypeName(dstMd.getColumnTypeName(mdIdx));
                        updateItem.setDstDataPFType( PFDataHelper.GetPFTypeBySqlType2(dstMd.getColumnType(mdIdx)));
 * 
 * @author 1011712002
 *
 */
public interface IPFSqlUpdateField extends Serializable {
	/**
	 * 字段名
	 * 
	 * @return
	 */
	String getKey();

	/**
	 * 字段值
	 * 
	 * @return
	 */
	Object getValue();

	Integer getSrcDataType();

	/**
	 * 来源值格式,一般就相当于value的格式
	 * @param srcDataType
	 */
	void setSrcDataType(Integer srcDataType);

	/**
	 * 来源数据格式(一般指在java中的实际类型)
	 * 
	 * @return
	 */
	SGSqlFieldTypeEnum getSrcDataPFType();

	void setSrcDataPFType(SGSqlFieldTypeEnum type);

	/**
	 * 目标字段格式 如: varchar timestamp.
	 * 
	 * 为了性能,都用小写
	 * 
	 * 建议使用getDstDataTypeLowercaseName
	 *
	 * @return
	 */
	String getDstDataTypeName();

	/**
	 * 目标小写的字段格式 如: varchar timestamp.
	 * 请在实现setDstDataPFType方法时设置此属性的值
	 * 
	 * @return
	 */
	String getDstDataTypeLowercaseName();

	void setDstDataTypeName(String dstDataTypeName);

	/**
	 * 目标字段格式 如: java.sql.Types
	 * 
	 * @return
	 */
	Integer getDstDataType();

	void setDstDataType(Integer dstDataType);

	/**
	 * 只是为了表示dstDataType对于系统来讲是什么类型,便于在BaseSqlUpdateCollection等地方做判断
	 * @return
	 */
	SGSqlFieldTypeEnum getDstDataPFType();

	void setDstDataPFType(SGSqlFieldTypeEnum type);
	

	IPFSqlFieldTypeConverter getConvertFrom();

	void setConvertFrom(IPFSqlFieldTypeConverter convertFrom);

	IPFSqlFieldTypeConverter getConvertTo();

	void setConvertTo(IPFSqlFieldTypeConverter convertTo);
}
