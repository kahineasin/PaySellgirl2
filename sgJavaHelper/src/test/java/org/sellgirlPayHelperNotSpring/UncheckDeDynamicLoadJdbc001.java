package org.sellgirlPayHelperNotSpring;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.function.Function;

import javax.imageio.ImageIO;
import javax.mail.Message;

import org.sellgirlPayHelperNotSpring.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sellgirl.sgJavaHelper.*;
import com.sellgirl.sgJavaHelper.config.*;
import com.sellgirl.sgJavaHelper.config.SGDataHelper.LocalDataType;
import com.sellgirl.sgJavaHelper.express.PFExpressHelper;
import com.sellgirl.sgJavaHelper.model.SysType;
import com.sellgirl.sgJavaHelper.model.UserOrg;
import com.sellgirl.sgJavaHelper.model.UserTypeClass;
import com.sellgirl.sgJavaHelper.sql.*;
import com.sellgirl.sgJavaHelper.task.IPFTask;
import com.sellgirl.sgJavaHelper.task.PFDayTask;
import com.sellgirl.sgJavaHelper.task.PFDayTask2;
import com.sellgirl.sgJavaHelper.task.PFIntervalExactTask;
import com.sellgirl.sgJavaHelper.task.PFIntervalExactTask2;
import com.sellgirl.sgJavaHelper.task.PFIntervalTask;
import com.sellgirl.sgJavaHelper.task.PFMonthTask;
import com.sellgirl.sgJavaHelper.task.PFMonthTask2;
import com.sellgirl.sgJavaHelper.task.PFTimeTaskBase2.TimePoint;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

//import test.java.pfHelper.model.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * 不要随便更改此类名,以防打包时执行了此类
 * 
 * @author Administrator
 *
 */
@SuppressWarnings(value = { "unused", "deprecation" })
public class UncheckDeDynamicLoadJdbc001 extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(UncheckDeDynamicLoadJdbc001.class);

	private String getDriverInfo(Driver driver) {
		return driver.getClass().getName() + "\r\n"
				+ (driver instanceof IPFSqlDriverShim ? ((IPFSqlDriverShim) driver).getDriverName() : "") + "\r\n"
				+ "getMajorVersion:" + String.valueOf(driver.getMajorVersion()) + "\r\n" + "getMinorVersion:"
				+ String.valueOf(driver.getMinorVersion()) + "\r\n" + "jdbcCompliant:"
				+ String.valueOf(driver.jdbcCompliant()) + "\r\n";
	}

	/**
	 * 显示当前已加载的驱动
	 * 
	 * @throws Exception
	 */
	public void testShowAllLoadedDriver() throws Exception {
		showAllLoadedDriver();
//		//LOGGER.info("------------------------------");
//		MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion("5.1.34","com.mysql.jdbc.Driver");
//		MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion("5.1.46","com.mysql.jdbc.Driver");
//		MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion("5.1.46","com.mysql.jdbc.Driver");
//		MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion("8.0.23","com.mysql.cj.jdbc.Driver");
//
//		showAllLoadedDriver();
	}

	/**
	 * 测试ClassLoader接收多个url时,url的顺序是否有影响
	 * 
	 * 结论:无影响
	 */
	public void testClassLoaderOrder() throws Exception {
		String version = "5.1.34";
		String className = "com.mysql.jdbc.Driver";
		String jarPath = "mysql-connector-java-" + version + ".jar";
		URL u = new URL("jar:file:lib/" + jarPath + "!/");
		URL u2 = new URL("jar:file:lib/" + jarPath + "1414!/");
		URLClassLoader ucl = new URLClassLoader(new URL[] { u, u2 });
		Class<?> dClass = null;
		try {
			dClass = Class.forName(className, true, ucl);
		} catch (Exception e) {
		}
		LOGGER.info(String.valueOf(null == dClass));

		URLClassLoader ucl2 = new URLClassLoader(new URL[] { u2, u });
		Class<?> dClass2 = null;
		try {
			dClass2 = Class.forName(className, true, ucl2);
		} catch (Exception e) {
		}
		LOGGER.info(String.valueOf(null == dClass2));
	}

	/**
	 * 如果某个url为空,Class.forName时会报错
	 * 
	 * @throws Exception
	 */
	public void testClassLoaderOrder2() throws Exception {
		String version = "5.1.34";
		String className = "com.mysql.jdbc.Driver";
		String jarPath = "mysql-connector-java-" + version + ".jar";
		URL u = new URL("jar:file:lib/" + jarPath + "!/");
		URL u2 = Thread.currentThread().getContextClassLoader().getResource(Paths.get("jar", jarPath).toString());
		URL u3 = MycatMulitJdbcVersionTest.class.getResource(Paths.get("jar", jarPath).toString());
		URLClassLoader ucl = new URLClassLoader(new URL[] { u, u2, u3 });
		Class<?> dClass = Class.forName(className, true, ucl);
		LOGGER.info(String.valueOf(null == dClass));

		URLClassLoader ucl2 = new URLClassLoader(new URL[] { u3, u, u2 });
		Class<?> dClass2 = Class.forName(className, true, ucl2);
		LOGGER.info(String.valueOf(null == dClass2));
	}

	public int showAllLoadedDriver() {
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		int cnt = 0;
		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			LOGGER.info("\r\n \r\n" + (cnt + 1) + ": " + getDriverInfo(driver) + "\r\n \r\n");
			cnt++;
		}
		return cnt;
	}

