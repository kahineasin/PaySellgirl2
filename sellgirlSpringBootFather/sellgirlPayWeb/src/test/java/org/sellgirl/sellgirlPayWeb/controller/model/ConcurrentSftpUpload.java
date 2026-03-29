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

public class ConcurrentSftpUpload {
	private static final String TAG="ConcurrentSftpUpload";
    // 配置参数
    private static final String HOST ="156.224.19.162";// "你的服务器IP";
    private static final int PORT = 22;
    private static final String USER ="root";// "ubuntu";
//    private static final String SSH="C:\\Users\\Administrator\\.ssh\\id_rsa.pub";
    private static final String SSH=null;//"C:/Users/Administrator/.ssh/id_rsa";
    private static final String PASSWORD = "x96rNe9e1D";
//    private static final String LOCAL_ROOT = "D:\\cache\\html1\\shop\\static\\resourceImg";          // 本地根目录
//    private static final String REMOTE_ROOT = "/root/myapp/shop/static/resourceImg"; // 远程根目录
    private static final String LOCAL_ROOT = "D:\\cache\\html1\\shop\\static\\resourceImg\\movie";          // 本地根目录
    private static final String REMOTE_ROOT = "/root/myapp/shop/static/resourceImg/movie"; // 远程根目录
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
        Thread scanner = new Thread(ConcurrentSftpUpload::scanLocalFiles);
        scanner.start();

        // 启动上传线程池
        ExecutorService uploadPool = Executors.newFixedThreadPool(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            uploadPool.submit(ConcurrentSftpUpload::uploadWorker);
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

    /**
     * 上传线程：从队列中获取任务并上传
     * @deprecated 这方法有put,可能会和scan造成竞争死锁啊
     */
    @Deprecated
    private static void uploadWorker2() {
        while (true) {
            FileTask task;
            try {
                // 如果扫描已完成且队列为空，退出
                if (scanningDone && taskQueue.isEmpty()) {
                    break;
                }
                // 阻塞获取，最多等待1秒，避免死循环
                task = taskQueue.poll(1, TimeUnit.SECONDS);
                if (task == null) {
                    continue;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            try {
                // 确保远程父目录存在（线程安全）
                ensureRemoteDir(task.remotePath);
                // 执行上传
                uploadFile(task.localFile, task.remotePath);
                System.out.println("上传成功: " + task.localFile.getName() + " -> " + task.remotePath);

                // 减少待处理计数
                if (pendingFiles.decrementAndGet() == 0) {
                    // 所有文件上传完成，可提前结束
                }
            } catch (Exception e) {
                task.tryTimes++;
                if(3<task.tryTimes) {
                	System.err.println("上传失败: " + task.localFile.getPath() + " - " + e.getMessage());
                	SGDataHelper.getLog().write("上传失败: " + task.localFile.getPath() + " -> " + task.remotePath, TAG);
                	SGDataHelper.getLog().writeException(e, TAG+"1");
                    // 减少待处理计数
                    if (pendingFiles.decrementAndGet() == 0) {
                        // 所有文件上传完成，可提前结束
                    }
                }else {
                    try {
						taskQueue.put(task);
						SGDataHelper.getLog().print("放回队列(重试"+task.tryTimes+"): " + task.localFile.getPath() + " - " + e.getMessage());
					} catch (InterruptedException e1) {
	                	SGDataHelper.getLog().write("放回队列失败: " + task.localFile.getPath() + " -> " + task.remotePath, TAG);
	                	SGDataHelper.getLog().writeException(e, TAG+"1");

	                    // 减少待处理计数
	                    if (pendingFiles.decrementAndGet() == 0) {
	                        // 所有文件上传完成，可提前结束
	                    }
					}
                    //pendingFiles.incrementAndGet();  // 增加待处理文件数
                }
            } finally {
//                // 无论成功失败，减少待处理计数
//                if (pendingFiles.decrementAndGet() == 0) {
//                    // 所有文件上传完成，可提前结束
//                }
            }
        }
    }
    private static void uploadWorker() {
        while (true) {
            FileTask task = null;
            try {
                task = taskQueue.poll(1, TimeUnit.SECONDS);
                if (task == null) {
                    if (scanningDone && taskQueue.isEmpty()) break;
                    continue;
                }

                // 本地重试上传，最多3次
                boolean success = false;
                for (int retry = 0; retry < 3; retry++) {
                    try {
                        ensureRemoteDir(task.remotePath);
                        uploadFile(task.localFile, task.remotePath);
                        success = true;
                        break;
                    } catch (Exception e) {
                        System.err.println("上传失败（第" + (retry+1) + "次）: " + task.localFile.getPath());
                        if (retry == 2) {
                            // 最后一次失败，记录日志并放弃
                            SGDataHelper.getLog().write("上传最终失败: " + task.localFile.getPath() + " -> " + task.remotePath, TAG);
                        }
                    }
                }
                // 无论成功或放弃，都减少计数
                pendingFiles.decrementAndGet();
                if (success) {
                    System.out.println("上传成功: " + task.localFile.getName() + " -> " + task.remotePath);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                // 其他异常（如网络问题导致 poll 失败），可以打印日志并继续
                if (task != null) {
                    // 可选：放回队列，但用 offer 并设置超时
                    // 但更简单是直接减少计数，或本地重试
                    pendingFiles.decrementAndGet();
                }
            }
        }
    }
    /**
     * 确保远程目录存在（线程安全）
     */
    private static synchronized void ensureRemoteDir(String remoteFilePath) throws JSchException, SftpException {
        String parentDir = remoteFilePath.substring(0, remoteFilePath.lastIndexOf('/'));
        if (createdDirs.contains(parentDir)) {
            return;
        }
        // 双重检查，避免重复创建
        synchronized (createdDirs) {
            if (createdDirs.contains(parentDir)) {
                return;
            }
            // 创建目录（递归）
            JSch jsch = new JSch();
            Session session = null;
            ChannelSftp channel = null;
            try {
                if(null==SSH) {
                    session = jsch.getSession(USER, HOST, PORT);
                	session.setPassword(PASSWORD);
                }else {
                    jsch.addIdentity(SSH);
                    session=jsch.getSession(USER, HOST, PORT);
                }
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();
                channel = (ChannelSftp) session.openChannel("sftp");
                channel.connect();

                String[] parts = parentDir.split("/");
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
                createdDirs.add(parentDir);
            } finally {
                if (channel != null) channel.disconnect();
                if (session != null) session.disconnect();
            }
        }
    }

    /**
     * 上传单个文件（SFTP）
     */
    private static void uploadFile(File localFile, String remotePath) throws JSchException, SftpException, IOException {
        JSch jsch = new JSch();
        Session session =null;// jsch.getSession(USER, HOST, PORT);
        if(null==SSH) {
            session = jsch.getSession(USER, HOST, PORT);
        	session.setPassword(PASSWORD);
        }else {
            jsch.addIdentity(SSH);
            session=jsch.getSession(USER, HOST, PORT);
        }
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();

        try (FileInputStream fis = new FileInputStream(localFile)) {
            channel.put(fis, remotePath);
        } finally {
            channel.disconnect();
            session.disconnect();
        }
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