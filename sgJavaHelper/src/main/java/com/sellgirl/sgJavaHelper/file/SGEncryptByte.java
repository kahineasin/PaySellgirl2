package com.sellgirl.sgJavaHelper.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;

public class SGEncryptByte {

	//private static final int numOfEncAndDec = 0x1;
//	private static final int numOfEncAndDec = 0x123;
//	private static final int numOfEncAndDec = 123;
	//private static int dataOfFile = 0; // 文件字节内容
	
	/**
	 * 当DEFAULT_BUFFER_SIZE = 4096, 58KB文件
	 *     read 15 times
	 *     所以4KB每次
	 *     
	 * 当DEFAULT_BUFFER_SIZE = 4096, 2.89MB文件
	 *     估计 read 700 times
	 *     speed:5.26E+000row/s
	 *     totalTime:0h2m22s260ms
	 */
	public static final int DEFAULT_BUFFER_SIZE = 4096;//2^13=16^3*2

	/**
	 * 此方法将改为私有。尽量使用递归的doEncFromFolder
	 * @param srcFile
	 * @param encFile
	 * @param key
	 * @throws Exception
	 */
	@Deprecated
	public static void EncFile(File srcFile, File encFile,int key) throws Exception {

		if (!srcFile.exists()) {

			System.out.println("source file not exixt");

			return;

		}

		if (!encFile.exists()) {


			encFile.createNewFile();
			System.out.println("encrypt file created");

		}

		FileInputStream fis = new FileInputStream(srcFile);

		FileOutputStream fos = new FileOutputStream(encFile);

		// int dataOfFile=0;
		//int b=0;

//		while ((dataOfFile = fis.read()) > -1) {
//
//			fos.write(0==b?dataOfFile ^ numOfEncAndDec:dataOfFile);
//			b++;
//			if(b>3) {b=0;}
//		}
		byte[] buffer=new byte[DEFAULT_BUFFER_SIZE];
		int dataOfFile = 0;
		
//		int cnt=0;
//        SGSpeedCounter speed2=new SGSpeedCounter();
//        speed2.setBeginTime(SGDate.Now());
		
		while ((dataOfFile = fis.read(buffer)) != -1) {
//				//fos.write(buffer, 0, dataOfFile^numOfEncAndDec);
//			//fos.write(((int)buffer)^numOfEncAndDec, 0, dataOfFile);
//				//fos.write(buffer, 0, dataOfFile);
//			if(0==b) {
//				for(int i=0;i<dataOfFile;i++) {
//					fos.write(buffer[i]^key);
//				}
//			}else {
//				for(int i=0;i<dataOfFile;i++) {
//					fos.write(buffer[i]);
//				}
//			}
//			b++;
//			if(b>3) {b=0;}
			
//			//speed:5.26E+000row/s
//			for(int i=0;i<dataOfFile;i++) {
//				fos.write(buffer[i]^key);
//			}
			
			//speed:4.49E+003row/s
			byte[] bs=new byte[dataOfFile];
			for(int i=0;i<dataOfFile;i++) {
				//fos.write(buffer[i]^key);
				int tmp=buffer[i]^key;
				byte b=(byte)tmp;
				bs[i]=b;
			}
			fos.write(bs);
			
//			cnt++;
//
//	        speed2.setEndTime(SGDate.Now());
//	        System.out.printf(""+speed2.getEnSpeed2(cnt)+" \r\n");
//	        speed2.setBeginTime(SGDate.Now());
		}
		
		//System.out.printf("read %d times \r\n",cnt);

		fis.close();

		fos.flush();

		fos.close();

	}	
	public static void doEncFromFolder(File sourceFile,String encFileFolder,int key) throws Exception {
//		int mb=9;
//		long size=1024l*1024l*9;
		if(sourceFile.isFile()) {
			SGDirectory.EnsureFilePath(encFileFolder);
			EncFile(sourceFile, new File(encFileFolder),key);
		}else if(sourceFile.isDirectory()) {
			for(File f:sourceFile.listFiles()) {
//				if(f.isFile()) {
////					SGFileSplit.doFileSplit(f,Paths.get(chunkFileFolder,SGPath.GetFileNameWithoutExtension(f)).toString(),mb);
//
//					if(size<f.length()) {
////						SGFileSplit.doFileSplit(f,Paths.get(chunkFileFolder,SGPath.GetFileNameWithoutExtension(f)).toString(),mb);//这样保存不到扩展名
//						String fileName=f.getName();
////						int i=fileName.lastIndexOf(".");
//						fileName=fileName.replace('.','_');
//						SGFileSplit.doFileSplit(f,Paths.get(chunkFileFolder,fileName).toString(),mb);	
//					}else {
//						SGDirectory.EnsureExists(chunkFileFolder);
//						File dstFile = new File(Paths.get(chunkFileFolder,f.getName()).toString());
//						SGPath.copyFile(f, dstFile);
////						java.nio.file.Files.copy(null, null)
//					}
//				}else if(f.isDirectory()) {
//					SGFileSplit.doSplitFromFolder(f,Paths.get(chunkFileFolder,f.getName()).toString());
//				}
				String p=Paths.get(encFileFolder,f.getName()).toString();
				doEncFromFolder(f,p,key);
			}
		}
	}

