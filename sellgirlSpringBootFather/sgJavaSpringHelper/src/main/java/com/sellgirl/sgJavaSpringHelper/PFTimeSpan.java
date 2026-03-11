package com.sellgirl.sgJavaSpringHelper;

import com.sellgirl.sgJavaHelper.SGYmd;
import com.sellgirl.sgJavaSpringHelper.config.SGDataHelper;

public class PFTimeSpan {

    private int _ymd;
    public int Hour;
    public int Minute;
    public int Second;
    public int Millisecond;
    public PFTimeSpan(Integer ymd)
    {
    	if(ymd==null) {
    		ymd =SGYmd.Hour | SGYmd.Minute | SGYmd.Second;
    	}
        _ymd = ymd;
    }
    @Override
    public  String toString()
    {
        String s = "";
        if (SGDataHelper.EnumHasFlag(_ymd, SGYmd.Hour))
        {
            s += Hour + "时";
        }
        if (SGDataHelper.EnumHasFlag(_ymd, SGYmd.Minute))
        {
            s += Minute + "分";
        }
        if (SGDataHelper.EnumHasFlag(_ymd, SGYmd.Second))
        {
            s += Second + "秒";
        }
        if (SGDataHelper.EnumHasFlag(_ymd, SGYmd.Millisecond))
        {
            s += Millisecond + "毫秒";
        }
        return s;
        //return string.Format("{0}时{1}分{2}秒{3}毫秒", Hour,Minute,Second,Millisecond);
    }
}
