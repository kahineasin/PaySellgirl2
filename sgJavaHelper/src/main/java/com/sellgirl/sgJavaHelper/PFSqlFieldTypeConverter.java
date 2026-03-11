package com.sellgirl.sgJavaHelper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

//import com.sellgirl.pfHelperNotSpring.config.PFDataHelper;

/**
 * 数据库的字段类型(为了统一多种数据库的数据类型)
 * 
 * @author Administrator
 *
 */
public class PFSqlFieldTypeConverter {
//	public interface IPFSqlFieldTypeConverter{
//		public Object convert(Object o);
//	}

	public static IPFSqlFieldTypeConverter BoolConverter(Object value) {
		if (value == null // || value == DBNull.Value
		) {
			return null;
		}
		if (value instanceof byte[] && ((byte[]) value).length > 0) {
			return a -> {
				if (null == a) {
					return null;
				}
				if (((byte[]) a)[0] == 1) {
					return true;
				} else {
					return false;
				}
			};
		}
		return a -> {
			if (null == a) {
				return null;
			}
			String vs = a.toString();
			if ("1".equals(vs)) {
				return true;
			} // "1"的话tryParse是失败的
			if ("0".equals(vs)) {
				return false;
			} // "1"的话tryParse是失败的
			try {
				return Boolean.parseBoolean(vs);
			} catch (Exception e) {

			}
			if ("true,false".equals(vs)) {
				return true;
			} // mvc的Checkbox跟着一个hidden,如果选中时,传回来的是"true,false"
			return false;
		};
	}

	public static IPFSqlFieldTypeConverter BoolConverter0(Object value) {
		if (value == null // || value == DBNull.Value
		) {
			return null;
		}

		if (value instanceof byte[] && ((byte[]) value).length > 0) {
			return a -> {
				if (null == a) {
					return false;
				}
				if (((byte[]) a)[0] == 1) {
					return true;
				} else {
					return false;
				}
			};
		}
		return a -> {
			if (null == a) {
				return false;
			}
			String vs = a.toString();
			if ("1".equals(vs)) {
				return true;
			} // "1"的话tryParse是失败的
			if ("0".equals(vs)) {
				return false;
			} // "1"的话tryParse是失败的
			try {
				return Boolean.parseBoolean(vs);
			} catch (Exception e) {

			}
			if ("true,false".equals(vs)) {
				return true;
			} // mvc的Checkbox跟着一个hidden,如果选中时,传回来的是"true,false"
			return false;
		};
	}

	public static IPFSqlFieldTypeConverter IntConverter(Object value) {
		if (value == null // || value == DBNull.Value
		) {
			return null;
		}
		if (value instanceof Boolean) {
			return a -> {
				if (null == a) {
					return null;
				}
				Boolean b = (Boolean) a;
				return b ? 1 : 0;
			};
		}
		// int r = 0;
		if (value instanceof Integer) {
			return a -> {
				if (null == a) {
					return null;
				}
				return (Integer) a;
			};
		}
		if (value instanceof BigInteger) {
			return a -> {
				if (null == a) {
					return null;
				}
				return ((BigInteger) a).intValue();
			};
		}
		if (value instanceof Long) {
			return a -> {
				if (null == a) {
					return null;
				}
				return ((Long) a).intValue();
			};
		}
		if (value instanceof BigDecimal) {
			return a -> {
				if (null == a) {
					return null;
				}
				return ((BigDecimal) a).intValue();
			};
		}
		if (value instanceof String) {
			return a -> {
				if (null == a) {
					return null;
				}
				return Integer.valueOf((String) a);
			};
		}
//			        if (int.TryParse(value.ToString(), out r))
//			        {
//			            return r;
//			        }
		return null;
	}

