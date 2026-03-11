package com.sellgirl.sgJavaHelper;

import java.util.ArrayList;

public class PFDataColumnCollection extends ArrayList<PFDataColumn> implements Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PFDataColumn add(String columnName, Class<?> type) {
		PFDataColumn column=new PFDataColumn(columnName,type);		
		super.add(column );
		return column;
	}
	public PFDataColumn get(String key) {
		for(PFDataColumn i: this) {
			if(key.equals(i.getKey())) {
				return i;
			}
		}
		return null;
	}
    public PFDataColumnCollection TClone()
    {
    	PFDataColumnCollection r=new PFDataColumnCollection();
    	for(PFDataColumn i : this) {
    		r.add(i.TClone());
    	}
        return r;

    }

	public boolean contains(String key) {
		for(PFDataColumn i: this) {
			if(key.equals(i.getKey())) {
				return true;
			}
		}
		return false;
	}
    @Override
    public Object clone()
    {
        return TClone();
    }
}
