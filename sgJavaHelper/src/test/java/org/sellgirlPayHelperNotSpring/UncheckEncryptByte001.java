package org.sellgirlPayHelperNotSpring;

//import org.ho.yaml.Yaml;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sellgirl.sgJavaHelper.AES;
import com.sellgirl.sgJavaHelper.DirectNode;
import com.sellgirl.sgJavaHelper.EncryptByte;
import com.sellgirl.sgJavaHelper.IPFSqlFieldTypeConverter;
import com.sellgirl.sgJavaHelper.SGAction;
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
import com.sellgirl.sgJavaHelper.file.SGDecryptByte;
import com.sellgirl.sgJavaHelper.file.SGDirectory;
import com.sellgirl.sgJavaHelper.file.SGEncryptByte;
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
//import org.ho.yaml.Yaml;
import org.sellgirlPayHelperNotSpring.model.AppKey;
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
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
 * 不要随便更改此类名,以防打包时执行了此类
 * 
 * @author Administrator
 *
 */
@SuppressWarnings(value = { "unused", "rawtypes", "serial", "deprecation" })
public class UncheckEncryptByte001 extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(UncheckEncryptByte001.class);
	private static final int numOfEncAndDec = 0x123456;
	private static final String numOfEncAndDecS = "0x123456";
	/**
	 * * 如果想加载密码可以参考下面这样.
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public void testEncrypt() throws NumberFormatException, Exception {
//		System.out.println(0x123456);//1193046
//		System.out.println( Integer.decode("0x123456"));//
//		System.out.println( Integer.decode("1193046"));//
		Uncheck001.initPFHelper();

//		File srcFile = new File("d:\\2\\sashaKnight.png"); // 
//
//		File encFile = new File("d:\\2\\sashaKnight1.png"); // 
//
//		File decFile = new File("d:\\2\\sashaKnight2.png"); // 

//		File srcFile = new File("d:\\2\\sasha_cross.jpg"); // 
//		File srcFile = new File("d:\\2\\sasha_cross_1000h.jpg"); // 
//
//		File encFile = new File("d:\\2\\sasha_cross1.jpg"); // 
//
//		File decFile = new File("d:\\2\\sasha_cross2.jpg"); // 
//		File srcFile = new File("d:\\2\\sasha_tentacle_1000h.jpg"); // 
//
//		File encFile = new File("d:\\2\\sasha_cross1.jpg"); // 
//
//		File decFile = new File("d:\\2\\sasha_cross2.jpg"); // 
		
//		File srcFile = new File("d:\\3\\man1.png"); //
//
//		File encFile = new File("d:\\3\\1.png"); // 
//
		//File decFile = new File("d:\\3\\2.png"); // 

//		File srcFile = new File("d:\\3\\sashaKnight.png"); // 
//
//		File encFile = new File("d:\\3\\sashaKnight1.png"); // 
//
//		File decFile = new File("d:\\3\\sashaKnight2.png"); // 
		
//		File srcFile = new File("d:\\github\\1\\2\\a.txt"); // 
//
//		File encFile = new File("d:\\github\\1\\2\\b.txt"); // 
//
//		File decFile = new File("d:\\github\\1\\2\\c.txt"); // 

//		try {
//
//			SGEncryptByte.EncFile(srcFile, encFile,numOfEncAndDec); // 
//			SGEncryptByte.DecFile(encFile, decFile,numOfEncAndDec); // 
////			EncFile(srcFile, encFile); // 加密操作
////			DecFile(encFile, decFile); // 
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		}
		
		//SGEncryptByte.main(new String[] {"d:\\2\\sasha_tentacle_1000h.jpg","0x123456"});
		
		//test OK

//		String imgPath="d:\\2\\sashaKnight.png";
//		String imgPath2="d:\\2\\sashaKnight_01.png";		

//		 imgPath="d:\\2\\35085c3e9154631d.gif";
//		 imgPath2="d:\\2\\35085c3e9154631d_01.gif";
//		SGEncryptByte.main(new String[] {imgPath,numOfEncAndDecS});
//		SGDecryptByte.main(new String[] {imgPath2,numOfEncAndDecS});

		SGEncryptByte.main(new String[] {"D:\\3\\src","0x123456","D:\\3\\enc"});
	}
	private static String imgPath="";
	 private static String imgPath2="";

	private static int dataOfFile = 0; // 文件字节内容

	private static void EncFile(File srcFile, File encFile) throws Exception {

		if (!srcFile.exists()) {

			System.out.println("source file not exixt");

			return;

		}

		if (!encFile.exists()) {

			System.out.println("encrypt file created");

			encFile.createNewFile();

		}

		FileInputStream fis = new FileInputStream(srcFile);

		FileOutputStream fos = new FileOutputStream(encFile);

		// int dataOfFile=0;
		//int b=0;

		while ((dataOfFile = fis.read()) > -1) {

//			fos.write(0==b?dataOfFile ^ numOfEncAndDec:dataOfFile);
//			b++;
//			if(b>3) {b=0;}

			fos.write(dataOfFile ^ numOfEncAndDec);
		}

		fis.close();

		fos.flush();

		fos.close();

	}

	private static void DecFile(File encFile, File decFile) throws Exception {

		if (!encFile.exists()) {

			System.out.println("encrypt file not exixt");

			return;

		}

		if (!decFile.exists()) {

			System.out.println("decrypt file created");

			decFile.createNewFile();

		}

		FileInputStream fis = new FileInputStream(encFile);

		FileOutputStream fos = new FileOutputStream(decFile);

//int dataOfFile=0;

		int b=0;
		while ((dataOfFile = fis.read()) > -1) {

			fos.write(0==b?dataOfFile ^ numOfEncAndDec:dataOfFile);
			b++;
			if(b>3) {b=0;}

		}

		fis.close();

		fos.flush();

		fos.close();

	}

	public static void copyStream (InputStream input, OutputStream output, byte[] buffer) throws IOException {
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}
	

    public void testDecryptByteToString() throws IOException {

    	File encFile = new File("D:\\2\\font_cn_01.txt");
    	//File encFile = new File("D:\\2\\font_cn2_01.txt");
		FileInputStream fis = new FileInputStream(encFile);
		System.out.println(SGEncryptByte.DecryptByteToString(fis, SGEncryptByte.DEFAULT_BUFFER_SIZE, 0x123456));

    }
//    @Deprecated
//    public void testDecryptByte() throws IOException {
//
//		File srcFile = new File("D:\\2\\font_cn_01.txt"); //
//		File encFile = new File("D:\\2\\font_cn_01_02.txt"); // 
//
//		try {
//			SGEncryptByte.DecFile(srcFile, encFile,0x123456);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//    }
    public void testDecrypt() throws IOException {

		File srcFile = new File("D:\\3\\enc"); //
		//File encFile = new File("D:\\3\\sex"); // 

		try {
			SGEncryptByte.doDecFromFolder(srcFile, "D:\\3\\src",0x123456);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    private final String k="0x123456";
    //加密同时分块，便于上传敏感图片到云盘
	public void testEncryptAndSplit() throws NumberFormatException, Exception {
		Uncheck001.initPFHelper();
		String srcPath="D:\\3\\src";
		String encPath="D:\\3\\enc";
		String splitPath="D:\\3\\split";
		SGEncryptByte.main(new String[] {srcPath,k,encPath});
		com.sellgirl.sgJavaHelper.file.SGFileSplit.main(new String[] {encPath,splitPath});
	}
	public void testMergeAndDecrypt() throws NumberFormatException, Exception {
		Uncheck001.initPFHelper();
		String srcPath="D:\\3\\src";
		String encPath="D:\\3\\enc";
		String splitPath="D:\\3\\split";
		com.sellgirl.sgJavaHelper.file.SGFileMerge.main(new String[] {splitPath,encPath});
		com.sellgirl.sgJavaHelper.file.SGDecryptByte.main(new String[] {encPath,k,srcPath});
	}

	public void testMergeAndDecrypt2() throws NumberFormatException, Exception {
		Uncheck001.initPFHelper();
		String srcPath="D:\\3\\src";
		String encPath="D:\\3\\enc";
//		String splitPath="D:\\3\\split";
//		com.sellgirl.sgJavaHelper.SGFileMerge.main(new String[] {splitPath,encPath});
		com.sellgirl.sgJavaHelper.file.SGDecryptByte.main(new String[] {encPath,k,srcPath});
	}
}
