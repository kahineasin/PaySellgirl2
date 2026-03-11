package com.sellgirl.sgJavaHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/**
 * 所有byte相关的方法都集合到这里，因为byte很重要
 */
public class SGByteHelper {

	/**
	 * 对比
	 * readFileToByte 和 readFileToChar
	 * 现在感觉 byte到File的过程是和utf8等编码方式无关的; char到File的过程才和编码方式有关
	 * @param file
	 * @return
	 */
	public static byte[] readFileToByte(File file){//String path) {
//	       File file = new File(path);
	        try (FileInputStream fileInputStream = new FileInputStream(file)) {
	            byte[] fileContent = new byte[(int) file.length()];
	            fileInputStream.read(fileContent);
	            return fileContent;
	            // 处理字节数组
	            // ...
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	}

	@Deprecated
	public static char[] readFileToChar(File file,String encoding){//String path) {
//	       File file = new File(path);
	        //try (FileInputStream fileInputStream = new FileInputStream(file)) {
			try (// FileWriter writer = new FileWriter(filePath,false);
					// BufferedWriter out = new BufferedWriter(writer)
					BufferedReader in = new BufferedReader(
							new InputStreamReader(new FileInputStream(file), encoding))// 如果不写utf8,在cmd运行时,保存的文件会变成ansi
			) {
	            char[] fileContent = new char[(int) file.length()];
	            in.read(fileContent);
	            return fileContent;
	            // 处理字节数组
	            // ...
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	}
	
	/**
	 * 把string按byte转为int值的串(主要用于测试)
	 * 比如：\r\n -> 13,10
	 * @return
	 */
	public static String stringToByteInt(String s) {
		StringBuilder sb=new StringBuilder();		
		int idx=0;
		for(byte i:s.getBytes()) {
			if(0!=idx++) {
				sb.append(",");
			}
			int i2=i;
			sb.append(i2);
		}
		return sb.toString();
	}

	/**
	 * 注意和 stringToByteInt方法是有区别的 byteInt的范围最大是127
	 * 如：
	 * ’ ->8217
	 * 
	 * @return
	 */
	public static int charToInt(char c) {
		int i=c;
		return i;
	}
	/**
	 * byte转为对应int的串(常用于分析File文件的字节内容)
	 * 注意此方法和直接new String(bytes) 的作用是不一样的
	 * 
	 * 比如转换byte[]{53,90}
	 * byteToIntLine(b)的结果是53,90
	 * String(b)的结果是5Z
	 * 
	 * @param bytes
	 * @return
	 */
	public static String byteToIntLine(byte[] bytes) {
		StringBuilder sb=new StringBuilder();
		int idx=0;
		for(byte i:bytes) {
			if(0!=idx++) {
				sb.append(",");
			}
			int i2=i;
			sb.append(i2);
		}
		return sb.toString();
	}
	/**
	 * 这方法似乎不能转大于127的int值
	 * 大于127时可以用byteIntToString3()方法
	 * @param bytes
	 * @return
	 */
	public static String byteIntToString(int[] bytes) {
		byte[] b=new byte[bytes.length];
		for(int i=0;i<bytes.length;i++) {
			b[i]=(byte)bytes[i];
		}
		return new String(b);
	}
	/**
	 * c=Character.forDigit(a, b)  把a转换成b进制的c
	 * 
	 * 转换例子：
	 * 1. ({1,9,9,3},10)->1993
	 * 2. ({1,15,11,10},16)->1fba
	 * 

使用场景和注意事项
‌使用场景‌：当你需要将数字转换为特定进制的字符表示时，可以使用此方法。例如，在处理二进制、八进制或十六进制数据时。
‌注意事项‌：Character.forDigit方法只能用于转换0到radix-1之间的数字。如果数字不在这个范围内，方法会返回0。
此外，转换后的字符是根据ASCII码表进行转换的，如果数字大于ASCII码的最大值(127)，可能会得到意外结果‌1。

	 * @param bytes
	 * @return
	 * @deprecated 感觉此方法没什么用
	 */
	@Deprecated
	public static String byteIntToString2(int[] bytes,int radix) {
		char[] b=new char[bytes.length];
		for(int i=0;i<bytes.length;i++) {
//			b[i]=(byte)bytes[i];
			b[i]=Character.forDigit(bytes[i], radix);
		}
		return new String(b);
	}
	/**
	 * 对的,如比
	 * 90->Z  91->[  92->\
	 * @param bytes
	 * @return
	 */
	public static String byteIntToString3(int[] bytes) {
		char[] b=new char[bytes.length];
		for(int i=0;i<bytes.length;i++) {
//			b[i]=(byte)bytes[i];
			b[i]=(char)bytes[i];
		}
		return new String(b);
	}
	/**
	 * 效果和byteIntToString4差不多，区别未知
	 * @param bytes
	 * @return
	 * @deprecated 效果和byteIntToString4差不多，感觉应该没什么用
	 */
	@Deprecated
	public static String byteIntToString4(int[] bytes) {
//		char[] b=new char[bytes.length];
		StringBuilder sb=new StringBuilder(); 
		for(int i=0;i<bytes.length;i++) {
			char ch=(char)bytes[i];
			//char ch = 8216; // 这是单引号的ASCII码
			String str = Character.toString(ch);		 
			// 如果你想要确保是中文单引号
//			str = str.equals("'") ? "\u2018" : str;			 
//			System.out.println(str); // 输出中文单引号
			sb.append(str);
		}
		return sb.toString();
	}
	public static int getByteAppearTimes(int b,String s) {
		if(null==s) {return 0;}
		int r=0;
		for(byte i:s.getBytes()) {
			if(i==b) {
				r++;
			}
		}
		return r;
	}
	/**
	 * 来自 https://my.oschina.net/u/169390/blog/97495
	 * 但转换中文单引号 ’		-30,-128,-103 时似乎不正确
	 * int到byte[]
	 * @param i
	 * @return
	 */
	public static byte[] intToByteArray(int i) {   
		  byte[] result = new byte[4];   
		  //由高位到低位
		  result[0] = (byte)((i >> 24) & 0xFF);
		  result[1] = (byte)((i >> 16) & 0xFF);
		  result[2] = (byte)((i >> 8) & 0xFF); 
		  result[3] = (byte)(i & 0xFF);
		  return result;
		}

		/**
	 * 来自 https://my.oschina.net/u/169390/blog/97495
	 * 但转换中文单引号 ’		-30,-128,-103 	时似乎不正确
		 * byte[]转int
		 * @param bytes
		 * @return
		 */
		public static int byteArrayToInt(byte[] bytes) {
		       int value= 0;
		       //由高位到低位
		       for (int i = 0; i < 4; i++) {
		           int shift= (4 - 1 - i) * 8;
		           value +=(bytes[i] & 0x000000FF) << shift;//往高位游
		       }
		       return value;
		 }
		public static char intToChar(int i) {
			char c=(char) i;
			return c;
		}
    public static void main(String[] args)
    {
//    	System.out.println(stringToByteInt("\r\n"));
    	System.out.println(new String(new byte[] {'e',-17,-65,-67,'e'}));
    	System.out.println(new String(new byte[] {'e','e','e','e','e'}));
    	
    }
}
