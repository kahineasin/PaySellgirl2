package org.sgGameHelper.mp3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.sellgirl.sgJavaHelper.SGAction;
import com.sellgirl.sgJavaHelper.SGByteHelper;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.config.SGDataHelper.LocalDataType;
 
/**
 *
 * 这个类用来获取一首音乐的详细信息
 * @author pengqinping
 */
public class Mp3
{
   
    private RandomAccessFile ran = null;
   
    private static File file = null;
   
    /**
     * @param file(我们创建当前类的对象的时候初始化我们传过来的music对象)
     * @throws FileNotFoundException
     */
    public Mp3(File file) throws FileNotFoundException
    {
        super();
        Mp3.setFile(file);
        System.out.println(file.length() + "字节");
        System.out.println(((double) file.length()) / (1024 * 1024));
        ran = new RandomAccessFile(file, "r");
        System.out.println("文件装载完毕");
    }
   
    /**
     *
     * 获取音乐的详细信息并且保存在map中
     * @param file
     * @return 返回类型说明
     */
    public static Map<String, String> getMusicMsg(File file)
    {
        Map<String, String> map = new HashMap<String, String>();
        try
        {
            Mp3 read = new Mp3(file);
            byte[] buffer = new byte[128];
            read.ran.seek(read.ran.length() - 128);
            read.ran.read(buffer);
            SongInfo info = new SongInfo(buffer);
            System.out.println("Name:"+info.getSongName()+";artist:"+info.getArtist()+";album:"+info.getAlbum());
            map.put("musicname", info.getSongName());
            map.put("musicauthor", info.getArtist());
            map.put("musicalbum", info.getAlbum());
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return map;
    }

    public static SongInfo getMusicMsg2(File file)
    {
        Map<String, String> map = new HashMap<String, String>();
        try
        {
            Mp3 read = new Mp3(file);
            byte[] buffer = new byte[128];
            read.ran.seek(read.ran.length() - 128);
            read.ran.read(buffer);
            SongInfo info = new SongInfo(buffer);
            return info;
//            System.out.println("Name:"+info.getSongName()+";artist:"+info.getArtist()+";album:"+info.getAlbum());
//            map.put("musicname", info.getSongName());
//            map.put("musicauthor", info.getArtist());
//            map.put("musicalbum", info.getAlbum());
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
   
    public static void setFile(File file)
    {
        Mp3.file = file;
    }
   
    public static File getFile()
    {
        return file;
    }
   
    
    public static String[] songs=new String[] {
    		"D:/cache/mp3/001.电灯胆_邓丽欣1.mp3",//musicPlayer保存的版本 无声
    		"D:/cache/mp3/001.电灯胆_邓丽欣.mp3",//原版本 无声
    		"D:/cache/mp3/002.电灯胆_Benjamin原声.mp3",//无声
    		"D:/cache/mp3/003.电灯胆_邓丽欣_伴奏.mp3",//有声
    		"D:/cache/mp3/004.Lady of the Pier(Du Mei Shin).mp3", //56毫秒就触发complete
    		"D:/cache/mp3/005.Lady of the Pier(Du Mei Shin)_ben制作消音伴奏.mp3",//有声
    		"D:/cache/mp3/017.寂寞_谢容儿_伴奏.mp3",//有声
    		"D:/cache/mp3/019.越长大越孤单_伴奏.mp3"//有声
    };
    public static String[] getSongs() {
//    	ArrayList<String> songList=new ArrayList<String>();
//		for(File f:(new File("D:\\cache\\mp3")).listFiles()) {
//			songList.add(f.getAbsolutePath());
//		}
//		return SGDataHelper.ObjectToArray(songList, String.class);
		return getFiles("D:\\cache\\mp3");
    }
//    public static String[] getFilePaths(String folder) {
//    	ArrayList<String> songList=new ArrayList<String>();
//		for(File f:(new File(folder)).listFiles()) {
//			songList.add(f.getAbsolutePath());
//		}
//		return SGDataHelper.ObjectToArray(songList, String.class);
//    }

    public static String[] getFiles(String folder) {
    	ArrayList<String> songList=new ArrayList<String>();
		for(File f:(new File(folder)).listFiles()) {
			songList.add(f.getAbsolutePath());
		}
		return SGDataHelper.ObjectToArray(songList, String.class);
    }
    public static ArrayList<Mp3Model> getSongModels(){
    	ArrayList<Mp3Model> r=new ArrayList<Mp3Model>();
    	Mp3Model mp3=new Mp3Model();
    	mp3.setFileName("001.电灯胆_邓丽欣.mp3");
		mp3.setNoSound(true);
    	r.add(mp3);    	
    	mp3=new Mp3Model();
    	mp3.setFileName("002.电灯胆_Benjamin原声.mp3");
		mp3.setNoSound(true);
    	r.add(mp3);
    	mp3=new Mp3Model();
    	mp3.setFileName("003.电灯胆_邓丽欣_伴奏.mp3");
		mp3.setNoSound(false);
    	r.add(mp3);
    	mp3=new Mp3Model();
    	mp3.setFileName("004.Lady of the Pier(Du Mei Shin).mp3");
    	mp3.setFastComplete(true);
    	r.add(mp3);
    	mp3=new Mp3Model();
    	mp3.setFileName("005.Lady of the Pier(Du Mei Shin)_ben制作消音伴奏.mp3");
		mp3.setNoSound(false);
    	r.add(mp3);
    	mp3=new Mp3Model();
    	mp3.setFileName("010.AliceGoodNight_伴奏.mp3");
		mp3.setNoSound(false);
    	r.add(mp3);
    	mp3=new Mp3Model();
    	mp3.setFileName("011.天真_弦子_伴奏.mp3");
		mp3.setNoSound(false);
    	r.add(mp3);
    	mp3=new Mp3Model();
    	mp3.setFileName("012.小幸运_伴奏.mp3");//这歌好像不能流播放
		mp3.setNoSound(false);
    	r.add(mp3);
    	mp3=new Mp3Model();
    	mp3.setFileName("013.小幸运_ben.mp3");
		mp3.setFastComplete(true);
    	r.add(mp3);
    	mp3=new Mp3Model();
    	mp3.setFileName("014.可惜我是水瓶座_ben.mp3");
		mp3.setNoSound(false);
    	r.add(mp3);
    	mp3=new Mp3Model();
    	mp3.setFileName("015.可惜我是水瓶座_伴奏.mp3");
		mp3.setNoSound(false);
    	r.add(mp3);
    	mp3=new Mp3Model();
    	mp3.setFileName("016.寂寞_谢容儿.mp3");
		mp3.setNoSound(false);
    	r.add(mp3);
    	mp3=new Mp3Model();
    	mp3.setFileName("017.寂寞_谢容儿_伴奏.mp3");
		mp3.setNoSound(false);
    	r.add(mp3);
    	mp3=new Mp3Model();
    	mp3.setFileName("019.越长大越孤单_伴奏.mp3");
		mp3.setNoSound(false);
    	r.add(mp3);
//    	mp3=new Mp3Model();
//    	mp3.setFileName("");
//    	r.add(mp3);
    	return r;
    }
    /**
     * 通过此方法分析mp3不能播放的原因 --benjamin todo
     * @param args
     * @throws FileNotFoundException 
     */
    public static void main(String[] args) throws FileNotFoundException
    {
//    	ReadMp3Inf.updateMp3Model("D:/cache/mp3",getSongModels().get(11));//16(11)异常 17(12)正常
//    	if(true) {
//    		return;
//    	}
    	
//    	String aa="\r\n";
//    	String bb=new String(new byte[] {13,10});
//    	boolean cc=aa.equals(bb);//true
    
//    	String dd=new String
//    	SGDataHelper.Byte
    	
  	 	ArrayList<Mp3Model> songs=getSongModels();
  	 	int cnt=songs.size();
        for(Mp3Model song:songs) {     
            System.out.println("-------------"+song.getFileName()+"-------------");
        	ReadMp3Inf.updateMp3Model("D:/cache/mp3",song);
        	System.out.println("--------------------------");
        }

//  	 	ArrayList<Mp3Model> songs=getSongModels();
//  	 	ArrayList<SongInfo> list=new ArrayList<SongInfo>();
//  	 	int cnt=songs.size();
//        for(Mp3Model song:songs) {     
//            System.out.println("-------------"+song.getFileName()+"-------------");
//            SongInfo model=Mp3.getMusicMsg2(new File("D:/cache/mp3/"+song.getFileName()));
//            if(null==model) {
//            	System.out.println("------------null--------------");
//            }else {
//            	list.add(model);
//            	System.out.println("-------------ok-------------");
//            }
//        }
        
        //挑选异常字段来分析
//        Mp3Model errorModel=songs.get(3);
//        errorModel.setSongName(SGDataHelper.escapeCsv3(errorModel.getSongName()));
//        String err=errorModel.getSongName();
        
//        errorModel.setSongName("");//就是songName导致换行了,计算一下，它有什么字符是在其它行都没有出现的吧
        
        SGDataHelper.listToCsv(songs, csvPath3);
//        SGDataHelper.listToCsv(list, csvPath2);
        
//        System.out.println("--------------------------");
//        System.out.println(err);
//        System.out.println(SGByteHelper.stringToByteInt(err));
        
        
        //exportCsv2();

// 	   List<String> data = new ArrayList<>();
// 	   data.add("John,30,john@example.com 中国");
// 	   data.add("Alice,25,alice@example.com 中国");
//        SGDataHelper.WriteLinesToFile(data, csvPath,false);//乱码
//        SGDataHelper.WriteLocalTxt("中人", "aabb.txt", LocalDataType.Deletable);//不乱码
        //SGDataHelper.WriteLocalTxt("中人", "output.csv", LocalDataType.Deletable);//中文乱码. 后缀名有影响？
    	

//        String csvFile = "D:\\github\\PaySellgirl\\sgGameHelper\\DeletableLocalData\\Txt\\output.csv";
//        String data = "编号,姓名,年龄\n" +
//                      "1,张三,28\n" +
//                      "2,李四,35\n";
// 
//        try (java.io.Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), StandardCharsets.UTF_8))) {
//        	writer.write(new String(new byte[] {-17,-69,-65}).toCharArray());//有这句不会中文乱码
//            writer.write(data);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        
    }
   private static String csvPath="D:\\cache\\csv\\output.csv";
   private static String csvPath2="D:\\cache\\csv\\output2.csv";
   private static String csvPath3="D:\\cache\\csv\\output3.csv";
   public static void exportCsv() {
	   String fileName = "D:\\cache\\csv\\output.csv";
	   String header = "Name,Age,Email";
	   List<String> data = new ArrayList<>();
	   data.add("John,30,john@example.com");
	   data.add("Alice,25,alice@example.com");

  	 	//ArrayList<Mp3Model> songs=getSongModels();
	    
	   try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
	       writer.write(header);
	       writer.newLine();
	       for (String row : data) {
	           writer.write(row);
	           writer.newLine();
	       }
	   } catch (IOException e) {
	       e.printStackTrace();
	   }
   }
   
   public static void exportCsv2() {
	   String filePath = "D:\\cache\\csv\\output.csv";
//	   String header = "Name,Age,Email";
//	   List<String> data = new ArrayList<>();
//	   data.add("John,30,john@example.com");
//	   data.add("Alice,25,alice@example.com");

  	 	ArrayList<Mp3Model> songs=getSongModels();
  	 	
  	 	SGDataHelper.listToCsv(songs, filePath);
//  	 	SGDataHelper.EachObjectProperty(songs, null);
//	    
////	   try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
//			try (// FileWriter writer = new FileWriter(filePath,false);
//					// BufferedWriter out = new BufferedWriter(writer)
//					BufferedWriter out = new BufferedWriter(
//							new OutputStreamWriter(new FileOutputStream(new File(filePath)), SGDataHelper.encoding))// 如果不写utf8,在cmd运行时,保存的文件会变成ansi
//			) {
//				out.write(new String(new byte[] {-17,-69,-65}).toCharArray());//有这句不会中文乱码
////	       writer.write(header);
////	       writer.newLine();
//				int i=0;
//				ArrayList<String> heads=new ArrayList<String>();
//	       for (Mp3Model row : songs) {	    	
//	    	   ArrayList<String> values=new ArrayList<String>();
//	    	   final int i2=i;
//	    	   SGDataHelper.EachObjectProperty(row,
//	    			   new com.sellgirl.sgJavaHelper.SGAction<Integer, String, Object>(){
//
//						@Override
//						public void go(Integer t1, String t2, Object t3) {
//							if(0==i2) {
//								heads.add(t2);
//							}
//							values.add(SGDataHelper.ObjectToString(t3));
//						}});
////	    	   String.join(",", values);
////	           writer.write(JSON.toJSONString(row));
//	    	   if(0==i) {
//		    	   out.write(String.join(",", heads));}
//	    	   out.write(String.join(",", values));
//	    	   out.newLine();
//	    	   i++;
//	       }
//	   } catch (IOException e) {
//	       e.printStackTrace();
//	   }
   }   
}
 
 