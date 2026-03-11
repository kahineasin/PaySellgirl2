package org.sellgirlPayHelperNotSpring;

import com.alibaba.fastjson.JSON;
import com.sellgirl.sgJavaHelper.PFCmonth;
import com.sellgirl.sgJavaHelper.SGSpeedCounter;
import com.sellgirl.sgJavaHelper.SGSqlCommandString;
import com.sellgirl.sgJavaHelper.PFSqlCommandTimeoutSecond;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGDateRange;
import com.sellgirl.sgJavaHelper.config.PFAppConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.config.SGDataHelper.LocalDataType;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;

import junit.framework.TestCase;
import org.sellgirlPayHelperNotSpring.model.JdbcHelperTest;
import org.sellgirlPayHelperNotSpring.model.PFConfigTestMapper;

import java.sql.ResultSet;
import java.util.List;

/**
 * 不要随便更改此类名,以防打包时执行了此类
 * @author Administrator
 *
 */
@SuppressWarnings(value = { "unused", //"rawtypes", "serial",
		"deprecation" })
public class UncheckBatchAlterSqlTable extends TestCase {
	public static void initPFHelper() {
		SGDataHelper.SetConfigMapper(new PFConfigTestMapper());		
		new SGDataHelper(new PFAppConfig());
	}
	public void testAlterTableByYear() {
		//批量加列
		int startYear=2012;
		int endYear=2022;
		ISGJdbc dstJdbc = JdbcHelperTest.GetTidbSaleJdbc();
		//IPFJdbc dstJdbc = JdbcHelperTest.GetLiGeShopJdbc();
		ISqlExecute dstExec =null;
		try {
			dstExec = SGSqlExecute.Init(dstJdbc);
			for(int i=startYear;i<=endYear;i++) {
//				String sql="alter table `monthly_hyzl_"+i+"` add column hyxm varchar(100) COMMENT '顾客姓名'; ";
				String sql="CREATE INDEX  idx_has_updated ON `monthly_hyzl_"+i+"` (`has_updated`)";
				dstExec.ExecuteSql(new SGSqlCommandString(sql));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}
	public void testAlterTableByYear2() {
		//批量改列
		int startYear=2012;
		//int endYear=2021;
		int endYear=2012;
		ISGJdbc dstJdbc = JdbcHelperTest.GetTidbSaleJdbc();
		//IPFJdbc dstJdbc = JdbcHelperTest.GetLiGeShopJdbc();
		ISqlExecute dstExec =null;
		try {
			dstExec = SGSqlExecute.Init(dstJdbc);
			dstExec.UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());
			for(int i=startYear;i<=endYear;i++) {
				dstExec.ExecuteSql(new SGSqlCommandString(
						//"alter table monthly_hyzl_"+i+" drop column has_updated",
						"alter table monthly_hyzl_"+i+" add column has_updated bit"));
				//dstExec.ExecuteSql(new PFSqlCommandString("alter table monthly_hyzl_"+i+" add column has_updated2 bit"));
				//dstExec.HugeUpdate("update monthly_hyzl_"+i+" set has_updated2=has_updated where has_updated2 is null", null);
				//dstExec.HugeUpdate("update monthly_hyzl_"+i+" set has_updated=1 where has_updated is null", null);
//				dstExec.ExecuteSql(new PFSqlCommandString(
//						//"alter table monthly_hyzl_"+i+" change has_updated has_updated3 varchar(100)",
//						"alter table monthly_hyzl_"+i+" drop column has_updated",
//						"alter table monthly_hyzl_"+i+" change has_updated2 has_updated bit"//,
//						//"alter table monthly_hyzl_"+i+" drop column has_updated3"
//						));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}
//	public void testAlterTableByMonth() {
//		PFCmonth startMonth=new PFCmonth("2011.04");
//		PFCmonth endMonth=new PFCmonth("2012.03");
//		DateRange monRange=new DateRange(startMonth.ToDateTime(),endMonth.ToDateTime());
//		List<PFDate> perMonth = monRange.GetPerMonthStart();
//		IPFJdbc dstJdbc = JdbcHelperTest.GetTidbSaleJdbc();
//		for(PFDate i : perMonth) {
//			//this.PrintObject(i.toYM());
//
//	           int lastMonthYear =new PFDate( i.toPFCmonth().ToDateTime()).AddMonths(-1).GetYear();
//	    	   String lastMonthTableName ="monthly_hyzl_" +String.valueOf( lastMonthYear);
//	    	   String dstTableName ="monthly_hyzl_" +String.valueOf( i.GetYear());
//
//	      	 ISqlExecute sqlExec = PFSqlExecute.Init(dstJdbc);
//		   sqlExec.UpToCommandTimeOut(PFSqlCommandTimeoutSecond.NormalTransfer());
//		   String sql=PFDataHelper.ReadLocalTxt("tidb_monthly_hyzl_after.txt",LocalDataType.Deletable);
//		   sql=PFDataHelper.FormatString(sql, dstTableName, lastMonthTableName, i.toCmonth())
//				   .replace("{active_pv_standard}", String.valueOf(TransferTaskHelper.GetPvStandard(transfer.getNow()).getActivePv()));
//	           sqlExec.HugeUpdate(sql);//月结订单是清全部
////	          	sqlExec.Delete(transfer.DstTableName, where->{
////	          		where.Add("Cmonth",transfer.getPFCmonth().getCmonth());
////	          	});
//		}
//	}
	public void testAlterTableByMonth2() throws Exception {
		PFCmonth startMonth=new PFCmonth("2022.02");
		PFCmonth endMonth=new PFCmonth("2022.02");
		SGDateRange monRange=new SGDateRange(startMonth.ToDateTime(),endMonth.ToDateTime());
		List<SGDate> perMonth = monRange.GetPerMonthStart();
		ISGJdbc srcJdbc = JdbcHelperTest.GetDayJdbc();
		ISGJdbc dstJdbc = JdbcHelperTest.GetBonusJdbc();
		SGSpeedCounter speed=new SGSpeedCounter(SGDate.Now());
		long start=System.currentTimeMillis();
		long delay=5000;
		int cnt=0;
		for(SGDate i : perMonth) {
			//this.PrintObject(i.toYM());

			try (ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
				//int lastMonthYear =new PFDate( i.toPFCmonth().ToDateTime()).AddMonths(-1).GetYear();
				//String lastMonthTableName ="monthly_hyzl_" +String.valueOf( lastMonthYear);
				//String dstTableName ="monthly_hyzl_" +String.valueOf( i.GetYear());
				myResource.AutoCloseConn(false);
				//ISqlExecute sqlExec = PFSqlExecute.Init(dstJdbc);
				String sql=SGDataHelper.ReadLocalTxt("day_orders.txt",LocalDataType.Deletable)
						.replace("{cmonth}","2022.08");
				ResultSet rs=srcExec.GetHugeDataReader(sql);
				while (rs.next()) {
					String agentno=rs.getString("agentno");
					String order_agentno=rs.getString("order_agentno");
					if(null!=order_agentno&&!order_agentno.equals(agentno)){
						myResource.ExecuteSql(new SGSqlCommandString(SGDataHelper.FormatString(
								"update t_orders set order_agentno='{0}' where Orderno='{1}'",
								order_agentno,
								rs.getString("Orderno")
						)));
						if(System.currentTimeMillis()-start>delay){
							System.out.println(speed.getSpeed(cnt,SGDate.Now()));
							start=System.currentTimeMillis();
						}
					}
//					if(System.currentTimeMillis()-start>delay){
//						System.out.println(speed.getSpeed(cnt,PFDate.Now()));
//						start=System.currentTimeMillis();
//					}
					cnt++;
				}
			} catch (Exception e) {
				throw e;
			}
			//String aa="aa";
		}
		System.out.println("testAlterTableByMonth2 finished");
	}
	public void testAlterTableByMonth3() throws Exception {
		PFCmonth startMonth=new PFCmonth("2022.08");
		PFCmonth endMonth=new PFCmonth("2022.08");
		SGDateRange monRange=new SGDateRange(startMonth.ToDateTime(),endMonth.ToDateTime());
		List<SGDate> perMonth = monRange.GetPerMonthStart();
		ISGJdbc srcJdbc = JdbcHelperTest.GetDayJdbc();
		ISGJdbc dstJdbc = JdbcHelperTest.GetBonusJdbc();
		SGSpeedCounter speed=new SGSpeedCounter(SGDate.Now());
		long start=System.currentTimeMillis();
		long delay=5000;
		int cnt=0;
		for(SGDate i : perMonth) {
			//this.PrintObject(i.toYM());

			try (ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
				//int lastMonthYear =new PFDate( i.toPFCmonth().ToDateTime()).AddMonths(-1).GetYear();
				//String lastMonthTableName ="monthly_hyzl_" +String.valueOf( lastMonthYear);
				//String dstTableName ="monthly_hyzl_" +String.valueOf( i.GetYear());
				myResource.AutoCloseConn(false);
				//ISqlExecute sqlExec = PFSqlExecute.Init(dstJdbc);
				String sql=SGDataHelper.ReadLocalTxt("day_orders.txt",LocalDataType.Deletable)
						.replace("{cmonth}",i.toCmonth());
				ResultSet rs=srcExec.GetHugeDataReader(sql);
				SGSqlCommandString updateSql=new SGSqlCommandString();
				while (rs.next()) {
					String agentno=rs.getString("agentno");
					String order_agentno=rs.getString("order_agentno");
					if(null!=order_agentno&&!order_agentno.equals(agentno)){
//						myResource.ExecuteSql(new PFSqlCommandString(PFDataHelper.FormatString(
//								"update t_orders set order_agentno='{0}' where Orderno='{1}'",
//								order_agentno,
//								rs.getString("Orderno")
//						)));
						updateSql.add(SGDataHelper.FormatString(
								"update t_orders set order_agentno='{0}' where Orderno='{1}'",
								order_agentno,
								rs.getString("Orderno")
						));
//						if(System.currentTimeMillis()-start>delay){
//							System.out.println(speed.getSpeed(cnt,PFDate.Now()));
//							start=System.currentTimeMillis();
//						}
					}
					if(System.currentTimeMillis()-start>delay){
						System.out.println(speed.getSpeed(cnt,SGDate.Now()));
						start=System.currentTimeMillis();
					}
					cnt++;
				}
				myResource.ExecuteSql(updateSql);
			} catch (Exception e) {
				throw e;
			}
			//String aa="aa";
		}
		System.out.println(speed.getSpeed(cnt,SGDate.Now()));
		System.out.println("testAlterTableByMonth2 finished");
	}
//	public void testDataReader() {
//		initPFHelper();
//		IPFJdbc dstJdbc = JdbcHelperTest.GetTidbSaleJdbc();
//		//IPFJdbc dstJdbc = JdbcHelperTest.GetLiGeShopJdbc();
//		ISqlExecute dstExec =null;
//		try {
//			dstExec = PFSqlExecute.Init(dstJdbc);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return;
//		}
//		//这里发现个奇怪的问题,tidb读不了 date_add(STR_TO_DATE('2021.10.01','%Y.%m.%d'), INTERVAL -1 year) 列
//		//报错:java.sql.SQLException: Invalid length (10) for type TIMESTAMP 未有解决方法 改为 '%Y-%m-%d %H:%i:%s' 格式才行
////		String sql="select  2 as col1 "+
////		", date_add(STR_TO_DATE('2021.10.01','%Y.%m.%d'), INTERVAL -1 year) as ly_create_date  "+
////		",date_add(STR_TO_DATE('2021-10-01 00:00:00','%Y-%m-%d %H:%i:%s'), INTERVAL -1 year) as ly_create_date2"+
////		",STR_TO_DATE('2021.10.01','%Y.%m.%d') as ly_create_date3";
//
//		String sql="select  "+
//		" date_add(STR_TO_DATE('2021.10.01','%Y.%m.%d'), INTERVAL -1 year) as ly_create_date  "+
//		",date_add(STR_TO_DATE('2021-10-01 00:00:00','%Y-%m-%d %H:%i:%s'), INTERVAL -1 year) as ly_create_date2";
//		
////		Object v=dstExec.QuerySingleValue(sql);
////		String vs=PFDataHelper.ObjectToDateString(v);
////		assertTrue("2021-10-01".equals( vs));
//
//		ResultSet rs=dstExec.GetDataReader(sql);
//		try {
//			if(rs.next()) {
//				int type2=rs.getMetaData().getColumnType(2);
//				String typeName2=rs.getMetaData().getColumnTypeName(2);
//				Object r1=rs.getTimestamp(2);
//				Object r2=rs.getTimestamp(1);
////				//Object r1=rs.getObject(1);
////				//Calendar.getAvailableCalendarTypes();
////				//Object r2=rs.getString(2);
////				//Object r6=rs.getDateTime(2);
////				Object r9=rs.getDate(2);
////				Object r8=rs.getDate(4);
////				Object r5=rs.getTimestamp(2);
////				Object r7=rs.getDate(2);
////				Object r3=rs.getTimestamp(2);
////				Object r4=rs.getObject(2);
//				String aa="aa";
//			}
//			dstExec.CloseReader(rs);
//			dstExec.CloseConn();;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	public void testSmallDatetime() throws Exception {
//
//		IPFJdbc dstJdbc = JdbcHelperTest.GetDayJdbc();
//		ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//		ResultSet rs = dstExec.GetDataReader("select c4,c5 from test1");
//		try {
//			ResultSetMetaData md = rs.getMetaData();
//			int dataTypeI1 = md.getColumnType(1);// 93
//			int dataTypeI2 = md.getColumnType(2);// 93
//			String dataType1 = md.getColumnTypeName(1);// smalldatetime
//			String dataType2 = md.getColumnTypeName(2);// datetime
//			int pre1 = md.getPrecision(1);
//			int pre2 = md.getPrecision(2);
//			int scale1 = md.getScale(1);
//			int scale2 = md.getScale(2);
//			rs.next();
//
//			Object ds = PFDataHelper.ObjectToDateString(rs.getTimestamp(2), "yyyy-MM-dd HH:mm:00");
//			Timestamp ds2 = rs.getTimestamp(2);
//			Calendar c2 = Calendar.getInstance();
//			c2.setTime(ds2);
//			// ds2.setSeconds(0);
//			String aa = "aa";
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//	public void testBulkNString() {
//		initPFHelper();
////	IPFJdbc dstJdbc = GetDayJdbc();
////	ISqlExecute srcExec = PFSqlExecute.Init(dstJdbc);
////	ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
////	//使用NString前
////	ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,'aa' as c6,'中国' as c7");//ok
////	//ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,'中国' as c6,'中国' as c7");//报错:表示列 5 的 Unicode 数据大小的字节数为奇数。应为偶数。
////	//ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,N'中国' as c6,'中国' as c7");//报错:表示列 5 的 Unicode 数据大小的字节数为奇数。应为偶数。
////	//使用updateString后(这个micro库似乎不支持updateNString方法)
////	//ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,'aa' as c6,'中国' as c7");//尚不支持该操作
////	//ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,'中国' as c6,'中国' as c7");//表示列 5 的 Unicode 数据大小的字节数为奇数。应为偶数。
////	//ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,N'中国' as c6,'中国' as c7");//表示列 5 的 Unicode 数据大小的字节数为奇数。应为偶数。
////	
////	  dstExec.Delete("test1", null);
////	  dstExec.HugeBulkReader(null, srcDr, "test1", null,null,null);
//
////		// 测试int导入decimal
////		IPFJdbc srcJdbc = this.GetLiGeShopJdbc();
////		IPFJdbc dstJdbc = GetDayJdbc();
////		ISqlExecute srcExec = PFSqlExecute.Init(dstJdbc);
////		ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
////		// 使用NString前
////		ResultSet srcDr = srcExec
////				.GetDataReader("select 1 as c1,1 as c2,(CASE 1 WHEN 1 THEN 200 WHEN 2 THEN 800 ELSE NULL END) as c8");
////		// ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,cast((CASE 1
////		// WHEN 1 THEN 200 WHEN 2 THEN 800 ELSE NULL END) as DECIMAL) as c8");//这样ok
////
////		dstExec.Delete("test1", null);
////		dstExec.HugeBulkReader(null, srcDr, "test1", null, null, null);
//	}
//	public void testClickHouseBulk() throws Exception {
////		System.out.print("    SELECT  \r\n" + 
////				"     ORDINAL_POSITION fieldIdx,\r\n" + 
////				"      COLUMN_NAME fieldName, -- 列名,  \r\n" + 
////				"      if(COLUMN_KEY='PRI',b'1',b'0')  isPrimaryKey,\r\n" + 
////				"       COLUMN_TYPE 数据类型,  \r\n" + 
////				"        DATA_TYPE fieldType, -- 字段类型,  \r\n" + 
////				"      CHARACTER_MAXIMUM_LENGTH fieldSqlLength, -- 长度,  \r\n" + 
////				"      -- IS_NULLABLE 是否为空,  \r\n" + 
////				"      if(IS_NULLABLE='YES',b'0',b'1')  isRequired,\r\n" + 
////				"      COLUMN_DEFAULT defaultValue, -- 默认值,  \r\n" + 
////				"      COLUMN_COMMENT columnDescription -- 备注   \r\n" + 
////				"    FROM  \r\n" + 
////				"     INFORMATION_SCHEMA.COLUMNS  \r\n" + 
////				"    where  \r\n" + 
////				"    -- developerclub为数据库名称，到时候只需要修改成你要导出表结构的数据库即可  \r\n" + 
////				"    -- table_schema ='cbbk'  \r\n" + 
////				"    -- AND  \r\n" + 
////				"    -- article为表名，到时候换成你要导出的表的名称  \r\n" + 
////				"    -- 如果不写的话，默认会查询出所有表中的数据，这样可能就分不清到底哪些字段是哪张表中的了，所以还是建议写上要导出的名名称  \r\n" + 
////				"    table_name  = '{0}' ");
//		
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
//
//	}
//
//	public void testBitConvert() throws Exception {
//		initPFHelper();
//		// bulk到ClickHouse
//		IPFJdbc srcJdbc = JdbcHelperTest.GetLiGeShopJdbc();
//		IPFJdbc dstJdbc = JdbcHelperTest.GetDayJdbc();
//		ISqlExecute srcExec = PFSqlExecute.Init(srcJdbc);
//		ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//		String dstTableName="test1";
//		// 使用NString前
//		ResultSet srcDr = srcExec
//				.GetHugeDataReader(("select 1 as c1,1 as c2,b'1' as c9 union select 2 as c1,2 as c2,b'0' as c9")
//						
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
//
//	}
//
//	public void testBulk2Mysql() throws Exception {
//
//		IPFJdbc srcJdbc = JdbcHelperTest.GetYJQueryJdbc();
//		IPFJdbc dstJdbc = JdbcHelperTest.GetUserProfileJdbc();
//
//		srcJdbc.setUrl(srcJdbc.getUrl().replace("yjquery", "yjquery202101"));
//
//		ISqlExecute srcExec = PFSqlExecute.Init(JdbcHelperTest.GetYJQueryJdbc());
//		ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//		// 使用NString前
//		ResultSet srcDr = srcExec.GetDataReader("SELECT  hybh AS [user_id],'' as tag_weight,\r\n"
//				+ "CONVERT(datetime,CONVERT(varchar(10),GETDATE(),120)) AS data_date,\r\n"
//				+ "'ATTR_U_02_001' AS tag_id ,'sex' as theme\r\n" + "FROM hyzl WHERE qx=0 and hyxb='男'");// ok
//		// ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,'中国' as
//		// c6,'中国' as c7");//报错:表示列 5 的 Unicode 数据大小的字节数为奇数。应为偶数。
//		// ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,N'中国' as
//		// c6,'中国' as c7");//报错:表示列 5 的 Unicode 数据大小的字节数为奇数。应为偶数。
//		// 使用updateString后(这个micro库似乎不支持updateNString方法)
//		// ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,'aa' as
//		// c6,'中国' as c7");//尚不支持该操作
//		// ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,'中国' as
//		// c6,'中国' as c7");//表示列 5 的 Unicode 数据大小的字节数为奇数。应为偶数。
//		// ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,N'中国' as
//		// c6,'中国' as c7");//表示列 5 的 Unicode 数据大小的字节数为奇数。应为偶数。
//
//		dstExec.Delete("user_profile_attr_all", null);
//		dstExec.HugeBulkReader(null, srcDr, "user_profile_attr_all", null, null, null);
//
//	}
//
//	/**
//	 * 实测效率 Wed Mar 17 11:23:08 CST 2021 Wed Mar 17 11:27:33 CST 2021 行数:1000000
//	 * 耗时:0时4分26秒
//	 */
//	public void testBulk2Mysql2() {
//		try {
//
//			IPFJdbc srcJdbc = JdbcHelperTest.GetYJQueryJdbc();
//			IPFJdbc dstJdbc = JdbcHelperTest.GetUserProfileJdbc();
//
//			Connection conn;
//			PreparedStatement stmt;
//
////     String driver = "com.mysql.jdbc.Driver";
////     String url = "jdbc:mysql://localhost:3306/techstars";
////     String user = "root";
////     String password = "123456";
////     String sql = "insert into test  (id,name,address)  values (?,?,?)";
//
//			String driver = dstJdbc.getDriverClassName();
//			String url = dstJdbc.getUrl();
//			String user = dstJdbc.getUsername();
//			String password = dstJdbc.getPassword();
//			String sql = "insert into user_profile_attr_all  (user_id,data_date,theme)  values (?,?,?)";
//
//			long[] taskBeginTime = new long[] { PFDate.Now().ToCalendar().getTimeInMillis() };
//			// String sql = "insert into test values (?,?,?),(?,?,?),(?,?,?)";
//			// Class.forName(driver);
//			// conn = DriverManager.getConnection(url, user, password);
//			conn = PFSqlExecute.Init(dstJdbc).GetConn();
//			stmt = (PreparedStatement) conn.prepareStatement(sql);
//			// 关闭事务自动提交 ,这一行必须加上
//			conn.setAutoCommit(false);
//			System.out.println(new Date());
//			/* —————————————————————————— */
//			// ------- 1000,000条 ---------
//			// Wed Jul 29 20:22:49 CST 2020
//			// Wed Jul 29 20:25:06 CST 2020
//			// 耗时;137秒 平均7299条/秒
//			for (int i = 1; i <= 1000000; i++) {
//				stmt.setString(1, i + "_t");
//				stmt.setString(2, "2021-02-01 00:00:00");
//				stmt.setString(3, "member");
//				stmt.addBatch();
//				if (i % 5000 == 0) {
//					stmt.executeBatch();
//					conn.commit();
//				}
//			}
//			System.out.println(new Date());
//			System.out.println(PFDataHelper.FormatString(" 耗时:{0} \r\n",
//					PFDataHelper.GetTimeSpan(PFDate.Now().ToCalendar().getTimeInMillis() - taskBeginTime[0], null)));
//
//			try {
//				if (stmt != null) {
//					stmt.close();
//				}
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (SQLException e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//		} catch (Exception e) {
//
//		}
//
//	}
//
////这种方法未测试通过,好像要引用com.mysql.jdbc.PreparedStatement
////public void testBulk2Mysql3() {
////try {
////
////	IPFJdbc srcJdbc = GetYJQueryJdbc();
////	IPFJdbc dstJdbc = GetUserProfileJdbc();
////	
////	 Connection conn;
////     PreparedStatement stmt;
////     
//////     String driver = "com.mysql.jdbc.Driver";
//////     String url = "jdbc:mysql://localhost:3306/techstars";
//////     String user = "root";
//////     String password = "123456";
//////     String sql = "insert into test  (id,name,address)  values (?,?,?)";
////     
////     String driver = dstJdbc.getDriverClassName();
////     String url = dstJdbc.getUrl();
////     String user = dstJdbc.getUsername();
////     String password = dstJdbc.getPassword();
////     //String sql = "insert into user_profile_attr_all  (user_id,data_date,theme)  values (?,?,?)";
////		String sql = "LOAD DATA LOCAL INFILE 'sql.csv' IGNORE INTO TABLE user_profile_attr_all (user_id,data_date,theme)";
////
////		long[] taskBeginTime=new long[] {PFDate.Now().ToCalendar().getTimeInMillis()};
////     //String sql = "insert into test values (?,?,?),(?,?,?),(?,?,?)";
////     Class.forName(driver);
////     conn = DriverManager.getConnection(url, user, password);
////     stmt = (PreparedStatement) conn.prepareStatement(sql);
////     // 关闭事务自动提交 ,这一行必须加上
////     //conn.setAutoCommit(false);
////     System.out.println(new Date());
////     /* —————————————————————————— */
////     // ------- 1000,000条 ---------
////		StringBuilder builder = new StringBuilder();
////		for (int i = 1; i <= 1000000; i++) {
////			builder.append(i + "_t");
////			builder.append("\t");
////			builder.append( "2021-02-01 00:00:00");
////			builder.append("\t");
////			builder.append( "member");
////			builder.append("\n");
////
//////			for (int j = 0; j <= 10000; j++) {
//////
//////				builder.append(4);
//////				builder.append("\t");
//////				builder.append(4 + 1);
//////				builder.append("\t");
//////				builder.append(4 + 2);
//////				builder.append("\t");
//////				builder.append(4 + 3);
//////				builder.append("\t");
//////				builder.append(4 + 4);
//////				builder.append("\t");
//////				builder.append(4 + 5);
//////				builder.append("\n");
//////			}
////		}
////		byte[] bytes = builder.toString().getBytes();
////		InputStream is = new ByteArrayInputStream(bytes);
////
////		com.mysql.jdbc.PreparedStatement mysqlStatement = statement
////				.unwrap(com.mysql.jdbc.PreparedStatement.class);
////
////		mysqlStatement.setLocalInfileInputStream(is);
////		 mysqlStatement.executeUpdate();
////     System.out.println(new Date());
////     System.out.println(PFDataHelper.FormatString(
//// 			" 耗时:{0} \r\n",
//// 			PFDataHelper.GetTimeSpan(PFDate.Now().ToCalendar().getTimeInMillis() - taskBeginTime[0], null)
//// 			));
////
////     try {
////         if (stmt != null) {
////             stmt.close();
////         }
////         if (conn != null) {
////             conn.close();
////         }
////     } catch (SQLException e) {
////         // TODO: handle exception
////         e.printStackTrace();
////     }
////}catch(Exception e) {
////	
////}
////	
////}
//
//	public void testConvertSpeed() {
////		ArrayList<Calendar> dl=new ArrayList<Calendar>();
////		int m=2000000;
////		for(int i=0;i<m;i++) {
////			dl.add(PFDate.Now().ToCalendar());
////		}
////
////      	PFDate now =null;
////		Function<Object,Object> c=null;
//
////		now =PFDate.Now(); 
////		for(Calendar d :dl) {			
////			if(c==null) {
////				c=PFDataHelper.GetObjectToPFTypeBySqlTypeConverter(d,-1,PFSqlFieldType.DateTime);
////			}
////			c.apply(d);
////		}
////		PrintSpeed(now,m);//耗时:129毫秒,已插入数:2000000,速度15503875条/秒
////	   
////		
////
////		
////      	now=PFDate.Now(); 
////		for(Calendar d :dl) {
////			PFDataHelper.ConvertObjectToPFTypeBySqlType(d,-1,PFSqlFieldType.DateTime);
////		}
////		PrintSpeed(now,m);//耗时:671毫秒(0时0分0秒),已插入数:2000000,速度2980625条/秒
//
////      	now=PFDate.Now(); 
////  		//System.out.println(PFDate.Now().ToCalendar().getTimeInMillis());
////		for(Calendar d :dl) {
////			PFDataHelper.ConvertObjectToSqlTypeByPFType(d,null,java.sql.Types.TIMESTAMP);
////		}
////  		//System.out.println(PFDate.Now().ToCalendar().getTimeInMillis());
////		PrintSpeed(now,m);//耗时:197毫秒(0时0分0秒),已插入数:2000000,速度10152284条/秒
//
////		now =PFDate.Now(); 
////		for(Calendar d :dl) {			
////			if(c==null) {
////				c=PFDataHelper.GetObjectToSqlTypeByPFTypeConverter(d,null,java.sql.Types.TIMESTAMP);
////			}
////			c.apply(d);
////		}
////		PrintSpeed(now,m);//耗时:286毫秒(0时0分0秒),已插入数:2000000,速度6993006条/秒
//
////  	now=PFDate.Now(); 
////	for(Calendar d :dl) {
////		PFDataHelper.ConvertObjectToSqlTypeByPFType(d,PFSqlFieldType.DateTime,java.sql.Types.DATE);
////	}
////	PrintSpeed(now,m);//耗时:67毫秒(0时0分0秒),已插入数:2000000,速度29850746条/秒
//
////		now =PFDate.Now(); 
////		for(Calendar d :dl) {			
////			if(c==null) {
////				c=PFDataHelper.GetObjectToSqlTypeByPFTypeConverter(d,null,java.sql.Types.DATE);
////			}
////			c.apply(d);
////		}
////		PrintSpeed(now,m);//耗时:128毫秒(0时0分0秒),已插入数:2000000,速度15625000条/秒
//
//	}
//
//	public void testGetTableFields() throws Exception {
//
//		IPFJdbc dstJdbc = JdbcHelperTest.GetTidbSaleJdbc();
//		// ISqlExecute srcExec = PFSqlExecute.Init(dstJdbc);
//		ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//		List<SGSqlFieldInfo> list = dstExec.GetTableFields("hyzl");
//
//		System.out.println(JSON.toJSONString(list));
//
//	}
//	public void testTidb() throws Exception {
//
//		IPFJdbc srcJdbc = JdbcHelperTest.GetBalanceJdbc();
//		IPFJdbc dstJdbc = JdbcHelperTest.GetTidbSaleJdbc();
//		ISqlExecute srcExec = PFSqlExecute.Init( srcJdbc);
//		ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//
//		String sql="select top 500000 a.[hybh]\r\n" + 
//				"      ,a.[hyxm]\r\n" + 
//				"      ,a.[bjhybh]\r\n" + 
//				"      ,a.[accmon]\r\n" + 
//				"      ,a.rhrq  as [acc_day]\r\n" + 
//				"      -- ,lt_t_hyzl.fjoindate  as [acc_day]\r\n" + 
//				"      -- ,a.[qx]\r\n" + 
//				"      ,CONVERT(bit,a.[qx]) as qx\r\n" + 
//				"      ,a.[qxmonth]\r\n" + 
//				"      ,GETDATE()  as [qx_day]\r\n" + 
//				"      ,a.[hysfzh]\r\n" + 
//				"      ,GETDATE() as hy_birthday\r\n" + 
//				"      ,0 as Age\r\n" + 
//				"      ,a.[pesfzh]\r\n" + 
//				"      ,a.[mobile]\r\n" + 
//				"      ,a.[pexm]\r\n" + 
//				"      ,a.[agentno],\r\n" + 
//				"  c.hpos,c.qpos,\r\n" + 
//				"  '' as province\r\n" + 
//				"from hyzl a \r\n" + 
//				"left join p1 c on c.hybh=a.hybh\r\n" + 
//				"-- where a.accmon='{cmonth}'";
//		ResultSet dr=srcExec.GetHugeDataReader(sql);
//		String tbName="hyzl";
//		dstExec.TruncateTable(tbName);
//		String sql2="show variables LIKE 'tidb_batch_insert' ";
//		PFDataTable variablesDt=dstExec.GetDataTable(sql2,null);
//		boolean b = dstExec.HugeBulkReader(null, dr, tbName,
////                (a) -> {
////                	a.setProcessBatch(50000) ;
////                	//if(insertOptionAction!=null) {insertOptionAction.accept(a);}
////                },
//			null, // transferItem == null ? null :
//								// transferItem.BeforeInsertAction,
//			(already) -> {
//			}, null);
//
//	}
//
//	public void testBulk2Tidb() {
//		try {
//
//			//IPFJdbc srcJdbc = GetYJQueryJdbc();
//			IPFJdbc dstJdbc = JdbcHelperTest.GetTidbSaleJdbc();
//			ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//			String tbName="hyzl";
//			dstExec.TruncateTable(tbName);
//
//			Connection conn;
//			PreparedStatement stmt;
//
////     String driver = "com.mysql.jdbc.Driver";
////     String url = "jdbc:mysql://localhost:3306/techstars";
////     String user = "root";
////     String password = "123456";
////     String sql = "insert into test  (id,name,address)  values (?,?,?)";
//
//			String driver = dstJdbc.getDriverClassName();
//			String url = dstJdbc.getUrl();
//			String user = dstJdbc.getUsername();
//			String password = dstJdbc.getPassword();
//			String sql = "insert into hyzl  (hybh)  values (?)";
//
//			long[] taskBeginTime = new long[] { PFDate.Now().ToCalendar().getTimeInMillis() };
//			// String sql = "insert into test values (?,?,?),(?,?,?),(?,?,?)";
//			// Class.forName(driver);
//			// conn = DriverManager.getConnection(url, user, password);
//			conn = PFSqlExecute.Init(dstJdbc).GetConn();
//
////			String sql2="set tidb_batch_insert = 1;\r\n" + 
////					"set tidb_batch_delete = 1;\r\n" + 
////					"update mysql.tidb set variable_value='24h' where variable_name='tikv_gc_life_time';\r\n" + 
////					"update mysql.tidb set variable_value='30m' where variable_name='tikv_gc_life_time';";
//			Statement stmt2 = conn.createStatement();
//			//stmt2.execute(sql2);
//			stmt2.execute("set tidb_batch_insert = 1");
//			stmt2.execute("set tidb_batch_delete = 1");
//			stmt2.execute("update mysql.tidb set variable_value='24h' where variable_name='tikv_gc_life_time'");
//			PreparedStatement ps = conn.prepareStatement("show variables LIKE 'tidb_batch_insert' ");
//			ResultSet rs = ps.executeQuery();
//			rs.next();
//			Object aa=rs.getInt(2);
//
//			stmt = (PreparedStatement) conn.prepareStatement(sql);
//			// 关闭事务自动提交 ,这一行必须加上
//			conn.setAutoCommit(false);
//			System.out.println(new Date());
//			/* —————————————————————————— */
//			// ------- 1000,000条 ---------
//			// Wed Jul 29 20:22:49 CST 2020
//			// Wed Jul 29 20:25:06 CST 2020
//			// 耗时;137秒 平均7299条/秒
//			for (int i = 1; i <= 1000000; i++) {
//				stmt.setString(1, i + "_t");
////				stmt.setString(1, i + "_t");
////				stmt.setString(2, "2021-02-01 00:00:00");
////				stmt.setString(3, "member");
//				stmt.addBatch();
//				//if (i % 5000 == 0) {
//				if (i % 500 == 0) {
//					stmt.executeBatch();
//					conn.commit();
//				}
//			}
//			System.out.println(new Date());
//			System.out.println(PFDataHelper.FormatString(" 耗时:{0} \r\n",
//					PFDataHelper.GetTimeSpan(PFDate.Now().ToCalendar().getTimeInMillis() - taskBeginTime[0], null)));
//
//			try {
//				if (stmt != null) {
//					stmt.close();
//				}
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (SQLException e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//	public void testTidbBulkInsert() throws Exception {
//
//		long[] taskBeginTime = new long[] { PFDate.Now().ToCalendar().getTimeInMillis() };
//		initPFHelper();
//		// bulk到ClickHouse
//		IPFJdbc srcJdbc = JdbcHelperTest.GetBalanceJdbc();
//		IPFJdbc dstJdbc = JdbcHelperTest.GetTidbSaleJdbc();
//		ISqlExecute srcExec = PFSqlExecute.Init(srcJdbc);
//		ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//		String dstTableName="hyzl";
//		// 使用NString前
//		ResultSet srcDr = srcExec
//				.GetHugeDataReader(("select top 10 a.[hybh]\r\n" + 
//						"      ,a.[hyxm]\r\n" + 
//						"      ,a.[bjhybh]\r\n" + 
//						"      ,a.[accmon]\r\n" + 
//						"      ,a.rhrq  as [acc_day]\r\n" + 
//						"      -- ,lt_t_hyzl.fjoindate  as [acc_day]\r\n" + 
//						"      -- ,a.[qx]\r\n" + 
//						"      ,CONVERT(bit,a.[qx]) as qx\r\n" + 
//						"      ,a.[qxmonth]\r\n" + 
//						"      ,GETDATE()  as [qx_day]\r\n" + 
//						"      ,a.[hysfzh]\r\n" + 
//						"      ,GETDATE() as hy_birthday\r\n" + 
//						"      ,0 as Age\r\n" + 
//						"      ,a.[pesfzh]\r\n" + 
//						"      ,a.[mobile]\r\n" + 
//						"      ,a.[pexm]\r\n" + 
//						"      ,a.[agentno],\r\n" + 
//						"  c.hpos,c.qpos,\r\n" + 
//						"  '' as province\r\n" + 
//						"from hyzl a \r\n" + 
//						"left join p1 c on c.hybh=a.hybh\r\n" + 
//						"-- where a.accmon='{cmonth}'\r\n"+
//						"-- where a.hybh='00022153'\r\n"+
//						" where a.hybh='0000000' and a.agentno='930903' \r\n"
//						)
//						.replace("{cmonth}","2021.04")
//						);
//		// ResultSet srcDr = srcExec.GetDataReader("select 1 as c1,1 as c2,cast((CASE 1
//		// WHEN 1 THEN 200 WHEN 2 THEN 800 ELSE NULL END) as DECIMAL) as c8");//这样ok
//
//		//dstExec.Delete("test1", null);
////		dstExec.HugeDelete(dstTableName, where -> {
//////  		where.Add("data_date",PFDate.Now().ToCalendar());
////		// where.Add("data_date",PFDate.Now().GetDayStart().ToCalendar());
////		//where.Add("data_date", transfer.getPFCmonth().ToDateTime());
////	});
//		dstExec.TruncateTable(dstTableName);
//		//dstExec.SetInsertOption(a->a.setProcessBatch(10));
//		dstExec.HugeInsertReader(null, srcDr,dstTableName, insert->{
//			if (insert.Get("hysfzh").Value != null) {
//				Calendar hy_birthday = PFDataHelper.IDCardToBirthDay(insert.Get("hysfzh").Value);
//				// insert["acc_day"].Value = PFDataHelper.CMonthToDate(insert["accmon"].Value);
//
//				insert.Set("hy_birthday", hy_birthday);
//				insert.Set("Age", PFDataHelper.GetAge(hy_birthday));
//			}
//			if (insert.Get("qxmonth").Value != null) {
//				try {
//				insert.Set("qx_day", PFDataHelper.CMonthToDate(insert.Get("qxmonth").Value.toString()));
//				}catch(Exception e) {
////					System.out.println("qx_day error");
////					System.out.println(insert.Get("qxmonth").Value.toString());
//				}
//			}
//		}, null, null);//这样可以，但50000一批时会卡住
//		//dstExec.HugeBulkReader(null, srcDr,dstTableName, null, null, null);
//
//		System.out.println(new Date());
//		System.out.println(PFDataHelper.FormatString(" 耗时:{0} \r\n",
//				PFDataHelper.GetTimeSpan(PFDate.Now().ToCalendar().getTimeInMillis() - taskBeginTime[0], null)));
//	}
//	
//
//	private void PrintTime(long millionSeconds) {
//		System.out.println(PFDataHelper.GetTimeSpan(millionSeconds, null).toString());
//	}
//
//	private static void PrintSpeed(PFDate begin, int qty) {
//		long diff = PFDate.Now().ToCalendar().getTimeInMillis() - begin.ToCalendar().getTimeInMillis();
//		// System.out.println(String.valueOf(diff));
//		PFTimeSpan ts = PFDataHelper.GetTimeSpan(diff, null);
//		System.out.println(PFDataHelper.FormatString("耗时:{0}毫秒({1}),已插入数:{2},速度{3}条/秒", diff, ts.toString(), qty,
//				qty * 1000 / diff));
//	}
//
//
//
//	class T {
//		public List<String> a = new ArrayList<String>();
//		public B b = new B();
//		public C c = new C();
//		public D<String> d = new D<String>();
//		int e;
////		List l ;
//		Map<Integer, String> map = new HashMap<Integer, String>();
//	}
//
//	// class A {}
//	class B extends ArrayList<String> {
//	}
//
//	class C extends B {
//	}
//
//	class D<TT> {
//
//	}
//
//	private static void printClassInfo(Type cls) {
//		System.out.println(cls.toString());
//		System.out.println(cls.getClass().toString());
////		System.out.println(cls.getClass().getSuperclass());	
////		System.out.println(cls.getClass().getGenericSuperclass());		
//
//		if (cls instanceof ParameterizedType) {
//			System.out.println("is ParameterizedType");
//		} else {
//			System.out.println("not ParameterizedType");
//		}
//		if (cls instanceof Class) {
//			Class cls1 = (Class) cls;
//			System.out.println("is class,isPrimitive:" + cls1.isPrimitive());
//
////			if(cls1.getDeclaredClasses().length>0) {
////				System.out.println(cls1.getAnnotatedSuperclass()()[0]);
////			}
//			// System.out.println(cls1.getAnnotatedSuperclass());
//
//			Class superCls = cls1.getSuperclass();
//			System.out.println("super: " + superCls);
//			if (superCls != null) {
//				System.out.println(superCls.getClass());
//				System.out.println(superCls.getClass().getClass());
//			}
//			Type gSuperClass = cls1.getGenericSuperclass();
//			System.out.println("generic super: " + gSuperClass);
//			if (gSuperClass != null) {
//				System.out.println(gSuperClass.getClass());
//				System.out.println(gSuperClass.getClass().getClass());
//				if (gSuperClass instanceof Class) {
//					System.out.println("gSuperClass is Class");
//				} else {
//					System.out.println("gSuperClass not Class");
//				}
//			}
////			System.out.println();
//		} else {
//			System.out.println("not class");
//		}
//
////        ParameterizedType argType = (ParameterizedType) ((ParameterizedType) cls).getActualTypeArguments()[0];	
////		System.out.println(argType.getRawType());
////		System.out.println(argType.getOwnerType());
//	}
//
//	/**
//	 * 测试Class和Type的区别
//	 */
//	public void testDifferenceOfClassAndType() {
//
//		Map<String, Type> result = new LinkedHashMap<String, Type>();
//		TypeReference tr = new TypeReference<java.util.List<String>>() {
//		};
//		result.put("TypeReference<java.util.List<String>>", tr.getType());
//		tr = new TypeReference<B>() {
//		};
//		result.put("TypeReference<B>", tr.getType());
//
////		List<String> aa=new ArrayList<String>();
////		String bb="bb";
////		//PFTypeReference ptr=new PFTypeReference<List<String>>() {};
////		PFTypeReference ptr=new PFTypeReference<List<String>>() {};
////		PFTypeReference ptr2=new PFTypeReference<String>() {};
////		//Boolean b=ptr.getType().equals(aa.getClass());
////		Boolean b=ptr.IsTypeOf(aa);
////		assertTrue(ptr.IsTypeOf(aa));
////		assertFalse(ptr.IsTypeOf(bb));
////		assertFalse(ptr2.IsTypeOf(aa));
////		assertTrue(ptr2.IsTypeOf(bb));
////		String aaa="aa";
//
//		Class tc = T.class;
//		T to = new T();
//		Field[] fields = tc.getDeclaredFields();
//		for (Field f : fields) {
//			Class fc = f.getType();
//			result.put("field-- " + f.getName(), fc);
//			Type fgc = f.getGenericType();
//			result.put("field GenericType-- " + f.getName(), fgc);
////			System.out.println("field-- "+f.getName());
////			printClassInfo(fc);
//////			System.out.println(fc.getClass().toString());
//////			System.out.println(fc.getClass().getGenericSuperclass().toString());			
//////			System.out.println(fc.toString());
//			try {
//				Object v = f.get(to);
//				result.put("field value-- " + f.getName(), v.getClass());
////				printClassInfo(v.getClass());
//////				System.out.println(v.getClass().getClass().toString());
//////				System.out.println(v.getClass().toString());
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
////			if(fc.isPrimitive()){
////				System.out.println("基本数据类型： " + f.getName() + "  " + fc.getName());
////			}else{
////				if(fc.isAssignableFrom(List.class)){ //判断是否为List
////					System.out.println("List类型：" + f.getName());
////					Type gt = f.getGenericType();	//得到泛型类型
////					ParameterizedType pt = (ParameterizedType)gt;
////					Class lll = (Class)pt.getActualTypeArguments()[0];
////					System.out.println("\t\t" + lll.getName());
////				}
////			}
//		}
//
//		Iterator<Entry<String, Type>> iter = result.entrySet().iterator();
//		while (iter.hasNext()) {
//			Entry<String, Type> key = iter.next();
//			System.out.println(key.getKey());
//			printClassInfo(key.getValue());
//			System.out.println("---------------------");
//		}
//
////		   //事实证明ArrayList<T>.getClass().newInstance()得到的类型可以转为任意的T类型
////			ArrayList<Integer> aaa=null;
////			try {
////				aaa=(ArrayList<Integer>) to.a.getClass().newInstance();
////				aaa.add(111);
////			} catch (InstantiationException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			} catch (IllegalAccessException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////			String bbb="aa";
//	}
//
//	public void testJsonSerial() {
//		String aa = JSON.toJSONString(new TestSerialModel());
//		assertTrue(aa.indexOf("bb") > 0);
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
//	}
//	public void testJsonDeserial() {
//		TestSerialModel3<TestSerialModel2> o1=new TestSerialModel3<TestSerialModel2>();
//		o1.kk=new TestSerialModel2();
//		String aa = JSON.toJSONString(o1);
//		this.PrintObject(aa);
//		
//		TestSerialModel3<TestSerialModel2> o2 = JSON.parseObject(aa, new TypeReference<TestSerialModel3<TestSerialModel2>>(){}) ;
//
//		this.PrintObject(o2);
//		//assertTrue(aa.indexOf("bb") > 0);
////		aa = JSON.toJSONString(new TestSerialModel2());
////		assertTrue(aa.indexOf("bb") > 0);
////		assertTrue(aa.indexOf("cc") < 0);
////		assertTrue(aa.indexOf("dd") > 0);
////		assertTrue(aa.indexOf("ee") < 0);
////		assertTrue(aa.indexOf("ff") > 0);
////		assertTrue(aa.indexOf("gg") < 0);
////		assertTrue(aa.indexOf("hh") > 0);
////		assertTrue(aa.indexOf("ii") < 0);
////		// assertTrue(aa.indexOf("jj")<0);
////		// com.fasterxml.jackson.annotation.JsonValue
//	}
//	public void testJsonDeserial2() {
//		TestSerialModel3<TestSerialModel2> o1=new TestSerialModel3<TestSerialModel2>();
//		o1.kk=new TestSerialModel2();
//		
//		TestSerialModel4<TestSerialModel3<TestSerialModel2>> o2=new TestSerialModel4<TestSerialModel3<TestSerialModel2>>();
//		o2.ll=o1;
//		String aa = JSON.toJSONString(o2);
//		this.PrintObject(aa);
//		
//		TestSerialModel4<TestSerialModel3<TestSerialModel2>> o3 = JSON.parseObject(aa, new TypeReference<TestSerialModel4<TestSerialModel3<TestSerialModel2>>>(){}) ;
//
//		this.PrintObject(o3);
//		//assertTrue(aa.indexOf("bb") > 0);
////		aa = JSON.toJSONString(new TestSerialModel2());
////		assertTrue(aa.indexOf("bb") > 0);
////		assertTrue(aa.indexOf("cc") < 0);
////		assertTrue(aa.indexOf("dd") > 0);
////		assertTrue(aa.indexOf("ee") < 0);
////		assertTrue(aa.indexOf("ff") > 0);
////		assertTrue(aa.indexOf("gg") < 0);
////		assertTrue(aa.indexOf("hh") > 0);
////		assertTrue(aa.indexOf("ii") < 0);
////		// assertTrue(aa.indexOf("jj")<0);
////		// com.fasterxml.jackson.annotation.JsonValue
//	}
//
//	public void testJsonDeserial3() {
////		TestSerialModel3<TestSerialModel2> o1=new TestSerialModel3<TestSerialModel2>();
////		o1.kk=new TestSerialModel2();
////		
////		TestSerialModel4<TestSerialModel3<TestSerialModel2>> o2=new TestSerialModel4<TestSerialModel3<TestSerialModel2>>();
////		o2.ll=o1;
////		String aa = JSON.toJSONString(o2);
////		this.PrintObject(aa);
//		
//		String aa="{\"aa\":\"aa\",\"bb\":\"bb\",\"dd\":\"dd\",\"ff\":\"ff\",\"hh\":\"hh\",\"ll\":{\"aa\":\"aa\",\"bb\":\"bb\",\"dd\":\"dd\",\"ff\":\"ff\",\"hh\":\"hh\",\"kk\":{\"aa\":\"aa\",\"bb\":\"bb\",\"dd\":\"dd\",\"ff\":\"ff\",\"hh\":\"hh\"}}}";
//		TestSerialModel4<TestSerialModel3<TestSerialModel2>> o3 = JSON.parseObject(aa, new TypeReference<TestSerialModel4<TestSerialModel3<TestSerialModel2>>>(){}) ;
//
//		this.PrintObject(o3);
//		//assertTrue(aa.indexOf("bb") > 0);
////		aa = JSON.toJSONString(new TestSerialModel2());
////		assertTrue(aa.indexOf("bb") > 0);
////		assertTrue(aa.indexOf("cc") < 0);
////		assertTrue(aa.indexOf("dd") > 0);
////		assertTrue(aa.indexOf("ee") < 0);
////		assertTrue(aa.indexOf("ff") > 0);
////		assertTrue(aa.indexOf("gg") < 0);
////		assertTrue(aa.indexOf("hh") > 0);
////		assertTrue(aa.indexOf("ii") < 0);
////		// assertTrue(aa.indexOf("jj")<0);
////		// com.fasterxml.jackson.annotation.JsonValue
//	}
//
//	public void testJsonDeserial4() {
//		TestSerialModel3<TestSerialModel2> o1=new TestSerialModel3<TestSerialModel2>();
//		o1.kk=new TestSerialModel2();
//		
//		TestSerialModel5<TestSerialModel3<TestSerialModel2>> o2=new TestSerialModel5<TestSerialModel3<TestSerialModel2>>();
//		//o2.ll=o1;
//		o2.ll=new ArrayList<TestSerialModel3<TestSerialModel2>>();
//		o2.ll.add(o1);
//		String aa = JSON.toJSONString(o2);
//		this.PrintObject(aa);
//		
//		TestSerialModel5<TestSerialModel3<TestSerialModel2>> o3 = JSON.parseObject(aa, new TypeReference<TestSerialModel5<TestSerialModel3<TestSerialModel2>>>(){}) ;
//
//		this.PrintObject(o3);
//
//	}
//	public void testJsonDeserial5() {
//		String aa = "{\"DataSource\":[{\"DatamodelQueryId\":\"90\",\"ReqAvgUseMs\":\"2439\",\"Id\":\"96bbda7c-8dfe-461f-9ea0-f78269334682\",\"DatamodelQueryName\":\"促销活动\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IGEuaWQsYS5vcmRlcl9zdGF0dXMsYS5vcmRlcl9tb250aCxiLm9yZGVyX2lkLFxyXG5jYXNlIFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0ZTJyB0aGVuICcxLeaWsOWTgeS4iuW4gu+8mkZT5LiA55Sf57OWJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0ZTNicgdGhlbiAnMi3mlrDlk4HkuIrluILvvJpGUzbkuIDnlJ/ns5YnXHJcbiAgd2hlbiBiLnNlcmlhbF9ubz0nRlMzMCcgdGhlbiAnMy3mlrDlk4HkuIrluILvvJpGUzMw5rS756uL5aSaJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0FDQzIwMjInIHRoZW4gJzctQUND5LyY5oOg5aWX6KOFJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J1NQQ1MwMicgdGhlbiAnNi3lpI/lraPkuKrmiqTlpZfoo4Ut55S35aOrJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J1NQQ1MwMScgdGhlbiAnNC3lpI/lraPkuKrmiqTlpZfoo4Ut5aWz5aOrJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0VUMjAyMicgdGhlbiAnNS3niLHmlr3ok5PlqbTnq6XmtJfmiqTlpZfoo4UnXHJcbiAgZW5kIGFzICfmtLvliqjlkI3np7AnLFxyXG5zdW0oYi5xdWFudGl0eSkgYXMgJ+mUgOWUrumHjycsc3VtKGIudG90YWxfcHJpY2UpIGFzICfplIDllK7pop0nLFxyXG5jYXNlIFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0FDQzIwMjInIHRoZW4gc3VtKGIucXVhbnRpdHkpLzI1MDAwXHJcbiAgd2hlbiBiLnNlcmlhbF9ubz0nU1BDUzAyJyB0aGVuIHN1bShiLnF1YW50aXR5KS8yMDg1MFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J1NQQ1MwMScgdGhlbiBzdW0oYi5xdWFudGl0eSkvMTM1ODBcclxuICB3aGVuIGIuc2VyaWFsX25vPSdFVDIwMjInIHRoZW4gc3VtKGIucXVhbnRpdHkpLzQ1MDBcclxuICBlbmQgYXMgJ+ebruagh+WujOaIkOeOhycgXHJcbmZyb21cclxuKHNlbGVjdCBpZCxvcmRlcl9zdGF0dXMsb3JkZXJfbW9udGggZnJvbSBvcmRlcl9vcmRlcikgYVxyXG5qb2luXHJcbihzZWxlY3Qgb3JkZXJfaWQsc2VyaWFsX25vLHF1YW50aXR5LHRvdGFsX3ByaWNlIGZyb20gb3JkZXJfcHJvZHVjdCkgYlxyXG5vbiAoYS5pZD1iLm9yZGVyX2lkKVxyXG53aGVyZSBhLm9yZGVyX3N0YXR1cyBpbiAoMiwzLDk5KSBhbmQgYS5vcmRlcl9tb250aCA+PScyMDIyMDQnIGFuZCBiLnNlcmlhbF9ubyBpbiAoJ0FDQzIwMjInLCdTUENTMDInLCdTUENTMDEnLCdFVDIwMjInLCdGUycsJ0ZTNicsJ0ZTMzAnKVxyXG5ncm91cCBieSBiLnNlcmlhbF9ub1xyXG5vcmRlciBieSDmtLvliqjlkI3np7AiLCJ0ZW1wbGF0ZS10YWdzIjp7fX0sImRhdGFiYXNlIjoxM30=\",\"QueryType\":\"native\",\"Description\":\"促销活动\",\"Time\":\"2022-05-07 08:49:51\",\"UpdateTime\":\"2022-05-28 15:55:27\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"101\",\"ReqAvgUseMs\":\"169\",\"Id\":\"413e56b9-18fa-4e85-9b16-5a8cee9c3387\",\"DatamodelQueryName\":\"本月办卡人数及升级方式\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IFxyXG4gIGNhc2UgXHJcbiAgICB3aGVuIHByb21vdGlvbl90eXBlPTEgdGhlbiAnMjk556S85YyFJ1xyXG4gICAgd2hlbiBwcm9tb3Rpb25fdHlwZT0yIHRoZW4gJzYwMHB2J1xyXG4gICAgd2hlbiBwcm9tb3Rpb25fdHlwZSBpcyBudWxsIHRoZW4gJ+acquWNh+e6pydcclxuICAgIGVuZCBhcyAn5Yqe5Y2h5Lq65pWw5Y+K5Y2H57qn5pa55byPJyxjb3VudCgqKSBhcyAn5Lq65pWwJ1xyXG5mcm9tIG1lbWJlcl9pbmZvXHJcbndoZXJlIG9wZW5fY2FyZF90aW1lPj0nMjAyMi4wNS4wMScgYW5kIG9wZW5fY2FyZF90aW1lPCcyMDIyLjA2LjAxJ1xyXG5ncm91cCBieSDlip7ljaHkurrmlbDlj4rljYfnuqfmlrnlvI8iLCJ0ZW1wbGF0ZS10YWdzIjp7fX0sImRhdGFiYXNlIjoxNH0=\",\"QueryType\":\"native\",\"Description\":\"本月办卡人数及升级方式\",\"Time\":\"2022-05-07 14:04:56\",\"UpdateTime\":\"2022-05-26 10:53:34\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"14a3b845-f66e-2b5f-73a8-9332a98faa97\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"42\",\"ReqAvgUseMs\":\"6\",\"Id\":\"4bb1903d-8d59-4771-a7a6-69e84fe62832\",\"DatamodelQueryName\":\"散点图\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MiwicXVlcnkiOnsic291cmNlLXRhYmxlIjoxLCJvcmRlci1ieSI6W1siYXNjIixbImZpZWxkLWlkIiwzOTBdXV0sImZpZWxkcyI6W1siZmllbGQtaWQiLDM3N10sWyJmaWVsZC1pZCIsMzc4XSxbImZpZWxkLWlkIiwzNzldLFsiZmllbGQtaWQiLDM4M10sWyJmaWVsZC1pZCIsMzg0XSxbImZpZWxkLWlkIiwzODVdLFsiZmllbGQtaWQiLDM4Nl0sWyJmaWVsZC1pZCIsMzg3XSxbImZpZWxkLWlkIiwzODhdLFsiZmllbGQtaWQiLDM4OV0sWyJmaWVsZC1pZCIsMzkwXSxbImZpZWxkLWlkIiwzOTFdXX0sInR5cGUiOiJxdWVyeSIsInZlcnNpb24iOiJwZXJmZWN0In0=\",\"QueryType\":\"\",\"Description\":\"散点图散点图散点图散点图\",\"Time\":\"2022-01-14 08:53:31\",\"UpdateTime\":\"2022-05-25 16:50:20\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"8edc4c89-88d1-a59d-8cb5-787489993b08\",\"GroupId\":\"8a1469a6-bdb6-4b43-b90c-816184cdadec\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"23\",\"ReqAvgUseMs\":\"3\",\"Id\":\"9b92671a-2bb7-4f22-a21f-2c69e2ba3922\",\"DatamodelQueryName\":\"折线图\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MiwicXVlcnkiOnsic291cmNlLXRhYmxlIjoxLCJicmVha291dCI6W1siZmllbGQtaWQiLDM3OF1dLCJhZ2dyZWdhdGlvbiI6W1siY291bnQiXSxbInN1bSIsWyJmaWVsZC1pZCIsMzg0XV0sWyJzdW0iLFsiZmllbGQtaWQiLDM4NV1dXX0sInR5cGUiOiJxdWVyeSJ9\",\"QueryType\":\"\",\"Description\":\"dataCenterLinedataCenterLinedataCenterLinedataCenterLine\",\"Time\":\"2021-12-22 11:24:27\",\"UpdateTime\":\"2022-05-25 14:51:28\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"8edc4c89-88d1-a59d-8cb5-787489993b08\",\"GroupId\":\"8a1469a6-bdb6-4b43-b90c-816184cdadec\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"155\",\"ReqAvgUseMs\":\"117151\",\"Id\":\"5506e64b-8211-4ee6-ba93-774262094487\",\"DatamodelQueryName\":\"内蒙古各月业绩\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IFxyXG5vcmRlcl9tb250aCxzdW0ob3JkZXJfYW1vdW50KVxyXG5mcm9tIG9yZGVyX29yZGVyIFxyXG53aGVyZSBvcmRlcl9tb250aD49JzIwMjEwMScgYW5kIG9yZGVyX3N0YXR1cyBpbigyLDMsOTkpIGFuZCBjb21wYW55X2NvZGU9JzEzMDAwJ1xyXG5ncm91cCBieSBvcmRlcl9tb250aFxyXG5vcmRlciBieSBvcmRlcl9tb250aCIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"内蒙古\",\"Time\":\"2022-05-24 14:20:45\",\"UpdateTime\":\"2022-05-24 14:20:45\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"a81638b9-1f19-44dc-a9d6-86ba474a3fb3\",\"Cache\":\"True\",\"CacheInterval\":\"30\"},{\"DatamodelQueryId\":\"91\",\"ReqAvgUseMs\":\"372\",\"Id\":\"2632d148-d54f-4996-bcf4-2fe535a29fd0\",\"DatamodelQueryName\":\"今日消费人数\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTMsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NjYzLCJqb2lucyI6W10sImZpbHRlciI6W1sidGltZS1pbnRlcnZhbCIsWyJmaWVsZC1pZCIsMTEwNDBdLCJjdXJyZW50IiwiZGF5Il0sImFuZCIsWyI9IixbImZpZWxkLWlkIiwxMTA3Ml0sIjIwMjIwNSJdLCJhbmQiLFsiYnJhY2tldCIsW1siPSIsWyJmaWVsZC1pZCIsMTEwNzhdLCIyIl0sIm9yIixbIj0iLFsiZmllbGQtaWQiLDExMDc4XSwiMyJdLCJvciIsWyI9IixbImZpZWxkLWlkIiwxMTA3OF0sIjk5Il1dXV0sImFnZ3JlZ2F0aW9uIjpbWyJkaXN0aW5jdCIsWyJmaWVsZC1pZCIsMTEwNjBdXV19LCJ0eXBlIjoicXVlcnkiLCJ2ZXJzaW9uIjoicGVyZmVjdCJ9\",\"QueryType\":\"query\",\"Description\":\"今日消费人数\",\"Time\":\"2022-05-07 08:58:56\",\"UpdateTime\":\"2022-05-23 10:57:58\",\"UserId\":\"03fe16bd-0e3a-4655-a057-ace07e772ee1\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"126\",\"ReqAvgUseMs\":\"9388\",\"Id\":\"ceeeb8bb-6b08-455d-9028-51f07a572476\",\"DatamodelQueryName\":\"liang-test002\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5IjoiU0VMRUNUIGBtb250aGx5X3N0YXRpc3RpY3NgLmBvcmRlcl9tb250aGAgQVMgYG9yZGVyX21vbnRoYCxgbW9udGhseV9zdGF0aXN0aWNzYC5gYnV5ZXJfcXR5YCBBUyBgYnV5ZXJfcXR5YFxyXG4gRlJPTSBgbWFsbF9zYWxlYC5gbW9udGhseV9zdGF0aXN0aWNzYFxyXG4gd2hlcmUgb3JkZXJfbW9udGg8PScyMDIyMDQnXHJcbiB1bmlvbiBcclxuIHNlbGVjdCBvcmRlcl9tb250aCxjb3VudChkaXN0aW5jdCBjdXN0b21lcl9jYXJkKSBhcyAnYnV5ZXJfcXR5JyBmcm9tIG9yZGVyX29yZGVyXHJcbndoZXJlIG9yZGVyX21vbnRoPj0nMjAyMjA1JyBhbmQgb3JkZXJfc3RhdHVzIGluICgyLDMsOTkpXHJcbmdyb3VwIGJ5IG9yZGVyX21vbnRoXHJcbm9yZGVyIGJ5IG9yZGVyX21vbnRoXHJcbiIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"柱状图便签修改测试\",\"Time\":\"2022-05-12 16:21:49\",\"UpdateTime\":\"2022-05-20 17:16:40\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"c5df6129-0d20-4b58-8185-f731a8348396\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"99\",\"ReqAvgUseMs\":\"7971\",\"Id\":\"2c833dfd-5dc6-494c-8126-bcf6e24e93e3\",\"DatamodelQueryName\":\"本月消费人数\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTMsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NjYzLCJqb2lucyI6W10sImZpbHRlciI6W1siPSIsWyJmaWVsZC1pZCIsMTEwNzJdLCIyMDIyMDUiXSwiYW5kIixbImJyYWNrZXQiLFtbIj0iLFsiZmllbGQtaWQiLDExMDc4XSwiMiJdLCJvciIsWyI9IixbImZpZWxkLWlkIiwxMTA3OF0sIjMiXSwib3IiLFsiPSIsWyJmaWVsZC1pZCIsMTEwNzhdLCI5OSJdXV1dLCJhZ2dyZWdhdGlvbiI6W1siZGlzdGluY3QiLFsiZmllbGQtaWQiLDExMDYwXV1dfSwidHlwZSI6InF1ZXJ5IiwidmVyc2lvbiI6InBlcmZlY3QifQ==\",\"QueryType\":\"query\",\"Description\":\"本月消费人数\",\"Time\":\"2022-05-07 13:56:30\",\"UpdateTime\":\"2022-05-20 14:28:21\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"f961d11c-4d35-4838-a562-1bbc93269083\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"143\",\"ReqAvgUseMs\":\"0\",\"Id\":\"bb1ff512-584d-4090-a0e9-b40168c4d65c\",\"DatamodelQueryName\":\"testCustomColumnFromAggregation_02\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTUsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NzAwLCJicmVha291dCI6W1siZmllbGQtaWQiLDExNjgzXV0sImFnZ3JlZ2F0aW9uIjpbWyJzdW0iLFsiZmllbGQtaWQiLDExNjg0XV1dLCJleHByZXNzaW9ucyI6e30sImpvaW5zIjpbXSwiZmlsdGVyIjpbXX0sInR5cGUiOiJxdWVyeSIsInZlcnNpb24iOiJwZXJmZWN0In0=\",\"QueryType\":\"query\",\"Description\":\"testCustomColumnFromAggregation_02\",\"Time\":\"2022-05-17 15:44:38\",\"UpdateTime\":\"2022-05-19 10:52:05\",\"UserId\":\"33d8d854-6a07-47b3-8326-c9403db7a430\",\"Enable\":\"True\",\"DatabaseId\":\"9b4187ea-99be-ca76-ffdb-304b0a7f2a8b\",\"GroupId\":\"835f8160-6e09-41d9-89dc-b41662991631\",\"Cache\":\"True\",\"CacheInterval\":\"20\"},{\"DatamodelQueryId\":\"148\",\"ReqAvgUseMs\":\"287\",\"Id\":\"5b6d2773-12bf-49de-889b-6ebf00db053d\",\"DatamodelQueryName\":\"办卡数据\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IFxyXG4gIGNhc2UgXHJcbiAgICB3aGVuIHByb21vdGlvbl90eXBlPTEgdGhlbiAnMjk556S85YyFJ1xyXG4gICAgd2hlbiBwcm9tb3Rpb25fdHlwZT0yIHRoZW4gJzYwMHB2J1xyXG4gICAgd2hlbiBwcm9tb3Rpb25fdHlwZSBpcyBudWxsIHRoZW4gJ+acquWNh+e6pydcclxuICAgIGVuZCBhcyAn5Yqe5Y2h5Lq65pWw5Y+K5Y2H57qn5pa55byPJyxjb3VudCgqKSBhcyAn5Lq65pWwJ1xyXG5mcm9tIG1lbWJlcl9pbmZvXHJcbndoZXJlIG9wZW5fY2FyZF90aW1lPj0nMjAyMi4wNS4wMScgYW5kIG9wZW5fY2FyZF90aW1lPCcyMDIyLjA2LjAxJ1xyXG5ncm91cCBieSDlip7ljaHkurrmlbDlj4rljYfnuqfmlrnlvI8iLCJ0ZW1wbGF0ZS10YWdzIjp7fX0sImRhdGFiYXNlIjoxNH0=\",\"QueryType\":\"native\",\"Description\":\"办卡人数及升级人数\",\"Time\":\"2022-05-18 10:38:01\",\"UpdateTime\":\"2022-05-18 10:40:58\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"14a3b845-f66e-2b5f-73a8-9332a98faa97\",\"GroupId\":\"00000000-0000-0000-0000-000000000000\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"144\",\"ReqAvgUseMs\":\"46424\",\"Id\":\"4c6702d3-88af-47d5-97a2-74d1f4a79cb8\",\"DatamodelQueryName\":\"月销售额统计\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IG9yZGVyX21vbnRoLHN1bShvcmRlcl9hbW91bnQpXHJcbmZyb20gb3JkZXJfb3JkZXJcclxud2hlcmUgb3JkZXJfbW9udGg+PScyMDIyMDEnIGFuZCBvcmRlcl9zdGF0dXMgaW4oMiwzLDk5KVxyXG5ncm91cCBieSBvcmRlcl9tb250aCIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"统计月销售额，未扣除换购\",\"Time\":\"2022-05-17 16:18:39\",\"UpdateTime\":\"2022-05-17 16:34:45\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"00000000-0000-0000-0000-000000000000\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"128\",\"ReqAvgUseMs\":\"11379\",\"Id\":\"0084aaa1-ace7-4367-84fe-99455c2d71b8\",\"DatamodelQueryName\":\"本月新增客户数\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5IjoiU0VMRUNUIGNvdW50KCopIEFTIGBjb3VudGBcclxuRlJPTSBgaHl6bGBcclxuV0hFUkUgYGFjY21vbmA9ZGF0ZV9mb3JtYXQobm93KCksICclWS4lbScpIiwidGVtcGxhdGUtdGFncyI6e319LCJkYXRhYmFzZSI6MTJ9\",\"QueryType\":\"native\",\"Description\":\"本月新增客户数\",\"Time\":\"2022-05-13 11:08:30\",\"UpdateTime\":\"2022-05-13 11:09:52\",\"UserId\":\"3c879e85-5b86-4dba-b5e2-c1facfeabaa5\",\"Enable\":\"True\",\"DatabaseId\":\"b1605471-1082-1f82-85a8-a9c505e8bfc1\",\"GroupId\":\"880a5dda-918e-425f-8bc8-e0b89a77f11a\",\"Cache\":\"True\",\"CacheInterval\":\"20\"},{\"DatamodelQueryId\":\"104\",\"ReqAvgUseMs\":\"25812\",\"Id\":\"e3570a03-58fc-4373-86a1-4db33f823f7e\",\"DatamodelQueryName\":\"4月单品TOP20（单位：万元）\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IGEuaWQsYS5vcmRlcl9zdGF0dXMsYS5vcmRlcl9tb250aCxiLm9yZGVyX2lkLGIuc2VyaWFsX25vLHN1bShiLnRvdGFsX3ByaWNlKSxcclxuICAgIENBU0VcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdGQScgVEhFTiAn5rKZ5qOY6Iy2J1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0ZBMTAnIFRIRU4gJ+aymeajmOiMtidcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdGQTMwJyBUSEVOICfmspnmo5jojLYnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nRkExMDAxJyBUSEVOICfmspnmo5jojLYnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nQUcnIFRIRU4gJ+iKpuiNn+iDtidcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdBRzInIFRIRU4gJ+iKpuiNn+iDtidcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdBRzEwJyBUSEVOICfoiqbojZ/og7YnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nQUcxMDAxJyBUSEVOICfoiqbojZ/og7YnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nQU1QTicgVEhFTiAn55+/54mp57KJJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0FNUE4xMCcgVEhFTiAn55+/54mp57KJJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0FNUE4zMCcgVEhFTiAn55+/54mp57KJJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0FNUE4xMDAxJyBUSEVOICfnn7/niannsoknXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nUFNOJyBUSEVOICfogr3ol7vokKXlhbvnsoknXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nUFNONicgVEhFTiAn6IK96Je76JCl5YW757KJJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J1BTTjMwJyBUSEVOICfogr3ol7vokKXlhbvnsoknXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nUFNONjAxJyBUSEVOICfogr3ol7vokKXlhbvnsoknXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nQ1BUMDInIFRIRU4gJ+ivreazsOeJhydcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdDUFQwMycgVEhFTiAn6K+t5rOw54mHJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0NQVDgnIFRIRU4gJ+ivreazsOeJhydcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdOQicgVEhFTiAn6JCl5YW76aSQJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J05CMzAnIFRIRU4gJ+iQpeWFu+mkkCdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdPTFMnIFRIRU4gJ+S4gOeUn+ezlidcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdPTFM2JyBUSEVOICfkuIDnlJ/ns5YnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nUFNHJyBUSEVOICfmspnokpwnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nUFNHNCcgVEhFTiAn5rKZ6JKcJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J1BTRzQwMScgVEhFTiAn5rKZ6JKcJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J01BNTAyMScgVEhFTiAn5ruL5ram54i96IKk5rC0J1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J01BNTAyMTMnIFRIRU4gJ+a7i+a2pueIveiCpOawtCdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdQU0QnIFRIRU4gJ+S6v+eUn+iPjCdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdDRFAnIFRIRU4gJ+aymeajmOaxgSdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdNQTUwMTEnIFRIRU4gJ+aflOWSjOa0geiCpOS5sydcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdNQTcwMzknIFRIRU4gJ+WuieeTtueyvuWNjua2sidcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdBTTAwNzMwJyBUSEVOICfoiqbojZ/pnaLohpwnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nQU0wMDcnIFRIRU4gJ+iKpuiNn+mdouiGnCdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdFSFAnIFRIRU4gJ+iGs+mjn+e6pOe7tOeyiSdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdFSFAzMCcgVEhFTiAn5aSN5ZCI6Iaz6aOf57qk57u057KJJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0dQTScgVEhFTiAn5YGl5omsJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0ZEMzAnIFRIRU4gJ+mrmOe6pOS5kCdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdPTEknIFRIRU4gJ+a0u+eri+WkmidcclxuICAgICAgICBlbHNlIGIuc2VyaWFsX25vXHJcbiAgICBFTkQgQVMgYmNjXHJcbmZyb21cclxuKHNlbGVjdCBpZCxvcmRlcl9zdGF0dXMsb3JkZXJfbW9udGggZnJvbSBvcmRlcl9vcmRlcikgYVxyXG5qb2luXHJcbihzZWxlY3Qgb3JkZXJfaWQsc2VyaWFsX25vLHRvdGFsX3ByaWNlIGZyb20gb3JkZXJfcHJvZHVjdCkgYlxyXG5vbiAoYS5pZD1iLm9yZGVyX2lkKVxyXG53aGVyZSBhLm9yZGVyX3N0YXR1cyBpbiAoMiwzLDk5KSBhbmQgYS5vcmRlcl9tb250aCA9JzIwMjIwNSdcclxuZ3JvdXAgYnkgYmNjXHJcbm9yZGVyIGJ5IHN1bShiLnRvdGFsX3ByaWNlKSBkZXNjXHJcbmxpbWl0IDIwIiwidGVtcGxhdGUtdGFncyI6e319LCJkYXRhYmFzZSI6MTN9\",\"QueryType\":\"native\",\"Description\":\"4月单品TOP20\",\"Time\":\"2022-05-07 14:23:50\",\"UpdateTime\":\"2022-05-13 09:27:44\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"88\",\"ReqAvgUseMs\":\"8009\",\"Id\":\"806940f4-9f27-4e0b-b278-1f146728406c\",\"DatamodelQueryName\":\"本月销售额\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTMsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NjYzLCJqb2lucyI6W10sImZpbHRlciI6W1siPSIsWyJmaWVsZC1pZCIsMTEwNzJdLCIyMDIyMDUiXSwiYW5kIixbImJyYWNrZXQiLFtbIj0iLFsiZmllbGQtaWQiLDExMDc4XSwiMiJdLCJvciIsWyI9IixbImZpZWxkLWlkIiwxMTA3OF0sIjMiXSwib3IiLFsiPSIsWyJmaWVsZC1pZCIsMTEwNzhdLCI5OSJdXV1dLCJhZ2dyZWdhdGlvbiI6W1sic3VtIixbImZpZWxkLWlkIiwxMTA0MV1dXX0sInR5cGUiOiJxdWVyeSIsInZlcnNpb24iOiJwZXJmZWN0In0=\",\"QueryType\":\"query\",\"Description\":\"本月销售额\",\"Time\":\"2022-05-06 15:59:11\",\"UpdateTime\":\"2022-05-11 14:47:36\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"93\",\"ReqAvgUseMs\":\"31719\",\"Id\":\"1bc1df9f-4d5f-460f-91ec-286d40d0eb48\",\"DatamodelQueryName\":\"本月省份业绩排名\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5IjoiU0VMRUNUICBcclxuICAgIENBU0VcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45bm/5Lic5YiG5YWs5Y+4JyBUSEVOICflub/kuJwnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOa1meaxn+WIhuWFrOWPuCcgVEhFTiAn5rWZ5rGfJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jmsZ/oi4/liIblhazlj7gnIFRIRU4gJ+axn+iLjydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45rKz5Y2X5YiG5YWs5Y+4JyBUSEVOICfmsrPljZcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOays+WMl+WIhuWFrOWPuCcgVEhFTiAn5rKz5YyXJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jlsbHkuJzliIblhazlj7gnIFRIRU4gJ+WxseS4nCdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+46Z2S5bKb5YiG5YWs5Y+4JyBUSEVOICflsbHkuJwnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOemj+W7uuWIhuWFrOWPuCcgVEhFTiAn56aP5bu6J1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jmuZbljZfliIblhazlj7gnIFRIRU4gJ+a5luWNlydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45a6J5b695YiG5YWs5Y+4JyBUSEVOICflronlvr0nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOWugeazouWIhuWFrOWPuCcgVEhFTiAn5rWZ5rGfJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jlkInmnpfliIblhazlj7gnIFRIRU4gJ+WQieaelydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45LqR5Y2X5YiG5YWs5Y+4JyBUSEVOICfkupHljZcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOWbm+W3neWIhuWFrOWPuCcgVEhFTiAn5Zub5bedJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jovr3lroHliIblhazlj7gnIFRIRU4gJ+i+veWugSdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45rGf6KW/5YiG5YWs5Y+4JyBUSEVOICfmsZ/opb8nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOWkqea0peWIhuWFrOWPuCcgVEhFTiAn5aSp5rSlJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jmuZbljJfliIblhazlj7gnIFRIRU4gJ+a5luWMlydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+46YeN5bqG5YiG5YWs5Y+4JyBUSEVOICfph43luoYnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOW5v+ilv+WIhuWFrOWPuCcgVEhFTiAn5bm/6KW/J1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jlsbHopb/liIblhazlj7gnIFRIRU4gJ+WxseilvydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45LiK5rW35YiG5YWs5Y+4JyBUSEVOICfkuIrmtbcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOS4reWxseWIhuWFrOWPuCcgVEhFTiAn5bm/5LicJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jotLXlt57liIblhazlj7gnIFRIRU4gJ+i0teW3nidcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45YaF6JKZ5Y+k5YiG5YWs5Y+4JyBUSEVOICflhoXokpnlj6QnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOWMl+S6rOWIhuWFrOWPuCcgVEhFTiAn5YyX5LqsJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jpu5HpvpnmsZ/liIblhazlj7gnIFRIRU4gJ+m7kem+meaxnydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+46ZmV6KW/5YiG5YWs5Y+4JyBUSEVOICfpmZXopb8nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOaWsOeWhuWIhuWFrOWPuCcgVEhFTiAn5paw55aGJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jnlJjogoPliIblhazlj7gnIFRIRU4gJ+eUmOiCgydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45aSn6L+e5YiG5YWs5Y+4JyBUSEVOICfovr3lroEnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOa1t+WNl+WIhuWFrOWPuCcgVEhFTiAn5rW35Y2XJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jlroHlpI/liIblhazlj7gnIFRIRU4gJ+WugeWkjydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+46Z2S5rW35YiG5YWs5Y+4JyBUSEVOICfpnZLmtbcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOilv+iXj+WIhuWFrOWPuCcgVEhFTiAn6KW/6JePJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflub/kuJzliIblhazlj7gnIFRIRU4gJ+W5v+S4nCdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5rWZ5rGf5YiG5YWs5Y+4JyBUSEVOICfmtZnmsZ8nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+axn+iLj+WIhuWFrOWPuCcgVEhFTiAn5rGf6IuPJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfmsrPljZfliIblhazlj7gnIFRIRU4gJ+ays+WNlydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5rKz5YyX5YiG5YWs5Y+4JyBUSEVOICfmsrPljJcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WxseS4nOWIhuWFrOWPuCcgVEhFTiAn5bGx5LicJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfpnZLlspvliIblhazlj7gnIFRIRU4gJ+WxseS4nCdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n56aP5bu65YiG5YWs5Y+4JyBUSEVOICfnpo/lu7onXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+a5luWNl+WIhuWFrOWPuCcgVEhFTiAn5rmW5Y2XJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflronlvr3liIblhazlj7gnIFRIRU4gJ+WuieW+vSdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6B5rOi5YiG5YWs5Y+4JyBUSEVOICfmtZnmsZ8nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WQieael+WIhuWFrOWPuCcgVEhFTiAn5ZCJ5p6XJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfkupHljZfliIblhazlj7gnIFRIRU4gJ+S6keWNlydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5Zub5bed5YiG5YWs5Y+4JyBUSEVOICflm5vlt50nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+i+veWugeWIhuWFrOWPuCcgVEhFTiAn6L695a6BJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfmsZ/opb/liIblhazlj7gnIFRIRU4gJ+axn+ilvydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5aSp5rSl5YiG5YWs5Y+4JyBUSEVOICflpKnmtKUnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+a5luWMl+WIhuWFrOWPuCcgVEhFTiAn5rmW5YyXJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfph43luobliIblhazlj7gnIFRIRU4gJ+mHjeW6hidcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5bm/6KW/5YiG5YWs5Y+4JyBUSEVOICflub/opb8nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+Wxseilv+WIhuWFrOWPuCcgVEhFTiAn5bGx6KW/J1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfkuIrmtbfliIblhazlj7gnIFRIRU4gJ+S4iua1tydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5Lit5bGx5YiG5YWs5Y+4JyBUSEVOICflub/kuJwnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+i0teW3nuWIhuWFrOWPuCcgVEhFTiAn6LS15beeJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflhoXokpnlj6TliIblhazlj7gnIFRIRU4gJ+WGheiSmeWPpCdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5YyX5Lqs5YiG5YWs5Y+4JyBUSEVOICfljJfkuqwnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+m7kem+meaxn+WIhuWFrOWPuCcgVEhFTiAn6buR6b6Z5rGfJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfpmZXopb/liIblhazlj7gnIFRIRU4gJ+mZleilvydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5paw55aG5YiG5YWs5Y+4JyBUSEVOICfmlrDnloYnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+eUmOiCg+WIhuWFrOWPuCcgVEhFTiAn55SY6IKDJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflpKfov57liIblhazlj7gnIFRIRU4gJ+i+veWugSdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5rW35Y2X5YiG5YWs5Y+4JyBUSEVOICfmtbfljZcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WugeWkj+WIhuWFrOWPuCcgVEhFTiAn5a6B5aSPJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfpnZLmtbfliIblhazlj7gnIFRIRU4gJ+mdkua1tydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n6KW/6JeP5YiG5YWs5Y+4JyBUSEVOICfopb/ol48nXHJcbiAgICAgICAgZWxzZSBjb21wYW55X25hbWVcclxuICAgIEVORCBBUyAn55yB5Lu9Jyxcclxuc3VtKGNhc2Ugd2hlbiBvcmRlcl9tb250aD0nMjAyMjA1JyB0aGVuIG9yZGVyX2Ftb3VudCoxIGVsc2Ugb3JkZXJfYW1vdW50KjAgZW5kKSBBUyAn5pys5pyI6ZSA5ZSu6aKdJyxcclxuc3VtKGNhc2Ugd2hlbiBvcmRlcl9tb250aD0nMjAyMTA1JyB0aGVuIG9yZGVyX2Ftb3VudCoxIGVsc2Ugb3JkZXJfYW1vdW50KjAgZW5kKSBBUyAnMjAyMTA1Jyxcclxuc3VtKGNhc2Ugd2hlbiBvcmRlcl9tb250aD0nMjAyMjA1JyB0aGVuIG9yZGVyX2Ftb3VudCoxIGVsc2Ugb3JkZXJfYW1vdW50KjAgZW5kKS9zdW0oY2FzZSB3aGVuIG9yZGVyX21vbnRoPScyMDIxMDUnIHRoZW4gb3JkZXJfYW1vdW50KjEgZWxzZSBvcmRlcl9hbW91bnQqMCBlbmQpIGFzICflkIzmr5Tovr7miJDnjocnXHJcbkZST00gb3JkZXJfb3JkZXJcclxuV0hFUkUgKG9yZGVyX3N0YXR1cz0yIE9SIG9yZGVyX3N0YXR1cz0zIE9SIG9yZGVyX3N0YXR1cz05OSkgYW5kIChvcmRlcl9tb250aD0nMjAyMjA1JyBvciBvcmRlcl9tb250aD0nMjAyMTA1JylcclxuR1JPVVAgQlkg55yB5Lu9XHJcbk9SREVSIEJZIOacrOaciOmUgOWUruminSBERVNDXHJcbiIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"本月省份业绩排名\",\"Time\":\"2022-05-07 10:44:20\",\"UpdateTime\":\"2022-05-11 14:35:18\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"98\",\"ReqAvgUseMs\":\"43\",\"Id\":\"d48d136a-5eb9-4e4b-a732-333d2cc39ee4\",\"DatamodelQueryName\":\"本年新增人数\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IGNvdW50KCopIGZyb20gbWVtYmVyX2luZm9cclxud2hlcmUgb3Blbl9jYXJkX3RpbWU+PScyMDIyLjAxLjAxJyIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjE0fQ==\",\"QueryType\":\"native\",\"Description\":\"本年新增人数\",\"Time\":\"2022-05-07 13:54:23\",\"UpdateTime\":\"2022-05-11 11:03:26\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"14a3b845-f66e-2b5f-73a8-9332a98faa97\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"92\",\"ReqAvgUseMs\":\"604\",\"Id\":\"0c7ed8d4-0193-4216-be23-20c26751c3a6\",\"DatamodelQueryName\":\"今日客单价\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5IjoiU0VMRUNUIGF2Zyhgc291cmNlYC5gc3VtYCkgQVMgYGF2Z2BcclxuRlJPTSAoU0VMRUNUIGBvcmRlcl9vcmRlcmAuYGN1c3RvbWVyX2NhcmRgIEFTIGBjdXN0b21lcl9jYXJkYCwgc3VtKGBvcmRlcl9vcmRlcmAuYG9yZGVyX2Ftb3VudGApIEFTIGBzdW1gIEZST00gYG9yZGVyX29yZGVyYFxyXG5XSEVSRSAoYG9yZGVyX29yZGVyYC5gcGF5X3RpbWVgID49ZGF0ZShub3coNikpIGFuZCBgb3JkZXJfb3JkZXJgLmBwYXlfdGltZWAgPj1kYXRlKG5vdyg2KSkgYW5kIGBvcmRlcl9vcmRlcmAuYHBheV90aW1lYCA8IGRhdGVfYWRkKGRhdGUobm93KDYpKSwgSU5URVJWQUwgMSBkYXkpXHJcbiAgIEFORCAoYG9yZGVyX29yZGVyYC5gb3JkZXJfc3RhdHVzYCA9IDJcclxuICAgIE9SIGBvcmRlcl9vcmRlcmAuYG9yZGVyX3N0YXR1c2AgPSAzIE9SIGBvcmRlcl9vcmRlcmAuYG9yZGVyX3N0YXR1c2AgPSA5OSkpXHJcbkdST1VQIEJZIGBvcmRlcl9vcmRlcmAuYGN1c3RvbWVyX2NhcmRgXHJcbk9SREVSIEJZIGBvcmRlcl9vcmRlcmAuYGN1c3RvbWVyX2NhcmRgIEFTQykgYHNvdXJjZWAiLCJ0ZW1wbGF0ZS10YWdzIjp7fX0sImRhdGFiYXNlIjoxM30=\",\"QueryType\":\"native\",\"Description\":\"今日客单价\",\"Time\":\"2022-05-07 09:02:23\",\"UpdateTime\":\"2022-05-11 11:02:26\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"78\",\"ReqAvgUseMs\":\"8868\",\"Id\":\"a306141b-e71c-43fc-8c58-b0e72302e5c7\",\"DatamodelQueryName\":\"本月促销活动\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IGEuaWQsYS5vcmRlcl9zdGF0dXMsYS5vcmRlcl9tb250aCxiLm9yZGVyX2lkLFxyXG5jYXNlIFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J01BWUgyMjQxJyB0aGVuICfnvo7ogqTlpZfoo4UnIFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0pDMjAyMicgdGhlbiAn5rS75Yqb6IKg6ZO45aWX6KOFJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0FDQzIwMjInIHRoZW4gJ0FDQ+S8mOaDoOWll+ijhSdcclxuICBlbmQgYXMgJ+a0u+WKqOWQjeensCcsXHJcbnN1bShiLnF1YW50aXR5KSBhcyAn6ZSA5ZSu6YePJyxzdW0oYi50b3RhbF9wcmljZSkgYXMgJ+mUgOWUruminScsXHJcbmNhc2UgXHJcbiAgd2hlbiBiLnNlcmlhbF9ubz0nTUFZSDIyNDEnIHRoZW4gc3VtKGIucXVhbnRpdHkpLzM3MDAwIFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0pDMjAyMicgdGhlbiBzdW0oYi5xdWFudGl0eSkvMzc4MDBcclxuICB3aGVuIGIuc2VyaWFsX25vPSdBQ0MyMDIyJyB0aGVuIHN1bShiLnF1YW50aXR5KS8yNTAwMFxyXG4gIGVuZCBhcyAn55uu5qCH5a6M5oiQ546HJyBcclxuZnJvbVxyXG4oc2VsZWN0IGlkLG9yZGVyX3N0YXR1cyxvcmRlcl9tb250aCBmcm9tIG9yZGVyX29yZGVyKSBhXHJcbmpvaW5cclxuKHNlbGVjdCBvcmRlcl9pZCxzZXJpYWxfbm8scXVhbnRpdHksdG90YWxfcHJpY2UgZnJvbSBvcmRlcl9wcm9kdWN0KSBiXHJcbm9uIChhLmlkPWIub3JkZXJfaWQpXHJcbndoZXJlIGEub3JkZXJfc3RhdHVzIGluICgyLDMsOTkpIGFuZCBhLm9yZGVyX21vbnRoID0nMjAyMjA0JyBhbmQgYi5zZXJpYWxfbm8gaW4gKCdNQVlIMjI0MScsJ0pDMjAyMicsJ0FDQzIwMjInKVxyXG5ncm91cCBieSBiLnNlcmlhbF9ubyIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"统计本月促销产品的销售额\",\"Time\":\"2022-04-29 10:25:33\",\"UpdateTime\":\"2022-05-11 11:01:29\",\"UserId\":\"33d8d854-6a07-47b3-8326-c9403db7a430\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"00000000-0000-0000-0000-000000000000\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"87\",\"ReqAvgUseMs\":\"300\",\"Id\":\"e59be632-b047-4f64-9c38-85841253fae2\",\"DatamodelQueryName\":\"今日销售额（万元）\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTMsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NjYzLCJqb2lucyI6W10sImZpbHRlciI6W1sidGltZS1pbnRlcnZhbCIsWyJmaWVsZC1pZCIsMTEwNDBdLCJjdXJyZW50IiwiZGF5Il0sImFuZCIsWyI9IixbImZpZWxkLWlkIiwxMTA3Ml0sIjIwMjIwNSJdLCJhbmQiLFsiYnJhY2tldCIsW1siPSIsWyJmaWVsZC1pZCIsMTEwNzhdLCIyIl0sIm9yIixbIj0iLFsiZmllbGQtaWQiLDExMDc4XSwiMyJdLCJvciIsWyI9IixbImZpZWxkLWlkIiwxMTA3OF0sIjk5Il1dXV0sImFnZ3JlZ2F0aW9uIjpbWyJzdW0iLFsiZmllbGQtaWQiLDExMDQxXV1dfSwidHlwZSI6InF1ZXJ5IiwidmVyc2lvbiI6InBlcmZlY3QifQ==\",\"QueryType\":\"query\",\"Description\":\"今日销售额（万元）\",\"Time\":\"2022-05-06 15:55:28\",\"UpdateTime\":\"2022-05-11 11:01:29\",\"UserId\":\"cf8d3088-31ec-4230-bfcf-fcffa1fdd56c\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"67\",\"ReqAvgUseMs\":\"2118\",\"Id\":\"84bd12d7-299c-4691-ba63-ca7c17aa9633\",\"DatamodelQueryName\":\"testTodayMoney\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTMsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NjYzLCJqb2lucyI6W10sImZpbHRlciI6W1sidGltZS1pbnRlcnZhbCIsWyJmaWVsZC1pZCIsMTEwNDBdLCJjdXJyZW50IiwiZGF5Il0sImFuZCIsWyI9IixbImZpZWxkLWlkIiwxMTA3Ml0sIjIwMjIwNCJdLCJhbmQiLFsiPSIsWyJmaWVsZC1pZCIsMTEwNzhdLCIzIl1dLCJhZ2dyZWdhdGlvbiI6W1sic3VtIixbImZpZWxkLWlkIiwxMTA0MV1dXX0sInR5cGUiOiJxdWVyeSIsInZlcnNpb24iOiJwZXJmZWN0In0=\",\"QueryType\":\"\",\"Description\":\"今日业绩\",\"Time\":\"2022-04-07 11:54:39\",\"UpdateTime\":\"2022-05-11 11:00:04\",\"UserId\":\"33d8d854-6a07-47b3-8326-c9403db7a430\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"00000000-0000-0000-0000-000000000000\",\"Cache\":\"True\",\"CacheInterval\":\"10\"},{\"DatamodelQueryId\":\"97\",\"ReqAvgUseMs\":\"57913\",\"Id\":\"e33cceab-e648-49e4-9ab6-b0adc183ae3d\",\"DatamodelQueryName\":\"本年销售额\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IHN1bShvcmRlcl9hbW91bnQpIGZyb20gb3JkZXJfb3JkZXJcclxud2hlcmUgb3JkZXJfbW9udGg+PScyMDIyMDEnIGFuZCBvcmRlcl9zdGF0dXMgaW4oMiwzLDk5KSIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"本年销售额（sql有>号报错）\",\"Time\":\"2022-05-07 13:43:26\",\"UpdateTime\":\"2022-05-11 10:59:51\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"30\"},{\"DatamodelQueryId\":\"68\",\"ReqAvgUseMs\":\"2\",\"Id\":\"f536c67b-b1d1-4695-8392-64b0801264c2\",\"DatamodelQueryName\":\"liang_test\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MiwicXVlcnkiOnsic291cmNlLXRhYmxlIjoyfSwidHlwZSI6InF1ZXJ5IiwidmVyc2lvbiI6InBlcmZlY3QifQ==\",\"QueryType\":null,\"Description\":\"liang_testliang_testliang_testliang_test\",\"Time\":\"2022-04-08 14:59:49\",\"UpdateTime\":\"2022-05-10 15:40:07\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"False\",\"DatabaseId\":\"8edc4c89-88d1-a59d-8cb5-787489993b08\",\"GroupId\":\"00000000-0000-0000-0000-000000000000\",\"Cache\":\"True\",\"CacheInterval\":\"10\"}],\"RecordCount\":\"22\"}";
//		this.PrintObject(aa);
//		
//		DefaultListDataSource<List<DataModelInfo>> o3 = JSON.parseObject(aa, new TypeReference<DefaultListDataSource<List<DataModelInfo>>>(){}) ;
//
//		this.PrintObject(o3);
//
//	}
//	public void testJsonDeserial6() {
//		String aa = "{\"DataSource\":[{\"DatamodelQueryId\":\"90\",\"ReqAvgUseMs\":\"2439\",\"Id\":\"96bbda7c-8dfe-461f-9ea0-f78269334682\",\"DatamodelQueryName\":\"促销活动\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IGEuaWQsYS5vcmRlcl9zdGF0dXMsYS5vcmRlcl9tb250aCxiLm9yZGVyX2lkLFxyXG5jYXNlIFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0ZTJyB0aGVuICcxLeaWsOWTgeS4iuW4gu+8mkZT5LiA55Sf57OWJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0ZTNicgdGhlbiAnMi3mlrDlk4HkuIrluILvvJpGUzbkuIDnlJ/ns5YnXHJcbiAgd2hlbiBiLnNlcmlhbF9ubz0nRlMzMCcgdGhlbiAnMy3mlrDlk4HkuIrluILvvJpGUzMw5rS756uL5aSaJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0FDQzIwMjInIHRoZW4gJzctQUND5LyY5oOg5aWX6KOFJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J1NQQ1MwMicgdGhlbiAnNi3lpI/lraPkuKrmiqTlpZfoo4Ut55S35aOrJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J1NQQ1MwMScgdGhlbiAnNC3lpI/lraPkuKrmiqTlpZfoo4Ut5aWz5aOrJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0VUMjAyMicgdGhlbiAnNS3niLHmlr3ok5PlqbTnq6XmtJfmiqTlpZfoo4UnXHJcbiAgZW5kIGFzICfmtLvliqjlkI3np7AnLFxyXG5zdW0oYi5xdWFudGl0eSkgYXMgJ+mUgOWUrumHjycsc3VtKGIudG90YWxfcHJpY2UpIGFzICfplIDllK7pop0nLFxyXG5jYXNlIFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0FDQzIwMjInIHRoZW4gc3VtKGIucXVhbnRpdHkpLzI1MDAwXHJcbiAgd2hlbiBiLnNlcmlhbF9ubz0nU1BDUzAyJyB0aGVuIHN1bShiLnF1YW50aXR5KS8yMDg1MFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J1NQQ1MwMScgdGhlbiBzdW0oYi5xdWFudGl0eSkvMTM1ODBcclxuICB3aGVuIGIuc2VyaWFsX25vPSdFVDIwMjInIHRoZW4gc3VtKGIucXVhbnRpdHkpLzQ1MDBcclxuICBlbmQgYXMgJ+ebruagh+WujOaIkOeOhycgXHJcbmZyb21cclxuKHNlbGVjdCBpZCxvcmRlcl9zdGF0dXMsb3JkZXJfbW9udGggZnJvbSBvcmRlcl9vcmRlcikgYVxyXG5qb2luXHJcbihzZWxlY3Qgb3JkZXJfaWQsc2VyaWFsX25vLHF1YW50aXR5LHRvdGFsX3ByaWNlIGZyb20gb3JkZXJfcHJvZHVjdCkgYlxyXG5vbiAoYS5pZD1iLm9yZGVyX2lkKVxyXG53aGVyZSBhLm9yZGVyX3N0YXR1cyBpbiAoMiwzLDk5KSBhbmQgYS5vcmRlcl9tb250aCA+PScyMDIyMDQnIGFuZCBiLnNlcmlhbF9ubyBpbiAoJ0FDQzIwMjInLCdTUENTMDInLCdTUENTMDEnLCdFVDIwMjInLCdGUycsJ0ZTNicsJ0ZTMzAnKVxyXG5ncm91cCBieSBiLnNlcmlhbF9ub1xyXG5vcmRlciBieSDmtLvliqjlkI3np7AiLCJ0ZW1wbGF0ZS10YWdzIjp7fX0sImRhdGFiYXNlIjoxM30=\",\"QueryType\":\"native\",\"Description\":\"促销活动\",\"Time\":\"2022-05-07 08:49:51\",\"UpdateTime\":\"2022-05-28 15:55:27\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"101\",\"ReqAvgUseMs\":\"169\",\"Id\":\"413e56b9-18fa-4e85-9b16-5a8cee9c3387\",\"DatamodelQueryName\":\"本月办卡人数及升级方式\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IFxyXG4gIGNhc2UgXHJcbiAgICB3aGVuIHByb21vdGlvbl90eXBlPTEgdGhlbiAnMjk556S85YyFJ1xyXG4gICAgd2hlbiBwcm9tb3Rpb25fdHlwZT0yIHRoZW4gJzYwMHB2J1xyXG4gICAgd2hlbiBwcm9tb3Rpb25fdHlwZSBpcyBudWxsIHRoZW4gJ+acquWNh+e6pydcclxuICAgIGVuZCBhcyAn5Yqe5Y2h5Lq65pWw5Y+K5Y2H57qn5pa55byPJyxjb3VudCgqKSBhcyAn5Lq65pWwJ1xyXG5mcm9tIG1lbWJlcl9pbmZvXHJcbndoZXJlIG9wZW5fY2FyZF90aW1lPj0nMjAyMi4wNS4wMScgYW5kIG9wZW5fY2FyZF90aW1lPCcyMDIyLjA2LjAxJ1xyXG5ncm91cCBieSDlip7ljaHkurrmlbDlj4rljYfnuqfmlrnlvI8iLCJ0ZW1wbGF0ZS10YWdzIjp7fX0sImRhdGFiYXNlIjoxNH0=\",\"QueryType\":\"native\",\"Description\":\"本月办卡人数及升级方式\",\"Time\":\"2022-05-07 14:04:56\",\"UpdateTime\":\"2022-05-26 10:53:34\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"14a3b845-f66e-2b5f-73a8-9332a98faa97\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"42\",\"ReqAvgUseMs\":\"6\",\"Id\":\"4bb1903d-8d59-4771-a7a6-69e84fe62832\",\"DatamodelQueryName\":\"散点图\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MiwicXVlcnkiOnsic291cmNlLXRhYmxlIjoxLCJvcmRlci1ieSI6W1siYXNjIixbImZpZWxkLWlkIiwzOTBdXV0sImZpZWxkcyI6W1siZmllbGQtaWQiLDM3N10sWyJmaWVsZC1pZCIsMzc4XSxbImZpZWxkLWlkIiwzNzldLFsiZmllbGQtaWQiLDM4M10sWyJmaWVsZC1pZCIsMzg0XSxbImZpZWxkLWlkIiwzODVdLFsiZmllbGQtaWQiLDM4Nl0sWyJmaWVsZC1pZCIsMzg3XSxbImZpZWxkLWlkIiwzODhdLFsiZmllbGQtaWQiLDM4OV0sWyJmaWVsZC1pZCIsMzkwXSxbImZpZWxkLWlkIiwzOTFdXX0sInR5cGUiOiJxdWVyeSIsInZlcnNpb24iOiJwZXJmZWN0In0=\",\"QueryType\":\"\",\"Description\":\"散点图散点图散点图散点图\",\"Time\":\"2022-01-14 08:53:31\",\"UpdateTime\":\"2022-05-25 16:50:20\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"8edc4c89-88d1-a59d-8cb5-787489993b08\",\"GroupId\":\"8a1469a6-bdb6-4b43-b90c-816184cdadec\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"23\",\"ReqAvgUseMs\":\"3\",\"Id\":\"9b92671a-2bb7-4f22-a21f-2c69e2ba3922\",\"DatamodelQueryName\":\"折线图\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MiwicXVlcnkiOnsic291cmNlLXRhYmxlIjoxLCJicmVha291dCI6W1siZmllbGQtaWQiLDM3OF1dLCJhZ2dyZWdhdGlvbiI6W1siY291bnQiXSxbInN1bSIsWyJmaWVsZC1pZCIsMzg0XV0sWyJzdW0iLFsiZmllbGQtaWQiLDM4NV1dXX0sInR5cGUiOiJxdWVyeSJ9\",\"QueryType\":\"\",\"Description\":\"dataCenterLinedataCenterLinedataCenterLinedataCenterLine\",\"Time\":\"2021-12-22 11:24:27\",\"UpdateTime\":\"2022-05-25 14:51:28\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"8edc4c89-88d1-a59d-8cb5-787489993b08\",\"GroupId\":\"8a1469a6-bdb6-4b43-b90c-816184cdadec\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"155\",\"ReqAvgUseMs\":\"117151\",\"Id\":\"5506e64b-8211-4ee6-ba93-774262094487\",\"DatamodelQueryName\":\"内蒙古各月业绩\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IFxyXG5vcmRlcl9tb250aCxzdW0ob3JkZXJfYW1vdW50KVxyXG5mcm9tIG9yZGVyX29yZGVyIFxyXG53aGVyZSBvcmRlcl9tb250aD49JzIwMjEwMScgYW5kIG9yZGVyX3N0YXR1cyBpbigyLDMsOTkpIGFuZCBjb21wYW55X2NvZGU9JzEzMDAwJ1xyXG5ncm91cCBieSBvcmRlcl9tb250aFxyXG5vcmRlciBieSBvcmRlcl9tb250aCIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"内蒙古\",\"Time\":\"2022-05-24 14:20:45\",\"UpdateTime\":\"2022-05-24 14:20:45\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"a81638b9-1f19-44dc-a9d6-86ba474a3fb3\",\"Cache\":\"True\",\"CacheInterval\":\"30\"},{\"DatamodelQueryId\":\"91\",\"ReqAvgUseMs\":\"372\",\"Id\":\"2632d148-d54f-4996-bcf4-2fe535a29fd0\",\"DatamodelQueryName\":\"今日消费人数\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTMsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NjYzLCJqb2lucyI6W10sImZpbHRlciI6W1sidGltZS1pbnRlcnZhbCIsWyJmaWVsZC1pZCIsMTEwNDBdLCJjdXJyZW50IiwiZGF5Il0sImFuZCIsWyI9IixbImZpZWxkLWlkIiwxMTA3Ml0sIjIwMjIwNSJdLCJhbmQiLFsiYnJhY2tldCIsW1siPSIsWyJmaWVsZC1pZCIsMTEwNzhdLCIyIl0sIm9yIixbIj0iLFsiZmllbGQtaWQiLDExMDc4XSwiMyJdLCJvciIsWyI9IixbImZpZWxkLWlkIiwxMTA3OF0sIjk5Il1dXV0sImFnZ3JlZ2F0aW9uIjpbWyJkaXN0aW5jdCIsWyJmaWVsZC1pZCIsMTEwNjBdXV19LCJ0eXBlIjoicXVlcnkiLCJ2ZXJzaW9uIjoicGVyZmVjdCJ9\",\"QueryType\":\"query\",\"Description\":\"今日消费人数\",\"Time\":\"2022-05-07 08:58:56\",\"UpdateTime\":\"2022-05-23 10:57:58\",\"UserId\":\"03fe16bd-0e3a-4655-a057-ace07e772ee1\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"126\",\"ReqAvgUseMs\":\"9388\",\"Id\":\"ceeeb8bb-6b08-455d-9028-51f07a572476\",\"DatamodelQueryName\":\"liang-test002\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5IjoiU0VMRUNUIGBtb250aGx5X3N0YXRpc3RpY3NgLmBvcmRlcl9tb250aGAgQVMgYG9yZGVyX21vbnRoYCxgbW9udGhseV9zdGF0aXN0aWNzYC5gYnV5ZXJfcXR5YCBBUyBgYnV5ZXJfcXR5YFxyXG4gRlJPTSBgbWFsbF9zYWxlYC5gbW9udGhseV9zdGF0aXN0aWNzYFxyXG4gd2hlcmUgb3JkZXJfbW9udGg8PScyMDIyMDQnXHJcbiB1bmlvbiBcclxuIHNlbGVjdCBvcmRlcl9tb250aCxjb3VudChkaXN0aW5jdCBjdXN0b21lcl9jYXJkKSBhcyAnYnV5ZXJfcXR5JyBmcm9tIG9yZGVyX29yZGVyXHJcbndoZXJlIG9yZGVyX21vbnRoPj0nMjAyMjA1JyBhbmQgb3JkZXJfc3RhdHVzIGluICgyLDMsOTkpXHJcbmdyb3VwIGJ5IG9yZGVyX21vbnRoXHJcbm9yZGVyIGJ5IG9yZGVyX21vbnRoXHJcbiIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"柱状图便签修改测试\",\"Time\":\"2022-05-12 16:21:49\",\"UpdateTime\":\"2022-05-20 17:16:40\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"c5df6129-0d20-4b58-8185-f731a8348396\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"99\",\"ReqAvgUseMs\":\"7971\",\"Id\":\"2c833dfd-5dc6-494c-8126-bcf6e24e93e3\",\"DatamodelQueryName\":\"本月消费人数\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTMsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NjYzLCJqb2lucyI6W10sImZpbHRlciI6W1siPSIsWyJmaWVsZC1pZCIsMTEwNzJdLCIyMDIyMDUiXSwiYW5kIixbImJyYWNrZXQiLFtbIj0iLFsiZmllbGQtaWQiLDExMDc4XSwiMiJdLCJvciIsWyI9IixbImZpZWxkLWlkIiwxMTA3OF0sIjMiXSwib3IiLFsiPSIsWyJmaWVsZC1pZCIsMTEwNzhdLCI5OSJdXV1dLCJhZ2dyZWdhdGlvbiI6W1siZGlzdGluY3QiLFsiZmllbGQtaWQiLDExMDYwXV1dfSwidHlwZSI6InF1ZXJ5IiwidmVyc2lvbiI6InBlcmZlY3QifQ==\",\"QueryType\":\"query\",\"Description\":\"本月消费人数\",\"Time\":\"2022-05-07 13:56:30\",\"UpdateTime\":\"2022-05-20 14:28:21\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"f961d11c-4d35-4838-a562-1bbc93269083\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"143\",\"ReqAvgUseMs\":\"0\",\"Id\":\"bb1ff512-584d-4090-a0e9-b40168c4d65c\",\"DatamodelQueryName\":\"testCustomColumnFromAggregation_02\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTUsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NzAwLCJicmVha291dCI6W1siZmllbGQtaWQiLDExNjgzXV0sImFnZ3JlZ2F0aW9uIjpbWyJzdW0iLFsiZmllbGQtaWQiLDExNjg0XV1dLCJleHByZXNzaW9ucyI6e30sImpvaW5zIjpbXSwiZmlsdGVyIjpbXX0sInR5cGUiOiJxdWVyeSIsInZlcnNpb24iOiJwZXJmZWN0In0=\",\"QueryType\":\"query\",\"Description\":\"testCustomColumnFromAggregation_02\",\"Time\":\"2022-05-17 15:44:38\",\"UpdateTime\":\"2022-05-19 10:52:05\",\"UserId\":\"33d8d854-6a07-47b3-8326-c9403db7a430\",\"Enable\":\"True\",\"DatabaseId\":\"9b4187ea-99be-ca76-ffdb-304b0a7f2a8b\",\"GroupId\":\"835f8160-6e09-41d9-89dc-b41662991631\",\"Cache\":\"True\",\"CacheInterval\":\"20\"},{\"DatamodelQueryId\":\"148\",\"ReqAvgUseMs\":\"287\",\"Id\":\"5b6d2773-12bf-49de-889b-6ebf00db053d\",\"DatamodelQueryName\":\"办卡数据\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IFxyXG4gIGNhc2UgXHJcbiAgICB3aGVuIHByb21vdGlvbl90eXBlPTEgdGhlbiAnMjk556S85YyFJ1xyXG4gICAgd2hlbiBwcm9tb3Rpb25fdHlwZT0yIHRoZW4gJzYwMHB2J1xyXG4gICAgd2hlbiBwcm9tb3Rpb25fdHlwZSBpcyBudWxsIHRoZW4gJ+acquWNh+e6pydcclxuICAgIGVuZCBhcyAn5Yqe5Y2h5Lq65pWw5Y+K5Y2H57qn5pa55byPJyxjb3VudCgqKSBhcyAn5Lq65pWwJ1xyXG5mcm9tIG1lbWJlcl9pbmZvXHJcbndoZXJlIG9wZW5fY2FyZF90aW1lPj0nMjAyMi4wNS4wMScgYW5kIG9wZW5fY2FyZF90aW1lPCcyMDIyLjA2LjAxJ1xyXG5ncm91cCBieSDlip7ljaHkurrmlbDlj4rljYfnuqfmlrnlvI8iLCJ0ZW1wbGF0ZS10YWdzIjp7fX0sImRhdGFiYXNlIjoxNH0=\",\"QueryType\":\"native\",\"Description\":\"办卡人数及升级人数\",\"Time\":\"2022-05-18 10:38:01\",\"UpdateTime\":\"2022-05-18 10:40:58\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"14a3b845-f66e-2b5f-73a8-9332a98faa97\",\"GroupId\":\"00000000-0000-0000-0000-000000000000\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"144\",\"ReqAvgUseMs\":\"46424\",\"Id\":\"4c6702d3-88af-47d5-97a2-74d1f4a79cb8\",\"DatamodelQueryName\":\"月销售额统计\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IG9yZGVyX21vbnRoLHN1bShvcmRlcl9hbW91bnQpXHJcbmZyb20gb3JkZXJfb3JkZXJcclxud2hlcmUgb3JkZXJfbW9udGg+PScyMDIyMDEnIGFuZCBvcmRlcl9zdGF0dXMgaW4oMiwzLDk5KVxyXG5ncm91cCBieSBvcmRlcl9tb250aCIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"统计月销售额，未扣除换购\",\"Time\":\"2022-05-17 16:18:39\",\"UpdateTime\":\"2022-05-17 16:34:45\",\"UserId\":\"216df0f1-d99d-4896-ab27-c9d8aa07b0c3\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"00000000-0000-0000-0000-000000000000\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"128\",\"ReqAvgUseMs\":\"11379\",\"Id\":\"0084aaa1-ace7-4367-84fe-99455c2d71b8\",\"DatamodelQueryName\":\"本月新增客户数\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5IjoiU0VMRUNUIGNvdW50KCopIEFTIGBjb3VudGBcclxuRlJPTSBgaHl6bGBcclxuV0hFUkUgYGFjY21vbmA9ZGF0ZV9mb3JtYXQobm93KCksICclWS4lbScpIiwidGVtcGxhdGUtdGFncyI6e319LCJkYXRhYmFzZSI6MTJ9\",\"QueryType\":\"native\",\"Description\":\"本月新增客户数\",\"Time\":\"2022-05-13 11:08:30\",\"UpdateTime\":\"2022-05-13 11:09:52\",\"UserId\":\"3c879e85-5b86-4dba-b5e2-c1facfeabaa5\",\"Enable\":\"True\",\"DatabaseId\":\"b1605471-1082-1f82-85a8-a9c505e8bfc1\",\"GroupId\":\"880a5dda-918e-425f-8bc8-e0b89a77f11a\",\"Cache\":\"True\",\"CacheInterval\":\"20\"},{\"DatamodelQueryId\":\"104\",\"ReqAvgUseMs\":\"25812\",\"Id\":\"e3570a03-58fc-4373-86a1-4db33f823f7e\",\"DatamodelQueryName\":\"4月单品TOP20（单位：万元）\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IGEuaWQsYS5vcmRlcl9zdGF0dXMsYS5vcmRlcl9tb250aCxiLm9yZGVyX2lkLGIuc2VyaWFsX25vLHN1bShiLnRvdGFsX3ByaWNlKSxcclxuICAgIENBU0VcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdGQScgVEhFTiAn5rKZ5qOY6Iy2J1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0ZBMTAnIFRIRU4gJ+aymeajmOiMtidcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdGQTMwJyBUSEVOICfmspnmo5jojLYnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nRkExMDAxJyBUSEVOICfmspnmo5jojLYnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nQUcnIFRIRU4gJ+iKpuiNn+iDtidcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdBRzInIFRIRU4gJ+iKpuiNn+iDtidcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdBRzEwJyBUSEVOICfoiqbojZ/og7YnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nQUcxMDAxJyBUSEVOICfoiqbojZ/og7YnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nQU1QTicgVEhFTiAn55+/54mp57KJJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0FNUE4xMCcgVEhFTiAn55+/54mp57KJJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0FNUE4zMCcgVEhFTiAn55+/54mp57KJJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0FNUE4xMDAxJyBUSEVOICfnn7/niannsoknXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nUFNOJyBUSEVOICfogr3ol7vokKXlhbvnsoknXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nUFNONicgVEhFTiAn6IK96Je76JCl5YW757KJJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J1BTTjMwJyBUSEVOICfogr3ol7vokKXlhbvnsoknXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nUFNONjAxJyBUSEVOICfogr3ol7vokKXlhbvnsoknXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nQ1BUMDInIFRIRU4gJ+ivreazsOeJhydcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdDUFQwMycgVEhFTiAn6K+t5rOw54mHJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0NQVDgnIFRIRU4gJ+ivreazsOeJhydcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdOQicgVEhFTiAn6JCl5YW76aSQJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J05CMzAnIFRIRU4gJ+iQpeWFu+mkkCdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdPTFMnIFRIRU4gJ+S4gOeUn+ezlidcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdPTFM2JyBUSEVOICfkuIDnlJ/ns5YnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nUFNHJyBUSEVOICfmspnokpwnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nUFNHNCcgVEhFTiAn5rKZ6JKcJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J1BTRzQwMScgVEhFTiAn5rKZ6JKcJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J01BNTAyMScgVEhFTiAn5ruL5ram54i96IKk5rC0J1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J01BNTAyMTMnIFRIRU4gJ+a7i+a2pueIveiCpOawtCdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdQU0QnIFRIRU4gJ+S6v+eUn+iPjCdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdDRFAnIFRIRU4gJ+aymeajmOaxgSdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdNQTUwMTEnIFRIRU4gJ+aflOWSjOa0geiCpOS5sydcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdNQTcwMzknIFRIRU4gJ+WuieeTtueyvuWNjua2sidcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdBTTAwNzMwJyBUSEVOICfoiqbojZ/pnaLohpwnXHJcbiAgICAgICAgV0hFTiBiLnNlcmlhbF9ubz0nQU0wMDcnIFRIRU4gJ+iKpuiNn+mdouiGnCdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdFSFAnIFRIRU4gJ+iGs+mjn+e6pOe7tOeyiSdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdFSFAzMCcgVEhFTiAn5aSN5ZCI6Iaz6aOf57qk57u057KJJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0dQTScgVEhFTiAn5YGl5omsJ1xyXG4gICAgICAgIFdIRU4gYi5zZXJpYWxfbm89J0ZEMzAnIFRIRU4gJ+mrmOe6pOS5kCdcclxuICAgICAgICBXSEVOIGIuc2VyaWFsX25vPSdPTEknIFRIRU4gJ+a0u+eri+WkmidcclxuICAgICAgICBlbHNlIGIuc2VyaWFsX25vXHJcbiAgICBFTkQgQVMgYmNjXHJcbmZyb21cclxuKHNlbGVjdCBpZCxvcmRlcl9zdGF0dXMsb3JkZXJfbW9udGggZnJvbSBvcmRlcl9vcmRlcikgYVxyXG5qb2luXHJcbihzZWxlY3Qgb3JkZXJfaWQsc2VyaWFsX25vLHRvdGFsX3ByaWNlIGZyb20gb3JkZXJfcHJvZHVjdCkgYlxyXG5vbiAoYS5pZD1iLm9yZGVyX2lkKVxyXG53aGVyZSBhLm9yZGVyX3N0YXR1cyBpbiAoMiwzLDk5KSBhbmQgYS5vcmRlcl9tb250aCA9JzIwMjIwNSdcclxuZ3JvdXAgYnkgYmNjXHJcbm9yZGVyIGJ5IHN1bShiLnRvdGFsX3ByaWNlKSBkZXNjXHJcbmxpbWl0IDIwIiwidGVtcGxhdGUtdGFncyI6e319LCJkYXRhYmFzZSI6MTN9\",\"QueryType\":\"native\",\"Description\":\"4月单品TOP20\",\"Time\":\"2022-05-07 14:23:50\",\"UpdateTime\":\"2022-05-13 09:27:44\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"88\",\"ReqAvgUseMs\":\"8009\",\"Id\":\"806940f4-9f27-4e0b-b278-1f146728406c\",\"DatamodelQueryName\":\"本月销售额\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTMsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NjYzLCJqb2lucyI6W10sImZpbHRlciI6W1siPSIsWyJmaWVsZC1pZCIsMTEwNzJdLCIyMDIyMDUiXSwiYW5kIixbImJyYWNrZXQiLFtbIj0iLFsiZmllbGQtaWQiLDExMDc4XSwiMiJdLCJvciIsWyI9IixbImZpZWxkLWlkIiwxMTA3OF0sIjMiXSwib3IiLFsiPSIsWyJmaWVsZC1pZCIsMTEwNzhdLCI5OSJdXV1dLCJhZ2dyZWdhdGlvbiI6W1sic3VtIixbImZpZWxkLWlkIiwxMTA0MV1dXX0sInR5cGUiOiJxdWVyeSIsInZlcnNpb24iOiJwZXJmZWN0In0=\",\"QueryType\":\"query\",\"Description\":\"本月销售额\",\"Time\":\"2022-05-06 15:59:11\",\"UpdateTime\":\"2022-05-11 14:47:36\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"93\",\"ReqAvgUseMs\":\"31719\",\"Id\":\"1bc1df9f-4d5f-460f-91ec-286d40d0eb48\",\"DatamodelQueryName\":\"本月省份业绩排名\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5IjoiU0VMRUNUICBcclxuICAgIENBU0VcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45bm/5Lic5YiG5YWs5Y+4JyBUSEVOICflub/kuJwnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOa1meaxn+WIhuWFrOWPuCcgVEhFTiAn5rWZ5rGfJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jmsZ/oi4/liIblhazlj7gnIFRIRU4gJ+axn+iLjydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45rKz5Y2X5YiG5YWs5Y+4JyBUSEVOICfmsrPljZcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOays+WMl+WIhuWFrOWPuCcgVEhFTiAn5rKz5YyXJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jlsbHkuJzliIblhazlj7gnIFRIRU4gJ+WxseS4nCdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+46Z2S5bKb5YiG5YWs5Y+4JyBUSEVOICflsbHkuJwnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOemj+W7uuWIhuWFrOWPuCcgVEhFTiAn56aP5bu6J1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jmuZbljZfliIblhazlj7gnIFRIRU4gJ+a5luWNlydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45a6J5b695YiG5YWs5Y+4JyBUSEVOICflronlvr0nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOWugeazouWIhuWFrOWPuCcgVEhFTiAn5rWZ5rGfJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jlkInmnpfliIblhazlj7gnIFRIRU4gJ+WQieaelydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45LqR5Y2X5YiG5YWs5Y+4JyBUSEVOICfkupHljZcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOWbm+W3neWIhuWFrOWPuCcgVEhFTiAn5Zub5bedJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jovr3lroHliIblhazlj7gnIFRIRU4gJ+i+veWugSdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45rGf6KW/5YiG5YWs5Y+4JyBUSEVOICfmsZ/opb8nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOWkqea0peWIhuWFrOWPuCcgVEhFTiAn5aSp5rSlJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jmuZbljJfliIblhazlj7gnIFRIRU4gJ+a5luWMlydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+46YeN5bqG5YiG5YWs5Y+4JyBUSEVOICfph43luoYnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOW5v+ilv+WIhuWFrOWPuCcgVEhFTiAn5bm/6KW/J1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jlsbHopb/liIblhazlj7gnIFRIRU4gJ+WxseilvydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45LiK5rW35YiG5YWs5Y+4JyBUSEVOICfkuIrmtbcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOS4reWxseWIhuWFrOWPuCcgVEhFTiAn5bm/5LicJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jotLXlt57liIblhazlj7gnIFRIRU4gJ+i0teW3nidcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45YaF6JKZ5Y+k5YiG5YWs5Y+4JyBUSEVOICflhoXokpnlj6QnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOWMl+S6rOWIhuWFrOWPuCcgVEhFTiAn5YyX5LqsJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jpu5HpvpnmsZ/liIblhazlj7gnIFRIRU4gJ+m7kem+meaxnydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+46ZmV6KW/5YiG5YWs5Y+4JyBUSEVOICfpmZXopb8nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOaWsOeWhuWIhuWFrOWPuCcgVEhFTiAn5paw55aGJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jnlJjogoPliIblhazlj7gnIFRIRU4gJ+eUmOiCgydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+45aSn6L+e5YiG5YWs5Y+4JyBUSEVOICfovr3lroEnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOa1t+WNl+WIhuWFrOWPuCcgVEhFTiAn5rW35Y2XJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflroznvo7vvIjkuK3lm73vvInmnInpmZDlhazlj7jlroHlpI/liIblhazlj7gnIFRIRU4gJ+WugeWkjydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6M576O77yI5Lit5Zu977yJ5pyJ6ZmQ5YWs5Y+46Z2S5rW35YiG5YWs5Y+4JyBUSEVOICfpnZLmtbcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WujOe+ju+8iOS4reWbve+8ieaciemZkOWFrOWPuOilv+iXj+WIhuWFrOWPuCcgVEhFTiAn6KW/6JePJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflub/kuJzliIblhazlj7gnIFRIRU4gJ+W5v+S4nCdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5rWZ5rGf5YiG5YWs5Y+4JyBUSEVOICfmtZnmsZ8nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+axn+iLj+WIhuWFrOWPuCcgVEhFTiAn5rGf6IuPJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfmsrPljZfliIblhazlj7gnIFRIRU4gJ+ays+WNlydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5rKz5YyX5YiG5YWs5Y+4JyBUSEVOICfmsrPljJcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WxseS4nOWIhuWFrOWPuCcgVEhFTiAn5bGx5LicJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfpnZLlspvliIblhazlj7gnIFRIRU4gJ+WxseS4nCdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n56aP5bu65YiG5YWs5Y+4JyBUSEVOICfnpo/lu7onXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+a5luWNl+WIhuWFrOWPuCcgVEhFTiAn5rmW5Y2XJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflronlvr3liIblhazlj7gnIFRIRU4gJ+WuieW+vSdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5a6B5rOi5YiG5YWs5Y+4JyBUSEVOICfmtZnmsZ8nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WQieael+WIhuWFrOWPuCcgVEhFTiAn5ZCJ5p6XJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfkupHljZfliIblhazlj7gnIFRIRU4gJ+S6keWNlydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5Zub5bed5YiG5YWs5Y+4JyBUSEVOICflm5vlt50nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+i+veWugeWIhuWFrOWPuCcgVEhFTiAn6L695a6BJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfmsZ/opb/liIblhazlj7gnIFRIRU4gJ+axn+ilvydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5aSp5rSl5YiG5YWs5Y+4JyBUSEVOICflpKnmtKUnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+a5luWMl+WIhuWFrOWPuCcgVEhFTiAn5rmW5YyXJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfph43luobliIblhazlj7gnIFRIRU4gJ+mHjeW6hidcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5bm/6KW/5YiG5YWs5Y+4JyBUSEVOICflub/opb8nXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+Wxseilv+WIhuWFrOWPuCcgVEhFTiAn5bGx6KW/J1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfkuIrmtbfliIblhazlj7gnIFRIRU4gJ+S4iua1tydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5Lit5bGx5YiG5YWs5Y+4JyBUSEVOICflub/kuJwnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+i0teW3nuWIhuWFrOWPuCcgVEhFTiAn6LS15beeJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflhoXokpnlj6TliIblhazlj7gnIFRIRU4gJ+WGheiSmeWPpCdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5YyX5Lqs5YiG5YWs5Y+4JyBUSEVOICfljJfkuqwnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+m7kem+meaxn+WIhuWFrOWPuCcgVEhFTiAn6buR6b6Z5rGfJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfpmZXopb/liIblhazlj7gnIFRIRU4gJ+mZleilvydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5paw55aG5YiG5YWs5Y+4JyBUSEVOICfmlrDnloYnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+eUmOiCg+WIhuWFrOWPuCcgVEhFTiAn55SY6IKDJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSflpKfov57liIblhazlj7gnIFRIRU4gJ+i+veWugSdcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n5rW35Y2X5YiG5YWs5Y+4JyBUSEVOICfmtbfljZcnXHJcbiAgICAgICAgV0hFTiBjb21wYW55X25hbWU9J+WugeWkj+WIhuWFrOWPuCcgVEhFTiAn5a6B5aSPJ1xyXG4gICAgICAgIFdIRU4gY29tcGFueV9uYW1lPSfpnZLmtbfliIblhazlj7gnIFRIRU4gJ+mdkua1tydcclxuICAgICAgICBXSEVOIGNvbXBhbnlfbmFtZT0n6KW/6JeP5YiG5YWs5Y+4JyBUSEVOICfopb/ol48nXHJcbiAgICAgICAgZWxzZSBjb21wYW55X25hbWVcclxuICAgIEVORCBBUyAn55yB5Lu9Jyxcclxuc3VtKGNhc2Ugd2hlbiBvcmRlcl9tb250aD0nMjAyMjA1JyB0aGVuIG9yZGVyX2Ftb3VudCoxIGVsc2Ugb3JkZXJfYW1vdW50KjAgZW5kKSBBUyAn5pys5pyI6ZSA5ZSu6aKdJyxcclxuc3VtKGNhc2Ugd2hlbiBvcmRlcl9tb250aD0nMjAyMTA1JyB0aGVuIG9yZGVyX2Ftb3VudCoxIGVsc2Ugb3JkZXJfYW1vdW50KjAgZW5kKSBBUyAnMjAyMTA1Jyxcclxuc3VtKGNhc2Ugd2hlbiBvcmRlcl9tb250aD0nMjAyMjA1JyB0aGVuIG9yZGVyX2Ftb3VudCoxIGVsc2Ugb3JkZXJfYW1vdW50KjAgZW5kKS9zdW0oY2FzZSB3aGVuIG9yZGVyX21vbnRoPScyMDIxMDUnIHRoZW4gb3JkZXJfYW1vdW50KjEgZWxzZSBvcmRlcl9hbW91bnQqMCBlbmQpIGFzICflkIzmr5Tovr7miJDnjocnXHJcbkZST00gb3JkZXJfb3JkZXJcclxuV0hFUkUgKG9yZGVyX3N0YXR1cz0yIE9SIG9yZGVyX3N0YXR1cz0zIE9SIG9yZGVyX3N0YXR1cz05OSkgYW5kIChvcmRlcl9tb250aD0nMjAyMjA1JyBvciBvcmRlcl9tb250aD0nMjAyMTA1JylcclxuR1JPVVAgQlkg55yB5Lu9XHJcbk9SREVSIEJZIOacrOaciOmUgOWUruminSBERVNDXHJcbiIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"本月省份业绩排名\",\"Time\":\"2022-05-07 10:44:20\",\"UpdateTime\":\"2022-05-11 14:35:18\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"98\",\"ReqAvgUseMs\":\"43\",\"Id\":\"d48d136a-5eb9-4e4b-a732-333d2cc39ee4\",\"DatamodelQueryName\":\"本年新增人数\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IGNvdW50KCopIGZyb20gbWVtYmVyX2luZm9cclxud2hlcmUgb3Blbl9jYXJkX3RpbWU+PScyMDIyLjAxLjAxJyIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjE0fQ==\",\"QueryType\":\"native\",\"Description\":\"本年新增人数\",\"Time\":\"2022-05-07 13:54:23\",\"UpdateTime\":\"2022-05-11 11:03:26\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"14a3b845-f66e-2b5f-73a8-9332a98faa97\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"92\",\"ReqAvgUseMs\":\"604\",\"Id\":\"0c7ed8d4-0193-4216-be23-20c26751c3a6\",\"DatamodelQueryName\":\"今日客单价\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5IjoiU0VMRUNUIGF2Zyhgc291cmNlYC5gc3VtYCkgQVMgYGF2Z2BcclxuRlJPTSAoU0VMRUNUIGBvcmRlcl9vcmRlcmAuYGN1c3RvbWVyX2NhcmRgIEFTIGBjdXN0b21lcl9jYXJkYCwgc3VtKGBvcmRlcl9vcmRlcmAuYG9yZGVyX2Ftb3VudGApIEFTIGBzdW1gIEZST00gYG9yZGVyX29yZGVyYFxyXG5XSEVSRSAoYG9yZGVyX29yZGVyYC5gcGF5X3RpbWVgID49ZGF0ZShub3coNikpIGFuZCBgb3JkZXJfb3JkZXJgLmBwYXlfdGltZWAgPj1kYXRlKG5vdyg2KSkgYW5kIGBvcmRlcl9vcmRlcmAuYHBheV90aW1lYCA8IGRhdGVfYWRkKGRhdGUobm93KDYpKSwgSU5URVJWQUwgMSBkYXkpXHJcbiAgIEFORCAoYG9yZGVyX29yZGVyYC5gb3JkZXJfc3RhdHVzYCA9IDJcclxuICAgIE9SIGBvcmRlcl9vcmRlcmAuYG9yZGVyX3N0YXR1c2AgPSAzIE9SIGBvcmRlcl9vcmRlcmAuYG9yZGVyX3N0YXR1c2AgPSA5OSkpXHJcbkdST1VQIEJZIGBvcmRlcl9vcmRlcmAuYGN1c3RvbWVyX2NhcmRgXHJcbk9SREVSIEJZIGBvcmRlcl9vcmRlcmAuYGN1c3RvbWVyX2NhcmRgIEFTQykgYHNvdXJjZWAiLCJ0ZW1wbGF0ZS10YWdzIjp7fX0sImRhdGFiYXNlIjoxM30=\",\"QueryType\":\"native\",\"Description\":\"今日客单价\",\"Time\":\"2022-05-07 09:02:23\",\"UpdateTime\":\"2022-05-11 11:02:26\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"78\",\"ReqAvgUseMs\":\"8868\",\"Id\":\"a306141b-e71c-43fc-8c58-b0e72302e5c7\",\"DatamodelQueryName\":\"本月促销活动\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IGEuaWQsYS5vcmRlcl9zdGF0dXMsYS5vcmRlcl9tb250aCxiLm9yZGVyX2lkLFxyXG5jYXNlIFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J01BWUgyMjQxJyB0aGVuICfnvo7ogqTlpZfoo4UnIFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0pDMjAyMicgdGhlbiAn5rS75Yqb6IKg6ZO45aWX6KOFJ1xyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0FDQzIwMjInIHRoZW4gJ0FDQ+S8mOaDoOWll+ijhSdcclxuICBlbmQgYXMgJ+a0u+WKqOWQjeensCcsXHJcbnN1bShiLnF1YW50aXR5KSBhcyAn6ZSA5ZSu6YePJyxzdW0oYi50b3RhbF9wcmljZSkgYXMgJ+mUgOWUruminScsXHJcbmNhc2UgXHJcbiAgd2hlbiBiLnNlcmlhbF9ubz0nTUFZSDIyNDEnIHRoZW4gc3VtKGIucXVhbnRpdHkpLzM3MDAwIFxyXG4gIHdoZW4gYi5zZXJpYWxfbm89J0pDMjAyMicgdGhlbiBzdW0oYi5xdWFudGl0eSkvMzc4MDBcclxuICB3aGVuIGIuc2VyaWFsX25vPSdBQ0MyMDIyJyB0aGVuIHN1bShiLnF1YW50aXR5KS8yNTAwMFxyXG4gIGVuZCBhcyAn55uu5qCH5a6M5oiQ546HJyBcclxuZnJvbVxyXG4oc2VsZWN0IGlkLG9yZGVyX3N0YXR1cyxvcmRlcl9tb250aCBmcm9tIG9yZGVyX29yZGVyKSBhXHJcbmpvaW5cclxuKHNlbGVjdCBvcmRlcl9pZCxzZXJpYWxfbm8scXVhbnRpdHksdG90YWxfcHJpY2UgZnJvbSBvcmRlcl9wcm9kdWN0KSBiXHJcbm9uIChhLmlkPWIub3JkZXJfaWQpXHJcbndoZXJlIGEub3JkZXJfc3RhdHVzIGluICgyLDMsOTkpIGFuZCBhLm9yZGVyX21vbnRoID0nMjAyMjA0JyBhbmQgYi5zZXJpYWxfbm8gaW4gKCdNQVlIMjI0MScsJ0pDMjAyMicsJ0FDQzIwMjInKVxyXG5ncm91cCBieSBiLnNlcmlhbF9ubyIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"统计本月促销产品的销售额\",\"Time\":\"2022-04-29 10:25:33\",\"UpdateTime\":\"2022-05-11 11:01:29\",\"UserId\":\"33d8d854-6a07-47b3-8326-c9403db7a430\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"00000000-0000-0000-0000-000000000000\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"87\",\"ReqAvgUseMs\":\"300\",\"Id\":\"e59be632-b047-4f64-9c38-85841253fae2\",\"DatamodelQueryName\":\"今日销售额（万元）\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTMsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NjYzLCJqb2lucyI6W10sImZpbHRlciI6W1sidGltZS1pbnRlcnZhbCIsWyJmaWVsZC1pZCIsMTEwNDBdLCJjdXJyZW50IiwiZGF5Il0sImFuZCIsWyI9IixbImZpZWxkLWlkIiwxMTA3Ml0sIjIwMjIwNSJdLCJhbmQiLFsiYnJhY2tldCIsW1siPSIsWyJmaWVsZC1pZCIsMTEwNzhdLCIyIl0sIm9yIixbIj0iLFsiZmllbGQtaWQiLDExMDc4XSwiMyJdLCJvciIsWyI9IixbImZpZWxkLWlkIiwxMTA3OF0sIjk5Il1dXV0sImFnZ3JlZ2F0aW9uIjpbWyJzdW0iLFsiZmllbGQtaWQiLDExMDQxXV1dfSwidHlwZSI6InF1ZXJ5IiwidmVyc2lvbiI6InBlcmZlY3QifQ==\",\"QueryType\":\"query\",\"Description\":\"今日销售额（万元）\",\"Time\":\"2022-05-06 15:55:28\",\"UpdateTime\":\"2022-05-11 11:01:29\",\"UserId\":\"cf8d3088-31ec-4230-bfcf-fcffa1fdd56c\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"5\"},{\"DatamodelQueryId\":\"67\",\"ReqAvgUseMs\":\"2118\",\"Id\":\"84bd12d7-299c-4691-ba63-ca7c17aa9633\",\"DatamodelQueryName\":\"testTodayMoney\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MTMsInF1ZXJ5Ijp7InNvdXJjZS10YWJsZSI6NjYzLCJqb2lucyI6W10sImZpbHRlciI6W1sidGltZS1pbnRlcnZhbCIsWyJmaWVsZC1pZCIsMTEwNDBdLCJjdXJyZW50IiwiZGF5Il0sImFuZCIsWyI9IixbImZpZWxkLWlkIiwxMTA3Ml0sIjIwMjIwNCJdLCJhbmQiLFsiPSIsWyJmaWVsZC1pZCIsMTEwNzhdLCIzIl1dLCJhZ2dyZWdhdGlvbiI6W1sic3VtIixbImZpZWxkLWlkIiwxMTA0MV1dXX0sInR5cGUiOiJxdWVyeSIsInZlcnNpb24iOiJwZXJmZWN0In0=\",\"QueryType\":\"\",\"Description\":\"今日业绩\",\"Time\":\"2022-04-07 11:54:39\",\"UpdateTime\":\"2022-05-11 11:00:04\",\"UserId\":\"33d8d854-6a07-47b3-8326-c9403db7a430\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"00000000-0000-0000-0000-000000000000\",\"Cache\":\"True\",\"CacheInterval\":\"10\"},{\"DatamodelQueryId\":\"97\",\"ReqAvgUseMs\":\"57913\",\"Id\":\"e33cceab-e648-49e4-9ab6-b0adc183ae3d\",\"DatamodelQueryName\":\"本年销售额\",\"QueryConfig\":\"eyJ0eXBlIjoibmF0aXZlIiwibmF0aXZlIjp7InF1ZXJ5Ijoic2VsZWN0IHN1bShvcmRlcl9hbW91bnQpIGZyb20gb3JkZXJfb3JkZXJcclxud2hlcmUgb3JkZXJfbW9udGg+PScyMDIyMDEnIGFuZCBvcmRlcl9zdGF0dXMgaW4oMiwzLDk5KSIsInRlbXBsYXRlLXRhZ3MiOnt9fSwiZGF0YWJhc2UiOjEzfQ==\",\"QueryType\":\"native\",\"Description\":\"本年销售额（sql有>号报错）\",\"Time\":\"2022-05-07 13:43:26\",\"UpdateTime\":\"2022-05-11 10:59:51\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"True\",\"DatabaseId\":\"ae9b33fe-d4c2-0192-cd64-b5576b36baeb\",\"GroupId\":\"e4da23b8-48e3-4708-a4df-a20cf064adae\",\"Cache\":\"True\",\"CacheInterval\":\"30\"},{\"DatamodelQueryId\":\"68\",\"ReqAvgUseMs\":\"2\",\"Id\":\"f536c67b-b1d1-4695-8392-64b0801264c2\",\"DatamodelQueryName\":\"liang_test\",\"QueryConfig\":\"eyJkYXRhYmFzZSI6MiwicXVlcnkiOnsic291cmNlLXRhYmxlIjoyfSwidHlwZSI6InF1ZXJ5IiwidmVyc2lvbiI6InBlcmZlY3QifQ==\",\"QueryType\":null,\"Description\":\"liang_testliang_testliang_testliang_test\",\"Time\":\"2022-04-08 14:59:49\",\"UpdateTime\":\"2022-05-10 15:40:07\",\"UserId\":\"86683e37-41d4-4ef6-be6b-ce178eb186bb\",\"Enable\":\"False\",\"DatabaseId\":\"8edc4c89-88d1-a59d-8cb5-787489993b08\",\"GroupId\":\"00000000-0000-0000-0000-000000000000\",\"Cache\":\"True\",\"CacheInterval\":\"10\"}],\"RecordCount\":\"22\"}";
//		this.PrintObject(aa);
//		
//		DefaultListDataSource2<List<DataModelInfo>> o3 = JSON.parseObject(aa, new TypeReference<DefaultListDataSource2<List<DataModelInfo>>>(){}) ;
//
//		this.PrintObject(o3);
//
//	}
//
//	public void testClassName() {
//		Object o = new UserOrg();
//		String a = o.getClass().toString();
//		String b = o.getClass().getName();
//		String c = o.getClass().getSimpleName();
//		String d = o.getClass().getTypeName();
//		System.out.println(a);
//		System.out.println(b);
//		System.out.println(c);
//		System.out.println(d);
//		/**
//		 * 输出: class pf.java.pfHelper.model.UserOrg pf.java.pfHelper.model.UserOrg
//		 * UserOrg pf.java.pfHelper.model.UserOrg
//		 */
//	}
//
//	public void testCreateSqlTable() throws Exception {
//		// 发现mysql8.0.23里,语句中有分号的号不能用execute方法
//		
////		//clickhouse
////		String sql = "CREATE TABLE `user_profile_attr_all` ( `user_id` varchar(100) ,`tag_weight` varchar(100) ,`data_date` datetime ,`tag_id` varchar(100) \r\n"
////				+ "\r\n"
////				+ ",`theme` varchar(100)    ); CREATE INDEX  idx_user_id ON user_profile_attr_all (`user_id`); CREATE INDEX  idx_theme ON \r\n"
////				+ "\r\n" + "user_profile_attr_all (`theme`) ";
//////		String sql="CREATE TABLE `user_profile_attr_all` ( `user_id` varchar(100) ,`tag_weight` varchar(100) ,`data_date` datetime ,`tag_id` varchar(100),`theme` varchar(100))";
//////		String sql1="CREATE INDEX  idx_user_id ON user_profile_attr_all (`user_id`)";
//////		String sql2="CREATE INDEX  idx_theme ON user_profile_attr_all (`theme`);";
////
////		IPFJdbc dstJdbc = JdbcHelperTest.GetUserProfileJdbc();
////		ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
////		Object r = dstExec.ExecuteSql(new PFSqlCommandString(sql.split(";")));
////		assertTrue(r != null);
//		
//		//sqlserver
//		String sql = "" + 
//				"SELECT top 10 [ProductId]\r\n" + 
//				"      ,[IceFlag]\r\n" + 
//				"      ,[CreateDate]\r\n" + 
//				"      ,[CreatePerson]\r\n" + 
//				"      ,[UpdateDate]\r\n" + 
//				"      ,[UpdatePerson]\r\n" + 
//				"       ,[DeleteFlag]\r\n" + 
//				"       ,[BelongToId]\r\n" + 
//				"       ,[TypeCode]\r\n" + 
//				"       ,[ProductCode]\r\n" + 
//				"       ,[ProductName]\r\n" + 
//				"       ,[BuyModel]\r\n" + 
//				"      -- ,[ModelFile]\r\n" + 
//				"      -- ,[TypeId]\r\n" + 
//				"      -- ,[ApproveState]\r\n" + 
//				"       ,[Price]\r\n" + 
//				"      ,[OrganizeCode]\r\n" + 
//				"      ,[OwnerOrganize]\r\n" + 
//				"      ,[TopFlag]\r\n" + 
//				"      ,1 as Age\r\n" + 
//				"  FROM [SellGirl.Business].[dbo].[product_product]";
//
//
//		IPFJdbc srcJdbc = JdbcHelperTest.GetSellGirlJdbc();
//		IPFJdbc dstJdbc = JdbcHelperTest.GetSellGirlJdbc2();
//
//		PFSqlCreateTableCollection models =PFSqlCreateTableCollection.Init(dstJdbc);
//		models.TableName = "product_product";
//
////		//建表
//		ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//		
//		 models=dstExec.GetCreateTableSql(srcJdbc, sql, models, null);		 
//
//			PFSqlCommandString sqlstr = models.ToSql();
//			System.out.print(sqlstr);
//			dstExec.ExecuteSql(sqlstr);
//			
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
//			
//			
//		
//
////		Class.forName("com.mysql.cj.jdbc.Driver");
////////      Class.forName("com.mysql.jdbc.Driver");
////////		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_demo?useSSL=false&serverTimezone=UTC","root","password");
//////		//Connection conn = DriverManager.getConnection(dstJdbc.getUrl(),dstJdbc.getUsername(),dstJdbc.getPassword());//这样一样报错语法错误
////		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.205.111:3306/user_profile?useSSL=false&serverTimezone=UTC","root","perfect");
////		Statement stmt = conn.createStatement();
////        Boolean b=false;
////        
////        b=stmt.execute(sql);
////////         b=stmt.execute(sql1);
////////         b=stmt.execute(sql2);
////// 		stmt.addBatch(sql);
////// 		stmt.addBatch(sql1);
////// 		stmt.addBatch(sql2);
////// 		stmt.executeBatch();
////        assertTrue(b!=null);
//	}
//
//	public void testCreateMySqlTable() throws Exception {
//		// 发现mysql8.0.23里,语句中有分号的号不能用execute方法
//		//sqlserver
////		String sql = "" + 
////				"select order_month,count(distinct customer_card) as 'buyer_qty',\r\n" + 
////				"sum(order_amount) as order_amount\r\n" + 
////				"from mall_center_order.order_order\r\n" + 
////				"where order_month='202201' and order_status in (2,3,99)\r\n" + 
////				"group by order_month";
////		PFDataHelper.WriteLocalTxt("aaa","test01.txt", LocalDataType.Deletable);
////		return ;
//		String sql =PFDataHelper.ReadLocalTxt("test01.txt",  LocalDataType.Deletable);
//
//
//		IPFJdbc srcJdbc = JdbcHelperTest.GetLiGeOrderJdbc();
//		IPFJdbc dstJdbc = JdbcHelperTest.GetLiGeSaleJdbc();
//
//		PFSqlCreateTableCollection models =PFSqlCreateTableCollection.Init(dstJdbc);
//		models.TableName = "order_order_cur_month";
//
////		//建表
//		ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//		
//		 models=dstExec.GetCreateTableSql(srcJdbc, sql, models, null);		 
//
//			PFSqlCommandString sqlstr = models.ToSql();
//			System.out.print(sqlstr);
//			dstExec.ExecuteSql(sqlstr);
//			
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
//			
//			
//		
//
////		Class.forName("com.mysql.cj.jdbc.Driver");
////////      Class.forName("com.mysql.jdbc.Driver");
////////		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_demo?useSSL=false&serverTimezone=UTC","root","password");
//////		//Connection conn = DriverManager.getConnection(dstJdbc.getUrl(),dstJdbc.getUsername(),dstJdbc.getPassword());//这样一样报错语法错误
////		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.205.111:3306/user_profile?useSSL=false&serverTimezone=UTC","root","perfect");
////		Statement stmt = conn.createStatement();
////        Boolean b=false;
////        
////        b=stmt.execute(sql);
////////         b=stmt.execute(sql1);
////////         b=stmt.execute(sql2);
////// 		stmt.addBatch(sql);
////// 		stmt.addBatch(sql1);
////// 		stmt.addBatch(sql2);
////// 		stmt.executeBatch();
////        assertTrue(b!=null);
//	}
//	public void testCreateClickHouseTable() throws Exception {
//		PFDataHelper.SetConfigMapper(new PFConfigTestMapper());
//
//		IPFJdbc srcJdbc = JdbcHelperTest.GetYJQueryJdbc();
//		IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseShopJdbc();
//
//		srcJdbc.setUrl(srcJdbc.getUrl().replace("yjquery", "yjquery202102"));
//
//		// 发现mysql8.0.23里,语句中有分号的号不能用execute方法
////		String sql="SELECT top 100 \r\n" + 
////				"Cast('2020.12.01' as datetime) as data_date,\r\n" + 
////				" a.hybh as [user_id],\r\n" + 
////				" a.hyxb as [sex], \r\n" + 
////				" 1 as age,\r\n" + 
////				" b.inv_no as good_no,\r\n" + 
////				" b.qty as qty,\r\n" + 
////				" -- a.rhrq,\r\n" + 
////				" -- dateadd(m,3,a.rhrq),\r\n" + 
////				" (case when dateadd(m,3,a.rhrq)>GETDATE() then CONVERT(bit,1) else CONVERT(bit,0) end) as new_user,\r\n" + 
////				" DATEDIFF(m,a.rhrq,GETDATE()) as regist_month_total,\r\n" + 
////				" a.accmon as regist_month,\r\n" + 
////				" b.[code] as order_no\r\n" + 
////				"FROM hyzl a \r\n" + 
////				"inner join [sales] b   on b.hybh=a.hybh\r\n" + 
////				"" ;
//
////		String sql="SELECT  top 100 \r\n" + 
////				"Cast('2020.12.01' as datetime) as data_date,\r\n" + 
////				" a.hybh as [user_id],\r\n" + 
////				" a.hysfzh as id_card,\r\n" + 
////				" a.hyxb as [sex], \r\n" + 
////				" 1 as age,\r\n" + 
////				" b.inv_no as good_no,\r\n" + 
////				" b.qty as good_qty,\r\n" + 
////				" -- a.rhrq,\r\n" + 
////				" -- dateadd(m,3,a.rhrq),\r\n" + 
////				" (case when dateadd(m,3,a.rhrq)>GETDATE() then CONVERT(bit,1) else CONVERT(bit,0) end) as new_user,\r\n" + 
////				" DATEDIFF(m,a.rhrq,GETDATE()) as regist_month_total,\r\n" + 
////				" a.accmon as regist_month,\r\n" + 
////				" b.[code] as order_no\r\n" + 
////				"FROM hyzl a \r\n" + 
////				"inner join [sales] b   on b.hybh=a.hybh\r\n" + 
////				"" ;
//
//		String sql = "SELECT top 100 \r\n" + "Cast('2021.04.01' as datetime) as data_date,\r\n"
//				+ " hybh as user_id,\r\n" + " hyxb as sex \r\n" + "FROM hyzl WHERE qx=0";
//
//		PFSqlCreateTableCollection models = PFSqlCreateTableCollection.Init(dstJdbc);
//		models.DbName = "shop_data";
//		models.TableName = "monthly_user2";
//		// models.PrimaryKey = new String[] {"data_date","user_id"};
//		// models.ClusteredIndex = new String[] {"toYYYYMM(data_date)"} ;
//		models.Order = new String[] { "data_date", "user_id" };
//		models.Partition = new String[] { "toYYYYMM(data_date)" };
//		// models.TableIndex = new String[] {"good_no"};
//
//		// ResultSet dr = srcExec.GetDataReader(sql);
//
//		ISqlExecute srcExec = PFSqlExecute.Init(srcJdbc);
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
//
////	    String ms = null;
//		PFSqlCommandString sqlStr = models.ToSql();
//		System.out.print(sqlStr);
//
//		// ISqlExecute dstExec = new PFClickHouseSqlExecute(dstJdbc);
//		ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//		Connection conn = dstExec.GetConn();
////	     
//		Statement stmt = conn.createStatement();
////	     //这样不行
//////			stmt.addBatch(sqlStr.get(0));
//////			stmt.addBatch(sqlStr.get(1));
//////			int[] b=stmt.executeBatch();
////			
//		try {
//			// 方法1 报错
//			boolean b = stmt.execute(sqlStr.toString());
//
////	    	//方法2 ok
////				for(String i :sqlStr) {
////					stmt.execute(i);
////				}
//
////				//方法3 执行成功, 但没生效
////				for(String i :sqlStr) {
////					stmt.addBatch(i);					
////				}
////				int[] b=stmt.executeBatch();
//		} catch (Exception e) {
//			System.err.print(e);
//		}
//		String aa = "aa";
//		// stmt.setQueryTimeout(CommandTimeOut);
//
//	}
////	public void testCreateClickHouseTable02() throws Exception {
////		PFDataHelper.SetConfigMapper(new PFConfigTestMapper());
////
////		//IPFJdbc srcJdbc = JdbcHelperTest.GetYJQueryJdbc();
////		IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTestJdbc();
////
////
////		PFSqlCreateTableCollection models = PFSqlCreateTableCollection.Init(dstJdbc);
////		models.DbName = "perfect_test";
////		models.TableName = "test_tb_01";
////		models.Order = new String[] { "data_date" };
////		SqlCreateTableItem item1=new SqlCreateTableItem();
////		item1.FieldName="data_date";
////		item1.FieldType=String.class;
////		models.add(item1);
////
////		
////		ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
////		Connection conn = dstExec.GetConn();
//////	     
////		dstExec.ExecuteSql(models.ToSql());
////
////	}
//	public void testCreateHiveTable() throws Exception {
//		//PFDataHelper.SetConfigMapper(new PFConfigTestMapper());
//		initPFHelper();
//
//		IPFJdbc srcJdbc = JdbcHelperTest.GetLiGeShopJdbc();
//		//IPFJdbc dstJdbc = UncheckTest.GetHiveJdbc();
//		
//		
//
//
//		String sql = "  select now() as data_date, card_no\r\n" + 
//				" from mall_center_member.member_info where card_no is not null and card_no <>'' limit 10";
//
//		PFSqlCreateTableCollection models = new PFHiveSqlCreateTableCollection();
//		models.DbName = "wm_hive_db";
//		models.TableName = "shop_user1";
//		// models.PrimaryKey = new String[] {"data_date","user_id"};
//		// models.ClusteredIndex = new String[] {"toYYYYMM(data_date)"} ;
//		//models.Order = new String[] { "data_date", "user_id" };
//		models.Partition = new String[] { "data_date" };
//		// models.TableIndex = new String[] {"good_no"};
//
//		// ResultSet dr = srcExec.GetDataReader(sql);
//
//		ISqlExecute srcExec = PFSqlExecute.Init(srcJdbc);
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
//
//		PFSqlCommandString sqlStr = models.ToSql();
//		System.out.print(sqlStr);
//
//
//	}
//
//	public void testCreateSqlserverTable() throws Exception {
////		PFDataHelper.SetConfigMapper(new PFConfigTestMapper());
//		initPFHelper();
//
//		//DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
//		IPFJdbc srcJdbc = JdbcHelperTest.GetSellGirlJdbc();
//		IPFJdbc dstJdbc = JdbcHelperTest.GetSellGirlJdbc();
//		
//
//
//		String sql = "  select * from [news_reply]";
//
//		PFSqlCreateTableCollection models =PFSqlCreateTableCollection.Init(dstJdbc);
//		//models.DbName = "wm_hive_db";
//		models.TableName = "news_reply";
//		// models.PrimaryKey = new String[] {"data_date","user_id"};
//		// models.ClusteredIndex = new String[] {"toYYYYMM(data_date)"} ;
//		//models.Order = new String[] { "data_date", "user_id" };
//		//models.Partition = new String[] { "data_date" };
//		// models.TableIndex = new String[] {"good_no"};
//
//		// ResultSet dr = srcExec.GetDataReader(sql);
//
//		//ISqlExecute srcExec = PFSqlExecute.Init(srcJdbc);
//		ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//		
//		 models=dstExec.GetCreateTableSql(srcJdbc, sql, models, null);
////		.ToSql();
////		ResultSet dr = srcExec.GetHugeDataReader(sql);
////
////		if (dr == null) {
////			return;
////		}
////
////		PFModelConfigCollection modelConfig = new PFModelConfigCollection();
////		try {
////			ResultSetMetaData md = dr.getMetaData();
////
////			for (int i = 0; i < md.getColumnCount(); i++) {
////				// String fieldName = md.getColumnName(i+1);
////				String fieldName = md.getColumnLabel(i + 1);
////				SqlCreateTableItem m = null;
////				if (modelConfig.containsKey(fieldName)) {
//////	           	 m=new SqlCreateTableItem(modelConfig.get(fieldName));
////					m = new SqlCreateTableItem(modelConfig.Get(fieldName));
////					m.FieldName = fieldName;// modelConfig里有可能是大小写不一样的,还是按dr的字段大小写来吧
////				} else {
////					m = new SqlCreateTableItem();
////					m.FieldName = fieldName;
////				}
////				if (m.FieldType == null) {
////					m.FieldType = PFDataHelper.GetTypeByString(md.getColumnTypeName(i + 1));
////				}
//////	            if (columnComment != null && columnComment.containsKey(m.FieldName))
//////	            {
//////	                m.FieldText = columnComment.get(m.FieldName);
//////	            }
////				// var updateItem = new SqlUpdateItem { Key = rdr.GetName(i), VType =
////				// rdr.GetFieldType(i) };
////				models.add(m);
////			}
////		} catch (Exception e) {
////
////		}
////		srcExec.CloseReader(dr);
////		srcExec.CloseConn();
//
//		PFSqlCommandString sqlStr = models.ToSql();
//		System.out.print(sqlStr);
//
//
//	}
//	public void testCreateTidbTable() throws Exception {
//		initPFHelper();
//
//		IPFJdbc srcJdbc = JdbcHelperTest.GetTidbSaleJdbc();
//		IPFJdbc dstJdbc = JdbcHelperTest.GetTidbSaleJdbc();
//		
//
//
//		String sql = "SELECT  \r\n" + 
//				"STR_TO_DATE('2021.10.01','%Y.%m.%d') as data_date,\r\n" + 
//				" a.hybh as user_id,\r\n" + 
//				" a.hysfzh as id_card,\r\n" + 
//				" a.sex as sex, \r\n" + 
//				" a.Age as age,\r\n" + 
//				" d.Inv_no as good_no,\r\n" + 
//				" sum(d.qty) as good_qty,\r\n" + 
//				" c.is_new_hy as new_user,\r\n" + 
//				" -- DATEDIFF(m,a.rhrq,GETDATE()) as regist_month_total,\r\n" + 
//				" c.accmon as regist_month,\r\n" + 
//				" b.Orderno as order_no\r\n" + 
//				"FROM monthly_order_2021 b\r\n" + 
//				"inner join monthly_orderdetail_2021 d on d.cmonth='2021.10' and d.Orderno=b.Orderno\r\n" + 
//				"inner join monthly_hyzl_2021 c on c.create_date='2021-10-01 00:00:00' and c.hybh=b.hybh\r\n" + 
//				"inner join hyzl a on b.hybh=a.hybh\r\n" + 
//				"where b.create_date='2021-10-01 00:00:00' -- b.cmonth='2021.10'\r\n" + 
//				"group by \r\n" + 
//				" a.hybh ,\r\n" + 
//				" a.hysfzh ,\r\n" + 
//				" -- a.hyxb as [sex], 无\r\n" + 
//				" -- 1 as age,\r\n" + 
//				" d.Inv_no ,\r\n" + 
//				" -- d.qty ,\r\n" + 
//				" c.is_new_hy,\r\n" + 
//				" -- DATEDIFF(m,a.rhrq,GETDATE()) as regist_month_total,\r\n" + 
//				" c.accmon ,\r\n" + 
//				" b.Orderno \r\n" + 
//				"limit 10";
//
//		PFSqlCreateTableCollection models =PFSqlCreateTableCollection.Init(dstJdbc);
//		models.TableName = "monthly_user_sale2";
//		// models.PrimaryKey = new String[] {"data_date","user_id"};
//		// models.ClusteredIndex = new String[] {"toYYYYMM(data_date)"} ;
//		//models.Order = new String[] { "data_date", "user_id" };
//		//models.Partition = new String[] { "data_date" };
//		// models.TableIndex = new String[] {"good_no"};
//
//		// ResultSet dr = srcExec.GetDataReader(sql);
//
//		//ISqlExecute srcExec = PFSqlExecute.Init(srcJdbc);
//		ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//		
//		 models=dstExec.GetCreateTableSql(srcJdbc, sql, models, null);
////		.ToSql();
////		ResultSet dr = srcExec.GetHugeDataReader(sql);
////
////		if (dr == null) {
////			return;
////		}
////
////		PFModelConfigCollection modelConfig = new PFModelConfigCollection();
////		try {
////			ResultSetMetaData md = dr.getMetaData();
////
////			for (int i = 0; i < md.getColumnCount(); i++) {
////				// String fieldName = md.getColumnName(i+1);
////				String fieldName = md.getColumnLabel(i + 1);
////				SqlCreateTableItem m = null;
////				if (modelConfig.containsKey(fieldName)) {
//////	           	 m=new SqlCreateTableItem(modelConfig.get(fieldName));
////					m = new SqlCreateTableItem(modelConfig.Get(fieldName));
////					m.FieldName = fieldName;// modelConfig里有可能是大小写不一样的,还是按dr的字段大小写来吧
////				} else {
////					m = new SqlCreateTableItem();
////					m.FieldName = fieldName;
////				}
////				if (m.FieldType == null) {
////					m.FieldType = PFDataHelper.GetTypeByString(md.getColumnTypeName(i + 1));
////				}
//////	            if (columnComment != null && columnComment.containsKey(m.FieldName))
//////	            {
//////	                m.FieldText = columnComment.get(m.FieldName);
//////	            }
////				// var updateItem = new SqlUpdateItem { Key = rdr.GetName(i), VType =
////				// rdr.GetFieldType(i) };
////				models.add(m);
////			}
////		} catch (Exception e) {
////
////		}
////		srcExec.CloseReader(dr);
////		srcExec.CloseConn();
//
//		PFSqlCommandString sqlStr = models.ToSql();
//		System.out.print(sqlStr);
//
//
//	}
//
//	public void testClickHouseDelete() throws Exception {
//		PFDataHelper.SetConfigMapper(new PFConfigTestMapper());
//
//		IPFJdbc srcJdbc = JdbcHelperTest.GetYJQueryJdbc();
//		// IPFJdbc dstJdbc=UncheckTest.GetClickHouseShopJdbc();
//		IPFJdbc dstJdbc = JdbcHelperTest.GetUserProfileJdbc();
//
//		srcJdbc.setUrl(srcJdbc.getUrl().replace("yjquery", "yjquery202102"));
//
//		String sql = "ALTER TABLE  user_profile_attr_all  ON CLUSTER default_cluster delete  where  `data_date`='2021-04-21 00:00:00' ";
//
//		ISqlExecute dstExec = PFSqlExecute.Init(dstJdbc);
//		Connection conn = dstExec.GetConn();
////	     
//		Statement stmt = conn.createStatement();
//
//		try {
//			// 方法1 报错
//			boolean b = stmt.execute(sql);
//
////	    	//方法2 ok
////				for(String i :sqlStr) {
////					stmt.execute(i);
////				}
//
////				//方法3 执行成功, 但没生效
////				for(String i :sqlStr) {
////					stmt.addBatch(i);					
////				}
////				int[] b=stmt.executeBatch();
//		} catch (Exception e) {
//			System.err.print(e);
//		}
//		String aa = "aa";
//		// stmt.setQueryTimeout(CommandTimeOut);
//
//	}
//
//	public void testMySqlVersion() throws Exception {
//
////	     String url = "jdbc:mysql://rm-wz90v9vru7d524b3hwo.mysql.rds.aliyuncs.com/mall_center_product?connectTimeout=20000";
//		// ok
//		String url = "jdbc:mysql://dzqv2n74qe5gzdn7ol10-rw4rm.rwlb.rds.aliyuncs.com/mall_center_product?useUnicode=true&characterEncoding=utf8&connectTimeout=20000&useSSL=false";
//		// String url =
//		// "jdbc:mysql://dzqv2n74qe5gzdn7ol10-rw4rm.rwlb.rds.aliyuncs.com/mall_center_product?useUnicode=true&characterEncoding=utf8&connectTimeout=20000&useSSL=false";
//		String user = "perfectmall";
//		String password = "lige@2020_lige%2020";
//		Connection conn = DriverManager.getConnection(url, user, password);
//		boolean b = false;
//		assertTrue(b == true);
//	}
//
//	public void testSysType() throws Exception {
//
//		assertTrue(SysType.Windows == PFDataHelper.GetSysType());
//	}
//	public void testException() {
//		initPFHelper();
////		try {
////			String aa="aa";
////			throw new Exception("aa");
////		}catch(Exception e) {
//////			//e.printStackTrace();
////			PFDataHelper.WriteError(new Throwable() , e);
////			
////
//////			Throwable trace = e.fillInStackTrace();
//////			if (trace == null) {
//////				DoWriteLog("PFDataHelper.WriteError()待优化,trace为null", "pfError");
//////			} else {
//////				trace.printStackTrace(printWriter);
//////				s = result.toString();
//////			}
////		}
////		try {
////			Thread.sleep(2000);
////		} catch (InterruptedException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		
//		try {
//			throw new SQLSyntaxErrorException("aaa");
//		} 
//		catch(SQLSyntaxErrorException e) {
////			e.printStackTrace();
//			System.out.println("111");
//		}catch (Exception e) {
//			// TODO Auto-generated catch block
//			//e.printStackTrace();
//			System.out.println("222");
//		}
//	}
//	public void testJdbc() throws Exception {
//		
//		//IPFJdbc dstJdbc=JdbcHelper.GetTidbSale();
//		initPFHelper();
//		IPFJdbc dstJdbc=JdbcHelperTest.GetTidbSaleJdbc();
//     	 ISqlExecute sqlExec = PFSqlExecute.Init(dstJdbc);
////     	sqlExec.HugeUpdate("update monthly_hyzl_2020 set is_new_hy_b=\r\n" + 
////     			"case   \r\n" + 
////     			"  when is_new_hy='true' then b'1'\r\n" + 
////     			"  when is_new_hy='1' then b'1' \r\n" + 
////     			"  else b'0'\r\n" + 
////     			"end\r\n" + 
////     			"where is_new_hy_b is null");
//     	 
//      	//sqlExec.HugeUpdate("update monthly_hyzl_2020 set is_new_hy=null where is_new_hy is not null");
//		Object aa=sqlExec.QuerySingleValue("select 1 as aa");
//		String bb="aa";
//		String cc=bb;
//	}
//	public void testSqlUrl() {
//	try {
//		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//		String url = "jdbc:sqlserver://192.168.0.29:1433;DatabaseName=bonus;user=sa;password=perfect";
//		Connection conn = DriverManager.getConnection(url);
//
//		System.out.println(conn.isClosed());
//		conn.close();
//		System.out.println(conn.isClosed());
//	} catch (Exception e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//}
//	public void testIntervalTask() {
//		int[] cnt=new int[] {0};
//		PFIntervalTask task=new PFIntervalTask("IntervalTask001",(t)->{
//			System.out.println(cnt[0]);
//			cnt[0]++;
//			return true;
//		},1); 
//		task.Start();
//		while(cnt[0]<10) {
//			try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
//	public void testApi() {
//		int[] cnt=new int[] {0};
//		PFDataHelper.HttpPost("https://uc.perfect99.com/api/esb/api/member/bank/batchDeleteSellerBank", "{\"cardNos\":[\"00001374\"]}");
//	}
//	public void testIDCard() {
//		List<Pair<String,String>> cards=new ArrayList<Pair<String,String>>();
//		cards.add(ImmutablePair.of("440620195702090710", "男"));
//		cards.add(ImmutablePair.of("440701196408200019", "男"));
//		cards.add(ImmutablePair.of("440702197002040917", "男"));
//		cards.add(ImmutablePair.of("440701192104280912", "男"));
//		cards.add(ImmutablePair.of("440701196001120017", "男"));
//		cards.add(ImmutablePair.of("422801197412190026", "女"));
//		cards.add(ImmutablePair.of("422801197001044024", "女"));
//		cards.add(ImmutablePair.of("440620195311290547", "女"));
//		cards.add(ImmutablePair.of("440620196511143685", "女"));
//		cards.add(ImmutablePair.of("44062019620418054X", "女"));
//		cards.add(ImmutablePair.of("530103195304032927", "女"));
//		for(int i=0;i<cards.size();i++) {
//			Pair<String,String> card=cards.get(i);
//			String sex=PFDataHelper.IDCardToSex(card.getKey());
//			assertTrue(sex.equals(card.getValue()));
//		}
//	}
	private <T1> void PrintObject(T1 o) {
	System.out.println(JSON.toJSONString(o));
}
//	private void eachDirectNode(DirectNode n1,PFAction<DirectNode,Object,Object> action,List<DirectNode> processedList) {
//		action.go(n1,null,null);
//		if(processedList==null) {
//			processedList=new ArrayList<DirectNode>();
//		}
//		processedList.add(n1);
//		if(n1.next!=null&&!n1.next.isEmpty()) {
//			for(DirectNode i:n1.next) {
//				if(!processedList.contains(i)) {
//					eachDirectNode(i,action,processedList);
//				}
//			}
//		}
//	}
//	private void checkDirectNode(DirectNode n1) {
//		eachDirectNode(n1,(a, b, c) -> {
////		a.finish = true;
////		a.setFinishedPercent(100);
////		System.out.print(a.getHashId());
//
//			PFAction<DirectNode, Integer, Object> ac = (d, e, f) -> {
//				d.finish = true;
//				d.setFinishedPercent(100);
//				System.out.print(a.getHashId());
//		
//			};
//		a.action = (g,h,i) -> {ac.go(g, null, null);return true;};
//
//	},null);
//
//		System.out.print("\r\n");
//		PrintObject(n1.getAllNodeFinishedPercent());
//		n1.GoSync(null);
//
//		System.out.print("\r\n");
//		PrintObject(n1.getAllNodeFinishedPercent());
//	}
//	public void testDirectNode() {
//	/**
//	 * 1 ->2->3 ->5 ->6 ->8 ->4 ->7->9
//	 * 142357968
//	 */
//	DirectNode n1 = new DirectNode("1");
//	DirectNode n2 = new DirectNode("2");
//	DirectNode n3 = new DirectNode("3");
//	DirectNode n4 = new DirectNode("4");
//	DirectNode n5 = new DirectNode("5");
//	DirectNode n6 = new DirectNode("6");
//	DirectNode n7 = new DirectNode("7");
//	DirectNode n8 = new DirectNode("8");
//	DirectNode n9 = new DirectNode("9");
////		n1.next=new ArrayList() {{add(n2);add(n3);}};
////		n2.next=new ArrayList() {{add(n2);add(n3);}};
//	n1.addNext(n2, n4);
//	n2.addNext(n3);
//	n3.addNext(n5);
//	n4.addNext(n5);
//	n5.addNext(n6, n7);
//	// n6.addNext(n8);
//	n7.addNext(n9);
//	// n9.addNext(n8);
//	n8.addPrev(n6, n9);
//	
//	checkDirectNode(n1);
//
////	PFAction<DirectNode, Integer, Object> ac = (a, b, c) -> {
////		a.finish = true;
////		a.setFinishedPercent(100);
////		System.out.print(a.getHashId());
////
////	};
////	n1.action = a -> ac.go(a, null, null);
////	n2.action = a -> ac.go(a, null, null);
////	n3.action = a -> ac.go(a, null, null);
////	n4.action = a -> ac.go(a, null, null);
////	n5.action = a -> ac.go(a, null, null);
////	n6.action = a -> ac.go(a, null, null);
////	n7.action = a -> ac.go(a, null, null);
////	n8.action = a -> ac.go(a, null, null);
////	n9.action = a -> ac.go(a, null, null);
////
////	System.out.print("\r\n");
////	PrintObject(n1.getAllNodeFinishedPercent());
////	n1.GoSync(null);
////
////	System.out.print("\r\n");
////	PrintObject(n1.getAllNodeFinishedPercent());
//////	try {
//////		while (!n8.finish) {
//////			Thread.sleep(1000);
//////			System.out.print("\r\n");
//////			PrintObject(n1.getAllNodeFinishedPercent());
//////		}
//////	} catch (InterruptedException e) {
//////		// TODO Auto-generated catch block
//////		e.printStackTrace();
//////	}
//////	System.out.print("\r\n");
//////	PrintObject(n1.getAllNodeFinishedPercent());
//
//}
//	public void testDirectNode02() {
//	/**
//	 * 1 ->2->3 ->5 ->6 ->8 ->4 ->7->9
//	 * 142357968
//	 */
//		DirectNode n0 = new DirectNode("0");
//	DirectNode n1 = new DirectNode("1");
//	DirectNode n2 = new DirectNode("2");
//	DirectNode n3 = new DirectNode("3");
//	DirectNode n4 = new DirectNode("4");
//	DirectNode n5 = new DirectNode("5");
//	DirectNode n6 = new DirectNode("6");
//	DirectNode n7 = new DirectNode("7");
//	DirectNode n8 = new DirectNode("8");
//	DirectNode n99 = new DirectNode("99");
//
//	n0.addNext(n1,n2,n3,n5);
//	n4.addPrev(n2,n3);
//	n6.addPrev(n3,n5);//n6还用到prod表,但prod表好像不是每天更新的,prod表可能需要改为每天更新--benjamin todo
//	n7.addPrev(n2);
//	n8.addPrev(n1,n2,n3,n5);
//	n99.addPrev(n1,n4,n5,n6,n7,n8);
//	
//	checkDirectNode(n0);
//}
//
//	public void testPFIntervalExactTask()  {
//		IPFTask _intervalRedoFailedTask=new PFIntervalExactTask("PFIntervalExactTask001",
//				(cDay, lastBackupTime, task)->{
////			//List<String> tasks=
////	 	 	   if(t1.isFailed()) {
////	  			  t1.RedoFailed();
////	  		   }
//	 	 	   this.PrintObject(cDay);
//			return true;
//		},2,PFDate.Now()//.AddMinutes(0)
//				,true,null
//				); 
//		_intervalRedoFailedTask.Start();
//		try {
//			while(true) {
//					Thread.sleep(2000);
//			}
//			//Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	public void testPFIntervalExactTask2()  {
//		UncheckBatchAlterSqlTable.initPFHelper();
//		IPFTask _intervalRedoFailedTask=new PFIntervalExactTask2("PFIntervalExactTask001",
//				(cDay, lastBackupTime, task)->{
////			//List<String> tasks=
////	 	 	   if(t1.isFailed()) {
////	  			  t1.RedoFailed();
////	  		   }
//	 	 	   this.PrintObject(cDay);
//			return true;
//		},//.AddMinutes(0)
//				TimePoint.every,0,0,
//				3,
//				true,null
//				); 
//		_intervalRedoFailedTask.Start();
//		try {
//			while(true) {
//					Thread.sleep(2000);
//			}
//			//Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	public void testDayTask2()  {
//		initPFHelper();
//		boolean[] b=new boolean[] {false,false,false};
//		PFDate now=PFDate.Now().AddMinutes(-1);
//		PFDate now1=now.AddMinutes(1);
//		int housr=now.GetHour();
////		int min=now.GetMinute();
//		int min=19;
//		int[] cnt=new int[] {1};
//		PFDayTask2 t1=new PFDayTask2("DayHyDataTest", (cDay, lastBackupTime, task) -> {
////			if(cnt[0]>0) {
////				this.PrintObject("执行失败:"+String.valueOf(cnt[0]));
////				cnt[0]--;
////				return false;
////			}else {
////				this.PrintObject("执行成功:"+String.valueOf(cnt[0]));
////				return true;
////			}
//			this.PrintObject("执行成功:"+cDay);
//			return true;
//		}, housr, min,// 每月11日3时执行
//		false,
//		null
//		); 
//		
//		t1.Start();
//
//		try {
//			while(true) {
//					Thread.sleep(2000);
//			}
//			//Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	public void testTimeTaskRetry()  {
//		initPFHelper();
//		boolean[] b=new boolean[] {false,false,false};
//		PFDate now=PFDate.Now().AddMinutes(-1);
//		PFDate now1=now.AddMinutes(1);
//		int housr=now.GetHour();
//		int min=now.GetMinute();
//		int[] cnt=new int[] {1};
//		PFDayTask t1=new PFDayTask("DayHyDataTest", (cDay, lastBackupTime, task) -> {
//			if(cnt[0]>0) {
//				this.PrintObject("执行失败:"+String.valueOf(cnt[0]));
//				cnt[0]--;
//				return false;
//			}else {
//				this.PrintObject("执行成功:"+String.valueOf(cnt[0]));
//				return true;
//			}
//		}, housr, min,// 每月11日3时执行
//		false,
//		null
//		); 
//		
//		t1.Start();
//		
//		IPFTask _intervalRedoFailedTask=new PFIntervalTask("IntervalTask001",(t)->{
//			//List<String> tasks=
//	 	 	   if(t1.isFailed()) {
//	  			  t1.RedoFailed();
//	  		   }
//			return true;
//		},1); 
//		_intervalRedoFailedTask.Start();
//		try {
//			while(true) {
//					Thread.sleep(2000);
//			}
//			//Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	public void testTimeTask2Retry()  {
//		initPFHelper();
//		boolean[] b=new boolean[] {false,false,false};
//		PFDate now=PFDate.Now().AddMinutes(-1);
//		PFDate now1=now.AddMinutes(1);
//		int housr=now.GetHour();
//		int min=now.GetMinute();
//		int[] cnt=new int[] {1};
//		PFDayTask2 t1=new PFDayTask2("DayHyDataTest", (cDay, lastBackupTime, task) -> {
//			if(cnt[0]>0) {
//				this.PrintObject("执行失败:"+String.valueOf(cnt[0]));
//				cnt[0]--;
//				return false;
//			}else {
//				this.PrintObject("执行成功:"+String.valueOf(cnt[0]));
//				return true;
//			}
//		}, housr, min,// 每月11日3时执行
//		false,
//		null
//		); 
//		
//		t1.Start();
//		
//		IPFTask _intervalRedoFailedTask=new PFIntervalTask("IntervalTask001",(t)->{
//			//List<String> tasks=
//	 	 	   if(t1.isFailed()) {
//	  			  t1.RedoFailed();
//	  		   }
//			return true;
//		},1); 
//		_intervalRedoFailedTask.Start();
//		try {
//			while(true) {
//					Thread.sleep(2000);
//			}
//			//Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 
//	 * 测试通过
//	 */
//	public void testTransferTaskBase() {		
////		IPFJdbc srcJdbc = JdbcHelperTest.GetTidbSaleJdbc();
////		IPFJdbc dstJdbc = JdbcHelperTest.GetTidbSaleJdbc();
////		//IPFJdbc dstJdbc = JdbcHelperTest.GetLiGeShopJdbc();
//
//		IPFJdbc srcJdbc = JdbcHelperTest.GetSellGirlJdbc2();
//		IPFJdbc dstJdbc = JdbcHelperTest.GetSellGirlJdbc2();
//
//		String tableName="test_tb_01";
//		String sql="select 2 as col1";
//		
//		ISqlExecute srcExec =null;
//		ISqlExecute dstExec =null;
//		try {
//			srcExec = PFSqlExecute.Init(srcJdbc);
//			dstExec = PFSqlExecute.Init(dstJdbc);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return;
//		}
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
//
//		model.TableName=tableName;
//		
//		ResultSet dr=srcExec.GetDataReader(sql);
//		
//		//自动建表
////		PFSqlCommandString createSql=dstExec.GetCreateTableSql(srcJdbc, sql, 
////				model
////				, null).ToSql();
//		//boolean b1=dstExec.ExecuteSql(createSql);
//		
////		boolean b2 =dstExec.HugeBulkReader(null,dr , tableName,
////
////	null, // transferItem == null ? null :
////						// transferItem.BeforeInsertAction,
////	(already) -> {
////////             total = already;
//////		arr[0] = already;
//////		if (alreadyAction != null) {
//////			alreadyAction.accept(already);
//////		}
//////		// loadingfrm.SetJD("当前：正在导入reader", "当前进度："+dstTableName+"(" +
//////		// PFDataHelper.ScientificNotation(already) + "/未知)");
////	}, null);
//
//		boolean b2 =dstExec.HugeInsertReader(null,dr , tableName,
//	null, 
//	(already) -> {
//
//	}, null);
//		
//		assertTrue(b2 == true);
//	}
//	
//
//	public void testTransferTask() {
//		PFSqlTransferItem task=null;
////	PFSqlTransferItem task=new TransferTaskTest001().get();
//////	task.setCmonth();
////	//task.updateByCmonth(PFDate.Now().AddMonths(-1).toCmonth());
////	task.updateByCmonth("2021.09");
//	
//	
//	task=new TransferTaskTest002().get();
//	
//	//----------------------------------------
//	ISqlExecute dstExec;
//	try {
//		dstExec = PFSqlExecute.Init(task.DstJdbc);
//		dstExec.SetCommandTimeOut(PFSqlCommandTimeoutSecond.Huge());
//	} catch (Exception e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		return;
//	}
//	SGRef<String> ms=new SGRef<String>();
//	//dstExec.CreateTable(task, ms);
//	Boolean b = dstExec.TransferTable(task, null, null);
//	
//	}
//	public void testUpdateYearOnYearField() {
//	
//	PFSqlTransferItem task=new TransferTaskTest001().get();
//////	task.setCmonth();
////	//task.updateByCmonth(PFDate.Now().AddMonths(-1).toCmonth());
//	//task.updateByCmonth("2021.09");
//	task.updateByCmonth(new PFDate(2021,9,1,1,1,1));
//	ISqlExecute dstExec;
//	try {
//		dstExec = PFSqlExecute.Init(task.DstJdbc);
//		dstExec.SetCommandTimeOut(PFSqlCommandTimeoutSecond.Huge());
//	} catch (Exception e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		return;
//	}
//	((PFTidbSqlExecute)dstExec).UpdateYearOnYearField(
//			task.DstTableName,
//			task.getCmonth(),
//			task.DstTablePrimaryKeys,
//  new String[] { "new_hy_qty", "new_buyer_qty", "valid_hy_qty", "buyer_qty", "valid_hy_hpos_above_5_qty", "valid_hy_hpos_0_qty" }
//  );
//	String aa="aa";
//	}
//	public void testPossibilityCombination() {
//		String[] dice=new String[] {"a","b"};
//		List<List<String>> r=PFDataHelper.GetPossibilityCombination(Arrays.asList(dice));
//		for(List<String> i :r) {
//			this.PrintObject(String.join("", i));
//		}
//
//		this.PrintObject("---------------");
//		dice=new String[] {"a","b","c"};
//		//dice=new String[] {"a","b","c","d","e"};
//		r=PFDataHelper.GetPossibilityCombination(Arrays.asList(dice));
//		for(List<String> i :r) {
//			this.PrintObject(String.join("", i));
//		}
//		this.PrintObject("---------------");
//		dice=new String[] {"a","b","c","d","e"};
//		r=PFDataHelper.GetPossibilityCombination(Arrays.asList(dice));
//		for(List<String> i :r) {
//			this.PrintObject(String.join("", i));
//		}
//	}
//	public void testEnglishWord() {
//
//		ISqlExecute dstExec;
//		try {
//			dstExec = PFSqlExecute.Init(JdbcHelperTest.GetSellGirlJdbc());
//			dstExec.SetCommandTimeOut(PFSqlCommandTimeoutSecond.Huge());
//			List<SellGirlProduct> list=dstExec.QueryList(SellGirlProduct.class, "SELECT   \r\n" + 
//					"      [ProductCode] as productCode \r\n" + 
//					"      ,[ProductName] as productName \r\n" + 
//					"  FROM [SellGirl.Business].[dbo].[product_product]\r\n" + 
//					"  where TypeId='C5546FAC-D067-4683-8DFE-3AB81250FC91'");
//			
//			String[] dice0=new String[] {"s","r","d","f","h","u"};
//			String[] dice1=new String[] {"p","g","m","t","n","b"};
//			String[] dice2=new String[] {"e","y","o","k","l","s"};
//			String[] dice3=new String[] {"e","w","a","h","s","i"};
//			List<String> combinationList =new ArrayList<String>();
//			for(String i : dice0) {
//				for(String j : dice1) {
//					for(String k : dice2) {
//						for(String l : dice3) {
//							List<List<String>> combination= PFDataHelper.GetPossibilityCombination(Arrays.asList(new String[] {i,j,k,l}));
//							for(List<String> m: combination) {
//								String word=String.join("", m);
//								combinationList.add(word);
//							}
//						}
//					}
//					
//				}
//			}
//			List<SellGirlProduct> maybeWords=PFDataHelper.ListWhere(list, a->PFDataHelper.ListAny(combinationList, b->b.equals(a.getProductCode())));
////			PFDataHelper.ListSelect(
////					
////					, a->PFDataHelper.);
//			for(SellGirlProduct i :maybeWords) {
//				this.PrintObject(i.getProductCode()+"___"+i.getProductName());
//			}
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return;
//		}
//	}
//	private static Dimension pcSize=new Dimension(2160,4680);//p30pro 1080, 2340 
//	private static String sashaImgPath = "D:\\\\picture\\ps\\princessSasha_lostVirginity_byBenjamin.jpg";
//	private static String sashaImgPath2 = "D:\\\\picture\\ps\\princessSasha_lostVirginity_byBenjamin2.jpg";
//	private static String sashaBeingTiedToTheCrossImgPath = "D:\\\\picture\\ps\\princessSashaBeingTiedToTheCross\\princessSashaBeingTiedToTheCross_byBenjamin.jpg";
//	public void testBackgroundImg() {
//try {
//		int backWidth = 3840; // 1920;
//		int backHeight = 2160;// 1080;
//		
//		File infile = new File(sashaImgPath);
//		Image image = ImageIO.read(infile);
////		int backWidth = w;
////		int backHeight = h;
//		
//		String tmpImgPath = PFDataHelper.backgroundImg(new Dimension(backWidth, backHeight), image,
//				null,
//				new PFLine(new PFPoint(0,0),new PFPoint(100,100)).IsPercent(), Color.RED,
//				false);
//		File file = new File(tmpImgPath);
//		FileInputStream inputStream = new FileInputStream(file);
//		byte[] bytes = new byte[inputStream.available()];
//		inputStream.read(bytes, 0, inputStream.available());
//		inputStream.close();
//		//file.delete();
//	} catch (Exception e) {
//	}
//	return ;
//	}
//	public void testBackgroundImgInBuffer() {
//try {
//		int backWidth = 3840; // 1920;
//		int backHeight = 2160;// 1080;
//		
//		File infile = new File(sashaImgPath);
//		Image image = ImageIO.read(infile);
//		File infile2 = new File(sashaBeingTiedToTheCrossImgPath);
//		Image image2 = ImageIO.read(infile2);
//		File infile3 = new File(sashaImgPath2);
//		Image image3 = ImageIO.read(infile);
////		int backWidth = w;
////		int backHeight = h;
//		//BufferedImage paintBi = new BufferedImage(wWidth * pixelTatio, wHeight * pixelTatio, 1);
////		Canvas canvas = new Canvas();
////		Graphics ctx1 = canvas.getGraphics();
//
////		Canvas canvas = null;
////		Graphics ctx1 = null;
//		SGRef<Canvas> canvasRef = new SGRef<Canvas>(null);
//		SGRef<Graphics> ctx1Ref = new SGRef<Graphics>(null);
//		BufferedImage paintBi =null;
//		 paintBi = PFDataHelper.backgroundImgInBuffer(
//				//canvas,ctx1,
//				canvasRef,ctx1Ref,
//				null,new Dimension(backWidth, backHeight), image,
//				null,
//				//new PFLine(new PFPoint(0,0),new PFPoint(3840,2160)),
//				//new PFLine(new PFPoint(0,0),new PFPoint(3840,1000)),
//				new PFLine(new PFPoint(0,0),new PFPoint(1790,2160)),
//				new PFLine(new PFPoint(0,0),new PFPoint(100,100)).IsPercent(), Color.RED,
//				false);
////		 paintBi = PFDataHelper.backgroundImgInBuffer(
////					//canvas,ctx1,
////					canvasRef,ctx1Ref,
////					null,new Dimension(backWidth, backHeight), image3,
////					null,
////					//new PFLine(new PFPoint(0,0),new PFPoint(3840,2160)),
////					//new PFLine(new PFPoint(0,0),new PFPoint(3840,1000)),
////					new PFLine(new PFPoint(0,0),new PFPoint(2000,2160)),
////					new PFLine(new PFPoint(0,0),new PFPoint(100,100)).IsPercent(), Color.RED,
////					false);
//
//		 paintBi = PFDataHelper.backgroundImgInBuffer(
////				 canvas,ctx1,
//					canvasRef,ctx1Ref,
//				 paintBi,
//				 new Dimension(backWidth, backHeight), image2,
//				null,
//				//new PFLine(new PFPoint(0,0),new PFPoint(3840,2160)),
//				//new PFLine(new PFPoint(0,0),new PFPoint(3840,1000)),
//				new PFLine(new PFPoint(1790,0),new PFPoint(1790+1680,2160)),
//				new PFLine(new PFPoint(0,0),new PFPoint(100,100)).IsPercent(), Color.RED,
//				false);
//
//		 //canvas=canvasRef.GetValue();
//		canvasRef.GetValue().printAll(ctx1Ref.GetValue());
//
//		String tmpImgPath = Paths
//				.get(PFDataHelper.GetBaseDirectory(), new String[] { "tmpfile\\tmpimg_" + PFDataHelper.GetNewUniqueHashId() + ".jpg" })
//				.toString();
//		PFDirectory.EnsureExists(tmpImgPath);
//		File file2 = new File(tmpImgPath);
//		ImageIO.write(paintBi, "jpg", file2);
//		//ctx1.dispose();
//		//file.delete();
//	} catch (Exception e) {
//		e.printStackTrace();
//	}
//	return ;
//	}
//	public void testDateRange() {
//		PFDate now=PFDate.Now().GetHourStart();
//		PFDate lastMinute=now;
//		DateRange monRange=new DateRange(lastMinute.GetYear());
//		List<PFDate> perMonth = monRange.GetPerMonthStart();
//		for(PFDate i : perMonth) {
//			this.PrintObject(i.toYM());
//		}
//	}
//	public void testReplace() {
//		String s="{cmonth}aaa{cmonth}";
//		s=s.replace("{cmonth}","ccc");
//		this.PrintObject(s);
//	}
//	public void testUserSetting() {
//		PFKeyValueCollection<String> userSetting=PFDataHelper.readUserSetting("userSetting01.txt");
//		this.PrintObject(userSetting);
//		PFDataHelper.writeUserSetting("userSetting01.txt", a->{a.Set("needValidateLiGeDayOrderApi","2");
//		a.Set("needValidate02","3");
//		});
//		 userSetting=PFDataHelper.readUserSetting("userSetting01.txt");
//		this.PrintObject(userSetting);
//		String aa="aa";
//	}
}
