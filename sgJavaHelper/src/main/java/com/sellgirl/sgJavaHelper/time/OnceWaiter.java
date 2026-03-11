package com.sellgirl.sgJavaHelper.time;

/**
 * 用于控制多少秒后执行1次的操作(常用于控制message多少秒后自动消失的情况)
 * 使用方法：
 * 1. waiter.start()
 * 2. if(waiter.isOK()){...}
 */
public class OnceWaiter extends Waiter {
//    private long milliSecond;
//    private long now;
	private boolean started=false;
    public OnceWaiter(int second){
    	super(second);
//        this.milliSecond=second*1000;
//        now=System.currentTimeMillis();
    }
    public void start() {
        now=System.currentTimeMillis();
    	started=true;
    }
//
    public boolean isOK(){
//        long n=System.currentTimeMillis();
//        if(n-now>milliSecond){
//            now=n;
//            return true;
//        }
//        return false;
    	if(started) {
    		boolean b=super.isOK();
    		if(b) {started=false;}
    		return b;
		}
    	else {return false;}
    }
//    /**
//     * second
//     * @return
//     */
//    public float getDelta() {
////    	return (System.currentTimeMillis()-now)/1000l;//这样是整数
//    	return ((Long)(System.currentTimeMillis()-now)).floatValue()/1000f;
//    }
}
