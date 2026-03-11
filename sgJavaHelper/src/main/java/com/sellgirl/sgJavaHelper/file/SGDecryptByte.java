package com.sellgirl.sgJavaHelper.file;

import java.io.File;
import java.nio.file.Paths;

public class SGDecryptByte {

	   public static void main (String[] arg) throws NumberFormatException, Exception {
		   //命令行用法 java -cp pfHelperNotSpring-0.0.36.jar com.sellgirl.pfHelperNotSpring.SGDecryptByte "d:\2\sashaKnight1.png" "0x123456"
		   //或java -cp sgJavaHelper-fusion.jar com.sellgirl.sgJavaHelper.SGEncryptByte "D:\\3\\sex" "0x123456" "D:\\3\\src"

			System.out.println("-----1------");
		   //String[] arr=SGPath.splitPath(arg[0]);
			File srcFile = new File(arg[0]); //
			//File encFile = new File(Paths.get(arr[0],arr[1]+"_02"+arr[2]).toUri()); // 

			System.out.println("-----2------");
			//SGEncryptByte.DecFile(srcFile, encFile,Integer.decode(arg[1]));		
			SGEncryptByte.doDecFromFolder(srcFile,arg[2] ,Integer.decode(arg[1]));	

			System.out.println("d finish");
	   }
}
