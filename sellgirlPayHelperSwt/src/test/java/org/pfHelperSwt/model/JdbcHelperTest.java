package org.pfHelperSwt.model;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.SGJdbc;
import com.sellgirl.sgJavaHelper.sql.PFJdbcBase;

public class JdbcHelperTest {
	public static ISGJdbc GetYJQueryJdbc() {

		ISGJdbc jdbc = new SGJdbc();
		jdbc.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
		jdbc.setPassword("perfect");
		jdbc.setUsername("sa");
		jdbc.setUrl("jdbc:jtds:sqlserver://10.0.0.11:1433/yjquery");
//		jdbc.setIp("192.168.0.29");
//		jdbc.setPort("1433");
//		jdbc.setDbName("balance");
		return jdbc;
	}
    public static ISGJdbc GetYJQueryMonthJdbc(String ym)
    {
//    	IPFJdbc r=GetYJQueryJdbc();
////    	r.setUrl(r.getUrl()+ym);
////    	r.setDbName(r.getDbName()+ym);
//    	r.setUrl(r.getUrl().replace("{ym}", ym));
//    	r.setDbName(r.getDbName().replace("{ym}", ym));

		ISGJdbc jdbc = new SGJdbc();
		jdbc.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
		jdbc.setPassword("perfect");
		jdbc.setUsername("sa");
		jdbc.setUrl("jdbc:jtds:sqlserver://10.0.0.11:1433/yjquery"+ym);
    	return jdbc;
    }
	public static ISGJdbc GetBalanceJdbc() {
		ISGJdbc jdbc = new SGJdbc();
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

	/**
	 * 29.balance
	 * @return
	 */
	public static ISGJdbc GetDayJdbc() {

		ISGJdbc jdbc = new SGJdbc();
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
	public static ISGJdbc GetDayJdbc2() {
		ISGJdbc jdbc = GetDayJdbc();
		jdbc.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		//jdbc.setUrl("jdbc:sqlserver://192.168.0.29:1433/balance");
		String url = SGDataHelper.FormatString("jdbc:sqlserver://{0};DatabaseName={1};user={2};password={3}",
				jdbc.getIp(),
				// _jdbc.getPort(),
				jdbc.getDbName(), jdbc.getUsername(), jdbc.getPassword());
		jdbc.setUrl(url);
		return jdbc;
	}
	public static ISGJdbc GetDayProdJdbc() {

		ISGJdbc jdbc = new SGJdbc();
		jdbc.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
		jdbc.setPassword("perfect");
		jdbc.setUsername("sa");
		jdbc.setUrl("jdbc:jtds:sqlserver://192.168.0.33:1433/balance");
		// jdbc.setIp("192.168.0.29");
		// jdbc.setPort("1433");
		jdbc.setIp("192.168.0.33:1433");
		jdbc.setDbName("balance");
		return jdbc;
	}

	/**
	 * 192.168.0.29:1433/bonus
	 * @return
	 */
	public static ISGJdbc GetBonusJdbc() {
		ISGJdbc jdbc = new SGJdbc();
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
		ISGJdbc jdbc = new SGJdbc();
		jdbc.setDriverClassName("ru.yandex.clickhouse.ClickHouseDriver");
		jdbc.setPassword("perfect@ClickHouse");
		jdbc.setUsername("local");
		jdbc.setUrl("jdbc:clickhouse://cloud.perfect99.com:20006/shop_data");//

		return jdbc;
	}
	@Deprecated
	public static ISGJdbc GetClickHouseTestJdbc() {
		ISGJdbc jdbc = new SGJdbc();
		jdbc.setDriverClassName("ru.yandex.clickhouse.ClickHouseDriver");
		jdbc.setPassword("perfect@ClickHouse");
		jdbc.setUsername("local");
		jdbc.setUrl("jdbc:clickhouse://cloud.perfect99.com:20006/perfect_test");//

		return jdbc;
	}

	/**
	 * http协议
	 * @return
	 */
	public static ISGJdbc GetClickHouseTest2Jdbc() {
		ISGJdbc jdbc = new SGJdbc();
		jdbc.setDriverClassName("ru.yandex.clickhouse.ClickHouseDriver");
		jdbc.setPassword("perfect@ClickHouse");
		jdbc.setUsername("local");
		//jdbc.setUsername("local-ck1");
		jdbc.setUrl("jdbc:clickhouse://uat-cloud.perfect99.com:20006/perfect_clickhouse");//

		return jdbc;
	}

	/**
	 * //tcp 报错 Port 9000 is for clickhouse-client program
	 * tcp协议不能用此驱动
	 * @return
	 */
	@Deprecated
	public static ISGJdbc GetClickHouseTest3Jdbc() {
		ISGJdbc jdbc = new SGJdbc();
		jdbc.setDriverClassName("ru.yandex.clickhouse.ClickHouseDriver");
		jdbc.setPassword("perfect@ClickHouse2020");
		jdbc.setUsername("default");
		jdbc.setUrl("jdbc:clickhouse://uat-cloud.perfect99.com:20099/perfect_clickhouse");//

		return jdbc;
	}

	/**
	 * uat-cloud.perfect99.com:20099/perfect_clickhouse.
	 * tcp协议
	 *
	 * @return
	 */
	public static ISGJdbc GetClickHouseTest4Jdbc() {
		ISGJdbc jdbc = new SGJdbc();
		jdbc.setDriverClassName("com.github.housepower.jdbc.ClickHouseDriver");
		jdbc.setPassword("perfect@ClickHouse2020");
		jdbc.setUsername("default");
		jdbc.setUrl("jdbc:clickhouse://uat-cloud.perfect99.com:20099/perfect_clickhouse");//

		return jdbc;
	}

	/**
	 * uat-cloud.perfect99.com:20099/perfect_test
	 * tcp协议
	 * @return
	 */
	public static ISGJdbc GetClickHouseTest5Jdbc() {
		ISGJdbc jdbc = new SGJdbc();
		jdbc.setDriverClassName("com.github.housepower.jdbc.ClickHouseDriver");
		jdbc.setPassword("perfect@ClickHouse2020");
		jdbc.setUsername("default");
		jdbc.setUrl("jdbc:clickhouse://uat-cloud.perfect99.com:20099/perfect_test"); //

		return jdbc;
	}
	public static ISGJdbc GetHiveJdbc() {
		ISGJdbc jdbc = new SGJdbc();
		jdbc.setDriverClassName("org.apache.hive.jdbc.HiveDriver");
		jdbc.setPassword("perfect@Hive");
		jdbc.setUsername("root");
		jdbc.setUrl("jdbc:hive2://uat-cloud.perfect99.com:11005/wm_hive_db");//

		return jdbc;
	}

//	public static IPFJdbc GetUserProfileJdbc() {//clickhouse如果不能用,主用本地的mysql
//
////      driverClassName: com.mysql.jdbc.Driver
////      password: Ng7_x3_yjK
////      url: jdbc:mysql://172.100.37.88:3306/prod_trade
////      username: prod_trade_ro
////      driverVersion: 5.1.34
//		IPFJdbc srcJdbc=new PFJdbc();
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
		ISGJdbc jdbc = new SGJdbc();
		jdbc.setDriverClassName("ru.yandex.clickhouse.ClickHouseDriver");
		jdbc.setPassword("perfect@ClickHouse");
		jdbc.setUsername("local");
		jdbc.setUrl("jdbc:clickhouse://cloud.perfect99.com:20006/user_profile");//

		return jdbc;

	}

//	public static IPFJdbc GetLiGeShopJdbc() {
//		IPFJdbc srcJdbc = new PFJdbc();
//		srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
//		srcJdbc.setPassword("lige@2020");
//		srcJdbc.setUsername("perfectmall");
//		srcJdbc.setUrl("jdbc:mysql://47.115.118.63:3306/mall_center_store");
//		return srcJdbc;
//	}

	/**
	 * cloud.perfect99.com:10129/mall_center_store
	 * @return
	 */
	public static ISGJdbc GetLiGeShopJdbc() {
		ISGJdbc srcJdbc = new SGJdbc();
		srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
		srcJdbc.setPassword("perfectMALL");
		srcJdbc.setUsername("root");
		srcJdbc.setUrl("jdbc:mysql://cloud.perfect99.com:10129/mall_center_store");

		return srcJdbc;
	}
	public static ISGJdbc GetLiGeShop10177Jdbc() {
		ISGJdbc srcJdbc = new SGJdbc();
		srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
		srcJdbc.setPassword("perfectMALL");
		srcJdbc.setUsername("root");
		srcJdbc.setUrl("jdbc:mysql://cloud.perfect99.com:10177/mall_center_store");

		return srcJdbc;
	}
	public static ISGJdbc GetLiGeOrderJdbc() {
		ISGJdbc srcJdbc = new SGJdbc();
		srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
		srcJdbc.setPassword("perfectMALL");
		srcJdbc.setUsername("root");
		srcJdbc.setUrl("jdbc:mysql://cloud.perfect99.com:10077/mall_center_store?useUnicode=true&characterEncoding=utf8");

		return srcJdbc;
	}

	public static ISGJdbc GetLiGeShopUatJdbc() {
		ISGJdbc srcJdbc = new SGJdbc();
		srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
		srcJdbc.setPassword("perfectMALL");
		srcJdbc.setUsername("root");
		// srcJdbc.setUrl("jdbc:mysql://uat-cloud.perfect99.com:10077/mall_center_store");
		srcJdbc.setUrl(
				"jdbc:mysql://uat-cloud.perfect99.com:10077/mall_center_store?useUnicode=true&characterEncoding=utf8");// 这样可以解决
																														// select
																														// '男'
																														// 变成问号的问题
		return srcJdbc;
	}

	public static ISGJdbc GetYunXiShopJdbc() {

//      driverClassName: com.mysql.jdbc.Driver
//      password: Ng7_x3_yjK
//      url: jdbc:mysql://172.100.37.88:3306/prod_trade
//      username: prod_trade_ro
//      driverVersion: 5.1.34
		ISGJdbc srcJdbc = new SGJdbc();
		srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
		srcJdbc.setPassword("Ng7_x3_yjK");
		srcJdbc.setUsername("prod_trade_ro");
		srcJdbc.setUrl("jdbc:mysql://172.100.37.88:3306/prod_trade");
		srcJdbc.setDriverVersion("5.1.34");

		return srcJdbc;
	}

	public static ISGJdbc GetTidbSaleJdbc() {

//      driverClassName: com.mysql.jdbc.Driver
//      password: Ng7_x3_yjK
//      url: jdbc:mysql://172.100.37.88:3306/prod_trade
//      username: prod_trade_ro
//      driverVersion: 5.1.34
		PFJdbcBase srcJdbc = new SGJdbc();
		srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
		srcJdbc.setPassword("perfectTIDB");
		srcJdbc.setUsername("root");
//		srcJdbc.setUrl("jdbc:mysql://172.16.1.246:10010/sale");
		srcJdbc.setUrl("jdbc:mysql://172.16.1.246:10010/sale?rewriteBatchedStatements=true");
		srcJdbc.setSqlType("Tidb");
		srcJdbc.setDbaMobile("15907xxx");
		srcJdbc.setDbaName("benjamin");

		return srcJdbc;
	}
	public static ISGJdbc GetTidbSale() {

		return GetTidbSaleJdbc();
	}
	public static PFJdbcBase GetSellGirlJdbc() {

//      driverClassName: com.mysql.jdbc.Driver
//      password: Ng7_x3_yjK
//      url: jdbc:mysql://172.100.37.88:3306/prod_trade
//      username: prod_trade_ro
//      driverVersion: 5.1.34
		PFJdbcBase srcJdbc = new SGJdbc();
		srcJdbc.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
		srcJdbc.setPassword("luluSKIcom");
		srcJdbc.setUsername("sa");
		srcJdbc.setUrl("jdbc:jtds:sqlserver://121.199.2.204:1433/SellGirl.Bbs");
		//srcJdbc.setSqlType("Tidb");
		srcJdbc.setDbaMobile("15907xxx");
		srcJdbc.setDbaName("benjamin");
		// srcJdbc.setDriverVersion("5.1.34");
		srcJdbc.setIp("121.199.2.204:1433");
//		srcJdbc.setPort("1433");
		srcJdbc.setDbName("SellGirl.Bbs");
		return srcJdbc;
	}
	//@Deprecated
	public static PFJdbcBase GetSellGirlJdbc2() {

//      driverClassName: com.mysql.jdbc.Driver
//      password: Ng7_x3_yjK
//      url: jdbc:mysql://172.100.37.88:3306/prod_trade
//      username: prod_trade_ro
//      driverVersion: 5.1.34
		PFJdbcBase srcJdbc = new SGJdbc();
		srcJdbc.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
		srcJdbc.setPassword("luluSKIcom");
		srcJdbc.setUsername("sa");
		srcJdbc.setUrl("jdbc:jtds:sqlserver://121.199.2.204:1433/SellGirl.Bbs2");
		//srcJdbc.setSqlType("Tidb");
		srcJdbc.setDbaMobile("15907xxx");
		srcJdbc.setDbaName("benjamin");
		// srcJdbc.setDriverVersion("5.1.34");
		srcJdbc.setIp("121.199.2.204:1433");
//		srcJdbc.setPort("1433");
		srcJdbc.setDbName("SellGirl.Bbs2");
		return srcJdbc;
	}
	public static ISGJdbc GetLiGeSaleJdbc() {

		ISGJdbc srcJdbc = new SGJdbc();
		srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
		srcJdbc.setPassword("perfectMALL");
		srcJdbc.setUsername("root");
		srcJdbc.setUrl("jdbc:mysql://cloud.perfect99.com:10129/mall_sale");

		return srcJdbc;
	}

	/**
	 * @deprecated 此实例没运行
	 * @return
	 */
	@Deprecated
	public static ISGJdbc GetMySqlTestJdbc() {

		ISGJdbc srcJdbc = new SGJdbc();
		srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
		srcJdbc.setPassword("perfect@AIKF");
		srcJdbc.setUsername("root");
		srcJdbc.setUrl("jdbc:mysql://uat-cloud.perfect99.com:10000/test?rewriteBatchedStatements=true");

		return srcJdbc;
	}
	/**
	 * uat-cloud.perfect99.com:10129/test
	 * @return
	 */
	public static ISGJdbc GetMySqlTest2Jdbc() {

		ISGJdbc srcJdbc = new SGJdbc();
		srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
		srcJdbc.setPassword("perfect@EAS");
		srcJdbc.setUsername("root");
		srcJdbc.setUrl("jdbc:mysql://uat-cloud.perfect99.com:10129/test?rewriteBatchedStatements=true");

		return srcJdbc;
	}
}
