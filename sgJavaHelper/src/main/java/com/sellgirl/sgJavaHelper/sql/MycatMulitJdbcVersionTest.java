package com.sellgirl.sgJavaHelper.sql;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import com.sellgirl.sgJavaHelper.SGClassLoader;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.sql.PFSqlConnHelper.PFDriverInfo;

/**
 * 改用SqlConnHelper
 */
@Deprecated
public class MycatMulitJdbcVersionTest {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MycatMulitJdbcVersionTest.class);

	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/testdb?serverTimezone=UTC&useSSL=false";
	private static final String USER = "root";
	private static final String PASSWORD = "mysql";
	// private static final Map<String, String> jdbcVersionMap = new HashMap<String,
	// String>();
//	public static final Map<String, String> jdbcVersionMap = new HashMap<String, String>() {
//		private static final long serialVersionUID = 1L;
//		{
//			// put("5.1.6","com.mysql.jdbc.Driver");
//			put("5.1.31", "com.mysql.jdbc.Driver");
//			put("5.1.34", "com.mysql.jdbc.Driver");
////    		put("5.1.35","com.mysql.jdbc.Driver");
//			// put("5.1.39","com.mysql.jdbc.Driver");
//			put("5.1.46", "com.mysql.jdbc.Driver");
//			// put("6.0.3","com.mysql.cj.jdbc.Driver");
//			put("8.0.13", "com.mysql.jdbc.Driver");// 这个版本的类名似乎是com.mysql.cj.jdbc.Driver
//			put("8.0.15", "com.mysql.jdbc.Driver");
//			put("8.0.23", "com.mysql.cj.jdbc.Driver");// 测试通过
//		}
//	};
//	private static final Map<String, Driver> tmpDriverMap = new HashMap<String, Driver>();
	private static String currentMySqlDriver = "";
	private static String currentSqlServerDriver = "";
	private static String currentClickHouseDriver = "";
	public static Map<String,String> currentSqlDriver = new HashMap<>();

	/**
	 * mysql方式
	 * 
	 * @param version
	 * @param className
	 * @throws Exception
	 */
	public static void dynamicLoadJdbcByVersion(String version, String className) throws Exception {

		doDynamicLoadJdbcByVersion(version,className,"mysql",PFSqlConnHelper.getMySqlDriverDefaultList());
	}
