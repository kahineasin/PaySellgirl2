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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
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
 * 比对netstat的记录，把关注的ip的访问时间记录下来
 */
@SuppressWarnings(value = { "unused", "rawtypes", "serial", "deprecation" })
public class UncheckCheckIp extends TestCase {

	//
	public void testAutoCheckIp() throws IOException, InterruptedException {
		//netstat -ano
		ProcessBuilder processBuilder = new ProcessBuilder(
	            );
	            setWorkDir(processBuilder);
	            processBuilder.redirectErrorStream(true);
//	            processBuilder.command(
//	"cmd.exe","/C",
//	                    "ffmpeg.exe",
//	                    "-i", inputFilePath,
//	                    "-c:v","libx264", 
//	                    "-crf","23",
//	                    "-preset","medium",//Option not found
//	                    outputFilePath);

	            processBuilder.command(
                    		"cmd.exe","/C",
	"netstat -ano");
	            Process process = processBuilder.start();
	         // 获取命令输出结果
	            InputStream inputStream = process.getInputStream();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)); // 设置编码为GBK
	            String line;
	            List<String> visitIps=new ArrayList<String>();
	            while ((line = reader.readLine()) != null) {
	                //System.out.println(line);
	                visitIps.add(line);
	            }
	            process.waitFor();
	            
		List<String> lines= SGDataHelper.ReadFileToLines("D:\\cache\\1\\watchIp.txt");
		List<String> catchIps=new ArrayList<String>();
		for(String i:lines) {
//            System.out.println(i);
            for(String j:visitIps) {
            	if(-1<j.indexOf(i)) {
            		System.out.println(String.format("检测到以下ip于时间(%s)访问了本机:\r\n%s",SGDate.Now().toString(),j));
            		catchIps.add(String.format("检测到以下ip于时间(%s)访问了本机:\r\n协议  本地地址          外部地址        状态           PID\r\n%s",SGDate.Now().toString(),j)+"\r\n");

            	}
            }
		}
		SGDataHelper.WriteLinesToFile(catchIps, "D:\\cache\\1\\catchIp.txt",true);
		
	}
	private static void setWorkDir(ProcessBuilder processBuilder) {
        processBuilder.directory(new File("D:\\cache\\1\\"));
        Map<String, String> env = processBuilder.environment();
        env.put("LANG", "zh_CN.UTF-8");
	 }

}
