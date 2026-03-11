package com.sellgirl.sgJavaHelper.midi;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

//import com.alibaba.fastjson.JSON;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

import java.io.File;
import java.nio.file.Paths;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;

@SuppressWarnings("unused")
public class MIDIMusic {
    public static void main(String args[]) {
//    	sound1();
    	//sound2();
    	

//        int[][] song1=getMaiBaoGe();
//        playSong(song1,5,"卖报歌",true);
        int[][] song2=getGuoGe();
        playSong(song2,5,"国歌",false);
    }
    public static int getRealSoundHeight(int h) {
    	switch (h){
    	case 1:
    		return 0;
    	case 2:
    		return 2;
    	case 3:
    		return 4;
    	case 4:
    		return 5;
    	case 5:
    		return 7;
    	case 6:
    		return 9;
    	case 7:
    		return 11;
    	}
    	return 0;
    }
    /**
     * 这样生成的mide好像不标准, 2个问题:
     * 1.a用前端播放没声音(可能是因为track有音量,但总的midi没有音量?
     * 2.a前端播放时,按键按下后没有弹起
     * @param song
     * @param speed
     * @param songName
     * @param deleteMidiFile
     */
	private static void playSong(int[][] song,int speed,String songName,boolean deleteMidiFile) {
    	try {
            int command = 144; //Òô·ûÏûÏ¢µÄÃüÁîÖµ£¬ÀýÈçNOTE_ON £¨0x90 »ò 144£©,NOTE_OFF ÏûÏ¢µÄÃüÁîÖµ£¨0x80 »ò 128£©
            int channel = 6; //¸ºÔðÑÝ×àÒô·ûµÄÆµµÀ£¬Ïàµ±ÓÚÒ»¸öÀÖÆ÷»ò¼¸¸öÀÖÆ÷µÄÉùÒô£¬ÀýÈç6ÆµµÀÊÇ¸ÖÇÙ
            int noteHeight = 48;     //Òô·ûµÄÒô¸ß 0-127£¨48ÊÇC´óµ÷µÄdo¡£48,50,52,53,55,57,59¾ÍÊÇdo ruai,mi,fa,so,la,xi)
            int noteVolume  = 100;   //a音量
            int noteTimevalueScale  = 2; //Òô·û±»¿ªÊ¼ÑÝ×àµÄMIDI¿Ì¶ÈÎ»ÖÃ£¨ÔÚTrackÒô¹ìÉÏµÄÎ»ÖÃ)
        	
            Sequence seq = new Sequence(Sequence.PPQ,4);//钢琴

            Track track = seq.createTrack(); 
            
//            ShortMessage  musicSound = new ShortMessage(); // 音调
//            musicSound.setMessage(command, channel, noteHeight, noteVolume);
//            MidiEvent event = new MidiEvent(musicSound, noteTimevalueScale);//¹¹½¨Ò»¸öMdidEvent£¬¼´Ò»´ÎÑÝ×àÐ§¹û
//            track.add(event);    //¹ìµÀÌí¼ÓÒ»¸öÑÝ×àÐ§¹û
//            
//
//              musicSound = new ShortMessage(); // 音调
//            musicSound.setMessage(command, channel, noteHeight+2, noteVolume);
//             event = new MidiEvent(musicSound, noteTimevalueScale+5);//¹¹½¨Ò»¸öMdidEvent£¬¼´Ò»´ÎÑÝ×àÐ§¹û
//            track.add(event);    //¹ìµÀÌí¼ÓÒ»¸öÑÝ×àÐ§¹û
//
//            musicSound = new ShortMessage(); // 音调
//          musicSound.setMessage(command, channel, noteHeight+4, noteVolume);
//           event = new MidiEvent(musicSound, noteTimevalueScale+5*2);//¹¹½¨Ò»¸öMdidEvent£¬¼´Ò»´ÎÑÝ×àÐ§¹û
//          track.add(event);    //¹ìµÀÌí¼ÓÒ»¸öÑÝ×àÐ§¹û
          
            //int oneSoundLength=5;
          for(int i=0;i<song.length;i++) {        	  
//        	  for(int j=0;j<song[i].length;j++) {
//        		  
//        	  }
              ShortMessage  musicSound = new ShortMessage(); // 音调
              int h=getRealSoundHeight(song[i][0])+song[i][1]*12;//12: 1-> 高音1之间是相差12个音
//              MIDIMusic.PrintObject(h,noteTimevalueScale);
              musicSound.setMessage(command, channel, noteHeight+h, noteVolume);
              MidiEvent event = new MidiEvent(musicSound, noteTimevalueScale);//¹¹½¨Ò»¸öMdidEvent£¬¼´Ò»´ÎÑÝ×àÐ§¹û
              noteTimevalueScale += speed*song[i][2];//下个音节的起始时间为本音节的长度? 
              track.add(event); 
          }

          //a如果不加1个音,最后1个音的长度会不正确
          ShortMessage  musicSoundLast = new ShortMessage(); // 音调
          musicSoundLast.setMessage(command, channel, 0, noteVolume);
          MidiEvent eventLast = new MidiEvent(musicSoundLast, noteTimevalueScale);//¹¹½¨Ò»¸öMdidEvent£¬¼´Ò»´ÎÑÝ×àÐ§¹û
          track.add(eventLast); 

            
            int [] type = MidiSystem.getMidiFileTypes(seq);
//            String path=Paths.get(SGDataHelper.GetBaseDirectory(), "tmpfile", songName+".midi").toString();
            String path=SGDataHelper.GetBaseDirectory()+ "tmpfile"+ songName+".midi";
            File file=new File(path);
            MidiSystem.write(seq,type[0],file);//±£´æmidiÒôÀÖ
            Sequencer player = MidiSystem.getSequencer(); //¶¨ÐòÆ÷£¬¸ºÔð²¥·ÅÒô·ûÐòÁÐ
            player.open();
            player.setSequence(seq);   //¸ºÔð²¥·ÅÒô·ûÐòÁÐ
            //player.setLoopCount(1);    //
            player.setTempoInBPM(57*2); //Ã¿·ÖÖÓ114ÅÄ
            player.start();
            String aa="aa";
            if(deleteMidiFile) {
                file.delete();
            }
//            while(true) {
//            	PrintObject(player.isRunning());
//            	Thread.sleep(1000);
//            }
            while(player.isRunning()) {
//            	PrintObject(player.isRunning());
            	Thread.sleep(1000);
            }
            player.close();
            
    }
    catch(Exception exp){
        System.out.println(exp);
    }
    }
    private static void sound1() {
        int command = 144; //Òô·ûÏûÏ¢µÄÃüÁîÖµ£¬ÀýÈçNOTE_ON £¨0x90 »ò 144£©,NOTE_OFF ÏûÏ¢µÄÃüÁîÖµ£¨0x80 »ò 128£©
        int channel = 6; //¸ºÔðÑÝ×àÒô·ûµÄÆµµÀ£¬Ïàµ±ÓÚÒ»¸öÀÖÆ÷»ò¼¸¸öÀÖÆ÷µÄÉùÒô£¬ÀýÈç6ÆµµÀÊÇ¸ÖÇÙ
        int noteHeight = 48;     //Òô·ûµÄÒô¸ß 0-127£¨48ÊÇC´óµ÷µÄdo¡£48,50,52,53,55,57,59¾ÍÊÇdo ruai,mi,fa,so,la,xi)
        int noteVolume  = 100;   //Òô·ûµÄÒôÁ¿´óÐ¡
        int noteTimevalueScale  = 2; //Òô·û±»¿ªÊ¼ÑÝ×àµÄMIDI¿Ì¶ÈÎ»ÖÃ£¨ÔÚTrackÒô¹ìÉÏµÄÎ»ÖÃ)
     //¿Ì¶ÈÉÏÒô·û±»ÑÝ×àµÄ³ÖÐøÃëÊýÓÉ°üº¬´ËTrackÒô¹ìµÄ Sequence µÄ¼ÆÊ±¾«¶ÈÈ·¶¨£¬Í¬Ê±Ò²È¡¾öÓÚÓÉ sequencer ÉèÖÃµÄÒôÀÖµÄ½ÚÅÄ£©
        try{
            Sequence seq = new Sequence(Sequence.PPQ,4);  //¸ºÔð´æ·ÅÒô·ûÐòÁÐ£¬4/4ÅÄ
            Track track = seq.createTrack();   //seqÖÐ¸ºÔð·ÅÖÃMidiEventµÄTrack¹ìµÀ£¬¼´·ÅÖÃ¡°ÑÝ×àÐ§¹û¡±µÄ¹ìµÀ
            for(int i=0;i<8;i++) {
              //musicSoundÒô·ûÊÇ´ò¿ª×´Ì¬£¬1ÆµµÀ¸ºÔðÑÝ×à£¨¸ÖÇÙ£©£¬Òô¸ßÊÇ48(do)ÒôÁ¿´óÐ¡ÊÇ100
               ShortMessage  musicSound = new ShortMessage(); 
               musicSound.setMessage(command, channel, noteHeight, noteVolume); //ÉèÖÃÒô·ûµÄÓÐ¹ØÊôÐÔ
               if( i != 2&&i != 6 ){
                   noteHeight = noteHeight+2;
               }
               else {
                   noteHeight = noteHeight+1;
               }
               MidiEvent event = new MidiEvent(musicSound, noteTimevalueScale);//¹¹½¨Ò»¸öMdidEvent£¬¼´Ò»´ÎÑÝ×àÐ§¹û
               if(i%2 == 0)
                 noteTimevalueScale += 10; //Ã¿¸öÒô·û±»ÑÝ×àµÄMIDIV¿Ì¶ÈÎ»ÖÃ»¥²»ÏàÍ¬£¨Èç¹ûÏàÍ¬£¬¾ÍÍ¬Ò»Ê±¿ÌÑÝ×à£©
               else
                  noteTimevalueScale += 5; 
               track.add(event);    //¹ìµÀÌí¼ÓÒ»¸öÑÝ×àÐ§¹û
            }
            int [] type = MidiSystem.getMidiFileTypes(seq);
            MidiSystem.write(seq,type[0],new File("Òô½Ú.midi"));//±£´æmidiÒôÀÖ
            Sequencer player = MidiSystem.getSequencer(); //¶¨ÐòÆ÷£¬¸ºÔð²¥·ÅÒô·ûÐòÁÐ
            player.open();
            player.setSequence(seq);   //¸ºÔð²¥·ÅÒô·ûÐòÁÐ
            player.setLoopCount(5);    //Ö¸¶¨ÖØ¸´´ÎÊý£¬Êµ¼ÊÊÇ6´Î£¬0±íÊ¾ÖØ¸´Ò»´Î(Ä¬ÈÏÊÇ0)
            player.setTempoInBPM(57*2); //Ã¿·ÖÖÓ114ÅÄ
            player.start();
         }
         catch(Exception exp){
             System.out.println(exp);
         }
    	
    }
//    private static void sound2()  {
//try {
//        int command = 144; //Òô·ûÏûÏ¢µÄÃüÁîÖµ£¬ÀýÈçNOTE_ON £¨0x90 »ò 144£©,NOTE_OFF ÏûÏ¢µÄÃüÁîÖµ£¨0x80 »ò 128£©
//        int channel = 6; //¸ºÔðÑÝ×àÒô·ûµÄÆµµÀ£¬Ïàµ±ÓÚÒ»¸öÀÖÆ÷»ò¼¸¸öÀÖÆ÷µÄÉùÒô£¬ÀýÈç6ÆµµÀÊÇ¸ÖÇÙ
//        int noteHeight = 48;     //Òô·ûµÄÒô¸ß 0-127£¨48ÊÇC´óµ÷µÄdo¡£48,50,52,53,55,57,59¾ÍÊÇdo ruai,mi,fa,so,la,xi)
//        int noteVolume  = 100;   //Òô·ûµÄÒôÁ¿´óÐ¡
//        int noteTimevalueScale  = 2; //Òô·û±»¿ªÊ¼ÑÝ×àµÄMIDI¿Ì¶ÈÎ»ÖÃ£¨ÔÚTrackÒô¹ìÉÏµÄÎ»ÖÃ)
//
//        Sequence seq = new Sequence(Sequence.PPQ,4);//钢琴
//
//        Track track = seq.createTrack();
//
//        ShortMessage  musicSound = new ShortMessage(); // 音调
//        musicSound.setMessage(command, channel, noteHeight, noteVolume);
//        MidiEvent event = new MidiEvent(musicSound, noteTimevalueScale);//¹¹½¨Ò»¸öMdidEvent£¬¼´Ò»´ÎÑÝ×àÐ§¹û
//        track.add(event);    //¹ìµÀÌí¼ÓÒ»¸öÑÝ×àÐ§¹û
//
//
//          musicSound = new ShortMessage(); // 音调
//        musicSound.setMessage(command, channel, noteHeight+2, noteVolume);
//         event = new MidiEvent(musicSound, noteTimevalueScale+5);//¹¹½¨Ò»¸öMdidEvent£¬¼´Ò»´ÎÑÝ×àÐ§¹û
//        track.add(event);    //¹ìµÀÌí¼ÓÒ»¸öÑÝ×àÐ§¹û
//
//        musicSound = new ShortMessage(); // 音调
//      musicSound.setMessage(command, channel, noteHeight+4, noteVolume);
//       event = new MidiEvent(musicSound, noteTimevalueScale+5*2);//¹¹½¨Ò»¸öMdidEvent£¬¼´Ò»´ÎÑÝ×àÐ§¹û
//      track.add(event);    //¹ìµÀÌí¼ÓÒ»¸öÑÝ×àÐ§¹û
//
//
//        int [] type = MidiSystem.getMidiFileTypes(seq);
//        String path=Paths.get(SGDataHelper.GetBaseDirectory(), "tmpfile", "song01.xml").toString();
//        MidiSystem.write(seq,type[0],new File(path));//±£´æmidiÒôÀÖ
//        Sequencer player = MidiSystem.getSequencer(); //¶¨ÐòÆ÷£¬¸ºÔð²¥·ÅÒô·ûÐòÁÐ
//        player.open();
//        player.setSequence(seq);   //¸ºÔð²¥·ÅÒô·ûÐòÁÐ
//        //player.setLoopCount(1);    //
//        player.setTempoInBPM(57*2); //Ã¿·ÖÖÓ114ÅÄ
//        player.start();
//
//}
//catch(Exception exp){
//    System.out.println(exp);
//}
//    }
//	private static  void PrintObject(Object... o) {
//		String s="";
//		for(Object i:o) {
//			s+=JSON.toJSONString(i)+" ";
//		}
//		s+="\r\n";
//	System.out.println(s);
//}

