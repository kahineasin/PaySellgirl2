package org.sellgirl.sellgirlPayWeb.controller.model;

import com.jcraft.jsch.*;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DeployTool {

    // 配置
    private static final String HOST = "你的服务器IP";
    private static final int PORT = 22;
    private static final String USER = "ubuntu";
    private static final String PASSWORD = "你的密码";   // 或使用私钥
    private static final String LOCAL_FOLDER = "D:/images";          // 本地要上传的文件夹
    private static final String REMOTE_TARGET_DIR = "/home/ubuntu";  // 服务器目标目录
    private static final String ZIP_NAME = "upload.zip";             // 临时 zip 文件名

    public static void main(String[] args) {
        try {
            // 1. 打包本地文件夹为 zip（递归）
            String zipPath = LOCAL_FOLDER + ".zip";
            System.out.println("正在打包 " + LOCAL_FOLDER + " ...");
            zipFolder(LOCAL_FOLDER, zipPath);

            // 2. 上传 zip 文件
            System.out.println("正在上传 " + zipPath + " 到服务器...");
            uploadFile(zipPath, REMOTE_TARGET_DIR + "/" + ZIP_NAME);

            // 3. 在服务器上解压
            System.out.println("正在服务器上解压...");
            unzipOnServer(REMOTE_TARGET_DIR + "/" + ZIP_NAME, REMOTE_TARGET_DIR);

            // 4. 清理临时文件（可选）
            System.out.println("清理本地临时文件...");
            new File(zipPath).delete();

            System.out.println("部署完成！");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归打包文件夹
     */
    public static void zipFolder(String sourceDir, String zipFile) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            File src = new File(sourceDir);
            zipRecursive(src, src.getName(), zos);
        }
    }

    private static void zipRecursive(File file, String entryName, ZipOutputStream zos) throws IOException {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    zipRecursive(child, entryName + "/" + child.getName(), zos);
                }
            }
        } else {
            try (FileInputStream fis = new FileInputStream(file)) {
                zos.putNextEntry(new ZipEntry(entryName));
                byte[] buffer = new byte[8192];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                zos.closeEntry();
            }
        }
    }

    /**
     * 通过 SFTP 上传文件
     */
    public static void uploadFile(String localFile, String remoteFile) throws JSchException, SftpException, IOException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(USER, HOST, PORT);
        session.setPassword(PASSWORD);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();

        try (FileInputStream fis = new FileInputStream(localFile)) {
            // 确保远程目录存在
            ensureRemoteDir(channel, remoteFile.substring(0, remoteFile.lastIndexOf('/')));
            channel.put(fis, remoteFile);
        }

        channel.disconnect();
        session.disconnect();
    }

    private static void ensureRemoteDir(ChannelSftp channel, String dir) throws SftpException {
        String[] parts = dir.split("/");
        String current = "";
        for (String part : parts) {
            if (part.isEmpty()) continue;
            current += "/" + part;
            try {
                channel.stat(current);
            } catch (SftpException e) {
                channel.mkdir(current);
            }
        }
    }

    /**
     * 在服务器上执行解压命令（需要 unzip 命令，可通过 apt install unzip 安装）
     */
    public static void unzipOnServer(String zipPath, String destDir) throws JSchException, IOException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(USER, HOST, PORT);
        session.setPassword(PASSWORD);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        // 解压命令，覆盖已有文件（可选 -o）
        String command = String.format("unzip -o %s -d %s", zipPath, destDir);
        channel.setCommand(command);

        InputStream in = channel.getInputStream();
        channel.connect();

        // 读取执行结果（可选）
        byte[] buffer = new byte[1024];
        while (in.read(buffer) != -1) {
            System.out.write(buffer, 0, buffer.length);
        }
        channel.disconnect();
        session.disconnect();
    }
}