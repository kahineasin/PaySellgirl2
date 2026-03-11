package org.sellgirlPayHelperNotSpring;

import static com.sellgirl.sgJavaHelper.express.PFFormulaItem.mark;
import static com.sellgirl.sgJavaHelper.express.PFFormulaItem.variable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.sellgirlPayHelperNotSpring.model.AppArgModel;
import org.sellgirlPayHelperNotSpring.model.DayJdbc;
import org.sellgirlPayHelperNotSpring.model.JdbcHelperTest;
import org.sellgirlPayHelperNotSpring.model.PFConfigTestMapper;
import org.sellgirlPayHelperNotSpring.model.TestEnum001;

import com.alibaba.fastjson.JSON;
import com.sellgirl.sgJavaHelper.*;
import com.sellgirl.sgJavaHelper.config.PFAppConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.express.PFExpressHelper;
import com.sellgirl.sgJavaHelper.express.PFFormula;
import com.sellgirl.sgJavaHelper.express.PFFormulaItem;
import com.sellgirl.sgJavaHelper.express.PFFormulaItemCollection;
import com.sellgirl.sgJavaHelper.file.SGPath;
import com.sellgirl.sgJavaHelper.model.FileSizeUnitType;
import com.sellgirl.sgJavaHelper.model.UserTypeClass;
import com.sellgirl.sgJavaHelper.sgEnum.SGFlagEnum;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.MycatMulitJdbcVersionTest;
import com.sellgirl.sgJavaHelper.task.TaskFollower;
import com.sellgirl.sgJavaHelper.time.SGTimeSpan;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
@SuppressWarnings("unused")
public class AppTest extends TestCase {
	public static void initPFHelper() {
		SGDataHelper.SetConfigMapper(new PFConfigTestMapper());		
	}
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AppTest.class);

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
	}

	public void testPFPath() {
		String fullPath = "/aa/bb/cc.jar";
		String[] r = SGPath.splitPath(fullPath);
		assertTrue("aa/bb".equals(r[0]));
		assertTrue("cc".equals(r[1]));
		assertTrue(".jar".equals(r[2]));

		fullPath = "cc.jar";
		r = SGPath.splitPath(fullPath);
		assertTrue("cc".equals(r[1]));
		assertTrue(".jar".equals(r[2]));

		fullPath = "/aa/bb/";
		r = SGPath.splitPath(fullPath);
		assertTrue("aa/bb".equals(r[0]));
		String aa = "aa";
	}

	public void testScientificNotation() {
		String r = SGDataHelper.ScientificNotation(2090);
		System.out.println(r);
	}

