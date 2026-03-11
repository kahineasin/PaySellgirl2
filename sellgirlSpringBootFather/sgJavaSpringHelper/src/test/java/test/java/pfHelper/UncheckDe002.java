package test.java.pfHelper;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.express.PFExpressHelper;
import com.sellgirl.sgJavaHelper.model.SysType;
import com.sellgirl.sgJavaHelper.model.UserOrg;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import javax.mail.Message;

import test.java.pfHelper.model.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.sellgirl.sgJavaHelper.*;
import com.sellgirl.sgJavaHelper.config.*;
import com.sellgirl.sgJavaHelper.sql.*;
//import test.java.pfHelper.model.*;
import com.sellgirl.sgJavaHelper.time.SGTimeSpan;
import com.sellgirl.sgJavaHelper.sql.*;

@SuppressWarnings(value = { "unused", "rawtypes", "serial" })
public class UncheckDe002 extends TestCase {
	public static void initPFHelper() {
		SGDataHelper.SetConfigMapper(new PFConfigTestMapper());		
	}
	public void testSmallDatetime() {

		ISGJdbc dstJdbc = GetDayJdbc();
		ISqlExecute dstExec=null;
		try {
			dstExec = SGSqlExecute.Init(dstJdbc);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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

//		// 测试int导入decimal
//		IPFJdbc srcJdbc = this.GetLiGeShopJdbc();
//		IPFJdbc dstJdbc = GetDayJdbc();
//		ISqlExecute srcExec = PFSqlExecute.Init(dstJdbc);
//		ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//		// 使用NString前
//		ResultSet srcDr = srcExec
//				.GetDataReader("select 1 as c1,1 as c2,(CASE 1 WHEN 1 THEN 200 WHEN 2 THEN 800 ELSE NULL END) as c8");
//		// ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,cast((CASE 1
//		// WHEN 1 THEN 200 WHEN 2 THEN 800 ELSE NULL END) as DECIMAL) as c8");//这样ok
//
//		dstExec.Delete("test1", null);
//		dstExec.HugeBulkReader(null, srcDr, "test1", null, null, null);
	}
	public void testClickHouseBulk() {
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
		
		initPFHelper();
		// bulk到ClickHouse
		ISGJdbc srcJdbc = UncheckDe002.GetLiGeShopJdbc();
		ISGJdbc dstJdbc = UncheckDe002.GetClickHouseShopJdbc();
		ISqlExecute srcExec = null;
		try {
			srcExec = SGSqlExecute.Init(srcJdbc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ISqlExecute dstExec = null;
		try {
			dstExec = SGSqlExecute.Init(dstJdbc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String dstTableName="monthly_shop_user";
		// 使用NString前
		ResultSet srcDr = srcExec
				.GetHugeDataReader(("select\r\n" + 
						"Cast('{cmonth}.01' as datetime) as data_date,\r\n" + 
						"a.card_no as user_id,\r\n" + 
						"STR_TO_DATE(if(b.birthday<'1753-01-01 00:00:00',null,b.birthday),'%Y-%m-%d') as birthday,\r\n" + 
						"if(a.gender=1,'男','女') as sex,\r\n" + 
						"b.education,\r\n" + 
						"date_format(a.open_card_time, '%Y.%m') as regist_month, -- 办卡月份\r\n" + 
						"b.province as regist_province,\r\n" + 
						" trim(a.certificates_no) as id_card,\r\n" + 
						" a.mobile, -- 注册手机号 也就是用来登陆的手机号\r\n" + 
						" a.email,\r\n" + 
						" a.user_source, -- 顾客来源 1完美商城 2邀请注册\r\n" + 
						" b.profession,\r\n" + 
						" b.spouse_or_not, -- 是否有配偶：0->否；1->是\r\n" + 
						"\r\n" + 
						" \r\n" + 
						" \r\n" + 
						" \r\n" + 
						" -- b.nationality, -- 国籍  0 中国国籍，1 外国国籍 \r\n" + 
						"-- a.realname as Hyxm,\r\n" + 
						"-- if(a.cancel_status=0,0,1) as Qx,\r\n" + 
						"-- b.spouse_realname as Pexm,\r\n" + 
						"-- b.spouse_certificates_no as Pesfzh,\r\n" + 
						"-- b.emer_contact_home_phone as Tel1,\r\n" + 
						"-- b.emer_contact_mobile as Tel2,\r\n" + 
						"-- c.bank_no as Bankzh,\r\n" + 
						"-- c.bank_open_name as Bankname,\r\n" + 
						"-- a.handled_card_no as bjhybh,\r\n" + 
						"-- '' as l_open_card_shop_id,\r\n" + 
						"-- store_code as agentno,\r\n" + 
						"-- replace(a.cancel_date,'-','.') as qxmonth,\r\n" + 
						"-- b.province  ,\r\n" + 
						"-- b.city  ,\r\n" + 
						"-- b.district as county,\r\n" + 
						"-- b.address  ,\r\n" + 
						"-- STR_TO_DATE(b.birthday,'%Y-%m-%d') as fbirth,\r\n" + 
						"-- open_card_time as fjoindate,\r\n" + 
						"-- bank_open_type as BankNo\r\n" + 
						"-- c.bank_open_type as BankNo ,\r\n" + 
						"-- null as l_user_id,\r\n" + 
						"-- null as l_personal_info_id,\r\n" + 
						"-- card_status as status,\r\n" + 
						"-- mobile  \r\n" + 
						"a.id as sys_limit_id\r\n" + 
						"from mall_center_member.member_info a\r\n" + 
						"left join mall_center_member.member_detail b on b.id=a.id\r\n" + 
						"-- left join mall_center_member.member_provide_bank c on c.member_id =a.id and c.provide_type=2\r\n" + 
						"-- left join mall_center_sys.sys_region d on d.code = c.province\r\n" + 
						"-- left join mall_center_sys.sys_region e on e.code = c.city\r\n" + 
						" where a.card_status<>-3  and a.card_status<>2\r\n" + 
						" -- and a.id>1218860835308504156\r\n" + 
						" -- and a.card_no  in('12638832')\r\n" + 
						" -- limit 700000,28000 -- ok\r\n" + 
						" -- and b.province is not null and b.province <>''\r\n" + 
						" limit 100\r\n" + 
						"\r\n" + 
						"")
						.replace("{cmonth}","2021.04")
						);
		// ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,cast((CASE 1
		// WHEN 1 THEN 200 WHEN 2 THEN 800 ELSE NULL END) as DECIMAL) as c8");//这样ok

		//dstExec.Delete("test1", null);
		dstExec.HugeDelete(dstTableName, where -> {
//  		where.Add("data_date",PFDate.Now().ToCalendar());
		// where.Add("data_date",PFDate.Now().GetDayStart().ToCalendar());
		//where.Add("data_date", transfer.getPFCmonth().ToDateTime());
	});
		//dstExec.HugeInsertReader(null, srcDr,dstTableName, null, null, null);//这样可以，但50000一批时会卡住
		dstExec.HugeBulkReader(null, srcDr,dstTableName, null, null, null);

	}

	public void testBitConvert() {
		initPFHelper();
		// bulk到ClickHouse
		ISGJdbc srcJdbc = UncheckDe002.GetLiGeShopJdbc();
		ISGJdbc dstJdbc = UncheckDe002.GetDayJdbc();
		ISqlExecute srcExec = null;
		try {
			srcExec = SGSqlExecute.Init(srcJdbc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ISqlExecute dstExec = null;
		try {
			dstExec = SGSqlExecute.Init(dstJdbc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String dstTableName="test1";
		// 使用NString前
		ResultSet srcDr = srcExec
				.GetHugeDataReader(("select 1 as c1,1 as c2,b'1' as c9 union select 2 as c1,2 as c2,b'0' as c9")
						
						);
		// ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,cast((CASE 1
		// WHEN 1 THEN 200 WHEN 2 THEN 800 ELSE NULL END) as DECIMAL) as c8");//这样ok

		//dstExec.Delete("test1", null);
		dstExec.HugeDelete(dstTableName, where -> {
//  		where.Add("data_date",PFDate.Now().ToCalendar());
		// where.Add("data_date",PFDate.Now().GetDayStart().ToCalendar());
		//where.Add("data_date", transfer.getPFCmonth().ToDateTime());
	});
		//dstExec.HugeInsertReader(null, srcDr,dstTableName, null, null, null);//这样可以，但50000一批时会卡住
		dstExec.HugeBulkReader(null, srcDr,dstTableName, null, null, null);

	}

	public void testBulk2Mysql() throws Exception {

		ISGJdbc srcJdbc = GetYJQueryJdbc();
		ISGJdbc dstJdbc = GetUserProfileJdbc();

		srcJdbc.setUrl(srcJdbc.getUrl().replace("yjquery", "yjquery202101"));

		ISqlExecute srcExec = null;
		try {
			srcExec = SGSqlExecute.Init(GetYJQueryJdbc());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

			ISGJdbc srcJdbc = GetYJQueryJdbc();
			ISGJdbc dstJdbc = GetUserProfileJdbc();

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
//		ArrayList<Calendar> dl=new ArrayList<Calendar>();
//		int m=2000000;
//		for(int i=0;i<m;i++) {
//			dl.add(PFDate.Now().ToCalendar());
//		}
//
//      	PFDate now =null;
//		Function<Object,Object> c=null;

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

//  	now=PFDate.Now(); 
//	for(Calendar d :dl) {
//		PFDataHelper.ConvertObjectToSqlTypeByPFType(d,PFSqlFieldType.DateTime,java.sql.Types.DATE);
//	}
//	PrintSpeed(now,m);//耗时:67毫秒(0时0分0秒),已插入数:2000000,速度29850746条/秒

//		now =PFDate.Now(); 
//		for(Calendar d :dl) {			
//			if(c==null) {
//				c=PFDataHelper.GetObjectToSqlTypeByPFTypeConverter(d,null,java.sql.Types.DATE);
//			}
//			c.apply(d);
//		}
//		PrintSpeed(now,m);//耗时:128毫秒(0时0分0秒),已插入数:2000000,速度15625000条/秒

	}

	public void testGetTableFields() throws Exception {

		ISGJdbc dstJdbc = GetTidbSaleJdbc();
		// ISqlExecute srcExec = PFSqlExecute.Init(dstJdbc);
		ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
		List<SGSqlFieldInfo> list = dstExec.GetTableFields("hyzl");

		System.out.println(JSON.toJSONString(list));

	}
	public void testTidb() throws Exception {

		ISGJdbc srcJdbc = GetBalanceJdbc();
		ISGJdbc dstJdbc = GetTidbSaleJdbc();
		ISqlExecute srcExec = SGSqlExecute.Init( srcJdbc);
		ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);

		String sql="select top 500000 a.[hybh]\r\n" + 
				"      ,a.[hyxm]\r\n" + 
				"      ,a.[bjhybh]\r\n" + 
				"      ,a.[accmon]\r\n" + 
				"      ,a.rhrq  as [acc_day]\r\n" + 
				"      -- ,lt_t_hyzl.fjoindate  as [acc_day]\r\n" + 
				"      -- ,a.[qx]\r\n" + 
				"      ,CONVERT(bit,a.[qx]) as qx\r\n" + 
				"      ,a.[qxmonth]\r\n" + 
				"      ,GETDATE()  as [qx_day]\r\n" + 
				"      ,a.[hysfzh]\r\n" + 
				"      ,GETDATE() as hy_birthday\r\n" + 
				"      ,0 as Age\r\n" + 
				"      ,a.[pesfzh]\r\n" + 
				"      ,a.[mobile]\r\n" + 
				"      ,a.[pexm]\r\n" + 
				"      ,a.[agentno],\r\n" + 
				"  c.hpos,c.qpos,\r\n" + 
				"  '' as province\r\n" + 
				"from hyzl a \r\n" + 
				"left join p1 c on c.hybh=a.hybh\r\n" + 
				"-- where a.accmon='{cmonth}'";
		ResultSet dr=srcExec.GetHugeDataReader(sql);
		String tbName="hyzl";
		dstExec.TruncateTable(tbName);
		String sql2="show variables LIKE 'tidb_batch_insert' ";
		SGDataTable variablesDt=dstExec.GetDataTable(sql2,null);
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

			//IPFJdbc srcJdbc = GetYJQueryJdbc();
			ISGJdbc dstJdbc = GetTidbSaleJdbc();
			ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
			String tbName="hyzl";
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
			//stmt2.execute(sql2);
			stmt2.execute("set tidb_batch_insert = 1");
			stmt2.execute("set tidb_batch_delete = 1");
			stmt2.execute("update mysql.tidb set variable_value='24h' where variable_name='tikv_gc_life_time'");
			PreparedStatement ps = conn.prepareStatement("show variables LIKE 'tidb_batch_insert' ");
			ResultSet rs = ps.executeQuery();
			rs.next();
			Object aa=rs.getInt(2);

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
				//if (i % 5000 == 0) {
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
		ISGJdbc srcJdbc = GetBalanceJdbc();
		ISGJdbc dstJdbc = GetTidbSaleJdbc();
		ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);
		ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
		String dstTableName="hyzl";
		// 使用NString前
		ResultSet srcDr = srcExec
				.GetHugeDataReader(("select top 10 a.[hybh]\r\n" + 
						"      ,a.[hyxm]\r\n" + 
						"      ,a.[bjhybh]\r\n" + 
						"      ,a.[accmon]\r\n" + 
						"      ,a.rhrq  as [acc_day]\r\n" + 
						"      -- ,lt_t_hyzl.fjoindate  as [acc_day]\r\n" + 
						"      -- ,a.[qx]\r\n" + 
						"      ,CONVERT(bit,a.[qx]) as qx\r\n" + 
						"      ,a.[qxmonth]\r\n" + 
						"      ,GETDATE()  as [qx_day]\r\n" + 
						"      ,a.[hysfzh]\r\n" + 
						"      ,GETDATE() as hy_birthday\r\n" + 
						"      ,0 as Age\r\n" + 
						"      ,a.[pesfzh]\r\n" + 
						"      ,a.[mobile]\r\n" + 
						"      ,a.[pexm]\r\n" + 
						"      ,a.[agentno],\r\n" + 
						"  c.hpos,c.qpos,\r\n" + 
						"  '' as province\r\n" + 
						"from hyzl a \r\n" + 
						"left join p1 c on c.hybh=a.hybh\r\n" + 
						"-- where a.accmon='{cmonth}'\r\n"+
						"-- where a.hybh='00022153'\r\n"+
						" where a.hybh='0000000' and a.agentno='930903' \r\n"
						)
						.replace("{cmonth}","2021.04")
						);
		// ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,cast((CASE 1
		// WHEN 1 THEN 200 WHEN 2 THEN 800 ELSE NULL END) as DECIMAL) as c8");//这样ok

		//dstExec.Delete("test1", null);
//		dstExec.HugeDelete(dstTableName, where -> {
////  		where.Add("data_date",PFDate.Now().ToCalendar());
//		// where.Add("data_date",PFDate.Now().GetDayStart().ToCalendar());
//		//where.Add("data_date", transfer.getPFCmonth().ToDateTime());
//	});
		dstExec.TruncateTable(dstTableName);
		//dstExec.SetInsertOption(a->a.setProcessBatch(10));
		dstExec.HugeInsertReader(null, srcDr,dstTableName, insert->{
			if (insert.Get("hysfzh").Value != null) {
				Calendar hy_birthday = SGDataHelper.IDCardToBirthDay(insert.Get("hysfzh").Value);
				// insert["acc_day"].Value = PFDataHelper.CMonthToDate(insert["accmon"].Value);

				insert.Set("hy_birthday", hy_birthday);
				insert.Set("Age", SGDataHelper.GetAge(hy_birthday));
			}
			if (insert.Get("qxmonth").Value != null) {
				try {
				insert.Set("qx_day", SGDataHelper.CMonthToDate(insert.Get("qxmonth").Value.toString()));
				}catch(Exception e) {
//					System.out.println("qx_day error");
//					System.out.println(insert.Get("qxmonth").Value.toString());
				}
			}
		}, null, null);//这样可以，但50000一批时会卡住
		//dstExec.HugeBulkReader(null, srcDr,dstTableName, null, null, null);

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

	public static ISGJdbc GetYJQueryJdbc() {

		ISGJdbc jdbc = new DayJdbc();
		jdbc.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
		jdbc.setPassword("perfect");
		jdbc.setUsername("sa");
		jdbc.setUrl("jdbc:jtds:sqlserver://10.0.0.11:1433/yjquery");
//		jdbc.setIp("192.168.0.29");
//		jdbc.setPort("1433");
//		jdbc.setDbName("balance");
		return jdbc;
	}
	public static ISGJdbc GetBalanceJdbc() {
		ISGJdbc jdbc = new DayJdbc();
		jdbc.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
		jdbc.setPassword("wm828");
		jdbc.setUsername("sa");
		jdbc.setUrl("jdbc:jtds:sqlserver://172.18.2.132:1433/balance");
////		jdbc.setIp("192.168.0.29");
////		jdbc.setPort("1433");
//		jdbc.setIp("192.168.0.29:1433");
		jdbc.setDbName("balance");
		return jdbc;
	}

	public static ISGJdbc GetDayJdbc() {

		ISGJdbc jdbc = new DayJdbc();
		jdbc.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
		jdbc.setPassword("perfect");
		jdbc.setUsername("sa");
		jdbc.setUrl("jdbc:jtds:sqlserver://192.168.0.29:1433/balance");
		// jdbc.setIp("192.168.0.29");
		// jdbc.setPort("1433");
		jdbc.setIp("192.168.0.29:1433");
		jdbc.setDbName("balance");
		return jdbc;
	}

	public static ISGJdbc GetBonusJdbc() {
		ISGJdbc jdbc = new DayJdbc();
		jdbc.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
		jdbc.setPassword("perfect");
		jdbc.setUsername("sa");
		jdbc.setUrl("jdbc:jtds:sqlserver://192.168.0.29:1433/bonus");
//		jdbc.setIp("192.168.0.29");
//		jdbc.setPort("1433");
		jdbc.setIp("192.168.0.29:1433");
		jdbc.setDbName("bonus");
		return jdbc;
	}

	public static ISGJdbc GetClickHouseShopJdbc() {
		ISGJdbc jdbc = new DayJdbc();
		jdbc.setDriverClassName("ru.yandex.clickhouse.ClickHouseDriver");
		jdbc.setPassword("perfect@ClickHouse");
		jdbc.setUsername("local");
		jdbc.setUrl("jdbc:clickhouse://cloud.perfect99.com:20006/shop_data");//
//		jdbc.setIp("192.168.0.29");
//		jdbc.setPort("1433");
//		jdbc.setDbName("bonus");
		return jdbc;
	}
	public static ISGJdbc GetHiveJdbc() {
		ISGJdbc jdbc = new DayJdbc();
		jdbc.setDriverClassName("org.apache.hive.jdbc.HiveDriver");
		jdbc.setPassword("perfect@Hive");
		jdbc.setUsername("root");
		jdbc.setUrl("jdbc:hive2://uat-cloud.perfect99.com:11005/wm_hive_db");//
//		jdbc.setIp("192.168.0.29");
//		jdbc.setPort("1433");
//		jdbc.setDbName("bonus");
		return jdbc;
	}

//	public static IPFJdbc GetUserProfileJdbc() {//clickhouse如果不能用,主用本地的mysql
//
////      driverClassName: com.mysql.jdbc.Driver
////      password: Ng7_x3_yjK
////      url: jdbc:mysql://172.100.37.88:3306/prod_trade
////      username: prod_trade_ro
////      driverVersion: 5.1.34
//		IPFJdbc srcJdbc=new DayJdbc();
//		srcJdbc.setDriverClassName("com.mysql.cj.jdbc.Driver");
//		srcJdbc.setPassword("perfect");
//		srcJdbc.setUsername("root");
////		srcJdbc.setUrl("jdbc:mysql://192.168.205.111:3306/user_profile");
//		srcJdbc.setUrl("jdbc:mysql://192.168.205.111:3306/user_profile?useSSL=false");		
//		srcJdbc.setDriverVersion("8.0.23");
////		srcJdbc.setIp("192.168.0.29");
////		srcJdbc.setPort("1433");
////		srcJdbc.setDbName("balance");
//		return srcJdbc;
//	}
	public static ISGJdbc GetUserProfileJdbc() {
		ISGJdbc jdbc = new DayJdbc();
		jdbc.setDriverClassName("ru.yandex.clickhouse.ClickHouseDriver");
		jdbc.setPassword("perfect@ClickHouse");
		jdbc.setUsername("local");
		jdbc.setUrl("jdbc:clickhouse://cloud.perfect99.com:20006/user_profile");//
//		jdbc.setIp("192.168.0.29");
//		jdbc.setPort("1433");
//		jdbc.setDbName("bonus");
		return jdbc;

	}

//	public static IPFJdbc GetLiGeShopJdbc() {
//		IPFJdbc srcJdbc = new DayJdbc();
//		srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
//		srcJdbc.setPassword("lige@2020");
//		srcJdbc.setUsername("perfectmall");
//		srcJdbc.setUrl("jdbc:mysql://47.115.118.63:3306/mall_center_store");
////		srcJdbc.setIp("192.168.0.29");
////		srcJdbc.setPort("1433");
////		srcJdbc.setDbName("balance");
//		return srcJdbc;
//	}

	public static ISGJdbc GetLiGeShopJdbc() {
		ISGJdbc srcJdbc = new DayJdbc();
		srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
		srcJdbc.setPassword("perfectMALL");
		srcJdbc.setUsername("root");
		srcJdbc.setUrl("jdbc:mysql://cloud.perfect99.com:10129/mall_center_store");
//		srcJdbc.setIp("192.168.0.29");
//		srcJdbc.setPort("1433");
//		srcJdbc.setDbName("balance");
		return srcJdbc;
	}

	public static ISGJdbc GetLiGeShopUatJdbc() {
		ISGJdbc srcJdbc = new DayJdbc();
		srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
		srcJdbc.setPassword("perfectMALL");
		srcJdbc.setUsername("root");
		// srcJdbc.setUrl("jdbc:mysql://uat-cloud.perfect99.com:10077/mall_center_store");
		srcJdbc.setUrl(
				"jdbc:mysql://uat-cloud.perfect99.com:10077/mall_center_store?useUnicode=true&characterEncoding=utf8");// 这样可以解决
																														// select
																														// '男'
																														// 变成问号的问题
//		srcJdbc.setIp("192.168.0.29");
//		srcJdbc.setPort("1433");
//		srcJdbc.setDbName("balance");
		return srcJdbc;
	}

	public static ISGJdbc GetYunXiShopJdbc() {

//      driverClassName: com.mysql.jdbc.Driver
//      password: Ng7_x3_yjK
//      url: jdbc:mysql://172.100.37.88:3306/prod_trade
//      username: prod_trade_ro
//      driverVersion: 5.1.34
		ISGJdbc srcJdbc = new DayJdbc();
		srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
		srcJdbc.setPassword("Ng7_x3_yjK");
		srcJdbc.setUsername("prod_trade_ro");
		srcJdbc.setUrl("jdbc:mysql://172.100.37.88:3306/prod_trade");
		srcJdbc.setDriverVersion("5.1.34");
//		srcJdbc.setIp("192.168.0.29");
//		srcJdbc.setPort("1433");
//		srcJdbc.setDbName("balance");
		return srcJdbc;
	}

	public static PFJdbcBase GetTidbSaleJdbc() {

//      driverClassName: com.mysql.jdbc.Driver
//      password: Ng7_x3_yjK
//      url: jdbc:mysql://172.100.37.88:3306/prod_trade
//      username: prod_trade_ro
//      driverVersion: 5.1.34
		PFJdbcBase srcJdbc = new DayJdbc();
		srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
		srcJdbc.setPassword("perfectTIDB");
		srcJdbc.setUsername("root");
		srcJdbc.setUrl("jdbc:mysql://172.16.1.246:10010/sale");
		srcJdbc.setSqlType("Tidb");
		// srcJdbc.setDriverVersion("5.1.34");
//		srcJdbc.setIp("192.168.0.29");
//		srcJdbc.setPort("1433");
//		srcJdbc.setDbName("balance");
		return srcJdbc;
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
	 * @throws IllegalAccessException 
	 */
	public void testDifferenceOfClassAndType() throws IllegalAccessException {

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

	public void testCreateSqlTable() throws Exception {
		// 发现mysql8.0.23里,语句中有分号的号不能用execute方法
		String sql = "CREATE TABLE `user_profile_attr_all` ( `user_id` varchar(100) ,`tag_weight` varchar(100) ,`data_date` datetime ,`tag_id` varchar(100) \r\n"
				+ "\r\n"
				+ ",`theme` varchar(100)    ); CREATE INDEX  idx_user_id ON user_profile_attr_all (`user_id`); CREATE INDEX  idx_theme ON \r\n"
				+ "\r\n" + "user_profile_attr_all (`theme`) ";
//		String sql="CREATE TABLE `user_profile_attr_all` ( `user_id` varchar(100) ,`tag_weight` varchar(100) ,`data_date` datetime ,`tag_id` varchar(100),`theme` varchar(100))";
//		String sql1="CREATE INDEX  idx_user_id ON user_profile_attr_all (`user_id`)";
//		String sql2="CREATE INDEX  idx_theme ON user_profile_attr_all (`theme`);";

		ISGJdbc dstJdbc = UncheckDe002.GetUserProfileJdbc();
		ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
		Object r = dstExec.ExecuteSql(new SGSqlCommandString(sql.split(";")));
		assertTrue(r != null);

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

		ISGJdbc srcJdbc = UncheckDe002.GetYJQueryJdbc();
		ISGJdbc dstJdbc = UncheckDe002.GetClickHouseShopJdbc();

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
	public void testCreateHiveTable() throws Exception {
		SGDataHelper.SetConfigMapper(new PFConfigTestMapper());

		ISGJdbc srcJdbc = UncheckDe002.GetLiGeShopJdbc();
		//IPFJdbc dstJdbc = UncheckTest.GetHiveJdbc();
		
		


		String sql = "  select now() as data_date, card_no\r\n" + 
				" from mall_center_member.member_info where card_no is not null and card_no <>'' limit 10";

		SGSqlCreateTableCollection models = new PFHiveSqlCreateTableCollection();
		models.DbName = "wm_hive_db";
		models.TableName = "shop_user1";
		// models.PrimaryKey = new String[] {"data_date","user_id"};
		// models.ClusteredIndex = new String[] {"toYYYYMM(data_date)"} ;
		//models.Order = new String[] { "data_date", "user_id" };
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

	public void testClickHouseDelete() throws Exception {
		SGDataHelper.SetConfigMapper(new PFConfigTestMapper());

		ISGJdbc srcJdbc = UncheckDe002.GetYJQueryJdbc();
		// IPFJdbc dstJdbc=UncheckTest.GetClickHouseShopJdbc();
		ISGJdbc dstJdbc = UncheckDe002.GetUserProfileJdbc();

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
}