    private static int[][] getMaiBaoGe(){
    	return new int[][] {
        	//a数组的3位分别表示: 1:音调1~7 2:低音区/中音区/高音区... 3:音长
        	new int[] {1,0,1},
        	new int[] {2,0,1},
        	new int[] {3,0,1},
        	new int[] {1,0,1},
        	new int[] {5,0,2},
        	
        	new int[] {6,0,1},
        	new int[] {6,0,1},
        	new int[] {1,1,1},
        	new int[] {6,0,1},
        	new int[] {5,0,2},
        	
        	new int[] {6,0,1},
        	new int[] {6,0,1},
        	new int[] {1,1,2},
        	new int[] {5,0,1},
        	new int[] {6,0,1},
        	new int[] {3,0,2},
        	
        	new int[] {6,0,1},
        	new int[] {5,0,1},
        	new int[] {3,0,1},
        	new int[] {5,0,1},
        	new int[] {3,0,1},
        	new int[] {1,0,1},
        	new int[] {2,0,1},
        	new int[] {3,0,1},
        	new int[] {1,0,1},
        };
    }

    private static int[][] getGuoGe(){
    	return new int[][] {
        	//a数组的3位分别表示: 1:音调1~7 2:低音区/中音区/高音区... 3:音长
        	new int[] {5,-1,1},
        	new int[] {1,0,2},
        	
        	new int[] {1,0,1},
        	new int[] {1,0,1},
        	new int[] {1,0,1},        	
        	new int[] {5,-1,1},
        	new int[] {6,-1,1},
        	new int[] {7,-1,1},
        	new int[] {1,0,2},
        	new int[] {1,0,2},
        	
        	new int[] {3,0,1},
        	new int[] {1,0,1},
        	new int[] {2,0,1},
        	new int[] {3,0,1},
        	new int[] {5,0,2},
        	new int[] {5,0,2},
        	
        	new int[] {3,0,1},
        	new int[] {3,0,1},
        	new int[] {1,0,1},
        	new int[] {3,0,1},
        	new int[] {5,0,1},
        	new int[] {3,0,1},
        	new int[] {2,0,2},
        	new int[] {2,0,2},
//        	new int[] {1,0,1},
        };
    }
}