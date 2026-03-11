//package org.sgGameHelper;
//
//import java.util.ArrayList;
//
//import com.badlogic.gdx.ApplicationAdapter;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.audio.Music;
//import com.badlogic.gdx.files.FileHandle;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.math.MathUtils;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.Skin;
//import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
//import com.sellgirl.sgGameHelper.gamepad.XBoxKey;
//import com.sellgirl.sgJavaHelper.SGAction;
//import com.sellgirl.sgJavaHelper.config.SGDataHelper;
//import com.sellgirl.sgJavaHelper.time.Waiter;
//
//import junit.framework.TestCase;
//import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
//
///**
//* 用action写 反而更麻烦了
// */
//public class UncheckMp3  extends TestCase {
//	private static final String tag="UncheckMp3";
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
//	public static void runOnDesk(SGAction<Object,Object,Object> action,
//			SGAction<Object,Object,Object> renderAction
//			)  {
//        new Lwjgl3Application(new ApplicationAdapter() {
//            Stage stage;
//            Skin skin;
//
//            TextButton button;
//            private boolean started=false;
//            @Override
//            public void create () {
//            			
//
//            }
//
//            @Override
//            public void render () {
//                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
////                stage.act();
////                stage.draw();
//                if(!started) {
//                	started=true;
//                	action.go(null, null, null);
//                }
//                renderAction.go(null, null, null);
//            }
//
//        });
////        while (true){
////            String aa="aa";
////        }
//	}
//
//}
