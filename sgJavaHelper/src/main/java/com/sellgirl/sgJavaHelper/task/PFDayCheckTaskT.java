package com.sellgirl.sgJavaHelper.task;

import com.sellgirl.sgJavaHelper.PFFunc3;
import com.sellgirl.sgJavaHelper.SGDate;


@Deprecated
public class PFDayCheckTaskT<TUserData>
extends PFDayTaskBase<PFDayCheckTaskT<TUserData>>
//implements IPFTimeTask
{
    public TUserData UserData =null;
    /**
     * 
     * @param hashId
     * @param doAction 参数1：执行的日期,参数2:上次执行的时间
     * @param backupHour
     * @param backupMinute
     * @param jumpThePassedTask
     */
    public PFDayCheckTaskT(String hashId, PFFunc3<String, SGDate, PFDayCheckTaskT<TUserData>,Boolean> doAction, 
    		PFFunc3<String, SGDate,PFDayCheckTaskT<TUserData>, Boolean> canDoPFAction,  
    		int backupHour, int backupMinute,boolean jumpThePassedTask,TaskFollower[] follower)
    {
    	super(hashId,  doAction,  backupHour,  backupMinute, jumpThePassedTask, follower);
    	 this.CanDoAction = canDoPFAction;
    	//super(hashId,doAction);
    	
        //HashId = hashId;
        //DoAction = doAction;
//        BackupHour = backupHour;//super()时要用到这个参数,所有写在这里会有问题的
//        BackupMinute = backupMinute;
//        init(hashId,doAction,jumpThePassedTask);//这里要用到上面BackupHour等参数,所以一定要写在后面

    }

}