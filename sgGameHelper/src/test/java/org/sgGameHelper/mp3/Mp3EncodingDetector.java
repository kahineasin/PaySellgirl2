package org.sgGameHelper.mp3;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

import java.io.File;
import java.nio.charset.Charset;
 
/**
 * 百度ai写的
 * 好像无效
 */
public class Mp3EncodingDetector {
    public static void main(String[] args) {
    }
    @Deprecated
    public static void isUtf8(String mp3Path) {
        try {
//            File mp3File = new File("path/to/your/mp3file.mp3");
            File mp3File = new File(mp3Path);
            
            //报错：javax.sound.sampled.UnsupportedAudioFileException: File of unsupported format
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(mp3File);
            if (fileFormat != null) {
                String artist = (String) fileFormat.properties().get("artist");
                if (artist != null) {
                    // 尝试使用UTF-8和GBK解码，并捕获异常来判断
                    try {
                        artist.getBytes("UTF-8");
                        System.out.println("艺术家名编码为UTF-8");
                    } catch (Exception e) {
                        try {
                            artist.getBytes("GBK");
                            System.out.println("艺术家名编码为GBK");
                        } catch (Exception ex) {
                            System.out.println("无法识别艺术家名的编码");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}