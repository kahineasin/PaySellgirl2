package com.sellgirl.sgJavaHelper;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

//后端post的返回结果
public class SGRequestResult {
	public String content;
	public int statusCode;
	public String error;
	public Boolean success=true;
	public Boolean refuse=false;
	public String toString() {
		String r=String.valueOf(statusCode)+"\r\n";
		if(!SGDataHelper.StringIsNullOrWhiteSpace(content)) {
			r+=content+"\r\n";
		}
		if(!SGDataHelper.StringIsNullOrWhiteSpace(error)) {
			r+=error+"\r\n";
		}
		return r;
	}
	public void setError(String error) {
		this.error=error;
		success=false;
	}
}
