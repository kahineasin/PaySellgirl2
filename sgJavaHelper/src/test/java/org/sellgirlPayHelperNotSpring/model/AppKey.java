package org.sellgirlPayHelperNotSpring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppKey {
    private String email;
    private String emailPwd;
    private String san;
    private String gensin;
	public String getEmailPwd() {
        return emailPwd;
    }

    public void setEmailPwd(String emailPwd) {
        this.emailPwd = emailPwd;
    }

	public String getSan() {
		return san;
	}

	public void setSan(String san) {
		this.san = san;
	}
    public String getGensin() {
		return gensin;
	}

	public void setGensin(String gensin) {
		this.gensin = gensin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
