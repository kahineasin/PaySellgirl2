//package org.pfHelperSwt;
//
//import junit.framework.TestCase;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//
//public class UncheckUrl001 extends TestCase {
//    //只能打开http的文件?
//    public void readFileFromUrl(String filePath) throws Exception{
//        URL url = new URL(filePath);
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
//        String line;
//        while ((line = reader.readLine()) != null) {
//            builder.append(line);
//            builder.append("\r\n");
//        }
//        reader.close();
//        inputStreamReader.close();
//        inputStream.close();
//        System.out.println("读取成功：\r\n" + builder);
//    }
//    public void readLocal(URL url) throws IOException {
//        //URL url = new URL("D:\\\\1.txt");
//        try(InputStream is = url.openStream()){
//            InputStreamReader inputStreamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
//            BufferedReader reader = new BufferedReader(inputStreamReader);
//            StringBuilder builder = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                builder.append(line);
//                builder.append("\r\n");
//            }
//            reader.close();
//            inputStreamReader.close();
//            is.close();
//            System.out.println("读取成功：\r\n" + builder);
//        }
//        //原文链接：https://blog.csdn.net/qq_31183071/article/details/106934681
//    }
//    public void testUrl() throws Exception {
//        //readFileFromUrl("D:\\\\1.txt");//报错
//        //readLocal(new URL("D:\\\\1.txt"));//报错
//        readLocal(new URL("file:/D:/1.txt"));//ok
//        //readFileFromUrl("https://html.sellgirl.com/js/sellgirl.js");//ok
//        //readFileFromUrl("jar:file:lib/lz4-1.3.0-sources.jar!/");//error sun.net.www.protocol.jar.JarURLConnection cannot be cast to java.net.HttpURLConnection
////        URL url = new URL("jar:file:lib/lz4-1.3.0-sources.jar!/");
////        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//        readLocal(new URL("jar:file:lib/lz4-1.3.0-sources.jar!/"));//ok
//
//    }
//}
