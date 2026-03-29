//package org.sellgirl.sellgirlPayWeb.controller.model;
//
//import com.jcraft.jsch.*;
//import com.sellgirl.sgJavaHelper.file.SGDirectory;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.util.Properties;
//
///**
// * 上传到ubuntu, 递归版本, 可用,但不快
// */
//public class SftpUpload2 {
//
//    private static String sp="/";//ubuntu路径分隔符
//    public static void main(String[] args) {
//        String host = "1.2.3.4";
//        int port = 22;
//        String user ="root";// "ubuntu";
//        String password = "xxx";  // 或者用私钥
////        String localDir = "D:/images";          // 本地文件夹
////        String remoteDir = "/home/ubuntu/images"; // 远程目标目录
//        String localDir = "D:\\cache\\html1\\shop\\static\\resourceImg2";          // 本地文件夹
//        String remoteDir = "/root/myapp/shop/static/resourceImg2"; // 远程目标目录
//        JSch jsch = new JSch();
//        Session session = null;
//        ChannelSftp channel = null;
//
//        try {
//            // 创建会话
//            session = jsch.getSession(user, host, port);
//            session.setPassword(password);
//            Properties config = new Properties();
//            config.put("StrictHostKeyChecking", "no");
//            session.setConfig(config);
//            session.connect();
//
//            // 打开 SFTP 通道
//            channel = (ChannelSftp) session.openChannel("sftp");
//            channel.connect();
//
//            // 创建远程目录（如果不存在）
//            mkdirs(channel, remoteDir);
//
//            // 上传本地文件夹下的所有文件
//            File localFile = new File(localDir);
//            File[] files = localFile.listFiles();
//            if (files != null) {
//                for (File file : files) {
//                    if (file.isFile()) {
//                        System.out.println("上传: " + file.getName());
//                        try (FileInputStream fis = new FileInputStream(file)) {
//                            channel.put(fis, remoteDir + "/" + file.getName());
//                        }
//                    }else if(file.isDirectory()){
//                    	doCopy(channel,file,remoteDir+sp+file.getName());
//                    }
//                }
//            }
//
//            System.out.println("上传完成！");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (channel != null) channel.disconnect();
//            if (session != null) session.disconnect();
//        }
//    }
//
//	public static void doCopy(ChannelSftp channel,File sourceFolder,String dstFolder) throws IOException, SftpException {		
//        mkdirs(channel, dstFolder);
//        File[] files = sourceFolder.listFiles();
//        if (files != null) {
//            for (File file : files) {
//                if (file.isFile()) {
//                    System.out.println("上传: " + file.getName());
//                    try (FileInputStream fis = new FileInputStream(file)) {
//                        channel.put(fis, dstFolder + sp + file.getName());
//                    }
//                }else if(file.isDirectory()){
//                	doCopy(channel,file,dstFolder+sp+file.getName());
//                }
//            }
//        }
//	}
//    // 递归创建远程目录
//    private static void mkdirs(ChannelSftp channel, String path) throws SftpException {
//        String[] dirs = path.split("/");
//        String current = "";
//        for (String dir : dirs) {
//            if (dir.isEmpty()) continue;
//            current += "/" + dir;
//            try {
//                channel.stat(current);
//            } catch (SftpException e) {
//                channel.mkdir(current);
//            }
//        }
//    }
//}