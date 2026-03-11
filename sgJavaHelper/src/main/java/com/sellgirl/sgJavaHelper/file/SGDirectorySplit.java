package com.sellgirl.sgJavaHelper.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGRef;

/**
 * 分割文件夹。用于应对百度云等限制单次上传数量的情况
 */
public class SGDirectorySplit {

	/**
	 * 
	 * SGDirectorySplit.main(new String[] {"D:\\3\\src\\Screenshots","D:\\3\\split","199"});
	 * 注意这里会根据src胡第一层目录名来计算分割的子文件名，所以要注意参数习惯
	 * 
	 * @param arg
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	   public static void main (String[] arg) throws NumberFormatException, Exception {
//		   //命令行用法 
//		   //java -cp sgJavaHelper-fusion.jar com.sellgirl.sgJavaHelper.SGFileSplit "D:\\3\\zip\\knightSasha_pc_exe.7z" "D:\\3\\zip\\split"
//		   //1次分多个
//		   //java -cp sgJavaHelper-0.0.42.jar com.sellgirl.sgJavaHelper.SGFileSplit "D:\\3\\src" "D:\\3\\split"
////			SGDirectory.EnsureExists(arg[1]);
//		   
//		   
//			SGRef<Integer> cnt=new SGRef<Integer>(0);
//			
////		   fileSplit(arg[0],arg[1]);
//			File f=new File(arg[0]);
//			if(f.isFile()) {
//				System.out.println("本方法是用于处理文件夹");
//				return ;
//			}
//			
//			int batch=Integer.valueOf(arg[2]);
//			int total=f.listFiles().length;
//			int i=0;
//			while(0<total) {
//				cnt.SetValue(0);
//				doSplitFromFolder(f,Paths.get(arg[1],f.getName()+i).toString(),batch,cnt);
//				total=f.listFiles().length;
//				
//			}
		   
//			split("D:\\3\\src","D:\\3\\split",1);
			split(arg[0],arg[1],Integer.valueOf(arg[2]));
			System.out.println("split folder finish");
	   }
	   private static void split(String src,String dst,int batch) throws IOException {
			File f=new File(src);
			if(f.isFile()) {
				System.out.println("本方法是用于处理文件夹");
				return ;
			}
			SGRef<Integer> cnt=new SGRef<Integer>(0);
			int i=0;
			   int total=f.listFiles().length;
			while(0<total) {
				cnt.SetValue(0);
				doSplitFromFolder(f,Paths.get(dst,f.getName()+i).toString(),batch,cnt,false);
				total=f.listFiles().length;
				i++;
				System.out.println("proccessed:"+cnt.GetValue()+" time:"+SGDate.Now().toTimestamp());
				
			}
	   }
	   
		private static void doSplitFromFolder(File sourceFile,String chunkFileFolder,int batch,SGRef<Integer> cnt,boolean deleteFolder) throws IOException {
//			int mb=9;
//			long size=1024l*1024l*9;
			if(batch<=cnt.GetValue()) {
				return;
			}
			if(sourceFile.isFile()) {
//				SGFileSplit.doFileSplit(sourceFile, chunkFileFolder,mb);
				SGDirectory.EnsureFilePath(chunkFileFolder);
//				File dstFile = new File(Paths.get(chunkFileFolder,sourceFile.getName()).toString());
				File dstFile = new File(chunkFileFolder);
				SGPath.copyFile(sourceFile, dstFile);
				sourceFile.delete();
				cnt.SetValue(cnt.GetValue()+1); 
			}else if(sourceFile.isDirectory()) {
				for(File f:sourceFile.listFiles()) {
//					if(f.isFile()) {
//						if(size<f.length()) {
////							SGFileSplit.doFileSplit(f,Paths.get(chunkFileFolder,SGPath.GetFileNameWithoutExtension(f)).toString(),mb);//这样保存不到扩展名
//							String fileName=f.getName();
////							int i=fileName.lastIndexOf(".");
//							fileName=fileName.replace('.','_');
//							SGFileSplit.doFileSplit(f,Paths.get(chunkFileFolder,fileName).toString(),mb);	
//						}else {
//							SGDirectory.EnsureExists(chunkFileFolder);
//							File dstFile = new File(Paths.get(chunkFileFolder,f.getName()).toString());
//							SGPath.copyFile(f, dstFile);
////							java.nio.file.Files.copy(null, null)
//						}
//					}else if(f.isDirectory()) {
//						SGFileSplit.doSplitFromFolder(f,Paths.get(chunkFileFolder,f.getName()).toString());
//					}
					doSplitFromFolder(f,Paths.get(chunkFileFolder,f.getName()).toString(),batch,cnt,true);
					if(batch<=cnt.GetValue()) {
						return;
					}
				}
				if(deleteFolder) {
					sourceFile.delete();
				}
			}
		}
}
