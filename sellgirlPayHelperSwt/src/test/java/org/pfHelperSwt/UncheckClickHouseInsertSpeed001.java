//package org.pfHelperSwt;
//
//import junit.framework.TestCase;
//import org.pfHelperSwt.model.JdbcHelperTest;
//import org.pfHelperSwt.model.PFConfigTestMapper;
//import com.sellgirl.pfHelperNotSpring.PFSpeedCounter;
//import com.sellgirl.pfHelperNotSpring.config.PFAppConfig;
//import com.sellgirl.pfHelperNotSpring.config.PFDataHelper;
//import com.sellgirl.pfHelperNotSpring.sql.IPFJdbc;
//import com.sellgirl.pfHelperNotSpring.sql.ISqlExecute;
//import com.sellgirl.pfHelperNotSpring.sql.PFSqlExecute;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//public class UncheckClickHouseInsertSpeed001 extends TestCase {
//    public static void initPFHelper() {
//        PFDataHelper.SetConfigMapper(new PFConfigTestMapper());
//        new PFDataHelper(new PFAppConfig());
//    }
//    /**
//     * 100000插入非常快:
//     * rows:9.99E+005 speed:5.34E+005条/秒 averageTime:0时0分18秒720毫秒/千万行 totalTime:0时0分1秒872毫秒 (begin:2023-02-20 08:46:54 -> now:2023-02-20 08:46:55)
//     *
//     * 结论:无影响
//     */
//    public void testHousepowerDriverSpeed() throws Exception {
//        initPFHelper();
//        IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest5Jdbc();//ok
//        PFSpeedCounter speed=new PFSpeedCounter(com.sellgirl.pfHelperNotSpring.PFDate.Now());
//        try (ISqlExecute myResource = PFSqlExecute.Init(dstJdbc)) {
//            myResource.GetConn().setAutoCommit(false);
//            int insertCnt=1000000;
//            int[] idx= new int[]{0};
//            boolean b =
//                    myResource.doInsertList(
//                            Arrays.asList(new String[]{"group_name","json"}),
//                            "test_tb_02_all",
//                            (a, b2, c) -> a < insertCnt,
//                            (a) -> {
//                                Map<String,String> map=new HashMap<>();
//                                map.put("group_name", String.valueOf(idx[0]));
//                                map.put("json", String.valueOf(idx[0]));
//                                idx[0]++;
//                                return map;
//                            },
//                            null,
//                            a -> {
//                                // 测试速度
//                                System.out.println(speed.getSpeed(a,com.sellgirl.pfHelperNotSpring.PFDate.Now()));
//                            },
//                            null);
//        } catch (Exception e) {
//            throw e;
//        } catch (Throwable e) {
//            throw new RuntimeException(e);
//        }
//    }
//    public void testYandexDriverSpeed() throws Exception {
//
//    }
//
//}
