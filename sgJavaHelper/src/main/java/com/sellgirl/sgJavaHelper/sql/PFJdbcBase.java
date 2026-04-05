package com.sellgirl.sgJavaHelper.sql;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * springboot使用此基类时可以这么复写: 
	@@Component
	public class DayJdbc 
	extends PFJdbcBase{
	@@Override
    @@Value("${jdbc.day.url}")
    public void setUrl(String url) {
        this.url = url;
    }
    
 * @author Administrator
 *
 */
public abstract class PFJdbcBase implements ISGJdbc {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
    protected  String driverClassName;
    @JsonProperty
    protected  String driverVersion;
    @JsonProperty
    protected  String url;
    @JsonProperty
    protected  String username;
    @JsonProperty
    protected  String password;

    @JsonProperty
    protected String ip;
//    @Value("${jdbc.bonus.port}")
//	public String port;
    @JsonProperty
    protected String dbName;
    
    @JsonProperty
    protected String sqlType;
    
    @JsonProperty
    protected String dbaMobile;
    
    @JsonProperty
    protected String dbaName;
    @JsonProperty
    protected String dbaEmail;
    @JsonProperty
    protected Boolean isMobileMessageConn=false;
    
    
	public PFJdbcBase() {}
	public PFJdbcBase(String sqlType, String host, int port, String db, String username, String password, Map<String,String> urlParam) {
		SGSqlType sqlTypeEnum=StringToSqlType(sqlType);
//		if(PFSqlType.MySql==sqlTypeEnum||PFSqlType.Tidb==sqlTypeEnum) {
//			driverClassName="com.mysql.jdbc.Driver";
//			Map<String,String> urlParamDef=new HashMap<>();
//			urlParamDef.put("rewriteBatchedStatements","true");//考虑要不要把 useSSL=false 设置为默认值
//			if(null!=urlParam){urlParamDef.putAll(urlParam);}
//			//url=PFDataHelper.FormatString("jdbc:mysql://{0}:{1}/{2}?rewriteBatchedStatements=true",host,port,db);
//			url=PFDataHelper.FormatString("jdbc:mysql://{0}:{1}/{2}?{3}",host,port,db,String.join("&",PFDataHelper.MapSelect(urlParamDef,(k,v)->k+"="+v)));
//		}else if(PFSqlType.SqlServer==sqlTypeEnum) {
//			driverClassName="net.sourceforge.jtds.jdbc.Driver";
//			url=PFDataHelper.FormatString("jdbc:jtds:sqlserver://{0}:{1}/{2}",host,port,db);
//		}else if(PFSqlType.ClickHouse==sqlTypeEnum) {
//			driverClassName="ru.yandex.clickhouse.ClickHouseDriver";
//			url=PFDataHelper.FormatString("jdbc:clickhouse://{0}:{1}/{2}",host,port,db);//
//		}
		switch (sqlTypeEnum){
			case MySql:
			case Tidb:
				driverClassName="com.mysql.cj.jdbc.Driver";
				Map<String,String> urlParamDef=new HashMap<>();
				urlParamDef.put("rewriteBatchedStatements","true");//考虑要不要把 useSSL=false 设置为默认值
				if(null!=urlParam){urlParamDef.putAll(urlParam);}
				//url=PFDataHelper.FormatString("jdbc:mysql://{0}:{1}/{2}?rewriteBatchedStatements=true",host,port,db);
				url=SGDataHelper.FormatString("jdbc:mysql://{0}:{1}/{2}?{3}",host,port,db,String.join("&",SGDataHelper.MapSelect(urlParamDef,(k,v)->k+"="+v)));

				break;
			case SqlServer:
				driverClassName="net.sourceforge.jtds.jdbc.Driver";
				url=SGDataHelper.FormatString("jdbc:jtds:sqlserver://{0}:{1}/{2}",host,port,db);
				break;
			case ClickHouse:
				driverClassName="ru.yandex.clickhouse.ClickHouseDriver";
				url=SGDataHelper.FormatString("jdbc:clickhouse://{0}:{1}/{2}",host,port,db);//
				break;
			default:
				break;
		}
		this.username=username;
		this.password=password;
        this.ip=host + ":" + port;
        this.dbName=db;
		this.sqlType=sqlTypeEnum.toString();
	}
//	public PFJdbcBase(String sqlType,String host,int port,String db,String username,String password,Map<String,String> urlParam) {
//		PFSqlType sqlTypeEnum=StringToSqlType(sqlType);
//		String urlParamStr="";
//		if(null!=urlParam&&!urlParam.isEmpty()) {
//			urlParamStr="?"+String.join("&", PFDataHelper.MapSelect(urlParam,(k,v)->k+"="+v));
//		}
//		if(PFSqlType.MySql==sqlTypeEnum) {
//			driverClassName="com.mysql.jdbc.Driver";
//			url=PFDataHelper.FormatString("jdbc:mysql://{0}:{1}/{2}{3}",host,port,db,urlParamStr);
//		}else if(PFSqlType.SqlServer==sqlTypeEnum) {
//			driverClassName="net.sourceforge.jtds.jdbc.Driver";
//			url=PFDataHelper.FormatString("jdbc:jtds:sqlserver://{0}:{1}/{2}{3}",host,port,db,urlParamStr);
//		}else if(PFSqlType.ClickHouse==sqlTypeEnum) {
//			driverClassName="ru.yandex.clickhouse.ClickHouseDriver";
//			url=PFDataHelper.FormatString("jdbc:clickhouse://{0}:{1}/{2}{3}",host,port,db,urlParamStr);//
//		}
//		this.username=username;
//		this.password=password;
//		this.sqlType=sqlTypeEnum.toString();
//	}

	public String getSqlType() {
		return sqlType;
	}
	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}
	public SGSqlType GetSqlType() {
    	if(getDriverClassName().equals("ru.yandex.clickhouse.ClickHouseDriver")
				||"com.github.housepower.jdbc.ClickHouseDriver".equals(getDriverClassName())) {
            // 连接数据库
    		return SGSqlType.ClickHouse;
    	}
    	if(getDriverClassName().equals("net.sourceforge.jtds.jdbc.Driver")||"com.microsoft.sqlserver.jdbc.SQLServerDriver".equals(driverClassName)) {
            // 连接数据库
    		return SGSqlType.SqlServer;
    	}
    	if(getDriverClassName().equals("com.mysql.jdbc.Driver")
    			||getDriverClassName().equals("com.mysql.cj.jdbc.Driver")) {
            // 连接数据库
    		if(getSqlType()!=null&&(!"".equals(getSqlType()))) {
    			return SGSqlType.valueOf(getSqlType());
    		}
    		return SGSqlType.MySql;
    	}
    	return SGSqlType.MySql;
	}
	public static  SGSqlType StringToSqlType(String sqlType) {
		sqlType=sqlType.toLowerCase();
		if("mysql".equals(sqlType)) {
			return SGSqlType.MySql;
		}else if("sqlserver".equals(sqlType)) {
			return SGSqlType.SqlServer;
		}else if("clickhouse".equals(sqlType)) {
			return SGSqlType.ClickHouse;
		}else if("tidb".equals(sqlType)) {
			return SGSqlType.Tidb;
		}else if("hive".equals(sqlType)) {
			return SGSqlType.Hive;
		}
		return SGSqlType.MySql;
	}
	public static  boolean StringIsSqlType(String sqlType) {
		sqlType=sqlType.toLowerCase();
		if("mysql".equals(sqlType)) {
			return true;
		}else if("sqlserver".equals(sqlType)) {
			return true;
		}else if("clickhouse".equals(sqlType)) {
			return true;
		}else if("tidb".equals(sqlType)) {
			return true;
		}else if("hive".equals(sqlType)) {
			return true;
		}
		return false;
	}
	public String getUrlShortName() {
//		if(PFDataHelper.StringIsNullOrWhiteSpace(ip)) {
//			if(!PFDataHelper.StringIsNullOrWhiteSpace(url)) {
//				//从url如jdbc:mysql://cloud.perfect99.com:10129/mall_center_store111?useUnicode=true&characterEncoding=utf8
//				//中拆出ip和dbName
//				String tmpUrl=url;
//				int idx=tmpUrl.indexOf("//");
//				if(idx>-1) {
//					tmpUrl=tmpUrl.substring(idx+2);
//					int idx2=tmpUrl.indexOf("/");
//					if(idx2>-1) {
//						ip=tmpUrl.substring(0, idx2);
//						tmpUrl=tmpUrl.substring(idx2+1);
//						//到这里,tmpUrl格式如mall_center_store111?useUnicode
//						if(PFDataHelper.StringIsNullOrWhiteSpace(dbName)) {
//							int idx3=tmpUrl.indexOf("?");
//							if(idx3>-1) {
//								dbName=tmpUrl.substring(0,idx3);
//							}else {
//								dbName=tmpUrl;
//							}
//						}
//					}else {
//						ip=tmpUrl;
//					}
//				}
//			}
//		}
		fillEmptyPropertyByUrl();
		return ip+" "+dbName;
	}
    public PFJdbcBase Apply(PFJdbcBase src)
    {
        this.setUrl(src.getUrl());
        this.setDriverClassName(src.getDriverClassName());
        this.setUsername(src.getUsername());
        this.setPassword(src.getPassword());
        this.setDriverVersion(src.getDriverVersion());

        this.setIp(src.getIp());
        //this.setPort(src.getPort());
        this.setDbName(src.getDbName());
        this.setSqlType(src.getSqlType());
        this.setDbaMobile(src.getDbaMobile());
        this.setDbaName(src.getDbaName());
        this.setDbaEmail(src.getDbaEmail());
        return this;
    }
    

	@Override
	public String getUrl() {
        return url;
    }

	@Override
    public String getDriverClassName() {
        return driverClassName;
    }

	@Override
    public String getUsername() {
        return username;
    }


	@Override
    public String getPassword() {
        return password;
    }


	@Override
	public String getDriverVersion() {
		return driverVersion;
	}


	@Override
	public String getIp() {
		return ip;
	}


	@Override
	public String getHost() {
		if(null==ip) {return "";};
		String[] arr=ip.split(":");
		return arr.length>0?arr[0]:"127.0.0.1";
	}
	@Override
	public String getPort() {
		if(null==ip) {return "";};
		String[] arr=ip.split(":");
		return arr.length>1?arr[1]:"0";
	}
//
//	@Override
//	public void setPort(String port) {
//		this.port = port;
//	}

	@Override
	public String getDbName() {
		fillEmptyPropertyByUrl();
		return dbName;
	}

	@Override
	public String getDbaMobile() {
		return dbaMobile;
	}

	@Override
	public String getDbaName() {
		return dbaName;
	}

	@Override
	public String getDbaEmail() {
		return dbaEmail;
	}
	@Override
	public Boolean getIsMobileMessageConn() {
		return isMobileMessageConn;
	}
//	public void setIsMobileMessageConn(Boolean isMobileMessageConn) {
//		this.isMobileMessageConn = isMobileMessageConn;
//	}
	private void fillEmptyPropertyByUrl() {
		if(!SGDataHelper.StringIsNullOrWhiteSpace(url)) {
			//从url如jdbc:mysql://cloud.perfect99.com:10129/mall_center_store111?useUnicode=true&characterEncoding=utf8
			//中拆出ip和dbName
			String tmpUrl=url;
			int idx=tmpUrl.indexOf("//");
			if(idx>-1) {
				tmpUrl=tmpUrl.substring(idx+2);
				int idx2=tmpUrl.indexOf("/");
				if(idx2>-1) {
					if(null==ip) {
						ip=tmpUrl.substring(0, idx2);
					}
					tmpUrl=tmpUrl.substring(idx2+1);
					//到这里,tmpUrl格式如mall_center_store111?useUnicode
					if(SGDataHelper.StringIsNullOrWhiteSpace(dbName)) {
						int idx3=tmpUrl.indexOf("?");
						if(idx3>-1) {
							dbName=tmpUrl.substring(0,idx3);
						}else {
							dbName=tmpUrl;
						}
					}
				}else {
					if(null==ip) {
						ip=tmpUrl;
					}
				}
			}
		}
	}
}
