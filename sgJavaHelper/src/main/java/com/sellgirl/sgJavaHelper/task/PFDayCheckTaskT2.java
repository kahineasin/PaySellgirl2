package com.sellgirl.sgJavaHelper.task;

import com.sellgirl.sgJavaHelper.PFFunc3;
import com.sellgirl.sgJavaHelper.SGDate;


public class PFDayCheckTaskT2<TUserData>
extends PFDayTaskBase2<PFDayCheckTaskT2<TUserData>>
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
    public PFDayCheckTaskT2(String hashId, PFFunc3<SGDate, SGDate, PFDayCheckTaskT2<TUserData>,Boolean> doAction, 
    		PFFunc3<SGDate, SGDate,PFDayCheckTaskT2<TUserData>, Boolean> canDoPFAction,  
    		int backupHour, int backupMinute,boolean jumpThePassedTask,TaskFollower[] follower)
    {
    	super(hashId,  doAction,  backupHour,  backupMinute, jumpThePassedTask, follower);
    	 this.CanDoAction = canDoPFAction;

    }

}