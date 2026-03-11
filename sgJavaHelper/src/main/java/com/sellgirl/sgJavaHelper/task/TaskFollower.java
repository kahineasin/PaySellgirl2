package com.sellgirl.sgJavaHelper.task;

/**
 * 用户关注者(便于当任务失败时发送短信通知)
 * @author Administrator
 *
 */
public class TaskFollower {

	private String userName;

	private String email;

    private String telephone;
    public TaskFollower(String userName, String email, String telephone) {
		super();
		this.userName = userName;
		this.email = email;
		this.telephone = telephone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
}
