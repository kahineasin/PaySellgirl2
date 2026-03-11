package org.sgGameHelper.mp3;

public class Mp3Model {
	public enum AudioVersion{
		MPEG1,
		MPEG2,
		/**
		 * MPEG2.5
		 */
		MPEG2d5,
		/**
		 * 保留
		 */
		KEEP
	}
	public enum ChannelMode{
		联合立体声,
		立体声,
		双声道,
		单声道
	}
    public enum MusicFormat{
        NONE,
        MP3
    }
	private String fileName;
	//识别的格式
	private Long size;
	private Integer frequency;
	private AudioVersion audioVersion;
	private ChannelMode channelMode;
	private Boolean hasTag;
	//识别的专辑
	private String songName;
	private String singer;
	private String album;
	//播放异常
	private Boolean noSound;
	private Boolean fastComplete;
    private MusicFormat musicFormat;
	

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public Integer getFrequency() {
		return frequency;
	}
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	public AudioVersion getAudioVersion() {
		return audioVersion;
	}
	public void setAudioVersion(AudioVersion audioVersion) {
		this.audioVersion = audioVersion;
	}
	public ChannelMode getChannelMode() {
		return channelMode;
	}
	public void setChannelMode(ChannelMode channelMode) {
		this.channelMode = channelMode;
	}
	public String getSongName() {
		return songName;
	}
	public void setSongName(String songName) {
		this.songName = songName;
	}
	public String getSinger() {
		return singer;
	}
	public void setSinger(String author) {
		this.singer = author;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public Boolean isHasTag() {
		return hasTag;
	}
	public void setHasTag(Boolean hasTag) {
		this.hasTag = hasTag;
	}

	public Boolean getNoSound() {
		return noSound;
	}
	public void setNoSound(Boolean noSound) {
		this.noSound = noSound;
	}
	public Boolean getFastComplete() {
		return fastComplete;
	}
	public void setFastComplete(Boolean fastComplete) {
		this.fastComplete = fastComplete;
	}
	public Boolean getHasTag() {
		return hasTag;
	}
	
	public MusicFormat getMusicFormat() {
		return musicFormat;
	}
	public void setMusicFormat(MusicFormat musicFormat) {
		this.musicFormat = musicFormat;
	}
}