//	public void testLog() {
//        try {
//            throw new Exception("测试错误日志");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//	}
//	public void testLog2() {
//		
//        try {
//            throw new Exception("测试错误日志2");
//        } catch (Exception e) {
//    		LOGGER.error("error",e);
//        }
//	}
	public void testLog3() {
		System.out.println(LOGGER.getClass());
		LOGGER.info(TimeZone.getDefault().toZoneId().toString());
		// TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.SHORT_IDS.get("CTT")));
	}

	public void tesDateTime() {
		String sDate = "2021-08-12 03:00:00";
		Object o = SGDataHelper.ObjectToDateTime(sDate);
		Object o1 = SGDataHelper.StringToDateTime(sDate);
		String aa = "aa";
	}

    @SuppressWarnings("deprecation")
	public  void testEnum() {
    	//Object o=UserTypeClass.Fgs;
    	UserTypeClass r=UserTypeClass.EnumParseByInt(UserTypeClass.class, 1);
    	assertTrue(UserTypeClass.Fgs.equals(r));//失败
    	

    	TestEnum001 r2=TestEnum001.EnumParseByInt(TestEnum001.class, 5);
    	assertTrue(TestEnum001.Fgs.equals(r2));//失败
    }
    @SuppressWarnings("deprecation")
	public  void testEnum2() {
    	//Object o=UserTypeClass.Fgs;
    	TestEnum001 r=TestEnum001.valueOf(TestEnum001.class, "Fgs");
    	assertTrue(TestEnum001.Fgs.equals(r));
    	
    	//TestEnum001.values()[i];
    }
	public  void testEnumFlag() {
		SGFlagEnum<FileSizeUnitType> unit3=new SGFlagEnum<FileSizeUnitType>(FileSizeUnitType.GB).or(FileSizeUnitType.MB);
    	assertTrue(unit3.hasFlag(FileSizeUnitType.GB));
    	assertTrue(unit3.hasFlag(FileSizeUnitType.MB));
    	assertFalse(unit3.hasFlag(FileSizeUnitType.KB));
    }
	public enum enum1 {
		a, b
	}


	private <T> void PrintObject(T o) {
		System.out.println(JSON.toJSONString(o));
	}

	public void testExpression() {
		// PFExpressHelper ex=new PFExpressHelper();
		PFExpressHelper ex = PFExpressHelper.popular();
		// &运算
		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.variable(),
				PFFormulaItem.mark("&&"), PFFormulaItem.variable()), (a, b, c) -> {
					return SGDataHelper.ObjectToInt(a) > 0 && SGDataHelper.ObjectToInt(b) > 0;
				}, 100));
		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.variable(),
				PFFormulaItem.mark("||"), PFFormulaItem.variable()), (a, b, c) -> {
					return SGDataHelper.ObjectToInt(a) > 0 || SGDataHelper.ObjectToInt(b) > 0;
				}, 100));
		// 标签运算
		Map<String, String> tags = new HashMap<String, String>();
		tags.put("ATTRITUBE_U_06_001", "5");
		tags.put("ATTRITUBE_U_07_001", "5");
		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.variable(), PFFormulaItem.mark(","),
				PFFormulaItem.variable()), (a, b, c) -> {
					return tags.containsKey(a.toString());
				}, 50));

		Object r = null;
		r = ex.eval("1+3");
		PrintObject("answer: " + r.toString());
		assertTrue((int) r == 4);
		r = ex.eval("2+(3-1)");
		PrintObject("answer: " + r.toString());
		assertTrue((int) r == 4);
		r = ex.eval("2+(5-1-3)");
		PrintObject("answer: " + r.toString());
		assertTrue((int) r == 3);
		r = ex.eval("3*(10+2)+2");
		PrintObject("answer: " + r.toString());
		assertTrue((int) r == 38);
		r = ex.eval("2-3+4*5");
		PrintObject("answer: " + r.toString());
		assertTrue((int) r == 19);
		r = ex.eval("2-(3+4*5)");
		PrintObject("answer: " + r.toString());
		assertTrue((int) r == -21);
		r = ex.eval("3*(10+2)+2&&0");
		PrintObject("answer: " + r.toString());
		assertTrue((boolean) r == false);
		r = ex.eval("\"aa\"+\"bb\"");
		PrintObject("answer: " + r.toString());
		assertTrue(r.equals("\"aabb\""));
		r = ex.eval("\"aa\"+\"bb\"+\"\"+33");
		PrintObject("answer: " + r.toString());
		assertTrue(r.equals("\"aabb33\""));
		r = ex.eval("ATTRITUBE_U_06_001,5&&ATTRITUBE_U_06_002,6");// 现在不支持这种(因为没优先级)
		PrintObject("answer: " + r.toString());
		assertTrue((boolean) r == false);
		r = ex.eval("ATTRITUBE_U_06_001,5&&(ATTRITUBE_U_07_001,6||ATTRITUBE_U_08_001,6)");// 现在不支持这种(因为没优先级)
		PrintObject("answer: " + r.toString());
		assertTrue((boolean) r == true);
		r = ex.eval("(ATTRITUBE_U_06_001,5)&&(ATTRITUBE_U_06_002,6)");
		PrintObject("answer: " + r.toString());
		assertTrue((boolean) r == false);
		r = ex.eval("(ATTRITUBE_U_06_001,5)&&(ATTRITUBE_U_07_001,6)");
		PrintObject("answer: " + r.toString());
		assertTrue((boolean) r == true);
	}

	/**
	 * 这种是参考metabase的方式,如 [and,a,b,c,d]
	 */
	public void testSqlExpression() {
		// PFExpressHelper ex=new PFExpressHelper();
		PFExpressHelper ex = PFExpressHelper.popular();
		// &运算
//		//and单变量方式
//		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.mark("[\"and\","),
//				PFFormulaItem.variable(), PFFormulaItem.mark(","),PFFormulaItem.variable(),PFFormulaItem.mark("]") ), (a, b, c) -> {
//					return PFDataHelper.ObjectToString(a)+" and "+ PFDataHelper.ObjectToString(b);
//				}, 100));

////		//and多变量方式
		ex.addCustomFormula(new PFFormula(
				new PFFormulaItemCollection(PFFormulaItem.mark("[\"and\","), PFFormulaItem.multiVariable(),
						PFFormulaItem.mark("]")),
				(a, b, c) -> {
					// return PFDataHelper.ObjectToString(a)+" and "+
					// PFDataHelper.ObjectToString(b);
					return String.join(" and ", SGDataHelper.ObjectToArray(a, String.class));
				}, 100));

//		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.mark("[\"or\","),
//				PFFormulaItem.variable(), PFFormulaItem.mark(","),PFFormulaItem.variable(),PFFormulaItem.mark("]") ), (a, b, c) -> {
//					return PFDataHelper.ObjectToString(a)+" or "+ PFDataHelper.ObjectToString(b);
//				}, 100));

		ex.addCustomFormula(new PFFormula(
				new PFFormulaItemCollection(PFFormulaItem.mark("[\"or\","), PFFormulaItem.multiVariable(),
						PFFormulaItem.mark("]")),
				(a, b, c) -> {
					// return PFDataHelper.ObjectToString(a)+" and "+
					// PFDataHelper.ObjectToString(b);
					return String.join(" or ", SGDataHelper.ObjectToArray(a, String.class));
				}, 100));
		ex.addCustomFormula(
				new PFFormula(
						new PFFormulaItemCollection(PFFormulaItem.mark("[\"=\","), PFFormulaItem.variable(),
								PFFormulaItem.mark(","), PFFormulaItem.variable(), PFFormulaItem.mark("]")),
						(a, b, c) -> {
							return SGDataHelper.ObjectToString(a) + " = " + SGDataHelper.ObjectToString(b);
						}, 100));
		ex.addCustomFormula(
				new PFFormula(
						new PFFormulaItemCollection(PFFormulaItem.mark("[\">\","), PFFormulaItem.variable(),
								PFFormulaItem.mark(","), PFFormulaItem.variable(), PFFormulaItem.mark("]")),
						(a, b, c) -> {
							return SGDataHelper.ObjectToString(a) + " > " + SGDataHelper.ObjectToString(b);
						}, 100));
		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.mark("[\"field-id\","),
				PFFormulaItem.variable(), PFFormulaItem.mark("]")), (a, b, c) -> {
					return "col" + SGDataHelper.ObjectToString(a).trim();
				}, 100));
		ex.addCustomFormula(
				new PFFormula(
						new PFFormulaItemCollection(PFFormulaItem.mark("[\"joined-field\","), PFFormulaItem.variable(),
								PFFormulaItem.mark(","), PFFormulaItem.variable(), PFFormulaItem.mark("]")),
						(a, b, c) -> {
							return SGDataHelper.ObjectToString(a).substring(1, a.toString().length() - 1) + "."
									+ SGDataHelper.ObjectToString(b);
						}, 100));
		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.mark("[\"bracket\","),
				PFFormulaItem.variable(), PFFormulaItem.mark("]")), (a, b, c) -> {
					return "(" + SGDataHelper.ObjectToString(a) + ")";
				}, 100));

		Object r = null;
		r = ex.eval("[\"and\",aaa,bbb]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[\"=\",\"field1\",\"900\"]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[\"and\",[\"=\",\"field1\",\"900\"],bbb]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[\"and\",[\"=\",\"field1\",\"900\"],[\">\",\"field2\",\"800\"]]");
		PrintObject("answer: " + r.toString());

		r = ex.eval("[\"field-id\", 252]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[\"joined-field\",\"chinese_city\",[\"field-id\", 252]]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[\"joined-field\",\"chinese_city\", [\"field-id\",252]]");
		PrintObject("answer: " + r.toString());
		r = ex.eval(
				"[\"and\",[\"=\",[\"field-id\", 276],\"900\"],[\">\",[\"field-id\", 288],[\"joined-field\", \"chinese_city\", [\"field-id\", 252]]]]");
		PrintObject("answer: " + r.toString());
		r = ex.eval(
				"[\"bracket\",[\"and\",[\"=\",[\"field-id\", 276],\"900\"],[\">\",\"field2\",[\"joined-field\", \"chinese_city\", [\"field-id\", 252]]]]]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[\"and\",aa,bb,cc]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[\"and\",aa,[\"field-id\", 252]]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[\"field-id\",276]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[\"=\",[\"field-id\",276],aa]");
		PrintObject("answer: " + r.toString());

		r = ex.eval("[\"and\",[\"=\", [\"field-id\", 276], \"967122\"],aa,[\"field-id\", 252],dd]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[\"and\", [\"=\", [\"field-id\", 276], \"967122\"],[\"=\", [\"field-id\", 274], true]]");
		PrintObject("answer: " + r.toString());
		r = ex.eval(
				"[\"and\", [\"=\", [\"field-id\", 276], \"967122\"],[\"=\", [\"field-id\", 274], true],[\"=\", [\"joined-field\", \"chinese_city\", [\"field-id\", 249]], \"临沂市\"]]");
		PrintObject("answer: " + r.toString());
		r = ex.eval(
				"[\"bracket\",[\"and\", [\"=\", [\"field-id\", 276], \"967122\"],[\"=\", [\"field-id\", 274], true],[\"=\", [\"joined-field\", \"chinese_city\", [\"field-id\", 249]], \"临沂市\"]]]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[\"or\",aa,bb,cc]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[\"and\",aa,[\"bracket\",bb]]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[\"and\",aa,[\"bracket\",[\"or\",aa,bb,dd]]]");
		PrintObject("answer: " + r.toString());
		r = ex.eval(
				"[\"and\",[\"=\", [\"field-id\", 276], \"967122\"],[\"bracket\",[\"or\", [\"=\", [\"field-id\", 276], \"967122\"],[\"=\", [\"field-id\", 274], true],[\"=\", [\"joined-field\", \"chinese_city\", [\"field-id\", 249]], \"临沂市\"]]]]");
		PrintObject("answer: " + r.toString());
		assertTrue("col276 = \"967122\" and (col276 = \"967122\" or col274 = true or chinese_city.col249 = \"临沂市\")"
				.equals(r));
	}

	/**
	 * 改造过的filter配置,如 [a,and,b,and,[bracket,[c,and,d,or,e]]]
	 */
	public void testSellgirlSqlExpression() {
		// PFExpressHelper ex=new PFExpressHelper();
		PFExpressHelper ex = PFExpressHelper.popular();
		// &运算
//		//and单变量方式
////		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.mark("[\"and\","),
////				PFFormulaItem.variable(), PFFormulaItem.mark(","),PFFormulaItem.variable(),PFFormulaItem.mark("]") ), (a, b, c) -> {
////					return PFDataHelper.ObjectToString(a)+" and "+ PFDataHelper.ObjectToString(b);
////				}, 100));
//
//////		//and多变量方式
//		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.mark("[\"and\","),
//				PFFormulaItem.multiVariable(), PFFormulaItem.mark("]") ), (a, b, c) -> {
//					//return PFDataHelper.ObjectToString(a)+" and "+ PFDataHelper.ObjectToString(b);
//					return String.join(" and ",PFDataHelper.ObjectToArray(a, String.class));
//				}, 100));

		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.variable(),
				PFFormulaItem.mark(",\"and\","), PFFormulaItem.variable()), (a, b, c) -> {
					return SGDataHelper.ObjectToString(a) + " and " + SGDataHelper.ObjectToString(b);
					// return String.join(" and ",PFDataHelper.ObjectToArray(a, String.class));
				}, 100));

		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(mark("["), variable(), mark("]")),
				(a, b, c) -> SGDataHelper.ObjectToString(a), 100));

