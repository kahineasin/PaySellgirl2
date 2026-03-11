package com.sellgirl.sgJavaHelper.task;

import java.util.Calendar;

import com.sellgirl.sgJavaHelper.PFFunc3;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
//import com.sellgirl.pfHelperNotSpring.model.PFEnvir;
//import redis.clients.jedis.Jedis;
@Deprecated
public class PFDayTaskBase<TTask>
extends PFTimeTaskBase<TTask>
//implements IPFTimeTask
{
//    public String HashId=null;
//    /// <summary>
//    /// 参数执行的月份
//    /// </summary>
//    //public PFAction<String, PFDate, PFDayTask> DoAction;
//    public Thread TaskThread=null;
//    public Boolean _running = false;

    protected String GetCDay()
    {
    	return SGDataHelper.ObjectToDateString(Calendar.getInstance());
    }
    /// <summary>
    /// a上次执行的年月日(有成员是为了重设时间是清空)(好像没用,待删除)--benjamin todo
    /// </summary>
    @Deprecated
    public String _lastBackupCDay=null;
//    /// <summary>
//    /// 上次备份的时间,便于增量更新
//    /// </summary>
//    public PFDate _lastBackupTime =null;

    /**
     * 备份的小时,后来想想此属性其实没必要,完全可以通过基类的FirstRunTime来计算执行时间.待删除--benjamin todo
     */
    @Deprecated
    public int BackupHour =0;
    /// <summary>
    /// 备份的分钟(便于测试)
    /// </summary>
    public int BackupMinute =0;

//    /// <summary>
//    /// 第一次执行要在此时间之后(默认为类初始化的时间)
//    /// 注:这是为了实现18点设置1点执行的话,是立即执行还是第二天开始执行
//    /// </summary>
//    public PFDate FirstRunTime =null;
//
//    /// <summary>
//    /// 下次执行的时间(准备用这个属性代替FirstRunTime)
//    /// </summary>
//    public PFDate _nextRunTime =null;
//    
//    /**
//     * 完成度 0~100 (便于显示任务进度)
//     */
//    public int FinishedPercent=0;
//    private String finishedMsg="";

    /**
     * 
     * @param hashId
     * @param doAction 参数1：执行的日期,参数2:上次执行的时间
     * @param backupHour
     * @param backupMinute
     * @param jumpThePassedTask
     */
    public PFDayTaskBase(String hashId, PFFunc3<String, SGDate, TTask,Boolean> doAction, int backupHour, int backupMinute,boolean jumpThePassedTask,TaskFollower[] follower)
    {
    	//super(hashId,doAction);
    	
        //HashId = hashId;
        //DoAction = doAction;
        BackupHour = backupHour;//super()时要用到这个参数,所有写在这里会有问题的
        BackupMinute = backupMinute;
        init(hashId,doAction,jumpThePassedTask, follower);//这里要用到上面BackupHour等参数,所以一定要写在后面


    }

	@Override
	protected String GetTimeX() {
		return GetCDay();
	}

	@Override
	protected SGDate GetBackupDay(SGDate now) {
		return  new SGDate(now.GetYear(), now.GetMonth(), now.GetDay(), BackupHour, BackupMinute, 0);
	}
//    public void StartThread()
//    {
//        _lastBackupCDay = null;
//        _lastBackupTime = null;
//        while (_running == true)
//        {
//            try
//            {
//                String cDay = GetCDay();
//                if (cDay.equals(_lastBackupCDay))//该日已执行
//                {
//                    Thread.sleep(PFDataHelper.CheckMessageInterval());
//                    continue;
//                }
//                PFDate now = PFDate.Now();
//
//                PFDate backupDay = new PFDate(now.GetYear(), now.GetMonth(), now.GetDay(), BackupHour, BackupMinute, 0);
//                if (backupDay.compareTo(now)>0 || FirstRunTime.compareTo(now)>0)//未到执行的时间
//                {
//                    Thread.sleep(PFDataHelper.CheckMessageInterval());
//                    continue;
//                }
//
//                _lastBackupCDay = cDay;
//                _nextRunTime = backupDay.AddDays(1);
//
//                PFDataHelper.WriteLog(PFDataHelper.FormatString("任务{0}开始执行,日期为:{1}", HashId, cDay));
//                try
//                {
//                	DoActionCheckRedis(cDay);
//                    _lastBackupTime = now;
//                    PFDataHelper.WriteLog(PFDataHelper.FormatString("任务{0}执行完毕,日期为:{1}", HashId, cDay));
//                }
//                catch (Exception e)
//                {
//                    //PFDataHelper.WriteError(e);
//                    PFDataHelper.WriteError(new Throwable(),e);
//                }
//                PFDataHelper.GCCollect(true);//一定要有句，否则SendMobileMessage里面的所有List会使内存越来越高
//
//            }
//            catch (Exception e)
//            {
//                //PFDataHelper.WriteError(e);
//                PFDataHelper.WriteError(new Throwable(),e);
//            }
//        }
//    }

	@Override
    public void ResetBackupTime(Calendar calendar)//如果传入08:30 会自动转为当天
    {
		super.ResetBackupTime(calendar);
    	SGDate time=new SGDate(calendar);
        BackupHour = time.GetHour();
        BackupMinute = time.GetMinute();

//        FirstRunTime = time;
//        _nextRunTime = FirstRunTime;
//        _lastBackupCDay = null;
//        _lastBackupTime = null;
    }

    public String GetStatusDescription()
    {
        if (_running.get())
        {
            return SGDataHelper.FormatString("任务{0}运行中,定时设置在每天的{1}时{2}分,下次执行的时间为{3}", HashId, BackupHour, BackupMinute, _nextRunTime.toString());
        }
        else
        {
            return SGDataHelper.FormatString("任务{0}已停止", HashId);
        }
    }
	@Override
	protected SGDate GetNextTime(SGDate backupDay) {
		return backupDay.AddDays(1);
	}

//    public String GetHashId()
//    {
//        return HashId;
//    }
//	@Override
//    public Boolean IsRunning() {
//    	return _running;
//    }
//	@Override
//	public int GetFinishedPercent() {
//		return FinishedPercent;
//	}
//	@Override
//	public void SetFinishedPercent(int percent) {
//		FinishedPercent=percent;
//	}
//	@Override
//    public String getFinishedMsg() {
//		return finishedMsg;
//	}
//	@Override
//	public void setFinishedMsg(String finishedMsg) {
//		this.finishedMsg = finishedMsg;
//	}

}
