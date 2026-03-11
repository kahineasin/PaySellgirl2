package com.sellgirl.sgJavaHelper;

public class SGSqlFieldInfo implements IPFSqlField {
	@PFSqlFieldAttribute(FieldText="表名")
	private String tableName;
	@PFSqlFieldAttribute(FieldText="表说明")
	private String tableDescription;
	@PFSqlFieldAttribute(FieldText="字段序号")
	private int fieldIdx;
	@PFSqlFieldAttribute(FieldText="字段名")
	private String fieldName;
	@PFSqlFieldAttribute(FieldText="主键")
	private Boolean isPrimaryKey;	
	@PFSqlFieldAttribute(FieldText="类型")
	private String fieldType;
	@PFSqlFieldAttribute(FieldText="占用字节数")
	private int bitLength;
	@PFSqlFieldAttribute(FieldText="长度")
	private int fieldSqlLength;
	@PFSqlFieldAttribute(FieldText="小数位数")
	private int precision;
	@PFSqlFieldAttribute(FieldText="非空")
	private Boolean isRequired;
	@PFSqlFieldAttribute(FieldText="默认值")
	private String defaultValue;
	@PFSqlFieldAttribute(FieldText="说明")
	private String columnDescription;
	@PFSqlFieldAttribute(FieldText="已读值")
	private Object transferedData;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableDescription() {
		return tableDescription;
	}
	public void setTableDescription(String tableDescription) {
		this.tableDescription = tableDescription;
	}
	public int getFieldIdx() {
		return fieldIdx;
	}
	public void setFieldIdx(int fieldIdx) {
		this.fieldIdx = fieldIdx;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public Boolean getIsPrimaryKey() {
		return isPrimaryKey;
	}
	public void setIsPrimaryKey(Boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public int getBitLength() {
		return bitLength;
	}
	public void setBitLength(int bitLength) {
		this.bitLength = bitLength;
	}
	public int getFieldSqlLength() {
		return fieldSqlLength;
	}
	public void setFieldSqlLength(int fieldSqlLength) {
		this.fieldSqlLength = fieldSqlLength;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public Boolean getIsRequired() {
		return isRequired;
	}
	public void setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getColumnDescription() {
		return columnDescription;
	}
	public void setColumnDescription(String columnDescription) {
		this.columnDescription = columnDescription;
	}
	public Object getTransferedData() {
		return transferedData;
	}
	public void setTransferedData(Object transferedData) {
		this.transferedData = transferedData;
	}
	
}
