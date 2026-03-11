package com.sellgirl.sgJavaSpringHelper;

import java.sql.Blob;
import java.sql.Date;
/**
 * @功能描述  sql参数，sql执行时传递的参数用此类封装
 * @可能的错误 
 * @作者 叶小钗
 * @修改说明 
 * @修改人
 */
public class PFSqlParameter {

    public PFSqlParameter(String type, String value) {
        this.type = type;
        this.value = value;
    }
 
    public PFSqlParameter(String type, int intValue) {
        this.type = type;
        this.intValue = intValue;
    }
 
    public PFSqlParameter(String type, boolean boolValue) {
        this.type = type;
        this.boolValue = boolValue;
    }
 
    public PFSqlParameter(String type, Date dateValue) {
        this.type = type;
        this.dateValue = dateValue;
    }
 
    public PFSqlParameter(String type, Blob blobValue) {
        this.type = type;
        this.blobValue = blobValue;
    }
 
    String type;
    String value;
    int intValue;
    boolean boolValue;
    Date dateValue;
    Blob blobValue;
    public String getType() {
        return type;
    }
 
    public String getValue() {
        return value;
    }
 
    public int getIntValue() {
        return intValue;
    }
 
    public boolean getBoolValue() {
        return boolValue;
    }
 
    public Date getDateValue() {
        return dateValue;
    }
 
    public Blob getBlobValue() {
        return blobValue;
    }
 
    public void setType(String type) {
        this.type = type;
    }
 
    public void setValue(String value) {
        this.value = value;
    }
 
    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }
 
    public void setBoolValue(boolean boolValue) {
        this.boolValue = boolValue;
    }
 
    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }
 
    public void setBlobValue(Blob blobValue) {
        this.blobValue = blobValue;
    }
}
