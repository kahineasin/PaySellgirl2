package org.sellgirl.sellgirlPayWeb.controller.model;

import com.jcraft.jsch.*;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

/**
 * 多线程深度递归上传文件到云ubuntu,重试3次
 */
public class ConcurrentSftpUpload2 {
	private static final String TAG="ConcurrentSftpUpload";
    // 配置参数
    private static final String HOST ="156.224.19.162";// "你的服务器IP";
    private static final int PORT = 22;
    private static final String USER ="root";// "ubuntu";
//    private static final String SSH="C:\\Users\\Administrator\\.ssh\\id_rsa.pub";
  //"C:/Users/Administrator/.ssh/id_rsa";
    private static final String SSH="C:\\Users\\Administrator\\.ssh\\id_rsa";
//    private static final String PASSWORD = "";
    
//    private static final String LOCAL_ROOT = "D:\\cache\\html1\\shop\\static\\resourceImg";          // 本地根目录
//    private static final String REMOTE_ROOT = "/root/myapp/shop/static/resourceImg"; // 远程根目录
//    private static final String LOCAL_ROOT = "D:\\cache\\html1\\shop\\static\\resourceImg\\movie";          // 本地根目录
//    private static final String REMOTE_ROOT = "/root/myapp/shop/static/resourceImg/movie"; // 远程根目录
//    private static final String LOCAL_ROOT = "D:\\cache\\html1\\shop\\static\\resourceImg\\movie60";          // 本地根目录
//    private static final String REMOTE_ROOT = "/root/myapp/shop/static/resourceImg/movie60"; // 远程根目录

    private static final String LOCAL_ROOT = "D:\\cache\\html1\\shop\\static\\resourceImg\\comic60";    //全部上传完成，总耗时 167 秒 
    private static final String REMOTE_ROOT = "/root/download/aaa"; // 
    
    private static final int THREAD_COUNT = 5;                      // 上传线程数
    private static final int QUEUE_CAPACITY = 1000;                 // 队列容量，防止内存爆炸

    // 线程安全的阻塞队列
    private static final BlockingQueue<FileTask> taskQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
    // 已创建的远程目录集合（线程安全）
    private static final Set<String> createdDirs = Collections.synchronizedSet(new HashSet<>());
    // 待上传文件总数
    private static final AtomicInteger pendingFiles = new AtomicInteger(0);
    // 控制程序退出
    private static final CountDownLatch finishLatch = new CountDownLatch(1);

    // 扫描线程标志
    private static volatile boolean scanningDone = false;
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        // 启动扫描线程
        Thread scanner = new Thread(ConcurrentSftpUpload2::scanLocalFiles);
        scanner.start();

        // 启动上传线程池
        ExecutorService uploadPool = Executors.newFixedThreadPool(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            uploadPool.submit(ConcurrentSftpUpload2::uploadWorker);
        }

        // 等待扫描完成且所有文件上传完成
        scanner.join();                // 等待扫描线程结束
        // 扫描结束后，等待所有上传任务完成
        while (pendingFiles.get() > 0) {
            Thread.sleep(500);
        }
        // 通知上传线程可以退出（因为队列可能还有任务，但 pendingFiles 已为零，它们会自然退出）
        scanningDone = true;
        uploadPool.shutdown();
        uploadPool.awaitTermination(10, TimeUnit.MINUTES);

