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
import com.sellgirl.sgJavaHelper.file.SGDirectorySplit;
import com.sellgirl.sgJavaHelper.file.SGFileMerge;
import com.sellgirl.sgJavaHelper.file.SGFileSplit;
import com.sellgirl.sgJavaHelper.file.SGPath;
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
public class UncheckFile extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(UncheckFile.class);

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

	public void testFileSplit() throws IOException {

		//源文件地址
		//File sourceFile = new File("D:\\3\\sashaKnight.png");
		File sourceFile = new File("D:\\3\\zip\\knightSasha_pc_exe.7z");
		//块文件目录
		String chunkFileFolder = "D:\\3\\zip\\split";
		//块文件大小
		//int chunkFileSize = 1 * 1024 * 1024;
		int chunkFileSize = 9 * 1024 * 1024;//9MB
		//块文件数量
		int chunkFileNum = (int) Math.ceil(sourceFile.length() * 1.0 / chunkFileSize);
		//
		RandomAccessFile readFile = new RandomAccessFile(sourceFile, "r");
		byte[] bytes = new byte[1024];
		for(int i = 0; i < chunkFileNum; i++) {
			File chunkFile = new File(chunkFileFolder  + "\\" + i);
			int len = -1;
			//创建块文件
			RandomAccessFile writeFile = new RandomAccessFile(chunkFile, "rw");
			while((len = readFile.read(bytes)) != -1) {
				writeFile.write(bytes, 0 ,len);
				//如果块文件大小达到1M，就读下一块
				if(chunkFile.length() >= chunkFileSize) {
					break;
				}
			}
			writeFile.close();
		}
		readFile.close();
	}
	public void testFileSplit2() throws IOException {
		SGFileSplit.fileSplit("D:\\3\\zip\\knightSasha_pc_exe.7z", "D:\\3\\zip\\split");
		//SGFileSplit.fileSplit("D:\\3\\src", "D:\\3\\split");
	}
	public void testFileSplitForGibhub() throws IOException {
		String sourceFilePath="D:\\3\\src";
		String chunkFileFolder="D:\\3\\split";
		File sourceFile = new File(sourceFilePath);
		int mb=9;
		long size=1024l*1024l*9;
		for(File f:sourceFile.listFiles()) {
			if(f.isFile()) {
				if(size<f.length()) {
					SGFileSplit.doFileSplit(f,Paths.get(chunkFileFolder,SGPath.GetFileNameWithoutExtension(f)).toString(),mb);	
				}else {
					File dstFile = new File(Paths.get(chunkFileFolder,f.getName()).toString());
					SGPath.copyFile(f, dstFile);
//					java.nio.file.Files.copy(null, null)
				}
			}else if(f.isDirectory()) {
				//SGFileSplit.doSplitFromFolder(f,Paths.get(chunkFileFolder,f.getName()).toString());
			}
		}
	}
	public void testFileMerge() throws IOException {
        //块文件目录
//        String chunkFileFolderPath = "D:\\3\\split";
        String chunkFileFolderPath = "D:\\3\\zip\\split";
        //获取块文件对象
        File chunkFileFolder = new File(chunkFileFolderPath);
        //块文件列表
        File[] files = chunkFileFolder.listFiles();
        //将文件排序
        List<File> filesList = Arrays.asList(files);
        Collections.sort(filesList, (o1, o2) -> {
            if(Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())){
                return 1;
            }
            return -1;
        });
        //合并后的文件
        File mergerFile = new File("D:\\3\\zip\\file_merger.psd");
        //创建新文件
        boolean newFile = mergerFile.createNewFile();
        //创建写对象
        RandomAccessFile writeFile = new RandomAccessFile(mergerFile, "rw");
 
        byte[] b = new byte[1024];
        for(File chunkFile : filesList){
            int len = -1;
            //创建读块文件的对象
            RandomAccessFile readFile = new RandomAccessFile(chunkFile, "r");
            while((len = readFile.read(b)) != -1){
                writeFile.write(b, 0, len);
            }
            readFile.close();
        }
        writeFile.close();
	}
	public void testFileMerge2() throws Exception {
		SGFileMerge.main(new String[]{"D:\\3\\zip\\split", "D:\\3\\zip\\file_merger.psd"});
//		SGFileMerge.main(new String[]{"D:\\3\\split", "D:\\3\\merge"});
	}

	public void testDirectorySplit() throws NumberFormatException, Exception {
		SGDirectorySplit.main(new String[] {"D:\\3\\src\\Screenshots2","D:\\3\\split","199"});
	}
}
