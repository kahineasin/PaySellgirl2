package com.sellgirl.sgJavaHelper.time;

import com.sellgirl.sgJavaHelper.SGYmd;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class SGTimeSpan {

    private int _ymd;
    public int Hour;
    public int Minute;
    public int Second;
    public int Millisecond;
    public SGTimeSpan(Integer ymd)
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
    /**
     * 便于测试项目中文乱码,使用英文
     * @return
     */
    public  String toEnString()
    {
        String s = "";
        if (SGDataHelper.EnumHasFlag(_ymd, SGYmd.Hour))
        {
            s += Hour + "h";
        }
        if (SGDataHelper.EnumHasFlag(_ymd, SGYmd.Minute))
        {
            s += Minute + "m";
        }
        if (SGDataHelper.EnumHasFlag(_ymd, SGYmd.Second))
        {
            s += Second + "s";
        }
        if (SGDataHelper.EnumHasFlag(_ymd, SGYmd.Millisecond))
        {
            s += Millisecond + "ms";
        }
        return s;
        //return string.Format("{0}时{1}分{2}秒{3}毫秒", Hour,Minute,Second,Millisecond);
    }
//    public void init() {
//    	Hour=0;
//    	Minute=0;
//    	Second=0;
//    	Millisecond=0;
//    }

	public  SGTimeSpan initByTime(long ElapsedMilliseconds//, Integer ymd
			)// = PFYmd.Hour | PFYmd.Minute |
																				// PFYmd.Second)
	{
//		if (ymd == null) {
//			ymd = SGYmd.Hour | SGYmd.Minute | SGYmd.Second;
//		}
		// int xx=PFYmd.Hour|PFYmd.Millisecond;

		// long after = ElapsedMilliseconds / 1000;// 这里得到是秒
		long after = ElapsedMilliseconds;// 这里得到是毫秒
//		SGTimeSpan result = new SGTimeSpan(ymd);

//		this._ymd=ymd;
    	Hour=0;
    	Minute=0;
    	Second=0;
    	Millisecond=0;
		if (SGDataHelper.EnumHasFlag(_ymd, SGYmd.Hour)) {
			Hour = (int) (after / (1000 * 60 * 60));// 计算整数小时数
			after -= (Hour * 1000 * 60 * 60);// 取得算出小时数后剩余的秒数
		}
		;
		if (SGDataHelper.EnumHasFlag(_ymd, SGYmd.Minute)) {
			// result.Minute = (int) (after / 60);// 计算整数小时数
			Minute = (int) (after / (1000 * 60));// 计算整数小时数
			after -= (Minute * 1000 * 60);// 取得算出小时数后剩余的秒数
		}
		;
		if (SGDataHelper.EnumHasFlag(_ymd, SGYmd.Second)) {
			// result.Second = (int) (after);// 计算整数秒数
			Second = (int) (after / 1000);// 计算整数秒数
			after -= Second * 1000;// 取得算出秒数后剩余的毫秒数
		}
		;
		if (SGDataHelper.EnumHasFlag(_ymd, SGYmd.Millisecond)) {
			Millisecond = (int) (after);
		}
		;
		return this;
	}
}
