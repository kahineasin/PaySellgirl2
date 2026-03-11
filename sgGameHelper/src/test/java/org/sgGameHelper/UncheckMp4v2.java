package org.sgGameHelper;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.sgGameHelper.mp3.Mp3;
import org.sgGameHelper.mp3.ReadMp3Inf;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.sellgirl.sgGameHelper.gamepad.XBoxKey;
import com.sellgirl.sgGameHelper.list.Array2;
import com.sellgirl.sgGameHelper.list.ISGList;
import com.sellgirl.sgJavaHelper.SGAction;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.file.SGDirectory;
import com.sellgirl.sgJavaHelper.file.SGPath;
import com.sellgirl.sgJavaHelper.time.Waiter;

import junit.framework.TestCase;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;

/**
 * 此测试无法运行 pom中没有办法配置natives-desktop的插件依赖，所以没有办法使用Lwjgl3Application
 * 
 * 后来发现可以配置的。。
 */
public class UncheckMp4v2 extends TestCase {
	private static final String tag = "UncheckMp3";

//	public static void testNoSound() {
//		runOnDesk(
//				new SGAction<Object,Object,Object>(){
//
//					@Override
//					public void go(Object t1, Object t2, Object t3) {
//
//						//---------------------------
//				try {
//		        //FileHandle fileHandle = Gdx.files.absolute("D:/cache/mp3/001.电灯胆_邓丽欣1.mp3");//下载的版本，无声
//		        //FileHandle fileHandle = Gdx.files.absolute("D:/cache/mp3/001.电灯胆_邓丽欣.mp3");//mp3.sellgirl.com的网上版本, 无声
//		        FileHandle fileHandle = Gdx.files.absolute("D:/cache/mp3/017.寂寞_谢容儿_伴奏.mp3");//有声
//		        
//		        //由此判断,是 001.电灯胆_邓丽欣.mp3 的格式有问题，所以没有声音
//		        
////				FileHandle fileHandle=new Fi
//		        if(fileHandle.exists()){
//			        Music playingMusic = Gdx.audio.newMusic(fileHandle);
//			        playingMusic.play();
//		        	SGDataHelper.getLog().print("开始了播放");
//		        }else {
//		        	SGDataHelper.getLog().print("文件不存在");
//		        }
//		     
//////		        Waiter waiter=new Waiter(1);
////		        while(true) {
////		        	Thread.sleep(1000);
////		        }
//				}catch(Throwable e) {
//					SGDataHelper.getLog().printException(e,tag);
//				}
//
//						//---------------------------
//					}}
//				);
//	}
	public static <T> T[] ObjectToArray(Object value, Class<T> type) {
		if (value instanceof ISGList) {
			ISGList<?> list = (ISGList<?>) value;
			T[] array = SGDataHelper.<T[]>ObjectAs(Array.newInstance(type, list.size()));
			for (int i = 0; i < list.size(); i++) {
				array[i] = SGDataHelper.<T>ObjectAs(list.get(i));
			}
			return array;
		}
		return null;
	}
	public static void testNoSound(
//			SGAction<Object,Object,Object> action,
//			SGAction<Object,Object,Object> renderAction
	) {
		new Lwjgl3Application(new ApplicationAdapter() {
			Stage stage;
			Skin skin;

			TextButton button;
			private boolean started = false;
			private int curIdx = 0;
			private Waiter waiter = null;
			Music playingMusic=null;
			Array2<String> songList;
			String[] songArray;
			@Override
			public void create() {

				waiter = new Waiter(5);
				songList=new Array2<String>();
				
				for(File f:(new File("D:\\cache\\mp3")).listFiles()) {
					songList.add(f.getAbsolutePath());
				}
				songArray=ObjectToArray(songList, String.class);
			}

			@Override
			public void render() {
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//                stage.act();
//                stage.draw();
				if (!started) {
					started = true;
//                	action.go(null, null, null);
				}
//                renderAction.go(null, null, null);

				// ---------------------------
				String[] songs=songArray;
				if (waiter.isOK()&&songs.length>curIdx) {
					try {
						SGDataHelper.getLog().print("---------------------");
						// FileHandle fileHandle =
						// Gdx.files.absolute("D:/cache/mp3/001.电灯胆_邓丽欣1.mp3");//下载的版本，无声
						// FileHandle fileHandle =
						// Gdx.files.absolute("D:/cache/mp3/001.电灯胆_邓丽欣.mp3");//mp3.sellgirl.com的网上版本, 无声
//						FileHandle fileHandle = Gdx.files.absolute("D:/cache/mp3/017.寂寞_谢容儿_伴奏.mp3");// 有声
						// 由此判断,是 001.电灯胆_邓丽欣.mp3 的格式有问题，所以没有声音
						
						if(null!=playingMusic) {
							playingMusic.stop();
							playingMusic.dispose();
							playingMusic=null;
						}
						
						String song=songs[curIdx];
//			        	Mp3.getMusicMsg(new File(song));
			        	ReadMp3Inf.showInfo(song);
			        	
						FileHandle fileHandle = Gdx.files.absolute(song);
//		FileHandle fileHandle=new Fi
						if (fileHandle.exists()) {
							 playingMusic = Gdx.audio.newMusic(fileHandle);
							playingMusic.play();
							SGDataHelper.getLog().print(""+song+" 开始了播放");
						} else {
							SGDataHelper.getLog().print(""+song+" 文件不存在");
						}

						SGDataHelper.getLog().print("---------------------");
////        Waiter waiter=new Waiter(1);
//        while(true) {
//        	Thread.sleep(1000);
//        }
					} catch (Throwable e) {
						SGDataHelper.getLog().printException(e, tag);
					}
					curIdx++;
					// ---------------------------
				}
			}

		});
//        while (true){
//            String aa="aa";
//        }
	}

