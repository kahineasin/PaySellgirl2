package org.sellgirlPayHelperNotSpring;

//import org.ho.yaml.Yaml;
//import org.ho.yaml.Yaml;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.sellgirlPayHelperNotSpring.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sellgirl.sgJavaHelper.*;
import com.sellgirl.sgJavaHelper.config.*;
import com.sellgirl.sgJavaHelper.config.SGDataHelper.LocalDataType;
import com.sellgirl.sgJavaHelper.model.FileSizeUnitType;
import com.sellgirl.sgJavaHelper.model.SysType;
import com.sellgirl.sgJavaHelper.model.UserOrg;
import com.sellgirl.sgJavaHelper.model.UserTypeClass;
import com.sellgirl.sgJavaHelper.sgEnum.IPFEnum;
import com.sellgirl.sgJavaHelper.sgEnum.SGFlagEnum;
import com.sellgirl.sgJavaHelper.sql.*;
import com.sellgirl.sgJavaHelper.task.IPFTask;
import com.sellgirl.sgJavaHelper.task.PFDayTask;
import com.sellgirl.sgJavaHelper.task.PFDayTask2;
import com.sellgirl.sgJavaHelper.task.PFIntervalExactTask;
import com.sellgirl.sgJavaHelper.task.PFIntervalExactTask2;
import com.sellgirl.sgJavaHelper.task.PFIntervalTask;
import com.sellgirl.sgJavaHelper.task.PFMonthTask;
import com.sellgirl.sgJavaHelper.task.PFMonthTask2;
import com.sellgirl.sgJavaHelper.task.PFTimeTaskBase2.TimePoint;

import junit.framework.TestCase;

//import test.java.pfHelper.model.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * 不要随便更改此类名,以防打包时执行了此类
 * 
 * @author Administrator
 *
 */
