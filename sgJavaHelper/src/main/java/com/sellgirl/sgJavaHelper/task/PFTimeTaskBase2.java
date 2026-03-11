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
 * 定时器基类.
 *
 * 感觉原来的PFTimeTaskBase不够好,应该可以参考cron的方式把定时和interval更好的融合到一起(已经实现成功)
 *
 * 此定时器的原则:
 * 1. 假如是按天执行, 1日成功,2到3日是异常,4日重试成功时,不会以2 3日的时间作为currentTime来执行补偿, 只会按4日的当前时间来执行1次.
 *     如果想要改为可以补偿的话, 需要更准确地记录lastTime才能做到(当前版本没有这样做)
 * @author li
 *
 * @param <T>
 */
public abstract class PFTimeTaskBase2<T> extends PFTaskBase implements IPFTimeTask {

	/**
	 * 先试试不写文件行不行(停电应该可以通过之前加的补偿机制来处理)
	 */
	private boolean _isFailed=false;
	/**
	 * 表示正在进行action相关操作或状态更新(此属性不应该移到PFTaskBase内,因为是专用于控制StartThread内部的定时)
	 */
	private AtomicBoolean _isDoing=new AtomicBoolean(false);
	/**
	 * 锁住定时的时间设置. 否则在多线程下会有意外
	 */
	private AtomicBoolean _isLockTime=new AtomicBoolean(false);
	/**
	 * 为了测试哪个锁没有解开.
	 */
	public String _isLockTimeName="";
	//private boolean _isLockTime2=false;
	/**
	 * 锁时间的间隔. 如果没有这个时间间隔, StartThread方法可能会一直锁着时间, 然后会造成RedoFailed等方法一直等待中.
	 */
	public int lockTimeInterval=2000;
	/**
	 * 服务器重启时错过的任务.
	 * 注意当此属性为true时,任务失败的话是会立即重试的,这样可能是有好处的
	 */
	private boolean _isMissed=false;
	//private boolean _needRedoRailed=false;
	//private PFDate _missedTime=null;
	private TaskFollower[] _follower=null;
	
	public static class TimePoint{
		public  static int every=-1;
	}
	/// <summary>
	/// a备份的日期
	/// </summary>
	public int BackupDay =TimePoint.every;
	/// <summary>
	/// a备份的小时
	/// </summary>
	public int BackupHour = 0;
	/// <summary>
	/// a备份的分钟(便于测试)
	/// </summary>
	public int BackupMinute = 0;
	
	public int _intervalMinute= -1;//15;
	/// <summary>
	/// a上次备份的时间,便于增量更新
	/// </summary>
	/**
	 * 上次备份的时间,便于增量更新
	 * 新版本里也是用此值来判断是否到时间
	 */
	public SGDate _lastBackupTime = null;
	//public String _lastBackupTimeX = null;// 带格式,便于知道当天有没有运行过,格式根据基类可能不同 如 yyyy.MM 或 yyyy-MM-dd

//	/**
//	 * 本来打算删除此属性,但后来发现是很有必要保留的(而且init时必需赋值):
//	 * 1.当_lastBackupTime为null时,必需在init时对FirstRunTime赋值,否则很难判断是否已到时
//	 */
//	public PFDate FirstRunTime = null;
	/**
	 * 下次执行的时间(准备用这个属性代替FirstRunTime)
	 */
	private SGDate _nextRunTime = null;
//	protected Jedis jedis = null;
//	protected PFRedisConfig jedisConfig=null;
	protected boolean lockWithRedis = false;
	public PFFunc3<SGDate, SGDate, T,Boolean> DoAction;
	/**
	 * 暂时只有PFMonthCheckTaskT用到此属性
	 */
	public PFFunc3<SGDate, SGDate, T, Boolean> CanDoAction = null;
	/**
	 * 完成度 0~100 (便于显示任务进度)
	 */
	protected int FinishedPercent = 0;
	private String finishedMsg = "";
	


