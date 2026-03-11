package com.sellgirl.sgJavaHelper.log;

/**
 * 不想用slf4j, 用此替代
 */
public interface ISGLog {
	public void writeException(Throwable e, String filePrev);
	public void write(Object e, String filePrev);
	/**
	 * tag的必要性, android调试时很可能需要tag和异常输出在同一行,才容易过滤查看(所有app的日志可能是混一起的)
	 * @param e
	 * @param tag
	 */
	public void printException(Throwable e,String tag);
	public void print(Object e);
}
