package org.sellgirlPayHelperNotSpring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sellgirl.sgJavaHelper.DirectNode;
import com.sellgirl.sgJavaHelper.IPFSqlFieldTypeConverter;
import com.sellgirl.sgJavaHelper.SGAction;
import com.sellgirl.sgJavaHelper.SGDataTable;
import com.sellgirl.sgJavaHelper.PFEnumClass;
import com.sellgirl.sgJavaHelper.PFHiveSqlCreateTableCollection;
import com.sellgirl.sgJavaHelper.PFKeyValueCollection;
import com.sellgirl.sgJavaHelper.PFLine;
import com.sellgirl.sgJavaHelper.PFModelConfig;
import com.sellgirl.sgJavaHelper.PFModelConfigCollection;
import com.sellgirl.sgJavaHelper.PFPoint;
import com.sellgirl.sgJavaHelper.SGRequestResult;
import com.sellgirl.sgJavaHelper.SGSpeedCounter;
import com.sellgirl.sgJavaHelper.SGSqlCommandString;
import com.sellgirl.sgJavaHelper.PFSqlCommandTimeoutSecond;
import com.sellgirl.sgJavaHelper.SGSqlFieldInfo;
import com.sellgirl.sgJavaHelper.PFSqlFieldType;
import com.sellgirl.sgJavaHelper.SGSqlFieldTypeEnum;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGDateRange;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.SuccessApiResult;
import com.sellgirl.sgJavaHelper.config.PFAppConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.config.SGDataHelper.LocalDataType;
import com.sellgirl.sgJavaHelper.file.SGDirectory;
import com.sellgirl.sgJavaHelper.model.FileSizeUnitType;
import com.sellgirl.sgJavaHelper.model.SysType;
import com.sellgirl.sgJavaHelper.model.UserOrg;
import com.sellgirl.sgJavaHelper.model.UserTypeClass;
import com.sellgirl.sgJavaHelper.sgEnum.IPFEnum;
import com.sellgirl.sgJavaHelper.sgEnum.SGFlagEnum;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlCreateTableCollection;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlInsertCollection;
import com.sellgirl.sgJavaHelper.sql.SGSqlTransferItem;
import com.sellgirl.sgJavaHelper.sql.PFTidbSqlExecute;
import com.sellgirl.sgJavaHelper.sql.SqlCreateTableItem;
import com.sellgirl.sgJavaHelper.sql.SqlUpdateItem;
import com.sellgirl.sgJavaHelper.task.IPFTask;
import com.sellgirl.sgJavaHelper.task.PFDayTask;
import com.sellgirl.sgJavaHelper.task.PFDayTask2;
import com.sellgirl.sgJavaHelper.task.PFIntervalExactTask;
import com.sellgirl.sgJavaHelper.task.PFIntervalExactTask2;
import com.sellgirl.sgJavaHelper.task.PFIntervalTask;
import com.sellgirl.sgJavaHelper.task.PFMonthTask;
import com.sellgirl.sgJavaHelper.task.PFMonthTask2;
import com.sellgirl.sgJavaHelper.task.TaskFollower;
import com.sellgirl.sgJavaHelper.task.PFTimeTaskBase2.TimePoint;
import com.sellgirl.sgJavaHelper.time.SGTimeSpan;

import junit.framework.TestCase;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.sellgirlPayHelperNotSpring.model.ByteUtil;
import org.sellgirlPayHelperNotSpring.model.DataModelInfo;
import org.sellgirlPayHelperNotSpring.model.DefaultListDataSource;
import org.sellgirlPayHelperNotSpring.model.DefaultListDataSource2;
import org.sellgirlPayHelperNotSpring.model.HealthOrdersApiResult;
import org.sellgirlPayHelperNotSpring.model.JdbcHelperTest;
import org.sellgirlPayHelperNotSpring.model.PFConfigTestMapper;
import org.sellgirlPayHelperNotSpring.model.SellGirlProduct;
import org.sellgirlPayHelperNotSpring.model.TaskTest001;
import org.sellgirlPayHelperNotSpring.model.TaskTest002;
import org.sellgirlPayHelperNotSpring.model.TestEnum001;
import org.sellgirlPayHelperNotSpring.model.TestSerialModel;
import org.sellgirlPayHelperNotSpring.model.TestSerialModel2;
import org.sellgirlPayHelperNotSpring.model.TestSerialModel3;
import org.sellgirlPayHelperNotSpring.model.TestSerialModel4;
import org.sellgirlPayHelperNotSpring.model.TestSerialModel5;
import org.sellgirlPayHelperNotSpring.model.TransferTaskTest001;
import org.sellgirlPayHelperNotSpring.model.TransferTaskTest002;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * 不要随便更改此类名,以防打包时执行了此类
 * 
 * @author Administrator
 *
 */
