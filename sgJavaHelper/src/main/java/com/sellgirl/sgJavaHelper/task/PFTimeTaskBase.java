package com.sellgirl.sgJavaHelper.task;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

import com.sellgirl.sgJavaHelper.SGEmailSend;
import com.sellgirl.sgJavaHelper.PFFunc3;
import com.sellgirl.sgJavaHelper.PFRedisConfig;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.config.SGDataHelper.LocalDataType;
import com.sellgirl.sgJavaHelper.model.PFEnvir;

import redis.clients.jedis.Jedis;

/**
 * 
 * @author li
 * @Deprecated 考虑用更好的PFTimeTaskBase2 来取代
 * @param <T>
 */
@Deprecated
public abstract class PFTimeTaskBase<T> extends PFTaskBase implements IPFTimeTask {
//	public String HashId = null;
//	public Thread TaskThread = null;
//	public Boolean _running = false;
	/**
	 * 先试试不写文件行不行(停电应该可以通过之前加的补偿机制来处理)
	 */
	protected boolean _isFailed=false;
	/**
	 * 表示正在进行action相关操作或状态更新
	 */
	protected AtomicBoolean _isDoing=new AtomicBoolean(false);
	/**
	 * 服务器重启时错过的任务
	 */
	protected boolean _isMissed=false;
	protected TaskFollower[] _follower=null;
//	/// <summary>
//	/// 备份的日期
//	/// </summary>
//	public int BackupDay = 0;
//	/// <summary>
//	/// 备份的小时
//	/// </summary>
//	public int BackupHour = 0;
//	/// <summary>
//	/// 备份的分钟(便于测试)
//	/// </summary>
//	public int BackupMinute = 0;
	/// <summary>
	/// 上次备份的时间,便于增量更新
	/// </summary>
	public SGDate _lastBackupTime = null;
	public String _lastBackupTimeX = null;// 带格式,便于知道当天有没有运行过,格式根据基类可能不同 如 yyyy.MM 或 yyyy-MM-dd
	/// <summary>
	/// 第一次执行要在此时间之后(默认为类初始化的时间)
	/// 注:这是为了实现18点设置1点执行的话,是立即执行还是第二天开始执行
	/// </summary>
	public SGDate FirstRunTime = null;
	/**
	 * 下次执行的时间(准备用这个属性代替FirstRunTime)
	 */
	public SGDate _nextRunTime = null;
//	protected Jedis jedis = null;
//	protected PFRedisConfig jedisConfig=null;
	protected boolean lockWithRedis = false;
	public PFFunc3<String, SGDate, T,Boolean> DoAction;
	/**
	 * 暂时只有PFMonthCheckTaskT用到此属性
	 */
	public PFFunc3<String, SGDate, T, Boolean> CanDoAction = null;
	/**
	 * 完成度 0~100 (便于显示任务进度)
	 */
	protected int FinishedPercent = 0;
	private String finishedMsg = "";
	

