package com.sellgirl.sgGameHelper;

public class ScreenSetting {

	private  float WORLD_WIDTH = 1800;
	private  float WORLD_HEIGHT = 1000;
	private  float WORLD_HEIGHT_METER =3f;// 4米的话人物像素太低;
	private  int FPS=60;
//	/**
//	 * 每帧的时间
//	 */
//	public static final float SPF=1f/60;
//	/**
//	 * SPF*1.3. 慢1.3倍,我都乐观接受
//	 */
//	public static final float SPFM=1.3f/60;
	/**
	 * 像素:米
	 */
    private Float PIXEL_DIVIDE_METER=null;//=WORLD_HEIGHT/WORLD_HEIGHT_METER;
	/**
	 * 像素:米
	 */
    public float getPIXEL_DIVIDE_METER() {
    	if(null==PIXEL_DIVIDE_METER) {PIXEL_DIVIDE_METER=WORLD_HEIGHT/WORLD_HEIGHT_METER;}
    	return PIXEL_DIVIDE_METER;
    }

	public float getWORLD_WIDTH() {
		return WORLD_WIDTH;
	}
	public void setWORLD_WIDTH(float wORLD_WIDTH) {
		WORLD_WIDTH = wORLD_WIDTH;
	}
	public float getWORLD_HEIGHT() {
		return WORLD_HEIGHT;
	}
	public void setWORLD_HEIGHT(float wORLD_HEIGHT) {
		WORLD_HEIGHT = wORLD_HEIGHT;
	}
	public float getWORLD_HEIGHT_METER() {
		return WORLD_HEIGHT_METER;
	}
	public void setWORLD_HEIGHT_METER(float wORLD_HEIGHT_METER) {
		WORLD_HEIGHT_METER = wORLD_HEIGHT_METER;
	}
	public int getFPS() {
		return FPS;
	}
	public void setFPS(int fPS) {
		FPS = fPS;
	}
}
