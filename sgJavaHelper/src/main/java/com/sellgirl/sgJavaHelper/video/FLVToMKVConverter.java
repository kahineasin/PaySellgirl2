package com.sellgirl.sgJavaHelper.video;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
 
public class FLVToMKVConverter {
//    public static void convertFlvToMkv(String inputFilePath, String outputFilePath) {
//        try {
//            ProcessBuilder processBuilder = new ProcessBuilder(
//                ".\\ffmpeg.exe",
//                "-i", inputFilePath,
//                "-codec", "copy",
//                outputFilePath
//            );
//            setWorkDir(processBuilder);
//            Process process = processBuilder.start();
//            process.waitFor();
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//            // 处理异常情况
//        }
//    }
    
    public static void convertFlvToSonyTV(String inputFilePath, String outputFilePath) {
        try {
//            ProcessBuilder processBuilder = new ProcessBuilder(
////                ".\\ffmpeg.exe",
//            		"cmd.exe","/C",
//                "ffmpeg",
//                "-i", inputFilePath,
//                "-c:v libx264", 
//                "-crf 23",
//                "-preset medium",
//                outputFilePath
//            );

            ProcessBuilder processBuilder = new ProcessBuilder(
            );
            setWorkDir(processBuilder);
            processBuilder.redirectErrorStream(true);
            processBuilder.command(
"cmd.exe","/C",
                    "ffmpeg",
                    "-i", inputFilePath,
                    "-c:v","libx264", 
                    "-crf","23",
                    "-preset","medium",//Option not found
                    outputFilePath);

//            processBuilder.command(
//"cmd.exe","/c",
//"ffmpeg",
//"-i", inputFilePath,
//"-codec", "copy",
//outputFilePath
//                    );
            
//            processBuilder.command("cmd.exe");
//            Process process = processBuilder.start();
//            process.waitFor();
            Process process = processBuilder.start();

//         // 获取命令输出结果
//            InputStream inputStream = process.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)); // 设置编码为GBK
//            String line;
//            while ((line = reader.readLine()) != null) {
//                //System.out.println(line);
//            }
            
  	      try (InputStream is = process.getInputStream();
	              java.io.Reader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
	              BufferedReader r = new BufferedReader(isr)) {
//	          long count = r.lines().count();
//	          System.out.printf("started %d lines \r\n",count);
	          
	          String line;
    	      while ((line = r.readLine()) != null) {
	            System.out.println(line);
    	      }
	      }

            process.waitFor();
            //String aa="aa";
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            // 处理异常情况
        }
    }
 
//    public static void main(String[] args) {
//        String inputFilePath = "input.flv";
//        String outputFilePath = "output.mkv";
//        convertFlvToMkv(inputFilePath, outputFilePath);
//    }
	private static ArrayList<ProcessBuilder> taskList=new ArrayList<ProcessBuilder>();
    public static void convertFlvToSonyTVAsync(String inputFilePath, String outputFilePath) {
        try {
//            ProcessBuilder processBuilder = new ProcessBuilder(
////                ".\\ffmpeg.exe",
//            		"cmd.exe","/C",
//                "ffmpeg",
//                "-i", inputFilePath,
//                "-c:v libx264", 
//                "-crf 23",
//                "-preset medium",
//                outputFilePath
//            );

            ProcessBuilder processBuilder = new ProcessBuilder(
            );
            setWorkDir(processBuilder);
//            processBuilder.redirectErrorStream(true);
            
            //这个指令好像本身就是多线程，用pipe好像有问题，原因未明
            //原因应该是同一目录的同一exe运行多实例，会有多临时文件的风险
            processBuilder.command(
"cmd.exe","/C",
                    "ffmpeg",
                    "-i", inputFilePath,
                    "-c:v","libx264", 
                    "-crf","23",
                    "-preset","medium",//Option not found
                    outputFilePath);
            

//            processBuilder.command(
//                    "java",
//                    "-version");

//            processBuilder.command(
//"cmd.exe","/c",
//"ffmpeg",
//"-i", inputFilePath,
//"-codec", "copy",
//outputFilePath
//                    );
            
//            processBuilder.command("cmd.exe");
//            Process process = processBuilder.start();
//            process.waitFor();
            
            //1.多线程的方式
            taskList.add(processBuilder);
            
            //2.单线程的方式
//            Process process = processBuilder.start();
//
//         // 获取命令输出结果
//            InputStream inputStream = process.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)); // 设置编码为GBK
//            String line;
//            while ((line = reader.readLine()) != null) {
//                //System.out.println(line);
//            }
//
//            process.waitFor();
//            //String aa="aa";
        } catch (Exception e) {
            e.printStackTrace();
            // 处理异常情况
        }
    }
    
