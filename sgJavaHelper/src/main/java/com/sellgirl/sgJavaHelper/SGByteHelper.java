package com.sellgirl.sgJavaHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import com.sellgirl.sgJavaHelper.SGByteHelper.SGEncoding;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/**
 * 所有byte相关的方法都集合到这里，因为byte很重要
 */
public class SGByteHelper {
	private static final String TAG="SGByteHelper";

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
	/**
	 * 见SGFileHead
	 * @param file
	 * @param b
	 * @param head
	 */
	public static void writeFileFromByte(File file, byte[] b,byte[] head){//String path) {
	        try (FileOutputStream fileInputStream = new FileOutputStream(file)) {
	            if(null!=head) {fileInputStream.write(head);}
	            fileInputStream.write(b);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
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
	public static byte[] stringToByteInt2(String s,SGEncoding encode) {
		try {
			return s.getBytes(encode.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String stringToByteInt(String s,SGEncoding encode) {
		StringBuilder sb=new StringBuilder();		
		int idx=0;
		try {
			for(byte i:s.getBytes(encode.toString())) {
				if(0!=idx++) {
					sb.append(",");
				}
				int i2=i;
				sb.append(i2);
			}
		} catch (UnsupportedEncodingException e) {
			SGDataHelper.getLog().printException(e, TAG);
		}
		return sb.toString();
	}

	/**
	 * 注意返回的String是包含了文件头的,比如unicode串会有 0xfeff
	 * @param s
	 * @param encode
	 * @return
	 */
	public static String stringToByteHex(String s,SGEncoding encode) {
		StringBuilder sb=new StringBuilder();		
		int idx=0;
		try {
			for(byte i:s.getBytes(encode.toString())) {//这里会加文件头
				if(0!=idx++) {
					sb.append(",");
				}
//				int i2=i;
				int i2=0>i?(i+256):i;
				sb.append(Integer.toHexString(i2));
			}
		} catch (UnsupportedEncodingException e) {
			SGDataHelper.getLog().printException(e, TAG);
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
	 * 注意:
	 * 1. 此方法和直接new String(bytes) 的作用是不一样的
	 * 
	 *   比如转换byte[]{53,90}
	 *   byteToIntLine(b)的结果是53,90
	 *   String(b)的结果是5Z
	 * 
	 * 2. 此方法和print(byte)的结果可能一致, 比如
	 *   byte b='0';
	 *   System.out.println(b);  //48
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
	//测试用于显示
	public static String intToLine(int[] bytes) {
		StringBuilder sb=new StringBuilder();
		int idx=0;
		for(int i:bytes) {
			if(0!=idx++) {
				sb.append(",");
			}
//			int i2=i;
			sb.append(i);
		}
		return sb.toString();
	}
	public static String intToHexLine(int[] bytes) {
		StringBuilder sb=new StringBuilder();
		int idx=0;
		for(int i:bytes) {
			if(0!=idx++) {
				sb.append(",");
			}
//			int i2=i;
			sb.append(Integer.toHexString(i));
		}
		return sb.toString();
	}
	public static String byteToHexLine(byte[] bytes) {
		StringBuilder sb=new StringBuilder();
		int idx=0;
		for(byte i:bytes) {
			if(0!=idx++) {
				sb.append(",");
			}
			int i2=0>i?(i+256):i;
			sb.append(Integer.toHexString(i2));
		}
		return sb.toString();
	}
	/**
	 * 相当于"编码与转码_pf.xlsx"中 列1->列1
	 * @param b
	 * @return
	 */
	public static String byteToString(byte b) {
		return new String(new byte[] {b});
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
	public static String byteIntToString(int[] bytes,SGEncoding encode) {
		byte[] b=new byte[bytes.length];
		for(int i=0;i<bytes.length;i++) {
			b[i]=(byte)bytes[i];
		}
		try {
			return new String(b,encode.toString());
		} catch (UnsupportedEncodingException e) {
			SGDataHelper.getLog().print(e);
			return null;
		}
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
	 * 效果和byteIntToString差不多，区别未知.
	 * 其实有区别,输入-28,-72,-83,输出不是'中'字
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
		
		/**
		 * 
		 * @param i
		 * @return new String(new char[] {0x4e2d }) 可以把结果转为unicode字符串 , 或调用intToUnicodeString(...)
		 */
		public static char intToChar(int i) {
			char c=(char) i;
			return c;
		}
		/**
		 * 按unicode编码来转换
		 * 实际上是,把unicode的byte转成了utf8的String(由于后面常常是统一处理utf8,比如txt)
		 * @param i
		 * @return
		 */
		public static String intToUnicodeString(int i) {
			return new String(new char[] {intToChar(i) });
		}
		/**
		 * 转换字节
		 * @throws UnsupportedEncodingException
		 */
		public static byte[] convertByte(byte[] a,SGEncoding src,SGEncoding dst) throws UnsupportedEncodingException{
			String s1=new String(a,src.toString());
			//String的内部byte转换原理是:以 Unicode 码点（Code Point）作为唯一的“中转站”或“通用语言” 做中转
			byte[] b=s1.getBytes(dst.toString());
			return b;
			
		}

//		public static String getCodePoint(String str) {
////			IntStream is=str.codePoints();
////			is.forEach(new IntConsumer() );
////			for(i:is) {
////				
////			}
//			int cnt=str.codePointCount(0, str.length()) ;
//			StringBuilder sb=new StringBuilder(); 
//			for(int i=0;cnt>i;i++) {
//				int c=Character.codePointAt(str, i); 
//				sb.append(c);
//			}
//			return sb.toString();
//		}
		public static int[] getCodePoint(String str) {
//			IntStream is=str.codePoints();
//			is.forEach(new IntConsumer() );
//			for(i:is) {
//				
//			}
			int cnt=str.codePointCount(0, str.length()) ;
			int[] r=new int[cnt];
			StringBuilder sb=new StringBuilder(); 
			for(int i=0;cnt>i;i++) {
				int c=Character.codePointAt(str, i); 
//				sb.append(c);
				r[i]=c;
			}
//			return sb.toString();
			return r;
		}
		
		public enum SGEncoding{
			/**
			 * 即windows的txt文件的 UTF-16 LE 格式
			 */
			UNICODE,
			UNICODE_BE,
			/**
			 * no bom
			 */
			UTF8,
			/**
			 * EF BB BF
			 */
			UTF8_withBom
		}
		/**
		 * 文件字节码的头部
		 */
		public static class SGFileHead{
			public static byte[] JPG=new byte[] {(byte) 0xff, (byte) 0xd8,(byte) 0xff};
			public static byte[] TxtUnicode=new byte[] {(byte) 0xff, (byte) 0xfe};
			public static byte[] TxtUnicodeBE=new byte[] {(byte) 0xfe, (byte) 0xff};
			public static byte[] TxtUTF8withBom=new byte[] {(byte) 0xEF , (byte) 0xBB ,(byte) 0xBF};
		}
    public static void main(String[] args)
    {
//    	System.out.println(stringToByteInt("\r\n"));
//    	System.out.println(new String(new byte[] {'e',-17,-65,-67,'e'}));
//    	System.out.println(new String(new byte[] {'e','e','e','e','e'}));
		System.out.println(SGByteHelper.byteIntToString(new int[] {-17,-65,-67},SGEncoding.UNICODE));//
		System.out.println(SGByteHelper.byteIntToString(new int[] {-17,-65,-67},SGEncoding.UTF8));//
    	
    }
}
