package com.sellgirl.sgJavaHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SGDateRange {

    public SGDate StartDate=null;
    public SGDate EndDate=null;
    public SGDateRange(int year)
    {
    	//Calendar c=Calendar.getInstance();
//    	//c.set(year, 1, 1);
//    	c.set(year, 0, 1);
//    	StartDate=new PFDate(c);
    	StartDate=new SGDate(year,1,1,0,0,0);
    	
//    	Calendar ec=Calendar.getInstance();
//    	ec.set(year+1, 1, 1);
//    	ec.add(Calendar.SECOND, -1);
    	EndDate=new SGDate(year+1,1,1,0,0,0);
    	EndDate=EndDate.AddSeconds(-1);
    }
    //public DateRange(DateRangeType rangeType,int num)
    public SGDateRange(int year, int month)
    {
    	Calendar c=Calendar.getInstance();
    	c.set(year, month, 1);
    	StartDate=new SGDate(c);
    	Calendar ec=Calendar.getInstance();
    	ec.set(year, month, 1);
    	ec.add(Calendar.MONTH, 1);
    	ec.add(Calendar.SECOND, -1);
    	EndDate=new SGDate(ec);

    }
    public SGDateRange(Calendar startDate, Calendar endDate)
    {
        StartDate =new SGDate( startDate);
        EndDate = new SGDate(endDate);
////        EndDate = endDate;
//        //为了避免startDate和endDate是同一个引用的情况
//        if(startDate==endDate) {
//            EndDate = new PFDate(endDate).TClone();//
//        }else {
//        	 EndDate = new PFDate(endDate);
//        }
    }

//    public List<Calendar> GetPerMonthStart()
//    {
//    	PFDate currentDate =new PFDate(StartDate);
//    	PFDate maxDate =new PFDate(EndDate);
//    	 List<Calendar> r=new ArrayList<Calendar>();
//        while (currentDate.compareTo(maxDate)<1)
//       //     while (currentDate <= EndDate)
//        {
//        	r.add(PFDataHelper.GetMonthStart( currentDate.ToCalendar()));
//        	currentDate=currentDate.AddMonths(1);
//        }
//        return r;
//    }
    public List<SGDate> GetPerMonthStart()
    {
    	//PFDate minDate =new PFDate(StartDate);
    	SGDate currentDate =StartDate.TClone().GetMonthStart();
//    	PFDate maxDate =new PFDate(EndDate);
    	 List<SGDate> r=new ArrayList<SGDate>();
        while (currentDate.compareTo(EndDate)<1)
       //     while (currentDate <= EndDate)
        {
        	//r.add(PFDataHelper.GetMonthStart( currentDate.ToCalendar()));
        	if(currentDate.compareTo(StartDate)>-1) {
            	r.add(currentDate);
        	}
        	currentDate=currentDate.AddMonths(1);
        }
        return r;
    }
//    public List<Calendar> GetPerDayStart()
//    {
//    	Calendar currentDate = StartDate;
//   	 List<Calendar> r=new ArrayList<Calendar>();
//       while (currentDate.compareTo(EndDate)<1)
//      //     while (currentDate <= EndDate)
//       {
//       	r.add(PFDataHelper.GetDayStart( currentDate));
//       	currentDate.add(Calendar.DAY_OF_MONTH, 1);
//       }
//       return r;
//    }
    public List<SGDate> GetPerDayStart()
    {
    	SGDate currentDate = StartDate.TClone().GetDayStart();
   	 List<SGDate> r=new ArrayList<SGDate>();
       while (currentDate.compareTo(EndDate)<1)
      //     while (currentDate <= EndDate)
       {
          	//r.add(PFDataHelper.GetDayStart( currentDate));
       	if(currentDate.compareTo(StartDate)>-1) {
        	r.add(currentDate);
    	}
    	currentDate=currentDate.AddDays(1);
       }
       return r;
    }
}