	/**
	 * 
	 * @param hashId
	 * @param doAction
	 * @param jumpThePassedTask 原本默认跳过已经超过时间点的任务,从now开始执行第一次,但后来发现这样有个不好的地方,服务器如果停电重启之后,那些超过时间点的任务就不会执行了(原本的逻辑里,相当于此参数默认给true)(其实既然有历史记录防止重执行,此参数都给false值是没大问题的)(本质上,此参数只影响启动任务后的第一个时间点,从第二个时间点开始无任何影响)
	 */
	public void init(String hashId, PFFunc3<SGDate, SGDate, T,Boolean> doAction,
			 int backupDay,int backupHour, int backupMinute,
			 int intervalMinute,
			boolean jumpThePassedTask,TaskFollower[] follower) {
		HashId = hashId;
		DoAction = doAction;
		_follower=follower;
		
		this.BackupDay=backupDay;
		this.BackupHour=backupHour;
		this.BackupMinute=backupMinute;
		this._intervalMinute=intervalMinute;


		// 这里按一般的习惯,如果当前8时,设置为2时,会等到明天2时才执行
		SGDate now = SGDate.Now();

		//PFDate firstRunTime = GetBackupDay(now);
		SGDate FirstRunTime=null;
		if(jumpThePassedTask) {
			//FirstRunTime = now.compareTo(firstRunTime) > 0 ? GetNextTime(firstRunTime) : firstRunTime;
			FirstRunTime=this.GetNextTime(now);
			
		}else {//这部分的逻辑是新增的,为了使服务器停电时执行超过时间点的任务,这部分要求把最后一次的执行时间保存下来(防止把执行过的任务又执行一次)
//			String backupTimeX=GetTimeX();
//			String lastBackupTimeX=PFDataHelper.ReadLocalTxt(Paths.get("TaskHistory", GetHashId(),"lastBackupTimeX.txt").toString(), LocalDataType.User);
//			if(lastBackupTimeX!=null&&lastBackupTimeX.equals(backupTimeX)) {//当 日|月 的任务已经执行过
//				FirstRunTime = now.compareTo(firstRunTime) > 0 ? GetNextTime(firstRunTime) : firstRunTime;
//			}else {
//				FirstRunTime = firstRunTime;
//				if(now.compareTo(firstRunTime) > 0) {
//					_isMissed=true;
//				}
//			}

			//新的比较原理是
			//a=用lastBackupTime计算next
			//b=用now计算next
			//如果b>a,那么可以执行
			String lastBackupTimeX=SGDataHelper.ReadLocalTxt(Paths.get("TaskHistory", GetHashId(),"lastBackupTimeX.txt").toString(), LocalDataType.User);
			if(SGDataHelper.StringIsNullOrWhiteSpace(lastBackupTimeX)) {
				//FirstRunTime=this.GetNextTime(PFDate.Now());
				_isMissed=true;
				FirstRunTime=this.GetPrevTime(SGDate.Now());				
			}else {
				try {
				_lastBackupTime=new SGDate(SGDataHelper.StringToDateTime(lastBackupTimeX));
				}catch(Exception e) {
					//e.printStackTrace();
				}
				if(_lastBackupTime!=null) {
					SGDate next1=this.GetNextTime(_lastBackupTime);
					SGDate next2=this.GetNextTime(now);
					if(next1.compareTo(next2)>=0) {//其实一般情况下,最多就是相等,不存在next1大于next2的情况
						//FirstRunTime=next2;
						//没有漏执行的情况
						FirstRunTime=next1;
					}else {
						_isMissed=true;
						FirstRunTime=this.GetPrevTime(SGDate.Now());		
						//_missedTime=next1;
						//FirstRunTime=next1;
					}
				}else {
					//FirstRunTime=this.GetNextTime(PFDate.Now());
					_isMissed=true;
					FirstRunTime=this.GetPrevTime(SGDate.Now());		
				}
			}
			
			
		}
		_nextRunTime = FirstRunTime;
		
//		if(FirstRunTime==null) {
//			throw new Exception("FirstRunTime不能为null");
//		}

		if (SGDataHelper.GetConfigMapper().GetRedisConfig() != null) {
			lockWithRedis=true;
			try {
//				PFDataHelper.ReTry((a,b,c)->{
					Jedis jedis = SGDataHelper.GetConfigMapper().GetRedisConfig().open();
					jedis.del(HashId);// 以防任务中断时刚好没有清空redis
					PFRedisConfig.close(jedis);
//				});
			} catch (Exception e) {
				//PFDataHelper.WriteError(new Throwable(), e);
				SGDataHelper.WriteError( e);
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
	protected boolean DoActionCheckRedis(SGDate cDay) {
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
						//PFDataHelper.WriteError(new Throwable(),e);
						SGDataHelper.WriteError(e);
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
					//PFDataHelper.WriteError(new Throwable(), e1);
					SGDataHelper.WriteError( e1);
					return false;
				}
				//PFDataHelper.WriteError(new Throwable(),e);
				SGDataHelper.WriteError(e);
			}
		} else {
			try {
				b=DoAction.go(cDay, _lastBackupTime, SGDataHelper.ObjectAs(this));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//PFDataHelper.WriteError(new Throwable(), e);
				SGDataHelper.WriteError( e);
				return false;
			}
		}
		return b;
	}

//	/**
//	 * 获得当前时间的执行日期格式,如 yyyy.MM 或 yyyy-MM-dd
//	 * 
//	 * @return
//	 */
//	protected abstract String GetTimeX();

//	/**
//	 * 获得当天/月的备份时间
//	 * 
//	 * @param now
//	 * @return
//	 */
//	protected abstract PFDate GetBackupDay(PFDate now);
	/**
	 * 按时间规则取得时间点
	 * (此方法相当于旧版本让子类复写的PFTimeTaskBase.GetBackupDay)
	 * @param date
	 * @return
	 */
	private SGDate GetBackupPoint(SGDate date) {
		//每日return new PFDate(now.GetYear(), now.GetMonth(), now.GetDay(), BackupHour, BackupMinute, 0);
		//每月return new PFDate(now.GetYear(), now.GetMonth(), BackupDay, BackupHour, BackupMinute, 0);
		return new SGDate(date.GetYear(), date.GetMonth(),
				TimePoint.every==BackupDay? date.GetDay(): BackupDay,
						BackupHour, BackupMinute, 0);
	}
	/**
	 * 按时间规则取得时间点
	 * (此方法相当于旧版本让子类复写的PFTimeTaskBase.GetNextTime)
	 * @param backupPoint
	 * @return
	 */
	private SGDate GetNextBackupPoint(SGDate backupPoint) {
		if(TimePoint.every==BackupDay) {
			return backupPoint.AddDays(1);
		}else {
			return backupPoint.AddMonths(1);
		}
	}
	private SGDate GetPrevBackupPoint(SGDate backupPoint) {
		if(TimePoint.every==BackupDay) {
			return backupPoint.AddDays(-1);
		}else {
			return backupPoint.AddMonths(-1);
		}
	}