//	public static Connection dynamicGetConnByVersion(String version, String className,String user,String password) throws Exception {
//		List<PFDriverInfo> defaultClasses=new ArrayList<>();
//		PFDriverInfo d1=new PFDriverInfo();
//		d1.className="com.mysql.cj.jdbc.Driver";//此驱动好像不能动态加载,报错java.lang.NoClassDefFoundError: net/jpountz/lz4/LZ4Factory
//		d1.version="8.0.23";
//		d1.jarPathFormat="mysql-connector-java-{version}.jar";
//		defaultClasses.add(d1);
//		try {
//			Driver d=dynamicGetJdbcDriverByVersion(version, className, "mysql", defaultClasses);
//			java.util.Properties info = new java.util.Properties();
//			info.put("user", user);
//			info.put("password", password);
//			return d.connect("jdbc:mysql://uat-cloud.perfect99.com:10129/test?rewriteBatchedStatements=true",
//					info);
//		}catch (Exception e){
//
//		}
//	}
//	/**
//	 * 测试不转为DriverShim,看看能不能用.
//	 * 
//	 * 结果是不套1层DriverShim,就不能用
//	 * @param version
//	 * @param className
//	 * @throws Exception
//	 */
//	@Deprecated
//	public static void dynamicLoadJdbcByVersionOriginDriver(String version, String className) throws Exception {
//		if (null == version) {
//			version = "";
//		}
//		String key = className + "_" + version;
//		if (key.equals(currentMySqlDriver)) {
//			return;
//		}
//		Driver d=getLoadedDriver(version,  className);
//		if(null==d) {
//			d = (Driver) doLoadClass("mysql-connector-java-" + version + ".jar", className,
//					"com.mysql.cj.jdbc.Driver");
//
//			DriverManager.registerDriver(d);
//			
////			//奇怪,如果不套一层,好像加不进去.
////			//后来看到好像是DriverManager.getDrivers里面判断了ClassLoader,所以读不到其它classLoader得到的driver
////			DriverShim driver = new DriverShim(d); 
////			DriverManager.registerDriver(driver);
//		}
//		tmpDriverMap.put(key, d);
//		currentMySqlDriver = key;
//	}

	/**
	 * 动态加载SqlServer驱动.
	 * 
	 * 使用方法:
	 * MycatMulitJdbcVersionTest.dynamicLoadSqlServerJdbcByVersion("","net.sourceforge.jtds.jdbc.Driver")
	 * 
	 * 实测可以动态加载jtds-1.3.1.jar
	 * 
	 * @param version
	 * @param className
	 * @throws Exception
	 */
	public static void dynamicLoadSqlServerJdbcByVersion(String version, String className) throws Exception {

		doDynamicLoadJdbcByVersion(version,className,"sqlserver",PFSqlConnHelper.getSqlServerDriverDefaultList());
	}


	/**
	 * clickhouse的驱动,动态加载时好像都不正常,需要引入pom中
	 *
	 * 此方法好像有bug,pom里有驱动,但lib目录不放驱动的jar时,好像异常 -- benjamin todo
	 * @param version
	 * @param className
	 * @throws Exception
	 */
	public static void dynamicLoadClickHouseJdbcByVersion(String version, String className) throws Exception {
		doDynamicLoadJdbcByVersion(version,className,"clickhouse",PFSqlConnHelper.getClickHouseDriverDefaultList());
	}
	//测试子引用extClasses的情况
	@Deprecated
	public static void dynamicLoadClickHouseJdbcByVersion2(String version, String className) throws Exception {

		List<PFDriverInfo> defaultClasses=new ArrayList<>();
		PFDriverInfo d1=new PFDriverInfo();
		d1.className="com.github.housepower.jdbc.ClickHouseDriver";//此驱动好像不能动态加载,报错java.lang.NoClassDefFoundError: net/jpountz/lz4/LZ4Factory
		d1.version="1.7-stable";
		d1.jarPathFormat="clickhouse-native-jdbc-{version}.jar";
		d1.extClasses=new ArrayList<>();
		PFDriverInfo e1=new PFDriverInfo();
		e1.className="net.jpountz.lz4.LZ4Factory";//此驱动好像不能动态加载,报错java.lang.NoClassDefFoundError: net/jpountz/lz4/LZ4Factory
		e1.version="1.3.0";
		//e1.jarPathFormat="lz4-{version}-sources.jar";
		e1.jarPathFormat="lz4-{version}.jar";
		d1.extClasses.add(e1);
		defaultClasses.add(d1);
		//doDynamicLoadJdbcByVersion(version,className,"clickhouse",defaultClasses);

		String sqlType="clickhouse";

		if (null == version) {
			version = "";
		}
		if(null==defaultClasses||defaultClasses.isEmpty()){
			throw new Exception("defaultClasses不能为空");
		}
		String key = className + "_" + version;
		if (currentSqlDriver.containsKey(sqlType)&&key.equals(currentSqlDriver.get(sqlType))) {
			return;
		}
		Driver d= PFSqlConnHelper.getLoadedDriver(version,  className);
		if(null==d) {
//			String notNullVersion=PFDataHelper.StringIsNullOrWhiteSpace(version)?"1.3.1":version;
//			d = (Driver) doLoadClass("jtds-" + notNullVersion + ".jar", className, "net.sourceforge.jtds.jdbc.Driver");
			PFDriverInfo defaultClass=null;
			if(!defaultClasses.isEmpty()){
				for(PFDriverInfo i:defaultClasses){
					if(className.equals(i.className)){
						defaultClass=i;
						break;
					}
				}
				if(null==defaultClass){
					defaultClass=defaultClasses.get(0);
				}
			}
			String defaultVersion=defaultClass.version;
			String notNullVersion=SGDataHelper.StringIsNullOrWhiteSpace(version)?defaultVersion:version;
			String defaultClassName=defaultClass.className;
			boolean isEqualDefaultVersion=defaultVersion.equals(notNullVersion)&&defaultClassName.equals(className);

			//这样似乎没有用,因为jdbc库初始化子引用的类时,还是不行(这样调用的private final LZ4Compressor lz4Compressor = LZ4Factory.safeInstance().fastCompressor();)
			// ,除非可以把它放入ClassLoader里面
			if(null!=defaultClass.extClasses&&!defaultClass.extClasses.isEmpty()){
				for(PFDriverInfo item:defaultClass.extClasses){
					SGRef<Integer> tryStepRef=new SGRef<Integer>(0);
					Object dClass=SGClassLoader.doLoadClass(item.jarPathFormat.replace("{version}",item.version),  item.className,tryStepRef);
					//Thread.currentThread().getContextClassLoader().;
					String aa="aa";
				}
			}
			//com.github.housepower.jdbc.buffer.CompressedBuffedWriter


			d = (Driver) doLoadClass(defaultClass.jarPathFormat.replace("{version}",notNullVersion), className,
					isEqualDefaultVersion?"":defaultClass.jarPathFormat.replace("{version}",defaultVersion),
					isEqualDefaultVersion?"":defaultClassName
			);
			Driver d2= PFSqlConnHelper.getLoadedDriver(version,  className);//这个区动好像是loadClass时就已经加载进去的了
			if(null==d2){
				DriverShim driver = new DriverShim(d);
				DriverManager.registerDriver(driver);

				//DriverManager.registerDriver(d);
			}

		}
		PFSqlConnHelper.tmpDriverMap.put(key, d);
		//currentClickHouseDriver = key;
		currentSqlDriver.put(sqlType,key);
	}
	@Deprecated
	private static void doDynamicLoadJdbcByVersionOld(String version, String className,String sqlType,List<PFDriverInfo> defaultClasses) throws Exception {
		if (null == version) {
			version = "";
		}
		if(null==defaultClasses||defaultClasses.isEmpty()){
			throw new Exception("defaultClasses不能为空");//这里不能为空,其中一个原因是,一定需要jarPathFormat来计算jar路径
		}
		String key = className + "_" + version;
		if (currentSqlDriver.containsKey(sqlType)&&key.equals(currentSqlDriver.get(sqlType))) {
			return;
		}
		Driver d= PFSqlConnHelper.getLoadedDriver(version,  className);
		if(null==d) {
//			String notNullVersion=PFDataHelper.StringIsNullOrWhiteSpace(version)?"1.3.1":version;
//			d = (Driver) doLoadClass("jtds-" + notNullVersion + ".jar", className, "net.sourceforge.jtds.jdbc.Driver");
			PFDriverInfo defaultClass=null;
			if(!defaultClasses.isEmpty()){
				for(PFDriverInfo i:defaultClasses){
					if(className.equals(i.className)){
						defaultClass=i;
						break;
					}
				}
				if(null==defaultClass){
					defaultClass=defaultClasses.get(0);
				}
			}
			String defaultVersion=defaultClass.version;
			String notNullVersion=SGDataHelper.StringIsNullOrWhiteSpace(version)?defaultVersion:version;
			String defaultClassName=defaultClass.className;
			boolean isEqualDefaultVersion=defaultVersion.equals(notNullVersion)&&defaultClassName.equals(className);
			d = (Driver) doLoadClass(defaultClass.jarPathFormat.replace("{version}",notNullVersion), className,
					isEqualDefaultVersion?"":defaultClass.jarPathFormat.replace("{version}",defaultVersion),
					isEqualDefaultVersion?"":defaultClassName
			);
			//这里应该把其它默认驱动都试试,-- benjamin todo
			Driver d2= PFSqlConnHelper.getLoadedDriver(version,  className);//这个区动好像是loadClass时就已经加载进去的了
			if(null==d2){
				DriverShim driver = new DriverShim(d);
				DriverManager.registerDriver(driver);
			}

		}
		PFSqlConnHelper.tmpDriverMap.put(key, d);
		//currentClickHouseDriver = key;
		currentSqlDriver.put(sqlType,key);
	}

	/**
	 * 可以用 SqlConnHelper.dynamicGetJdbcDriverByVersion 方法取代
	 * @param version
	 * @param className
	 * @param sqlType
	 * @param defaultClasses
	 * @throws Exception
	 */
	@Deprecated
	private static void doDynamicLoadJdbcByVersion(String version, String className,String sqlType,List<PFDriverInfo> defaultClasses) throws Exception {


		Driver d= PFSqlConnHelper.dynamicGetJdbcDriverByVersion(version,  className, sqlType, defaultClasses);
		Driver d2= PFSqlConnHelper.getLoadedDriver(version,  className);//有的驱动好像是loadClass时就已经加载进去的了,如新版本的mysql驱动
		if(null==d2){
				DriverShim driver = new DriverShim(d);
				DriverManager.registerDriver(driver);
				//java.sql.DriverManager.registerDriver(d2);//这样注册好像不行,可能和classLoader有关
				//java.sql.DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());//mysql是这样注册的
		}
	}
