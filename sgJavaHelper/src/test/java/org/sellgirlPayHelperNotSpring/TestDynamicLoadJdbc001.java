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
public class TestDynamicLoadJdbc001 extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestDynamicLoadJdbc001.class);

	private String getDriverInfo(Driver driver) {
		return driver.getClass().getName() + "\r\n" 
	+ "getMajorVersion:" + String.valueOf(driver.getMajorVersion()) + "\r\n" 
	+ "getMinorVersion:"+ String.valueOf(driver.getMinorVersion()) + "\r\n" 
	+ "jdbcCompliant:" + String.valueOf(driver.jdbcCompliant());
	}

	public int showAllLoadedDriver(boolean print) {
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		int cnt=0;
		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			if(print) {
				LOGGER.info("\r\n \r\n"+getDriverInfo(driver)+"\r\n \r\n");
			}
			cnt++;
		}
		return cnt;
	}
	/**
	 * 防止加载重复的驱动
	 * @throws Exception
	 */
	public void testDynamicLoadClass() throws Exception {
		int cnt=showAllLoadedDriver(false);
		MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion("8.0.23","com.mysql.cj.jdbc.Driver");
		int cnt2=showAllLoadedDriver(false);
		MycatMulitJdbcVersionTest.dynamicLoadJdbcByVersion("8.0.23","com.mysql.cj.jdbc.Driver");
		int cnt3=showAllLoadedDriver(false);
		LOGGER.info(String.valueOf(cnt)+"_"+String.valueOf(cnt2)+"_"+String.valueOf(cnt3)+"_");
		assertTrue(cnt2==cnt3);
		assertTrue(cnt<=cnt2);
	}

}
