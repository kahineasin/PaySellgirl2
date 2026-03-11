package com.sellgirl.sgJavaHelper.task;

import com.sellgirl.sgJavaHelper.PFFunc3;
import com.sellgirl.sgJavaHelper.SGDate;
//import com.sellgirl.pfHelperNotSpring.config.PFDataHelper;
//import com.sellgirl.pfHelperNotSpring.model.PFEnvir;
//import redis.clients.jedis.Jedis;
public class PFDayTask2
//extends PFTimeTaskBase<PFDayTask>
extends PFDayTaskBase2<PFDayTask2>
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
    public PFDayTask2(String hashId, PFFunc3<SGDate, SGDate, PFDayTask2,Boolean> doAction, int backupHour, int backupMinute,boolean jumpThePassedTask,TaskFollower[] follower)
    {
    	super(hashId,  doAction,  backupHour,  backupMinute, jumpThePassedTask, follower);
    }


}
