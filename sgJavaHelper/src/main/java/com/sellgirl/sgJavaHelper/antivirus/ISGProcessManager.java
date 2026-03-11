package com.sellgirl.sgJavaHelper.antivirus;

import java.util.List;

/**
 * 多平台兼容的进程接口，现支持pc
 */
public interface ISGProcessManager {
	void stopProcess(String pid);
	List<SGProcess> getProcesses();
	List<SGProcess>  getBadProcesses();
	void  tagGoodProcess(SGProcess process);
}
