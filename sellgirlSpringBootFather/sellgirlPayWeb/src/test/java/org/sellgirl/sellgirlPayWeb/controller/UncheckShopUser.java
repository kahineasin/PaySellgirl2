package org.sellgirl.sellgirlPayWeb.controller;

import junit.framework.TestCase;
import org.sellgirl.sellgirlPayWeb.controller.model.JdbcHelperTest;
import org.sellgirl.sellgirlPayWeb.controller.model.PFConfigTestMapper;

import com.sellgirl.sellgirlPayWeb.configuration.jdbc.JdbcHelper;
import com.sellgirl.sellgirlPayWeb.pay.model.vipOrder;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class UncheckShopUser  extends TestCase {
    public static void initPFHelper() {
        SGDataHelper.SetConfigMapper(new PFConfigTestMapper());
        new SGDataHelper(new PFAppConfig());
    }
    /**
     * ok
     * 批量添加测试账号 
     */
    public void testAddUser() {
//		ISGJdbc dstJdbc = JdbcHelperTest.GetSgShopJdbc();
		ISGJdbc dstJdbc = JdbcHelperTest.GetSgShop2Jdbc();
//    	JdbcHelper.setShop(JdbcHelperTest.GetSgShop2Jdbc());
//    	UserService userService = new UserService();
    	SGRef<String> msg=new SGRef<String>();
    	int begin=1;
    	int end=1000;
//    	List<LinkedHashMap<String, Object>> list=new ArrayList<LinkedHashMap<String, Object>>();
    	
    	SGDate now=SGDate.Now();
//    	for(int i=begin;end+1>i;i++) {
////    		UserCreate user=new UserCreate();
////    		user.setUserName(String.valueOf(i));
////    		user.setEmail(""+i+"@qq.com");
////    		user.setPwd(String.valueOf(i));
////    		user.setInvitationCode(String.valueOf(9527) );;
////        	userService.addUser(user, msg);
//        	
//        	LinkedHashMap<String, Object> map=new LinkedHashMap<String, Object>();
//        	map.put("user_name", String.valueOf(i));
//        	map.put("email",""+i+"@qq.com" );
//        	map.put("pwd", String.valueOf(i));
//        	map.put("invitation_code",9527 );
//        	map.put("create_date",now );
////        	list.add(map);
//
////            int insertCnt = list.size();
//    	}

		try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
//			boolean b = myResource.HugeBulkList(null, list, "sg_user", null, null, null);

	        boolean b2= myResource.doInsertList( 
	                null, "sg_user",
	                (a, b, c) -> a < end-begin+1, (a) -> {
	                	int i=a+1;
	                	LinkedHashMap<String, Object> map=new LinkedHashMap<String, Object>();
	                	map.put("user_id", i);
	                	map.put("user_name", String.valueOf(i));
	                	map.put("email",""+i+"@qq.com" );
	                	map.put("pwd", String.valueOf(i));
	                	map.put("invitation_code",9527 );
	                	map.put("create_date",now );
	                	return map;
	                },null,null,null);
		}catch(Exception e) {}
    }
}
