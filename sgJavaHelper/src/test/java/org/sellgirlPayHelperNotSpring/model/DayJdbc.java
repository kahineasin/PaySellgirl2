package org.sellgirlPayHelperNotSpring.model;

import com.sellgirl.sgJavaHelper.sql.PFJdbcBase;

//@Component
public class DayJdbc 
extends PFJdbcBase
//implements IPFJdbc 
{
/**
	 * 
	 */
	private static final long serialVersionUID = -49156482258592922L;
	//    //@Value("${jdbc.day.driverClassName}")
//    private  String driverClassName;
//    //@Value("${jdbc.day.url}")
//    private  String url;
//    //@Value("${jdbc.day.username}")
//    private  String username;
//    //@Value("${jdbc.day.password}")
//    private  String password;
//    //@Value("${jdbc.day.driverVersion:}")
//    private  String driverVersion;
//
//    @Value("${jdbc.day.ip}")
//    public String ip;
////    @Value("${jdbc.day.port}")
////	public String port;
//    @Value("${jdbc.day.dbName}")
//    public String dbName;
//
//    @Value("${jdbc.day.dbaMobile}")
//    protected String dbaMobile;
//    @Value("${jdbc.day.dbaName}")
//    protected String dbaName;
	public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	@Override
	public String getDriverVersion() {
		return driverVersion;
	}
	@Override
    public void setDriverVersion(String driverVersion) {
		this.driverVersion = driverVersion;
	}

	@Override
	public String getIp() {
		return ip;
	}

	@Override
	public void setIp(String addr) {
		this.ip = addr;
	}

//	@Override
//	public String getPort() {
//		return port;
//	}
//
//	@Override
//	public void setPort(String port) {
//		this.port = port;
//	}

	@Override
	public String getDbName() {
		return dbName;
	}

	@Override
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	@Override
	public void setDbaMobile(String dbaMobile) {
		this.dbaMobile=dbaMobile;
		
	}

	@Override
	public void setDbaName(String dbaName) {
		this.dbaName=dbaName;
		
	}
	@Override
	public void setDbaEmail(String dbaEmail) {
		this.dbaEmail=dbaEmail;
		
	}


}