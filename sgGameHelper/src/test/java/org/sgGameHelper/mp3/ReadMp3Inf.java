package org.sgGameHelper.mp3;

import java.io.IOException;
import java.io.RandomAccessFile;

import org.sgGameHelper.SGEncodeHelper;
import org.sgGameHelper.mp3.Mp3Model.MusicFormat;

import com.sellgirl.sgJavaHelper.SGByteHelper;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

import java.io.*;

public class ReadMp3Inf {
	public static void showInfo(String mp3Path) {
		try {
//			RandomAccessFile raf = new RandomAccessFile("薛之谦 - 丑八怪.mp3", "rw");
			RandomAccessFile raf = new RandomAccessFile(mp3Path, "rw");
			byte buf[] = new byte[128];//标签专辑信息
			byte buf1[] = new byte[4];
			raf.seek(raf.length() - 128);
			raf.read(buf);
			if(SGEncodeHelper.isUTF8(buf)) {
				System.out.println("is utf8");
			}else {
				System.out.println("not utf8");
			}
			raf.seek(45);// 指针往前走45个字节，从第46个字节开始
			raf.read(buf1);
			if (buf.length != 128) {
				System.err.println("MP3标签信息数据长度不合法!");
			}
			if (!"TAG".equalsIgnoreCase(new String(buf, 0, 3))) {
				System.err.println("MP3标签信息数据格式不正确!");
			}
			/**
			 * utf8会乱码
			 * 此类的作者说：
			 * 注意：如果直接用UTF-8格式读取字节流中的文字，解码方式不与编码gbk不一致，会导致出现乱码，
			 * 所以在读取字节时要加上gbk格式String SongName = new String(buf, 3, 30,"gbk");
			 * 
			 * 没完全明白，为什么编码是gbk的?
			 */
			String encode="gbk";//
			System.out.println(encode);
			String SongName = new String(buf, 3, 30,encode);
			System.out.println("歌名：" + SongName);
			String Singer = new String(buf, 33, 30,encode);
			System.out.println("作者：" + Singer);
			String Album = new String(buf, 63, 30,encode);
			System.out.println("专辑：" + Album);
			//System.out.println("文件长度：" + raf.length());

			String encode2=SGDataHelper.encoding;//
			System.out.println(encode2);
			String SongName2 = new String(buf, 3, 30,encode2);
			System.out.println("歌名：" + SongName2);
			String Singer2 = new String(buf, 33, 30,encode2);
			System.out.println("作者：" + Singer2);
			String Album2 = new String(buf, 63, 30,encode2);
			System.out.println("专辑：" + Album2);
			
			System.out.println("文件长度：" + raf.length());
			
			raf.close();
			// 将字节转化为二进制字符串
			String s1 = String.format("%8s", Integer.toBinaryString(buf1[0] & 0xFF)).replace(' ', '0');
			String s2 = String.format("%8s", Integer.toBinaryString(buf1[1] & 0xFF)).replace(' ', '0');
			String s3 = String.format("%8s", Integer.toBinaryString(buf1[2] & 0xFF)).replace(' ', '0');
			String s4 = String.format("%8s", Integer.toBinaryString(buf1[3] & 0xFF)).replace(' ', '0');
			String c = s1 + s2 + s3 + s4;
			char sc[] = c.toCharArray();
			System.out.println("音频版本：" + AudioVersion(sc[11], sc[12]));
			System.out.println("采样频率：" + SampleFrequency(sc[11], sc[12], sc[20], sc[21]));
			System.out.println("声道模式：" + ChannelMode(sc[24], sc[25]));

		} catch (IOException e) {
			System.err.println("发生异常:" + e);
			e.printStackTrace();
		}
	}

