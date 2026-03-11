package com.sellgirl.sgJavaHelper.task;

import java.util.function.Function;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;
//import com.sellgirl.pfHelperNotSpring.model.PFEnvir;
//import redis.clients.jedis.Jedis;
public class PFIntervalTask
//implements IPFTask
extends PFTaskBase
{

	public double _intervalMinute= 15;

	//public PFFunc<String, PFDate, PFIntervalTask,Boolean> DoAction;
	public Function<PFIntervalTask,Boolean> DoAction;
	
	
	public PFIntervalTask( String hashId,
			//PFFunc<String, PFDate, PFIntervalTask,Boolean> doAction,
			Function<PFIntervalTask,Boolean> doAction,
			double intervalMinute) {
		HashId = hashId;
		DoAction = doAction;
		
		_intervalMinute=intervalMinute;
	}
	

	@Override
	protected void StartThread() {
		while (_running.get()) {
			try {
				DoAction.apply(this);
				Thread.sleep(Double.valueOf(_intervalMinute*60*1000).longValue());
				//TaskThread.wait(Double.valueOf(_intervalMinute*60*1000).longValue());
				SGDataHelper.GCCollect(true); //一定要有句，否则SendMobileMessage里面的所有List会使内存越来越高

			} catch (Exception e) {
				 SGDataHelper.WriteError(e);
				//PFDataHelper.WriteError(new Throwable(), e);
			}

		}
	}
}