//
//	/**
//	 * 在x-flink中发现pom中有多种驱动时(mysql和clickhouse),有时会似乎有冲突,DriverManager.getConnection连接mysql却是clickhouse的驱动报错
//	 * 所以现在改为返回Driver的方式,尝试不使用DriverManager.getConnection
//	 * @param version
//	 * @param className
//	 * @param sqlType
//	 * @param defaultClasses
//	 * @throws Exception
//	 */
//	public static Driver dynamicGetJdbcDriverByVersion(String version, String className,String sqlType,List<PFDriverInfo> defaultClasses) throws Exception {
//		if (null == version) {
//			version = "";
//		}
////		if(null==defaultClasses||defaultClasses.isEmpty()){
////			throw new Exception("defaultClasses不能为空");//这里不能为空,其中一个原因是,一定需要jarPathFormat来计算jar路径
////		}
//		String key = className + "_" + version;
////		if (currentSqlDriver.containsKey(sqlType)&&key.equals(currentSqlDriver.get(sqlType))) {
////			return;
////		}
//		if(SqlConnHelper.tmpDriverMap.containsKey(key)){
//			return SqlConnHelper.tmpDriverMap.get(key);
//		}
//		Driver d=getLoadedDriver(version,  className);
//		if(null==d) {
//			if(null==defaultClasses){
//				defaultClasses=new ArrayList<>();
//			}
//			List<PFDriverInfo> classList=new ArrayList<>();
//
//			if(!defaultClasses.isEmpty()){
//				PFDriverInfo matched1=null;
//				//类名和版本都匹配的驱动
//				for(PFDriverInfo i:defaultClasses){
//					if(className.equals(i.className)&&version.equals(i.version)){
//						matched1=i;
//						defaultClasses.remove(i);
//						break;
//					}
//				}
//				if(null==matched1){
//					//找类名匹配的
//					PFDriverInfo tmp=PFDataHelper.ListFirstOrDefault(defaultClasses,a->className.equals(a.className));
//					if(null!=tmp){
//						matched1=new PFDriverInfo();
//						matched1.className=className;
//						matched1.version=version;
//						matched1.jarPathFormat=tmp.jarPathFormat;
//					}else{
//						matched1=new PFDriverInfo();
//						matched1.className=className;
//						matched1.version=version;
//					}
//				}
//				//到这里,matched1一定有值
//				classList.add(matched1);
//				//类名匹配但版本不匹配的驱动
////				for(int i=defaultClasses.size()-1;i>=0;i--){
////					if(className.equals(defaultClasses.get(i).className)&&!version.equals(defaultClasses.get(i).version)){
////						classList.add(defaultClasses.get(i));
////						defaultClasses.remove(i);
////					}
////				}
//				for(PFDriverInfo i:defaultClasses){
//					if(className.equals(i.className)&&!version.equals(i.version)){
//						classList.add(i);
//					}
//				}
//				//类名和版本不匹配的驱动
//				for(PFDriverInfo i:defaultClasses){
//					if(!className.equals(i.className)&&!version.equals(i.version)){
//						classList.add(i);
//					}
//				}
////				if(null==defaultClass){
////					defaultClass=defaultClasses.get(0);
////				}
//			}
//
//
//			d = (Driver) doLoadClass(classList
//			);
//			//return d;
//
////			Driver d2=getLoadedDriver(version,  className);//有的驱动好像是loadClass时就已经加载进去的了,如新版本的mysql驱动
////			if(null==d2){
////				DriverShim driver = new DriverShim(d);
////				DriverManager.registerDriver(driver);
////				//java.sql.DriverManager.registerDriver(d2);//这样注册好像不行,可能和classLoader有关
////				//java.sql.DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());//mysql是这样注册的
////			}
//
//		}
//		SqlConnHelper.tmpDriverMap.put(key, d);
//		//currentClickHouseDriver = key;
//		currentSqlDriver.put(sqlType,key);
//		LOGGER.info(d.getClass().getName()+" load success MajorVersion["+d.getMajorVersion()+"] MinorVersion["+d.getMinorVersion()+"]");
//		return d;
//	}

