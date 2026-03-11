package com.sellgirl.sgJavaHelper.time;

/**
 * 用于控制每多少秒可以点击一次的按钮
 */
public class ButtonWaiter {
    private long milliSecond;
    protected long now;
    public ButtonWaiter(float second){
        this.milliSecond=(long) (second*1000);
        now=System.currentTimeMillis();
    }

    public boolean isOK(){
        long n=System.currentTimeMillis();
        if(n-now>milliSecond){
//            now=n;
            return true;
        }
        return false;
    }
    public void setNow() {
    	now=System.currentTimeMillis();
    }
    /**
     * second
     * @return
     */
    public float getDelta() {
//    	return (System.currentTimeMillis()-now)/1000l;//这样是整数
    	return ((Long)(System.currentTimeMillis()-now)).floatValue()/1000f;
    }
}