	//protected boolean _needRedo = false;

//	/**
//	 * @deprecated 改用 init(String hashId, PFAction<String, PFDate, T> doAction,boolean jumpThePassedTask)
//	 * @param hashId
//	 * @param doAction
//	 */
//	public void init(String hashId, PFAction<String, PFDate, T> doAction) {
////		HashId = hashId;
////		DoAction = doAction;
////
//////    //这里按一般的习惯,如果当前8时,设置为2时,会等到明天2时才执行
//////    PFDate now=PFDate.Now();
//////    //PFDate firstRunTime = new PFDate(now.GetYear(), now.GetMonth(), now.GetDay(), BackupHour, BackupMinute, 0);
//////    PFDate firstRunTime = GetBackupDay(now);
//////    FirstRunTime = now.compareTo(firstRunTime)>0 ? firstRunTime.AddDays(1) : firstRunTime;    
//////    _nextRunTime = FirstRunTime;
////		// 这里按一般的习惯,如果当前8时,设置为2时,会等到明天2时才执行
////		PFDate now = PFDate.Now();
////		// PFDate firstRunTime = new PFDate(now.GetYear(), now.GetMonth(), now.GetDay(),
////		// BackupHour, BackupMinute, 0);
////		PFDate firstRunTime = GetBackupDay(now);
////		// FirstRunTime = now.compareTo(firstRunTime)>0 ? firstRunTime.AddDays(1) :
////		// firstRunTime;
////		FirstRunTime = now.compareTo(firstRunTime) > 0 ? GetNextTime(firstRunTime) : firstRunTime;
////		_nextRunTime = FirstRunTime;
////
////		if (PFDataHelper.GetConfigMapper().GetRedisConfig() != null) {
////			lockWithRedis=true;
////			try {
////				PFDataHelper.ReTry((a,b,c)->{
////					Jedis jedis = PFDataHelper.GetConfigMapper().GetRedisConfig().open();
////					jedis.del(HashId);// 以防任务中断时刚好没有清空redis
////					PFRedisConfig.close(jedis);
////				});
////			} catch (Exception e) {
////				PFDataHelper.WriteError(new Throwable(), e);
////				;
////			}
////		}
//		
//		init( hashId, doAction,true);
//	}
	/**
	 * 
	 * @param hashId
	 * @param doAction
	 * @param jumpThePassedTask 原本默认跳过已经超过时间点的任务,从now开始执行第一次,但后来发现这样有个不好的地方,服务器如果停电重启之后,那些超过时间点的任务就不会执行了(原本的逻辑里,相当于此参数默认给true)(其实既然有历史记录防止重执行,此参数都给false值是没大问题的)(本质上,此参数只影响启动任务后的第一个时间点,从第二个时间点开始无任何影响)
	 */
	public void init(String hashId, PFFunc3<String, SGDate, T,Boolean> doAction,boolean jumpThePassedTask,TaskFollower[] follower) {
		HashId = hashId;
		DoAction = doAction;
		_follower=follower;

//    //这里按一般的习惯,如果当前8时,设置为2时,会等到明天2时才执行
//    PFDate now=PFDate.Now();
//    //PFDate firstRunTime = new PFDate(now.GetYear(), now.GetMonth(), now.GetDay(), BackupHour, BackupMinute, 0);
//    PFDate firstRunTime = GetBackupDay(now);
//    FirstRunTime = now.compareTo(firstRunTime)>0 ? firstRunTime.AddDays(1) : firstRunTime;    
//    _nextRunTime = FirstRunTime;
		// 这里按一般的习惯,如果当前8时,设置为2时,会等到明天2时才执行
		SGDate now = SGDate.Now();
		// PFDate firstRunTime = new PFDate(now.GetYear(), now.GetMonth(), now.GetDay(),
		// BackupHour, BackupMinute, 0);
		SGDate firstRunTime = GetBackupDay(now);
		// FirstRunTime = now.compareTo(firstRunTime)>0 ? firstRunTime.AddDays(1) :
		// firstRunTime;
		if(jumpThePassedTask) {
			FirstRunTime = now.compareTo(firstRunTime) > 0 ? GetNextTime(firstRunTime) : firstRunTime;
		}else {//这部分的逻辑是新增的,为了使服务器停电时执行超过时间点的任务,这部分要求把最后一次的执行时间保存下来(防止把执行过的任务又执行一次)
			String backupTimeX=GetTimeX();
			String lastBackupTimeX=SGDataHelper.ReadLocalTxt(Paths.get("TaskHistory", GetHashId(),"lastBackupTimeX.txt").toString(), LocalDataType.User);
			if(lastBackupTimeX!=null&&lastBackupTimeX.equals(backupTimeX)) {//当 日|月 的任务已经执行过
				FirstRunTime = now.compareTo(firstRunTime) > 0 ? GetNextTime(firstRunTime) : firstRunTime;
			}else {
				FirstRunTime = firstRunTime;
				if(now.compareTo(firstRunTime) > 0) {
					_isMissed=true;
				}
			}
		}
		_nextRunTime = FirstRunTime;

		if (SGDataHelper.GetConfigMapper().GetRedisConfig() != null) {
			lockWithRedis=true;
			try {
//				PFDataHelper.ReTry((a,b,c)->{
					Jedis jedis = SGDataHelper.GetConfigMapper().GetRedisConfig().open();
					jedis.del(HashId);// 以防任务中断时刚好没有清空redis
					PFRedisConfig.close(jedis);
//				});
			} catch (Exception e) {
				SGDataHelper.WriteError(new Throwable(), e);
				;
			}
		}
	}

//	public void Start() {
//		if (!_running) {
//			_running = true;
//
//			TaskThread = new Thread() {// 线程操作
//				public void run() {
//					StartThread();
//				}
//			};
//			TaskThread.start();
//
////            TaskThread = new Thread(new ParameterizedThreadStart(StartThread));
////            TaskThread.Start();
//		}
//	}
//
//	public void Stop() {
//		if (_running) {
//			_running = false;
//			try {
//				TaskThread.interrupt();// .destroy();//之前先释放MessageService的话,进程里仍在使用MessageService会报错,现在改为先释放Thread试试
//			} catch (Exception e) {
//				PFDataHelper.WriteError(new Throwable(),e);
//			}
//
//		}
//	}

