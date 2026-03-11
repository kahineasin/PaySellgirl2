package org.sellgirlPayHelperNotSpring;

import com.alibaba.fastjson.JSON;
import com.sellgirl.sgJavaHelper.PFCmonth;
import com.sellgirl.sgJavaHelper.SGSpeedCounter;
import com.sellgirl.sgJavaHelper.SGSqlCommandString;
import com.sellgirl.sgJavaHelper.PFSqlCommandTimeoutSecond;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGDateRange;
import com.sellgirl.sgJavaHelper.config.PFAppConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.config.SGDataHelper.LocalDataType;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;

import junit.framework.TestCase;
import org.sellgirlPayHelperNotSpring.model.JdbcHelperTest;
import org.sellgirlPayHelperNotSpring.model.PFConfigTestMapper;
import org.sellgirlPayHelperNotSpring.model.bbs_user;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 不要随便更改此类名,以防打包时执行了此类
 * @author Administrator
 *
 */
@SuppressWarnings(value = { "unused", //"rawtypes", "serial",
		"deprecation" })
public class UncheckBatchInsertSql extends TestCase {
	public static void initPFHelper() {
		SGDataHelper.SetConfigMapper(new PFConfigTestMapper());		
		new SGDataHelper(new PFAppConfig());
	}
	public void testBatchInsert() throws Exception {
//		PrintObject(compareVersions("5.1.34","5.1.36"));
//		PrintObject(compareVersions("6.1.34","5.1.36"));
		List<bbs_user> list=new ArrayList<bbs_user>(); 
		int insertCnt=100;
		for(int i=0;i<insertCnt;i++) {
			bbs_user item=new bbs_user();
			item.setUser_name("user"+i);
			list.add(item);
		}
		ISGJdbc dstJdbc = JdbcHelperTest.GetSellGirlJdbc();
		ISqlExecute sqlExec = SGSqlExecute.Init(dstJdbc);
    	boolean b=sqlExec.doInsertList(
//    			srcFieldNames,
    			null,
    			"bbs_user",
				//list.size(), 
				(a,b2,c)->a <insertCnt , 
				a->list.get(a), 
//				(update,b2,c)->update.Set("order_source", 2),//1 crm 2 health
				null,
				null, null);
	}
}
