package org.sellgirlPayHelperNotSpring;

//import org.ho.yaml.Yaml;
//import org.ho.yaml.Yaml;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import java.time.ZoneId;
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
import java.util.TimeZone;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.sellgirlPayHelperNotSpring.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sellgirl.sgJavaHelper.*;
import com.sellgirl.sgJavaHelper.config.*;
import com.sellgirl.sgJavaHelper.config.SGDataHelper.LocalDataType;
import com.sellgirl.sgJavaHelper.model.FileSizeUnitType;
import com.sellgirl.sgJavaHelper.model.SysType;
import com.sellgirl.sgJavaHelper.model.UserOrg;
import com.sellgirl.sgJavaHelper.model.UserTypeClass;
import com.sellgirl.sgJavaHelper.sgEnum.IPFEnum;
import com.sellgirl.sgJavaHelper.sgEnum.SGFlagEnum;
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

import junit.framework.TestCase;

//import test.java.pfHelper.model.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * 向量测试
 * 
 * @author Administrator
 *
 */
@SuppressWarnings(value = { "unused", "rawtypes", "serial", "deprecation" })
public class UncheckVector extends TestCase {
	//private static final Logger LOGGER = LoggerFactory.getLogger(UncheckVector.class);

	public static void initPFHelper() {
		try {
			SGDataHelper.SetConfigMapper(new PFConfigTestMapper());
			new SGDataHelper(new PFAppConfig());
			// File dumpFile = new File("D:\\gitee\\secretKey\\paySellgirl\\key.yml");
			// AppKey appKey = Yaml.loadType(dumpFile, AppKey.class);
//			PFEmailSend.EMAIL_OWNER_ADDR_HOST="";
//			PFEmailSend.EMAIL_OWNER_ADDR="";
//			PFEmailSend.EMAIL_OWNER_ADDR_PASS=AES.AESDecryptDemo(appKey.getEmailPwd(),"");
//			PFEmailSend.EMAIL_OWNER_ADDR_HOST_PROPERTY=HostType.SELLGIRL.getProperties();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testSunlightStrong() {
		SGDataHelper.getLog().print("------------春 3月------------");
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(0, new SGDate(2000,3,1,6,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(0, new SGDate(2000,3,1,12,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(0, new SGDate(2000,3,1,18,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(0, new SGDate(2000,3,1,24,0,0)));		
		//夏
		SGDataHelper.getLog().print("------------夏 6月------------");
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(0, new SGDate(2000,6,1,6,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(0, new SGDate(2000,6,1,12,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(0, new SGDate(2000,6,1,18,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(0, new SGDate(2000,6,1,24,0,0)));
		
		//秋
		SGDataHelper.getLog().print("------------秋 9月------------");
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(0, new SGDate(2000,9,1,6,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(0, new SGDate(2000,9,1,12,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(0, new SGDate(2000,9,1,18,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(0, new SGDate(2000,9,1,24,0,0)));
		//冬
		SGDataHelper.getLog().print("------------冬 12月------------");
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(0, new SGDate(2000,12,1,6,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(0, new SGDate(2000,12,1,12,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(0, new SGDate(2000,12,1,18,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(0, new SGDate(2000,12,1,24,0,0)));
		SGDataHelper.getLog().print("------------从上面看出, 赤道春秋最热------------");
		
		double a=23d*Math.PI/180d;//北半球
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(a, new SGDate(2000,3,1,6,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(a, new SGDate(2000,3,1,12,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(a, new SGDate(2000,3,1,18,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(a, new SGDate(2000,3,1,24,0,0)));
		
		//夏
		SGDataHelper.getLog().print("------------夏 6月------------");
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(a, new SGDate(2000,6,1,6,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(a, new SGDate(2000,6,1,12,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(a, new SGDate(2000,6,1,18,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(a, new SGDate(2000,6,1,24,0,0)));
		
		//秋
		SGDataHelper.getLog().print("------------秋 9月------------");
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(a, new SGDate(2000,9,1,6,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(a, new SGDate(2000,9,1,12,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(a, new SGDate(2000,9,1,18,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(a, new SGDate(2000,9,1,24,0,0)));
		//冬
		SGDataHelper.getLog().print("------------冬 12月------------");
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(a, new SGDate(2000,12,1,6,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(a, new SGDate(2000,12,1,12,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(a, new SGDate(2000,12,1,18,0,0)));
		SGDataHelper.getLog().print(SGDataHelper.getSunlightStrong(a, new SGDate(2000,12,1,24,0,0)));
		SGDataHelper.getLog().print("------------从上面看出, 北半球夏天最热------------");
	}

	public void testCrossProduct() {
		float[] direction=new float[] {0,0,-1};
		float[] up=new float[] {0,1,0};
		float[] right=SGVectorHelper.crossProduct(direction, up);
		SGDataHelper.getLog().print(""+right[0]+" "+right[1]+" "+right[2]);
	}
}