	// protected abstract void StartThread();
	protected boolean DoActionCheckRedis(String cDay) {
		boolean b=false;
		//if (jedis != null && PFEnvir.release == PFDataHelper.CurrentEnvironmental) {
		if (lockWithRedis&& PFEnvir.release == SGDataHelper.CurrentEnvironmental) {
			try {
//				if(jedis==null) {
//					jedis = PFDataHelper.GetConfigMapper().GetRedisConfig().open();
//				}else if (!jedis.isConnected()) {
//					jedis.connect();
//				}
				Jedis jedis = SGDataHelper.GetConfigMapper().GetRedisConfig().open();
				if (jedis.setnx(GetHashId(), "1") == 1) {
					//jedis.close();//如果用完不关闭可能会有问题 https://www.cnblogs.com/kissazi2/archive/2012/09/04/2669830.html
					PFRedisConfig.close(jedis);
					try {
						b=DoAction.go(cDay, _lastBackupTime, SGDataHelper.ObjectAs(this));
					} catch (Exception e) {// 为了避免报错时不删除redis锁
						SGDataHelper.WriteError(new Throwable(),e);
					}
					SGDataHelper.ReTry((a1, b1, c1) -> {
						Jedis jedis2= SGDataHelper.GetConfigMapper().GetRedisConfig().open();
						jedis2.del(GetHashId());
//						jedis.close();
						PFRedisConfig.close(jedis);
					});
				} else {
					PFRedisConfig.close(jedis);
					String log = SGDataHelper.FormatString("redis中检测到有重复的{0}任务在其它环境上正在执行,所以本次执行跳过", GetHashId());
					SGDataHelper.WriteLog(log);
					System.out.println(log);
				}
			} catch (Exception e) {// 如果redis有网络异常,就直接执行好了
				try {
					b=DoAction.go(cDay, _lastBackupTime, SGDataHelper.ObjectAs(this));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					SGDataHelper.WriteError(new Throwable(), e1);
					return false;
				}
				SGDataHelper.WriteError(new Throwable(),e);
			}
		} else {
			try {
				b=DoAction.go(cDay, _lastBackupTime, SGDataHelper.ObjectAs(this));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				SGDataHelper.WriteError(new Throwable(), e);
				return false;
			}
		}
		return b;
	}

	/**
	 * 获得当前时间的执行日期格式,如 yyyy.MM 或 yyyy-MM-dd
	 * 
	 * @return
	 */
	protected abstract String GetTimeX();

	/**
	 * 获得当天/月的备份时间
	 * 
	 * @param now
	 * @return
	 */
	protected abstract SGDate GetBackupDay(SGDate now);

	protected abstract SGDate GetNextTime(SGDate backupDay);
	
	public void SetNextBackupTime(Calendar calendar) {
//		// BackupHour, BackupMinute, 0);
//		PFDate pfDate=new PFDate(calendar);
//		PFDate firstRunTime = this.GetBackupDay(pfDate);
////		FirstRunTime = pfDate.compareTo(firstRunTime) > 0 ? GetNextTime(firstRunTime) : firstRunTime;
////		_nextRunTime = FirstRunTime;
//		while(pfDate.compareTo(firstRunTime) > 0) {
//			firstRunTime=GetNextTime(firstRunTime);
//		}
//		FirstRunTime =firstRunTime;
//		_nextRunTime = FirstRunTime;
		
		//PFDate pfDate=new PFDate(calendar);
		//PFDate firstRunTime = this.GetBackupDay(pfDate);//注意这里的逻辑和Init时不一样，是要根据calendar改变定时设置的，所以不能用GetBackupDay
		SGDate firstRunTime = new SGDate(calendar);
		SGDate now=SGDate.Now();
//		FirstRunTime = pfDate.compareTo(firstRunTime) > 0 ? GetNextTime(firstRunTime) : firstRunTime;
//		_nextRunTime = FirstRunTime;
		while(now.compareTo(firstRunTime) > 0) {
			firstRunTime=GetNextTime(firstRunTime);
		}
		ResetBackupTime(firstRunTime.ToCalendar());
//		FirstRunTime =firstRunTime;
//		_nextRunTime = FirstRunTime;
	}