	public static IPFSqlFieldTypeConverter IntConverter0(Object value) {
		if (value == null // || value == DBNull.Value
		) {
			return null;
		}
		if (value instanceof Boolean) {
			return a -> {
				if (null == a) {
					return 0;
				}
				Boolean b = (Boolean) a;
				return b ? 1 : 0;
			};
		}
		// int r = 0;
		if (value instanceof Integer) {
			return a -> {
				if (null == a) {
					return 0;
				}
				return (Integer) a;
			};
		}
		if (value instanceof BigInteger) {
			return a -> {
				if (null == a) {
					return 0;
				}
				return ((BigInteger) a).intValue();
			};
		}
		if (value instanceof Long) {
			return a -> {
				if (null == a) {
					return 0;
				}
				return ((Long) a).intValue();
			};
		}
		if (value instanceof BigDecimal) {
			return a -> {
				if (null == a) {
					return 0;
				}
				return ((BigDecimal) a).intValue();
			};
		}
		if (value instanceof String) {
			return a -> {
				if (null == a) {
					return 0;
				}
				return Integer.valueOf((String) a);
			};
		}
//	        if (int.TryParse(value.ToString(), out r))
//	        {
//	            return r;
//	        }
		return null;
	}

	public static IPFSqlFieldTypeConverter DoubleConverter(Object value) {
		if (value == null // || value == DBNull.Value
		) {
			return null;
		}
		if (value instanceof Double) {
			return a -> {
				if (null == a) {
					return null;
				}
				return (Double) a;
			};
		}
		if (value instanceof BigDecimal) {
			return a -> {
				if (null == a) {
					return null;
				}
				return ((BigDecimal) a).doubleValue();
			};
		}
		return null;
	}

	public static IPFSqlFieldTypeConverter DoubleConverter0(Object value) {
		if (value == null // || value == DBNull.Value
		) {
			return null;
		}
		if (value instanceof Double) {
			return a -> {
				if (null == a) {
					return new Double(0);
				}
				return (Double) a;
			};
		}
		if (value instanceof BigDecimal) {
			return a -> {
				if (null == a) {
					return new Double(0);
				}
				return ((BigDecimal) a).doubleValue();
			};
		}
		return null;
	}

	public static IPFSqlFieldTypeConverter DecimalConverter(Object value) {
		if (value == null // || value == DBNull.Value
		) {
			return null;
		}
		if (value instanceof BigDecimal) {
			return a -> {
				if (null == a) {
					return null;
				}
				return (BigDecimal) a;
			};
		}
		if (value instanceof Integer) {
			return a -> {
				if (null == a) {
					return null;
				}
				return new BigDecimal((Integer) a);
			};
		}
		if (value instanceof Long) {
			return a -> {
				if (null == a) {
					return null;
				}
				return new BigDecimal((Long) a);
			};
		}
		if (value instanceof Double) {
			return a -> {
				if (null == a) {
					return null;
				}
				return new BigDecimal((Double) a);
			};
		}
		if (value instanceof String) {
			return a -> {
				if (null == a) {
					return null;
				}
				return new BigDecimal((String) a);
			};
		}
		return null;
	}

	/**
	 * 常常报这个错误java.lang.ClassCastException: java.lang.Integer cannot be cast to java.math.BigDecimal. 可能是由于同1列的,来源值类型有多种造成的
	 * @param value
	 * @return
	 */
	public static IPFSqlFieldTypeConverter DecimalConverter0(Object value) {
		if (value == null // || value == DBNull.Value
		) {
			return null;
		}
		if (value instanceof BigDecimal) {
			return a -> {
				if (null == a) {
					return new BigDecimal(0);
				}
				return (BigDecimal) a;
			};
		}
		if (value instanceof Integer) {
			return a -> {
				if (null == a) {
					return new BigDecimal(0);
				}
				return new BigDecimal((Integer) a);
			};
		}
		if (value instanceof Long) {
			return a -> {
				if (null == a) {
					return new BigDecimal(0);
				}
				return new BigDecimal((Long) a);
			};
		}
		if (value instanceof Double) {
			return a -> {
				if (null == a) {
					return new BigDecimal(0);
				}
				return new BigDecimal((Double) a);
			};
		}
		if (value instanceof String) {
			return a -> {
				if (null == a) {
					return new BigDecimal(0);
				}
				return new BigDecimal((String) a);
			};
		}
		return null;
	}
//	public static IPFSqlFieldTypeConverter DecimalFaultTolerantConverter0(Object value) {
//		if (value == null // || value == DBNull.Value
//		) {
//			return null;
//		}
//		return a->{
//			return PFDataHelper.ObjectToDecimal0(a);
//		};
//	}

