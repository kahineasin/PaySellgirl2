package com.sellgirl.sgJavaHelper.file;

public class SGFileMerge {

	   public static void main (String[] arg) throws NumberFormatException, Exception {
		   //命令行用法 
		   //java -cp sgJavaHelper-fusion.jar com.sellgirl.sgJavaHelper.SGFileMerge "D:\\3\\zip\\split" "D:\\3\\zip\\file_merger.psd"
		   //合多个
		   //java -cp sgJavaHelper-fusion.jar com.sellgirl.sgJavaHelper.SGFileMerge "D:\\3\\split" "D:\\3\\merge"
		   SGFileSplit.fileMerge(arg[0],arg[1]);

           System.out.println("m finish");
	   }
}