	//protected abstract PFDate GetNextTime(PFDate backupDay);
	/**
	 * 根据时间点和间隔计算下1个backupDay
	 * 新版本的这个方法,已经不需要让子类来复写了
	 * @param backupDay
	 * @return
	 */
	private SGDate GetNextTime(SGDate backupDay) {
		//return backupDay.AddDays(1);
		SGDate startPoint=GetBackupPoint(backupDay);
		//PFDate nextPoint=GetBackupPoint(backupDay);
		SGDate currentTime=startPoint.TClone();
		SGDate nextPoint=GetNextBackupPoint(startPoint);
		while(currentTime.compareTo(backupDay)<=0) {//一直的到更大的时间
			if(_intervalMinute<1) {
				currentTime=GetNextBackupPoint(currentTime);
			}else {
				SGDate nextInterval=currentTime.AddMinutes(_intervalMinute);
				if(nextInterval.compareTo(nextPoint)>0) {//如果下个间隔已经超过下个时间点,就取下个时间点
					currentTime=nextPoint.TClone();
					nextPoint=GetNextBackupPoint(currentTime);
				}else {
					currentTime=nextInterval.TClone();
				}
			}
		}
		return currentTime;
	}
	/**
	 * 服务器停机漏几次时,找上1次是有用的
	 * @param backupDay
	 * @return
	 */
	private SGDate GetPrevTime(SGDate backupDay) {
		//return backupDay.AddDays(1);
		SGDate startPoint=GetBackupPoint(backupDay);
		//PFDate nextPoint=GetBackupPoint(backupDay);
		SGDate currentTime=startPoint.TClone();
		SGDate nextPoint=GetPrevBackupPoint(startPoint);
		while(currentTime.compareTo(backupDay)>=0) {
			if(_intervalMinute<1) {
				currentTime=GetPrevBackupPoint(currentTime);
			}else {
				SGDate nextInterval=currentTime.AddMinutes(-_intervalMinute);
				if(nextInterval.compareTo(nextPoint)<0) {//如果下个间隔已经超过下个时间点,就取下个时间点
					currentTime=nextPoint.TClone();
					nextPoint=GetPrevBackupPoint(currentTime);
				}else {
					currentTime=nextInterval.TClone();
				}
			}
		}
		return currentTime;
	}
	
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

