package com.sellgirl.sellgirlPayWeb.user.model;

import com.sellgirl.sgJavaHelper.SGDate;

public class User {
	private String userName;
//	private String pwd;
	private String invitationCode;
	private int signDay;
	private SGDate lastSign;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
//	public String getPwd() {
//		return pwd;
//	}
//	public void setPwd(String pwd) {
//		this.pwd = pwd;
//	}
	public String getInvitationCode() {
		return invitationCode;
	}
	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

	public int getSignDay() {
		return signDay;
	}
	public void setSignDay(int signDay) {
		this.signDay = signDay;
	}
	public SGDate getLastSign() {
		return lastSign;
	}
	public void setLastSign(SGDate lastSign) {
		this.lastSign = lastSign;
	}
}
