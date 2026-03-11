package com.sellgirl.sgJavaHelper.video;

import java.io.File;
import java.nio.file.Paths;


import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGSpeedCounter;
import com.sellgirl.sgJavaHelper.file.SGDirectory;
import com.sellgirl.sgJavaHelper.file.SGPath;
import com.sellgirl.sgJavaHelper.video.FLVToMKVConverter.PlayDevice;

public class VideoConverter {

	public static void main(String[] args) {


//		 imgPath="D:\\a_video\\erika\\一公升的眼泪";
//		 imgPath2="D:\\a_video\\erika\\out";

//		 imgPath="D:\\a_video\\erika\\一公升的眼泪 1リットルの涙 2005.rmvb";
//		 imgPath2=null;
		 
		 String imgPath=args[0];
		 String imgPath2=2<args.length? args[2]:null;
		 boolean hasDstPath=null!=imgPath2;
		 
		 //PlayDevice device= PlayDevice.SonyTV;
		 PlayDevice device= PlayDevice.values()[Integer.parseInt(args[1])];
	        String fmt=FLVToMKVConverter.getFormat(device);
		 
		File srcFile = new File(imgPath); //

		System.out.println("convert start at "+SGDate.Now());
		int cnt=0;
       SGSpeedCounter speed2=new SGSpeedCounter();
       speed2.setBeginTime(SGDate.Now());
       
       
		if(srcFile.isFile()) {
			String[] arr=SGPath.splitPath(imgPath);
//			File encFile = new File(Paths.get(arr[0],arr[1]+"_01"+arr[2]).toUri()); //
//			SGEncryptByte.EncFile(srcFile, encFile,Integer.decode(arg[1]));
			 
			imgPath2=Paths.get(arr[0],arr[1]+"_01"+fmt).toString();
			FLVToMKVConverter.convert(imgPath, imgPath2, device);

		}else if(srcFile.isDirectory()) {
			if(hasDstPath) {
				SGDirectory.EnsureExists(imgPath2);
			}
			int batch=0;
			int maxBatch=4;
			for(File f:srcFile.listFiles()) {
				if(f.isFile()) {
					speed2.setBeginTime(SGDate.Now());
					
					String[] arr=SGPath.splitPath(f.getAbsolutePath());
//					File encFile = new File(Paths.get(arg.length>2?arg[2]:arr[0],arr[1]+(arg.length>2?"":"_01")+arr[2]).toUri()); // 
//					SGEncryptByte.EncFile(f, encFile,Integer.decode(arg[1]));

					String tmpImgPath2=Paths.get(hasDstPath?imgPath2:arr[0],arr[1]+(hasDstPath?"":"_01")+fmt).toString();
					FLVToMKVConverter.convert(f.getPath(), tmpImgPath2, device);
					
					speed2.setEndTime(SGDate.Now());
					cnt++;
					System.out.printf(""+speed2.getEnSpeed2(cnt)+" \r\n");
					
					//多线程方式，似乎和ffmpeg内部的多线程冲突
//					batch++;
//					if(batch>=maxBatch) {
//						Process process=FLVToMKVConverter.start();
//						System.out.printf(""+speed2.getEnSpeed2(cnt)+" \r\n");
//						batch=0;
//					}
				}
			}
			
		}
		//Process process=FLVToMKVConverter.start();
		
		System.out.println("convert finish");


	}

}
