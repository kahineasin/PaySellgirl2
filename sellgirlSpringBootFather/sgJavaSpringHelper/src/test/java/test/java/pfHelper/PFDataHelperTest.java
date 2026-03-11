package test.java.pfHelper;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.mail.Message;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
//import org.sellgirlPayHelper.UncheckTest;
//import org.sellgirlPayHelper.model.DayJdbc;
//import org.sellgirlPayHelper.model.PFConfigTestMapper;

import com.alibaba.fastjson.JSON;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.sellgirl.sgJavaHelper.*;
import com.sellgirl.sgJavaHelper.config.*;
import com.sellgirl.sgJavaHelper.config.SGDataHelper.TrueOrStop;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlTransferItem;
import com.sellgirl.sgJavaHelper.express.PFExpressHelper;
import com.sellgirl.sgJavaHelper.express.PFFormula;
import com.sellgirl.sgJavaHelper.express.PFFormulaItem;
import com.sellgirl.sgJavaHelper.express.PFFormulaItemCollection;
import com.sellgirl.sgJavaHelper.model.SystemUser;
import com.sellgirl.sgJavaHelper.model.UserTypeClass;
import com.sellgirl.sgJavaHelper.task.IPFTimeTask;
//import com.sellgirl.sgJavaHelper.task.PFDayTask;
//import com.sellgirl.sgJavaHelper.task.PFMonthCheckTaskT;
import redis.clients.jedis.Jedis;
import test.java.pfHelper.model.DayJdbc;
import test.java.pfHelper.model.PFConfigTestMapper;

/**
 * Unit test for simple App.
 */
