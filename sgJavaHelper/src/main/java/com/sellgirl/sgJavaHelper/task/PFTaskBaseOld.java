package com.sellgirl.sgJavaHelper.task;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/**
 * 这个是旧版本,_running可能有线程安全
 * @author Administrator
 *
 */
public abstract class PFTaskBaseOld implements IPFTask {
	public String HashId = null;
	public Thread TaskThread = null;
	public Boolean _running = false;
	private int number=0;

	public void Start() {
		if (!_running) {
			number++;
			if(number>1) {
				//throw new RuntimeException("xxxxxxxxxxxxxx");
				System.out.println("2222222222222222222");
			}
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			_running = true;
			//number++;

			TaskThread = new Thread() {// 线程操作
				public void run() {
					StartThread();
				}
			};
			TaskThread.start();

			number--;
		}
	}

	public void Stop() {
		if (_running) {
			_running = false;
			//number--;
			try {
				TaskThread.interrupt();// .destroy();//之前先释放MessageService的话,进程里仍在使用MessageService会报错,现在改为先释放Thread试试
			} catch (Exception e) {
				//PFDataHelper.WriteError(new Throwable(),e);
				SGDataHelper.WriteError(e);
			}

		}
	}
	protected abstract void StartThread() ;
//	public int getNumber() {
//		return number;
//	}
	
	@Override
	public Boolean IsRunning() {return _running;}
}