	public void ResetBackupTime(Calendar calendar) {
		SGDate time = new SGDate(calendar);
//        BackupDay = time.GetDay();
//        BackupHour = time.GetHour();
//        BackupMinute = time.GetMinute();

		FirstRunTime = time;
		_nextRunTime = FirstRunTime;
		// _lastBackupCmonth = null;
		if(!_isDoing.get()) {
			_lastBackupTimeX = null;
		}
	}
	public void RedoFailed() {
		if(this.isFailed()//&&!_isDoing
				) {
			//_lastBackupTimeX = null;//旧版直接这样的话,redo会有bug,在Thread的while里对_lastBackupTimeX给值后,等待action的时候,如果又执行了RedoFail,即便action成功了也会重做,后来改为在doaction后多设置一次_lastBackupTimeX,似乎可以解决此问题
			
			if(!_isDoing.get()) {
				_lastBackupTimeX = null;//旧版直接这样的话,redo会有bug,在Thread的while里对_lastBackupTimeX给值后,等待action的时候,如果又执行了RedoFail,即便action成功了也会重做,后来改为在doaction后多设置一次_lastBackupTimeX,似乎可以解决此问题
			}
			
		}
	}
//	private Path GetFailedTimeXHistoryFilePath() {
//		return Paths.get("TaskHistory", GetHashId(),"failedTimeX.txt");
//	}

	protected void sendMsgToFollower(String msgTitle,String msg) {
//		String msg = PFDataHelper.FormatString("任务 {0} 补偿执行完成", HashId);
//		String msgTitle=PFDataHelper.FormatString("任务 {0} 补偿执行通知",HashId);
		String[] telephones=Arrays.asList(_follower).stream().map(a->a.getTelephone()).filter(a->!SGDataHelper.StringIsNullOrWhiteSpace(a)).toArray(String[]::new);
		SGDataHelper.SendMobileMessage(telephones, msg);

		String[] emails=Arrays.asList(_follower).stream().map(a->a.getEmail()).filter(a->!SGDataHelper.StringIsNullOrWhiteSpace(a)).toArray(String[]::new);
		SGEmailSend.SendMail(emails,
				msgTitle,  msg);

		SGEmailSend.SendMail(new String[] { SGEmailSend.EMAIL_OWNER_ADDR }, msgTitle,
				SGDataHelper.FormatString("{0} {1}",msg,
						"已通知关注者"));
	}
	@Override
	protected void StartThread() {
		// _lastBackupCmonth = null;
		_lastBackupTimeX = null;
		_lastBackupTime = null;
		while (_running.get()) {
			try {
				// String cmonth = GetCmonth();
				String cmonth = GetTimeX();
				// if (cmonth.equals(_lastBackupCmonth))//该月已执行
				if (cmonth.equals(_lastBackupTimeX))// 该月已执行
				{
					// Thread.Sleep(ProjDataHelper.CheckMessageInterval);
					Thread.sleep(SGDataHelper.CheckMessageInterval());
					continue;
				}

				SGDate now = SGDate.Now();

//                PFDate backupDay = new PFDate(now.GetYear(), now.GetMonth(), BackupDay, BackupHour, BackupMinute, 0);
				SGDate backupDay = GetBackupDay(now);
				// if (backupDay > now || FirstRunTime > now)//未到执行的日期
				if (backupDay.compareTo(now) > 0 || FirstRunTime.compareTo(now) > 0) {
					// Thread.Sleep(PFTaskHelper.CheckMessageInterval);
					Thread.sleep(SGDataHelper.CheckMessageInterval());
					continue;
				}

				if (CanDoAction != null && (!CanDoAction.go(cmonth, _lastBackupTime, SGDataHelper.ObjectAs(this)))) {
					Thread.sleep(SGDataHelper.CheckMessageInterval());
					continue;
				}
				//_isDoing=true;
				_isDoing.set(true);
				// _lastBackupCmonth = cmonth;
				_lastBackupTimeX = cmonth;//现在这样的话,redo会有bug,当此句执行后,等待action的时候,如果又执行了RedoFail,使
				// _nextRunTime = backupDay.AddMonths(1);
				_nextRunTime = GetNextTime(backupDay);

				// PFDataHelper.WriteLog(PFDataHelper.FormatString("任务{0}开始执行,月份为:{1}", HashId,
				// cmonth));//调用这个方法来写这个值并不好,因为每天都单独一个txt的
				SGDataHelper.WriteLog(SGDataHelper.FormatString("任务{0}开始执行,日期为:{1}", HashId, cmonth));
				try {
//                    DoPFAction(cmonth, this);
					// DoPFAction.go(cmonth, this,null);
					//boolean b=DoActionCheckRedis(cmonth);
					if(this.FinishedPercent<1) {
						this.FinishedPercent=1;
					}
					boolean lastTimeIsFailed=_isFailed;
					_isFailed=!DoActionCheckRedis(cmonth);
					_lastBackupTimeX = cmonth;//加上这句比较保险
//					if(b) {
					if(!_isFailed) {
						String msg = SGDataHelper.FormatString("任务{0}执行完毕,日期为:{1}", HashId, cmonth);
						if((lastTimeIsFailed||_isMissed)&&_follower!=null&&_follower.length>0) {
							String msgTitle=SGDataHelper.FormatString("任务 {0} 补偿执行通知",HashId);
//							String[] telephones=Arrays.asList(_follower).stream().map(a->a.getTelephone()).filter(a->!PFDataHelper.StringIsNullOrWhiteSpace(a)).toArray(String[]::new);
//							PFDataHelper.SendMobileMessage(telephones, msg);
//
//							String[] emails=Arrays.asList(_follower).stream().map(a->a.getEmail()).filter(a->!PFDataHelper.StringIsNullOrWhiteSpace(a)).toArray(String[]::new);
//							PFEmailSend.SendMail(emails,
//									msgTitle,  msg);
//
//							PFEmailSend.SendMail(new String[] { PFEmailSend.EMAIL_OWNER_ADDR }, msgTitle,
//									PFDataHelper.FormatString("{0} {1}",msg,
//											"已通知关注者"));
							sendMsgToFollower(msgTitle,msg);
						}

						_lastBackupTime = now;
						SGDataHelper.WriteLocalTxt(_lastBackupTimeX,Paths.get("TaskHistory", GetHashId(),"lastBackupTimeX.txt").toString(), LocalDataType.User);
						// PFDataHelper.WriteLog(PFDataHelper.FormatString("任务{0}执行完毕,月份为:{1}", HashId,
						// cmonth));
						SGDataHelper.WriteLog(msg);
						//PFDirectory.DeleteFile(GetFailedTimeXHistoryFilePath().toString());
						
						//PFDataHelper.DeleteLocalTxtFile(GetFailedTimeXHistoryFilePath().toString(), LocalDataType.User);
						//this._isFailed=false;	
						this._isMissed=false;
					}else {
						String errMsg=SGDataHelper.FormatString("任务{0}执行失败,日期为:{1}", HashId, cmonth);
						SGDataHelper.WriteError(new Throwable(), new Exception(errMsg));
						//this._isFailed=true;	
						if(_follower!=null&&_follower.length>0) {
							String msgTitle=SGDataHelper.FormatString("任务 {0} 执行失败通知",HashId);
							sendMsgToFollower(msgTitle,errMsg);
						}
					}
				} catch (Exception e) {
					// PFDataHelper.WriteError(e);
					SGDataHelper.WriteError(new Throwable(), e);
					//PFDataHelper.WriteLocalTxt(_lastBackupTimeX,GetFailedTimeXHistoryFilePath().toString(), LocalDataType.User);
					this._isFailed=true;
					
				}
				SGDataHelper.GCCollect(true);
				// GC.Collect();//一定要有句，否则SendMobileMessage里面的所有List会使内存越来越高

			} catch (Exception e) {
				// PFDataHelper.WriteError(e);
				SGDataHelper.WriteError(new Throwable(), e);
			}
//			_isDoing=false;
			_isDoing.set(false);
		}
	}

