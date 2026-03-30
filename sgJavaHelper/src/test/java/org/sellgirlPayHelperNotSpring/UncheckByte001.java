package org.sellgirlPayHelperNotSpring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sellgirl.sgJavaHelper.DirectNode;
import com.sellgirl.sgJavaHelper.IPFSqlFieldTypeConverter;
import com.sellgirl.sgJavaHelper.SGAction;
import com.sellgirl.sgJavaHelper.SGByteHelper;
import com.sellgirl.sgJavaHelper.PFBatchHelper;
import com.sellgirl.sgJavaHelper.SGDataTable;
import com.sellgirl.sgJavaHelper.PFEnumClass;
import com.sellgirl.sgJavaHelper.PFHiveSqlCreateTableCollection;
import com.sellgirl.sgJavaHelper.PFKeyValueCollection;
import com.sellgirl.sgJavaHelper.SGLine;
import com.sellgirl.sgJavaHelper.PFModelConfig;
import com.sellgirl.sgJavaHelper.PFModelConfigCollection;
import com.sellgirl.sgJavaHelper.PFPoint;
import com.sellgirl.sgJavaHelper.SGRequestResult;
import com.sellgirl.sgJavaHelper.SGSpeedCounter;
import com.sellgirl.sgJavaHelper.SGSqlCommandString;
import com.sellgirl.sgJavaHelper.PFSqlCommandTimeoutSecond;
import com.sellgirl.sgJavaHelper.SGSqlFieldInfo;
import com.sellgirl.sgJavaHelper.PFSqlFieldType;
import com.sellgirl.sgJavaHelper.SGSqlFieldTypeEnum;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGDateRange;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.SuccessApiResult;
import com.sellgirl.sgJavaHelper.config.PFAppConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.config.SGDataHelper.LocalDataType;
import com.sellgirl.sgJavaHelper.file.SGDirectory;
import com.sellgirl.sgJavaHelper.model.FileSizeUnitType;
import com.sellgirl.sgJavaHelper.model.SysType;
import com.sellgirl.sgJavaHelper.model.UserOrg;
import com.sellgirl.sgJavaHelper.model.UserTypeClass;
import com.sellgirl.sgJavaHelper.sgEnum.IPFEnum;
import com.sellgirl.sgJavaHelper.sgEnum.SGFlagEnum;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlCreateTableCollection;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlInsertCollection;
import com.sellgirl.sgJavaHelper.sql.SGSqlTransferItem;
import com.sellgirl.sgJavaHelper.sql.PFTidbSqlExecute;
import com.sellgirl.sgJavaHelper.sql.SqlCreateTableItem;
import com.sellgirl.sgJavaHelper.sql.SqlUpdateItem;
import com.sellgirl.sgJavaHelper.task.IPFTask;
import com.sellgirl.sgJavaHelper.task.PFDayTask;
import com.sellgirl.sgJavaHelper.task.PFDayTask2;
import com.sellgirl.sgJavaHelper.task.PFIntervalExactTask;
import com.sellgirl.sgJavaHelper.task.PFIntervalExactTask2;
import com.sellgirl.sgJavaHelper.task.PFIntervalTask;
import com.sellgirl.sgJavaHelper.task.PFMonthTask;
import com.sellgirl.sgJavaHelper.task.PFMonthTask2;
import com.sellgirl.sgJavaHelper.task.PFTimeTaskBase2.TimePoint;
import com.sellgirl.sgJavaHelper.time.SGTimeSpan;

