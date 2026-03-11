package com.sellgirl.sgJavaHelper;

import java.util.ArrayList;

public class PFDataTableFieldValidCollection extends ArrayList<PFDataTableFieldValidModel>{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void addError(String field,String msg) {

    	add(new PFDataTableFieldValidModel(
    			field,false,
    			msg)   
				);
    }
}
