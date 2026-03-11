//package org.sellgirl.sellgirlPayWeb.controller;
//
//
//import java.io.File;
//import java.nio.file.Paths;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.dom4j.Element;
//import org.dom4j.io.SAXReader;
//
//import com.sellgirl.sellgirlPayWeb.configuration.PFConfigMapper;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//import com.sellgirl.sgJavaHelper.IPFJdbc;
//import com.sellgirl.sgJavaHelper.ISqlExecute;
//import com.sellgirl.sgJavaHelper.PFDataTable;
//import com.sellgirl.sgJavaHelper.PFHiveSqlCreateTableCollection;
//import com.sellgirl.sgJavaHelper.PFModelConfigCollection;
//import com.sellgirl.sgJavaHelper.PFSqlCommandString;
//import com.sellgirl.sgJavaHelper.PFSqlCreateTableCollection;
//import com.sellgirl.sgJavaHelper.PFSqlExecute;
//import com.sellgirl.sgJavaHelper.PFYmd;
//import com.sellgirl.sgJavaHelper.SqlCreateTableItem;
//import com.sellgirl.sgJavaHelper.config.PFDataHelper;
//
///**
// * Unit test for simple App.
// */
//@SuppressWarnings("unused")
//public class PFDataHelperTest 
//    extends TestCase
//{
//    /**
//     * Create the test case
//     *
//     * @param testName name of the test case
//     */
//    public PFDataHelperTest( String testName )
//    {
//        super( testName );
//    }
//
//    /**
//     * @return the suite of tests being tested
//     */
//    public static Test suite()
//    {
//        return new TestSuite( PFDataHelperTest.class );
//    }
//
//    /**
//     * Rigourous Test :-)
//     */
//    public void testApp()
//    {
//        assertTrue( true );
//    }
//    /**
//     * Rigourous Test :-)
//     */
//    public void testPFEnum()
//    {
//    	int a=PFYmd.Hour|PFYmd.Minute;
//    	Boolean b=PFDataHelper.EnumHasFlag(a, PFYmd.Minute);
//        assertTrue( b );
//    	Boolean c=PFDataHelper.EnumHasFlag(a, PFYmd.Second);
//        assertFalse( c );
//    }
//	public void testReadLocalDataTable() {
//		PFDataTable r=PFDataHelper.ReadLocalDataTable("GetSF.json");
//	  	assertTrue(r!=null&&r.Rows!=null&&r.Rows.size()>0&&r.Rows.get(0).getColumn("SFNO")!=null);
//	}
//	public void testReadLocalResourceWithEnvironmentVariable() {
//		String s=PFDataHelper.ReadLocalResourceWithEnvironmentVariable("testYml.yml");
//		assertTrue(s.indexOf("$")<0);
//	}
//	public void testXml() {
//		//String xmlfile = Paths.get(PFDataHelper.GetBaseDirectory(), pathConfig.getConfigPath(), "FieldSets.xml").toString(); // linux下ok
//		String xmlfile = Paths.get(PFDataHelper.GetBaseDirectory(), "XmlConfig", "FieldSets.xml").toString(); // linux下ok
//		// System.out.println("GetModelConfig");
//		// System.out.println(xmlfile);
//
//		// Resource resource = new ClassPathResource( pathConfig.getConfigPath() +
//		// "\\FieldSets.xml");//路径:resources/SysMenu.xml
//		SAXReader saxReader = new SAXReader();
//		org.dom4j.Document document=null;
//		try {
//			// document = saxReader.read(resource.getFile());
//			File tmpFile = new java.io.File(xmlfile);
//			if (!tmpFile.exists()) {
//				return ;
//			}
//			document = saxReader.read(tmpFile);
//			// document = saxReader.read(new java.io.File(xmlfile));
//		} catch (Exception e) {
////			WriteError(e);
////			return null;
//		}
//		Element root = document.getRootElement();
//		Element dataSets = root;
////		List<?> dataSetFields = dataSets
////				.selectNodes(FormatString("DataSet[@name='{0}']/Fields/Field", mapper.XmlDataSetName));
//		List<?> dataSetFields = dataSets
//				.selectNodes("DataSet[@name='monthly_shop_user']/Fields/Field");
//		assertTrue(true);
//	}
//
//	public static IPFJdbc GetLiGeShopJdbc() {
//		IPFJdbc srcJdbc = new com.sellgirl.sellgirlPayWeb.configuration.jdbc.DayJdbc();
//		srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
//		srcJdbc.setPassword("perfectMALL");
//		srcJdbc.setUsername("root");
//		srcJdbc.setUrl("jdbc:mysql://cloud.perfect99.com:10129/mall_center_store");
////		srcJdbc.setIp("192.168.0.29");
////		srcJdbc.setPort("1433");
////		srcJdbc.setDbName("balance");
//		return srcJdbc;
//	}
//	public static IPFJdbc GetLiGeIamJdbc() {
//		IPFJdbc srcJdbc = new com.sellgirl.sellgirlPayWeb.configuration.jdbc.DayJdbc();
//		srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
//		srcJdbc.setPassword("#*&##jfjeYEUH83499");
//		srcJdbc.setUsername("xinxizhongxin_getiam");
//		srcJdbc.setUrl("jdbc:mysql://rr-wz9ec8fa046z08044.mysql.rds.aliyuncs.com/iam");
////		srcJdbc.setIp("192.168.0.29");
////		srcJdbc.setPort("1433");
////		srcJdbc.setDbName("balance");
//		return srcJdbc;
//	}
//
////	public void testCreateHiveTable() throws Exception {
////		PFDataHelper.SetConfigMapper(new PFConfigMapper());
////
////		IPFJdbc srcJdbc = GetLiGeShopJdbc();
////		//IPFJdbc dstJdbc = UncheckTest.GetHiveJdbc();
////		
////		
////
////
////		String sql = "  select now() as data_date, card_no as user_id\r\n" + 
////				" from mall_center_member.member_info where card_no is not null and card_no <>'' limit 10";
////
////		PFSqlCreateTableCollection models = new PFHiveSqlCreateTableCollection();
////		models.DbName = "wm_hive_db";
////		models.TableName = "shop_user1";
////		// models.PrimaryKey = new String[] {"data_date","user_id"};
////		// models.ClusteredIndex = new String[] {"toYYYYMM(data_date)"} ;
////		//models.Order = new String[] { "data_date", "user_id" };
////		models.Partition = new String[] { "data_date" };
////		// models.TableIndex = new String[] {"good_no"};
////
////		// ResultSet dr = srcExec.GetDataReader(sql);
////
////		ISqlExecute srcExec = PFSqlExecute.Init(srcJdbc);
////		ResultSet dr = srcExec.GetHugeDataReader(sql);
////
////		if (dr == null) {
////			return;
////		}
////
////		PFModelConfigCollection modelConfig=PFDataHelper.GetModelConfig("yjquery",null);
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
////
////		PFSqlCommandString sqlStr = models.ToSql();
////		System.out.print(sqlStr);
////
////
////	}
//	public static void testCreateHiveTable() throws Exception {
//		PFDataHelper.SetConfigMapper(new PFConfigMapper());
//
//		IPFJdbc srcJdbc = GetLiGeIamJdbc();
//		//IPFJdbc dstJdbc = UncheckTest.GetHiveJdbc();
//		
//		
//
//
//		String sql = "  select *,1.01 as test_decimal from member_login_log limit 10";
//
//		PFHiveSqlCreateTableCollection models = new PFHiveSqlCreateTableCollection();
//		models.DbName = "wm_hive_db";
//		models.TableName = "member_login_log1";
//		// models.PrimaryKey = new String[] {"data_date","user_id"};
//		// models.ClusteredIndex = new String[] {"toYYYYMM(data_date)"} ;
//		//models.Order = new String[] { "data_date", "user_id" };
//		models.Partition = new String[] { "user_id" };
//		// models.TableIndex = new String[] {"good_no"};
//
//		// ResultSet dr = srcExec.GetDataReader(sql);
//
//		ISqlExecute srcExec = PFSqlExecute.Init(srcJdbc);
//		PFModelConfigCollection modelConfig=PFDataHelper.GetModelConfig("yjquery",null);
//		PFHiveSqlCreateTableCollection sqlStr=srcExec.GetCreateTableSql(srcJdbc, sql, models, modelConfig);
//
//		//生成datax的列格式
////		List<String> r=PFDataHelper.ListSelect(sqlStr, a->PFDataHelper.FormatString("{\"name\":\"{0}\",\"type\":\"{1}\"}", a.FieldName,sqlStr.GetFieldTypeString(a)));
////		System.out.println("["+String.join(",\r\n", r)+"]");
//		System.out.println(sqlStr.GetDataxColumnJson());
//		
////		ResultSet dr = srcExec.GetHugeDataReader(sql);
////
////		if (dr == null) {
////			return;
////		}
////
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
////
////		PFSqlCommandString sqlStr = models.ToSql();
//		//System.out.print(sqlStr);
//
//
//	}
//
//}