//		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.mark("[\"or\","),
//				PFFormulaItem.variable(), PFFormulaItem.mark(","),PFFormulaItem.variable(),PFFormulaItem.mark("]") ), (a, b, c) -> {
//					return PFDataHelper.ObjectToString(a)+" or "+ PFDataHelper.ObjectToString(b);
//				}, 100));

//		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.mark("[\"or\","),
//				PFFormulaItem.multiVariable(), PFFormulaItem.mark("]") ), (a, b, c) -> {
//					//return PFDataHelper.ObjectToString(a)+" and "+ PFDataHelper.ObjectToString(b);
//					return String.join(" or ",PFDataHelper.ObjectToArray(a, String.class));
//				}, 100));
		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.variable(),
				PFFormulaItem.mark(",\"or\","), PFFormulaItem.variable()), (a, b, c) -> {
					return SGDataHelper.ObjectToString(a) + " or " + SGDataHelper.ObjectToString(b);
				}, 100));

//		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.mark("[\"=\","),
//				PFFormulaItem.variable(), PFFormulaItem.mark(","),PFFormulaItem.variable(),PFFormulaItem.mark("]") ), (a, b, c) -> {
//					return PFDataHelper.ObjectToString(a)+" = "+ PFDataHelper.ObjectToString(b);
//				}, 100));
		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.mark("\"=\","),
				PFFormulaItem.variable(), PFFormulaItem.mark(","), PFFormulaItem.variable()), (a, b, c) -> {
					return SGDataHelper.ObjectToString(a) + " = " + SGDataHelper.ObjectToString(b);
				}, 100));
