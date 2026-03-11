package com.sellgirl.sgJavaHelper.exception;

//public class PFUserException  extends RuntimeException{
	public class PFUserException  extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3667826474487194660L;

	public PFUserException(String message) {
		super(message);
	}
	public PFUserException(Throwable message) {
		super(message);
	}

}