	/**
	 * 改为私有。外部用doDecFromFolder
	 * @param encFile
	 * @param decFile
	 * @param key
	 * @throws Exception
	 */
//	@Deprecated
	private static void DecFile(File encFile, File decFile,int key) throws Exception {

		if (!encFile.exists()) {

			System.out.println("encrypt file not exixt");

			return;

		}

		if (!decFile.exists()) {


			decFile.createNewFile();
			System.out.println("decrypt file created");

		}

		FileInputStream fis = new FileInputStream(encFile);

		FileOutputStream fos = new FileOutputStream(decFile);

//int dataOfFile=0;

//		int b=0;
//		while ((dataOfFile = fis.read()) > -1) {
//
//			fos.write(0==b?dataOfFile ^ numOfEncAndDec:dataOfFile);
//			b++;
//			if(b>3) {b=0;}
//
//		}

//		byte[] buffer=new byte[DEFAULT_BUFFER_SIZE];
//		while ((dataOfFile = fis.read(buffer)) != -1) {
//				//fos.write(buffer, 0, dataOfFile^numOfEncAndDec);
//			//fos.write(((int)buffer)^numOfEncAndDec, 0, dataOfFile);
//				//fos.write(buffer, 0, dataOfFile);
//			for(int i=0;i<buffer.length;i++) {
//				fos.write(buffer[i]^numOfEncAndDec);
//			}
//		}
		
		DecryptByte(fis,fos,DEFAULT_BUFFER_SIZE,key);

		fis.close();

		fos.flush();

		fos.close();

	}

	/**
	 * 递归文件夹
	 * @param sourceFile
	 * @param decFileFolder
	 * @param key
	 * @throws Exception
	 */
	public static void doDecFromFolder(File sourceFile,String decFileFolder,int key) throws Exception {
		if(sourceFile.isFile()) {
			SGDirectory.EnsureFilePath(decFileFolder);
			DecFile(sourceFile, new File(decFileFolder),key);
		}else if(sourceFile.isDirectory()) {
			for(File f:sourceFile.listFiles()) {
				String p=Paths.get(decFileFolder,f.getName()).toString();
				doDecFromFolder(f,p,key);
			}
		}
	}
	public static void DecryptByte(InputStream fis,OutputStream fos,int bufferSize,int key) throws IOException {
		byte[] buffer=new byte[bufferSize];
		//int b=0;
		int dataOfFile = 0;
		while ((dataOfFile = fis.read(buffer)) != -1) {//read 参数是读的数据, 返回值是读的数据的长度

//			if(0==b) {
//			for(int i=0;i<dataOfFile;i++) {
//				fos.write(buffer[i]^key);
//			}
//			}else {
//
//				for(int i=0;i<dataOfFile;i++) {
//					fos.write(buffer[i]);
//				}
//			}
//			b++;
//			if(b>3) {b=0;}

//			//太慢 speed:5.26E+000row/s
//			for(int i=0;i<dataOfFile;i++) {
//				fos.write(buffer[i]^key);
//			}
			

			byte[] bs=new byte[dataOfFile];
			for(int i=0;i<dataOfFile;i++) {
				//fos.write(buffer[i]^key);
				int tmp=buffer[i]^key;
				byte b=(byte)tmp;
				bs[i]=b;
			}
			fos.write(bs);
		}
	}

