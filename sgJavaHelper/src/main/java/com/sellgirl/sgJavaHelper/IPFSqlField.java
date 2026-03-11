package com.sellgirl.sgJavaHelper;

public interface IPFSqlField {
	 String getTableName();
	 void setTableName(String tableName);
	 String getTableDescription() ;
	 void setTableDescription(String tableDescription) ;
	 int getFieldIdx() ;
	 void setFieldIdx(int fieldIdx) ;
	 String getFieldName() ;
	 void setFieldName(String fieldName) ;
	 Boolean getIsPrimaryKey();
	 void setIsPrimaryKey(Boolean isPrimaryKey);
	 String getFieldType() ;
	 void setFieldType(String fieldType) ;
	 int getBitLength() ;
	 void setBitLength(int bitLength) ;
	 int getFieldSqlLength();
	 void setFieldSqlLength(int fieldSqlLength) ;
	 int getPrecision() ;
	 void setPrecision(int precision);
	 Boolean getIsRequired() ;
	 void setIsRequired(Boolean isRequired);
	 String getDefaultValue();
	 void setDefaultValue(String defaultValue);
	 String getColumnDescription();
	 void setColumnDescription(String columnDescription);
//	 Object getTransferedData();
//	 void setTransferedData(Object transferedData) ;
	
}
