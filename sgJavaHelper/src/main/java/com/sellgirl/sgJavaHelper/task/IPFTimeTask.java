package com.sellgirl.sgJavaHelper.task;

import java.util.Calendar;

public interface IPFTimeTask extends IPFTask {

	/**
	 * 设置成下次运行的时间(和ResetBackupTime的区别是：ResetBackupTime设置的时间已经到了，会立即运行，SetNextBackupTime设置的时间如果已经到了，会推下次时间)
	 * @param calendar
	 */
	void SetNextBackupTime(Calendar calendar);

	/**
	 * 重设备份的时间点
	 * @param time
	 */
    void ResetBackupTime(Calendar time);
    
    /**
     * 重做当 天|月 执行失败的任务
     */
    void RedoFailed();

    /// <summary>
    /// 获得当前任务状态的说明,如:任务xx运行中,定时设置在8时9分
    /// </summary>
    /// <returns></returns>
    String GetStatusDescription();

    String GetHashId();
    
    int GetFinishedPercent();
    void SetFinishedPercent(int percent);
    /**
     * 便于设置执行进度描述
     * @return
     */
    String getFinishedMsg();
    void setFinishedMsg(String msg);
    
    /**
     * 当 天|月 执行失败
     * @return
     */
    boolean isFailed();
    
    /**
     * 正在执行action
     * @return
     */
	public boolean isDoing() ;
}
