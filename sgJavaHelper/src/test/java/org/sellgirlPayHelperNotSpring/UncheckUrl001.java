package org.sellgirlPayHelperNotSpring;

import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import com.sellgirl.sgJavaHelper.SGByteHelper;
import com.sellgirl.sgJavaHelper.SGHttpHelper;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class UncheckUrl001  extends TestCase {
    public void readFileFromUrl(String filePath) throws Exception{
        URL url = new URL(filePath);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(6000);
        urlConnection.setReadTimeout(6000);
        if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("文件读取失败");
        }
        InputStream inputStream = urlConnection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
            builder.append("\r\n");
        }
        reader.close();
        inputStreamReader.close();
        inputStream.close();
        System.out.println("读取成功：\r\n" + builder);
    }

    //65533 65533 65533 65533 0 16 74 70 73 70 0 1 1 1 0 96 0 96 0 0 65533 65533 16 65533 69 120 105 ...
    //注意图片用UTF8编码后再输出,就分辨不出是图片了
    @Deprecated
    public void testStream() throws Exception{
//        URL url = new URL("http://mp3.sellgirl.com/img/web_sasha_thumbnail.jpg");
        URL url = new URL("http://mp3.sellgirl.com/img/web_sasha_thumbnail.jpg");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(6000);
        urlConnection.setReadTimeout(6000);
        if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("文件读取失败");
        }
        InputStream inputStream = urlConnection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder builder = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            builder.append(line);
//            builder.append("\r\n");
//        }
        int line;
        while ((line = reader.read()) >-1) {
            builder.append(line);
//            builder.append(Integer.toHexString(line));
            builder.append(" ");
        }
//        SGHttpHelper.HttpGet(getName(), getName())
        inputStreamReader.close();
        inputStream.close();
        System.out.println("读取成功：\r\n" + builder);
    }
    
//
//    public void printStream(URL url) throws Exception{
////        URL url = new URL("http://mp3.sellgirl.com/img/web_sasha_thumbnail.jpg");
//        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//        urlConnection.setConnectTimeout(6000);
//        urlConnection.setReadTimeout(6000);
//        if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//            throw new RuntimeException("文件读取失败");
//        }
//        InputStream inputStream = urlConnection.getInputStream();
//        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
//        BufferedReader reader = new BufferedReader(inputStreamReader);
//        StringBuilder builder = new StringBuilder();
////        String line;
////        while ((line = reader.readLine()) != null) {
////            builder.append(line);
////            builder.append("\r\n");
////        }
//        int line;
//        while ((line = reader.read()) >-1) {
//            builder.append(line);
////            builder.append(Integer.toHexString(line));
//            builder.append(" ");
//        }
////        SGHttpHelper.HttpGet(getName(), getName())
//        inputStreamReader.close();
//        inputStream.close();
//        System.out.println("读取成功：\r\n" + builder);
//    }

    //ff d8 ff e0 0 10 4a 46 49 46 0 1 1 1 ... 
    //ff d8 ff 能看出是jpg格式
    //非常推荐用此方法来分析文件的编码
    public void testStream2() throws Exception{
    	//ff d8 ff e0 0 10 4a 46 49 46 0 1 1 1 ...
//        URL url = new URL("http://mp3.sellgirl.com/img/web_sasha_thumbnail.jpg");
//        URL url = new URL("file:/D:/download/web_sasha_thumbnail.jpg");
    	//内容为"a中"的txt输出61 e4 b8 ad //16即a,e4 b8 ad 即 -28,-72,-83 即 SGByteHelper.stringToByteInt("中")	
//        URL url = new URL("file:/D:/cache/16/stream1.txt");
        SGDataHelper.printStream(new URL("file:/D:/cache/16/stream1.txt"),1000);
//        SGDataHelper.printStream(new URL("file:/D:/cache/16/stream2.txt"),100);
////        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//        URLConnection urlConnection = url.openConnection();
//        urlConnection.setConnectTimeout(6000);
//        urlConnection.setReadTimeout(6000);
////        if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
////            throw new RuntimeException("文件读取失败");
////        }
//        InputStream inputStream = urlConnection.getInputStream();
////        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
////        BufferedReader reader = new BufferedReader(inputStreamReader);
//        StringBuilder builder = new StringBuilder();
//////        String line;
//////        while ((line = reader.readLine()) != null) {
//////            builder.append(line);
//////            builder.append("\r\n");
//////        }
//        int line;
//        while ((line = inputStream.read()) >-1) {
////            builder.append(line);
//            builder.append(Integer.toHexString(line));
//            builder.append(" ");
//        }
////        SGHttpHelper.HttpGet(getName(), getName())
////        inputStreamReader.close();
//        inputStream.close();
//        System.out.println("读取成功：\r\n" + builder);
    }

    public void testStream3() throws Exception{

    	SGDataHelper.streamTo(new URL("http://192.168.10.105:8080/stream.ts"), 
    			new URL("file:/D:/cache/16/stream2.txt"));
//    	//ff d8 ff e0 0 10 4a 46 49 46 0 1 1 1 ...
////        URL url = new URL("http://mp3.sellgirl.com/img/web_sasha_thumbnail.jpg");
////        URL url = new URL("file:/D:/download/web_sasha_thumbnail.jpg");
//    	//内容为"a中"的txt输出61 e4 b8 ad //16即a,e4 b8 ad 即 -28,-72,-83 即 SGByteHelper.stringToByteInt("中")	
//        URL url = new URL("file:/D:/cache/16/1.txt");
//        URLConnection urlConnection = url.openConnection();
//        urlConnection.setConnectTimeout(6000);
//        urlConnection.setReadTimeout(6000);
//        InputStream inputStream = urlConnection.getInputStream();    	
//        URL url2 = new URL("file:/D:/cache/16/2.txt");
//        File file=(new File(url2.toURI()));
//        OutputStream outStream=new FileOutputStream(file);
////        URLConnection urlConnection2 = url2.openConnection();//不能写        
//        int line;
//        while ((line = inputStream.read()) >-1) {
//            outStream.write(line);
//        }
//        inputStream.close();
//        outStream.close();
    }

