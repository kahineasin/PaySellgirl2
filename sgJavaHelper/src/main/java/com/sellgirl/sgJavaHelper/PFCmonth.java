package com.sellgirl.sgJavaHelper;

import java.util.Calendar;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class PFCmonth {
/**
 * yyyy.MM
 */
    private String _cmonth;
    /**
     * yyyyMM
     */
	private String _ym;
	/**
	 * yyyy-MM
	 */
	private String _nym;
	private int _year=-1;
	public PFCmonth() {}
	public PFCmonth(String cmonth ) {
		this.setCmonth(cmonth);
	}
    public String getCmonth() {
		return _cmonth;
	}
	public void setCmonth(String cmonth) {
		this._cmonth = cmonth;
		if(_cmonth!=null) {
			_ym=_cmonth.replace(".", "");
			_nym=_cmonth.replace(".", "-");
			_year=Integer.parseInt(_ym.substring(0, 4));
			}
	}
	public String getYm() {
		return _ym;
	}
	public int getYear() {
		return _year;
	}
	public void setYm(String ym) {
		this._ym = ym;		
		if (_ym != null && _ym.length() > 5) { 
			_cmonth = SGDataHelper.StringInsert(_ym, 4, "."); 
			_nym = SGDataHelper.StringInsert(_ym, 4, "-"); 
			}
	}
	public String getNYM() {
		return _nym;
	}
    public PFCmonth AddMonths(int month)
    {
    	PFCmonth r=new PFCmonth();
    	r.setCmonth(SGDataHelper.CMonthAddMonths(_cmonth, month));
    	return r;
    }
    public Calendar ToDateTime()
    {
        return SGDataHelper.CMonthToDate(_cmonth);
    }
    public SGDate ToSGDate()
    {
        return new SGDate( SGDataHelper.CMonthToDate(_cmonth));
    }
}
