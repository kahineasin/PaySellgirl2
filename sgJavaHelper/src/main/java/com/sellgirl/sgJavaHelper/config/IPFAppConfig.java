package com.sellgirl.sgJavaHelper.config;

public interface IPFAppConfig {
	String getAppKey() ;
	
	 void setAppKey(String appKey) ;

	 Boolean getUseLocalData() ;

	 void setUseLocalData(Boolean useLocalData);

	 Boolean getPreventFgs() ;

	 void setPreventFgs(Boolean preventFgs) ;

	 Boolean getPfDebug();

	 void setPfDebug(Boolean pfDebug) ;

	 Boolean getAllowAutoLogin();

	 void setAllowAutoLogin(Boolean allowAutoLogin) ;

	 long getCheckMessageInterval() ;

	 void setCheckMessageInterval(long checkMessageInterval) ;

	 Boolean getAllowGCCollect() ;

	 void setAllowGCCollect(Boolean allowGCCollect);

	 String getServerPort() ;

	 void setServerPort(String port) ;
	
	 String getSiteDomain() ;

	 void setSiteDomain(String siteDomain);
	 
	 String getSsoSiteDomain();

	 void setSsoSiteDomain(String ssoSiteDomain) ;

	 String getCookieDomain() ;

	 void setCookieDomain(String cookieDomain);
	 
	 boolean getSendSysMsg() ;

	 void setSendSysMsg(Boolean sendSysMsg) ;
}
