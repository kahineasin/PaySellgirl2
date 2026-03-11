//package pf.java.pfHelperExport;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import junit.framework.TestCase;
//import com.sellgirl.pfHelperNotSpring.IPFJdbc;
//import com.sellgirl.pfHelperNotSpring.ISqlExecute;
//import com.sellgirl.pfHelperNotSpring.PFDataRow;
//import com.sellgirl.pfHelperNotSpring.PFDataTable;
//import com.sellgirl.pfHelperNotSpring.PFDate;
//import com.sellgirl.pfHelperNotSpring.PFDirectory;
//import com.sellgirl.pfHelperNotSpring.PFHiveSqlCreateTableCollection;
//import com.sellgirl.pfHelperNotSpring.PFModelConfigCollection;
//import com.sellgirl.pfHelperNotSpring.PFSqlCommandString;
//import com.sellgirl.pfHelperNotSpring.PFSqlCreateTableCollection;
//import com.sellgirl.pfHelperNotSpring.PFSqlExecute;
//import com.sellgirl.pfHelperNotSpring.PFTimeSpan;
//import com.sellgirl.pfHelperNotSpring.PagingParameters;
//import com.sellgirl.pfHelperNotSpring.PagingResult;
//import com.sellgirl.pfHelperNotSpring.SqlCreateTableItem;
//import com.sellgirl.pfHelperNotSpring.config.PFDataHelper;
//import pf.java.pfHelperExport.model.DayJdbc;
//
//public class UncheckDe003 extends TestCase{
//
//    /**
//     * Create the test case
//     *
//     * @param testName name of the test case
//     */
//    public UncheckDe003( String testName )
//    {
//        super( testName );
//    }
//    
//	public static IPFJdbc GetYJQueryJdbc() {
//
//		IPFJdbc jdbc = new DayJdbc();
//		jdbc.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
//		jdbc.setPassword("perfect");
//		jdbc.setUsername("sa");
//		jdbc.setUrl("jdbc:jtds:sqlserver://10.0.0.11:1433/yjquery");
////		jdbc.setIp("192.168.0.29");
////		jdbc.setPort("1433");
////		jdbc.setDbName("balance");
//		return jdbc;
//	}
//	public static IPFJdbc GetBalanceJdbc() {
//		IPFJdbc jdbc = new DayJdbc();
//		jdbc.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
//		jdbc.setPassword("wm828");
//		jdbc.setUsername("sa");
//		jdbc.setUrl("jdbc:jtds:sqlserver://172.18.2.132:1433/balance");
//////		jdbc.setIp("192.168.0.29");
//////		jdbc.setPort("1433");
////		jdbc.setIp("192.168.0.29:1433");
//		jdbc.setDbName("balance");
//		return jdbc;
//	}
//
//	public static IPFJdbc GetDayJdbc() {
//
//		IPFJdbc jdbc = new DayJdbc();
//		jdbc.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
//		jdbc.setPassword("perfect");
//		jdbc.setUsername("sa");
//		jdbc.setUrl("jdbc:jtds:sqlserver://192.168.0.29:1433/balance");
////		jdbc.setIp("192.168.0.29");
////		jdbc.setPort("1433");
//		jdbc.setIp("192.168.0.29:1433");
//		jdbc.setDbName("balance");
//		return jdbc;
//	}
//	public static IPFJdbc GetBonusJdbc() {
//		IPFJdbc jdbc=new DayJdbc();
//		jdbc.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
//		jdbc.setPassword("perfect");
//		jdbc.setUsername("sa");
//		jdbc.setUrl("jdbc:jtds:sqlserver://192.168.0.29:1433/bonus");
////		jdbc.setIp("192.168.0.29");
////		jdbc.setPort("1433");
//		jdbc.setIp("192.168.0.29:1433");
//		jdbc.setDbName("bonus");
//		return jdbc;
//	}
//	
////	public static IPFJdbc GetLiGeShopJdbc() {
////		IPFJdbc srcJdbc = new DayJdbc();
////		srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
////		srcJdbc.setPassword("lige@2020");
////		srcJdbc.setUsername("perfectmall");
////		srcJdbc.setUrl("jdbc:mysql://47.115.118.63:3306/mall_center_store");
//////		srcJdbc.setIp("192.168.0.29");
//////		srcJdbc.setPort("1433");
//////		srcJdbc.setDbName("balance");
////		return srcJdbc;
////	}
//	
//	public static IPFJdbc GetLiGeShopJdbc() {
//		IPFJdbc srcJdbc = new DayJdbc();
//		srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
//		srcJdbc.setPassword("perfectMALL");
//		srcJdbc.setUsername("root");
//		srcJdbc.setUrl("jdbc:mysql://cloud.perfect99.com:10129/mall_center_store");
////		srcJdbc.setIp("192.168.0.29");
////		srcJdbc.setPort("1433");
////		srcJdbc.setDbName("balance");
//		return srcJdbc;
//	}
//	public static IPFJdbc GetLiGeShopUatJdbc() {
//		IPFJdbc srcJdbc = new DayJdbc();
//		srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
//		srcJdbc.setPassword("perfectMALL");
//		srcJdbc.setUsername("root");
//		//srcJdbc.setUrl("jdbc:mysql://uat-cloud.perfect99.com:10077/mall_center_store");
//		srcJdbc.setUrl("jdbc:mysql://uat-cloud.perfect99.com:10077/mall_center_store?useUnicode=true&characterEncoding=utf8");//这样可以解决 select '男' 变成问号的问题
////		srcJdbc.setIp("192.168.0.29");
////		srcJdbc.setPort("1433");
////		srcJdbc.setDbName("balance");
//		return srcJdbc;
//	}
//
//	public static IPFJdbc GetYunXiShopJdbc() {
//
////      driverClassName: com.mysql.jdbc.Driver
////      password: Ng7_x3_yjK
////      url: jdbc:mysql://172.100.37.88:3306/prod_trade
////      username: prod_trade_ro
////      driverVersion: 5.1.34
//		IPFJdbc srcJdbc=new DayJdbc();
//		srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
//		srcJdbc.setPassword("Ng7_x3_yjK");
//		srcJdbc.setUsername("prod_trade_ro");
//		srcJdbc.setUrl("jdbc:mysql://172.100.37.88:3306/prod_trade");
//		srcJdbc.setDriverVersion("5.1.34");
////		srcJdbc.setIp("192.168.0.29");
////		srcJdbc.setPort("1433");
////		srcJdbc.setDbName("balance");
//		return srcJdbc;
//	}
//	public static IPFJdbc GetTidbSaleJdbc() {
//
////      driverClassName: com.mysql.jdbc.Driver
////      password: Ng7_x3_yjK
////      url: jdbc:mysql://172.100.37.88:3306/prod_trade
////      username: prod_trade_ro
////      driverVersion: 5.1.34
//		IPFJdbc srcJdbc=new DayJdbc();
//		srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
//		srcJdbc.setPassword("perfectTIDB");
//		srcJdbc.setUsername("root");
//		srcJdbc.setUrl("jdbc:mysql://172.16.1.246:10010/sale");
//		//srcJdbc.setDriverVersion("5.1.34");
////		srcJdbc.setIp("192.168.0.29");
////		srcJdbc.setPort("1433");
////		srcJdbc.setDbName("balance");
//		return srcJdbc;
//	}
//	public static IPFJdbc GetUserProfileJdbc() {
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
//
//	private static void PrintSpeed(PFDate begin, int qty,int total) {
//		long diff = PFDate.Now().ToCalendar().getTimeInMillis() - begin.ToCalendar().getTimeInMillis();
//		// System.out.println(String.valueOf(diff));
//		PFTimeSpan ts = PFDataHelper.GetTimeSpan(diff, null);
//		System.out.println(PFDataHelper.FormatString("耗时:{0}毫秒({1}),已插入数:{2},速度{3}条/秒,预计{4}", diff, ts.toString(), qty,
//				qty * 1000 / diff,(diff==0||qty  / diff==0)?"":PFDataHelper.GetTimeSpan(total/(qty  / diff),null).toString()));
//	}
//
//
//	public static void initPFHelper() {
//		PFDataHelper.SetConfigMapper(new pf.java.pfHelperExport.model.PFConfigTestMapper());		
//	}
//	
//	public static List<Map<String, Object>> ExcelToDictList(String path){
//		try {
//			Workbook wb1;
//			//wb1 = new XSSFWorkbook(new FileInputStream(new File(path)));
//			wb1 =PFExcelHelper.create(new FileInputStream(new File(path)));
//			List<Map<String, Object>> list1=PFExcelHelper.ExcelToDictList(wb1);
//			return list1;
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
//	/**
//	 * 有两个问题未解决:
//	 * 1.不要循环查数据库,插入到tmp里处理吧
//	 * 
//	 * 后来把excel导入sqlserver中,再用下面sql查:
//	 * 		select a.*,
//	  (
//		case 
//			when c.hybh is not null and c.hyxm=a.hybh
//				then '是正卡'
//			when c.hybh is not null and c.pexm=a.hybh
//				then '是副卡'
//			when c.hybh is not null 
//				then '是会员,但不知正卡副卡'
//			else
//				'不是会员'
//		end
//	  ) as 是否会员,
//	  c.hybh as hybh1,
//	  b.cdate
//	  from [tmp_list1] a
//	  left join [tmp_list2] b on b.barcode=a.barcode
//	  left join hyzl c on c.mobile=a.mobile and (c.hyxm=a.hybh or c.pexm=a.hybh)
//	  where a.hybh is not null	  
//	 */
//    public void testMatchBalanceHyzl() {
//    	try {
//		initPFHelper();
//
//		//IPFJdbc dstJdbc = UncheckTest.GetHiveJdbc();
//		//XSSFWorkbook wb1=new XSSFWorkbook(new FileInputStream(new File("D:\\wxj\\workRecord\\20210617_调查问卷会员匹配\\调查问卷信息汇总-态昌.xls")));
//		Workbook wb1=PFExcelHelper.create(new FileInputStream(new File("D:\\wxj\\workRecord\\20210617_调查问卷会员匹配\\调查问卷信息汇总-态昌.xls")));
//		List<Map<String, Object>> list1=PFExcelHelper.ExcelToDictList(wb1);
//		List<Map<String, Object>> list2=ExcelToDictList("D:\\wxj\\workRecord\\20210617_调查问卷会员匹配\\问卷填写时间.xls");
//
//
//		IPFJdbc srcJdbc = UncheckDe003.GetBalanceJdbc();
//		ISqlExecute srcExec = PFSqlExecute.Init(srcJdbc);
//		srcExec.AutoCloseConn(false);
//		String sqlFmt = "  select hyxm,pexm from [Balance].[dbo].[hyzl] where mobile='{mobile}' ";
//		PFDate begin=PFDate.Now();
//		int cnt=0;
//		for(Map<String, Object> i :list1) {
//			if(cnt>10) {
//				break;
//			}
//			String mobile=i.get("手机号码").toString();
//			String excelHyName=i.get("用户姓名").toString();
//			String barCoding=i.get("条形编码").toString();
//			PFDataTable dt=srcExec.GetDataTable(sqlFmt.replace("{mobile}",mobile ), null);
//			if(dt!=null&&!dt.IsEmpty()) {
//				PFDataRow row=dt.Rows.get(0);
//				String hyxm=PFDataHelper.ObjectToString(row.getColumn("hyxm"));
//				String pexm=PFDataHelper.ObjectToString(row.getColumn("pexm"));
//				if(excelHyName.equals(hyxm)) {
//					i.put("是否会员","是正卡");
//				}else if(excelHyName.equals(pexm)) {
//					i.put("是否会员","是副卡");
//				}else {
//					i.put("是否会员","是会员,但不知正卡副卡");
//				}
//				Map<String, Object> list2Item=PFDataHelper.ListFirstOrDefault(list2,a->a.get("条形编码").toString().equals(barCoding));
//				if(list2Item!=null) {
//					i.put("问卷填写时间",list2Item.get("问卷填写时间").toString());
//				}else {
//					i.put("问卷填写时间","");
//				}
//			}else {
//				i.put("是否会员","不是会员");
//				i.put("问卷填写时间","");
//			}
//			UncheckDe003.PrintSpeed(begin, cnt++,48271);
//		}
//		
//		//导出
//		list1=((ArrayList<Map<String, Object>>)list1).subList(0,5);
//    	PFDataTable dt=PFDataHelper.DictListToDataTable(list1);
//    	PagingResult pagingResult = PFDataHelper.PagingStore(dt, new PagingParameters (){ },
//                null,
//                false, null);
//    	Exporter exporter = Exporter.Instance(pagingResult, new ExporterOption()
////            {
////                FileType = "xlsNoMulti",
////                Scheme = Exporter.FinancialScheme,
////                SheetTitle = GetWordCMonth(cmonthff)+hr+fgsname
////            }
//    	).FileName("总表");//这里的下载名没用到
//
//		String server = PFDataHelper.GetBaseDirectory();// AppDomain.CurrentDomain.BaseDirectory;
//
//		Path pDirPath = Paths.get(server, "log");
//		String dirPath = pDirPath.toString();
//		String logPath = Paths
//				.get(dirPath, "testExportExcel.xlsx")
//				.toString();
//		PFDataHelper.EnsureFilePath(logPath);
//		if(PFDirectory.Exists(logPath)) {
//			(new File(logPath)).delete();
//		}
//    	OutputStream stream;
//		try {
//			stream = new FileOutputStream(logPath, true);
//	    	exporter.WriteToStream(stream);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//    }
//}