	public static void updateMp3Model(String mp3FolderPath,Mp3Model model) {
		try {
			String mp3Path=mp3FolderPath+"/"+model.getFileName();
			if(ReadMp3Inf.isMP3File(mp3Path)) {
				model.setMusicFormat(MusicFormat.MP3);
			}
//			RandomAccessFile raf = new RandomAccessFile("薛之谦 - 丑八怪.mp3", "rw");
			RandomAccessFile raf = new RandomAccessFile(mp3Path, "rw");
			byte buf[] = new byte[128];//标签专辑信息
			byte buf1[] = new byte[4];
			raf.seek(raf.length() - 128);
			raf.read(buf);
			
			//尝试去掉文件尾的-1
//			int i=1;
////			while(-1==buf[0]) {
////				raf.seek(raf.length() - 128-(i*128));
////				raf.read(buf);
////				String aa="aa";
////				i++;
////			}
//			if(-1==buf[0]) {
//				int lastCnt=0;
//				byte[] lastB=new byte[128];
//				int times=1;
//				boolean success=false;
//				while(true) {
//					if(-1==buf[buf.length-1]) {
//						//本次肯定不够
//						if(-1!=buf[0]) {
//							//留到下次累计的
//							lastCnt=0;
//							for(int j=0;i<buf.length;j++) {
//								if(-1!=buf[j]) {
//									lastCnt++;
//								}else {
//									break;
//								}
//							}
//						}else {
//							lastCnt=0;
//						}
//						raf.seek(raf.length() - 128-(times*128));
//						raf.read(buf);
//						times++;
//						lastB=buf.clone();
//						continue;
//					}else {
//						//本次可能会够
//						int tmpCnt=0;
//						for(int j=buf.length-1;j>=0;j--) {
//							if(-1!=buf[j]) {
//								tmpCnt++;
//							}else {
//								break;
//							}
//						}
//						if(tmpCnt+lastCnt>127) {
//							//够了
//							success=true;
//							break;
//						}else {
//
//							if(-1!=buf[0]) {
//								//留到下次累计的
//								lastCnt=0;
//								for(int j=0;i<buf.length;j++) {
//									if(-1!=buf[j]) {
//										lastCnt++;
//									}else {
//										break;
//									}
//								}
//							}else {
//								lastCnt=0;
//							}
//							raf.seek(raf.length() - 128-(times*128));
//							raf.read(buf);
//							times++;
//							lastB=buf.clone();
//							continue;
//						}
//					}
//				}
//				if(success) {
//					//合并上次和本次
//					byte[] merge=new byte[256];
//					for(int j=0;j<256;j++) {
//						if(j<128) {
//							merge[j]=buf[j];
//						}else {
//							merge[j]=lastB[j-128];
//						}
//					}
//					//把merge设回buf
//					int cur=255;
//					while(-1==merge[cur]) {
//						cur--;
//					}
//					if(126<cur) {
//						int k=127;
//						for(int j=cur;0<=j;j--) {
//							if(-1!=merge[j]) {
//								buf[k]=merge[j];
//								k--;
//							}else {
//								break;
//							}
//						}
//					}
//				}
//			}
//			String s=SGByteHelper.stringToByteInt(mp3Path)

//			//这样往前找到底好不好，待验证
//			//感觉没必要，只有第16首mp3有影响
//			if(false&&-1==buf[0]) {
//				int cur=1;
//				boolean ok=false;
//				long lastPos=-1;
////				byte[] singer=new byte[] {-24,-80,-94,-27,-82,-71,-27,-124,-65};//谢容儿
//				byte[] singer=new byte[] {84,65,71};//TAG
////				byte[] singer=new byte[] {-24,-80,-94};//谢
//				
//				while(true) {
//					for(int i=0;i<128-singer.length+1;i++) {
//						//找歌手名试试
//						boolean b=true;
//						for(int j=0;j<singer.length;j++) {
//							if(singer[j]==buf[i+j]) {}
//							else {b=false;break;}
//						}
//						if(b) {
//							String aa="aa";
//							raf.seek(lastPos+i);
////							raf.seek(i);
//							raf.read(buf);
//							ok=true;
//							break;
//						}
//					}	
//					if(true==ok) {
//						break;
//					}
//					lastPos=raf.length() - 128-(128*cur);
//					if(0>lastPos) {
//						break;
//					}
//					raf.seek(lastPos);
//					raf.read(buf);
//					cur++;
//				}
//			}
			
			if(SGEncodeHelper.isUTF8(buf)) {
				System.out.println("is utf8");//实测全部mp3的tag都不是utf8, 所以可能是gbk之类的，也可能是英文
			}else {
				System.out.println("not utf8");
			}
			raf.seek(45);// 指针往前走45个字节，从第46个字节开始
			raf.read(buf1);
			boolean b1=true;
			if (buf.length != 128) {
				System.err.println("MP3标签信息数据长度不合法!");
				b1=false;
			}
			boolean b2=true;
			if (!"TAG".equalsIgnoreCase(new String(buf, 0, 3))) {
				System.err.println("MP3标签信息数据格式不正确!");
				b2=false;
			}
			model.setHasTag(b1&&b2);
			/**
			 * utf8会乱码
			 * 此类的作者说：
			 * 注意：如果直接用UTF-8格式读取字节流中的文字，解码方式不与编码gbk不一致，会导致出现乱码，
			 * 所以在读取字节时要加上gbk格式String SongName = new String(buf, 3, 30,"gbk");
			 * 
			 * 没完全明白，为什么编码是gbk的?
			 */
			String encode="GB18030";
			System.out.println(encode);
			String SongName = new String(buf, 3, 30,encode);
			System.out.println("歌名：" + SongName);
			model.setSongName(SongName);
			String Singer = new String(buf, 33, 30,encode);
			System.out.println("作者：" + Singer);
			model.setSinger(Singer);
			String Album = new String(buf, 63, 30,encode);
			System.out.println("专辑：" + Album);
			model.setAlbum(Album);
			//System.out.println("文件长度：" + raf.length());

			String encode2=SGDataHelper.encoding;//
			System.out.println(encode2);
			String SongName2 = new String(buf, 3, 30,encode2);
			System.out.println("歌名：" + SongName2);
			String Singer2 = new String(buf, 33, 30,encode2);
			System.out.println("作者：" + Singer2);
			String Album2 = new String(buf, 63, 30,encode2);
			System.out.println("专辑：" + Album2);
			
			String encode3="gbk";//
			System.out.println(encode3);
			String SongName3 = new String(buf, 3, 30,encode3);
			System.out.println("歌名：" + SongName3);
			String Singer3 = new String(buf, 33, 30,encode3);
			System.out.println("作者：" + Singer2);
			String Album3 = new String(buf, 63, 30,encode3);
			System.out.println("专辑：" + Album2);
			
			System.out.println("文件长度：" + raf.length());
			model.setSize(raf.length());
			
			raf.close();
			// 将字节转化为二进制字符串
			String s1 = String.format("%8s", Integer.toBinaryString(buf1[0] & 0xFF)).replace(' ', '0');
			String s2 = String.format("%8s", Integer.toBinaryString(buf1[1] & 0xFF)).replace(' ', '0');
			String s3 = String.format("%8s", Integer.toBinaryString(buf1[2] & 0xFF)).replace(' ', '0');
			String s4 = String.format("%8s", Integer.toBinaryString(buf1[3] & 0xFF)).replace(' ', '0');
			String c = s1 + s2 + s3 + s4;
			char sc[] = c.toCharArray();
			System.out.println("音频版本：" + AudioVersion(sc[11], sc[12]));
			System.out.println("采样频率：" + SampleFrequency(sc[11], sc[12], sc[20], sc[21]));
			System.out.println("声道模式：" + ChannelMode(sc[24], sc[25]));
			model.setAudioVersion(AudioVersion2(sc[11], sc[12]));
			model.setFrequency(SampleFrequency2(sc[11], sc[12], sc[20], sc[21]));
			model.setChannelMode(ChannelMode2(sc[24], sc[25]));

		} catch (IOException e) {
			System.err.println("发生异常:" + e);
			e.printStackTrace();
		}
	}