//	/**
//	 *
//	 * @param jarPath   如jtds-1.3.1.jar
//	 * @param className 如net.sourceforge.jtds.jdbc.Driver
//	 * @throws Exception
//	 */
//	@Deprecated
//	public static Object doLoadClassOld(String jarPath, String className, String defaultClassName) throws Exception {
//		Class<?> dClass = null;
//		int tryStep = 0;
//		try {
//			tryStep++;
//			// 以前是用这个方法,感觉可以从实际文件里加载class?
//			// 这种方式是在pom里面写了依赖就会加载到,包含开发运行和java -jar运行 -- 20220923
//			// 这种方式,在单元测试中,可以加载到/lib文件夹里的文件 -- 20221102
//			URL u = new URL("jar:file:lib/" + jarPath + "!/");
////          String className = jdbcVersionMap.get(mysqlJdbcFile);
//			URLClassLoader ucl = new URLClassLoader(new URL[] { u });
//			dClass = Class.forName(className, true, ucl);
//		} catch (Exception e) {
//		}
//
//		if (null == dClass) {
//			try {
//				// 非spring项目可以把mysql的jar放到resource文件夹下自动加载，如放到
//				// resources/jar/mysql-connector-java-8.0.15.jar (实测可用)
//				tryStep++;
//				URL url = Thread.currentThread().getContextClassLoader()
//						.getResource(Paths.get("jar", jarPath).toString());
//				URLClassLoader ucl = new URLClassLoader(new URL[] { url });
//				try {
//					dClass = Class.forName(className, true, ucl);
//				} catch (Exception e) {
//					dClass = Class.forName(defaultClassName, true, ucl);
//				}
//			} catch (Exception e) {
//				// PFDataHelper.WriteError(new Throwable(),e);
//			}
//		}
//
//		// 参考https://blog.csdn.net/weixin_35048266/article/details/114714311
//		if (null == dClass) {
//			try {
//				// 非spring项目可以把mysql的jar放到resource文件夹下自动加载，如放到
//				// resources/jar/mysql-connector-java-8.0.15.jar (实测可用)
//				// 奇怪的是,这种方式加载 sqlserver的驱动 net.sourceforge.jtds.jdbc.Driver时会报错
//				tryStep++;
//				URL url = MycatMulitJdbcVersionTest.class.getResource(Paths.get("jar", jarPath).toString());
//				try (URLClassLoader ucl = new URLClassLoader(new URL[] { url })) {
//					dClass = ucl.loadClass(defaultClassName);// "net.sourceforge.jtds.jdbc.Driver"
//				}
//			} catch (Exception e) {
//				// PFDataHelper.WriteError(new Throwable(),e);
//			}
//		}
//		if (null == dClass) {
//			try {
//				tryStep++;
//				// 后来在flink平台上执行时用上面方法get不了class,改用下面方法就可以(这种方式应该是在主jar包的pom中引用了mysql的库的情况)
//				dClass = Class.forName(className);
//			} catch (Exception e) {
//				// PFDataHelper.WriteError(new Throwable(),e);
//			}
//		}
//		if (null == dClass) {
//			throw new Exception(
//					"MycatMulitJdbcVersionTest.dynamicLoadSqlServerJdbcByVersion ERROR,Class.forName()找不到class:"
//							+ className + ",step:" + String.valueOf(tryStep));
//		} else {
//			LOGGER.info("load class:" + className + " SUCCESS, step:" + String.valueOf(tryStep));
//		}
//		return dClass.getDeclaredConstructor().newInstance();
//	}

	/**
	 * 用重载 doLoadClass(List<PFDriverInfo> classList)
	 * @param jarPath   如jtds-1.3.1.jar
	 * @param className 如net.sourceforge.jtds.jdbc.Driver
	 * @throws Exception
	 */
	@Deprecated
	public static Object doLoadClass(String jarPath, String className,String defaultJarPath, String defaultClassName) throws Exception {
		Class<?> dClass = null;

		SGRef<Integer> tryStepRef=new SGRef<Integer>(0);

		dClass=SGClassLoader.doLoadClass(jarPath,  className,tryStepRef);
		
		if(null==dClass&&null!=defaultClassName&&(!"".equals(defaultClassName))) {
			dClass=SGClassLoader.doLoadClass(defaultJarPath,  defaultClassName,tryStepRef);
		}
		int tryStep=tryStepRef.GetValue();
		if (null == dClass) {
			throw new Exception(
					"MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion ERROR,Class.forName()找不到class:"
							+ className + ",step:" + String.valueOf(tryStep));
		} else {
			LOGGER.info("load class:" + className + " SUCCESS, step:" + String.valueOf(tryStep));
		}
		return dClass.getDeclaredConstructor().newInstance();
	}
