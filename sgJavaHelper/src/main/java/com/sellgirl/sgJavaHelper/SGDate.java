package com.sellgirl.sgJavaHelper;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

//不继承Calendar的方式
/**
 * 序列化:
 *@JsonSerialize(using = PFDateSerialiaer.class)
 * 
 * @author Administrator
 *
 */
public class SGDate
implements Comparable<SGDate> ,Cloneable
{
	private Calendar _calendar;
	private static int _yearEnum=Calendar.YEAR;
	private static int _monthEnum=Calendar.MONTH;
	private static int _dayEnum=Calendar.DAY_OF_MONTH;
	private static int _hourEnum=Calendar.HOUR_OF_DAY;
	private static int _minuteEnum=Calendar.MINUTE;
	private static int _secondEnum=Calendar.SECOND;

	public static SGDate Now() {
		return new SGDate();
	}
	
	private SGDate() {
		_calendar = Calendar.getInstance();
	}
	/**
	 * 用@RequestParam绑定到rest接口时会自动调用string的构造方法,非常好用
	 * @param s
	 */
	public SGDate(String s) {		
		_calendar = SGDataHelper.StringToDateTime(s);
	}
	public SGDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		setByCalendar(cal);
	}
	public SGDate(LocalDateTime date) {
		this(date.getYear(),date.getMonthValue(),date.getDayOfMonth(),date.getHour(),date.getMinute(),date.getSecond());
	}
	public SGDate(Calendar c) {
//		_calendar =new Calendar.Builder().build()..(calendar);
//		_calendar = calendar;
		//_calendar = Calendar.getInstance().setTime(calendar.getTime());
		this(c.get(_yearEnum),GetCalendarMonth(c),c.get(_dayEnum),c.get(_hourEnum),c.get(_minuteEnum),c.get(_secondEnum));		
	}
	public SGDate(long timestamp) {
		_calendar = Calendar.getInstance();
		_calendar.setTimeInMillis(timestamp);	
	}
	private void setByCalendar(Calendar c) {
		setByYearMonth(c.get(_yearEnum),GetCalendarMonth(c),c.get(_dayEnum),c.get(_hourEnum),c.get(_minuteEnum),c.get(_secondEnum));		
	}
	private void setByYearMonth(int year, int month, int day, int hour, int minute,int second) {
		_calendar = Calendar.getInstance();
		_calendar.set(year,  month-1,  day,  hour,  minute, second);//注意月份是从0开始的
		_calendar.set(Calendar.MILLISECOND,0);
	}
	/**
	 * 
	 * @param year
	 * @param month 从1开始
	 * @param day 从1开始
	 * @param hour 从0开始
	 * @param minute 从0开始
	 * @param second 从0开始
	 */
	public SGDate(int year, int month, int day, int hour, int minute,int second) {
		_calendar = Calendar.getInstance();
		_calendar.set(year,  month-1,  day,  hour,  minute, second);//注意月份是从0开始的
		_calendar.set(Calendar.MILLISECOND,0);
	}
	protected static int GetCalendarMonth(Calendar c) {
		return c.get(_monthEnum)+1;
	}
	public int GetYear() {
		return _calendar.get(_yearEnum);
	}
	public int GetMonth() {
		//return _calendar.get(_monthEnum)+1;
		return GetCalendarMonth(_calendar);
		}
	/**
	 * 1(周日),2(周一)~7(周六)
	 * @return
	 */
	public int GetWeek() {
		//return _calendar.get(_monthEnum)+1;
		return _calendar.get(Calendar.DAY_OF_WEEK);
		}
	public int GetDay() {
		return _calendar.get(_dayEnum);}
	public int GetHour() {
		return _calendar.get(_hourEnum);}
	public int GetMinute() {
		return _calendar.get(_minuteEnum);}
	public int GetSecond() {
		return _calendar.get(_secondEnum);}
	public long GetTotalHours() {
		return _calendar.getTimeInMillis()/(1000*60*60);
	}
	public SGDate AddDays(int value) {
//		_calendar.add(_dayEnum, value);
//		return this;
		Calendar c=new SGDate(_calendar).ToCalendar();
		c.add(_dayEnum, value);
		return new SGDate(c);
	}
	public SGDate AddMonths(int value) {
////		_calendar.add(_monthEnum, value);
////		return this.TClone().ToCalendar().add(_monthEnum, value);
		Calendar c=new SGDate(_calendar).ToCalendar();
		c.add(_monthEnum, value);
		return new SGDate(c);
//		Calendar c=cloneCalendar(_calendar);
//		c.add(_monthEnum, value);
//		return new PFDate(c);
	}
	public SGDate AddHour(int value) {
		Calendar c=new SGDate(_calendar).ToCalendar();
		c.add(_hourEnum, value);
		return new SGDate(c);
	}
	public SGDate AddMinutes(int value) {
		Calendar c=new SGDate(_calendar).ToCalendar();
		c.add(_minuteEnum, value);
		return new SGDate(c);
	}
	public SGDate AddSeconds(int value) {
		Calendar c=new SGDate(_calendar).ToCalendar();
		c.add(_secondEnum, value);
		return new SGDate(c);
	}
	public SGDate AddYears(int value) {
		Calendar c=new SGDate(_calendar).ToCalendar();
		c.add(_yearEnum, value);
		return new SGDate(c);
	}
	   public SGDate GetYearStart()
	   {
		   return new SGDate(GetYear(),1,1,0,0,0);				  
	   }
	   public SGDate GetMonthStart()
	   {
		   //return new PFDate(GetYear(),GetMonth(),1,1,1,1);		
		   return new SGDate(GetYear(),GetMonth(),1,0,0,0);				  
	   }
	   public SGDate GetMonthEnd()
	   {
           return AddMonths(1).GetMonthStart().AddSeconds(-1);		  
	   }
	   public SGDate GetWeekStart()
	   {	
		   SGDate r=new SGDate(GetYear(),GetMonth(),GetDay(),0,0,0);
			int weekBegin=2;//周的开始设置,便于变更, 1周日 2周一 3周二...
			int w=r.GetWeek();
            if(w>weekBegin) {r=r.AddDays(-(w-weekBegin));}//周二以上的调成周一
            else if(w<weekBegin) {r=r.AddDays((-(w-weekBegin)-7));}//周日调成上星期的周一
            else {
            	//r=begin;
            }
		   return r;				  
	   }

	   public  SGDate GetDayStart()
	   {
		   //return new PFDate(GetYear(),GetMonth(),GetDay(),1,1,1);	
		   return new SGDate(GetYear(),GetMonth(),GetDay(),0,0,0);			
	   }
       public SGDate GetDayEnd()
       {
           //return GetDayStart(date.AddDays(1)).AddSeconds(-1);	
           return AddDays(1).GetDayStart().AddSeconds(-1);
       }
	   public SGDate GetHourStart()
	   {	
		   return new SGDate(GetYear(),GetMonth(),GetDay(),GetHour(),0,0);				  
	   }
	   
	 /**
	  * r>0: this更大
	  * r<0: this更小
	  */
	@Override
	public int compareTo(SGDate o) {
		if(o==null) {return 1;}
		return this.ToCalendar().compareTo(o.ToCalendar());
	}
	@Override
	public boolean equals(Object o) {
		return 0==this.compareTo((SGDate)o);
	}
	@Override
	public String toString() {
		return SGDataHelper.ObjectToDateString(_calendar, SGDataHelper.DateFormat);
	}
	public String toString( String format) {
		return SGDataHelper.ObjectToDateString(_calendar, format);
	}
	public Calendar ToCalendar() {
		return _calendar;
	}	
	public long toTimestamp() {
		return _calendar.getTimeInMillis();
	}

	/***
	 * yyyy.MM
	 * @return
	 */
	public String toCmonth() {
		return SGDataHelper.ObjectToDateString(_calendar, SGDataHelper.MonthFormat);
	}
	public PFCmonth toPFCmonth() {
		PFCmonth r=new PFCmonth();
		r.setCmonth(toCmonth());
		return r;
	}
	public String toYM() {
		return SGDataHelper.ObjectToDateString(_calendar, SGDataHelper.YMFormat);
	}
    public SGDate TClone()
    {
    	SGDate r=new SGDate(GetYear(),GetMonth(),GetDay(),GetHour(),GetMinute(),GetSecond());
    	return r;
    }
    /**
     * 性能应该高于SGDataHelper.compareDate
     * @param date
     * @return
     */
    public double getDaysMoreThen(SGDate date) {
    	return (this.toTimestamp()-date.toTimestamp())/(1000*60*60*24);
    }
    @Override
    public Object clone()
    {
        return TClone();
    }
