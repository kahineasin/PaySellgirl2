//package org.sellgirlPayHelperNotSpring;
//
//import com.sellgirl.pfHelperNotSpring.config.PFDataHelper;
//import com.sellgirl.pfHelperNotSpring.config.PFDataHelper.LocalDataType;
//import com.sellgirl.pfHelperNotSpring.express.PFExpressHelper;
//import com.sellgirl.pfHelperNotSpring.model.SysType;
//import com.sellgirl.pfHelperNotSpring.model.UserOrg;
//import com.sellgirl.pfHelperNotSpring.model.UserTypeClass;
//import com.sellgirl.pfHelperNotSpring.task.IPFTask;
//import com.sellgirl.pfHelperNotSpring.task.PFDayTask;
//import com.sellgirl.pfHelperNotSpring.task.PFDayTask2;
//import com.sellgirl.pfHelperNotSpring.task.PFIntervalExactTask;
//import com.sellgirl.pfHelperNotSpring.task.PFIntervalExactTask2;
//import com.sellgirl.pfHelperNotSpring.task.PFIntervalTask;
//import com.sellgirl.pfHelperNotSpring.task.PFMonthTask;
//import com.sellgirl.pfHelperNotSpring.task.PFMonthTask2;
//import com.sellgirl.pfHelperNotSpring.task.PFTimeTaskBase2.TimePoint;
//
//import java.awt.Canvas;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Graphics;
//import java.awt.Image;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.UnsupportedEncodingException;
//import java.lang.reflect.Field;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.math.BigDecimal;
//import java.nio.file.Paths;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import java.sql.SQLSyntaxErrorException;
//import java.sql.Statement;
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
////import java.util.Base64;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.TimeZone;
//import java.util.function.Function;
//
//import javax.imageio.ImageIO;
//import javax.mail.Message;
//
//import org.sellgirlPayHelperNotSpring.model.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.sellgirl.pfHelperNotSpring.sql.*;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.TypeReference;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//import com.sellgirl.pfHelperNotSpring.*;
//import com.sellgirl.pfHelperNotSpring.config.*;
////import test.java.pfHelper.model.*;
//import org.apache.commons.lang3.tuple.ImmutablePair;
//import org.apache.commons.lang3.tuple.Pair;
//
///**
// * 不要随便更改此类名,以防打包时执行了此类
// *
// * @author Administrator
// *
// */
//@SuppressWarnings(value = { "unused",  "deprecation" })
//public class UncheckDeConvertSpeed001 extends TestCase {
//	private static final Logger LOGGER = LoggerFactory.getLogger(UncheckDeConvertSpeed001.class);
//
////	//rows:2.00E+007 speed:4.00E+008条/秒 耗时:0时0分0秒25毫秒/千万行 总耗时:0时0分0秒50毫秒 (begin:2022-09-29 15:24:38 -> now:2022-09-29 15:24:39)
////	public void testCalendarToCalendar001() {
////		Calendar d = PFDate.Now().ToCalendar();
////		int m = 20000000;
////		int i =0;
////
////		System.out.println("Calendar -> Calendar NORMAL");
////		PFSpeedCounter speed = new PFSpeedCounter(com.sellgirl.pfHelperNotSpring.PFDate.Now());
////		while(i<m) {
////			PFDataHelper.ConvertObjectToSqlTypeByPFType(d,PFSqlFieldType.DateTime,java.sql.Types.DATE);
////			i++;
////		}
////		System.out.println(speed.getSpeed(m, com.sellgirl.pfHelperNotSpring.PFDate.Now()));
////	}
////	//rows:2.00E+007 speed:5.00E+009条/秒 耗时:0时0分0秒2毫秒/千万行 总耗时:0时0分0秒4毫秒 (begin:2022-09-29 15:24:39 -> now:2022-09-29 15:24:39)
////	public void testCalendarToCalendar002() {
////		Calendar d = PFDate.Now().ToCalendar();
////		int m = 20000000;
////		int i =0;
////
////		IPFSqlFieldTypeConverter c2 = null;
////		System.out.println("Calendar -> Calendar CONVERTER");
////		PFSpeedCounter speed = new PFSpeedCounter(com.sellgirl.pfHelperNotSpring.PFDate.Now());
////		while(i<m) {
////			if(c2==null) {
////				c2=PFDataHelper.GetObjectToSqlTypeByPFTypeConverter(d,PFSqlFieldType.DateTime,java.sql.Types.DATE);
////			}
////			c2.convert(d);
////			i++;
////		}
////		System.out.println(speed.getSpeed(m, com.sellgirl.pfHelperNotSpring.PFDate.Now()) );
////	}
////	//rows:2.00E+007 speed:5.00E+009条/秒 耗时:0时0分0秒2毫秒/千万行 总耗时:0时0分0秒4毫秒 (begin:2022-09-29 15:24:39 -> now:2022-09-29 15:24:39)
////	public void testCalendarToCalendar003() {
////		Calendar d = PFDate.Now().ToCalendar();
////		int m = 20000000;
////		int i =0;
////
////		IPFSqlFieldTypeConverter2 c2 = null;
////		System.out.println("Calendar -> Calendar CONVERTER INTERFACE");
////		PFSpeedCounter speed = new PFSpeedCounter(com.sellgirl.pfHelperNotSpring.PFDate.Now());
////		while(i<m) {
////			if(c2==null) {
////				c2=PFSqlFieldTypeConverter2.CalendarToCalendar();
////			}
////			c2.convert(d);
////			i++;
////		}
////		System.out.println(speed.getSpeed(m, com.sellgirl.pfHelperNotSpring.PFDate.Now()) );
////	}
//
//	//rows:2.00E+007 speed:5.46E+006条/秒 耗时:0时0分1秒830毫秒/千万行 总耗时:0时0分34秒524毫秒 (begin:2022-09-29 15:24:28 -> now:2022-09-29 15:24:32)
//	public void testLongToDate001() {
//		Long d = PFDate.Now().ToCalendar().getTimeInMillis();
//		int m = 200000000;
//		int i =0;
//
//		System.out.println("long -> date NORMAL");
//		PFSpeedCounter speed = new PFSpeedCounter(com.sellgirl.pfHelperNotSpring.PFDate.Now());
//		while(i<m) {
//			Object tmpV=PFDataHelper.ConvertObjectToSqlTypeByPFType(d,PFSqlFieldTypeEnum.Long,java.sql.Types.DATE);
//			//Object tmpV=PFDataHelper.GetObjectToSqlTypeByPFTypeConverter(d,null,java.sql.Types.DATE,"").convert(d);//测试
//			//d+=1;
//			i++;
//		}
//		System.out.println(speed.getSpeed(m, com.sellgirl.pfHelperNotSpring.PFDate.Now()));
//	}
//	//rows:2.00E+007 speed:6.40E+006条/秒 耗时:0时0分1秒562毫秒/千万行 总耗时:0时0分33秒0毫秒 (begin:2022-09-29 15:24:32 -> now:2022-09-29 15:24:35)
//	public void testLongToDate002() {
//		Long d = PFDate.Now().ToCalendar().getTimeInMillis();
//		int m = 200000000;
//		int i =0;
//
//		IPFSqlFieldTypeConverter c2 = null;
//		System.out.println("long -> date CONVERTER");
//		PFSpeedCounter speed = new PFSpeedCounter(com.sellgirl.pfHelperNotSpring.PFDate.Now());
//		while(i<m) {
//			if(c2==null) {
//				c2=PFDataHelper.GetObjectToSqlTypeByPFTypeConverter(d,null,java.sql.Types.DATE,"");
//			}
//			Object tmpV=c2.convert(d);
//			//d+=1;
//			i++;
//		}
//		System.out.println(speed.getSpeed(m, com.sellgirl.pfHelperNotSpring.PFDate.Now()) );
//	}
////	//rows:2.00E+007 speed:6.17E+006条/秒 耗时:0时0分1秒620毫秒/千万行 总耗时:0时0分3秒240毫秒 (begin:2022-09-29 15:24:35 -> now:2022-09-29 15:24:38)
////	public void testLongToDate003() {
////		Long d = PFDate.Now().ToCalendar().getTimeInMillis();
////		int m = 20000000;
////		int i =0;
////
////		IPFSqlFieldTypeConverter2 c2 = null;
////		System.out.println("long -> date CONVERTER INTERFACE");
////		PFSpeedCounter speed = new PFSpeedCounter(com.sellgirl.pfHelperNotSpring.PFDate.Now());
////		while(i<m) {
////			if(c2==null) {
////				c2=PFSqlFieldTypeConverter2.LongToCalendar();
////			}
////			Object tmpV=c2.convert(d);
////			d+=1;
////			i++;
////		}
////		System.out.println(speed.getSpeed(m, com.sellgirl.pfHelperNotSpring.PFDate.Now()) );
////	}
//}
