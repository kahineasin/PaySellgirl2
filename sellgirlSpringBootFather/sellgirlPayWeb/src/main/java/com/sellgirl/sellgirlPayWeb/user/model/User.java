package com.sellgirl.sellgirlPayWeb.user.model;

import com.sellgirl.sgJavaHelper.SGDate;

public class User {
	private long userId;
	private String userName;
//	private String pwd;
	private String invitationCode;
	private int signDay;
	private SGDate lastSign;
	private  boolean vip1;
	private SGDate vip1_expire;
	private  boolean vip2;
	private SGDate vip2_expire;
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
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

	public boolean isVip1() {
		return vip1;
	}
	public void setVip1(boolean vip1) {
		this.vip1 = vip1;
	}
	public SGDate getVip1_expire() {
		return vip1_expire;
	}
	public void setVip1_expire(SGDate vip1_expire) {
		this.vip1_expire = vip1_expire;
	}
	public boolean isVip2() {
		return vip2;
	}
	public void setVip2(boolean vip2) {
		this.vip2 = vip2;
	}
	public SGDate getVip2_expire() {
		return vip2_expire;
	}
	public void setVip2_expire(SGDate vip2_expire) {
		this.vip2_expire = vip2_expire;
	}
}
