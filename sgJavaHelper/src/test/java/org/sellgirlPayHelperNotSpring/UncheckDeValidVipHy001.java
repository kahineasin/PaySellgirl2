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
@SuppressWarnings(value = { "unused", "deprecation" })
public class UncheckDeValidVipHy001 extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(UncheckDeValidVipHy001.class);

	// private static final Logger LOGGER=
	// PFDataHelper.getLogger(UncheckDeValidVipHy001.class);
	// private static Logger LOGGER=null;
	public static void initPFHelper() {
		SGDataHelper.SetConfigMapper(new PFConfigTestMapper());
		new SGDataHelper(new PFAppConfig());

		// LOGGER= PFDataHelper.getLogger(UncheckDeValidVipHy001.class);
	}

	public void testVipHybh() {
		String vipHybh = "3001425710";//""3001418080";

		initPFHelper();
		Boolean hasError=false;
		// org.slf4j.impl.SimpleLogger
		// org.slf4j.impl.StaticLoggerBinder
		// bulk到ClickHouse
		ISGJdbc dayJdbc = JdbcHelperTest.GetDayProdJdbc();
		// IPFJdbc dstJdbc = JdbcHelperTest.GetDayJdbc();
		ISGJdbc liGet10129Jdbc = JdbcHelperTest.GetLiGeShopJdbc();
		ISGJdbc liGet10177Jdbc = JdbcHelperTest.GetLiGeShop10177Jdbc();
		ISqlExecute sqlExec = null;
		try {
			sqlExec = SGSqlExecute.Init(dayJdbc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sql = "select hybh from t_hyzl where hybh='" + vipHybh + "'";
		if (null == sqlExec.QuerySingleValue(sql)) {
			LOGGER.info("day33库没有此会员 " + sql);
		}

		try {
			sqlExec = SGSqlExecute.Init(liGet10129Jdbc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//sql = "select card_no from mall_center_member.member_info where card_no='" + vipHybh + "'";
		sql = "select card_status from mall_center_member.member_info where card_no='" + vipHybh + "'";
		Integer r = SGDataHelper.ObjectToInt(sqlExec.QuerySingleValue(sql));
		if (null == r) {
			LOGGER.error(SGDataHelper.FormatString("liGeMerge10129库没有此会员[{0}] \r\n sql:{1}", vipHybh,sql));
			if(!hasError) {hasError=true;}
		} else {
//			LiGeHyType b = LiGeHyType.values()[r];
//			switch (b) {
//			case 未开卡:
//			case 未激活:
//			case 已注销:
//				LOGGER.error(PFDataHelper.FormatString("liGeMerge10129库会员[{0}]的状态异常,{1} \r\n sql:{2}", vipHybh,b.getText(),sql));
//				if(!hasError) {hasError=true;}
//				break;
//			default:
//				break;
//			}
			if(LiGeHyType.未开卡.getValue()==r||LiGeHyType.未激活.getValue()==r||LiGeHyType.已注销.getValue()==r){
				LOGGER.error(SGDataHelper.FormatString("liGeMerge10129库会员[{0}]的状态异常,{1} \r\n sql:{2}", vipHybh,LiGeHyType.initByInt(r).getText(),sql));
				if(!hasError) {hasError=true;}
			}else{

			}
//			switch (r) {
//				case LiGeHyType.未开卡.getValue():
//				case LiGeHyType.未激活.getValue():
//				case LiGeHyType.已注销.getValue():
//					LOGGER.error(PFDataHelper.FormatString("liGeMerge10129库会员[{0}]的状态异常,{1} \r\n sql:{2}", vipHybh,LiGeHyType.initByInt(r).getText(),sql));
//					if(!hasError) {hasError=true;}
//					break;
//				default:
//					break;
//			}
		}


		try {
			sqlExec = SGSqlExecute.Init(liGet10177Jdbc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		sql = "select card_status from mall_center_member.member_info where card_no='" + vipHybh + "'";
		 r = SGDataHelper.ObjectToInt(sqlExec.QuerySingleValue(sql));
		if (null == r) {
			//LOGGER.error("liGeMerge10129库没有此会员 " + sql);
			LOGGER.error(SGDataHelper.FormatString("liGeMerge10177库没有此会员[{0}] \r\n sql:{1}", vipHybh,sql));
			if(!hasError) {hasError=true;}
		} else {
			if(LiGeHyType.未开卡.getValue()==r||LiGeHyType.未激活.getValue()==r||LiGeHyType.已注销.getValue()==r){
				LOGGER.error(SGDataHelper.FormatString("liGeMerge10177库会员[{0}]的状态异常,{1} \r\n sql:{2}", vipHybh,LiGeHyType.initByInt(r).getText(),sql));
				if(!hasError) {hasError=true;}
			}else{

			}
		}
		if(!hasError) {LOGGER.info("会员 " + vipHybh + " 的数据正常");}
		
	}
}