//    public void toStream3(URL url,URL url2) throws Exception{
//    	//ff d8 ff e0 0 10 4a 46 49 46 0 1 1 1 ...
////        URL url = new URL("http://mp3.sellgirl.com/img/web_sasha_thumbnail.jpg");
////        URL url = new URL("file:/D:/download/web_sasha_thumbnail.jpg");
//    	//内容为"a中"的txt输出61 e4 b8 ad //16即a,e4 b8 ad 即 -28,-72,-83 即 SGByteHelper.stringToByteInt("中")	
//        URL url = new URL("file:/D:/cache/16/1.txt");
//        URLConnection urlConnection = url.openConnection();
//        urlConnection.setConnectTimeout(6000);
//        urlConnection.setReadTimeout(6000);
//        InputStream inputStream = urlConnection.getInputStream();    	
//        URL url2 = new URL("file:/D:/cache/16/2.txt");
//        File file=(new File(url2.toURI()));
//        OutputStream outStream=new FileOutputStream(file);
////        URLConnection urlConnection2 = url2.openConnection();//不能写        
//        int line;
//        while ((line = inputStream.read()) >-1) {
//            outStream.write(line);
//        }
//        inputStream.close();
//        outStream.close();
//    }
    public void testUrl() throws Exception {
        //readFileFromUrl("D:\\\\1.txt");//报错
        //readLocal(new URL("D:\\\\1.txt"));//报错
        System.out.println(SGDataHelper.readLocalResource2(new URL("file:/D:/1.txt"),2));
        System.out.println(SGDataHelper.StringIsNullOrWhiteSpace(SGDataHelper.readLocalResource2(new URL("file:/D:/1.txt"),2)));
        System.out.println(SGDataHelper.readLocalResource2(new URL("file:/D:/2.txt"),2));
        System.out.println(SGDataHelper.StringIsNullOrWhiteSpace(SGDataHelper.readLocalResource2(new URL("file:/D:/2.txt"),2)));
        System.out.println(SGDataHelper.readLocalResource2(new URL("https://html.sellgirl.com/js/sellgirl.js"),2));
//        readLocal(new URL("file:/D:/1.txt"));//ok
//        //readFileFromUrl("https://html.sellgirl.com/js/sellgirl.js");//ok
//        //readFileFromUrl("jar:file:lib/lz4-1.3.0-sources.jar!/");//error sun.net.www.protocol.jar.JarURLConnection cannot be cast to java.net.HttpURLConnection
////        URL url = new URL("jar:file:lib/lz4-1.3.0-sources.jar!/");
////        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//        readLocal(new URL("jar:file:lib/lz4-1.3.0-sources.jar!/"));//ok

    }
    public void testUrl2() throws Exception {
//        //readFileFromUrl("D:\\\\1.txt");//报错
//        //readLocal(new URL("D:\\\\1.txt"));//报错
//        URL u1=new URL("file:/D:/1.txt");//存在
//        URL u2=new URL("file:/D:/2.txt");//不存在
//        //URL u3=new URL("https://html.sellgirl.com/js/sellgirl.js"); //存在
//        URL u4=new URL("file:/D:/clickhouse-native-jdbc-1.7-stable.jar");//存在
//        URL u5=new URL("file:lib/clickhouse-native-jdbc-1.7-stable.jar"); //存在
//        URL u6=new URL("file:/./lib/clickhouse-native-jdbc-1.7-stable.jar"); //不存在
//        URL u7=new URL("file:/lib/clickhouse-native-jdbc-1.7-stable.jar"); //存在
        URL u8 = new URL("jar:file:lib/clickhouse-native-jdbc-1.7-stable.jar!/"); //not ok
        URL u9=new URL("jar:file:/D:/eclipse_release/javaDemo4/javaDemo4-0.0.1-jar-with-dependencies.jar!/jar/mysql-connector-java-8.0.23.jar");//ok

        URL u10 = new URL("jar:file:lib/clickhouse-native-jdbc-1.7-stable.jar!/examples/BatchQuery.class"); //ok
        URL u11 = new URL("jar:file:lib/lz4-1.3.0-sources.jar!/net/jpountz/lz4/LZ4Factory.class"); //not ok
        URL u12 = new URL("jar:file:lib/lz4-1.3.0-sources.jar!/net/jpountz/lz4/LZ4Factory2.class"); //not ok
        URL u13 = new URL("jar:file:lib/lz4-1.3.0-sources.jar!/net/jpountz/lz4/LZ4Factory.java"); //ok
        URL u14 = new URL("jar:file:lib/lz4-1.3.0.jar!/net/jpountz/lz4/LZ4Factory.class"); // ok

//        System.out.println("----------1----------");
//        System.out.println(PFDataHelper.readLocalResource2(u1,2));
//        System.out.println(PFDataHelper.isUrlExist(u1));
//        System.out.println("----------2----------");
//        System.out.println(PFDataHelper.readLocalResource2(u2,2));
//        System.out.println(PFDataHelper.isUrlExist(u2));
////        System.out.println(PFDataHelper.readLocalResource2(u3,2));
////        System.out.println(PFDataHelper.isUrlExist(u3));
//        System.out.println("----------4----------");
//        System.out.println(PFDataHelper.readLocalResource2(u4,2));
//        System.out.println(PFDataHelper.isUrlExist(u4));
//        System.out.println("----------5----------");
//        System.out.println(PFDataHelper.readLocalResource2(u5,2));
//        System.out.println(PFDataHelper.isUrlExist(u5));
//        System.out.println("----------6----------");
//        System.out.println(PFDataHelper.readLocalResource2(u6,2));
//        System.out.println(PFDataHelper.isUrlExist(u6));
//        System.out.println("----------7----------");
//        System.out.println(PFDataHelper.readLocalResource2(u7,2));
//        System.out.println(PFDataHelper.isUrlExist(u7));
        System.out.println("----------8----------");
        System.out.println(SGDataHelper.readLocalResource2(u8,2));
        System.out.println(SGDataHelper.isUrlExist(u8));
        System.out.println("----------9----------");
        System.out.println(SGDataHelper.readLocalResource2(u9,2));
        System.out.println(SGDataHelper.isUrlExist(u9));
        System.out.println("----------10----------");
        System.out.println(SGDataHelper.readLocalResource2(u10,2));
        System.out.println(SGDataHelper.isUrlExist(u10));
        System.out.println("----------11----------");
        System.out.println(SGDataHelper.readLocalResource2(u11,2));
        System.out.println(SGDataHelper.isUrlExist(u11));
        System.out.println("----------12----------");
        System.out.println(SGDataHelper.readLocalResource2(u12,2));
        System.out.println(SGDataHelper.isUrlExist(u12));
        System.out.println("----------13----------");
        System.out.println(SGDataHelper.readLocalResource2(u13,2));
        System.out.println(SGDataHelper.isUrlExist(u13));
        System.out.println("----------14----------");
        System.out.println(SGDataHelper.readLocalResource2(u14,2));
        System.out.println(SGDataHelper.isUrlExist(u14));

    }
    public void testUrlEncode() {
    	/**
002-粤剧 - 分飞燕 - 帝女花.mp3
002-%E7%B2%A4%E5%89%A7%20-%20%E5%88%86%E9%A3%9E%E7%87%95%20-%20%E5%B8%9D%E5%A5%B3%E8%8A%B1.mp3
002-%E7%B2%A4%E5%89%A7+-+%E5%88%86%E9%A3%9E%E7%87%95+-+%E5%B8%9D%E5%A5%B3%E8%8A%B1.mp3
002-%E7%B2%A4%E5%89%A7+-+%E5%88%86%E9%A3%9E%E7%87%95+-+%E5%B8%9D%E5%A5%B3%E8%8A%B1.mp3
002-%E7%B2%A4%E5%89%A7
+
    	 */
    	String srcName="002-粤剧 - 分飞燕 - 帝女花.mp3";
    	String correctEn="002-%E7%B2%A4%E5%89%A7%20-%20%E5%88%86%E9%A3%9E%E7%87%95%20-%20%E5%B8%9D%E5%A5%B3%E8%8A%B1.mp3";
    	String enName=SGDataHelper.getURLEncoderString(srcName);
    	String enName2=SGDataHelper.getURLEncoderString(srcName);
    	System.out.println(srcName);
    	System.out.println(correctEn);
    	System.out.println(enName);
    	System.out.println(enName2);
    	
    	//看出问题了,程序中空格转成了加号,但浏览器转为了%20
    	System.out.println(SGDataHelper.getURLEncoderString("002-粤剧"));
    	System.out.println(SGDataHelper.getURLEncoderString(" "));
    	System.out.println(SGDataHelper.getURLEncoderString("+"));
    	System.out.println(SGDataHelper.getURLEncoderString(" "));
    	
    	//奇怪的是
    	//http://mp3.sellgirl.com/mp3/s/Catch You Catch Me.mp3  在程序转为了下面
    	//http://mp3.sellgirl.com/mp3/s/Catch%20You%20Catch%20Me.mp3
    	//http://mp3.sellgirl.com/mp3/s/Catch+You+Catch+Me.mp3
    }
}
