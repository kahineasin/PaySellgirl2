package com.sellgirl.sgGameHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.StreamUtils;
import com.sellgirl.sgJavaHelper.ISGDisposable;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.time.Waiter;

/**
 * 下载较大文件并计算进度
 */
public class SGFileDownloader implements ISGDisposable{
	private final String tag="SGFileDownloader";
	public boolean downloading=false;
	/**
	 * 进度，最大100
	 */
	public int progress=0;
	public void download(//MusicInfo musicInfoDomain,MusicInfoModel musicInfo,
			String url,
//			String saveExternalPath,
			FileHandle fileHandle,
			 boolean forceUpdate//,
		  //FileHandle fileHandle, String url,
//		  TextButton playBtn,
//			 TextButton updateBtn,
//			 int idx
) {

////////String songName=SGPath.GetFileName(musicInfo.getLrc());
////////String songName=SGPath.GetFileName(musicInfo.getUrl());
////////songName=SGDataHelper.getURLDecoderString(songName);
//////String songName = musicInfo.getDecodeFileName();
////////String songPath = "song/" + folder + "/" + songName;
//////String songPath = com.mygdx.game.sasha.util.Constants.EXTERNAL_MP3_ROOT + "/" + folder + "/" + songName;
////String songPath = com.mygdx.game.sasha.util.Constants.EXTERNAL_APK_FILE;
//String songPath = saveExternalPath;
//FileHandle fileHandle = Gdx.files.external(songPath);
boolean fileExist=fileHandle.exists();

//if (//test||
////((!forceUpdate) && fileExist
////		&&null!=musicInfo.getDownloaded()&& true==musicInfo.getDownloaded()
////)
////		||musicInfo.isDownloading()
//true
//) {
////System.out.println("-----------1----------" + musicInfo.getUrl());
////AudioManager.instance.play(fileHandle);
////System.out.println("-----------1----------" + musicInfo.getUrl());
//if(AudioManager.instance.play(fileHandle,musicInfo,null!=musicInfo.getDownloaded()&& musicInfo.getDownloaded())){
////System.out.println("-----------1 end----------" + musicInfo.getUrl());
//doPlay(musicInfo,playBtn,updateBtn,idx);
//}else{
//playBtn.setText(playFailText);
//playBtn.setDisabled(false);
//updateBtn.setDisabled(false);
//}
//return;
//}

//musicInfo.setDownloading(true);
downloading=true;

//if(null!=musicInfo.getDownloaded()&&true==musicInfo.getDownloaded()&&forceUpdate){
////已下载时强制更新的情况，可能因为文件不完整或者远程文件有更新时
//musicInfoDomain.setDownloaded(false);
//musicInfo.setDownloaded(false);
//FileHandle file = Gdx.files.external(Constants.EXTERNAL_SUBSCRIBE_ROOT + "/" + folder + ".json");
//file.writeString(json.toJson( musicCollection,MusicCollection.class),false,SGDataHelper.encoding);
//}

////String url=musicInfo.getUrl().replace(songName,"")+SGDataHelper.getURLEncoderString(songName);
////String url = musicInfo.getEncodeUrl();
//String url=this.url.getApkUrl();
//button.setText(TXT.g("downloading"));
//playBtn.setText(downloadingText);
// Make a GET request
Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.GET);
//           request.setTimeOut(2500);
//           request.setUrl("http://libgdx.badlogicgames.com/nightlies/libgdx-nightly-latest.zip");
//           request.setUrl(bad.getUrl());
//String url=bad.getUrl().replace(songName,"sgTmpName")+SGDataHelper.getURLEncoderString(songName);
request.setUrl(url);

// Send the request, listen for the response
Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
@Override
public void handleHttpResponse(Net.HttpResponse httpResponse) {
if (HttpStatus.SC_OK != httpResponse.getStatus().getStatusCode()) {
//	Gdx.app.postRunnable(new Runnable() {
//		@Override
//		public void run() {
////       playBtn.setText("Too bad. Download failed.");
//			playBtn.setText(playFailText);
//			updateBtn.setDisabled(false);
//			playBtn.setDisabled(false);
//			//SGDataHelper.getLog().writeException(t,tag);
//		}
//	});
	downloading=false;
	return;
}
// Determine how much we have to download
long length = Long.parseLong(httpResponse.getHeader("Content-Length"));

// We're going to download the file to external storage, create the streams
InputStream is = httpResponse.getResultAsStream();
//                   OutputStream os = Gdx.files.external("libgdx-nightly-latest.zip").write(false);
//                   OutputStream os = Gdx.files.external("song/"+folder+"/"+songName).write(false);
long fileSize=0;
boolean continueSuccess=false;
if(fileExist){
	fileSize=fileHandle.length();
}
OutputStream os = fileHandle.write(fileExist&&!forceUpdate);

//byte[] bytes = new byte[1024];
//byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
byte[] bytes = new byte[StreamUtils.DEFAULT_BUFFER_SIZE];

int count = -1;
long read = 0;
SGRef<Boolean> started = new SGRef<>(false);
SGRef<Boolean> errorToStop = new SGRef<Boolean>(false);
//SGRef<Boolean> streamPlayStarted = new SGRef<>(false);
SGRef<Boolean> streamPlayFailed = new SGRef<Boolean>(false);//流播放失败的话，最好还是继续下载，因为下载完之后，有的能正常播放
try {
	// Keep reading bytes and storing them until there are no more.
//   SGSpeedCounter speed2 = new SGSpeedCounter();
//   SGSpeedCounter speed = new SGSpeedCounter();
	Waiter waiter=new Waiter(1);
	while ((count = is.read(bytes, 0, bytes.length)) != -1) {
		if(disposed//  null==stage
				) {
			try {
				is.close();
				os.close();
				return;
			} catch (Throwable e) {
				SGDataHelper.getLog().printException(e, tag);
			}
		}
////       speed.setEndTime(SGDate.Now());

//       speed2.setBeginTime(SGDate.Now());
		if(read+count>=fileSize||forceUpdate) {
			if(fileExist&&!continueSuccess&&!forceUpdate) {
				//注意count和bytes.length不一定相等的，所以断点续传要注意起点
				int more=((Long)(fileSize-read)).intValue();
				os.write(bytes, more, count-more);
				continueSuccess=true;
			}else {
				os.write(bytes, 0, count);
			}

			if (waiter.isOK()||read+count>=length) {
				os.flush();
			}
		}
//       speed2.setEndTime(SGDate.Now());

		read += count;


		// Update the UI with the download progress
		 progress = ((int) (((double) read / (double) length) * 100));
		final String progressString = progress == 100 ? "play" : progress + "%";

//		if (MusicInfoModel.MusicFormat.MP3 == musicInfo.getMusicFormat()
//				&& (!started.GetValue())
////           &&(!streamPlayStarted.GetValue())
//				&&2<progress//1%开始播放试试, 太早播放好像会失败
//				&& 64403 * 3 < read//还是感觉用已读判断好些，网上说用ID3标签判断，以后看看
//				&& (!streamPlayFailed.GetValue())
//		) {
////           started.SetValue(true);//早一些设置可以
//			//流播放
//			if (null == stage) {
//				//当PlayScreen1下载中时，回到菜单，再打开PlayScreen2播放时，PlayScreen1好像会进入这里
//				return;
//			}
//
//			//如果这部分和同步播放时一样，测试没问题的话，就把Runnable共用吧--todo
//			Gdx.app.postRunnable(new Runnable() {
//				@Override
//				public void run() {
////                   boolean playSuccess=false;
////                   try{
//					if (started.GetValue()|| errorToStop.GetValue() || streamPlayFailed.GetValue()) {
//						//因为runnable的循环比byte read更快，所以在这要判断
//						return;
//					}
//					started.SetValue(true);
////                       errorToStop.SetValue(!AudioManager.instance.play(fileHandle));
//					//System.out.println("-----------2----------" + musicInfo.getUrl());
//					streamPlayFailed.SetValue(!AudioManager.instance.play(fileHandle,musicInfo,false));//fileHandle));Gdx.files.external(songPath)
//					//System.out.println("-----------2 end----------" + musicInfo.getUrl());
//					if (streamPlayFailed.GetValue()) {
//						//异步不换歌，等下载完再播放更好了，免得出问题
//						started.SetValue(false);
//						playBtn.setText(playFailText);
//						return;
//
//					}
//
//					started.SetValue(true);
////                   }catch (Throwable e){
////                       SGDataHelper.getLog().printException(e,"steam play too fast");
////                       return;
////                   }
//					//
//					doPlay(musicInfo,playBtn,updateBtn,idx);
////                   AudioManager.instance.getPlayingMusic().setOnCompletionListener(new MusicEndListener());
////                   curIdx = idx;
////                   playBtn.setText(playingText);
////                   playBtn.setDisabled(false);
////                   if (null != lastPlayBtn && lastPlayBtn != playBtn) {
////                       lastPlayBtn.setText(playText);
////                   }
////                   lastPlayBtn = playBtn;
////                   updateBtn.setText(updateText);
////                   updateBtn.setDisabled(false);
////                   currentTime = 0;
////                   currentLine = -1;
////                   currentMusic = musicInfo;
//
//				}
//			});
//
//		}

//		// Since we are downloading on a background thread, post a runnable to touch ui
//		Gdx.app.postRunnable(new Runnable() {
//			@Override
//			public void run() {
//				if (progress == 100) {
//
////                   musicInfo.setDownloading(false);
//					if (null == stage) {
//						//当PlayScreen1下载中时，回到菜单，再打开PlayScreen2播放时，PlayScreen1好像会进入这里
//						return;
//					}
//////					//保存下载结果
//////					musicInfoDomain.setDownloaded(true);
//////					musicInfo.setDownloaded(true);
////					FileHandle file = Gdx.files.external(Constants.EXTERNAL_SUBSCRIBE_ROOT + "/" + folder + ".json");
//////                   String tmp = file.readString(SGDataHelper.encoding);
//////                   MusicCollection musicCollection = json.fromJson(MusicCollection.class, tmp);
////					file.writeString(json.toJson( musicCollection,MusicCollection.class),false,SGDataHelper.encoding);
//
////					if (streamPlayFailed.GetValue()) {
//////                       playBtn.setText(playText);
//////                       playBtn.setText(playFailText);
////						updateBtn.setText(updateText);
////						playBtn.setDisabled(false);
////						updateBtn.setDisabled(false);
////						return;
////					}
////					if (started.GetValue()//&&streamPlayFailed.GetValue()
////					) {
//////                       if(downloadingText.equals(playBtn.getText())){
//////                           playBtn.setText(playText);
//////                       }
//////                       playBtn.setText(playText);
//////						updateBtn.setText(updateText);
////						playBtn.setDisabled(false);
////						updateBtn.setDisabled(false);
////						return;
////					}
//					//System.out.println("-----------3----------" + musicInfo.getUrl());
//					if(AudioManager.instance.play(fileHandle,musicInfo,true)){//Gdx.files.external(songPath)
//						//System.out.println("-----------3 end----------" + musicInfo.getUrl());
//						doPlay(musicInfo,playBtn,updateBtn,idx);
//					}else{
//
//						playBtn.setText(playFailText);
//						playBtn.setDisabled(false);
//						updateBtn.setDisabled(false);
//					}
//
//				} else {
//					if (null == stage) {
//						//当PlayScreen1下载中时，回到菜单，再打开PlayScreen2播放时，PlayScreen1好像会进入这里
////                       return;
//						String aa = "aa";
//					} else {
//						updateBtn.setText(progressString);
//					}
//				}
//			}
//		});

		if (100 <= progress) {
			try {
				is.close();
				os.close();
			} catch (Throwable e) {
				SGDataHelper.getLog().printException(e, tag);
			}
			return;
		}

		//speed.setBeginTime(SGDate.Now());
	}
	try {
		is.close();
		os.close();
	} catch (Throwable e) {
		SGDataHelper.getLog().printException(e, tag);
	}
//   finally {
//   }
} catch (IOException e) {
	try {
		is.close();
		os.close();
	} catch (Throwable e2) {
		SGDataHelper.getLog().printException(e, tag);
	}
	SGDataHelper.getLog().writeException(e, tag);

//	Gdx.app.postRunnable(new Runnable() {
//		@Override
//		public void run() {
////       playBtn.setText("Too bad. Download failed.");
//			playBtn.setText(playFailText);
//			updateBtn.setDisabled(false);
//			playBtn.setDisabled(false);
//		}
//	});
}finally {

//	musicInfo.setDownloading(false);
}
}

@Override
public void failed(Throwable t) {
//Gdx.app.postRunnable(new Runnable() {
//	@Override
//	public void run() {
////       playBtn.setText("Too bad. Download failed.");
//		playBtn.setText(playFailText);
//		updateBtn.setDisabled(false);
//		playBtn.setDisabled(false);
//
//	}
//});

downloading=false;
SGDataHelper.getLog().writeException(t, tag);
}

@Override
public void cancelled() {
SGDataHelper.getLog().print("cancelled");
}
});
}
	private boolean disposed=false;
	@Override
	public void dispose() {
		disposed=true;
	}
}