//	/**
//	 * 测试动态加载driver(不套1层),能否正常连接.
//	 * 
//	 * @throws Exception
//	 */
//	public void testDynamicLoadOriginDriver() throws Exception {
//		int cnt = showAllLoadedDriver();
//		LOGGER.info("有" + cnt + "个驱动");
//		LOGGER.info("\r\n------------------------------");
//		Connection conn = null;
//		try {
//			conn = DriverManager.getConnection(
//					"jdbc:mysql://uat-cloud.perfect99.com:10129/test?rewriteBatchedStatements=true", "root",
//					"perfect@EAS");
//		} catch (Exception e) {
//
//		}
//		LOGGER.info("第1次连接成功: " + (null != conn));
//
//		// MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion("5.1.46","com.mysql.jdbc.Driver");//这种方式,下面连接成功
//		MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersionOriginDriver("5.1.46", "com.mysql.jdbc.Driver");// 这种方式,下面连接失败
//		conn = null;
//		try {
//			conn = DriverManager.getConnection(
//					"jdbc:mysql://uat-cloud.perfect99.com:10129/test?rewriteBatchedStatements=true", "root",
//					"perfect@EAS");
//		} catch (Exception e) {
//
//		}
//		LOGGER.info("第2次连接成功: " + (null != conn));
//	}

	/**
	 * 测试加载重复的驱动.
	 * 
	 * 新版本已经可以判断驱动是否已加载重复了
	 * 
	 * @throws Exception
	 */
	public void testDynamicLoadMultiDriver() throws Exception {
		int cnt = showAllLoadedDriver();
		LOGGER.info("有" + cnt + "个驱动");
		LOGGER.info("\r\n------------------------------");
		MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion("5.1.46", "com.mysql.jdbc.Driver");
		MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion("8.0.23", "com.mysql.cj.jdbc.Driver");
		MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion("5.1.46", "com.mysql.jdbc.Driver");// 新版本已经可以不加载这条重复的驱动了
		MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion("8.0.23", "com.mysql.cj.jdbc.Driver");
		MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion("5.1.46", "com.mysql.jdbc.Driver");// 新版本已经可以不加载这条重复的驱动了
		MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion("8.0.23", "com.mysql.cj.jdbc.Driver");
		cnt = showAllLoadedDriver();
		LOGGER.info("有" + cnt + "个驱动");
		LOGGER.info("\r\n------------------------------");
	}

	/**
	 * 测试当pom中有驱动时,加载不同版本的情况.
	 * 
	 * 旧版加载的仍是pom中的版本,可能没有优先从jar中找
	 * 
	 * @throws Exception
	 */
	public void testDynamicLoadDriverWhenPomHas() throws Exception {
		int cnt = showAllLoadedDriver();
		LOGGER.info("有" + cnt + "个驱动");
		LOGGER.info("\r\n------------------------------");
		MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion("5.1.46", "com.mysql.jdbc.Driver");//实测这里虽然成功加载com.mysql.jdbc.Driver,但major版本却是5,原因未明
		cnt = showAllLoadedDriver();
		LOGGER.info("有" + cnt + "个驱动");
		LOGGER.info("\r\n------------------------------");
	}

	public void testDynamicLoadClass() throws Exception {
		URL u = new URL("jar:file:lib/mysql-connector-java-5.1.46.jar!/");
//      String className = jdbcVersionMap.get(mysqlJdbcFile);
		URLClassLoader ucl = new URLClassLoader(new URL[] { u });
		Class<?> dClass = Class.forName("com.mysql.jdbc.Driver", true, ucl);
		TestCase.assertTrue(null != dClass);
	}

	/**
	 * lib文件夹中不存在时,报错
	 * 
	 * @throws Exception
	 */
	public void testDynamicLoadClass2() throws Exception {
		URL u = new URL("jar:file:lib/mysql-connector-java-5.1.31xxx.jar!/");
		URLClassLoader ucl = new URLClassLoader(new URL[] { u });
		Class<?> dClass = Class.forName("com.mysql.jdbc.Driver", true, ucl);
		TestCase.assertTrue(null != dClass);
	}

	public void testDynamicLoadClass3() throws Exception {
		URLClassLoader ucl = new URLClassLoader(new URL[] { new URL("jar:file:lib/mysql-connector-java-5.1.31.jar!/"),
				new URL("jar:file:lib/mysql-connector-java-5.1.46.jar!/") // 任意一个url中有,就不会报错
		});
		Class<?> dClass = Class.forName("com.mysql.jdbc.Driver", true, ucl);
		TestCase.assertTrue(null != dClass);
	}

	public void testDynamicLoadJdbcByVersion() {
		try {
			LOGGER.info("测试能不能动态加载jdbc驱动");

			String version = "8.0.15";
			String className = "com.mysql.cj.jdbc.Driver";
			URL u = new URL("jar:file:lib/mysql-connector-java-" + version + ".jar!/");
			URLClassLoader ucl = new URLClassLoader(new URL[] { u });
			Class<?> dClass = null;
			try {
				dClass = Class.forName(className, true, ucl);
				if (null == dClass) {
					SGDataHelper.WriteError(new Throwable(),
							new Exception("Class.forName(className, true, ucl);找不到class:" + className));
					// return;
				} else {
					LOGGER.info("Class.forName(className, true, ucl)加载class成功:" + dClass);
				}
			} catch (Exception e) {
				LOGGER.error(e.toString());
			}

			try {
				dClass = Class.forName(className);
				if (null == dClass) {
					SGDataHelper.WriteError(new Throwable(),
							new Exception("Class.forName(className)找不到class:" + className));
					// return;
				} else {
					LOGGER.info("Class.forName(className)加载class成功:" + dClass);
				}
			} catch (Exception e) {
				LOGGER.error(e.toString());
			}
			LOGGER.info("测试能不能动态加载jdbc驱动--end");

			File dir = SGDataHelper.urlToFile(u);
			if (null == dir) {
				// throw new Exception("file为null");
			}

			MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion(version, className);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 测试能不能动态加mySql的jdbc驱动.
	 * 
	 * 实测可以动态加载 mysql-connector-java-8.0.23.jar
	 */
	public void testDynamicLoadMySqlJdbcByVersion() {
		try {
			LOGGER.info("测试能不能动态加载mySql jdbc驱动");

			Connection conn = null;
			try {
				conn = DriverManager.getConnection("jdbc:mysql://uat-cloud.perfect99.com:10129/test?rewriteBatchedStatements=true",
						"root", "perfect@EAS");
			} catch (Exception e) {

			}
			LOGGER.info("第1次 连接mySql成功: "+(null!=conn));
			
			String version = "";
			String className = "com.mysql.jdbc.Driver";
			MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion(version, className);

			conn = null;
			try {
				conn = DriverManager.getConnection("jdbc:mysql://uat-cloud.perfect99.com:10129/test?rewriteBatchedStatements=true",
						"root", "perfect@EAS");
			} catch (Exception e) {

			}
			LOGGER.info("第2次 连接mySql成功: "+(null!=conn));
		} catch (Exception e) {
			LOGGER.error(e.toString());
		}
	}
	/**
	 * 测试阿里云数据库.
	 */
	public void testDynamicLoadMySqlJdbcByVersion2() {
		try {
			//String url="jdbc:mysql://uat-cloud.perfect99.com:10129/test?rewriteBatchedStatements=true";
			
			String url="jdbc:mysql://rm-wz9xg4c64rv2x4bn7.mysql.rds.aliyuncs.com:3306/uc_health_data?rewriteBatchedStatements=true";
			//String url="jdbc:mysql://rm-wz9xg4c64rv2x4bn7.mysql.rds.aliyuncs.com:3306/uc_health_data";
			String user="perfect";
			String pwd="perfect@Health";
			
			LOGGER.info("测试能不能动态加载mySql jdbc驱动");

			Connection conn = null;
			try {
				conn = DriverManager.getConnection(url,
						user, pwd);
			} catch (Exception e) {

			}
			LOGGER.info("第1次 连接mySql成功: "+(null!=conn));
			
//			//超时
//			String version = "";
//			String className = "com.mysql.jdbc.Driver";
			
//			//超时
			String version = "8.0.15";
			String className = "com.mysql.cj.jdbc.Driver";

//			String version = "5.1.46";
//			String className = "com.mysql.jdbc.Driver";
			MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion(version, className);

			conn = null;
			try {
				conn = DriverManager.getConnection(url,
						user, pwd);
			} catch (Exception e) {

			}
			LOGGER.info("第2次 连接mySql成功: "+(null!=conn));
		} catch (Exception e) {
			LOGGER.error(e.toString());
		}
	}

	/**
	 * 测试能不能动态加载sqlServer的jdbc驱动.
	 * 
	 * 实测可以动态加载jtds-1.3.1.jar
	 */
	public void testDynamicLoadSqlServerJdbcByVersion() {
		try {
			LOGGER.info("测试能不能动态加载sqlServer jdbc驱动");

			Connection conn = null;
			try {
				conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.0.29:1433/balance", "sa", "perfect");
			} catch (Exception e) {

			}
			LOGGER.info("第1次 连接sqlserver成功: "+(null!=conn));
			
			String version = "";
			String className = "net.sourceforge.jtds.jdbc.Driver";
			MycatMulitJdbcVersionTest.dynamicLoadSqlServerJdbcByVersion(version, className);
			
			conn = null;
			try {
				conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.0.29:1433/balance", "sa", "perfect");
			} catch (Exception e) {

			}
			LOGGER.info("第2次 连接sqlserver成功: "+(null!=conn));
		} catch (Exception e) {
			LOGGER.error(e.toString());
		}
	}
	

	public void testClassLoader() throws Exception {
		ClassLoader cl1=MycatMulitJdbcVersionTest.class.getClassLoader();
		ClassLoader cl2=MycatMulitJdbcVersionTest.class.getClassLoader();
		ClassLoader cl3=Thread.currentThread().getContextClassLoader();

		String version = "5.1.34";
		String className = "com.mysql.jdbc.Driver";
		String jarPath = "mysql-connector-java-" + version + ".jar";
		URL u = new URL("jar:file:lib/" + jarPath + "!/");
		URL u2 = Thread.currentThread().getContextClassLoader()
				.getResource(Paths.get("jar", jarPath).toString());
		URL u3 = MycatMulitJdbcVersionTest.class.getResource(Paths.get("jar", jarPath).toString());
		List<URL> urls=new ArrayList<URL>();
		if(null!=u) {urls.add(u);}
		if(null!=u2) {urls.add(u2);}
		if(null!=u3) {urls.add(u3);}
		URLClassLoader cl4 = new URLClassLoader(urls.toArray(new URL[urls.size()]));
		
		assertTrue(cl1==cl2);//true
		assertTrue(cl1.equals(cl2));//true;
		assertTrue(cl1==cl3);//true
		assertTrue(cl1.equals(cl3));//true;
		assertFalse(cl1==cl4);//true
		assertFalse(cl1.equals(cl4));//true;
	}

	private String GetDriverDescription(Driver driver) {
		if (driver == null) {
			return "null";
		}
		return SGDataHelper.FormatString("MajorVersion:{0}\r\n" + "MinorVersion:{1}", driver.getMajorVersion(),
				driver.getMinorVersion());
	}

	public void testShowCurrentJdbcVersion() {
		try {
			Driver driver = MycatMulitJdbcVersionTest.getDriverByVersion("com.mysql.jdbc.Driver");
			LOGGER.info(SGDataHelper.FormatString("当前驱动为:{0}", GetDriverDescription(driver)));

			driver = MycatMulitJdbcVersionTest.getDriverByVersion("com.mysql.cj.jdbc.Driver");
			LOGGER.info(SGDataHelper.FormatString("当前驱动为:{0}", GetDriverDescription(driver)));

		} catch (Exception e) {
			LOGGER.error(e.toString());
		}
	}
}