	/**
	 * 此方法可以指定运行的时间,也可以用于测试方法里task.start之前设置第一次的运行时间.
	 * @param calendar
	 */
	public void ResetBackupTime(Calendar calendar) {
		SGDate time = new SGDate(calendar);
//        BackupDay = time.GetDay();
//        BackupHour = time.GetHour();
//        BackupMinute = time.GetMinute();

//		FirstRunTime = time;
//		_nextRunTime = FirstRunTime;

		_nextRunTime = time;

//        BackupHour = time.GetHour();
//        BackupMinute = time.GetMinute();
        if(TimePoint.every!=BackupDay) {
        	BackupDay=time.GetDay();
        }
		if(TimePoint.every!=BackupHour) {
			BackupHour=time.GetHour();
		}
		if(TimePoint.every!=BackupMinute) {
			BackupMinute=time.GetMinute();
		}
		// _lastBackupCmonth = null;
		if(!_isDoing.get()) {
//			//_lastBackupTimeX = null;
//			if (_isLockTime.compareAndSet(false, true)) {
//				_lastBackupTime=null;
//				if(_isMissed){_isMissed=false;}
//
//				_isLockTime.set(false);
//			}else{
//				System.out.println("任务isLockTime,ResetBackupTime失败");
//			}
			while(!_isLockTime.compareAndSet(false, true)){
				try {
					Thread.sleep(lockTimeInterval/2);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			_isLockTimeName="ResetBackupTime";
			_lastBackupTime=null;
			if(_isMissed){_isMissed=false;}

			_isLockTimeName="";
			_isLockTime.set(false);
			System.out.println("ResetBackupTime成功");
		}else{
			System.out.println("任务执行中,ResetBackupTime失败");
		}
	}
	public void RedoFailed() {
		if(this.isFailed()//&&!_isDoing
				) {
			//_lastBackupTimeX = null;//旧版直接这样的话,redo会有bug,在Thread的while里对_lastBackupTimeX给值后,等待action的时候,如果又执行了RedoFail,即便action成功了也会重做,后来改为在doaction后多设置一次_lastBackupTimeX,似乎可以解决此问题
			
			if(!_isDoing.get()//&&!_needRedoRailed
			) {
//				if (_isLockTime.compareAndSet(false, true)) {
//					_lastBackupTime=null;
//					_nextRunTime = this.GetPrevTime(PFDate.Now()) ;
//
//					_isLockTime.set(false);
//				}else{
//					System.out.println("任务isLockTime,RedoFailed失败");
//				}
				while(!_isLockTime.compareAndSet(false, true)){
//					_lastBackupTime=null;
//					_nextRunTime = this.GetPrevTime(PFDate.Now()) ;
//
//					_isLockTime.set(false);
					try {
						Thread.sleep(lockTimeInterval/2);
						//System.out.println("lockTimeName:"+_isLockTimeName);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
				_isLockTimeName="RedoFailed";
				_lastBackupTime=null;
				_nextRunTime = this.GetPrevTime(SGDate.Now()) ;
				_isLockTimeName="";
				_isLockTime.set(false);
				System.out.println("RedoFailed成功");
				//System.out.println("11111111111111111111111111");
				//_needRedoRailed=true;
				//_lastBackupTimeX = null;//旧版直接这样的话,redo会有bug,在Thread的while里对_lastBackupTimeX给值后,等待action的时候,如果又执行了RedoFail,即便action成功了也会重做,后来改为在doaction后多设置一次_lastBackupTimeX,似乎可以解决此问题
			}else{
				System.out.println("任务执行中,RedoFailed失败");
				//System.out.println("22222222222222222222222222");
			}
			
		}
	}
//	private Path GetFailedTimeXHistoryFilePath() {
//		return Paths.get("TaskHistory", GetHashId(),"failedTimeX.txt");
//	}

	protected void sendMsgToFollower(String msgTitle,String msg) {
//		String msg = PFDataHelper.FormatString("任务 {0} 补偿执行完成", HashId);
//		String msgTitle=PFDataHelper.FormatString("任务 {0} 补偿执行通知",HashId);
		if(SGDataHelper.GetAppConfig().getSendSysMsg()) {
			String[] telephones=Arrays.asList(_follower).stream().map(a->a.getTelephone()).filter(a->!SGDataHelper.StringIsNullOrWhiteSpace(a)).toArray(String[]::new);
			SGDataHelper.SendMobileMessage(telephones, msg);

			String[] emails=Arrays.asList(_follower).stream().map(a->a.getEmail()).filter(a->!SGDataHelper.StringIsNullOrWhiteSpace(a)).toArray(String[]::new);
			SGEmailSend.SendMail(emails,
					msgTitle,  msg);

			SGEmailSend.SendMail(new String[] { SGEmailSend.EMAIL_OWNER_ADDR }, msgTitle,
					SGDataHelper.FormatString("{0} {1}",msg,
							"已通知关注者"));
		}
	}

	@Override
	protected void StartThread() {
		// _lastBackupCmonth = null;
		//_lastBackupTimeX = null;
		_lastBackupTime = null;
		while (_running.get()) {
			try {
				if (_isLockTime.compareAndSet(false, true)) {
					_isLockTimeName="StartThread";
				}else{
					Thread.sleep(1000);
					continue;
				}

//				if (_isLockTime2) {
//					Thread.sleep(1000);
//					continue;
//				}else{
//					if(!&&_isLockTime.compareAndSet(false, true)){
//
//					}
//				}
				SGDate now = SGDate.Now();
//				//旧的比较原理
//				// String cmonth = GetCmonth();
//				String cmonth = GetTimeX();
//				// if (cmonth.equals(_lastBackupCmonth))//该月已执行
//				if (cmonth.equals(_lastBackupTimeX))// 该月已执行
//				{
//					// Thread.Sleep(ProjDataHelper.CheckMessageInterval);
//					Thread.sleep(PFDataHelper.CheckMessageInterval());
//					continue;
//				}
//
//				PFDate now = PFDate.Now();
//
////                PFDate backupDay = new PFDate(now.GetYear(), now.GetMonth(), BackupDay, BackupHour, BackupMinute, 0);
//				PFDate backupDay = GetBackupDay(now);
//				// if (backupDay > now || FirstRunTime > now)//未到执行的日期
//				if (backupDay.compareTo(now) > 0 || FirstRunTime.compareTo(now) > 0) {
//					// Thread.Sleep(PFTaskHelper.CheckMessageInterval);
//					Thread.sleep(PFDataHelper.CheckMessageInterval());
//					continue;
//				}
				
				SGDate currentTime=null;
				if(this._isMissed) {
					//currentTime=this.FirstRunTime;
					//currentTime=this.GetPrevTime(now);
					currentTime=now;
				}
//				else if(this.isFailed()&&this._needRedoRailed){
//					//重试失败任务的情况
//					this._needRedoRailed=false;
//					currentTime=now;
//				}
				else {
					if(this._lastBackupTime==null) {
						//1次都未执行过的情况,重试失败任务的情况,重新设置执行时间的情况
						////if(now.compareTo(this.FirstRunTime)>0) {
						//if(now.compareTo(this.FirstRunTime)<=0) {
						if(now.compareTo(this._nextRunTime)<=0) {
							_isLockTimeName="";
							_isLockTime.set(false);
							Thread.sleep(SGDataHelper.CheckMessageInterval());
							Thread.sleep(lockTimeInterval);
							continue;
						}else {
							//currentTime=this.FirstRunTime;//这样有可能不准确的,比如外部redo时如果清空了_lastBackupTime,不过后来改为了用_needRedoRailed来表示
							//currentTime=this.GetPrevTime(now);
							currentTime=now;
						}
					}else {

//						//新的比较原理是
//						//a=用lastBackupTime计算next
//						//b=用now计算next
//						//如果b>a,那么可以执行
						SGDate next1=this.GetNextTime(this._lastBackupTime);
						SGDate next2=this.GetNextTime(now);
						if(next1.compareTo(next2)>=0) {//其实一般情况下,最多就是相等,不存在next1大于next2的情况
							_isLockTimeName="";
							_isLockTime.set(false);
							Thread.sleep(SGDataHelper.CheckMessageInterval());
							Thread.sleep(lockTimeInterval);
							continue;
						}else {
							//如果进入这里,是有补偿任务或者漏任务
							//currentTime=next1;
							//currentTime=this.GetPrevTime(now);
							currentTime=now;
						}

					}

				}

				_isLockTimeName="";
				_isLockTime.set(false);
				String cmonth =currentTime.toString();
				//if (CanDoAction != null && (!CanDoAction.go(cmonth, _lastBackupTime, PFDataHelper.ObjectAs(this)))) {
				if (CanDoAction != null && (!CanDoAction.go(currentTime, _lastBackupTime, SGDataHelper.ObjectAs(this)))) {
					Thread.sleep(SGDataHelper.CheckMessageInterval());
					Thread.sleep(lockTimeInterval);
					continue;
				}
				//_isDoing=true;
				_isDoing.set(true);
				// _lastBackupCmonth = cmonth;
				//_lastBackupTimeX = cmonth;//现在这样的话,redo会有bug,当此句执行后,等待action的时候,如果又执行了RedoFail,使
				_lastBackupTime=currentTime;
				// _nextRunTime = backupDay.AddMonths(1);
				_nextRunTime = GetNextTime(currentTime);

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
					//_isFailed=!DoActionCheckRedis(cmonth);
					_isFailed=!DoActionCheckRedis(currentTime);//旧版里月任务是yyyy.MM 日任务是yyyy-MM-dd hh:ss:mm, 现在新版本统一是带时分秒了，所以直接用PFDate吧
					//_lastBackupTimeX = cmonth;//加上这句比较保险
//					if(b) {
					if(!_isFailed) {
						String msg = SGDataHelper.FormatString("任务{0}执行完毕,日期为:{1} 完成时间:{2}", HashId, cmonth,SGDate.Now().toString());
						if((lastTimeIsFailed||_isMissed)&&_follower!=null&&_follower.length>0) {
							String msgTitle=SGDataHelper.FormatString("任务 {0} 补偿执行通知",HashId);

							sendMsgToFollower(msgTitle,msg);
						}

						_lastBackupTime = now;
						SGDataHelper.WriteLocalTxt(cmonth,Paths.get("TaskHistory", GetHashId(),"lastBackupTimeX.txt").toString(), LocalDataType.User);
						// PFDataHelper.WriteLog(PFDataHelper.FormatString("任务{0}执行完毕,月份为:{1}", HashId,
						// cmonth));
						SGDataHelper.WriteLog(msg);
						//PFDirectory.DeleteFile(GetFailedTimeXHistoryFilePath().toString());
						
						//PFDataHelper.DeleteLocalTxtFile(GetFailedTimeXHistoryFilePath().toString(), LocalDataType.User);
						//this._isFailed=false;	
						if(_isMissed){this._isMissed=false;}
					}else {
						String errMsg=SGDataHelper.FormatString("任务{0}执行失败,日期为:{1}", HashId, cmonth);
						//PFDataHelper.WriteError(new Throwable(), new Exception(errMsg));
						SGDataHelper.WriteError( new Exception(errMsg));
						//this._isFailed=true;	
						if(_follower!=null&&_follower.length>0) {
							String msgTitle=SGDataHelper.FormatString("任务 {0} 执行失败通知",HashId);
							sendMsgToFollower(msgTitle,errMsg);
						}
					}
				} catch (Exception e) {
					 SGDataHelper.WriteError(e);
					//PFDataHelper.WriteError(new Throwable(), e);
					//PFDataHelper.WriteLocalTxt(_lastBackupTimeX,GetFailedTimeXHistoryFilePath().toString(), LocalDataType.User);
					this._isFailed=true;
					
				}
				SGDataHelper.GCCollect(true);
				// GC.Collect();//一定要有句，否则SendMobileMessage里面的所有List会使内存越来越高

			} catch (Exception e) {
				 SGDataHelper.WriteError(e);
				//PFDataHelper.WriteError(new Throwable(), e);
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

	@Override
	public String GetStatusDescription() {
        if (_running.get())
        {
        	String intervalText="";
        	if(this._intervalMinute>0) {
//        		intervalText= String.valueOf(this._intervalMinute)+"分钟";
        		intervalText=SGDataHelper.FormatString("每隔{0}分钟执行1次",_intervalMinute);
        	}else if(TimePoint.every==BackupDay) {
        		intervalText=SGDataHelper.FormatString("定时设置在每天的{0}时{1}分",this.BackupHour,this.BackupMinute);
        	}else {
        		//intervalText=PFDataHelper.FormatString("定时设置在每月的{1}日{2}时{3}分",this.BackupDay,this.BackupHour,this.BackupMinute);
        		intervalText=SGDataHelper.FormatString("定时设置在每月的{0}日{1}时{2}分",this.BackupDay,this.BackupHour,this.BackupMinute);
        	}
            return SGDataHelper.FormatString("任务{0}运行中,{1}{2},下次执行的时间为{3}", HashId, 
            		intervalText,
            		this.CanDoAction==null?"":",满足时运行一次",
            		_nextRunTime.toString());
        }
        else
        {
            return SGDataHelper.FormatString("任务{0}已停止", HashId);
        }
	}
}