//		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.mark("[\">\","),
//				PFFormulaItem.variable(), PFFormulaItem.mark(","),PFFormulaItem.variable(),PFFormulaItem.mark("]") ), (a, b, c) -> {
//					return PFDataHelper.ObjectToString(a)+" > "+ PFDataHelper.ObjectToString(b);
//				}, 100));
		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.mark("\">\","),
				PFFormulaItem.variable(), PFFormulaItem.mark(","), PFFormulaItem.variable()), (a, b, c) -> {
					return SGDataHelper.ObjectToString(a) + " > " + SGDataHelper.ObjectToString(b);
				}, 100));
		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.mark("\">=\","),
				PFFormulaItem.variable(), PFFormulaItem.mark(","), PFFormulaItem.variable()), (a, b, c) -> {
					return SGDataHelper.ObjectToString(a) + " >= " + SGDataHelper.ObjectToString(b);
				}, 100));
		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.mark("\"<\","),
				PFFormulaItem.variable(), PFFormulaItem.mark(","), PFFormulaItem.variable()), (a, b, c) -> {
					return SGDataHelper.ObjectToString(a) + " < " + SGDataHelper.ObjectToString(b);
				}, 100));
		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.mark("\"<=\","),
				PFFormulaItem.variable(), PFFormulaItem.mark(","), PFFormulaItem.variable()), (a, b, c) -> {
					return SGDataHelper.ObjectToString(a) + " <= " + SGDataHelper.ObjectToString(b);
				}, 100));

		ex.addCustomFormula(
				new PFFormula(
						new PFFormulaItemCollection(PFFormulaItem.mark("["), PFFormulaItem.mark("\"contains\","),
								PFFormulaItem.variable(), PFFormulaItem.mark(","), PFFormulaItem.variable(),
								PFFormulaItem.mark(","), PFFormulaItem.variable(), PFFormulaItem.mark("]")),
						(a, b, c) -> {
							return SGDataHelper.FormatString("{0} like '%{1}%'", a, b);
						}, 100));
//		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.mark("["),PFFormulaItem.mark("\"contains\","),
//				PFFormulaItem.variable(),PFFormulaItem.mark(","),PFFormulaItem.variable(),PFFormulaItem.mark("]")), (a, b, c) -> {
//					return PFDataHelper.FormatString("{0} like '%{1}%'", a,b);
//				}, 100));

//		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.mark("[\"field-id\","),
//				PFFormulaItem.variable(),PFFormulaItem.mark("]") ), (a, b, c) -> {
//					return "col"+PFDataHelper.ObjectToString(a).trim();
//				}, 100));
		ex.addCustomFormula(new PFFormula(
				new PFFormulaItemCollection(PFFormulaItem.mark("\"field-id\","), PFFormulaItem.variable()),
				(a, b, c) -> {
					return "col" + SGDataHelper.ObjectToString(a).trim();
				}, 100));

//		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.mark("[\"joined-field\","),
//				PFFormulaItem.variable(),PFFormulaItem.mark(","),PFFormulaItem.variable(),PFFormulaItem.mark("]") ), (a, b, c) -> {
//					return PFDataHelper.ObjectToString(a).substring(1, a.toString().length()-1)+"."+PFDataHelper.ObjectToString(b);
//				}, 100));
		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.mark("\"joined-field\","),
				PFFormulaItem.variable(), PFFormulaItem.mark(","), PFFormulaItem.variable()), (a, b, c) -> {
					return SGDataHelper.ObjectToString(a).substring(1, a.toString().length() - 1) + "."
							+ SGDataHelper.ObjectToString(b);
				}, 100));

//		ex.addCustomFormula(new PFFormula(new PFFormulaItemCollection(PFFormulaItem.mark("[\"bracket\","),
//				PFFormulaItem.multiVariable(),PFFormulaItem.mark("]") ), (a, b, c) -> {
//					return "("+PFDataHelper.ObjectToString(a)+")";
//				}, 100));

		ex.addCustomFormula(
				new PFFormula(new PFFormulaItemCollection(PFFormulaItem.mark("["), PFFormulaItem.mark("\"bracket\","),
						PFFormulaItem.multiVariable(), PFFormulaItem.mark("]")), (a, b, c) -> {
							return "(" + SGDataHelper.ObjectToString(a) + ")";
						}, 100));

		Object r = null;
//		r = ex.eval("[\"and\",aaa,bbb]");
//		PrintObject("answer: " + r.toString());
		r = ex.eval("aaa,\"and\",bbb");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[aaa,\"and\",bbb]");
		PrintObject("answer: " + r.toString());