	public static void testWrongComplete(
//			SGAction<Object,Object,Object> action,
//			SGAction<Object,Object,Object> renderAction
	) {
		new Lwjgl3Application(new ApplicationAdapter() {
			Stage stage;
			Skin skin;

			TextButton button;
			private boolean started = false;
			private int curIdx = 0;
			private Waiter waiter = null;
			Music playingMusic=null;
			Array2<String> songList;
			String[] songArray;
//			long time=0;
			@Override
			public void create() {

				waiter = new Waiter(5);
				songList=new Array2<String>();
				
				for(File f:(new File("D:\\cache\\mp3")).listFiles()) {
					songList.add(f.getAbsolutePath());
				}
				songArray=ObjectToArray(songList, String.class);
			}

			@Override
			public void render() {
//				time+=Gdx.graphics.getDeltaTime();
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//                stage.act();
//                stage.draw();
				if (!started) {
					started = true;
					String song=Mp3.songs[4];//5正常 4x
					FileHandle fileHandle = Gdx.files.absolute(song);
//	FileHandle fileHandle=new Fi
					if (fileHandle.exists()) {
						 playingMusic = Gdx.audio.newMusic(fileHandle);
						playingMusic.play();
//						time=0;
						playingMusic.setOnCompletionListener(new MusicEndListener());
						SGDataHelper.getLog().print(""+song+" 开始了播放");
					} else {
						SGDataHelper.getLog().print(""+song+" 文件不存在");
					}
//                	action.go(null, null, null);
				}

			}

		});
//        while (true){
//            String aa="aa";
//        }
	}

    public static class MusicEndListener implements Music.OnCompletionListener {
//    	ApplicationAdapter ad;
    	long time;
    	public MusicEndListener() {
    		time=System.currentTimeMillis();
    	}
        @Override
        public void onCompletion(Music music) {
        	System.out.println("complete time:"+(System.currentTimeMillis()-time));
        }
    }
}
