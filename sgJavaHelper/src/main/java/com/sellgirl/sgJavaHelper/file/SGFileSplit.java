package com.sellgirl.sgJavaHelper.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SGFileSplit {

	   public static void main (String[] arg) throws NumberFormatException, Exception {
		   //命令行用法 
		   //java -cp sgJavaHelper-fusion.jar com.sellgirl.sgJavaHelper.SGFileSplit "D:\\3\\zip\\knightSasha_pc_exe.7z" "D:\\3\\zip\\split"
		   //1次分多个
		   //java -cp sgJavaHelper-0.0.42.jar com.sellgirl.sgJavaHelper.SGFileSplit "D:\\3\\src" "D:\\3\\split"
			SGDirectory.EnsureExists(arg[1]);
		   fileSplit(arg[0],arg[1]);

           System.out.println("s finish");
	   }
	public static void fileSplit(String sourceFilePath,String chunkFileFolder) throws IOException {

//		//源文件地址
		File sourceFile = new File(sourceFilePath);
		doSplitFromFolder(sourceFile,chunkFileFolder);
	}
	private static void doSplitFromFolder(File sourceFile,String chunkFileFolder) throws IOException {
		int mb=9;
		long size=1024l*1024l*9;
		if(sourceFile.isFile()) {
			SGFileSplit.doFileSplit(sourceFile, chunkFileFolder,mb);
		}else if(sourceFile.isDirectory()) {
			for(File f:sourceFile.listFiles()) {
				if(f.isFile()) {
//					SGFileSplit.doFileSplit(f,Paths.get(chunkFileFolder,SGPath.GetFileNameWithoutExtension(f)).toString(),mb);

					if(size<f.length()) {
//						SGFileSplit.doFileSplit(f,Paths.get(chunkFileFolder,SGPath.GetFileNameWithoutExtension(f)).toString(),mb);//这样保存不到扩展名
						String fileName=f.getName();
//						int i=fileName.lastIndexOf(".");
						fileName=fileName.replace('.','_');
						SGFileSplit.doFileSplit(f,Paths.get(chunkFileFolder,fileName).toString(),mb);	
					}else {
						SGDirectory.EnsureExists(chunkFileFolder);
						File dstFile = new File(Paths.get(chunkFileFolder,f.getName()).toString());
						SGPath.copyFile(f, dstFile);
//						java.nio.file.Files.copy(null, null)
					}
				}else if(f.isDirectory()) {
					SGFileSplit.doSplitFromFolder(f,Paths.get(chunkFileFolder,f.getName()).toString());
				}
			}
		}
	}
//	/**
//	 * 改用doFileSplit
//	 * @param sourceFile
//	 * @param chunkFileFolder
//	 * @throws IOException
//	 */
//	@Deprecated
//	private static void doFileSplitOld(File sourceFile,String chunkFileFolder) throws IOException {
//
//		SGDirectory.EnsureExists(chunkFileFolder);
////		//块文件目录
////		String chunkFileFolder = "D:\\3\\zip\\split";
//		//块文件大小
//		//int chunkFileSize = 1 * 1024 * 1024;
//		int chunkFileSize = 9 * 1024 * 1024;//9MB
//		//块文件数量
//		int chunkFileNum = (int) Math.ceil(sourceFile.length() * 1.0 / chunkFileSize);
//		//
//		RandomAccessFile readFile = new RandomAccessFile(sourceFile, "r");
//		byte[] bytes = new byte[1024];
//		for(int i = 0; i < chunkFileNum; i++) {
//			File chunkFile = new File(chunkFileFolder  + "\\" + i);
//			int len = -1;
//			//创建块文件
//			RandomAccessFile writeFile = new RandomAccessFile(chunkFile, "rw");
//			while((len = readFile.read(bytes)) != -1) {
//				writeFile.write(bytes, 0 ,len);
//				//如果块文件大小达到1M，就读下一块
//				if(chunkFile.length() >= chunkFileSize) {
//					break;
//				}
//			}
//			writeFile.close();
//		}
//		readFile.close();
//	}	

	/**
	 * 
	 * @param sourceFile 待分文件
	 * @param chunkFileFolder 目标目录
	 * @param mb
	 * @throws IOException
	 */
	public static void doFileSplit(File sourceFile,String chunkFileFolder,int mb) throws IOException {		
		
		SGDirectory.EnsureExists(chunkFileFolder);
//		//块文件目录
//		String chunkFileFolder = "D:\\3\\zip\\split";
		//块文件大小
		//int chunkFileSize = 1 * 1024 * 1024;
//		int chunkFileSize = 9 * 1024 * 1024;//9MB
		int chunkFileSize = mb * 1024 * 1024;//9MB
		//块文件数量
		int chunkFileNum = (int) Math.ceil(sourceFile.length() * 1.0 / chunkFileSize);
		//
		RandomAccessFile readFile = new RandomAccessFile(sourceFile, "r");
		byte[] bytes = new byte[1024];
		for(int i = 0; i < chunkFileNum; i++) {
			File chunkFile = new File(chunkFileFolder  + "\\" + i);
			int len = -1;
			//创建块文件
			RandomAccessFile writeFile = new RandomAccessFile(chunkFile, "rw");
			while((len = readFile.read(bytes)) != -1) {
				writeFile.write(bytes, 0 ,len);
				//如果块文件大小达到1M，就读下一块
				if(chunkFile.length() >= chunkFileSize) {
					break;
				}
			}
			writeFile.close();
		}
		readFile.close();
	}
	public static void fileMerge(String chunkFileFolderPath,String mergerFilePath) throws IOException {
        //块文件目录
////        String chunkFileFolderPath = "D:\\3\\split";
//        String chunkFileFolderPath = "D:\\3\\zip\\split";
        //获取块文件对象
        File chunkFileFolder = new File(chunkFileFolderPath);
        
//        //块文件列表
//        File[] files = chunkFileFolder.listFiles();
//        SGFileSplit.doMergeFromFolder(files, mergerFilePath);

        SGFileSplit.doMergeFromFolder2(chunkFileFolder, mergerFilePath);
	}
	
	/**
	 * 此方法处理不了 分块和不分块混合的情况
	 * @param chunkFiles
	 * @param mergerFilePath
	 * @throws IOException
	 */
	@Deprecated
	private static void doMergeFromFolder( File[] chunkFiles,String mergerFilePath) throws IOException {
		//SGDirectory.EnsureExists(mergerFilePath);
		if(null==chunkFiles||0>=chunkFiles.length) {return;}
		if(chunkFiles[0].isFile()) {
			SGFileSplit.doFileMerge(chunkFiles, mergerFilePath);
		}else if(chunkFiles[0].isDirectory()) {
			for(File f:chunkFiles) {
				if(f.isFile()) {
					//SGFileSplit.doFileSplit(f,Paths.get(chunkFileFolder,SGPath.GetFileNameWithoutExtension(f)).toString());
				}else if(f.isDirectory()) {
					SGFileSplit.doMergeFromFolder(f.listFiles(),Paths.get(mergerFilePath,f.getName()).toString());
				}
			}
		}
	}

	private static void doMergeFromFolder2( File chunkFile,String mergerFilePath) throws IOException {
		//SGDirectory.EnsureExists(mergerFilePath);
//		if(null==chunkFiles||0>=chunkFiles.length) {return;}
		if(chunkFile.isFile()) {
//			SGFileSplit.doFileMerge(chunkFiles, mergerFilePath);
//			File dstFile = new File(Paths.get(mergerFilePath,chunkFile.getName()).toString());
			File dstFile = new File(mergerFilePath);
			SGPath.copyFile(chunkFile, dstFile);
		}else if(chunkFile.isDirectory()) {
			boolean isAllFile=true;
			boolean has0=false;
			boolean hasMax=false;
			String max=String.valueOf(chunkFile.listFiles().length-1);
			for(File f:chunkFile.listFiles()) {
				if(!f.isFile()) {
					isAllFile=false;
					break;
				}
				if("0".equals(f.getName())){
					has0=true;
				}
				if(max.equals(f.getName())){
					hasMax=true;
				}
			}
			if(isAllFile&&has0&&hasMax) {
				SGFileSplit.doFileMerge(chunkFile.listFiles(), mergerFilePath);
			}else {
				for(File f:chunkFile.listFiles()) {
					SGFileSplit.doMergeFromFolder2(f,Paths.get(mergerFilePath,f.getName()).toString());	
				}
			}
		}
	}
	private static void doFileMerge( File[] chunkFiles,String mergerFilePath) throws IOException {
		//SGDirectory.EnsureExists(mergerFilePath);
        //将文件排序
        List<File> filesList = Arrays.asList(chunkFiles);
        Collections.sort(filesList, (o1, o2) -> {
            if(Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())){
                return 1;
            }
            return -1;
        });
        //合并后的文件
        //File mergerFile = new File("D:\\3\\zip\\file_merger.psd");
        int i=mergerFilePath.lastIndexOf('_');
        if(0<=i&&mergerFilePath.length()-1>i) {
        	mergerFilePath=mergerFilePath.substring(0, i)+"."+mergerFilePath.substring(i+1);
        }
        File mergerFile = new File(mergerFilePath);
        //创建新文件
        //boolean newFile = 
        		mergerFile.createNewFile();
        //创建写对象
        RandomAccessFile writeFile = new RandomAccessFile(mergerFile, "rw");
 
        byte[] b = new byte[1024];
        for(File chunkFile : filesList){
            int len = -1;
            //创建读块文件的对象
            RandomAccessFile readFile = new RandomAccessFile(chunkFile, "r");
            while((len = readFile.read(b)) != -1){
                writeFile.write(b, 0, len);
            }
            readFile.close();
        }
        writeFile.close();
	}
}
