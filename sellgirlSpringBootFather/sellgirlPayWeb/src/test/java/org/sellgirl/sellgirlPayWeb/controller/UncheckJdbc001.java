package org.sellgirl.sellgirlPayWeb.controller;

import junit.framework.TestCase;
import org.sellgirl.sellgirlPayWeb.controller.model.JdbcHelperTest;
import org.sellgirl.sellgirlPayWeb.controller.model.PFConfigTestMapper;
import com.sellgirl.sgJavaHelper.config.PFAppConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.MycatMulitJdbcVersionTest;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;

import java.sql.DriverManager;

public class UncheckJdbc001  extends TestCase {
    public static void initPFHelper() {
        SGDataHelper.SetConfigMapper(new PFConfigTestMapper());
        new SGDataHelper(new PFAppConfig());
    }
    public void testMySql() throws Exception {
        initPFHelper();
        //IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTestJdbc();
        //IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest2Jdbc(); //http ok
        //IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest3Jdbc();  //tcp 报错 Port 9000 is for clickhouse-client program
        ISGJdbc dstJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();//tcp ok
        try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
            int cnt=SGDataHelper.ObjectToInt0( myResource.QuerySingleValue("select count(*) from test_tb_03"));
            System.out.println(cnt);
        } catch (Exception e) { throw e; }

        //com.google.common.collect.MapMaker
    }
    public void testMySql2() throws Exception {
        initPFHelper();
        try {
            System.out.println("测试能不能动态加载mysql jdbc驱动");

            String version = "";
            String className = "com.mysql.cj.jdbc.Driver";
            MycatMulitJdbcVersionTest.dynamicLoadSqlServerJdbcByVersion(version, className);

            DriverManager.getConnection(
                    "jdbc:mysql://uat-cloud.perfect99.com:10129/test?rewriteBatchedStatements=true",
                    "root",
                    "perfect@EAS");
            System.out.println("连接mysql成功");
        } catch (Exception e) {
            //            LOGGER.error(e.toString());
            e.printStackTrace();
        }
    }
    public void testClickHouse() throws Exception {
        initPFHelper();
        //IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTestJdbc();
        //IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest2Jdbc(); //http ok
        //IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest3Jdbc();  //tcp 报错 Port 9000 is for clickhouse-client program
        ISGJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest4Jdbc();//tcp ok
        try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
            int cnt=SGDataHelper.ObjectToInt0( myResource.QuerySingleValue("select count(*) from current_update_date_distributed"));
            System.out.println(cnt);
        } catch (Exception e) { throw e; }

        //com.google.common.collect.MapMaker
    }
}
