package com.sellgirl.sgJavaHelper.config;

import javax.annotation.PostConstruct;

//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;

//@Component
public class PFAppConfig extends PFAppConfigBase {

	@PostConstruct
	public void beforeInit() {
	}

//	//@Value("${pf.appKey}")
//	private String appKey;
//
//	//@Value("${pf.useLocalData:}")
//	private Boolean useLocalData;
//
//	//@Value("${pf.preventFgs:}")
//	private Boolean preventFgs;
//
//	//@Value("${pf.pfDebug:false}")
//	private Boolean pfDebug;
//
//	//@Value("${pf.allowAutoLogin:}")
//	private Boolean allowAutoLogin;
//
//	//@Value("${pf.checkMessageInterval:5000}")
//	private long checkMessageInterval;
//
//	//@Value("${pf.allowGCCollect:false}")
//	private Boolean allowGCCollect;
//
//	//@Value("${server.port:}")
//	private String serverPort;
//
//	//@Value("${pf.siteDomain:}")
//	private String siteDomain;
//
//	//@Value("${pf.ssoSiteDomain:}")
//	private String ssoSiteDomain;
//
//	//@Value("${pf.cookieDomain:}")
//	private String cookieDomain;

//	public String getAppKey() {
//		return appKey;
//	}


	@Override
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

//	public Boolean getUseLocalData() {
//		// return true;
//		return useLocalData;
//	}


	@Override
	public void setUseLocalData(Boolean useLocalData) {
		this.useLocalData = useLocalData;
	}

//	public Boolean getPreventFgs() {
//		return preventFgs;
//	}


	@Override
	public void setPreventFgs(Boolean preventFgs) {
		this.preventFgs = preventFgs;
	}

//	public Boolean getPfDebug() {
//		return pfDebug;
//	}


	@Override
	public void setPfDebug(Boolean pfDebug) {
		this.pfDebug = pfDebug;
	}

//	public Boolean getAllowAutoLogin() {
//		return allowAutoLogin;
//	}


	@Override
	public void setAllowAutoLogin(Boolean allowAutoLogin) {
		this.allowAutoLogin = allowAutoLogin;
	}

//	public long getCheckMessageInterval() {
//		return checkMessageInterval;
//	}


	@Override
	public void setCheckMessageInterval(long checkMessageInterval) {
		this.checkMessageInterval = checkMessageInterval;
	}

//	public Boolean getAllowGCCollect() {
//		return allowGCCollect;
//	}


	@Override
	public void setAllowGCCollect(Boolean allowGCCollect) {
		this.allowGCCollect = allowGCCollect;
	}

//	public String getServerPort() {
//		return serverPort;
//	}


	@Override
	public void setServerPort(String port) {
		this.serverPort = port;
	}
	
//	public String getSiteDomain() {
//		return siteDomain;
//	}


	@Override
	public void setSiteDomain(String siteDomain) {
		this.siteDomain = siteDomain;
	}
//	public String getSsoSiteDomain() {
//		return ssoSiteDomain;
//	}


	@Override
	public void setSsoSiteDomain(String ssoSiteDomain) {
		this.ssoSiteDomain = ssoSiteDomain;
	}

//	public String getCookieDomain() {
//		return cookieDomain;
//	}


	@Override
	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}


	@Override
	public void setSendSysMsg(Boolean sendSysMsg) {
		this.sendSysMsg = sendSysMsg;
	}
}
