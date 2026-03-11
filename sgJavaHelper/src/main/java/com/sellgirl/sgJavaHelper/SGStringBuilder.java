package com.sellgirl.sgJavaHelper;

import java.util.ArrayList;

/**
 * StringBuilder类在androidSdk21之前和jdk17之前好像有兼容性提示的问题
 */
public class SGStringBuilder  extends ArrayList<String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3811808132038159778L;

	@Override
	public String toString() {
		return String.join("", this);
	}
	public boolean append(String s) {
		return add(s);
	}
	public boolean append(char s) {
		return add(new String(new char[] {s}));
	}
}
