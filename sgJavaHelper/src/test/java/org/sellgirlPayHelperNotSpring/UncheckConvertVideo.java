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
import com.sellgirl.sgJavaHelper.file.SGPath;
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
import com.sellgirl.sgJavaHelper.video.FLVToMKVConverter.PlayDevice;
import com.sellgirl.sgJavaHelper.video.VideoConverter;

import junit.framework.TestCase;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
//import org.ho.yaml.Yaml;
import org.sellgirlPayHelperNotSpring.model.AppKey;
import org.sellgirlPayHelperNotSpring.model.ByteUtil;
import org.sellgirlPayHelperNotSpring.model.DataModelInfo;
import org.sellgirlPayHelperNotSpring.model.DefaultListDataSource;
import org.sellgirlPayHelperNotSpring.model.DefaultListDataSource2;
//import org.sellgirlPayHelperNotSpring.model.FLVToMKVConverter;
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
//import org.sellgirlPayHelperNotSpring.model.FLVToMKVConverter.PlayDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
public class UncheckConvertVideo extends TestCase {
	/**
	 * * 如果想加载密码可以参考下面这样.
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public void testFlvToSonyTV() throws NumberFormatException, Exception {

		Uncheck001.initPFHelper();
		


////		 imgPath="D:\\a_video\\erika\\一公升的眼泪";
////		 imgPath2="D:\\a_video\\erika\\out";
//
//		 imgPath="D:\\a_video\\erika\\一公升的眼泪 1リットルの涙 2005.rmvb";
//		 imgPath2=null;
//		 
//		 boolean hasDstPath=null!=imgPath2;
//		 PlayDevice device= PlayDevice.SonyTV;
//	        String fmt=FLVToMKVConverter.getFormat(device);
//		 
//		File srcFile = new File(imgPath); //
//
//		System.out.println("convert start at "+SGDate.Now());
//		int cnt=0;
//        SGSpeedCounter speed2=new SGSpeedCounter();
//        speed2.setBeginTime(SGDate.Now());
//        
//        
//		if(srcFile.isFile()) {
//			String[] arr=SGPath.splitPath(imgPath);
////			File encFile = new File(Paths.get(arr[0],arr[1]+"_01"+arr[2]).toUri()); //
////			SGEncryptByte.EncFile(srcFile, encFile,Integer.decode(arg[1]));
//			 
//			imgPath2=Paths.get(arr[0],arr[1]+"_01"+fmt).toString();
//			FLVToMKVConverter.convert(imgPath, imgPath2, device);
//
//		}else if(srcFile.isDirectory()) {
//			if(hasDstPath) {
//				SGDirectory.EnsureExists(imgPath2);
//			}
//			int batch=0;
//			int maxBatch=4;
//			for(File f:srcFile.listFiles()) {
//				if(f.isFile()) {
//					speed2.setBeginTime(SGDate.Now());
//					
//					String[] arr=SGPath.splitPath(f.getAbsolutePath());
////					File encFile = new File(Paths.get(arg.length>2?arg[2]:arr[0],arr[1]+(arg.length>2?"":"_01")+arr[2]).toUri()); // 
////					SGEncryptByte.EncFile(f, encFile,Integer.decode(arg[1]));
//
//					String tmpImgPath2=Paths.get(hasDstPath?imgPath2:arr[0],arr[1]+(hasDstPath?"":"_01")+fmt).toString();
//					FLVToMKVConverter.convert(f.getPath(), tmpImgPath2, device);
//					
//					speed2.setEndTime(SGDate.Now());
//					cnt++;
//					System.out.printf(""+speed2.getEnSpeed2(cnt)+" \r\n");
//					
//					//多线程方式，似乎和ffmpeg内部的多线程冲突
////					batch++;
////					if(batch>=maxBatch) {
////						Process process=FLVToMKVConverter.start();
////						System.out.printf(""+speed2.getEnSpeed2(cnt)+" \r\n");
////						batch=0;
////					}
//				}
//			}
//			
//		}
//		//Process process=FLVToMKVConverter.start();
//		
//		System.out.println("convert finish");
        

		 imgPath="D:\\a_video\\erika\\一公升的眼泪 1リットルの涙 2005.rmvb";
		 imgPath2=null;
		 
		 PlayDevice device= PlayDevice.SonyTV;
		 VideoConverter.main(new String[]{
				 imgPath,
				 String.valueOf(device.ordinal()),				 
				 imgPath2
		 });
	}
	private static String imgPath="";
	 private static String imgPath2="";
}
