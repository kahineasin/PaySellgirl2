package org.sellgirl.sellgirlPayWeb.controller;

import junit.framework.TestCase;
import org.sellgirl.sellgirlPayWeb.controller.model.JdbcHelperTest;
import org.sellgirl.sellgirlPayWeb.controller.model.PFConfigTestMapper;

import com.sellgirl.sellgirlPayWeb.configuration.jdbc.JdbcHelper;
//import com.sellgirl.sellgirlPayWeb.pay.model.vipOrder;
import com.sellgirl.sellgirlPayWeb.pay.service.OrderService;
import com.sellgirl.sellgirlPayWeb.pay.service.ZPayNativeService;
import com.sellgirl.sellgirlPayWeb.user.model.UserCreate;
import com.sellgirl.sellgirlPayWeb.user.service.UserService;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.config.PFAppConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.MycatMulitJdbcVersionTest;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;

import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class UncheckShopData  extends TestCase {
    public static void initPFHelper() {
        SGDataHelper.SetConfigMapper(new PFConfigTestMapper());
        new SGDataHelper(new PFAppConfig());
    }
    /**
     * ok
     * 批量添加测试账号 
     */
    public void testTransferBookChap() {
		ISGJdbc srcJdbc = JdbcHelperTest.GetSgShopJdbc();
		ISGJdbc dstJdbc = JdbcHelperTest.GetSgShopLocalJdbc();
//    	JdbcHelper.setShop(JdbcHelperTest.GetSgShop2Jdbc());
//    	UserService userService = new UserService();
    	SGRef<String> msg=new SGRef<String>();
    	int begin=1;
    	int end=1000;
//    	List<LinkedHashMap<String, Object>> list=new ArrayList<LinkedHashMap<String, Object>>();
    	
    	SGDate now=SGDate.Now();

		try (ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
				ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc)) {
			ResultSet srcDr = srcExec.GetHugeDataReader("select * from sg_book_chap");
			dstExec.HugeBulkReader(null, srcDr,"sg_book_chap", null, null, null);
		}catch(Exception e) {}
    }
}
