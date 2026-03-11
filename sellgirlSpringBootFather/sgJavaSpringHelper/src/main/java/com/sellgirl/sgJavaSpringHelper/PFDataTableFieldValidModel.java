package com.sellgirl.sgJavaSpringHelper;

/**
 * 导入Table前可以在测试环境中验证
 * @author Administrator
 *
 */
public class PFDataTableFieldValidModel {
	private String fieldName;
	private Boolean isValid;
	private String errMsg;
	public PFDataTableFieldValidModel(String fieldName,Boolean isValid,String errMsg) {
		this.fieldName=fieldName;
		this.isValid=isValid;
		this.errMsg=errMsg;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public Boolean getIsValid() {
		return isValid;
	}
	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