	/**
	 * 此方法解D:\\2\\font_cn_01.txt的结果不正确,应该是不支持中文的原因(已解决此问题)
	 * @param fis
	 * @param bufferSize
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public static String DecryptByteToString(InputStream fis,int bufferSize,int key) throws IOException {
		byte[] buffer=new byte[bufferSize];


		//int b=0;
		StringBuilder text=new StringBuilder();
		//String text="";
		int dataOfFile = 0;
		while ((dataOfFile = fis.read(buffer)) != -1) {//read 参数是读的数据, 返回值是读的数据的长度

//			if(0==b) {
//			for(int i=0;i<dataOfFile;i++) {
//				
////				text+=new String(new byte[] {(byte) (buffer[i]^key)});
//				text.append(new String(new byte[] {(byte) (buffer[i]^key)}));
//			}
//			}else {
//
//				for(int i=0;i<dataOfFile;i++) {
////					text+=new String(new byte[] {(byte) (buffer[i])});
//					text.append(new String(new byte[] {(byte) (buffer[i])}));
//				}
//			}
//			b++;
//			if(b>3) {b=0;}

//			//这样处理中文有问题20231210
//			for(int i=0;i<dataOfFile;i++) {
//				
////				text+=new String(new byte[] {(byte) (buffer[i]^key)});
//				text.append(new String(new byte[] {(byte) (buffer[i]^key)}));
//			}
			
			//这样可以支持中文
			byte[] b2=new byte[dataOfFile];
			for(int i=0;i<dataOfFile;i++) {				
				b2[i]=(byte) (buffer[i]^key);
			}
			text.append(new String(b2));
		}
		fis.close();
		return text.toString();
	}
//	   private static void enFile (String path,String key) throws NumberFormatException, Exception {
//		   //命令行用法 java -cp pfHelperNotSpring-0.0.40.jar com.sellgirl.pfHelperNotSpring.SGEncryptByte "d:\2\sashaKnight.png" "0x123456"
//			String[] arr=SGPath.splitPath(path);
//			File srcFile = new File(path); //
//			File encFile = new File(Paths.get(arr[0],arr[1]+"_01"+arr[2]).toUri()); // 
//
//			SGEncryptByte.EncFile(srcFile, encFile,Integer.decode(key));
//	   }
//
//	   public static void mainOld (String[] arg) throws NumberFormatException, Exception {
//		   //命令行用法 java -cp pfHelperNotSpring-0.0.40.jar com.sellgirl.pfHelperNotSpring.SGEncryptByte "d:\2\sashaKnight.png" "0x123456"
//			String[] arr=SGPath.splitPath(arg[0]);
//			File srcFile = new File(arg[0]); //
//			File encFile = new File(Paths.get(arr[0],arr[1]+"_01"+arr[2]).toUri()); // 
//
//			SGEncryptByte.EncFile(srcFile, encFile,Integer.decode(arg[1]));
//	   }

	   public static void main (String[] arg) throws NumberFormatException, Exception {
		   //命令行用法 java -cp pfHelperNotSpring-0.0.40.jar com.sellgirl.pfHelperNotSpring.SGEncryptByte "d:\2\sashaKnight.png" "0x123456"
		   //或 java -cp pfHelperNotSpring-0.0.40.jar com.sellgirl.pfHelperNotSpring.SGEncryptByte "d:\2\walk" "0x123456" "d:\2\walk\dist"
		   //或java -cp sgJavaHelper-fusion.jar com.sellgirl.sgJavaHelper.SGEncryptByte "D:\\3\\sex" "0x123456" "D:\\3\\src"
			
			File srcFile = new File(arg[0]); //
			if(srcFile.isFile()) {
				String[] arr=SGPath.splitPath(arg[0]);
				File encFile = new File(Paths.get(arr[0],arr[1]+"_01"+arr[2]).toUri()); // 

				SGEncryptByte.EncFile(srcFile, encFile,Integer.decode(arg[1]));
			}else if(srcFile.isDirectory()) {
				if(arg.length>2) {
					SGDirectory.EnsureExists(arg[2]);
					SGEncryptByte.doEncFromFolder(srcFile, arg[2], Integer.decode(arg[1]));
				}else {
					for(File f:srcFile.listFiles()) {
						if(f.isFile()) {
							String[] arr=SGPath.splitPath(f.getAbsolutePath());
							
							File encFile = new File(Paths.get(arg.length>2?arg[2]:arr[0],arr[1]+(arg.length>2?"":"_01")+arr[2]).toUri()); //
							SGEncryptByte.EncFile(f, encFile,Integer.decode(arg[1]));
							
	//						String encFilePath=Paths.get(arg.length>2?arg[2]:arr[0],arr[1]+(arg.length>2?"":"_01")+arr[2]).toString();
							//SGEncryptByte.doEncFromFolder(f, encFilePath, Integer.decode(arg[1]));
						}
	//					else if(2<arg.length){
	//						String encFilePath=Paths.get(arg[2],f.getName()).toString();
	//						SGDirectory.EnsureExists(arg[2]);
	//						SGEncryptByte.doEncFromFolder(f, encFilePath, Integer.decode(arg[1]));	
	//					}
					}
				}
			}
			System.out.println("e finish");
	   }
}
