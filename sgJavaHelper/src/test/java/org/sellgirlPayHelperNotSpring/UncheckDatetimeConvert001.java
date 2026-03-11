package org.sellgirlPayHelperNotSpring;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
//import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;
//import test.java.pfHelper.model.*;


/**
 * 不要随便更改此类名,以防打包时执行了此类
 * 
 * @author Administrator
 *
 */
@SuppressWarnings(value = { "unused",  "deprecation" })
public class UncheckDatetimeConvert001 extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(UncheckDatetimeConvert001.class);

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
