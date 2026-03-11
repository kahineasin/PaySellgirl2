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
import com.sellgirl.sgJavaHelper.classLoader.SGClassLoader2;
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
public class UncheckClassLoader001 extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(UncheckClassLoader001.class);

//	private final static String jarName="knightsashax.jar";
	private final static String jarName="D:/cache/knightsashax.jar";
	public void testClassLoader() throws Exception {
		SGRef<Integer> i=new SGRef<Integer>(0);
		if(new File(jarName).exists()) {
			System.out.println("f exist");
		}else {
			System.out.println("f not exist");
		}
//		Class<?> c1=SGClassLoader.doLoadClass(jarName, "com.sellgirl.knightsashax.GameXScreen", i);
//		Class<?> c1=SGClassLoader.doLoadClass(jarName, "com.sellgirl.sgJavaHelper.SGDataHelper", i);
		Class<?> c1=SGClassLoader.doLoadClass(jarName, "org.sellgirlPayHelperNotSpring.model.AppKey", i);
		if(null!=c1) {
			System.out.println("c exist step:"+i.GetValue());
			
//			AppKey ins=(AppKey)c1.getDeclaredConstructor().newInstance();
//			ins.setEmail("aaa");
//			System.out.println("instance:"+ins.getEmail()+" cls:"+c1.getName());
//			SGClassLoader.ucl.close();
//
//			Class<?> c2=SGClassLoader.ucl.loadClass("org.sellgirlPayHelperNotSpring.model.AppKey");
//			AppKey ins2=(AppKey)c1.getDeclaredConstructor().newInstance();
//			ins2.setEmail("bbb");
//			System.out.println("instance:"+ins2.getEmail()+" cls:"+c1.getName());	
//			System.out.println("c2 is "+(null==c2?"":"not")+" null");	
			
			
		}else {
			System.out.println("c not exist");
		}
	}

	public void testClassLoader2() throws Exception {
		SGRef<Integer> i=new SGRef<Integer>(0);
		if(new File(jarName).exists()) {
			System.out.println("f exist");
		}else {
			System.out.println("f not exist");
		}
		
		SGClassLoader2 loader=new SGClassLoader2();
//		Class<?> c1=SGClassLoader.doLoadClass(jarName, "com.sellgirl.knightsashax.GameXScreen", i);
//		Class<?> c1=SGClassLoader.doLoadClass(jarName, "com.sellgirl.sgJavaHelper.SGDataHelper", i);
		Class<?> c1=loader.doLoadClass(jarName, "org.sellgirlPayHelperNotSpring.model.AppKey", i);
		if(null!=c1) {
			System.out.println("c exist step:"+i.GetValue());
			
//			AppKey ins=(AppKey)c1.getDeclaredConstructor().newInstance();
//			ins.setEmail("aaa");
//			System.out.println("instance:"+ins.getEmail()+" cls:"+c1.getName());
//			SGClassLoader.ucl.close();
//
//			Class<?> c2=SGClassLoader.ucl.loadClass("org.sellgirlPayHelperNotSpring.model.AppKey");
//			AppKey ins2=(AppKey)c1.getDeclaredConstructor().newInstance();
//			ins2.setEmail("bbb");
//			System.out.println("instance:"+ins2.getEmail()+" cls:"+c1.getName());	
//			System.out.println("c2 is "+(null==c2?"":"not")+" null");	
			
			
		}else {
			System.out.println("c not exist");
		}
		loader.dispose();
	}
}
