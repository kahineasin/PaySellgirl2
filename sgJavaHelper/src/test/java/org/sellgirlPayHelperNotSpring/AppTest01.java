package org.sellgirlPayHelperNotSpring;

import static com.sellgirl.sgJavaHelper.express.PFFormulaItem.mark;
import static com.sellgirl.sgJavaHelper.express.PFFormulaItem.variable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.sellgirlPayHelperNotSpring.model.TestModel001;
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
public class AppTest01 extends TestCase {
	public static void initPFHelper() {
		SGDataHelper.SetConfigMapper(new PFConfigTestMapper());		
	}
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AppTest01.class);

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public AppTest01(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest01.class);
	}

	private <T> void PrintObject(T o) {
		System.out.println(JSON.toJSONString(o));
	}
	
	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		PrintObject("-------------------------AppTest01 run-------------------------");
	}

	public void testTreeListFilter() {
		initPFHelper();
		
		//List<TestModel001> data=new ArrayList<TestModel001>();
		
		TestModel001 n0=new TestModel001();
		
		TestModel001 n1=new TestModel001();
		n1.setF01("n1_f01");
		n1.setF02("n1_f02");
		
		TestModel001 c1=new TestModel001();
		c1.setF01("n1_c1_f01");
		c1.setF02("n1_c1_f02");
		n1.Children=new ArrayList<TestModel001>();
		n1.Children.add(c1);

		TestModel001 n2=new TestModel001();
		n2.setF01("n2_f01");
		n2.setF02("n2_f02");
		
		TestModel001 c2=new TestModel001();
		c2.setF01("n2_c2_f01");
		c2.setF02("n2_c2_f02");
		n2.Children=new ArrayList<TestModel001>();
		n2.Children.add(c2);

		n0.Children=new ArrayList<TestModel001>();
		n0.Children.add(n1);
		n0.Children.add(n2);
		
		//末级一致
		n0.FilterByLeaf(a->a.getF02().equals("n2_c2_f02"));

		assertTrue(n0.Children.get(0).Children.get(0).getF02().equals("n2_c2_f02"));
		
		//不是末级应该无数据
		//n0.FilterByLeaf(a->a.getF02().equals("n2_f02"));
		//n0.FilterByLeaf2(a->a.getF02().equals("n2_f02"));
	}

	public void testDirectNode() {
		initPFHelper();
		DirectNode n0=new DirectNode();
		DirectNode n1=new DirectNode();
		DirectNode n2=new DirectNode();
		DirectNode n3=new DirectNode();
		DirectNode n4=new DirectNode();
		DirectNode n5=new DirectNode();
		
		n0.setHashId("0");
		n1.setHashId("1");
		n2.setHashId("2");
		n3.setHashId("3");
		n4.setHashId("4");
		n5.setHashId("5");
		
		PFFunc3<DirectNode, Object, Object,Boolean> successAction=(a,b,c)->{
			a.setFinishedPercent(100);
			return true;
		};
		PFFunc3<DirectNode, Object, Object,Boolean> errorAction=(a,b,c)->{
			a.setFinishedPercent(50);
			return false;
		};
		n1.action=successAction;
		n2.action=successAction;
		n3.action=errorAction;
		n4.action=successAction;
		
		/**
		 *   0 -> 1           -> 5
		 *        2 -> 3 -> 4 ->
		 */
		n0.addNext(n1,n2);
		n2.addNext(n3);
		n3.addNext(n4);
		n5.addPrev(n1,n4);
		
		n0.GoSync(null);

		assertTrue(n1.success==true&&n2.success==true&&n3.success==false&&n4.success==false&&n5.success==false);
	}
}
