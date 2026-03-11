package com.sellgirl.sgJavaHelper;

import java.util.ArrayList;
import java.util.Collection;

public class SysBtnCollection extends ArrayList<SysBtn>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public SysBtnCollection() {
		
	}
    public SysBtnCollection(Collection<SysBtn> c) {
    	super(c);
//        elementData = c.toArray();
//        if ((size = elementData.length) != 0) {
//            // c.toArray might (incorrectly) not return Object[] (see 6260652)
//            if (elementData.getClass() != Object[].class)
//                elementData = Arrays.copyOf(elementData, size, Object[].class);
//        } else {
//            // replace with empty array.
//            this.elementData = EMPTY_ELEMENTDATA;
//        }
    }
    public void Add(String name, String text, 
    		FuncAuthorityClass authority //= FuncAuthority.Default
    		, 
    		Boolean alwayShow// = false
    		)
    {
    	SysBtn btn=new SysBtn();
    	btn.Name = name; btn.Text = text; btn.Authority = authority; 
    			btn.AlwayShow = alwayShow ;
        add(btn);

    }
    public SysBtnCollection FilterByAuthority(FuncAuthorityClass funcAuthority)
    {
        if (funcAuthority.HasFlag(FuncAuthorityClass.All)) { return this; }
//        if (PFDataHelper.EnumHasFlag(funcAuthority, FuncAuthorityClass.All)){ return this; }
        SysBtnCollection result = new SysBtnCollection();
        for(SysBtn i : this)
        {
            if (funcAuthority.HasFlag(i.Authority)) { result.add(i); }
            //if (PFDataHelper.EnumHasFlag(funcAuthority, i.Authority)) { result.Add(i); }
        }
        return result;
    }
}