@SuppressWarnings(value = { "unused", "rawtypes", "serial", "deprecation" })
public class UncheckTimeTask001 extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(UncheckTimeTask001.class);

	public static void initPFHelper() {
		SGDataHelper.SetConfigMapper(new PFConfigTestMapper());
		new SGDataHelper(new PFAppConfig());
	}

	public void testMetaDataReadOnly() throws Exception {
		initPFHelper();
		//IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTestJdbc();
		//IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest2Jdbc(); //ok
		//IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest3Jdbc();  //tcp 报错 Port 9000 is for clickhouse-client program
		//IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest4Jdbc();//ok
		//IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest5Jdbc();//ok
		ISGJdbc dstJdbc = JdbcHelperTest.GetDayJdbc();
		try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
//            int cnt=PFDataHelper.ObjectToInt0( myResource.QuerySingleValue("select count(*) from current_update_date_distributed"));
//            System.out.println(cnt);

			List<String> keys =new ArrayList<>();
			keys=myResource.GetTableFields("t_orders_recent").stream().map(a->a.getFieldName()).collect(Collectors.toList());
			ResultSetMetaData dstMd = myResource.GetMetaData("t_orders_recent", keys);
			if(null==dstMd){
				//throw new Exception("dstMd为空");
				throw  myResource.GetErrorLine();
			}
			for (int i = 0; i < dstMd.getColumnCount(); i++) {
				int mdIdx = i + 1;
				String colName = dstMd.getColumnLabel(mdIdx);
				System.out.println(colName+"_isReadOnly:"+dstMd.isReadOnly(mdIdx)+"_isAutoIncrement:"+dstMd.isAutoIncrement(mdIdx)
						+"_isWritable:"+dstMd.isWritable(mdIdx));
			}
			//System.out.println("metaData正常");
		} catch (Exception e) {
			throw e;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}

		//com.google.common.collect.MapMaker
	}
	public void testMetaDataReadOnly2() throws Exception {
		initPFHelper();
		//IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTestJdbc();
		//IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest2Jdbc(); //ok
		//IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest3Jdbc();  //tcp 报错 Port 9000 is for clickhouse-client program
		//IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest4Jdbc();//ok
		ISGJdbc dstJdbc = JdbcHelperTest.GetDayJdbc();
		try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
//            int cnt=PFDataHelper.ObjectToInt0( myResource.QuerySingleValue("select count(*) from current_update_date_distributed"));
//            System.out.println(cnt);

//            List<String> keys =new ArrayList<>();
//            keys=myResource.GetTableFields("test_tb_04_all").stream().map(a->a.getFieldName()).collect(Collectors.toList());
//            ResultSetMetaData dstMd = myResource.GetMetaData("test_tb_04_all", keys);
			PreparedStatement pstmt = myResource.GetConn().prepareStatement("insert into test1(c1) values(?)");
			ResultSetMetaData dstMd=pstmt.getMetaData();
			if(null==dstMd){
				//throw new Exception("dstMd为空");
				throw  myResource.GetErrorLine();
			}
			for (int i = 0; i < dstMd.getColumnCount(); i++) {
				int mdIdx = i + 1;
				String colName = dstMd.getColumnLabel(mdIdx);
				System.out.println(colName+"_isReadOnly:"+dstMd.isReadOnly(mdIdx)+"_isAutoIncrement:"+dstMd.isAutoIncrement(mdIdx)
						+"_isWritable:"+dstMd.isWritable(mdIdx));
			}
			//System.out.println("metaData正常");
		} catch (Exception e) {
			throw e;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}

		//com.google.common.collect.MapMaker
	}
