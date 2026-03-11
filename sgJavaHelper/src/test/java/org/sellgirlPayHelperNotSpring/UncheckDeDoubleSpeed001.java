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
public class UncheckDeDoubleSpeed001 extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(UncheckDeDoubleSpeed001.class);

	
	//rows:2.00E+008 speed:5.88E+009条/秒 averageTime:0时0分0秒1毫秒/千万行 totalTime:0时0分0秒34毫秒 (begin:2022-11-22 09:48:01 -> now:2022-11-22 09:48:01)
	public void testLongSpeed() {
		long d = 1000L;
		int m = 200000000;
		int i =0;
		
		System.out.println("long speed");
		SGSpeedCounter speed = new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
		while(i<m) {	
			d=(i+5)*8;
			
			i++;
		}
		System.out.println(d);
		System.out.println(speed.getSpeed(m, com.sellgirl.sgJavaHelper.SGDate.Now()));
	}	
	//rows:2.00E+008 speed:4.76E+009条/秒 averageTime:0时0分0秒2毫秒/千万行 totalTime:0时0分0秒42毫秒 (begin:2022-11-22 09:48:14 -> now:2022-11-22 09:48:14)
	public void testDoubleSpeed() {
		double d = 1000;
		int m = 200000000;
		int i =0;
		
		System.out.println("long speed");
		SGSpeedCounter speed = new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
		while(i<m) {	
			d=(i+5)*8;
			
			i++;
		}
		System.out.println(d);
		System.out.println(speed.getSpeed(m, com.sellgirl.sgJavaHelper.SGDate.Now()));
	}	
	public void testDoubleMax() {
		double d = 90000000000000000000000000000000D;
		System.out.println(d);
		long d2 = 9000000000000000000L;
		System.out.println(d2);
	}
}
