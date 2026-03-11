package com.sellgirl.sgJavaHelper.exception;

/**
 * 系統记录过的错误
 * @author li
 *
 */
//public class PFSysProcessedException  extends RuntimeException{
	public class PFSysProcessedException  extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3667826474487194660L;

	public PFSysProcessedException(String message) {
		super(message);
	}
	public PFSysProcessedException(Throwable message) {
		super(message);
	}

}
