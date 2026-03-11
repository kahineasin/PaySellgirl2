package com.sellgirl.sgJavaHelper.task;

import com.sellgirl.sgJavaHelper.PFFunc3;
import com.sellgirl.sgJavaHelper.SGDate;

public class PFMonthCheckTaskT2<TUserData> 
//extends PFTimeTaskBase<PFMonthCheckTaskT<TUserData>>
extends PFMonthTaskBase2<PFMonthCheckTaskT2<TUserData>>
	// implements IPFTimeTask
	    {
	        //public String HashId =null;
	        //public PFAction DoPFAction =null;
//	        /// <summary>
//	        /// 参数执行的月份
//	        /// </summary>
//	        public PFAction<String, PFMonthCheckTaskT<TUserData>,Object> DoPFAction =null;
//	        /// <summary>
//	        /// 可以执行的条件
//	        /// </summary>
//	        public PFFunc<String, PFMonthCheckTaskT<TUserData>,Object, Boolean> CanDoPFAction =null;
//	        public Thread TaskThread =null;
//	        public Boolean _running = false;
	        
//	        private Long _checkMessageInterval=null;
//	        /// <summary>
//	        /// 检测的过程可能非常耗时,所以允许自定义
//	        /// </summary>
////	        public int CheckMessageInterval { get { return PFDataHelper.ObjectToInt(ConfigurationManager.AppSettings["CheckMessageInterval_" + HashId]) ?? PFTaskHelper.CheckMessageInterval; } }
//	        public long GetCheckMessageInterval() {
//	        	return _checkMessageInterval==null?PFDataHelper.CheckMessageInterval():_checkMessageInterval.longValue();
//	        }
//	        public void SetCheckMessageInterval(long checkMessageInterval) {
//	        	_checkMessageInterval=checkMessageInterval;
//	        }
//	        protected String GetCmonth()
//	        {
//	        	return PFDataHelper.DateToCMonth(PFDate.Now());
////	                var now = DateTime.Now;
////	                var year = now.Year;
////	                var month = now.Month;
////	                var cmonth = year + "." + month.ToString("00");
////	                return cmonth;
//	        }
////	        /// <summary>
////	        /// 上次执行的年月
////	        /// </summary>
////	        public String _lastBackupCmonth =null;
//	        /// <summary>
//	        /// 备份的日期
//	        /// </summary>
//	        public int BackupDay =0;
//	        /// <summary>
//	        /// 备份的小时
//	        /// </summary>
//	        public int BackupHour =0;
//	        /// <summary>
//	        /// 备份的分钟(便于测试)
//	        /// </summary>
//	        public int BackupMinute =0;

//	        /// <summary>
//	        /// 第一次执行要在此时间之后(默认为类初始化的时间)
//	        /// 注:这是为了实现18点设置1点执行的话,是立即执行还是第二天开始执行
//	        /// </summary>
//	        public PFDate FirstRunTime =null;
//
//	        /// <summary>
//	        /// 下次执行的时间(准备用这个属性代替FirstRunTime)
//	        /// </summary>
//	        public PFDate _nextRunTime =null;

	        /// <summary>
	        /// 便于在candoPFAction里传参数到doPFAction--benjamin20200115
	        /// </summary>
	        public TUserData UserData =null;

	        
//	        /**
//	         * 完成度 0~100 (便于显示任务进度)
//	         */
//	        public int FinishedPercent=0;
//	        private String finishedMsg="";
	        
//	        [Obsolete]
//	        public PFMonthCheckTask(String hashId, PFAction<String, PFMonthCheckTask<TUserData>> doPFAction, Func<String, PFMonthCheckTask<TUserData>, Boolean> canDoPFAction)
//	        {
//	            HashId = hashId;
//	            DoPFAction = doPFAction;
//	            CanDoPFAction = canDoPFAction;
//	        }
//	        public PFMonthCheckTaskT(String hashId, 
//	        		//PFAction<String, PFMonthCheckTaskT<TUserData>,Object> doPFAction, 
//	        		PFFunc<String,PFDate, PFMonthCheckTaskT<TUserData>,Boolean> doAction,
//	        		//PFFunc<String, PFMonthCheckTaskT<TUserData>,Object, Boolean> canDoPFAction,
//	        		PFFunc<String, PFDate,PFMonthCheckTaskT<TUserData>, Boolean> canDoPFAction,  
//	        		int backupDay, int backupHour, int backupMinute,boolean jumpThePassedTask)
//	        {
//	        	//super(hashId,doAction);
//	        	
//	            //HashId = hashId;
//	            //DoPFAction = doPFAction;
//	            CanDoAction = canDoPFAction;
//	            BackupDay = backupDay;
//	            BackupHour = backupHour;
//	            BackupMinute = backupMinute;
//	            init(hashId,doAction,jumpThePassedTask);//这里要用到上面BackupHour等参数,所以一定要写在后面
//
////	            //这里按一般的习惯,如果当前10日,设置为2日,会等到下个月的2日才执行
////	            PFDate now = PFDate.Now();
////	            PFDate firstRunTime = new PFDate(now.GetYear(), now.GetMonth(), BackupDay, BackupHour, BackupMinute, 0);
////	            FirstRunTime = now.compareTo(firstRunTime) > 0? firstRunTime.AddMonths(1) : firstRunTime;
////	            _nextRunTime = FirstRunTime;
//	        }
	        /**
	         * 
	         * @param hashId
	         * @param doAction 参数1：执行的日期,参数2:上次执行的时间
	         * @param backupHour
	         * @param backupMinute
	         * @param jumpThePassedTask
	         */
	        public PFMonthCheckTaskT2(String hashId, 
	        		//PFAction<String, PFMonthCheckTaskT<TUserData>,Object> doPFAction, 
	        		PFFunc3<SGDate,SGDate, PFMonthCheckTaskT2<TUserData>,Boolean> doAction,
	        		//PFFunc<String, PFMonthCheckTaskT<TUserData>,Object, Boolean> canDoPFAction,
	        		PFFunc3<SGDate, SGDate,PFMonthCheckTaskT2<TUserData>, Boolean> canDoPFAction,  
	        		int backupDay, int backupHour, int backupMinute,boolean jumpThePassedTask,TaskFollower[] follower)
	        {
	        	super(hashId,  doAction,backupDay,  backupHour,  backupMinute, jumpThePassedTask, follower);
	       	 this.CanDoAction = canDoPFAction;
	        	//super(hashId,doAction);
	        	
	            //HashId = hashId;
	            //DoAction = doAction;
//	            BackupHour = backupHour;//super()时要用到这个参数,所有写在这里会有问题的
//	            BackupMinute = backupMinute;
//	            init(hashId,doAction,jumpThePassedTask);//这里要用到上面BackupHour等参数,所以一定要写在后面

	        }
//	        public void Start()
//	        {
//	            if (!_running)
//	            {
//	                _running = true;
//	                
//	                TaskThread =new Thread() {//线程操作
//	    	               public void run() {
//	    	            	   StartThread();
//	    	               }
//	    	        };	        
//	                TaskThread.start();
//	                
//	            }
////	            
////	            if (!_running)
////	            {
////	                _running = true;
////
////	                TaskThread = new Thread(new ParameterizedThreadStart(StartThread));
////	                TaskThread.Start();
////	            }
//	        }
//	        public void Stop()
//	        {
//	            if (_running)
//	            {
//	                _running = false;
//	                TaskThread.interrupt();//.destroy();//之前先释放MessageService的话,进程里仍在使用MessageService会报错,现在改为先释放Thread试试
//
//	            }
////	            if (_running)
////	            {
////	                _running = false;
////	                TaskThread.Abort();//之前先释放MessageService的话,进程里仍在使用MessageService会报错,现在改为先释放Thread试试
////	                //SaveThread.Abort();
////	                //MessageService.Dispose();
////	                //MessageService = null;
////	            }
//	        }
//	        public void StartThread()
//	        {
//	            _lastBackupCmonth = null;
//	            while (_running == true)
//	            {
//	                try
//	                {
//	                    String cmonth = GetCmonth();
//	                    if (cmonth.equals(_lastBackupCmonth))//该月已执行
//	                    {
//	                        //Thread.Sleep(ProjDataHelper.CheckMessageInterval);
//	                        Thread.sleep(GetCheckMessageInterval());
//	                        continue;
//	                    }
//
//	                    PFDate now = PFDate.Now();
//
//	                    PFDate backupDay = new PFDate(now.GetYear(), now.GetMonth(), BackupDay, BackupHour, BackupMinute, 0);
//	                    //if (backupDay > now || FirstRunTime > now)//未到执行的日期
//	                    if (backupDay.compareTo(now)>0 || FirstRunTime.compareTo(now)>0)
//	                    {
//	                        //Thread.Sleep(PFTaskHelper.CheckMessageInterval);
//	                        Thread.sleep(PFDataHelper.CheckMessageInterval());
//	                        continue;
//	                    }
//
//	                    if (!CanDoPFAction.go(cmonth, this,null))
//	                    {
//	                        Thread.sleep(GetCheckMessageInterval());
//	                        continue;
//	                    }
//
//	                    _lastBackupCmonth = cmonth;
//	                    _nextRunTime = backupDay.AddMonths(1);
//
//	                    PFDataHelper.WriteLog(PFDataHelper.FormatString("任务{0}开始执行,月份为:{1}", HashId, cmonth));//调用这个方法来写这个值并不好,因为每天都单独一个txt的
//	                    try
//	                    {
////	                        DoPFAction(cmonth, this);
//	                        DoPFAction.go(cmonth,  this,null);
//	                        PFDataHelper.WriteLog(PFDataHelper.FormatString("任务{0}执行完毕,月份为:{1}", HashId, cmonth));
//	                    }
//	                    catch (Exception e)
//	                    {
//	                        PFDataHelper.WriteError(new Throwable(),e);
//	                    }
//	                    PFDataHelper.GCCollect(true);
//	                    //GC.Collect();//一定要有句，否则SendMobileMessage里面的所有List会使内存越来越高
//
//	                }
//	                catch (Exception e)
//	                {
//	                    //PFDataHelper.WriteError(e);
//                        PFDataHelper.WriteError(new Throwable(),e);
//	                }
//
//	            }
//	        }

//	        public void ResetBackupTime(Calendar calendar)
//	        {
//	        	super.ResetBackupTime(calendar);
//	        	PFDate time=new PFDate(calendar);
//	            BackupDay = time.GetDay();
//	            BackupHour = time.GetHour();
//	            BackupMinute = time.GetMinute();
//
//
////	            FirstRunTime = time;
////	            _nextRunTime = FirstRunTime;
////	            _lastBackupCmonth = null;
//	        }
//
//	        public String GetStatusDescription()
//	        {
//	            if (_running)
//	            {
//	                return PFDataHelper.FormatString("任务{0}运行中,定时设置在每月的{1}日{2}时{3}分,每月条件满足时运行一次,下次执行的时间为{4}", HashId, BackupDay, BackupHour, BackupMinute, _nextRunTime.toString());
//	            }
//	            else
//	            {
//	                return PFDataHelper.FormatString("任务{0}已停止", HashId);
//	            }
//	        }

//	        public String GetHashId()
//	        {
//	            return HashId;
//	        }
//			@Override
//		    public Boolean IsRunning() {
//		    	return _running;
//		    }
//			@Override
//			public int GetFinishedPercent() {
//				// TODO Auto-generated method stub
//				return FinishedPercent;
//			}
//			@Override
//			public void SetFinishedPercent(int percent) {
//				FinishedPercent=percent;
//			}
//			@Override
//		    public String getFinishedMsg() {
//				return finishedMsg;
//			}
//			@Override
//			public void setFinishedMsg(String finishedMsg) {
//				this.finishedMsg = finishedMsg;
//			}
//			@Override
//			protected String GetTimeX() {
//				return GetCmonth();
//			}
//			@Override
//			protected PFDate GetBackupDay(PFDate now) {
//				return new PFDate(now.GetYear(), now.GetMonth(), BackupDay, BackupHour, BackupMinute, 0);
//			}
//			@Override
//			protected PFDate GetNextTime(PFDate backupDay) {
//				return backupDay.AddMonths(1);
//			}
}
