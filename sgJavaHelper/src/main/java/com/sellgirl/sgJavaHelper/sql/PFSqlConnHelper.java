package com.sellgirl.sgJavaHelper.sql;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.sellgirl.sgJavaHelper.SGClassLoader;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;


public class PFSqlConnHelper {
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PFSqlConnHelper.class);
	public static final Map<String, String> jdbcVersionMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			// put("5.1.6","com.mysql.jdbc.Driver");
			put("5.1.31", "com.mysql.jdbc.Driver");
			put("5.1.34", "com.mysql.jdbc.Driver");
//    		put("5.1.35","com.mysql.jdbc.Driver");
			// put("5.1.39","com.mysql.jdbc.Driver");
			put("5.1.46", "com.mysql.jdbc.Driver");
			// put("6.0.3","com.mysql.cj.jdbc.Driver");
			put("8.0.13", "com.mysql.jdbc.Driver");// 这个版本的类名似乎是com.mysql.cj.jdbc.Driver
			put("8.0.15", "com.mysql.jdbc.Driver");
			put("8.0.23", "com.mysql.cj.jdbc.Driver");// 测试通过
		}
	};

	public static final Map<String, Driver> tmpDriverMap = new HashMap<String, Driver>();

	public static class PFDriverInfo{
		/*
		 * 如 com.github.housepower.jdbc.ClickHouseDriver
		 */
		public String className=null;
		/**
		 * 如 clickhouse-native-jdbc-{version}.jar
		 */
		public String jarPathFormat=null;
		/**
		 * 如 1.7-stable
		 */
		public String version=null;
		/**
		 * @deprecated 这样并不能使驱动的子库正常调用
		 */
		@Deprecated
		public List<PFDriverInfo> extClasses=null;
	}

	public static List<PFDriverInfo> getMySqlDriverDefaultList(){
		List<PFDriverInfo> defaultClasses=new ArrayList<>();
		PFDriverInfo d1=new PFDriverInfo();
		d1.className="com.mysql.cj.jdbc.Driver";//此驱动好像不能动态加载,报错java.lang.NoClassDefFoundError: net/jpountz/lz4/LZ4Factory
		d1.version="8.0.23";
		d1.jarPathFormat="mysql-connector-java-{version}.jar";
		PFDriverInfo d2=new PFDriverInfo();
		d2.className="com.mysql.jdbc.Driver";//动态加载的1个前提是,每个className至少要定义1个jarPathFormat,否则不知道文件名
		d2.version="5.1.46";
		d2.jarPathFormat="mysql-connector-java-{version}.jar";
		defaultClasses.add(d1);
		defaultClasses.add(d2);
		return defaultClasses;
	}
	public static List<PFDriverInfo> getSqlServerDriverDefaultList(){
		List<PFDriverInfo> defaultClasses=new ArrayList<>();
		PFDriverInfo d1=new PFDriverInfo();
		d1.className="net.sourceforge.jtds.jdbc.Driver";//此驱动好像不能动态加载,报错java.lang.NoClassDefFoundError: net/jpountz/lz4/LZ4Factory
		d1.version="1.3.1";
		d1.jarPathFormat="jtds-{version}.jar";
		defaultClasses.add(d1);
		return defaultClasses;
	}
	public static List<PFDriverInfo> getClickHouseDriverDefaultList(){
		List<PFDriverInfo> defaultClasses=new ArrayList<>();
		PFDriverInfo d1=new PFDriverInfo();
		d1.className="ru.yandex.clickhouse.ClickHouseDriver";//此驱动好像不能动态加载,报错java.lang.NoClassDefFoundError: net/jpountz/lz4/LZ4Factory
		d1.version="0.1.54";
		d1.jarPathFormat="clickhouse-jdbc-{version}.jar";
		PFDriverInfo d2=new PFDriverInfo();
		d2.className="com.github.housepower.jdbc.ClickHouseDriver";//此驱动好像不能动态加载,报错java.lang.NoClassDefFoundError: net/jpountz/lz4/LZ4Factory
		d2.version="1.7-stable";
		d2.jarPathFormat="clickhouse-native-jdbc-{version}.jar";
		defaultClasses.add(d1);
		defaultClasses.add(d2);
		return defaultClasses;
	}
	/**
	 * mysql
	 * @return
	 * @throws Exception
	 */
	public static Connection dynamicGetConnByVersion(
			//String version, String className,String url,String user,String password
			ISGJdbc jdbc
	) throws Exception {
//		List<PFDriverInfo> defaultClasses=new ArrayList<>();
//		PFDriverInfo d1=new PFDriverInfo();
//		d1.className="com.mysql.cj.jdbc.Driver";//此驱动好像不能动态加载,报错java.lang.NoClassDefFoundError: net/jpountz/lz4/LZ4Factory
//		d1.version="8.0.23";
//		d1.jarPathFormat="mysql-connector-java-{version}.jar";
//		PFDriverInfo d2=new PFDriverInfo();
//		d2.className="com.mysql.jdbc.Driver";//动态加载的1个前提是,每个className至少要定义1个jarPathFormat,否则不知道文件名
//		d2.version="5.1.46";
//		d2.jarPathFormat="mysql-connector-java-{version}.jar";
//		defaultClasses.add(d1);
//		defaultClasses.add(d2);

		return getConnByDefaultDriverInfo(jdbc,getMySqlDriverDefaultList());
	}
	public static Connection dynamicGetSqlServerConnByVersion(
			//String version, String className,String url,String user,String password
			ISGJdbc jdbc) throws Exception {

//		List<PFDriverInfo> defaultClasses=new ArrayList<>();
//		PFDriverInfo d1=new PFDriverInfo();
//		d1.className="net.sourceforge.jtds.jdbc.Driver";//此驱动好像不能动态加载,报错java.lang.NoClassDefFoundError: net/jpountz/lz4/LZ4Factory
//		d1.version="1.3.1";
//		d1.jarPathFormat="jtds-{version}.jar";
//		defaultClasses.add(d1);
//
////		try {
////			Driver d=MycatMulitJdbcVersionTest.dynamicGetJdbcDriverByVersion(version, className, "sqlserver", defaultClasses);
////			Properties info = new Properties();
////			info.put("user", user);
////			info.put("password", password);
////			return d.connect(url,
////					info);
////		}catch (Exception e){
////			return DriverManager.getConnection(url, user, password);
////		}
		return getConnByDefaultDriverInfo(jdbc,getSqlServerDriverDefaultList());
	}
	public static Connection dynamicGetClickHouseConnByVersion(
			//String version, String className,String url,String user,String password
			ISGJdbc jdbc) throws Exception {

//		List<PFDriverInfo> defaultClasses=new ArrayList<>();
//		PFDriverInfo d1=new PFDriverInfo();
//		d1.className="ru.yandex.clickhouse.ClickHouseDriver";//此驱动好像不能动态加载,报错java.lang.NoClassDefFoundError: net/jpountz/lz4/LZ4Factory
//		d1.version="0.1.54";
//		d1.jarPathFormat="clickhouse-jdbc-{version}.jar";
//		PFDriverInfo d2=new PFDriverInfo();
//		d2.className="com.github.housepower.jdbc.ClickHouseDriver";//此驱动好像不能动态加载,报错java.lang.NoClassDefFoundError: net/jpountz/lz4/LZ4Factory
//		d2.version="1.7-stable";
//		d2.jarPathFormat="clickhouse-native-jdbc-{version}.jar";
//		defaultClasses.add(d1);
//		defaultClasses.add(d2);
//
////		try {
////			Driver d=MycatMulitJdbcVersionTest.dynamicGetJdbcDriverByVersion(version, className, "clickhouse", defaultClasses);
////			Properties info = new Properties();
////			info.put("user", user);
////			info.put("password", password);
////			return d.connect(url,
////					info);
////		}catch (Exception e){
////			return DriverManager.getConnection(url, user, password);
////		}
		return getConnByDefaultDriverInfo(jdbc,getClickHouseDriverDefaultList());
	}
	private static Connection getConnByDefaultDriverInfo(ISGJdbc jdbc,List<PFDriverInfo> defaultClasses) throws SQLException {
		try {
			Driver d=dynamicGetJdbcDriverByVersion(jdbc.getDriverVersion(), jdbc.getDriverClassName(),
					jdbc.GetSqlType().toString(), defaultClasses);
			Properties info = new Properties();
			info.put("user", jdbc.getUsername());
			info.put("password", jdbc.getPassword());
			return d.connect(jdbc.getUrl(),
					info);
		}catch (Exception e){
			e.printStackTrace();
			return DriverManager.getConnection(jdbc.getUrl(), jdbc.getUsername(), jdbc.getPassword());
		}
	}
	/**
	 * 暂时只比对驱动的类名,不比对大小版本号
	 *
	 * 应改为private
	 * @param version
	 * @param className
	 * @return
	 */
	public static Driver getLoadedDriver(String version, String className) {
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
//			//如果要考虑版本号MajorVersion和MinorVersion,可能需要先卸载同类名的其它版本号(不确定),还要了解一直用的方式DriverManager.getConnection是怎么处理同类名驱动的
//            if (driver.getClass().getName().equals(className)) {
//                return driver;
//            }
			if(DriverShim.isSqlDriverMatch(driver, version, className)) {
				return driver;
			}
		}
		return null;
	}
	/**
	 * 在x-flink中发现pom中有多种驱动时(mysql和clickhouse),有时会似乎有冲突,DriverManager.getConnection连接mysql却是clickhouse的驱动报错
	 * 所以现在改为返回Driver的方式,尝试不使用DriverManager.getConnection
	 * @param version
	 * @param className
	 * @param sqlType
	 * @param defaultClasses
	 * @throws Exception
	 */
	public static Driver dynamicGetJdbcDriverByVersion(String version, String className,String sqlType,List<PFDriverInfo> defaultClasses) throws Exception {
		if (null == version) {
			version = "";
		}
//		if(null==defaultClasses||defaultClasses.isEmpty()){
//			throw new Exception("defaultClasses不能为空");//这里不能为空,其中一个原因是,一定需要jarPathFormat来计算jar路径
//		}
		String key = className + "_" + version;
//		if (currentSqlDriver.containsKey(sqlType)&&key.equals(currentSqlDriver.get(sqlType))) {
//			return;
//		}
		if(PFSqlConnHelper.tmpDriverMap.containsKey(key)){
			return PFSqlConnHelper.tmpDriverMap.get(key);
		}
		Driver d=getLoadedDriver(version,  className);
		if(null==d) {
			if(null==defaultClasses){
				defaultClasses=new ArrayList<>();
			}
			List<PFDriverInfo> classList=new ArrayList<>();

			if(!defaultClasses.isEmpty()){
				PFDriverInfo matched1=null;
				//类名和版本都匹配的驱动
				for(PFDriverInfo i:defaultClasses){
					if(className.equals(i.className)&&version.equals(i.version)){
						matched1=i;
						defaultClasses.remove(i);
						break;
					}
				}
				if(null==matched1){
					//找类名匹配的
					PFDriverInfo tmp= SGDataHelper.ListFirstOrDefault(defaultClasses, a->className.equals(a.className));
					if(null!=tmp){
						matched1=new PFDriverInfo();
						matched1.className=className;
						matched1.version=version;
						matched1.jarPathFormat=tmp.jarPathFormat;
					}else{
						matched1=new PFDriverInfo();
						matched1.className=className;
						matched1.version=version;
					}
				}
				//到这里,matched1一定有值
				classList.add(matched1);
				//类名匹配但版本不匹配的驱动
//				for(int i=defaultClasses.size()-1;i>=0;i--){
//					if(className.equals(defaultClasses.get(i).className)&&!version.equals(defaultClasses.get(i).version)){
//						classList.add(defaultClasses.get(i));
//						defaultClasses.remove(i);
//					}
//				}
				for(PFDriverInfo i:defaultClasses){
					if(className.equals(i.className)&&!version.equals(i.version)){
						classList.add(i);
					}
				}
				//类名和版本不匹配的驱动
				for(PFDriverInfo i:defaultClasses){
					if(!className.equals(i.className)&&!version.equals(i.version)){
						classList.add(i);
					}
				}
//				if(null==defaultClass){
//					defaultClass=defaultClasses.get(0);
//				}
			}


			d = (Driver) doLoadClass(classList
			);
			//return d;

//			Driver d2=getLoadedDriver(version,  className);//有的驱动好像是loadClass时就已经加载进去的了,如新版本的mysql驱动
//			if(null==d2){
//				DriverShim driver = new DriverShim(d);
//				DriverManager.registerDriver(driver);
//				//java.sql.DriverManager.registerDriver(d2);//这样注册好像不行,可能和classLoader有关
//				//java.sql.DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());//mysql是这样注册的
//			}

		}
		PFSqlConnHelper.tmpDriverMap.put(key, d);
		//currentClickHouseDriver = key;
		MycatMulitJdbcVersionTest.currentSqlDriver.put(sqlType,key);
		LOGGER.info(d.getClass().getName()+" load success MajorVersion["+d.getMajorVersion()+"] MinorVersion["+d.getMinorVersion()+"]");
		return d;
	}

	public static void dynamicUnLoadJdbcByVersion(String version, String className) throws SQLException {
		// DriverManager.deregisterDriver(tmpDriverMap.get(version));
		DriverManager.deregisterDriver(tmpDriverMap.get(className + "_" + version));
	}
	
	public static Object doLoadClass(List<PFDriverInfo> classList) throws Exception {
		Class<?> dClass = null;

		SGRef<Integer> tryStepRef=new SGRef<Integer>(0);


		for(PFDriverInfo i:classList){
			SGRef<Integer> tryStepTmpRef=new SGRef<Integer>(0);
			dClass=SGClassLoader.doLoadClass(SGDataHelper.StringIsNullOrWhiteSpace(i.jarPathFormat)?"":i.jarPathFormat.replace("{version}",i.version),
					i.className,tryStepTmpRef);
			tryStepRef.SetValue(tryStepRef.GetValue()+tryStepTmpRef.GetValue());
			if(null!=dClass){
				LOGGER.info("load class:" + i.className + "_"+i.version+" SUCCESS, step:" + String.valueOf(tryStepTmpRef.GetValue()));
				break;
			}
		}

		int tryStep=tryStepRef.GetValue();
		if (null == dClass) {
			throw new Exception(
					"MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion ERROR,Class.forName()找不到class:"
							+ classList.get(0).className + ",step:" + String.valueOf(tryStep));
		} else {
			//LOGGER.info("load class:" + className + " SUCCESS, step:" + String.valueOf(tryStep));
		}
//		return dClass.newInstance();
//		return dClass.getConstructor().newInstance();
		return dClass.getDeclaredConstructor().newInstance();
	}

}
