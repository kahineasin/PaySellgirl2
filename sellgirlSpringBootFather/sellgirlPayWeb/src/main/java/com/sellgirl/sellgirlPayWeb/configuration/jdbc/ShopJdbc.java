package com.sellgirl.sellgirlPayWeb.configuration.jdbc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//import com.sellgirl.sellgirlPayService.IShopJdbc;
import com.sellgirl.sgJavaHelper.PFSqlType;
import com.sellgirl.sgJavaHelper.sql.PFJdbcBase;

@Component
public class ShopJdbc extends PFJdbcBase 
//implements IShopJdbc
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	@Value("${jdbc.shop.url:}")
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	@Value("${jdbc.shop.driverClassName:}")
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	@Override
	@Value("${jdbc.shop.username:}")
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	@Value("${jdbc.shop.password:}")
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	@Value("${jdbc.shop.driverVersion:}")
	public void setDriverVersion(String driverVersion) {
		this.driverVersion = driverVersion;

	}

	@Override
	@Value("${jdbc.shop.ip:}")
	public void setIp(String addr) {
		this.ip = addr;
	}

	@Override
	@Value("${jdbc.shop.dbName:}")
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	@Override
	@Value("${jdbc.shop.dbaMobile:}")
	public void setDbaMobile(String dbaMobile) {
		this.dbaMobile = dbaMobile;
	}

	@Override
	@Value("${jdbc.shop.dbaName:}")
	public void setDbaName(String dbaName) {
		this.dbaName = dbaName;
	}

	@Override
	@Value("${jdbc.shop.dbaEmail:}")
	public void setDbaEmail(String dbaEmail) {
		this.dbaEmail = dbaEmail;

	}
}