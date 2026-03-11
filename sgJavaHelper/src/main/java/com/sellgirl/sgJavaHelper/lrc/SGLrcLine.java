package com.sellgirl.sgJavaHelper.lrc;

public class SGLrcLine {
//    private String t;
	/**
	 * 单位：秒
	 */
    private float t;
//    private Long time;

    private String l;
    public SGLrcLine(float t, String l) {
        this.t = t;
        this.l = l;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public float getT() {
        return t;
    }

    public void setT(float t) {
        this.t = t;
    }
//    public Long getTime() {
//    	return time;
//    }
//    public void setTime(long time) {
//        this.time = time;
//    }
}