//    protected static Calendar cloneCalendar(Calendar c)
//    {
//    	Calendar r = Calendar.getInstance();
//    	r.set(c.get(_yearEnum),c.get(_monthEnum),c.get(_dayEnum),c.get(_hourEnum),c.get(_minuteEnum),c.get(_secondEnum));
//        return r;
//    }
	
//
//	public PFDate(int year, int month, int day, int hour, int minute,int second) {
//		_calendar = Calendar.getInstance();
//		_calendar.set(year,  month,  day,  hour,  minute, second);
//	}
//	public int GetYear() {
//		return _calendar.get(Calendar.YEAR);
//	}
//	public int GetMonth() {
//		return _calendar.get(Calendar.MONTH);}
//	public int GetDay() {
//		return _calendar.get(Calendar.DAY_OF_MONTH);}
//	public int GetHour() {
//		return _calendar.get(Calendar.HOUR_OF_DAY);}
//	public int GetMinute() {
//		return _calendar.get(Calendar.MINUTE);}
//	public int GetSecond() {
//		return _calendar.get(Calendar.SECOND);}
//	public Calendar ToCalendar() {
//		return _calendar;
//	}	
//	public PFDate AddDays(int value) {
//		_calendar.add(_dayEnum, value);
//		return this;
//	}
//	
	
//	@Override
//	protected void computeTime() {
//		this.computeTime();
//		
//	}
//	@Override
//	protected void computeFields() {
//		this.computeFields();
//		
//	}
//	@Override
//	public void add(int field, int amount) {
//		this.add(field,  amount);
//		
//	}
//	@Override
//	public void roll(int field, boolean up) {
//		this.roll( field,  up);
//		
//	}
//	@Override
//	public int getMinimum(int field) {
//		return this.getMinimum(field);
//	}
//	@Override
//	public int getMaximum(int field) {
//		return this.getMaximum(field);
//	}
//	@Override
//	public int getGreatestMinimum(int field) {
//		return this.getGreatestMinimum(field);
//	}
//	@Override
//	public int getLeastMaximum(int field) {
//		return this.getLeastMaximum(field);
//	}
//	
	public boolean isToday() {
		SGDate n=SGDate.Now();
		return n.GetYear()==this.GetYear()&&n.GetMonth()==this.GetMonth()&&
				n.GetDay()==this.GetDay();
	}
}
