package org.sellgirlPayHelperNotSpring.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * 读华为手机数据，同时删除源文件，来自deepseek(未测试)
 */
public class HuaweiFileFetcherAndDelete {

//    // 目标手机目录（需要根据你的实际情况修改）
//    private static final String PHONE_SOURCE_DIR = "/sdcard/DCIM/Camera/";
//    // 目标电脑目录（保存文件的本地路径）
//    private static final String LOCAL_SAVE_DIR = "C:/Temp/HuaweiPhotos/";
	 private static final String PHONE_SOURCE_DIR = "/sdcard/Pictures/";
	    // 目标电脑目录（保存文件的本地路径）
	    private static final String LOCAL_SAVE_DIR = "D:/cache/2/";

	    @Deprecated
    public static void main(String[] args) {
        try {
            // 1. 确保本地保存目录存在
            File localDir = new File(LOCAL_SAVE_DIR);
            if (!localDir.exists()) {
                localDir.mkdirs();
            }

            // 2. 构建并执行ADB PULL命令
            System.out.println("开始从手机拉取文件...");
            String[] pullCommand = {"adb", "pull", PHONE_SOURCE_DIR, LOCAL_SAVE_DIR};
            Process pullProcess = Runtime.getRuntime().exec(pullCommand);

            // 读取pull命令的输出和错误流
            printProcessStreams(pullProcess, "拉取");
            int pullExitCode = pullProcess.waitFor();

            if (pullExitCode != 0) {
                System.err.println("文件拉取失败，中止操作。请检查错误信息。");
                return; // 拉取失败，直接退出，不执行删除
            }

            System.out.println("文件拉取成功！");

            // --- 可选但推荐的验证步骤 ---
            // 这里简单地检查目标目录是否非空，你可以根据需要实现更复杂的验证（如检查文件哈希）
            File[] downloadedFiles = localDir.listFiles();
            if (downloadedFiles == null || downloadedFiles.length == 0) {
                System.err.println("警告：本地目录为空，可能未成功拉取任何文件。将不执行删除操作。");
                return;
            }
            System.out.println("已成功下载 " + downloadedFiles.length + " 个文件到本地。");

            // 3. 询问是否删除（在实际自动化任务中，可以注释掉这部分，直接执行删除）
            System.out.print("是否要删除手机上的原文件？ (yes/no): ");
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            String confirmation = consoleReader.readLine();
            if (!confirmation.equalsIgnoreCase("yes")) {
                System.out.println("已取消删除操作。");
                return;
            }

//            // 4. 构建并执行ADB DELETE命令
//            // 注意：这里使用 `adb shell rm -f` 来强制删除文件和目录
//            // 警告：rm -rf 是极其危险的命令，请确保路径正确！
//            System.out.println("开始删除手机上的原文件...");
//            
//            // 如果要删除整个目录及其内容（非常危险！）
//            // String[] deleteCommand = {"adb", "shell", "rm", "-rf", PHONE_SOURCE_DIR};
//            
//            // 更安全的做法：只删除目录下的所有.jpg文件，保留目录结构
//            // 假设我们只删除.jpg图片
//            String[] deleteCommand = {
//                "adb", "shell", "rm", "-f",
//                PHONE_SOURCE_DIR + "*.jpg" // 例如：删除 /sdcard/DCIM/Camera/ 下的所有jpg
//            };
//
//            Process deleteProcess = Runtime.getRuntime().exec(deleteCommand);
//            printProcessStreams(deleteProcess, "删除");
//            int deleteExitCode = deleteProcess.waitFor();
//
//            if (deleteExitCode == 0) {
//                System.out.println("手机原文件删除成功！");
//            } else {
//                System.err.println("删除命令执行可能出错，请检查错误信息。");
//            }
            deleteHuaweiFile(PHONE_SOURCE_DIR,"*.jpg");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
	    public static void deleteHuaweiFile(String path,String match) throws IOException, InterruptedException {
	    	deleteHuaweiFile(path+match);
	    }
	    
	    private static boolean debug=false;
	    /**
	     * 测试可用
	     * @param path
	     * @param match
	     * @throws IOException
	     * @throws InterruptedException
	     */
    public static void deleteHuaweiFile(String path) throws IOException, InterruptedException {// 4. 构建并执行ADB DELETE命令
        // 注意：这里使用 `adb shell rm -f` 来强制删除文件和目录
        // 警告：rm -rf 是极其危险的命令，请确保路径正确！
    	if(debug) {
    		System.out.println("开始删除手机上的原文件...");
    	}
        
        // 如果要删除整个目录及其内容（非常危险！）
        // String[] deleteCommand = {"adb", "shell", "rm", "-rf", PHONE_SOURCE_DIR};
        
        // 更安全的做法：只删除目录下的所有.jpg文件，保留目录结构
        // 假设我们只删除.jpg图片
        String[] deleteCommand = {
            "adb", "shell", "rm", "-f",
//            path+//PHONE_SOURCE_DIR +
//            match//"*.jpg" // 例如：删除 /sdcard/DCIM/Camera/ 下的所有jpg
            path
        };

        Process deleteProcess = Runtime.getRuntime().exec(deleteCommand);
        printProcessStreams(deleteProcess, "删除");
        int deleteExitCode = deleteProcess.waitFor();

        if (deleteExitCode == 0) {
        	if(debug) {
        		System.out.println("手机原文件删除成功！");
        	}
        } else {
            System.err.println("删除命令执行可能出错，请检查错误信息。");
        }
    }

    /**
     * 一个辅助方法，用于打印进程的输出流和错误流
     * @param process 进程对象
     * @param processName 进程名称，用于输出信息
     * @throws IOException
     */
    private static void printProcessStreams(Process process, String processName) throws IOException {
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String s;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(processName + "过程标准输出:");
            System.out.println(s);
        }

        while ((s = stdError.readLine()) != null) {
            System.out.println(processName + "过程错误输出 (如果有):");
            System.err.println(s);
        }
    }
}