//		r = ex.eval("[\"=\",\"field1\",\"900\"]");
//		PrintObject("answer: " + r.toString());
		r = ex.eval("[[\"=\",\"field1\",\"900\"],\"and\",bbb]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[[\"=\",\"field1\",\"900\"],\"and\",[\">\",\"field2\",\"800\"]]");
		PrintObject("answer: " + r.toString());

		r = ex.eval("[\"field-id\", 252]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[\"joined-field\",\"chinese_city\",[\"field-id\", 252]]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[\"joined-field\",\"chinese_city\", [\"field-id\",252]]");
		PrintObject("answer: " + r.toString());
		// r = ex.eval("[\"and\",[\"=\",[\"field-id\",
		// 276],\"900\"],[\">\",[\"field-id\", 288],[\"joined-field\", \"chinese_city\",
		// [\"field-id\", 252]]]]");
		r = ex.eval(
				"[[\"=\",[\"field-id\", 276],\"900\"],\"and\",[\">\",[\"field-id\", 288],[\"joined-field\", \"chinese_city\", [\"field-id\", 252]]]]");
		PrintObject("answer: " + r.toString());
		// r = ex.eval("[\"bracket\",[\"and\",[\"=\",[\"field-id\",
		// 276],\"900\"],[\">\",\"field2\",[\"joined-field\", \"chinese_city\",
		// [\"field-id\", 252]]]]]");
		r = ex.eval(
				"[\"bracket\",[[\"=\",[\"field-id\", 276],\"900\"],\"and\",[\">\",\"field2\",[\"joined-field\", \"chinese_city\", [\"field-id\", 252]]]]]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[aa,\"and\",bb,\"and\",cc]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[aa,\"and\",[\"field-id\", 252]]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[\"field-id\",276]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[\"=\",[\"field-id\",276],aa]");
		PrintObject("answer: " + r.toString());

		// r = ex.eval("[\"and\",[\"=\", [\"field-id\", 276],
		// \"967122\"],aa,[\"field-id\", 252],dd]");
		r = ex.eval("[[\"=\", [\"field-id\", 276], \"967122\"],\"and\",aa,\"and\",[\"field-id\", 252],\"and\",dd]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[[\"=\",[\"field-id\", 276],\"967122\"],\"and\",[\"=\", [\"field-id\", 274], true]]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("[\"=\", [\"joined-field\", \"chinese_city\", [\"field-id\", 249]], \"临沂市\"]");
		PrintObject("answer: " + r.toString());
		// r = ex.eval("[\"and\", [\"=\", [\"field-id\", 276], \"967122\"],[\"=\",
		// [\"field-id\", 274], true],[\"=\", [\"joined-field\", \"chinese_city\",
		// [\"field-id\", 249]], \"临沂市\"]]");
		r = ex.eval(
				"[[\"=\",[\"field-id\", 276],\"967122\"],\"and\",[\"=\", [\"field-id\", 274], true],\"or\",[\"=\", [\"joined-field\", \"chinese_city\", [\"field-id\", 249]], \"临沂市\"]]");
		PrintObject("answer: " + r.toString());
//		r = ex.eval("[\"bracket\",[\"and\", [\"=\", [\"field-id\", 276], \"967122\"],[\"=\", [\"field-id\", 274], true],[\"=\", [\"joined-field\", \"chinese_city\", [\"field-id\", 249]], \"临沂市\"]]]");
//		PrintObject("answer: " + r.toString());
//		r = ex.eval("[\"or\",aa,bb,cc]");
//		PrintObject("answer: " + r.toString());
//		r = ex.eval("[\"and\",aa,[\"bracket\",bb]]");
//		PrintObject("answer: " + r.toString());
//		r = ex.eval("[\"and\",aa,[\"bracket\",[\"or\",aa,bb,dd]]]");
//		PrintObject("answer: " + r.toString());
		r = ex.eval(
				"[[\"=\", [\"field-id\", 276], \"967122\"],\"and\",[\"bracket\",[ [\"=\", [\"field-id\", 276], \"967122\"],\"or\",[\"=\", [\"field-id\", 274], true],\"and\",[\"=\", [\"joined-field\", \"chinese_city\", [\"field-id\", 249]], \"临沂市\"]]]]");
		PrintObject("answer: " + r.toString());

		r = ex.eval(
				"[[\"=\",[\"field-id\",276],\"967122\"],\"and\",[\">\",[\"field-id\",274],true],\"and\",[\"<=\",[\"joined-field\",\"chinese_city\",[\"field-id\",249]],\"临沂市\"],\"and\",[\"bracket\",[[\"=\",[\"field-id\",276],\"967122\"],\"or\",[\">\",[\"field-id\",274],true]]]]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("\"aa\"+\"bb\"]");
		PrintObject("answer: " + r.toString());
		r = ex.eval("\"case-sensitive\"+\"bb\"");
		PrintObject("answer: " + r.toString());
//		r = ex.eval("[\"contains\",[\"field-id\",147],\"aa\"]");//不加sensitive配置的方式
//		PrintObject("answer: " + r.toString());
		r = ex.eval("[\"contains\",[\"field-id\",147],\"aa\",{\"case-sensitive\":false}]");
		PrintObject("answer: " + r.toString());
		r = ex.eval(
				"[[\"=\",[\"field-id\",276],\"967122\"],\"and\",[\">\",[\"field-id\",274],true],\"and\",[\"<=\",[\"joined-field\",\"chinese_city\",[\"field-id\",249]],\"临沂市\"],\"and\",[\"contains\",[\"field-id\",147],\"aa\",{\"case-sensitive\": false}],\"and\",[\"bracket\",[[\"=\",[\"field-id\",276],\"967122\"],\"or\",[\">\",[\"field-id\",274],true]]]]");
		PrintObject("answer: " + r.toString());

	}
	public void testMap() {
		ConcurrentHashMap<ErrorMobileMessageModel, SGDate> _errorMobileMessageHistory = new ConcurrentHashMap<ErrorMobileMessageModel, SGDate>();	
		_errorMobileMessageHistory.put(new ErrorMobileMessageModel("15900","xxx"),SGDate.Now().AddDays(-1));
		//PFDate v=_errorMobileMessageHistory.get(new ErrorMobileMessageModel("15900","xxx"));
		SGDate v=_errorMobileMessageHistory.getOrDefault(new ErrorMobileMessageModel("15900","xxx"),SGDate.Now());
		PrintObject("answer: " + v.toString());
		

		Map<ErrorMobileMessageModel, SGDate> _errorMobileMessageHistory2 = new HashMap<ErrorMobileMessageModel, SGDate>();	
		_errorMobileMessageHistory2.put(new ErrorMobileMessageModel("15900","xxx"),SGDate.Now().AddDays(-1));
		//PFDate v=_errorMobileMessageHistory.get(new ErrorMobileMessageModel("15900","xxx"));
		SGDate v2=_errorMobileMessageHistory2.get(new ErrorMobileMessageModel("15900","xxx"));
		PrintObject("answer: " + v.toString());
		

		ConcurrentHashMap<String, SGDate> _errorMobileMessageHistory3 = new ConcurrentHashMap<String, SGDate>();	
		_errorMobileMessageHistory3.put("15900",SGDate.Now().AddDays(-1));
		//PFDate v=_errorMobileMessageHistory.get(new ErrorMobileMessageModel("15900","xxx"));
		SGDate v3=_errorMobileMessageHistory3.get("15900");
		PrintObject("answer: " + v.toString());
	}
	
	private void checkJdbcSortName(ISGJdbc jdbc) {
		assertTrue("192.168.0.29:1433 balance".equals(jdbc.getUrlShortName()));
	}
	public void testJdbc() {
		ISGJdbc jdbc = new DayJdbc();
		jdbc.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
		jdbc.setPassword("perfect");
		jdbc.setUsername("sa");
		jdbc.setUrl("jdbc:jtds:sqlserver://192.168.0.29:1433/balance");
		jdbc.setIp("192.168.0.29:1433");
		jdbc.setDbName("balance");
		PrintObject("answer: " + jdbc.getUrlShortName());
		this.checkJdbcSortName(jdbc);
		
		jdbc = new DayJdbc();
		jdbc.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
		jdbc.setPassword("perfect");
		jdbc.setUsername("sa");
		jdbc.setUrl("jdbc:jtds:sqlserver://192.168.0.29:1433/balance");
//		jdbc.setIp("192.168.0.29:1433");
//		jdbc.setDbName("balance");
		PrintObject("answer: " + jdbc.getUrlShortName());
		this.checkJdbcSortName(jdbc);
		
		jdbc = new DayJdbc();
		jdbc.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
		jdbc.setPassword("perfect");
		jdbc.setUsername("sa");
		jdbc.setUrl("jdbc:jtds:sqlserver://192.168.0.29:1433/balance?xx=xx");
//		jdbc.setIp("192.168.0.29:1433");
//		jdbc.setDbName("balance");
		PrintObject("answer: " + jdbc.getUrlShortName());
		this.checkJdbcSortName(jdbc);
	}
	public void testStream() {
		TaskFollower[] _follower=new TaskFollower[] {
				new TaskFollower("001","","159"),
				new TaskFollower("002","li@sell","")
		};
		String [] r=Arrays.asList(_follower).stream().map(a->a.getTelephone()).filter(a->!SGDataHelper.StringIsNullOrWhiteSpace(a)).toArray(String[]::new);
		PrintObject(r);
	}
	
	public void testIDCard() {
		List<Pair<String, String>> idCards=new ArrayList<Pair<String, String>>();
		//idCards.add(ImmutablePair.of("key", "value"));
		idCards.add(ImmutablePair.of("432901670230201", "value"));
		idCards.add(ImmutablePair.of("370822580418323", "value"));
		idCards.add(ImmutablePair.of("440702766726001", "value"));
		idCards.add(ImmutablePair.of("430523688061543", "value"));
		idCards.add(ImmutablePair.of("110104360371302", "value"));
		idCards.add(ImmutablePair.of("452601540646032", "value"));
		idCards.add(ImmutablePair.of("150204701174213", "value"));
		for(Pair<String, String> idCard : idCards){
			String r =SGDataHelper.ObjectToDateString(SGDataHelper.IDCardToBirthDay(idCard.getKey()));
			this.PrintObject(idCard.getKey()+"__"+r);
		}
		for(Pair<String, String> idCard : idCards){
			String r =String.valueOf(SGDate.Now().GetYear()-Integer.parseInt(SGDataHelper.identityCard15(idCard.getKey()).get("age").toString()));
			this.PrintObject(idCard.getKey()+"__"+r);
		}
		idCards.clear();
		idCards.add(ImmutablePair.of("442000197010141284", "value"));
		idCards.add(ImmutablePair.of("440620195311290547", "value"));
		idCards.add(ImmutablePair.of("440620196511143685", "value"));
		idCards.add(ImmutablePair.of("44062019620418054X", "value"));
		for(Pair<String, String> idCard : idCards){
			String r =SGDataHelper.ObjectToDateString(SGDataHelper.IDCardToBirthDay(idCard.getKey()));
			this.PrintObject(idCard.getKey()+"__"+r);
		}
		for(Pair<String, String> idCard : idCards){
			String r =String.valueOf(SGDate.Now().GetYear()-Integer.parseInt(SGDataHelper.identityCard18(idCard.getKey()).get("age").toString()));
			this.PrintObject(idCard.getKey()+"__"+r);
		}
		
	}
	
	public void testObjectToDateString() {
		String r =SGDataHelper.ObjectToDateString(new Long("1630621206000"));
		this.PrintObject(r);
	}

	public void testEqual() {
 		boolean b=SGDataHelper.IsPFValueEquals(new BigDecimal(0), 0);
 		assertTrue(b);
 		b=SGDataHelper.IsPFValueEquals(0,new BigDecimal(0));
 		assertTrue(b);
 		b=SGDataHelper.IsPFValueEquals(new BigDecimal(398), 398);
 		assertTrue(b);
 		b=SGDataHelper.IsPFValueEquals(new BigDecimal(1), 0);
 		assertTrue(!b);
 		b=SGDataHelper.IsPFValueEquals(new BigDecimal(1.00), new BigDecimal(1.0));
 		assertTrue(b);
 		b=SGDataHelper.IsPFValueEquals(new BigDecimal("2.00"), new BigDecimal("2.0"));
 		assertTrue(b);
 		b=SGDataHelper.IsPFValueEquals(0,new BigDecimal(1));
 		assertTrue(!b);
 		b=SGDataHelper.IsPFValueEquals( Long.valueOf(333), Long.valueOf(333));
 		assertTrue(b);
 		b=SGDataHelper.IsPFValueEquals(Long.valueOf(333), new BigDecimal("333.0"));
 		assertTrue(b);
 		b=SGDataHelper.IsPFValueEquals(2L, 2L);
 		assertTrue(b);
 		b=SGDataHelper.IsPFValueEquals(2L, 3L);
 		assertTrue(!b);
 		b=SGDataHelper.IsPFValueEquals(new BigDecimal("333.0"),Long.valueOf(333));
 		assertTrue(b);
		 b=SGDataHelper.IsPFValueEquals(1.00, 1.0);
		assertTrue(b);
 		 b=SGDataHelper.IsPFValueEquals("aa", "bb");
  		assertTrue(!b);
		 b=SGDataHelper.IsPFValueEquals("bb", "bb");
 		assertTrue(b);
 		String m=null;
 		String n="";
		 b=SGDataHelper.IsPFValueEquals(m, n);
		assertTrue(b);
 		 m="";
 		n=null;
		 b=SGDataHelper.IsPFValueEquals(m, n);
		assertTrue(b);
		 b=SGDataHelper.IsPFValueEquals(null, "bb");
		assertTrue(!b);
		 b=SGDataHelper.IsPFValueEquals( Integer.valueOf(3), Integer.valueOf(3));
		assertTrue(b);
		 b=SGDataHelper.IsPFValueEquals(Integer.valueOf(3), Integer.valueOf(4));
		assertTrue(!b);
		 b=SGDataHelper.IsPFValueEquals(Integer.valueOf(3), 3);
		assertTrue(b);
		int x=3;
		int y=3;
		 b=SGDataHelper.IsPFValueEquals(x, y);
		assertTrue(b);
		 x=3;
		 y=4;
		 b=SGDataHelper.IsPFValueEquals(x, y);
		assertTrue(!b);
		 b=SGDataHelper.IsPFValueEquals(null, null);
		assertTrue(b);
		 b=SGDataHelper.IsPFValueEquals(null, 1);
		assertTrue(!b);
		 b=SGDataHelper.IsPFValueEquals(new SGDate("2022-01-01 13:13:13").ToCalendar().getTime(),
				 new SGDate("2022-01-01 13:13:13").ToCalendar().getTime());
		assertTrue(b);
		 b=SGDataHelper.IsPFValueEquals(new SGDate("2022-01-01 13:13:13").ToCalendar().getTime(),
				 new SGDate("2022-01-02 13:13:13").ToCalendar().getTime());
		assertTrue(!b);

		 DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		 b=SGDataHelper.IsPFValueEquals(Timestamp.valueOf("2022-06-01 00:00:35.0"),
				 LocalDateTime.parse("2022-06-01 00:00:35", df));
		assertTrue(b);
	}
