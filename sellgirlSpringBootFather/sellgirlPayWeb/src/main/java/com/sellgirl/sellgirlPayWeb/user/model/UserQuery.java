package com.sellgirl.sellgirlPayWeb.user.model;

public class UserQuery {
	private Long userId;
	private String userName;
//	private String email;
//	private String pwd;
//	private String invitationCode;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

//	public String getEmail() {
//		return email;
//	}
//	public void setEmail(String email) {
//		this.email = email;
//	}
//	public String getPwd() {
//		return pwd;
//	}
//	public void setPwd(String pwd) {
//		this.pwd = pwd;
//	}
//	public String getInvitationCode() {
//		return invitationCode;
//	}
//	public void setInvitationCode(String invitationCode) {
//		this.invitationCode = invitationCode;
//	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