	public static IPFSqlFieldTypeConverter LongConverter(Object value)// long就是int64
	{
		if (value == null// || value == DBNull.Value
		) {
			return null;
		}
		if (value instanceof Boolean) {
			return a -> {
				if (null == a) {
					return null;
				}
				Boolean b = (Boolean) a;
				return b ? 1L : 0L;
			};
		}
		// long r = 0;
		if (value instanceof Double) {
			return a -> {
				if (null == a) {
					return null;
				}
				return ((Double) a).longValue();
			};
		}
		if (value instanceof BigDecimal) {
			return a -> {
				if (null == a) {
					return null;
				}
				return ((BigDecimal) a).longValue();
			};
		}
		return a -> {
			if (null == a) {
				return null;
			}
			return Long.valueOf(String.valueOf(a));
		};
//	       if (long.TryParse(value.ToString(), out r))
//	       {
//	           return r;
//	       }
		// return null;
	}

	public static IPFSqlFieldTypeConverter LongConverter0(Object value)// long就是int64
	{
		if (value == null// || value == DBNull.Value
		) {
			return null;
		}
		if (value instanceof Boolean) {
			return a -> {
				if (null == a) {
					return 0L;
				}
				Boolean b = (Boolean) a;
				return b ? 1L : 0L;
			};
		}
		// long r = 0;
		if (value instanceof Double) {
			return a -> {
				if (null == a) {
					return 0L;
				}
				return ((Double) a).longValue();
			};
		}
		if (value instanceof BigDecimal) {
			return a -> {
				if (null == a) {
					return 0L;
				}
				return ((BigDecimal) a).longValue();
			};
		}
		return a -> {
			if (null == a) {
				return 0L;
			}
			return Long.valueOf(String.valueOf(a));
		};
//	       if (long.TryParse(value.ToString(), out r))
//	       {
//	           return r;
//	       }
		// return null;
	}

	public static IPFSqlFieldTypeConverter StringConverter(Object obj) {
		if (obj == null // || obj == DBNull.Value
		) {
			return null;
		}
		if (obj instanceof Integer // || obj == DBNull.Value
		) {
			return a -> {
				if (null == a) {
					return null;
				}
				return String.valueOf((Integer) a);
			};
		}
		return a -> {
			if (null == a) {
				return null;
			}
			return a.toString();
		};

	}

	/**
	 * (不要空值)
	 * 
	 * @param obj
	 * @return
	 */
	public static IPFSqlFieldTypeConverter StringConverter0(Object obj) {
		if (obj == null // || obj == DBNull.Value
		) {
			return null;
		}
		if (obj instanceof Integer // || obj == DBNull.Value
		) {
			return a -> {
				if (null == a) {
					return "";
				}
				return String.valueOf((Integer) a);
			};
		}
		return a -> {
			if (null == a) {
				return "";
			}
			return a.toString();
		};

	}
//	public static IPFSqlFieldTypeConverter StringConverter0x(Object obj) {
//		if (obj == null // || obj == DBNull.Value
//		) {
//			return (IPFSqlFieldTypeNullConverter)PFSqlFieldTypeConverter::StringConverter0x;
//			//return PFSqlFieldTypeConverter::StringConverter0x;
//		}
//		if (obj instanceof Integer // || obj == DBNull.Value
//		) {
//			return a -> {
//				if (null == a) {
//					return "";
//				}
//				return String.valueOf((Integer) a);
//			};
//		}
//		return a -> {
//			if (null == a) {
//				return "";
//			}
//			return a.toString();
//		};
//
//	}