	@Override
	public String GetHashId() {
		return HashId;
	}

	@Override
	public Boolean IsRunning() {
		return _running.get();
	}

	@Override
	public int GetFinishedPercent() {
		return FinishedPercent;
	}

	@Override
	public void SetFinishedPercent(int percent) {
		if(percent<1) {
			percent=1;
		}
		FinishedPercent = percent;
	}

	@Override
	public String getFinishedMsg() {
		return finishedMsg;
	}

	@Override
	public void setFinishedMsg(String finishedMsg) {
		this.finishedMsg = finishedMsg;
	}

	@Override
	public boolean isFailed() {
//		String backupTimeX=GetTimeX();
//		String lastFailedTimeX=PFDataHelper.ReadLocalTxt(GetFailedTimeXHistoryFilePath().toString(), LocalDataType.User);
//		if(lastFailedTimeX!=null&&lastFailedTimeX.equals(backupTimeX)) {//当 日|月 的任务已经执行过
//			return true;
//		}else {
//			return false;
//		}
		return _isFailed;
	}
	@Override
	public boolean isDoing() {
//		String backupTimeX=GetTimeX();
//		String lastFailedTimeX=PFDataHelper.ReadLocalTxt(GetFailedTimeXHistoryFilePath().toString(), LocalDataType.User);
//		if(lastFailedTimeX!=null&&lastFailedTimeX.equals(backupTimeX)) {//当 日|月 的任务已经执行过
//			return true;
//		}else {
//			return false;
//		}
		return _isDoing.get();
	}
}
