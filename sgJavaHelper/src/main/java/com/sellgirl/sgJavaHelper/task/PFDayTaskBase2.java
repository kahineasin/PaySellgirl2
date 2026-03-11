package com.sellgirl.sgJavaHelper.task;

import com.sellgirl.sgJavaHelper.PFFunc3;
import com.sellgirl.sgJavaHelper.SGDate;


public class PFDayTaskBase2<TTask>
extends PFTimeTaskBase2<TTask>
//implements IPFTimeTask
{


    /**
     * 
     * @param hashId
     * @param doAction 参数1：执行的日期,参数2:上次执行的时间
     * @param backupHour
     * @param backupMinute
     * @param jumpThePassedTask
     */
    public PFDayTaskBase2(String hashId, PFFunc3<SGDate, SGDate, TTask,Boolean> doAction, int backupHour, int backupMinute,boolean jumpThePassedTask,TaskFollower[] follower)
    {

        init(hashId,doAction,
        		TimePoint.every, backupHour,  backupMinute,-1,
        		jumpThePassedTask, follower);
    }



}
