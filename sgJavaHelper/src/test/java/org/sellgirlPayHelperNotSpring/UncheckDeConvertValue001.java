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
public class UncheckDeConvertValue001 extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(UncheckDeConvertValue001.class);

	/**
	 * 输出
null
0

2

4
...
	 */
	public void testObjectToString() {
		String aa = null;
		String bb = "aa";
		//int m = 20000000;
		int m = 20;
		int i =0;
		
		IPFSqlFieldTypeConverter c2 = null;
		//System.out.println("long -> date CONVERTER");
		SGSpeedCounter speed = new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
		while(i<m) {		
			Object v=0==i%2?aa:bb;
			if(c2==null) {
				//c2=PFDataHelper.GetObjectToSqlTypeByPFTypeConverter0(v,null,java.sql.Types.VARCHAR,"");
				c2=PFSqlFieldTypeConverter.StringConverter0(v);
			}
			Object tmpV=null;
			if(null!=c2) {
				 tmpV=c2.convert(v);
			}
			bb=Integer.valueOf(i).toString();
			System.out.println(null==tmpV?"null":tmpV );
			i++;
		}
		System.out.println(speed.getSpeed(m, com.sellgirl.sgJavaHelper.SGDate.Now()) );
	}
//	/**
//	 * 
//	 */
//	public void testObjectToString2() {
//		String aa = null;
//		String bb = "aa";
//		//int m = 20000000;
//		int m = 20;
//		int i =0;
//		
//		IPFSqlFieldTypeConverter c2 = null;
//		//System.out.println("long -> date CONVERTER");
//		PFSpeedCounter speed = new PFSpeedCounter(com.sellgirl.pfHelperNotSpring.PFDate.Now());
//		while(i<m) {		
//			Object v=0==i%2?aa:bb;
//			if(c2==null ) {
//				//c2=PFDataHelper.GetObjectToSqlTypeByPFTypeConverter0(v,null,java.sql.Types.VARCHAR,"");
//				c2=PFSqlFieldTypeConverter.StringConverter0x(v);
//			}else if(c2 instanceof IPFSqlFieldTypeNullConverter) {
//				c2=(IPFSqlFieldTypeConverter)c2.convert(v);
//			}
//			Object tmpV=null;
//			if(null!=c2&&(!(c2 instanceof IPFSqlFieldTypeNullConverter))) {
//				 tmpV=c2.convert(v);
//			}
////			if(null!=tmpV&&tmpV instanceof IPFSqlFieldTypeConverter) {
////			
////			}else {
////				
////			}
////			while(null!=tmpV&&tmpV instanceof IPFSqlFieldTypeConverter) {
////				tmpV=((IPFSqlFieldTypeConverter)tmpV).convert(v);
////			}
//			bb=Integer.valueOf(i).toString();
//			System.out.println(null==tmpV?"null":tmpV );
//			i++;
//		}
//		System.out.println(speed.getSpeed(m, com.sellgirl.pfHelperNotSpring.PFDate.Now()) );
//	}
//	/**
//	 * 为了提高效率,converter不返回空,当value为null时,返回小范围的converter,这样会更快
//	 */
//	public void testObjectToString3() {
//		String aa = null;
//		String bb = "aa";
//		//int m = 20000000;
//		int m = 20;
//		int i =0;
//		
//		IPFSqlFieldTypeConverter c2 = null;
//		//System.out.println("long -> date CONVERTER");
//		PFSpeedCounter speed = new PFSpeedCounter(com.sellgirl.pfHelperNotSpring.PFDate.Now());
//		while(i<m) {		
//			Object v=0==i%2?aa:bb;
//			if(c2==null) {
//				//c2=PFDataHelper.GetObjectToSqlTypeByPFTypeConverter0(v,null,java.sql.Types.VARCHAR,"");
//				c2=PFSqlFieldTypeConverter.StringConverter0x(v);
//			}else if(c2 instanceof IPFSqlFieldTypeConverter) {
//				
//			}
//			Object tmpV=null;
//			if(null!=c2) {
//				 tmpV=c2.convert(v);
//			}
//			while(null!=tmpV&&tmpV instanceof IPFSqlFieldTypeConverter) {
//				tmpV=((IPFSqlFieldTypeConverter)tmpV).convert(v);
//			}
//			bb=Integer.valueOf(i).toString();
//			System.out.println(null==tmpV?"null":tmpV );
//			i++;
//		}
//		System.out.println(speed.getSpeed(m, com.sellgirl.pfHelperNotSpring.PFDate.Now()) );
//	}
}
