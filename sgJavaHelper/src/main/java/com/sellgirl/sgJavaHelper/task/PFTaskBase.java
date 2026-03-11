package com.sellgirl.sgJavaHelper.task;

import java.util.concurrent.atomic.AtomicBoolean;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public abstract class PFTaskBase implements IPFTask {
	public String HashId = null;
	public Thread TaskThread = null;

	/**
	 * 此属性主要是控制,多线程下不会多次调用Start.此过程和Stop无关
	 */
	public AtomicBoolean _running = new AtomicBoolean(false);
	//private int number=0;//用于测试防止Start()的线程安全

	public void Start() {
		if (_running.compareAndSet(false, true)) {
//			number++;
//			if(number>1) {
//				//throw new RuntimeException("xxxxxxxxxxxxxx");
//				System.out.println("2222222222222222222");
//			}

			TaskThread = new Thread() {// 线程操作
				public void run() {
					StartThread();
				}
			};
			TaskThread.start();
			//number--;
		}
	}

	public void Stop() {
		if (_running.compareAndSet(true, false)) {
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
	public Boolean IsRunning() {
		return _running.get();
	}

}