import junit.framework.TestCase;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.sellgirlPayHelperNotSpring.model.ByteUtil;
import org.sellgirlPayHelperNotSpring.model.DataModelInfo;
import org.sellgirlPayHelperNotSpring.model.DefaultListDataSource;
import org.sellgirlPayHelperNotSpring.model.DefaultListDataSource2;
import org.sellgirlPayHelperNotSpring.model.HealthOrdersApiResult;
import org.sellgirlPayHelperNotSpring.model.JdbcHelperTest;
import org.sellgirlPayHelperNotSpring.model.PFConfigTestMapper;
import org.sellgirlPayHelperNotSpring.model.SellGirlProduct;
import org.sellgirlPayHelperNotSpring.model.TaskTest001;
import org.sellgirlPayHelperNotSpring.model.TaskTest002;
import org.sellgirlPayHelperNotSpring.model.TestEnum001;
import org.sellgirlPayHelperNotSpring.model.TestSerialModel;
import org.sellgirlPayHelperNotSpring.model.TestSerialModel2;
import org.sellgirlPayHelperNotSpring.model.TestSerialModel3;
import org.sellgirlPayHelperNotSpring.model.TestSerialModel4;
import org.sellgirlPayHelperNotSpring.model.TestSerialModel5;
import org.sellgirlPayHelperNotSpring.model.TransferTaskTest001;
import org.sellgirlPayHelperNotSpring.model.TransferTaskTest002;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

/**
 * 分析byte特征的测试类
 * 
 * 结合学习文档byte.txt和printByte()的相关测试来看，byte的特点就很清楚了
 * 
 * @author Administrator
 *
 */
