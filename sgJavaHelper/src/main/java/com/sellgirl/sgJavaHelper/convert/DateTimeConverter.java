////package com.sellgirl.pfHelperNotSpring.convert;
////
////import java.sql.Timestamp;
////import java.util.Calendar;
////import java.util.Date;
////
////import com.sellgirl.pfHelperNotSpring.IPFSqlFieldTypeConverter;
////import com.sellgirl.pfHelperNotSpring.PFDate;
////
////public class DateTimeConverter implements IPFSqlFieldTypeConverter {
////
////	@Override
////	public Object convert(Object o) {
////		if (o == null// || value == DBNull.Value
////		) {
////			return null;
////		}
//////      if (obj instanceof Calendar && ((Calendar)obj).compareTo(Calendar.MaxValue.Date) == 0) { return ""; }
////
////		if (o instanceof Calendar) {
////			return a->(Calendar) a;
////		} else if (o instanceof PFDate) {
////			return a->((PFDate) a).ToCalendar();
////		} else if (obj instanceof java.util.Date) {
////			return a->{
////				Calendar cal = Calendar.getInstance();
////				cal.setTime((Date) a);// 这个转换有问题,当obj为Date格式的1920-10-19时,设置手的值是1920-10-19
////										// 13:00:00,暂时不知道原因--benjamintodo20201217
////				return cal;
////			};
////		} else if (obj instanceof Timestamp) {
////			return a->{
////				Calendar cal = Calendar.getInstance();
////				cal.setTime((Timestamp) a);
////				return cal;
////			};
////		} else if (obj instanceof java.time.LocalDateTime) {
////			return a->new PFDate((java.time.LocalDateTime) a).ToCalendar();
////		} else if (obj instanceof Long) {
////			 return a->{
////					Calendar cal=Calendar.getInstance();
////					cal.setTimeInMillis((Long)a);
////					return cal;
////			 };
////		}
//////      else if (obj instanceof String)
//////      {
//////          return (String)obj;
//////      }
////		return null;
////	}
////
////}
//package com;
//
//


