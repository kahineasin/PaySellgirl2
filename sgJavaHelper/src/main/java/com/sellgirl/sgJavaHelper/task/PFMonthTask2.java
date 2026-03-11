package com.sellgirl.sgJavaHelper.task;

import com.sellgirl.sgJavaHelper.PFFunc3;
import com.sellgirl.sgJavaHelper.SGDate;

public class PFMonthTask2
extends PFMonthTaskBase2<PFMonthTask2>
	    {
	        /**
	         * 
	         * @param hashId
	         * @param doAction 参数1：执行的日期,参数2:上次执行的时间
	         * @param backupHour
	         * @param backupMinute
	         * @param jumpThePassedTask
	         */
	        public PFMonthTask2(String hashId, PFFunc3<SGDate, SGDate, PFMonthTask2,Boolean> doAction,int backupDay, int backupHour, int backupMinute,boolean jumpThePassedTask,TaskFollower[] follower)
	        {

	        	super(hashId,  doAction,backupDay,  backupHour,  backupMinute, jumpThePassedTask, follower);
	        }

}