@SuppressWarnings({"unused"})
public class PFDataHelperTest extends TestCase {
//	public static void initPFHelper() {
//		//new PFDataHelper(new PFAppConfig(),null);
//		new PFDataHelper(new PFAppConfig());
//		PFDataHelper.SetConfigMapper(new PFConfigTestMapper());		
//	}
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public PFDataHelperTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(PFDataHelperTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
	}
//
//	public void testCMonth() {
//		String nextCMonth = PFDataHelper.CMonthAddMonths("2020.09", 1);
//
//		assertTrue("2020.10".equals(nextCMonth));
//	}
//
//	public void testDecimal() {
//		BigDecimal d = new BigDecimal(2);
//		Object o = (Object) d;
//		BigDecimal r = PFDataHelper.ObjectToDecimal(o);
//
//		assertTrue(d == r);
//	}
//
//	public void testPFDate() {
//		// 验证月份
//		PFDate now = new PFDate(2020, 6, 1, 0, 0, 0);
//		assertTrue(now.GetMonth() == 6);
//
//		// 日格式验证
//		String aa = PFDate.Now().toString();
//		assertTrue(aa.length() == 19);
//
//		// PerMonth验证
//		now = PFDate.Now().GetMonthStart();
//		DateRange monRange = new DateRange(now.ToCalendar(), now.ToCalendar());
//		List<PFDate> perMonth = monRange.GetPerMonthStart();
//		assertTrue(perMonth.size() == 1);
//
//		// Cmonth转日期验证
//		PFDate ld = new PFDate(PFDataHelper.CMonthToDate("2020.06"));
//		PFDate rd = new PFDate(2020, 6, 1, 0, 0, 0);
//		assertTrue(ld.compareTo(rd) == 0);
//
//		PFDate sd = PFDate.Now().AddMonths(-2);
//		PFDate ed = PFDate.Now();
//		int r = PFDataHelper.compareDate(sd.ToCalendar(), ed.ToCalendar(), 1);
//		assertTrue(r == 2);
//
//		String dateStr = PFDataHelper.ObjectToDateString(PFDataHelper.ObjectToDateTime("1920-10-19"));
//
//		assertTrue(dateStr != null);
//
//	}
//
//	public void testWordsCharLength() {
////		PFCharType aa = PFCharType.Default;
////		PFCharType bb = PFCharType.English;
////		PFCharType cc = PFCharType.Chinese;
//		int len = PFDataHelper.GetWordsCharLength("12312");
//		assertTrue(len == 5);
//	}
//
//	public void testEnum() {
//		SummaryType r = PFDataHelper.ObjectToEnum(SummaryType.class, "Average");
//		assertTrue(r == SummaryType.Average);
//
//		UserTypeClass userT = UserTypeClass.EnumParse(UserTypeClass.class, "Fgs");
//		FuncAuthorityClass funcT = FuncAuthorityClass.EnumParse(FuncAuthorityClass.class, "Export");
//		FuncAuthorityClass funcT1 = funcT.Or(FuncAuthorityClass.Add);
//		assertTrue(userT != null && funcT != null && funcT1 != null && FuncAuthorityClass.Export.getValue() == 1 << 5);
//
//		Map<String, Field> prMap = PFDataHelper.GetProperties(StoreColumn.class);
//		Class<?> pt = prMap.get("_summaryType").getType();
//		@SuppressWarnings({ "unchecked", "rawtypes" })
//		Class<Enum> pt1 = (Class<Enum>) prMap.get("_summaryType").getType();
//		@SuppressWarnings({ "unchecked", "rawtypes" })
//		Object o = PFDataHelper.ObjectToEnum((Class<Enum>) pt, "Average");
//		// Object o1=PFDataHelper.ObjectToEnumNotT(pt, "Average");
//		// Object o2=PFDataHelper.ObjectToEnum(StoreColumn.class, "Average");
//		// Object o3=PFDataHelper.ObjectToEnumNotT(StoreColumn.class, "Average");
//		@SuppressWarnings("unchecked")
//		Object o4 = PFDataHelper.ObjectToEnum(pt1, "Average");
//		assertTrue(o == SummaryType.Average);
//		// assertTrue(o1==SummaryType.Average);
//		assertTrue(o4 == SummaryType.Average);
//	}
//
//	class B extends ArrayList<String> {
//
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = 1L;
//	}
//
//	public void testType() {
//		PFDate now = new PFDate(2020, 6, 1, 0, 0, 0);
//		String r = PFDataHelper.GetStringByType(now.getClass());
//		assertTrue(r.equals("PFDate"));
//		r = PFDataHelper.GetStringByType((Type) (now.getClass()));
//		assertTrue("PFDate".equals(r));
//
//		List<String> a = new ArrayList<String>();
//		B b = new B();
//		String c = "cc";
//		// PFTypeReference ptr=new PFTypeReference<List<String>>() {};
//		PFTypeReference<List<String>> ptrA = new PFTypeReference<List<String>>() {
//		};
//		PFTypeReference<B> ptrB = new PFTypeReference<B>() {
//		};
//		PFTypeReference<String> ptrC = new PFTypeReference<String>() {
//		};
//		// Boolean b=ptr.getType().equals(aa.getClass());
//		assertTrue(ptrA.isInstance(a));
//		assertTrue(ptrA.isInstance(b));
//		assertFalse(ptrA.isInstance(c));
//		assertFalse(ptrB.isInstance(a));
//		assertTrue(ptrB.isInstance(b));
//		assertFalse(ptrB.isInstance(c));
//		assertFalse(ptrC.isInstance(a));
//		assertFalse(ptrC.isInstance(b));
//		assertTrue(ptrC.isInstance(c));
//
//		assertTrue(ptrA.isAssignableFrom(a.getClass()));
//		assertTrue(ptrA.isAssignableFrom(b.getClass()));
//		assertFalse(ptrA.isAssignableFrom(c.getClass()));
//		assertFalse(ptrB.isAssignableFrom(a.getClass()));
//		assertTrue(ptrB.isAssignableFrom(b.getClass()));
//		assertFalse(ptrB.isAssignableFrom(c.getClass()));
//		assertFalse(ptrC.isAssignableFrom(a.getClass()));
//		assertFalse(ptrC.isAssignableFrom(b.getClass()));
//		assertTrue(ptrC.isAssignableFrom(c.getClass()));
//
//		assertFalse(ptrC.isAssignableFrom(ptrA.getType()));
//
//	}
//
//	public void testEmail() {
////		MessageParser.parse(PFEmailSimpleReceiver.fetchInbox(HostType.NETEASE.getProperties(),
////				AuthenticatorGenerator.getAuthenticator("youraccount", "yourpassword")));
//
////		MessageParser.parse(PFEmailSimpleReceiver.fetchInbox(HostType.TENCENT.getProperties(),
////				AuthenticatorGenerator.getAuthenticator("251561897", "wzpfikxpnrdkbjdj")));//javax.mail.AuthenticationFailedException: Please using authorized code to login
//
////		// 测试通过，但部分标题有乱码
////		MessageParser.parse(PFEmailSimpleReceiver.fetchInbox(HostType.PERFECT.getProperties(),
////				AuthenticatorGenerator.getAuthenticator("wxj@perfect99.com", "shi3KjkE48QZ3SPA")));
//
////		//测试通过
////		try {
////			PFEmailSend.sendMail("吸引力", "wxj@perfect99.com", "spring boot 邮件测试");
////		} catch (MessagingException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//
//		// PFEmailRecive aa=new PFEmailRecive();
////		aa.getFrom(message)
////		MailRecive.remail();
//
//		PFEmailManager man = new PFEmailManager(HostType.PERFECT.getProperties(), /*
//																					 * PFEmailSend.
//																					 * EMAIL_OWNER_ADDR_HOST,
//																					 */
//				PFEmailSend.EMAIL_OWNER_ADDR, PFEmailSend.EMAIL_OWNER_ADDR_PASS);
//		man.Connect_Click();
//		Message msg = man.Retrieve_Click(man.MessageCount);
//		PFEmail pfEmail = new PFEmail(msg);
//		// new PFEmailRecive(msg);
//		String cc = pfEmail.getBody();
//		assertTrue(cc != null);
//	}
//
//	public void testNull() {
//		Boolean b = "aa".equals(null);
//		assertTrue(b != null && b == false);
//	}
//
////	public void testSql() {
//////		IPFJdbc dstJdbc = GetDayJdbc();
//////		ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//////		Boolean b = dstExec.ExecuteSql("update test1 set c3='2'");
//////		assertTrue(b != null && b == true);
//////		b = dstExec.ExecuteSql("update test1 set c3='2';select * from test1");
//////		assertTrue(b != null && b == true);
//////		b = dstExec.ExecuteSql("exec p_updateTableByChange 't_hyzl_change','Hybh'");
//////		assertTrue(b != null && b == true);
//////		b = dstExec.ExecuteSql(PFDataHelper.FormatString("exec p_updateTableByChange '{0}','Hybh' ", "t_hyzl_change"));
//////		assertTrue(b != null && b == true);
//////
//////		 dstJdbc = GetLiGeShopJdbc();//uat的有乱码
//////			dstExec = PFSqlExecute.Init(dstJdbc);
//////			Object hyxb = dstExec.QuerySingleValue(PFDataHelper.FormatString("select '男'"));
//////			assertTrue(hyxb.toString().equals("男"));
//////		 dstJdbc = GetLiGeShopUatJdbc();//uat的有乱码
//////			dstExec = PFSqlExecute.Init(dstJdbc);
//////			 hyxb = dstExec.QuerySingleValue(PFDataHelper.FormatString("select \"男\""));
//////			 hyxb = dstExec.QuerySingleValue(PFDataHelper.FormatString("select N'男'"));
//////			 hyxb = dstExec.QuerySingleValue(PFDataHelper.FormatString("select '男'"));
//////			assertTrue(hyxb.toString().equals("男"));
////
//////    	b=dstExec.ExecuteSql("update test2 set c3='2'");
//////    	assertTrue(b!=null&&b==false);
//////    	b=dstExec.ExecuteSql("exec p_updateTableByChange2 't_hyzl_change','Hybh'");
//////    	assertTrue(b!=null&&b==false);
////
////		IPFJdbc dstJdbc = UncheckTest.GetYunXiShopJdbc();
////		ISqlExecute dstExec=null;
////		try {
////			dstExec = PFSqlExecute.Init(dstJdbc);
////		} catch (Exception e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////		Object aa = dstExec.QuerySingleValue("select 1");
////		assertTrue(aa != null);
////	}
//
////	public void testDateTime() {
////		String cmonth = "2019.12";
////		// PFSqlTransferItem transfer=(new yunXi_return()).get();
////		PFSqlTransferItem transfer = new PFSqlTransferItem();
////		transfer.setCmonth(cmonth);
////		// PFCmonth pfCmonth=transfer.getNextPFCmonth();
////		PFCmonth pfCmonth = transfer.getPFCmonth();
////		Calendar cmonthDate = pfCmonth.ToDateTime();
////		String r = PFDataHelper.DateToCMonth(cmonthDate);
////		assertTrue(cmonth.equals(r));
////
////		ISqlExecute srcExec = null;
////		ISqlExecute dstExec = null;
////		ResultSet srcDr = null;
////		ResultSet dstDr = null;
////		try {
////			IPFJdbc srcJdbc = UncheckTest.GetLiGeShopJdbc();
////			srcExec = PFSqlExecute.Init(srcJdbc);
////			IPFJdbc dstJdbc = UncheckTest.GetBonusJdbc();
////			dstExec = PFSqlExecute.Init(dstJdbc);
////			String sql = null;
////			// ----------------------------------------------
//////			// 日期格式转换测试
//////			// java.sql.Date date = null;
//////			java.util.Date date = null;
//////			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//////			date = sdf.parse("2020-11-30 14:49:27");
//////			String s = sdf.format(date);
//////			
//////			String sql = " select update_time from mall_center_member.member_info where card_no='3000000582'";
//////			ResultSet dr = srcExec.GetDataReader(sql);
//////			dr.next();
//////
//////			java.sql.Date liGeSqlDate = dr.getDate("update_time");//无分秒
//////			Calendar liGeSqlCalendar = Calendar.getInstance();
//////			java.sql.Date liGeSqlDate2 = dr.getDate("update_time",liGeSqlCalendar);//值错
//////			java.sql.Timestamp liGeSqlObjStamp = (Timestamp) dr.getObject("update_time");//多一天
//////			java.sql.Timestamp liGeSqlStamp = (java.sql.Timestamp) dr.getTimestamp("update_time");//多一天
//////			java.sql.Timestamp liGeSqlStamp2 =  dr.getTimestamp("update_time",liGeSqlCalendar);//只有这样才是准确且带完整时分秒的
//////			assertTrue(s.equals(sdf.format(liGeSqlStamp2)));
////
////			// ----------------------------------------------
////			// numeric导入测试
////			sql = "select \r\n" + "p.total_price as c1 \r\n"
////					+ "from mall_center_order.order_order o,mall_center_order.order_product p \r\n"
////					+ "where o.id = p.order_id\r\n" + "and o.order_no='SG001006201102000015'";
////			srcDr = srcExec.GetDataReader(sql);
//////				  sql = " select top 1 [c1] from t_ordersdetail";
//////				  dstDr = dstExec.GetDataReader(sql);
//////				  ResultSetMetaData srcMd=srcDr.getMetaData();
//////				  ResultSetMetaData dstMd=dstDr.getMetaData();
////
//////				  //转换正常
//////				  srcDr.next();
//////		    Object o1=srcDr.getObject("c1");
//////		    int srcSqlType=srcMd.getColumnType(1);//3 DECIMAL
//////		    int dstSqlType=dstMd.getColumnType(1);//2 NUMERIC
//////		    int tt=java.sql.Types.NUMERIC;
//////		    PFSqlFieldType dstPFType=PFDataHelper.GetPFTypeBySqlType(dstMd.getColumnType(1));
//////		    Object o2=PFDataHelper.ConvertObjectToPFTypeBySqlType(o1, srcSqlType, dstPFType);//o2相当于InsertItem的Value
//////		    Object o3=PFDataHelper.ConvertObjectToSqlTypeByPFType(o2,dstPFType,dstSqlType);
////
////			// 插入到test1表是38.0,正常
////			dstExec.Delete("test1", null);
////			dstExec.HugeBulkReader(null, srcDr, "test1", null, null, null);
////
//////				 //在测试项目上正常,但在javaTransfer项目上不多10倍Money
//////				 //DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
//////				  sql = "	select o.order_no as Orderno,\r\n" + 
//////				  		"-- order_month as Cmonth,\r\n" + 
//////				  		"CONCAT(left(o.order_month,4),'.',right(o.order_month,2)) as Cmonth,\r\n" + 
//////				  		"p.serial_no as Inv_no,p.title as Inv_name,p.quantity as Qty,\r\n" + 
//////				  		"p.price as Price,\r\n" + 
//////				  		"-- p.pv as Pv,\r\n" + 
//////				  		"CAST(p.pv AS SIGNED) as Pv,\r\n" + 
//////				  		"p.security_price as discount,p.total_price as Money \r\n" + 
//////				  		"from mall_center_order.order_order o,mall_center_order.order_product p \r\n" + 
//////				  		"where o.id = p.order_id\r\n" + 
//////				  		"and o.order_month='202011'\r\n" + 
//////				  		"and o.order_no='SG001006201102000015'\r\n" + 
//////				  		"-- and p.serial_no not in('test-zyf-000001')\r\n" + 
//////				  		"-- limit 0,1\r\n" + 
//////				  		"";
//////				  srcDr = srcExec.GetDataReader(sql);
//////				  dstExec.Delete("t_ordersdetail", null);
//////				  dstExec.HugeBulkReader(null, srcDr, "t_ordersdetail", null,null,null);
////			assertTrue(true);
////
////		} catch (Exception e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} finally {
////			if (srcDr != null) {
////				srcExec.CloseReader(srcDr);
////			}
////			if (dstExec != null) {
////				dstExec.CloseConn();
////			}
////			if (dstDr != null) {
////				dstExec.CloseReader(dstDr);
////			}
////			if (dstExec != null) {
////				dstExec.CloseConn();
////			}
////		}
////	}
//
//	public void testTimeInMillis() {
//		PFDate t = PFDate.Now();
//		int i = 0;
//		long lastTime = 0;
//		while (i++ < 10000) {
//			long m = t.ToCalendar().getTimeInMillis();
//			System.out.print(t.toString());
//			System.out.print("  ----   ");
//			System.out.print(m);
//			System.out.print("  ----   ");
//			System.out.print(m - lastTime);
//			System.out.print("\r\n");
//			lastTime = m;
//			t = t.AddMinutes(1);
//
//		}
//	}
//
//	public void testList() {
//		String[] sArr = new String[] { "55", "66" };
//		List<String> r = PFDataHelper.<String>ObjectToList(sArr);
//		// List<String> r2=r.subList(0, 2);
//		assertTrue(sArr.length == r.size());
//
//		sArr = PFDataHelper.ObjectToArray(r, String.class);
//		assertTrue(sArr.length == r.size());
//	}
//
//	public void testArray() {
//		int[] i = new int[] { 0 };
//		i[0]++;
//		assertTrue(i[0] == 1);
//
//		String[] b = new String[] { "aa" };
//		b[0] = "bb";
//		assertTrue("bb".equals(b[0]));
//	}
//
//	public void testMap() {
//		int cnt = 20000000;// 20000000 50000
//		Object map = new HashMap<String, Object>() {
//			/**
//			* 
//			*/
//			private static final long serialVersionUID = 1L;
//
//			{
//				for (int i = 0; i < cnt; i++) {
//					put(String.valueOf(i), i);
//					// put("22","22");
//				}
//			}
//		};
//		long t = 0;
//
////         t = PFDataHelper.CountTime((o) -> {
////    		Map<String,Object> modelDict2 = PFDataHelper.ObjectAsMap2(map,new HashMap<String,Object>());//0秒/2e7行
////    		System.out.println(modelDict2.size());
////    		assertTrue(modelDict2.size()==cnt);
////        });
////         PrintTime(t);
//
//		t = PFDataHelper.CountTime((o) -> {
//			Map<String, Object> modelDict = PFDataHelper.ObjectAsMap(map);// 14秒/2e7行
//			System.out.println(modelDict.size());
//			assertTrue(modelDict.size() == cnt);
//		});
//		PrintTime(t);
//
////       t = PFDataHelper.CountTime((o) -> {
////   		Map<String,Object> modelDict = PFDataHelper.ObjectAs3(map,new HashMap<String,Object>());//14秒/2e7行
////   		System.out.println(modelDict.size());
////   		assertTrue(modelDict.size()==cnt);
////       });
////       PrintTime(t);
//
//		t = PFDataHelper.CountTime((o) -> {
//			Map<String, Object> modelDict = PFDataHelper.ObjectAs(map);// 14秒/2e7行
//			System.out.println(modelDict.size());
//			assertTrue(modelDict.size() == cnt);
//		});
//		PrintTime(t);
//
////       Class<HashMap<String,Object>>.cast(map);
////       HashMap.class.cast(map);
////       Class cls = (new HashMap<String,Object>()).getClass();
////       HashMap<String,Object> modelDict = cls.cast(map);
//		// HashMap<String,Object> modelDict = (new
//		// HashMap<String,Object>()).getClass().cast(map);
//
////         ((HashMap<String,Object>)map).clear();
//
//	}
//
//	public void testUrl() {
////		String baseUrl="http://uc2-uat.perfect99.com/";
////		String dateStr="2021-01-05 16:04:45";
////		//String url=baseUrl+"esb/api/member/getMemberNum?joinDateBefore="+dateStr;
////		
////		//谷歌自动解码成这样:http://uc2-uat.perfect99.com/esb/api/member/getMemberNum?joinDateBefore=2021-01-05%2016:04:45
////		String urlEncoded=baseUrl+"esb/api/member/getMemberNum?joinDateBefore=2021-01-05%2016:04:45";
////		//String urlEncode=PFDataHelper.getURLEncoderString(url);//这样会对地址中的字符也转码，导致有问题
////		
////		//这种编码结果是:http://uc2-uat.perfect99.com/esb/api/member/getMemberNum?joinDateBefore=2021-01-05+16%3A04%3A45		
////		String urlEncode=baseUrl+"esb/api/member/getMemberNum?joinDateBefore="+PFDataHelper.getURLEncoderString(dateStr);
////		//assertTrue(urlEncoded.equals(urlEncode));
////
////		PFRequestResult r=null;
////		PFRequestResult rEncoded=PFDataHelper.HttpGet(urlEncoded,null);//可以
////		 r=PFDataHelper.HttpGet(urlEncode,null);
////			assertTrue(r.content.equals(rEncoded.content));//虽然编码方式不一样，但结果似乎都成功
////		
//////		 //r=PFDataHelper.HttpGet(baseUrl+"esb/api/member/getMemberNum?joinDateBefore=2021-01-05 16:04:45",null);//报错:Illegal character in query at index 82
//////		 r=PFDataHelper.HttpGet(baseUrl+"esb/api/member/getMemberNum?joinDateBefore=2021-01-05 16:04:45",null);
//////
//////		assertTrue(r!=null&&r.success);
//
//		String sf = "全部";
//		// %e5%85%a8%e9%83%a8
//		// %E5%85%A8%E9%83%A8
//		String encodeSf = PFDataHelper.getURLEncoderString(sf);
//		String decodeSf = PFDataHelper.getURLDecoderString(encodeSf);
//		assertTrue("%E5%85%A8%E9%83%A8".equals(encodeSf));
//		assertTrue(sf.equals(decodeSf));
//
//	}
//
//	public void testDataTable() {
//		PFDataTable dt = GetDemoTable();
//		PFDataTable dt2 = PFDataHelper.DataPager(dt, 0, 10);
//		assertTrue(dt2.Rows.size() == 10);
//	}
//
//	public void testJson() {
//		String s = null;
//		SystemUser ssoUser = null;
//
////		  UserTypeClass a=UserTypeClass.Default;
////		  UserTypeClass b=UserTypeClass.Fgs;
//
////		  UserOrg user=new UserOrg();
////	  	user.Org="02000";
////	  	user.UserType=UserTypeClass.Fgs;
////	  	String userJsonStr=JSON.toJSONString(user);
////	  	UserOrg userJsonObj=JSON.parseObject(userJsonStr,UserOrg.class);
////	  	assertTrue(userJsonObj!=null);
//
////		   s="{\"Email\":\"wxj@perfect99.com\",\"Org\":\"101.06.02\",\"OrgList\":[{\"Org\":\"101.06.02\",\"OrgName\":\"软件开发部\",\"UserType\":\"Default\"},{\"Org\":\"02000\",\"OrgName\":\"广东分公司\",\"UserType\":\"Fgs\"},{\"Org\":\"03000\",\"OrgName\":\"北京分公司\",\"UserType\":\"Fgs\"},{\"Org\":\"14000\",\"OrgName\":\"山西分公司\",\"UserType\":\"Fgs\"}],\"OrgName\":\"软件开发部\",\"UserCode\":\"1712002\",\"UserName\":\"吴肖均\"}";
////		  //String s="{\"Email\":\"wxj@perfect99.com\",\"Org\":\"101.06.02\",\"OrgName\":\"软件开发部\",\"UserCode\":\"1712002\",\"UserName\":\"吴肖均\"}";
////		  ssoUser = JSONObject.parseObject(s,SystemUser.class);
//		// assertTrue(ssoUser!=null);
//
//		s = "{\"Email\":\"wxj@perfect99.com\",\"Org\":\"101.06.02\",\"OrgList\":[{\"Org\":\"101.06.02\",\"OrgName\":\"软件开发部\",\"UserType\":\"Default\"},{\"Org\":\"02000\",\"OrgName\":\"广东分公司\",\"UserType\":\"Fgs\"},{\"Org\":\"03000\",\"OrgName\":\"北京分公司\",\"UserType\":\"Fgs\"},{\"Org\":\"14000\",\"OrgName\":\"山西分公司\",\"UserType\":\"Fgs\"}],\"OrgName\":\"软件开发部\",\"UserCode\":\"1712002\",\"UserName\":\"吴肖均\"}";
//		ssoUser = JSON.parseObject(s, SystemUser.class);
////	      PFDataHelper.WriteLog("test");
////	      PFDataHelper.WriteLog(s);
//		assertTrue(ssoUser != null);
//
//	}
//
//	public void testArraySelect() {
//		DayJdbc[] s = new DayJdbc[] { new DayJdbc() {
//			{
//				setUrl("aa");
//			}
//		} };
//		String[] r = PFDataHelper.ArraySelect(String.class, s, a -> a.getUrl());
//		assertTrue("aa".equals(r[0]));
//	}
//
//	public void testScientificNotation() {
//		String r = PFDataHelper.ScientificNotation(105000);
//		assertTrue("1.05E+005".equals(r));
//	}
//
//	public void testBoolean() {
//		Boolean b = true;
//		assertTrue(boolean.class.equals(b.getClass()));
//	}
//
////	public void testConvert() {
////		try {
////
//////	IPFJdbc dstJdbc = UncheckTest.GetTidbSaleJdbc();
//////	//ISqlExecute srcExec = PFSqlExecute.Init(dstJdbc);
//////	ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//////	Object v=dstExec.QuerySingleValue("select b'1' as c1");
//////
//////	Map<String, Field> prMap =PFDataHelper.GetProperties(SGSqlFieldInfo.class);
//////	Object r=PFDataHelper.ConvertObjectByType(v, v.getClass(), prMap.get("isRequired").getType());
//////
//////	//测试transferTask内部的类型转换
//////	IPFJdbc srcJdbc = UncheckTest.GetLiGeShopJdbc();
//////	 dstJdbc = UncheckTest.GetDayJdbc();
//////	ISqlExecute srcExec = PFSqlExecute.Init(srcJdbc);
//////		 dstExec = PFSqlExecute.Init(dstJdbc);
//////		 ResultSet srcDr = srcExec.GetDataReader("select member_id from mall_center_member.member_provide_bank limit 1");
//////		  String sql = " select top 1 [l_user_id] from [t_hyzl_bank]";
//////		  ResultSet dstDr = dstExec.GetDataReader(sql);
//////		  ResultSetMetaData srcMd=srcDr.getMetaData();
//////		  ResultSetMetaData dstMd=dstDr.getMetaData();
//////
////////		  //转换正常
//////		  srcDr.next();
////////    Object o1=srcDr.getObject("c1");
//////    int srcSqlType=srcMd.getColumnType(1);//3 DECIMAL
////////    int dstSqlType=dstMd.getColumnType(1);//2 NUMERIC
////////    int tt=java.sql.Types.NUMERIC;
//////    PFSqlFieldType dstPFType=PFDataHelper.GetPFTypeBySqlType(dstMd.getColumnType(1));
////////    Object o2=PFDataHelper.ConvertObjectToPFTypeBySqlType(o1, srcSqlType, dstPFType);//o2相当于InsertItem的Value
////////    Object o3=PFDataHelper.ConvertObjectToSqlTypeByPFType(o2,dstPFType,dstSqlType);
////// v=srcDr.getObject("member_id");
//////		Function<Object,Object> ConvertFrom=PFDataHelper.GetObjectToPFTypeBySqlTypeConverter(v, srcSqlType, dstPFType);
//////	Object m=ConvertFrom.apply(v);
////////r=PFDataHelper.ConvertObjectToSqlTypeByPFType("111",PFSqlFieldType.String,java.sql.Types.BIGINT);
//////r=PFDataHelper.ConvertObjectToSqlTypeByPFType(m,dstPFType,dstMd.getColumnType(1));
//////assertTrue(m instanceof Long);
//////assertTrue(r instanceof Long);
////
////			// 日期
////
////			// Object v=dstExec.QuerySingleValue("select b'1' as c1");
////
//////	Map<String, Field> prMap =PFDataHelper.GetProperties(SGSqlFieldInfo.class);
//////	Object r=PFDataHelper.ConvertObjectByType(v, v.getClass(), prMap.get("isRequired").getType());
////
////			// 测试transferTask内部的类型转换
////			IPFJdbc srcJdbc = UncheckTest.GetLiGeShopJdbc();
////			ISqlExecute srcExec = PFSqlExecute.Init(srcJdbc);
////
////			IPFJdbc dstJdbc = UncheckTest.GetBonusJdbc();
////			ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
////
////			ResultSet srcDr = srcExec.GetDataReader(" select\r\n" + "STR_TO_DATE(b.birthday,'%Y-%m-%d') as fbirth\r\n"
////					+ "from mall_center_member.member_info a\r\n"
////					+ "left join mall_center_member.member_detail b on b.id=a.id\r\n"
////					+ "-- left join mall_center_member.member_provide_bank c on c.member_id =a.id and c.provide_type=2\r\n"
////					+ "-- left join mall_center_sys.sys_region d on d.code = c.province\r\n"
////					+ "-- left join mall_center_sys.sys_region e on e.code = c.city\r\n"
////					+ " where a.card_status<>-3  and a.card_status<>2\r\n" + " and a.id>1218860835308504156\r\n"
////					+ " limit 1");
////			ResultSet dstDr = dstExec.GetDataReader(" select top 1 [fbirth] from [t_hyzl]");
////			ResultSetMetaData srcMd = srcDr.getMetaData();
////			ResultSetMetaData dstMd = dstDr.getMetaData();
////
//////		  //转换正常
////			srcDr.next();
////			int srcSqlType = srcMd.getColumnType(1);// 3 DECIMAL
//////    int dstSqlType=dstMd.getColumnType(1);//2 NUMERIC
////
////			PFSqlFieldType dstPFType = PFDataHelper.GetPFTypeBySqlType(dstMd.getColumnType(1));
////
////			Object v = srcDr.getObject("fbirth");
////			Function<Object, Object> ConvertFrom = PFDataHelper.GetObjectToPFTypeBySqlTypeConverter(v, srcSqlType,
////					dstPFType);
////			Object m = ConvertFrom.apply(v);
//////r=PFDataHelper.ConvertObjectToSqlTypeByPFType("111",PFSqlFieldType.String,java.sql.Types.BIGINT);
////			Object r = PFDataHelper.ConvertObjectToSqlTypeByPFType(m, dstPFType, dstMd.getColumnType(1));
////			assertTrue(m instanceof Long);
////			assertTrue(r instanceof Long);
////
//////String aa="aa";
////		} catch (Exception e) {
////		}
////	}
//
//	public void testGuessNumer() {
//		Integer x = 59001234;
//		Integer range = 1000;
//		Pair<Integer, Integer> answer = PFDataHelper.GuessNumber(0, a -> a + 10000, range,
//				(a, b, c) -> (a <= x && b >= x) ? TrueOrStop.IsTrue : TrueOrStop.IsFalse);
//
//		assertTrue(answer.getKey() <= x && answer.getValue() >= x);
//		assertTrue(Math.abs(answer.getKey() - answer.getValue()) <= range);
//	}
//
//	public void testPFRef() {
//		SGRef<AtomicInteger> pfRef = new SGRef<AtomicInteger>(new AtomicInteger(0));
//		pfRef.GetValue().addAndGet(1);
//		pfRef.GetValue().addAndGet(1);
//		assertTrue(pfRef.GetValue().intValue() == 2);
//	}
//
//	public void testAge() {
//		int age = PFDataHelper.GetAge(PFDataHelper.IDCardToBirthDay("222222198409031111"));
//
//		assertTrue(36 == age);
//	}
//
//	public void testDirectNodeGoSync() {
//		/**
//		 * 1 ->2->3 ->5 ->6 ->8 ->4 ->7->9
//		 * 132456798
//		 */
//		DirectNode n1 = new DirectNode("1");
//		DirectNode n2 = new DirectNode("2");
//		DirectNode n3 = new DirectNode("3");
//		DirectNode n4 = new DirectNode("4");
//		DirectNode n5 = new DirectNode("5");
//		DirectNode n6 = new DirectNode("6");
//		DirectNode n7 = new DirectNode("7");
//		DirectNode n8 = new DirectNode("8");
//		DirectNode n9 = new DirectNode("9");
////			n1.next=new ArrayList() {{add(n2);add(n3);}};
////			n2.next=new ArrayList() {{add(n2);add(n3);}};
//		n1.addNext(n3, n4);
//		n3.addNext(n2);
//		n2.addNext(n5);
//		n4.addNext(n5);
//		n5.addNext(n6, n7);
//		// n6.addNext(n8);
//		n7.addNext(n9);
//		// n9.addNext(n8);
//		n8.addPrev(n6, n9);
//
//		SGAction<DirectNode, Integer, Object> ac = (a, b, c) -> {
//			a.finish = true;
//			a.setFinishedPercent(100);
//			System.out.print(a.getHashId());
//
//		};
//		n1.action = a -> ac.go(a, null, null);
//		n2.action = a -> ac.go(a, null, null);
//		n3.action = a -> ac.go(a, null, null);
//		n4.action = a -> ac.go(a, null, null);
//		n5.action = a -> ac.go(a, null, null);
//		n6.action = a -> ac.go(a, null, null);
//		n7.action = a -> ac.go(a, null, null);
//		n8.action = a -> ac.go(a, null, null);
//		n9.action = a -> ac.go(a, null, null);
//
//		System.out.print("\r\n");
//		//PrintObject(n1.getAllNodeFinishedPercent());
////		n1.Go();
////
////		System.out.print("\r\n");
////		PrintObject(n1.getAllNodeFinishedPercent());
////		try {
////			while (!n8.finish) {
////				Thread.sleep(1000);
////				System.out.print("\r\n");
////				PrintObject(n1.getAllNodeFinishedPercent());
////			}
////		} catch (InterruptedException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////		System.out.print("\r\n");
////		PrintObject(n1.getAllNodeFinishedPercent());
//		
//		n1.GoSync(a->{
//			PrintObject(a);
//			});
//
//	}
//
//	public void testDirectNodePackEmpty() {
//		/**
//		 * 1 ->2 ->3 ->5 ->4->6
//		 * 
//		 */
//		DirectNode n1 = new DirectNode("1");
//		DirectNode n2 = new DirectNode("2");
//		DirectNode n3 = new DirectNode("3");
//		DirectNode n4 = new DirectNode("4");
//		DirectNode n5 = new DirectNode("5");
//		DirectNode n6 = new DirectNode("6");
//		DirectNode en = DirectNode.PackEmptyNode();
//		en.addNext(n2, n4);
//		n4.addNext(n6);
//		n1.addNext(en);
//		n3.addPrev(en);
//		n3.addNext(n5);
//
//		SGAction<DirectNode, Integer, Object> ac = (a, b, c) -> {
//			a.finish = true;
//			a.setFinishedPercent(100);
//			System.out.print(a.getHashId());
//
//		};
//		n1.action = a -> ac.go(a, null, null);
//		n2.action = a -> ac.go(a, null, null);
//		n3.action = a -> ac.go(a, null, null);
//		n4.action = a -> ac.go(a, null, null);
//		n5.action = a -> ac.go(a, null, null);
//		n6.action = a -> ac.go(a, null, null);
//
//		System.out.print("\r\n");
//		PrintObject(n1.getAllNodeFinishedPercent());
//		n1.Go();
//
//		System.out.print("\r\n");
//		PrintObject(n1.getAllNodeFinishedPercent());
//		try {
//			while (!n5.finish) {
//				Thread.sleep(1000);
//				System.out.print("\r\n");
//				PrintObject(n1.getAllNodeFinishedPercent());
//			}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.print("\r\n");
//		PrintObject(n1.getAllNodeFinishedPercent());
//
//	}
//
//	public void testWriteError() {
//		PFDataHelper.WriteError(new Throwable(), new Exception("测试错误日志"));
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
////	public void testUpdateCollection() {
////		IPFJdbc dstJdbc = UncheckTest.GetBonusJdbc();
////		ISqlExecute proc=null;
////		try {
////			proc = PFSqlExecute.Init(dstJdbc);
////		} catch (Exception e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////		int qty = PFDataHelper.ObjectToInt0(proc.QuerySingleValue("select count(*) from t_inv"));
////		SqlUpdateCollection update = new SqlUpdateCollection();
////		update.Add("tbname", "test1");
////		update.Add("qty", qty);
////		update.Add("cdate", PFDate.Now().ToCalendar());
////		update.PrimaryKeyFields("tbname");
////		System.out.print(update.ToSetSql());
////	}
//
//	public void testGetBaseDirectory() {
//		System.out.println(System.getProperty("user.dir"));
//////			String p=PFDataHelper.GetBaseDirectory();
//////			String p1=PFDataHelper.GetBaseDirectoryNew();
////			String p=PFDataHelper.GetBaseDirectoryBySpring();
////			String p1=PFDataHelper.GetBaseDirectory();
////	        URL path =Thread.currentThread().getContextClassLoader().getResource("");
////	        String p2=path.getPath();
////	        System.out.println(p);
////	        System.out.println(p1);
////	        System.out.println(p2);
////	        System.out.println(path.getHost());
////	        System.out.println(path.getQuery());
//	}
//
//
//	public void testGetClassByInteface() {
//		 List<Class<?>> jdbcCls=PFDataHelper.getAllAssignedClass(PFConfigTestMapper.class, IPFConfigMapper.class);
//		 System.out.println(JSON.toJSONString(jdbcCls));
//		try {
//			//PFDataHelper.getClassesTest();
//			//PFDataHelper.getClassesTest(SystemUser.class);
//			
//			
////			List<Class<?>> jdbcCls = null;
////			// PFDataHelper.getClasses(SystemUser.class);
////			// System.out.println(JSON.toJSONString(jdbcCls));
////
////			File f = new File(
////					"D:\\\\eclipse_workspace_sellgirlPay\\sellgirlPay\\sellgirlPayHelper\\target\\classes\\pf\\java\\pfHelper\\model");
////			jdbcCls = PFDataHelper.getClasses(f, "pf.java.pfHelper.model");
////			System.out.println(JSON.toJSONString(jdbcCls));
////			f = new File(
////					"D:\\\\eclipse_workspace_sellgirlPay\\sellgirlPay\\sellgirlPayHelper\\target\\pfHelper-0.0.1-SNAPSHOT.jar!\\pf\\java\\pfHelper\\model");
////			jdbcCls = PFDataHelper.getClasses(f, "pf.java.pfHelper.model");
////			System.out.println(JSON.toJSONString(jdbcCls));
////			
////
////			f = new File(
////					"D:/eclipse_workspace_sellgirlPay/sellgirlPay/sellgirlPayHelper/target/classes/pf/java/pfHelper/model");
////			jdbcCls = PFDataHelper.getClasses(f, "pf.java.pfHelper.model");
////			System.out.println(f.exists());//true
////			f = new File(
////					"D:/eclipse_workspace_sellgirlPay/sellgirlPay/sellgirlPayHelper/target/pfHelper-0.0.1-SNAPSHOT.jar!/pf/java/pfHelper/model");
////			jdbcCls = PFDataHelper.getClasses(f, "pf.java.pfHelper.model");
////			System.out.println(f.exists());//true
////			
////
////		      ClassLoader classloader = Thread.currentThread().getContextClassLoader();
////		      InputStream is = classloader.getSystemResourceAsStream("pf/java/pfHelper/model");
////		      java.net.URL url = classloader.getResource("pf/java/pfHelper/model");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	public void testIsNormalDeviation()  {
//		List<Pair<BigDecimal,BigDecimal>> normalMoneyList=new ArrayList<Pair<BigDecimal,BigDecimal>>();
//		normalMoneyList.add(ImmutablePair.of(new BigDecimal(174762373.0),new BigDecimal(176557997.0)));
//		printDeviationList(normalMoneyList,"正常金额偏差数据集",2000000,true);
//		
//		List<Pair<BigDecimal,BigDecimal>> normalPvList=new ArrayList<Pair<BigDecimal,BigDecimal>>();
//		normalPvList.add(ImmutablePair.of(new BigDecimal(146420533.0),new BigDecimal(144890360.0)));
//		printDeviationList(normalPvList,"正常pv偏差数据集",1000,true);
//
//		List<Pair<BigDecimal,BigDecimal>> errorMoneyList=new ArrayList<Pair<BigDecimal,BigDecimal>>();
//		errorMoneyList.add(ImmutablePair.of(new BigDecimal(84676660.0),new BigDecimal(176557997.0)));
//		printDeviationList(errorMoneyList,"异常金额偏差数据集",2000000,false);
//		List<Pair<BigDecimal,BigDecimal>> errorPvList=new ArrayList<Pair<BigDecimal,BigDecimal>>();
//		errorPvList.add(ImmutablePair.of(new BigDecimal(146420533.0),new BigDecimal(71007866.0)));
//		printDeviationList(errorPvList,"异常pv偏差数据集",1000,false);
//	}
//	public void testRedis() {
//		initPFHelper();	
//		Jedis jedis = PFDataHelper.GetConfigMapper().GetRedisConfig().open();
////        jedis.del("aa");
//		if (jedis.setnx("aa", "1") == 1) {
//			System.out.println("redis执行完毕");
//            jedis.del("aa");
//		}
//	}
//	public void testTimeTaskWithRedis()  {
//		initPFHelper();
//		boolean[] b=new boolean[] {false,false,false};
//		PFDate now=PFDate.Now().AddMinutes(1);
//		PFDate now1=now.AddMinutes(1);
//		int housr=now.GetHour();
//		int min=now.GetMinute();
//		PFDayTask t1=new PFDayTask("DayHyData", (cDay, lastBackupTime, task) -> {
//			System.out.println("t1 正在运行");
//			try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.out.println("t1 执行完毕");
//			b[0]=true;
//			return true;
//		}, housr, min,// 每月11日3时执行
//		true,
//		null
//		); 
//		PFDayTask t2=new PFDayTask("DayHyData", (cDay, lastBackupTime, task) -> {
//			System.out.println("t2 正在运行");
//			try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.out.println("t2 执行完毕");
//			b[1]=true;
//			return true;
//		}, housr, min,// 每月11日3时执行
//				true,
//				null
//		); 
//		PFDayTask t3=new PFDayTask("DayHyData", (cDay, lastBackupTime, task) -> {
//			System.out.println("t3正在运行");
//			try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.out.println("t3执行完毕");
//			b[2]=true;
//			return true;
//		}, now1.GetHour(), now1.GetMinute(),// 每月11日3时执行
//				true,
//				null
//		); 
//		t1.Start();
//		t2.Start();
//		t3.Start();
//		try {
//			while(!(b[0]==true&&b[1]==true)) {
//					Thread.sleep(2000);
//			}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
////	public void testMonthTaskWithRedis()  {
////		initPFHelper();
////		boolean[] b=new boolean[] {false,false,false};
////		PFDate now=PFDate.Now().AddMinutes(1);
////		PFDate now1=now.AddMinutes(1);
////		int day=now.GetDay();
////		int housr=now.GetHour();
////		int min=now.GetMinute();
////		String hashId="DayHyData";
////		IPFTimeTask t1=new PFMonthCheckTaskT<Object>(hashId, (cDay, lastBackupTime, task) -> {
////			System.out.println("t1 正在运行");
////			try {
////				Thread.sleep(10000);
////			} catch (InterruptedException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////			System.out.println("t1 执行完毕");
////			b[0]=true;
////			return true;
////		},(cDay, lastBackupTime, task) -> {
////			return true;
////		},day, housr, min,// 每月11日3时执行
////				true,
////				null
////		); 
////		IPFTimeTask t2=new PFMonthCheckTaskT<Object>(hashId, (cDay, lastBackupTime, task) -> {
////			System.out.println("t2 正在运行");
////			try {
////				Thread.sleep(10000);
////			} catch (InterruptedException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////			System.out.println("t2 执行完毕");
////			b[1]=true;
////			return true;
////		},(cDay, lastBackupTime, task) -> {
////			return true;
////		},day, housr, min,// 每月11日3时执行
////				true,
////				null
////		); 
////		IPFTimeTask t3=new PFMonthCheckTaskT<Object>(hashId, (cDay, lastBackupTime, task) -> {
////			System.out.println("t3正在运行");
////			try {
////				Thread.sleep(10000);
////			} catch (InterruptedException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////			System.out.println("t3执行完毕");
////			b[2]=true;
////			return true;
////		},(cDay, lastBackupTime, task) -> {
////			return true;
////		},day, now1.GetHour(), now1.GetMinute(),// 每月11日3时执行
////				true,
////				null
////		); 
////		t1.Start();
////		t2.Start();
////		t3.Start();
////		try {
////			while(!(b[0]==true&&b[1]==true)) {
////					Thread.sleep(2000);
////			}
////		} catch (InterruptedException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////	}
//	/**
//	 * 
//	 * @param normalList
//	 * @param title
//	 * @param validMin
//	 * @param forecastDeviation 预测偏差
//	 */
//	private void printDeviationList(List<Pair<BigDecimal,BigDecimal>> normalList,String title,int validMin,boolean forecastDeviation)  {
//		System.out.println(title);
//		for(Pair<BigDecimal,BigDecimal> i:normalList) {
//			boolean isNormalDeviation=PFDataHelper.IsNormalDeviation(i.getLeft(), i.getRight(), validMin);
//			String msg=i.getLeft().toString()+"->"+i.getRight()
//			+"="+isNormalDeviation
//			+" "+(forecastDeviation==isNormalDeviation?"pass":"notPass");
//			System.out.println(msg);
//			assertTrue(forecastDeviation==isNormalDeviation);
//		}
//	}
//
//	private <T> void PrintObject(T o) {
//		System.out.println(JSON.toJSONString(o));
//	}
//
//	private void PrintTime(long millionSeconds) {
//		System.out.println(PFDataHelper.GetTimeSpan(millionSeconds, null).toString());
//	}
//
//	@SuppressWarnings("unused")
//	private static void PrintSpeed(PFDate begin, int qty) {
//		long diff = PFDate.Now().ToCalendar().getTimeInMillis() - begin.ToCalendar().getTimeInMillis();
//		// System.out.println(String.valueOf(diff));
//		PFTimeSpan ts = PFDataHelper.GetTimeSpan(diff, null);
//		System.out.println(PFDataHelper.FormatString("耗时:{0}毫秒({1}),已插入数:{2},速度{3}条/秒", diff, ts.toString(), qty,
//				qty * 1000 / diff));
//	}
//
//	private static PFDataTable GetDemoTable() {
//
//		PFDataTable t = null;
//		List<PFDataRow> row = new ArrayList<PFDataRow>();// 表所有行集合
//		List<PFDataColumn> col = null;// 行所有列集合
//		PFDataRow r = null; // 单独一行
//		PFDataColumn c = null;// 单独一列
//		// 此处开始循环读数据，每次往表格中插入一行记录
//		for (int i = 0; i < 10; i++) {
//			col = new ArrayList<PFDataColumn>();
//			for (int j = 0; j < 3; j++) {
//				String columnName = String.valueOf(j);
//				Object value = i * j;
//				// 初始化单元列
//				c = new PFDataColumn(columnName, value);
//				// 将列信息加入列集合
//				col.add(c);
//			}
//			// 初始化单元行
//			r = new PFDataRow(col);
//			// 将行信息降入行结合
//			row.add(r);
//
//		}
//		// 得到数据表
//		t = new PFDataTable(row);
//		return t;
//	}
}
