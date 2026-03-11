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
import com.sellgirl.sgJavaHelper.sql.PFSqlInsertCollection;
import com.sellgirl.sgJavaHelper.sql.SGSqlTransferItem;
import com.sellgirl.sgJavaHelper.sql.PFTidbSqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecuteBase;
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
public class UncheckMySqlInsertSpeed001 extends TestCase {

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

	/**
	 * 100000插入非常快:
	 * rows:9.99E+005 speed:3.04E+004条/秒 averageTime:0时5分28秒70毫秒/千万行 totalTime:0时0分32秒807毫秒 (begin:2023-02-20 16:45:59 -> now:2023-02-20 16:46:32)
	 * ProcessBatch:5000
	 *
	 * rows:9.95E+005 speed:2.67E+004条/秒 averageTime:0时6分13秒919毫秒/千万行 totalTime:0时0分37秒205毫秒 (begin:2023-02-22 17:22:54 -> now:2023-02-22 17:23:31)
	 * ProcessBatch:5000
	 *
	 * rows:9.50E+005 speed:5.04E+004条/秒 averageTime:0时3分18秒115毫秒/千万行 totalTime:0时0分18秒821毫秒 (begin:2023-02-22 17:25:51 -> now:2023-02-22 17:26:10)
	 * ProcessBatch:50000
	 *
	 rows:9.50E+005 speed:6.69E+004条/秒 averageTime:0时2分29秒326毫秒/千万行 totalTime:0时0分14秒186毫秒 (begin:2023-02-23 10:55:16 -> now:2023-02-23 10:55:30)
	 ProcessBatch:50000
	 *
	 * 结论:无影响
	 */
	public void testMySqlDriverSpeed() throws Exception {
		initPFHelper();
		ISGJdbc dstJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();//ok
		SGSpeedCounter[] speed=new SGSpeedCounter[]{null};
		int[] startCnt=new int[]{0};
		try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
			myResource.GetConn().setAutoCommit(false);
			myResource.SetInsertOption(a->a.setProcessBatch(50000));
			int insertCnt=1000000;
			int[] idx= new int[]{0};
			boolean b =
					myResource.doInsertList(
							Arrays.asList(new String[]{"id","col1","col2"}),
							"test_tb_04",
							(a, b2, c) -> a < insertCnt,
							(a) -> {
								Map<String,Object> map=new HashMap<>();
								map.put("id", idx[0]);
								map.put("col1", String.valueOf(idx[0]));
								map.put("col2", String.valueOf(idx[0]));
								idx[0]++;
								return map;
							},
							null,
							a -> {
								// 测试速度
								if(null==speed[0]){
									speed[0]=new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
									startCnt[0]=a;
								}
								System.out.println(speed[0].getSpeed(a-startCnt[0],com.sellgirl.sgJavaHelper.SGDate.Now()));
								System.out.println("ProcessBatch:"+myResource.GetInsertOption().getProcessBatch());
							},
							null);
		} catch (Exception e) {
			throw e;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * sql方式
	 * rows:2.00E+005 speed:3.98E+003条/秒 averageTime:0时41分52秒150毫秒/千万行 totalTime:0时0分50秒243毫秒 (begin:2023-02-23 14:10:59 -> now:2023-02-23 14:11:49)
	 * ProcessBatch:50000
	 *
	 * 改用PrepareStatment后
	 rows:9.50E+005 speed:6.61E+003条/秒 averageTime:0时25分11秒494毫秒/千万行 totalTime:0时2分23秒592毫秒 (begin:2023-02-23 15:13:58 -> now:2023-02-23 15:16:22)
	 ProcessBatch:50000
	 *
	 * @throws Exception
	 */
	public void testMySqlDriverUpdateSpeed() throws Exception {
		initPFHelper();
		ISGJdbc dstJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();//ok
		SGSpeedCounter[] speed=new SGSpeedCounter[]{null};
		int[] startCnt=new int[]{0};
		try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
			myResource.GetConn().setAutoCommit(false);
			myResource.SetInsertOption(a->a.setProcessBatch(50000));
			int insertCnt=1000000;//1000000;
			int[] idx= new int[]{0};
			boolean b =
					myResource.doUpdateList(
							Arrays.asList(new String[]{"id","col1","col2"}),
							"test_tb_04",
							new String[]{"id"},
							(a, b2, c) -> a < insertCnt,
							(a) -> {
								Map<String,Object> map=new HashMap<>();
								map.put("id", idx[0]);
								map.put("col1", String.valueOf(idx[0]));
								map.put("col2", String.valueOf(idx[0]*100));
								idx[0]++;
								return map;
							},
							null,
							a -> {
								// 测试速度
								if(null==speed[0]){
									speed[0]=new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
									startCnt[0]=a;
								}
								System.out.println(speed[0].getSpeed(a-startCnt[0],com.sellgirl.sgJavaHelper.SGDate.Now()));
								System.out.println("ProcessBatch:"+myResource.GetInsertOption().getProcessBatch());
							},
							null);
		} catch (Exception e) {
			throw e;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		Thread.sleep(2000);//写日志的时间
	}

	/**
	 *
	 * 测试doDeleteList的批量删除的性能
	 *
	 rows:9.50E+005 speed:6.36E+003条/秒 averageTime:0时26分11秒768毫秒/千万行 totalTime:0时2分29秒318毫秒 (begin:2023-02-23 10:56:29 -> now:2023-02-23 10:58:58)
	 ProcessBatch:50000

	 rows:1.10E+005 speed:4.32E+003条/秒 averageTime:0时38分33秒545毫秒/千万行 totalTime:0时0分25秒449毫秒 (begin:2023-02-23 11:01:46 -> now:2023-02-23 11:02:11)
	 ProcessBatch:5000

	 改用PrepareStatment后
	 rows:9.95E+005 speed:7.67E+003条/秒 averageTime:0时21分42秒512毫秒/千万行 totalTime:0时2分9秒600毫秒 (begin:2023-02-23 11:44:07 -> now:2023-02-23 11:46:16)
	 ProcessBatch:5000

	 rows:9.80E+005 speed:8.78E+003条/秒 averageTime:0时18分57秒765毫秒/千万行 totalTime:0时1分51秒501毫秒 (begin:2023-02-23 11:48:08 -> now:2023-02-23 11:50:00)
	 ProcessBatch:20000

	 * @throws Exception
	 */
	public void testMySqlDriverDeleteSpeed() throws Exception {
		initPFHelper();
		ISGJdbc dstJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();//ok
		SGSpeedCounter[] speed=new SGSpeedCounter[]{null};
		int[] startCnt=new int[]{0};
		try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
			myResource.GetConn().setAutoCommit(false);
			myResource.SetInsertOption(a->a.setProcessBatch(20000));
			int insertCnt=1000000;//1000000;
			int[] idx= new int[]{0};
			boolean b =
					myResource.doDeleteList(
							//Arrays.asList(new String[]{"id","col1","col2"}),
							"test_tb_04",
							new String[]{"id"},
							//insertCnt,
							(a, b2, c) -> a < insertCnt,
							(a) -> {
								Map<String,Object> map=new HashMap<>();
								map.put("id", idx[0]);
								map.put("col1", String.valueOf(idx[0]));
								map.put("col2", String.valueOf(idx[0]*1000));
								idx[0]++;
								return map;
							},
							null,
							a -> {
								// 测试速度
								if(null==speed[0]){
									speed[0]=new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
									startCnt[0]=a;
								}
								System.out.println(speed[0].getSpeed(a-startCnt[0],com.sellgirl.sgJavaHelper.SGDate.Now()));
								System.out.println("ProcessBatch:"+myResource.GetInsertOption().getProcessBatch());
							},
							null);
		} catch (Exception e) {
			throw e;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		Thread.sleep(2000);//写日志的时间
	}

//	public void testMySqlDriverSpeed2() throws Exception {
//		initPFHelper();
//		IPFJdbc dstJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();//ok
//		PFSpeedCounter[] speed=new PFSpeedCounter[]{null};
//		try (ISqlExecute myResource = PFSqlExecute.Init(dstJdbc)) {
//			myResource.GetConn().setAutoCommit(false);
//			int insertCnt=1000000;
//			int[] idx= new int[]{0};
//			boolean b =
//					((PFSqlExecuteBase)myResource).doInsertListOld(
//							Arrays.asList(new String[]{"id","col1","col2"}),
//							"test_tb_04",
//							(a, b2, c) -> a < insertCnt,
//							(a) -> {
//								Map<String,Object> map=new HashMap<>();
//								map.put("id", idx[0]);
//								map.put("col1", String.valueOf(idx[0]));
//								map.put("col2", String.valueOf(idx[0]));
//								idx[0]++;
//								return map;
//							},
//							null,
//							a -> {
//								// 测试速度
//								if(null==speed[0]){
//									speed[0]=new PFSpeedCounter(com.sellgirl.pfHelperNotSpring.PFDate.Now());
//								}
//								System.out.println(speed[0].getSpeed(a,com.sellgirl.pfHelperNotSpring.PFDate.Now()));
//								System.out.println("ProcessBatch:"+myResource.GetInsertOption().getProcessBatch());
//							},
//							null);
//		} catch (Exception e) {
//			throw e;
//		} catch (Throwable e) {
//			throw new RuntimeException(e);
//		}
//	}
//	public void testMySqlDriverSpeed3() throws Exception {
//		initPFHelper();
//		IPFJdbc dstJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();//ok
//		PFSpeedCounter[] speed=new PFSpeedCounter[]{null};
//		try (ISqlExecute myResource = PFSqlExecute.Init(dstJdbc)) {
//			myResource.GetConn().setAutoCommit(false);
//			int insertCnt=1000000;
//			int[] idx= new int[]{0};
//			boolean b =
//					((PFSqlExecuteBase)myResource).doInsertListNew2(
//							Arrays.asList(new String[]{"id","col1","col2"}),
//							"test_tb_04",
//							(a, b2, c) -> a < insertCnt,
//							(a) -> {
//								Map<String,Object> map=new HashMap<>();
//								map.put("id", idx[0]);
//								map.put("col1", String.valueOf(idx[0]));
//								map.put("col2", String.valueOf(idx[0]));
//								idx[0]++;
//								return map;
//							},
//							null,
//							a -> {
//								// 测试速度
//								if(null==speed[0]){
//									speed[0]=new PFSpeedCounter(com.sellgirl.pfHelperNotSpring.PFDate.Now());
//								}
//								System.out.println(speed[0].getSpeed(a,com.sellgirl.pfHelperNotSpring.PFDate.Now()));
//								System.out.println("ProcessBatch:"+myResource.GetInsertOption().getProcessBatch());
//							},
//							null);
//		} catch (Exception e) {
//			throw e;
//		} catch (Throwable e) {
//			throw new RuntimeException(e);
//		}
//	}
}
