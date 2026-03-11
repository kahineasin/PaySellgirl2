package org.sellgirlPayHelperNotSpring;

import static com.sellgirl.sgJavaHelper.express.PFFormulaItem.mark;
import static com.sellgirl.sgJavaHelper.express.PFFormulaItem.variable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import org.sellgirlPayHelperNotSpring.model.DayJdbc;
import org.sellgirlPayHelperNotSpring.model.JdbcHelperTest;
import org.sellgirlPayHelperNotSpring.model.PFConfigTestMapper;

import com.alibaba.fastjson.JSON;
import com.sellgirl.sgJavaHelper.*;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.express.PFExpressHelper;
import com.sellgirl.sgJavaHelper.express.PFFormula;
import com.sellgirl.sgJavaHelper.express.PFFormulaItem;
import com.sellgirl.sgJavaHelper.express.PFFormulaItemCollection;
import com.sellgirl.sgJavaHelper.task.TaskFollower;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
@SuppressWarnings("unused")
public class AppDe003 extends TestCase {
	public static void initPFHelper() {
		SGDataHelper.SetConfigMapper(new PFConfigTestMapper());		
	}
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AppDe003.class);

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public AppDe003(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppDe003.class);
	}

	private <T> void PrintObject(T o) {
		System.out.println(JSON.toJSONString(o));
	}
	
	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		PrintObject("-------------------------AppDe003 run-------------------------");
	}
	

}