@SuppressWarnings(value = { "unused", "rawtypes", "serial", "deprecation" })
public class UncheckByte001 extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(UncheckByte001.class);

	public static void initPFHelper() {
		SGDataHelper.SetConfigMapper(new PFConfigTestMapper());
		new SGDataHelper(new PFAppConfig());
	}
	public void testByteIntToString() {
//		System.out.println(SGByteHelper.byteIntToString(new int[] {101,44}));//e,
//		System.out.println(SGByteHelper.byteIntToString(new int[] {44}));//,
//		System.out.println(SGByteHelper.byteIntToString(new int[] {-5, -32, 64, 0, 0, 15, -4, 0, 75, -128, 0, 0, 9, -100, -128, 9, 112, 0, 0,1, 10, -128, 1, 46, 0, 0, 0, 32, 0, 0, 37, -64, 0, 0, 4}));//
//		System.out.println(SGByteHelper.byteIntToString(new int[] {-24,-80,-94,-27,-82,-71,-27,-124,-65}));//
//		System.out.println(SGByteHelper.byteIntToString(new int[] {8213,8214,8215,8216,8217,8218,8219,8220}));
//		System.out.println(SGByteHelper.byteIntToString(new int[] {8216}));
//		System.out.println(SGByteHelper.byteIntToString2(new int[] {101,44}));//01
//		System.out.println(SGByteHelper.byteIntToString2(new int[] {1,9,9,3},10));//1993
//		System.out.println(SGByteHelper.byteIntToString2(new int[] {1,15,11,10},16));//1fba
//		for(int i=0;i<=127;i++) {
//			System.out.println(Character.forDigit(i, 10));//	
//		}
//		System.out.println(SGByteHelper.byteIntToString2(new int[] {8216}));//0
//		System.out.println(SGByteHelper.byteIntToString3(new int[] {101,44}));//乱码
//		System.out.println(SGByteHelper.byteIntToString3(new int[] {8216}));//乱码
//		System.out.println(SGByteHelper.byteIntToString4(new int[] {101,44}));//
//		System.out.println(SGByteHelper.byteIntToString4(new int[] {8216}));//
		
//		System.out.println(new String(SGByteHelper.intToByteArray(8217)));
//		
//		char ch = 8216; // 这是单引号的ASCII码
//		String str = Character.toString(ch);
//		 
//		// 如果你想要确保是中文单引号
//		str = str.equals("'") ? "\u2018" : str;
//		 
//		System.out.println(str); // 输出中文单引号
		
		/**
Z
[
\
]
^
_
`
a
		 */
//		for(int i=90;97>=i;i++) {
//			System.out.println(SGByteHelper.byteIntToString3(new int[] {i}));
//		}

		/**
		 * 乱码
		 */
//		for(int i=127;150>=i;i++) {
//			System.out.println(SGByteHelper.byteIntToString3(new int[] {i}));
//		}
//		for(int i=-95;i<=-77;i++) {
//
//			System.out.println(SGByteHelper.byteIntToString(new int[] {-30,-106,i}));
//		}
		for(int i=-130;i<=-100;i++) {

			System.out.println(SGByteHelper.byteIntToString(new int[] {-30,-105,i}));
		}
	}
	public void testStringToByteInt() {
		System.out.println(SGByteHelper.stringToByteInt("	"));//9
		System.out.println(SGByteHelper.stringToByteInt(" "));//32
		System.out.println(SGByteHelper.stringToByteInt("谢容儿"));//-24,-80,-94,-27,-82,-71,-27,-124,-65
		System.out.println(SGByteHelper.stringToByteInt("TAG"));//84,65,71
		System.out.println(SGByteHelper.stringToByteInt("tag"));//116,97,103
		System.out.println(SGByteHelper.stringToByteInt("谢"));//-24,-80,-94
		System.out.println(SGByteHelper.stringToByteInt("’"));//-30,-128,-103
		System.out.println(SGByteHelper.stringToByteInt("'"));//39
		System.out.println(SGByteHelper.stringToByteInt("△"));//-30,-106,-77
		System.out.println(SGByteHelper.stringToByteInt("○"));//-30,-105,-117
		System.out.println(SGByteHelper.stringToByteInt("□"));//-30,-106,-95
		System.out.println(SGByteHelper.stringToByteInt("✕"));//-30,-100,-107
		System.out.println(SGByteHelper.stringToByteInt("✖"));//-30,-100,-106
		System.out.println(SGByteHelper.stringToByteInt("〖"));//-29,-128,-106
		System.out.println(SGByteHelper.stringToByteInt("〗"));//-29,-128,-105
		System.out.println(SGByteHelper.stringToByteInt("‖"));//-30,-128,-106
		
	}
	public void testDataLong(){
		long a=Long.MAX_VALUE;
		System.out.println(Long.toBinaryString(a).length());  //63
	}

	public void testByteEqual() {
		byte a=3;
		byte b='3';
		int c=b;
		byte d=51;
		byte e='b';
		byte f=98;
		byte g='\t';
		int h=9;
		char i='\t';
		System.out.println(a==b);
		System.out.println(a);
		System.out.println("----b----");
		System.out.println(b);
		System.out.println(new String(new byte[] {b}));
		System.out.println(new String(new byte[] {3}));
		System.out.println("----new String(new byte[] {'3'})----");
		System.out.println(new String(new byte[] {'3'}));
		System.out.println("----new String(new byte[] {51})----");
		System.out.println(new String(new byte[] {51}));
		System.out.println(c);
		System.out.println(b==d);
		System.out.println("----d----");
		System.out.println(d);

		System.out.println("----new String(new byte[] {'9'})----");
		System.out.println(new String(new byte[] {'9'}));
		System.out.println("----new String(new byte[] {'b'})----");
		System.out.println(new String(new byte[] {'b'}));
		System.out.println("----byte e='b'----");
		System.out.println(e);
		System.out.println("----e==f----");
		System.out.println(e==f);
		System.out.println(Integer.toHexString(b));
		System.out.println(Integer.toHexString(d));
		System.out.println(g==h);
		System.out.println(i==h);
	}
	public void testFileByeInt() {
		//谢容儿的GB18030编码: -48,-69,-56,-35,-74,-7
		System.out.println(SGByteHelper.byteToIntLine(SGByteHelper.readFileToByte(new File("D:\\cache\\encode\\gbk_1.txt"))));
		//谢容儿的utf8编码: -24,-80,-94,-27,-82,-71,-27,-124,-65
		System.out.println(SGByteHelper.byteToIntLine(SGByteHelper.readFileToByte(new File("D:\\cache\\encode\\utf8_1.txt"))));
	}
	public void testPrintByte2() {
//		byte b='9';
//		printByte(b);
		//byte最大128，2的5次方
		for(int i=127;i<=130;i++) {
//			byte c=i;
			printByte((byte)i);
		}
	}
	public void testUtf8Bom() {
		//带bom的utf8文件的前几个字符为[-17, -69, -65, 91, 116, 105, 58, -24,
		//似乎[-17, -69, -65, 91]这4个字符是组成了"["符号
		//其实91就是[, 所以前面的-17, -69, -65应该是文件头
		//这3个byte的Integer.toHexString值分别是ffffffef,ffffffbb,ffffffbf,和百度说的文件头EF BB BF对应上了
		System.out.println(new String(new byte[] {-17,-69,-65,91}));
		byte b='[';
		int c=b;
		System.out.println(c);
		printByte(b);
		printByte((byte)-17);
		printByte((byte)-69);
		printByte((byte)-65);
	}
	public void printByte(byte b) {
		int c=b;
//		System.out.println(a==b);
//		System.out.println(a);
		System.out.println("--------------------------------");
		System.out.println("----byte b=3----");
		System.out.println(b);
		System.out.println("----int c=b----");
		System.out.println(c);
		System.out.println("----3=='3'----");
		System.out.println(b==c);
		System.out.println("----new String(new byte[] {3}----");
		System.out.println(new String(new byte[] {b}));
		System.out.println("----Integer.toHexString(3)----");
		System.out.println(Integer.toHexString(b));

//		System.out.println("----byte b='3'----");
//		System.out.println(new String(new byte[] {(byte) c}));
//		System.out.println("----int c=b----");
//		System.out.println(c);
//		System.out.println(b);
//		System.out.println(new String(new byte[] {3}));
//		System.out.println("----new String(new byte[] {'3'})----");
//		System.out.println(new String(new byte[] {'3'}));
//		System.out.println("----new String(new byte[] {51})----");
//		System.out.println(new String(new byte[] {51}));
//		System.out.println(c);
//		System.out.println(b==d);
//		System.out.println("----d----");
//		System.out.println(d);
//
//		System.out.println("----new String(new byte[] {'9'})----");
//		System.out.println(new String(new byte[] {'9'}));
//		System.out.println("----new String(new byte[] {'b'})----");
//		System.out.println(new String(new byte[] {'b'}));
//		System.out.println("----byte e='b'----");
//		System.out.println(e);
//		System.out.println("----e==f----");
//		System.out.println(e==f);
//		System.out.println(Integer.toHexString(b));
	}
	public void testByte() throws Exception {
		byte b='3';
		System.out.println(b);  //51  应该是字符的编码？对
		byte b2=5;
		System.out.println(b2);  //5
		byte b3='5';
		System.out.println(b3);  //53
		byte b4=127;
		System.out.println(b4);  //127
		//byte b5=128;  //最大127，编译不通过
		byte b6=100;
		System.out.println(b6);  //100
		byte b7=0x7F;
		System.out.println(b7);  //127
		long b8=0x80;
		System.out.println(b8);  //128
		long b9=0x101111111L;
		System.out.println(b9);  //4312862993
		long b10=0x8088;
		System.out.println(b10);  //32904

	}
	public void testPrintByte() throws Exception {
		byte b='3';
		System.out.println(Integer.toBinaryString(b));  //110011
		byte b2=5;
		System.out.println(Integer.toBinaryString(b2));  //101
		byte b3='5';
		System.out.println(Integer.toBinaryString(b3));  //110101
		byte b4=127;
		System.out.println(Integer.toBinaryString(b4));  //1111111
		//byte b5=128;  //最大127，编译不通过
		byte b6=100;
		System.out.println(Integer.toBinaryString(b6));  //1100100
		byte b7=0x7F;//127(10进制)
		System.out.println(Integer.toBinaryString(b7));  //1111111
		long b8=0x80;
		System.out.println(Long.toBinaryString(b8));  //10000000
		long b9=0x101111111L;
		System.out.println(Long.toBinaryString(b9));  //100000001000100010001000100010001
		long b10=0x8088;
		System.out.println(Long.toBinaryString(b10));  //1000000010001000
		long b11=200L;
		System.out.println(Long.toBinaryString(b11));  //11001000
		byte b12=-111;
		//下面两行不一致是正确的,因为二进制的负数的补码算法是和位数有关的
		System.out.println(Long.toBinaryString(b12));  //1111111111111111111111111111111111111111111111111111111110010001
		System.out.println(Integer.toBinaryString(b12));  //11111111111111111111111110010001
		byte b13=111;
		System.out.println(Long.toBinaryString(b13));  //1101111
		System.out.println(Integer.toBinaryString(b13));//1101111
		byte b14=20;
		System.out.println(Integer.toBinaryString(b14));  //10100
		//long b15=Long.valueOf(9*Math.pow(10,18));
		long b15=Long.MAX_VALUE;
		System.out.println(Long.toBinaryString(b15));  //

	}
	public void testByteToInt() throws Exception {
////		byte a=-111;
//		byte a=3;
//		int b=a;
//		System.out.println(b);  //
		
		byte[] bs=new byte[] {53,90};
		System.out.println(SGByteHelper.byteToIntLine(bs));
		System.out.println(new String(bs));
		System.out.println(bs);
	}
	public void testByteToLong() throws Exception {
		byte a=-111;
		long b=a;
		System.out.println(b);  //
	}

	/**
	 * 16进制
	 * @throws Exception
	 */
	public void testHexadecimal() throws Exception {
		byte b='3';
		System.out.println(Integer.toHexString(b));  //33
		byte b2=5;
		System.out.println(Integer.toHexString(b2));  //5
		byte b3='5';
		System.out.println(Integer.toHexString(b3));  //35
		byte b4=127;
		System.out.println(Integer.toHexString(b4));  //7f
		//byte b5=128;  //最大127，编译不通过
		byte b6=100;
		System.out.println(Integer.toHexString(b6));  //64
		byte b7=0x7F;
		System.out.println(Integer.toHexString(b7));  //7f
	}
	public void testPrintHexadecimal() throws Exception {
		byte b='3';
		System.out.println(Integer.toHexString(b));  //33
		byte b2=5;
		System.out.println(Integer.toHexString(b2));  //5
		byte b3='5';
		System.out.println(Integer.toHexString(b3));  //35
		byte b4=127;
		System.out.println(Integer.toHexString(b4));  //7f
		//byte b5=128;  //最大127，编译不通过
		byte b6=100;
		System.out.println(Integer.toHexString(b6));  //64
		byte b7=0x7F;
		System.out.println(Integer.toHexString(b7));  //7f
		long b8=0x80;
		System.out.println(Long.toHexString(b8));  //80
		long b9=0x101111111L;
		System.out.println(Long.toHexString(b9));  //101111111
		long b10=0x8088;
		System.out.println(Long.toHexString(b10));  //8088
	}

	/**
	 * 二进制
	 * @throws Exception
	 */
	public void testBinary() throws Exception {
/**
 * 测试奇怪现象
 * long 17  | long 128 =long 145
 * byte 17 | 0x80 =byte -111?
 */
		long r1=17L|128L;
		System.out.println(r1);

		byte a2=17;
		a2|=0x80;
		System.out.println(a2);

		byte a3=17;
		int b3=a3|0x80;
		System.out.println(b3);
	}
	private long and(Object l,Object r){
		long ll=SGDataHelper.ObjectToLong0(l);
		long rr=SGDataHelper.ObjectToLong0(r);
		long result=ll&rr;
		System.out.println(
				Long.toBinaryString(ll)+" & "
				+Long.toBinaryString(rr)+" = "
				+Long.toBinaryString(result)
		);
		return result;
	}
	private long or(Object l,Object r){
		long ll=SGDataHelper.ObjectToLong0(l);
		long rr=SGDataHelper.ObjectToLong0(r);
		long result=ll|rr;
		System.out.println(
				Long.toBinaryString(ll)+" | "
						+Long.toBinaryString(rr)+" = "
						+Long.toBinaryString(result)
		);
		return result;
	}
	public void testBitwiseOperation() throws Exception {
		System.out.println(100L&10L);
		System.out.println(100L&0x7F);// 100 & 1111111   = 100
		System.out.println(100L|0x80);// 100 | 10000000  = 228
		System.out.println(200L|0x80);// 200
		System.out.println(300L|0x80);// 428
		System.out.println("---------------");
		and(100L,10L);//1100100 & 1010 = 0
		and(100L,0x7F);//1100100 & 1111111 = 1100100
		or(100L,0x80);//1100100 | 10000000 = 11100100
		or(200L,0x80);//11001000 | 10000000 = 11001000
		or(300L,0x80);//100101100 | 10000000 = 110101100
	}
	public void testWriteVarInt() throws Exception {
		//writeVarInt(100L);
		writeVarInt(200L);
//		System.out.println("------------");
		//writeVarInt(0x101111111L);
		//writeVarInt2(0x101111111L);
		//writeVarInt3(0x101111111L);
		//writeVarInt2(100);
		//writeVarInt2(200);
		//writeVarInt2(300);

	}

	/**
	 * 输出：
	 * 100
	 * 100
	 * ------
	 * @param x
	 * @throws IOException
	 */
	public void writeVarInt(long x) throws IOException {
		boolean printMiddle=true;
		System.out.println("x origin:");
		System.out.println(Long.toBinaryString(x));
		for (int i = 0; i < 9; i++) {
			System.out.println("-------- i = "+i+" --------");
			if(printMiddle){
				System.out.println("x befroe:");
				System.out.println(Long.toBinaryString(x));
				and(x,0x7F);
			}
			byte byt = (byte) (x & 0x7F);
			if(printMiddle){
				System.out.println("byt &:");
				System.out.println(Integer.toBinaryString(byt));  //
			}

			if (x > 0x7F) {
				if(printMiddle){
					or(byt,0x80);
				}
				byt |= 0x80;
				if(printMiddle){
					System.out.println("byt or:");
					System.out.println(Integer.toBinaryString(byt));  //
				}
			}
			System.out.println("byt:");
			String tmpS=Integer.toBinaryString(byt);
			System.out.println(tmpS);  //

			x >>= 7;
			//switcher.get().writeBinary(byt);
//			System.out.println(byt);
//			int tmp=byt;
//			System.out.println(tmp);
			if(printMiddle){
				System.out.println("x after:");
				System.out.println(Long.toBinaryString(x));  //
			}
			//System.out.println("------");

			if (x == 0) {
				return;
			}
		}
	}

	/**
	 * clickhouse原版的写法
	 *
	 * 原理
	 * 1. 9次循环,每次移7位,long类型的长度相当于63位2进制数,原理就是9*7=63
	 * 2.
	 * @param x
	 * @throws IOException
	 */
	public void writeVarInt2(long x) throws IOException {
		System.out.println("x origin:");
		System.out.println(x);
		System.out.println(Long.toBinaryString(x));
		//9次循环,每次移7位,long类型的长度相当于63位2进制数,原理就是9*7=63
		for (int i = 0; i < 9; i++) {

			System.out.println("i = "+i);
			byte byt = (byte) (x & 0x7F);

			//这个应该是负数补码的运算,未完成弄懂
			if (x > 0x7F) {
				byt |= 0x80;
			}

			x >>= 7;
			//switcher.get().writeBinary(byt);
			System.out.println(byt);
			System.out.println(Integer.toBinaryString(byt));  //
			System.out.println("------");

			if (x == 0) {
				return;
			}
		}
	}
	public void writeVarInt3(long x) throws IOException {
		System.out.println("x origin:");
		System.out.println(x);
		System.out.println(Long.toBinaryString(x));
		for (int i = 0; i < 9; i++) {
			byte byt = (byte) (x & 0x7F);

			if (x > 0x7F) {
				byt |= 0x80;
			}

			System.out.println(Long.toBinaryString(byt));  //

			x >>= 7;
			//switcher.get().writeBinary(byt);
			String tmpS=Long.toBinaryString(byt);
			System.out.println(byt);
			System.out.println(tmpS);  //
			System.out.println("------");

			if (x == 0) {
				return;
			}
		}
	}
	public void testCharType() {
/**
HalfChar
HalfChar
Chinese
HalfChar
FullChar
ChineseChar
 */
		System.out.println(SGDataHelper.GetCharTypeEnum('a'));
		System.out.println(SGDataHelper.GetCharTypeEnum('e'));
		System.out.println(SGDataHelper.GetCharTypeEnum('星'));
		System.out.println(SGDataHelper.GetCharTypeEnum('6'));
		System.out.println(SGDataHelper.GetCharTypeEnum('：'));
		System.out.println(SGDataHelper.GetCharTypeEnum('’'));
		System.out.println(SGDataHelper.GetCharTypeEnum('‘'));
		

//		System.out.println(SGByteHelper.stringToByteInt("〖"));//-29,-128,-106
//		System.out.println(SGByteHelper.stringToByteInt("〗"));//-29,-128,-105
//		System.out.println(SGByteHelper.stringToByteInt("‖"));//-30,-128,-106
		System.out.println(SGDataHelper.GetCharTypeEnum('〖'));
		System.out.println(SGDataHelper.GetCharTypeEnum('〗'));
		System.out.println(SGDataHelper.GetCharTypeEnum('‖'));
		
	}
	public void testCharToInt() {

		char[] c=new char[] {'’','「','」','〖','〗','‖'};
		for(char i:c) {
			System.out.println(i);
			System.out.println(SGByteHelper.charToInt(i));
			System.out.println(SGDataHelper.GetCharTypeEnum(i));
		}
		//return;
		
		
//		char a='e';
//		int b=a;
//		System.out.println(a);
//		System.out.println(b);
//		System.out.println(Integer.toHexString(b));
//		System.out.println(new String(new byte[] {-30,-128,-103}));
//
		//System.out.println(SGByteHelper.byteToIntLine(intToByteArray(8217)));//0,0,32,25  转换好像不对
		//System.out.println(SGByteHelper.byteArrayToInt(new byte[] {-30,-128,-103}));
//		System.out.println(SGByteHelper.stringToByteInt("’"));
//		System.out.println(SGByteHelper.stringToByteInt("「"));
//		System.out.println(SGByteHelper.stringToByteInt("」"));
//		int a=8217;
//		char b='’';
//		char c='‘';
//		int d=c;
//		System.out.println(a>=b&&a<=b);
//		System.out.println(d);
//		int chfrom = 0x4e00; // 中文:范围（0x4e00～0x9fff）转换成int（chfrom～chend）
//		 int chend = 0x9fff;
//		System.out.println(chfrom);
//		System.out.println(chend);
	} 
//	public void testCharToInt() {
//		char[] c=new char[] {'’','「','」'};
//		for(char i:c) {
//			System.out.println(SGByteHelper.charToInt(i));
//		}
//		return;
//	}
	public void testIntToChar() {
		for(int i=8214;8216>=i;i++) {
			System.out.println(i);
//			System.out.println(new String(new char[] {SGByteHelper.intToChar(i)}));
			System.out.println(SGByteHelper.intToChar(i));
		}
	}
	public void testFirstLetter(){
		String[] title=new String[] {"奥","北","杀","朱"};
		for(String i:title) {
			char c=SGDataHelper.getFirstLetter(i.charAt(0));
//			System.out.println("-------------------------");
			System.out.println(i+"->"+c);
//			String s=new String(new char[] {c});
//			System.out.println(s);	
		}
	}
}