//	public static Object doLoadClass(List<PFDriverInfo> classList) throws Exception {
//		Class<?> dClass = null;
//
//		SGRef<Integer> tryStepRef=new SGRef<Integer>(0);
//
//
//		for(PFDriverInfo i:classList){
//			SGRef<Integer> tryStepTmpRef=new SGRef<Integer>(0);
//			dClass=SGClassLoader.doLoadClass(SGDataHelper.StringIsNullOrWhiteSpace(i.jarPathFormat)?"":i.jarPathFormat.replace("{version}",i.version),
//					i.className,tryStepTmpRef);
//			tryStepRef.SetValue(tryStepRef.GetValue()+tryStepTmpRef.GetValue());
//			if(null!=dClass){
//				LOGGER.info("load class:" + i.className + "_"+i.version+" SUCCESS, step:" + String.valueOf(tryStepTmpRef.GetValue()));
//				break;
//			}
//		}
//
//		int tryStep=tryStepRef.GetValue();
//		if (null == dClass) {
//			throw new Exception(
//					"MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion ERROR,Class.forName()找不到class:"
//							+ classList.get(0).className + ",step:" + String.valueOf(tryStep));
//		} else {
//			//LOGGER.info("load class:" + className + " SUCCESS, step:" + String.valueOf(tryStep));
//		}
//		return dClass.getDeclaredConstructor().newInstance();
//	}
//	/**
//	 * 有些情况读不到,如:
//	 * 1.子jar放到resource,打成合并主jar,后用java -jar运行时,由于是嵌套jar,所以不能加载到
//	 * 
//	 * 
//	 * @param jarPath
//	 * @param className
//	 * @param tryStepRef
//	 * @return
//	 * @throws Exception
//	 * @deprecated 改用 SGClassLoader.doLoadClass
//	 */
//	@Deprecated
//	public static Class<?> doLoadClass2(String jarPath, String className,SGRef<Integer> tryStepRef) throws Exception {
//		Class<?> dClass = null;
//		
//		//注意,不建议嵌套jar包(如把jar放到resource里面打包),因为这样做很可能不兼容,如下面的u4,很可能不兼容(当java -jar执行主包时)
//		
//		URL u = new URL("jar:file:lib/" + jarPath + "!/");
//
//		// 非spring项目可以把mysql的jar放到resource文件夹下自动加载，如放到
//		// resources/jar/mysql-connector-java-8.0.15.jar (实测可用)
//		// 奇怪了,今天用win10测试这种方式无法读取,直接放到resources文件夹下才行 -- benjamin 20221202
//		//    明白了,这种方式确实能读到u2(不为null),只是后面用class.forName时报错,因为不支持jar嵌套
//		URL u2 = Thread.currentThread().getContextClassLoader()
//				.getResource(Paths.get("jar", jarPath).toString());
//		
//		// 非spring项目可以把mysql的jar放到resource文件夹下自动加载，如放到
//		// resources/jar/mysql-connector-java-8.0.15.jar (实测可用)
//		// 奇怪的是,这种方式加载 sqlserver的驱动 net.sourceforge.jtds.jdbc.Driver时会报错
//		URL u3 = MycatMulitJdbcVersionTest.class.getResource(Paths.get("jar", jarPath).toString());
//		URL u4 = new URL("jar:file:jar/" + jarPath + "!/");//这个未测试
//		//resources/*.jar
//		//win10 cmd 上 java -jar 虽然能读到此url(不为空),但是loadClass却报错
//		URL u5 = Thread.currentThread().getContextClassLoader().getResource(jarPath);//这个未测试
//		
//		//注意
//		//1.如果某个url为空,Class.forName时会报null异常
//		//2.url的顺序没有影响
//		
//		List<URL> urls=new ArrayList<URL>();
//		if(null!=u) {urls.add(u);}
//		if(null!=u2) {urls.add(u2);}
//		if(null!=u3) {urls.add(u3);}
//		if(null!=u4) {urls.add(u4);}
//		if(null!=u5) {urls.add(u5);}
//		//注意,DriverManager.getDrivers()获得的驱动好像和ClassLoader有关,所以可能要考虑url的顺序(不确定)
//		
//		//URLClassLoader ucl = new URLClassLoader(new URL[] { u,u2,u3 });
//		URLClassLoader ucl = new URLClassLoader(urls.toArray(new URL[urls.size()]));
//		
//
//		int tryStep = tryStepRef.GetValue();
//		//Class.forName方式
//		try {
////			tryStep++;
//			tryStep=1;
//			dClass = Class.forName(className, true, ucl);
//		} catch (Exception e) {
//		}
//
//		//URLClassLoader.loadClass方式
//		//参考https://blog.csdn.net/weixin_35048266/article/details/114714311
//		if (null == dClass) {
//			try {
//				//tryStep++;
//				tryStep=2;
//				dClass = ucl.loadClass(className);// "net.sourceforge.jtds.jdbc.Driver"
//			} catch (Exception e) {
//			}
//		}
//				
//		//不用url的方式
//		if (null == dClass) {
//			try {
//				//tryStep++;
//				tryStep=3;
//				// 后来在flink平台上执行时用上面方法get不了class,改用下面方法就可以(这种方式应该是在主jar包的pom中引用了mysql的库的情况)
//				dClass = Class.forName(className);
//			} catch (Exception e) {
//			}
//		}
//
//		//flink项目有用这种方式(暂不知道有没有跟其它方式重复)
//		if (null == dClass) {
//			try {
//				//tryStep++;
//				tryStep=4;
//				dClass = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
//			} catch (Exception e) {
//			}
//		}
//		tryStepRef.SetValue(tryStep);
//		return dClass;
//	}
	
