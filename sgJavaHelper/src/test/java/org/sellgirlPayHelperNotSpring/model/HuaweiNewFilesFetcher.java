package org.sellgirlPayHelperNotSpring.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sellgirl.sgJavaHelper.SGDate;

/**
 * 摘取华为手机上的新文件，来自deepseek,未测试
 * 实测报错：查找文件时出错: find: bad arg '-newermt'
 * 
 */
@Deprecated
public class HuaweiNewFilesFetcher {

    // 目标手机目录
    private static final String PHONE_SOURCE_DIR = "/sdcard/DCIM/Camera/";
    // 目标电脑目录
//    private static final String LOCAL_SAVE_DIR = "C:/Temp/HuaweiPhotos/";
    private static final String LOCAL_SAVE_DIR = "D:/3/src/";
    // 时间格式
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        try {
            // 1. 设置时间点（这里设置为2023-01-01 00:00:00之后）
//            Date targetDate = DATE_FORMAT.parse("2023-01-01 00:00:00");
            Date targetDate = DATE_FORMAT.parse("2025-08-22 01:00:00");
//            Date targetDate =new com.sellgirl.sgJavaHelper.SGDate(2025,8,22,1,1,1).ToCalendar().getTime();
            
            // 2. 确保本地保存目录存在
            File localDir = new File(LOCAL_SAVE_DIR);
            if (!localDir.exists()) {
                localDir.mkdirs();
            }
            
            // 3. 查找指定时间点后的文件
            List<String> newFiles = findNewFiles(targetDate);
            
            if (newFiles.isEmpty()) {
                System.out.println("没有找到指定时间点后的新文件。");
                return;
            }
            
            System.out.println("找到 " + newFiles.size() + " 个新文件:");
            for (String file : newFiles) {
                System.out.println(" - " + file);
            }
            
            // 4. 拉取找到的文件
            pullFiles(newFiles);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 查找指定时间点后的文件
     */
    private static List<String> findNewFiles(Date targetDate) throws IOException, InterruptedException {
        List<String> fileList = new ArrayList<>();
        
        // 将Java Date转换为adb shell find命令可接受的格式
        SimpleDateFormat adbDateFormat = new SimpleDateFormat("yyyyMMdd.HHmmss");
        String timeStr = adbDateFormat.format(targetDate);
        
        // 构建查找命令
        // 注意：不同Android版本find命令参数可能有所不同
        String[] findCommand = {
            "adb", "shell", "find", PHONE_SOURCE_DIR, 
            "-type", "f", 
            "-newermt", timeStr
        };
        
        Process process = Runtime.getRuntime().exec(findCommand);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                fileList.add(line.trim());
            }
        }
        
        // 检查错误
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        StringBuilder errorOutput = new StringBuilder();
        while ((line = errorReader.readLine()) != null) {
            errorOutput.append(line).append("\n");
        }
        
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            System.err.println("查找文件时出错: " + errorOutput.toString());
            
            // 如果-newermt参数不支持，尝试使用备用方法
            if (errorOutput.toString().contains("unknown option") || 
                errorOutput.toString().contains("illegal option")) {
                System.out.println("尝试备用查找方法...");
                return findNewFilesAlternative(targetDate);
            }
        }
        
        return fileList;
    }
    
    /**
     * 备用查找方法（适用于不支持-newermt参数的设备）
     */
    private static List<String> findNewFilesAlternative(Date targetDate) throws IOException, InterruptedException {
        List<String> fileList = new ArrayList<>();
        
        // 获取目标时间的时间戳（秒）
        long targetTimestamp = targetDate.getTime() / 1000;
        
        // 使用ls命令列出文件并解析时间
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
            // 格式：-rw-rw---- 1 root sdcard_rw 1234567 2023-01-02 10:30:45.000000000 +0800 filename.jpg
            String[] parts = line.split("\\s+");
            if (parts.length >= 6) {
                try {
                    // 尝试获取文件修改时间
                    String dateStr = parts[5];
                    String timeStr = parts[6];
                    
                    // 如果时间包含.（如10:30:45.000000000），则分割取前半部分
                    if (timeStr.contains(".")) {
                        timeStr = timeStr.split("\\.")[0];
                    }
                    
                    // 解析日期时间
                    Date fileDate = DATE_FORMAT.parse(dateStr + " " + timeStr);
                    
                    // 比较时间
                    if (!fileDate.before(targetDate)) {
                        // 获取文件名（可能是parts[8]或之后的部分，因为文件名可能包含空格）
                        StringBuilder fileName = new StringBuilder();
                        for (int i = 8; i < parts.length; i++) {
                            if (i > 8) fileName.append(" ");
                            fileName.append(parts[i]);
                        }
                        
                        fileList.add(PHONE_SOURCE_DIR + fileName.toString());
                    }
                } catch (Exception e) {
                    // 解析失败，跳过此行
                    System.err.println("解析文件信息失败: " + line);
                }
            }
        }
        
        process.waitFor();
        return fileList;
    }
    
    /**
     * 拉取文件列表中的文件
     */
    private static void pullFiles(List<String> files) throws IOException, InterruptedException {
        int successCount = 0;
        
        for (String phoneFile : files) {
            // 构建拉取命令
            String[] pullCommand = {"adb", "pull", phoneFile, LOCAL_SAVE_DIR};
            
            Process process = Runtime.getRuntime().exec(pullCommand);
            
            // 读取输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            // 读取错误
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder errorOutput = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }
            
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                System.out.println("成功拉取: " + phoneFile);
                successCount++;
            } else {
                System.err.println("拉取失败: " + phoneFile + " - " + errorOutput.toString());
            }
        }
        
        System.out.println("拉取完成。成功: " + successCount + "/" + files.size());
    }
}