public void testGetOneAppArg() {
	try {
		SGDataHelper.setAppArg(new String[] {"--url=0"});
		//String r=PFDataHelper.<DayJdbc>getOneAppArg(DayJdbc.class,a->a.getUrl());
		String r=SGDataHelper.getOneAppArg(AppArgModel.class,a->a.url);
		assertTrue(r.equals("0"));
		String r2=SGDataHelper.getOneAppArg(AppArgModel.class,a->a.url);
		assertTrue(r2.equals("0"));
		String aa="aa";
	}catch (Exception e){
		LOGGER.error(e.toString());
	}
}
//interface MathOperation {
//    int operation(int a, int b);
// }
//	interface Function2<R, T1, T2> extends Serializable {
//	    R apply(T1 t1, T2 t2);
//	}
//	public void testGetOneAppArg2() {
//
//		try {
//		    Function2<String, Integer, Double> lambda = (a, b) -> a + "," + b;
//		    Method method = lambda.getClass().getDeclaredMethod("writeReplace");
//		    method.setAccessible(true);
//		    SerializedLambda serializedLambda = (SerializedLambda) method.invoke(lambda);
//		    System.out.println(serializedLambda);
//		} catch (Exception e) {
//		    throw new RuntimeException("获取Lambda信息失败", e);
//		}
//
//	}
//	interface MathOperation2<R, DayJdbc>  extends Serializable {
//		R apply(DayJdbc t1);
//	 }
//	public void testGetOneAppArg3() {
//
//		try {
//			MathOperation2<Object, DayJdbc> lambda = (a) -> a.url;
//		    Method method = lambda.getClass().getDeclaredMethod("writeReplace");
//		    method.setAccessible(true);
//		    SerializedLambda serializedLambda = (SerializedLambda) method.invoke(lambda);
//		    serializedLambda.getInstantiatedMethodType();
//		    System.out.println(serializedLambda);
//		} catch (Exception e) {
//		    throw new RuntimeException("获取Lambda信息失败", e);
//		}
//
//	}
	@SuppressWarnings("deprecation")
	public void testPFEnumClass() {
		assertFalse(PFSqlFieldType.Decimal.HasFlag(PFSqlFieldType.Int));
		assertTrue(PFSqlFieldType.Decimal.Or(PFSqlFieldType.Int).HasFlag(PFSqlFieldType.Int));
		assertTrue(PFSqlFieldType.Decimal.Or(PFSqlFieldType.Int).Or(PFSqlFieldType.String).HasFlag(PFSqlFieldType.Decimal));

		assertFalse(PFSqlFieldType.Decimal.Or(PFSqlFieldType.Int).Or(PFSqlFieldType.String).HasFlag(PFSqlFieldType.Bool));
	}
	public void testGetTimeSpan() {
		long diff=60000;
		SGTimeSpan ts = SGDataHelper.GetTimeSpan(diff,SGYmd.Hour | SGYmd.Minute | SGYmd.Second|SGYmd.Millisecond);
        //this.PrintObject(ts.toString());
        assertTrue("0时1分0秒0毫秒".equals(ts.toString()));
		 diff=1200;
		ts = SGDataHelper.GetTimeSpan(diff,SGYmd.Hour | SGYmd.Minute | SGYmd.Second|SGYmd.Millisecond);
        //this.PrintObject(ts.toString());
        assertTrue("0时0分1秒200毫秒".equals(ts.toString()));
	}
	public void testSpeedCounter() {
		SGDate beginTimeDate=new SGDate(SGDataHelper.StringToDateTime("2022-09-27 00:00:00"));
		SGDate endTimeDate=beginTimeDate.AddMinutes(1);
		
		long beginTime = beginTimeDate.ToCalendar().getTimeInMillis();
        long endTime =endTimeDate.ToCalendar().getTimeInMillis();
        int total=500000;

        this.PrintObject(
                SGDataHelper.FormatString(
                        "\r\nrows:{0}  --  " + "speed:{1} \r\n",
                        SGDataHelper.ScientificNotation(total),
                        SGDataHelper.ScientificNotation(
                                        Double.valueOf(total) * 1000 / (endTime - beginTime))
                                + "条/秒"
                        ));
        
        SGSpeedCounter speed=new SGSpeedCounter(beginTimeDate);
        this.PrintObject(speed.getSpeed(total, endTimeDate));
        assertTrue(speed.getSpeed(total, endTimeDate).indexOf("rows:5.00E+005 speed:8.33E+003条/秒 averageTime:0时20分0秒0毫秒/千万行 totalTime:0时1分0秒0毫秒")==0);
	}
	  public void testBool() {
		  new SGDataHelper(new PFAppConfig());
		  SGDataHelper.GetAppConfig().setSendSysMsg(null);
			if(SGDataHelper.GetAppConfig().getSendSysMsg()) {
				String aa="aa";
			}
	  }
	  public void testPFDate() {
		  SGDate date=new SGDate(2022,2,3,0,0,0);
		  this.PrintObject(date.toString());
		  date = date.GetYearStart();
		  this.PrintObject(date.toString());
		  assertTrue(date.equals(new SGDate(2022,1,1,0,0,0)));
	  }
	public void testPFDate2() throws Exception {
		SGDate date1=new SGDate(2023,03,02,14,36,16);
		SGDate date2=new SGDate(2023,03,02,14,31,00);
		if(date1.compareTo(date2)<=0){
			throw new Exception("比较错误");
		}
	}