//
//	public static void dynamicUnLoadJdbcByVersion(String version, String className) throws SQLException {
//		// DriverManager.deregisterDriver(tmpDriverMap.get(version));
//		DriverManager.deregisterDriver(tmpDriverMap.get(className + "_" + version));
//	}

	public static Driver getDriverByVersion(String version) throws SQLException {
		return DriverManager.getDriver("jdbc:mysql://172.16.1.246:10010/sale");
		// return
		// DriverManager.getDriver("jar:file:lib/mysql-connector-java-"+version+".jar!/");
	}

	public static void dynamicLoadJdbcByFileName(String fileName, String className) throws Exception {
		// 这样读的话,发包后要复制lib目录(尝试打进resources失败)
		URL u = new URL("jar:file:lib/" + fileName + ".jar!/");
//        String className = jdbcVersionMap.get(mysqlJdbcFile);
		URLClassLoader ucl = new URLClassLoader(new URL[] { u });
		// Driver d = (Driver)Class.forName(className, true, ucl).newInstance();
		Driver d = (Driver) Class.forName(className, true, ucl).getDeclaredConstructor().newInstance();
		DriverShim driver = new DriverShim(d);
		DriverManager.registerDriver(driver);
		PFSqlConnHelper.tmpDriverMap.put(className, driver);
	}

	// 动态加载jdbc驱动
	@Deprecated
	private static void dynamicLoadJdbc(String mysqlJdbcFile) throws Exception {
		URL u = new URL("jar:file:lib/" + mysqlJdbcFile + "!/");
		String classname = PFSqlConnHelper.jdbcVersionMap.get(mysqlJdbcFile);
		URLClassLoader ucl = new URLClassLoader(new URL[] { u });
		// Driver d = (Driver)Class.forName(classname, true, ucl).newInstance();
		Driver d = (Driver) Class.forName(classname, true, ucl).getDeclaredConstructor().newInstance();
		DriverShim driver = new DriverShim(d);
		DriverManager.registerDriver(driver);
		PFSqlConnHelper.tmpDriverMap.put(mysqlJdbcFile, driver);
	}

	// 每一次测试完卸载对应版本的jdbc驱动
	private static void dynamicUnLoadJdbc(String mysqlJdbcFile) throws SQLException {
		DriverManager.deregisterDriver(PFSqlConnHelper.tmpDriverMap.get(mysqlJdbcFile));
	}

	// 进行一次测试
	private static void testOneVersion(String mysqlJdbcFile) {

		System.out.println("start test mysql jdbc version : " + mysqlJdbcFile);

		try {
			dynamicLoadJdbc(mysqlJdbcFile);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select user()");
			System.out.println("select user() output : ");
			while (rs.next()) {
				System.out.println(rs.getObject(1));
			}
			rs = stmt.executeQuery("show tables");
			System.out.println("show tables output : ");
			while (rs.next()) {
				System.out.println(rs.getObject(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		try {
			dynamicUnLoadJdbc(mysqlJdbcFile);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("end !!!");
		System.out.println();
	}
//
//	/**
//	 * 暂时只比对驱动的类名,不比对大小版本号
//	 * @param version
//	 * @param className
//	 * @return
//	 */
//	private static Driver getLoadedDriver(String version, String className) {
//		Enumeration<Driver> drivers = DriverManager.getDrivers();
//		while (drivers.hasMoreElements()) {
//			Driver driver = drivers.nextElement();
////			//如果要考虑版本号MajorVersion和MinorVersion,可能需要先卸载同类名的其它版本号(不确定),还要了解一直用的方式DriverManager.getConnection是怎么处理同类名驱动的
////            if (driver.getClass().getName().equals(className)) {
////                return driver;
////            }
//            if(DriverShim.isSqlDriverMatch(driver, version, className)) {
//            	return driver;
//            }
//		}
//		return null;
//	}
//	public static void main(String[] args) {
//
////        jdbcVersionMap.put("mysql-connector-java-6.0.3.jar", "com.mysql.cj.jdbc.Driver");
////        jdbcVersionMap.put("mysql-connector-java-5.1.6.jar", "com.mysql.jdbc.Driver");
////        jdbcVersionMap.put("mysql-connector-java-5.1.31.jar", "com.mysql.jdbc.Driver");
////        jdbcVersionMap.put("mysql-connector-java-5.1.35.jar", "com.mysql.jdbc.Driver");
////        jdbcVersionMap.put("mysql-connector-java-5.1.39.jar", "com.mysql.jdbc.Driver");
//
////		SqlConnHelper.jdbcVersionMap.put("mysql-connector-java-5.1.34.jar", "com.mysql.jdbc.Driver");
////		SqlConnHelper.jdbcVersionMap.put("mysql-connector-java-8.0.15.jar", "com.mysql.jdbc.Driver");
//
//		for (String mysqlJdbcFile : SqlConnHelper.jdbcVersionMap.keySet()) {
//			testOneVersion(mysqlJdbcFile);
//		}
//
//	}
	

	/**
	 * 为了测试驱动数量有没有膨胀
	 * @param driver
	 * @return
	 */
	private static String getDriverInfo(Driver driver) {
		return driver.getClass().getName() + "\r\n"
				+ (driver instanceof IPFSqlDriverShim ? (((IPFSqlDriverShim) driver).getDriverName()+ "\r\n") : "") 
				+ "getMajorVersion:" + String.valueOf(driver.getMajorVersion()) + "\r\n" + "getMinorVersion:"
				+ String.valueOf(driver.getMinorVersion()) + "\r\n" + "jdbcCompliant:"
				+ String.valueOf(driver.jdbcCompliant()) + "\r\n";
	}

    public static int showAllLoadedDriver(boolean print) {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        int cnt = 0;
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (print) {
                LOGGER.info("\r\n \r\n" + getDriverInfo(driver) + "\r\n \r\n");
            }
            cnt++;
        }
        return cnt;
    }

	/**
	 * 为了测试驱动数量有没有膨胀
	 * @return
	 */
    public static void printDriverCount(org.slf4j.Logger logger, String prev) {
        int cnt = showAllLoadedDriver(false);
        logger.info(prev + "驱动数量为[" + cnt + "]个");
    }

}

/**
 * 如果不套1层,DriverManager.registerDriver 之后 DriverManager.getDrivers() 无法得到, 也无法正常getConnection. 原因未明.
 * @author 1011712002
 *
 */
class DriverShim implements Driver,IPFSqlDriverShim {
	private Driver driver;
	private String driverName;

	DriverShim(Driver d) {
		this.driver = d;
		this.driverName=d.getClass().getName();
	}

	public boolean acceptsURL(String u) throws SQLException {
		return this.driver.acceptsURL(u);
	}

	public Connection connect(String u, Properties p) throws SQLException {
		return this.driver.connect(u, p);
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
		return this.driver.getPropertyInfo(url, info);
	}

	@Override
	public int getMajorVersion() {
		return this.driver.getMajorVersion();
	}

	@Override
	public int getMinorVersion() {
		return this.driver.getMinorVersion();
	}

	@Override
	public boolean jdbcCompliant() {
		return this.driver.jdbcCompliant();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return this.driver.getParentLogger();
	}
	@Override
	public String getDriverName() {
		return driverName;
	}
	@Override
	public boolean isMatch(Driver other) {
		if(other instanceof IPFSqlDriverShim) {
			return getDriverName().equals(((IPFSqlDriverShim)other).getDriverName());
		}else {
			return getDriverName().equals(other.getClass().getName());
		}
	}
	public static boolean isSqlDriverMatch(Driver other,String version, String className) {
		//如果要考虑版本号MajorVersion和MinorVersion,可能需要先卸载同类名的其它版本号(不确定),还要了解一直用的方式DriverManager.getConnection是怎么处理同类名驱动的
		boolean hasVersion=!SGDataHelper.StringIsNullOrWhiteSpace(version);
		int majorVersion=-1;
		int minorVersion=-1;
		if(hasVersion) {
			String[] versionList=version.split(".");
			if(versionList.length>0) {
				majorVersion=Integer.valueOf(versionList[0]);
			}
			if(versionList.length>1) {
				minorVersion=Integer.valueOf(versionList[1]);
			}
			if(majorVersion>-1&&other.getMajorVersion()!=majorVersion) {
				return false;
			}
			if(minorVersion>-1&&other.getMinorVersion()!=minorVersion) {
				return false;
			}
		}
		if(other instanceof IPFSqlDriverShim) {
			return ((IPFSqlDriverShim)other).getDriverName().equals(className);
		}else {
			return other.getClass().getName().equals(className);
		}
	}
}
