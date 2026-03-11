package com.sellgirl.sgJavaHelper.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*
 * 代替C#里的Path类，相当于处理uri等路径
 */
public class SGPath {
	public static String GetFileName(String dirPath) {
		File file = new File(dirPath);
		return file.getName();
	}
    //
    // 摘要:
    //     Returns the file name of the specified path string without the extension.
    //
    // 参数:
    //   path:
    //     The path of the file.
    //
    // 返回结果:
    //     The string returned by System.IO.Path.GetFileName(System.String), minus the last
    //     period (.) and all characters following it.
    //
    // 异常:
    //   T:System.ArgumentException:
    //     path contains one or more of the invalid characters defined in System.IO.Path.GetInvalidPathChars.
    public static String GetFileNameWithoutExtension(String path) {
    	String fileName=GetFileName(path);
    	return fileName.substring(0,fileName.lastIndexOf("."));
    }
    public static String GetFileNameWithoutExtension(File file) {
    	String fileName=file.getName();
    	return fileName.substring(0,fileName.lastIndexOf("."));
    }
    /**
     * 返回扩展名，如 mp3 不带点
     * @param fileName
     * @return
     */
    public static String getFileExtension(String fileName) {
//    	String fileName=file.getName();
    	int i=fileName.lastIndexOf(".");
    	if(0>i||fileName.length()-1<=i) {return null;}
    	return fileName.substring(i+1);
    }
    /**
     * 把 /aa/bb/cc.jar 之类的路径 分隔成3部分
     * 1 aa/bb
     * 2 cc
     * 3 .jar
     * @param fullPath
     * @return
     */
    public static String[] splitPath(String fullPath){
		fullPath=fullPath.replaceAll("\\\\","/");
		if(fullPath.length()>0&&fullPath.charAt(0)=='/'){
			fullPath=fullPath.substring(1);
		}
		String[] r=new String[]{"","",""};
		int postIdx=fullPath.lastIndexOf(".");
		int pathEndIdx=fullPath.lastIndexOf("/");
		String postfix =postIdx>-1? fullPath.substring(postIdx):"";//后缀,带.
		//String path=pathEndIdx>-1?fullPath.substring(0,pathEndIdx+1):"";
		String path=pathEndIdx>-1?fullPath.substring(0,pathEndIdx):"";
		//String fileName=fullPath.substring(path.length(),fullPath.length()-postfix.length());
		//String fileName=fullPath.substring(path.length()+1,fullPath.length()-postfix.length());
		String fileName=fullPath.substring(path.length(),fullPath.length()-postfix.length());
		if(fileName.charAt(0)=='/') {//当pathEndIdx是-1时,
			fileName=fileName.substring(1);
		}
		r[0]=path;
		r[1]=fileName;
		r[2]=postfix;
		return r;
	}
    
    public static void copyFile(File source, File dest)
      throws IOException {    
       InputStream input = null;    
       OutputStream output = null;    
       try {
              input = new FileInputStream(source);
              output = new FileOutputStream(dest);        
              byte[] buf = new byte[1024];        
              int bytesRead;        
              while ((bytesRead = input.read(buf)) != -1) {
                  output.write(buf, 0, bytesRead);
              }
       } finally {
           input.close();
           output.close();
       }
   }
}
