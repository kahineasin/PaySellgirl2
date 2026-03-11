package org.sellgirlPayHelperNotSpring.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sellgirl.sgJavaHelper.SGFunc;

/**
 * 读取华为手机文件，来自deepseek
 * 实测可以读取图片
 * 
 * 注意这些华为类，要在手机用USB线连接电脑并弹窗确定开启调试以后，才能正常读取数据
 */
public class HuaweiFileFetcher {

//    // 目标手机目录（需要根据你的实际情况修改）
////    private static final String PHONE_SOURCE_DIR = "/sdcard/DCIM/Camera/";
////    private static final String PHONE_SOURCE_DIR = "/内部存储/Pictures/";//文件拉取失败，请检查错误信息。
////    private static final String PHONE_SOURCE_DIR = "/Pictures/";//无目录
////    private static final String PHONE_SOURCE_DIR = "此电脑\\BENJAMIN's mobile\\内部存储\\Pictures";//无目录
////    private static final String PHONE_SOURCE_DIR = "此电脑/BENJAMIN's mobile/内部存储/Pictures";//无目录
//	 private static final String PHONE_SOURCE_DIR = "/sdcard/Pictures/";//ok 注意文件夹也会过来的
//    // 目标电脑目录（保存文件的本地路径）
//    private static final String LOCAL_SAVE_DIR = "D:/cache/2/";

    public static int exitCode =-1;
//    public static SGFunc<File,Object,Object,Boolean> fileFilter=null;
	/**
	 * (深度递归子目录)
	 * @param args
	 */
    public static void main(String[] args) {
    	String PHONE_SOURCE_DIR=args[0];
    	String LOCAL_SAVE_DIR=args[1];
        try {
        	exitCode=-1;
            // 1. 确保本地保存目录存在
            File localDir = new File(LOCAL_SAVE_DIR);
            if (!localDir.exists()) {
                localDir.mkdirs(); // 创建多级目录
            }

            // 2. 构建ADB命令
            // 命令格式：adb pull <手机路径> <本地路径>
            String[] pullCommand = {
                    "adb", "pull",
                    PHONE_SOURCE_DIR,
                    LOCAL_SAVE_DIR
            };

            // 3. 执行命令
            System.out.println("开始从手机拉取文件...");
            Process process = Runtime.getRuntime().exec(pullCommand);

            // 4. 读取命令执行输出（非常重要！可以查看进度和错误）
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String s;
            System.out.println("标准输出:");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s); // 这里会打印类似“正在复制... 123MB/s (12345678字节中的5678901)”的信息
            }

            System.out.println("错误输出 (如果有):");
            while ((s = stdError.readLine()) != null) {
                System.err.println(s); // 打印错误信息，例如文件不存在、设备未找到等
            }

            // 5. 等待命令执行完成并获取退出码
            exitCode = process.waitFor();
            System.out.println("\n命令执行完毕，退出码: " + exitCode);

            if (exitCode == 0) {
                System.out.println("文件拉取成功！");
                // 可以在这里添加代码，遍历 LOCAL_SAVE_DIR 来处理刚刚下载的图片
                File[] downloadedFiles = localDir.listFiles();
                if (downloadedFiles != null) {
                    for (File file : downloadedFiles) {
                        if (file.isFile() && file.getName().toLowerCase().endsWith(".jpg")) {
                            System.out.println("找到图片: " + file.getName());
                            // TODO: 对你的图片文件进行操作，例如读取、分析等
                        }
                    }
                }
            } else {
                System.err.println("文件拉取失败，请检查错误信息。");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}