	/**
	 * 不知道为何不能用utf8读
	 * @param mp3Path
	 */
	@Deprecated
	public static void showInfoUtf8(String mp3Path) {
		try {
//			RandomAccessFile raf = new RandomAccessFile("薛之谦 - 丑八怪.mp3", "rw");
			RandomAccessFile raf = new RandomAccessFile(mp3Path, "rw");
			byte buf[] = new byte[128];
			byte buf1[] = new byte[4];
			raf.seek(raf.length() - 128);
			raf.read(buf);
			raf.seek(45);// 指针往前走45个字节，从第46个字节开始
			raf.read(buf1);
			if (buf.length != 128) {
				System.err.println("MP3标签信息数据长度不合法!");
			}
			if (!"TAG".equalsIgnoreCase(new String(buf, 0, 3))) {
				System.err.println("MP3标签信息数据格式不正确!");
			}
			/**
			 * utf8会乱码
			 * 此类的作者说：
			 * 注意：如果直接用UTF-8格式读取字节流中的文字，解码方式不与编码gbk不一致，会导致出现乱码，
			 * 所以在读取字节时要加上gbk格式String SongName = new String(buf, 3, 30,"gbk");
			 * 
			 * 没完全明白，为什么编码是gbk的?
			 */
//			String encode=SGDataHelper.encoding;//
			String encode="gbk";//
			String SongName = new String(buf, 3, 30,encode);
			System.out.println("歌名：" + SongName);
			String Singer = new String(buf, 33, 30,encode);
			System.out.println("作者：" + Singer);
			String Album = new String(buf, 63, 30,encode);
			System.out.println("专辑：" + Album);
			System.out.println("文件长度：" + raf.length());
			raf.close();
			// 将字节转化为二进制字符串
			String s1 = String.format("%8s", Integer.toBinaryString(buf1[0] & 0xFF)).replace(' ', '0');
			String s2 = String.format("%8s", Integer.toBinaryString(buf1[1] & 0xFF)).replace(' ', '0');
			String s3 = String.format("%8s", Integer.toBinaryString(buf1[2] & 0xFF)).replace(' ', '0');
			String s4 = String.format("%8s", Integer.toBinaryString(buf1[3] & 0xFF)).replace(' ', '0');
			String c = s1 + s2 + s3 + s4;
			char sc[] = c.toCharArray();
			System.out.println("音频版本：" + AudioVersion(sc[11], sc[12]));
			System.out.println("采样频率：" + SampleFrequency(sc[11], sc[12], sc[20], sc[21]));
			System.out.println("声道模式：" + ChannelMode(sc[24], sc[25]));
			

		} catch (IOException e) {
			System.err.println("发生异常:" + e);
			e.printStackTrace();
		}
	}
//判断音频版本
	public static String AudioVersion(char a, char b) {
		String result = "";
		if ('0' == a && '0' == b)
			result = "MPEG 2.5";
		else if ('0' == a && '1' == b)
			result = "保留";
		else if ('1' == a && '0' == b)
			result = "MPEG 2";
		else if ('1' == a && '1' == b)
			result = "MPEG 1";
		return result;
	}
	public static Mp3Model.AudioVersion AudioVersion2(char a, char b) {
		Mp3Model.AudioVersion result = null;
		if ('0' == a && '0' == b) {
//			result = "MPEG 2.5";
			result=Mp3Model.AudioVersion.MPEG2d5;}
		else if ('0' == a && '1' == b) {
			result = Mp3Model.AudioVersion.KEEP;}
		else if ('1' == a && '0' == b) {
			result = Mp3Model.AudioVersion.MPEG2;}
		else if ('1' == a && '1' == b) {
			result = Mp3Model.AudioVersion.MPEG1;}
		return result;
	}
//判断采样频率
	public static int SampleFrequency(char a1, char b1, char a, char b) {
		int f = 0;
		if (AudioVersion(a1, b1) == "MPEG 1" && '0' == a && '0' == b)
			f = 44100;
		if (AudioVersion(a1, b1) == "MPEG 2" && '0' == a && '0' == b)
			f = 22050;
		if (AudioVersion(a1, b1) == "MPEG 2.5" && '0' == a && '0' == b)
			f = 11025;
		if (AudioVersion(a1, b1) == "MPEG 1" && '0' == a && '1' == b)
			f = 48000;
		if (AudioVersion(a1, b1) == "MPEG 2" && '0' == a && '1' == b)
			f = 24000;
		if (AudioVersion(a1, b1) == "MPEG 2.5" && '0' == a && '1' == b)
			f = 12000;
		if (AudioVersion(a1, b1) == "MPEG 1" && '1' == a && '0' == b)
			f = 32000;
		if (AudioVersion(a1, b1) == "MPEG 2" && '1' == a && '0' == b)
			f = 16000;
		if (AudioVersion(a1, b1) == "MPEG 2.5" && '1' == a && '0' == b)
			f = 8000;
		return f;
	}

