package com.sellgirl.sgJavaHelper.config;

//import javax.annotation.PostConstruct;

import com.fasterxml.jackson.annotation.JsonProperty;

//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;

//@Component
public abstract class PFAppConfigBase implements IPFAppConfig{

//	@PostConstruct
//	public void beforeInit() {
//	}

	//@Value("${pf.appKey}")
    @JsonProperty
    protected  String appKey;

	//@Value("${pf.useLocalData:}")
    @JsonProperty
    protected  Boolean useLocalData;

	//@Value("${pf.preventFgs:}")
    @JsonProperty
    protected  Boolean preventFgs;

	//@Value("${pf.pfDebug:false}")
    @JsonProperty
    protected   Boolean pfDebug;

	//@Value("${pf.allowAutoLogin:}")
    @JsonProperty
    protected   Boolean allowAutoLogin;

	/**
	 * 检查系统email时用到此时间,如果默认0好像会影响邮箱的性能.
	 */
	//@Value("${pf.checkMessageInterval:5000}")
    @JsonProperty
//    protected  long checkMessageInterval;
	protected  long checkMessageInterval=2000;

	//@Value("${pf.allowGCCollect:false}")
    @JsonProperty
    protected  Boolean allowGCCollect;

	//@Value("${server.port:}")
    @JsonProperty
    protected  String serverPort;

	//@Value("${pf.siteDomain:}")
    @JsonProperty
    protected   String siteDomain;

	//@Value("${pf.ssoSiteDomain:}")
    @JsonProperty
    protected   String ssoSiteDomain;

	//@Value("${pf.cookieDomain:}")
    @JsonProperty
    protected  String cookieDomain;
    
    /**
     * 发送系统信息(如:conn异常短信->dba;)
     */
    @JsonProperty
    protected  Boolean sendSysMsg;


	@Override
	public String getAppKey() {
		return appKey;
	}

//	public void setAppKey(String appKey) {
//		this.appKey = appKey;
//	}

	@Override
	public Boolean getUseLocalData() {
		// return true;
		return useLocalData;
	}

//	public void setUseLocalData(Boolean useLocalData) {
//		this.useLocalData = useLocalData;
//	}

	@Override
	public Boolean getPreventFgs() {
		return preventFgs;
	}

//	public void setPreventFgs(Boolean preventFgs) {
//		this.preventFgs = preventFgs;
//	}

	@Override
	public Boolean getPfDebug() {
		return pfDebug;
	}

//	public void setPfDebug(Boolean pfDebug) {
//		this.pfDebug = pfDebug;
//	}

	@Override
	public Boolean getAllowAutoLogin() {
		return allowAutoLogin;
	}

//	public void setAllowAutoLogin(Boolean allowAutoLogin) {
//		this.allowAutoLogin = allowAutoLogin;
//	}

	@Override
	public long getCheckMessageInterval() {
		return checkMessageInterval;
	}

//	public void setCheckMessageInterval(long checkMessageInterval) {
//		this.checkMessageInterval = checkMessageInterval;
//	}

	@Override
	public Boolean getAllowGCCollect() {
		return allowGCCollect;
	}

//	public void setAllowGCCollect(Boolean allowGCCollect) {
//		this.allowGCCollect = allowGCCollect;
//	}

	@Override
	public String getServerPort() {
		return serverPort;
	}

//	public void setServerPort(String port) {
//		this.serverPort = port;
//	}

	@Override
	public String getSiteDomain() {
		return siteDomain;
	}

//	public void setSiteDomain(String siteDomain) {
//		this.siteDomain = siteDomain;
//	}
	
	@Override
	public String getSsoSiteDomain() {
		return ssoSiteDomain;
	}

//	public void setSsoSiteDomain(String ssoSiteDomain) {
//		this.ssoSiteDomain = ssoSiteDomain;
//	}

	@Override
	public String getCookieDomain() {
		return cookieDomain;
	}

//	public void setCookieDomain(String cookieDomain) {
//		this.cookieDomain = cookieDomain;
//	}

	public boolean getSendSysMsg() {
		return sendSysMsg==null?true:sendSysMsg;
	}

//	public void setSendSysMsg(boolean sendSysMsg) {
//		this.sendSysMsg = sendSysMsg;
//	}
}
