package org.sellgirlPayClickHouse;

import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;
import org.sellgirlPayClickHouse.model.JdbcHelperTest;
import org.sellgirlPayClickHouse.model.PFConfigTestMapper;
import com.sellgirl.sgJavaHelper.SGSpeedCounter;
import com.sellgirl.sgJavaHelper.config.PFAppConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.MycatMulitJdbcVersionTest;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UncheckClickHouseInsertSpeed001 extends TestCase {
    public static void initPFHelper() {
        SGDataHelper.SetConfigMapper(new PFConfigTestMapper());
        new SGDataHelper(new PFAppConfig());
    }
    /**
     * 100000插入非常快:
     * rows:9.99E+005 speed:5.34E+005条/秒 averageTime:0时0分18秒720毫秒/千万行 totalTime:0时0分1秒872毫秒 (begin:2023-02-20 08:46:54 -> now:2023-02-20 08:46:55)
     *
     * 结论:无影响
     */
    public void testHousepowerDriverSpeed() throws Exception {
        initPFHelper();
        ISGJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest5Jdbc();//ok
        SGSpeedCounter speed=new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
        try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
            myResource.GetConn().setAutoCommit(false);
            int insertCnt=1000000;
            int[] idx= new int[]{0};
            boolean b =
                    myResource.doInsertList(
                            Arrays.asList(new String[]{"group_name","json"}),
                            "test_tb_02_all",
                            (a, b2, c) -> a < insertCnt,
                            (a) -> {
                                Map<String,String> map=new HashMap<>();
                                map.put("group_name", String.valueOf(idx[0]));
                                map.put("json", String.valueOf(idx[0]));
                                idx[0]++;
                                return map;
                            },
                            null,
                            a -> {
                                // 测试速度
                                System.out.println(speed.getSpeed(a,com.sellgirl.sgJavaHelper.SGDate.Now()));
                            },
                            null);
        } catch (Exception e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
    public void testYandexDriverSpeed() throws Exception {

    }

}
