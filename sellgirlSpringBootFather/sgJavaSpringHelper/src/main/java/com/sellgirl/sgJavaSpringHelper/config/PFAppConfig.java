package com.sellgirl.sgJavaSpringHelper.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sellgirl.sgJavaHelper.config.PFAppConfigBase;

@Component
//@Configuration
public class PFAppConfig extends  PFAppConfigBase{

	@PostConstruct
	public void beforeInit() {
	}


	/**
	 * 此属性只是为了测试sg项目配置了bean默认值后yml的设置不生效的问题
	 */
	@Value("${pf.testp001:001d}")
    protected  String testp001;
//	
//	protected  String testp002;
//	protected  String testp003;
//	protected  String testp004;
//	@Value("${pf.testp005}")
//	protected  String testp005;
//	@Value("${pf.testp006:}")
//	protected  String testp006;
//	
	public String getTestp001() {
		return testp001;
	}

	public void setTestp001(String testp001) {
		this.testp001 = testp001;
	}
//
//	public String getTestp002() {
//		return testp002;
//	}
//
//	@Value("${pf.testp002:bb}")
//	public void setTestp002(String testp002) {
//		this.testp002 = testp002;
//	}
//	public String getTestp003() {
//		return testp003;
//	}
//
//	@Value("${pf.testp003:}")
//	public void setTestp003(String testp003) {
//		this.testp003 = testp003;
//	}
//	public String getTestp004() {
//		return testp004;
//	}
//
//	@Value("${pf.testp004:}")
//	public void setTestp004(String testp004) {
//		this.testp004 = testp004;
//	}
//	
//    public String getTestp005() {
//		return testp005;
//	}
//
//	public void setTestp005(String testp005) {
//		this.testp005 = testp005;
//	}
//
//	public String getTestp006() {
//		return testp006;
//	}
//
//	public void setTestp006(String testp006) {
//		this.testp006 = testp006;
//	}



	@Override
	@Value("${pf.appKey}")
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

//	public Boolean getUseLocalData() {
//		// return true;
//		return useLocalData;
//	}


	@Override
	@Value("${pf.useLocalData:}")
	public void setUseLocalData(Boolean useLocalData) {
		this.useLocalData = useLocalData;
	}

//	public Boolean getPreventFgs() {
//		return preventFgs;
//	}


	@Override
	@Value("${pf.preventFgs:}")
	public void setPreventFgs(Boolean preventFgs) {
		this.preventFgs = preventFgs;
	}

//	public Boolean getPfDebug() {
//		return pfDebug;
//	}


	@Override
	@Value("${pf.pfDebug:false}")
	public void setPfDebug(Boolean pfDebug) {
		this.pfDebug = pfDebug;
	}

//	public Boolean getAllowAutoLogin() {
//		return allowAutoLogin;
//	}


	@Override
	@Value("${pf.allowAutoLogin:}")
	public void setAllowAutoLogin(Boolean allowAutoLogin) {
		this.allowAutoLogin = allowAutoLogin;
	}

//	public long getCheckMessageInterval() {
//		return checkMessageInterval;
//	}


	@Override
	@Value("${pf.checkMessageInterval:5000}")
	public void setCheckMessageInterval(long checkMessageInterval) {
		this.checkMessageInterval = checkMessageInterval;
	}

//	public Boolean getAllowGCCollect() {
//		return allowGCCollect;
//	}


	@Override
	@Value("${pf.allowGCCollect:false}")
	public void setAllowGCCollect(Boolean allowGCCollect) {
		this.allowGCCollect = allowGCCollect;
	}

//	public String getServerPort() {
//		return serverPort;
//	}


	@Override
	@Value("${server.port:}")
	public void setServerPort(String port) {
		this.serverPort = port;
	}
	
//	public String getSiteDomain() {
//		return siteDomain;
//	}


	@Override
	@Value("${pf.siteDomain:}")
	public void setSiteDomain(String siteDomain) {
		this.siteDomain = siteDomain;
	}
//	public String getSsoSiteDomain() {
//		return ssoSiteDomain;
//	}


	@Override
	@Value("${pf.ssoSiteDomain:}")
	public void setSsoSiteDomain(String ssoSiteDomain) {
		this.ssoSiteDomain = ssoSiteDomain;
	}

//	public String getCookieDomain() {
//		return cookieDomain;
//	}


	@Override
	@Value("${pf.cookieDomain:}")
	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}


	@Override
	//@Value("${pf.sendSysMsg:false}")//在sg项目里,用了默认值时,yml配置就不生效,暂不知道原因(其实可以用没后缀的yml上的配置来作为默认值
	@Value("${pf.sendSysMsg:}")
	public void setSendSysMsg(Boolean sendSysMsg) {
		this.sendSysMsg = sendSysMsg;
	}

	public com.sellgirl.sgJavaHelper.config.PFAppConfig toNotSpring(){

//    	pf.java.pfHelper.config.PFAppConfig appConfig=pf.java.pfHelper.config.PFDataHelper.GetAppConfig();
    	com.sellgirl.sgJavaSpringHelper.config.PFAppConfig appConfig=this;
    	com.sellgirl.sgJavaHelper.config.PFAppConfig appConfigNotSpring=new com.sellgirl.sgJavaHelper.config.PFAppConfig();
    	appConfigNotSpring.setAppKey(appConfig.getAppKey());
    	appConfigNotSpring.setUseLocalData(appConfig.getUseLocalData());
    	appConfigNotSpring.setPreventFgs(appConfig.getPreventFgs());
    	appConfigNotSpring.setPfDebug(appConfig.getPfDebug());
    	appConfigNotSpring.setAllowAutoLogin(appConfig.getAllowAutoLogin());
    	appConfigNotSpring.setCheckMessageInterval(appConfig.getCheckMessageInterval());
    	appConfigNotSpring.setAllowGCCollect(appConfig.getAllowGCCollect());
    	appConfigNotSpring.setServerPort(appConfig.getServerPort());
    	appConfigNotSpring.setSiteDomain(appConfig.getSiteDomain());
    	appConfigNotSpring.setSsoSiteDomain(appConfig.getSsoSiteDomain());
    	appConfigNotSpring.setCookieDomain(appConfig.getCookieDomain());
    	appConfigNotSpring.setAllowAutoLogin(appConfig.getAllowAutoLogin());
    	appConfigNotSpring.setAllowAutoLogin(appConfig.getAllowAutoLogin());
    	appConfigNotSpring.setSendSysMsg(appConfig.getSendSysMsg());    	
    	return appConfigNotSpring;
	}
}
