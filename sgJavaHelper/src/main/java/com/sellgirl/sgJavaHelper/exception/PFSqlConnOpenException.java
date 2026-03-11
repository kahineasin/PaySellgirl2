package com.sellgirl.sgJavaHelper.exception;

/**
 * 打开数据库连接时的异常
 * 
 * @author Administrator
 *
 */
public class PFSqlConnOpenException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3667826474487194660L;

	public PFSqlConnOpenException(String message) {
		super(message);
	}

	public PFSqlConnOpenException(Throwable message) {
		super(message);
	}

}
