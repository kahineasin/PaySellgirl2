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
public class UncheckDeExceptionLine001 extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(UncheckDeExceptionLine001.class);

	public static void initPFHelper() {
		SGDataHelper.SetConfigMapper(new PFConfigTestMapper());		
	}
	/**
	 * line准确
	 * 
	 * UncheckDeExceptionLine001.java: Line 103
	 */
	public void testException() {
		initPFHelper();
		try {
			throw new SQLSyntaxErrorException("aaa");
		} 
		catch (Exception e) {
			System.out.println(SGDataHelper.getLineInfo(e));
		}
	}
	/**
	 * line不准确
	 * 
	 * 果然,用了GetErrorFullString方法之后,getLineInfo就有问题了
	 * 
	 * PFDataHelper.java: Line 7713
	 * 
	 */
	public void testException2() {
		initPFHelper();
		try {
			throw new SQLSyntaxErrorException("aaa");
		} 
		catch (Exception e) {
			String aa=SGDataHelper.getErrorFullString(e);
			System.out.println(SGDataHelper.getLineInfo(e));
		}
	}
	public void testException3() throws Exception {
		throw new Exception("aaa");
	}
	/**
	 * line准确
	 * 
	 * UncheckDeExceptionLine001.java: Line 126
	 */
	public void testException4() {
		initPFHelper();
		try {
			//throw new SQLSyntaxErrorException("aaa");
			testException3();
		} 
		catch (Exception e) {
			System.out.println(SGDataHelper.getLineInfo(e));
		}
	}
	public void testException5() {
		initPFHelper();
		try {
			//throw new SQLSyntaxErrorException("aaa");
			testException3();
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(SGDataHelper.getLineInfo(e));
		}
	}
	public void testException6() {
		initPFHelper();
		try {
			//throw new SQLSyntaxErrorException("aaa");
			testException3();
		} 
		catch (Exception e) {
			SGDataHelper.WriteError(e);
		}
	}
	/**
	 * 无输出
	 */
	public void testException7() {
		initPFHelper();
		try {
			//throw new SQLSyntaxErrorException("aaa");
			testException3();
		} 
		catch (Exception e) {
			LOGGER.trace("bbb",e);
		}
	}
	public void testException8() {
		initPFHelper();
		try {
			//throw new SQLSyntaxErrorException("aaa");
			testException3();
		} 
		catch (Exception e) {
			LOGGER.info("bbb",e);
		}
	}
}