//	private Timestamp longToTimestamp(long a) {
//		Calendar cal=Calendar.getInstance();
//		cal.setTimeInMillis((Long)a);
//		return new Timestamp(cal.getTimeInMillis());
//	}
//	private long TimestampTolong(Timestamp a) {
//		return a.getTime();
//	}
//	public void testConverter(){
//		//测试订单信息 SG023000210901000006  id:1270744013508607737
//		PFDate date=new PFDate(2021,9,1,1,25,50);//dbeaver中看到的时间,和里格系统时间一致
//		long v1=date.ToCalendar().getTimeInMillis();//1630722688000
//		long v2=1630722688000L;
//		long v3=1630751488000L;
//		this.PrintObject("v1");
//		this.PrintObject(v1);
//		this.PrintObject(v2);
//		this.PrintObject(v3);
//		//PFDataHelper.ObjectToLong(LOGGER);
//		
////		int dataT=93;		
////		String dataTName="datetime";
////		IPFSqlFieldTypeConverter ConvertTo =PFDataHelper.GetObjectToSqlTypeByPFTypeConverter(v1,
////				PFSqlFieldType.DateTime, dataT, dataTName);
////		Object convertedValue =ConvertTo.convert(v1);
////		
////
////		IPFSqlFieldTypeConverter ConvertTo2 =PFDataHelper.GetObjectToSqlTypeByPFTypeConverter(v2,
////				PFSqlFieldType.DateTime, dataT, dataTName);
////		Object convertedValue2 =ConvertTo.convert(v2);
//
//		this.PrintObject("result");
//		Object convertedValue1 =longToTimestamp(v1);
//		Object convertedValue2 =longToTimestamp(v2);
//		Object convertedValue3 =longToTimestamp(v3);
//		this.PrintObject(convertedValue1);
//		this.PrintObject(convertedValue2);
//		this.PrintObject(convertedValue3);
//		String aa="aa";
//		assertTrue(convertedValue1.equals(convertedValue2));
//	}
//	/**
//	 * flink消息里的timestamp大了8小时
//	 * 
//1630632135000
//1630660935000
//1630632135000
//1630660935000
//"2021-09-03 09:22:15"
//"2021-09-03 17:22:15"
//
//	 */
//	public void testConverter2(){
//		//测试订单信息 SG023000210901000006  id:1270744013508607737
//		//PFDate date=new PFDate(2021,9,1,10,59,46);//dbeaver中看到的时间,和里格系统时间一致
//		PFDate date=new PFDate(PFDataHelper.StringToDateTime("2021-09-03 09:22:15"));
//		long v1=date.ToCalendar().getTimeInMillis();//1630722688000
//		long v2=1630660935000L;
//		this.PrintObject(v1);
//		this.PrintObject(v2);
//		Object convertedValue1 =longToTimestamp(v1);
//		Object convertedValue2 =longToTimestamp(v2);
//		this.PrintObject(convertedValue1);
//		this.PrintObject(convertedValue2);
//
//		this.PrintObject((new PFDate((Timestamp)convertedValue1)).toString());
//		this.PrintObject((new PFDate((Timestamp)convertedValue2)).toString());
//		assertTrue(v1==v2);
//	}

	public void testCompareDate(){
		Calendar d1=new SGDate(2023,1,1,1,1,1).ToCalendar();
		Calendar d2=new SGDate(2023,3,1,1,1,1).ToCalendar();
		int r=SGDataHelper.compareDate(d1,d2,1);
		System.out.println(r);
		String s=SGDataHelper.ObjectToDateString(d1,SGDataHelper.DateFormat);
		System.out.println(s);
		assertTrue("2023-01-01 01:01:01".equals(s));
	}
	public void testSafe() throws Exception {
		if(!"".equals(UncheckLoadKey001.text)) {
			throw new Exception("文本未清理");
		}
	}
}
