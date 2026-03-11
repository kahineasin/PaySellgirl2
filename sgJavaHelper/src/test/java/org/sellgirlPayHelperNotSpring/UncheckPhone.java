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
//import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.sellgirlPayHelperNotSpring.model.*;
import org.sellgirlPayHelperNotSpring.model.HuaweiNewFilesFetcher2.FileInfo;
import org.sellgirlPayHelperNotSpring.model.HuaweiNewFilesFetcher2.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sellgirl.sgJavaHelper.*;
import com.sellgirl.sgJavaHelper.config.*;
import com.sellgirl.sgJavaHelper.config.SGDataHelper.LocalDataType;
import com.sellgirl.sgJavaHelper.file.SGDirectory;
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
import com.sellgirl.sgJavaHelper.time.SGTimeSpan;

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
public class UncheckPhone extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(UncheckPhone.class);
	public static AppKey appKey=null;
	public static void initPFHelper()  {
		try {
			SGDataHelper.SetConfigMapper(new PFConfigTestMapper());
			new SGDataHelper(new PFAppConfig());
//			File dumpFile = new File("D:\\gitee\\secretKey\\paySellgirl\\key.yml");
//			AppKey appKey = Yaml.loadType(dumpFile, AppKey.class);
			appKey = SGYamlHelper.loadType("D:\\gitee\\secretKey\\paySellgirl\\key.yml", AppKey.class);
			SGEmailSend.EMAIL_OWNER_ADDR_HOST="";
			SGEmailSend.EMAIL_OWNER_ADDR=AES.AESDecryptDemo(appKey.getEmail(),UncheckLoadKey001.getKey());
			SGEmailSend.EMAIL_OWNER_ADDR_PASS=AES.AESDecryptDemo(appKey.getEmailPwd(),UncheckLoadKey001.getKey());
			SGEmailSend.EMAIL_OWNER_ADDR_HOST_PROPERTY=HostType.SELLGIRL.getProperties();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 自动备份手机 自动整理手机文件(深度递归子目录)
	 * 待完成此方法(完成90%，基本可用)
	 * 
	 * 备份的关键有：
	 * 1.全手机的pdf文件
	 * 2.多个download目录和pictures文件夹
	 * 3.录像录音等
	 * 
	 */
//	@Deprecated
	//最后备份时间20250925
	public void testAutoBackupPhone() {
		try {
//			String[] path=new String[] {

//			};
//			String[] dstPath=new String[] {
//					"D:\\3\\split"
//			};
			String[] huaweiPath=new String[] {
					 "/sdcard/Pictures/"
//					"sdcard/tencent/QQ_Images"
//					"sdcard/Download/"
//					"sdcard/Android/data/com.tencent.mm/MicroMsg/Download/"
//					"sdcard/DCIM/Camera/"
//					"sdcard/Sounds/"
//					"sdcard/Documents/NotePad/"
			};

			String[] path=new String[] {
					"D:\\3\\src"
			};
			String[] dstPath=new String[] {
					"D:\\3\\split"
			};
			for(int i=0;path.length>i;i++) {
				HuaweiFileFetcher.main(new String[] {huaweiPath[i],path[i]});
				if(0==HuaweiFileFetcher.exitCode) {
					File tmp=new File(path[i]);
					SGDirectory.deleteOldFile(tmp,new SGDate(2025,9,24,1,0,0));//不处理旧文件					
//					com.sellgirl.sgJavaHelper.file.SGFileSplit.main(new String[] {path[i],dstPath[i]});
					
					//如果前面按时间只读新文件，下句没有也行了
					//HuaweiFileFetcherAndDelete.deleteHuaweiFile(huaweiPath[i],"*.*");
					
//					SGDirectory.cleanDirectory(path[i]);
					System.out.println("split success");
				}else {
					System.out.println("failed:"+HuaweiFileFetcher.exitCode);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//	private SGDate oldTime=new SGDate(2025,9,6,1,0,0);//今天20250925
	private SGDate oldTime=new SGDate(2025,9,25,1,0,0);//今天20251027
	public void testAutoBackupPhoneNewFile() {
		try {
//			String[] path=new String[] {

//			};
//			String[] dstPath=new String[] {
//					"D:\\3\\split"
//			};
			
//			String[] huaweiPath=new String[] {
////					 "/sdcard/Pictures/"
////					 "/sdcard/Pictures/WeiXin"
//					 "/sdcard/DCIM/Camera"
////					"sdcard/tencent/QQ_Images"
////					"sdcard/Download/"
////					"sdcard/Android/data/com.tencent.mm/MicroMsg/Download/"
////					"sdcard/Sounds/"//华为录音
////					"sdcard/Music/SoundRecording"
//			};
			String[] huaweiPath=getHuaweiImportmantFilePath();

//			String[] path=new String[] {
////					"D:\\3\\src"
////					"D:\\3\\src\\WeiXin"
//					"D:\\3\\src\\DCIM\\Camera"
////					"D:\\3\\src\\Sounds"
////					"D:\\3\\src\\Music\\SoundRecording"
//			};
//			String[] dstPath=new String[] {
//					"D:\\3\\split"
//			};
			for(int i=0;huaweiPath.length>i;i++) {	
				//HuaweiFileFetcher.main(new String[] {huaweiPath[i],path[i]});
				String pathI="D:\\3\\src\\"+huaweiPath[i];
//				HuaweiNewFilesFetcher2.main(new String[] {huaweiPath[i],path[i],oldTime.toString()});
				HuaweiNewFilesFetcher2.main(new String[] {huaweiPath[i],pathI,oldTime.toString()});
				if(FileStatus.NORMAL.ordinal()!=HuaweiNewFilesFetcher2.statusCode
						||null==HuaweiNewFilesFetcher2.pulled
						||FileStatus.NOFILE.ordinal()==HuaweiNewFilesFetcher2.statusCode
						) {continue;}
				for(FileInfo j:HuaweiNewFilesFetcher2.pulled) {
//					HuaweiFileFetcherAndDelete.deleteHuaweiFile(j.path,"*.*");
					if(!j.pullFailed) {
						HuaweiFileFetcherAndDelete.deleteHuaweiFile(j.path);
					}
				}
//				if(1==HuaweiNewFilesFetcher2.statusCode) {
//					com.sellgirl.sgJavaHelper.file.SGFileSplit.main(new String[] {path[i],dstPath[i]});
//				}
			}
			System.out.println("split success");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void testAutoSplit() {
		try {

			String[] path=new String[] {
					"D:\\3\\src"
			};
			String[] dstPath=new String[] {
					"D:\\3\\split"
			};
			for(int i=0;path.length>i;i++) {
				com.sellgirl.sgJavaHelper.file.SGFileSplit.main(new String[] {path[i],dstPath[i]});	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void testAutoMerge() {
		try {

			String[] path=new String[] {
					"D:\\3\\split"
			};
			String[] dstPath=new String[] {
					"D:\\3\\src"
			};
			for(int i=0;path.length>i;i++) {
				com.sellgirl.sgJavaHelper.file.SGFileMerge.main(new String[] {path[i],dstPath[i]});	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void testDeleteHuaweiFile() throws IOException, InterruptedException {		
//		String path="sdcard/Android/data/com.tencent.mm/MicroMsg/Download/";	
		String path="sdcard/Documents/NotePad/";
//		HuaweiFileFetcherAndDelete.deleteHuaweiFile(path,"*.xlsx");//无效
		HuaweiFileFetcherAndDelete.deleteHuaweiFile(path,"*.*");//无效，其实删除成功，但在电脑上看到缓存
//		HuaweiFileFetcherAndDelete.deleteHuaweiFile(path,"**.*");
	}
	public void testShowHuaweiFolderFileCount() throws IOException, InterruptedException {
		String[] huaweiPath=getHuaweiImportmantFilePath();
		for(int i=0;huaweiPath.length>i;i++) {
			//if()
            int totalItems = HuaweiFileCounter.countItemsWithLs(huaweiPath[i]);
            System.out.println("-------------" + huaweiPath[i]+"--------------");
            System.out.println("总项目数: " + totalItems);
		}

	}
	private String[] getHuaweiImportmantFilePath() {//					
		//"此电脑\\BENJAMIN's mobile\\内部存储\\Pictures",//这种目录读不了File.exists()==false
//		"此电脑\\BENJAMIN's mobile\\内部存储\\Download\\Browser"
//"此电脑\\BENJAMIN's mobile\\内部存储\\Download\\BaiduNetdisk\\来自：百度App"
//"此电脑\BENJAMIN's mobile\内部存储\Download\WeChat"
//"此电脑\BENJAMIN's mobile\内部存储\Download\QQ"
		//"此电脑\BENJAMIN's mobile\内部存储\Download\QuarkDownloads"
		//此电脑\BENJAMIN's mobile\内部存储\Documents\NotePad    //备忘录可以导出到这里(这个目录很奇怪的，用win10 explorer打开此路径是空的，但adb命令能读)
		return new String[] {
//				 "sdcard/Pictures/",//"/sdcard/Pictures/",//总图片目录需要小心操作，避免备份时没有递归，删除时递归了，那会丢数据
				 "sdcard/Pictures/WeiXin",//"/sdcard/Pictures/",
				"sdcard/tencent/QQ_Images/",
				"sdcard/Download/",
				"sdcard/Quark/Download/",
				"sdcard/Android/data/com.tencent.mm/MicroMsg/Download/",
//				"sdcard/Android/data/com.tencent.mobileqq/Tencent/QQfile_rec/",//感觉这里移动qq的文件不重要
				"sdcard/DCIM/Camera/",
				"sdcard/Sounds/",
				"sdcard/Documents/NotePad/",
				//-------原生安卓-----------//
				"sdcard/Music/SoundRecording"
		};
	}
}
