package com.sellgirl.sgJavaHelper;

import java.util.ArrayList;

public class SGDataTableFieldValidCollection extends ArrayList<SGDataTableFieldValidModel>{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void addError(String field,String msg) {

    	add(new SGDataTableFieldValidModel(
    			field,false,
    			msg)   
				);
    }
}
