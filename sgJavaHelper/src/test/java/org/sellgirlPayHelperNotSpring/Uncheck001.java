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
import java.io.IOException;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
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
import com.sellgirl.sgJavaHelper.file.SGDirectory;
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
import com.sellgirl.sgJavaHelper.time.SGTimeSpan;

import junit.framework.TestCase;

//import test.java.pfHelper.model.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * 不要随便更改此类名,以防打包时执行了此类
 * 
 * @author Administrator
 *
 */
@SuppressWarnings(value = { "unused", "rawtypes", "serial", "deprecation" })
public class Uncheck001 extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(Uncheck001.class);
	public static AppKey appKey=null;
	public static void initPFHelper()  {
		try {
			SGDataHelper.SetConfigMapper(new PFConfigTestMapper());
			new SGDataHelper(new PFAppConfig());
//			File dumpFile = new File("D:\\gitee\\secretKey\\paySellgirl\\key.yml");
//			AppKey appKey = Yaml.loadType(dumpFile, AppKey.class);
			appKey = SGYamlHelper.loadType("D:\\gitee\\secretKey\\paySellgirl\\key.yml", AppKey.class);
			SGEmailSend.EMAIL_OWNER_ADDR_HOST="";
			SGEmailSend.EMAIL_OWNER_ADDR=AES.AESDecryptDemo(appKey.getEmail(),UncheckLoadKey001.getKey());
			SGEmailSend.EMAIL_OWNER_ADDR_PASS=AES.AESDecryptDemo(appKey.getEmailPwd(),UncheckLoadKey001.getKey());
			SGEmailSend.EMAIL_OWNER_ADDR_HOST_PROPERTY=HostType.SELLGIRL.getProperties();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public void testIntRange() {
//		System.out.println(""+Long.MAX_VALUE);
//		System.out.println(""+Integer.MAX_VALUE);
		long mask=1<<29;
		for(long i=1;i<=40;i++) {
			mask=1l<<i;
			System.out.println(""+mask+"   "+i);	
		}
	}
	public void testLiCode() {
		int[] nums=new int[] {0,1,2,3,5};
		int n=nums.length;
		int r=(n*(n-1)*(n-2)*(n-3))/
				(4*3*2*1);
		System.out.println(r);
	}
	/**
	 * 上升4元组
	 * https://leetcode.cn/problems/count-increasing-quadruplets/
	 * 
	 * 78 / 121 个通过的测试用例
	 * 79 / 121 个通过的测试用例
	 * 0时0分0秒540毫秒
	 */
	public void testLiCode2() {
		//len:4  result:1
		//len:5  result:5
//		int[] nums=new int[] {0,1,2,3};  //1
		int[] nums=new int[] {0,2,1,3};  //1
		//int[] nums=new int[] {0,1,2,3,4}; //5
//		int[] nums=new int[] {1,3,2,4,5}; //2
//		int[] nums=new int[] {1,3,2,4,5,8,7};
		//int[] nums=new int[] {0,3,2,4,5,6}; //预测3
//		int[] nums=new int[] {3,9,5,4,8,2,1,10,7,6}; //预测7
		//预测51233413
		//int[] nums=new int[] {94,345,419,385,283,219,240,159,315,52,188,378,223,232,407,225,5,190,187,352,356,333,401,269,296,250,261,153,53,169,4,267,288,265,235,146,28,402,266,113,17,224,175,418,197,388,162,341,49,54,392,334,134,326,301,46,139,66,300,332,221,218,147,318,350,189,361,11,170,389,110,166,93,424,374,281,252,47,336,316,247,217,215,115,58,290,48,80,398,196,122,205,130,321,258,314,119,83,346,307,338,199,198,386,89,273,351,29,125,256,106,183,151,425,14,395,98,412,236,212,79,71,142,109,133,193,400,257,416,75,55,111,263,391,366,191,260,303,44,241,179,21,194,348,353,45,100,140,310,88,343,154,38,233,200,211,164,172,77,377,86,242,344,369,357,97,214,359,3,64,329,331,413,337,251,328,13,27,7,81,406,182,340,278,297,201,323,118,203,414,216,302,427,299,376,411,56,167,249,144,105,78,180,82,311,209,339,255,16,342,371,136,60,287,432,312,362,61,184,12,117,409,126,178,208,165,131,176,50,34,360,65,335,292,18,322,35,304,276,355,253,429,358,204,284,295,70,107,141,155,95,120,423,421,384,417,320,30,135,92,285,123,19,74,430,381,62,298,137,372,32,243,375,87,277,31,143,403,36,313,410,124,116,428,279,156,327,294,101,173,259,42,270,171,271,202,102,380,230,368,308,43,364,325,59,274,91,289,382,396,40,192,41,229,129,431,108,15,51,149,96,347,245,2,150,185,73,69,99,262,305,244,367,168,394,128,234,264,282,68,422,415,319,222,405,363,324,286,112,275,158,393,10,210,127,114,268,293,404,213,9,306,272,231,408,145,186,390,207,174,226,72,39,383,85,181,160,121,8,67,248,22,104,161,373,227,330,228,37,20,157,399,1,379,23,239,237,148,365,103,57,163,6,132,220,291,397,309,84,246,238,420,90,370,25,254,280,26,354,317,206,152,426,24,138,349,63,195,387,177,76,33}; 
		//int[] nums=new int[] {217,94,229,63,156,252,183,56,79,215,201,57,158,52,203,154,212,155,38,241,114,37,270,39,118,267,265,152,243,151,58,186,149,55,177,170,179,41,250,138,223,133,67,107,73,185,119,24,214,175,180,121,204,191,104,44,249,219,131,227,117,188,190,174,64,19,109,87,233,146,230,197,139,236,97,25,68,253,247,40,238,234,256,96,147,193,245,140,251,157,98,99,231,225,86,137,43,206,134,129,76,101,7,42,53,164,122,200,123,224,172,31,261,6,10,32,26,70,195,81,143,248,211,268,69,161,258,77,27,232,235,125,141,3,1,142,168,106,115,213,263,120,74,244,264,14,51,221,126,23,48,111,5,176,91,71,266,135,194,59,95,110,220,255,80,254,116,4,90,165,83,192,198,269,202,127,178,237,2,207,89,240,11,113,61,22,173,167,136,12,54,93,29,30,92,65,100,20,184,130,187,163,34,205,242,16,102,222,33,153,88,150,82,262,124,257,226,17,75,103,216,209,148,21,182,208,112,78,162,189,13,50,36,108,47,8,9,144,260,199,196,128,246,228,46,15,239,84,28,218,160,259,72,171,210,159,145,105,169,66,35,132,85,60,18,181,166,62,45,49};
		
//		System.out.println(getCnt(nums,0,3));
//		System.out.println(r);
//		System.out.println(getCnt2(nums,0,4,-1,0));
//		System.out.println(getCnt3(nums,0,4,-1));
		long t=SGDataHelper.CountTime(a->{
			size=nums.length;
			size2=size+5;
			size3=size+1;
//			System.out.println(getCnt4(nums,0,4,-1,size2));
			System.out.println(getCnt5(nums,0,4,-1,size2));
		});
		System.out.println(SGDataHelper.GetTimeSpan(t, SGYmd.Hour | SGYmd.Minute | SGYmd.Second|SGYmd.Millisecond));
	}
	
	private int size;
	private int size2;
	private int size3;
	private int getCnt5(int[] nums,int step,int need,int min,int max) {
		// step:0  cnt  0,  
		if(0==need) {return 1;}
		if(size<=step){return 0;}
		int r=0;

		for(int i=step;i<size3-need;i++) {
			if(0==step||(min<nums[i]&&max>nums[i])) {
				if(1>=need) {
					r++;
				}else {
//					r+=getCnt(nums,step+1,need-1);
//					r+=getCnt(nums,i,need-1);		
//					int tmp=getCnt3(nums,i+1,need-1,nums[i]);
//					r+=tmp;
					
					if(4==need) {
						//System.out.println("need:"+need+" min:"+nums[i]+" max:"+max);
						r+=getCnt5(nums,i+1,need-1,nums[i],max);
					}else if(3==need) {
						//System.out.println("need:"+need+" min:"+min+" max:"+nums[i]);
						r+=getCnt5(nums,i+1,need-1,min,nums[i]);
					}else if(2==need) {
						//System.out.println("need:"+need+" min:"+max+" max:"+(nums.length+5));
						r+=getCnt5(nums,i+1,need-1,max,size2);
					}

				}
			}
		}
		return r;
	}
//	private int getCnt4(int[] nums,int step,int need,int min,int max) {
//		// step:0  cnt  0,  
//		if(0==need) {return 1;}
//		if(size<=step){return 0;}
//		int r=0;
//		//int cur=nums[step];
////		boolean useMin=need%2==1;
//		int oldMin=min;
//		int oldMax=max;
//		for(int i=step;i<size;i++) {
//			if(0==step||(oldMin<nums[i]&&oldMax>nums[i])) {
//				if(1>=need) {
//					r++;
//				}else {
////					r+=getCnt(nums,step+1,need-1);
////					r+=getCnt(nums,i,need-1);		
////					int tmp=getCnt3(nums,i+1,need-1,nums[i]);
////					r+=tmp;
//					
//					if(4==need) {
//						//System.out.println("need:"+need+" min:"+nums[i]+" max:"+max);
//						r+=getCnt4(nums,i+1,need-1,nums[i],max);
//					}else if(3==need) {
//						//System.out.println("need:"+need+" min:"+min+" max:"+nums[i]);
//						r+=getCnt4(nums,i+1,need-1,min,nums[i]);
//					}else if(2==need) {
//						//System.out.println("need:"+need+" min:"+max+" max:"+(nums.length+5));
//						r+=getCnt4(nums,i+1,need-1,max,nums.length+5);
//					}
//					
////					if(4==need) {
////						min=nums[i];
////						System.out.println("need:"+need+" min:"+min+" max:"+max);
////						//r+=getCnt4(nums,i+1,need-1,min,max);
////					}else if(3==need) {
////						max=nums[i];
////						System.out.println("need:"+need+" min:"+min+" max:"+max);
////						//r+=getCnt4(nums,i+1,need-1,min,max);
////					}else if(2==need) {
////						min=oldMax;
////						max=nums.length+5;
////						System.out.println("need:"+need+" min:"+min+" max:"+max);
////						//r+=getCnt4(nums,i+1,need-1,min,max);
////					}else {
////						int aa=1;
////						//System.out.println("ttttttttttttttt");
////					}
////					r+=getCnt4(nums,i+1,need-1,min,max);
//				}
//			}
//		}
//		return r;
//	}
//	private int getCnt3(int[] nums,int step,int need,int cmp) {
//		// step:0  cnt  0,  
//		if(0==need) {return 1;}
//		if(nums.length<=step){return 0;}
//		int r=0;
//		//int cur=nums[step];
//		for(int i=step;i<nums.length;i++) {
//			if(0==step||cmp<nums[i]) {
//				if(1>=need) {
//					r++;
//				}else {
////					r+=getCnt(nums,step+1,need-1);
////					r+=getCnt(nums,i,need-1);		
////					int tmp=getCnt3(nums,i+1,need-1,nums[i]);
////					r+=tmp;
//					r+=getCnt3(nums,i+1,need-1,nums[i]);
//				}
//			}
//		}
//		return r;
//	}
//	private int r=0;
	
//	private int getCnt2(int[] nums,int step,int need,int last) {
//		// step:0  cnt  0,  
////		if(0==need) {return 1;}
//		if(nums.length<=step) {return 0;}
//		if(nums[step]>last) {
//			need--;
//			last=nums[step];
//		}
//		return getCnt2(nums,step+1,need,last);
//	}
//	private int getCnt(int[] nums,int step,int need) {
//		// step:0  cnt  0,  
//		if(0==need) {return 1;}
//		if(nums.length<=step){return 0;}
//		int r=0;
//		int cur=nums[step];
//		for(int i=step+1;i<nums.length;i++) {
//			if(cur<nums[i]) {
//				if(1>=need) {
//					r++;
//				}else {
////					r+=getCnt(nums,step+1,need-1);
////					r+=getCnt(nums,i,need-1);		
//					int tmp=getCnt(nums,i,need-1);
//					r+=tmp;
//				}
//			}
//		}
//		return r;
//	}
	
	public void testMetaData() throws SQLException, FileNotFoundException {
		initPFHelper();
		ISGJdbc dstJdbc = JdbcHelperTest.GetDayJdbc();
		// IPFJdbc dstJdbc = JdbcHelperTest.GetLiGeShopJdbc();
		ISqlExecute dstExec = null;
		try {
			dstExec = SGSqlExecute.Init(dstJdbc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		//String sql = "select  top 1 [Money] from t_ordersdetail_recent ";

//		Object v=dstExec.QuerySingleValue(sql);
//		String vs=PFDataHelper.ObjectToDateString(v);
//		assertTrue("2021-10-01".equals( vs));
		List<String> fieldNames=new ArrayList<>();
		fieldNames.add("Money");
		ResultSetMetaData rs = dstExec.GetMetaData("t_ordersdetail_recent",fieldNames);
		Integer a=rs.getPrecision(1);
		Integer b=rs.getScale(1);
		System.out.println(a);
		System.out.println(b);
	}

	public void testDataReader() {
		initPFHelper();
		ISGJdbc dstJdbc = JdbcHelperTest.GetTidbSaleJdbc();
		// IPFJdbc dstJdbc = JdbcHelperTest.GetLiGeShopJdbc();
		ISqlExecute dstExec = null;
		try {
			dstExec = SGSqlExecute.Init(dstJdbc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		// 这里发现个奇怪的问题,tidb读不了 date_add(STR_TO_DATE('2021.10.01','%Y.%m.%d'), INTERVAL -1
		// year) 列
		// 报错:java.sql.SQLException: Invalid length (10) for type TIMESTAMP 未有解决方法 改为
		// '%Y-%m-%d %H:%i:%s' 格式才行
//		String sql="select  2 as col1 "+
//		", date_add(STR_TO_DATE('2021.10.01','%Y.%m.%d'), INTERVAL -1 year) as ly_create_date  "+
//		",date_add(STR_TO_DATE('2021-10-01 00:00:00','%Y-%m-%d %H:%i:%s'), INTERVAL -1 year) as ly_create_date2"+
//		",STR_TO_DATE('2021.10.01','%Y.%m.%d') as ly_create_date3";

		String sql = "select  "
				+ " date_add(STR_TO_DATE('2021.10.01','%Y.%m.%d'), INTERVAL -1 year) as ly_create_date  "
				+ ",date_add(STR_TO_DATE('2021-10-01 00:00:00','%Y-%m-%d %H:%i:%s'), INTERVAL -1 year) as ly_create_date2";

//		Object v=dstExec.QuerySingleValue(sql);
//		String vs=PFDataHelper.ObjectToDateString(v);
//		assertTrue("2021-10-01".equals( vs));

		ResultSet rs = dstExec.GetDataReader(sql);
		try {
			if (rs.next()) {
				int type2 = rs.getMetaData().getColumnType(2);
				String typeName2 = rs.getMetaData().getColumnTypeName(2);
				Object r1 = rs.getTimestamp(2);
				Object r2 = rs.getTimestamp(1);
//				//Object r1=rs.getObject(1);
//				//Calendar.getAvailableCalendarTypes();
//				//Object r2=rs.getString(2);
//				//Object r6=rs.getDateTime(2);
//				Object r9=rs.getDate(2);
//				Object r8=rs.getDate(4);
//				Object r5=rs.getTimestamp(2);
//				Object r7=rs.getDate(2);
//				Object r3=rs.getTimestamp(2);
//				Object r4=rs.getObject(2);
				String aa = "aa";
			}
			dstExec.CloseReader(rs);
			dstExec.CloseConn();
			;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testSmallDatetime() throws Exception {

		ISGJdbc dstJdbc = JdbcHelperTest.GetDayJdbc();
		ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
		ResultSet rs = dstExec.GetDataReader("select c4,c5 from test1");
		try {
			ResultSetMetaData md = rs.getMetaData();
			int dataTypeI1 = md.getColumnType(1);// 93
			int dataTypeI2 = md.getColumnType(2);// 93
			String dataType1 = md.getColumnTypeName(1);// smalldatetime
			String dataType2 = md.getColumnTypeName(2);// datetime
			int pre1 = md.getPrecision(1);
			int pre2 = md.getPrecision(2);
			int scale1 = md.getScale(1);
			int scale2 = md.getScale(2);
			rs.next();

			Object ds = SGDataHelper.ObjectToDateString(rs.getTimestamp(2), "yyyy-MM-dd HH:mm:00");
			Timestamp ds2 = rs.getTimestamp(2);
			Calendar c2 = Calendar.getInstance();
			c2.setTime(ds2);
			// ds2.setSeconds(0);
			String aa = "aa";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testBulkNString() {
		initPFHelper();
//	IPFJdbc dstJdbc = GetDayJdbc();
//	ISqlExecute srcExec = PFSqlExecute.Init(dstJdbc);
//	ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//	//使用NString前
//	ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,'aa' as c6,'中国' as c7");//ok
//	//ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,'中国' as c6,'中国' as c7");//报错:表示列 5 的 Unicode 数据大小的字节数为奇数。应为偶数。
//	//ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,N'中国' as c6,'中国' as c7");//报错:表示列 5 的 Unicode 数据大小的字节数为奇数。应为偶数。
//	//使用updateString后(这个micro库似乎不支持updateNString方法)
//	//ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,'aa' as c6,'中国' as c7");//尚不支持该操作
//	//ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,'中国' as c6,'中国' as c7");//表示列 5 的 Unicode 数据大小的字节数为奇数。应为偶数。
//	//ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,N'中国' as c6,'中国' as c7");//表示列 5 的 Unicode 数据大小的字节数为奇数。应为偶数。
//	
//	  dstExec.Delete("test1", null);
//	  dstExec.HugeBulkReader(null, srcDr, "test1", null,null,null);

		// 测试int导入decimal
		ISGJdbc srcJdbc = JdbcHelperTest.GetLiGeOrderJdbc();
		ISGJdbc dstJdbc = JdbcHelperTest.GetDayJdbc();
		ISqlExecute srcExec = null;
		ISqlExecute dstExec = null;
		try {
			srcExec = SGSqlExecute.Init(srcJdbc);
			dstExec = SGSqlExecute.Init(dstJdbc);
			// 使用NString前
			ResultSet srcDr = srcExec.GetHugeDataReader("SELECT a.code as Agentno\r\n" + ", a.name as Agentname\r\n"
					+ ", CONCAT(a.province_name,a.city_name,a.area_name,a.street_name,b.detail_address) as Address\r\n"
					+ ",\r\n" + "company_code as Fgsno\r\n" + ",b.contact_address as Linkaddress\r\n"
					+ ",'' as Sendaddress\r\n" + ",c.card_no as fzrbh\r\n" + ",c.realname as fzrxm\r\n"
					+ ",d.name as Fgsname,a.del as Cancelflag,province_name as Province,\r\n"
					+ "city_name as City,is_service_shop as isnetwork,mobile as fzrmobile,\r\n"
					+ "zip_code as postcode,if(is_main_shop=2,1,0) as Agentflag,shop_type as agentType,\r\n"
					+ "concat(a.email, '@perfect99.com') email,\r\n" + "d.shop_province_name as fgs_province,\r\n"
					+ "a.business_mode \r\n" + "FROM mall_center_store.store a\r\n"
					+ "left join mall_center_store.store_delivery_info b on b.store_code =a.code and b.del=0\r\n"
					+ "-- left join mall_center_store.store_leader_info c on c.store_code =a.code and c.del=0\r\n"
					+ "left join mall_center_store.store_leader_info c on c.id =a.leader_id\r\n"
					+ "left join mall_center_sys.sys_company d on d.code =a.company_code \r\n"
					+ "where a.true_del = 0 limit 1");

			// dstExec.Delete("t_agent", null);
			dstExec.HugeBulkReader(null, srcDr, "t_agent", null, null, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testBulkNumeric() {
		initPFHelper();

		// 测试int导入decimal
		ISGJdbc srcJdbc = JdbcHelperTest.GetLiGeOrderJdbc();
		ISGJdbc dstJdbc = JdbcHelperTest.GetDayJdbc();
		ISqlExecute srcExec = null;
		ISqlExecute dstExec = null;
		try {
			srcExec = SGSqlExecute.Init(srcJdbc);
			dstExec = SGSqlExecute.Init(dstJdbc);
			// 使用NString前
			ResultSet srcDr = srcExec.GetHugeDataReader("select o.order_no as Orderno,\r\n"
					+ "-- order_month as Cmonth,\r\n"
					+ "CONCAT(left(o.order_month,4),'.',right(o.order_month,2)) as cmonth,\r\n"
					+ "p.serial_no as Inv_no,p.title as Inv_name,p.quantity as Qty,\r\n"
					+ "-- p.price as Price, -- 原价,非活动商品是空的\r\n" + "-- p.retail_price as Price, \r\n"
					+ "-- case p.promotion_type when 0 then p.retail_price else p.price end as Price,\r\n"
					+ "-- case when p.promotion_type = 0 and p.order_type <> 2 then p.retail_price  else p.price end as Price,\r\n"
					+ "case when p.promotion_type = 0 and (p.order_type is null or p.order_type <> 2) then p.retail_price  else p.price end as Price, -- 20220507\r\n"
					+ "-- p.pv as Pv,\r\n" + "CAST(p.pv AS SIGNED) as Pv,\r\n" + "p.security_price as discount,\r\n"
					+ "p.total_price as Money , -- 注意,在java代码里计算了Money列,所以这里不更新也没问题\r\n" + "0 as invalid  \r\n"
					+ "from mall_center_order.order_order o,mall_center_order.order_product p \r\n"
					+ "where o.id = p.order_id\r\n" + "-- and o.order_month='{ym}'\r\n"
					+ "and o.order_status in (2,3,99) \r\n" + "and o.order_type in (1,2,4)\r\n" + "and o.del = 0\r\n"
					+ "and o.order_no ='SG967067220805000022'\r\n" + "and p.serial_no ='TPP18'");

			// dstExec.Delete("t_agent", null);
			dstExec.HugeBulkReader(null, srcDr, "t_ordersdetail_recent", null, null, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testClickHouseBulk() throws Exception {
//		System.out.print("    SELECT  \r\n" + 
//				"     ORDINAL_POSITION fieldIdx,\r\n" + 
//				"      COLUMN_NAME fieldName, -- 列名,  \r\n" + 
//				"      if(COLUMN_KEY='PRI',b'1',b'0')  isPrimaryKey,\r\n" + 
//				"       COLUMN_TYPE 数据类型,  \r\n" + 
//				"        DATA_TYPE fieldType, -- 字段类型,  \r\n" + 
//				"      CHARACTER_MAXIMUM_LENGTH fieldSqlLength, -- 长度,  \r\n" + 
//				"      -- IS_NULLABLE 是否为空,  \r\n" + 
//				"      if(IS_NULLABLE='YES',b'0',b'1')  isRequired,\r\n" + 
//				"      COLUMN_DEFAULT defaultValue, -- 默认值,  \r\n" + 
//				"      COLUMN_COMMENT columnDescription -- 备注   \r\n" + 
//				"    FROM  \r\n" + 
//				"     INFORMATION_SCHEMA.COLUMNS  \r\n" + 
//				"    where  \r\n" + 
//				"    -- developerclub为数据库名称，到时候只需要修改成你要导出表结构的数据库即可  \r\n" + 
//				"    -- table_schema ='cbbk'  \r\n" + 
//				"    -- AND  \r\n" + 
//				"    -- article为表名，到时候换成你要导出的表的名称  \r\n" + 
//				"    -- 如果不写的话，默认会查询出所有表中的数据，这样可能就分不清到底哪些字段是哪张表中的了，所以还是建议写上要导出的名名称  \r\n" + 
//				"    table_name  = '{0}' ");

//		initPFHelper();
//		// bulk到ClickHouse
//		IPFJdbc srcJdbc = JdbcHelperTest.GetLiGeShopJdbc();
//		IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseShopJdbc();
//		ISqlExecute srcExec = PFSqlExecute.Init(srcJdbc);
//		ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//		String dstTableName="monthly_shop_user";
//		// 使用NString前
//		ResultSet srcDr = srcExec
//				.GetHugeDataReader(("select\r\n" + 
//						"Cast('{cmonth}.01' as datetime) as data_date,\r\n" + 
//						"a.card_no as user_id,\r\n" + 
//						"STR_TO_DATE(if(b.birthday<'1753-01-01 00:00:00',null,b.birthday),'%Y-%m-%d') as birthday,\r\n" + 
//						"if(a.gender=1,'男','女') as sex,\r\n" + 
//						"b.education,\r\n" + 
//						"date_format(a.open_card_time, '%Y.%m') as regist_month, -- 办卡月份\r\n" + 
//						"b.province as regist_province,\r\n" + 
//						" trim(a.certificates_no) as id_card,\r\n" + 
//						" a.mobile, -- 注册手机号 也就是用来登陆的手机号\r\n" + 
//						" a.email,\r\n" + 
//						" a.user_source, -- 顾客来源 1完美商城 2邀请注册\r\n" + 
//						" b.profession,\r\n" + 
//						" b.spouse_or_not, -- 是否有配偶：0->否；1->是\r\n" + 
//						"\r\n" + 
//						" \r\n" + 
//						" \r\n" + 
//						" \r\n" + 
//						" -- b.nationality, -- 国籍  0 中国国籍，1 外国国籍 \r\n" + 
//						"-- a.realname as Hyxm,\r\n" + 
//						"-- if(a.cancel_status=0,0,1) as Qx,\r\n" + 
//						"-- b.spouse_realname as Pexm,\r\n" + 
//						"-- b.spouse_certificates_no as Pesfzh,\r\n" + 
//						"-- b.emer_contact_home_phone as Tel1,\r\n" + 
//						"-- b.emer_contact_mobile as Tel2,\r\n" + 
//						"-- c.bank_no as Bankzh,\r\n" + 
//						"-- c.bank_open_name as Bankname,\r\n" + 
//						"-- a.handled_card_no as bjhybh,\r\n" + 
//						"-- '' as l_open_card_shop_id,\r\n" + 
//						"-- store_code as agentno,\r\n" + 
//						"-- replace(a.cancel_date,'-','.') as qxmonth,\r\n" + 
//						"-- b.province  ,\r\n" + 
//						"-- b.city  ,\r\n" + 
//						"-- b.district as county,\r\n" + 
//						"-- b.address  ,\r\n" + 
//						"-- STR_TO_DATE(b.birthday,'%Y-%m-%d') as fbirth,\r\n" + 
//						"-- open_card_time as fjoindate,\r\n" + 
//						"-- bank_open_type as BankNo\r\n" + 
//						"-- c.bank_open_type as BankNo ,\r\n" + 
//						"-- null as l_user_id,\r\n" + 
//						"-- null as l_personal_info_id,\r\n" + 
//						"-- card_status as status,\r\n" + 
//						"-- mobile  \r\n" + 
//						"a.id as sys_limit_id\r\n" + 
//						"from mall_center_member.member_info a\r\n" + 
//						"left join mall_center_member.member_detail b on b.id=a.id\r\n" + 
//						"-- left join mall_center_member.member_provide_bank c on c.member_id =a.id and c.provide_type=2\r\n" + 
//						"-- left join mall_center_sys.sys_region d on d.code = c.province\r\n" + 
//						"-- left join mall_center_sys.sys_region e on e.code = c.city\r\n" + 
//						" where a.card_status<>-3  and a.card_status<>2\r\n" + 
//						" -- and a.id>1218860835308504156\r\n" + 
//						" -- and a.card_no  in('12638832')\r\n" + 
//						" -- limit 700000,28000 -- ok\r\n" + 
//						" -- and b.province is not null and b.province <>''\r\n" + 
//						" limit 100\r\n" + 
//						"\r\n" + 
//						"")
//						.replace("{cmonth}","2021.04")
//						);
//		// ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,cast((CASE 1
//		// WHEN 1 THEN 200 WHEN 2 THEN 800 ELSE NULL END) as DECIMAL) as c8");//这样ok
//
//		//dstExec.Delete("test1", null);
//		dstExec.HugeDelete(dstTableName, where -> {
////  		where.Add("data_date",PFDate.Now().ToCalendar());
//		// where.Add("data_date",PFDate.Now().GetDayStart().ToCalendar());
//		//where.Add("data_date", transfer.getPFCmonth().ToDateTime());
//	});
//		//dstExec.HugeInsertReader(null, srcDr,dstTableName, null, null, null);//这样可以，但50000一批时会卡住
//		dstExec.HugeBulkReader(null, srcDr,dstTableName, null, null, null);

	}

	public void testBitConvert() throws Exception {
		initPFHelper();
		// bulk到ClickHouse
		ISGJdbc srcJdbc = JdbcHelperTest.GetLiGeShopJdbc();
		ISGJdbc dstJdbc = JdbcHelperTest.GetDayJdbc();
		ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);
		ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
		String dstTableName = "test1";
		// 使用NString前
		ResultSet srcDr = srcExec
				.GetHugeDataReader(("select 1 as c1,1 as c2,b'1' as c9 union select 2 as c1,2 as c2,b'0' as c9")

				);
		// ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,cast((CASE 1
		// WHEN 1 THEN 200 WHEN 2 THEN 800 ELSE NULL END) as DECIMAL) as c8");//这样ok

		// dstExec.Delete("test1", null);
		dstExec.HugeDelete(dstTableName, where -> {
//  		where.Add("data_date",PFDate.Now().ToCalendar());
			// where.Add("data_date",PFDate.Now().GetDayStart().ToCalendar());
			// where.Add("data_date", transfer.getPFCmonth().ToDateTime());
		});
		// dstExec.HugeInsertReader(null, srcDr,dstTableName, null, null,
		// null);//这样可以，但50000一批时会卡住
		dstExec.HugeBulkReader(null, srcDr, dstTableName, null, null, null);

	}

	public void testBulk2Mysql() throws Exception {

		ISGJdbc srcJdbc = JdbcHelperTest.GetYJQueryJdbc();
		ISGJdbc dstJdbc = JdbcHelperTest.GetUserProfileJdbc();

		srcJdbc.setUrl(srcJdbc.getUrl().replace("yjquery", "yjquery202101"));

		ISqlExecute srcExec = SGSqlExecute.Init(JdbcHelperTest.GetYJQueryJdbc());
		ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
		// 使用NString前
		ResultSet srcDr = srcExec.GetDataReader("SELECT  hybh AS [user_id],'' as tag_weight,\r\n"
				+ "CONVERT(datetime,CONVERT(varchar(10),GETDATE(),120)) AS data_date,\r\n"
				+ "'ATTR_U_02_001' AS tag_id ,'sex' as theme\r\n" + "FROM hyzl WHERE qx=0 and hyxb='男'");// ok
		// ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,'中国' as
		// c6,'中国' as c7");//报错:表示列 5 的 Unicode 数据大小的字节数为奇数。应为偶数。
		// ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,N'中国' as
		// c6,'中国' as c7");//报错:表示列 5 的 Unicode 数据大小的字节数为奇数。应为偶数。
		// 使用updateString后(这个micro库似乎不支持updateNString方法)
		// ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,'aa' as
		// c6,'中国' as c7");//尚不支持该操作
		// ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,'中国' as
		// c6,'中国' as c7");//表示列 5 的 Unicode 数据大小的字节数为奇数。应为偶数。
		// ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,N'中国' as
		// c6,'中国' as c7");//表示列 5 的 Unicode 数据大小的字节数为奇数。应为偶数。

		dstExec.Delete("user_profile_attr_all", null);
		dstExec.HugeBulkReader(null, srcDr, "user_profile_attr_all", null, null, null);

	}

	/**
	 * 实测效率 Wed Mar 17 11:23:08 CST 2021 Wed Mar 17 11:27:33 CST 2021 行数:1000000
	 * 耗时:0时4分26秒
	 */
	public void testBulk2Mysql2() {
		try {

			ISGJdbc srcJdbc = JdbcHelperTest.GetYJQueryJdbc();
			ISGJdbc dstJdbc = JdbcHelperTest.GetUserProfileJdbc();

			Connection conn;
			PreparedStatement stmt;

//     String driver = "com.mysql.jdbc.Driver";
//     String url = "jdbc:mysql://localhost:3306/techstars";
//     String user = "root";
//     String password = "123456";
//     String sql = "insert into test  (id,name,address)  values (?,?,?)";

			String driver = dstJdbc.getDriverClassName();
			String url = dstJdbc.getUrl();
			String user = dstJdbc.getUsername();
			String password = dstJdbc.getPassword();
			String sql = "insert into user_profile_attr_all  (user_id,data_date,theme)  values (?,?,?)";

			long[] taskBeginTime = new long[] { SGDate.Now().ToCalendar().getTimeInMillis() };
			// String sql = "insert into test values (?,?,?),(?,?,?),(?,?,?)";
			// Class.forName(driver);
			// conn = DriverManager.getConnection(url, user, password);
			conn = SGSqlExecute.Init(dstJdbc).GetConn();
			stmt = (PreparedStatement) conn.prepareStatement(sql);
			// 关闭事务自动提交 ,这一行必须加上
			conn.setAutoCommit(false);
			System.out.println(new Date());
			/* —————————————————————————— */
			// ------- 1000,000条 ---------
			// Wed Jul 29 20:22:49 CST 2020
			// Wed Jul 29 20:25:06 CST 2020
			// 耗时;137秒 平均7299条/秒
			for (int i = 1; i <= 1000000; i++) {
				stmt.setString(1, i + "_t");
				stmt.setString(2, "2021-02-01 00:00:00");
				stmt.setString(3, "member");
				stmt.addBatch();
				if (i % 5000 == 0) {
					stmt.executeBatch();
					conn.commit();
				}
			}
			System.out.println(new Date());
			System.out.println(SGDataHelper.FormatString(" 耗时:{0} \r\n",
					SGDataHelper.GetTimeSpan(SGDate.Now().ToCalendar().getTimeInMillis() - taskBeginTime[0], null)));

			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} catch (Exception e) {

		}

	}

//这种方法未测试通过,好像要引用com.mysql.jdbc.PreparedStatement
//public void testBulk2Mysql3() {
//try {
//
//	IPFJdbc srcJdbc = GetYJQueryJdbc();
//	IPFJdbc dstJdbc = GetUserProfileJdbc();
//	
//	 Connection conn;
//     PreparedStatement stmt;
//     
////     String driver = "com.mysql.jdbc.Driver";
////     String url = "jdbc:mysql://localhost:3306/techstars";
////     String user = "root";
////     String password = "123456";
////     String sql = "insert into test  (id,name,address)  values (?,?,?)";
//     
//     String driver = dstJdbc.getDriverClassName();
//     String url = dstJdbc.getUrl();
//     String user = dstJdbc.getUsername();
//     String password = dstJdbc.getPassword();
//     //String sql = "insert into user_profile_attr_all  (user_id,data_date,theme)  values (?,?,?)";
//		String sql = "LOAD DATA LOCAL INFILE 'sql.csv' IGNORE INTO TABLE user_profile_attr_all (user_id,data_date,theme)";
//
//		long[] taskBeginTime=new long[] {PFDate.Now().ToCalendar().getTimeInMillis()};
//     //String sql = "insert into test values (?,?,?),(?,?,?),(?,?,?)";
//     Class.forName(driver);
//     conn = DriverManager.getConnection(url, user, password);
//     stmt = (PreparedStatement) conn.prepareStatement(sql);
//     // 关闭事务自动提交 ,这一行必须加上
//     //conn.setAutoCommit(false);
//     System.out.println(new Date());
//     /* —————————————————————————— */
//     // ------- 1000,000条 ---------
//		StringBuilder builder = new StringBuilder();
//		for (int i = 1; i <= 1000000; i++) {
//			builder.append(i + "_t");
//			builder.append("\t");
//			builder.append( "2021-02-01 00:00:00");
//			builder.append("\t");
//			builder.append( "member");
//			builder.append("\n");
//
////			for (int j = 0; j <= 10000; j++) {
////
////				builder.append(4);
////				builder.append("\t");
////				builder.append(4 + 1);
////				builder.append("\t");
////				builder.append(4 + 2);
////				builder.append("\t");
////				builder.append(4 + 3);
////				builder.append("\t");
////				builder.append(4 + 4);
////				builder.append("\t");
////				builder.append(4 + 5);
////				builder.append("\n");
////			}
//		}
//		byte[] bytes = builder.toString().getBytes();
//		InputStream is = new ByteArrayInputStream(bytes);
//
//		com.mysql.jdbc.PreparedStatement mysqlStatement = statement
//				.unwrap(com.mysql.jdbc.PreparedStatement.class);
//
//		mysqlStatement.setLocalInfileInputStream(is);
//		 mysqlStatement.executeUpdate();
//     System.out.println(new Date());
//     System.out.println(PFDataHelper.FormatString(
// 			" 耗时:{0} \r\n",
// 			PFDataHelper.GetTimeSpan(PFDate.Now().ToCalendar().getTimeInMillis() - taskBeginTime[0], null)
// 			));
//
//     try {
//         if (stmt != null) {
//             stmt.close();
//         }
//         if (conn != null) {
//             conn.close();
//         }
//     } catch (SQLException e) {
//         // TODO: handle exception
//         e.printStackTrace();
//     }
//}catch(Exception e) {
//	
//}
//	
//}

	public void testConvertSpeed() {
		ArrayList<Calendar> dl = new ArrayList<Calendar>();
		ArrayList<Long> longDl = new ArrayList<Long>();
		long dItem = SGDate.Now().ToCalendar().getTimeInMillis();
		int m = 2000000;
		for (int i = 0; i < m; i++) {
			dl.add(SGDate.Now().ToCalendar());
			longDl.add(dItem);
		}
		ArrayList<Object> r = new ArrayList<Object>();
//
		SGDate now = null;
		// Function<Object,Object> c=null;
		IPFSqlFieldTypeConverter c2 = null;

//		now =PFDate.Now(); 
//		for(Calendar d :dl) {			
//			if(c==null) {
//				c=PFDataHelper.GetObjectToPFTypeBySqlTypeConverter(d,-1,PFSqlFieldType.DateTime);
//			}
//			c.apply(d);
//		}
//		PrintSpeed(now,m);//耗时:129毫秒,已插入数:2000000,速度15503875条/秒	

//	   
//		
//
//		
//      	now=PFDate.Now(); 
//		for(Calendar d :dl) {
//			PFDataHelper.ConvertObjectToPFTypeBySqlType(d,-1,PFSqlFieldType.DateTime);
//		}
//		PrintSpeed(now,m);//耗时:671毫秒(0时0分0秒),已插入数:2000000,速度2980625条/秒

//      	now=PFDate.Now(); 
//  		//System.out.println(PFDate.Now().ToCalendar().getTimeInMillis());
//		for(Calendar d :dl) {
//			PFDataHelper.ConvertObjectToSqlTypeByPFType(d,null,java.sql.Types.TIMESTAMP);
//		}
//  		//System.out.println(PFDate.Now().ToCalendar().getTimeInMillis());
//		PrintSpeed(now,m);//耗时:197毫秒(0时0分0秒),已插入数:2000000,速度10152284条/秒

//		now =PFDate.Now(); 
//		for(Calendar d :dl) {			
//			if(c==null) {
//				c=PFDataHelper.GetObjectToSqlTypeByPFTypeConverter(d,null,java.sql.Types.TIMESTAMP);
//			}
//			c.apply(d);
//		}
//		PrintSpeed(now,m);//耗时:286毫秒(0时0分0秒),已插入数:2000000,速度6993006条/秒

		this.PrintObject("Calendar -> date");
		SGSpeedCounter speed = new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
		now = SGDate.Now();
		for (Calendar d : dl) {
			SGDataHelper.ConvertObjectToSqlTypeByPFType(d, SGSqlFieldTypeEnum.DateTime, java.sql.Types.DATE);
		}
		this.PrintObject(speed.getSpeed(m, com.sellgirl.sgJavaHelper.SGDate.Now()) + " NORMAL");

		speed = new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
		now = SGDate.Now();
		for (Calendar d : dl) {
			if (c2 == null) {
				c2 = SGDataHelper.GetObjectToSqlTypeByPFTypeConverter(d, null, java.sql.Types.DATE, "",null
                );
			}
			c2.convert(d);
		}
		this.PrintObject(speed.getSpeed(m, com.sellgirl.sgJavaHelper.SGDate.Now()) + " CONVERTER");

//		now =PFDate.Now();
//		c=null;
//		for(long d :longDl) {			
//			if(c==null) {
//				c=PFDataHelper.GetObjectToPFTypeBySqlTypeConverter(d,-1,PFSqlFieldType.DateTime);
//			}
//			c.apply(d);
//		}
//		PrintSpeed(now,m);//耗时:1158毫秒(0时0分1秒),已插入数:2000000,速度1727115条/秒
//
//		now =PFDate.Now();
//		c=null;
//		for(long d :longDl) {			
//			if(c==null) {
//				c=PFDataHelper.GetObjectToPFTypeBySqlTypeConverter2(d,-1,PFSqlFieldType.DateTime);
//			}
//			c.apply(d);
//		}
//		PrintSpeed(now,m);//耗时:565毫秒(0时0分0秒),已插入数:2000000,速度3539823条/秒

		// now=PFDate.Now();
		this.PrintObject("long -> date");
		speed = new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
		for (long d : longDl) {
			Object tmpV = SGDataHelper.ConvertObjectToSqlTypeByPFType(d, SGSqlFieldTypeEnum.Long, java.sql.Types.DATE);
		}
		// PrintSpeed(now,m);//耗时:1153毫秒(0时0分1秒),已插入数:2000000,速度1734605条/秒
		this.PrintObject(speed.getSpeed(m, com.sellgirl.sgJavaHelper.SGDate.Now()) + " NORMAL");

		speed = new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
		c2 = null;
		for (long d : longDl) {
			if (c2 == null) {
				c2 = SGDataHelper.GetObjectToSqlTypeByPFTypeConverter(d, null, java.sql.Types.DATE, "",null
                );
			}
			Object tmpV = c2.convert(d);
		}
		// PrintSpeed(now,m);//耗时:487毫秒(0时0分0秒),已插入数:2000000,速度4106776条/秒
		this.PrintObject(speed.getSpeed(m, com.sellgirl.sgJavaHelper.SGDate.Now()) + " CONVERTER");

		this.PrintObject("long -> NUMERIC");
		speed = new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
		// now=PFDate.Now();
		for (long d : longDl) {
			Object tmpV = SGDataHelper.ConvertObjectToSqlTypeByPFType(d, SGSqlFieldTypeEnum.Long,
					java.sql.Types.NUMERIC);
		}
		// PrintSpeed(now,m);//耗时:59毫秒(0时0分0秒),已插入数:2000000,速度33898305条/秒
		this.PrintObject(speed.getSpeed(m, com.sellgirl.sgJavaHelper.SGDate.Now()) + " NORMAL");

		// now =PFDate.Now();
		speed = new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
		c2 = null;
		for (long d : longDl) {
			if (c2 == null) {
				c2 = SGDataHelper.GetObjectToSqlTypeByPFTypeConverter(d, null, java.sql.Types.NUMERIC, "",null
                );
			}
			Object tmpV = c2.convert(d);
		}
		// PrintSpeed(now,m);//耗时:73毫秒(0时0分0秒),已插入数:2000000,速度27397260条/秒
		this.PrintObject(speed.getSpeed(m, com.sellgirl.sgJavaHelper.SGDate.Now()) + " CONVERTER");

	}

	public void testGetTableFields() throws Exception {

		ISGJdbc dstJdbc = JdbcHelperTest.GetTidbSaleJdbc();
		// ISqlExecute srcExec = PFSqlExecute.Init(dstJdbc);
		ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
		List<SGSqlFieldInfo> list = dstExec.GetTableFields("hyzl");

		System.out.println(JSON.toJSONString(list));

	}

	public void testTidb() throws Exception {

		ISGJdbc srcJdbc = JdbcHelperTest.GetBalanceJdbc();
		ISGJdbc dstJdbc = JdbcHelperTest.GetTidbSaleJdbc();
		ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);
		ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);

		String sql = "select top 500000 a.[hybh]\r\n" + "      ,a.[hyxm]\r\n" + "      ,a.[bjhybh]\r\n"
				+ "      ,a.[accmon]\r\n" + "      ,a.rhrq  as [acc_day]\r\n"
				+ "      -- ,lt_t_hyzl.fjoindate  as [acc_day]\r\n" + "      -- ,a.[qx]\r\n"
				+ "      ,CONVERT(bit,a.[qx]) as qx\r\n" + "      ,a.[qxmonth]\r\n"
				+ "      ,GETDATE()  as [qx_day]\r\n" + "      ,a.[hysfzh]\r\n" + "      ,GETDATE() as hy_birthday\r\n"
				+ "      ,0 as Age\r\n" + "      ,a.[pesfzh]\r\n" + "      ,a.[mobile]\r\n" + "      ,a.[pexm]\r\n"
				+ "      ,a.[agentno],\r\n" + "  c.hpos,c.qpos,\r\n" + "  '' as province\r\n" + "from hyzl a \r\n"
				+ "left join p1 c on c.hybh=a.hybh\r\n" + "-- where a.accmon='{cmonth}'";
		ResultSet dr = srcExec.GetHugeDataReader(sql);
		String tbName = "hyzl";
		dstExec.TruncateTable(tbName);
		String sql2 = "show variables LIKE 'tidb_batch_insert' ";
		SGDataTable variablesDt = dstExec.GetDataTable(sql2, null);
		boolean b = dstExec.HugeBulkReader(null, dr, tbName,
//                (a) -> {
//                	a.setProcessBatch(50000) ;
//                	//if(insertOptionAction!=null) {insertOptionAction.accept(a);}
//                },
				null, // transferItem == null ? null :
						// transferItem.BeforeInsertAction,
				(already) -> {
				}, null);

	}

	public void testBulk2Tidb() {
		try {

			// IPFJdbc srcJdbc = GetYJQueryJdbc();
			ISGJdbc dstJdbc = JdbcHelperTest.GetTidbSaleJdbc();
			ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
			String tbName = "hyzl";
			dstExec.TruncateTable(tbName);

			Connection conn;
			PreparedStatement stmt;

//     String driver = "com.mysql.jdbc.Driver";
//     String url = "jdbc:mysql://localhost:3306/techstars";
//     String user = "root";
//     String password = "123456";
//     String sql = "insert into test  (id,name,address)  values (?,?,?)";

			String driver = dstJdbc.getDriverClassName();
			String url = dstJdbc.getUrl();
			String user = dstJdbc.getUsername();
			String password = dstJdbc.getPassword();
			String sql = "insert into hyzl  (hybh)  values (?)";

			long[] taskBeginTime = new long[] { SGDate.Now().ToCalendar().getTimeInMillis() };
			// String sql = "insert into test values (?,?,?),(?,?,?),(?,?,?)";
			// Class.forName(driver);
			// conn = DriverManager.getConnection(url, user, password);
			conn = SGSqlExecute.Init(dstJdbc).GetConn();

//			String sql2="set tidb_batch_insert = 1;\r\n" + 
//					"set tidb_batch_delete = 1;\r\n" + 
//					"update mysql.tidb set variable_value='24h' where variable_name='tikv_gc_life_time';\r\n" + 
//					"update mysql.tidb set variable_value='30m' where variable_name='tikv_gc_life_time';";
			Statement stmt2 = conn.createStatement();
			// stmt2.execute(sql2);
			stmt2.execute("set tidb_batch_insert = 1");
			stmt2.execute("set tidb_batch_delete = 1");
			stmt2.execute("update mysql.tidb set variable_value='24h' where variable_name='tikv_gc_life_time'");
			PreparedStatement ps = conn.prepareStatement("show variables LIKE 'tidb_batch_insert' ");
			ResultSet rs = ps.executeQuery();
			rs.next();
			Object aa = rs.getInt(2);

			stmt = (PreparedStatement) conn.prepareStatement(sql);
			// 关闭事务自动提交 ,这一行必须加上
			conn.setAutoCommit(false);
			System.out.println(new Date());
			/* —————————————————————————— */
			// ------- 1000,000条 ---------
			// Wed Jul 29 20:22:49 CST 2020
			// Wed Jul 29 20:25:06 CST 2020
			// 耗时;137秒 平均7299条/秒
			for (int i = 1; i <= 1000000; i++) {
				stmt.setString(1, i + "_t");
//				stmt.setString(1, i + "_t");
//				stmt.setString(2, "2021-02-01 00:00:00");
//				stmt.setString(3, "member");
				stmt.addBatch();
				// if (i % 5000 == 0) {
				if (i % 500 == 0) {
					stmt.executeBatch();
					conn.commit();
				}
			}
			System.out.println(new Date());
			System.out.println(SGDataHelper.FormatString(" 耗时:{0} \r\n",
					SGDataHelper.GetTimeSpan(SGDate.Now().ToCalendar().getTimeInMillis() - taskBeginTime[0], null)));

			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void testTidbBulkInsert() throws Exception {

		long[] taskBeginTime = new long[] { SGDate.Now().ToCalendar().getTimeInMillis() };
		initPFHelper();
		// bulk到ClickHouse
		ISGJdbc srcJdbc = JdbcHelperTest.GetBalanceJdbc();
		ISGJdbc dstJdbc = JdbcHelperTest.GetTidbSaleJdbc();
		ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);
		ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
		String dstTableName = "hyzl";
		// 使用NString前
		ResultSet srcDr = srcExec.GetHugeDataReader(("select top 10 a.[hybh]\r\n" + "      ,a.[hyxm]\r\n"
				+ "      ,a.[bjhybh]\r\n" + "      ,a.[accmon]\r\n" + "      ,a.rhrq  as [acc_day]\r\n"
				+ "      -- ,lt_t_hyzl.fjoindate  as [acc_day]\r\n" + "      -- ,a.[qx]\r\n"
				+ "      ,CONVERT(bit,a.[qx]) as qx\r\n" + "      ,a.[qxmonth]\r\n"
				+ "      ,GETDATE()  as [qx_day]\r\n" + "      ,a.[hysfzh]\r\n" + "      ,GETDATE() as hy_birthday\r\n"
				+ "      ,0 as Age\r\n" + "      ,a.[pesfzh]\r\n" + "      ,a.[mobile]\r\n" + "      ,a.[pexm]\r\n"
				+ "      ,a.[agentno],\r\n" + "  c.hpos,c.qpos,\r\n" + "  '' as province\r\n" + "from hyzl a \r\n"
				+ "left join p1 c on c.hybh=a.hybh\r\n" + "-- where a.accmon='{cmonth}'\r\n"
				+ "-- where a.hybh='00022153'\r\n" + " where a.hybh='0000000' and a.agentno='930903' \r\n")
				.replace("{cmonth}", "2021.04"));
		// ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,cast((CASE 1
		// WHEN 1 THEN 200 WHEN 2 THEN 800 ELSE NULL END) as DECIMAL) as c8");//这样ok

		// dstExec.Delete("test1", null);
//		dstExec.HugeDelete(dstTableName, where -> {
////  		where.Add("data_date",PFDate.Now().ToCalendar());
//		// where.Add("data_date",PFDate.Now().GetDayStart().ToCalendar());
//		//where.Add("data_date", transfer.getPFCmonth().ToDateTime());
//	});
		dstExec.TruncateTable(dstTableName);
		// dstExec.SetInsertOption(a->a.setProcessBatch(10));
		dstExec.HugeInsertReader(null, srcDr, dstTableName, insert -> {
			if (insert.Get("hysfzh").Value != null) {
				Calendar hy_birthday = SGDataHelper.IDCardToBirthDay(insert.Get("hysfzh").Value);
				// insert["acc_day"].Value = PFDataHelper.CMonthToDate(insert["accmon"].Value);

				insert.Set("hy_birthday", hy_birthday);
				insert.Set("Age", SGDataHelper.GetAge(hy_birthday));
			}
			if (insert.Get("qxmonth").Value != null) {
				try {
					insert.Set("qx_day", SGDataHelper.CMonthToDate(insert.Get("qxmonth").Value.toString()));
				} catch (Exception e) {
//					System.out.println("qx_day error");
//					System.out.println(insert.Get("qxmonth").Value.toString());
					insert.Set("qx_day", null);
				}
			} else {
				insert.Set("qx_day", null);
			}
		}, null, null);// 这样可以，但50000一批时会卡住
		// dstExec.HugeBulkReader(null, srcDr,dstTableName, null, null, null);

		System.out.println(new Date());
		System.out.println(SGDataHelper.FormatString(" 耗时:{0} \r\n",
				SGDataHelper.GetTimeSpan(SGDate.Now().ToCalendar().getTimeInMillis() - taskBeginTime[0], null)));
	}

	private void PrintTime(long millionSeconds) {
		System.out.println(SGDataHelper.GetTimeSpan(millionSeconds, null).toString());
	}

	private static void PrintSpeed(SGDate begin, int qty) {
		long diff = SGDate.Now().ToCalendar().getTimeInMillis() - begin.ToCalendar().getTimeInMillis();
		// System.out.println(String.valueOf(diff));
		SGTimeSpan ts = SGDataHelper.GetTimeSpan(diff, null);
		System.out.println(SGDataHelper.FormatString("耗时:{0}毫秒({1}),已插入数:{2},速度{3}条/秒", diff, ts.toString(), qty,
				qty * 1000 / diff));
	}

	class T {
		public List<String> a = new ArrayList<String>();
		public B b = new B();
		public C c = new C();
		public D<String> d = new D<String>();
		int e;
//		List l ;
		Map<Integer, String> map = new HashMap<Integer, String>();
	}

	// class A {}
	class B extends ArrayList<String> {
	}

	class C extends B {
	}

	class D<TT> {

	}

	private static void printClassInfo(Type cls) {
		System.out.println(cls.toString());
		System.out.println(cls.getClass().toString());
//		System.out.println(cls.getClass().getSuperclass());	
//		System.out.println(cls.getClass().getGenericSuperclass());		

		if (cls instanceof ParameterizedType) {
			System.out.println("is ParameterizedType");
		} else {
			System.out.println("not ParameterizedType");
		}
		if (cls instanceof Class) {
			Class cls1 = (Class) cls;
			System.out.println("is class,isPrimitive:" + cls1.isPrimitive());

//			if(cls1.getDeclaredClasses().length>0) {
//				System.out.println(cls1.getAnnotatedSuperclass()()[0]);
//			}
			// System.out.println(cls1.getAnnotatedSuperclass());

			Class superCls = cls1.getSuperclass();
			System.out.println("super: " + superCls);
			if (superCls != null) {
				System.out.println(superCls.getClass());
				System.out.println(superCls.getClass().getClass());
			}
			Type gSuperClass = cls1.getGenericSuperclass();
			System.out.println("generic super: " + gSuperClass);
			if (gSuperClass != null) {
				System.out.println(gSuperClass.getClass());
				System.out.println(gSuperClass.getClass().getClass());
				if (gSuperClass instanceof Class) {
					System.out.println("gSuperClass is Class");
				} else {
					System.out.println("gSuperClass not Class");
				}
			}
//			System.out.println();
		} else {
			System.out.println("not class");
		}

//        ParameterizedType argType = (ParameterizedType) ((ParameterizedType) cls).getActualTypeArguments()[0];	
//		System.out.println(argType.getRawType());
//		System.out.println(argType.getOwnerType());
	}

	/**
	 * 测试Class和Type的区别
	 */
	public void testDifferenceOfClassAndType() {

		Map<String, Type> result = new LinkedHashMap<String, Type>();
		TypeReference tr = new TypeReference<java.util.List<String>>() {
		};
		result.put("TypeReference<java.util.List<String>>", tr.getType());
		tr = new TypeReference<B>() {
		};
		result.put("TypeReference<B>", tr.getType());

//		List<String> aa=new ArrayList<String>();
//		String bb="bb";
//		//PFTypeReference ptr=new PFTypeReference<List<String>>() {};
//		PFTypeReference ptr=new PFTypeReference<List<String>>() {};
//		PFTypeReference ptr2=new PFTypeReference<String>() {};
//		//Boolean b=ptr.getType().equals(aa.getClass());
//		Boolean b=ptr.IsTypeOf(aa);
//		assertTrue(ptr.IsTypeOf(aa));
//		assertFalse(ptr.IsTypeOf(bb));
//		assertFalse(ptr2.IsTypeOf(aa));
//		assertTrue(ptr2.IsTypeOf(bb));
//		String aaa="aa";

		Class tc = T.class;
		T to = new T();
		Field[] fields = tc.getDeclaredFields();
		for (Field f : fields) {
			Class fc = f.getType();
			result.put("field-- " + f.getName(), fc);
			Type fgc = f.getGenericType();
			result.put("field GenericType-- " + f.getName(), fgc);
//			System.out.println("field-- "+f.getName());
//			printClassInfo(fc);
////			System.out.println(fc.getClass().toString());
////			System.out.println(fc.getClass().getGenericSuperclass().toString());			
////			System.out.println(fc.toString());
			try {
				Object v = f.get(to);
				result.put("field value-- " + f.getName(), v.getClass());
//				printClassInfo(v.getClass());
////				System.out.println(v.getClass().getClass().toString());
////				System.out.println(v.getClass().toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

//			if(fc.isPrimitive()){
//				System.out.println("基本数据类型： " + f.getName() + "  " + fc.getName());
//			}else{
//				if(fc.isAssignableFrom(List.class)){ //判断是否为List
//					System.out.println("List类型：" + f.getName());
//					Type gt = f.getGenericType();	//得到泛型类型
//					ParameterizedType pt = (ParameterizedType)gt;
//					Class lll = (Class)pt.getActualTypeArguments()[0];
//					System.out.println("\t\t" + lll.getName());
//				}
//			}
		}

		Iterator<Entry<String, Type>> iter = result.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Type> key = iter.next();
			System.out.println(key.getKey());
			printClassInfo(key.getValue());
			System.out.println("---------------------");
		}

//		   //事实证明ArrayList<T>.getClass().newInstance()得到的类型可以转为任意的T类型
//			ArrayList<Integer> aaa=null;
//			try {
//				aaa=(ArrayList<Integer>) to.a.getClass().newInstance();
//				aaa.add(111);
//			} catch (InstantiationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			String bbb="aa";
	}

	public void testJsonSerial() {
		String aa = JSON.toJSONString(new TestSerialModel());
		assertTrue(aa.indexOf("bb") > 0);
		aa = JSON.toJSONString(new TestSerialModel2());
		assertTrue(aa.indexOf("bb") > 0);
		assertTrue(aa.indexOf("cc") < 0);
		assertTrue(aa.indexOf("dd") > 0);
		assertTrue(aa.indexOf("ee") < 0);
		assertTrue(aa.indexOf("ff") > 0);
		assertTrue(aa.indexOf("gg") < 0);
		assertTrue(aa.indexOf("hh") > 0);
		assertTrue(aa.indexOf("ii") < 0);
		// assertTrue(aa.indexOf("jj")<0);
		// com.fasterxml.jackson.annotation.JsonValue
	}

	public void testJsonDeserial() {
		TestSerialModel3<TestSerialModel2> o1 = new TestSerialModel3<TestSerialModel2>();
		o1.kk = new TestSerialModel2();
		String aa = JSON.toJSONString(o1);
		this.PrintObject(aa);

		TestSerialModel3<TestSerialModel2> o2 = JSON.parseObject(aa,
				new TypeReference<TestSerialModel3<TestSerialModel2>>() {
				});

		this.PrintObject(o2);
		// assertTrue(aa.indexOf("bb") > 0);
//		aa = JSON.toJSONString(new TestSerialModel2());
//		assertTrue(aa.indexOf("bb") > 0);
//		assertTrue(aa.indexOf("cc") < 0);
//		assertTrue(aa.indexOf("dd") > 0);
//		assertTrue(aa.indexOf("ee") < 0);
//		assertTrue(aa.indexOf("ff") > 0);
//		assertTrue(aa.indexOf("gg") < 0);
//		assertTrue(aa.indexOf("hh") > 0);
//		assertTrue(aa.indexOf("ii") < 0);
//		// assertTrue(aa.indexOf("jj")<0);
//		// com.fasterxml.jackson.annotation.JsonValue
	}

	public void testJsonDeserial2() {
		TestSerialModel3<TestSerialModel2> o1 = new TestSerialModel3<TestSerialModel2>();
		o1.kk = new TestSerialModel2();

		TestSerialModel4<TestSerialModel3<TestSerialModel2>> o2 = new TestSerialModel4<TestSerialModel3<TestSerialModel2>>();
		o2.ll = o1;
		String aa = JSON.toJSONString(o2);
		this.PrintObject(aa);

		TestSerialModel4<TestSerialModel3<TestSerialModel2>> o3 = JSON.parseObject(aa,
				new TypeReference<TestSerialModel4<TestSerialModel3<TestSerialModel2>>>() {
				});

		this.PrintObject(o3);
		// assertTrue(aa.indexOf("bb") > 0);
//		aa = JSON.toJSONString(new TestSerialModel2());
//		assertTrue(aa.indexOf("bb") > 0);
//		assertTrue(aa.indexOf("cc") < 0);
//		assertTrue(aa.indexOf("dd") > 0);
//		assertTrue(aa.indexOf("ee") < 0);
//		assertTrue(aa.indexOf("ff") > 0);
//		assertTrue(aa.indexOf("gg") < 0);
//		assertTrue(aa.indexOf("hh") > 0);
//		assertTrue(aa.indexOf("ii") < 0);
//		// assertTrue(aa.indexOf("jj")<0);
//		// com.fasterxml.jackson.annotation.JsonValue
	}

	public void testJsonDeserial3() {
//		TestSerialModel3<TestSerialModel2> o1=new TestSerialModel3<TestSerialModel2>();
//		o1.kk=new TestSerialModel2();
//		
//		TestSerialModel4<TestSerialModel3<TestSerialModel2>> o2=new TestSerialModel4<TestSerialModel3<TestSerialModel2>>();
//		o2.ll=o1;
//		String aa = JSON.toJSONString(o2);
//		this.PrintObject(aa);

		String aa = "{\"aa\":\"aa\",\"bb\":\"bb\",\"dd\":\"dd\",\"ff\":\"ff\",\"hh\":\"hh\",\"ll\":{\"aa\":\"aa\",\"bb\":\"bb\",\"dd\":\"dd\",\"ff\":\"ff\",\"hh\":\"hh\",\"kk\":{\"aa\":\"aa\",\"bb\":\"bb\",\"dd\":\"dd\",\"ff\":\"ff\",\"hh\":\"hh\"}}}";
		TestSerialModel4<TestSerialModel3<TestSerialModel2>> o3 = JSON.parseObject(aa,
				new TypeReference<TestSerialModel4<TestSerialModel3<TestSerialModel2>>>() {
				});

		this.PrintObject(o3);
		// assertTrue(aa.indexOf("bb") > 0);
//		aa = JSON.toJSONString(new TestSerialModel2());
//		assertTrue(aa.indexOf("bb") > 0);
//		assertTrue(aa.indexOf("cc") < 0);
//		assertTrue(aa.indexOf("dd") > 0);
//		assertTrue(aa.indexOf("ee") < 0);
//		assertTrue(aa.indexOf("ff") > 0);
//		assertTrue(aa.indexOf("gg") < 0);
//		assertTrue(aa.indexOf("hh") > 0);
//		assertTrue(aa.indexOf("ii") < 0);
//		// assertTrue(aa.indexOf("jj")<0);
//		// com.fasterxml.jackson.annotation.JsonValue
	}

	public void testJsonDeserial4() {
		TestSerialModel3<TestSerialModel2> o1 = new TestSerialModel3<TestSerialModel2>();
		o1.kk = new TestSerialModel2();

		TestSerialModel5<TestSerialModel3<TestSerialModel2>> o2 = new TestSerialModel5<TestSerialModel3<TestSerialModel2>>();
		// o2.ll=o1;
		o2.ll = new ArrayList<TestSerialModel3<TestSerialModel2>>();
		o2.ll.add(o1);
		String aa = JSON.toJSONString(o2);
		this.PrintObject(aa);

		TestSerialModel5<TestSerialModel3<TestSerialModel2>> o3 = JSON.parseObject(aa,
				new TypeReference<TestSerialModel5<TestSerialModel3<TestSerialModel2>>>() {
				});

		this.PrintObject(o3);

	}

	public void testJsonDeserial5() {
		String aa = "{\"DataSource\":[{\"DatamodelQueryId\":\"90\",\"ReqAvgUseMs\":\"2439\",\"Id\":\"96bbda7c-8dfe-461f-9ea0-f78269334682\",\"DatamodelQueryName\":\"促销活动\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IGEuaWQsYS5vcmRlcl9zdGF0dXMsYS5vcmRlcl9tb250aCxiLm9yZGVyX2lkLFxyXG5jYXNlIFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0ZTJyB0aGVuICcxLeaWsOWTgeS4iuW4gu+8mkZT5LiA55Sf57OWJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0ZTNicgdGhlbiAnMi3mlrDlk4HkuIrluILvvJpGUzbkuIDnlJ/ns5YnXHJcbiAgd2hlbiBiLnNlcmlhbF9ubz0nRlMzMCcgdGhlbiAnMy3mlrDlk4HkuIrluILvvJpGUzMw5rS756uL5aSaJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0FDQzIwMjInIHRoZW4gJzctQUND5LyY5oOg5aWX6KOFJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J1NQQ1MwMicgdGhlbiAnNi3lpI/lraPkuKrmiqTlpZfoo4Ut55S35aOrJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J1NQQ1MwMScgdGhlbiAnNC3lpI/lraPkuKrmiqTlpZfoo4Ut5aWz5aOrJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0VUMjAyMicgdGhlbiAnNS3niLHmlr3ok5PlqbTnq6XmtJfmiqTlpZfoo4UnXHJcbiAgZW5kIGFzICfmtLvliqjlkI3np7AnLFxyXG5zdW0oYi5xdWFudGl0eSkgYXMgJ+mUgOWUrumHjycsc3VtKGIudG90YWxfcHJpY2UpIGFzICfplIDllK7pop0nLFxyXG5jYXNlIFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0FDQzIwMjInIHRoZW4gc3VtKGIucXVhbnRpdHkpLzI1MDAwXHJcbiAgd2hlbiBiLnNlcmlhbF9ubz0nU1BDUzAyJyB0aGVuIHN1bShiLnF1YW50aXR5KS8yMDg1MFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J1NQQ1MwMScgdGhlbiBzdW0oYi5xdWFudGl0eSkvMTM1ODBcclxuICB3aGVuIGIuc2VyaWFsX25vPSdFVDIwMjInIHRoZW4gc3VtKGIucXVhbnRpdHkpLzQ1MDBcclxuICBlbmQgYXMgJ+ebruagh+WujOaIkOeOhycgXHJcbmZyb21cclxuKHNlbGVjdCBpZCxvcmRlcl9zdGF0dXMsb3JkZXJfbW9udGggZnJvbSBvcmRlcl9vcmRlcikgYVxyXG5qb2luXHJcbihzZWxlY3Qgb3JkZXJfaWQsc2VyaWFsX25vLHF1YW50aXR5LHRvdGFsX3ByaWNlIGZyb20gb3JkZXJfcHJvZHVjdCkgYlxyXG5vbiAoYS5pZD1iLm9yZGVyX2lkKVxyXG53aGVyZSBhLm9yZGVyX3N0YXR1cyBpbiAoMiwzLDk5KSBhbmQgYS5vcmRlcl9tb250aCA+PScyMDIyMDQnIGFuZCBiLnNlcmlhbF9ubyBpbiAoJ0FDQzIwMjInLCdTUENTMDInLCdTUENTMDEnLCdFVDIwMjInLCdGUycsJ0ZTNicsJ0ZTMzAnKVxyXG5ncm91cCBieSBiLnNlcmlhbF9ub1xyXG5vcmRlciBieSDmtLvliqjlkI3np7AiLCJ0ZW1wbGF0ZS10YWdzIjp7fX0sImRhdGFiYXNlIjoxM30=\",\"QueryType\":\"native\",\"Description\":\"促销活动\",\"Time\":\"2022-05-07 08:49:51\",\"UpdateTime\":\"2022-05-28 15:55:27\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"101\",\"ReqAvgUseMs\":\"169\",\"Id\":\"413e56b9-18fa-4e85-9b16-5a8cee9c3387\",\"DatamodelQueryName\":\"本月办卡人数及升级方式\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IFxyXG4gIGNhc2UgXHJcbiAgICB3aGVuIHByb21vdGlvbl90eXBlPTEgdGhlbiAnMjk556S85YyFJ1xyXG4gICAgd2hlbiBwcm9tb3Rpb25fdHlwZT0yIHRoZW4gJzYwMHB2J1xyXG4gICAgd2hlbiBwcm9tb3Rpb25fdHlwZSBpcyBudWxsIHRoZW4gJ+acquWNh+e6pydcclxuICAgIGVuZCBhcyAn5Yqe5Y2h5Lq65pWw5Y+K5Y2H57qn5pa55byPJyxjb3VudCgqKSBhcyAn5Lq65pWwJ1xyXG5mcm9tIG1lbWJlcl9pbmZvXHJcbndoZXJlIG9wZW5fY2FyZF90aW1lPj0nMjAyMi4wNS4wMScgYW5kIG9wZW5fY2FyZF90aW1lPCcyMDIyLjA2LjAxJ1xyXG5ncm91cCBieSDlip7ljaHkurrmlbDlj4rljYfnuqfmlrnlvI8iLCJ0ZW1wbGF0ZS10YWdzIjp7fX0sImRhdGFiYXNlIjoxNH0=\",\"QueryType\":\"native\",\"Description\":\"本月办卡人数及升级方式\",\"Time\":\"2022-05-07 14:04:56\",\"UpdateTime\":\"2022-05-26 10:53:34\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"14a3b845-f66e-2b5f-73a8-9332a98faa97\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"42\",\"ReqAvgUseMs\":\"6\",\"Id\":\"4bb1903d-8d59-4771-a7a6-69e84fe62832\",\"DatamodelQueryName\":\"散点图\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MiwicXVlcnkiOnsic291cmNlLXRhYmxlIjoxLCJvcmRlci1ieSI6W1siYXNjIixbImZpZWxkLWlkIiwzOTBdXV0sImZpZWxkcyI6W1siZmllbGQtaWQiLDM3N10sWyJmaWVsZC1pZCIsMzc4XSxbImZpZWxkLWlkIiwzNzldLFsiZmllbGQtaWQiLDM4M10sWyJmaWVsZC1pZCIsMzg0XSxbImZpZWxkLWlkIiwzODVdLFsiZmllbGQtaWQiLDM4Nl0sWyJmaWVsZC1pZCIsMzg3XSxbImZpZWxkLWlkIiwzODhdLFsiZmllbGQtaWQiLDM4OV0sWyJmaWVsZC1pZCIsMzkwXSxbImZpZWxkLWlkIiwzOTFdXX0sInR5cGUiOiJxdWVyeSIsInZlcnNpb24iOiJwZXJmZWN0In0=\",\"QueryType\":\"\",\"Description\":\"散点图散点图散点图散点图\",\"Time\":\"2022-01-14 08:53:31\",\"UpdateTime\":\"2022-05-25 16:50:20\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"8edc4c89-88d1-a59d-8cb5-787489993b08\",\"GroupId\":\"8a1469a6-bdb6-4b43-b90c-816184cdadec\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"23\",\"ReqAvgUseMs\":\"3\",\"Id\":\"9b92671a-2bb7-4f22-a21f-2c69e2ba3922\",\"DatamodelQueryName\":\"折线图\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MiwicXVlcnkiOnsic291cmNlLXRhYmxlIjoxLCJicmVha291dCI6W1siZmllbGQtaWQiLDM3OF1dLCJhZ2dyZWdhdGlvbiI6W1siY291bnQiXSxbInN1bSIsWyJmaWVsZC1pZCIsMzg0XV0sWyJzdW0iLFsiZmllbGQtaWQiLDM4NV1dXX0sInR5cGUiOiJxdWVyeSJ9\",\"QueryType\":\"\",\"Description\":\"dataCenterLinedataCenterLinedataCenterLinedataCenterLine\",\"Time\":\"2021-12-22 11:24:27\",\"UpdateTime\":\"2022-05-25 14:51:28\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"8edc4c89-88d1-a59d-8cb5-787489993b08\",\"GroupId\":\"8a1469a6-bdb6-4b43-b90c-816184cdadec\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"155\",\"ReqAvgUseMs\":\"117151\",\"Id\":\"5506e64b-8211-4ee6-ba93-774262094487\",\"DatamodelQueryName\":\"内蒙古各月业绩\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IFxyXG5vcmRlcl9tb250aCxzdW0ob3JkZXJfYW1vdW50KVxyXG5mcm9tIG9yZGVyX29yZGVyIFxyXG53aGVyZSBvcmRlcl9tb250aD49JzIwMjEwMScgYW5kIG9yZGVyX3N0YXR1cyBpbigyLDMsOTkpIGFuZCBjb21wYW55X2NvZGU9JzEzMDAwJ1xyXG5ncm91cCBieSBvcmRlcl9tb250aFxyXG5vcmRlciBieSBvcmRlcl9tb250aCIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"内蒙古\",\"Time\":\"2022-05-24 14:20:45\",\"UpdateTime\":\"2022-05-24 14:20:45\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"a81638b9-1f19-44dc-a9d6-86ba474a3fb3\",\"Cache\":\"True\",\"CacheInterval\":\"30\"},{\"DatamodelQueryId\":\"91\",\"ReqAvgUseMs\":\"372\",\"Id\":\"2632d148-d54f-4996-bcf4-2fe535a29fd0\",\"DatamodelQueryName\":\"今日消费人数\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTMsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NjYzLCJqb2lucyI6W10sImZpbHRlciI6W1sidGltZS1pbnRlcnZhbCIsWyJmaWVsZC1pZCIsMTEwNDBdLCJjdXJyZW50IiwiZGF5Il0sImFuZCIsWyI9IixbImZpZWxkLWlkIiwxMTA3Ml0sIjIwMjIwNSJdLCJhbmQiLFsiYnJhY2tldCIsW1siPSIsWyJmaWVsZC1pZCIsMTEwNzhdLCIyIl0sIm9yIixbIj0iLFsiZmllbGQtaWQiLDExMDc4XSwiMyJdLCJvciIsWyI9IixbImZpZWxkLWlkIiwxMTA3OF0sIjk5Il1dXV0sImFnZ3JlZ2F0aW9uIjpbWyJkaXN0aW5jdCIsWyJmaWVsZC1pZCIsMTEwNjBdXV19LCJ0eXBlIjoicXVlcnkiLCJ2ZXJzaW9uIjoicGVyZmVjdCJ9\",\"QueryType\":\"query\",\"Description\":\"今日消费人数\",\"Time\":\"2022-05-07 08:58:56\",\"UpdateTime\":\"2022-05-23 10:57:58\",\"UserId\":\"03fe16bd-0e3a-4655-a057-ace07e772ee1\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"126\",\"ReqAvgUseMs\":\"9388\",\"Id\":\"ceeeb8bb-6b08-455d-9028-51f07a572476\",\"DatamodelQueryName\":\"liang-test002\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5IjoiU0VMRUNUIGBtb250aGx5X3N0YXRpc3RpY3NgLmBvcmRlcl9tb250aGAgQVMgYG9yZGVyX21vbnRoYCxgbW9udGhseV9zdGF0aXN0aWNzYC5gYnV5ZXJfcXR5YCBBUyBgYnV5ZXJfcXR5YFxyXG4gRlJPTSBgbWFsbF9zYWxlYC5gbW9udGhseV9zdGF0aXN0aWNzYFxyXG4gd2hlcmUgb3JkZXJfbW9udGg8PScyMDIyMDQnXHJcbiB1bmlvbiBcclxuIHNlbGVjdCBvcmRlcl9tb250aCxjb3VudChkaXN0aW5jdCBjdXN0b21lcl9jYXJkKSBhcyAnYnV5ZXJfcXR5JyBmcm9tIG9yZGVyX29yZGVyXHJcbndoZXJlIG9yZGVyX21vbnRoPj0nMjAyMjA1JyBhbmQgb3JkZXJfc3RhdHVzIGluICgyLDMsOTkpXHJcbmdyb3VwIGJ5IG9yZGVyX21vbnRoXHJcbm9yZGVyIGJ5IG9yZGVyX21vbnRoXHJcbiIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"柱状图便签修改测试\",\"Time\":\"2022-05-12 16:21:49\",\"UpdateTime\":\"2022-05-20 17:16:40\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"c5df6129-0d20-4b58-8185-f731a8348396\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"99\",\"ReqAvgUseMs\":\"7971\",\"Id\":\"2c833dfd-5dc6-494c-8126-bcf6e24e93e3\",\"DatamodelQueryName\":\"本月消费人数\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTMsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NjYzLCJqb2lucyI6W10sImZpbHRlciI6W1siPSIsWyJmaWVsZC1pZCIsMTEwNzJdLCIyMDIyMDUiXSwiYW5kIixbImJyYWNrZXQiLFtbIj0iLFsiZmllbGQtaWQiLDExMDc4XSwiMiJdLCJvciIsWyI9IixbImZpZWxkLWlkIiwxMTA3OF0sIjMiXSwib3IiLFsiPSIsWyJmaWVsZC1pZCIsMTEwNzhdLCI5OSJdXV1dLCJhZ2dyZWdhdGlvbiI6W1siZGlzdGluY3QiLFsiZmllbGQtaWQiLDExMDYwXV1dfSwidHlwZSI6InF1ZXJ5IiwidmVyc2lvbiI6InBlcmZlY3QifQ==\",\"QueryType\":\"query\",\"Description\":\"本月消费人数\",\"Time\":\"2022-05-07 13:56:30\",\"UpdateTime\":\"2022-05-20 14:28:21\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"f961d11c-4d35-4838-a562-1bbc93269083\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"143\",\"ReqAvgUseMs\":\"0\",\"Id\":\"bb1ff512-584d-4090-a0e9-b40168c4d65c\",\"DatamodelQueryName\":\"testCustomColumnFromAggregation_02\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTUsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NzAwLCJicmVha291dCI6W1siZmllbGQtaWQiLDExNjgzXV0sImFnZ3JlZ2F0aW9uIjpbWyJzdW0iLFsiZmllbGQtaWQiLDExNjg0XV1dLCJleHByZXNzaW9ucyI6e30sImpvaW5zIjpbXSwiZmlsdGVyIjpbXX0sInR5cGUiOiJxdWVyeSIsInZlcnNpb24iOiJwZXJmZWN0In0=\",\"QueryType\":\"query\",\"Description\":\"testCustomColumnFromAggregation_02\",\"Time\":\"2022-05-17 15:44:38\",\"UpdateTime\":\"2022-05-19 10:52:05\",\"UserId\":\"33d8d854-6a07-47b3-8326-c9403db7a430\",\"Enable\":\"True\",\"DatabaseId\":\"9b4187ea-99be-ca76-ffdb-304b0a7f2a8b\",\"GroupId\":\"835f8160-6e09-41d9-89dc-b41662991631\",\"Cache\":\"True\",\"CacheInterval\":\"20\"},{\"DatamodelQueryId\":\"148\",\"ReqAvgUseMs\":\"287\",\"Id\":\"5b6d2773-12bf-49de-889b-6ebf00db053d\",\"DatamodelQueryName\":\"办卡数据\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IFxyXG4gIGNhc2UgXHJcbiAgICB3aGVuIHByb21vdGlvbl90eXBlPTEgdGhlbiAnMjk556S85YyFJ1xyXG4gICAgd2hlbiBwcm9tb3Rpb25fdHlwZT0yIHRoZW4gJzYwMHB2J1xyXG4gICAgd2hlbiBwcm9tb3Rpb25fdHlwZSBpcyBudWxsIHRoZW4gJ+acquWNh+e6pydcclxuICAgIGVuZCBhcyAn5Yqe5Y2h5Lq65pWw5Y+K5Y2H57qn5pa55byPJyxjb3VudCgqKSBhcyAn5Lq65pWwJ1xyXG5mcm9tIG1lbWJlcl9pbmZvXHJcbndoZXJlIG9wZW5fY2FyZF90aW1lPj0nMjAyMi4wNS4wMScgYW5kIG9wZW5fY2FyZF90aW1lPCcyMDIyLjA2LjAxJ1xyXG5ncm91cCBieSDlip7ljaHkurrmlbDlj4rljYfnuqfmlrnlvI8iLCJ0ZW1wbGF0ZS10YWdzIjp7fX0sImRhdGFiYXNlIjoxNH0=\",\"QueryType\":\"native\",\"Description\":\"办卡人数及升级人数\",\"Time\":\"2022-05-18 10:38:01\",\"UpdateTime\":\"2022-05-18 10:40:58\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"14a3b845-f66e-2b5f-73a8-9332a98faa97\",\"GroupId\":\"00000000-0000-0000-0000-000000000000\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"144\",\"ReqAvgUseMs\":\"46424\",\"Id\":\"4c6702d3-88af-47d5-97a2-74d1f4a79cb8\",\"DatamodelQueryName\":\"月销售额统计\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IG9yZGVyX21vbnRoLHN1bShvcmRlcl9hbW91bnQpXHJcbmZyb20gb3JkZXJfb3JkZXJcclxud2hlcmUgb3JkZXJfbW9udGg+PScyMDIyMDEnIGFuZCBvcmRlcl9zdGF0dXMgaW4oMiwzLDk5KVxyXG5ncm91cCBieSBvcmRlcl9tb250aCIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"统计月销售额，未扣除换购\",\"Time\":\"2022-05-17 16:18:39\",\"UpdateTime\":\"2022-05-17 16:34:45\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"00000000-0000-0000-0000-000000000000\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"128\",\"ReqAvgUseMs\":\"11379\",\"Id\":\"0084aaa1-ace7-4367-84fe-99455c2d71b8\",\"DatamodelQueryName\":\"本月新增客户数\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5IjoiU0VMRUNUIGNvdW50KCopIEFTIGBjb3VudGBcclxuRlJPTSBgaHl6bGBcclxuV0hFUkUgYGFjY21vbmA9ZGF0ZV9mb3JtYXQobm93KCksICclWS4lbScpIiwidGVtcGxhdGUtdGFncyI6e319LCJkYXRhYmFzZSI6MTJ9\",\"QueryType\":\"native\",\"Description\":\"本月新增客户数\",\"Time\":\"2022-05-13 11:08:30\",\"UpdateTime\":\"2022-05-13 11:09:52\",\"UserId\":\"3c879e85-5b86-4dba-b5e2-c1facfeabaa5\",\"Enable\":\"True\",\"DatabaseId\":\"b1605471-1082-1f82-85a8-a9c505e8bfc1\",\"GroupId\":\"880a5dda-918e-425f-8bc8-e0b89a77f11a\",\"Cache\":\"True\",\"CacheInterval\":\"20\"},{\"DatamodelQueryId\":\"104\",\"ReqAvgUseMs\":\"25812\",\"Id\":\"e3570a03-58fc-4373-86a1-4db33f823f7e\",\"DatamodelQueryName\":\"4月单品TOP20（单位：万元）\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IGEuaWQsYS5vcmRlcl9zdGF0dXMsYS5vcmRlcl9tb250aCxiLm9yZGVyX2lkLGIuc2VyaWFsX25vLHN1bShiLnRvdGFsX3ByaWNlKSxcclxuICAgIENBU0VcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdGQScgVEhFTiAn5rKZ5qOY6Iy2J1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0ZBMTAnIFRIRU4gJ+aymeajmOiMtidcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdGQTMwJyBUSEVOICfmspnmo5jojLYnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nRkExMDAxJyBUSEVOICfmspnmo5jojLYnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nQUcnIFRIRU4gJ+iKpuiNn+iDtidcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdBRzInIFRIRU4gJ+iKpuiNn+iDtidcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdBRzEwJyBUSEVOICfoiqbojZ/og7YnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nQUcxMDAxJyBUSEVOICfoiqbojZ/og7YnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nQU1QTicgVEhFTiAn55+/54mp57KJJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0FNUE4xMCcgVEhFTiAn55+/54mp57KJJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0FNUE4zMCcgVEhFTiAn55+/54mp57KJJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0FNUE4xMDAxJyBUSEVOICfnn7/niannsoknXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nUFNOJyBUSEVOICfogr3ol7vokKXlhbvnsoknXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nUFNONicgVEhFTiAn6IK96Je76JCl5YW757KJJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J1BTTjMwJyBUSEVOICfogr3ol7vokKXlhbvnsoknXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nUFNONjAxJyBUSEVOICfogr3ol7vokKXlhbvnsoknXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nQ1BUMDInIFRIRU4gJ+ivreazsOeJhydcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdDUFQwMycgVEhFTiAn6K+t5rOw54mHJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0NQVDgnIFRIRU4gJ+ivreazsOeJhydcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdOQicgVEhFTiAn6JCl5YW76aSQJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J05CMzAnIFRIRU4gJ+iQpeWFu+mkkCdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdPTFMnIFRIRU4gJ+S4gOeUn+ezlidcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdPTFM2JyBUSEVOICfkuIDnlJ/ns5YnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nUFNHJyBUSEVOICfmspnokpwnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nUFNHNCcgVEhFTiAn5rKZ6JKcJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J1BTRzQwMScgVEhFTiAn5rKZ6JKcJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J01BNTAyMScgVEhFTiAn5ruL5ram54i96IKk5rC0J1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J01BNTAyMTMnIFRIRU4gJ+a7i+a2pueIveiCpOawtCdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdQU0QnIFRIRU4gJ+S6v+eUn+iPjCdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdDRFAnIFRIRU4gJ+aymeajmOaxgSdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdNQTUwMTEnIFRIRU4gJ+aflOWSjOa0geiCpOS5sydcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdNQTcwMzknIFRIRU4gJ+WuieeTtueyvuWNjua2sidcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdBTTAwNzMwJyBUSEVOICfoiqbojZ/pnaLohpwnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nQU0wMDcnIFRIRU4gJ+iKpuiNn+mdouiGnCdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdFSFAnIFRIRU4gJ+iGs+mjn+e6pOe7tOeyiSdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdFSFAzMCcgVEhFTiAn5aSN5ZCI6Iaz6aOf57qk57u057KJJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0dQTScgVEhFTiAn5YGl5omsJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0ZEMzAnIFRIRU4gJ+mrmOe6pOS5kCdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdPTEknIFRIRU4gJ+a0u+eri+WkmidcclxuICAgICAgICBlbHNlIGIuc2VyaWFsX25vXHJcbiAgICBFTkQgQVMgYmNjXHJcbmZyb21cclxuKHNlbGVjdCBpZCxvcmRlcl9zdGF0dXMsb3JkZXJfbW9udGggZnJvbSBvcmRlcl9vcmRlcikgYVxyXG5qb2luXHJcbihzZWxlY3Qgb3JkZXJfaWQsc2VyaWFsX25vLHRvdGFsX3ByaWNlIGZyb20gb3JkZXJfcHJvZHVjdCkgYlxyXG5vbiAoYS5pZD1iLm9yZGVyX2lkKVxyXG53aGVyZSBhLm9yZGVyX3N0YXR1cyBpbiAoMiwzLDk5KSBhbmQgYS5vcmRlcl9tb250aCA9JzIwMjIwNSdcclxuZ3JvdXAgYnkgYmNjXHJcbm9yZGVyIGJ5IHN1bShiLnRvdGFsX3ByaWNlKSBkZXNjXHJcbmxpbWl0IDIwIiwidGVtcGxhdGUtdGFncyI6e319LCJkYXRhYmFzZSI6MTN9\",\"QueryType\":\"native\",\"Description\":\"4月单品TOP20\",\"Time\":\"2022-05-07 14:23:50\",\"UpdateTime\":\"2022-05-13 09:27:44\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"88\",\"ReqAvgUseMs\":\"8009\",\"Id\":\"806940f4-9f27-4e0b-b278-1f146728406c\",\"DatamodelQueryName\":\"本月销售额\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTMsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NjYzLCJqb2lucyI6W10sImZpbHRlciI6W1siPSIsWyJmaWVsZC1pZCIsMTEwNzJdLCIyMDIyMDUiXSwiYW5kIixbImJyYWNrZXQiLFtbIj0iLFsiZmllbGQtaWQiLDExMDc4XSwiMiJdLCJvciIsWyI9IixbImZpZWxkLWlkIiwxMTA3OF0sIjMiXSwib3IiLFsiPSIsWyJmaWVsZC1pZCIsMTEwNzhdLCI5OSJdXV1dLCJhZ2dyZWdhdGlvbiI6W1sic3VtIixbImZpZWxkLWlkIiwxMTA0MV1dXX0sInR5cGUiOiJxdWVyeSIsInZlcnNpb24iOiJwZXJmZWN0In0=\",\"QueryType\":\"query\",\"Description\":\"本月销售额\",\"Time\":\"2022-05-06 15:59:11\",\"UpdateTime\":\"2022-05-11 14:47:36\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"93\",\"ReqAvgUseMs\":\"31719\",\"Id\":\"1bc1df9f-4d5f-460f-91ec-286d40d0eb48\",\"DatamodelQueryName\":\"本月省份业绩排名\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5IjoiU0VMRUNUICBcclxuICAgIENBU0VcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45bm/5Lic5YiG5YWs5Y+4JyBUSEVOICflub/kuJwnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOa1meaxn+WIhuWFrOWPuCcgVEhFTiAn5rWZ5rGfJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jmsZ/oi4/liIblhazlj7gnIFRIRU4gJ+axn+iLjydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45rKz5Y2X5YiG5YWs5Y+4JyBUSEVOICfmsrPljZcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOays+WMl+WIhuWFrOWPuCcgVEhFTiAn5rKz5YyXJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jlsbHkuJzliIblhazlj7gnIFRIRU4gJ+WxseS4nCdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+46Z2S5bKb5YiG5YWs5Y+4JyBUSEVOICflsbHkuJwnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOemj+W7uuWIhuWFrOWPuCcgVEhFTiAn56aP5bu6J1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jmuZbljZfliIblhazlj7gnIFRIRU4gJ+a5luWNlydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45a6J5b695YiG5YWs5Y+4JyBUSEVOICflronlvr0nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOWugeazouWIhuWFrOWPuCcgVEhFTiAn5rWZ5rGfJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jlkInmnpfliIblhazlj7gnIFRIRU4gJ+WQieaelydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45LqR5Y2X5YiG5YWs5Y+4JyBUSEVOICfkupHljZcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOWbm+W3neWIhuWFrOWPuCcgVEhFTiAn5Zub5bedJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jovr3lroHliIblhazlj7gnIFRIRU4gJ+i+veWugSdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45rGf6KW/5YiG5YWs5Y+4JyBUSEVOICfmsZ/opb8nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOWkqea0peWIhuWFrOWPuCcgVEhFTiAn5aSp5rSlJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jmuZbljJfliIblhazlj7gnIFRIRU4gJ+a5luWMlydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+46YeN5bqG5YiG5YWs5Y+4JyBUSEVOICfph43luoYnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOW5v+ilv+WIhuWFrOWPuCcgVEhFTiAn5bm/6KW/J1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jlsbHopb/liIblhazlj7gnIFRIRU4gJ+WxseilvydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45LiK5rW35YiG5YWs5Y+4JyBUSEVOICfkuIrmtbcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOS4reWxseWIhuWFrOWPuCcgVEhFTiAn5bm/5LicJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jotLXlt57liIblhazlj7gnIFRIRU4gJ+i0teW3nidcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45YaF6JKZ5Y+k5YiG5YWs5Y+4JyBUSEVOICflhoXokpnlj6QnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOWMl+S6rOWIhuWFrOWPuCcgVEhFTiAn5YyX5LqsJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jpu5HpvpnmsZ/liIblhazlj7gnIFRIRU4gJ+m7kem+meaxnydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+46ZmV6KW/5YiG5YWs5Y+4JyBUSEVOICfpmZXopb8nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOaWsOeWhuWIhuWFrOWPuCcgVEhFTiAn5paw55aGJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jnlJjogoPliIblhazlj7gnIFRIRU4gJ+eUmOiCgydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45aSn6L+e5YiG5YWs5Y+4JyBUSEVOICfovr3lroEnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOa1t+WNl+WIhuWFrOWPuCcgVEhFTiAn5rW35Y2XJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jlroHlpI/liIblhazlj7gnIFRIRU4gJ+WugeWkjydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+46Z2S5rW35YiG5YWs5Y+4JyBUSEVOICfpnZLmtbcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOilv+iXj+WIhuWFrOWPuCcgVEhFTiAn6KW/6JePJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflub/kuJzliIblhazlj7gnIFRIRU4gJ+W5v+S4nCdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5rWZ5rGf5YiG5YWs5Y+4JyBUSEVOICfmtZnmsZ8nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+axn+iLj+WIhuWFrOWPuCcgVEhFTiAn5rGf6IuPJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfmsrPljZfliIblhazlj7gnIFRIRU4gJ+ays+WNlydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5rKz5YyX5YiG5YWs5Y+4JyBUSEVOICfmsrPljJcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WxseS4nOWIhuWFrOWPuCcgVEhFTiAn5bGx5LicJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfpnZLlspvliIblhazlj7gnIFRIRU4gJ+WxseS4nCdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n56aP5bu65YiG5YWs5Y+4JyBUSEVOICfnpo/lu7onXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+a5luWNl+WIhuWFrOWPuCcgVEhFTiAn5rmW5Y2XJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflronlvr3liIblhazlj7gnIFRIRU4gJ+WuieW+vSdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6B5rOi5YiG5YWs5Y+4JyBUSEVOICfmtZnmsZ8nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WQieael+WIhuWFrOWPuCcgVEhFTiAn5ZCJ5p6XJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfkupHljZfliIblhazlj7gnIFRIRU4gJ+S6keWNlydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5Zub5bed5YiG5YWs5Y+4JyBUSEVOICflm5vlt50nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+i+veWugeWIhuWFrOWPuCcgVEhFTiAn6L695a6BJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfmsZ/opb/liIblhazlj7gnIFRIRU4gJ+axn+ilvydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5aSp5rSl5YiG5YWs5Y+4JyBUSEVOICflpKnmtKUnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+a5luWMl+WIhuWFrOWPuCcgVEhFTiAn5rmW5YyXJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfph43luobliIblhazlj7gnIFRIRU4gJ+mHjeW6hidcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5bm/6KW/5YiG5YWs5Y+4JyBUSEVOICflub/opb8nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+Wxseilv+WIhuWFrOWPuCcgVEhFTiAn5bGx6KW/J1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfkuIrmtbfliIblhazlj7gnIFRIRU4gJ+S4iua1tydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5Lit5bGx5YiG5YWs5Y+4JyBUSEVOICflub/kuJwnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+i0teW3nuWIhuWFrOWPuCcgVEhFTiAn6LS15beeJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflhoXokpnlj6TliIblhazlj7gnIFRIRU4gJ+WGheiSmeWPpCdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5YyX5Lqs5YiG5YWs5Y+4JyBUSEVOICfljJfkuqwnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+m7kem+meaxn+WIhuWFrOWPuCcgVEhFTiAn6buR6b6Z5rGfJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfpmZXopb/liIblhazlj7gnIFRIRU4gJ+mZleilvydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5paw55aG5YiG5YWs5Y+4JyBUSEVOICfmlrDnloYnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+eUmOiCg+WIhuWFrOWPuCcgVEhFTiAn55SY6IKDJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflpKfov57liIblhazlj7gnIFRIRU4gJ+i+veWugSdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5rW35Y2X5YiG5YWs5Y+4JyBUSEVOICfmtbfljZcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WugeWkj+WIhuWFrOWPuCcgVEhFTiAn5a6B5aSPJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfpnZLmtbfliIblhazlj7gnIFRIRU4gJ+mdkua1tydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n6KW/6JeP5YiG5YWs5Y+4JyBUSEVOICfopb/ol48nXHJcbiAgICAgICAgZWxzZSBjb21wYW55X25hbWVcclxuICAgIEVORCBBUyAn55yB5Lu9Jyxcclxuc3VtKGNhc2Ugd2hlbiBvcmRlcl9tb250aD0nMjAyMjA1JyB0aGVuIG9yZGVyX2Ftb3VudCoxIGVsc2Ugb3JkZXJfYW1vdW50KjAgZW5kKSBBUyAn5pys5pyI6ZSA5ZSu6aKdJyxcclxuc3VtKGNhc2Ugd2hlbiBvcmRlcl9tb250aD0nMjAyMTA1JyB0aGVuIG9yZGVyX2Ftb3VudCoxIGVsc2Ugb3JkZXJfYW1vdW50KjAgZW5kKSBBUyAnMjAyMTA1Jyxcclxuc3VtKGNhc2Ugd2hlbiBvcmRlcl9tb250aD0nMjAyMjA1JyB0aGVuIG9yZGVyX2Ftb3VudCoxIGVsc2Ugb3JkZXJfYW1vdW50KjAgZW5kKS9zdW0oY2FzZSB3aGVuIG9yZGVyX21vbnRoPScyMDIxMDUnIHRoZW4gb3JkZXJfYW1vdW50KjEgZWxzZSBvcmRlcl9hbW91bnQqMCBlbmQpIGFzICflkIzmr5Tovr7miJDnjocnXHJcbkZST00gb3JkZXJfb3JkZXJcclxuV0hFUkUgKG9yZGVyX3N0YXR1cz0yIE9SIG9yZGVyX3N0YXR1cz0zIE9SIG9yZGVyX3N0YXR1cz05OSkgYW5kIChvcmRlcl9tb250aD0nMjAyMjA1JyBvciBvcmRlcl9tb250aD0nMjAyMTA1JylcclxuR1JPVVAgQlkg55yB5Lu9XHJcbk9SREVSIEJZIOacrOaciOmUgOWUruminSBERVNDXHJcbiIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"本月省份业绩排名\",\"Time\":\"2022-05-07 10:44:20\",\"UpdateTime\":\"2022-05-11 14:35:18\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"98\",\"ReqAvgUseMs\":\"43\",\"Id\":\"d48d136a-5eb9-4e4b-a732-333d2cc39ee4\",\"DatamodelQueryName\":\"本年新增人数\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IGNvdW50KCopIGZyb20gbWVtYmVyX2luZm9cclxud2hlcmUgb3Blbl9jYXJkX3RpbWU+PScyMDIyLjAxLjAxJyIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjE0fQ==\",\"QueryType\":\"native\",\"Description\":\"本年新增人数\",\"Time\":\"2022-05-07 13:54:23\",\"UpdateTime\":\"2022-05-11 11:03:26\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"14a3b845-f66e-2b5f-73a8-9332a98faa97\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"92\",\"ReqAvgUseMs\":\"604\",\"Id\":\"0c7ed8d4-0193-4216-be23-20c26751c3a6\",\"DatamodelQueryName\":\"今日客单价\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5IjoiU0VMRUNUIGF2Zyhgc291cmNlYC5gc3VtYCkgQVMgYGF2Z2BcclxuRlJPTSAoU0VMRUNUIGBvcmRlcl9vcmRlcmAuYGN1c3RvbWVyX2NhcmRgIEFTIGBjdXN0b21lcl9jYXJkYCwgc3VtKGBvcmRlcl9vcmRlcmAuYG9yZGVyX2Ftb3VudGApIEFTIGBzdW1gIEZST00gYG9yZGVyX29yZGVyYFxyXG5XSEVSRSAoYG9yZGVyX29yZGVyYC5gcGF5X3RpbWVgID49ZGF0ZShub3coNikpIGFuZCBgb3JkZXJfb3JkZXJgLmBwYXlfdGltZWAgPj1kYXRlKG5vdyg2KSkgYW5kIGBvcmRlcl9vcmRlcmAuYHBheV90aW1lYCA8IGRhdGVfYWRkKGRhdGUobm93KDYpKSwgSU5URVJWQUwgMSBkYXkpXHJcbiAgIEFORCAoYG9yZGVyX29yZGVyYC5gb3JkZXJfc3RhdHVzYCA9IDJcclxuICAgIE9SIGBvcmRlcl9vcmRlcmAuYG9yZGVyX3N0YXR1c2AgPSAzIE9SIGBvcmRlcl9vcmRlcmAuYG9yZGVyX3N0YXR1c2AgPSA5OSkpXHJcbkdST1VQIEJZIGBvcmRlcl9vcmRlcmAuYGN1c3RvbWVyX2NhcmRgXHJcbk9SREVSIEJZIGBvcmRlcl9vcmRlcmAuYGN1c3RvbWVyX2NhcmRgIEFTQykgYHNvdXJjZWAiLCJ0ZW1wbGF0ZS10YWdzIjp7fX0sImRhdGFiYXNlIjoxM30=\",\"QueryType\":\"native\",\"Description\":\"今日客单价\",\"Time\":\"2022-05-07 09:02:23\",\"UpdateTime\":\"2022-05-11 11:02:26\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"78\",\"ReqAvgUseMs\":\"8868\",\"Id\":\"a306141b-e71c-43fc-8c58-b0e72302e5c7\",\"DatamodelQueryName\":\"本月促销活动\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IGEuaWQsYS5vcmRlcl9zdGF0dXMsYS5vcmRlcl9tb250aCxiLm9yZGVyX2lkLFxyXG5jYXNlIFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J01BWUgyMjQxJyB0aGVuICfnvo7ogqTlpZfoo4UnIFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0pDMjAyMicgdGhlbiAn5rS75Yqb6IKg6ZO45aWX6KOFJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0FDQzIwMjInIHRoZW4gJ0FDQ+S8mOaDoOWll+ijhSdcclxuICBlbmQgYXMgJ+a0u+WKqOWQjeensCcsXHJcbnN1bShiLnF1YW50aXR5KSBhcyAn6ZSA5ZSu6YePJyxzdW0oYi50b3RhbF9wcmljZSkgYXMgJ+mUgOWUruminScsXHJcbmNhc2UgXHJcbiAgd2hlbiBiLnNlcmlhbF9ubz0nTUFZSDIyNDEnIHRoZW4gc3VtKGIucXVhbnRpdHkpLzM3MDAwIFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0pDMjAyMicgdGhlbiBzdW0oYi5xdWFudGl0eSkvMzc4MDBcclxuICB3aGVuIGIuc2VyaWFsX25vPSdBQ0MyMDIyJyB0aGVuIHN1bShiLnF1YW50aXR5KS8yNTAwMFxyXG4gIGVuZCBhcyAn55uu5qCH5a6M5oiQ546HJyBcclxuZnJvbVxyXG4oc2VsZWN0IGlkLG9yZGVyX3N0YXR1cyxvcmRlcl9tb250aCBmcm9tIG9yZGVyX29yZGVyKSBhXHJcbmpvaW5cclxuKHNlbGVjdCBvcmRlcl9pZCxzZXJpYWxfbm8scXVhbnRpdHksdG90YWxfcHJpY2UgZnJvbSBvcmRlcl9wcm9kdWN0KSBiXHJcbm9uIChhLmlkPWIub3JkZXJfaWQpXHJcbndoZXJlIGEub3JkZXJfc3RhdHVzIGluICgyLDMsOTkpIGFuZCBhLm9yZGVyX21vbnRoID0nMjAyMjA0JyBhbmQgYi5zZXJpYWxfbm8gaW4gKCdNQVlIMjI0MScsJ0pDMjAyMicsJ0FDQzIwMjInKVxyXG5ncm91cCBieSBiLnNlcmlhbF9ubyIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"统计本月促销产品的销售额\",\"Time\":\"2022-04-29 10:25:33\",\"UpdateTime\":\"2022-05-11 11:01:29\",\"UserId\":\"33d8d854-6a07-47b3-8326-c9403db7a430\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"00000000-0000-0000-0000-000000000000\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"87\",\"ReqAvgUseMs\":\"300\",\"Id\":\"e59be632-b047-4f64-9c38-85841253fae2\",\"DatamodelQueryName\":\"今日销售额（万元）\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTMsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NjYzLCJqb2lucyI6W10sImZpbHRlciI6W1sidGltZS1pbnRlcnZhbCIsWyJmaWVsZC1pZCIsMTEwNDBdLCJjdXJyZW50IiwiZGF5Il0sImFuZCIsWyI9IixbImZpZWxkLWlkIiwxMTA3Ml0sIjIwMjIwNSJdLCJhbmQiLFsiYnJhY2tldCIsW1siPSIsWyJmaWVsZC1pZCIsMTEwNzhdLCIyIl0sIm9yIixbIj0iLFsiZmllbGQtaWQiLDExMDc4XSwiMyJdLCJvciIsWyI9IixbImZpZWxkLWlkIiwxMTA3OF0sIjk5Il1dXV0sImFnZ3JlZ2F0aW9uIjpbWyJzdW0iLFsiZmllbGQtaWQiLDExMDQxXV1dfSwidHlwZSI6InF1ZXJ5IiwidmVyc2lvbiI6InBlcmZlY3QifQ==\",\"QueryType\":\"query\",\"Description\":\"今日销售额（万元）\",\"Time\":\"2022-05-06 15:55:28\",\"UpdateTime\":\"2022-05-11 11:01:29\",\"UserId\":\"cf8d3088-31ec-4230-bfcf-fcffa1fdd56c\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"67\",\"ReqAvgUseMs\":\"2118\",\"Id\":\"84bd12d7-299c-4691-ba63-ca7c17aa9633\",\"DatamodelQueryName\":\"testTodayMoney\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTMsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NjYzLCJqb2lucyI6W10sImZpbHRlciI6W1sidGltZS1pbnRlcnZhbCIsWyJmaWVsZC1pZCIsMTEwNDBdLCJjdXJyZW50IiwiZGF5Il0sImFuZCIsWyI9IixbImZpZWxkLWlkIiwxMTA3Ml0sIjIwMjIwNCJdLCJhbmQiLFsiPSIsWyJmaWVsZC1pZCIsMTEwNzhdLCIzIl1dLCJhZ2dyZWdhdGlvbiI6W1sic3VtIixbImZpZWxkLWlkIiwxMTA0MV1dXX0sInR5cGUiOiJxdWVyeSIsInZlcnNpb24iOiJwZXJmZWN0In0=\",\"QueryType\":\"\",\"Description\":\"今日业绩\",\"Time\":\"2022-04-07 11:54:39\",\"UpdateTime\":\"2022-05-11 11:00:04\",\"UserId\":\"33d8d854-6a07-47b3-8326-c9403db7a430\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"00000000-0000-0000-0000-000000000000\",\"Cache\":\"True\",\"CacheInterval\":\"10\"},{\"DatamodelQueryId\":\"97\",\"ReqAvgUseMs\":\"57913\",\"Id\":\"e33cceab-e648-49e4-9ab6-b0adc183ae3d\",\"DatamodelQueryName\":\"本年销售额\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IHN1bShvcmRlcl9hbW91bnQpIGZyb20gb3JkZXJfb3JkZXJcclxud2hlcmUgb3JkZXJfbW9udGg+PScyMDIyMDEnIGFuZCBvcmRlcl9zdGF0dXMgaW4oMiwzLDk5KSIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"本年销售额（sql有>号报错）\",\"Time\":\"2022-05-07 13:43:26\",\"UpdateTime\":\"2022-05-11 10:59:51\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"30\"},{\"DatamodelQueryId\":\"68\",\"ReqAvgUseMs\":\"2\",\"Id\":\"f536c67b-b1d1-4695-8392-64b0801264c2\",\"DatamodelQueryName\":\"liang_test\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MiwicXVlcnkiOnsic291cmNlLXRhYmxlIjoyfSwidHlwZSI6InF1ZXJ5IiwidmVyc2lvbiI6InBlcmZlY3QifQ==\",\"QueryType\":null,\"Description\":\"liang_testliang_testliang_testliang_test\",\"Time\":\"2022-04-08 14:59:49\",\"UpdateTime\":\"2022-05-10 15:40:07\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"False\",\"DatabaseId\":\"8edc4c89-88d1-a59d-8cb5-787489993b08\",\"GroupId\":\"00000000-0000-0000-0000-000000000000\",\"Cache\":\"True\",\"CacheInterval\":\"10\"}],\"RecordCount\":\"22\"}";
		this.PrintObject(aa);

		DefaultListDataSource<List<DataModelInfo>> o3 = JSON.parseObject(aa,
				new TypeReference<DefaultListDataSource<List<DataModelInfo>>>() {
				});

		this.PrintObject(o3);

	}

	public void testJsonDeserial6() {
		String aa = "{\"DataSource\":[{\"DatamodelQueryId\":\"90\",\"ReqAvgUseMs\":\"2439\",\"Id\":\"96bbda7c-8dfe-461f-9ea0-f78269334682\",\"DatamodelQueryName\":\"促销活动\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IGEuaWQsYS5vcmRlcl9zdGF0dXMsYS5vcmRlcl9tb250aCxiLm9yZGVyX2lkLFxyXG5jYXNlIFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0ZTJyB0aGVuICcxLeaWsOWTgeS4iuW4gu+8mkZT5LiA55Sf57OWJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0ZTNicgdGhlbiAnMi3mlrDlk4HkuIrluILvvJpGUzbkuIDnlJ/ns5YnXHJcbiAgd2hlbiBiLnNlcmlhbF9ubz0nRlMzMCcgdGhlbiAnMy3mlrDlk4HkuIrluILvvJpGUzMw5rS756uL5aSaJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0FDQzIwMjInIHRoZW4gJzctQUND5LyY5oOg5aWX6KOFJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J1NQQ1MwMicgdGhlbiAnNi3lpI/lraPkuKrmiqTlpZfoo4Ut55S35aOrJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J1NQQ1MwMScgdGhlbiAnNC3lpI/lraPkuKrmiqTlpZfoo4Ut5aWz5aOrJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0VUMjAyMicgdGhlbiAnNS3niLHmlr3ok5PlqbTnq6XmtJfmiqTlpZfoo4UnXHJcbiAgZW5kIGFzICfmtLvliqjlkI3np7AnLFxyXG5zdW0oYi5xdWFudGl0eSkgYXMgJ+mUgOWUrumHjycsc3VtKGIudG90YWxfcHJpY2UpIGFzICfplIDllK7pop0nLFxyXG5jYXNlIFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0FDQzIwMjInIHRoZW4gc3VtKGIucXVhbnRpdHkpLzI1MDAwXHJcbiAgd2hlbiBiLnNlcmlhbF9ubz0nU1BDUzAyJyB0aGVuIHN1bShiLnF1YW50aXR5KS8yMDg1MFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J1NQQ1MwMScgdGhlbiBzdW0oYi5xdWFudGl0eSkvMTM1ODBcclxuICB3aGVuIGIuc2VyaWFsX25vPSdFVDIwMjInIHRoZW4gc3VtKGIucXVhbnRpdHkpLzQ1MDBcclxuICBlbmQgYXMgJ+ebruagh+WujOaIkOeOhycgXHJcbmZyb21cclxuKHNlbGVjdCBpZCxvcmRlcl9zdGF0dXMsb3JkZXJfbW9udGggZnJvbSBvcmRlcl9vcmRlcikgYVxyXG5qb2luXHJcbihzZWxlY3Qgb3JkZXJfaWQsc2VyaWFsX25vLHF1YW50aXR5LHRvdGFsX3ByaWNlIGZyb20gb3JkZXJfcHJvZHVjdCkgYlxyXG5vbiAoYS5pZD1iLm9yZGVyX2lkKVxyXG53aGVyZSBhLm9yZGVyX3N0YXR1cyBpbiAoMiwzLDk5KSBhbmQgYS5vcmRlcl9tb250aCA+PScyMDIyMDQnIGFuZCBiLnNlcmlhbF9ubyBpbiAoJ0FDQzIwMjInLCdTUENTMDInLCdTUENTMDEnLCdFVDIwMjInLCdGUycsJ0ZTNicsJ0ZTMzAnKVxyXG5ncm91cCBieSBiLnNlcmlhbF9ub1xyXG5vcmRlciBieSDmtLvliqjlkI3np7AiLCJ0ZW1wbGF0ZS10YWdzIjp7fX0sImRhdGFiYXNlIjoxM30=\",\"QueryType\":\"native\",\"Description\":\"促销活动\",\"Time\":\"2022-05-07 08:49:51\",\"UpdateTime\":\"2022-05-28 15:55:27\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"101\",\"ReqAvgUseMs\":\"169\",\"Id\":\"413e56b9-18fa-4e85-9b16-5a8cee9c3387\",\"DatamodelQueryName\":\"本月办卡人数及升级方式\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IFxyXG4gIGNhc2UgXHJcbiAgICB3aGVuIHByb21vdGlvbl90eXBlPTEgdGhlbiAnMjk556S85YyFJ1xyXG4gICAgd2hlbiBwcm9tb3Rpb25fdHlwZT0yIHRoZW4gJzYwMHB2J1xyXG4gICAgd2hlbiBwcm9tb3Rpb25fdHlwZSBpcyBudWxsIHRoZW4gJ+acquWNh+e6pydcclxuICAgIGVuZCBhcyAn5Yqe5Y2h5Lq65pWw5Y+K5Y2H57qn5pa55byPJyxjb3VudCgqKSBhcyAn5Lq65pWwJ1xyXG5mcm9tIG1lbWJlcl9pbmZvXHJcbndoZXJlIG9wZW5fY2FyZF90aW1lPj0nMjAyMi4wNS4wMScgYW5kIG9wZW5fY2FyZF90aW1lPCcyMDIyLjA2LjAxJ1xyXG5ncm91cCBieSDlip7ljaHkurrmlbDlj4rljYfnuqfmlrnlvI8iLCJ0ZW1wbGF0ZS10YWdzIjp7fX0sImRhdGFiYXNlIjoxNH0=\",\"QueryType\":\"native\",\"Description\":\"本月办卡人数及升级方式\",\"Time\":\"2022-05-07 14:04:56\",\"UpdateTime\":\"2022-05-26 10:53:34\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"14a3b845-f66e-2b5f-73a8-9332a98faa97\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"42\",\"ReqAvgUseMs\":\"6\",\"Id\":\"4bb1903d-8d59-4771-a7a6-69e84fe62832\",\"DatamodelQueryName\":\"散点图\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MiwicXVlcnkiOnsic291cmNlLXRhYmxlIjoxLCJvcmRlci1ieSI6W1siYXNjIixbImZpZWxkLWlkIiwzOTBdXV0sImZpZWxkcyI6W1siZmllbGQtaWQiLDM3N10sWyJmaWVsZC1pZCIsMzc4XSxbImZpZWxkLWlkIiwzNzldLFsiZmllbGQtaWQiLDM4M10sWyJmaWVsZC1pZCIsMzg0XSxbImZpZWxkLWlkIiwzODVdLFsiZmllbGQtaWQiLDM4Nl0sWyJmaWVsZC1pZCIsMzg3XSxbImZpZWxkLWlkIiwzODhdLFsiZmllbGQtaWQiLDM4OV0sWyJmaWVsZC1pZCIsMzkwXSxbImZpZWxkLWlkIiwzOTFdXX0sInR5cGUiOiJxdWVyeSIsInZlcnNpb24iOiJwZXJmZWN0In0=\",\"QueryType\":\"\",\"Description\":\"散点图散点图散点图散点图\",\"Time\":\"2022-01-14 08:53:31\",\"UpdateTime\":\"2022-05-25 16:50:20\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"8edc4c89-88d1-a59d-8cb5-787489993b08\",\"GroupId\":\"8a1469a6-bdb6-4b43-b90c-816184cdadec\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"23\",\"ReqAvgUseMs\":\"3\",\"Id\":\"9b92671a-2bb7-4f22-a21f-2c69e2ba3922\",\"DatamodelQueryName\":\"折线图\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MiwicXVlcnkiOnsic291cmNlLXRhYmxlIjoxLCJicmVha291dCI6W1siZmllbGQtaWQiLDM3OF1dLCJhZ2dyZWdhdGlvbiI6W1siY291bnQiXSxbInN1bSIsWyJmaWVsZC1pZCIsMzg0XV0sWyJzdW0iLFsiZmllbGQtaWQiLDM4NV1dXX0sInR5cGUiOiJxdWVyeSJ9\",\"QueryType\":\"\",\"Description\":\"dataCenterLinedataCenterLinedataCenterLinedataCenterLine\",\"Time\":\"2021-12-22 11:24:27\",\"UpdateTime\":\"2022-05-25 14:51:28\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"8edc4c89-88d1-a59d-8cb5-787489993b08\",\"GroupId\":\"8a1469a6-bdb6-4b43-b90c-816184cdadec\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"155\",\"ReqAvgUseMs\":\"117151\",\"Id\":\"5506e64b-8211-4ee6-ba93-774262094487\",\"DatamodelQueryName\":\"内蒙古各月业绩\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IFxyXG5vcmRlcl9tb250aCxzdW0ob3JkZXJfYW1vdW50KVxyXG5mcm9tIG9yZGVyX29yZGVyIFxyXG53aGVyZSBvcmRlcl9tb250aD49JzIwMjEwMScgYW5kIG9yZGVyX3N0YXR1cyBpbigyLDMsOTkpIGFuZCBjb21wYW55X2NvZGU9JzEzMDAwJ1xyXG5ncm91cCBieSBvcmRlcl9tb250aFxyXG5vcmRlciBieSBvcmRlcl9tb250aCIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"内蒙古\",\"Time\":\"2022-05-24 14:20:45\",\"UpdateTime\":\"2022-05-24 14:20:45\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"a81638b9-1f19-44dc-a9d6-86ba474a3fb3\",\"Cache\":\"True\",\"CacheInterval\":\"30\"},{\"DatamodelQueryId\":\"91\",\"ReqAvgUseMs\":\"372\",\"Id\":\"2632d148-d54f-4996-bcf4-2fe535a29fd0\",\"DatamodelQueryName\":\"今日消费人数\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTMsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NjYzLCJqb2lucyI6W10sImZpbHRlciI6W1sidGltZS1pbnRlcnZhbCIsWyJmaWVsZC1pZCIsMTEwNDBdLCJjdXJyZW50IiwiZGF5Il0sImFuZCIsWyI9IixbImZpZWxkLWlkIiwxMTA3Ml0sIjIwMjIwNSJdLCJhbmQiLFsiYnJhY2tldCIsW1siPSIsWyJmaWVsZC1pZCIsMTEwNzhdLCIyIl0sIm9yIixbIj0iLFsiZmllbGQtaWQiLDExMDc4XSwiMyJdLCJvciIsWyI9IixbImZpZWxkLWlkIiwxMTA3OF0sIjk5Il1dXV0sImFnZ3JlZ2F0aW9uIjpbWyJkaXN0aW5jdCIsWyJmaWVsZC1pZCIsMTEwNjBdXV19LCJ0eXBlIjoicXVlcnkiLCJ2ZXJzaW9uIjoicGVyZmVjdCJ9\",\"QueryType\":\"query\",\"Description\":\"今日消费人数\",\"Time\":\"2022-05-07 08:58:56\",\"UpdateTime\":\"2022-05-23 10:57:58\",\"UserId\":\"03fe16bd-0e3a-4655-a057-ace07e772ee1\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"126\",\"ReqAvgUseMs\":\"9388\",\"Id\":\"ceeeb8bb-6b08-455d-9028-51f07a572476\",\"DatamodelQueryName\":\"liang-test002\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5IjoiU0VMRUNUIGBtb250aGx5X3N0YXRpc3RpY3NgLmBvcmRlcl9tb250aGAgQVMgYG9yZGVyX21vbnRoYCxgbW9udGhseV9zdGF0aXN0aWNzYC5gYnV5ZXJfcXR5YCBBUyBgYnV5ZXJfcXR5YFxyXG4gRlJPTSBgbWFsbF9zYWxlYC5gbW9udGhseV9zdGF0aXN0aWNzYFxyXG4gd2hlcmUgb3JkZXJfbW9udGg8PScyMDIyMDQnXHJcbiB1bmlvbiBcclxuIHNlbGVjdCBvcmRlcl9tb250aCxjb3VudChkaXN0aW5jdCBjdXN0b21lcl9jYXJkKSBhcyAnYnV5ZXJfcXR5JyBmcm9tIG9yZGVyX29yZGVyXHJcbndoZXJlIG9yZGVyX21vbnRoPj0nMjAyMjA1JyBhbmQgb3JkZXJfc3RhdHVzIGluICgyLDMsOTkpXHJcbmdyb3VwIGJ5IG9yZGVyX21vbnRoXHJcbm9yZGVyIGJ5IG9yZGVyX21vbnRoXHJcbiIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"柱状图便签修改测试\",\"Time\":\"2022-05-12 16:21:49\",\"UpdateTime\":\"2022-05-20 17:16:40\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"c5df6129-0d20-4b58-8185-f731a8348396\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"99\",\"ReqAvgUseMs\":\"7971\",\"Id\":\"2c833dfd-5dc6-494c-8126-bcf6e24e93e3\",\"DatamodelQueryName\":\"本月消费人数\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTMsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NjYzLCJqb2lucyI6W10sImZpbHRlciI6W1siPSIsWyJmaWVsZC1pZCIsMTEwNzJdLCIyMDIyMDUiXSwiYW5kIixbImJyYWNrZXQiLFtbIj0iLFsiZmllbGQtaWQiLDExMDc4XSwiMiJdLCJvciIsWyI9IixbImZpZWxkLWlkIiwxMTA3OF0sIjMiXSwib3IiLFsiPSIsWyJmaWVsZC1pZCIsMTEwNzhdLCI5OSJdXV1dLCJhZ2dyZWdhdGlvbiI6W1siZGlzdGluY3QiLFsiZmllbGQtaWQiLDExMDYwXV1dfSwidHlwZSI6InF1ZXJ5IiwidmVyc2lvbiI6InBlcmZlY3QifQ==\",\"QueryType\":\"query\",\"Description\":\"本月消费人数\",\"Time\":\"2022-05-07 13:56:30\",\"UpdateTime\":\"2022-05-20 14:28:21\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"f961d11c-4d35-4838-a562-1bbc93269083\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"143\",\"ReqAvgUseMs\":\"0\",\"Id\":\"bb1ff512-584d-4090-a0e9-b40168c4d65c\",\"DatamodelQueryName\":\"testCustomColumnFromAggregation_02\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTUsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NzAwLCJicmVha291dCI6W1siZmllbGQtaWQiLDExNjgzXV0sImFnZ3JlZ2F0aW9uIjpbWyJzdW0iLFsiZmllbGQtaWQiLDExNjg0XV1dLCJleHByZXNzaW9ucyI6e30sImpvaW5zIjpbXSwiZmlsdGVyIjpbXX0sInR5cGUiOiJxdWVyeSIsInZlcnNpb24iOiJwZXJmZWN0In0=\",\"QueryType\":\"query\",\"Description\":\"testCustomColumnFromAggregation_02\",\"Time\":\"2022-05-17 15:44:38\",\"UpdateTime\":\"2022-05-19 10:52:05\",\"UserId\":\"33d8d854-6a07-47b3-8326-c9403db7a430\",\"Enable\":\"True\",\"DatabaseId\":\"9b4187ea-99be-ca76-ffdb-304b0a7f2a8b\",\"GroupId\":\"835f8160-6e09-41d9-89dc-b41662991631\",\"Cache\":\"True\",\"CacheInterval\":\"20\"},{\"DatamodelQueryId\":\"148\",\"ReqAvgUseMs\":\"287\",\"Id\":\"5b6d2773-12bf-49de-889b-6ebf00db053d\",\"DatamodelQueryName\":\"办卡数据\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IFxyXG4gIGNhc2UgXHJcbiAgICB3aGVuIHByb21vdGlvbl90eXBlPTEgdGhlbiAnMjk556S85YyFJ1xyXG4gICAgd2hlbiBwcm9tb3Rpb25fdHlwZT0yIHRoZW4gJzYwMHB2J1xyXG4gICAgd2hlbiBwcm9tb3Rpb25fdHlwZSBpcyBudWxsIHRoZW4gJ+acquWNh+e6pydcclxuICAgIGVuZCBhcyAn5Yqe5Y2h5Lq65pWw5Y+K5Y2H57qn5pa55byPJyxjb3VudCgqKSBhcyAn5Lq65pWwJ1xyXG5mcm9tIG1lbWJlcl9pbmZvXHJcbndoZXJlIG9wZW5fY2FyZF90aW1lPj0nMjAyMi4wNS4wMScgYW5kIG9wZW5fY2FyZF90aW1lPCcyMDIyLjA2LjAxJ1xyXG5ncm91cCBieSDlip7ljaHkurrmlbDlj4rljYfnuqfmlrnlvI8iLCJ0ZW1wbGF0ZS10YWdzIjp7fX0sImRhdGFiYXNlIjoxNH0=\",\"QueryType\":\"native\",\"Description\":\"办卡人数及升级人数\",\"Time\":\"2022-05-18 10:38:01\",\"UpdateTime\":\"2022-05-18 10:40:58\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"14a3b845-f66e-2b5f-73a8-9332a98faa97\",\"GroupId\":\"00000000-0000-0000-0000-000000000000\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"144\",\"ReqAvgUseMs\":\"46424\",\"Id\":\"4c6702d3-88af-47d5-97a2-74d1f4a79cb8\",\"DatamodelQueryName\":\"月销售额统计\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IG9yZGVyX21vbnRoLHN1bShvcmRlcl9hbW91bnQpXHJcbmZyb20gb3JkZXJfb3JkZXJcclxud2hlcmUgb3JkZXJfbW9udGg+PScyMDIyMDEnIGFuZCBvcmRlcl9zdGF0dXMgaW4oMiwzLDk5KVxyXG5ncm91cCBieSBvcmRlcl9tb250aCIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"统计月销售额，未扣除换购\",\"Time\":\"2022-05-17 16:18:39\",\"UpdateTime\":\"2022-05-17 16:34:45\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"00000000-0000-0000-0000-000000000000\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"128\",\"ReqAvgUseMs\":\"11379\",\"Id\":\"0084aaa1-ace7-4367-84fe-99455c2d71b8\",\"DatamodelQueryName\":\"本月新增客户数\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5IjoiU0VMRUNUIGNvdW50KCopIEFTIGBjb3VudGBcclxuRlJPTSBgaHl6bGBcclxuV0hFUkUgYGFjY21vbmA9ZGF0ZV9mb3JtYXQobm93KCksICclWS4lbScpIiwidGVtcGxhdGUtdGFncyI6e319LCJkYXRhYmFzZSI6MTJ9\",\"QueryType\":\"native\",\"Description\":\"本月新增客户数\",\"Time\":\"2022-05-13 11:08:30\",\"UpdateTime\":\"2022-05-13 11:09:52\",\"UserId\":\"3c879e85-5b86-4dba-b5e2-c1facfeabaa5\",\"Enable\":\"True\",\"DatabaseId\":\"b1605471-1082-1f82-85a8-a9c505e8bfc1\",\"GroupId\":\"880a5dda-918e-425f-8bc8-e0b89a77f11a\",\"Cache\":\"True\",\"CacheInterval\":\"20\"},{\"DatamodelQueryId\":\"104\",\"ReqAvgUseMs\":\"25812\",\"Id\":\"e3570a03-58fc-4373-86a1-4db33f823f7e\",\"DatamodelQueryName\":\"4月单品TOP20（单位：万元）\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IGEuaWQsYS5vcmRlcl9zdGF0dXMsYS5vcmRlcl9tb250aCxiLm9yZGVyX2lkLGIuc2VyaWFsX25vLHN1bShiLnRvdGFsX3ByaWNlKSxcclxuICAgIENBU0VcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdGQScgVEhFTiAn5rKZ5qOY6Iy2J1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0ZBMTAnIFRIRU4gJ+aymeajmOiMtidcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdGQTMwJyBUSEVOICfmspnmo5jojLYnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nRkExMDAxJyBUSEVOICfmspnmo5jojLYnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nQUcnIFRIRU4gJ+iKpuiNn+iDtidcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdBRzInIFRIRU4gJ+iKpuiNn+iDtidcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdBRzEwJyBUSEVOICfoiqbojZ/og7YnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nQUcxMDAxJyBUSEVOICfoiqbojZ/og7YnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nQU1QTicgVEhFTiAn55+/54mp57KJJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0FNUE4xMCcgVEhFTiAn55+/54mp57KJJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0FNUE4zMCcgVEhFTiAn55+/54mp57KJJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0FNUE4xMDAxJyBUSEVOICfnn7/niannsoknXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nUFNOJyBUSEVOICfogr3ol7vokKXlhbvnsoknXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nUFNONicgVEhFTiAn6IK96Je76JCl5YW757KJJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J1BTTjMwJyBUSEVOICfogr3ol7vokKXlhbvnsoknXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nUFNONjAxJyBUSEVOICfogr3ol7vokKXlhbvnsoknXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nQ1BUMDInIFRIRU4gJ+ivreazsOeJhydcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdDUFQwMycgVEhFTiAn6K+t5rOw54mHJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0NQVDgnIFRIRU4gJ+ivreazsOeJhydcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdOQicgVEhFTiAn6JCl5YW76aSQJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J05CMzAnIFRIRU4gJ+iQpeWFu+mkkCdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdPTFMnIFRIRU4gJ+S4gOeUn+ezlidcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdPTFM2JyBUSEVOICfkuIDnlJ/ns5YnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nUFNHJyBUSEVOICfmspnokpwnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nUFNHNCcgVEhFTiAn5rKZ6JKcJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J1BTRzQwMScgVEhFTiAn5rKZ6JKcJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J01BNTAyMScgVEhFTiAn5ruL5ram54i96IKk5rC0J1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J01BNTAyMTMnIFRIRU4gJ+a7i+a2pueIveiCpOawtCdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdQU0QnIFRIRU4gJ+S6v+eUn+iPjCdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdDRFAnIFRIRU4gJ+aymeajmOaxgSdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdNQTUwMTEnIFRIRU4gJ+aflOWSjOa0geiCpOS5sydcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdNQTcwMzknIFRIRU4gJ+WuieeTtueyvuWNjua2sidcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdBTTAwNzMwJyBUSEVOICfoiqbojZ/pnaLohpwnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nQU0wMDcnIFRIRU4gJ+iKpuiNn+mdouiGnCdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdFSFAnIFRIRU4gJ+iGs+mjn+e6pOe7tOeyiSdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdFSFAzMCcgVEhFTiAn5aSN5ZCI6Iaz6aOf57qk57u057KJJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0dQTScgVEhFTiAn5YGl5omsJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0ZEMzAnIFRIRU4gJ+mrmOe6pOS5kCdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdPTEknIFRIRU4gJ+a0u+eri+WkmidcclxuICAgICAgICBlbHNlIGIuc2VyaWFsX25vXHJcbiAgICBFTkQgQVMgYmNjXHJcbmZyb21cclxuKHNlbGVjdCBpZCxvcmRlcl9zdGF0dXMsb3JkZXJfbW9udGggZnJvbSBvcmRlcl9vcmRlcikgYVxyXG5qb2luXHJcbihzZWxlY3Qgb3JkZXJfaWQsc2VyaWFsX25vLHRvdGFsX3ByaWNlIGZyb20gb3JkZXJfcHJvZHVjdCkgYlxyXG5vbiAoYS5pZD1iLm9yZGVyX2lkKVxyXG53aGVyZSBhLm9yZGVyX3N0YXR1cyBpbiAoMiwzLDk5KSBhbmQgYS5vcmRlcl9tb250aCA9JzIwMjIwNSdcclxuZ3JvdXAgYnkgYmNjXHJcbm9yZGVyIGJ5IHN1bShiLnRvdGFsX3ByaWNlKSBkZXNjXHJcbmxpbWl0IDIwIiwidGVtcGxhdGUtdGFncyI6e319LCJkYXRhYmFzZSI6MTN9\",\"QueryType\":\"native\",\"Description\":\"4月单品TOP20\",\"Time\":\"2022-05-07 14:23:50\",\"UpdateTime\":\"2022-05-13 09:27:44\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"88\",\"ReqAvgUseMs\":\"8009\",\"Id\":\"806940f4-9f27-4e0b-b278-1f146728406c\",\"DatamodelQueryName\":\"本月销售额\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTMsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NjYzLCJqb2lucyI6W10sImZpbHRlciI6W1siPSIsWyJmaWVsZC1pZCIsMTEwNzJdLCIyMDIyMDUiXSwiYW5kIixbImJyYWNrZXQiLFtbIj0iLFsiZmllbGQtaWQiLDExMDc4XSwiMiJdLCJvciIsWyI9IixbImZpZWxkLWlkIiwxMTA3OF0sIjMiXSwib3IiLFsiPSIsWyJmaWVsZC1pZCIsMTEwNzhdLCI5OSJdXV1dLCJhZ2dyZWdhdGlvbiI6W1sic3VtIixbImZpZWxkLWlkIiwxMTA0MV1dXX0sInR5cGUiOiJxdWVyeSIsInZlcnNpb24iOiJwZXJmZWN0In0=\",\"QueryType\":\"query\",\"Description\":\"本月销售额\",\"Time\":\"2022-05-06 15:59:11\",\"UpdateTime\":\"2022-05-11 14:47:36\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"93\",\"ReqAvgUseMs\":\"31719\",\"Id\":\"1bc1df9f-4d5f-460f-91ec-286d40d0eb48\",\"DatamodelQueryName\":\"本月省份业绩排名\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5IjoiU0VMRUNUICBcclxuICAgIENBU0VcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45bm/5Lic5YiG5YWs5Y+4JyBUSEVOICflub/kuJwnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOa1meaxn+WIhuWFrOWPuCcgVEhFTiAn5rWZ5rGfJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jmsZ/oi4/liIblhazlj7gnIFRIRU4gJ+axn+iLjydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45rKz5Y2X5YiG5YWs5Y+4JyBUSEVOICfmsrPljZcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOays+WMl+WIhuWFrOWPuCcgVEhFTiAn5rKz5YyXJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jlsbHkuJzliIblhazlj7gnIFRIRU4gJ+WxseS4nCdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+46Z2S5bKb5YiG5YWs5Y+4JyBUSEVOICflsbHkuJwnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOemj+W7uuWIhuWFrOWPuCcgVEhFTiAn56aP5bu6J1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jmuZbljZfliIblhazlj7gnIFRIRU4gJ+a5luWNlydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45a6J5b695YiG5YWs5Y+4JyBUSEVOICflronlvr0nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOWugeazouWIhuWFrOWPuCcgVEhFTiAn5rWZ5rGfJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jlkInmnpfliIblhazlj7gnIFRIRU4gJ+WQieaelydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45LqR5Y2X5YiG5YWs5Y+4JyBUSEVOICfkupHljZcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOWbm+W3neWIhuWFrOWPuCcgVEhFTiAn5Zub5bedJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jovr3lroHliIblhazlj7gnIFRIRU4gJ+i+veWugSdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45rGf6KW/5YiG5YWs5Y+4JyBUSEVOICfmsZ/opb8nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOWkqea0peWIhuWFrOWPuCcgVEhFTiAn5aSp5rSlJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jmuZbljJfliIblhazlj7gnIFRIRU4gJ+a5luWMlydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+46YeN5bqG5YiG5YWs5Y+4JyBUSEVOICfph43luoYnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOW5v+ilv+WIhuWFrOWPuCcgVEhFTiAn5bm/6KW/J1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jlsbHopb/liIblhazlj7gnIFRIRU4gJ+WxseilvydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45LiK5rW35YiG5YWs5Y+4JyBUSEVOICfkuIrmtbcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOS4reWxseWIhuWFrOWPuCcgVEhFTiAn5bm/5LicJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jotLXlt57liIblhazlj7gnIFRIRU4gJ+i0teW3nidcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45YaF6JKZ5Y+k5YiG5YWs5Y+4JyBUSEVOICflhoXokpnlj6QnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOWMl+S6rOWIhuWFrOWPuCcgVEhFTiAn5YyX5LqsJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jpu5HpvpnmsZ/liIblhazlj7gnIFRIRU4gJ+m7kem+meaxnydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+46ZmV6KW/5YiG5YWs5Y+4JyBUSEVOICfpmZXopb8nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOaWsOeWhuWIhuWFrOWPuCcgVEhFTiAn5paw55aGJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jnlJjogoPliIblhazlj7gnIFRIRU4gJ+eUmOiCgydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45aSn6L+e5YiG5YWs5Y+4JyBUSEVOICfovr3lroEnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOa1t+WNl+WIhuWFrOWPuCcgVEhFTiAn5rW35Y2XJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jlroHlpI/liIblhazlj7gnIFRIRU4gJ+WugeWkjydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+46Z2S5rW35YiG5YWs5Y+4JyBUSEVOICfpnZLmtbcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOilv+iXj+WIhuWFrOWPuCcgVEhFTiAn6KW/6JePJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflub/kuJzliIblhazlj7gnIFRIRU4gJ+W5v+S4nCdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5rWZ5rGf5YiG5YWs5Y+4JyBUSEVOICfmtZnmsZ8nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+axn+iLj+WIhuWFrOWPuCcgVEhFTiAn5rGf6IuPJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfmsrPljZfliIblhazlj7gnIFRIRU4gJ+ays+WNlydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5rKz5YyX5YiG5YWs5Y+4JyBUSEVOICfmsrPljJcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WxseS4nOWIhuWFrOWPuCcgVEhFTiAn5bGx5LicJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfpnZLlspvliIblhazlj7gnIFRIRU4gJ+WxseS4nCdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n56aP5bu65YiG5YWs5Y+4JyBUSEVOICfnpo/lu7onXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+a5luWNl+WIhuWFrOWPuCcgVEhFTiAn5rmW5Y2XJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflronlvr3liIblhazlj7gnIFRIRU4gJ+WuieW+vSdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6B5rOi5YiG5YWs5Y+4JyBUSEVOICfmtZnmsZ8nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WQieael+WIhuWFrOWPuCcgVEhFTiAn5ZCJ5p6XJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfkupHljZfliIblhazlj7gnIFRIRU4gJ+S6keWNlydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5Zub5bed5YiG5YWs5Y+4JyBUSEVOICflm5vlt50nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+i+veWugeWIhuWFrOWPuCcgVEhFTiAn6L695a6BJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfmsZ/opb/liIblhazlj7gnIFRIRU4gJ+axn+ilvydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5aSp5rSl5YiG5YWs5Y+4JyBUSEVOICflpKnmtKUnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+a5luWMl+WIhuWFrOWPuCcgVEhFTiAn5rmW5YyXJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfph43luobliIblhazlj7gnIFRIRU4gJ+mHjeW6hidcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5bm/6KW/5YiG5YWs5Y+4JyBUSEVOICflub/opb8nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+Wxseilv+WIhuWFrOWPuCcgVEhFTiAn5bGx6KW/J1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfkuIrmtbfliIblhazlj7gnIFRIRU4gJ+S4iua1tydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5Lit5bGx5YiG5YWs5Y+4JyBUSEVOICflub/kuJwnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+i0teW3nuWIhuWFrOWPuCcgVEhFTiAn6LS15beeJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflhoXokpnlj6TliIblhazlj7gnIFRIRU4gJ+WGheiSmeWPpCdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5YyX5Lqs5YiG5YWs5Y+4JyBUSEVOICfljJfkuqwnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+m7kem+meaxn+WIhuWFrOWPuCcgVEhFTiAn6buR6b6Z5rGfJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfpmZXopb/liIblhazlj7gnIFRIRU4gJ+mZleilvydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5paw55aG5YiG5YWs5Y+4JyBUSEVOICfmlrDnloYnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+eUmOiCg+WIhuWFrOWPuCcgVEhFTiAn55SY6IKDJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflpKfov57liIblhazlj7gnIFRIRU4gJ+i+veWugSdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5rW35Y2X5YiG5YWs5Y+4JyBUSEVOICfmtbfljZcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WugeWkj+WIhuWFrOWPuCcgVEhFTiAn5a6B5aSPJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfpnZLmtbfliIblhazlj7gnIFRIRU4gJ+mdkua1tydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n6KW/6JeP5YiG5YWs5Y+4JyBUSEVOICfopb/ol48nXHJcbiAgICAgICAgZWxzZSBjb21wYW55X25hbWVcclxuICAgIEVORCBBUyAn55yB5Lu9Jyxcclxuc3VtKGNhc2Ugd2hlbiBvcmRlcl9tb250aD0nMjAyMjA1JyB0aGVuIG9yZGVyX2Ftb3VudCoxIGVsc2Ugb3JkZXJfYW1vdW50KjAgZW5kKSBBUyAn5pys5pyI6ZSA5ZSu6aKdJyxcclxuc3VtKGNhc2Ugd2hlbiBvcmRlcl9tb250aD0nMjAyMTA1JyB0aGVuIG9yZGVyX2Ftb3VudCoxIGVsc2Ugb3JkZXJfYW1vdW50KjAgZW5kKSBBUyAnMjAyMTA1Jyxcclxuc3VtKGNhc2Ugd2hlbiBvcmRlcl9tb250aD0nMjAyMjA1JyB0aGVuIG9yZGVyX2Ftb3VudCoxIGVsc2Ugb3JkZXJfYW1vdW50KjAgZW5kKS9zdW0oY2FzZSB3aGVuIG9yZGVyX21vbnRoPScyMDIxMDUnIHRoZW4gb3JkZXJfYW1vdW50KjEgZWxzZSBvcmRlcl9hbW91bnQqMCBlbmQpIGFzICflkIzmr5Tovr7miJDnjocnXHJcbkZST00gb3JkZXJfb3JkZXJcclxuV0hFUkUgKG9yZGVyX3N0YXR1cz0yIE9SIG9yZGVyX3N0YXR1cz0zIE9SIG9yZGVyX3N0YXR1cz05OSkgYW5kIChvcmRlcl9tb250aD0nMjAyMjA1JyBvciBvcmRlcl9tb250aD0nMjAyMTA1JylcclxuR1JPVVAgQlkg55yB5Lu9XHJcbk9SREVSIEJZIOacrOaciOmUgOWUruminSBERVNDXHJcbiIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"本月省份业绩排名\",\"Time\":\"2022-05-07 10:44:20\",\"UpdateTime\":\"2022-05-11 14:35:18\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"98\",\"ReqAvgUseMs\":\"43\",\"Id\":\"d48d136a-5eb9-4e4b-a732-333d2cc39ee4\",\"DatamodelQueryName\":\"本年新增人数\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IGNvdW50KCopIGZyb20gbWVtYmVyX2luZm9cclxud2hlcmUgb3Blbl9jYXJkX3RpbWU+PScyMDIyLjAxLjAxJyIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjE0fQ==\",\"QueryType\":\"native\",\"Description\":\"本年新增人数\",\"Time\":\"2022-05-07 13:54:23\",\"UpdateTime\":\"2022-05-11 11:03:26\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"14a3b845-f66e-2b5f-73a8-9332a98faa97\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"92\",\"ReqAvgUseMs\":\"604\",\"Id\":\"0c7ed8d4-0193-4216-be23-20c26751c3a6\",\"DatamodelQueryName\":\"今日客单价\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5IjoiU0VMRUNUIGF2Zyhgc291cmNlYC5gc3VtYCkgQVMgYGF2Z2BcclxuRlJPTSAoU0VMRUNUIGBvcmRlcl9vcmRlcmAuYGN1c3RvbWVyX2NhcmRgIEFTIGBjdXN0b21lcl9jYXJkYCwgc3VtKGBvcmRlcl9vcmRlcmAuYG9yZGVyX2Ftb3VudGApIEFTIGBzdW1gIEZST00gYG9yZGVyX29yZGVyYFxyXG5XSEVSRSAoYG9yZGVyX29yZGVyYC5gcGF5X3RpbWVgID49ZGF0ZShub3coNikpIGFuZCBgb3JkZXJfb3JkZXJgLmBwYXlfdGltZWAgPj1kYXRlKG5vdyg2KSkgYW5kIGBvcmRlcl9vcmRlcmAuYHBheV90aW1lYCA8IGRhdGVfYWRkKGRhdGUobm93KDYpKSwgSU5URVJWQUwgMSBkYXkpXHJcbiAgIEFORCAoYG9yZGVyX29yZGVyYC5gb3JkZXJfc3RhdHVzYCA9IDJcclxuICAgIE9SIGBvcmRlcl9vcmRlcmAuYG9yZGVyX3N0YXR1c2AgPSAzIE9SIGBvcmRlcl9vcmRlcmAuYG9yZGVyX3N0YXR1c2AgPSA5OSkpXHJcbkdST1VQIEJZIGBvcmRlcl9vcmRlcmAuYGN1c3RvbWVyX2NhcmRgXHJcbk9SREVSIEJZIGBvcmRlcl9vcmRlcmAuYGN1c3RvbWVyX2NhcmRgIEFTQykgYHNvdXJjZWAiLCJ0ZW1wbGF0ZS10YWdzIjp7fX0sImRhdGFiYXNlIjoxM30=\",\"QueryType\":\"native\",\"Description\":\"今日客单价\",\"Time\":\"2022-05-07 09:02:23\",\"UpdateTime\":\"2022-05-11 11:02:26\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"78\",\"ReqAvgUseMs\":\"8868\",\"Id\":\"a306141b-e71c-43fc-8c58-b0e72302e5c7\",\"DatamodelQueryName\":\"本月促销活动\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IGEuaWQsYS5vcmRlcl9zdGF0dXMsYS5vcmRlcl9tb250aCxiLm9yZGVyX2lkLFxyXG5jYXNlIFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J01BWUgyMjQxJyB0aGVuICfnvo7ogqTlpZfoo4UnIFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0pDMjAyMicgdGhlbiAn5rS75Yqb6IKg6ZO45aWX6KOFJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0FDQzIwMjInIHRoZW4gJ0FDQ+S8mOaDoOWll+ijhSdcclxuICBlbmQgYXMgJ+a0u+WKqOWQjeensCcsXHJcbnN1bShiLnF1YW50aXR5KSBhcyAn6ZSA5ZSu6YePJyxzdW0oYi50b3RhbF9wcmljZSkgYXMgJ+mUgOWUruminScsXHJcbmNhc2UgXHJcbiAgd2hlbiBiLnNlcmlhbF9ubz0nTUFZSDIyNDEnIHRoZW4gc3VtKGIucXVhbnRpdHkpLzM3MDAwIFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0pDMjAyMicgdGhlbiBzdW0oYi5xdWFudGl0eSkvMzc4MDBcclxuICB3aGVuIGIuc2VyaWFsX25vPSdBQ0MyMDIyJyB0aGVuIHN1bShiLnF1YW50aXR5KS8yNTAwMFxyXG4gIGVuZCBhcyAn55uu5qCH5a6M5oiQ546HJyBcclxuZnJvbVxyXG4oc2VsZWN0IGlkLG9yZGVyX3N0YXR1cyxvcmRlcl9tb250aCBmcm9tIG9yZGVyX29yZGVyKSBhXHJcbmpvaW5cclxuKHNlbGVjdCBvcmRlcl9pZCxzZXJpYWxfbm8scXVhbnRpdHksdG90YWxfcHJpY2UgZnJvbSBvcmRlcl9wcm9kdWN0KSBiXHJcbm9uIChhLmlkPWIub3JkZXJfaWQpXHJcbndoZXJlIGEub3JkZXJfc3RhdHVzIGluICgyLDMsOTkpIGFuZCBhLm9yZGVyX21vbnRoID0nMjAyMjA0JyBhbmQgYi5zZXJpYWxfbm8gaW4gKCdNQVlIMjI0MScsJ0pDMjAyMicsJ0FDQzIwMjInKVxyXG5ncm91cCBieSBiLnNlcmlhbF9ubyIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"统计本月促销产品的销售额\",\"Time\":\"2022-04-29 10:25:33\",\"UpdateTime\":\"2022-05-11 11:01:29\",\"UserId\":\"33d8d854-6a07-47b3-8326-c9403db7a430\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"00000000-0000-0000-0000-000000000000\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"87\",\"ReqAvgUseMs\":\"300\",\"Id\":\"e59be632-b047-4f64-9c38-85841253fae2\",\"DatamodelQueryName\":\"今日销售额（万元）\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTMsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NjYzLCJqb2lucyI6W10sImZpbHRlciI6W1sidGltZS1pbnRlcnZhbCIsWyJmaWVsZC1pZCIsMTEwNDBdLCJjdXJyZW50IiwiZGF5Il0sImFuZCIsWyI9IixbImZpZWxkLWlkIiwxMTA3Ml0sIjIwMjIwNSJdLCJhbmQiLFsiYnJhY2tldCIsW1siPSIsWyJmaWVsZC1pZCIsMTEwNzhdLCIyIl0sIm9yIixbIj0iLFsiZmllbGQtaWQiLDExMDc4XSwiMyJdLCJvciIsWyI9IixbImZpZWxkLWlkIiwxMTA3OF0sIjk5Il1dXV0sImFnZ3JlZ2F0aW9uIjpbWyJzdW0iLFsiZmllbGQtaWQiLDExMDQxXV1dfSwidHlwZSI6InF1ZXJ5IiwidmVyc2lvbiI6InBlcmZlY3QifQ==\",\"QueryType\":\"query\",\"Description\":\"今日销售额（万元）\",\"Time\":\"2022-05-06 15:55:28\",\"UpdateTime\":\"2022-05-11 11:01:29\",\"UserId\":\"cf8d3088-31ec-4230-bfcf-fcffa1fdd56c\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"67\",\"ReqAvgUseMs\":\"2118\",\"Id\":\"84bd12d7-299c-4691-ba63-ca7c17aa9633\",\"DatamodelQueryName\":\"testTodayMoney\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTMsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NjYzLCJqb2lucyI6W10sImZpbHRlciI6W1sidGltZS1pbnRlcnZhbCIsWyJmaWVsZC1pZCIsMTEwNDBdLCJjdXJyZW50IiwiZGF5Il0sImFuZCIsWyI9IixbImZpZWxkLWlkIiwxMTA3Ml0sIjIwMjIwNCJdLCJhbmQiLFsiPSIsWyJmaWVsZC1pZCIsMTEwNzhdLCIzIl1dLCJhZ2dyZWdhdGlvbiI6W1sic3VtIixbImZpZWxkLWlkIiwxMTA0MV1dXX0sInR5cGUiOiJxdWVyeSIsInZlcnNpb24iOiJwZXJmZWN0In0=\",\"QueryType\":\"\",\"Description\":\"今日业绩\",\"Time\":\"2022-04-07 11:54:39\",\"UpdateTime\":\"2022-05-11 11:00:04\",\"UserId\":\"33d8d854-6a07-47b3-8326-c9403db7a430\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"00000000-0000-0000-0000-000000000000\",\"Cache\":\"True\",\"CacheInterval\":\"10\"},{\"DatamodelQueryId\":\"97\",\"ReqAvgUseMs\":\"57913\",\"Id\":\"e33cceab-e648-49e4-9ab6-b0adc183ae3d\",\"DatamodelQueryName\":\"本年销售额\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IHN1bShvcmRlcl9hbW91bnQpIGZyb20gb3JkZXJfb3JkZXJcclxud2hlcmUgb3JkZXJfbW9udGg+PScyMDIyMDEnIGFuZCBvcmRlcl9zdGF0dXMgaW4oMiwzLDk5KSIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"本年销售额（sql有>号报错）\",\"Time\":\"2022-05-07 13:43:26\",\"UpdateTime\":\"2022-05-11 10:59:51\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"30\"},{\"DatamodelQueryId\":\"68\",\"ReqAvgUseMs\":\"2\",\"Id\":\"f536c67b-b1d1-4695-8392-64b0801264c2\",\"DatamodelQueryName\":\"liang_test\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MiwicXVlcnkiOnsic291cmNlLXRhYmxlIjoyfSwidHlwZSI6InF1ZXJ5IiwidmVyc2lvbiI6InBlcmZlY3QifQ==\",\"QueryType\":null,\"Description\":\"liang_testliang_testliang_testliang_test\",\"Time\":\"2022-04-08 14:59:49\",\"UpdateTime\":\"2022-05-10 15:40:07\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"False\",\"DatabaseId\":\"8edc4c89-88d1-a59d-8cb5-787489993b08\",\"GroupId\":\"00000000-0000-0000-0000-000000000000\",\"Cache\":\"True\",\"CacheInterval\":\"10\"}],\"RecordCount\":\"22\"}";
		this.PrintObject(aa);

		DefaultListDataSource2<List<DataModelInfo>> o3 = JSON.parseObject(aa,
				new TypeReference<DefaultListDataSource2<List<DataModelInfo>>>() {
				});

		this.PrintObject(o3);

	}

	public void testClassName() {
		Object o = new UserOrg();
		String a = o.getClass().toString();
		String b = o.getClass().getName();
		String c = o.getClass().getSimpleName();
		String d = o.getClass().getTypeName();
		System.out.println(a);
		System.out.println(b);
		System.out.println(c);
		System.out.println(d);
		/**
		 * 输出: class pf.java.pfHelper.model.UserOrg pf.java.pfHelper.model.UserOrg
		 * UserOrg pf.java.pfHelper.model.UserOrg
		 */
	}

	public void testClass() throws ClassNotFoundException{
		try {
			Class cls1 = Class.forName("org.sellgirlPayHelperNotSpring.model.DataModelInfo");
			Class cls2 = Class.forName("com.mysql.cj.jdbc.Driver");
			Class cls3 = Class.forName("com.mysql.jdbc.Driver");
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(true);
	}

	public void testCreateSqlTable() throws Exception {
		// 发现mysql8.0.23里,语句中有分号的号不能用execute方法

//		//clickhouse
//		String sql = "CREATE TABLE `user_profile_attr_all` ( `user_id` varchar(100) ,`tag_weight` varchar(100) ,`data_date` datetime ,`tag_id` varchar(100) \r\n"
//				+ "\r\n"
//				+ ",`theme` varchar(100)    ); CREATE INDEX  idx_user_id ON user_profile_attr_all (`user_id`); CREATE INDEX  idx_theme ON \r\n"
//				+ "\r\n" + "user_profile_attr_all (`theme`) ";
////		String sql="CREATE TABLE `user_profile_attr_all` ( `user_id` varchar(100) ,`tag_weight` varchar(100) ,`data_date` datetime ,`tag_id` varchar(100),`theme` varchar(100))";
////		String sql1="CREATE INDEX  idx_user_id ON user_profile_attr_all (`user_id`)";
////		String sql2="CREATE INDEX  idx_theme ON user_profile_attr_all (`theme`);";
//
//		IPFJdbc dstJdbc = JdbcHelperTest.GetUserProfileJdbc();
//		ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//		Object r = dstExec.ExecuteSql(new PFSqlCommandString(sql.split(";")));
//		assertTrue(r != null);

		// sqlserver
		String sql = "SELECT '2022.08' as cmonth, a.hybh,b.hyxm,SUM([qty]) as inv_qty\r\n"
				+ "  FROM [dbo].[sales] a\r\n" + "  left join hyzl b on b.hybh=a.hybh\r\n"
				+ "  where inv_no in('WP','WP101','WP102','WP1021','WP10211','WP1021407','WP1022','WP102218','WP181','WP182','XZWP1000','XZWP1021','XZWP1500','XZWP800','AW1701','AW182')\r\n"
				+ "  group by a.hybh,b.hyxm";

		ISGJdbc srcJdbc = JdbcHelperTest.GetYJQueryMonthJdbc("200801");
		ISGJdbc dstJdbc = JdbcHelperTest.GetBonusJdbc();

		SGSqlCreateTableCollection models = SGSqlCreateTableCollection.Init(dstJdbc);
		models.TableName = "tmp_hyzl";

		ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);
//		//建表
		ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);

		//models = dstExec.GetCreateTableSql(srcJdbc, sql, models, null);
		models = srcExec.GetCreateTableSql( sql, models, null);

		SGSqlCommandString sqlstr = models.ToSql();
		System.out.print(sqlstr);
		// dstExec.ExecuteSql(sqlstr);

////		//转数据
//			ISqlExecute srcExec = PFSqlExecute.Init(srcJdbc);
//			ResultSet dr=srcExec.GetDataReader(sql);
//
//			//ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//			boolean b2 =dstExec.HugeBulkReader(null,dr , models.TableName,
//		null, 
//		(already) -> {
//
//		}, null);

//		Class.forName("com.mysql.cj.jdbc.Driver");
//////      Class.forName("com.mysql.jdbc.Driver");
//////		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_demo?useSSL=false&serverTimezone=UTC","root","password");
////		//Connection conn = DriverManager.getConnection(dstJdbc.getUrl(),dstJdbc.getUsername(),dstJdbc.getPassword());//这样一样报错语法错误
//		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.205.111:3306/user_profile?useSSL=false&serverTimezone=UTC","root","perfect");
//		Statement stmt = conn.createStatement();
//        Boolean b=false;
//        
//        b=stmt.execute(sql);
//////         b=stmt.execute(sql1);
//////         b=stmt.execute(sql2);
//// 		stmt.addBatch(sql);
//// 		stmt.addBatch(sql1);
//// 		stmt.addBatch(sql2);
//// 		stmt.executeBatch();
//        assertTrue(b!=null);
	}

	public void testCreateMySqlTable() throws Exception {
		// 发现mysql8.0.23里,语句中有分号的号不能用execute方法
		// sqlserver
//		String sql = "" + 
//				"select order_month,count(distinct customer_card) as 'buyer_qty',\r\n" + 
//				"sum(order_amount) as order_amount\r\n" + 
//				"from mall_center_order.order_order\r\n" + 
//				"where order_month='202201' and order_status in (2,3,99)\r\n" + 
//				"group by order_month";
//		PFDataHelper.WriteLocalTxt("aaa","test01.txt", LocalDataType.Deletable);
//		return ;
		String sql = SGDataHelper.ReadLocalTxt("test01.txt", LocalDataType.Deletable);

		ISGJdbc srcJdbc = JdbcHelperTest.GetLiGeOrderJdbc();
		ISGJdbc dstJdbc = JdbcHelperTest.GetLiGeSaleJdbc();

		SGSqlCreateTableCollection models = SGSqlCreateTableCollection.Init(dstJdbc);
		models.TableName = "order_order_cur_month";

		ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);
//		//建表
		ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);

		//models = dstExec.GetCreateTableSql(srcJdbc, sql, models, null);
		models = srcExec.GetCreateTableSql( sql, models, null);

		SGSqlCommandString sqlstr = models.ToSql();
		System.out.print(sqlstr);
		dstExec.ExecuteSql(sqlstr);

//		//转数据
		//ISqlExecute srcExec = PFSqlExecute.Init(srcJdbc);
		ResultSet dr = srcExec.GetDataReader(sql);

		// ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
		boolean b2 = dstExec.HugeBulkReader(null, dr, models.TableName, null, (already) -> {

		}, null);

//		Class.forName("com.mysql.cj.jdbc.Driver");
//////      Class.forName("com.mysql.jdbc.Driver");
//////		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_demo?useSSL=false&serverTimezone=UTC","root","password");
////		//Connection conn = DriverManager.getConnection(dstJdbc.getUrl(),dstJdbc.getUsername(),dstJdbc.getPassword());//这样一样报错语法错误
//		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.205.111:3306/user_profile?useSSL=false&serverTimezone=UTC","root","perfect");
//		Statement stmt = conn.createStatement();
//        Boolean b=false;
//        
//        b=stmt.execute(sql);
//////         b=stmt.execute(sql1);
//////         b=stmt.execute(sql2);
//// 		stmt.addBatch(sql);
//// 		stmt.addBatch(sql1);
//// 		stmt.addBatch(sql2);
//// 		stmt.executeBatch();
//        assertTrue(b!=null);
	}

	public void testCreateClickHouseTable() throws Exception {
		SGDataHelper.SetConfigMapper(new PFConfigTestMapper());

		ISGJdbc srcJdbc = JdbcHelperTest.GetYJQueryJdbc();
		ISGJdbc dstJdbc = JdbcHelperTest.GetClickHouseShopJdbc();

		srcJdbc.setUrl(srcJdbc.getUrl().replace("yjquery", "yjquery202102"));

		// 发现mysql8.0.23里,语句中有分号的号不能用execute方法
//		String sql="SELECT top 100 \r\n" + 
//				"Cast('2020.12.01' as datetime) as data_date,\r\n" + 
//				" a.hybh as [user_id],\r\n" + 
//				" a.hyxb as [sex], \r\n" + 
//				" 1 as age,\r\n" + 
//				" b.inv_no as good_no,\r\n" + 
//				" b.qty as qty,\r\n" + 
//				" -- a.rhrq,\r\n" + 
//				" -- dateadd(m,3,a.rhrq),\r\n" + 
//				" (case when dateadd(m,3,a.rhrq)>GETDATE() then CONVERT(bit,1) else CONVERT(bit,0) end) as new_user,\r\n" + 
//				" DATEDIFF(m,a.rhrq,GETDATE()) as regist_month_total,\r\n" + 
//				" a.accmon as regist_month,\r\n" + 
//				" b.[code] as order_no\r\n" + 
//				"FROM hyzl a \r\n" + 
//				"inner join [sales] b   on b.hybh=a.hybh\r\n" + 
//				"" ;

//		String sql="SELECT  top 100 \r\n" + 
//				"Cast('2020.12.01' as datetime) as data_date,\r\n" + 
//				" a.hybh as [user_id],\r\n" + 
//				" a.hysfzh as id_card,\r\n" + 
//				" a.hyxb as [sex], \r\n" + 
//				" 1 as age,\r\n" + 
//				" b.inv_no as good_no,\r\n" + 
//				" b.qty as good_qty,\r\n" + 
//				" -- a.rhrq,\r\n" + 
//				" -- dateadd(m,3,a.rhrq),\r\n" + 
//				" (case when dateadd(m,3,a.rhrq)>GETDATE() then CONVERT(bit,1) else CONVERT(bit,0) end) as new_user,\r\n" + 
//				" DATEDIFF(m,a.rhrq,GETDATE()) as regist_month_total,\r\n" + 
//				" a.accmon as regist_month,\r\n" + 
//				" b.[code] as order_no\r\n" + 
//				"FROM hyzl a \r\n" + 
//				"inner join [sales] b   on b.hybh=a.hybh\r\n" + 
//				"" ;

		String sql = "SELECT top 100 \r\n" + "Cast('2021.04.01' as datetime) as data_date,\r\n"
				+ " hybh as user_id,\r\n" + " hyxb as sex \r\n" + "FROM hyzl WHERE qx=0";

		SGSqlCreateTableCollection models = SGSqlCreateTableCollection.Init(dstJdbc);
		models.DbName = "shop_data";
		models.TableName = "monthly_user2";
		// models.PrimaryKey = new String[] {"data_date","user_id"};
		// models.ClusteredIndex = new String[] {"toYYYYMM(data_date)"} ;
		models.Order = new String[] { "data_date", "user_id" };
		models.Partition = new String[] { "toYYYYMM(data_date)" };
		// models.TableIndex = new String[] {"good_no"};

		// ResultSet dr = srcExec.GetDataReader(sql);

		ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);
		ResultSet dr = srcExec.GetHugeDataReader(sql);

		if (dr == null) {
			return;
		}

		PFModelConfigCollection modelConfig = new PFModelConfigCollection();
		try {
			ResultSetMetaData md = dr.getMetaData();

			for (int i = 0; i < md.getColumnCount(); i++) {
				// String fieldName = md.getColumnName(i+1);
				String fieldName = md.getColumnLabel(i + 1);
				SqlCreateTableItem m = null;
				if (modelConfig.containsKey(fieldName)) {
//	           	 m=new SqlCreateTableItem(modelConfig.get(fieldName));
					m = new SqlCreateTableItem(modelConfig.Get(fieldName));
					m.FieldName = fieldName;// modelConfig里有可能是大小写不一样的,还是按dr的字段大小写来吧
				} else {
					m = new SqlCreateTableItem();
					m.FieldName = fieldName;
				}
				if (m.FieldType == null) {
					m.FieldType = SGDataHelper.GetTypeByString(md.getColumnTypeName(i + 1));
				}
//	            if (columnComment != null && columnComment.containsKey(m.FieldName))
//	            {
//	                m.FieldText = columnComment.get(m.FieldName);
//	            }
				// var updateItem = new SqlUpdateItem { Key = rdr.GetName(i), VType =
				// rdr.GetFieldType(i) };
				models.add(m);
			}
		} catch (Exception e) {

		}
		srcExec.CloseReader(dr);
		srcExec.CloseConn();

//	    String ms = null;
		SGSqlCommandString sqlStr = models.ToSql();
		System.out.print(sqlStr);

		// ISqlExecute dstExec = new PFClickHouseSqlExecute(dstJdbc);
		ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
		Connection conn = dstExec.GetConn();
//	     
		Statement stmt = conn.createStatement();
//	     //这样不行
////			stmt.addBatch(sqlStr.get(0));
////			stmt.addBatch(sqlStr.get(1));
////			int[] b=stmt.executeBatch();
//			
		try {
			// 方法1 报错
			boolean b = stmt.execute(sqlStr.toString());

//	    	//方法2 ok
//				for(String i :sqlStr) {
//					stmt.execute(i);
//				}

//				//方法3 执行成功, 但没生效
//				for(String i :sqlStr) {
//					stmt.addBatch(i);					
//				}
//				int[] b=stmt.executeBatch();
		} catch (Exception e) {
			System.err.print(e);
		}
		String aa = "aa";
		// stmt.setQueryTimeout(CommandTimeOut);

	}

//	public void testCreateClickHouseTable02() throws Exception {
//		PFDataHelper.SetConfigMapper(new PFConfigTestMapper());
//
//		//IPFJdbc srcJdbc = JdbcHelperTest.GetYJQueryJdbc();
//		IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTestJdbc();
//
//
//		PFSqlCreateTableCollection models = PFSqlCreateTableCollection.Init(dstJdbc);
//		models.DbName = "perfect_test";
//		models.TableName = "test_tb_01";
//		models.Order = new String[] { "data_date" };
//		SqlCreateTableItem item1=new SqlCreateTableItem();
//		item1.FieldName="data_date";
//		item1.FieldType=String.class;
//		models.add(item1);
//
//		
//		ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//		Connection conn = dstExec.GetConn();
////	     
//		dstExec.ExecuteSql(models.ToSql());
//
//	}
	public void testCreateHiveTable() throws Exception {
		// PFDataHelper.SetConfigMapper(new PFConfigTestMapper());
		initPFHelper();

		ISGJdbc srcJdbc = JdbcHelperTest.GetLiGeShopJdbc();
		// IPFJdbc dstJdbc = UncheckTest.GetHiveJdbc();

		String sql = "  select now() as data_date, card_no\r\n"
				+ " from mall_center_member.member_info where card_no is not null and card_no <>'' limit 10";

		SGSqlCreateTableCollection models = new PFHiveSqlCreateTableCollection();
		models.DbName = "wm_hive_db";
		models.TableName = "shop_user1";
		// models.PrimaryKey = new String[] {"data_date","user_id"};
		// models.ClusteredIndex = new String[] {"toYYYYMM(data_date)"} ;
		// models.Order = new String[] { "data_date", "user_id" };
		models.Partition = new String[] { "data_date" };
		// models.TableIndex = new String[] {"good_no"};

		// ResultSet dr = srcExec.GetDataReader(sql);

		ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);
		ResultSet dr = srcExec.GetHugeDataReader(sql);

		if (dr == null) {
			return;
		}

		PFModelConfigCollection modelConfig = new PFModelConfigCollection();
		try {
			ResultSetMetaData md = dr.getMetaData();

			for (int i = 0; i < md.getColumnCount(); i++) {
				// String fieldName = md.getColumnName(i+1);
				String fieldName = md.getColumnLabel(i + 1);
				SqlCreateTableItem m = null;
				if (modelConfig.containsKey(fieldName)) {
//	           	 m=new SqlCreateTableItem(modelConfig.get(fieldName));
					m = new SqlCreateTableItem(modelConfig.Get(fieldName));
					m.FieldName = fieldName;// modelConfig里有可能是大小写不一样的,还是按dr的字段大小写来吧
				} else {
					m = new SqlCreateTableItem();
					m.FieldName = fieldName;
				}
				if (m.FieldType == null) {
					m.FieldType = SGDataHelper.GetTypeByString(md.getColumnTypeName(i + 1));
				}
//	            if (columnComment != null && columnComment.containsKey(m.FieldName))
//	            {
//	                m.FieldText = columnComment.get(m.FieldName);
//	            }
				// var updateItem = new SqlUpdateItem { Key = rdr.GetName(i), VType =
				// rdr.GetFieldType(i) };
				models.add(m);
			}
		} catch (Exception e) {

		}
		srcExec.CloseReader(dr);
		srcExec.CloseConn();

		SGSqlCommandString sqlStr = models.ToSql();
		System.out.print(sqlStr);

	}

	public void testCreateSqlserverTable() throws Exception {
//		PFDataHelper.SetConfigMapper(new PFConfigTestMapper());
		initPFHelper();

		// DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
		ISGJdbc srcJdbc = JdbcHelperTest.GetSellGirlJdbc();
		ISGJdbc dstJdbc = JdbcHelperTest.GetSellGirlJdbc();

		String sql = "  select * from [news_reply]";

		SGSqlCreateTableCollection models = SGSqlCreateTableCollection.Init(dstJdbc);
		// models.DbName = "wm_hive_db";
		models.TableName = "news_reply";
		// models.PrimaryKey = new String[] {"data_date","user_id"};
		// models.ClusteredIndex = new String[] {"toYYYYMM(data_date)"} ;
		// models.Order = new String[] { "data_date", "user_id" };
		// models.Partition = new String[] { "data_date" };
		// models.TableIndex = new String[] {"good_no"};

		// ResultSet dr = srcExec.GetDataReader(sql);

		 ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);
		ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);

		//models = dstExec.GetCreateTableSql(srcJdbc, sql, models, null);
		models = srcExec.GetCreateTableSql( sql, models, null);
//		.ToSql();
//		ResultSet dr = srcExec.GetHugeDataReader(sql);
//
//		if (dr == null) {
//			return;
//		}
//
//		PFModelConfigCollection modelConfig = new PFModelConfigCollection();
//		try {
//			ResultSetMetaData md = dr.getMetaData();
//
//			for (int i = 0; i < md.getColumnCount(); i++) {
//				// String fieldName = md.getColumnName(i+1);
//				String fieldName = md.getColumnLabel(i + 1);
//				SqlCreateTableItem m = null;
//				if (modelConfig.containsKey(fieldName)) {
////	           	 m=new SqlCreateTableItem(modelConfig.get(fieldName));
//					m = new SqlCreateTableItem(modelConfig.Get(fieldName));
//					m.FieldName = fieldName;// modelConfig里有可能是大小写不一样的,还是按dr的字段大小写来吧
//				} else {
//					m = new SqlCreateTableItem();
//					m.FieldName = fieldName;
//				}
//				if (m.FieldType == null) {
//					m.FieldType = PFDataHelper.GetTypeByString(md.getColumnTypeName(i + 1));
//				}
////	            if (columnComment != null && columnComment.containsKey(m.FieldName))
////	            {
////	                m.FieldText = columnComment.get(m.FieldName);
////	            }
//				// var updateItem = new SqlUpdateItem { Key = rdr.GetName(i), VType =
//				// rdr.GetFieldType(i) };
//				models.add(m);
//			}
//		} catch (Exception e) {
//
//		}
//		srcExec.CloseReader(dr);
//		srcExec.CloseConn();

		SGSqlCommandString sqlStr = models.ToSql();
		System.out.print(sqlStr);

	}

	public void testCreateTidbTable() throws Exception {
		initPFHelper();

		ISGJdbc srcJdbc = JdbcHelperTest.GetTidbSaleJdbc();
		ISGJdbc dstJdbc = JdbcHelperTest.GetTidbSaleJdbc();

		String sql = "SELECT  \r\n" + "STR_TO_DATE('2021.10.01','%Y.%m.%d') as data_date,\r\n"
				+ " a.hybh as user_id,\r\n" + " a.hysfzh as id_card,\r\n" + " a.sex as sex, \r\n" + " a.Age as age,\r\n"
				+ " d.Inv_no as good_no,\r\n" + " sum(d.qty) as good_qty,\r\n" + " c.is_new_hy as new_user,\r\n"
				+ " -- DATEDIFF(m,a.rhrq,GETDATE()) as regist_month_total,\r\n" + " c.accmon as regist_month,\r\n"
				+ " b.Orderno as order_no\r\n" + "FROM monthly_order_2021 b\r\n"
				+ "inner join monthly_orderdetail_2021 d on d.cmonth='2021.10' and d.Orderno=b.Orderno\r\n"
				+ "inner join monthly_hyzl_2021 c on c.create_date='2021-10-01 00:00:00' and c.hybh=b.hybh\r\n"
				+ "inner join hyzl a on b.hybh=a.hybh\r\n"
				+ "where b.create_date='2021-10-01 00:00:00' -- b.cmonth='2021.10'\r\n" + "group by \r\n"
				+ " a.hybh ,\r\n" + " a.hysfzh ,\r\n" + " -- a.hyxb as [sex], 无\r\n" + " -- 1 as age,\r\n"
				+ " d.Inv_no ,\r\n" + " -- d.qty ,\r\n" + " c.is_new_hy,\r\n"
				+ " -- DATEDIFF(m,a.rhrq,GETDATE()) as regist_month_total,\r\n" + " c.accmon ,\r\n" + " b.Orderno \r\n"
				+ "limit 10";

		SGSqlCreateTableCollection models = SGSqlCreateTableCollection.Init(dstJdbc);
		models.TableName = "monthly_user_sale2";
		// models.PrimaryKey = new String[] {"data_date","user_id"};
		// models.ClusteredIndex = new String[] {"toYYYYMM(data_date)"} ;
		// models.Order = new String[] { "data_date", "user_id" };
		// models.Partition = new String[] { "data_date" };
		// models.TableIndex = new String[] {"good_no"};

		// ResultSet dr = srcExec.GetDataReader(sql);

		 ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);
		ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);

		//models = dstExec.GetCreateTableSql(srcJdbc, sql, models, null);
		models = srcExec.GetCreateTableSql( sql, models, null);
//		.ToSql();
//		ResultSet dr = srcExec.GetHugeDataReader(sql);
//
//		if (dr == null) {
//			return;
//		}
//
//		PFModelConfigCollection modelConfig = new PFModelConfigCollection();
//		try {
//			ResultSetMetaData md = dr.getMetaData();
//
//			for (int i = 0; i < md.getColumnCount(); i++) {
//				// String fieldName = md.getColumnName(i+1);
//				String fieldName = md.getColumnLabel(i + 1);
//				SqlCreateTableItem m = null;
//				if (modelConfig.containsKey(fieldName)) {
////	           	 m=new SqlCreateTableItem(modelConfig.get(fieldName));
//					m = new SqlCreateTableItem(modelConfig.Get(fieldName));
//					m.FieldName = fieldName;// modelConfig里有可能是大小写不一样的,还是按dr的字段大小写来吧
//				} else {
//					m = new SqlCreateTableItem();
//					m.FieldName = fieldName;
//				}
//				if (m.FieldType == null) {
//					m.FieldType = PFDataHelper.GetTypeByString(md.getColumnTypeName(i + 1));
//				}
////	            if (columnComment != null && columnComment.containsKey(m.FieldName))
////	            {
////	                m.FieldText = columnComment.get(m.FieldName);
////	            }
//				// var updateItem = new SqlUpdateItem { Key = rdr.GetName(i), VType =
//				// rdr.GetFieldType(i) };
//				models.add(m);
//			}
//		} catch (Exception e) {
//
//		}
//		srcExec.CloseReader(dr);
//		srcExec.CloseConn();

		SGSqlCommandString sqlStr = models.ToSql();
		System.out.print(sqlStr);

	}

	public void testAddMySqlColumn() throws Exception {

		// IPFJdbc dstJdbc = JdbcHelperTest.GetBonusJdbc();
		ISGJdbc dstJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();

//		PFSqlCreateTableCollection models =PFSqlCreateTableCollection.Init(dstJdbc);
//		models.TableName = "order_order_cur_month";

//		//建表
		ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
		PFModelConfig model = new PFModelConfig();
		model.FieldName = "pf_row_cnt";
		model.FieldType = long.class;
		// dstExec.addColumn("test_tb_10", "pf_row_cnt", PFSqlFieldType.Int);
		dstExec.addColumn("test_tb_10", model);
	}

	public void testClickHouseDelete() throws Exception {
		SGDataHelper.SetConfigMapper(new PFConfigTestMapper());

		ISGJdbc srcJdbc = JdbcHelperTest.GetYJQueryJdbc();
		// IPFJdbc dstJdbc=UncheckTest.GetClickHouseShopJdbc();
		ISGJdbc dstJdbc = JdbcHelperTest.GetUserProfileJdbc();

		srcJdbc.setUrl(srcJdbc.getUrl().replace("yjquery", "yjquery202102"));

		String sql = "ALTER TABLE  user_profile_attr_all  ON CLUSTER default_cluster delete  where  `data_date`='2021-04-21 00:00:00' ";

		ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
		Connection conn = dstExec.GetConn();
//	     
		Statement stmt = conn.createStatement();

		try {
			// 方法1 报错
			boolean b = stmt.execute(sql);

//	    	//方法2 ok
//				for(String i :sqlStr) {
//					stmt.execute(i);
//				}

//				//方法3 执行成功, 但没生效
//				for(String i :sqlStr) {
//					stmt.addBatch(i);					
//				}
//				int[] b=stmt.executeBatch();
		} catch (Exception e) {
			System.err.print(e);
		}
		String aa = "aa";
		// stmt.setQueryTimeout(CommandTimeOut);

	}

	public void testMySqlVersion() throws Exception {

//	     String url = "jdbc:mysql://rm-wz90v9vru7d524b3hwo.mysql.rds.aliyuncs.com/mall_center_product?connectTimeout=20000";
		// ok
		String url = "jdbc:mysql://dzqv2n74qe5gzdn7ol10-rw4rm.rwlb.rds.aliyuncs.com/mall_center_product?useUnicode=true&characterEncoding=utf8&connectTimeout=20000&useSSL=false";
		// String url =
		// "jdbc:mysql://dzqv2n74qe5gzdn7ol10-rw4rm.rwlb.rds.aliyuncs.com/mall_center_product?useUnicode=true&characterEncoding=utf8&connectTimeout=20000&useSSL=false";
		String user = "perfectmall";
		String password = "lige@2020_lige%2020";
		Connection conn = DriverManager.getConnection(url, user, password);
		boolean b = false;
		assertTrue(b == true);
	}

	public void testSysType() throws Exception {

		assertTrue(SysType.Windows == SGDataHelper.GetSysType());
	}

	public void testException() {
		initPFHelper();
//		try {
//			String aa="aa";
//			throw new Exception("aa");
//		}catch(Exception e) {
////			//e.printStackTrace();
//			PFDataHelper.WriteError(new Throwable() , e);
//			
//
////			Throwable trace = e.fillInStackTrace();
////			if (trace == null) {
////				DoWriteLog("PFDataHelper.WriteError()待优化,trace为null", "pfError");
////			} else {
////				trace.printStackTrace(printWriter);
////				s = result.toString();
////			}
//		}
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		try {
			throw new SQLSyntaxErrorException("aaa");
		} catch (SQLSyntaxErrorException e) {
//			e.printStackTrace();
			System.out.println("111");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("222");
		}
	}
	public void testException2() {
		try {
			try {
				throw new Exception("aa");
				//String aa="aa";//不可达
			} finally {
				String aa="aa";
			}
			//String aa="aa";//不可达
		} catch (Exception e){
			String aa="aa";
		}finally {
			String aa="aa";
		}
	}

	public void testJdbc() throws Exception {

		// IPFJdbc dstJdbc=JdbcHelper.GetTidbSale();
		initPFHelper();
		ISGJdbc dstJdbc = JdbcHelperTest.GetTidbSaleJdbc();
		ISqlExecute sqlExec = SGSqlExecute.Init(dstJdbc);
//     	sqlExec.HugeUpdate("update monthly_hyzl_2020 set is_new_hy_b=\r\n" + 
//     			"case   \r\n" + 
//     			"  when is_new_hy='true' then b'1'\r\n" + 
//     			"  when is_new_hy='1' then b'1' \r\n" + 
//     			"  else b'0'\r\n" + 
//     			"end\r\n" + 
//     			"where is_new_hy_b is null");

		// sqlExec.HugeUpdate("update monthly_hyzl_2020 set is_new_hy=null where
		// is_new_hy is not null");
		Object aa = sqlExec.QuerySingleValue("select 1 as aa");
		String bb = "aa";
		String cc = bb;
	}

	public void testSqlUrl() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String url = "jdbc:sqlserver://192.168.0.29:1433;DatabaseName=bonus;user=sa;password=perfect";
			Connection conn = DriverManager.getConnection(url);

			System.out.println(conn.isClosed());
			conn.close();
			System.out.println(conn.isClosed());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testIntervalTask() {
		int[] cnt = new int[] { 0 };
		PFIntervalTask task = new PFIntervalTask("IntervalTask001", (t) -> {
			System.out.println(cnt[0]);
			System.out.println(SGDate.Now().toString());
			cnt[0]++;
			return true;
		}, 0.15);
		task.Start();
		while (cnt[0] < 10) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void testIntervalTask2() {
		int[] cnt = new int[] { 0 };
		PFIntervalTask task = new PFIntervalTask("IntervalTask002", (t) -> {
			System.out.println(cnt[0]);
			cnt[0]++;
			return true;
		}, 1);
		task.Start();
		while (cnt[0] < 10) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void testApi() {
		int[] cnt = new int[] { 0 };
//		SGDataHelper.HttpPost("https://uc.perfect99.com/api/esb/api/member/bank/batchDeleteSellerBank",
//				"{\"cardNos\":[\"00001374\"]}");
		SGHttpHelper.HttpPost("https://uc.perfect99.com/api/esb/api/member/bank/batchDeleteSellerBank",
				"{\"cardNos\":[\"00001374\"]}");
	}

	public void testIDCard() {
		List<Pair<String, String>> cards = new ArrayList<Pair<String, String>>();
		cards.add(ImmutablePair.of("440620195702090710", "男"));
		cards.add(ImmutablePair.of("440701196408200019", "男"));
		cards.add(ImmutablePair.of("440702197002040917", "男"));
		cards.add(ImmutablePair.of("440701192104280912", "男"));
		cards.add(ImmutablePair.of("440701196001120017", "男"));
		cards.add(ImmutablePair.of("422801197412190026", "女"));
		cards.add(ImmutablePair.of("422801197001044024", "女"));
		cards.add(ImmutablePair.of("440620195311290547", "女"));
		cards.add(ImmutablePair.of("440620196511143685", "女"));
		cards.add(ImmutablePair.of("44062019620418054X", "女"));
		cards.add(ImmutablePair.of("530103195304032927", "女"));
		for (int i = 0; i < cards.size(); i++) {
			Pair<String, String> card = cards.get(i);
			String sex = SGDataHelper.IDCardToSex(card.getKey());
			assertTrue(sex.equals(card.getValue()));
		}
	}

//	private <T1> void PrintObject(T1 o) {
//	System.out.println(JSON.toJSONString(o));
//}
	private <T1> void PrintObject(T1 o) {
		// System.out.println(JSON.toJSONString(o));
		// System.out.println("--------------");
		String s = JSON.toJSONString(o);
		String[] sa = s.split("\\\\r\\\\n");
		for (String i : sa) {
			System.out.println(i.replaceAll("\\\\t", "  "));
		}
		// System.out.println("--------------");
	}
//	private <T1> void PrintObjects(T1... lists) {
//		System.out.println("--------------");
//		for(T1 o:lists) {
//			String s=JSON.toJSONString(o);
//			String[] sa=s.split("\\\\r\\\\n");
//			for(String i : sa) {
//				System.out.println(i.replaceAll("\\\\t", "  "));
//			}
//		}
//		System.out.println("--------------");
//	}

	private void eachDirectNode(DirectNode n1, SGAction<DirectNode, Object, Object> action,
			List<DirectNode> processedList) {
		action.go(n1, null, null);
		if (processedList == null) {
			processedList = new ArrayList<DirectNode>();
		}
		processedList.add(n1);
		if (n1.next != null && !n1.next.isEmpty()) {
			for (DirectNode i : n1.next) {
				if (!processedList.contains(i)) {
					eachDirectNode(i, action, processedList);
				}
			}
		}
	}

	private void checkDirectNode(DirectNode n1) {
		eachDirectNode(n1, (a, b, c) -> {
//		a.finish = true;
//		a.setFinishedPercent(100);
//		System.out.print(a.getHashId());

			SGAction<DirectNode, Integer, Object> ac = (d, e, f) -> {
//				d.finish = true;
//				d.setFinishedPercent(100);
				System.out.print(a.getHashId() + "->");

			};
			a.action = (g, h, i) -> {
				ac.go(g, null, null);
				return true;
			};

		}, null);

		System.out.print("\r\n");
		PrintObject(n1.getAllNodeFinishedPercent());
		n1.GoSync(null);

		System.out.print("\r\n");
		PrintObject(n1.getAllNodeFinishedPercent());
	}

	public void testDirectNode() {
		/**
		 * 1 ->2->3 ->5 ->6 ->8 ->4 ->7->9 142357968
		 */
		DirectNode n1 = new DirectNode("1");
		DirectNode n2 = new DirectNode("2");
		DirectNode n3 = new DirectNode("3");
		DirectNode n4 = new DirectNode("4");
		DirectNode n5 = new DirectNode("5");
		DirectNode n6 = new DirectNode("6");
		DirectNode n7 = new DirectNode("7");
		DirectNode n8 = new DirectNode("8");
		DirectNode n9 = new DirectNode("9");
//		n1.next=new ArrayList() {{add(n2);add(n3);}};
//		n2.next=new ArrayList() {{add(n2);add(n3);}};
		n1.addNext(n2, n4);
		n2.addNext(n3);
		n3.addNext(n5);
		n4.addNext(n5);
		n5.addNext(n6, n7);
		// n6.addNext(n8);
		n7.addNext(n9);
		// n9.addNext(n8);
		n8.addPrev(n6, n9);

		checkDirectNode(n1);

//	PFAction<DirectNode, Integer, Object> ac = (a, b, c) -> {
//		a.finish = true;
//		a.setFinishedPercent(100);
//		System.out.print(a.getHashId());
//
//	};
//	n1.action = a -> ac.go(a, null, null);
//	n2.action = a -> ac.go(a, null, null);
//	n3.action = a -> ac.go(a, null, null);
//	n4.action = a -> ac.go(a, null, null);
//	n5.action = a -> ac.go(a, null, null);
//	n6.action = a -> ac.go(a, null, null);
//	n7.action = a -> ac.go(a, null, null);
//	n8.action = a -> ac.go(a, null, null);
//	n9.action = a -> ac.go(a, null, null);
//
//	System.out.print("\r\n");
//	PrintObject(n1.getAllNodeFinishedPercent());
//	n1.GoSync(null);
//
//	System.out.print("\r\n");
//	PrintObject(n1.getAllNodeFinishedPercent());
////	try {
////		while (!n8.finish) {
////			Thread.sleep(1000);
////			System.out.print("\r\n");
////			PrintObject(n1.getAllNodeFinishedPercent());
////		}
////	} catch (InterruptedException e) {
////		// TODO Auto-generated catch block
////		e.printStackTrace();
////	}
////	System.out.print("\r\n");
////	PrintObject(n1.getAllNodeFinishedPercent());

	}

	public void testDirectNode02() {
		/**
		 * 1 ->2->3 ->5 ->6 ->8 ->4 ->7->9 142357968
		 */
		DirectNode n0 = new DirectNode("0");
		DirectNode n1 = new DirectNode("1");
		DirectNode n2 = new DirectNode("2");
		DirectNode n3 = new DirectNode("3");
		DirectNode n4 = new DirectNode("4");
		DirectNode n5 = new DirectNode("5");
		DirectNode n6 = new DirectNode("6");
		DirectNode n7 = new DirectNode("7");
		DirectNode n8 = new DirectNode("8");
		DirectNode n99 = new DirectNode("99");

		n0.addNext(n1, n2, n3, n5);
		n4.addPrev(n2, n3);
		n6.addPrev(n3, n5);// n6还用到prod表,但prod表好像不是每天更新的,prod表可能需要改为每天更新--benjamin todo
		n7.addPrev(n2);
		n8.addPrev(n1, n2, n3, n5);
		n99.addPrev(n1, n4, n5, n6, n7, n8);

		checkDirectNode(n0);
	}

	public void testDirectNode03() {
		/**
		 * 1 ->2->3 ->5 ->6 ->8 ->4 ->7->9 142357968
		 */
		DirectNode n0 = new DirectNode("0");
		DirectNode n1 = new DirectNode("1");
		DirectNode n2 = new DirectNode("2");
		// DirectNode n3 = new DirectNode("3");
		DirectNode n4 = new DirectNode("4");
		// DirectNode n5 = new DirectNode("5");
		// DirectNode n6 = new DirectNode("6");
		DirectNode n7 = new DirectNode("7");
		DirectNode n8 = new DirectNode("8");
		DirectNode n9 = new DirectNode("9");
		DirectNode n10 = new DirectNode("10");
		DirectNode n99 = new DirectNode("99");

		n0.addNext(n1, n2// ,n3,n5
		// ,n6
		);
		n4.addPrev(n2// ,n3
		);
		// n6.addPrev(n3,n5);//n6还用到prod表,但prod表好像不是每天更新的,prod表可能需要改为每天更新--benjamin
		n7.addPrev(n2);
		n10.addPrev(n2);
		n8.addPrev(n1, n2// ,n3,n5
		);
		n9.addPrev(n1);

		n99.addPrev(n1, n4, // n5,
				// n6,
				n7, n8, n9, n10);

		checkDirectNode(n0);
	}

	/**
	 * 测试多次prev时为什么不会重复执行
	 */
	public void testDirectNode04() {
		/**
		 * 1 ->2->3 ->5 ->6 ->8 ->4 ->7->9 142357968
		 */
		DirectNode n0 = new DirectNode("0");
		DirectNode n1 = new DirectNode("1");
		DirectNode n2 = new DirectNode("2");
		DirectNode n3 = new DirectNode("3");

		n1.action = (g, h, i) -> {
			Thread.sleep(1000);
			System.out.print(g.getHashId() + "->");
			return true;
		};
		n2.action = (g, h, i) -> {
			Thread.sleep(4000);
			System.out.print(g.getHashId() + "->");
			return true;
		};
		n3.action = (g, h, i) -> {
			Thread.sleep(1000);
			System.out.print(g.getHashId() + "->");
			return true;
		};

		n0.addNext(n1, n2// ,n3,n5
				// ,n6
		);
		n3.addPrev(n1,n2// ,n3
		);

		//checkDirectNode(n0);
		n0.GoSync(null);
	}

	public void testPFIntervalExactTask() {
		IPFTask _intervalRedoFailedTask = new PFIntervalExactTask("PFIntervalExactTask001",
				(cDay, lastBackupTime, task) -> {
//			//List<String> tasks=
//	 	 	   if(t1.isFailed()) {
//	  			  t1.RedoFailed();
//	  		   }
					this.PrintObject(cDay);
					return true;
				}, 1, SGDate.Now()// .AddMinutes(0)
				, true, null);
		_intervalRedoFailedTask.Start();
		try {
			while (true) {
				Thread.sleep(2000);
			}
			// Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testPFIntervalExactTask2() {
		Uncheck001.initPFHelper();
		IPFTask _intervalRedoFailedTask = new PFIntervalExactTask2("PFIntervalExactTask001",
				(cDay, lastBackupTime, task) -> {
//			//List<String> tasks=
//	 	 	   if(t1.isFailed()) {
//	  			  t1.RedoFailed();
//	  		   }
					this.PrintObject(cDay.toString());
					return true;
				}, // .AddMinutes(0)
				TimePoint.every, 0, 0, 3, true, null);
		_intervalRedoFailedTask.Start();
		try {
			while (true) {
				Thread.sleep(2000);
			}
			// Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testDayTask() throws Exception {
		initPFHelper();
		boolean[] b = new boolean[] { false, false, false };
		SGDate start = SGDate.Now();
		this.PrintObject("start:" + start.toString());
		SGDate point = start.AddMinutes(1);
		int housr = point.GetHour();
//		int min=now.GetMinute();
		int min = point.GetMinute();
		int[] cnt = new int[] { 1 };

		PFDayTask t1 = new PFDayTask("DayHyDataTest", (cDay, lastBackupTime, task) -> {
//			if(cnt[0]>0) {
//				this.PrintObject("执行失败:"+String.valueOf(cnt[0]));
//				cnt[0]--;
//				return false;
//			}else {
//				this.PrintObject("执行成功:"+String.valueOf(cnt[0]));
//				return true;
//			}
			this.PrintObject("执行成功:" + cDay);
			b[0] = true;
			return true;
		}, housr, min, // 每月11日3时执行
				false, null);

		t1.Start();

		try {
			while (!b[0]) {
				Thread.sleep(2000);
			}
			SGDate now = SGDate.Now();
			this.PrintObject("now:" + now.toString());
			this.PrintObject("point:" + point.toString());
			// int cmp=now.compareTo(point);
			long cmp = now.toTimestamp() - point.toTimestamp();
			this.PrintObject("compare:" + cmp);
			if (cmp < -60000) {// 定时器是精确到分
				throw new Exception("PFDayTask2异常,未到时间就执行了");
			}
			// Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 有问题,未到时间也执行了
	 * 
	 * @throws Exception
	 */
	public void testDayTask2() throws Exception {
		initPFHelper();
		boolean[] b = new boolean[] { false, false, false };
		SGDate start = SGDate.Now();
		this.PrintObject("start:" + start.toString());
		SGDate point = start.AddMinutes(1);
		int housr = point.GetHour();
//		int min=now.GetMinute();
		int min = point.GetMinute();
		int[] cnt = new int[] { 1 };

		PFDayTask2 t1 = new PFDayTask2("DayHyDataTest", (cDay, lastBackupTime, task) -> {
//			if(cnt[0]>0) {
//				this.PrintObject("执行失败:"+String.valueOf(cnt[0]));
//				cnt[0]--;
//				return false;
//			}else {
//				this.PrintObject("执行成功:"+String.valueOf(cnt[0]));
//				return true;
//			}
			this.PrintObject("执行成功:" + cDay);
			b[0] = true;
			return true;
		}, housr, min, // 每月11日3时执行
				false, null);

		t1.Start();

		try {
			while (!b[0]) {
				Thread.sleep(2000);
			}
			SGDate now = SGDate.Now();
			this.PrintObject("now:" + now.toString());
			this.PrintObject("point:" + point.toString());
			// int cmp=now.compareTo(point);
			long cmp = now.toTimestamp() - point.toTimestamp();
			this.PrintObject("compare:" + cmp);
			if (cmp < -60000) {// 定时器是精确到分
				throw new Exception("PFDayTask2异常,未到时间就执行了");
			}
			// Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testMonthTask() {
		initPFHelper();
		boolean[] b = new boolean[] { false, false, false };
		SGDate now = SGDate.Now().AddMinutes(-1);
		// PFDate now1=now.AddMinutes(1);
		int day = now.GetDay();
		int housr = now.GetHour();
//		int min=now.GetMinute();
		int min = now.GetMinute();
		int[] cnt = new int[] { 1 };
		PFMonthTask t1 = new PFMonthTask("PFMonthTaskTest", (cDay, lastBackupTime, task) -> {
//			if(cnt[0]>0) {
//				this.PrintObject("执行失败:"+String.valueOf(cnt[0]));
//				cnt[0]--;
//				return false;
//			}else {
//				this.PrintObject("执行成功:"+String.valueOf(cnt[0]));
//				return true;
//			}
			this.PrintObject("执行成功:" + cDay);
			return true;
		}, day, housr, min, // 每月11日3时执行
				false, null);

		t1.Start();

		try {
			while (true) {
				Thread.sleep(2000);
			}
			// Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testMonthTask2() {
		initPFHelper();
		boolean[] b = new boolean[] { false, false, false };
		SGDate now = SGDate.Now().AddMinutes(-1);
		// PFDate now1=now.AddMinutes(1);
		int day = now.GetDay();
		int housr = now.GetHour();
//		int min=now.GetMinute();
		int min = now.GetMinute();
		int[] cnt = new int[] { 1 };
		PFMonthTask2 t1 = new PFMonthTask2("PFMonthTask2Test", (cDay, lastBackupTime, task) -> {
//			if(cnt[0]>0) {
//				this.PrintObject("执行失败:"+String.valueOf(cnt[0]));
//				cnt[0]--;
//				return false;
//			}else {
//				this.PrintObject("执行成功:"+String.valueOf(cnt[0]));
//				return true;
//			}
			this.PrintObject("执行成功:" + cDay);
			return true;
		}, day, housr, min, // 每月11日3时执行
				false, null);

		t1.Start();

		try {
			while (true) {
				Thread.sleep(2000);
			}
			// Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testTimeTaskRetry() {
		initPFHelper();
		boolean[] b = new boolean[] { false, false, false };
		SGDate now = SGDate.Now().AddMinutes(-1);
		SGDate now1 = now.AddMinutes(1);
		int housr = now.GetHour();
		int min = now.GetMinute();
		int[] cnt = new int[] { 1 };
		PFDayTask t1 = new PFDayTask("DayHyDataTest", (cDay, lastBackupTime, task) -> {
			if (cnt[0] > 0) {
				this.PrintObject("执行失败:" + String.valueOf(cnt[0]));
				cnt[0]--;
				return false;
			} else {
				this.PrintObject("执行成功:" + String.valueOf(cnt[0]));
				return true;
			}
		}, housr, min, // 每月11日3时执行
				false, null);

		t1.Start();

		IPFTask _intervalRedoFailedTask = new PFIntervalTask("IntervalTask001", (t) -> {
			// List<String> tasks=
			if (t1.isFailed()) {
				t1.RedoFailed();
			}
			return true;
		}, 1);
		_intervalRedoFailedTask.Start();
		try {
			while (true) {
				Thread.sleep(2000);
			}
			// Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testTimeTask2Retry() {
		initPFHelper();
		boolean[] b = new boolean[] { false, false, false };
		SGDate now = SGDate.Now().AddMinutes(-1);
		SGDate now1 = now.AddMinutes(1);
		int housr = now.GetHour();
		int min = now.GetMinute();
		int[] cnt = new int[] { 1 };
		PFDayTask2 t1 = new PFDayTask2("DayHyDataTest", (cDay, lastBackupTime, task) -> {
			if (cnt[0] > 0) {
				this.PrintObject("执行失败:" + String.valueOf(cnt[0]));
				cnt[0]--;
				return false;
			} else {
				this.PrintObject("执行成功:" + String.valueOf(cnt[0]));
				return true;
			}
		}, housr, min, // 每月11日3时执行
				false, null);

		t1.Start();

		IPFTask _intervalRedoFailedTask = new PFIntervalTask("IntervalTask001", (t) -> {
			// List<String> tasks=
			if (t1.isFailed()) {
				t1.RedoFailed();
			}
			return true;
		}, 1);
		_intervalRedoFailedTask.Start();
		try {
			while (true) {
				Thread.sleep(2000);
			}
			// Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 测试通过
	 */
	public void testTransferTaskBase() {
//		IPFJdbc srcJdbc = JdbcHelperTest.GetTidbSaleJdbc();
//		IPFJdbc dstJdbc = JdbcHelperTest.GetTidbSaleJdbc();
//		//IPFJdbc dstJdbc = JdbcHelperTest.GetLiGeShopJdbc();

		// IPFJdbc srcJdbc = JdbcHelperTest.GetSellGirlJdbc2();
		ISGJdbc srcJdbc = JdbcHelperTest.GetLiGeOrderJdbc();

		ISGJdbc dstJdbc = JdbcHelperTest.GetMySqlTestJdbc();

		String dstTableName = "order_order";
//		String sql="select 2 as col1";
//		String sql="select deposit_pay_type,group_order_id,order_deposit from mall_center_order.order_order where id=1270744013518428510";
		String sql = "select *  from mall_center_order.order_order where id=1270744013518428510";

		ISqlExecute srcExec = null;
		ISqlExecute dstExec = null;
		try {
			srcExec = SGSqlExecute.Init(srcJdbc);
			dstExec = SGSqlExecute.Init(dstJdbc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
////		String sql="select date_add(STR_TO_DATE('2021-10-01 00:00:00','%Y-%m-%d %H:%i:%s'), INTERVAL -1 year) as col1 "
////;
////		
//////		Object v=dstExec.QuerySingleValue(sql);
//////		String vs=PFDataHelper.ObjectToDateString(v);
//////		assertTrue("2021-10-01".equals( vs));
////
////		String sql="select sum(sales) as new_hy_sales from monthly_hyzl_2021 ";
//		//String sql=TransferTaskTest001.getSql2();
//		
//		PFMySqlCreateTableCollection model=new PFMySqlCreateTableCollection();
		SGSqlCreateTableCollection model = SGSqlCreateTableCollection.Init(dstJdbc);

		model.TableName = dstTableName;

		ResultSet dr = srcExec.GetDataReader(sql);

		// 自动建表
//		PFSqlCommandString createSql=dstExec.GetCreateTableSql(srcJdbc, sql, 
//				model
//				, null).ToSql();
		// boolean b1=dstExec.ExecuteSql(createSql);

//		boolean b2 =dstExec.HugeBulkReader(null,dr , tableName,
//
//	null, // transferItem == null ? null :
//						// transferItem.BeforeInsertAction,
//	(already) -> {
//////             total = already;
////		arr[0] = already;
////		if (alreadyAction != null) {
////			alreadyAction.accept(already);
////		}
////		// loadingfrm.SetJD("当前：正在导入reader", "当前进度："+dstTableName+"(" +
////		// PFDataHelper.ScientificNotation(already) + "/未知)");
//	}, null);

		boolean b2 = dstExec.HugeInsertReader(null, dr, dstTableName, null, (already) -> {

		}, null);

		assertTrue(b2 == true);
	}
//	/**
//	 * json处理不了PFSqlCommandString这种array型的
//	 */
//	public void testPFSqlCommandStringToJson() {
//		String sql1="";
//		ISqlExecute dstExec =null;
//		IPFJdbc dstJdbc = JdbcHelperTest.GetBonusJdbc();
//		dstExec = PFSqlExecute.Init(dstJdbc);
//		dstExec.ExecuteSql(new PFSqlCommandString(sql,sql2));
//	}
	/**
	 * 这种方法非常慢
	 * 
	 * @throws Exception
	 */
	public void testHugeInsertReader3() throws Exception {

		ISqlExecute dstExec = null;
		SGSqlCommandString sql = SGDataHelper.readObject("test001.txt", LocalDataType.Deletable);
		ISGJdbc dstJdbc = JdbcHelperTest.GetBonusJdbc();
		dstExec = SGSqlExecute.Init(dstJdbc);
		Connection conn = dstExec.GetConn();
		// conn.setAutoCommit(false);
		Statement stmt = conn.createStatement();
		Integer[] m = new Integer[] { 0 };
		m[0] = sql.size();
		for (int i = 0; i < m[0] && i < 10; i++) {// i<10时 0时0分0秒855毫秒
			stmt.addBatch(sql.get(i));
		}
		SGSpeedCounter speed = new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
		String aa = "bb";
		stmt.executeBatch();
		String bb = "bb";
		// conn.commit();
		System.out.println(speed.getSpeed(m[0], com.sellgirl.sgJavaHelper.SGDate.Now()));

	}

	public void testHugeInsertReader4() throws Exception {

		ISqlExecute dstExec = null;
		SGSqlCommandString sql = SGDataHelper.readObject("test001.txt", LocalDataType.Deletable);
		ISGJdbc dstJdbc = JdbcHelperTest.GetBonusJdbc();
		dstExec = SGSqlExecute.Init(dstJdbc);
		Connection conn = dstExec.GetConn();
		conn.setAutoCommit(false);
		Statement stmt = conn.createStatement();
		// conn.setAutoCommit(false);
		Integer[] m = new Integer[] { 0 };
		m[0] = sql.size();
		for (int i = 0; i < m[0] && i < 10; i++) {// i<10时 0时0分0秒557毫秒
			stmt.addBatch(sql.get(i));
		}
		SGSpeedCounter speed = new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
		String aa = "bb";
		stmt.executeBatch();
		// stmt.executeLargeBatch();
		String bb = "bb";
		conn.commit();
		System.out.println(speed.getSpeed(m[0], com.sellgirl.sgJavaHelper.SGDate.Now()));

	}

	public void testHugeInsertReader5() throws Exception {

		ISqlExecute dstExec = null;
		SGSqlCommandString sql = SGDataHelper.readObject("test001.txt", LocalDataType.Deletable);
		ISGJdbc dstJdbc = JdbcHelperTest.GetBonusJdbc();
		dstExec = SGSqlExecute.Init(dstJdbc);
		Connection conn = dstExec.GetConn();
		SGSpeedCounter speed = new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
		Integer[] m = new Integer[] { 0 };
		m[0] = sql.size();
		SGSqlCommandString sql2 = new SGSqlCommandString();
		for (int i = 0; i < sql.size() && i < 10; i++) {
			sql2.add(sql.get(i));
		}
		dstExec.ExecuteSql(sql2);
		System.out.println(speed.getSpeed(m[0], com.sellgirl.sgJavaHelper.SGDate.Now()));

	}

	public void testTransferTask() {
		SGSqlTransferItem task = null;
//	PFSqlTransferItem task=new TransferTaskTest001().get();
////	task.setCmonth();
//	//task.updateByCmonth(PFDate.Now().AddMonths(-1).toCmonth());
//	task.updateByCmonth("2021.09");

		task = new TransferTaskTest002().get();

		// ----------------------------------------
		ISqlExecute dstExec;
		try {
			dstExec = SGSqlExecute.Init(task.DstJdbc);
			dstExec.SetCommandTimeOut(PFSqlCommandTimeoutSecond.Huge());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		SGRef<String> ms = new SGRef<String>();
		// dstExec.CreateTable(task, ms);
		Boolean b = dstExec.TransferTable(task, null, null);

	}

	public void testUpdateYearOnYearField() {

		SGSqlTransferItem task = new TransferTaskTest001().get();
////	task.setCmonth();
//	//task.updateByCmonth(PFDate.Now().AddMonths(-1).toCmonth());
		// task.updateByCmonth("2021.09");
		task.updateByCmonth(new SGDate(2021, 9, 1, 1, 1, 1));
		ISqlExecute dstExec;
		try {
			dstExec = SGSqlExecute.Init(task.DstJdbc);
			dstExec.SetCommandTimeOut(PFSqlCommandTimeoutSecond.Huge());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		((PFTidbSqlExecute) dstExec).UpdateYearOnYearField(task.DstTableName, task.getCmonth(),
				task.DstTablePrimaryKeys, new String[] { "new_hy_qty", "new_buyer_qty", "valid_hy_qty", "buyer_qty",
						"valid_hy_hpos_above_5_qty", "valid_hy_hpos_0_qty" });
		String aa = "aa";
	}

	public void testPossibilityCombination() {
		String[] dice = new String[] { "a", "b" };
		List<List<String>> r = SGDataHelper.GetPossibilityCombination(Arrays.asList(dice));
		for (List<String> i : r) {
			this.PrintObject(String.join("", i));
		}

		this.PrintObject("---------------");
		dice = new String[] { "a", "b", "c" };
		// dice=new String[] {"a","b","c","d","e"};
		r = SGDataHelper.GetPossibilityCombination(Arrays.asList(dice));
		for (List<String> i : r) {
			this.PrintObject(String.join("", i));
		}
		this.PrintObject("---------------");
		dice = new String[] { "a", "b", "c", "d", "e" };
		r = SGDataHelper.GetPossibilityCombination(Arrays.asList(dice));
		for (List<String> i : r) {
			this.PrintObject(String.join("", i));
		}
	}

	public void testEnglishWord() {

		ISqlExecute dstExec;
		try {
			dstExec = SGSqlExecute.Init(JdbcHelperTest.GetSellGirlJdbc());
			dstExec.SetCommandTimeOut(PFSqlCommandTimeoutSecond.Huge());
			List<SellGirlProduct> list = dstExec.QueryList(SellGirlProduct.class,
					"SELECT   \r\n" + "      [ProductCode] as productCode \r\n"
							+ "      ,[ProductName] as productName \r\n"
							+ "  FROM [SellGirl.Business].[dbo].[product_product]\r\n"
							+ "  where TypeId='C5546FAC-D067-4683-8DFE-3AB81250FC91'");

			String[] dice0 = new String[] { "s", "r", "d", "f", "h", "u" };
			String[] dice1 = new String[] { "p", "g", "m", "t", "n", "b" };
			String[] dice2 = new String[] { "e", "y", "o", "k", "l", "s" };
			String[] dice3 = new String[] { "e", "w", "a", "h", "s", "i" };
			List<String> combinationList = new ArrayList<String>();
			for (String i : dice0) {
				for (String j : dice1) {
					for (String k : dice2) {
						for (String l : dice3) {
							List<List<String>> combination = SGDataHelper
									.GetPossibilityCombination(Arrays.asList(new String[] { i, j, k, l }));
							for (List<String> m : combination) {
								String word = String.join("", m);
								combinationList.add(word);
							}
						}
					}

				}
			}
			List<SellGirlProduct> maybeWords = SGDataHelper.ListWhere(list,
					a -> SGDataHelper.ListAny(combinationList, b -> b.equals(a.getProductCode())));
//			PFDataHelper.ListSelect(
//					
//					, a->PFDataHelper.);
			for (SellGirlProduct i : maybeWords) {
				this.PrintObject(i.getProductCode() + "___" + i.getProductName());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

	private static Dimension pcSize = new Dimension(2160, 4680);// p30pro 1080, 2340
	private static String sashaImgPath = "D:\\\\picture\\ps\\princessSasha_lostVirginity_byBenjamin.jpg";
	private static String sashaImgPath2 = "D:\\\\picture\\ps\\princessSasha_lostVirginity_byBenjamin2.jpg";
	private static String sashaBeingTiedToTheCrossImgPath = "D:\\\\picture\\ps\\princessSashaBeingTiedToTheCross\\princessSashaBeingTiedToTheCross_byBenjamin.jpg";

	public void testBackgroundImg() {
		try {
			int backWidth = 3840; // 1920;
			int backHeight = 2160;// 1080;

			File infile = new File(sashaImgPath);
			Image image = ImageIO.read(infile);
//		int backWidth = w;
//		int backHeight = h;

			String tmpImgPath = SGDataHelper.backgroundImg(new Dimension(backWidth, backHeight), image, null,
					new PFLine(new PFPoint(0, 0), new PFPoint(100, 100)).IsPercent(), Color.RED, false);
			File file = new File(tmpImgPath);
			FileInputStream inputStream = new FileInputStream(file);
			byte[] bytes = new byte[inputStream.available()];
			inputStream.read(bytes, 0, inputStream.available());
			inputStream.close();
			// file.delete();
		} catch (Exception e) {
		}
		return;
	}

	public void testBackgroundImgInBuffer() {
		try {
			int backWidth = 3840; // 1920;
			int backHeight = 2160;// 1080;

			File infile = new File(sashaImgPath);
			Image image = ImageIO.read(infile);
			File infile2 = new File(sashaBeingTiedToTheCrossImgPath);
			Image image2 = ImageIO.read(infile2);
			File infile3 = new File(sashaImgPath2);
			Image image3 = ImageIO.read(infile);
//		int backWidth = w;
//		int backHeight = h;
			// BufferedImage paintBi = new BufferedImage(wWidth * pixelTatio, wHeight *
			// pixelTatio, 1);
//		Canvas canvas = new Canvas();
//		Graphics ctx1 = canvas.getGraphics();

//		Canvas canvas = null;
//		Graphics ctx1 = null;
			SGRef<Canvas> canvasRef = new SGRef<Canvas>(null);
			SGRef<Graphics> ctx1Ref = new SGRef<Graphics>(null);
			BufferedImage paintBi = null;
			paintBi = SGDataHelper.backgroundImgInBuffer(
					// canvas,ctx1,
					canvasRef, ctx1Ref, null, new Dimension(backWidth, backHeight), image, null,
					// new PFLine(new PFPoint(0,0),new PFPoint(3840,2160)),
					// new PFLine(new PFPoint(0,0),new PFPoint(3840,1000)),
					new PFLine(new PFPoint(0, 0), new PFPoint(1790, 2160)),
					new PFLine(new PFPoint(0, 0), new PFPoint(100, 100)).IsPercent(), Color.RED, false);
//		 paintBi = PFDataHelper.backgroundImgInBuffer(
//					//canvas,ctx1,
//					canvasRef,ctx1Ref,
//					null,new Dimension(backWidth, backHeight), image3,
//					null,
//					//new PFLine(new PFPoint(0,0),new PFPoint(3840,2160)),
//					//new PFLine(new PFPoint(0,0),new PFPoint(3840,1000)),
//					new PFLine(new PFPoint(0,0),new PFPoint(2000,2160)),
//					new PFLine(new PFPoint(0,0),new PFPoint(100,100)).IsPercent(), Color.RED,
//					false);

			paintBi = SGDataHelper.backgroundImgInBuffer(
//				 canvas,ctx1,
					canvasRef, ctx1Ref, paintBi, new Dimension(backWidth, backHeight), image2, null,
					// new PFLine(new PFPoint(0,0),new PFPoint(3840,2160)),
					// new PFLine(new PFPoint(0,0),new PFPoint(3840,1000)),
					new PFLine(new PFPoint(1790, 0), new PFPoint(1790 + 1680, 2160)),
					new PFLine(new PFPoint(0, 0), new PFPoint(100, 100)).IsPercent(), Color.RED, false);

			// canvas=canvasRef.GetValue();
			canvasRef.GetValue().printAll(ctx1Ref.GetValue());

			String tmpImgPath = Paths
					.get(SGDataHelper.GetBaseDirectory(),
							new String[] { "tmpfile\\tmpimg_" + SGDataHelper.GetNewUniqueHashId() + ".jpg" })
					.toString();
			SGDirectory.EnsureExists(tmpImgPath);
			File file2 = new File(tmpImgPath);
			ImageIO.write(paintBi, "jpg", file2);
			// ctx1.dispose();
			// file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	public void testDateRange() {
		SGDate now = SGDate.Now().GetHourStart();
		SGDate lastMinute = now;
		SGDateRange monRange = new SGDateRange(lastMinute.GetYear());
		List<SGDate> perMonth = monRange.GetPerMonthStart();
		for (SGDate i : perMonth) {
			this.PrintObject(i.toYM());
		}
	}

	public void testReplace() {
		String s = "{cmonth}aaa{cmonth}";
		s = s.replace("{cmonth}", "ccc");
		this.PrintObject(s);
		
		String content="aaaa<img src=\"01.jpg\" />bbbb";
		this.PrintObject(content);
		content=content.replaceAll("<img src=\"", "<img src=\"bookImg/content/");
		this.PrintObject(content);
	}

	public void testReplace2() {
		String s = "aa:${p1:1}bb:${p2:2}cc:${p3:}";
		// s=s.replaceAll("\\\\${[^\\\\$]+\\:(.*)}","$1");
		String s2 = s.replaceAll("\\\\$", "bbbb");
		String s3 = s.replaceAll("\\$", "bbbb");
		String s4 = s.replaceAll("\\$\\{", "bbbb");
		String s5 = s.replaceAll("\\$\\{.*\\:.*\\}", "bbbb");
		String s6 = s.replaceAll("\\$\\{.*\\:(.*)\\}", "$1");
		String s7 = s.replaceAll("\\$\\{[^\\$]*\\:(.*)\\}", "$1");
		String s8 = s.replaceAll("\\$\\{[^\\$]*\\:([^\\$]*)\\}", "$1");
		this.PrintObject(s);
	}

	public void testUserSetting() {
		PFKeyValueCollection<String> userSetting = SGDataHelper.readUserSetting("userSetting01.txt");
		this.PrintObject(userSetting);
		SGDataHelper.writeUserSetting("userSetting01.txt", a -> {
			a.Set("needValidateLiGeDayOrderApi", "2");
			a.Set("needValidate02", "3");
		});
		userSetting = SGDataHelper.readUserSetting("userSetting01.txt");
		this.PrintObject(userSetting);
		String aa = "aa";
	}

	public void testDeleteLog() {
		// PFDataHelper.DeleteLocalTxtFile(fileName, dataType);
		SGDataHelper.DeleteLog(new SGDate(2021, 11, 25, 0, 0, 0));
		while (true) {

		}
	}

	public void testWriteLog() {
		SGDataHelper.WriteLog("aa\r\nbb");
		while (true) {

		}
	}

	public void testWriteError() {
		SGDataHelper.WriteError(new Exception("aaa"));
		while (true) {

		}
	}
//	public void testThreadBoolean() throws Exception {
//		TaskTest001 task=new TaskTest001();
//		boolean[] hasError=new boolean[] {false};
//		for(int i=0;i<100000;i++) {
//
//
//			new Thread() {// 线程操作
//				public void run()  {
//					task.Stop();
//					if(task.getNumber()>1) {
//						System.out.println(task.getNumber());
//						//throw new Exception("异常");
//						hasError[0]=true;
//					}
//					task.Start();
//					if(task.getNumber()>1) {
//						System.out.println(task.getNumber());
//						//throw new Exception("异常");
//						hasError[0]=true;
//					}
//				}
//			}.start();
//			if(hasError[0]) {
//				System.out.println(task.getNumber());
//				throw new Exception("异常");
//			}
//		}
//		System.out.println(task.getNumber());
//	}
//	public void testThreadBoolean2() throws Exception {
//		TaskTest002 task=new TaskTest002();
//		boolean[] hasError=new boolean[] {false};
//		for(int i=0;i<100000;i++) {
//
//
//			new Thread() {// 线程操作
//				public void run()  {
//					task.Stop();
//					if(task.getNumber()>1) {
//						System.out.println(task.getNumber());
//						//throw new Exception("异常");
//						hasError[0]=true;
//					}
//					task.Start();
//					if(task.getNumber()>1) {
//						System.out.println(task.getNumber());
//						//throw new Exception("异常");
//						hasError[0]=true;
//					}
//				}
//			}.start();
//			if(hasError[0]) {
//				System.out.println(task.getNumber());
//				throw new Exception("异常");
//			}
//		}
//		System.out.println(task.getNumber());
//	}

	public void testThreadBoolean() throws Exception {
		TaskTest001 task = new TaskTest001();
		boolean[] hasError = new boolean[] { false };
		for (int i = 0; i < 100000; i++) {

			new Thread() {// 线程操作
				public void run() {
					task.Start();
//					try {
//						Thread.sleep(20);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					task.Stop();//stop不需要测试
//					if(task.getNumber()>1) {
//						System.out.println(task.getNumber());
//						//throw new Exception("异常");
//						hasError[0]=true;
//					}
//					if(task.getNumber()>1) {
//						System.out.println(task.getNumber());
//						//throw new Exception("异常");
//						hasError[0]=true;
//					}
				}
			}.start();
//			if(hasError[0]) {
//				System.out.println(task.getNumber());
//				throw new Exception("异常");
//			}
		}
		// System.out.println(task.getNumber());
		System.out.println("11111111111111");
	}

	/**
	 * 此方法会输出2222222222222222222(平均执行10次出现1次),所以PFTaskBase2.running有线程安全
	 * 
	 * @throws Exception
	 */
	public void testThreadBoolean2() throws Exception {
		TaskTest002 task = new TaskTest002();
		boolean[] hasError = new boolean[] { false };
		for (int i = 0; i < 100000; i++) {

			new Thread() {// 线程操作
				public void run() {
					task.Start();
//					try {
//						Thread.sleep(20);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					task.Stop();
//					if(task.getNumber()>1) {
//						System.out.println(task.getNumber());
//						//throw new Exception("异常");
//						hasError[0]=true;
//					}
//					if(task.getNumber()>1) {
//						System.out.println(task.getNumber());
//						//throw new Exception("异常");
//						hasError[0]=true;
//					}
				}
			}.start();
			if (hasError[0]) {
				// System.out.println(task.getNumber());
				throw new Exception("异常");
			}
		}
		// System.out.println(task.getNumber());
		System.out.println("11111111111111");
	}

	public void testHugeUpdateByComparedTable() {
		try {

			this.PrintObject(SGDate.Now().toString());
			ISGJdbc srcJdbc = JdbcHelperTest.GetLiGeOrderJdbc();
			ISGJdbc dstJdbc = JdbcHelperTest.GetLiGeSaleJdbc();

			ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);
			srcExec.SetCommandTimeOut(PFSqlCommandTimeoutSecond.Huge());
			// ISqlExecute sqlExec = PFSqlExecute.Init(transfer.DstJdbc);

			// sqlExec.Delete(transfer.DstTableName,a->a.Add("order_month",r.getPFCmonth().getYm()));
			String ym = "202207";
			String dstTableName = "order_order";
			String srcSql = SGDataHelper.ReadLocalTxt("liGe_sale_order.txt").replace("{ym}", ym);
			// String srcSql=r.SrcSql;
			// String dstSql=PFDataHelper.FormatString("select * from {0}.{1} where
			// order_month='{2}' and order_no='SG942269220601000002' ",
			// r.DstDbName,r.DstTableName,r.getPFCmonth().getYm());
//			String dstSql=PFDataHelper.FormatString("select * from {0}.{1} where order_month='{2}' ", r.DstDbName,r.DstTableName,r.getPFCmonth().getYm());
			String dstSql = SGDataHelper.FormatString("select * from {0} where order_month='{1}' ", dstTableName, ym);
			srcExec.HugeUpdateByComparedTable(dstJdbc, srcSql, dstSql, new String[] { "id" },
					new String[] { "order_no", "customer_card", "pay_time", "order_status", "order_amount" },
					(a, b,c) -> {
						this.PrintObject("已比对" + a + "行,共" + b + "行");
					}, // null,
					(a, b) -> {
						this.PrintObject("已更新" + a + "行,共" + b + "行");
					}, // null,
					null, dstTableName);

			this.PrintObject(SGDate.Now().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testGetDataTableSpeedForMySql() {
		try {
			this.PrintObject(SGDate.Now().toString());
			// IPFJdbc srcJdbc = JdbcHelperTest.GetLiGeOrderJdbc();
			ISGJdbc dstJdbc = JdbcHelperTest.GetLiGeSaleJdbc();

			// ISqlExecute srcExec = PFSqlExecute.Init(srcJdbc);
			ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
			// srcExec.SetCommandTimeOut(PFSqlCommandTimeoutSecond.Huge());
			// ISqlExecute sqlExec = PFSqlExecute.Init(transfer.DstJdbc);

			// sqlExec.Delete(transfer.DstTableName,a->a.Add("order_month",r.getPFCmonth().getYm()));
			String ym = "202207";
			String dstTableName = "order_order";
			// String
			// srcSql=PFDataHelper.ReadLocalTxt("liGe_sale_order.txt").replace("{ym}",ym);
			// String srcSql=r.SrcSql;
			// String dstSql=PFDataHelper.FormatString("select * from {0}.{1} where
			// order_month='{2}' and order_no='SG942269220601000002' ",
			// r.DstDbName,r.DstTableName,r.getPFCmonth().getYm());
//			String dstSql=PFDataHelper.FormatString("select * from {0}.{1} where order_month='{2}' ", r.DstDbName,r.DstTableName,r.getPFCmonth().getYm());
			String dstSql = SGDataHelper.FormatString("select * from {0} where order_month='{1}' ", dstTableName, ym);
			dstExec.UpToCommandTimeOut(PFSqlCommandTimeoutSecond.Huge());
			SGDataTable dstTable = dstExec.GetDataTable(dstSql, null);
			this.PrintObject(SGDate.Now().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testToAbsolutePath() {
		String[] paths = new String[] { "usr/local/app/app.jar", "D:\\usr/local/app/app.jar",
				"." + File.separatorChar + "usr/local/app/app.jar", "/usr/local/app/app.jar", };
		for (int i = 0; i < paths.length; i++) {
			this.PrintObject(paths[i]);
			String a = SGDirectory.ToAbsolutePath(paths[i]);
			this.PrintObject(a);
			this.PrintObject(SGDirectory.FullPathToAbsolutePath(paths[i]));
			this.PrintObject("==========================");
		}
//		for(int i=0;i<paths.length;i++) {
//			String a=PFDirectory.ToAbsolutePath(paths[i]);
//			this.PrintObject(a);
//		}
	}

	public void testSerialize() {
		try {
			String filePath = Paths
					.get(SGDataHelper.GetBaseDirectoryAbsolutePath(),
							SGDataHelper.LocalDataType.Deletable.toString() + "LocalData", "Txt", "file01.txt")
					.toString();
			SGDirectory.EnsureExists(filePath);
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));

			out.writeObject("序列化的日期是：");// 序列化一个字符串到文件
			out.writeObject(new Date());// 序列化一个当前日期对象到文件
			SqlUpdateItem userInfo = new SqlUpdateItem();
			userInfo.Key = "key1";
			userInfo.Value = "value1";
			userInfo.VType = int.class;
//			userInfo.VPFType= PFSqlFieldType.BigDecimal;
			userInfo.setSrcDataPFType(SGSqlFieldTypeEnum.BigDecimal);
			out.writeObject(userInfo);// 序列化一个会员对象

			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testDeserialize() {
		try {
			String filePath = Paths
					.get(SGDataHelper.GetBaseDirectoryAbsolutePath(),
							SGDataHelper.LocalDataType.Deletable.toString() + "LocalData", "Txt", "file01.txt")
					.toString();
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));

			String str = (String) in.readObject();// 刚才的字符串对象
			Date date = (Date) in.readObject();// 日期对象
			SqlUpdateItem userInfo = (SqlUpdateItem) in.readObject();// 会员对象
			in.close();

			System.out.println(str);
			System.out.println(date);
			System.out.println(JSON.toJSONString(userInfo));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testCopyObject() {
		SqlUpdateItem userInfo = new SqlUpdateItem();
		userInfo.Key = "key1";
		userInfo.Value = "value1";
		userInfo.VType = int.class;
		// userInfo.VPFType= PFSqlFieldType.BigDecimal;
		userInfo.setSrcDataPFType(SGSqlFieldTypeEnum.BigDecimal);

		SqlUpdateItem userInfo2 = SGDataHelper.CopyObject(userInfo);

		assertTrue(userInfo.Key.equals(userInfo2.Key));
		userInfo2.Key = "key2";
		assertFalse(userInfo.Key.equals(userInfo2.Key));
	}

	public void testDoInsertList() {
//        var jsonSerizlizerSetting = new JsonSerializerSettings();
//        jsonSerizlizerSetting.Converters.Add(new PFDateTimeConvert());
//
//        string ms = null;
//        var now = DateTime.Now;
//        //var cday = PFDataHelper.CMonthToDate(cmonth);
//        var cmonth = PFDataHelper.DateToCMonth(now);
		// var service = new DbReportService(cmonth);
		String cmonth = "202207";
		ISqlExecute sqlExec;
		try {
			sqlExec = SGSqlExecute.Init(JdbcHelperTest.GetDayJdbc());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		;
		String url = SGDataHelper.FormatString("{0}/perfectdatasync/queryItem",
				"https://perfect-r.leshiguang.com/mall-rest");
		SGRequestResult r2 = SGHttpHelper.HttpGet(url, "");
		// pageNum++;
		try {
			if ((!r2.success) || null == r2.content) {
				throw new Exception(SGDataHelper.FormatString("{2}接口没有读到数据\r\n", "url:{0}\r\n", "返回信息:{1}\r\n", url,
						r2.content, "健康公司产品"));
			}
			String healthOrdersStr = r2.content;
			// var healthOrders =
			// JsonConvert.DeserializeObject<HealthInvResult>(healthOrdersStr,
			// jsonSerizlizerSetting);

			SuccessApiResult<HealthOrdersApiResult<JSONObject>> healthOrders = JSON.parseObject(healthOrdersStr,
					new TypeReference<SuccessApiResult<HealthOrdersApiResult<JSONObject>>>() {
					});
			if (healthOrders.getData() != null && healthOrders.getData().data != null
					&& healthOrders.getData().data.size() > 0) {
//                if (!service.AddDayTable(healthOrders.data.data, "t_health_inv", out ms))
//                {
//                    throw new Exception(ms);
//                }
				List<JSONObject> list = healthOrders.getData().data;
				int insertCnt = list.size();
				sqlExec.Delete("t_health_inv", null);
				boolean b = sqlExec.doInsertList(
//            			srcFieldNames,
						null, "t_health_inv",
						// list.size(),
						(a, b2, c) -> a < insertCnt,
						// a->list.get(a),
						a -> {
							JSONObject r = list.get(a);
//        					if(r.containsKey("Price")&&!r.containsKey("price")) {
//            					r.put("price",r.get("Price"));
//        					}
							return r;
						},
//        				(update,b2,c)->update.Set("order_source", 2),//1 crm 2 health
						null, null, null);
				if (!b) {
					// throw new Exception(ms);
					throw new RuntimeException(sqlExec.GetErrorFullMessage());
				}
			} else {
				// isEnd = true;
				throw new Exception(SGDataHelper.FormatString("健康订单接口没有读到数据\r\n", "url:{0}\r\n", "返回信息:{1}\r\n", url,
						healthOrdersStr == null ? "" : healthOrdersStr));
			}
		} catch (Exception e) {
			// isEnd = true;
			SGDataHelper.WriteError(e);
		}
		// }
	}

	/**
	 * @throws Exception
	 * 
	 */
	public void testBase64() throws Exception {
		String s1 = "340.00";
		// 想弄清楚 AITQ 是不是 340.00 的 base64形式
		// String r1=new
		// String(org.apache.commons.codec.binary.Base64.encodeBase64(s1));

		String r1 = java.util.Base64.getEncoder().encodeToString(s1.getBytes("utf-8"));
		// byte[] base64decodedBytes = java.util.Base64.getDecoder().decode(s1);
		this.PrintObject(s1);
		this.PrintObject(r1);
		this.PrintObject("-----------------------------");

		String r2 = "AITQ";
		String s2 = new String(java.util.Base64.getDecoder().decode(r2), "utf-8");
		this.PrintObject(r2);
		this.PrintObject(s2);
		this.PrintObject("-----------------------------");
	}

	public void testPreparedStatement() throws Exception {
		ISGJdbc dstJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();
		// IPFJdbc dstJdbc = JdbcHelperTest.GetLiGeShopJdbc();
		ISqlExecute dstExec = null;
		try {
			dstExec = SGSqlExecute.Init(dstJdbc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		String sql = "update test_tb_03 set col1=? where id=?";
		Connection conn = dstExec.GetConn();
		PreparedStatement crs = (PreparedStatement) conn.prepareStatement(sql);
		// ResultSetMetaData r=crs.getMetaData();
		crs.setString(1, "aaaaaa");
		crs.setInt(2, 1);
		crs.addBatch();

		crs.executeBatch();
		// conn.commit();
		crs.close();
		conn.close();
	}

	public void testPreparedStatementNumeric() throws Exception {
		ISGJdbc dstJdbc = JdbcHelperTest.GetBonusJdbc();
		// IPFJdbc dstJdbc = JdbcHelperTest.GetLiGeShopJdbc();
		ISqlExecute dstExec = null;
		try {
			dstExec = SGSqlExecute.Init(dstJdbc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		String sql = "update test_tb_03 set col1=? where id=?";
		Connection conn = dstExec.GetConn();
		PreparedStatement crs = (PreparedStatement) conn.prepareStatement(sql);
		// ResultSetMetaData r=crs.getMetaData();
		crs.setString(1, "aaaaaa");
		crs.setInt(2, 1);
		crs.addBatch();

		crs.executeBatch();
		// conn.commit();
		crs.close();
		conn.close();

	}

	public void testResultSet() throws Exception {
		ISGJdbc dstJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();
		// IPFJdbc dstJdbc = JdbcHelperTest.GetLiGeShopJdbc();
          try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
              //...
			  String sql = "select * from test_tb_04";
			  Connection conn = myResource.GetConn();
//			  PreparedStatement crs = (PreparedStatement) conn.prepareStatement(sql);
//			  ResultSet result = crs.executeQuery();
			  Statement stmt =  conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					  ResultSet.CONCUR_UPDATABLE);

			  ResultSet result = stmt.executeQuery(
					  sql);
			  result.last();
			  result.deleteRow();
			  System.out.println("testResultSet 执行完成");
          } catch (Exception e) {
              throw e;
          }
	}

	public void testResultSet2() throws Exception {
		ISGJdbc dstJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();
		// IPFJdbc dstJdbc = JdbcHelperTest.GetLiGeShopJdbc();
		SGSpeedCounter speed=new SGSpeedCounter(SGDate.Now());
		//long delay=5000;
		//long start=System.currentTimeMillis();
		try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
			//...
			String sql = "select * from test_tb_04";
			Connection conn = myResource.GetConn();
			conn.setAutoCommit(false);
////			  PreparedStatement crs = (PreparedStatement) conn.prepareStatement(sql);
////			  ResultSet result = crs.executeQuery();
			Statement stmt =  conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);

			ResultSet result = stmt.executeQuery(
					sql);
			//ResultSet result=myResource.GetHugeDataReader(sql);
			//result.last();
			int cnt=0;
			PFBatchHelper batchHelper=new PFBatchHelper();
			batchHelper.batchSize = 20000;
			batchHelper.batchCnt = 0;
			while (true){
				batchHelper.hasNext=result.next();
				batchHelper.batchCnt++;
				cnt++;
				result.deleteRow();
//				if(System.currentTimeMillis()>start+delay){
//					conn.commit();
//					System.out.println(speed.getSpeed(cnt,PFDate.Now()));
//					start=System.currentTimeMillis();
//				}
				if (batchHelper.ifDo()) {
					conn.commit();
					System.out.println(speed.getSpeed(cnt,SGDate.Now()));
					//start=System.currentTimeMillis();
					batchHelper.hasDone();
				}
				if (!batchHelper.hasNext) {break;}
			}
			System.out.println("testResultSet 执行完成");
		} catch (Exception e) {
			throw e;
		}
	}
	public void testSpeed() {
		SGDate beginTimeDate = SGDate.Now();
		SGDate endTimeDate = beginTimeDate.AddMinutes(3);
		long beginTime = beginTimeDate.ToCalendar().getTimeInMillis();
		long endTime = endTimeDate.ToCalendar().getTimeInMillis();
		int total = 640128 - 280056;

		this.PrintObject(SGDataHelper.FormatString("\r\nrows:{0}  --  " + "speed:{1} \r\n",
				SGDataHelper.ScientificNotation(total),
				SGDataHelper.ScientificNotation(Double.valueOf(total) * 1000 / (endTime - beginTime)) + "条/秒"));

		SGSpeedCounter speed = new SGSpeedCounter(beginTimeDate);
		this.PrintObject(speed.getSpeed(total, endTimeDate));

		SGSpeedCounter speed2 = new SGSpeedCounter();
		speed2.setBeginTime(beginTimeDate);
		speed2.setEndTime(beginTimeDate.AddMinutes(1));
		speed2.setBeginTime(beginTimeDate.AddMinutes(5));
		speed2.setEndTime(beginTimeDate.AddMinutes(6));
		this.PrintObject(speed2.getSpeed2(total));// 耗时2分钟

	}

	public void testSpeed2() {
		SGSpeedCounter speed2 = new com.sellgirl.sgJavaHelper.SGSpeedCounter();
		speed2.setBeginTime(1000);
		speed2.setEndTime(10000000);
		String r1 = speed2.getSpeed2(5001);
		SGDataHelper.FormatString("\r\nconvert time waste:\r\n{0}\r\n  (processBatch:{1})\r\n", r1, 5000);
		String aa = "aa";

	}

	public void testSpeed3() {
		SGSpeedCounter speed2 = new com.sellgirl.sgJavaHelper.SGSpeedCounter();
		speed2.setBeginTime(1000);
		speed2.setEndTime(10000000);
		SGDataHelper.FormatString("\r\nconvert time waste:\r\n{0}\r\n  (processBatch:{1})\r\n", speed2.getSpeed2(5001),
				5000);
		String aa = "aa";

	}

	public void testInteger() {
//		Object i=new Integer(1);
//		Long l=Long.valueOf(i);
		this.PrintObject(Long.MAX_VALUE);
	}

	public enum enum01 {
		aa, bb, cc
	}

	/**
	 * 这样写虽然 values是对的,但ordinal是错的
	 * 
	 * @author 1011712002
	 *
	 */
	public enum enum02 implements IPFEnum {
		Default("Default", 3), Fgs("Fgs", 5);

		private int _value;
		private String _text;

		@Override
		public int getValue() {
			// TODO Auto-generated method stub
			return _value;
		}

		@Override
		public String getText() {
			// TODO Auto-generated method stub
			return _text;
		}
//
//		@Override
//		public boolean hasFlag(PFEnum other) {
//			// TODO Auto-generated method stub
//			return PFDataHelper.EnumHasFlag(this._value, other.getValue());
//		}			

		private enum02(String text, int value) {
			_value = value;
			_text = text;
		}
		// 不能复写ordinal
//		public  int ordinal() {
//			
//		}
	}

	public enum InvoiceType1 implements IPFEnum {

		INVALID(9, "你好1"),

		OPEN(8, "你好2"),

		UNOPEN(7, "你好3");

		String name;

		int value;

		private static Map<Integer, String> map = new HashMap<Integer, String>();

		static {
			for (InvoiceType1 type : values()) {
				map.put(type.ordinal(), type.name());
			}
		}

		private InvoiceType1(int value, String name) {
			this.value = value;
			// 初始化map列表
			this.name = name;
			try {
				Field fieldName = getClass().getSuperclass().getDeclaredField("ordinal");
				fieldName.setAccessible(true);
				fieldName.set(this, value);
				fieldName.setAccessible(false);

				Field fieldName1 = getClass().getSuperclass().getDeclaredField("name");
				fieldName1.setAccessible(true);
				fieldName1.set(this, name);
				fieldName1.setAccessible(false);

			} catch (Exception e) {
			}
		}

//		     public static void main(String[] args) {
//		         System.out.println(InvoiceType1.getMap());
//		         
//		         System.out.println(InvoiceType1.INVALID.ordinal());
//		         System.out.println(InvoiceType1.INVALID.name());
//		     }

		public String getText() {
			return name;
		}

		public void setText(String name) {
			this.name = name;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

//		     public static Map<Integer, String> getMap() {
//		         return map;
//		     }
//		 
//		     public static void setMap(Map<Integer, String> map) {
//		         InvoiceType1.map = map;
//		     }
	}

	public enum EnumDemoFirst {

		RED(1, "hongse"), GREEN(2, "lvse"), YELLOW(3, "huangse");

		private int code;
		private String msg;

		private EnumDemoFirst(int ordinal, String name) {
			this.code = ordinal;
			this.msg = name;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
	}

	public void testEnum() {
//	  enum01 a=enum01.aa;
//		//int b=(int)a;//enum不能这样转换,会报错
//		int b=a.ordinal();
//		this.PrintObject(b);
//		this.PrintObject(enum01.cc.ordinal());
//		enum02 a2=enum02.Fgs;
//		 //b=(int)a;
//		this.PrintObject(a2.ordinal());//注意这里输出是1,并不是期望的5
//		this.PrintObject(InvoiceType1.UNOPEN.ordinal());
//		this.PrintObject(enum01.bb.ordinal());
//		this.PrintObject(TestEnum001.Fgs.ordinal());
//		
//		Object o=TestEnum001.valueOf(TestEnum001.class, "Fgs");
//		this.PrintObject(o);

//		this.PrintObject(EnumDemoFirst.YELLOW.ordinal());//注意这里输出是1,并不是期望的5
//	    EnumDemoFirst[] values = EnumDemoFirst.values(); 
//	    for (EnumDemoFirst enumDemoFirst : values) { 
//	      System.out.println(enumDemoFirst + "--" + enumDemoFirst.getCode() + "--" + enumDemoFirst.getMsg()); 
//	      System.out.println("============="); 
//	    } 
//	    enum02[] values2 = enum02.values(); 
//	    for (enum02 enumDemoFirst2 : values2) { 
//	      System.out.println(enumDemoFirst2 + "--" + enumDemoFirst2.getValue() + "--" + enumDemoFirst2.getText()); 
//	      System.out.println("============="); 
//	    } 

//	    LiGeHyType[] values3 = LiGeHyType.values(); 
//	    for (LiGeHyType enumDemoFirst3 : values3) { 
//	      System.out.println(enumDemoFirst3 + "--" + enumDemoFirst3.getValue() + "--" + enumDemoFirst3.getText()); 
//	      System.out.println("============="); 
//	    } 

//	    assertEquals(FileSizeUnitType.isEnabled(2, FileSizeUnitType.MB), true);
//	    assertEquals(FileSizeUnitType.isEnabled(2, FileSizeUnitType.KB), false);
	}

	// rows:1.00E+006 speed:3.70E+007条/秒 耗时:0时0分0秒270毫秒/千万行 总耗时:0时0分0秒27毫秒
	// (begin:2022-09-27 14:05:34 -> now:2022-09-27 14:05:34)
	public void testEnumSpeed() throws Exception {
		int total = 1000000;
		SGSpeedCounter speed = new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
		for (int i = 0; i < total; i++) {
			if (!UserTypeClass.Fgs.equals(UserTypeClass.Fgs)) {
				throw new Exception("判断错误");
			}
		}
		LOGGER.info(speed.getSpeed(total, com.sellgirl.sgJavaHelper.SGDate.Now()));
	}

	// rows:1.00E+006 speed:3.70E+007条/秒 耗时:0时0分0秒270毫秒/千万行 总耗时:0时0分0秒27毫秒
	// (begin:2022-09-27 14:05:48 -> now:2022-09-27 14:05:48)
	public void testEnumSpeed2() throws Exception {
		int total = 1000000;
		SGSpeedCounter speed = new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
		for (int i = 0; i < total; i++) {
			if (!TestEnum001.Fgs.equals(TestEnum001.Fgs)) {
				throw new Exception("判断错误");
			}
		}
		LOGGER.info(speed.getSpeed(total, com.sellgirl.sgJavaHelper.SGDate.Now()));
	}

	// rows:1.00E+007 speed:6.44E+006条/秒 耗时:0时0分1秒551毫秒/千万行 总耗时:0时0分1秒551毫秒
	// (begin:2022-10-26 08:43:00 -> now:2022-10-26 08:43:01)
	public void testEnumSpeed3() throws Exception {
		int total = 10000000;
		SGSpeedCounter speed = new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
		for (int i = 0; i < total; i++) {
			if (PFSqlFieldType.String != PFEnumClass.EnumParse(PFSqlFieldType.class, "String")) {
				throw new Exception("判断错误");
			}
			if (PFSqlFieldType.Bool != PFEnumClass.EnumParse(PFSqlFieldType.class, "Bool")) {
				throw new Exception("判断错误");
			}
		}
		LOGGER.info(speed.getSpeed(total, com.sellgirl.sgJavaHelper.SGDate.Now()));
	}

	// 事实证明PFSqlFieldTypeEnum比PFSqlFieldType更快
	// rows:1.00E+007 speed:1.05E+008条/秒 耗时:0时0分0秒95毫秒/千万行 总耗时:0时0分0秒95毫秒
	// (begin:2022-10-26 08:45:04 -> now:2022-10-26 08:45:04)
	public void testEnumSpeed4() throws Exception {
		int total = 10000000;
		SGSpeedCounter speed = new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
		for (int i = 0; i < total; i++) {
			if (SGSqlFieldTypeEnum.String != SGSqlFieldTypeEnum.valueOf("String")) {
				throw new Exception("判断错误");
			}
			if (SGSqlFieldTypeEnum.Bool != SGSqlFieldTypeEnum.valueOf("Bool")) {
				throw new Exception("判断错误");
			}
		}
		LOGGER.info(speed.getSpeed(total, com.sellgirl.sgJavaHelper.SGDate.Now()));
	}
//[main] INFO org.sellgirlPayHelperNotSpring.UncheckDe001 - rows:1.00E+007 speed:2.94E+008条/秒 averageTime:0时0分0秒34毫秒/千万行 totalTime:0时0分0秒34毫秒 (begin:2022-11-29 15:08:18 -> now:2022-11-29 15:08:18)
	public void testFlagEnumSpeed1() throws Exception {
		SGSpeedCounter speed = new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
		SGFlagEnum<FileSizeUnitType> unit3 = new SGFlagEnum<FileSizeUnitType>(FileSizeUnitType.GB)
				.or(FileSizeUnitType.MB);
		int total = 10000000;
		for (int i = 0; i < total; i++) {
			unit3.hasFlag(FileSizeUnitType.GB);
			unit3.hasFlag(FileSizeUnitType.MB);
			unit3.hasFlag(FileSizeUnitType.KB);
		}
		LOGGER.info(speed.getSpeed(total, com.sellgirl.sgJavaHelper.SGDate.Now()));
	}
//[main] INFO org.sellgirlPayHelperNotSpring.UncheckDe001 - rows:1.00E+007 speed:1.34E+007条/秒 averageTime:0时0分0秒743毫秒/千万行 totalTime:0时0分0秒743毫秒 (begin:2022-11-29 15:08:42 -> now:2022-11-29 15:08:43)
	public void testFlagEnumSpeed2() throws Exception {
		SGSpeedCounter speed = new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
		PFSqlFieldType unit3 = PFSqlFieldType.Decimal.Or(PFSqlFieldType.Int);
		int total = 10000000;
		for (int i = 0; i < total; i++) {
			unit3.HasFlag(PFSqlFieldType.Decimal);
			unit3.HasFlag(PFSqlFieldType.Int);
			unit3.HasFlag(PFSqlFieldType.DateTime);
		}
		LOGGER.info(speed.getSpeed(total, com.sellgirl.sgJavaHelper.SGDate.Now()));
	}

	/**
	 * 输出:
	 * 
	 * [main] INFO org.sellgirlPayHelperNotSpring.UncheckDe001 - GMT+08:00 [main]
	 * INFO org.sellgirlPayHelperNotSpring.UncheckDe001 - GMT+08:00 [main] INFO
	 * org.sellgirlPayHelperNotSpring.UncheckDe001 -
	 * sun.util.calendar.ZoneInfo[id="GMT+08:00",offset=28800000,dstSavings=0,useDaylight=false,transitions=0,lastRule=null]
	 * 
	 * @throws Exception
	 */
	public void testTimeZone() throws Exception {
		LOGGER.info(TimeZone.getTimeZone("GMT+8").getDisplayName());
		LOGGER.info(TimeZone.getTimeZone("GMT+8").getID());
		LOGGER.info(TimeZone.getTimeZone("GMT+8").toString());
	}

    /**
     * 注意,想去掉秒的话,把后面4位设置为0是不行的,因为是60000进位的
     * @throws Exception
     */
    public void testTimestamp() throws Exception {
        Calendar cal = Calendar.getInstance();
        String dFormat="yyyy-MM-dd HH:mm:ss.SSS";
        cal.setTimeInMillis(1672996100192L);
        LOGGER.info(String.valueOf(cal.getTimeInMillis()));
        LOGGER.info(SGDataHelper.ObjectToDateString(cal,dFormat));
        cal.set(Calendar.SECOND, 0);
        LOGGER.info(String.valueOf(cal.getTimeInMillis()));
        LOGGER.info(SGDataHelper.ObjectToDateString(cal,dFormat));
        cal.set(Calendar.MILLISECOND, 0);
        LOGGER.info(String.valueOf(cal.getTimeInMillis()));
        LOGGER.info(SGDataHelper.ObjectToDateString(cal,dFormat));

        LOGGER.info("---------------------");
//
//        LOGGER.info(PFDataHelper.ObjectToDateString(1672995373071L,dFormat));
//        LOGGER.info(PFDataHelper.ObjectToDateString(1672995360071L,dFormat));
//        LOGGER.info(PFDataHelper.ObjectToDateString(1672995360000L,dFormat));
        long l=1672996100192L;
        long l2=(l/10000)*10000;
        long l3=(l/100000)*100000;
        LOGGER.info(String.valueOf(l2));
        LOGGER.info(String.valueOf(l3));
        LOGGER.info(SGDataHelper.ObjectToDateString(l,dFormat));
        LOGGER.info(SGDataHelper.ObjectToDateString(l2,dFormat));
        LOGGER.info(SGDataHelper.ObjectToDateString(l3,dFormat));
    }
    public void testTimestamp2() throws Exception {
        String dFormat="yyyy-MM-dd HH:mm:ss.SSS";
        LOGGER.info(SGDataHelper.ObjectToDateString(1670428800000L,dFormat));
        LOGGER.info(SGDataHelper.ObjectToDateString(1670371200000L,dFormat));
    }
    public void testTimestamp3() throws Exception {
        LOGGER.info(String.valueOf(getTimestampLongFromApp("2022-09-09 18:00:00"))); //1662717600000
    }
    public void testTimestamp4() throws Exception {
        String dFormat="yyyy-MM-dd HH:mm:ss.SSS";
        long l=1662717600000L;
        String ds="2022-09-09 18:00:00";
        ZoneId zoneId=ZoneId.of("+08:00");
        LOGGER.info(SGDataHelper.ObjectToDateString(l,dFormat));//2022-09-09 18:00:00.000
        TimeZone timeZone = TimeZone.getTimeZone("GMT+6");
        TimeZone.setDefault(timeZone);
        LOGGER.info(SGDataHelper.ObjectToDateString(l,dFormat));//2022-09-09 16:00:00.000

        //cal的时区没问题,已证实
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(zoneId));
        cal.setTimeInMillis(l);
        assertTrue(cal.getTimeInMillis()==l);
        assertTrue("2022-09-09 18:00:00.000".equals(SGDataHelper.ObjectToDateString(cal,dFormat)));
//        LOGGER.info(String.valueOf(cal.getTimeInMillis()));
//        LOGGER.info(PFDataHelper.ObjectToDateString(cal,dFormat));

//        //看看timestamp有没问题,timestamp的时区好像是和当前程序时区动态保持一致的
//        Timestamp ts =new Timestamp(l);
//        assertTrue(ts.getTime()==l);
//        assertTrue("2022-09-09 18:00:00.000".equals(PFDataHelper.ObjectToDateString(ts,dFormat)));
//        LOGGER.info(PFDataHelper.ObjectToDateString(ts,dFormat));
//        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));//神奇的是,这句竟然会影响ts里面的cdate.zoneinfo属性的值,cdate.zoneinfo属性是动态的?
//        LOGGER.info(PFDataHelper.ObjectToDateString(ts,dFormat));

//        Instant ins=Instant.now(Clock.system(zoneId));
//        ins.
//        Instant instant4 = Instant.ofEpochMilli(5000);
//
//        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG,Locale.CHINA);
//        LOGGER.info(df.format(cal.getTime()));
//
//        SimpleDateFormat sdf = new SimpleDateFormat(dFormat);
//        sdf.setCalendar(cal);//把sdf设置为cal的时区
//        LOGGER.info(sdf.format(cal.getTime()));

//        //Timestamp ts =Timestamp.from(cal.toInstant());
//        LOGGER.info(String.valueOf(ts.getTime()));
//
//        Instant ins=cal.toInstant();
//        ZonedDateTime zdt =ins.atZone(zoneId);
////        Timestamp ts2 =Timestamp.from(ins);
////        LOGGER.info(PFDataHelper.ObjectToDateString(ts2,dFormat));
//
////        SimpleDateFormat sdf = new SimpleDateFormat(dFormat);
////        LOGGER.info(sdf.format(ts));
//
//        LOGGER.info(PFDataHelper.ObjectToDateString(ts,dFormat));
//        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
//        LOGGER.info(PFDataHelper.ObjectToDateString(ts,dFormat));


    }
	public void testTimestampLong() throws Exception {
		long s1=System.currentTimeMillis();
		long s2=System.nanoTime() / 1_000_000;
		long s3=System.nanoTime();
		LOGGER.info(String.valueOf( s1)); //
		LOGGER.info(String.valueOf( s2)); //
		LOGGER.info(String.valueOf( s3)); //
		Thread.sleep(2000);
		LOGGER.info(String.valueOf( System.currentTimeMillis()-s1)); //
		LOGGER.info(String.valueOf( (System.nanoTime() / 1_000_000)-s2)); //和上行的结果是一样的
		LOGGER.info(String.valueOf( System.nanoTime() -s3)); //和上行的结果是一样的
	}

	private long getTimestampLongFromApp(String dateStr) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(dateStr);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}

	public void testAppTime001() throws Exception {
		String dateStr = "2022-09-30 14:04:56";
		long dateLong = getTimestampLongFromApp(dateStr);
		long dateLongRight = 1664546696000L;
		System.out.println(dateStr);
		System.out.println(dateLong);
		System.out.println(new SGDate(new Timestamp(dateLongRight)));
		assertTrue(dateLongRight == dateLong);
	}

	public void testAppLongToDate() throws Exception {
		long dateLong = 1635613765000L;
		System.out.println(dateLong);
		System.out.println(new SGDate(new Timestamp(dateLong)));
	}

	private static int _yearEnum = Calendar.YEAR;
	private static int _monthEnum = Calendar.MONTH;
	private static int _dayEnum = Calendar.DAY_OF_MONTH;
	private static int _hourEnum = Calendar.HOUR_OF_DAY;
	private static int _minuteEnum = Calendar.MINUTE;
	private static int _secondEnum = Calendar.SECOND;

	/**
	 * 周日~周六
	 * 
	 * @throws Exception
	 */
	public void testAppTimeWeekDimention() throws Exception {
		SGDate now = SGDate.Now();
		SGDate begin = SGDate.Now().AddMonths(-1);
		while (begin.compareTo(now) < 0) {
			Calendar date = begin.TClone().ToCalendar();
			int w = date.get(Calendar.DAY_OF_WEEK);
			// date.set(Calendar.WEEK_OF_MONTH, 1);
//            if(w>1) {
//            	
//            }
			SGDate r = w > 1 ? begin.AddDays(-w + 1) : begin;
			// date.set(_dayEnum, 1);
//            date.set(_hourEnum, 0);
//            date.set(_minuteEnum, 0);
//            date.set(_secondEnum, 0);
			// LOGGER.info(begin.toString()+"----"+PFDataHelper.ObjectToDateString(date,PFDataHelper.DateFormat));
			LOGGER.info(begin.toString() + "----" + w + "----"
					+ SGDataHelper.ObjectToDateString(r, SGDataHelper.DateFormat));
			begin = begin.AddDays(1);
		}
	}

	/**
	 * 周一~周天
	 * 
	 * @throws Exception
	 */
	public void testAppTimeWeekDimention2() throws Exception {
		SGDate now = SGDate.Now();
		SGDate begin = SGDate.Now().AddMonths(-1);
		int weekBegin = 2;// 周的开始设置,便于变更, 1周日 2周一 3周二...
		while (begin.compareTo(now) < 0) {
			Calendar date = begin.TClone().ToCalendar();
			int w = date.get(Calendar.DAY_OF_WEEK);// w 是 1(周日)~7(周六)
			// date.set(Calendar.WEEK_OF_MONTH, 1);
//            if(w>1) {
//            	
//            }
			SGDate r = null;
			if (w > weekBegin) {
				r = begin.AddDays(-(w - weekBegin));
			} // 周二以上的调成周一
			else if (w < weekBegin) {
				r = begin.AddDays((-(w - weekBegin) - 7));
			} // 周日调成上星期的周一
			else {
				r = begin;
			}
			Calendar date1 = r.ToCalendar();
			// date1.set(_dayEnum, 1);
			date1.set(_hourEnum, 0);
			date1.set(_minuteEnum, 0);
			date1.set(_secondEnum, 0);
			// LOGGER.info(begin.toString()+"----"+PFDataHelper.ObjectToDateString(date,PFDataHelper.DateFormat));
			LOGGER.info(begin.toString() + "----" + w + "----"
					+ SGDataHelper.ObjectToDateString(r, SGDataHelper.DateFormat));
			begin = begin.AddDays(1);
		}
	}

	/**
	 * 周一~周天 封装
	 * 
	 * @throws Exception
	 */
	public void testAppTimeWeekDimention3() throws Exception {
		SGDate now = SGDate.Now();
		SGDate begin = SGDate.Now().AddMonths(-1);
		int weekBegin = 2;// 周的开始设置,便于变更, 1周日 2周一 3周二...
		while (begin.compareTo(now) < 0) {
			SGDate date = begin.GetWeekStart();
			LOGGER.info(begin.toString() + "----" + begin.GetWeek() + "----"
					+ SGDataHelper.ObjectToDateString(date, SGDataHelper.DateFormat));
			begin = begin.AddDays(1);
		}
	}

	public void testFileSizeFormat() throws Exception {
//		LOGGER.info(PFDataHelper.formatFileSize(2000000, FileSizeUnitType.MB.getBinary()));
//		LOGGER.info(PFDataHelper.formatFileSize(2000000, FileSizeUnitType.GB.getBinary()));
//		LOGGER.info(PFDataHelper.formatFileSize(2000000, FileSizeUnitType.KB.getBinary()));
//
////		int unit=0;
////		unit=FileSizeUnitType.config(unit, FileSizeUnitType.GB, true);
////		unit=FileSizeUnitType.config(unit, FileSizeUnitType.MB, true);
////		unit=FileSizeUnitType.config(unit, FileSizeUnitType.KB, true);
////		LOGGER.info(PFDataHelper.formatFileSize(2000000, unit));
//		
//
////		int unit2=new PFEnumFlag().and(FileSizeUnitType.GB).and(FileSizeUnitType.MB).getBinary();
////		LOGGER.info(PFDataHelper.formatFileSize(2000000, unit2));
////		LOGGER.info(PFDataHelper.formatFileSize(2000000, new PFEnumFlag().and(FileSizeUnitType.GB).and(FileSizeUnitType.MB).and(FileSizeUnitType.KB).getBinary()));
//		
//		LOGGER.info(PFDataHelper.formatFileSize(2000000, FileSizeUnitType.GB.getBinary()|FileSizeUnitType.MB.getBinary()|FileSizeUnitType.KB.getBinary()));

		SGFlagEnum<FileSizeUnitType> unit3 = new SGFlagEnum<FileSizeUnitType>(FileSizeUnitType.GB)
				.or(FileSizeUnitType.MB);
		LOGGER.info(SGDataHelper.formatFileSize(2000000, unit3));
		SGFlagEnum<FileSizeUnitType> unit4 = new SGFlagEnum<FileSizeUnitType>(FileSizeUnitType.GB)
				.or(FileSizeUnitType.MB).or(FileSizeUnitType.KB);
		LOGGER.info(SGDataHelper.formatFileSize(2000000, unit4));
	}

	public void testFileSize() throws Exception {
		LOGGER.info(ByteUtil.bytes2Gb(2000000L).toString());
		LOGGER.info(ByteUtil.bytes2Mb(2000000L).toString());
		LOGGER.info(ByteUtil.bytes2Kb(2000000L).toString());
		LOGGER.info(ByteUtil.bytes2Kb(2000L).toString());
		LOGGER.info(ByteUtil.unit2Byte(new BigDecimal(1L), ByteUtil.KB_SIZE).toString());
//		LOGGER.info(String.valueOf(1<<10));
//		LOGGER.info(String.valueOf(2<<9));
	}

	/**
	 * 测试16进制
	 */
	public void testHexadecimal() {
		int a = 0xFF;
		assertEquals(a == (15 * 16 + 15), true);
	}

    /**
     * 实测updatePs会受到程序时区的影响
     * @throws Exception
     */
    public void testUpdatePs() throws Exception {
        TimeZone timeZone = TimeZone.getTimeZone("GMT+6");
        TimeZone.setDefault(timeZone);//怀疑这个时区会影响jdbc的初始化,事实证明不会

		//IPFJdbc dstJdbc=JdbcHelperTest.GetMySqlTest2Jdbc();
		//IPFJdbc dstJdbc=JdbcHelperTest.GetDayJdbc();
		ISGJdbc dstJdbc=JdbcHelperTest.GetDayJdbc2();
        ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
		//ISqlExecute dstExec = PFSqlExecute.Init(JdbcHelperTest.GetDayJdbc());
		//ISqlExecute dstExec = PFSqlExecute.Init(JdbcHelperTest.GetDayJdbc2());
        //TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));//怀疑这个时区会影响jdbc的初始化
        List<String> fieldNames=new ArrayList<String>();
        fieldNames.add("id");
        fieldNames.add("col1");
        fieldNames.add("col2");
        fieldNames.add("col3");
//        ResultSetMetaData dstMd = dstExec.GetMetaData("test_tb_07", fieldNames);
		ResultSetMetaData dstMd = dstExec.GetMetaDataNotClose("test_tb_07", fieldNames);
        PFSqlInsertCollection insert=dstExec.getInsertCollection(dstMd);
        insert.Set("id",8);
        insert.Set("col1",1662717600000L);
        insert.Set("col2",1662717600000L);
        insert.Set("col3",1662717600000L);
        //TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));//ok
        PreparedStatement ps = dstExec.GetPs("test_tb_07", fieldNames, true);//事实证明,获得ps时的时区,会影响后面插入时间的时区
        //TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        for (int i = 0; i < dstMd.getColumnCount(); i++) {
            int mdIdx = i + 1;
            String colName = dstMd.getColumnLabel(mdIdx);
            SqlUpdateItem dstInsertI = insert.get(colName);
            dstExec.updatePs(ps, dstMd, mdIdx, dstInsertI);
        }
        ps.addBatch();
        ps.executeBatch();
        ps.clearBatch();
        LOGGER.info("插入完成");
    }

	public void testHashCode(){
		LOGGER.info(String.valueOf(new String("").hashCode()));//0
		LOGGER.info(String.valueOf(new String("aa").hashCode()));//3104
		LOGGER.info(String.valueOf(new String("aa").hashCode()));//3104
		LOGGER.info(String.valueOf(new String("ab").hashCode()));//3105
		LOGGER.info(String.valueOf(new String("b1").hashCode()));//3087
		LOGGER.info(String.valueOf(new String("b1").hashCode()));//3087
		LOGGER.info(String.valueOf(new String("bb").hashCode()));//3136
		LOGGER.info(String.valueOf(new String("bb").hashCode()));//3136
	}
	public void testBigDecimal() throws Exception {
		BigDecimal a=new BigDecimal("6.40");
		BigDecimal b=new BigDecimal("3");
		BigDecimal c=a.multiply(b);
		System.out.println(String.valueOf(a.precision())+"_"+String.valueOf(a.scale()));
		System.out.println(String.valueOf(b.precision())+"_"+String.valueOf(b.scale()));
		System.out.println(String.valueOf(c.precision())+"_"+String.valueOf(c.scale()));

	}
	public void testBigDecimal2() throws Exception {
		BigDecimal a=new BigDecimal("1234.56");
		System.out.println(String.valueOf(a.precision())+"_"+String.valueOf(a.scale())+"_"+a.toString());
		a=a.setScale(1,BigDecimal.ROUND_HALF_UP);
		System.out.println(String.valueOf(a.precision())+"_"+String.valueOf(a.scale())+"_"+a.toString());

	}
	public void testHttpGetPageByPage() throws Exception {

		try {
			Type apiResultCls=(new TypeReference<HealthOrdersApiResult<JSONObject>>(){}).getType();
			//if (!PFDataHelper.<SuccessApiResult<HealthOrdersApiResult<T>>, T>HttpGetPageByPage(
			if (!SGDataHelper.<HealthOrdersApiResult<T>, T>HttpGetPageByPage(
					apiResultCls,
					1,
//			        pageNum -> PFDataHelper.FormatString("{0}/mall-rest/perfectdatasync/{4}?pageNum={1}&pageSize={2}&month={3}", _healthBaseUrl, pageNum, _healthPageSize, cmonth,url),
					pageNum -> {
//						String r=PFDataHelper.FormatString("{0}/perfectdatasync/{4}?pageNum={1}&pageSize={2}&month={3}",  ProjHelper.ProjConfig.getHealthBaseUrl(), pageNum, _healthPageSize, cmonth,url);
						//String r="http://aaabbbxxx.com/";
						String r="http://aaabbb.com/";
						return r;
					},

					// res -> (res.getData() != null && res.getData().data != null && res.getData().data.size()>0) ? res.getData().data : null,
					res -> ( res.data != null && res.data.size()>0) ? res.data : null,
					list ->
					{
						int insertCnt=list.size();

					},
					res -> {
					},
					//res -> res.getCode() == 500,
					res -> false,
					"健康订单"
					, a -> a.NoDataError = 1!=SGDate.Now().GetDay()
			))
			{
				throw new RuntimeException(
						"健康订单接口读取失败");
			}
		} catch (Exception e) {
			throw e;
//	        throw new RuntimeException(
//e);
//	        throw new RuntimeException("aaaaaaaaaaaaaaaaaaaa",
//e);
		}
	}
	public void testAppConfig(){

		initPFHelper();
		System.out.println(SGDataHelper.CheckMessageInterval());
	}
	public void testFloatPrecision() throws Exception {
		SGDataHelper.getLog().print(String.valueOf(SGDataHelper.getFloatPrecision(123.1234f,2)));
		SGDataHelper.getLog().print(String.valueOf(SGDataHelper.getFloatPrecision(123.1234f,1)));
		SGDataHelper.getLog().print(String.valueOf(SGDataHelper.getFloatPrecision(123.1234f,0)));
		SGDataHelper.getLog().print(String.valueOf(SGDataHelper.getFloatPrecision(123.1234f,-1)));
		SGDataHelper.getLog().print(String.valueOf(SGDataHelper.getFloatPrecision(123.1234f,-2)));
	}
	public void testObjectToFloat() throws Exception {
		Float aa=Float.valueOf("");//会报错
		SGDataHelper.getLog().print("is null:"+(null==aa));
	}
	
	/**
	 * 去掉重复字符 过滤重复字符
	 * @param str
	 */
	public void testRemoveDuplicates() {
	    HashSet<Character> set = new HashSet<>();
	    StringBuilder result = new StringBuilder();
	    
	    //String path="D:\\gitee\\sgMusicPlayer\\sgMusicPlayer\\assets\\font_cn.txt";
//	    String path="D:\\actiongame2d\\knightsasha\\knightsasha\\assets\\font_cn.txt";
	    String path="D:\\github\\sgGamepadTool\\assets\\font_cn.txt";
	    String str=SGDataHelper.ReadFileToString(path);
	    for (char c : str.toCharArray()) {
	        if (!set.contains(c)) {
	            set.add(c);
	            result.append(c);
	        }
	    }
	    
	    SGDataHelper.SaveStringToFile(result.toString(),path);
	    System.out.println("success");
//	    return result.toString();
	}
//	
//	/**
//	 * 自动备份手机 自动整理手机文件(深度递归子目录)
//	 * 待完成此方法(完成90%，基本可用)
//	 * 
//	 * 备份的关键有：
//	 * 1.全手机的pdf文件
//	 * 2.多个download目录和pictures文件夹
//	 * 3.录像录音等
//	 * 
//	 */
////	@Deprecated
//	public void testAutoBackupPhone() {
//		try {
////			String[] path=new String[] {
//
////			};
////			String[] dstPath=new String[] {
////					"D:\\3\\split"
////			};
//			String[] huaweiPath=new String[] {
////					 "/sdcard/Pictures/"
////					"sdcard/tencent/QQ_Images"
////					"sdcard/Download/"
////					"sdcard/Android/data/com.tencent.mm/MicroMsg/Download/"
////					"sdcard/DCIM/Camera/"
////					"sdcard/Sounds/"
//					"sdcard/Documents/NotePad/"
//			};
//
//			String[] path=new String[] {
//					"D:\\3\\src"
//			};
//			String[] dstPath=new String[] {
//					"D:\\3\\split"
//			};
//			for(int i=0;path.length>i;i++) {
//				HuaweiFileFetcher.main(new String[] {huaweiPath[i],path[i]});
//				if(0==HuaweiFileFetcher.exitCode) {
//					com.sellgirl.sgJavaHelper.file.SGFileSplit.main(new String[] {path[i],dstPath[i]});
//					HuaweiFileFetcherAndDelete.deleteHuaweiFile(huaweiPath[i],"*.*");
//					SGDirectory.cleanDirectory(path[i]);
//					System.out.println("split success");
//				}else {
//					System.out.println("failed:"+HuaweiFileFetcher.exitCode);
//				}
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	public void testAutoBackupPhoneNewFile() {
//		try {
////			String[] path=new String[] {
//
////			};
////			String[] dstPath=new String[] {
////					"D:\\3\\split"
////			};
//			String[] huaweiPath=new String[] {
////					 "/sdcard/Pictures/"
////					"sdcard/tencent/QQ_Images"
////					"sdcard/Download/"
////					"sdcard/Android/data/com.tencent.mm/MicroMsg/Download/"
//					"sdcard/Sounds/"
//			};
//
//			String[] path=new String[] {
//					"D:\\3\\src"
//			};
//			String[] dstPath=new String[] {
//					"D:\\3\\split"
//			};
//			for(int i=0;path.length>i;i++) {	
//				//HuaweiFileFetcher.main(new String[] {huaweiPath[i],path[i]});
//				HuaweiNewFilesFetcher2.main(new String[] {huaweiPath[i],path[i],new SGDate(2025,8,8,1,0,0).toString()});
//				if(1==HuaweiNewFilesFetcher2.statusCode) {
//					com.sellgirl.sgJavaHelper.file.SGFileSplit.main(new String[] {path[i],dstPath[i]});
//				}
//			}
//			System.out.println("split success");
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	public void testAutoSplit() {
//		try {
//
//			String[] path=new String[] {
//					"D:\\3\\src"
//			};
//			String[] dstPath=new String[] {
//					"D:\\3\\split"
//			};
//			for(int i=0;path.length>i;i++) {
//				com.sellgirl.sgJavaHelper.file.SGFileSplit.main(new String[] {path[i],dstPath[i]});	
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	public void testAutoMerge() {
//		try {
//
//			String[] path=new String[] {
//					"D:\\3\\split"
//			};
//			String[] dstPath=new String[] {
//					"D:\\3\\src"
//			};
//			for(int i=0;path.length>i;i++) {
//				com.sellgirl.sgJavaHelper.file.SGFileMerge.main(new String[] {path[i],dstPath[i]});	
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	public void testDeleteHuaweiFile() throws IOException, InterruptedException {		
////		String path="sdcard/Android/data/com.tencent.mm/MicroMsg/Download/";	
//		String path="sdcard/Documents/NotePad/";
////		HuaweiFileFetcherAndDelete.deleteHuaweiFile(path,"*.xlsx");//无效
//		HuaweiFileFetcherAndDelete.deleteHuaweiFile(path,"*.*");//无效，其实删除成功，但在电脑上看到缓存
////		HuaweiFileFetcherAndDelete.deleteHuaweiFile(path,"**.*");
//	}
//	public void testShowHuaweiFolderFileCount() throws IOException, InterruptedException {
//		String[] huaweiPath=getHuaweiImportmantFilePath();
//		for(int i=0;huaweiPath.length>i;i++) {
//			//if()
//            int totalItems = HuaweiFileCounter.countItemsWithLs(huaweiPath[i]);
//            System.out.println("-------------" + huaweiPath[i]+"--------------");
//            System.out.println("总项目数: " + totalItems);
//		}
//
//	}
//	private String[] getHuaweiImportmantFilePath() {//					
//		//"此电脑\\BENJAMIN's mobile\\内部存储\\Pictures",//这种目录读不了File.exists()==false
////		"此电脑\\BENJAMIN's mobile\\内部存储\\Download\\Browser"
////"此电脑\\BENJAMIN's mobile\\内部存储\\Download\\BaiduNetdisk\\来自：百度App"
////"此电脑\BENJAMIN's mobile\内部存储\Download\WeChat"
////"此电脑\BENJAMIN's mobile\内部存储\Download\QQ"
//		//"此电脑\BENJAMIN's mobile\内部存储\Download\QuarkDownloads"
//		//此电脑\BENJAMIN's mobile\内部存储\Documents\NotePad    //备忘录可以导出到这里(这个目录很奇怪的，用win10 explorer打开此路径是空的，但adb命令能读)
//		return new String[] {
//				 "/sdcard/Pictures/",
//				"sdcard/tencent/QQ_Images/",
//				"sdcard/Download/",
//				"sdcard/Quark/Download/",
//				"sdcard/Android/data/com.tencent.mm/MicroMsg/Download/",
////				"sdcard/Android/data/com.tencent.mobileqq/Tencent/QQfile_rec/",//感觉这里移动qq的文件不重要
//				"sdcard/DCIM/Camera/",
//				"sdcard/Sounds/",
//				"sdcard/Documents/NotePad/"
//		};
//	}
}
