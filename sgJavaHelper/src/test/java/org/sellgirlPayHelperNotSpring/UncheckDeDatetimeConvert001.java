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
import java.nio.file.Paths;
import java.sql.Connection;
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
@SuppressWarnings(value = { "unused",  "deprecation" })
public class UncheckDeDatetimeConvert001 extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(UncheckDeDatetimeConvert001.class);

	private long getTimestampLongFromSql(String connStr, String sql, String fieldName) throws SQLException {
		Connection conn = DriverManager.getConnection(connStr, "root", "perfect@EAS");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		return rs.getTimestamp(fieldName, Calendar.getInstance()).getTime();
	}

	public static long getTimestampLongFromSql2(String connStr, String sql, String fieldName, TimeZone tz)
			throws SQLException {
		Connection conn = DriverManager.getConnection(connStr, "root", "perfect@EAS");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		return rs.getTimestamp(fieldName, Calendar.getInstance(tz)).getTime();
	}
	public static Timestamp getTimestampFromSql2(String connStr, String sql, String fieldName, TimeZone tz)
			throws SQLException {
		Connection conn = DriverManager.getConnection(connStr, "root", "perfect@EAS");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		return rs.getTimestamp(fieldName, Calendar.getInstance(tz));
	}

	public static long getTimestampLongFromApp(String dateStr) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(dateStr);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}

	/**
	 * 2022-01-01 10:00:00
	 */
	private String dateStr = "2022-01-01 10:00:00";
	/**
	 * 1641002400000L
	 */
	private long dateLongRight = 1641002400000L;

	public void testAppTime001() throws Exception {
		long dateLong = getTimestampLongFromApp(dateStr);
		System.out.println(dateStr);
		System.out.println(dateLong);
		assertTrue(dateLongRight == dateLong);
	}

	public void testAppTime002() throws Exception {
		TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
		TimeZone.setDefault(timeZone);
		String dateStr = "2022-01-01 10:00:00";
		long dateLong = getTimestampLongFromApp(dateStr);
		System.out.println(dateStr);
		System.out.println(dateLong);
		assertTrue(dateLongRight == dateLong);
	}

	public void testAppTime003() throws Exception {
		TimeZone timeZone = TimeZone.getTimeZone("GMT+6");
		TimeZone.setDefault(timeZone);
		String dateStr = "2022-01-01 10:00:00";
		long dateLong = getTimestampLongFromApp(dateStr);
		System.out.println(dateStr);
		System.out.println(dateLong);
		assertTrue(dateLongRight == dateLong);// 这里的输出不是1641002400000L
	}

	//程序和数据库都是+8,没问题
	public void testSqlTime001() throws Exception {
		long dateLong = getTimestampLongFromSql("jdbc:mysql://uat-cloud.perfect99.com:10129/test",
				"select col1 from test.test_tb_07 where id=1", "col1");
		System.out.println(dateLong);
		assertTrue(dateLongRight == dateLong);
	}

	public void testSqlTime002() throws Exception {
		TimeZone timeZone = TimeZone.getTimeZone("GMT+6");
		TimeZone.setDefault(timeZone);
		long dateLong = getTimestampLongFromSql("jdbc:mysql://uat-cloud.perfect99.com:10129/test",
				"select col1 from test.test_tb_07 where id=1", "col1");
		System.out.println(dateLong);
		assertTrue(dateLongRight == dateLong);// 这里的输出不是1641002400000L,因为程序时区和数据库时区不一致
	}

	public void testSqlTime003() throws Exception {
		TimeZone timeZone = TimeZone.getTimeZone("GMT+6");
		TimeZone.setDefault(timeZone);
		long dateLong = getTimestampLongFromSql(
				"jdbc:mysql://uat-cloud.perfect99.com:10129/test?serverTimezone=GMT%2B8",
				"select col1 from test.test_tb_07 where id=1", "col1");
		
		System.out.println(dateLong);
		assertTrue(dateLongRight == dateLong);// 这里的输出也不是1641002400000L,因为getTimestampLongFromSql转换为long时用的是程序时区
		//assertTrue(dateLong>dateLongRight);//这其实就是之前例子里面,读取数据更大的那种情况
	}

	public void testSqlTime004() throws Exception {
		TimeZone timeZone = TimeZone.getTimeZone("GMT+6");
		TimeZone.setDefault(timeZone);
		long dateLong = getTimestampLongFromSql2(
				"jdbc:mysql://uat-cloud.perfect99.com:10129/test?serverTimezone=GMT%2B8",
				"select col1 from test.test_tb_07 where id=1", "col1", TimeZone.getTimeZone("GMT+8"));
		System.out.println(dateLong);
		assertTrue(dateLongRight == dateLong);

	}
}
