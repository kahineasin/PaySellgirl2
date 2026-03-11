package org.sellgirlPayHelperNotSpring.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 从华为手机读取新文件，实测可行
 * 
 * 但存在问题：
 * 1.当文件名有中文时报错
 * 比如adb pull 20250809_170538妈说华旅游不带她.m4a d:/3/src 报错Illegal byte sequence
 * 
 * 对于读新数据，的必要性：
 * 1.由于一些不重要的旧数据，可能留在手机中偶尔看，但不需要备份。，所以只对新数据检查是否重要文件就足够了
 * 
 * @deprecated 中文文件名无法读，无解。（后来在纯安卓上发现，中文也可以，但文件名太长就不行
 */
@Deprecated
public class HuaweiNewFilesFetcher2 {

//    // 目标手机目录
//    private static  String PHONE_SOURCE_DIR = "/sdcard/DCIM/Camera/";
//    private static  String PHONE_SOURCE_DIR = "sdcard/Sounds/";
    private static  String PHONE_SOURCE_DIR = "sdcard/Sounds/CallRecord";
//    // 目标电脑目录
////    private static final String LOCAL_SAVE_DIR = "C:/Temp/HuaweiPhotos/";
    private static  String LOCAL_SAVE_DIR = "D:/3/src/";
    // 时间格式
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    private static  SimpleDateFormat FIND_DATE_FORMAT = null;
    public static int statusCode=-1;
    public enum FileStatus{
    	NORMAL,ERROR,NOFILE
    }
    private static boolean debug=false;
    public static void main(String[] args) {
        try {
//        	statusCode=-1;
            statusCode=FileStatus.NORMAL.ordinal();//0;
        	PHONE_SOURCE_DIR=args[0];
        	LOCAL_SAVE_DIR=args[1];
        	
            // 1. 设置时间点（这里设置为2024-01-01 00:00:00之后）
//            Date targetDate = DATE_FORMAT.parse("2024-01-01 00:00:00");
//            Date targetDate = DATE_FORMAT.parse("2025-08-22 01:00:00");
//            Date targetDate = DATE_FORMAT.parse("2025-08-04 01:00:00");
            Date targetDate = DATE_FORMAT.parse(args[2]);
            
            
            // 3. 获取所有文件列表
            List<FileInfo> allFiles = listAllFiles();
            if(statusCode!=FileStatus.NORMAL.ordinal()) {
            	return;
            }
            
            // 4. 筛选指定时间后的文件
            List<FileInfo> newFiles = filterFilesByDate(allFiles, targetDate);
            
            if (newFiles.isEmpty()) {
            	if(debug) {
            		System.out.println("没有找到指定时间点后的新文件。");
            	}
            	statusCode=FileStatus.NOFILE.ordinal();
                return;
            }
            
            System.out.println("找到 " + newFiles.size() + " 个新文件:");
            for (FileInfo file : newFiles) {
                System.out.println(" - " + file.name + " (" + DATE_FORMAT.format(file.modifiedTime) + ")");
            }

            // 2. 确保本地保存目录存在
            File localDir = new File(LOCAL_SAVE_DIR);
//            File localDir = new File(args[1]);
            if (!localDir.exists()) {
                localDir.mkdirs();
            }
            
            // 5. 拉取找到的文件
            pullFiles(newFiles);
            System.out.println("status:"+statusCode);
//            if(0==statusCode) {
//            	
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 文件信息类
     */
    public static class FileInfo {
    	public String path;
    	public String name;
    	public Date modifiedTime;
    	public boolean pullFailed=false;
        
        FileInfo(String path, String name, Date modifiedTime) {
            this.path = path;
            this.name = name;
            this.modifiedTime = modifiedTime;
        }
    }
    
    /**
     * 列出目录中的所有文件
     */
    private static List<FileInfo> listAllFiles() throws IOException, InterruptedException {
        List<FileInfo> fileList = new ArrayList<>();
        
        // 使用ls命令列出文件详细信息
        String[] lsCommand = {
            "adb", "shell", "ls", "-l", PHONE_SOURCE_DIR
        };
        
        Process process = Runtime.getRuntime().exec(lsCommand);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        
        String line;
        while ((line = reader.readLine()) != null) {
            // 跳过非文件行（如总用量行）
            if (line.startsWith("total") || line.trim().isEmpty()) {
                continue;
            }
            
            // 解析ls -l输出
            FileInfo fileInfo = parseLsOutput(line);
            if (fileInfo != null) {
                fileList.add(fileInfo);
            }
        }
        
        // 检查错误
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        StringBuilder errorOutput = new StringBuilder();
        while ((line = errorReader.readLine()) != null) {
            errorOutput.append(line).append("\n");
        }
        
        int exitCode = process.waitFor();
        if (exitCode != FileStatus.NORMAL.ordinal()) {
            System.err.println("列出文件时出错: " + errorOutput.toString());
        }
        
        return fileList;
    }
    
    /**
     * 解析ls -l命令的输出行
     */
    private static FileInfo parseLsOutput(String line) {
        try {
            // 分割行，但保留文件名中的空格
            String[] parts = line.split("\\s+");
            if (parts.length < 6) {
                return null; // 无效行
            }
            
            // 检查是否是文件（以"-"开头）
            if (!parts[0].startsWith("-")) {
                return null; // 不是常规文件（可能是目录或链接）
            }
            
            // 日期和时间部分（位置可能因系统而异）
            String dateStr;
            String timeStr;
            
            // 尝试不同的日期格式
            if (parts[5].contains("-")) {
                // 格式: 2024-01-15
                dateStr = parts[5];
                timeStr = parts[6];
            } else {
                // 格式: Jan 15
                dateStr = parts[5] + " " + parts[6];
                
                // 判断第7部分是年份还是时间
                if (parts[7].contains(":")) {
                    timeStr = parts[7];
                    // 添加当前年份
                    dateStr += " " + new SimpleDateFormat("yyyy").format(new Date());
                } else {
                    // 第7部分是年份
                    dateStr += " " + parts[7];
                    timeStr = parts[8];
                }
            }
            
            // 解析日期和时间
            Date modifiedTime=null;
//            if(null==FIND_DATE_FORMAT) {
//	            try {
//	                // 尝试解析标准格式
//	                modifiedTime = DATE_FORMAT.parse(dateStr + " " + timeStr);
//	                FIND_DATE_FORMAT=DATE_FORMAT;
//	            } catch (Exception e) {
//	            }
//	            if(null==FIND_DATE_FORMAT) {
//		            try {
//	                // 尝试解析其他格式
//	                SimpleDateFormat altFormat = new SimpleDateFormat("MMM dd yyyy HH:mm");
//	                modifiedTime = altFormat.parse(dateStr + " " + timeStr);
//	                FIND_DATE_FORMAT=DATE_FORMAT;
//		            } catch (Exception e) {
//		            }
//	            }
//	            if(null==FIND_DATE_FORMAT) {
//		            try {
//	                // 尝试解析其他格式
////	                SimpleDateFormat altFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
////	                modifiedTime = altFormat.parse(dateStr + " " + timeStr);
//	                modifiedTime = DATE_FORMAT.parse(dateStr + " " + timeStr+":00");
//	                
//	                FIND_DATE_FORMAT=DATE_FORMAT;
//		            } catch (Exception e) {
//		            }
//	            }
//            }else {
//                modifiedTime = FIND_DATE_FORMAT.parse(dateStr + " " + timeStr);
//            }
            modifiedTime = DATE_FORMAT.parse(dateStr + " " + timeStr+":00");
            
            // 获取文件名（可能是最后一个部分或多个部分）
            StringBuilder fileName = new StringBuilder();
            int startIndex = line.indexOf(parts[6]) + parts[6].length() + 1;
            if (startIndex < line.length()) {
                fileName.append(line.substring(startIndex).trim());
            } else {
                // 备用方法：使用最后一个部分作为文件名
                fileName.append(parts[parts.length - 1]);
            }
            
            char x=PHONE_SOURCE_DIR.charAt(PHONE_SOURCE_DIR.length()-1);
            String sp='/'==x?"":"/";
            return new FileInfo(
//                PHONE_SOURCE_DIR + fileName.toString()
//                Path.of(PHONE_SOURCE_DIR,fileName.toString()).toString()
            		PHONE_SOURCE_DIR+sp + fileName.toString()
                ,
                fileName.toString(),
                modifiedTime
            );
            
        } catch (Exception e) {
            System.err.println("解析行失败: " + line + " - " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 筛选指定时间后的文件
     */
    private static List<FileInfo> filterFilesByDate(List<FileInfo> files, Date targetDate) {
        List<FileInfo> result = new ArrayList<>();
        
        for (FileInfo file : files) {
            if (file.modifiedTime != null && !file.modifiedTime.before(targetDate)) {
                result.add(file);
            }
        }
        
        return result;
    }
    
    public static List<FileInfo> pulled=null;
    
    /**
     * 拉取文件列表中的文件
     */
    private static void pullFiles(List<FileInfo> files) throws IOException, InterruptedException {
        int successCount = 0;
        pulled=new ArrayList<FileInfo>();
        
        for (FileInfo file : files) {
        	
        	//方法1 当文件名有中文，报错Illegal byte sequence
            // 构建拉取命令
            String[] pullCommand = {"adb", "pull", file.path, LOCAL_SAVE_DIR};
            
            Process process = Runtime.getRuntime().exec(pullCommand);
            
//            //方法2
//ProcessBuilder processBuilder = new ProcessBuilder(
//            );
//            setWorkDir(processBuilder);
//            processBuilder.redirectErrorStream(true);
//            
//            processBuilder.command(
////"cmd.exe","/C",
//					"adb",
//                    "pull", 
//                    file.path, LOCAL_SAVE_DIR);

//            processBuilder.command(
////"cmd.exe","/C",
//    "adb","shell","/C",
//    "cp",file.path,"sdcard/Sounds/tmp.m4a","/C",
//    "exit","/C",
//					"adb",
//                    "pull", 
//                    "sdcard/Sounds/tmp.m4a", LOCAL_SAVE_DIR);
            
//            Process process = processBuilder.start();
        	
            // 读取输出
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(),StandardCharsets.UTF_8));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            // 读取错误
//            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(),StandardCharsets.UTF_8));
            StringBuilder errorOutput = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }
            
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                System.out.println("成功拉取: " + file.name);
                pulled.add(file);
                successCount++;
            } else {
                System.err.println("拉取失败: " + file.name + " - " + errorOutput.toString());
                file.pullFailed=true;
            }
        }
        
        System.out.println("拉取完成。成功: " + successCount + "/" + files.size());
//        if(successCount<files.size()) {
//        	statusCode=-1;
//        }
    }
    private static void setWorkDir(ProcessBuilder processBuilder) {
        processBuilder.directory(new File("D:\\Program Files (x86)\\FormatFactory"));
        Map<String, String> env = processBuilder.environment();
        env.put("LANG", "zh_CN.UTF-8");
	 }
}