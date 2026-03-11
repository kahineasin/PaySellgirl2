package org.pfHelperSwt;

import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;
import org.pfHelperSwt.model.JdbcHelperTest;
import org.pfHelperSwt.model.PFConfigTestMapper;
import com.sellgirl.sgJavaHelper.config.PFAppConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.MycatMulitJdbcVersionTest;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Uncheck001 extends TestCase {
    public static void initPFHelper() {
        SGDataHelper.SetConfigMapper(new PFConfigTestMapper());
        new SGDataHelper(new PFAppConfig());
    }
    public void testMetaData() throws Exception {
        initPFHelper();
        //IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTestJdbc();
        //IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest2Jdbc(); //ok
        //IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest3Jdbc();  //tcp 报错 Port 9000 is for clickhouse-client program
        //IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest4Jdbc();//ok
        ISGJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest5Jdbc();//ok
        try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
//            int cnt=PFDataHelper.ObjectToInt0( myResource.QuerySingleValue("select count(*) from current_update_date_distributed"));
//            System.out.println(cnt);

            List<String> keys =new ArrayList<>();
            keys.add("group_name");
            ResultSetMetaData dstMd = myResource.GetMetaData("test_tb_04_all", keys);
            if(null==dstMd){
                //throw new Exception("dstMd为空");
                throw  myResource.GetErrorLine();
            }
            System.out.println("metaData正常");
        } catch (Exception e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        //com.google.common.collect.MapMaker
    }

    /**
     * table_id_isReadOnly:true_isAutoIncrement:false_isWritable:false
     * group_name_isReadOnly:true_isAutoIncrement:false_isWritable:false
     * json_isReadOnly:true_isAutoIncrement:false_isWritable:false
     * table_status_isReadOnly:true_isAutoIncrement:false_isWritable:false
     * update_time_isReadOnly:true_isAutoIncrement:false_isWritable:false
     * @throws Exception
     */
    public void testMetaDataReadOnly() throws Exception {
        initPFHelper();
        //IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTestJdbc();
        //IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest2Jdbc(); //ok
        //IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest3Jdbc();  //tcp 报错 Port 9000 is for clickhouse-client program
        //IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest4Jdbc();//ok
        ISGJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest5Jdbc();//ok
        try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
//            int cnt=PFDataHelper.ObjectToInt0( myResource.QuerySingleValue("select count(*) from current_update_date_distributed"));
//            System.out.println(cnt);

            List<String> keys =new ArrayList<>();
            keys=myResource.GetTableFields("test_tb_04").stream().map(a->a.getFieldName()).collect(Collectors.toList());
            ResultSetMetaData dstMd = myResource.GetMetaData("test_tb_04", keys);
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
        ISGJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest5Jdbc();//ok
        try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
//            int cnt=PFDataHelper.ObjectToInt0( myResource.QuerySingleValue("select count(*) from current_update_date_distributed"));
//            System.out.println(cnt);

//            List<String> keys =new ArrayList<>();
//            keys=myResource.GetTableFields("test_tb_04_all").stream().map(a->a.getFieldName()).collect(Collectors.toList());
//            ResultSetMetaData dstMd = myResource.GetMetaData("test_tb_04_all", keys);
            PreparedStatement pstmt = myResource.GetConn().prepareStatement("insert into perfect_test.test_tb_01_all values(?)");
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
    public void testMetaDataReadOnly4() throws Exception {
        initPFHelper();
        //IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTestJdbc();
        //IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest2Jdbc(); //ok
        //IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest3Jdbc();  //tcp 报错 Port 9000 is for clickhouse-client program
        //IPFJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest4Jdbc();//ok
        ISGJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest5Jdbc();//ok
        //IPFJdbc dstJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();
        try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
            DatabaseMetaData m_DBMetaData = myResource.GetConn().getMetaData();
            System.out.println("m_DBMetaData.isReadOnly()");
            System.out.println(m_DBMetaData.isReadOnly());
            String columnName;

            String columnType;

            ResultSet colRet = m_DBMetaData.getColumns(null,"%", "test_tb_04","%");

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
    /**
     * 测试能不能动态加载clickhouse的jdbc驱动.
     *
     * 实测可以动态加载jtds-1.3.1.jar
     */
    public void testDynamicLoadClickHouseJdbcByVersion() {
        try {
            System.out.println("测试能不能动态加载clickhouse jdbc驱动");

            Connection conn = null;
            try {
                conn = DriverManager.getConnection("jdbc:clickhouse://uat-cloud.perfect99.com:20099/perfect_clickhouse",
                        "default", "perfect@ClickHouse2020");
            } catch (Exception e) {

            }
            System.out.println("第1次 连接clickhouse成功: "+(null!=conn));
            if(null!=conn){
                conn.close();
                conn = null;
            }

            String version = "";
            String className = "com.github.housepower.jdbc.ClickHouseDriver";
            MycatMulitJdbcVersionTest.dynamicLoadClickHouseJdbcByVersion(version, className);
            //MycatMulitJdbcVersionTest.dynamicLoadClickHouseJdbcByVersion2(version, className);
            //net.jpountz.lz4.LZ4Factory xx=null;
            try {
                conn = DriverManager.getConnection("jdbc:clickhouse://uat-cloud.perfect99.com:20099/perfect_clickhouse",
                        "default", "perfect@ClickHouse2020");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("第2次 连接clickhouse成功: "+(null!=conn));
            if(null!=conn){
                conn.close();
                conn = null;
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * ok
     * @throws Exception
     */
    public void testDoInsertList() throws Exception {
        initPFHelper();

        List<JSONObject> list = new ArrayList<>();
        JSONObject item=new JSONObject();
        item.put("group_name","testGroup_04");
        list.add(item);
        int insertCnt = list.size();
        ISGJdbc dstJdbc = JdbcHelperTest.GetClickHouseTest5Jdbc();//ok
        try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
            boolean b = myResource.doInsertList(
//            			srcFieldNames,
                    null, "test_tb_01_all",
                    // list.size(),
                    (a, b2, c) -> a < insertCnt,
                    // a->list.get(a),
                    a -> {
                        JSONObject r = list.get(a);
//        					if(r.containsKey("Price")&&!r.containsKey("price")) {
//            					r.put("price",r.get("Price"));
//        					}
                        return r;
                    },
//        				(update,b2,c)->update.Set("order_source", 2),//1 crm 2 health
                    null, null, null);
            if(!b){
                throw myResource.GetErrorLine();
            }
        } catch (Exception e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