@SuppressWarnings(value = { "unused", "rawtypes", "serial", "deprecation" })
public class UncheckMath extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(UncheckMath.class);

	public static void initPFHelper() {
		try {
			SGDataHelper.SetConfigMapper(new PFConfigTestMapper());
			new SGDataHelper(new PFAppConfig());
			// File dumpFile = new File("D:\\gitee\\secretKey\\paySellgirl\\key.yml");
			// AppKey appKey = Yaml.loadType(dumpFile, AppKey.class);
//			PFEmailSend.EMAIL_OWNER_ADDR_HOST="";
//			PFEmailSend.EMAIL_OWNER_ADDR="";
//			PFEmailSend.EMAIL_OWNER_ADDR_PASS=AES.AESDecryptDemo(appKey.getEmailPwd(),"");
//			PFEmailSend.EMAIL_OWNER_ADDR_HOST_PROPERTY=HostType.SELLGIRL.getProperties();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testSqrt() {
		//a的b次方等于c,那么
		//c=pow(a,b)
		//

		System.out.println(""+Math.exp(2));
		System.out.println(""+Math.pow(2, -3));// =1/(2的3次方)
		System.out.println(""+Math.log10(100));//输出2. 因为10的2次方为100
//		System.out.println(""+Math.pow(1, -1));
		System.out.println(""+Math.pow(2, -2));
		System.out.println(""+Math.pow(2, -1));
		System.out.println(""+Math.pow(2, 0));
		System.out.println(""+Math.pow(2, 1));
		System.out.println(""+Math.pow(2, 2));
		System.out.println(""+Math.sqrt(2));
	}
	public void testSin() {
		double[] d0List = new double[] { 30d, 60d, 150d, -30d, -60d, 210d, 390d, 750d, 1110d };
		int multi=1000000;
		boolean print=false;
//		int multi = 1;
//		boolean print = true;
		double[] dList = new double[d0List.length * multi];
		for (int i = 0; i < multi; i++) {
			System.arraycopy(d0List, 0, dList, d0List.length * (i), d0List.length);
		}

		HashMap<String, Function<Double, Double>> funs = new LinkedHashMap<String, Function<Double, Double>>();
		funs.put("sin1", d -> doTestSin(d));
		funs.put("sin2", d -> sin2(d * Math.PI / 180d, 1e-14));
		funs.put("Math.sin", d -> Math.sin(d * Math.PI / 180d));
		funs.put("sin3", d -> SGDataHelper.sin(d * Math.PI / 180d, 1e-5));
		for (Map.Entry<String, Function<Double, Double>> fun : funs.entrySet()) {
			long l = SGDataHelper.CountTime(a -> {
				for (double d : dList) {
					double r = (double) fun.getValue().apply(d);
					if (print) {
						System.out.println(d + " >>> " + r);
					}
				}
			});
			System.out.println(fun.getKey() + " 耗时 " + l + " ms");
			System.out.println("-------------------");
		}
	}

	public double doTestSin(double d) {
		d = d % 360;
		if (d > 180) {
			d = d - 360;
		}
		;
//		return doDoTestSin(d,4);
		return doDoTestSin(d * Math.PI / 180d, 5);
	}

	public double doDoTestSin(double d, int level) {
		double p = 0;
		for (int i = 0, v = 1; i < level; i += 1, v += 2) {
			if (i % 2 == 0) {
				p += t(d, v);
			} else {
				p -= t(d, v);
			}
		}
		return p;
	}

	public double t(double d, int level) {
		return Math.pow(d, level) / factorial(level);
	}

	public double factorial(int n) {
		int v = 1;
		for (int i = 2; i <= n; i++) {
			v *= i;
		}
		return v;
	}

	public double doSin2(double d, double epsilon) {
		return sin2(d * Math.PI / 180d, epsilon);
	}

	public double sin2(double v, double epsilon) {
//		 double epsilon=1e-5;
		int i = 1;
		double tmp = 1;
		if (v < 0) {
			tmp *= -1;
			v *= -1;
		}
		v -= ((int) (v / (2 * Math.PI))) * (2 * Math.PI);
		if (v >= Math.PI) {
			v -= Math.PI;
			tmp *= -1;
		}
		if (v >= (Math.PI / 2)) {
			v = Math.PI - v;
		}
		tmp *= v;
		double ret = 0;
		while (tmp < -epsilon || tmp > epsilon) {
			ret += tmp;
			tmp *= (-1 * (v / (++i)) * (v / (++i)));
		}
		return ret;
	}

	public double doSin3(double d) {
		return Math.sin(d * Math.PI / 180d);
	}
	public void testArc() {
		Double d=Math.atan2(1, 1);
		System.out.println(d);//0.7 弧度
		 d=Math.atan2(2, 2);
		System.out.println(d);//0.7 弧度
		 d=Math.atan2(1, 0);
		System.out.println(d);//1.5
		
	}

	public void testMapXY() {
		float x=0;
		float y=0;
		for(x=0;x<4;x++) {
			for(y=0;y<4;y++) {
				SGDataHelper.getLog().print(SGDataHelper.mapXYOld(x,y));
			}	
		}
	}
	public void testMapXY2() {
		float x=0;
		float y=0;
		for(x=0;x<4;x++) {
			for(y=0;y<4;y++) {
				SGDataHelper.getLog().print(SGDataHelper.mapXY(x,y));
			}	
		}
	}
	public void testMapXYReverse() {
//		float[] xy=SGDataHelper.mapXYReverse(8);
//		SGDataHelper.getLog().print("z:"+7+" x:"+xy[0]+" y:"+xy[1]);
		for(int i=6;i<12;i++) {
			float[] xy=SGDataHelper.mapXYReverse(i);
			SGDataHelper.getLog().print("z:"+i+" x:"+xy[0]+" y:"+xy[1]);
		}
	}
	public void testMapXYZ() {
		float x=0;
		float y=0;
		float z=0;
	    HashMap<Float,Boolean> map=new HashMap<Float,Boolean>(); 
		for(x=0;x<4;x++) {
			for(y=0;y<4;y++) {
				for(z=0;z<4;z++) {
					float tmp=SGDataHelper.mapXYZ(x,y,z);
					SGDataHelper.getLog().print("x:"+x+" y:"+y+"z:"+z+" -----"+tmp);
					if(map.containsKey(tmp)) {
						SGDataHelper.getLog().print("error x:"+x+" y:"+y+"z:"+z+" -----"+tmp);
					}else {
						map.put(tmp, true);
					}
				}
			}	
		}
	}
	public void testMapXYZ2() {
		float x=0;
		float y=0;
		float z=0;
		SGDataHelper.getLog().print("x:"+x+" y:"+y+"z:"+z+" -----"+SGDataHelper.mapXYZ(x,y,z));
		x=0;
		y=1;
		z=0;
		SGDataHelper.getLog().print("x:"+x+" y:"+y+"z:"+z+" -----"+SGDataHelper.mapXYZ(x,y,z));
		 x=1;
		 y=1;
		 z=0;
		SGDataHelper.getLog().print("x:"+x+" y:"+y+"z:"+z+" -----"+SGDataHelper.mapXYZ(x,y,z));
		 x=0;
		 y=1;
		 z=1;
		SGDataHelper.getLog().print("x:"+x+" y:"+y+"z:"+z+" -----"+SGDataHelper.mapXYZ(x,y,z));
		 x=1;
		 y=1;
		 z=1;
		SGDataHelper.getLog().print("x:"+x+" y:"+y+"z:"+z+" -----"+SGDataHelper.mapXYZ(x,y,z));
		 x=0;
		 y=0;
		 z=1;
		SGDataHelper.getLog().print("x:"+x+" y:"+y+"z:"+z+" -----"+SGDataHelper.mapXYZ(x,y,z));
		 x=1;
		 y=0;
		 z=1;
		SGDataHelper.getLog().print("x:"+x+" y:"+y+"z:"+z+" -----"+SGDataHelper.mapXYZ(x,y,z));
		 x=1;
		 y=0;
		 z=0;
		SGDataHelper.getLog().print("x:"+x+" y:"+y+"z:"+z+" -----"+SGDataHelper.mapXYZ(x,y,z));
		 x=0;
		 y=2;
		 z=0;
		SGDataHelper.getLog().print("x:"+x+" y:"+y+"z:"+z+" -----"+SGDataHelper.mapXYZ(x,y,z));
		 x=0;
		 y=2;
		 z=1;
		SGDataHelper.getLog().print("x:"+x+" y:"+y+"z:"+z+" -----"+SGDataHelper.mapXYZ(x,y,z));
		 x=2;
		 y=0;
		 z=2;
		SGDataHelper.getLog().print("x:"+x+" y:"+y+"z:"+z+" -----"+SGDataHelper.mapXYZ(x,y,z));
	}

	/**
	 * 递增接近曲线，多彡于游戏
	 * 
	 * 方程大概是它的变形:
	 * y=-5/(x+1)+5
	 */
	public void testGraduallyApproaching() {
		float y=0;
		float x=0;
		float a=0.06f;
		float b=7f;//正值，为y趋近的最大值
		float c=1f;//当x=0时y的解,c应小于b
		//关键解(0,0)
		float[] y2=new float[] {0,1,2,3,4,5,6,7};
		/**
-------a:0.06,b:5.0---------
0.0
4.166667
11.111111
25.0
66.66667
-Infinity
3.9991994
		 */
		SGDataHelper.getLog().print("-------a:"+a+",b:"+b+"---------");
		for(float i:y2) {
			SGDataHelper.getLog().print(GraduallyApproachingYToX(a,b,c,i));	
		}
		SGDataHelper.getLog().print(GraduallyApproachingXToY(a,b,c,66.6f));//3.9
		/**
-------a:1.0E-4,b:5.0---------
0.0
2500.0
6666.6665
15000.0
40000.0
-Infinity
		 */
		a=0.0001f;
		SGDataHelper.getLog().print("-------a:"+a+",b:"+b+",c:"+c+"---------");
		for(float i:y2) {
			SGDataHelper.getLog().print(GraduallyApproachingYToX(a,b,c,i));	
		}

		a=0.00001f;
		SGDataHelper.getLog().print("-------a:"+a+",b:"+b+",c:"+c+"---------");
		for(float i:y2) {
			SGDataHelper.getLog().print(GraduallyApproachingYToX(a,b,c,i));	
		}

		a=0.000001f;
		SGDataHelper.getLog().print("-------a:"+a+",b:"+b+",c:"+c+"---------");
		for(float i:y2) {
			SGDataHelper.getLog().print(GraduallyApproachingYToX(a,b,c,i));	
		}
	}
	public float GraduallyApproachingYToX(float a,float b,float c,float y) {
		return (((-b+c)/(y-b))-1)/a;
	}
	public float GraduallyApproachingXToY(float a,float b,float c,float x) {
//		return ((-b+c)/(a*x+1))+b;
		return (float)SGDataHelper.graduallyApproachingXToY(a,b,c,x);
	}
}
