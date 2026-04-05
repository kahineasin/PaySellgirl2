package com.sellgirl.sellgirlPayWeb.configuration.jdbc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sellgirl.sgJavaHelper.sql.PFJdbcBase;
import com.sellgirl.sgJavaHelper.sql.SGSqlType;

@Component
public class DayJdbc extends PFJdbcBase {
//    @Value("${jdbc.day.driverClassName}")
//    private  String driverClassName;
//    @Value("${jdbc.day.url}")
//    private  String url;
//    @Value("${jdbc.day.username}")
//    private  String username;
//    @Value("${jdbc.day.password}")
//    private  String password;

//    public String getUrl() {
//        return url;
//    }

/**
	 * 
	 */
	private static final long serialVersionUID = -1057931076981704129L;

@Override
    @Value("${jdbc.day.url}")
    public void setUrl(String url) {
        this.url = url;
    }
//
//    public String getDriverClassName() {
//        return driverClassName;
//    }

@Override
    @Value("${jdbc.day.driverClassName}")
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

//    public String getUsername() {
//        return username;
//    }

@Override
    @Value("${jdbc.day.username}")
    public void setUsername(String username) {
        this.username = username;
    }

//    public String getPassword() {
//        return password;
//    }

@Override
    @Value("${jdbc.day.password}")
    public void setPassword(String password) {
        this.password = password;
    }

	@Override
	public SGSqlType GetSqlType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDriverVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDriverVersion(String driverVersion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getIp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIp(String addr) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public String getPort() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void setPort(String port) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public String getDbName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDbName(String dbName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDbaMobile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDbaMobile(String dbaMobile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDbaName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDbaName(String dbaName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDbaEmail() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDbaEmail(String dbaEmail) {
		// TODO Auto-generated method stub
		
	}
}