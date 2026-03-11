package com.sellgirl.sgJavaHelper.model;

import com.sellgirl.sgJavaHelper.PFEnumClass;

public final class UserTypeClass extends PFEnumClass {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2028949911738150197L;
	protected UserTypeClass() {
		super();
	}
	protected UserTypeClass(String text, int value) {
		super(text, value);
	}
	public final static UserTypeClass Default=new UserTypeClass("Default",0);
	public final static UserTypeClass Fgs=new UserTypeClass("Fgs",1);
}
