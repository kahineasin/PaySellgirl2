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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
public class UncheckDeDatetimeConvert002 extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(UncheckDeDatetimeConvert002.class);

	public static void initPFHelper() throws Exception {
		SGDataHelper.SetConfigMapper(new PFConfigTestMapper());		
		new SGDataHelper(new PFAppConfig());
		MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion("", "com.mysql.cj.jdbc.Driver");
	}

	public void testGetTimeStringByTimestampLong() throws Exception {
		String dateStr=SGDataHelper.TimeStampToDateString(1662746400000L, null); //2022-09-10 02:00:00
		//String dateStr=PFDataHelper.TimeStampToDateString(19244L, null);//1970-01-01 08:00:19
		//String dateStr=PFDataHelper.TimeStampToDateString(1924400000000L, null);//2030-12-25 11:33:20
		System.out.println(dateStr);
	}
	public void testGetTimeStringByFlikMySqlDate() throws Exception {
		SGDate start=new SGDate(1970,1,1,0,0,0);
		SGDate end=start.AddDays(19244);
		System.out.println(end);
	}

	public void testTimezone() throws Exception {
		System.out.println(TimeZone.getTimeZone("GMT+8").getID());   //GMT+08:00
		System.out.println(TimeZone.getTimeZone("GMT+8").getDisplayName());   //GMT+08:00
		System.out.println(TimeZone.getTimeZone("Asia/Shanghai").getID());   //Asia/Shanghai
		System.out.println(TimeZone.getTimeZone("Asia/Shanghai").getDisplayName());   //中国标准时间
		//System.out.println(ZoneId.of("CTT"));   // 报错
		System.out.println(ZoneId.of("Asia/Shanghai")); //Asia/Shanghai
		System.out.println(ZoneId.of("GMT+8")); //GMT+08:00		
		System.out.println(ZoneId.systemDefault());   //Asia/Shanghai
		
	}
	public void testStringToTimestamp() throws Exception {
//		Timestamp ts = Timestamp.valueOf("2022-09-09T10:00:00Z");//报错
//		System.out.println(new PFDate(ts).toString());

//		SimpleDateFormat sdf = new SimpleDateFormat();
//		Date d = sdf.parse("2022-09-09T10:00:00Z");//报错
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(d);
//		System.out.println(new PFDate(cal).toString());
		
		String s=Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
		System.out.println(s);//2022-10-19T15:05:18.363+0800
		String s2=Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("GMT+0")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
		System.out.println(s2);//2022-10-19T07:05:18.421+0000
		

//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//		Date d = sdf.parse("2022-09-09T10:00:00Z");//报错
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(d);
//		System.out.println(new PFDate(cal).toString());
		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//		Date d = sdf.parse("2022-09-09T10:00:00+0800");//报错
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(d);
//		System.out.println(new PFDate(cal).toString());
		
		//ZonedDateTime.parse("2022-09-09T10:00:00+0800");//报错
		
		
		System.out.println(ZonedDateTime.parse("2022-09-09T10:00:00Z").getZone()); //输出Z
		System.out.println(ZonedDateTime.parse("2022-10-19T15:22:31+01:00").getZone()); //输出+01:00
	}
	public void testTimestampLong() throws Exception {
		SGDate s=SGDate.Now();
		SGDate e=s.AddMinutes(1);
		long r=e.toTimestamp()-s.toTimestamp();
		System.out.println(r); //59675
		System.out.println(1000*60); //60000
		
	}
	public void testTimestampLong2() throws Exception {
		//System.out.println(1662746400000L);
		long dateLong = UncheckDeDatetimeConvert001.getTimestampLongFromApp("2022-09-09 18:00:00");
		System.out.println(dateLong);
//		
//		PFDate start=new PFDate(1970,1,1,0,0,0);
//		PFDate end=start.AddDays(19244);
//		System.out.println(end.toTimestamp());
//		
		System.out.println(Timestamp.valueOf(ZonedDateTime.parse("2022-09-09T10:00:00Z").toLocalDateTime()).getTime());
		System.out.println(Timestamp.valueOf(ZonedDateTime.parse("2022-09-09T10:00:00+00:00").toLocalDateTime()).getTime());
		System.out.println(Timestamp.valueOf(ZonedDateTime.parse("2022-09-09T10:00:00+08:00").toLocalDateTime()).getTime());
		
			System.out.println(Timestamp.from(ZonedDateTime.parse("2022-09-09T10:00:00Z").toInstant()).getTime());
			System.out.println(Timestamp.from(ZonedDateTime.parse("2022-09-09T10:00:00+00:00").toInstant()).getTime());
			System.out.println(Timestamp.from(ZonedDateTime.parse("2022-09-09T10:00:00+08:00").toInstant()).getTime());
			
		System.out.println(ZonedDateTime.parse("2022-09-09T10:00:00Z").getZone());
		System.out.println(ZonedDateTime.parse("2022-09-09T10:00:00+00:00").getZone());
		System.out.println(ZonedDateTime.parse("2022-09-09T10:00:00+08:00").getZone());
		
	}
	public void testTimestampTimeZone01() throws Exception {
		TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
		TimeZone.setDefault(timeZone);
		Timestamp ts=new Timestamp(1662717600000L);
		System.out.println(ts.getHours());//18
		System.out.println(ts.getTimezoneOffset());//-480
		
	}
	public void testTimestampTimeZone02() throws Exception {
		TimeZone timeZone = TimeZone.getTimeZone("UTC+0");
		TimeZone.setDefault(timeZone);
		Timestamp ts=new Timestamp(1662717600000L);
		System.out.println(ts.getHours());//10
		System.out.println(ts.getTimezoneOffset());//0
	}
	public void testTimestampTimeZone03() throws Exception {
		TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
		TimeZone.setDefault(timeZone);
		Timestamp ts=new Timestamp(1662746400000L);
		System.out.println(ts.getHours());//2
		System.out.println(ts.getTimezoneOffset());
		
	}
	public void testTimestampTimeZone04() throws Exception {
		TimeZone timeZone = TimeZone.getTimeZone("UTC+0");
		TimeZone.setDefault(timeZone);
		Timestamp ts=new Timestamp(1662746400000L);
		System.out.println(ts.getHours());//18
		System.out.println(ts.getTimezoneOffset());//0
	}
	public void testTimestampTimeZone05() throws Exception {
		TimeZone timeZone = TimeZone.getTimeZone("UTC+6");
		TimeZone.setDefault(timeZone);
		Timestamp ts=new Timestamp(1662717600000L);
		System.out.println(ts.getHours());//10
		System.out.println(ts.getTimezoneOffset());
	}
	public void testTimestampTimeZone06() throws Exception {
		TimeZone timeZone = TimeZone.getTimeZone("GMT+6");
		TimeZone.setDefault(timeZone);
		Timestamp ts=new Timestamp(1662717600000L);
		System.out.println(ts.getHours());//10
		System.out.println(ts.getTimezoneOffset());
	}
	public void testTimestampYearAtDifferentTimeZone01() throws Exception {
		initPFHelper();
		TimeZone timeZone = TimeZone.getTimeZone("GMT+6");
		TimeZone.setDefault(timeZone);
		Timestamp date = UncheckDeDatetimeConvert001.getTimestampFromSql2(
				"jdbc:mysql://uat-cloud.perfect99.com:10129/test?serverTimezone=GMT%2B8",
				"select col1 from test.test_tb_07 where id=1", "col1", TimeZone.getTimeZone("GMT+8"));
		long dateLong=date.getTime();
		System.out.println(dateLong);//1662717600000
		System.out.println(date.getHours());//16
	}
	public void testTimestampYearAtDifferentTimeZone02() throws Exception {
		initPFHelper();
		TimeZone timeZone = TimeZone.getTimeZone("GMT+6");
		TimeZone.setDefault(timeZone);
		Timestamp date = UncheckDeDatetimeConvert001.getTimestampFromSql2(
				"jdbc:mysql://uat-cloud.perfect99.com:10129/test?serverTimezone=GMT%2B6",
				"select col1 from test.test_tb_07 where id=1", "col1", TimeZone.getTimeZone("GMT+6"));
		long dateLong=date.getTime();
		System.out.println(dateLong);//1662724800000
		System.out.println(date.getHours());//18
	}
}