//	public void testMetaDataReadOnly3() throws Exception {
//		initPFHelper();
//		//IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTestJdbc();
//		//IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest2Jdbc(); //ok
//		//IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest3Jdbc();  //tcp 报错 Port 9000 is for clickhouse-client program
//		//IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest4Jdbc();//ok
//		IPFJdbc dstJdbc = JdbcHelperTest.GetDayJdbc();
//		try (ISqlExecute myResource = PFSqlExecute.Init(dstJdbc)) {
////            int cnt=PFDataHelper.ObjectToInt0( myResource.QuerySingleValue("select count(*) from current_update_date_distributed"));
////            System.out.println(cnt);
//
////            List<String> keys =new ArrayList<>();
////            keys=myResource.GetTableFields("test_tb_04_all").stream().map(a->a.getFieldName()).collect(Collectors.toList());
////            ResultSetMetaData dstMd = myResource.GetMetaData("test_tb_04_all", keys);
//			PreparedStatement pstmt = myResource.GetConn().prepareStatement("insert into test1(c1) values(?)");
//			//ResultSetMetaData dstMd=pstmt.getMetaData();
//			ParameterMetaData dstMd=pstmt.getParameterMetaData();
//			if(null==dstMd){
//				//throw new Exception("dstMd为空");
//				throw  myResource.GetErrorLine();
//			}
//			for (int i = 0; i < 1; i++) {
//				int mdIdx = i + 1;
//				ParameterMetaData.parameterModeIn
//				String colName = dstMd.getParameterMode();
//				System.out.println(colName+"_isReadOnly:"+dstMd.isReadOnly(mdIdx)+"_isAutoIncrement:"+dstMd.isAutoIncrement(mdIdx)
//						+"_isWritable:"+dstMd.isWritable(mdIdx));
//			}
//			//System.out.println("metaData正常");
//		} catch (Exception e) {
//			throw e;
//		} catch (Throwable e) {
//			throw new RuntimeException(e);
//		}
//	}

	public void testMetaDataReadOnly4() throws Exception {
		initPFHelper();
		//IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTestJdbc();
		//IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest2Jdbc(); //ok
		//IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest3Jdbc();  //tcp 报错 Port 9000 is for clickhouse-client program
		//IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest4Jdbc();//ok
		ISGJdbc dstJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();
		try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
			DatabaseMetaData m_DBMetaData = myResource.GetConn().getMetaData();
			System.out.println("m_DBMetaData.isReadOnly()");
			System.out.println(m_DBMetaData.isReadOnly());
			String columnName;

			String columnType;

			ResultSet colRet = m_DBMetaData.getColumns(null,"%", "test_tb_03","%");

			while(colRet.next()) {

				columnName = colRet.getString("COLUMN_NAME");

				columnType = colRet.getString("TYPE_NAME");

				int datasize = colRet.getInt("COLUMN_SIZE");

				int digits = colRet.getInt("DECIMAL_DIGITS");

				int nullable = colRet.getInt("NULLABLE");
				//boolean readOnly = colRet.getBoolean("IS_READONLY");

				System.out.println(columnName+" "+columnType+" "+datasize+" "+digits+" "+

						nullable//+" "+readOnly
				);

			}

			//System.out.println("metaData正常");
		} catch (Exception e) {
			throw e;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public void testMetaData() throws SQLException {
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
		SGSpeedCounter speed = new SGSpeedCounter(SGDate.Now());
		now = SGDate.Now();
		for (Calendar d : dl) {
			SGDataHelper.ConvertObjectToSqlTypeByPFType(d, SGSqlFieldTypeEnum.DateTime, java.sql.Types.DATE);
		}
		this.PrintObject(speed.getSpeed(m, SGDate.Now()) + " NORMAL");

		speed = new SGSpeedCounter(SGDate.Now());
		now = SGDate.Now();
		for (Calendar d : dl) {
			if (c2 == null) {
				c2 = SGDataHelper.GetObjectToSqlTypeByPFTypeConverter(d, null, java.sql.Types.DATE, "",null
                );
			}
			c2.convert(d);
		}
		this.PrintObject(speed.getSpeed(m, SGDate.Now()) + " CONVERTER");

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
		speed = new SGSpeedCounter(SGDate.Now());
		for (long d : longDl) {
			Object tmpV = SGDataHelper.ConvertObjectToSqlTypeByPFType(d, SGSqlFieldTypeEnum.Long, java.sql.Types.DATE);
		}
		// PrintSpeed(now,m);//耗时:1153毫秒(0时0分1秒),已插入数:2000000,速度1734605条/秒
		this.PrintObject(speed.getSpeed(m, SGDate.Now()) + " NORMAL");

		speed = new SGSpeedCounter(SGDate.Now());
		c2 = null;
		for (long d : longDl) {
			if (c2 == null) {
				c2 = SGDataHelper.GetObjectToSqlTypeByPFTypeConverter(d, null, java.sql.Types.DATE, "",null
                );
			}
			Object tmpV = c2.convert(d);
		}
		// PrintSpeed(now,m);//耗时:487毫秒(0时0分0秒),已插入数:2000000,速度4106776条/秒
		this.PrintObject(speed.getSpeed(m, SGDate.Now()) + " CONVERTER");

		this.PrintObject("long -> NUMERIC");
		speed = new SGSpeedCounter(SGDate.Now());
		// now=PFDate.Now();
		for (long d : longDl) {
			Object tmpV = SGDataHelper.ConvertObjectToSqlTypeByPFType(d, SGSqlFieldTypeEnum.Long,
					java.sql.Types.NUMERIC);
		}
		// PrintSpeed(now,m);//耗时:59毫秒(0时0分0秒),已插入数:2000000,速度33898305条/秒
		this.PrintObject(speed.getSpeed(m, SGDate.Now()) + " NORMAL");

		// now =PFDate.Now();
		speed = new SGSpeedCounter(SGDate.Now());
		c2 = null;
		for (long d : longDl) {
			if (c2 == null) {
				c2 = SGDataHelper.GetObjectToSqlTypeByPFTypeConverter(d, null, java.sql.Types.NUMERIC, "",null
                );
			}
			Object tmpV = c2.convert(d);
		}
		// PrintSpeed(now,m);//耗时:73毫秒(0时0分0秒),已插入数:2000000,速度27397260条/秒
		this.PrintObject(speed.getSpeed(m, SGDate.Now()) + " CONVERTER");

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

//	public void testPFIntervalExactTask() {
//		IPFTask _intervalRedoFailedTask = new PFIntervalExactTask("PFIntervalExactTask001",
//				(cDay, lastBackupTime, task) -> {
////			//List<String> tasks=
////	 	 	   if(t1.isFailed()) {
////	  			  t1.RedoFailed();
////	  		   }
//					this.PrintObject(cDay);
//					return true;
//				}, 1, PFDate.Now()// .AddMinutes(0)
//				, true, null);
//		_intervalRedoFailedTask.Start();
//		try {
//			while (true) {
//				Thread.sleep(2000);
//			}
//			// Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	/**
	 * 测试定时间隔是否正确.
	 * 测试通过.
	 * @throws Exception
	 */
	public void testPFIntervalExactTask2() throws Exception {
		UncheckTimeTask001.initPFHelper();
		int[] checkCount=new int[]{3};
		boolean[] hasError=new boolean[]{false};
		SGDate lastTime=SGDate.Now();
		Double[] lastTimestamp=new Double[]{ null };
//		System.out.println(lastTime);
//		System.out.println(lastTime.toTimestamp());
		double intervalMinute=1;
		IPFTask _intervalRedoFailedTask = new PFIntervalExactTask2("PFIntervalExactTask001",
				(cDay, lastBackupTime, task) -> {
//			//List<String> tasks=
//	 	 	   if(t1.isFailed()) {
//	  			  t1.RedoFailed();
//	  		   }
					if(null==lastTimestamp[0]){
						SGDate tmpNow=SGDate.Now();
						lastTimestamp[0]=(double)tmpNow.toTimestamp();
						System.out.println(tmpNow);
						System.out.println(lastTimestamp[0]);
						return true;
					}
					this.PrintObject(cDay.toString());
					SGDate nowTime=SGDate.Now();
					double nowTimestamp=nowTime.toTimestamp();
					System.out.println(nowTime);
					System.out.println(nowTimestamp);
					double tmp=(nowTimestamp-lastTimestamp[0])/(intervalMinute*60000);
					if(tmp>1.2||tmp<0.8){
						hasError[0]=true;
					}
					lastTimestamp[0]=nowTimestamp;
					checkCount[0]--;
					return true;
				}, // .AddMinutes(0)
				TimePoint.every, 0, 0,(int) intervalMinute, true, null);
		_intervalRedoFailedTask.Start();
		while (true) {
			if(hasError[0]){
				throw new Exception("有异常,");
			}
			if(checkCount[0]<1){
				break;
			}
			Thread.sleep(2000);
		}
		System.out.println("测试通过");
	}

//	public void testDayTask() throws Exception {
//		initPFHelper();
//		boolean[] b = new boolean[] { false, false, false };
//		PFDate start = PFDate.Now();
//		this.PrintObject("start:" + start.toString());
//		PFDate point = start.AddMinutes(1);
//		int housr = point.GetHour();
////		int min=now.GetMinute();
//		int min = point.GetMinute();
//		int[] cnt = new int[] { 1 };
//
//		PFDayTask t1 = new PFDayTask("DayHyDataTest", (cDay, lastBackupTime, task) -> {
////			if(cnt[0]>0) {
////				this.PrintObject("执行失败:"+String.valueOf(cnt[0]));
////				cnt[0]--;
////				return false;
////			}else {
////				this.PrintObject("执行成功:"+String.valueOf(cnt[0]));
////				return true;
////			}
//			this.PrintObject("执行成功:" + cDay);
//			b[0] = true;
//			return true;
//		}, housr, min, // 每月11日3时执行
//				false, null);
//
//		t1.Start();
//
//		try {
//			while (!b[0]) {
//				Thread.sleep(2000);
//			}
//			PFDate now = PFDate.Now();
//			this.PrintObject("now:" + now.toString());
//			this.PrintObject("point:" + point.toString());
//			// int cmp=now.compareTo(point);
//			long cmp = now.toTimestamp() - point.toTimestamp();
//			this.PrintObject("compare:" + cmp);
//			if (cmp < -60000) {// 定时器是精确到分
//				throw new Exception("PFDayTask2异常,未到时间就执行了");
//			}
//			// Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public static final TaskFollower followerBenjamin = new TaskFollower("benjamin", "li@sellgirl.com", "15907658484");
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
		int minute=1;
		SGDate point = start.AddMinutes(minute);
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
				true,  new TaskFollower[] {
						//followerBenjamin
		});

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
	 * 作业不应该自动重试的.
	 * (其实是因为之前版本,当isMissed为true时,立即又重试了,现在新版本在ResetBackupTime里面重置isMissed)
	 * 所以现在是不会自动重试的
	 * @throws Exception
	 */
	public void testDayTask2AutoRedo() throws Exception {
		initPFHelper();
		TaskFollower[] followers=new TaskFollower[] {
				//followerBenjamin
		};
		boolean[] b = new boolean[] { false, false, false };
		SGDate start = SGDate.Now();
		this.PrintObject("start:" + start.toString());
		SGDate point = start.AddMinutes(-2);
		int housr = point.GetHour();
//		int min=now.GetMinute();
		int min = point.GetMinute();
		int[] cnt = new int[] { 1 };
		PFDayTask2 t1 = new PFDayTask2("DayHyDataTest", (cDay, lastBackupTime, task) -> {
			if(cnt[0]>0) {
				System.out.println("执行失败:"+String.valueOf(cnt[0]));
				cnt[0]--;
				return false;
			}else {
				System.out.println("执行成功:"+String.valueOf(cnt[0]));
				b[0] = true;
				return true;
			}
//			this.PrintObject("执行成功:" + cDay);
//			b[0] = true;
//			return true;
		}, housr, min, // 每月11日3时执行
				false,  followers);
		t1.ResetBackupTime(point.ToCalendar());//当前执行过的话,会写到txt里面,多次调试的情况下,没有这句的话,当天就不会再执行
		t1.Start();

//		while (!b[0]){
//			Thread.sleep(1000);
//		}
//		System.out.println("测试通过");
		Thread.sleep(5000);
		if(t1.isFailed()){
			System.out.println("测试通过");
		}else{
			throw new Exception("任务自动重试了,不在意料之中");
		}
	}
	/**
	 * 此方法之前不是每次都成功, 现在改版后已经没有这问题了.
	 * 测试通过
	 * @throws Exception
	 */
	public void testDayTask2RedoFailed() throws Exception {
		initPFHelper();
		TaskFollower[] followers=new TaskFollower[] { followerBenjamin };
		boolean[] b = new boolean[] { false, false, false };
		SGDate start = SGDate.Now();
		this.PrintObject("start:" + start.toString());
		SGDate point = start.AddMinutes(-2);
		int housr = point.GetHour();
//		int min=now.GetMinute();
		int min = point.GetMinute();
		int[] cnt = new int[] { 1 };
		PFDayTask2 t1 = new PFDayTask2("DayHyDataTest", (cDay, lastBackupTime, task) -> {
			if(cnt[0]>0) {
				System.out.println("执行失败:"+String.valueOf(cnt[0]));
				cnt[0]--;
				return false;
			}else {
				System.out.println("执行成功:"+String.valueOf(cnt[0]));
				b[0] = true;
				return true;
			}
//			this.PrintObject("执行成功:" + cDay);
//			b[0] = true;
//			return true;
		}, housr, min, // 每月11日3时执行
				false, followers);
		t1.ResetBackupTime(point.ToCalendar());
		t1.Start();
		System.out.println("任务启动"+SGDate.Now());
		while (!t1.isFailed()){
			Thread.sleep(1000);
		}
		System.out.println("任务当前是失败状态"+SGDate.Now());
		Thread.sleep(1000);
		while (t1.isDoing()){
			Thread.sleep(1000);
			System.out.println("5555555555555");
		}
		System.out.println("开始重试"+SGDate.Now());
		t1.RedoFailed();

		while (t1.isFailed()){
			Thread.sleep(1000);
			//System.out.println("lockTimeName:"+t1._isLockTimeName);
		}

		Thread.sleep(3000);
		if(t1.isFailed()){
			System.out.println("重试失败"+SGDate.Now());
			throw new Exception("重试失败");
		}else{
			System.out.println("重试成功"+SGDate.Now());
			if(null!=followers&&followers.length>0){
				//发补偿通知短信的时间
				Thread.sleep(2000);
			}
		}
		try {
			while (!b[0]) {
				Thread.sleep(2000);
			}
//			PFDate now = PFDate.Now();
//			this.PrintObject("now:" + now.toString());
//			this.PrintObject("point:" + point.toString());
//			// int cmp=now.compareTo(point);
//			long cmp = now.toTimestamp() - point.toTimestamp();
//			this.PrintObject("compare:" + cmp);
//			if (cmp < -60000) {// 定时器是精确到分
//				throw new Exception("PFDayTask2异常,未到时间就执行了");
//			}
//			// Thread.sleep(2000);
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
		SGSpeedCounter speed = new SGSpeedCounter(SGDate.Now());
		String aa = "bb";
		stmt.executeBatch();
		String bb = "bb";
		// conn.commit();
		System.out.println(speed.getSpeed(m[0], SGDate.Now()));

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
		SGSpeedCounter speed = new SGSpeedCounter(SGDate.Now());
		String aa = "bb";
		stmt.executeBatch();
		// stmt.executeLargeBatch();
		String bb = "bb";
		conn.commit();
		System.out.println(speed.getSpeed(m[0], SGDate.Now()));

	}

	public void testHugeInsertReader5() throws Exception {

		ISqlExecute dstExec = null;
		SGSqlCommandString sql = SGDataHelper.readObject("test001.txt", LocalDataType.Deletable);
		ISGJdbc dstJdbc = JdbcHelperTest.GetBonusJdbc();
		dstExec = SGSqlExecute.Init(dstJdbc);
		Connection conn = dstExec.GetConn();
		SGSpeedCounter speed = new SGSpeedCounter(SGDate.Now());
		Integer[] m = new Integer[] { 0 };
		m[0] = sql.size();
		SGSqlCommandString sql2 = new SGSqlCommandString();
		for (int i = 0; i < sql.size() && i < 10; i++) {
			sql2.add(sql.get(i));
		}
		dstExec.ExecuteSql(sql2);
		System.out.println(speed.getSpeed(m[0], SGDate.Now()));

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
		SGSpeedCounter speed2 = new SGSpeedCounter();
		speed2.setBeginTime(1000);
		speed2.setEndTime(10000000);
		String r1 = speed2.getSpeed2(5001);
		SGDataHelper.FormatString("\r\nconvert time waste:\r\n{0}\r\n  (processBatch:{1})\r\n", r1, 5000);
		String aa = "aa";

	}

	public void testSpeed3() {
		SGSpeedCounter speed2 = new SGSpeedCounter();
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
		SGSpeedCounter speed = new SGSpeedCounter(SGDate.Now());
		for (int i = 0; i < total; i++) {
			if (!UserTypeClass.Fgs.equals(UserTypeClass.Fgs)) {
				throw new Exception("判断错误");
			}
		}
		LOGGER.info(speed.getSpeed(total, SGDate.Now()));
	}

	// rows:1.00E+006 speed:3.70E+007条/秒 耗时:0时0分0秒270毫秒/千万行 总耗时:0时0分0秒27毫秒
	// (begin:2022-09-27 14:05:48 -> now:2022-09-27 14:05:48)
	public void testEnumSpeed2() throws Exception {
		int total = 1000000;
		SGSpeedCounter speed = new SGSpeedCounter(SGDate.Now());
		for (int i = 0; i < total; i++) {
			if (!TestEnum001.Fgs.equals(TestEnum001.Fgs)) {
				throw new Exception("判断错误");
			}
		}
		LOGGER.info(speed.getSpeed(total, SGDate.Now()));
	}

	// rows:1.00E+007 speed:6.44E+006条/秒 耗时:0时0分1秒551毫秒/千万行 总耗时:0时0分1秒551毫秒
	// (begin:2022-10-26 08:43:00 -> now:2022-10-26 08:43:01)
	public void testEnumSpeed3() throws Exception {
		int total = 10000000;
		SGSpeedCounter speed = new SGSpeedCounter(SGDate.Now());
		for (int i = 0; i < total; i++) {
			if (PFSqlFieldType.String != PFEnumClass.EnumParse(PFSqlFieldType.class, "String")) {
				throw new Exception("判断错误");
			}
			if (PFSqlFieldType.Bool != PFEnumClass.EnumParse(PFSqlFieldType.class, "Bool")) {
				throw new Exception("判断错误");
			}
		}
		LOGGER.info(speed.getSpeed(total, SGDate.Now()));
	}

	// 事实证明PFSqlFieldTypeEnum比PFSqlFieldType更快
	// rows:1.00E+007 speed:1.05E+008条/秒 耗时:0时0分0秒95毫秒/千万行 总耗时:0时0分0秒95毫秒
	// (begin:2022-10-26 08:45:04 -> now:2022-10-26 08:45:04)
	public void testEnumSpeed4() throws Exception {
		int total = 10000000;
		SGSpeedCounter speed = new SGSpeedCounter(SGDate.Now());
		for (int i = 0; i < total; i++) {
			if (SGSqlFieldTypeEnum.String != SGSqlFieldTypeEnum.valueOf("String")) {
				throw new Exception("判断错误");
			}
			if (SGSqlFieldTypeEnum.Bool != SGSqlFieldTypeEnum.valueOf("Bool")) {
				throw new Exception("判断错误");
			}
		}
		LOGGER.info(speed.getSpeed(total, SGDate.Now()));
	}
//[main] INFO org.sellgirlPayHelperNotSpring.UncheckDe001 - rows:1.00E+007 speed:2.94E+008条/秒 averageTime:0时0分0秒34毫秒/千万行 totalTime:0时0分0秒34毫秒 (begin:2022-11-29 15:08:18 -> now:2022-11-29 15:08:18)
	public void testFlagEnumSpeed1() throws Exception {
		SGSpeedCounter speed = new SGSpeedCounter(SGDate.Now());
		SGFlagEnum<FileSizeUnitType> unit3 = new SGFlagEnum<FileSizeUnitType>(FileSizeUnitType.GB)
				.or(FileSizeUnitType.MB);
		int total = 10000000;
		for (int i = 0; i < total; i++) {
			unit3.hasFlag(FileSizeUnitType.GB);
			unit3.hasFlag(FileSizeUnitType.MB);
			unit3.hasFlag(FileSizeUnitType.KB);
		}
		LOGGER.info(speed.getSpeed(total, SGDate.Now()));
	}
//[main] INFO org.sellgirlPayHelperNotSpring.UncheckDe001 - rows:1.00E+007 speed:1.34E+007条/秒 averageTime:0时0分0秒743毫秒/千万行 totalTime:0时0分0秒743毫秒 (begin:2022-11-29 15:08:42 -> now:2022-11-29 15:08:43)
	public void testFlagEnumSpeed2() throws Exception {
		SGSpeedCounter speed = new SGSpeedCounter(SGDate.Now());
		PFSqlFieldType unit3 = PFSqlFieldType.Decimal.Or(PFSqlFieldType.Int);
		int total = 10000000;
		for (int i = 0; i < total; i++) {
			unit3.HasFlag(PFSqlFieldType.Decimal);
			unit3.HasFlag(PFSqlFieldType.Int);
			unit3.HasFlag(PFSqlFieldType.DateTime);
		}
		LOGGER.info(speed.getSpeed(total, SGDate.Now()));
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
        SGSqlInsertCollection insert=dstExec.getInsertCollection(dstMd);
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
}