        long end = System.currentTimeMillis();
        System.out.println("全部上传完成，总耗时 " + (end - start) / 1000 + " 秒");
    }

    /**
     * 扫描线程：递归遍历本地目录，将文件任务放入队列
     */
    private static void scanLocalFiles() {
        File root = new File(LOCAL_ROOT);
        if (!root.exists()) {
            System.err.println("本地目录不存在: " + LOCAL_ROOT);
            return;
        }
        try {
            scanRecursive(root, "");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            scanningDone = true;
            System.out.println("扫描完成，共发现 " + pendingFiles.get() + " 个文件");
        }
    }

    private static void scanRecursive(File file, String relativePath) throws InterruptedException {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    scanRecursive(child, relativePath + "/" + child.getName());
                }
            }
        } else {
            // 构建远程路径：去掉本地根目录，拼接远程根目录
            String remotePath = REMOTE_ROOT + (relativePath.isEmpty() ? "" : relativePath);
            // 将任务放入队列，如果队列满则阻塞
            taskQueue.put(new FileTask(file, remotePath));
            pendingFiles.incrementAndGet();  // 增加待处理文件数
        }
    }

    
    private static void uploadWorker() {
        // 每个线程独立创建一次连接
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channel = null;

        try {
        	jsch.addIdentity(SSH);
            // 初始化连接
            session = jsch.getSession(USER, HOST, PORT);
//            session.setPassword(PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(30000);      // 连接超时
            session.connect();

            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect(30000);          // 通道超时

            // 主循环：不断从队列中取任务，复用 channel 上传
            while (true) {
                FileTask task = null;
                try {
                    // 检查退出条件
                    if (scanningDone && taskQueue.isEmpty() && pendingFiles.get() == 0) {
                        break;
                    }
                    task = taskQueue.poll(1, TimeUnit.SECONDS);
                    if (task == null) {
                        continue;
                    }

                    // 带重试的上传（复用 channel）
                    boolean success = false;
                    for (int retry = 0; retry < 3; retry++) {
                        try {
                            ensureRemoteDir(channel, task.remotePath); // 注意：这里传入 channel
                            uploadFile(channel, task.localFile, task.remotePath);
                            success = true;
                            break;
                        } catch (Exception e) {
                            if (retry == 2) {
                                System.err.println("上传最终失败: " + task.localFile.getPath());
                            } else {
                                // 等待后重试（可能连接已坏，需要重连？）
                                Thread.sleep(1000L * (1 << retry)); // 指数退避
                                // 如果判断是连接问题，可以尝试重连
                                if (channel == null || !channel.isConnected()) {
                                    reconnect(channel, session);
                                }
                            }
                        }
                    }
                    // 无论成功或放弃，都减少计数
                    pendingFiles.decrementAndGet();
                    if (success) {
                        System.out.println("上传成功: " + task.localFile.getName());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    // 其他异常，打印并尝试继续
                    e.printStackTrace();
                    if (task != null) {
                        pendingFiles.decrementAndGet();
                    }
                }
            }
        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            // 线程结束时关闭连接
            if (channel != null && channel.isConnected()) channel.disconnect();
            if (session != null && session.isConnected()) session.disconnect();
        }
    }

    // 确保远程目录存在（复用 channel）
    private static void ensureRemoteDir(ChannelSftp channel, String remotePath) throws SftpException {
        String parentDir = remotePath.substring(0, remotePath.lastIndexOf('/'));
        // 可以用一个本地 Set 缓存已经创建过的目录，避免重复 stat
        // 这里简化，每次尝试创建（可能会报错，忽略即可）
        mkdirs(channel, parentDir);
    }

    // 创建目录（递归）
    private static void mkdirs(ChannelSftp channel, String path) throws SftpException {
        String[] parts = path.split("/");
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

    // 上传文件（复用 channel）
    private static void uploadFile(ChannelSftp channel, File localFile, String remotePath) throws SftpException, IOException {
        try (FileInputStream fis = new FileInputStream(localFile)) {
            channel.put(fis, remotePath);
        }
    }

    // 重连逻辑（可选）
    private static void reconnect(ChannelSftp channel, Session session) throws JSchException {
        if (channel != null && channel.isConnected()) channel.disconnect();
        if (session != null && session.isConnected()) session.disconnect();
        session.connect();
        channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();
    }
    

    /**
     * 文件任务封装
     */
    private static class FileTask {
        final File localFile;
        final String remotePath;
        public int tryTimes=0;
        FileTask(File localFile, String remotePath) {
            this.localFile = localFile;
            this.remotePath = remotePath;
        }
    }
}