package com.sellgirl.sgJavaHelper.task;

import com.sellgirl.sgJavaHelper.PFFunc3;
import com.sellgirl.sgJavaHelper.SGDate;
//import com.sellgirl.pfHelperNotSpring.model.PFEnvir;
//import redis.clients.jedis.Jedis;
/**
 * 精确时间间隔，如每个小时的10分执行1次(之类)
 * PFIntervalTask不是精确的时间点，而是每次往后推多少时间
 * @author Administrator
 *
 */
public class PFIntervalExactTask2
//implements IPFTask
//extends PFTaskBase
extends PFTimeTaskBase2<PFIntervalExactTask2>
implements IPFTimeTask
{
	
	public PFIntervalExactTask2( String hashId,
			//PFFunc<String, PFDate, PFIntervalTask,Boolean> doAction,
			PFFunc3<SGDate, SGDate, PFIntervalExactTask2,Boolean> doAction ,
//			PFDate startDate,
			int backupDay,int backupHour, int backupMinute,
			int intervalMinute,
			boolean jumpThePassedTask,TaskFollower[] follower) {
		

        init(hashId,doAction,
        		backupDay, backupHour,  backupMinute,intervalMinute,
        		jumpThePassedTask, follower);//这里要用到上面BackupHour等参数,所以一定要写在后面
		
//		HashId = hashId;
//		DoAction = doAction;
//		
//		_intervalMinute=intervalMinute;
//
//		//_lastBackupTime=startDate.AddMinutes(-intervalMinute);
//		
//
//		PFDate now = PFDate.Now();
//		// PFDate firstRunTime = new PFDate(now.GetYear(), now.GetMonth(), now.GetDay(),
//		// BackupHour, BackupMinute, 0);
//		PFDate firstRunTime = startDate.TClone();
//		// FirstRunTime = now.compareTo(firstRunTime)>0 ? firstRunTime.AddDays(1) :
//		// firstRunTime;
//
//		if(jumpThePassedTask) {
//			while( now.compareTo(firstRunTime) > 0 ) {
//				firstRunTime=GetNextTime(firstRunTime);
//			}
//			FirstRunTime=firstRunTime;
//			_nextRunTime = FirstRunTime;	
//			_lastBackupTime=firstRunTime.AddMinutes(-intervalMinute);
//			//FirstRunTime = now.compareTo(firstRunTime) > 0 ? GetNextTime(firstRunTime) : firstRunTime;
//		}else {//这部分的逻辑是新增的,为了使服务器停电时执行超过时间点的任务,这部分要求把最后一次的执行时间保存下来(防止把执行过的任务又执行一次)
////			//String backupTimeX=GetTimeX();
////			while( now.compareTo(firstRunTime) > 0 ) {
////				firstRunTime=GetNextTime(firstRunTime);
////			}
//			while( now.compareTo(firstRunTime) > 0 ) {
//				firstRunTime=GetNextTime(firstRunTime);
//			}
//			PFDate lastRunTime=firstRunTime.AddMinutes(-intervalMinute);//补尝上1次
//			String lastBackupTimeX=lastRunTime.toString();
//			String logLastBackupTimeX=PFDataHelper.ReadLocalTxt(Paths.get("TaskHistory", GetHashId(),"lastBackupTimeX.txt").toString(), LocalDataType.User);
//			if(logLastBackupTimeX!=null&&logLastBackupTimeX.equals(lastBackupTimeX)) {//当 日|月 的任务已经执行过
//				//FirstRunTime = now.compareTo(firstRunTime) > 0 ? GetNextTime(firstRunTime) : firstRunTime;
//			}else {
//				if(now.compareTo(lastRunTime) > 0) {
//					_isMissed=true;
//					firstRunTime=lastRunTime;
//					FirstRunTime=firstRunTime;
//					_nextRunTime = FirstRunTime;	
//					_lastBackupTime=firstRunTime.AddMinutes(-intervalMinute);
////					FirstRunTime = lastRunTime;
//				}
//			}
//		}
//
//		if (PFDataHelper.GetConfigMapper().GetRedisConfig() != null) {
//			lockWithRedis=true;
//			try {
////				PFDataHelper.ReTry((a,b,c)->{
//					Jedis jedis = PFDataHelper.GetConfigMapper().GetRedisConfig().open();
//					jedis.del(HashId);// 以防任务中断时刚好没有清空redis
//					PFRedisConfig.close(jedis);
////				});
//			} catch (Exception e) {
//				PFDataHelper.WriteError(new Throwable(), e);
//				;
//			}
//		}
		
	}	
//
////	@Override
//	protected PFDate GetNextTime(PFDate backupDay) {
//		return backupDay.AddMinutes(_intervalMinute);
//	}
//	@Override
//	protected void StartThread() {
//		while (_running == true) {
//			try {
//				PFDate now = PFDate.Now();
//				PFDate backupDay = GetNextTime(_lastBackupTime);
//				
//				if (backupDay.compareTo(now) > 0 ) {
//					// Thread.Sleep(PFTaskHelper.CheckMessageInterval);
//					Thread.sleep(60*1000);//1分钟
//					continue;
//				}
//
////				_lastBackupTime = now;
////				
////				DoAction.apply(this);
////				Thread.sleep(60*1000);//1分钟
////				PFDataHelper.GCCollect(true); //一定要有句，否则SendMobileMessage里面的所有List会使内存越来越高
//				
//
//				_isDoing.set(true);
//				String cmonth=backupDay.toString();
//				// _lastBackupCmonth = cmonth;
//				_lastBackupTimeX = cmonth;//现在这样的话,redo会有bug,当此句执行后,等待action的时候,如果又执行了RedoFail,使
//				// _nextRunTime = backupDay.AddMonths(1);
//				_nextRunTime = GetNextTime(backupDay);
//				try {
////                    DoPFAction(cmonth, this);
//					// DoPFAction.go(cmonth, this,null);
//					//boolean b=DoActionCheckRedis(cmonth);
//					if(this.FinishedPercent<1) {
//						this.FinishedPercent=1;
//					}
//					boolean lastTimeIsFailed=_isFailed;
//					_isFailed=!DoActionCheckRedis(cmonth);
//					_lastBackupTimeX = cmonth;//加上这句比较保险
////					if(b) {
//					if(!_isFailed) {
//						String msg = PFDataHelper.FormatString("任务{0}执行完毕,日期为:{1}", HashId, cmonth);
//						if((lastTimeIsFailed||_isMissed)&&_follower!=null&&_follower.length>0) {
//							String msgTitle=PFDataHelper.FormatString("任务 {0} 补偿执行通知",HashId);
//
//							sendMsgToFollower(msgTitle,msg);
//						}
//
//						_lastBackupTime = now;
//						PFDataHelper.WriteLocalTxt(_lastBackupTimeX,Paths.get("TaskHistory", GetHashId(),"lastBackupTimeX.txt").toString(), LocalDataType.User);
//
//						PFDataHelper.WriteLog(msg);
//
//						this._isMissed=false;
//					}else {
//						String errMsg=PFDataHelper.FormatString("任务{0}执行失败,日期为:{1}", HashId, cmonth);
//						PFDataHelper.WriteError(new Throwable(), new Exception(errMsg));
//						//this._isFailed=true;	
//						if(_follower!=null&&_follower.length>0) {
//							String msgTitle=PFDataHelper.FormatString("任务 {0} 执行失败通知",HashId);
//							sendMsgToFollower(msgTitle,errMsg);
//						}
//					}
//				} catch (Exception e) {
//					PFDataHelper.WriteError(new Throwable(), e);
//					this._isFailed=true;
//					
//				}
//				PFDataHelper.GCCollect(true);
//
//			} catch (Exception e) {
//				PFDataHelper.WriteError(new Throwable(), e);
//			}
//
//		}
//	}
//	
////	@Override
////	protected boolean DoActionCheckRedis(String cDay) {
////		boolean b=false;
////		//if (jedis != null && PFEnvir.release == PFDataHelper.CurrentEnvironmental) {
////		if (lockWithRedis&& PFEnvir.release == PFDataHelper.CurrentEnvironmental) {
////			try {
//////				if(jedis==null) {
//////					jedis = PFDataHelper.GetConfigMapper().GetRedisConfig().open();
//////				}else if (!jedis.isConnected()) {
//////					jedis.connect();
//////				}
////				Jedis jedis = PFDataHelper.GetConfigMapper().GetRedisConfig().open();
////				if (jedis.setnx(GetHashId(), "1") == 1) {
////					//jedis.close();//如果用完不关闭可能会有问题 https://www.cnblogs.com/kissazi2/archive/2012/09/04/2669830.html
////					PFRedisConfig.close(jedis);
////					try {
////						b=DoAction.go(cDay, _lastBackupTime, PFDataHelper.ObjectAs(this));
////					} catch (Exception e) {// 为了避免报错时不删除redis锁
////						PFDataHelper.WriteError(new Throwable(),e);
////					}
////					PFDataHelper.ReTry((a1, b1, c1) -> {
////						Jedis jedis2= PFDataHelper.GetConfigMapper().GetRedisConfig().open();
////						jedis2.del(GetHashId());
//////						jedis.close();
////						PFRedisConfig.close(jedis);
////					});
////				} else {
////					PFRedisConfig.close(jedis);
////					String log = PFDataHelper.FormatString("redis中检测到有重复的{0}任务在其它环境上正在执行,所以本次执行跳过", GetHashId());
////					PFDataHelper.WriteLog(log);
////					System.out.println(log);
////				}
////			} catch (Exception e) {// 如果redis有网络异常,就直接执行好了
////				try {
////					b=DoAction.go(cDay, _lastBackupTime, PFDataHelper.ObjectAs(this));
////				} catch (Exception e1) {
////					// TODO Auto-generated catch block
////					e1.printStackTrace();
////					PFDataHelper.WriteError(new Throwable(), e1);
////					return false;
////				}
////				PFDataHelper.WriteError(new Throwable(),e);
////			}
////		} else {
////			try {
////				b=DoAction.go(cDay, _lastBackupTime, PFDataHelper.ObjectAs(this));
////			} catch (Exception e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////				PFDataHelper.WriteError(new Throwable(), e);
////				return false;
////			}
////		}
////		return b;
////	}
//
//	@Override
//	public String GetStatusDescription() {
//        if (_running)
//        {
//            return PFDataHelper.FormatString("任务{0}运行中,每隔{1}分钟执行1次,下次执行的时间为{2}", HashId, this._intervalMinute,  _nextRunTime.toString());
//        }
//        else
//        {
//            return PFDataHelper.FormatString("任务{0}已停止", HashId);
//        }
//	}
//
////	@Override
////	protected String GetTimeX() {
////		//此类不需要
////		return null;
////	}
////
////	@Override
////	protected PFDate GetBackupDay(PFDate now) {
////		//此类不需要
////		return null;
////	}

}
