package com.sellgirl.sgJavaHelper.sql;

import java.io.Serializable;

import com.sellgirl.sgJavaHelper.PFSqlType;

public interface ISGJdbc extends Serializable{

	/**
	 * 格式如　jdbc:mysql://uat-cloud.perfect99.com:10129/test?rewriteBatchedStatements=true
	 * @return
	 */
	String getUrl();

	void setUrl(String url);

	String getDriverClassName();

	void setDriverClassName(String driverClassName);

	String getUsername();

	void setUsername(String username);

	String getPassword();

	void setPassword(String password);

	PFSqlType GetSqlType();

	/**
	 * 如果除mysql外的数据库也要区分片,考虑共用此属性
	 * 
	 * @return
	 */
	String getDriverVersion();

	void setDriverVersion(String driverVersion);

	/**
	 * 含端口,如　uat-cloud.perfect99.com:10129
	 * 
	 * @return
	 */
	String getIp();

	void setIp(String addr);

	 String getHost() ;
 	 String getPort() ;

	// void setPort(String port);

	String getDbName();

	void setDbName(String dbName);

	/**
	 * 数据库管理员手机
	 */
	String getDbaMobile();

	void setDbaMobile(String dbaMobile);

	/**
	 * 数据库管理员姓名
	 */
	String getDbaName();

	void setDbaName(String dbaName);

	/**
	 * 数据库管理员邮箱
	 */
	String getDbaEmail();

	void setDbaEmail(String dbaEmail);

	/**
	 * 数据库短名,格式如 ip:dbname(如果没填ip,可以自动从url中拆解
	 * @return
	 */
	String getUrlShortName();
	
	/**
	 * 是发送手机短信的conn(异常时就不要调用发短信的方法,否则死循环)
	 * @return
	 */
	Boolean getIsMobileMessageConn();
}
