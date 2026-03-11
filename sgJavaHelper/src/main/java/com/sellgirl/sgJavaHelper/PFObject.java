package com.sellgirl.sgJavaHelper;

import java.util.HashMap;

public class PFObject extends HashMap<String,Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4045206336873462831L;

	public PFObject Add(String key,Object value ) {
		super.put(key,value);
		return this;
	}
}