    public static void convert(String src,String dst,PlayDevice device) {
    	switch(device) {
	    	case SonyTV:
	    		FLVToMKVConverter.convertFlvToSonyTV(src, dst);
	    		//FLVToMKVConverter.convertFlvToSonyTVAsync(src, dst);
	    		break;
    		default:
    			break;
    	}
    }

    public static void convertAsync(String src,String dst,PlayDevice device) {
    	switch(device) {
	    	case SonyTV:
	    		//FLVToMKVConverter.convertFlvToSonyTV(src, dst);
	    		FLVToMKVConverter.convertFlvToSonyTVAsync(src, dst);
	    		break;
    		default:
    			break;
    	}
    }

//    public static void convertAsync(String src,String dst,PlayDevice device) {
//    	switch(device) {
//	    	case SonyTV:
////	    		FLVToMKVConverter.convertFlvToSonyTV(src, dst);
//	    		FLVToMKVConverter.convertFlvToSonyTVAsync(src, dst);
//	    		break;
//    		default:
//    			break;
//    	}
//    }

    public static String getFormat(PlayDevice device) {
    	switch(device) {
    	case SonyTV:
    		return ".mp4";
		default:
			break;
			
    	}
    	return null;
    }
	 public static enum PlayDevice{
		 SonyTV
	 }
	 
	 private static void setWorkDir(ProcessBuilder processBuilder) {

         processBuilder.directory(new File("D:\\Program Files (x86)\\FormatFactory"));
         Map<String, String> env = processBuilder.environment();
         env.put("LANG", "zh_CN.UTF-8");
	 }
	 //需要jdk11+
//	 public static Process start() {
//		 try {
//			 if(taskList.isEmpty()) {return null;}
//			//ProcessBuilder.startPipeline(taskList);
//					  List<Process> processes = ProcessBuilder.startPipeline(
//							  taskList);
//
//	    	          System.out.printf("started %d tasks \r\n",processes.size());
//		    	      Process last = processes.get(processes.size()-1);
//		    	      try (InputStream is = last.getInputStream();
//		    	              java.io.Reader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
//		    	              BufferedReader r = new BufferedReader(isr)) {
//		    	          long count = r.lines().count();
//		    	          System.out.printf("started %d lines \r\n",count);
//		    	          
////		    	          String line;
////			    	      while ((line = r.readLine()) != null) {
////		    	            System.out.println("xxxxxx"+line);
////			    	      }
//		    	      }
//		    	      
//
////		    	      // 获取命令输出结果
////		    	      InputStream inputStream = last.getInputStream();
////		    	      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)); // 设置编码为GBK
////		    	      String line;
////		    	      while ((line = reader.readLine()) != null) {
////		    	          //System.out.println(line);
////		    	      }
//
////		    	      last.waitFor();
//		    	      System.out.println("---------------2------------");
//		              for (Process process : processes) {
//		                  process.waitFor(); // 等待进程完成
//		                  int exitValue = process.exitValue();
//		                  if (exitValue == 0) {
//		                      System.out.println("进程成功完成");
//		                  } else {
//		                      System.out.println("进程失败，退出值：" + exitValue);
//		                  }
//		              }
//		    	      System.out.println("---------------3------------");
//		    	      taskList.clear();
//		    	      return last;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 return null;
//	 }
}