	public static IPFSqlFieldTypeConverter DateTimeConverter(Object obj) {
		if (obj == null// || value == DBNull.Value
		) {
			return null;
		}
//	      if (obj instanceof Calendar && ((Calendar)obj).compareTo(Calendar.MaxValue.Date) == 0) { return ""; }

		if (obj instanceof Calendar) {
			return a -> {
				if (null == a) {
					return null;
				}
				return (Calendar) a;
			};
		} else if (obj instanceof SGDate) {
			return a -> {
				if (null == a) {
					return null;
				}
				return ((SGDate) a).ToCalendar();
			};
		} else if (obj instanceof java.util.Date) {
			return a -> {
				if (null == a) {
					return null;
				}
				Calendar cal = Calendar.getInstance();
				cal.setTime((Date) a);// 这个转换有问题,当obj为Date格式的1920-10-19时,设置手的值是1920-10-19
										// 13:00:00,暂时不知道原因--benjamintodo20201217
				return cal;
			};
		} else if (obj instanceof Timestamp) {
			return a -> {
				if (null == a) {
					return null;
				}
				Calendar cal = Calendar.getInstance();
				cal.setTime((Timestamp) a);
				return cal;
			};
		} else if (obj instanceof java.time.LocalDateTime) {
			return a -> {
				if (null == a) {
					return null;
				}
				return new SGDate((java.time.LocalDateTime) a).ToCalendar();
			};
		} else if (obj instanceof Long) {
			return a -> {
				if (null == a) {
					return null;
				}
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis((Long) a);
				return cal;
			};
		} else if (obj instanceof Integer) {// mysql里的Date格式,在flink的kafka结果中是int
			SGDate start = new SGDate(1970, 1, 1, 0, 0, 0);
			return a -> {
				if (null == a) {
					return null;
				}
				SGDate end = start.AddDays((Integer) a);
				return end.ToCalendar();
			};
		} else if (obj instanceof String) {
			// 支持的格式有2022-09-09T10:00:00Z 2022-10-19T15:22:31+01:00
			if (((String) obj).indexOf("T") > -1) {
				return a -> {
					if (null == a) {
						return null;
					}
					// return Timestamp.valueOf(ZonedDateTime.parse((String)a).toLocalDateTime());
					// //网上说这种方法和下面是一样的. 但实际不一样, 这种写法的话, 当字符串时区和运行时区不一样时,得到的时间戳不正确
					return Timestamp.from(ZonedDateTime.parse((String) a).toInstant());
				};
			}
		}
		return null;
	}

