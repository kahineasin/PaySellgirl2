package com.sellgirl.sgJavaHelper.sql;

import java.util.Map;

/**
 * spring注入使，请不要继承PFJdbc, 应该继承PFJdbcBase来实现    
 * @author Administrator
 *
 */
public class SGJdbc extends PFJdbcBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	public SGJdbc() {}
	public SGJdbc(String sqlType,String host,int port,String db,String username,String password) {
		super(sqlType, host, port, db, username, password,null);
	}
	public SGJdbc(String sqlType, String host, int port, String db, String username, String password, Map<String,String> urlParam) {
		super(sqlType,  host,  port,  db,  username,  password,urlParam);
	}
//	public PFJdbc(String sqlType,String host,int port,String db,String username,String password,Map<String,String> urlParam) {
//		super(sqlType, host, port, db, username, password, urlParam);
//	}

	@Override
	//@Value("${jdbc.day.url}")
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	//@Value("${jdbc.day.driverClassName}")
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	@Override
	//@Value("${jdbc.day.username}")
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	//@Value("${jdbc.day.password}")
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	//@Value("${jdbc.day.driverVersion:}")
	public void setDriverVersion(String driverVersion) {
		this.driverVersion = driverVersion;

	}

	@Override
	//@Value("${jdbc.day.ip:}")
	public void setIp(String addr) {
		this.ip = addr;
	}

	@Override
	//@Value("${jdbc.day.dbName:}")
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	@Override
	//@Value("${jdbc.day.dbaMobile:}")
	public void setDbaMobile(String dbaMobile) {
		this.dbaMobile = dbaMobile;
	}

	@Override
	//@Value("${jdbc.day.dbaName:}")
	public void setDbaName(String dbaName) {
		this.dbaName = dbaName;
	}

	@Override
	//@Value("${jdbc.day.dbaEmail:}")
	public void setDbaEmail(String dbaEmail) {
		this.dbaEmail = dbaEmail;

	}
}