	public static int SampleFrequency2(char a1, char b1, char a, char b) {
		int f = 0;
		if (AudioVersion2(a1, b1) == Mp3Model.AudioVersion.MPEG1 && '0' == a && '0' == b)
			f = 44100;
		if (AudioVersion2(a1, b1) == Mp3Model.AudioVersion.MPEG2 && '0' == a && '0' == b)
			f = 22050;
		if (AudioVersion2(a1, b1) == Mp3Model.AudioVersion.MPEG2d5 && '0' == a && '0' == b)
			f = 11025;
		if (AudioVersion2(a1, b1) == Mp3Model.AudioVersion.MPEG1 && '0' == a && '1' == b)
			f = 48000;
		if (AudioVersion2(a1, b1) == Mp3Model.AudioVersion.MPEG2 && '0' == a && '1' == b)
			f = 24000;
		if (AudioVersion2(a1, b1) == Mp3Model.AudioVersion.MPEG2d5 && '0' == a && '1' == b)
			f = 12000;
		if (AudioVersion2(a1, b1) == Mp3Model.AudioVersion.MPEG1 && '1' == a && '0' == b)
			f = 32000;
		if (AudioVersion2(a1, b1) == Mp3Model.AudioVersion.MPEG2 && '1' == a && '0' == b)
			f = 16000;
		if (AudioVersion2(a1, b1) == Mp3Model.AudioVersion.MPEG2d5 && '1' == a && '0' == b)
			f = 8000;
		return f;
	}
//判断声道模式
	public static String ChannelMode(char a, char b) {
		String result = "";
		if ('0' == a && '0' == b)
			result = "立体声";
		else if ('0' == a && '1' == b)
			result = "联合立体声";
		else if ('1' == a && '0' == b)
			result = "双声道";
		else if ('1' == a && '1' == b)
			result = "单声道";
		return result;
	}

	public static Mp3Model.ChannelMode ChannelMode2(char a, char b) {
		Mp3Model.ChannelMode result = null;
		if ('0' == a && '0' == b) {
			result =Mp3Model.ChannelMode.立体声;}
		else if ('0' == a && '1' == b) {
			result = Mp3Model.ChannelMode.联合立体声;}
		else if ('1' == a && '0' == b) {
			result = Mp3Model.ChannelMode.双声道;}
		else if ('1' == a && '1' == b) {
			result = Mp3Model.ChannelMode.单声道;}
		return result;
	}
	
	 public static boolean isMP3File(String filePath) {
	        File file = new File(filePath);
	        RandomAccessFile randomAccessFile = null;
	        try {
	            randomAccessFile = new RandomAccessFile(file, "r");
	            byte[] magic = new byte[3];
	            randomAccessFile.read(magic);
	            randomAccessFile.close();
	 
	            // Check the first 3 bytes of the file
	            return (magic[0] == 'I' && magic[1] == 'D' && magic[2] == '3');
	        } catch (IOException e) {
	            e.printStackTrace();
	            return false;
	        } finally {
	            if (randomAccessFile != null) {
	                try {
	                    randomAccessFile.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }
}