	private static void ChangeSystemToZoneId(ZoneId zoneId){

		if(null!=zoneId){
			TimeZone tz=TimeZone.getTimeZone(zoneId);
			if(!TimeZone.getDefault().equals(tz)){
				TimeZone.setDefault(tz);
			}
		}
	}
	public static IPFSqlFieldTypeConverter TimestampConverter(Object obj, boolean isSmalltime//, ZoneId zoneId
															   ) {
		if (obj == null// || value == DBNull.Value
		) {
			return null;
		}
//	      if (obj instanceof Calendar && ((Calendar)obj).compareTo(Calendar.MaxValue.Date) == 0) { return ""; }

		if (obj instanceof Calendar) {
			return a -> {
				if (null == a) {
					return null;
				}
				Calendar cal = (Calendar) a;
				if (isSmalltime) {
					cal.set(Calendar.SECOND, 0);
				}
				return new Timestamp(cal.getTimeInMillis());
			};
		} else if (obj instanceof SGDate) {
			return a -> {
				if (null == a) {
					return null;
				}
				Calendar cal = ((SGDate) a).ToCalendar();
				if (isSmalltime) {
					cal.set(Calendar.SECOND, 0);
				}
				return new Timestamp(cal.getTimeInMillis());
			};
		}else if (obj instanceof Timestamp) {
			return a -> {
				if (null == a) {
					return null;
				}
				return (Timestamp) a;
			};
		} else if (obj instanceof java.util.Date) {
			return a -> {
				if (null == a) {
					return null;
				}
				Calendar cal = Calendar.getInstance();
				cal.setTime((Date) a);// 这个转换有问题,当obj为Date格式的1920-10-19时,设置手的值是1920-10-19
										// 13:00:00,暂时不知道原因--benjamintodo20201217
				if (isSmalltime) {
					cal.set(Calendar.SECOND, 0);
				}
				return new Timestamp(cal.getTimeInMillis());
			};
		}  else if (obj instanceof java.time.LocalDateTime) {
			return a -> {
				if (null == a) {
					return null;
				}
				Calendar cal = new SGDate((java.time.LocalDateTime) a).ToCalendar();
				if (isSmalltime) {
					cal.set(Calendar.SECOND, 0);
				}
				return new Timestamp(cal.getTimeInMillis());
			};
		}
//		else if (obj instanceof Long) {
//			return a -> {
//				if (null == a) {
//					return null;
//				}
//				Calendar cal = Calendar.getInstance();
//				cal.setTimeInMillis((Long) a);
//				if (isSmalltime) {
//					cal.set(Calendar.SECOND, 0);
//				}
//				return new Timestamp(cal.getTimeInMillis());
//			};
//		}
		else if (obj instanceof Long) {
			return a -> {
				if (null == a) {
					return null;
				}
				if (isSmalltime) {
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis((Long) a);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					return new Timestamp((Long) a);
				}else{
					return new Timestamp((Long) a);
				}
			};
		}
//		else if (obj instanceof Long) {
//			return a -> {
//				if (null == a) {
//					return null;
//				}
////				if(null!=zoneId&&!ZoneId.systemDefault().equals(zoneId)){
////					java.time.ZoneOffset.of(zoneId.getId());
////					TimeZone.setDefault(TimeZone.getTimeZone(zoneId));
////				}
//				if(null!=zoneId){
////					TimeZone tz=TimeZone.getTimeZone(zoneId);
////					if(!TimeZone.getDefault().equals(tz)){
////						TimeZone.setDefault(tz);
////					}
//					ChangeSystemToZoneId(zoneId);
//				}
//				if (isSmalltime) {
//					Calendar cal = null==zoneId?Calendar.getInstance():Calendar.getInstance(TimeZone.getTimeZone(zoneId));
//					cal.setTimeInMillis((Long) a);
//					cal.set(Calendar.SECOND, 0);
//					cal.set(Calendar.MILLISECOND, 0);
//					return Timestamp.from(cal.toInstant());
//				}else{
//					if(null==zoneId){
//						return new Timestamp((Long) a);
//					}else{
//						Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(zoneId));
//						cal.setTimeInMillis((Long) a);
//						return Timestamp.from(cal.toInstant());
//					}
//				}
//			};
//		}
		else if (obj instanceof Integer) {// mysql里的Date格式,在flink的kafka结果中是int
			SGDate start = new SGDate(1970, 1, 1, 0, 0, 0);
			return a -> {
				if (null == a) {
					return null;
				}
				SGDate end = start.AddDays((Integer) a);
				return new Timestamp(end.toTimestamp());
			};
		} else if (obj instanceof String) {
			// 支持的格式有2022-09-09T10:00:00Z 2022-10-19T15:22:31+01:00
			if (((String) obj).indexOf("T") > -1) {
				return a -> {
					if (null == a) {
						return null;
					}
					// return Timestamp.valueOf(ZonedDateTime.parse((String)a).toLocalDateTime());
					// //网上说这种方法和下面是一样的. 但实际不一样, 这种写法的话, 当字符串时区和运行时区不一样时,得到的时间戳不正确
					return Timestamp.from(ZonedDateTime.parse((String) a).toInstant());
				};
			}
		}
		return null;
	}

	public static IPFSqlFieldTypeConverter TimestampConverter0(Object obj, boolean isSmalltime//, ZoneId zoneId
															    ) {
		return TimestampConverter(obj, isSmalltime//, zoneId
		);// 时间用默认值反而不好,暂不用
	}

}