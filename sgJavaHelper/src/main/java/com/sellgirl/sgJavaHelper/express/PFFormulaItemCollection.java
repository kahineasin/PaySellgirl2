package com.sellgirl.sgJavaHelper.express;

import java.util.ArrayList;

public class PFFormulaItemCollection extends ArrayList<PFFormulaItem> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PFFormulaItemCollection(PFFormulaItem... item) {
		for(PFFormulaItem i : item) {
			this.add(i);
		}
	}
}
