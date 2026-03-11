package org.sellgirlPayHelperNotSpring;

//import org.ho.yaml.Yaml;
//import org.ho.yaml.Yaml;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.prefs.Preferences;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.sellgirlPayHelperNotSpring.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sellgirl.sgJavaHelper.*;
import com.sellgirl.sgJavaHelper.antivirus.SGProcess;
import com.sellgirl.sgJavaHelper.antivirus.SGProcessHelper;
import com.sellgirl.sgJavaHelper.antivirus.SGProcessPcManager;
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
 * 
 * 测试杀毒软件技术
 * 
 * 不要随便更改此类名,以防打包时执行了此类
 * 
 * @author Administrator
 *
 */
@SuppressWarnings(value = { "unused", "rawtypes", "serial", "deprecation" })
public class UncheckAntivirus extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(UncheckAntivirus.class);

	public static void initPFHelper() {
		try {
			SGProcessHelper.setManager(new SGProcessPcManager());
//			SGDataHelper.SetConfigMapper(new PFConfigTestMapper());
//			new SGDataHelper(new PFAppConfig());
//			// File dumpFile = new File("D:\\gitee\\secretKey\\paySellgirl\\key.yml");
//			// AppKey appKey = Yaml.loadType(dumpFile, AppKey.class);
////			PFEmailSend.EMAIL_OWNER_ADDR_HOST="";
////			PFEmailSend.EMAIL_OWNER_ADDR="";
////			PFEmailSend.EMAIL_OWNER_ADDR_PASS=AES.AESDecryptDemo(appKey.getEmailPwd(),"");
////			PFEmailSend.EMAIL_OWNER_ADDR_HOST_PROPERTY=HostType.SELLGIRL.getProperties();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//
//	public void testFileSplit() throws IOException {
//
//		//源文件地址
//		//File sourceFile = new File("D:\\3\\sashaKnight.png");
//		File sourceFile = new File("D:\\3\\zip\\knightSasha_pc_exe.7z");
//		//块文件目录
//		String chunkFileFolder = "D:\\3\\zip\\split";
//		//块文件大小
//		//int chunkFileSize = 1 * 1024 * 1024;
//		int chunkFileSize = 9 * 1024 * 1024;//9MB
//		//块文件数量
//		int chunkFileNum = (int) Math.ceil(sourceFile.length() * 1.0 / chunkFileSize);
//		//
//		RandomAccessFile readFile = new RandomAccessFile(sourceFile, "r");
//		byte[] bytes = new byte[1024];
//		for(int i = 0; i < chunkFileNum; i++) {
//			File chunkFile = new File(chunkFileFolder  + "\\" + i);
//			int len = -1;
//			//创建块文件
//			RandomAccessFile writeFile = new RandomAccessFile(chunkFile, "rw");
//			while((len = readFile.read(bytes)) != -1) {
//				writeFile.write(bytes, 0 ,len);
//				//如果块文件大小达到1M，就读下一块
//				if(chunkFile.length() >= chunkFileSize) {
//					break;
//				}
//			}
//			writeFile.close();
//		}
//		readFile.close();
//	}
//	public void testFileSplit2() throws IOException {
//		SGFileSplit.fileSplit("D:\\3\\zip\\knightSasha_pc_exe.7z", "D:\\3\\zip\\split");
//		//SGFileSplit.fileSplit("D:\\3\\src", "D:\\3\\split");
//	}
//	public void testFileMerge() throws IOException {
//        //块文件目录
////        String chunkFileFolderPath = "D:\\3\\split";
//        String chunkFileFolderPath = "D:\\3\\zip\\split";
//        //获取块文件对象
//        File chunkFileFolder = new File(chunkFileFolderPath);
//        //块文件列表
//        File[] files = chunkFileFolder.listFiles();
//        //将文件排序
//        List<File> filesList = Arrays.asList(files);
//        Collections.sort(filesList, (o1, o2) -> {
//            if(Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())){
//                return 1;
//            }
//            return -1;
//        });
//        //合并后的文件
//        File mergerFile = new File("D:\\3\\zip\\file_merger.psd");
//        //创建新文件
//        boolean newFile = mergerFile.createNewFile();
//        //创建写对象
//        RandomAccessFile writeFile = new RandomAccessFile(mergerFile, "rw");
// 
//        byte[] b = new byte[1024];
//        for(File chunkFile : filesList){
//            int len = -1;
//            //创建读块文件的对象
//            RandomAccessFile readFile = new RandomAccessFile(chunkFile, "r");
//            while((len = readFile.read(b)) != -1){
//                writeFile.write(b, 0, len);
//            }
//            readFile.close();
//        }
//        writeFile.close();
//	}
	public void testTaskList() throws Exception {
		Process process=Runtime.getRuntime().exec("tasklist");
		BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		while((line=reader.readLine())!=null) {
			System.out.println(line);
		}
		reader.close();
	}

	public void testTaskList2() throws Exception {
		ProcessBuilder builder=new ProcessBuilder("tasklist");
		Process process=builder.start();
		BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;

//        List<String> processIDList = new ArrayList<>();
        List<SGProcess> processIDList = new ArrayList<>();
		while((line=reader.readLine())!=null) {
			System.out.println(line);
			
            String[] parts = line.split("\\s+");
//            if (parts.length > 0) {
//                processIDList.add(parts[0]);
//            }

//            if (parts.length > 1) {
//                processIDList.add(parts[1]);
//            }

            if (parts.length > 1) {
            	SGProcess item=new SGProcess();
            	item.setName(parts[0]);
            	item.setId(parts[1]);
            	item.setCpu(parts[2]);
            	item.setMem(parts[3]);
            	processIDList.add(item);
            }
		}
		reader.close();
		
        // 打印所有进程ID
//        for (String pid : processIDList) {
//            System.out.println(pid);
//        }

        for (SGProcess pid : processIDList) {
            System.out.println(pid.getName()+"---------"+pid.getId()+"---------"
            		+pid.getCpu()+"---------"+pid.getMem());
        }
	}
	public List<SGProcess> getTasks() throws Exception {
		return SGProcessHelper.getManger().getProcesses();
//		ProcessBuilder builder=new ProcessBuilder("tasklist");
//		Process process=builder.start();
//		BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream()));
//		String line;
//
////        List<String> processIDList = new ArrayList<>();
//        List<SGProcess> processIDList = new ArrayList<>();
//		while((line=reader.readLine())!=null) {
//			//System.out.println(line);
//			
//            String[] parts = line.split("\\s+");
////            if (parts.length > 0) {
////                processIDList.add(parts[0]);
////            }
//
////            if (parts.length > 1) {
////                processIDList.add(parts[1]);
////            }
//
//            if (parts.length > 1) {
//            	if(!parts[0].endsWith(".exe")) {
//            		continue;
//            	}
//            	SGProcess item=new SGProcess();
//            	item.setName(parts[0]);
//            	item.setId(parts[1]);
//            	item.setCpu(parts[2]);
//            	item.setMem(parts[3]);
//            	processIDList.add(item);
//            }
//		}
//		reader.close();
//		
////        // 打印所有进程ID
//////        for (String pid : processIDList) {
//////            System.out.println(pid);
//////        }
////
////        for (SGProcess pid : processIDList) {
////            System.out.println(pid.getName()+"---------"+pid.getId()+"---------"
////            		+pid.getCpu()+"---------"+pid.getMem());
////        }
//		return processIDList;
	}

	public void testTaskList3() throws Exception {

	}
	public void testTaskStop() throws Exception {
		//SGDataHelper.ShutdownSpringBootForWindowsSystemByPID("457300");
		SGProcessHelper.getManger().stopProcess("457300");
	}
	
	public void testSaveNormalTask() throws IOException {
		ProcessBuilder builder=new ProcessBuilder("tasklist");
		Process process=builder.start();
		BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;

//        List<String> processIDList = new ArrayList<>();
        List<SGProcess> processIDList = new ArrayList<>();
		while((line=reader.readLine())!=null) {
			//System.out.println(line);
			
            String[] parts = line.split("\\s+");
//            if (parts.length > 0) {
//                processIDList.add(parts[0]);
//            }

//            if (parts.length > 1) {
//                processIDList.add(parts[1]);
//            }

            if (parts.length > 1) {
            	if(!parts[0].endsWith(".exe")) {
            		continue;
            	}
            	SGProcess item=new SGProcess();
            	item.setName(parts[0]);
            	item.setId(parts[1]);
            	item.setCpu(parts[2]);
            	item.setMem(parts[3]);
            	processIDList.add(item);
            }
		}
		reader.close();
		
        // 打印所有进程ID
//        for (String pid : processIDList) {
//            System.out.println(pid);
//        }

        List<String> lines = new ArrayList<>();
        for (SGProcess pid : processIDList) {
//            System.out.println(pid.getName()+"---------"+pid.getId()+"---------"
//            		+pid.getCpu()+"---------"+pid.getMem());
            lines.add(pid.getName()+","+pid.getId()+","
            		+pid.getCpu()+","+pid.getMem());
        }

		SGDataHelper.WriteLinesToFile(lines, file);
	}
	private String file="d:\\download\\test1.txt";
	public void testBadTask() throws Exception {
		UncheckAntivirus.initPFHelper();
//		List<String> lines=SGDataHelper.ReadFileToLines(file);
//		List<String> names=new ArrayList<String>();
//		for(String line:lines) {
//			names.add(line.split(",")[0]);
//		}
//
//
//
////        List<String> processIDList = new ArrayList<>();
//        List<SGProcess> processIDList = getTasks();
//        List<SGProcess> badList = new ArrayList<>();
//		for(SGProcess item:processIDList) {
//
//        	boolean isGood=false;
//        	for(String good:names) {
//        		if(good.equals(item.getName())) {
//        			isGood=true;
//        			break;
//        		}
//        	}
//        	if(!isGood) {
//        		badList.add(item);
//        	}
//		}
        List<SGProcess> badList = SGProcessHelper.getManger().getBadProcesses();
		if(badList.isEmpty()) {
			System.out.println("no bad process");
		}else {
			System.out.println("has bad process");
	        for (SGProcess pid : badList) {
	            System.out.println(pid.getName()+"---------"+pid.getId()+"---------"
	            		+pid.getCpu()+"---------"+pid.getMem());
	//            lines.add(pid.getName()+","+pid.getId()+","
	//            		+pid.getCpu()+","+pid.getMem());
	        }
        }

		//SGDataHelper.WriteLinesToFile(lines, file);
	}

	public void testEnvir() throws Exception {
		Preferences root =Preferences.systemRoot();
		String value=root.node("SOFTWARE\\Microsoft\\Windows\\CurrentVersion").get("ProgramFilesDir",null);
//		String value=root.node("HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion").get("ProgramFilesDir",null);

		System.out.println(value);
		
//		root.flush();
//		root.sync();
	}

}
