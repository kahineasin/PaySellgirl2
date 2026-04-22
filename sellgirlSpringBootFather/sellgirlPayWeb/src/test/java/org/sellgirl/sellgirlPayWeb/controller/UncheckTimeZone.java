package org.sellgirl.sellgirlPayWeb.controller;

import junit.framework.TestCase;
import org.sellgirl.sellgirlPayWeb.controller.model.JdbcHelperTest;
import org.sellgirl.sellgirlPayWeb.controller.model.PFConfigTestMapper;

import com.sellgirl.sellgirlPayWeb.configuration.jdbc.JdbcHelper;
import com.sellgirl.sellgirlPayWeb.oAuth.FormsAuth;
import com.sellgirl.sellgirlPayWeb.pay.service.OrderService;
import com.sellgirl.sellgirlPayWeb.user.model.User;
import com.sellgirl.sellgirlPayWeb.user.service.UserService;
import com.sellgirl.sgHelperExport.SGExcelHelper;
import com.sellgirl.sgJavaHelper.SGDataTable;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.SGSqlCommandString;
import com.sellgirl.sgJavaHelper.config.PFAppConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.MycatMulitJdbcVersionTest;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlUpdateCollection;

import java.sql.DriverManager;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class UncheckTimeZone  extends TestCase {
    public  void initPFHelper() {
        SGDataHelper.SetConfigMapper(new PFConfigTestMapper());
        new SGDataHelper(new PFAppConfig());

//        /**
//         * 这个数据库正确
//            system_time_zone ""	
//			time_zone	SYSTEM
//         */
//    	JdbcHelper.setShop(JdbcHelperTest.GetSgShopJdbc());//
//    	userName="aaa";

        /**
system_time_zone	UTC
time_zone	SYSTEM  
在我本机调试服务器的数据库时,时区不一致会有问题, 但是, 程序运行在服务器时, 程序时区和mysql时区一致,好像又没问题?
         */
    	JdbcHelper.setShop(JdbcHelperTest.GetSgShop2Jdbc());//
    	userName="aa";
    }
    public void testUpdate() throws Exception {
        initPFHelper();
//        ISGJdbc dstJdbc = JdbcHelperTest.GetSgShop2Jdbc();//tcp ok

//    	JdbcHelper.setShop(JdbcHelperTest.GetSgShop2Jdbc());
    	UserService userService = new UserService ();
		SGRef<User> u=new SGRef<User>(); 
    	userService.signDay(userName,u,1);
    	
    	

        User user = FormsAuth.checkUser(userName,null,false);
        SGDate now= SGDate.Now();
        SGDate now2=user.getLastSign();
        System.out.println("--------correct------");
        System.out.println("time:"+now+" timestampLong:"+now.toTimestamp());
        System.out.println("--------mysql------");
        System.out.println("time:"+now2+" timestampLong:"+now2.toTimestamp());
        //com.google.common.collect.MapMaker
    }
    private String userName="aaa";
    public void testBackupToExcel() {
        initPFHelper();

        ISGJdbc jdbc=JdbcHelper.GetShop();
        try (ISqlExecute dstExec = SGSqlExecute.Init(jdbc)) {
            String tableName="sg_user";
            SGSqlCommandString sql=new SGSqlCommandString(
                    SGDataHelper.FormatString(
                            " select * from {0}",
                            tableName
                    ));
            dstExec.close();
            SGDataTable r=dstExec.GetDataTable(sql.toString(), null, true);
            dstExec.close();
            if(null==r) {
                return ;
            }
    		String dstPath="D:\\cache\\user.xlsx";
    		SGExcelHelper.exportTableToExcel(r,dstPath);
            //System.out.println("id2:"+dstExec.GetLastInsertedId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public void testMySql2() throws Exception {
//        initPFHelper();
//        try {
//            System.out.println("测试能不能动态加载mysql jdbc驱动");
//
//            String version = "";
//            String className = "com.mysql.cj.jdbc.Driver";
//            MycatMulitJdbcVersionTest.dynamicLoadSqlServerJdbcByVersion(version, className);
//
//            DriverManager.getConnection(
//                    "jdbc:mysql://uat-cloud.perfect99.com:10129/test?rewriteBatchedStatements=true",
//                    "root",
//                    "perfect@EAS");
//            System.out.println("连接mysql成功");
//        } catch (Exception e) {
//            //            LOGGER.error(e.toString());
//            e.printStackTrace();
//        }
//    }
//    public void testClickHouse() throws Exception {
//        initPFHelper();
//        //IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTestJdbc();
//        //IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest2Jdbc(); //http ok
//        //IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest3Jdbc();  //tcp 报错 Port 9000 is for clickhouse-client program
//        ISGJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest4Jdbc();//tcp ok
//        try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
//            int cnt=SGDataHelper.ObjectToInt0( myResource.QuerySingleValue("select count(*) from current_update_date_distributed"));
//            System.out.println(cnt);
//        } catch (Exception e) { throw e; }
//
//        //com.google.common.collect.MapMaker
//    }
}
