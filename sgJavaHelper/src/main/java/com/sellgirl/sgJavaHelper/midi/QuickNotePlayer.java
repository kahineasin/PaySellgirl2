package com.sellgirl.sgJavaHelper.midi;

import javax.sound.midi.*;

public class QuickNotePlayer {
    private Synthesizer synth;
    private MidiChannel channel;

    public void init() throws MidiUnavailableException {
        // 1. 获取并打开合成器
        synth = MidiSystem.getSynthesizer();
        synth.open();

        // 2. 加载默认乐器（例如，钢琴）
        Instrument[] instr = synth.getDefaultSoundbank().getInstruments();
        synth.loadInstrument(instr[0]);

        // 3. 获取频道（通常使用第一个频道）
        MidiChannel[] channels = synth.getChannels();
        channel = channels[0];
    }

    public void noteOn(int noteNumber) {
        noteOn(noteNumber,100);
    }
    public void noteOn(int noteNumber, int velocity) {
        if (channel != null) {
            channel.noteOn(noteNumber, velocity);
        }
    }

    public void noteOff(int noteNumber) {
        if (channel != null) {
            channel.noteOff(noteNumber);
        }
    }

    public void close() {
        synth.close();
    }

    @Deprecated
    public int getX(int[] x) {
        int h=MIDIMusic.getRealSoundHeight(x[0])+x[1]*12+x[2];
        return h;
    }

    public int xToNote(int[] x) {
        int noteHeight = 48;     //Òô·ûµÄÒô¸ß 0-127£¨48ÊÇC´óµ÷µÄdo¡£48,50,52,53,55,57,59¾ÍÊÇdo ruai,mi,fa,so,la,xi)
        int noteVolume  = 100;   //Òô·ûµÄÒôÁ¿´óÐ¡

        int h=MIDIMusic.getRealSoundHeight(x[0])+x[1]*12+x[2];//12: 1-> 高音1之间是相差12个音

        return noteHeight+h;
    }
    /**
     * [1~7,低高音阶,加减半音,音长,音量]
     * @param x
     */
    public void downX(int[] x) {
        int noteHeight = 48;     //Òô·ûµÄÒô¸ß 0-127£¨48ÊÇC´óµ÷µÄdo¡£48,50,52,53,55,57,59¾ÍÊÇdo ruai,mi,fa,so,la,xi)
        int noteVolume  = 100;   //Òô·ûµÄÒôÁ¿´óÐ¡

        int h=MIDIMusic.getRealSoundHeight(x[0])+x[1]*12+x[2];//12: 1-> 高音1之间是相差12个音
        
        noteOn(noteHeight+h, noteVolume);

//        Thread.sleep(1000); // 让声音持续1秒
//        noteOff(60);
    }
    public void upX(int[] x) {
        int noteHeight = 48;     //Òô·ûµÄÒô¸ß 0-127£¨48ÊÇC´óµ÷µÄdo¡£48,50,52,53,55,57,59¾ÍÊÇdo ruai,mi,fa,so,la,xi)
        int noteVolume  = 100;   //Òô·ûµÄÒôÁ¿´óÐ¡

        int h=MIDIMusic.getRealSoundHeight(x[0])+x[1]*12;//12: 1-> 高音1之间是相差12个音
        
//        noteOn(noteHeight+h, noteVolume);
//        Thread.sleep(1000); // 让声音持续1秒
        noteOff(noteHeight+h);
    }
    // 使用示例
    public static void main(String[] args) throws Exception {
        QuickNotePlayer player = new QuickNotePlayer();
        player.init();

//        // 播放中央C（音符60），力度100
//        player.noteOn(60, 100);
//        Thread.sleep(1000); // 让声音持续1秒
//        player.noteOff(60);
        
        player.downX(new int[] {5,-1,0});
        Thread.sleep(500); // 让声音持续1秒
        player.upX(new int[] {5,-1,0});

        player.downX(new int[] {1,1,0});
        Thread.sleep(1000); // 让声音持续1秒
        player.upX(new int[] {1,1,0});

        player.close();
    }
}