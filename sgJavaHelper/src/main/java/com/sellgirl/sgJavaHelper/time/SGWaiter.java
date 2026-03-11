package com.sellgirl.sgJavaHelper.time;

/**
 * 用于控制每多少秒执行1次的操作
 * 使用方法：
 * 1. if(waiter.isOK()){...}
 */
public class SGWaiter {
    private long milliSecond;
    protected long now;
    public SGWaiter(long milliSecond){
        this.milliSecond=milliSecond;
        now=System.currentTimeMillis();
    }

    public boolean isOK(){
        long n=System.currentTimeMillis();
        if(n-now>milliSecond){
            now=n;
            return true;
        }
        return false;
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
