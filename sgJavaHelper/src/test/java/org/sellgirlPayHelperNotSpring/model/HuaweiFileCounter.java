package org.sellgirlPayHelperNotSpring.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 获取华为手机目录下文件数量（测试可用），来自deepseek
 */
public class HuaweiFileCounter {

    public static void main(String[] args) {
        String phonePath = "/sdcard/DCIM/Camera";
        
        try {
            // 方法1: 使用ls命令统计所有项目数量
            int totalItems = countItemsWithLs(phonePath);
            System.out.println("总项目数: " + totalItems);
            
            // 方法2: 使用find命令统计文件数量
            int fileCount = countFilesWithFind(phonePath);
            System.out.println("文件数量: " + fileCount);
            
            // 方法3: 统计特定类型文件数量
            int jpgCount = countFilesByExtension(phonePath, "jpg");
            System.out.println("JPG图片数量: " + jpgCount);
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 使用ls命令统计目录中的所有项目数量（包括文件和文件夹）
     */
    public static int countItemsWithLs(String path) throws IOException, InterruptedException {
        String[] command = {"adb", "shell", "ls", path};
        Process process = Runtime.getRuntime().exec(command);
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        int count = 0;
        while (reader.readLine() != null) {
            count++;
        }
        
        process.waitFor();
        return count;
    }
    
    /**
     * 使用find命令统计文件数量（不包括文件夹）
     */
    public static int countFilesWithFind(String path) throws IOException, InterruptedException {
        String[] command = {"adb", "shell", "find", path, "-type", "f"};
        Process process = Runtime.getRuntime().exec(command);
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        int count = 0;
        while (reader.readLine() != null) {
            count++;
        }
        
        process.waitFor();
        return count;
    }
    
    /**
     * 统计指定扩展名的文件数量
     */
    public static int countFilesByExtension(String path, String extension) throws IOException, InterruptedException {
        String[] command = {"adb", "shell", "find", path, "-name", "*." + extension};
        Process process = Runtime.getRuntime().exec(command);
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        int count = 0;
        while (reader.readLine() != null) {
            count++;
        }
        
        process.waitFor();
        return count;
    }
    
    /**
     * 更精确的方法：使用ls -l并解析输出
     * 可以区分文件和文件夹，并获取更多详细信息
     */
    public static void analyzeDirectory(String path) throws IOException, InterruptedException {
        String[] command = {"adb", "shell", "ls", "-l", path};
        Process process = Runtime.getRuntime().exec(command);
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        int fileCount = 0;
        int dirCount = 0;
        long totalSize = 0;
        
        // 跳过第一行（总用量）
        reader.readLine();
        
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            
            String[] parts = line.split("\\s+");
            if (parts.length < 5) continue;
            
            // 检查是否是目录
            if (parts[0].startsWith("d")) {
                dirCount++;
            } else {
                fileCount++;
                // 尝试获取文件大小（可能是第4个或第5个字段）
                try {
                    long size = Long.parseLong(parts[4]);
                    totalSize += size;
                } catch (NumberFormatException e) {
                    // 如果无法解析大小，跳过
                }
            }
        }
        
        process.waitFor();
        
        System.out.println("文件夹数量: " + dirCount);
        System.out.println("文件数量: " + fileCount);
        System.out.println("总大小: " + formatFileSize(totalSize));
    }
    
    /**
     * 格式化文件大小
     */
    private static String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024.0 * 1024.0));
        }
    }
}