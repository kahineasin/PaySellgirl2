package com.sellgirl.sgJavaSpringHelper.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
//import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
//import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiFunction;
import java.util.function.Consumer;
//import java.util.function.Consumer;
import java.util.function.Function;
import java.util.jar.JarFile;
//import java.util.function.Predicate;
//import java.util.function.ToDoubleFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
//import com.sellgirl.sgJavaHelper.config.PFAppConfig;
//import pf.java.pfHelper.PFAppConfig;
//import pf.java.pfHelper.PFDateTime;
//import pf.java.pfHelper.SqlExpressionOperator;
import com.sellgirl.sgJavaHelper.*;
import com.sellgirl.sgJavaHelper.file.SGDirectory;
import com.sellgirl.sgJavaHelper.file.SGPath;
import com.sellgirl.sgJavaHelper.model.*;
import com.sellgirl.sgJavaHelper.sql.BaseSqlUpdateCollection;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.time.SGTimeSpan;
import com.sellgirl.sgJavaHelper.config.SGDataHelper.LocalDataType;

@Component
public class SGDataHelper {
	public static PFAppConfig _appConfig;
	public static String PID = null;
	public static String encoding = "UTF-8";
	public static int HTTP_STATUS_OK = 200;

	public static PFCmonth SysMaxMonth = null;
	public static PFCmonth SysMinMonth = null;
	private static ApplicationContext _applicationContext;
	public static PFEnvir CurrentEnvironmental = PFEnvir.release;
	/// <summary>
	/// 小数精确度默认4位(常用于除法的小数保留位数)
	/// </summary>
	public static int DecimalPrecision = 4;

	/**
	 * 网站域名(含http前缀,有必要可以带端口),在登陆页转换到域名地址(便于登陆metabase) 格式如:
	 * http://testebusiness.perfect99.com:88
	 * 
	 */
	public static String SiteDomain = null;
	/**
	 * sso单点登陆系统地址(含http前缀) 格式如: http://192.168.0.26:28113
	 */
	public static String SSOSiteDomain = null;
	public static String CookieDomain = null;

	@Autowired
	public SGDataHelper(PFAppConfig appConfig, ApplicationContext applicationContext) {
		_appConfig = appConfig;
		_applicationContext = applicationContext;
		if (_appConfig != null) {
			SiteDomain = _appConfig.getSiteDomain();
			SSOSiteDomain = _appConfig.getSsoSiteDomain();
			CookieDomain = _appConfig.getCookieDomain();
		}
	}

	/*
	 * 使用 ObjectToBool(大写开头)代替
	 */
	@Deprecated
	public static Boolean objectToBool(Object v) {
		return Boolean.valueOf(v.toString());
	}

	/**
	 * 不够位数的在前面补0，保留code的长度位数字
	 * 
	 * @param code
	 * @return
	 */
	public static String zeroOnTheLeft(int code, int len) {
		String result = "";
		// 保留len的位数
		// 0 代表前面补充0
		// d 代表参数为正数型
		result = String.format("%0" + len + "d", code);

		return result;
	}

	/// <summary>
	/// 转换为string类型 defult为string.Empty ,模板通用转换方法 对string的处理有bug, 所以用此方法独立处理
	/// </summary>
	/// <param name="obj"></param>
	/// <returns></returns>
	public static String ObjectToString(Object obj) {
		if (obj == null // || obj == DBNull.Value
		) {
			return "";
		}
		return obj.toString();

	}

//	/*
//	 * java的SelectNodes方法有问题(其实没问题)
//	 */
//	public List<Element> XmlSelectElements(List<Element> list, String pathExpression) {
//		List<Element> result = new ArrayList<Element>();
//		String[] paths = pathExpression.split("/");
//
//		for (Element i : list) {
//
//		}
//		if (paths.length == 1) {
//
//		}
//
//		return result;
//	}

	public static Double ObjectToDouble(Object value) {
		if (value == null // || value == DBNull.Value
		) {
			return null;
		}
		if (value instanceof Double) {
			return (Double) value;
		}
		if (value instanceof BigDecimal) {
			return ((BigDecimal) value).doubleValue();
		}
		return null;
	}

	public static BigDecimal ObjectToDecimal(Object value) {
		if (value == null // || value == DBNull.Value
		) {
			return null;
		}
		if (value instanceof BigDecimal) {
			return (BigDecimal) value;
		}
		if (value instanceof Integer) {
			return new BigDecimal((Integer) value);
		}
		if (value instanceof Long) {
			return new BigDecimal((Long) value);
		}
		return null;
	}

	public static BigDecimal ObjectToDecimal0(Object value) {
		BigDecimal r = ObjectToDecimal(value);
		if (r == null) {
			return new BigDecimal(0);
		}
		return r;
	}

	public static Integer ObjectToInt(Object value) {
		if (value == null // || value == DBNull.Value
		) {
			return null;
		}
		if (value instanceof Boolean) {
			Boolean b = (Boolean) value;
			return b ? 1 : 0;
		}
		// int r = 0;
		if (value instanceof Integer) {
			return (Integer) value;
		}
		if (value instanceof BigInteger) {
			return ((BigInteger) value).intValue();
		}
		if (value instanceof Long) {
			return ((Long) value).intValue();
		}
		if (value instanceof BigDecimal) {
			return ((BigDecimal) value).intValue();
		}
		if (value instanceof String) {
			return Integer.valueOf((String) value);
		}
//        if (int.TryParse(value.ToString(), out r))
//        {
//            return r;
//        }
		return null;
	}

	public static int ObjectToInt0(Object value) {
		Integer r = ObjectToInt(value);
		if (r == null) {
			return 0;
		}
		return r;
	}

//    public static long? ObjectToLong(object value)//long就是int64
//    {
//        if (value == null || value == DBNull.Value) { return null; }
//        if (value is bool)
//        {
//            var b = (bool)value;
//            return b ? 1 : 0;
//        }
//        long r = 0;
//        if (value is double)
//        {
//            if (long.TryParse(((double)value).ToString("0"), out r))//double小数位数过多会报错--benjamin20190927
//            {
//                return r;
//            }
//        }
//        if (long.TryParse(value.ToString(), out r))
//        {
//            return r;
//        }
//        return null;
//    }
//    public static double? ObjectToDouble(object value)
//    {
//        if (value == null || value == DBNull.Value) { return null; }
//        if (value is bool)
//        {
//            var b = (bool)value;
//            return b ? 1 : 0;
//        }
//        double r = 0;
//        if (double.TryParse(value.ToString(), out r))
//        {
//            return r;
//        }
//        return null;
//    }
	public static Boolean ObjectToBool(Object value) {
		if (value == null // || value == DBNull.Value
		) {
			return null;
		}
		if (value instanceof byte[] && ((byte[]) value).length > 0) {
			if (((byte[]) value)[0] == 1) {
				return true;
			} else {
				return false;
			}
		}
		// Boolean r = false;
		String vs = value.toString();
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
//        if (Boolean.TryParse(vs, out r))
//        {
//            return r;
//        }
		if (vs == "true,false") {
			return true;
		} // mvc的Checkbox跟着一个hidden,如果选中时,传回来的是"true,false"
		return null;
	}

	public static Calendar ObjectToDateTime(Object obj) {
		if (obj == null// || value == DBNull.Value
		) {
			return null;
		}
//      if (obj instanceof Calendar && ((Calendar)obj).compareTo(Calendar.MaxValue.Date) == 0) { return ""; }

		if (obj instanceof Calendar) {
			return (Calendar) obj;
		} else if (obj instanceof SGDate) {
			return ((SGDate) obj).ToCalendar();
		} else if (obj instanceof java.util.Date) {
			Calendar cal = Calendar.getInstance();
			cal.setTime((Date) obj);// 这个转换有问题,当obj为Date格式的1920-10-19时,设置手的值是1920-10-19
									// 13:00:00,暂时不知道原因--benjamintodo20201217
			return cal;
		} else if (obj instanceof Timestamp) {
			Calendar cal = Calendar.getInstance();
			cal.setTime((Timestamp) obj);
			return cal;
		}
//      else if (obj instanceof String)
//      {
//          return (String)obj;
//      }
		return null;
	}

	public static Calendar IDCardToBirthDay(Object value) {
		if (value == null // || value == DBNull.Value
		) {
			return null;
		}
		String sv = value.toString();
		if (sv.length() < 14) {
			return null;
		}
		// sv = sv.substring(6, 8);
		sv = sv.substring(6, 14);
//        //sv = sv.Insert(6, "-");
//        sv=PFDataHelper.StringInsert(sv, 6, "-");
//        //sv = sv.Insert(4, "-");
//        sv=PFDataHelper.StringInsert(sv, 4, "-");		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date;
		try {
			date = sdf.parse(sv);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			if(SGDate.Now().ToCalendar().compareTo(calendar)<0
					|| SGDataHelper.MinTime.ToCalendar().compareTo(calendar)>0
					||SGDataHelper.MaxTime.ToCalendar().compareTo(calendar)<0) {
				return null;
			}
			return calendar;
		} catch (java.text.ParseException e) {
			// e.printStackTrace();
		}
		return null;
	}

	/// <summary>
	/// 计算年龄(如果要用DateTime参数,就需要区别?可空类型,使用时也很麻烦,直接用object比较方便)
	/// </summary>
	/// <param name="birth">出生日期</param>
	/// <returns></returns>
	public static Integer GetAge(Object birth) {
		if (birth instanceof Calendar) {
			Calendar dBirth = (Calendar) birth;
			SGDate dBirthPFDate = new SGDate(dBirth);
			SGDate now = SGDate.Now();
			int age = now.GetYear() - dBirthPFDate.GetYear();
			if (now.GetMonth() < dBirthPFDate.GetMonth()
					|| (now.GetMonth() == dBirthPFDate.GetMonth() && now.GetDay() < dBirthPFDate.GetDay())) {
				age--;
			}
			return age;
		}
		return null;
	}

	/**
	 * object转enum
	 * 
	 * 反射时,如果要取消警告,可以这么写:
	 * 
	 * @@SuppressWarnings({ "unchecked", "rawtypes" }) Object
	 * ev=PFDataHelper.ObjectToEnum((Class<Enum>)pt, v);
	 * 
	 * @param cls
	 * @param value
	 * @return
	 */
	public static <TEnum extends Enum<TEnum>> TEnum ObjectToEnum(Class<TEnum> cls, Object value)
	// where TEnum : struct
	{
		if (value == null // || value == DBNull.Value
		) {
			return null;
		}
		try {
			return Enum.valueOf(cls, value.toString());
		} catch (Exception e) {
			return null;
		}

	}
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public static  Object ObjectToEnumNotT(Class<?> cls, Object value)
//	// where TEnum : struct
//	{
//		if (value == null // || value == DBNull.Value
//		) {
//			return null;
//		}
//		try {
//			return Enum.valueOf((Class<Enum>)cls, value.toString());
//		} catch (Exception e) {
//			return null;
//		}
//
//	}
//    public static TEnum? ObjectToEnum<TEnum>(object value)
//        where TEnum : struct
//    {
//        if (value == null || value == DBNull.Value) { return null; }
//        TEnum r;
//        try
//        {
//            r = (TEnum)Enum.Parse(typeof(TEnum), value.ToString());
//            return r;
//        }
//        catch (Exception e)
//        {
//            return null;
//        }
//        //if (Enum.TryParse(value.ToString(), out r))
//        //{
//        //    return r;
//        //}
//        //return null;
//    }

	/*
	 * 为了便于把Array List等都统一处理
	 */
	public static <T> ArrayList<T> ObjectToList(Object value) {
		if (value == null) {
			return null;
		}
		ArrayList<T> ls = new ArrayList<>();
		if (value instanceof List) {
			ls.addAll(SGDataHelper.<List<T>>ObjectAs(value));
		}
		if (value instanceof Object[]) {
			// ls.addAll(Arrays.<T>asList((T[]) value));
			ls.addAll(Arrays.<T>asList(SGDataHelper.<T[]>ObjectAs(value)));
		}
		return ls;
	}

	/*
	 * java内部的List.ToArray太低智商了，自己写一个高效的
	 */
	public static <T> T[] ObjectToArray(Object value, Class<T> type) {
		if (value instanceof List) {
			List<?> list = (List<?>) value;
			T[] array = SGDataHelper.<T[]>ObjectAs(Array.newInstance(type, list.size()));
			for (int i = 0; i < list.size(); i++) {
				array[i] = SGDataHelper.<T>ObjectAs(list.get(i));
			}
			return array;
		}
		return null;
	}

//	public static Object ConvertObjectByClass(Object value, Class<?> srcType, Class<?> dstType) {
//		// 当把DataTable转为LocalJson再读为DataTable时会出现这些情况,如"查网络图"里的ch字段,以后再考虑怎么排除此问题
//		String srcTypeStr = GetStringByType(srcType);
//		String dstTypeStr = GetStringByType(dstType);
//		PFSqlFieldType srcPFType = GetPFTypeByClass(srcType);
//		PFSqlFieldType dstPFType = GetPFTypeByClass(dstType);
//		if (BigDecimal.class.equals(srcType) && Double.class.equals(dstType)) {
//			return ((BigDecimal) value).doubleValue();
//		} else if (// dstType == typeof(decimal?) || dstType == typeof(decimal)
//		"decimal".equals(dstTypeStr)) {
//			return ObjectToDecimal(value);
//		}
//		// else if ((dstType == typeof(Int32?) || dstType == typeof(Int32))
//		// && srcType == typeof(Int64)
//		// )
//		// {
//		// return Convert.ToInt32(value);
//		// }
//		else if (// dstType == typeof(Int32?) || dstType == typeof(Int32)
//		"int".equals(dstTypeStr)) {
//			return PFDataHelper.ObjectToInt(value);
//		} else if (// srcType == typeof(SByte) && (dstType == typeof(bool?) || dstType ==
//					// typeof(bool))//mysql中读出来的bool类型是SBype的
//		"byte".equals(srcTypeStr) && ("bool".equals(dstTypeStr))) {
//			byte sb = (byte) value;
//			if (sb == 1) {
//				return true;
//			}
//			if (sb == 0) {
//				return false;
//			}
//			return null;
//		} else if (// srcType == typeof(SByte) && (dstType == typeof(bool?) || dstType ==
//					// typeof(bool))//mysql中读出来的bool类型是SBype的
//		"byte[]".equals(srcTypeStr) && ("bool".equals(dstTypeStr))) {
//			byte[] sb = (byte[]) value;
//			if (sb[0] == 1) {
//				return true;
//			}
//			if (sb[0] == 0) {
//				return false;
//			}
//			return null;
//		} else if (// srcType == typeof(int) && (dstType == typeof(bool?) || dstType ==
//					// typeof(bool))//mysql中读出来的bool类型是SBype的
//		("int".equals(srcTypeStr) || Integer.class.equals(srcType))
//				&& ("bool".equals(dstTypeStr) || Boolean.class.equals(dstType))) {
//			if (value != null) {
//				String sb = value.toString();
//				if ("1".equals(sb)) {
//					return true;
//				}
//				if ("0".equals(sb)) {
//					return false;
//				}
//			}
//			return null;
//		} else if (PFSqlFieldType.BigDecimal.equals(srcPFType) && (PFSqlFieldType.Bool.equals(dstPFType))) {
//			return ObjectToBool(value);
//		} else if (// srcType == typeof(decimal) && (dstType == typeof(Int64?) || dstType ==
//					// typeof(Int64))//mysql中读出来的bigint类型是decimal的?如us_user.id列--benjamin20190730
//		"decimal".equals(srcTypeStr) && ("int".equals(dstTypeStr))) {
//			return ObjectToLong(value);
//		} else if ("DateTime".equals(dstTypeStr)) {
//			return ObjectToDateTime(value);
//		} else if (// (srcType == typeof(string)|| (srcType == typeof(MySqlDateTime)) &&
//		// (dstType == typeof(DateTime?) || dstType ==
//		// typeof(DateTime))//mysql中if(c.card_time<'1753-01-01 00:00:00','1753-01-01
//		// 00:00:00',c.card_time) as fjoindate得到的竟然是string格式,原因不明
//		dstType.equals(Date.class)) {
//			Calendar c = ObjectToDateTime(value);
//			return c == null ? null : ObjectToDateTime(value).getTime();
//		} else if (String.class.equals(dstType)) {
//			return PFDataHelper.ObjectToString(value);
//		}
//
////      else if (srcType == typeof(string) && dstType == typeof(string[]))
////      {
////          return (value as string).Split(new[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
////      }
//		else {
//			return value;
//		}
//
//	}

	public static Object ConvertObjectByType(Object value, Type srcType, Type dstType) {
		// 当把DataTable转为LocalJson再读为DataTable时会出现这些情况,如"查网络图"里的ch字段,以后再考虑怎么排除此问题
		String srcTypeStr = GetStringByType(srcType);
		String dstTypeStr = GetStringByType(dstType);
		// PFSqlFieldType srcPFType = GetPFTypeByClass(srcType);
		// PFSqlFieldType dstPFType = GetPFTypeByClass(dstType);
		if (BigDecimal.class.equals(srcType) && Double.class.equals(dstType)) {
			return ((BigDecimal) value).doubleValue();
		} else if (// dstType == typeof(decimal?) || dstType == typeof(decimal)
		"decimal".equals(dstTypeStr)) {
			return ObjectToDecimal(value);
		}
		// else if ((dstType == typeof(Int32?) || dstType == typeof(Int32))
		// && srcType == typeof(Int64)
		// )
		// {
		// return Convert.ToInt32(value);
		// }
		else if (// dstType == typeof(Int32?) || dstType == typeof(Int32)
		"int".equals(dstTypeStr)) {
			return SGDataHelper.ObjectToInt(value);
		} else if (// srcType == typeof(SByte) && (dstType == typeof(bool?) || dstType ==
					// typeof(bool))//mysql中读出来的bool类型是SBype的
		"byte".equals(srcTypeStr) && ("bool".equals(dstTypeStr))) {
			byte sb = (byte) value;
			if (sb == 1) {
				return true;
			}
			if (sb == 0) {
				return false;
			}
			return null;
		} else if (// srcType == typeof(SByte) && (dstType == typeof(bool?) || dstType ==
					// typeof(bool))//mysql中读出来的bool类型是SBype的
		"byte[]".equals(srcTypeStr) && ("bool".equals(dstTypeStr))) {
			byte[] sb = (byte[]) value;
			if (sb[0] == 1) {
				return true;
			}
			if (sb[0] == 0) {
				return false;
			}
			return null;
		} else if (// srcType == typeof(int) && (dstType == typeof(bool?) || dstType ==
					// typeof(bool))//mysql中读出来的bool类型是SBype的
		("int".equals(srcTypeStr) || Integer.class.equals(srcType))
				&& ("bool".equals(dstTypeStr) || Boolean.class.equals(dstType))) {
			if (value != null) {
				String sb = value.toString();
				if ("1".equals(sb)) {
					return true;
				}
				if ("0".equals(sb)) {
					return false;
				}
			}
			return null;
		} else if (
		// PFSqlFieldType.BigDecimal.equals(srcPFType) &&
		// (PFSqlFieldType.Bool.equals(dstPFType))
		Boolean.class.equals(dstType) || boolean.class.equals(dstType)) {
			return ObjectToBool(value);
		} else if (// srcType == typeof(decimal) && (dstType == typeof(Int64?) || dstType ==
					// typeof(Int64))//mysql中读出来的bigint类型是decimal的?如us_user.id列--benjamin20190730
		"decimal".equals(srcTypeStr) && ("int".equals(dstTypeStr))) {
			return ObjectToLong(value);
		} else if ("DateTime".equals(dstTypeStr)) {
			return ObjectToDateTime(value);
		} else if (// (srcType == typeof(string)|| (srcType == typeof(MySqlDateTime)) &&
		// (dstType == typeof(DateTime?) || dstType ==
		// typeof(DateTime))//mysql中if(c.card_time<'1753-01-01 00:00:00','1753-01-01
		// 00:00:00',c.card_time) as fjoindate得到的竟然是string格式,原因不明
		dstType.equals(Date.class)) {
			Calendar c = ObjectToDateTime(value);
			return c == null ? null : ObjectToDateTime(value).getTime();
		} else if (String.class.equals(dstType)) {
			return SGDataHelper.ObjectToString(value);
		}

//      else if (srcType == typeof(string) && dstType == typeof(string[]))
//      {
//          return (value as string).Split(new[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
//      }
		else {
			return value;
		}

	}

//	/**
//	 * 
//	 * @param value
//	 * @param srcType
//	 * @param dstSqlType
//	 * @param dstSqlTypeName sqlserver里smalldatetime和datetime的inttype都是93,只有通过String名来判断
//	 * @return
//	 */
//	public static Object ConvertObjectToSqlTypeByPFType(Object value, PFSqlFieldType srcType, int dstSqlType,
//			String dstSqlTypeName) {
//		if (value == null) {
//			return null;
//		}
//		switch (dstSqlType) {
//		case java.sql.Types.BOOLEAN:
//		case java.sql.Types.BIT:
//			return ObjectToBool(value);
////          case "byte":
////              return Byte.class;
////          case "sbyte":
////              return Type.GetType("System.SByte", true, true);
////          case "char":
////              return char.class;
//		case java.sql.Types.DOUBLE:
//			return ObjectToDouble(value);
//		case java.sql.Types.DECIMAL:
////		case java.sql.Types.NUMERIC://如果NUMERIC转为BigDecimal的话,在spring boot 2.1.2.RELEASE 版本下bulk入sqlserver时,会多了10倍,但helper的2.0.4.RELEASE项目上测试则不会--benjamin20201225
//			return ObjectToDecimal(value);
//		case java.sql.Types.NUMERIC:
//			return ObjectToDecimal(value).floatValue();
////          case java.sql.Types.DECIMAL:
////              return PFSqlFieldType.BigDecimal;
//		case java.sql.Types.INTEGER:
//			return ObjectToInt(value);
//		case java.sql.Types.BIGINT:
//			return ObjectToLong(value);
//		case java.sql.Types.VARCHAR:
//		case java.sql.Types.NVARCHAR:
//			return ObjectToString(value);
//		case java.sql.Types.TIMESTAMP:
//			Calendar c = PFDataHelper.ObjectToDateTime(value);
//			if ("smalldatetime".equals(dstSqlTypeName)) {
//				c.set(Calendar.SECOND, 0);
//			}
//			return new Timestamp(c.getTimeInMillis());
//		case java.sql.Types.DATE:
//			return ObjectToDateTime(value);
////          case java.sql.Types.:
////              return PFSqlFieldType.UUID;
//		default:
//			return ObjectToString(value);
//		}
//
//	}
//
//	public static Object ConvertObjectToSqlTypeByPFType(Object value, PFSqlFieldType srcType, int dstSqlType) {
//		return ConvertObjectToSqlTypeByPFType(value, srcType, dstSqlType, null);
//	}

//	/**
//	 * 实测此方法还没有ConvertObjectToSqlTypeByPFType()高效
//	 * 
//	 * @param value
//	 * @param srcType
//	 * @param dstSqlType
//	 * @return
//	 */
//	@Deprecated
//	public static Function<Object, Object> GetObjectToSqlTypeByPFTypeConverter(Object value, PFSqlFieldType srcType,
//			int dstSqlType) {
//		if (value == null) {
//			return null;
//		}
//		switch (dstSqlType) {
//		case java.sql.Types.BOOLEAN:
//		case java.sql.Types.BIT:
//			return a -> ObjectToBool(a);
////          case "byte":
////              return Byte.class;
////          case "sbyte":
////              return Type.GetType("System.SByte", true, true);
////          case "char":
////              return char.class;
//		case java.sql.Types.DOUBLE:
//			return a -> ObjectToDouble(a);
//		case java.sql.Types.DECIMAL:
////		case java.sql.Types.NUMERIC://如果NUMERIC转为BigDecimal的话,在spring boot 2.1.2.RELEASE 版本下bulk入sqlserver时,会多了10倍,但helper的2.0.4.RELEASE项目上测试则不会--benjamin20201225
//			return a -> ObjectToDecimal(a);
//		case java.sql.Types.NUMERIC:
//			return a -> ObjectToDecimal(a).floatValue();
////          case java.sql.Types.DECIMAL:
////              return PFSqlFieldType.BigDecimal;
//		case java.sql.Types.INTEGER:
//			return a -> ObjectToInt(a);
//		case java.sql.Types.BIGINT:
//			return a -> ObjectToLong(a);
//		case java.sql.Types.VARCHAR:
//		case java.sql.Types.NVARCHAR:
//			return a -> ObjectToString(a);
//		case java.sql.Types.TIMESTAMP:
//			return a -> new Timestamp(PFDataHelper.ObjectToDateTime(a).getTimeInMillis());
//		case java.sql.Types.DATE:
//			return a -> ObjectToDateTime(a);
////          case java.sql.Types.:
////              return PFSqlFieldType.UUID;
//		default:
//			return a -> ObjectToString(a);
//		}
//
//	}
//
//	public static Object ConvertObjectToPFTypeBySqlType(Object value, int srcSqlType, PFSqlFieldType dstType) {
//		if (value == null) {
//			return null;
//		}
//		if (PFSqlFieldType.Bool.equals(dstType)) {
//			return ObjectToBool(value);
//		} else if (PFSqlFieldType.Decimal.equals(dstType)) {
//			return ObjectToDecimal(value);
//		} else if (PFSqlFieldType.Int.equals(dstType)) {
//			return ObjectToInt(value);
//		} else if (PFSqlFieldType.String.equals(dstType)) {
//			return ObjectToString(value);
//		} else if (PFSqlFieldType.DateTime.equals(dstType)) {
//			return ObjectToDateTime(value);
//		}
//		return ObjectToString(value);
//	}

//	/**
//	 * 为了保留转换方式(提高效率)
//	 * 
//	 * @param value
//	 * @param srcSqlType
//	 * @param dstType
//	 * @return
//	 */
//	public static Function<Object, Object> GetObjectToPFTypeBySqlTypeConverter(Object value, int srcSqlType,
//			PFSqlFieldType dstType) {
//		if (value == null) {
//			return null;
//		}
//		if (PFSqlFieldType.Bool.equals(dstType)) {
//			return a -> ObjectToBool(a);
//		} else if (PFSqlFieldType.Decimal.equals(dstType) || PFSqlFieldType.BigDecimal.equals(dstType)) {
//			return a -> ObjectToDecimal(a);
//		} else if (PFSqlFieldType.Int.equals(dstType)) {
//			return a -> ObjectToInt(a);
//		} else if (PFSqlFieldType.Long.equals(dstType)) {
//			return a -> ObjectToLong(a);
//		} else if (PFSqlFieldType.String.equals(dstType)) {
//			return a -> ObjectToString(a);
//		} else if (PFSqlFieldType.DateTime.equals(dstType)) {
//			return a -> ObjectToDateTime(a);
//		}
//		return a -> ObjectToString(a);
//	}

	public static void DisaposeObject(Object o1) {
		if (o1 == null) {
			return;
		}
		if (o1 instanceof ISGDisposable) {
			((ISGDisposable) o1).dispose();
		}
	}

//    
	/**
	 * 时间戳转换成日期格式字符串
	 * 
	 * @param seconds   精确到秒的字符串
	 * @param formatStr
	 * @return
	 */
	public static String TimeStampToDateString(long timeStamp, String format) {
//      if(seconds == null || seconds.isEmpty() || seconds.equals("null")){  
//          return "";  
//      }  
		if (format == null || format.isEmpty()) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(timeStamp));
	}

	public static Boolean ObjectIsNull(Object s) {
		if (s == null) {
			return true;
		}
		// if (s == DBNull.Value) { return true; }
		return false;

	}

	// #region String
	public static Boolean StringIsNullOrWhiteSpace(Object s) {
		if (s == null) {
			return true;
		}
		// if (s == DBNull.Value) { return true; }
		return StringIsNullOrWhiteSpace(s.toString());

	}

	public static Boolean StringIsNullOrWhiteSpace(String s) {
		if (s == null) {
			return true;
		}
		if (s == "null") {
			return true;
		}
		return s.isEmpty() || s.replace(" ", "").equals("");
	}

	public static String StringInsert(String dst, int pos, String s) {
		StringBuilder sb = new StringBuilder(dst);// 构造一个StringBuilder对象
		sb.insert(pos, s);// 在指定的位置1，插入指定的字符串
		return sb.toString();
	}

	/*
	 * java的String.format方法的索引用法不好用,此方法模拟C#的做法.现时不支持{{}}的转译--benjamin todo
	 */
	public static String FormatString(String s, Object... ps) {
		for (int i = 0; i < ps.length; i++) {
			String pattern = "\\{" + String.valueOf(i) + "\\}";
			String replacement = "";
			if (ps[i] != null) {
				replacement = ps[i].toString();// 以后需要根据类型来判断--benjamin todo
			}
			// s=s.replaceAll(pattern, replacement);//当ps中有$符号时会报错
			s = s.replaceAll(pattern, replacement.replace("$", "\\$"));
//	    	RegExp exp = new RegExp("\\{" + String.valueOf(i - 1)+ "\\}", "gm");
//	        arguments[0] = arguments[0].replace(exp, arguments[i]);
		}
		return s;
	}

	// #endregion String
	public static <RequestType extends HttpRequestBase> SGRequestResult HttpRequest(RequestType httpRequest, String url,
			String body, Map<String, String> headers) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;

		URIBuilder uriBuilder;
		try {
			uriBuilder = new URIBuilder(url);
			httpRequest.setURI(uriBuilder.build());
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		// 根据带参数的URI对象构建GET请求对象

		if (headers != null) {


			Iterator<Entry<String, String>> headerIterator = headers.entrySet().iterator(); // 循环增加header
			while (headerIterator.hasNext()) {
				Entry<String, String> elem = headerIterator.next();
				httpRequest.addHeader(elem.getKey(), elem.getValue());
			}
		}


		if (httpRequest instanceof HttpEntityEnclosingRequestBase && (!StringIsNullOrWhiteSpace(body))) {
			HttpEntityEnclosingRequestBase httpPost = ((HttpEntityEnclosingRequestBase) httpRequest);
			httpPost.setHeader("Content-Type", "application/json;charset=" + encoding);
			httpPost.setEntity(new StringEntity(body, encoding));

		}

 
		SGRequestResult result = new SGRequestResult();
		// 执行请求
		try {
			response = httpClient.execute(httpRequest);

			result.statusCode = response.getStatusLine().getStatusCode();
			if (HTTP_STATUS_OK == result.statusCode) {
				// 获取服务器请求的返回结果，注意此处为了保险要加上编码格式
				try {
					result.content = EntityUtils.toString(response.getEntity(), encoding);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				result.error = "Invalide response from API" + response.toString();
			}
		} catch (ClientProtocolException e) {
			result.setError(e.getMessage());
			result.refuse = true;
			// e.printStackTrace();
		} catch (IOException e) {
			result.setError(e.getMessage());
			result.refuse = true;
			// e.printStackTrace();
		}

		return result;
	}

	public static SGRequestResult HttpDelete(String url, String body, Map<String, String> headers) {
		return HttpRequest(new HttpDelete(), url, body, headers);
	}

	public static SGRequestResult HttpPut(String url, String body, Map<String, String> headers) {
		return HttpRequest(new HttpPut(), url, body, headers);
	}

	public static SGRequestResult HttpGet(String url, String body, Map<String, String> headers) {
		return HttpRequest(new HttpGet(), url, body, headers);
	}

	public static SGRequestResult HttpPost(String url, String body, Map<String, String> headers) {
		return HttpRequest(new HttpPost(), url, body, headers);
	}

	public static SGRequestResult HttpDelete(String url, String body) {
		return HttpRequest(new HttpDelete(), url, body, null);
	}

	public static SGRequestResult HttpPut(String url, String body) {
		return HttpRequest(new HttpPut(), url, body, null);
	}

	public static SGRequestResult HttpGet(String url, String body) {
		return HttpRequest(new HttpGet(), url, body, null);
	}

	public static SGRequestResult HttpPost(String url, String body) {
		return HttpRequest(new HttpPost(), url, body, null);
	}

	/**
	 * 文件上传支持中文 注意postBody里的每个参数，在controller里似乎只能用String或String[]等简单类型接收，原因不明
	 *
	 * @param reportUrl
	 * @param filepath
	 * @param userId
	 * @param systemName
	 * @throws IOException
	 */
	public static void HttpPostFile(String reportUrl, File file, // String filepath,
			Map<String, Object> postBody) {
		CloseableHttpClient closeableHttpClient = null;
		CloseableHttpResponse response = null;
		try {
			closeableHttpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(reportUrl);

			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(200000).setSocketTimeout(200000)
					.build();
			httpPost.setConfig(requestConfig);

			// String filename = filepath.split("/")[filepath.split("/").length - 1];
//            File file = new File(filepath);

//            String filename = file.getName(); 
//            FileBody fileBody = new FileBody(file, ContentType.create("multipart/form-data", "UTF-8"), filename);

//            StringBody userIdStringBody = new StringBody(userId, ContentType.create("multipart/form-data", "UTF-8"));
//            StringBody systemNameStringBody = new StringBody(systemName, ContentType.create("multipart/form-data", "UTF-8"));
//            StringBody reportNameStringBody = new StringBody(filename, ContentType.create("multipart/form-data", "UTF-8"));

//            HttpEntity httpEntity = MultipartEntityBuilder
//                    .create()
//                    .setCharset(Charset.forName("UTF-8"))
//                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
//                    .addPart("file", fileBody)
//                    .addPart("userId", userIdStringBody)
//                    .addPart("systemName", systemNameStringBody)
//                    .addPart("reportName", reportNameStringBody)
//                    .build();
//            MultipartEntityBuilder httpBuilder=MultipartEntityBuilder
//                    .create()
//                    .setCharset(Charset.forName("UTF-8"))
//                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
//                    .addPart("file", fileBody);

			MultipartEntityBuilder httpBuilder = MultipartEntityBuilder.create().setCharset(Charset.forName("UTF-8"))
					.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

			if (file != null) {
				String filename = file.getName();
				FileBody fileBody = new FileBody(file, ContentType.create("multipart/form-data", "UTF-8"), filename);
				httpBuilder.addPart("file", fileBody);
			}

			Iterator<Entry<String, Object>> postBodyIter = postBody.entrySet().iterator();
			while (postBodyIter.hasNext()) {
				Entry<String, Object> key = postBodyIter.next();
				Object value = key.getValue();
				if (value instanceof String) {
					httpBuilder.addPart(key.getKey(),
							new StringBody(ObjectToString(value), ContentType.create("multipart/form-data", "UTF-8")));
				} else {
					httpBuilder.addPart(key.getKey(),
							new StringBody(JSON.toJSONString(value), ContentType.APPLICATION_JSON));
					// httpBuilder.addTextBody(key.getKey(),JSON.toJSONString(value),
					// ContentType.APPLICATION_JSON);
					// httpBuilder.addBinaryBody(key.getKey(), new
					// StringBody(JSON.toJSONString(value), ContentType.APPLICATION_JSON));
				}
			}

			HttpEntity httpEntity = httpBuilder.build();

			httpPost.setEntity(httpEntity);
			response = closeableHttpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				System.out.println("上传成功");
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
				StringBuffer buffer = new StringBuffer();
				String str = "";
//                while ((str = reader.readLine()) != null && (str = reader.readLine()).trim().length() > 0) {
//                    buffer.append(str);
//                }
				while ((str = reader.readLine()) != null && str.trim().length() > 0) {
					buffer.append(str);
				}
				System.out.println(buffer.toString());
				System.out.println("上传失败：" + statusCode);
			}
			closeableHttpClient.close();
			if (response != null) {
				response.close();
			}

		} catch (Exception ex) {
			System.out.println("uploadReport发生异常：" + ex);
		} finally {
			try {
				if (closeableHttpClient != null) {
					closeableHttpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

//    public static void HttpPostFileAsync(String reportUrl, File file,//String filepath,
//                                    Map<String,Object> postBody) {
//        CloseableHttpClient closeableHttpClient = null;
//        CloseableHttpResponse response = null;
//        try {
//            closeableHttpClient = HttpClients.createDefault();
//            HttpPost httpPost = new HttpPost(reportUrl);
//            
//            RequestConfig requestConfig = RequestConfig.custom()
//            		.setConnectTimeout(200000).setSocketTimeout(200000)
//            		.build();
//            httpPost.setConfig(requestConfig);
//            
//            //String filename = filepath.split("/")[filepath.split("/").length - 1];
////            File file = new File(filepath);
//            String filename = file.getName();
// 
//            FileBody fileBody = new FileBody(file, ContentType.create("multipart/form-data", "UTF-8"), filename);
////            StringBody userIdStringBody = new StringBody(userId, ContentType.create("multipart/form-data", "UTF-8"));
////            StringBody systemNameStringBody = new StringBody(systemName, ContentType.create("multipart/form-data", "UTF-8"));
////            StringBody reportNameStringBody = new StringBody(filename, ContentType.create("multipart/form-data", "UTF-8"));
//
////            HttpEntity httpEntity = MultipartEntityBuilder
////                    .create()
////                    .setCharset(Charset.forName("UTF-8"))
////                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
////                    .addPart("file", fileBody)
////                    .addPart("userId", userIdStringBody)
////                    .addPart("systemName", systemNameStringBody)
////                    .addPart("reportName", reportNameStringBody)
////                    .build();
//            MultipartEntityBuilder httpBuilder=MultipartEntityBuilder
//                    .create()
//                    .setCharset(Charset.forName("UTF-8"))
//                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
//                    .addPart("file", fileBody);
//
//     	   Iterator<Entry<String,Object>> postBodyIter = postBody.entrySet().iterator();
//     	   while(postBodyIter.hasNext()){
//     		   Entry<String,Object> key=postBodyIter.next();
//     		   Object value=key.getValue();
//     		   if(value instanceof String) {
//          		  httpBuilder.addPart(key.getKey(),new StringBody(ObjectToString(value), ContentType.create("multipart/form-data", "UTF-8")));
//     		   }
//     	   }
//     	   
//            HttpEntity httpEntity = httpBuilder
//                    .build();
// 
//            httpPost.setEntity(httpEntity);
//
//            final CloseableHttpClient tmpCloseableHttpClient=closeableHttpClient;
//	        new Thread() {//线程操作
//	               public void run() {
//	                	   try {
//							tmpCloseableHttpClient.execute(httpPost);
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//	               }
//	        }.start();//不会死锁
////            HttpEntity responseEntity = response.getEntity();
////            int statusCode = response.getStatusLine().getStatusCode();
////            if (statusCode == 200) {
////                System.out.println("上传成功");
////            } else {
////                BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
////                StringBuffer buffer = new StringBuffer();
////                String str = "";
//////                while ((str = reader.readLine()) != null && (str = reader.readLine()).trim().length() > 0) {
//////                    buffer.append(str);
//////                }
////                while ((str = reader.readLine()) != null && str.trim().length() > 0) {
////                    buffer.append(str);
////                }
////                System.out.println(buffer.toString());
////                System.out.println("上传失败：" + statusCode);
////            }
////            closeableHttpClient.close();
////            if (response != null) {
////                response.close();
////            }
// 
//        } catch (Exception ex) {
//            System.out.println("uploadReport发生异常：" + ex);
//        } finally {
//            try {
//                if (closeableHttpClient != null) {
//                    closeableHttpClient.close();
//                }
//                if (response != null) {
//                    response.close();
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
	/**
	 * URL 解码
	 *
	 * @return String
	 * @author lifq
	 * @date 2015-3-17 下午04:09:51
	 */
	public static String getURLDecoderString(String str) {
		String result = "";
		if (null == str) {
			return "";
		}
		try {
			result = java.net.URLDecoder.decode(str, encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * URL 转码
	 *
	 * @return String
	 * @author lifq
	 * @date 2015-3-17 下午04:10:28
	 */
	public static String getURLEncoderString(String str, String encode) {
		String result = "";
		if (null == str) {
			return "";
		}
		try {
			result = java.net.URLEncoder.encode(str, encode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getURLEncoderString(String str) {
		return getURLEncoderString(str, encoding);
	}

	public static Boolean EnumHasFlag(int source, int target)

	{
		return (source & target) == target;
	}

	/// <summary>
	/// source中的任意值和target中的任意值相等时,返回true
	/// </summary>
	/// <param name="source"></param>
	/// <param name="target"></param>
	/// <returns></returns>
	public static Boolean EnumAnyFlag(int source, int target) {
		return (source >= 0 && target == 0) || (source != 0 && ((source & target) != 0));
	}

	public static SGTimeSpan GetTimeSpan(long ElapsedMilliseconds, Integer ymd)// = PFYmd.Hour | PFYmd.Minute |
																				// PFYmd.Second)
	{
		if (ymd == null) {
			ymd = SGYmd.Hour | SGYmd.Minute | SGYmd.Second;
		}
		// int xx=PFYmd.Hour|PFYmd.Millisecond;

		long after = ElapsedMilliseconds / 1000;// 这里得到是秒
		SGTimeSpan result = new SGTimeSpan(ymd);
		if (EnumHasFlag(ymd, SGYmd.Hour)) {
			result.Hour = (int) (after / (60 * 60));// 计算整数小时数
			after -= (result.Hour * 60 * 60);// 取得算出小时数后剩余的秒数
		}
		;
		if (EnumHasFlag(ymd, SGYmd.Minute)) {
			result.Minute = (int) (after / 60);// 计算整数小时数
			after -= (result.Minute * 60);// 取得算出小时数后剩余的秒数
		}
		;
		if (EnumHasFlag(ymd, SGYmd.Second)) {
			result.Second = (int) (after);// 计算整数秒数
			after -= result.Second;// 取得算出秒数后剩余的毫秒数
		}
		;
		if (EnumHasFlag(ymd, SGYmd.Millisecond)) {
			result.Millisecond = (int) (after);
		}
		;
		return result;
	}

	/**
	 * @param date1 需要比较的时间 不能为空(null),需要正确的日期格式
	 * @param date2 被比较的时间 为空(null)则为当前时间
	 * @param stype 返回值类型 0为多少天，1为多少个月，2为多少年
	 * @return
	 */
	public static int compareDate(Calendar c1, Calendar c2, int stype) {
		int n = 0;

		// String[] u = {"天","月","年"};
//        String formatStyle = stype==1?"yyyy-MM":"yyyy-MM-dd";  
//          
//        date2 = date2==null?DateTest.getCurrentDate():date2;  
//          
//        DateFormat df = new SimpleDateFormat(formatStyle);  
//        Calendar c1 = Calendar.getInstance();  
//        Calendar c2 = Calendar.getInstance();  
//        try {  
//            c1.setTime(df.parse(date1));  
//            c2.setTime(df.parse(date2));  
//        } catch (Exception e3) {  
//            System.out.println("wrong occured");  
//        }  
		// List list = new ArrayList();
		while (!c1.after(c2)) { // 循环对比，直到相等，n 就是所要的结果
			// list.add(df.format(c1.getTime())); // 这里可以把间隔的日期存到数组中 打印出来
			n++;
			if (stype == 1) {
				c1.add(Calendar.MONTH, 1); // 比较月份，月份+1
			} else {
				c1.add(Calendar.DATE, 1); // 比较天数，日期+1
			}
		}

		n = n - 1;

		if (stype == 2) {
			n = (int) n / 365;
		}

		// System.out.println(date1+" -- "+date2+" 相差多少"+u[stype]+":"+n);
		return n;
	}

	/**
	 * 暴露服务
	 *
	 * @param service 服务实现
	 * @param port    服务端口
	 * @throws Exception
	 */
	public static void RcpExport(final Object service, int port) throws Exception {
		if (service == null) {
			throw new IllegalArgumentException("service instance == null");
		}
		if (port <= 0 || port > 65535) {
			throw new IllegalArgumentException("Invalid port " + port);
		}
		System.out.println("Export service " + service.getClass().getName() + " on port " + port);
		// 建立Socket服务端
		@SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(port);
		for (;;) {
			try {
				// 监听Socket请求
				final Socket socket = server.accept();
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							try {
								/* 获取请求流，Server解析并获取请求 */
								// 构建对象输入流，从源中读取对象到程序中
								ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
								try {

									System.out.println("\nServer解析请求 ： ");
									String methodName = input.readUTF();
									System.out.println("methodName : " + methodName);
									// 泛型与数组是不兼容的，除了通配符作泛型参数以外
									Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
									System.out.println("parameterTypes : " + Arrays.toString(parameterTypes));
									Object[] arguments = (Object[]) input.readObject();
									System.out.println("arguments : " + Arrays.toString(arguments));

									/* Server 处理请求，进行响应 */
									ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
									try {
										// service类型为Object的(可以发布任何服务)，故只能通过反射调用处理请求
										// 反射调用，处理请求
										Method method = service.getClass().getMethod(methodName, parameterTypes);
										Object result = method.invoke(service, arguments);
										System.out.println("\nServer 处理并生成响应 ：");
										System.out.println("result : " + result);
										output.writeObject(result);
									} catch (Throwable t) {
										output.writeObject(t);
									} finally {
										output.close();
									}
								} finally {
									input.close();
								}
							} finally {
								socket.close();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 引用服务
	 *
	 * @param                <T> 接口泛型
	 * @param interfaceClass 接口类型
	 * @param host           服务器主机名
	 * @param port           服务器端口
	 * @return 远程服务，返回代理对象
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T RcpRefer(final Class<T> interfaceClass, final String host, final int port) throws Exception {
		if (interfaceClass == null) {
			throw new IllegalArgumentException("Interface class == null");
		}
		// JDK 动态代理的约束，只能实现对接口的代理
		if (!interfaceClass.isInterface()) {
			throw new IllegalArgumentException("The " + interfaceClass.getName() + " must be interface class!");
		}
		if (host == null || host.length() == 0) {
			throw new IllegalArgumentException("Host == null!");
		}
		if (port <= 0 || port > 65535) {
			throw new IllegalArgumentException("Invalid port " + port);
		}
		System.out.println("Get remote service " + interfaceClass.getName() + " from server " + host + ":" + port);

		// JDK 动态代理
		T proxy = (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] { interfaceClass },
				new InvocationHandler() {
					// invoke方法本意是对目标方法的增强，在这里用于发送RPC请求和接收响应
					@Override
					public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
						// 创建Socket客户端，并与服务端建立链接
						Socket socket = new Socket(host, port);
						try {
							/* 客户端像服务端进行请求，并将请求参数写入流中 */
							// 将对象写入到对象输出流，并将其发送到Socket流中去
							ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
							try {
								// 发送请求
								System.out.println("\nClient发送请求 ： ");
								output.writeUTF(method.getName());
								System.out.println("methodName : " + method.getName());
								output.writeObject(method.getParameterTypes());
								System.out.println("parameterTypes : " + Arrays.toString(method.getParameterTypes()));
								output.writeObject(arguments);
								System.out.println("arguments : " + Arrays.toString(arguments));

								/* 客户端读取并返回服务端的响应 */
								ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
								try {
									Object result = input.readObject();
									if (result instanceof Throwable) {
										throw (Throwable) result;
									}
									System.out.println("\nClient收到响应 ： ");
									System.out.println("result : " + result);
									return result;
								} finally {
									input.close();
								}
							} finally {
								output.close();
							}
						} finally {
							socket.close();
						}
					}
				});
		return proxy;
	}

	public static Boolean TryParseInt(Object s, SGRef<Integer> r) {
		if (s instanceof String) {
			r.SetValue(Integer.parseInt(ObjectToString(s)));
			return true;
		}
		return false;
	}

//   
//
	public static ISGConfigMapper _configMapper = null;

	public static String _baseDirectory = null;

	/**
	 * 旧方式,需要Spring框架
	 * 
	 * @return
	 */
	public static String GetBaseDirectoryBySpring() {
		if (_baseDirectory != null) {
			return _baseDirectory;
		}
		try {
			File path;
			path = new File(ResourceUtils.getURL("classpath:").getPath());
			if (!path.exists())
				path = new File("");
			// return path.toString();
			return path.toString().replace("\\target\\classes", "").replace("\\target\\test-classes", "")// 测试项目的编译目录
			;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			WriteError(e);
//			e.printStackTrace();
		}
		return null;
//	   System.out.println("path:"+path.getAbsolutePath());
	}

	/**
	 * 获得运行路径如
	 * D:\eclipse_workspace_sellgirlPay\sellgirlPay\PaySellgirl\sellgirlPayHelper
	 * 
	 * 此版本在linux上有 root/ 错误目录的问题 sgJavaHelper.GetBaseDirectoryAbsolutePath 是修正
	 * @return
	 */
	@Deprecated
	public static String GetBaseDirectory() {
		if (_baseDirectory != null) {
			return _baseDirectory;
		}
		try {
//			File path;
//			path = new File(ResourceUtils.getURL("classpath:").getPath());
//			if (!path.exists())
//				path = new File("");
//			// return path.toString();

			// 这种方式获得根目录有问题开发运行时为D:/eclipse_workspace_pfTransferTask/pfTransferTask
			// 但打包后为file:/D:/eclipse_release/pfTransferTask/pfTransferTask-0.0.1-SNAPSHOT.jar!/BOOT-INF/classes!
			// URL path =Thread.currentThread().getContextClassLoader().getResource("");

			String path = System.getProperty("user.dir");

			String r = path
					// .getPath()
					// .toString()
					.replace("\\target\\classes", "").replace("\\target\\test-classes", "")// 测试项目的编译目录
					.replace("/target/classes", "").replace("/target/test-classes", "");
			if (r.charAt(0) == '/') {
				r = r.substring(1);
			}
			if (r.charAt(r.length() - 1) == '/') {
				r = r.substring(0, r.length() - 1);
			}
			
//			/*ubuntu的root目录好像比较特别，和/home /user，这里会 返回 root/myapp/shop 这个路径打开文件会报错
//			应该把 root/xx替换为 ~*/
//			if(r.startsWith("root/")) {
//				r="/"+r;
//			}
			return r;
//			return path.toString().replace(Paths.get("target","classes").toString(), "").replace(Paths.get("target","test-classes").toString(), "")// 测试项目的编译目录
//			;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			WriteError(e);
//			e.printStackTrace();
		}
		return null;
//	   System.out.println("path:"+path.getAbsolutePath());
	}

//   public static string BaseDirectory
//   {
//       get
//       {
//           //return Path.Combine(AppDomain.CurrentDomain.BaseDirectory.Replace("\\bin\\Debug", ""));
//           return PathCombine(AppDomain.CurrentDomain.BaseDirectory.Replace("\\bin\\Debug", ""));
//       }
//   }
//
//   /// <summary>
//   /// 读取Web.config文件里的compilation节点的debug属性(发布后的项目里,这个属性应该设置为false)
//   /// </summary>
//   public static bool IsCompilationDebug
//   {
//       get
//       {
//           System.Web.Configuration.CompilationSection configSection = (System.Web.Configuration.CompilationSection)ConfigurationManager.GetSection("system.web/compilation");
//           return configSection.Debug;
//       }
//   }
	public static PFAppConfig GetAppConfig() {
		return _appConfig;
	}

	public static Boolean IsDebug() {
		return _appConfig.getPfDebug();
	}

	/// <summary>
	/// 测试时可以使用之前保存的本地数据
	/// </summary>
	public static Boolean UseLocalData() {
		return _appConfig.getUseLocalData();
	}

	public static Boolean PreventFgs() {
		return _appConfig.getPreventFgs();
	}

	/// <summary>
	/// 允许自动登陆(免登陆,分公司跨平台)
	/// </summary>
	public static Boolean AllowAutoLogin() {
		return _appConfig.getAllowAutoLogin();
	}

	public static long CheckMessageInterval() {
		return _appConfig.getCheckMessageInterval();
	}

	public static String GetNewUniqueHashId() {
		return UUID.randomUUID() + ObjectToDateString(Calendar.getInstance(), "yyyyMMddHHmmss");
	}

//
//   public static Encoding _encoding = Encoding.UTF8;
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static String DateFormat = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 月份格式 yyyy.MM
	 */
	public static String MonthFormat = "yyyy.MM";
	/**
	 * 日格式 yyyy-MM-dd
	 */
	public static String DayFormat = "yyyy-MM-dd";
	public static String YMFormat = "yyyyMM";

	/**
	 * 系统允许的最小时间(在sql的允许范围内),一般为了使Sql插入时不报错,可以把小于这个值的设成此值
	 */
	public static SGDate MinTime = new SGDate(1753, 1, 1, 0, 0, 0);
	/**
	 * 系统允许的最大时间(在sql的允许范围内),一般为了使Sql插入时不报错,可以把大于这个值的设成此值
	 */
	public static SGDate MaxTime = new SGDate(9999, 12, 31, 23, 59, 59);

//   #region Data
//
//   #region 随机数
//   public static List<T> ListRandomTake<T>(List<T> list, int num)
//   {
//       if (list == null || list.Count <= num) { return list; }
//       var result = new List<T>();
//       Random Rnd = new Random();
//       int lc = list.Count;
//       List<int> exist = new List<int>();
//       while (result.Count < num)
//       {
//           int i = Rnd.Next(0, lc);
//           if (!exist.Contains(i))
//           {
//               exist.Add(i);
//               result.Add(list[i]);
//           }
//       }
//       //for (int i = 0; i < num; i++) {
//       //    result.Add(ListRandomTakeOne(list));
//       //}
//       return result;
//   }
//   //private static T ListRandomTakeOne<T>(List<T> list)
//   //{
//   //    Random Rnd = new Random();
//   //    int i=Rnd.Next(0, list.Count);
//   //    return list[i];
//   //} 
//   #endregion
//
//   #region Date
	/// <summary>
	/// obj转为日期格式的字符串,如果转换不成功返回""
	/// </summary>
	/// <param name="obj"></param>
	/// <param name="format"></param>
	/// <returns></returns>
	public static String ObjectToDateString(Object obj, String format) {
		// if(format==null) {format="yyyy-MM-dd";}

		if (obj == null // || obj == DBNull.Value
		) {
			return "";
		}

//       if (obj instanceof Calendar && ((Calendar)obj).compareTo(Calendar.MaxValue.Date) == 0) { return ""; }

		if (obj instanceof Calendar) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(((Calendar) obj).getTime());
		} else if (obj instanceof SGDate) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(((SGDate) obj).ToCalendar().getTime());
		} else if (obj instanceof java.util.Date) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(((Date) obj).getTime());
		} else if (obj instanceof String) {
			return (String) obj;
		} else if (obj instanceof Integer)// 出生年份之类,数据库中有可能保存的是int格式如1988
		{
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				Date date = sdf.parse(obj.toString());
				return sdf.format(date);
			} catch (Exception e) {
				// WriteError(e);
			}
//           if (DateTime.TryParseExact(obj.ToString(), "yyyy", null, System.Globalization.DateTimeStyles.None, out s))
//           {
//               return s.ToString(format);
//           }
		}
		return "";
	}

	public static String ObjectToDateString(Object obj) {
		return ObjectToDateString(obj, "yyyy-MM-dd");
	}

//   /// <summary>
//   /// 转为大写日期,如二〇一八年一月五日
//   /// </summary>
//   /// <returns></returns>
//   public static string CMonthToCapitalDate(DateTime date)
//   {
//       //string result = "";
//       StringBuilder sb = new StringBuilder();
//       var year = date.Year.ToString();
//       var month = date.Month.ToString();
//       var day = date.Day.ToString();
//       foreach (char i in year)
//       {
//           sb.Append(CharToChinese(i));
//       }
//       sb.Append("年");
//       sb.Append(NumToChinese(month));
//       sb.Append("月");
//       sb.Append(NumToChinese(day));
//       sb.Append("日");
//       return sb.ToString();
//   }
//   /// <summary>
//   /// 
//   /// </summary>
//   /// <param name="cmonth">格式如:2019.03</param>
//   /// <returns></returns>
//   public static void CMonthToDateRange(string cmonth, out DateTime? start, out DateTime? end)
//   {
//       var year = int.Parse(cmonth.Substring(0, 4));
//       var month = int.Parse(cmonth.Substring(5, 2));
//       var date = new DateTime(year, month, 1);
//       start = date;
//       end = date.GetMonthEnd();
//   }
	public static String DateToCMonth(Calendar cmonth) {
		return ObjectToDateString(cmonth, MonthFormat);
	}

	public static String DateToCMonth(SGDate cmonth) {
		return ObjectToDateString(cmonth, MonthFormat);
	}

	public static String DateToYM(Calendar cmonth) {
		return ObjectToDateString(cmonth, YMFormat);
	}

	public static Calendar CMonthToDate(String cmonth) {
		int year = Integer.parseInt(cmonth.substring(0, 4));
		int month = Integer.parseInt(cmonth.substring(5, 7));
		return new SGDate(year, month, 1, 0, 0, 0).ToCalendar();
//       Calendar r =Calendar.getInstance();
//       r.set(year,month-1,1);
//       return r;
	}

	/// <summary>
	///
	/// </summary>
	/// <param name="cmonth"></param>
	/// <returns></returns>
	public static String CMonthAddMonths(String cmonth, int months) {
		if (cmonth == null) {
			return null;
		}
		Calendar d = SGDataHelper.CMonthToDate(cmonth);
		d.add(Calendar.MONTH, months);
		return DateToCMonth(d);
	}

//   /// <summary>
//   /// 最近一年的cmonth列表
//   /// </summary>
//   /// <returns></returns>
//   public static List<string> GetLastYearCMonthList()
//   {
//       var cmonthList = new List<string>();
//       var now = DateTime.Now;
//       for (int i = 0; i < 12; i++)
//       {
//           cmonthList.Add(now.ToString(MonthFormat));
//           now = now.AddMonths(-1);
//       }
//       return cmonthList;
//   }
//
//   public static string NumToChinese(string x, bool isCapitalization = false)
//   {
//       //数字转换为中文后的数组 //转载请注明来自 http://www.goubangwang.com
//       string[] P_array_num = isCapitalization ?
//           new string[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" } :
//           new string[] { "〇", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
//       //为数字位数建立一个位数组
//       string[] P_array_digit = isCapitalization ?
//           new string[] { "", "拾", "佰", "仟" } :
//           new string[] { "", "十", "百", "千" };
//       //为数字单位建立一个单位数组
//       string[] P_array_units = new string[] { "", "万", "亿", "万亿" };
//       string P_str_returnValue = ""; //返回值
//       int finger = 0; //字符位置指针
//       int P_int_m = x.Length % 4; //取模
//       int P_int_k = 0;
//       if (P_int_m > 0)
//           P_int_k = x.Length / 4 + 1;
//       else
//           P_int_k = x.Length / 4;
//       //外层循环,四位一组,每组最后加上单位: ",万亿,",",亿,",",万,"
//       for (int i = P_int_k; i > 0; i--)
//       {
//           int P_int_L = 4;
//           if (i == P_int_k && P_int_m != 0)
//               P_int_L = P_int_m;
//           //得到一组四位数
//           string four = x.Substring(finger, P_int_L);
//           int P_int_l = four.Length;
//           //内层循环在该组中的每一位数上循环
//           for (int j = 0; j < P_int_l; j++)
//           {
//               //处理组中的每一位数加上所在的位
//               int n = Convert.ToInt32(four.Substring(j, 1));
//               if (n == 0)
//               {
//                   if (j < P_int_l - 1 && Convert.ToInt32(four.Substring(j + 1, 1)) > 0 && !P_str_returnValue.EndsWith(P_array_num[n]))
//                       P_str_returnValue += P_array_num[n];
//               }
//               else
//               {
//                   if (!(n == 1 && (P_str_returnValue.EndsWith(P_array_num[0]) | P_str_returnValue.Length == 0) && j == P_int_l - 2))
//                       P_str_returnValue += P_array_num[n];
//                   P_str_returnValue += P_array_digit[P_int_l - j - 1];
//               }
//           }
//           finger += P_int_L;
//           //每组最后加上一个单位:",万,",",亿," 等
//           if (i < P_int_k) //如果不是最高位的一组
//           {
//               if (Convert.ToInt32(four) != 0)
//                   //如果所有4位不全是0则加上单位",万,",",亿,"等
//                   P_str_returnValue += P_array_units[i - 1];
//           }
//           else
//           {
//               //处理最高位的一组,最后必须加上单位
//               P_str_returnValue += P_array_units[i - 1];
//           }
//       }
//       return P_str_returnValue;
//   }
//   private static string CharToChinese(char c)
//   {
//       string result = "";
//       switch (c)
//       {
//           case '0':
//               result = "〇";
//               break;
//           case '1':
//               result = "一";
//               break;
//           case '2':
//               result = "二";
//               break;
//           case '3':
//               result = "三";
//               break;
//           case '4':
//               result = "四";
//               break;
//           case '5':
//               result = "五";
//               break;
//           case '6':
//               result = "六";
//               break;
//           case '7':
//               result = "七";
//               break;
//           case '8':
//               result = "八";
//               break;
//           case '9':
//               result = "九";
//               break;
//           default:
//               result = c.ToString();
//               break;
//       }
//       return result;
//   }
//   #endregion
//   /// <summary>
//   /// 千分位
//   /// </summary>
//   /// <param name="value"></param>
//   /// <returns></returns>
//   public static string Thousandth(object value)
//   {
//       if (value == null) { return ""; }
//       string strValue = value.ToString();
//       if (!PFDataHelper.StringIsNullOrWhiteSpace(strValue))
//       {
//           if (strValue.IndexOf('.') > -1)
//           {//有小数点时
//               return Regex.Replace(strValue, "(\\d)(?=(\\d{3})+\\.)", "$1,");
//           }
//           else
//           {
//               return Regex.Replace(strValue, "(\\d)(?=(\\d{3})+$)", "$1,");
//           }
//       }
//       return strValue;
//   }
	/// <summary>
	/// 千分位
	/// </summary>
	/// <param name="value"></param>
	/// <returns></returns>
	public static String Thousandth(Object value) {
		if (value == null) {
			return "";
		}
		String strValue = value.toString();
		if (!SGDataHelper.StringIsNullOrWhiteSpace(strValue)) {
			if (strValue.indexOf('.') > -1) {// 有小数点时
				strValue.replaceAll("(\\d)(?=(\\d{3})+\\.)", "$1,");
				// return Regex.Replace(strValue, "(\\d)(?=(\\d{3})+\\.)", "$1,");
			} else {
				strValue.replaceAll("(\\d)(?=(\\d{3})+$)", "$1,");
				// return Regex.Replace(strValue, "(\\d)(?=(\\d{3})+$)", "$1,");
			}
		}
		return strValue;
	}

//
//   public static bool StringIsNullOrWhiteSpace(object s)
//   {
//       if (s == null) { return true; }
//       if (s == DBNull.Value) { return true; }
//       return StringIsNullOrWhiteSpace(s.ToString());
//       //var ss = s.ToString();
//       //if (ss == "null") { return true; }
//       //return string.IsNullOrWhiteSpace(ss);
//   }
//   public static bool StringIsNullOrWhiteSpace(string s)
//   {
//       if (s == "null") { return true; }
//       if (s == "undefined") { return true; }
//       return string.IsNullOrEmpty(s) || s.Replace(" ", "") == "";
//   }
//
//   public static string StringFirstCharUpper(ref string s)
//   {
//       if (s == null) { return s; }
//       if (s.Length < 2) { return s.ToUpper(); }
//       return s[0].ToString().ToUpper() + s.Substring(1);
//   }
//
//
//   #region Enum方法
//   /// <summary>
//   /// enum转list(调用方法：PFDataHelper.EnumToKVList(FgsFunc.客单量);)
//   /// </summary>
//   /// <param name="valueEnum"></param>
//   /// <param name="useValue">是否使用Enum的Value作为Key</param>
//   /// <returns></returns>
//   public static List<KeyValuePair<object, object>> EnumToKVList(Enum valueEnum, bool useValue = false, bool showValueOnName = false)
//   {
//       var t = valueEnum.GetType();
//       return EnumToKVList(t, useValue, showValueOnName);
//       //return (from int value in Enum.GetValues(valueEnum.GetType())
//       //        select new KeyValuePair<object, object>(
//       //        useValue ? value.ToString() : Enum.GetName(valueEnum.GetType(), value),
//       //        Enum.GetName(valueEnum.GetType(), value))
//       //        ).ToList();
//   }
//   public static List<KeyValuePair<object, object>> EnumToKVList(Type enumType, bool useValue = false, bool showValueOnName = false)
//   {
//       Func<int, string> getName = (v) => {
//           string r = "";
//           r = Enum.GetName(enumType, v);
//           if (showValueOnName) { r = v + " " + r; }
//           return r;
//       };
//       return (from int value in Enum.GetValues(enumType)
//               select new KeyValuePair<object, object>(
//               useValue ? value.ToString() : getName(value),
//               getName(value))
//               ).ToList();
//   }
//   public static bool EnumHasFlag(Enum source, Enum target)
//
//   {
//       return ((source.GetHashCode() & target.GetHashCode()) == target.GetHashCode());
//   }
//   //public static List<KeyValuePair<object, object>> EnumFlagToArray(Enum valueEnum, bool useValue = false)
//   //{
//   //    var t = valueEnum.GetType();
//   //    foreach (int value in Enum.GetValues(t))
//   //    {
//   //        if(valueEnum.HasFlag())
//   //    }
//   //}
//   #endregion
//
//   private static int chfrom = Convert.ToInt32("4e00", 16); //中文:范围（0x4e00～0x9fff）转换成int（chfrom～chend）
//   private static int chend = Convert.ToInt32("9fff", 16);
//   public static PFCharType GetCharType(char c)
//   {
//       PFCharType result = PFCharType.Default;
//       int code = Char.ConvertToUtf32(c.ToString(), 0);
//       //var engChars="abcdefghijklmnopq"
//       if (code >= chfrom && code <= chend)//中文
//       {
//           result |= PFCharType.Chinese;
//       }
//       else if (c == 12288)//全角空格为12288，半角空格为32
//       {
//           result |= PFCharType.FullChar;
//       }
//       else if (c > 65280 && c < 65375)//其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
//       {
//           result |= PFCharType.FullChar;
//       }
//       else if (c == 32)//半角空格
//       {
//           result |= PFCharType.HalfChar;
//       }
//       else if (c > 32 && c < 127)//半角字符,如英文单引号
//       {
//           result |= PFCharType.HalfChar;
//       }
//       else if ((c >= 97 && c <= 122)//小写英文
//           || (c >= 65 && c <= 90)//大写英文
//           )
//       {
//           result |= PFCharType.English;
//       }
//       else if (c >= 48 && c <= 57)//数字
//       {
//           result |= PFCharType.Number;
//       }
//       else if (c == 9)//字符\t,数据库中占1
//       {
//           result |= PFCharType.HalfChar;
//       }
//       else if (c == 127)//特殊字符"方框",数据库中占1
//       {
//           result |= PFCharType.HalfChar;
//       }
//       else
//       {
//       }
//       return result;
//   }
//   //public static int GetWordsCharLengthOut(string words, out int englishCnt, out int cnCnt//, bool checkNull=true
//   //    )
//   //{
//   //    englishCnt = 0;
//   //    cnCnt = 0;
//   //    if (//checkNull&&
//   //        PFDataHelper.StringIsNullOrWhiteSpace(words)) { return 0; }
//   //    var english = Regex.Replace(words, "([\u4E00-\u9FA5\uf900-\ufa2d])", "");
//   //    englishCnt = english.Length;
//   //    cnCnt = words.Length - englishCnt;//余下的是中文数
//   //    return englishCnt + (cnCnt * 2);
//   //}
//   public static int GetWordsCharLengthOut(string words, out int englishCnt, out int cnCnt//, bool checkNull=true
//       )
//   {
//       var result = 0;
//       englishCnt = 0;
//       cnCnt = 0;
//
//       foreach (var i in words)
//       {
//           var ct = GetCharType(i);
//           if (ct == PFCharType.Chinese)
//           {
//               cnCnt += 1;
//               result += 2;
//           }
//           else if (ct == PFCharType.English)
//           {
//               englishCnt += 1;
//               result += 1;
//           }
//           else if (ct == PFCharType.Number)
//           {
//               englishCnt += 1;
//               result += 1;
//           }
//           else if (ct == PFCharType.FullChar)
//           {
//               cnCnt += 1;
//               result += 2;
//           }
//           else if (ct == PFCharType.HalfChar)
//           {
//               englishCnt += 1;
//               result += 1;
//           }
//           else
//           {
//               cnCnt += 1;
//               result += 2;
//           }
//       }
//       return result;
//   }
//   public static int GetWordsCharLength(string words//, bool checkNull=true
//       )
//   {
//       var englishCnt = 0;
//       var cnCnt = 0;
//       return GetWordsCharLengthOut(words, out englishCnt, out cnCnt);
//   }
//   /// <summary>
//   /// 设置字符串长度(截取,为了便于测试)
//   /// </summary>
//   /// <param name="words"></param>
//   /// <param name="englishCnt"></param>
//   /// <param name="cnCnt"></param>
//   /// <returns></returns>
//   public static string SetWordsCharLength(string words, int length
//       )
//   {
//       var result = "";
//       for (int i = 0; i < words.Length; i++)
//       {
//           var ct = GetCharType(words[i]);
//           if (ct == PFCharType.Chinese)
//           {
//               length -= 2;
//           }
//           else if (ct == PFCharType.English)
//           {
//               length -= 1;
//           }
//           else if (ct == PFCharType.Number)
//           {
//               length -= 1;
//           }
//           else if (ct == PFCharType.FullChar)
//           {
//               length -= 2;
//           }
//           else if (ct == PFCharType.HalfChar)
//           {
//               length -= 1;
//           }
//           else
//           {
//               length -= 2;
//           }
//
//           if (length < 0)
//           {
//               return result;
//           }
//           else
//           {
//               result += words[i];
//           }
//       }
//       return result;
//   }

//	private static char chfrom = 0x4e00; // 中文:范围（0x4e00～0x9fff）转换成int（chfrom～chend）
//	private static int chend = 0x9fff;

//	public static PFCharType GetCharType(char c) {
//		// Boolean aa = chfrom < chend;
//		// PFCharType result = PFCharType.Default;
////       int code = Char.ConvertToUtf32(c.ToString(), 0);
//		// var engChars="abcdefghijklmnopq"
//		if (c >= chfrom && c <= chend)// 中文
//		{
//			return PFCharType.Chinese;
//			// result.Or(PFCharType.Chinese); //这样有问题,改变了PFCharType.Default的值--benjamin todo
//		} else if (c == 12288)// 全角空格为12288，半角空格为32
//		{
//			return PFCharType.FullChar;
//			// result.Or(PFCharType.FullChar);
//		} else if (c > 65280 && c < 65375)// 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
//		{
//			return PFCharType.FullChar;
//			// result.Or(PFCharType.FullChar);
//		} else if (c == 32)// 半角空格
//		{
//			return PFCharType.HalfChar;
//			// result.Or(PFCharType.HalfChar);
//		} else if (c > 32 && c < 127)// 半角字符,如英文单引号
//		{
//			return PFCharType.HalfChar;
//			// result.Or(PFCharType.HalfChar);
//		} else if ((c >= 97 && c <= 122)// 小写英文
//				|| (c >= 65 && c <= 90)// 大写英文
//		) {
//			return PFCharType.English;
//			// result.Or(PFCharType.English);
//		} else if (c >= 48 && c <= 57)// 数字
//		{
//			return PFCharType.Number;
//			// result.Or(PFCharType.Number);
//		} else if (c == 9)// 字符\t,数据库中占1
//		{
//			return PFCharType.HalfChar;
//			// result.Or(PFCharType.HalfChar);
//		} else if (c == 127)// 特殊字符"方框",数据库中占1
//		{
//			return PFCharType.HalfChar;
//			// result.Or(PFCharType.HalfChar);
//		} else {
//		}
//		return PFCharType.Default;
////       return result;
//	}

//	// public static int GetWordsCharLengthOut(string words, out int englishCnt, out
//	// int cnCnt//, bool checkNull=true
//	// )
//	// {
//	// englishCnt = 0;
//	// cnCnt = 0;
//	// if (//checkNull&&
//	// PFDataHelper.StringIsNullOrWhiteSpace(words)) { return 0; }
//	// var english = Regex.Replace(words, "([\u4E00-\u9FA5\uf900-\ufa2d])", "");
//	// englishCnt = english.Length;
//	// cnCnt = words.Length - englishCnt;//余下的是中文数
//	// return englishCnt + (cnCnt * 2);
//	// }
//	public static int GetWordsCharLengthOut(String words, SGRef<Integer> englishCnt, SGRef<Integer> cnCnt// , bool
//																											// checkNull=true
//	) {
//		int result = 0;
////       englishCnt = 0;
////       cnCnt = 0;
//
//		// for (char i : words)
//		for (int idx = 0; idx < words.length(); idx++) {
//			char i = words.charAt(idx);
//			PFCharType ct = GetCharType(i);
//			if (PFCharType.Chinese.equals(ct)) {
//				cnCnt.SetValue(cnCnt.GetValue() + 1);
//				result += 2;
//			} else if (PFCharType.English.equals(ct)) {
//				englishCnt.SetValue(englishCnt.GetValue() + 1);
//				result += 1;
//			} else if (PFCharType.Number.equals(ct)) {
//				englishCnt.SetValue(englishCnt.GetValue() + 1);
//				result += 1;
//			} else if (PFCharType.FullChar.equals(ct)) {
//				cnCnt.SetValue(cnCnt.GetValue() + 1);
//				result += 2;
//			} else if (PFCharType.HalfChar.equals(ct)) {
//				englishCnt.SetValue(englishCnt.GetValue() + 1);
//				result += 1;
//			} else {
//				cnCnt.SetValue(cnCnt.GetValue() + 1);
//				result += 2;
//			}
//		}
//		return result;
//	}

//	public static int GetWordsCharLength(String words// , bool checkNull=true
//	) {
//		SGRef<Integer> englishCnt = new SGRef<Integer>(0);
//		SGRef<Integer> cnCnt = new SGRef<Integer>(0);
//		return GetWordsCharLengthOut(words, englishCnt, cnCnt);
//	}

//	/// <summary>
//	/// 设置字符串长度(截取,为了便于测试)
//	/// </summary>
//	/// <param name="words"></param>
//	/// <param name="englishCnt"></param>
//	/// <param name="cnCnt"></param>
//	/// <returns></returns>
//	public static String SetWordsCharLength(String words, int length) {
//		String result = "";
//		for (int i = 0; i < words.length(); i++) {
//			PFCharType ct = GetCharType(words.charAt(i));
//			if (PFCharType.Chinese.equals(ct)) {
//				length -= 2;
//			} else if (PFCharType.English.equals(ct)) {
//				length -= 1;
//			} else if (PFCharType.Number.equals(ct)) {
//				length -= 1;
//			} else if (PFCharType.FullChar.equals(ct)) {
//				length -= 2;
//			} else if (PFCharType.HalfChar.equals(ct)) {
//				length -= 1;
//			} else {
//				length -= 2;
//			}
//
//			if (length < 0) {
//				return result;
//			} else {
//				result += words.charAt(i);
//			}
//		}
//		return result;
//	}

//	public static Boolean StringHasChineseChar(String words) {
//		if (words == null) {
//			return false;
//		}
//		for (int i = 0; i < words.length(); i++) {
//			PFCharType ct = GetCharType(words.charAt(i));
//			if (ct == PFCharType.Chinese) {
//				return true;
//			}
//		}
//		return false;
//	}

//   /// 转全角的函数(SBC case)
//   ///
//   ///任意字符串
//   ///全角字符串
//   ///
//   ///全角空格为12288，半角空格为32
//   ///其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
//   ///
//   public static String ToSBC(String input)
//   {
//       // 半角转全角：
//       char[] c = input.ToCharArray();
//       for (int i = 0; i < c.Length; i++)
//       {
//           if (c[i] == 32)
//           {
//               c[i] = (char)12288;
//               continue;
//           }
//           if (c[i] < 127)
//               c[i] = (char)(c[i] + 65248);
//       }
//       return new String(c);
//   }
//
//   /**/
//   // /
//   // / 转半角的函数(DBC case)
//   // /
//   // /任意字符串
//   // /半角字符串
//   // /
//   // /全角空格为12288，半角空格为32
//   // /其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
//   // /
//   public static String ToDBC(String input)
//   {
//       char[] c = input.ToCharArray();
//       for (int i = 0; i < c.Length; i++)
//       {
//           if (c[i] == 12288)
//           {
//               c[i] = (char)32;
//               continue;
//           }
//           if (c[i] > 65280 && c[i] < 65375)
//               c[i] = (char)(c[i] - 65248);
//       }
//       return new String(c);
//   }
//	/// <summary>
//	/// 计算文字句子的宽度(主要用于计算表头宽度)
//	/// </summary>
//	/// <param name="words"></param>
//	/// <param name="fontSize">字体大小</param>
//	/// <param name="fontSpace">估计文字间隔</param>
//	/// <param name="paddingLR">默认0是因为有的地方在调用此方法的后面统一计算比较方便</param>
//	/// <returns></returns>
//	public static String GetWordsWidth(String words, Integer fontSizeIn, Integer fontSpaceIn, Integer paddingLRIn,
//			String fontWeight)// fontSpace = 2
//	{
//		// if (PFDataHelper.StringIsNullOrWhiteSpace(words)) { return null; }
//		Integer fontSize = fontSizeIn == null ? 12 : fontSizeIn.intValue();
//		Integer fontSpace = fontSpaceIn == null ? 0 : fontSpaceIn.intValue();
//		Integer paddingLR = paddingLRIn == null ? 0 : paddingLRIn.intValue();
////       int englishCnt = 0;
////       int cnCnt = 0;
//		SGRef<Integer> englishCnt = new SGRef<Integer>(0);
//		SGRef<Integer> cnCnt = new SGRef<Integer>(0);
//		GetWordsCharLengthOut(words, englishCnt, cnCnt);// , false);
//		double weightAddWidth = 0;
//		if (fontWeight != null)// 粗体字的情况下,bold所增加的px和fontSize成线性关系,k和b的值是通过线性回归得到的
//		{
//			double k = 0;
//			double b = 0;
//			if (fontWeight == "bold") {
//				k = 0.019530096957208705;
//				b = 0.017247150640138;
//			}
//			weightAddWidth = fontSize * k + b;
//			if (weightAddWidth < 1) {
//				weightAddWidth = 1;
//			} else {
//				weightAddWidth = Math.round(weightAddWidth);
//			}
//		}
//		DecimalFormat df = new DecimalFormat("0.##");
//		return df.format((fontSpace * (englishCnt.GetValue() + cnCnt.GetValue())) // 间隔长度
//				+ ((fontSize + weightAddWidth) * cnCnt.GetValue()) // 中文长度
//				+ ((fontSize / 2 + weightAddWidth) * englishCnt.GetValue()) // 英文长度
//				+ paddingLR) + "px";
//
//	}

//   /// <summary>
//   /// 计算年龄(如果要用DateTime参数,就需要区别?可空类型,使用时也很麻烦,直接用object比较方便)
//   /// </summary>
//   /// <param name="birth">出生日期</param>
//   /// <returns></returns>
//   public static int? GetAge(object birth)
//   {
//       if (birth is DateTime)
//       {
//           var dBirth = (DateTime)birth;
//           int age = DateTime.Now.Year - dBirth.Year;
//           if (DateTime.Now.Month < dBirth.Month || (DateTime.Now.Month == dBirth.Month && DateTime.Now.Day < dBirth.Day)) age--;
//           return age;
//       }
//       return null;
//   }
//   #region Object
//   public static decimal? ObjectToDecimal(object value)
//   {
//       if (value == null || value == DBNull.Value) { return null; }
//       decimal r = 0;
//       if (decimal.TryParse(value.ToString(), out r))
//       {
//           return r;
//       }
//       return null;
//   }
//   public static int? ObjectToInt(object value)
//   {
//       if (value == null || value == DBNull.Value) { return null; }
//       if (value is bool)
//       {
//           var b = (bool)value;
//           return b ? 1 : 0;
//       }
//       int r = 0;
//       if (int.TryParse(value.ToString(), out r))
//       {
//           return r;
//       }
//       return null;
//   }
	public static Long ObjectToLong(Object value)// long就是int64
	{
		if (value == null// || value == DBNull.Value
		) {
			return null;
		}
		if (value instanceof Boolean) {
			Boolean b = (Boolean) value;
			return b ? 1L : 0L;
		}
		// long r = 0;
		if (value instanceof Double) {
			return ((Double) value).longValue();
//    	   Long.valueOf(String.valueOf(data))
//           if (long.TryParse(((Double)value).toString("0"), out r))//double小数位数过多会报错--benjamin20190927
//           {
//               return r;
//           }
		}
		return Long.valueOf(String.valueOf(value));
//       if (long.TryParse(value.ToString(), out r))
//       {
//           return r;
//       }
		// return null;
	}
//   public static double? ObjectToDouble(object value)
//   {
//       if (value == null || value == DBNull.Value) { return null; }
//       if (value is bool)
//       {
//           var b = (bool)value;
//           return b ? 1 : 0;
//       }
//       double r = 0;
//       if (double.TryParse(value.ToString(), out r))
//       {
//           return r;
//       }
//       return null;
//   }
//   public static bool? ObjectToBool(object value)
//   {
//       if (value == null || value == DBNull.Value) { return null; }
//       bool r = false;
//       var vs = value.ToString();
//       if (vs == "1") { return true; }//"1"的话tryParse是失败的
//       if (bool.TryParse(vs, out r))
//       {
//           return r;
//       }
//       if (value.ToString() == "true,false") { return true; }//mvc的Checkbox跟着一个hidden,如果选中时,传回来的是"true,false"
//       return null;
//   }
//   public static DateTime? ObjectToDateTime(object value)
//   {
//       if (value == null || value == DBNull.Value) { return null; }
//       DateTime r;
//       if (DateTime.TryParse(value.ToString(), out r))
//       {
//           return r;
//       }
//       return null;
//   }
//   public static TEnum? ObjectToEnum<TEnum>(object value)
//       where TEnum : struct
//   {
//       if (value == null || value == DBNull.Value) { return null; }
//       TEnum r;
//       try
//       {
//           r = (TEnum)Enum.Parse(typeof(TEnum), value.ToString());
//           return r;
//       }
//       catch (Exception e)
//       {
//           return null;
//       }
//       //if (Enum.TryParse(value.ToString(), out r))
//       //{
//       //    return r;
//       //}
//       //return null;
//   }
//   public static Object ConvertObjectByType(object value, Type srcType, Type dstType)
//   {
//       //当把DataTable转为LocalJson再读为DataTable时会出现这些情况,如"查网络图"里的ch字段,以后再考虑怎么排除此问题
//       if (dstType == typeof(decimal?) || dstType == typeof(decimal)
//      )
//       {
//           return ObjectToDecimal(value);
//       }
//       //else if ((dstType == typeof(Int32?) || dstType == typeof(Int32))
//       //&& srcType == typeof(Int64)
//       //)
//       //{
//       //    return Convert.ToInt32(value);
//       //}
//       else if (dstType == typeof(Int32?) || dstType == typeof(Int32)
//       )
//       {
//           return PFDataHelper.ObjectToInt(value);
//       }
//       else if (srcType == typeof(SByte) && (dstType == typeof(bool?) || dstType == typeof(bool))//mysql中读出来的bool类型是SBype的
//       )
//       {
//           var sb = (SByte)value;
//           if (sb == 1) { return true; }
//           if (sb == 0) { return false; }
//           return null;
//       }
//       else if (srcType == typeof(decimal) && (dstType == typeof(Int64?) || dstType == typeof(Int64))//mysql中读出来的bigint类型是decimal的?如us_user.id列--benjamin20190730
//       )
//       {
//           return ObjectToLong(value);
//       }
//       else if (//(srcType == typeof(string)|| (srcType == typeof(MySqlDateTime)) && 
//           (dstType == typeof(DateTime?) || dstType == typeof(DateTime))//mysql中if(c.card_time<'1753-01-01 00:00:00','1753-01-01 00:00:00',c.card_time) as fjoindate得到的竟然是string格式,原因不明
//       )
//       {
//           return ObjectToDateTime(value);
//       }
//       else if(srcType == typeof(string) && dstType == typeof(string[]))
//       {
//           return (value as string).Split(new[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
//       }
//       else
//       {
//           return value;
//       }
//
//   }
//   #endregion Object
//   /// <summary>
//   /// 同比
//   /// </summary>
//   /// <param name="thisYear"></param>
//   /// <param name="lastYear"></param>
//   /// <returns></returns>
//   public static string GetYearOnYear(decimal? thisYear, decimal? lastYear)
//   {
//       //if(thisYear==null|| lastYear == null) { return null; }
//       //if (thisYear == 0 || lastYear == 0) { return 0; }
//       if (lastYear == null || lastYear == 0) { return null; }
//       var r = (thisYear - lastYear) * 100 / lastYear;
//       return r == null ? null : System.Decimal.Round(r.Value, 2).ToString() + " %";
//   }
//   /// <summary>
//   /// 环比
//   /// </summary>
//   /// <param name="thisYear"></param>
//   /// <param name="lastYear"></param>
//   /// <returns></returns>
//   public static string GetRingRatio(decimal? thisMonth, decimal? lastMonth)
//   {
//       //if(thisYear==null|| lastYear == null) { return null; }
//       //if (thisYear == 0 || lastYear == 0) { return 0; }
//       if (lastMonth == null || lastMonth == 0) { return null; }
//       var r = (thisMonth - lastMonth) * 100 / lastMonth;
//       return r == null ? null : System.Decimal.Round(r.Value, 2).ToString() + " %";
//   }
//
//   /// <summary>
//   /// 计划完成率
//   /// </summary>
//   /// <param name="thisYear"></param>
//   /// <param name="lastYear"></param>
//   /// <returns></returns>
//   public static string GetPlanCompletionRate(decimal? actual, decimal? plan)
//   {
//       if (plan == null || plan == 0) { return null; }
//       var r = (actual / plan) * 100;
//       return r == null ? null : System.Decimal.Round(r.Value, 2).ToString() + " %";
//   }
//
//   /// <summary>
//   /// NameValueCollection转T类型
//   /// </summary>
//   /// <typeparam name="T"></typeparam>
//   /// <param name="c"></param>
//   /// <returns></returns>
//   public static T NameValueCollectionToModel<T>(this NameValueCollection c)
//       where T : new()
//   {
//       var model = new T();
//       if (c != null)
//       {
//           var t = typeof(T);
//           foreach (var name in c.AllKeys)
//           {
//               if (name != "operator")
//               {
//                   var v = c[name];
//                   if (!PFDataHelper.StringIsNullOrWhiteSpace(v))
//                   {
//                       try
//                       {
//                           PropertyInfo p = null;
//                           p = t.GetProperty(name);
//                           var vObj = Convert.ChangeType(v, GetPropertyType(p));
//                           if (p != null) { p.SetValue(model, vObj, null); }
//                       }
//                       catch (Exception e)//有的字段用了系统关键字会报错,如operator
//                       {
//                           WriteError(e);
//                       }
//                   }
//               }
//           }
//       }
//       return model;
//   }
//   #endregion Data
//
//   #region DataTable
//   /// <summary>
//   /// KVP比SelectList更容易操作item
//   /// </summary>
//   /// <param name="dt"></param>
//   /// <param name="dataValueField"></param>
//   /// <param name="dataTextField"></param>
//   /// <returns></returns>
//   public static List<Map<Object, Object>> DataTableToKVList(this PFDataTable dt, string dataValueField, string dataTextField)
//   {
//       //创建返回的集合
//       List<KeyValuePair<object, object>> oblist = new List<KeyValuePair<object, object>>();
//       if (dt != null)
//       {
//           foreach (DataRow row in dt.Rows)
//           {
//               //创建TResult的实例
//               //KeyValuePair<string, string> ob = new KeyValuePair<string, string>((row[dataValueField]??"").ToString(), (row[dataValueField]??"").ToString());
//               KeyValuePair<object, object> ob = new KeyValuePair<object, object>(row[dataValueField], row[dataTextField]);
//               oblist.Add(ob);
//           }
//       }
//       return oblist;
//   }

	/// <summary>
	/// DataTable 转换为List 集合
	/// </summary>
	/// <typeparam name="TResult">类型</typeparam>
	/// <param name="dt">DataTable</param>
	/// <param name="eachRow">(o,dr)=></param>
	/// <returns></returns>
	public static <T> List<T> DataTableToList(SGDataTable dt, Class<T> cls, SGAction<T, PFDataRow, Object> eachRow) {
		return dt.ToList(cls, eachRow);
	}
//   /// <summary>
//   /// 转为字典(MVC直接返回DataTable不能序列化,可转为字典返回)
//   /// </summary>
//   /// <param name="dt"></param>
//   /// <returns></returns>
//   public static List<Dictionary<string, object>> DataTableToDictList(this DataTable dt, bool useStringValue = true)
//   {
//       //创建返回的集合
//       List<Dictionary<string, object>> arrayList = new List<Dictionary<string, object>>();
//       var columns = dt.Columns;
//       foreach (DataRow dataRow in dt.Rows)
//       {
//           Dictionary<string, object> dictionary = new Dictionary<string, object>();
//           foreach (DataColumn dataColumn in columns)
//           {
//               dictionary.Add(dataColumn.ColumnName.IndexOf(".") > -1 ? dataColumn.ColumnName.Replace(".", "_") : dataColumn.ColumnName
//                   , useStringValue ? dataRow[dataColumn.ColumnName].ToString() : dataRow[dataColumn.ColumnName]);
//           }
//           arrayList.Add(dictionary);
//       }
//       return arrayList;
//   }
//
//   /// <summary>
//   /// 转为字典
//   /// </summary>
//   /// <param name="dt"></param>
//   /// <returns></returns>
//   public static Dictionary<string, object> DataTableToDict(this DataTable dt, string keyField, string valueField)
//   {
//       //创建返回的集合
//       Dictionary<string, object> arrayList = new Dictionary<string, object>();
//       //try
//       //{
//       var key = "";
//       var columns = dt.Columns;
//       object value;
//
//       foreach (DataRow dataRow in dt.Rows)
//       {
//           key = (dataRow[keyField] ?? "").ToString();
//           value = dataRow[valueField];
//           if (!arrayList.ContainsKey(key))
//           {
//               arrayList.Add(key, dataRow[valueField]);
//           }
//           else
//           {
//               arrayList[key] += ("," + value);
//           }
//       }
//
//       return arrayList;
//   }
//   public static Map<String, Map<String, Object>> DataTableToRowDict(PFDataTable dt, String keyField, params String[] valueField)
//   {
//       //创建返回的集合
//       Map<String, Map<String, object>> arrayList = new Map<String, Map<String, object>>();
//       //try
//       //{
//       var key = "";
//       var columns = dt.Columns;
//       object value;
//
//       for (PFDataRow dataRow : dt.Rows)
//       {
//           key = (dataRow[keyField] ?? "").ToString();
//           //value = dataRow;
//           if (!arrayList.ContainsKey(key))
//           {
//               arrayList.Add(key, new Map<String, object>());
//           }
//           foreach (String vf in valueField)
//           {
//               value = dataRow[vf];
//               if (!arrayList[key].ContainsKey(vf))
//               {
//                   arrayList[key].Add(vf, value);
//               }
//               else
//               {
//                   arrayList[key][vf] += ("," + value);
//               }
//           }
//       }
//
//       return arrayList;
//   }
//   /// <summary>
//   /// 转化一个DataTable
//   /// </summary>
//   /// <typeparam name="T"></typeparam>
//   /// <param name="list"></param>
//   /// <returns></returns>
//   public static DataTable ListToDataTable<T>(this IEnumerable<T> list)
//   {
//       //创建属性的集合
//       List<PropertyInfo> pList = new List<PropertyInfo>();
//       //获得反射的入口
//       Type type = typeof(T);
//       DataTable dt = new DataTable();
//       //把所有的public属性加入到集合 并添加DataTable的列
//       //Array.ForEach<PropertyInfo>(type.GetProperties(), p => { pList.Add(p); dt.Columns.Add(p.Name, p.PropertyType); });
//       var aa = type.GetProperties().Where(p => p.PropertyType.IsPublic && p.CanRead
//       //&& p.CanWrite //CanRead已经够了--benjamin20190802
//       ).ToArray();
//       Array.ForEach<PropertyInfo>(aa, p => { pList.Add(p); dt.Columns.Add(p.Name, GetPropertyType(p)); });
//       foreach (var item in list)
//       {
//           //创建一个DataRow实例
//           DataRow row = dt.NewRow();
//           //给row 赋值
//           pList.ForEach(p => {
//               var v = p.GetValue(item, null);
//               if (v == null)
//               {
//                   row[p.Name] = DBNull.Value;
//               }
//               else
//               {
//                   row[p.Name] = v;
//               }
//           });
//           //加入到DataTable
//           dt.Rows.Add(row);
//       }
//       return dt;
//   }

	// #region Map
//   public static <T> Map<String,Object> ListToMap(List<T> list,ToDoubleFunction<? super T> mapper)
	public static <T> Map<String, Object> ListToMap(List<T> list, Function<T, String> keyAction,
			Function<T, Object> valueAction) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		for (int i = 0; i < list.size(); i++) {
			result.put(keyAction.apply(list.get(i)), valueAction.apply(list.get(i)));
		}
		return result;

	}

	public static <T, TK, TR> Map<TK, TR> ListToMapT(List<T> list, Function<T, TK> keyAction,
			Function<T, TR> valueAction) {
		Map<TK, TR> result = new LinkedHashMap<TK, TR>();
		for (int i = 0; i < list.size(); i++) {
			result.put(keyAction.apply(list.get(i)), valueAction.apply(list.get(i)));
		}
		return result;

	}

	/*
	 * 似乎可以用 list.stream().map(num -> new xx())来代替此方法
	 */
	public static <T, TReturn> List<TReturn> ListSelect(List<T> list, Function<T, TReturn> selectAction) {
		List<TReturn> result = new ArrayList<TReturn>();
		for (int i = 0; i < list.size(); i++) {
			result.add(selectAction.apply(list.get(i)));
		}
		return result;
	}

	public static <T> T ListFirst(List<T> list, Function<T, Boolean> matchAction) {
		return ListFirstOrDefault(list, matchAction);
	}

	public static <T> T ListFirstOrDefault(List<T> list, Function<T, Boolean> matchAction) {
		for (int i = 0; i < list.size(); i++) {
			if (matchAction.apply(list.get(i))) {
				return list.get(i);
			}
		}
		return null;
	}

	public static <T> List<T> ListWhere(List<T> list, Function<T, Boolean> selectAction) {
		List<T> result = new ArrayList<T>();
		for (T i : list) {
			if (selectAction.apply(i)) {
				result.add(i);
			}
		}
		return result;
	}

	public static <T> Boolean ListAny(List<T> list, Function<T, Boolean> matchAction) {
		for (int i = 0; i < list.size(); i++) {
			if (matchAction.apply(list.get(i))) {
				return true;
			}
		}
		return false;
	}

	/*
	 * C#中字典和List均是Enumberable,可以 共用方法,但java里用Map代替字典的话,Map和List没有统一接口
	 */
	public static <T1, T2, TReturn> List<TReturn> MapSelect(Map<T1, T2> map, BiFunction<T1, T2, TReturn> selectAction) {
		List<TReturn> result = new ArrayList<TReturn>();

		Iterator<T1> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			T1 key = iter.next();
			T2 value = map.get(key);
			result.add(selectAction.apply(key, value));
			// System.out.println(key+" "+value);
		}
		return result;

	}

	public static <T1, T2> Boolean MapAny(Map<T1, T2> map, Function<Entry<T1, T2>, Boolean> selectAction) {
		Iterator<Entry<T1, T2>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<T1, T2> key = iter.next();
			if (selectAction.apply(key)) {
				return true;
			}
		}
		return false;

	}

	public static <T> T[] ArrayWhere(Class<T> cls, T[] list, Function<T, Boolean> selectAction) {
		List<T> result = new ArrayList<T>();
		for (T i : list) {
			if (selectAction.apply(i)) {
				result.add(i);
			}
		}
		return result.toArray(SGDataHelper.<T[]>ObjectAs(Array.newInstance(cls, result.size())));
	}

	public static <T> Boolean ArrayAny(T[] list, Function<T, Boolean> matchAction) {
		for (T i : list) {
			if (matchAction.apply(i)) {
				return true;
			}
		}
		return false;
	}

	public static <T, TReturn> TReturn[] ArraySelect(Class<TReturn> cls, T[] list, Function<T, TReturn> selectAction) {
		List<TReturn> result = new ArrayList<TReturn>();
		for (T i : list) {
			result.add(selectAction.apply(i));
		}
//		for (int i = 0; i < list.size(); i++) {
//			result.add(selectAction.apply(list.get(i)));
//		}
		// return result.toArray(PFDataHelper.ObjectAs(Array.newInstance(cls,
		// result.size())));
		return result.toArray(SGDataHelper.<TReturn[]>ObjectAs(Array.newInstance(cls, result.size())));
	}

	/**
	 * Map转成实体对象
	 * 
	 * @param map   map实体对象包含属性
	 * @param clazz 实体对象类型
	 * @return
	 */
	public static Object map2Object(Map<String, Object> map, Class<?> clazz) {
		if (map == null) {
			return null;
		}
		Object obj = null;
		try {
			obj = clazz.newInstance();
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				int mod = field.getModifiers();
				if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
					continue;
				}
				field.setAccessible(true);
				field.set(obj, map.get(field.getName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

//	/**
//	 * 为了解决 (Map<String, Object>)model 强转换总有警告的问题
//	 * 这方法的效率比较低,按map的size来算的话  14秒/2e7行
//	 * @param obj
//	 * @param keyCls
//	 * @param valueCls
//	 * @return
//	 */
//	public static <T1, T2> Map<T1, T2> ObjectAsMap2(Object obj, Class<T1> keyCls,Class<T2> valueCls) {
//		Map<T1, T2> r=new HashMap<T1, T2>();
//        if (obj instanceof Map<?,?>)
//        {
//        	Map<?, ?> modelDict = (Map<?, ?>)obj;
//
//     	   Iterator<?> iter = modelDict.entrySet().iterator();
//     	   while(iter.hasNext()){
//     		   Object key= iter.next();
//     		   if(key instanceof Entry<?,?>) {
//     			  Entry<?, ?> keyEntry = (Entry<?, ?>)key;
//     			  T1 k=keyCls.cast(keyEntry.getKey());
//     			  T2 v=valueCls.cast(keyEntry.getValue());
//     			  r.put(k,v);
//     		   }
//     	   }
//        }		
//		return r;
//	}
//	//这种方式不知道怎么调用才行
//	public static <T1, T2> Map<T1,T2> ObjectAsMap2(Object obj, Class<Map<T1,T2>> cls) {
//		//return cls.GetValue().getClass().cast(obj);
//		return cls.cast(obj);
//	}
	/**
	 * 效率高 这种方式也有不好的地方,因为cls参数是需要实例,所以如果map是instanceof判断出来的话,map可能有几种的话,那此方法就不好用了
	 * 
	 * @param obj
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T1, T2> Map<T1, T2> ObjectAsMap2(Object obj, Map<T1, T2> cls) {
		// return cls.GetValue().getClass().cast(obj);
		if (cls != null) {
			return cls.getClass().cast(obj);
		}
		return null;
	}

	// @SuppressWarnings(value = { })
	public static <T1, T2> Map<T1, T2> ObjectAsMap(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Map) {
			return ObjectAs(obj);
			// return (new HashMap<T1,T2>()).getClass().cast(obj);
		}
//		if(obj instanceof HashMap){
//			return ObjectAs(obj);
//			//return (new HashMap<T1,T2>()).getClass().cast(obj);
//		}
//		if(obj instanceof LinkedHashMap){
//			return ObjectAs(obj);
//			//return (new LinkedHashMap<T1,T2>()).getClass().cast(obj);
//		}
////		if(obj instanceof TreeMap){
////			return (new TreeMap<T1,T2>()).getClass().cast(obj);
////		}
		return null;
	}

	/**
	 * 用此方法代替强制转换的话,就不会有警告了 用法:PFDataHelper.ObjectAs(map,new
	 * HashMap<String,Object>())
	 * 
	 * @param obj
	 * @param valueCls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T1> T1 ObjectAs(Object obj) {
		if (obj == null) {
			return null;
		}
		return (T1) obj;
	}
//	@SuppressWarnings("unchecked")
//	public static <T1> T1 ObjectAs3(Object obj,T1 valueCls) {
//		if(obj==null) {return null;}
//		return (T1)valueCls.getClass().cast(obj);
//	}

	// #endregion Map

	public static SGDataTable DictListToDataTable(List<Map<String, Object>> list) {
		if (list == null || list.size() < 1) {
			return null;
		}
		Map<String, Class<?>> columnType = new HashMap<String, Class<?>>();
		SGDataTable dt = new SGDataTable();

		Iterator<Entry<String, Object>> iter = list.get(0).entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Object> key = iter.next();
			Object value = key.getValue();
//				if(value!=null) {
//					
//				}
			dt.Columns.add(key.getKey(), value == null ? null : value.getClass());
			if (value != null) {
				columnType.put(key.getKey(), value.getClass());
			}
		}

		for (Map<String, Object> dataRow : list) {
			// 创建一个PFDataRow实例
			PFDataRow row = dt.NewRow();
			// Map<String, Object> dictionary = new Map<String, Object>();

			iter = dataRow.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Object> key = iter.next();
				Object value = key.getValue();

				if (columnType.containsKey(key.getKey())) {

				} else if (value != null // && value != DBNull.Value
				) {
					columnType.put(key.getKey(), value.getClass());
					// dt.Columns.add(key.getKey(), columnType.get(key));
					dt.Columns.get(key.getKey()).setDataType(value.getClass());
				}
				if (columnType.containsKey(key.getKey())) {
					row.set(key.getKey(), value == null ? "" : value);// DBNull.Value;
				}
			}

			// 加入到PFDataTable
			dt.Rows.add(row);
		}
		return dt;
	}

	public static BigDecimal ColumnTotal(SGDataTable mydt, String colname) {
		// var t = typeof(T);
		// T result = (T)Activator.CreateInstance(t); ;
		BigDecimal result = new BigDecimal(0);
//        int ColIndex = mydt.Columns.get(colname).Ordinal;
//        if (ColIndex >= 0 && ColIndex < mydt.Columns.Count)
//        {
//            for (int i = 0; i < mydt.Rows.Count; i++)
//            {
//                var t = mydt.Rows[i][ColIndex].GetType();
//                if (t != typeof(DBNull))
//                {
//                    if (t == typeof(decimal)) { result += Convert.ToDecimal(mydt.Rows[i][ColIndex]); }
//                    else if (t == typeof(int)) { result += Convert.ToInt32(mydt.Rows[i][ColIndex]); }
//                }
//            }
//        }
		for (int i = 0; i < mydt.Rows.size(); i++) {
			Object v = mydt.Rows.get(i).getColumn(colname);
			if (v != null) {
				if (v instanceof Integer) {
					result = result.add(new BigDecimal((Integer) v));
				} else if (v instanceof BigDecimal) {
					result = result.add((BigDecimal) v);
				}
			}
		}
		return result;
	}

//
//   //public static int ColumnTotal(DataTable mydt, string colname)
//   //{
//   //    int result = 0;
//   //    int ColIndex = mydt.Columns[colname].Ordinal;
//   //    if (ColIndex >= 0 && ColIndex < mydt.Columns.Count)
//   //    {
//   //        for (int i = 0; i < mydt.Rows.Count; i++)
//   //        {
//   //            if (mydt.Rows[i][ColIndex].GetType() != typeof(DBNull))
//   //            {
//   //                result += Convert.ToInt32(mydt.Rows[i][ColIndex]);
//   //            }
//   //        }
//   //    }
//   //    return result;
//   //}
//   public static decimal ColumnTotal(DataTable mydt, string colname)
//   {
//       //var t = typeof(T);
//       //T result = (T)Activator.CreateInstance(t); ;
//       decimal result = 0;
//       int ColIndex = mydt.Columns[colname].Ordinal;
//       if (ColIndex >= 0 && ColIndex < mydt.Columns.Count)
//       {
//           for (int i = 0; i < mydt.Rows.Count; i++)
//           {
//               var t = mydt.Rows[i][ColIndex].GetType();
//               if (t != typeof(DBNull))
//               {
//                   if (t == typeof(decimal)) { result += Convert.ToDecimal(mydt.Rows[i][ColIndex]); }
//                   else if (t == typeof(int)) { result += Convert.ToInt32(mydt.Rows[i][ColIndex]); }
//               }
//           }
//       }
//       return result;
//   }
//
//   public static string DataTableToString(DataTable dt, string split)
//   {
//       StringBuilder sb = new StringBuilder();
//       if (dt != null && dt.Rows.Count > 0)
//       {
//           int colInt = dt.Columns.Count;
//           foreach (DataRow row in dt.Rows)
//           {
//               for (int i = 0; i < colInt; i++)
//               {
//                   sb.Append(row[i].ToString() + split);
//               }
//           }
//           string str = sb.ToString();
//           return str.Substring(0, str.Length - 1);
//       }
//       else
//       {
//           return string.Empty;
//       }
//   }

	public static SGDataTable DataTableFilter(SGDataTable dt, String filterValue) {
		if (StringIsNullOrWhiteSpace(filterValue)) {
			return dt;
		}
		filterValue = filterValue.toLowerCase();
		String[] arrFillter = filterValue.split(",");
		SGDataTable newdt = new SGDataTable();
		for (PFDataColumn dcol : dt.Columns) {
//        	PFDataColumn dc = new PFDataColumn();
//            dc.DataType = dcol.DataType;
//            dc.ColumnName = dcol.ColumnName;
//            dc.DefaultValue = dcol.DefaultValue;
			PFDataColumn dc = new PFDataColumn(dcol.getKey(), dcol.getClass());
			newdt.Columns.add(dc);
		}
		int rowStart = 0;
		int rowEnd = dt.Rows.size() - 1;
		for (int i = rowStart; i <= rowEnd; i++) {
			PFDataRow row = dt.Rows.get(i);
			if (row.getCol() != null && IsFilterFitRow(row, arrFillter)) {
				PFDataRow dr = newdt.NewRow();
				dr.setCol(row.getCol());
				newdt.Rows.add(dr);
			}
		}
		return newdt;
	}

	public static Boolean IsFilterFitRow(PFDataRow row, String[] arr) {
		if (row.getCol() == null) {
			return false;
		}
		for (String i : arr) {
			if (StringIsNullOrWhiteSpace(i)) {
				continue;
			}
			if (
//            		!row.ItemArray.Any(a =>
//                 a == null ? false : a.ToString().ToLower() == i)
//            	!PFDataHelper.ListAny(row.getCol(), a->a == null ? false : a.toString().toLowerCase().equals(i))
			!SGDataHelper.ListAny(row.getCol(), a -> (a == null || a.getValue() == null) ? false
					: a.getValue().toString().toLowerCase().equals(i))) {
				return false;
			}
		}
		return true;
	}

//   public static DataTable DataTableFilter(this DataTable dt, string filterValue)
//   {
//       if (StringIsNullOrWhiteSpace(filterValue)) { return dt; }
//       filterValue = filterValue.ToLower();
//       var arrFillter = filterValue.Split(',');
//       DataTable newdt = new DataTable();
//       foreach (System.Data.DataColumn dcol in dt.Columns)
//       {
//           DataColumn dc = new DataColumn();
//           dc.DataType = dcol.DataType;
//           dc.ColumnName = dcol.ColumnName;
//           dc.DefaultValue = dcol.DefaultValue;
//           newdt.Columns.Add(dc);
//       }
//       var rowStart = 0;
//       var rowEnd = dt.Rows.Count - 1;
//       for (int i = rowStart; i <= rowEnd; i++)
//       {
//           if (dt.Rows[i].ItemArray != null
//               //&& dt.Rows[i].ItemArray.Any(a =>
//               //a==null?false:a.ToString().ToLower()==filterValue)
//               //&& dt.Rows[i].ItemArray.Any(a =>
//               //a == null ? false : arrFillter.Contains(a.ToString().ToLower()))
//               && IsFilterFitRow(dt.Rows[i], arrFillter)
//               )
//           {
//               DataRow dr = newdt.NewRow();
//               dr.ItemArray = dt.Rows[i].ItemArray;
//               newdt.Rows.Add(dr);
//           }
//       }
//       return newdt;
//   }
//   public static bool IsFilterFitRow(DataRow row, string[] arr)
//   {
//       if (row.ItemArray == null) { return false; }
//       foreach (var i in arr)
//       {
//           if (StringIsNullOrWhiteSpace(i)) { continue; }
//           if (!row.ItemArray.Any(a =>
//                a == null ? false : a.ToString().ToLower() == i))
//           {
//               return false;
//           }
//       }
//       return true;
//   }
	public static SGDataTable DataPager(SGDataTable dt, int pageIndex, int pageSize) {
		SGRef<Integer> pageCount = new SGRef<Integer>(0);
		return PFDataTablePaging(dt, pageIndex, pageSize, pageCount);
	}

	private static SGDataTable PFDataTablePaging(SGDataTable dt, int pageIndex, int pageSize,
			SGRef<Integer> pageCount) {
		if (dt == null || dt.Rows.size() <= 0) {
			pageCount.SetValue(-1);
			return null;
		}
		int recordCount = dt.Rows.size();
		int _pageCount2 = recordCount / pageSize;

		if (pageIndex < 0)
			pageIndex = 0;
		else if (pageIndex > _pageCount2)
			pageIndex = _pageCount2;

		SGDataTable newdt = new SGDataTable();
		for (PFDataColumn dcol : dt.Columns) {
			PFDataColumn dc = new PFDataColumn(dcol.getKey(), dcol.getDataType());
//            dc.DataType = dcol.DataType;
//            dc.ColumnName = dcol.ColumnName;
//            dc.DefaultValue = dcol.DefaultValue;
			newdt.Columns.add(dc);
		}
		int lastPageRecordCount = recordCount % pageSize;
		int rowStart = pageIndex * pageSize;
		int rowEnd;

		if (pageIndex == _pageCount2 && lastPageRecordCount != 0) {
			rowEnd = pageIndex * pageSize + lastPageRecordCount - 1;
		} else {
			rowEnd = (pageIndex + 1) * pageSize - 1;
		}

		for (int i = rowStart; i <= rowEnd; i++) {
			PFDataRow dr = newdt.NewRow();
			dr.setCol(dt.Rows.get(i).getCol());
			newdt.Rows.add(dr);
		}

		if (lastPageRecordCount != 0) {
			pageCount.SetValue(_pageCount2 + 1);
		} else {
			pageCount.SetValue(_pageCount2);
		}
		return newdt;
	}

//   public static DataTable DataPager(this DataTable dt, int pageIndex, int pageSize)
//   {
//       int pageCount = 0;
//       return DataTablePaging(dt, pageIndex, pageSize, out pageCount);
//   }
//
//   private static DataTable DataTablePaging(DataTable dt, int pageIndex, int pageSize, out int pageCount)
//   {
//       if (dt == null || dt.Rows.Count <= 0)
//       {
//           pageCount = -1;
//           return null;
//       }
//       int recordCount = dt.Rows.Count;
//       int _pageCount2 = recordCount / pageSize;
//
//       if (pageIndex < 0)
//           pageIndex = 0;
//       else if (pageIndex > _pageCount2)
//           pageIndex = _pageCount2;
//
//       DataTable newdt = new DataTable();
//       foreach (System.Data.DataColumn dcol in dt.Columns)
//       {
//           DataColumn dc = new DataColumn();
//           dc.DataType = dcol.DataType;
//           dc.ColumnName = dcol.ColumnName;
//           dc.DefaultValue = dcol.DefaultValue;
//           newdt.Columns.Add(dc);
//       }
//       int lastPageRecordCount = recordCount % pageSize;
//       int rowStart = pageIndex * pageSize;
//       int rowEnd;
//
//       if (pageIndex == _pageCount2 && lastPageRecordCount != 0)
//       {
//           rowEnd = pageIndex * pageSize + lastPageRecordCount - 1;
//       }
//       else
//       {
//           rowEnd = (pageIndex + 1) * pageSize - 1;
//       }
//
//       for (int i = rowStart; i <= rowEnd; i++)
//       {
//           DataRow dr = newdt.NewRow();
//           dr.ItemArray = dt.Rows[i].ItemArray;
//           newdt.Rows.Add(dr);
//       }
//
//       if (lastPageRecordCount != 0)
//       {
//           pageCount = _pageCount2 + 1;
//       }
//       else
//       {
//           pageCount = _pageCount2;
//       }
//       return newdt;
//   }
//
//   public static DataTable DataPager(this DataTable dt, int pageIndex, int pageSize, out int pageCount)
//   {
//       return DataTablePaging(dt, pageIndex, pageSize, out pageCount);
//   }
//   private static DataTable SetColumnProperty(this DataTable dt, string columnName, string propertyName, string propertyValue)
//   {
//
//       if (dt.Columns[columnName] != null)
//       {
//           dt.Columns[columnName].ExtendedProperties[propertyName] = propertyValue;
//       }
//       return dt;
//   }
//   private static DataTable SetColumnProperty(this DataTable dt, int columnIdx, string propertyName, string propertyValue)
//   {
//       if (dt.Columns.Count > columnIdx)
//       {
//           dt.Columns[columnIdx].ExtendedProperties[propertyName] = propertyValue;
//       }
//       return dt;
//   }
//   private static DataTable SetColumn(this DataTable dt, string columnName, Action<DataColumn> action)
//   {
//       var c = dt.Columns[columnName];
//       if (c != null)
//       {
//           action(c);
//       }
//       return dt;
//   }
//   private static object GetColumnProperty(this DataTable dt, string columnName, string propertyName)
//   {
//
//       if (dt.Columns[columnName] != null)
//       {
//           return dt.Columns[columnName].ExtendedProperties[propertyName];
//       }
//       return null;
//   }
//   /// <summary>
//   /// 自用的列中文标题
//   /// </summary>
//   /// <param name="dt"></param>
//   /// <param name="title"></param>
//   /// <returns></returns>
//   public static DataTable SetColumnTitle(this DataTable dt, string columnName, string title)
//   {
//       return SetColumnProperty(dt, columnName, "title", title);
//   }
//   public static DataTable SetColumnWidth(this DataTable dt, string columnName, string width)
//   {
//       return SetColumnProperty(dt, columnName, "width", width);
//   }
//   public static DataTable SetColumnWidth(this DataTable dt, int columnIdx, string width)
//   {
//       return SetColumnProperty(dt, columnIdx, "width", width);
//   }
//   //public static DataTable SetColumnCompute(this DataTable dt, string columnName, string expression)
//   //{
//   //    SetColumn(dt,columnName,a=> {
//   //        a.ExtendedProperties["compute"]="1";
//   //        a.Expression = expression;
//   //    });
//   //    return dt;
//   //}
//   public static DataColumn SetColumnCompute(this DataColumn col, string expression)
//   {
//       col.ExtendedProperties["compute"] = true;
//       col.Expression = expression;
//       return col;
//   }
	/// <summary>
	/// 是计算列时,groupby不运算
	/// </summary>
	/// <param name="dt"></param>
	/// <param name="columnIdx"></param>
	/// <param name="width"></param>
	/// <returns></returns>
	@Deprecated
	public static Boolean IsColumnCompute(PFDataColumn col) {
		if (col.ExtendedProperties.containsKey("compute")) {
			Object p = col.ExtendedProperties.get("compute");
			return p != null && ((Boolean) p).equals(true);
		}
		return false;
	}
//   //public static bool IsColumnCompute(this DataTable dt, string columnName)
//   //{
//   //    var p = GetColumnProperty(dt, columnName, "compute");
//   //    return p != null && (p as string).Equals("1");
//   //}
//    /**
//     * 移到PFDataTable内部
//     * @param srcColumn
//     * @param dst
//     */
//    @Deprecated
//    private static void CopyTableColumn(PFDataColumn srcColumn, PFDataTable dst)
//    {
//    	PFDataColumn dstColumn = dst.Columns.add(srcColumn.getKey(), srcColumn.getDataType());
//        for (String key : srcColumn.ExtendedProperties.keySet())
//        {
//            dstColumn.ExtendedProperties.put(key,srcColumn.ExtendedProperties.get(key));
//        }
//        //benjamintodo
//        //if (srcColumn.Expression != null) { dstColumn.Expression = srcColumn.Expression; }
//    }
//   public static void CopyTableRow(DataRow srcRow, DataTable dst)
//   {
//       //var src = rows.Table;
//       var dstRow = dst.NewRow();
//       foreach (DataColumn column in dst.Columns)
//       {
//           dstRow[column.ColumnName] = srcRow[column.ColumnName];
//       }
//       dst.Rows.Add(dstRow);
//   }

	/// <summary>
	/// Table汇总方法，如果不提供valueFields，默认对group之外的所有int或decimal字段汇总
	/// </summary>
	/// <param name="dt"></param>
	/// <param name="groupFields"></param>
	/// <param name="valueFields"></param>
	/// <returns></returns>
	@Deprecated
	public static SGDataTable DataTableGroupBy(SGDataTable dt, String[] groupFields,
			PFKeyValueCollection<SummaryType> valueFields) {
		return dt.DataTableGroupBy(groupFields, valueFields);
//        try
//        {
//        	String[][] groupFieldsF=new String[][] { groupFields};
//        	PFDataColumnCollection columns = dt.Columns;
//        	HashMap<String, Type> columnType = new HashMap<String, Type>();
//            //var valueColumnType = new Dictionary<String, Type>();
//            Boolean allValue = valueFields == null;
//            if (allValue) { valueFields = new PFKeyValueCollection<SummaryType>(); }
//            PFDataTable result = new PFDataTable();
//            List<PFDataRow> rowList = dt.Rows;
//            //加列
//            List<String> srcColumnNames = new ArrayList<String>();
//            for (PFDataColumn dataColumn : columns)
//            {
//                if (Arrays.asList(groupFieldsF[0]).contains(dataColumn.getKey()))
//                {
//                    CopyTableColumn(dataColumn, result);
//                    //result.Columns.Add(dataColumn.ColumnName, dataColumn.DataType).ExtendedProperties=dataColumn.ExtendedProperties;
//                }
//                else if (PFSqlFieldType.Decimal.equals(dataColumn.getPFDataType()) 
//                		|| PFSqlFieldType.Int.equals(dataColumn.getPFDataType())
//                    ||PFSqlFieldType.Long.equals(dataColumn.getPFDataType()) 
//                    )//其实字符串也可以汇总的，如Max之类，但好像没什么意义
//                {
//                    if (allValue)
//                    {
//                        valueFields.Add(dataColumn.getKey(), SummaryType.Sum);
//                    }
//                    //if (//allValue||
//                    //    valueFields.Keys.Contains(dataColumn.ColumnName))
//                    if (//allValue||
//                        valueFields.ContainsKey(dataColumn.getKey()))
//                    {
//                        CopyTableColumn(dataColumn, result);
//                        //result.Columns.Add(dataColumn.getKey(), dataColumn.DataType);
//                    }
//                }
//                srcColumnNames.add(dataColumn.getKey());
//            }
//            //因为dt有可能经过其它代码groupby的，就会出现groupField的列不存在于dt的情况
//            groupFieldsF[0] =PFDataHelper.ArrayWhere(String.class,groupFieldsF[0], a -> srcColumnNames.contains(a)) ;
////            valueFields = new PFKeyValueCollection<SummaryType>(valueFields.Where(a -> srcColumnNames.Contains(a.Key)).Select(b -> b).ToList());
//            valueFields = new PFKeyValueCollection<SummaryType>(valueFields.stream().filter(a -> srcColumnNames.contains(a.Key)).collect(Collectors.toList()));
//
////            List<IGrouping<String, PFDataRow>> group = rowList
////                .GroupBy<PFDataRow, String>(dr ->
////                {
////                    var g = "";
////                    foreach (var i in groupFieldsF[0])
////                    {
////                        //g += ObjectToString(dr[i]) ?? "";//太慢
////                        g += (dr[i] ?? "").ToString();
////                    }
////                    return g;
////                }).ToList();//按A分组  
//            Map<String,List<PFDataRow>> group = rowList.stream()
//            		.collect(Collectors.groupingBy(dr->{
//                        String g = "";
//                        for (String i : groupFieldsF[0])
//                        {
//                            //g += ObjectToString(dr[i]) ?? "";//太慢
////                            g += (dr.getColumn()[i] ?? "").ToString();
//                            Object c=dr.getColumn(i);
//                            g += c==null?"":c.toString();
//                        }
//                        return g;
//            		}));
//     	   Iterator<Entry<String,List<PFDataRow>>> iter = group.entrySet().iterator();
//    	   while(iter.hasNext()){
//    		   Entry<String,List<PFDataRow>> ig=iter.next();
//    	   //}
//            //for (IGrouping<String, PFDataRow> ig : group){
//    		   
//                //创建一个PFDataRow实例
//                PFDataRow row = result.NewRow();
//                //分组列
//                PFDataRow f = ig.getValue().get(0);//用于得到分组
//                for (String i : groupFieldsF[0])
//                {
//                    //row[i]=ig.Key[i]//这样写要改上面group的对象为Dictionary
//                    row.set(i,f.getColumn(i));
//                }
//                //值列
//                for (PFKeyValue<SummaryType> i : valueFields)
//                {
//                    if (!IsColumnCompute(result.Columns.get(i.Key)))//不是计算列才设置值
//                    {
//                    	PFSqlFieldType dataType = columns.get(i.Key).getPFDataType();
//                        if (i.Value == SummaryType.Sum)
//                        {
//                            if (PFSqlFieldType.Decimal.equals(dataType)
//                            	||PFSqlFieldType.Long.equals(dataType)	)
//                            {
////                                row[i.Key] = ig.getValue().stream().Sum(delegate (PFDataRow r)
////                                {
////                                    return PFDataHelper.ObjectToDecimal(r[i.Key]);
////                                });
////                            	List<BigDecimal> cmonthList=ig.getValue().stream().map(r->PFDataHelper.ObjectToDecimal(r.getColumn(i.Key))).collect(Collectors.toList());
//                            	Long s=ig.getValue().stream().mapToLong(r->PFDataHelper.ObjectToLong(r.getColumn(i.Key))).sum();
//                                row.set(i.Key,s);
//                            }
//                            else if (PFSqlFieldType.Int.equals(dataType) )
//                            {
////                                row[i.Key] = ig.Sum(delegate (PFDataRow r)
////                                {
////                                    return PFDataHelper.ObjectToInt(r[i.Key]);
////                                });
//                                int s=ig.getValue().stream().mapToInt(r->PFDataHelper.ObjectToInt(r.getColumn(i.Key))).sum();
//                                row.set(i.Key,s);
//                            }
////                            else if (dataType == typeof(double))
////                            {
////                                row[i.Key] = ig.Sum(delegate (PFDataRow r)
////                                {
////                                    return PFDataHelper.ObjectToDouble(r[i.Key]);
////                                });
////                            }
////                            else if (dataType == typeof(long))
////                            {
////                                row[i.Key] = ig.Sum(delegate (PFDataRow r)
////                                {
////                                    return PFDataHelper.ObjectToLong(r[i.Key]);
////                                });
////                            }
//                        }
//                        if (i.Value == SummaryType.Average)
//                        {
//                            if (PFSqlFieldType.Decimal.equals(dataType)
//                                	||PFSqlFieldType.Long.equals(dataType))
//                            {
////                                row[i.Key] = Math.Round(ig.Average(delegate (PFDataRow r)
////                                {
////                                    return PFDataHelper.ObjectToDecimal(r[i.Key]) ?? 0;
////                                }), DecimalPrecision);
//                                double s=ig.getValue().stream().mapToLong(r->PFDataHelper.ObjectToLong(r.getColumn(i.Key))).average().getAsDouble();
//                                row.set(i.Key,s);
//                            }
//                            else if (PFSqlFieldType.Int.equals(dataType) )
//                            {
////                                row[i.Key] = ig.Average(delegate (PFDataRow r)
////                                {
////                                    return PFDataHelper.ObjectToInt(r[i.Key]);
////                                });
//                            	double s=ig.getValue().stream().mapToInt(r->PFDataHelper.ObjectToInt(r.getColumn(i.Key))).average().getAsDouble();
//                                row.set(i.Key,s);
//                            }
////                            else if (dataType == typeof(double))
////                            {
////                                row[i.Key] = ig.Average(delegate (PFDataRow r)
////                                {
////                                    return PFDataHelper.ObjectToDouble(r[i.Key]);
////                                });
////                            }
////                            else if (dataType == typeof(long))
////                            {
////                                row[i.Key] = ig.Average(delegate (PFDataRow r)
////                                {
////                                    return PFDataHelper.ObjectToLong(r[i.Key]);
////                                });
////                            }
//                        }
//                    }
//
//                }
//
//                //加入到PFDataTable
//                result.Rows.add(row);
//            }
//            //ExtProp
//            for (String key : dt.ExtendedProperties.keySet())
//            {
//                result.ExtendedProperties.put(key,dt.ExtendedProperties.get(key));
//            }
//
//            return result;
//        }
//        catch (Exception e)
//        {
//            WriteError(e);
//        }
//        return null;
	}
//
//   /// <summary>
//   /// 同比的计算列表达式如 (thisYear-lastYear)*100/lastYear
//   /// </summary>
//   /// <param name="thisYear"></param>
//   /// <param name="lastYear"></param>
//   /// <returns></returns>
//   public static string GetColumnYearOnYear(string thisYear, string lastYear)
//   {
//       return string.Format("IIF({1}>0,({0}-{1})*100/{1},0)", thisYear, lastYear);
//   }
//   /// <summary>
//   /// 计划完成率表达式
//   /// </summary>
//   /// <param name="thisYear"></param>
//   /// <param name="lastYear"></param>
//   /// <returns></returns>
//   public static string GetColumnPlanCompletionRate(string actual, string plan)
//   {
//       return string.Format("IIF({1}>0,({0}/{1})*100,0)", actual, plan);
//   }

	/// <summary>
	/// 便于统一改为不执行
	/// </summary>
	public static void GCCollect(Boolean must) {
		if (must || SGDataHelper._appConfig.getAllowGCCollect()) {
			System.gc();
		}
	}

	public static void GCCollect() {
		GCCollect(false);
	}
//   #endregion DataTable
//
//   #region Tree
//   public static void BuildTreeList(List<TreeListItem> list, List<TreeListItem> parent, Func<List<TreeListItem>, TreeListItem, List<TreeListItem>> childrenAction)
//   {
//       if (parent != null && parent.Count > 0)
//       {
//           parent.ForEach(p =>
//           {
//               p.Children = childrenAction(list, p);
//               if (p.Children != null && p.Children.Count > 0)
//               {
//                   BuildTreeList(list, p.Children, childrenAction);
//               }
//           });
//       }
//   }
//   #endregion
//
//   #region List
//
//   public delegate T2 SelectListInvoker<T, T2>(T x);
//   /// <summary>
//   /// net2.0版本可使用的select方法
//   /// </summary>
//   public static List<T2> SelectList<T, T2>(List<T> source, SelectListInvoker<T, T2> action)
//   {
//       var result = new List<T2>();
//       source.ForEach(a =>
//       {
//           result.Add(action(a));
//       });
//       return result;
//   }
//   /// <summary>
//   /// 注意,这个方法会改变第一个list的值(为了效率)
//   /// </summary>
//   /// <typeparam name="T"></typeparam>
//   /// <param name="lists"></param>
//   /// <returns></returns>
//   public static List<T> MergeList<T>(params List<T>[] lists)
//   {
//       List<T> result = null;
//       foreach (var i in lists)
//       {
//           if (i == null) { continue; }
//           if (result == null)
//           {
//               result = i;
//           }
//           else
//           {
//               result.AddRange(i);
//           }
//       }
//       return result;
//   }
//   #endregion
//
//   #region ReflectionHelper
//   public static bool IsNullable(PropertyInfo property)
//   {
//       return IsNullable(property.PropertyType);
//       //if (property.PropertyType.IsGenericType &&
//       //    property.PropertyType.GetGenericTypeDefinition() == typeof(Nullable<>))
//       //    return true;
//
//       //return false;
//   }
//   public static bool IsNullable(Type t)
//   {
//       if (t.IsGenericType &&
//           t.GetGenericTypeDefinition() == typeof(Nullable<>))
//           return true;
//
//       return false;
//   }
//
//
//   /// <summary>
//   /// Includes a work around for getting the actual type of a Nullable type.(非空类型)
//   /// </summary>
//   public static Type GetPropertyType(PropertyInfo property)
//   {
//       if (IsNullable(property))
//           return property.PropertyType.GetGenericArguments()[0];
//
//       return property.PropertyType;
//   }
//   /// <summary>
//   /// Includes a work around for getting the actual type of a Nullable type.(非空类型)
//   /// </summary>
//   public static Type GetNonnullType(Type t)
//   {
//       if (IsNullable(t))
//           return t.GetGenericArguments()[0];
//
//       return t;
//   }
//   #endregion
//
//   #region Linq
//
//   //public static Expression<Func<T, T1>> FuncToExpression<T, T1>(Func<T, T1> call)
//   //{
//   //    MethodCallExpression methodCall = call.Target == null
//   //        ? Expression.Call(call.Method, Expression.Parameter(typeof(T)))
//   //        : Expression.Call(Expression.Constant(call.Target), call.Method, Expression.Parameter(typeof(T)));
//
//   //    return Expression.Lambda<Func<T, T1>>(methodCall);
//   //}
//
//   #endregion Linq

	/**
	 * 测试时返回这个(实际上,测试时也定义一个继承IPFConfigMapper的类比较好)
	 * 
	 * @author Administrator
	 *
	 */
	private static class PFConfigMapperEmpty implements ISGConfigMapper {

		public PFConfigMapperEmpty() {
		}

		@Override
		public List<PFModelConfigMapper> GetModelConfigMapper() {
			// TODO Auto-generated method stub
			return new ArrayList<PFModelConfigMapper>();
		}

		@Override
		public SGPathConfig GetPathConfig() {
			// TODO Auto-generated method stub
			return new SGPathConfig();
		}

		@Override
		public PFNetworkConfig GetNetworkConfig() {
			// TODO Auto-generated method stub
			return new PFNetworkConfig();
		}

		@Override
		public ISqlExecute GetClickHouseSqlExecute(ISGJdbc jdbc) {
			// TODO Auto-generated method stub
			return null;
		}
//		@Override
//		public ISqlExecute GetClickHouseSqlExecute(IPFJdbc jdbc,boolean sendConnErrorMessage) throws Exception {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		@Override
//		public Boolean IsEmpty() {
//			// TODO Auto-generated method stub
//			return true;
//		}

		@Override
		public PFRedisConfig GetRedisConfig() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean SendMobileMessage(String[] mobileNumber, String msg) {
			// TODO Auto-generated method stub
			return false;
		}


		@Override
		public <T> T getBeanInstance(Class<T> type) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	public static ISGConfigMapper GetConfigMapper() {
		if (_configMapper == null) {
			// #region 这样的效率似乎很低
			// var startTime = DateTime.Now;
			// var exAssems = new string[] { //.GetTypes会报错的程序集
			// "Microsoft.Practices.EnterpriseLibrary.PolicyInjection, Version=4.1.0.0,
			// Culture=neutral, PublicKeyToken=null" ,
			// "Microsoft.Web.Mvc, Version=3.0.0.0, Culture=neutral, PublicKeyToken=null"
			// };
			// var assem = AppDomain.CurrentDomain.GetAssemblies()
			// .Where(a => !exAssems.Contains(a.FullName));
			// var mappers = new List<Type>();
			// foreach (var i in assem)
			// {
			// try
			// {
			// mappers.AddRange(i.GetTypes().Where(t =>
			// t.GetInterfaces().Contains(typeof(IPFConfigMapper))));
			// }
			// catch// (Exception e)
			// {
			// //var aa = "";
			// //exTypes1.Add(i.FullName);
			// }
			// }
			// if (!mappers.Any()) { throw new Exception("必需实现接口IPFConfigMapper"); }
			// _configMapper = ((IPFConfigMapper)Activator.CreateInstance(mappers.First()));
			// #endregion

			// _configMapper = new PFConfigMapper();
			// var endTime = DateTime.Now;
			// #if DEBUG
			// var timeRange = endTime - startTime;
			// if (timeRange.TotalSeconds > 3)
			// {
			// throw new Exception(string.Format("反射IPFConfigMapper的时间为[{0}秒],太慢了",
			// timeRange.TotalSeconds));
			// }
			// #endif
			// return null;
			return new PFConfigMapperEmpty();
		}
		return _configMapper;
	}

	public static void SetConfigMapper(ISGConfigMapper configMapper) {
		_configMapper = configMapper;
	}

//
//
	public static PFModelConfigCollection MergeModelConfig(PFModelConfigCollection... configs) {
		PFModelConfigCollection m = new PFModelConfigCollection();
		int idx = 0;
		for (PFModelConfigCollection i : configs) {
			if (i != null) {
				if (idx == 0) {
					m = i;
				} else {
					PFModelConfigCollection other = i;

					Iterator<Entry<String, PFModelConfig>> iter = other.entrySet().iterator();
					while (iter.hasNext()) {
						Entry<String, PFModelConfig> key = iter.next();
						if (!m.containsKey(key.getKey())) {
							m.put(key.getKey(), key.getValue());
						}
					}
				}
				idx++;
			}
		}
		return m;

	}

	/// <summary>
	/// 获得多个Model的类型配置
	/// </summary>
	/// <param name="names"></param>
	/// <returns></returns>
	public static PFModelConfigCollection GetMultiModelConfig(String names) {
		String[] dataSet = names.split(",");
		// PFModelConfigCollection m = new PFModelConfigCollection();
		List<PFModelConfigCollection> arr = new ArrayList<PFModelConfigCollection>();
		for (String i : dataSet) {
			arr.add(SGDataHelper.GetModelConfig(i, null));
		}
		return MergeModelConfig(SGDataHelper.ObjectToArray(arr, PFModelConfigCollection.class));
	}

	/// <summary>
	/// 获得Model的类型配置
	/// </summary>
	/// <param name="name"></param>
	/// <param name="fullname"></param>
	/// <returns></returns>
	public static PFModelConfigCollection GetModelConfig(String name, String fullname) {
		ISGConfigMapper configMapper = GetConfigMapper();

//	   List<PFModelConfigMapper> mapper = configMapper.GetModelConfigMapper()
//           .FirstOrDefault(a => a.ModelName == fullname || a.ModelName == name);//当Model名有重复但命名空间不一样时,这样找是有风险的,定义mapper时最好把fullName的项放在前面

//		PFModelConfigMapper mapper = null;
//		if(!configMapper.IsEmpty()) {
//			mapper=ListFirstOrDefault(configMapper.GetModelConfigMapper(),
//					a -> a.ModelName.equals(fullname) || a.ModelName.equals(name));// 当Model名有重复但命名空间不一样时,这样找是有风险的,定义mapper时最好把fullName的项放在前面	
//		}
		PFModelConfigMapper mapper = ListFirstOrDefault(configMapper.GetModelConfigMapper(),
				a -> a.ModelName.equals(fullname) || a.ModelName.equals(name));// 当Model名有重复但命名空间不一样时,这样找是有风险的,定义mapper时最好把fullName的项放在前面

		if (mapper == null) {
			// return null;
			mapper = new PFModelConfigMapper();
			mapper.XmlDataSetName = fullname;
			mapper.setOtherXmlDataSetName(new ArrayList<String>() {
				/**
				* 
				*/
				private static final long serialVersionUID = 1L;

				{
					add(name);
				}
			});
		}

		SGPathConfig pathConfig = configMapper.GetPathConfig();

//     //读打包后的内文件不成功,原因不明--benjamin20201218
//     //String xmlfile = Path.Combine(HttpRuntime.AppDomainAppPath, pathConfig.ConfigPath + "\\FieldSets.xml");
//     //String xmlfile = Paths.get(GetBaseDirectory(), pathConfig.getConfigPath() + "\\FieldSets.xml").toString();
//     Resource resource = new ClassPathResource( pathConfig.getConfigPath() + "\\FieldSets.xml");//路径:resources/SysMenu.xml
//     SAXReader saxReader = new SAXReader();
//     //org.dom4j.Document document = saxReader.read(new java.io.File(xmlfile));   
//     org.dom4j.Document document;
//		try {
//			document = saxReader.read(resource.getFile());
//		} catch (DocumentException | IOException e) {
//			WriteError(e);
//			return null;
//		}       

		// 改为读jar同目录的
		// String xmlfile = Path.Combine(HttpRuntime.AppDomainAppPath,
		// pathConfig.ConfigPath + "\\FieldSets.xml");
		// String xmlfile = Paths.get(GetBaseDirectory(), pathConfig.getConfigPath() +
		// "\\FieldSets.xml").toString(); //linux下不能用\,一定要用/ .windows下都可以
		String xmlfile = Paths.get(GetBaseDirectory(), pathConfig.getConfigPath(), "FieldSets.xml").toString(); // linux下ok
		// System.out.println("GetModelConfig");
		// System.out.println(xmlfile);

		// Resource resource = new ClassPathResource( pathConfig.getConfigPath() +
		// "\\FieldSets.xml");//路径:resources/SysMenu.xml
		SAXReader saxReader = new SAXReader();
		org.dom4j.Document document;
		try {
			// document = saxReader.read(resource.getFile());
			File tmpFile = new java.io.File(xmlfile);
			if (!tmpFile.exists()) {
				return null;
			}
			document = saxReader.read(tmpFile);
			// document = saxReader.read(new java.io.File(xmlfile));
		} catch (Exception e) {
			WriteError(e);
			return null;
		}

		// 获取根元素
		Element root = document.getRootElement();
		Element dataSets = root;

//       XmlDocument doc = new XmlDocument();
//       doc.Load(xmlfile);
//       var dataSets = doc.ChildNodes[1];

		PFModelConfigCollection result = new PFModelConfigCollection();

//	    Element dataSet = dataSets.element(FormatString("DataSet[@name='{0}']", mapper.XmlDataSetName));
//       if (dataSet != null)
//       {
//           for(Object oi : dataSet.element("Fields").elements())
//           {
//        	   Element i=(Element)oi;
//        	   PFModelConfig config = new PFModelConfig(i, mapper.XmlDataSetName);
//               //if (!result.ContainsKey(config.FieldName))
//               //{
//               //    result.Add(config.FieldName, config);
//               //}
//               if (!result.containsKey(config.LowerFieldName))
//               {
//                   result.put(config.LowerFieldName, config);
//               }
//           }
//
//       }

		List<?> dataSetFields = dataSets
				.selectNodes(FormatString("DataSet[@name='{0}']/Fields/Field", mapper.XmlDataSetName));
		if (dataSetFields != null && dataSetFields.size() > 0) {
			for (Object oi : dataSetFields) {
				Element i = (Element) oi;
				PFModelConfig config = new PFModelConfig(i, mapper.XmlDataSetName);
				if (!result.containsKey(config.LowerFieldName)) {
					result.put(config.LowerFieldName, config);
				}
			}

		}

		// 特殊配置的属性
//       if (mapper.getOtherXmlDataSetName() != null && mapper.getOtherXmlDataSetName().size()>0)
//       {
//           for (String a : mapper.getOtherXmlDataSetName()) {
//        	   Element otherDataSet = dataSets
//                       .element(FormatString("DataSet[@name='{0}']", a));
//
//        	   if(otherDataSet!=null) {
//                   for (Object oi : otherDataSet.element("Fields").elements())
//                   {
//                	   Element i=(Element)oi;
//                	   Element fieldName = i.element("FieldName");
//                       //if (!result.Any(b => b.PropertyName == i.SelectSingleNode("FieldName").InnerText))
//                       if (!result.containsKey(fieldName.getText()))
//                       {
//                           result.put(fieldName.getText(), new PFModelConfig(i, a));
//                       }
//                   }
//        	   }
//           }
//       }

		if (mapper.getOtherXmlDataSetName() != null && mapper.getOtherXmlDataSetName().size() > 0) {
			for (String a : mapper.getOtherXmlDataSetName()) {
				List<?> otherDataSetFields = dataSets.selectNodes(FormatString("DataSet[@name='{0}']/Fields/Field", a));

				if (otherDataSetFields != null && otherDataSetFields.size() > 0) {
					for (Object oi : otherDataSetFields) {
						Element i = (Element) oi;
						Element fieldName = i.element("FieldName");
						// if (!result.Any(b => b.PropertyName ==
						// i.SelectSingleNode("FieldName").InnerText))
						if (!result.containsKey(fieldName.getText())) {
							// result.put(fieldName.getText(), new PFModelConfig(i, a));
							result.put(fieldName.getText().toLowerCase(), new PFModelConfig(i, a));
						}
					}
				}
			}
		}

		//// 特殊配置的属性
		// if (mapper.ExProperty != null)
		// {
		// mapper.ExProperty.ForEach(a =>
		// {
		// var old = result.FirstOrDefault(b => b.PropertyName == a.PropertyName);
		// if (old != null) { result.Remove(old); }
		// var exDataSet = dataSets
		// .SelectSingleNode(String.Format("DataSet[@name='{0}']", a.XmlDataSetName));
		// foreach (XmlNode i in exDataSet.SelectSingleNode("Fields").ChildNodes)
		// {
		// if (i.SelectSingleNode("FieldName").InnerText == a.XmlFieldName)
		// {
		// result.Add(new PFModelConfig(i, a.XmlDataSetName) { PropertyName =
		//// a.PropertyName });
		// }
		// }
		// //result.Add(a);

		// });
		// }
		return result;

	}
//
//   public static PFModelConfigCollection GetModelConfig(Type modelType)
//   {
//       return PFDataHelper.GetModelConfig(modelType.Name, modelType.FullName);
//   }

//	/**
//	 * 改用 PFSqlFieldType.Init()
//	 * @param type
//	 * @return
//	 * @Deprecated PFSqlFieldType.Init()
//	 */
//	@Deprecated
//	public static PFSqlFieldType GetPFTypeByString(String type) {
//		// String.class.getComponentType();
////	   if(type.equals(type)) {
////		   return Null.class;
////	   }
////	   if(Null.class.equals(type)) {
////		   return Null.class;
////	   }
//		switch (type.toLowerCase()) {
//		case "bool":
//		case "bit":
//			return PFSqlFieldType.Bool;
////           case "byte":
////               return Byte.class;
////           case "sbyte":
////               return Type.GetType("System.SByte", true, true);
////           case "char":
////               return char.class;
//		case "decimal":
//			return PFSqlFieldType.Decimal;
//		case "double":
//			return PFSqlFieldType.BigDecimal;
//		case "float":
//			return PFSqlFieldType.BigDecimal;
//		case "int":
//		case "bigint":
//		case "smallint":
//			return PFSqlFieldType.Int;
//		case "uint":
//			return PFSqlFieldType.Int;
//		case "long":
//			return PFSqlFieldType.BigDecimal;
//		case "ulong":
//			return PFSqlFieldType.BigDecimal;
////           case "object":
////               return Object.class;
//		case "short":
//			return PFSqlFieldType.Int;
//		case "ushort":
//			return PFSqlFieldType.Int;
//		case "string":
//		case "varchar":
//			return PFSqlFieldType.String;
//		case "datetime":
//		case "java.util.calendar":
//			return PFSqlFieldType.DateTime;
//		case "guid":
//			return PFSqlFieldType.UUID;
////           case "percent":
////               return PFPercent.class;
//		case "date":
//			return PFSqlFieldType.DateTime;
//		default:
//			return PFSqlFieldType.String;
//		}
//	}

//	public static PFSqlFieldType GetPFTypeBySqlType(int sqlType) {
//		// String.class.getComponentType();
////	   if(type.equals(type)) {
////		   return Null.class;
////	   }
////	   if(Null.class.equals(type)) {
////		   return Null.class;
////	   }
//		switch (sqlType) {
//		case java.sql.Types.BOOLEAN:
//		case java.sql.Types.BIT:
//			return PFSqlFieldType.Bool;
////           case "byte":
////               return Byte.class;
////           case "sbyte":
////               return Type.GetType("System.SByte", true, true);
////           case "char":
////               return char.class;
//		case java.sql.Types.DECIMAL:
//		case java.sql.Types.DOUBLE:
//		case java.sql.Types.NUMERIC:
//			return PFSqlFieldType.Decimal;
////           case java.sql.Types.DECIMAL:
////               return PFSqlFieldType.BigDecimal;
//		case java.sql.Types.INTEGER:
//			return PFSqlFieldType.Int;
//		case java.sql.Types.BIGINT:
//			return PFSqlFieldType.Long;
//		case java.sql.Types.VARCHAR:
//		case java.sql.Types.NVARCHAR:
//			return PFSqlFieldType.String;
//		case java.sql.Types.TIMESTAMP:
//		case java.sql.Types.DATE:
//			return PFSqlFieldType.DateTime;
////           case java.sql.Types.:
////               return PFSqlFieldType.UUID;
//		default:
//			return PFSqlFieldType.String;
//		}
//	}
//
//	@Deprecated
//	public static PFSqlFieldType GetPFTypeByClass(Class<?> type) {
//		return PFSqlFieldType.InitByClass(type);
//////	   String s=GetStringByType(cl);
//////	   return GetPFTypeByString(s);
////
////		// 以后应该先判断长名,找不到再找短名
////		// switch (type.toString())
////		if (type == null) {
////			return PFSqlFieldType.Null;
////		}
////		switch (type.getSimpleName()) {
////		case "System.Boolean":
////		case "Boolean":
////			return PFSqlFieldType.Bool;
//////           case "System.Byte":
//////               return "byte";
//////           case "System.SByte":
//////               return "sbyte";
//////           case "System.Char":
//////               return "char";
////		case "System.Decimal":
////		case "System.Single":
////			return PFSqlFieldType.Decimal;
////		case "System.Double":
////		case "System.BigDecimal":
////		case "BigDecimal":
////			return PFSqlFieldType.BigDecimal;
////		case "System.Int32":
////		case "Integer":
////			return PFSqlFieldType.Int;
////		case "System.UInt32":
////			return PFSqlFieldType.Int;
////		case "System.Int64":
////			return PFSqlFieldType.Long;
////		case "System.UInt64":
////			return PFSqlFieldType.Long;
//////           case "System.Object":
//////               return "object";
////		case "System.Int16":
////			return PFSqlFieldType.Int;
////		case "System.UInt16":
////			return PFSqlFieldType.Int;
////		case "System.String":
////		case "String":
////			return PFSqlFieldType.String;
////		case "System.DateTime":
////		case "Calendar":
////		case "Timestamp":
////		case "GregorianCalendar":
////		case "Perfect.PFDate":
////			return PFSqlFieldType.DateTime;
////		case "System.Guid":
////			return PFSqlFieldType.UUID;
//////           case "Perfect.PFPercent":
//////               return "percent";
////		default:
////			// return type.toString();
////			return GetPFTypeByString(type.getSimpleName());
////		}
//	}

	public static Class<?> GetTypeByString(String type) {
		// String.class.getComponentType();
//	   if(type.equals(type)) {
//		   return Null.class;
//	   }
//	   if(Null.class.equals(type)) {
//		   return Null.class;
//	   }
		switch (type.toLowerCase()) {
		case "bool":
		case "bit":
		case "varbinary":
		case "tinyint":// mysql的tinyint的Result的getObject直接就是false了
			return Boolean.class;
		case "byte":
			return Byte.class;
//           case "sbyte":
//               return Type.GetType("System.SByte", true, true);
		case "char":
			return char.class;
		case "decimal":
		case "numeric":
			return BigDecimal.class;
		case "double":
			return Double.class;
		case "float":
			return Float.class;
		case "int":
		case "bigint":
		case "smallint":
			return int.class;
		case "uint":
			return int.class;
		case "long":
			return long.class;
		case "ulong":
			return long.class;
		case "object":
			return Object.class;
		case "short":
			return short.class;
		case "ushort":
			return short.class;
		case "string":
		case "varchar":
		case "nvarchar":
			return String.class;
		case "datetime":
		case "smalldatetime":
		case "java.util.calendar":
		case "timestamp":
			return Calendar.class;
		case "guid":
			return UUID.class;
		case "percent":
			return PFPercent.class;
		case "date":
			return SGDate.class;
		case "null":
			return null;
		default:
			try {
				return Class.forName(type);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

	public static String GetStringByType(Type type) {
		return GetStringByType((Class<?>) type);
	}

	/*
	 * return的可能性: BigDecimal
	 */
	public static String GetStringByType(Class<?> type) {
		// switch (type.toString())
		if (type == null) {
			return null;
		}
		switch (type.getSimpleName()) {
		case "System.Boolean":
		case "Boolean":
			return "bool";
		case "System.Byte":
			return "byte";
		case "System.SByte":
			return "sbyte";
		case "System.Char":
			return "char";
		case "System.Decimal":
			return "decimal";
		case "System.Double":
			return "double";
		case "System.Single":
			return "float";
		case "System.Int32":
			return "int";
		case "System.UInt32":
			return "uint";
		case "System.Int64":
			return "long";
		case "System.UInt64":
			return "ulong";
		case "System.Object":
			return "object";
		case "System.Int16":
			return "short";
		case "System.UInt16":
			return "ushort";
		case "System.String":
		case "String":
			return "string";
		case "System.DateTime":
		case "Calendar":
			// return "datetime";
			return "DateTime";// 转string后必需和FieldSet.xml上的对应
		case "System.Guid":
			return "guid";
		case "Perfect.PFPercent":
			return "percent";
		case "Perfect.PFDate":
			return "date";
		default:
			// return type.toString();
			return type.getSimpleName();
		}
	}

//
//   #region 时间
	public static Calendar GetYearStart(Calendar date) {
		Calendar r = Calendar.getInstance();
		r.set(Calendar.YEAR, date.get(Calendar.YEAR));
		r.set(Calendar.MONTH, 1);
		r.set(Calendar.DAY_OF_MONTH, 1);
		return r;
		// return new DateTime(date.Year, 1, 1);
	}

	public static Calendar GetYearEnd(Calendar date) {
		// Calendar r=Calendar.getInstance();
		Calendar r = Calendar.getInstance();
		r.set(Calendar.YEAR, date.get(Calendar.YEAR));
		r.set(Calendar.MONTH, 1);
		r.set(Calendar.DAY_OF_MONTH, 1);
		r.add(Calendar.YEAR, 1);
		r = GetYearStart(r);
		r.add(Calendar.SECOND, -1);
		return r;
//       return GetYearStart(date.AddYears(1)).AddSeconds(-1);
	}

	public static Calendar GetMonthStart(Calendar date) {
		SGDate d = new SGDate(date);
		return new SGDate(d.GetYear(), d.GetMonth(), 1, 1, 1, 1).ToCalendar();

//	   Calendar r=Calendar.getInstance();
//	   r.set(Calendar.YEAR, date.get(Calendar.YEAR));  
//	   r.set(Calendar.MONTH, date.get(Calendar.MONTH));    
//	   r.set(Calendar.DAY_OF_MONTH, 1);    
//	   return r;
//       //return new DateTime(date.Year, date.Month, 1);
	}

	public static Calendar GetMonthEnd(Calendar date) {
		Calendar r = Calendar.getInstance();
		r.set(Calendar.YEAR, date.get(Calendar.YEAR));
		r.set(Calendar.MONTH, date.get(Calendar.MONTH));
		r.set(Calendar.DAY_OF_MONTH, 1);
		r.add(Calendar.MONTH, 1);
		r = GetMonthStart(r);
		r.add(Calendar.SECOND, -1);
		return r;
		// return GetMonthStart(date.AddMonths(1)).AddSeconds(-1);
	}

	public static Calendar GetDayStart(Calendar date) {
		Calendar r = Calendar.getInstance();
		r.set(Calendar.YEAR, date.get(Calendar.YEAR));
		r.set(Calendar.MONTH, date.get(Calendar.MONTH));
		r.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
		return r;
		// return new DateTime(date.Year, date.Month, date.Day);
	}

	public static Calendar GetDayEnd(Calendar date) {
		Calendar r = Calendar.getInstance();
		r.set(Calendar.YEAR, date.get(Calendar.YEAR));
		r.set(Calendar.MONTH, date.get(Calendar.MONTH));
		r.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
		r.add(Calendar.DATE, 1);
		r = GetDayStart(r);
		r.add(Calendar.SECOND, -1);
		return r;
		// return GetDayStart(date.AddDays(1)).AddSeconds(-1);
	}

	public static long CountTime(Consumer<?> action) {
		SGDate now = SGDate.Now();
		action.accept(null);
		SGDate end = SGDate.Now();
		return end.ToCalendar().getTimeInMillis() - now.ToCalendar().getTimeInMillis();
	}
//   /// <summary>
//   /// Data.getTime()相减之后的值转换为容易处理的对象
//   /// </summary>
//   /// <param name="minute"></param>
//   /// <returns></returns>
//   public static PFTimeSpan GetTimeSpan(long ElapsedMilliseconds, PFYmd ymd = PFYmd.Hour | PFYmd.Minute | PFYmd.Second)
//   {
//       var after = ElapsedMilliseconds / 1000;//这里得到是秒
//       var result = new PFTimeSpan();
//       if (EnumHasFlag(ymd, PFYmd.Hour))
//       {
//           result.Hour = ObjectToInt(after / (60 * 60)) ?? 0;//计算整数小时数
//           after -= (result.Hour * 60 * 60);//取得算出小时数后剩余的秒数
//       };
//       if (EnumHasFlag(ymd, PFYmd.Minute))
//       {
//           result.Minute = ObjectToInt(after / 60) ?? 0;//计算整数小时数
//           after -= (result.Minute * 60);//取得算出小时数后剩余的秒数
//       };
//       if (EnumHasFlag(ymd, PFYmd.Second))
//       {
//           result.Second = ObjectToInt(after) ?? 0;//计算整数秒数
//           after -= result.Second;//取得算出秒数后剩余的毫秒数
//       };
//       if (EnumHasFlag(ymd, PFYmd.Millisecond))
//       {
//           result.Millisecond = ObjectToInt(after) ?? 0;
//       };
//       return result;
//   }
//   public static PFTimeSpan GetTimeSpan(TimeSpan ts, PFYmd ymd = PFYmd.Hour | PFYmd.Minute | PFYmd.Second)
//   {
//       return GetTimeSpan(ObjectToLong(ts.TotalMilliseconds) ?? 0, ymd);
//   }
//   #endregion 时间
//   //public static string GetEnMonthByNum(int num)
//   //{
//   //    switch (num)
//   //    {
//   //        case 1:
//   //            return "Jan";
//   //      case 2:
//   //            return "Feb";
//   //        case 3:
//   //            return "Mar";
//   //        case 4:
//   //            return "Apr";
//   //        case 5:
//   //            return "May";
//   //        case 6:
//   //            return "Jun";
//   //        case 7:
//   //            return "Jul";
//   //        case 8:
//   //            return "Aug";
//   //        case 9:
//   //            return "Sep";
//   //        case 10:
//   //            return "Oct";
//   //        case 11:
//   //            return "Nov";
//   //        case 12:
//   //            return "Dec";
//   //        default:
//   //            return "";
//   //    }
//   //}
//
//
//   #region 文件

	/**
	 * java获取文件的md5值 https://blog.csdn.net/qq_25646191/article/details/78863110
	 * 
	 * @param fis 输入流
	 * @return
	 */
	public static String getHashMD5ByStream(InputStream fis) {
		try {
			// 拿到一个MD5转换器,如果想使用SHA-1或SHA-256，则传入SHA-1,SHA-256
			MessageDigest md = MessageDigest.getInstance("MD5");

			// 分多次将一个文件读入，对于大型文件而言，比较推荐这种方式，占用内存比较少。
			byte[] buffer = new byte[1024];
			int length = -1;
			while ((length = fis.read(buffer, 0, 1024)) != -1) {
				md.update(buffer, 0, length);
			}
			fis.close();
			// 转换并返回包含16个元素字节数组,返回数值范围为-128到127
			byte[] md5Bytes = md.digest();
			BigInteger bigInt = new BigInteger(1, md5Bytes);// 1代表绝对值
			return bigInt.toString(16);// 转换为16进制
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/// <summary>
	/// method for getting a files MD5 hash, say for
	/// a checksum operation
	/// </summary>
	/// <param name="file">the file we want the has from</param>
	/// <returns></returns>
	public static String GetHashMD5(File file) {
		try {
			FileInputStream inputStream = new FileInputStream(file);
			return getHashMD5ByStream(inputStream);

		} catch (Exception e) {

		}
		return "";
	}
//   /// <summary>
//   /// 创建指定目录
//   /// </summary>
//   /// <param name="targetDir"></param>
//   public static void CreateDirectory(string targetDir)
//   {
//       DirectoryInfo dir = new DirectoryInfo(targetDir);
//       if (!dir.Exists)
//           dir.Create();
//   }
//   /// <summary>
//   /// 下载文件
//   /// </summary>
//   /// <param name="FileFullPath">下载文件下载的完整路径及名称</param>
//   public static void DownLoadFile(string FileFullPath)
//   {
//       if (!string.IsNullOrEmpty(FileFullPath) && System.IO.File.Exists(FileFullPath))
//       {
//           FileInfo fi = new FileInfo(FileFullPath);//文件信息
//           FileFullPath = HttpUtility.UrlEncode(FileFullPath); //对文件名编码
//           FileFullPath = FileFullPath.Replace("+", "%20"); //解决空格被编码为"+"号的问题
//           HttpContext.Current.Response.Clear();
//           HttpContext.Current.Response.ContentType = "application/octet-stream";
//           HttpContext.Current.Response.AppendHeader("Content-Disposition", "attachment; filename=" + FileFullPath);
//           HttpContext.Current.Response.AppendHeader("content-length", fi.Length.ToString()); //文件长度
//           int chunkSize = 102400;//缓存区大小,可根据服务器性能及网络情况进行修改
//           byte[] buffer = new byte[chunkSize]; //缓存区
//           using (FileStream fs = fi.Open(FileMode.Open))  //打开一个文件流
//           {
//               while (fs.Position >= 0 && HttpContext.Current.Response.IsClientConnected) //如果没到文件尾并且客户在线
//               {
//                   int tmp = fs.Read(buffer, 0, chunkSize);//读取一块文件
//                   if (tmp <= 0) break; //tmp=0说明文件已经读取完毕,则跳出循环
//                   HttpContext.Current.Response.OutputStream.Write(buffer, 0, tmp);//向客户端传送一块文件
//                   HttpContext.Current.Response.Flush();//保证缓存全部送出
//                   Thread.Sleep(10);//主线程休息一下,以释放CPU
//               }
//           }
//       }
//   }
//   #region 下载大文件 支持续传、速度限制
//   public static bool DownloadFile(HttpContext context, string filePath, long speed)
//   {
//       string fileName = Path.GetFileName(filePath);
//       Stream myFile = new FileStream(filePath, FileMode.Open, FileAccess.Read, FileShare.ReadWrite);
//       return DownloadFile(context, myFile, fileName, speed);
//   }
//   /// <summary>
//   /// 下载文件，支持大文件、续传、速度限制。支持续传的响应头Accept-Ranges、ETag，请求头Range 。
//   /// Accept-Ranges：响应头，向客户端指明，此进程支持可恢复下载.实现后台智能传输服务（BITS），值为：bytes；
//   /// ETag：响应头，用于对客户端的初始（200）响应，以及来自客户端的恢复请求，
//   /// 必须为每个文件提供一个唯一的ETag值（可由文件名和文件最后被修改的日期组成），这使客户端软件能够验证它们已经下载的字节块是否仍然是最新的。
//   /// Range：续传的起始位置，即已经下载到客户端的字节数，值如：bytes=1474560- 。
//   /// 另外：UrlEncode编码后会把文件名中的空格转换中+（+转换为%2b），但是浏览器是不能理解加号为空格的，所以在浏览器下载得到的文件，空格就变成了加号；
//   /// 解决办法：UrlEncode 之后, 将 "+" 替换成 "%20"，因为浏览器将%20转换为空格
//   /// </summary>
//   /// <param name="httpContext">当前请求的HttpContext</param>
//   /// <param name="filePath">下载文件的物理路径，含路径、文件名</param>
//   /// <param name="speed">下载速度：每秒允许下载的字节数</param>
//   /// <returns>true下载成功，false下载失败</returns>
//   public static bool DownloadFile(HttpContext context, Stream myFile, string fileName, long speed)
//   {
//       bool ret = true;
//       try
//       {
//           #region 定义局部变量
//           long startBytes = 0;
//           int packSize = 1024 * 10; //分块读取，每块10K bytes
//
//           //FileStream myFile = new FileStream(filePath, FileMode.Open, FileAccess.Read, FileShare.ReadWrite);
//           BinaryReader br = new BinaryReader(myFile);
//           long fileLength = myFile.Length;
//
//           int sleep = (int)Math.Ceiling(1000.0 * packSize / speed);//毫秒数：读取下一数据块的时间间隔
//           //string lastUpdateTiemStr = File.GetLastWriteTimeUtc(filePath).ToString("r");
//           //string eTag = HttpUtility.UrlEncode(fileName, Encoding.UTF8) + lastUpdateTiemStr;//便于恢复下载时提取请求头;
//           #endregion
//
//           #region--验证：文件是否太大，是否是续传，且在上次被请求的日期之后是否被修改过--------------
//           if (myFile.Length > Int32.MaxValue)
//           {//-------文件太大了-------
//               context.Response.StatusCode = 413;//请求实体太大
//               return false;
//           }
//
//           //if (context.Request.Headers["If-Range"] != null)//对应响应头ETag：文件名+文件最后修改时间
//           //{
//           //    //----------上次被请求的日期之后被修改过--------------
//           //    if (context.Request.Headers["If-Range"].Replace("\"", "") != eTag)
//           //    {//文件修改过
//           //        context.Response.StatusCode = 412;//预处理失败
//           //        return false;
//           //    }
//           //}
//           #endregion
//
//           try
//           {
//               #region -------添加重要响应头、解析请求头、相关验证-------------------
//               context.Response.Clear();
//               context.Response.Buffer = false;
//               context.Response.AddHeader("Content-MD5", GetHashMD5(myFile));//用于验证文件
//               context.Response.AddHeader("Accept-Ranges", "bytes");//重要：续传必须
//               //context.Response.AppendHeader("ETag", "\"" + eTag + "\"");//重要：续传必须
//               //context.Response.AppendHeader("Last-Modified", lastUpdateTiemStr);//把最后修改日期写入响应              
//               context.Response.ContentType = "application/octet-stream";//MIME类型：匹配任意文件类型
//               context.Response.AddHeader("Content-Disposition", "attachment;filename=" + HttpUtility.UrlEncode(fileName, Encoding.UTF8).Replace("+", "%20"));
//               context.Response.AddHeader("Content-Length", (fileLength - startBytes).ToString());
//               context.Response.AddHeader("Connection", "Keep-Alive");
//               context.Response.ContentEncoding = Encoding.UTF8;
//               if (context.Request.Headers["Range"] != null)
//               {
//                   //------如果是续传请求，则获取续传的起始位置，即已经下载到客户端的字节数------
//                   context.Response.StatusCode = 206;//重要：续传必须，表示局部范围响应。初始下载时默认为200
//                   string[] range = context.Request.Headers["Range"].Split(new char[] { '=', '-' });//"bytes=1474560-"
//                   startBytes = Convert.ToInt64(range[1]);//已经下载的字节数，即本次下载的开始位置
//                   if (startBytes < 0 || startBytes >= fileLength)
//                   {
//                       //无效的起始位置
//                       return false;
//                   }
//               }
//               if (startBytes > 0)
//               {
//                   //------如果是续传请求，告诉客户端本次的开始字节数，总长度，以便客户端将续传数据追加到startBytes位置后----------
//                   context.Response.AddHeader("Content-Range", string.Format(" bytes {0}-{1}/{2}", startBytes, fileLength - 1, fileLength));
//               }
//               #endregion
//
//
//               #region -------向客户端发送数据块-------------------
//               br.BaseStream.Seek(startBytes, SeekOrigin.Begin);
//               int maxCount = (int)Math.Ceiling((fileLength - startBytes + 0.0) / packSize);//分块下载，剩余部分可分成的块数
//               for (int i = 0; i < maxCount && context.Response.IsClientConnected; i++)
//               {//客户端中断连接，则暂停
//                   context.Response.BinaryWrite(br.ReadBytes(packSize));
//                   context.Response.Flush();
//                   if (sleep > 1) Thread.Sleep(sleep);
//               }
//               #endregion
//           }
//           catch
//           {
//               ret = false;
//           }
//           finally
//           {
//               br.Close();
//               myFile.Close();
//           }
//       }
//       catch
//       {
//           ret = false;
//       }
//       return ret;
//   }
//   #endregion
//   #endregion
//
//   #region 反射
//   public static Type GetGenericType(object list)
//   {
//       return list.GetType().GetGenericArguments()[0];
//   }
//   //public static bool IsDynamicType(Type type)
//   //{
//   //    return type.Equals(typeof(ExpandoObject)) || type.Equals(typeof(object));
//   //}
//   #region dynamic需要用到(Net4.0版本以上)
//   //public static void EachListHeader(object list, Action<int, string, Type> handle)
//   //{
//   //    var index = 0;
//   //    var dict = GetListProperties(list);
//   //    foreach (var item in dict)
//   //        handle(index++, item.Key, item.Value);
//   //}
//   //public static Dictionary<string, Type> GetListProperties(dynamic list)
//   //{
//   //    var type = GetGenericType(list);
//   //    var names = new Dictionary<string, Type>();
//
//   //    if (IsDynamicType(type))
//   //    {
//   //        if (list.Count > 0)
//   //            foreach (var item in GetIDictionaryValues(list[0]))
//   //                names.Add(item.Key, (item.Value ?? string.Empty).GetType());
//   //    }
//   //    else
//   //    {
//   //        foreach (var p in GetProperties(type))
//   //            names.Add(p.Value.Name, p.Value.PropertyType);
//   //    }
//
//   //    return names;
//   //}
//   //public static void EachListRow(object list, Action<int, object> handle)
//   //{
//   //    var index = 0;
//   //    IEnumerator enumerator = ((dynamic)list).GetEnumerator();
//   //    while (enumerator.MoveNext())
//   //        handle(index++, enumerator.Current);
//   //}
//   #endregion
//   private static readonly PFThreadDictionary<Type, Dictionary<string, PropertyInfo>> _cachedProperties = new PFThreadDictionary<Type, Dictionary<string, PropertyInfo>>();
//   public static Dictionary<string, PropertyInfo> GetProperties(Type type)
//   {
//       var properties = _cachedProperties.GetOrAdd(type, BuildPropertyDictionary);
//
//       return properties;
//   }
//   private static Dictionary<string, PropertyInfo> BuildPropertyDictionary(Type type)
//   {
//       var result = new Dictionary<string, PropertyInfo>();
//
//       var properties = type.GetProperties();
//       foreach (var property in properties)
//       {
//           result.Add(property.Name//.ToLower()//SqlUpdateCollection使用时，如果每次对比时都转小写是非常麻烦的--wxj20181121
//               , property);
//       }
//       return result;
//   }
//   //public static IDictionary<string, object> GetIDictionaryValues(object item)
//   //{
//   //    if (item is IDictionary<string, object>)
//   //    {
//   //        return item as IDictionary<string, object>;
//   //    }
//   //    if (IsDynamicType(item.GetType()))
//   //        return item as IDictionary<string, object>;
//
//   //    var expando = (IDictionary<string, object>)new ExpandoObject();
//   //    var properties = GetProperties(item.GetType());
//   //    foreach (var p in properties)
//   //        expando.Add(p.Value.Name, p.Value.GetValue(item, null));
//   //    return expando;
//   //}
//   //public static void EachObjectProperty(object row, Action<int, string, object> handle)
//   //{
//   //    var index = 0;
//   //    var dict = GetIDictionaryValues(row);
//   //    foreach (var item in dict)
//   //        handle(index++, item.Key, item.Value);
//   //}

	private static Map<String, Field> BuildPropertyDictionary(Type type) {
		// Map<String, Field> result = new HashMap<String, Field>();
		Map<String, Field> result = new LinkedHashMap<String, Field>();// 有序map
		// Field[] properties = type.getClass().getDeclaredFields();
		Field[] properties = ((Class<?>) type).getDeclaredFields();

		for (Field property : properties) {
			property.setAccessible(true);
			result.put(property.getName()// .ToLower()//SqlUpdateCollection使用时，如果每次对比时都转小写是非常麻烦的--wxj20181121
					, property);
		}
		return result;
	}

	private static final ConcurrentHashMap<Type, Map<String, Field>> _cachedProperties = new ConcurrentHashMap<Type, Map<String, Field>>();

	public static Map<String, Field> GetProperties(Type type) {
		Map<String, Field> properties = _cachedProperties.getOrDefault(type, BuildPropertyDictionary(type));

		return properties;
	}

	public static Map<String, Object> GetIDictionaryValues(Object item) {
		if (item instanceof Map<?, ?>) {
			return SGDataHelper.<Map<String, Object>>ObjectAs(item);
		}
//       if (IsDynamicType(item.GetType()))
//           return item as IDictionary<string, Object>;

		// var expando = (IDictionary<string, object>)new ExpandoObject();
		// Map<String, Object> expando = new HashMap<String, Object>();
		Map<String, Object> expando = new LinkedHashMap<String, Object>();// 有序map
		Map<String, Field> properties = GetProperties(item.getClass());
		Iterator<Entry<String, Field>> iter = properties.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Field> key = iter.next();
			try {
				expando.put(key.getKey(), key.getValue().get(item));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//       for(var p in properties) {
//           expando.Add(p.Value.Name, p.Value.GetValue(item, null));
//       }
		return expando;
	}

	public static void EachObjectProperty(Object row, SGAction<Integer, String, Object> handle) {
		int index = 0;
		Map<String, Object> dict = GetIDictionaryValues(row);

		Iterator<Entry<String, Object>> iter = dict.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Object> key = iter.next();
			handle.go(index++, key.getKey(), key.getValue());
		}
//       foreach (var item in dict)
//           handle(index++, item.Key, item.Value);
	}

	public static void EachObjectPropertyType(Object row, SGAction<String, Field, PFModelConfig> handle) {
//int index = 0;	
		Map<String, Field> properties = GetProperties(row.getClass());
		Iterator<Entry<String, Field>> iter = properties.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Field> key = iter.next();
			try {
				PFModelConfig f = new PFModelConfig();
				f.FieldName = key.getKey();
				// f.FieldType=key.getValue().getDeclaringClass();
				f.FieldType = key.getValue().getType();

//       PFSqlFieldAttribute fieldType=f.FieldType.getAnnotation(PFSqlFieldAttribute.class);	
				PFSqlFieldAttribute fieldType = key.getValue().getAnnotation(PFSqlFieldAttribute.class);

				if (fieldType != null) {
					f.FieldText = fieldType.FieldText();
					if (!SGDataHelper.StringIsNullOrWhiteSpace(fieldType.FieldDescription())) {
						f.FieldDescription = fieldType.FieldDescription();
					}
				}
				// handle.go(index++, key.getKey(),f);
				handle.go(key.getKey(), key.getValue(), f);
//	   f.FieldType=key.getValue()	
				// expando.put(key.getKey(),key.getValue().get(item));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void ClassEachPropertyType(Class<?> cls, SGAction<String, Field, PFModelConfig> handle) {
		// int index = 0;
		Map<String, Field> properties = GetProperties(cls);
		Iterator<Entry<String, Field>> iter = properties.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Field> key = iter.next();
			try {
				PFModelConfig f = new PFModelConfig();
				f.FieldName = key.getKey();
				// f.FieldType=key.getValue().getDeclaringClass();
				f.FieldType = key.getValue().getType();

//		       PFSqlFieldAttribute fieldType=f.FieldType.getAnnotation(PFSqlFieldAttribute.class);	
				PFSqlFieldAttribute fieldType = key.getValue().getAnnotation(PFSqlFieldAttribute.class);

				if (fieldType != null) {
					f.FieldText = fieldType.FieldText();
					if (!SGDataHelper.StringIsNullOrWhiteSpace(fieldType.FieldDescription())) {
						f.FieldDescription = fieldType.FieldDescription();
					}
				}
				// handle.go(index++, key.getKey(),f);
				handle.go(key.getKey(), key.getValue(), f);
//			   f.FieldType=key.getValue()	
				// expando.put(key.getKey(),key.getValue().get(item));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

//   #endregion
//
//   #region Object
//
//   /// <summary>
//   /// 转换为string类型 defult为string.Empty ,模板通用转换方法 对string的处理有bug, 所以用此方法独立处理
//   /// </summary>
//   /// <param name="obj"></param>
//   /// <returns></returns>
//   public static string ObjectToString(object obj)
//   {
//       if (obj == null || obj == DBNull.Value
//           ) { return ""; }
//       return obj.ToString();
//
//       #region 慢,耗时:699毫秒,0秒,0分钟,listCount:200
//       ////传入guid形式的会报错，所以扩展为 by wxj
//       //Guid result = Guid.Empty;
//       //try
//       //{
//       //    var g = new Guid(obj == null ? "" : obj.ToString());
//       //    return ObjectToType<string>(g, obj.ToString());
//       //    //if (Guid.TryParse(obj == null ? "" : obj.ToString(), out result))
//       //    //{
//       //    //    return ObjectToType<string>(obj, obj.ToString());
//       //    //}
//       //}
//       //catch (Exception) { }
//       //return ObjectToType<string>(obj, string.Empty); 
//       #endregion
//   }
//   /// <summary>
//   /// 转换object为 T 值   
//   /// </summary>
//   /// <typeparam name="T">T 类型</typeparam>
//   /// <param name="obj">要被转换的值</param>
//   /// <returns>T 类型值</returns>
//   public static T ObjectToType<T>(object obj, T defaultValue)
//   {
//       if (obj == null)
//       {
//           return defaultValue;
//       }
//       else if (obj is T)
//       {
//           return (T)obj;
//       }
//       else
//       {
//           try
//           {
//               Type conversionType = typeof(T);
//               object obj2 = null;
//               if (conversionType.Equals(typeof(Guid)))
//                   obj2 = new Guid(Convert.ToString(obj));
//               else
//                   obj2 = Convert.ChangeType(obj, conversionType);
//               return (T)obj2;
//           }
//           catch (Exception)
//           {
//               return defaultValue;
//           }
//       }
//   }
//   #endregion
//
//   #region url

	/// <summary>
	/// 将查询字符串解析转换为名值集合.
	/// </summary>
	/// <param name="queryString"></param>
	/// <returns></returns>
	public static Map<String, String> GetQueryStringParams(String url) {
		// return GetQueryStringParams(queryString, null, true);
		ParseUrlUtil p = new ParseUrlUtil();
		p.parser(url);
		return p.strUrlParas;
	}

//   /// <summary>
//   /// 将查询字符串解析转换为名值集合.
//   /// </summary>
//   /// <param name="queryString"></param>
//   /// <returns></returns>
//   public static NameValueCollection GetQueryStringParams(string queryString)
//   {
//       return GetQueryStringParams(queryString, null, true);
//   }
//
//   /// <summary>
//   /// 将查询字符串解析转换为名值集合.
//   /// </summary>
//   /// <param name="queryString"></param>
//   /// <param name="encoding"></param>
//   /// <param name="isEncoded"></param>
//   /// <returns></returns>
//   public static NameValueCollection GetQueryStringParams(string queryString, Encoding encoding, bool isEncoded)
//   {
//       var qm = queryString.IndexOf('?');
//       if (qm > -1)
//       {
//           queryString = queryString.Substring(qm + 1);
//       }
//       //queryString = queryString.Replace("?", "");
//       NameValueCollection result = new NameValueCollection(StringComparer.OrdinalIgnoreCase);
//       if (!string.IsNullOrEmpty(queryString))
//       {
//           int count = queryString.Length;
//           for (int i = 0; i < count; i++)
//           {
//               int startIndex = i;
//               int index = -1;
//               while (i < count)
//               {
//                   char item = queryString[i];
//                   if (item == '=')
//                   {
//                       if (index < 0)
//                       {
//                           index = i;
//                       }
//                   }
//                   else if (item == '&')
//                   {
//                       break;
//                   }
//                   i++;
//               }
//               string key = null;
//               string value = null;
//               if (index >= 0)
//               {
//                   key = queryString.Substring(startIndex, index - startIndex);
//                   value = queryString.Substring(index + 1, (i - index) - 1);
//               }
//               else
//               {
//                   key = queryString.Substring(startIndex, i - startIndex);
//               }
//               if (isEncoded)
//               {
//                   result[MyUrlDeCode(key, encoding)] = MyUrlDeCode(value, encoding);
//               }
//               else
//               {
//                   result[key] = value;
//               }
//               if ((i == (count - 1)) && (queryString[i] == '&'))
//               {
//                   result[key] = string.Empty;
//               }
//           }
//       }
//       return result;
//   }
//
//   /// <summary>
//   /// 解码URL.
//   /// </summary>
//   /// <param name="encoding">null为自动选择编码</param>
//   /// <param name="str"></param>
//   /// <returns></returns>
//   public static string MyUrlDeCode(string str, Encoding encoding)
//   {
//       if (encoding == null)
//       {
//           Encoding utf8 = Encoding.UTF8;
//           //首先用utf-8进行解码                     
//           string code = HttpUtility.UrlDecode(str.ToUpper(), utf8);
//           //将已经解码的字符再次进行编码.
//           string encode = HttpUtility.UrlEncode(code, utf8).ToUpper();
//           if (str == encode)
//               encoding = Encoding.UTF8;
//           else
//               encoding = Encoding.GetEncoding("gb2312");
//       }
//       return HttpUtility.UrlDecode(str, encoding);
//   }
//   #endregion url
//
//
//   #region 浏览器
//
//   //public static RequestHostInfo GetRequestHostInfo(HttpRequestBase request)
//   //{
//   //    var result = new RequestHostInfo
//   //    {
//   //        OSVersion = PFDataHelper.GetOSVersion(request),//ok
//   //        Browser = PFDataHelper.GetBrowser(request)
//   //        //,//,ok
//   //        //IPAddress = PFDataHelper.GetIPAddress(request),
//   //        ,//,ok
//   //        IPAddress = request.UserHostAddress,
//
//   //    };
//
//   //    ////跨域访问时,这段代码报错:不知道这样的主机
//   //    //string HostName = string.Empty;
//   //    //string ip = string.Empty;
//   //    //string ipv4 = String.Empty;
//
//   //    //if (!string.IsNullOrEmpty(request.ServerVariables["HTTP_VIA"]))
//   //    //    ip = Convert.ToString(request.ServerVariables["HTTP_X_FORWARDED_FOR"]);
//   //    //if (string.IsNullOrEmpty(ip))
//   //    //    ip = request.UserHostAddress;
//
//   //    //// 利用 Dns.GetHostEntry 方法，由获取的 IPv6 位址反查 DNS 纪录，<br> // 再逐一判断何者为 IPv4 协议，即可转为 IPv4 位址。
//   //    //foreach (IPAddress ipAddr in Dns.GetHostEntry(ip).AddressList)
//   //    //{
//   //    //    if (ipAddr.AddressFamily.ToString() == "InterNetwork")
//   //    //    {
//   //    //        ipv4 = ipAddr.ToString();
//   //    //    }
//   //    //}
//   //    //result.HostName = Dns.GetHostEntry(ip).HostName;
//
//   //    return result;
//   //    //HostName = "主机名: " + Dns.GetHostEntry(ip).HostName + " IP: " + ipv4;
//   //}
//   #region 获取操作系统版本号
//
//   ///// <summary> 
//   ///// 获取操作系统版本号 
//   ///// </summary> 
//   ///// <returns></returns>
//
//   //public static string GetOSVersion(HttpRequestBase request)
//   //{
//   //    //UserAgent 
//   //    var userAgent = request.ServerVariables["HTTP_USER_AGENT"];
//
//   //    var osVersion = "未知";
//   //    if (userAgent.Contains("NT 10.0"))
//   //    {
//   //        osVersion = "Windows 10";
//   //    }
//   //    else if (userAgent.Contains("NT 6.3"))
//   //    {
//   //        osVersion = "Windows 8.1";
//   //    }
//   //    else if (userAgent.Contains("NT 6.2"))
//   //    {
//   //        osVersion = "Windows 8";
//   //    }
//
//   //    else if (userAgent.Contains("NT 6.1"))
//   //    {
//   //        osVersion = "Windows 7";
//   //    }
//   //    else if (userAgent.Contains("NT 6.0"))
//   //    {
//   //        osVersion = "Windows Vista/Server 2008";
//   //    }
//   //    else if (userAgent.Contains("NT 5.2"))
//   //    {
//   //        osVersion = "Windows Server 2003";
//   //    }
//   //    else if (userAgent.Contains("NT 5.1"))
//   //    {
//   //        osVersion = "Windows XP";
//   //    }
//   //    else if (userAgent.Contains("NT 5"))
//   //    {
//   //        osVersion = "Windows 2000";
//   //    }
//   //    else if (userAgent.Contains("NT 4"))
//   //    {
//   //        osVersion = "Windows NT4";
//   //    }
//   //    else if (userAgent.Contains("Me"))
//   //    {
//   //        osVersion = "Windows Me";
//   //    }
//   //    else if (userAgent.Contains("98"))
//   //    {
//   //        osVersion = "Windows 98";
//   //    }
//   //    else if (userAgent.Contains("95"))
//   //    {
//   //        osVersion = "Windows 95";
//   //    }
//   //    else if (userAgent.Contains("Mac"))
//   //    {
//   //        osVersion = "Mac";
//   //    }
//   //    else if (userAgent.Contains("Unix"))
//   //    {
//   //        osVersion = "UNIX";
//   //    }
//   //    else if (userAgent.Contains("Linux"))
//   //    {
//   //        osVersion = "Linux";
//   //    }
//   //    else if (userAgent.Contains("SunOS"))
//   //    {
//   //        osVersion = "SunOS";
//   //    }
//   //    return osVersion;
//   //}
//   #endregion
//   #region 获取IP地址
//
//   /////// <summary> 
//   /////// 获取IP地址
//   /////// </summary> 
//   /////// <returns></returns>
//
//   //public static string GetIPAddress(HttpRequestBase request)
//   //{
//   //    string ipv4 = String.Empty;
//   //    foreach (IPAddress IPA in Dns.GetHostAddresses(request.UserHostAddress))
//   //    {
//   //        if (IPA.AddressFamily.ToString() == "InterNetwork")
//   //        {
//   //            ipv4 = IPA.ToString();
//   //            break;
//   //        }
//   //    }
//   //    if (ipv4 != String.Empty)
//   //    {
//   //        return ipv4;
//   //    }
//   //    foreach (IPAddress IPA in Dns.GetHostAddresses(Dns.GetHostName()))
//   //    {
//   //        if (IPA.AddressFamily.ToString() == "InterNetwork")
//   //        {
//   //            ipv4 = IPA.ToString();
//   //            break;
//   //        }
//   //    }
//   //    return ipv4;
//   //}
//
//   #endregion
//   #region 获取浏览器版本号
//
//   ///// <summary> 
//   ///// 获取浏览器版本号 
//   ///// </summary> 
//   ///// <returns></returns> 
//   //public static string GetBrowser(HttpRequestBase request)
//   //{
//   //    HttpBrowserCapabilitiesBase bc = request.Browser;
//   //    return bc.Browser + bc.Version;
//   //}
//
//   #endregion
//   #endregion
//
//   #region 文件操作
//
//   ///// <summary>
//   ///// 保存文件,如果目录不存在会生成目录
//   ///// </summary>
//   ///// <param name="FromStream"></param>
//   ///// <param name="TargetFile"></param>
//   //public static void SaveStreamToFile(Stream FromStream, string TargetFile)
//   //{
//   //    // FromStream=the stream we wanna save to a file 
//   //    //TargetFile = name&path of file to be created to save to 
//   //    //i.e"c:\mysong.mp3" 
//   //    try
//   //    {
//   //        var pathWithoutFileName = TargetFile.Replace(Path.GetFileName(TargetFile), "");//如果目录不存在先生成目录,否则会报错--wxj20180713
//   //        if (!Directory.Exists(pathWithoutFileName))
//   //        {
//   //            Directory.CreateDirectory(pathWithoutFileName);
//   //        }
//   //        //Creat a file to save to
//   //        Stream ToStream = File.Create(TargetFile);
//
//   //        //use the binary reader & writer because
//   //        //they can work with all formats
//   //        //i.e images, text files ,avi,mp3..
//   //        BinaryReader br = new BinaryReader(FromStream);
//   //        BinaryWriter bw = new BinaryWriter(ToStream);
//
//   //        //copy data from the FromStream to the outStream
//   //        //convert from long to int 
//   //        bw.Write(br.ReadBytes((int)FromStream.Length));
//   //        //save
//   //        bw.Flush();
//   //        //clean up 
//   //        bw.Close();
//   //        br.Close();
//   //    }
//
//   //    //use Exception e as it can handle any exception 
//   //    catch (Exception e)
//   //    {
//   //        var aa = e;
//   //        //code if u like 
//   //    }
//   //}
//
	public static void EnsureFilePath(String filePath) {
		// PFDirectory.EnsureExists(filePath);

		String pathWithoutFileName = filePath.replace(SGPath.GetFileName(filePath), "");// 如果目录不存在先生成目录,否则会报错--wxj20180713

		SGDirectory.EnsureExists(pathWithoutFileName);
//       if (!Directory.Exists(pathWithoutFileName))
//       {
//           Directory.CreateDirectory(pathWithoutFileName);
//       }
	}

//
	/// <summary>
	/// 写字符串到txt(复盖)(多用于保存测试的字符)
	/// </summary>
	/// <param name="s"></param>
	/// <param name="filePath"></param>
	public static void SaveStringToFile(String s, String filePath) {
		EnsureFilePath(filePath);
		try (// FileWriter writer = new FileWriter(filePath,false);
				// BufferedWriter out = new BufferedWriter(writer)
				BufferedWriter out = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(new File(filePath)), SGDataHelper.encoding))// 如果不写utf8,在cmd运行时,保存的文件会变成ansi
		) {
			out.write(s); // \r\n即为换行
			out.flush(); // 把缓存区内容压入文件
			out.close();
		} catch (Exception e) {
		}

//       
//       //FileStream fs = new FileStream(filePath, FileMode.OpenOrCreate);
//       FileStream fs = new FileStream(filePath, FileMode.Create, FileAccess.Write);
//       var sw = new StreamWriter(fs);
//       sw.Write(s);
//       sw.Flush();
//       sw.Close();
	}

	public static String ReadFileToString(String filePath) {
		// EnsureFilePath(filePath);
		if (!SGDirectory.Exists(filePath)) {
			return null;
		}
		String text = "";
		try (BufferedReader out = new BufferedReader(new UnicodeReader(new FileInputStream(filePath), encoding))) {
			String b = null;
			while ((b = out.readLine()) != null) {
//				text+=b;
				text += (b + "\r\n");
			}
			out.close();
		} catch (Exception e) {
			WriteError(e);
		}

//       //中文乱码
//       try (FileReader writer = new FileReader(filePath);
//               BufferedReader out = new BufferedReader(writer)
//          ) {
//    	   String b = null;
//			while ((b = out.readLine()) != null) {
//				text+=b;
//			}
//              out.close();
//          }catch(Exception e) {
//        	  WriteError(e);
//          }       
//       
		return text;
	}

//   ///// <summary>
//   ///// 把文件(txt)读为多个字符串，支持逗号分隔或者每行一个（建议每行一个）
//   ///// </summary>
//   ///// <param name="filePath"></param>
//   ///// <returns></returns>
//   //public static string[] ReadFileToStringArray(string filePath)
//   //{
//   //    string text = string.Empty;
//   //    //System.Text.Encoding code = System.Text.Encoding.GetEncoding("gb2312");
//   //    using (var sr = new StreamReader(filePath, _encoding))
//   //    {
//   //        try
//   //        {
//   //            text = sr.ReadToEnd(); // 读取文件
//   //            sr.Close();
//   //        }
//   //        catch { }
//   //    }
//   //    return text;
//   //}
//   /// <summary>
//   /// 代替4.0的多path重载
//   /// </summary>
//   /// <param name="path"></param>
//   /// <returns></returns>
//   public static string PathCombine(params string[] path)
//   {
//       if (path == null || path.Length < 1) { return null; }
//       string r = path[0];
//       for (int i = 1; i < path.Length; i++)
//       {
//           r = Path.Combine(r, path[i]);
//       }
//       return r;
//   }
	public static String ReadLocalJson(String fileName) {
		String filePath = Paths.get(SGDataHelper.GetBaseDirectory(), "LocalData", "Json", fileName).toString();
		return ReadFileToString(filePath);
	}

	public static void WriteLocalJson(String s, String fileName) {
		String filePath = Paths.get(SGDataHelper.GetBaseDirectory(), "LocalData", "Json", fileName).toString();
		SaveStringToFile(s, filePath);
	}

	public static String ReadLocalJsonFromClassSource(String fileName) {
		Resource resource = new ClassPathResource("LocalData/Json/" + fileName);
		// File dumpFile = resource.getFile();
		String text = "";
		try {
//		BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			BufferedReader br = new BufferedReader(new UnicodeReader(resource.getInputStream(), encoding));
			String b = null;
			while ((b = br.readLine()) != null) {
				text += b;
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			WriteError(e);
		}
		return text;
//       //url =resource.getURL();
//       String filePath =Paths.get(PFDataHelper.GetBaseDirectory(), "LocalData", "Json", fileName).toString();
//       return ReadFileToString(filePath);
	}

	public static String ReadLocalTxt(String fileName) {
		String filePath = Paths.get(SGDataHelper.GetBaseDirectory(), "LocalData", "Txt", fileName).toString();
		return ReadFileToString(filePath);
	}

	public static void WriteLocalDataTable(SGDataTable dt, String fileName) {
		ArrayList<LinkedHashMap<String, Object>> list = dt.ToDictList();// PFDataHelper.DataTableToDictList(dt, false);
		// var json = DataTableConvertJson.DataTable2Json(dt);
		// var json = JsonConvert.SerializeObject(list);
		String json = JSON.toJSONString(list);
		SGDataHelper.WriteLocalJson(json, fileName);

//        //Console.Write(json);
//        String filename = Paths.get(PFDataHelper.GetBaseDirectory(), fileName);
//        FileStream fs = new FileStream(filename, FileMode.OpenOrCreate);
//        var sw = new StreamWriter(fs);
//        sw.Write(json);
//        sw.Flush();
//        sw.Close();
	}

	public static SGDataTable ReadLocalDataTable(String fileName) {
		String s = ReadLocalJsonFromClassSource(fileName);
		List<Map<String, Object>> resultType = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list = JSON.<List<Map<String, Object>>>parseObject(s, resultType.getClass());
		return DictListToDataTable(list);
	}

	public static void WriteLocalTxt(String s, String fileName) {
		String filePath = Paths.get(SGDataHelper.GetBaseDirectory(), "LocalData", "Txt", fileName).toString();
		SaveStringToFile(s, filePath);
	}

//	//移动sgJavaHelper
//	@Deprecated
//	public enum LocalDataType {
//		/**
//		 * 系统需要使用的本地数据,不可删除,如sql脚本之类(开发环境直接覆盖服务器的文件) 运行系统必需,永远不可清空
//		 */
//		System,
//		/**
//		 * 可删除的(一般是临时测试监听的文件) 非运行系统必需,系统初始化时可清空(应加入到.gitignore)
//		 */
//		Deletable,
//		/**
//		 * 用户使用系统时产生的文件,常作用户数据库之用(不可用开发环境覆盖服务器上的,如果文件格式有变,尽量考虑手工修改服务器上的格式为新格式,实现不行再用开发环境的复盖服务器的)
//		 * 非运行系统必需,系统初始化时可清空(应加入到.gitignore)
//		 */
//		User
//	}

	public static void WriteLocalTxt(String s, String fileName, LocalDataType dataType) {
		String filePath = Paths.get(SGDataHelper.GetBaseDirectory(), dataType.toString() + "LocalData", "Txt", fileName)
				.toString();
		SaveStringToFile(s, filePath);
	}

	public static String ReadLocalTxt(String fileName, LocalDataType dataType) {
		String filePath = Paths.get(SGDataHelper.GetBaseDirectory(), dataType.toString() + "LocalData", "Txt", fileName)
				.toString();
		return ReadFileToString(filePath);
	}

	/**
	 * 读取本地资源文件
	 * 
	 * @param fileFullPath
	 * @return
	 */
	public static String ReadLocalResource(String fileFullPath) {
		// 获取最后一个.的位置
		int lastIndexOf = fileFullPath.lastIndexOf(".");
		// 获取文件的后缀名 .jpg
		String suffix = fileFullPath.substring(lastIndexOf);
		String fileNameWithoutSuffix = fileFullPath.substring(0, lastIndexOf);

		// java.net.URL path =
		// Thread.currentThread().getContextClassLoader().getResource(fileFullPath);//"jdbcconfig.yml");//
		java.net.URL path = null;
		if (PFEnvir.release == SGDataHelper.CurrentEnvironmental) {
			path = Thread.currentThread().getContextClassLoader().getResource(fileFullPath);
		} else {
			path = Thread.currentThread().getContextClassLoader()
					.getResource(fileNameWithoutSuffix + "_" + SGDataHelper.CurrentEnvironmental.toString() + suffix);// "jdbcconfig.yml");//
			if (path == null) {
				path = Thread.currentThread().getContextClassLoader().getResource(fileFullPath);
			}
		}
		String text = "";
		try (BufferedReader out = new BufferedReader(new UnicodeReader(path.openStream(), SGDataHelper.encoding))) {
			String b = null;
			while ((b = out.readLine()) != null) {
				text += (b + "\r\n");
			}

			out.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return text;
	}

	/**
	 * 读取资源文件为字符串(替换环境变量)
	 * 
	 * @param fileFullPath 如"testYml.yml"
	 * @return
	 */
	public static String ReadLocalResourceWithEnvironmentVariable(String fileFullPath) {
		String s = ReadLocalResource(fileFullPath);

		Map<String, String> map = System.getenv();
		for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext();) {
			String key = itr.next();
			// System.out.println(key + "=" + map.get(key));
			s = s.replace("${" + key + "}", map.get(key));
			s = s.replace("${" + key + ":}", map.get(key));// 为了兼容spring的习惯冒号表示有黑夜值
		}
		return s;
	}

//
	// 读写锁，当资源处于写入模式时，其他线程写入需要等待本次写入结束之后才能继续写入
	private static ReadWriteLock LogWriteLock = new ReentrantReadWriteLock();

	private static <TMsg> void DoWriteLog(TMsg e, String filePrev) {// 一定要对log目录设置everyone权限
		new Thread() {// 线程操作
			public void run() {
				try {
					// //设置读写锁为写入模式独占资源，其他写入请求需要等待本次写入结束之后才能继续写入
					// //注意：长时间持有读线程锁或写线程锁会使其他线程发生饥饿 (starve)。
					// 为了得到最好的性能，需要考虑重新构造应用程序以将写访问的持续时间减少到最小。
					// // 从性能方面考虑，请求进入写入模式应该紧跟文件操作之前，在此处进入写入模式仅是为了降低代码复杂度
					// // 因进入与退出写入模式应在同一个try finally语句块内，所以在请求进入写入模式之前不能触发异常，否则释放次数大于请求次数将会触发异常
					LogWriteLock.writeLock().lock();
					String server = SGDataHelper.GetBaseDirectory();// AppDomain.CurrentDomain.BaseDirectory;

					Path pDirPath = Paths.get(server, "log");
					String dirPath = pDirPath.toString();
					String logPath = Paths
							.get(dirPath, FormatString("{0}_{1}.txt", filePrev == null ? "pfError" : filePrev,
									ObjectToDateString(Calendar.getInstance(), "yyyyMMdd")))
							.toString();

					// System.out.println("DoWriteLog");
					// System.out.println(logPath);
					SGDirectory.EnsureExists(dirPath);
//                   if (!Directory.Exists(dirPath)) { Directory.CreateDirectory(dirPath); }

					// 这样好像是复盖
//                   try (FileWriter writer = new FileWriter(logPath,false);
//                           BufferedWriter out = new BufferedWriter(writer)
//                      ) {
////                          out.write(FormatString("\r\ntime:[{0}]\r\n{1}\r\n", PFDate.Now().toString(), e)); // \r\n即为换行
//                          out.append(FormatString("\r\ntime:[{0}]\r\n{1}\r\n", PFDate.Now().toString(), e)); // \r\n即为换行
//                          out.flush(); // 把缓存区内容压入文件
//                          out.close();
//                      }catch(Exception e) {}
//                   
////                   var sw = new StreamWriter(logPath, true);
////                   sw.WriteLine(string.Format("\r\ntime:[{0}]\r\n{1}\r\n", DateTime.Now, e));//开始写入值
////                   sw.Flush();
////                   sw.Close();

//					// 改为append方式
//					BufferedWriter out = null;
//					try {
//						out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logPath, true)));
//						out.write(FormatString("\r\ntime:[{0}]\r\n{1}\r\n", PFDate.Now().toString(), e));
//					} catch (Exception e) {
//						e.printStackTrace();
//					} finally {
//						try {
//							out.close();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}

					// 改为append方式
					try (FileOutputStream fos = new FileOutputStream(logPath, true);
							// OutputStreamWriter osw=new OutputStreamWriter(fos);
							OutputStreamWriter osw = new OutputStreamWriter(fos, encoding);
							BufferedWriter out = new BufferedWriter(osw);) {
						out.write(FormatString("\r\ntime:[{0}]\r\n{1}\r\n", SGDate.Now().toString(), e));
						out.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
				} finally {
					// //退出写入模式，释放资源占用
					// //注意：一次请求对应一次释放
					// // 若释放次数大于请求次数将会触发异常[写入锁定未经保持即被释放]
					// // 若请求处理完成后未释放将会触发异常[此模式不下允许以递归方式获取写入锁定]
					LogWriteLock.writeLock().unlock();
				}
			}
		}.start();

	}

	/**
	 * 改用WriteError(Throwable errorLine,Exception e)
	 * 
	 * @param e
	 */
	@Deprecated
	public static void WriteError(Exception e) {// 一定要对log目录设置everyone权限
		e.printStackTrace();
		DoWriteLog(e, "pfError");
	}

	private static String getLineInfo(Throwable t) {
		StackTraceElement ste4 = t.getStackTrace()[0];
		return (ste4.getFileName() + ": Line " + (ste4.getLineNumber()));
	}

//	/**
//	 * t是为了强制获得行号(有时e里少了堆信息)
//	 */
//	public static void WriteError(Throwable t,Exception e) {
//		e.printStackTrace();
//		//e.addSuppressed(t);
//		String msg=FormatString(
//				"{0}\r\n"+
//		"{1}\r\n",getLineInfo(t),e);
//		DoWriteLog(new Exception(msg), "pfError");
//		//DoWriteLog(e, "pfError");//这里如果用Exception.getMessage()反而会没有值
////		DoWriteLog(new Exception(getLineInfo(t)+"\r\n",e), "pfError");//这里如果用Exception.getMessage()反而会没有值
//	}
	/**
	 * errorLine是为了强制获得行号(有时e里少了堆信息,直接传入new Throwable()即可)
	 * 使用方法:PFDataHelper.WriteError(new Throwable(),e);
	 */
	public static void WriteError(Throwable errorLine, Exception e) {
		// e.printStackTrace();
		try (final Writer result = new StringWriter(); final PrintWriter printWriter = new PrintWriter(result);) {
//			e.fillInStackTrace().printStackTrace(printWriter);//这句总是报空指针
//			String s=result.toString();
			String s = "";
			if (e == null) {
				DoWriteLog("PFDataHelper.WriteError()待优化,e为null", "pfError");
			} else {
				//这种方式的话，s的位置会变成调胳膊PFDataHelper.WriteError()的那一行,不记得当时为什么要用fillInStackTrace这个方法--benjamin 20210625
//				Throwable trace = e.fillInStackTrace();
//				if (trace == null) {
//					DoWriteLog("PFDataHelper.WriteError()待优化,trace为null", "pfError");
//				} else {
//					trace.printStackTrace(printWriter);
//					s = result.toString();
//				}
				
				e.printStackTrace(printWriter);
				s = result.toString();
			}
//			System.out.println(result.toString());
			String msg = FormatString("----errorLine----\r\n{0}\r\n----errorMessage----\r\n{1}\r\n\r\n", getLineInfo(errorLine), s);
			// 保存到文件
			DoWriteLog(msg, "pfError");
			printWriter.close();
			result.close();
			// 打印到系统
			System.err.print(s);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static String GetErrorFullString(Exception e) {
		try (final Writer result = new StringWriter(); final PrintWriter printWriter = new PrintWriter(result);) {
			e.fillInStackTrace().printStackTrace(printWriter);
			String msg = result.toString();
			printWriter.close();
			result.close();
			return msg;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}

	public static void WriteLog(String msg) {// 一定要对log目录设置everyone权限
		DoWriteLog(msg, "pfLog");
	}
//   #endregion
//   //public static object GetSystemUserData(string userId)
//   //{
//
//   //    if (PFDataHelper.StringIsNullOrWhiteSpace(userId)) { return null; }
//   //    return Caching.Get(userId);
//   //}
//   #region old

	/**
	 * 获得功能权限表(如功能A里面有Insert等按钮的功能)--wxj20180907
	 * 功能码格式如：YJQ_BaseData_FCmonLimit_Index.Add
	 * @param authorites
	 * @return
	 */
	public static Map<String, FuncAuthorityClass> GetFuncAuthorityClass(List<String> authorites) {
		// var purviewIdsArr = GetUserActions().Actions;
		List<String> purviewIdsArr = authorites;
		// var purviewIdsArr = purviewIds.Split(new char[] { ',' },
		// StringSplitOptions.RemoveEmptyEntries);
		Map<String, FuncAuthorityClass> result = new HashMap<String, FuncAuthorityClass>();
//       var authType = typeof(FuncAuthorityClass);
//       var authorities = Enum.GetNames(authType);
		String[] authorities = FuncAuthorityClass.Default.GetAllTexts();
		for (String i : purviewIdsArr) {
			String code = i;
			// if (code[0] == '\'') { code = code.SubString(1, code.Length - 1); }
			// if (code[code.Length - 1] == '\'') { code = code.SubString(0, code.Length -
			// 1); }

			// var code = i.Number;
			String authority = GetAuthorityByFuncCode(code, authorities);
			if (authority != null) {
				String pureCode = code.substring(0, code.length() - authority.length() - 1);// 去掉功能码后的纯编码
				FuncAuthorityClass v = FuncAuthorityClass.EnumParse(FuncAuthorityClass.class, authority);
				if (!result.containsKey(pureCode)) {
					result.put(pureCode, v);
				} else {
					// result[pureCode] = result[pureCode] &
					// (~FuncAuthorityClass.Default);//去掉Default项(如果存在).Default改用0值后,经过|运算会自动去掉
					// result.get(pureCode).Or(v);
					result.put(pureCode, result.get(pureCode).Or(v));
				}
			} else if (!result.containsKey(code)) {
				result.put(code, FuncAuthorityClass.Default);
			}
		}
		return result;
	}

//   #endregion
//

	/**
	 * 获得功能权限表(如功能A里面有Insert等按钮的功能)--wxj20180907
	 * 功能码格式如：YJQ_BaseData_FCmonLimit_Index.Add
	 * @param authorites
	 * @return
	 */
	public static Map<String, FuncAuthorityClass> GetFuncAuthorityClass(List<String> purviewIdsArr,
			SGRef<Map<String, List<String>>> rOtherAuthorities) {
		Map<String, List<String>> otherAuthorities = new HashMap<String, List<String>>();

		Map<String, FuncAuthorityClass> result = new HashMap<String, FuncAuthorityClass>();
//       var authType = typeof(FuncAuthority);
		for (String i : purviewIdsArr) {
			String code = i;
			String pattern = "\\.[^\\._]+$";
//           Boolean m = Pattern.matches("\\.[^\\._]+$",code);

			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(code); // 获取 matcher 对象
			if (m.find()) {
				String authority = m.group(0).replace(".", "");
				String pureCode = code.substring(0, code.length() - authority.length() - 1);// 去掉功能码后的纯编码

				FuncAuthorityClass v = FuncAuthorityClass.Default;
				v = FuncAuthorityClass.EnumParse(FuncAuthorityClass.class, authority);
				if (v != null) {
					if (!result.containsKey(pureCode)) {
						result.put(pureCode, v);
					} else {
						// result.get(pureCode).Or(v);
						result.put(pureCode, result.get(pureCode).Or(v));
					}
				} else {
					if (!otherAuthorities.containsKey(pureCode)) {
						otherAuthorities.put(pureCode, new ArrayList<String>());
					}
					otherAuthorities.get(pureCode).add(authority);
				}

			} else if (!result.containsKey(code)) {
				result.put(code, FuncAuthorityClass.Default);
			}
		}
		return result;
	}

	/// <summary>
	/// 根据功能码(如RiskManage.FXSJK.Add)找功能的权限码(如Add)
	/// </summary>
	/// <param name="funcCode"></param>
	/// <param name="authorities"></param>
	/// <returns></returns>
	private static String GetAuthorityByFuncCode(String funcCode, String[] authorities) {
		for (String i : authorities) {

			int idx = funcCode.indexOf(i);

			if (funcCode.length() > i.length()// 有这种特殊情况
					&& idx == funcCode.length() - i.length() && funcCode.charAt(idx - 1) == '.'// 排除特殊情况"AgentManage.Agent__DataDelete"
			) {
				return i;
			}

		}
		return null;
	}

//	private static double getAngle(PFPoint start, PFPoint end) {
//
//		int diff_x = end.x - start.x;
//		int diff_y = end.y - start.y;
//		double angle = 360.0D * Math.atan(Double.valueOf(diff_y).doubleValue() / diff_x) / 6.283185307179586D;
//		if (diff_x < 0 && diff_y > 0) {
//			angle += 180;
//			// return (360.0D * Math.atan(Double.valueOf(diff_y).doubleValue() / diff_x) /
//			// 6.283185307179586D)+180;
//		}
//		if (diff_x < 0 && diff_y < 0) {
//			angle -= 180;
//			// return (360.0D * Math.atan(Double.valueOf(diff_y).doubleValue() / diff_x) /
//			// 6.283185307179586D)-180;
//		}
//		return angle;
////	    return 360.0D * Math.atan(Double.valueOf(diff_y).doubleValue() / diff_x) / 6.283185307179586D;
//	}

	private static void DebugPrint(Object s) {
	}

//	/**
//	 * 把图片设置为背景图尺寸 imgSize可以为空
//	 */
//	public static String backgroundImg(Dimension backgroundSize, Image image1, Dimension imgSize, PFLine diagonalLine,
//			Color backgroundColor, Boolean flip) {
//
//		if (image1 instanceof BufferedImage && imgSize == null) {
//			BufferedImage bfImg = PFDataHelper.ObjectAs(image1);
//			imgSize = new Dimension(bfImg.getWidth(), bfImg.getHeight());
//		}
//		if (flip) {//
//			int newSX = imgSize.width - diagonalLine.e.x;
//			int newEX = imgSize.width - diagonalLine.s.x;
//			diagonalLine.s.x = newSX;
//			diagonalLine.e.x = newEX;
//		}
////		if (image1 instanceof BufferedImage && imgSize == null) {//flip用到imgSize,所以要移到前面--benjamin 20210523
////			BufferedImage bfImg = PFDataHelper.ObjectAs(image1);
////			imgSize = new Dimension(bfImg.getWidth(), bfImg.getHeight());
////		}
//
//		diagonalLine.CaculateLineByType(backgroundSize, imgSize);
//
//		int pixelTatio = 1;// 和前端js不一样。前端是生成4倍大的Image用style显示原来大小；后端java如果想更清晰也要生成4倍大的Image,但感觉这样前端调用不方便，所以模糊点也算了.结论是如果不生成放大4倍的Image,都会变模糊，不要浪费时间想了
//		Canvas canvas = new Canvas();
//		Graphics ctx1 = canvas.getGraphics();
//		int w = imgSize.width;
//		int h = imgSize.height;
//		PFLine line = diagonalLine;
//		double l = Math.sqrt(Math.pow((line.e.x - line.s.x), 2.0D) + Math.pow((line.e.y - line.s.y), 2.0D));
//		DebugPrint("l:" + l);
//		int wWidth = 0;
//		int wHeight = 0;
//		wWidth = backgroundSize.width;
//		wHeight = backgroundSize.height;
//		DebugPrint("wWidth:" + wWidth + "  wHeight" + wHeight);
//		double diagonal = Math.sqrt(Math.pow(wWidth, 2.0D) + Math.pow(wHeight, 2.0D));
//		DebugPrint("diagonal:" + diagonal);
//		double width = diagonal * w / l;
//		double height = diagonal * h / l;
//		double angleOfDifference = getAngle(new PFPoint(0, 0), new PFPoint(wWidth, wHeight))
//				- getAngle(new PFPoint(line.s.x, line.s.y), new PFPoint(line.e.x, line.e.y));
//		DebugPrint("angleOfDifference:" + angleOfDifference);// 角度算错了，-90算成90
//		// angleOfDifference=-90;
//		canvas.setSize(new Dimension(wWidth, wHeight));
//		BufferedImage paintBi = new BufferedImage(wWidth * pixelTatio, wHeight * pixelTatio, 1);
//		ctx1 = paintBi.createGraphics();
//		((Graphics2D) ctx1).setColor(backgroundColor);
//		ctx1.fillRect(0, 0, wWidth * pixelTatio, wHeight * pixelTatio);
//		// ((Graphics2D)ctx1).rotate(angleOfDifference * Math.PI / 180.0D);
////    if(angleOfDifference>90) {
////    	angleOfDifference-=180;
////    }
//		// angleOfDifference=88.0D;
//		// ((Graphics2D)ctx1).rotate(Math.toRadians(angleOfDifference));//90可以 -24可以
//		// 147不显示图像，所以把147减去180
//		((Graphics2D) ctx1).rotate(angleOfDifference * Math.PI / 180.0D);// 不加toRadians的话，逆时针旋转90度以上时会显示不处图像
//																			// https://www.runoob.com/java/number-cos.html
//		DebugPrint("rotate:" + (angleOfDifference * Math.PI / 180.0D));
//		int drawX = (new Double(-(line.s.x * diagonal * pixelTatio / l))).intValue();
//		int drawY = (new Double(-(line.s.y * diagonal * pixelTatio / l))).intValue();
//		int drawWidth = (new Double(width * pixelTatio)).intValue();
//		int drawHeight = (new Double(height * pixelTatio)).intValue();
//
//		if (flip) {// 水平翻转
//			((Graphics2D) ctx1).scale(-1, 1);
//			ctx1.drawImage(image1, -drawX - drawWidth, drawY, drawWidth, drawHeight, canvas);
//			// ctx1.drawImage(image1, drawX-new Double(wHeight).intValue(), drawY,
//			// drawWidth, drawHeight, canvas);
//		} else {
//			ctx1.drawImage(image1, drawX, drawY, drawWidth, drawHeight, canvas);
//		}
//		DebugPrint("draw:");
//		DebugPrint(Integer.valueOf(drawX));
//		DebugPrint(Integer.valueOf(drawY));
//		DebugPrint(Integer.valueOf(drawWidth));
//		DebugPrint(Integer.valueOf(drawHeight));
//		canvas.printAll(ctx1);
//
//		try {
//			String tmpImgPath = Paths
//					.get(GetBaseDirectory(), new String[] { "tmpfile\\tmpimg_" + GetNewUniqueHashId() + ".jpg" })
//					.toString();
//			PFDirectory.EnsureExists(tmpImgPath);
//			File file2 = new File(tmpImgPath);
//			ImageIO.write(paintBi, "jpg", file2);
//			ctx1.dispose();
//			return tmpImgPath;
//		} catch (Exception ex) {
//			DebugPrint("Error saving");
//			return null;
//		}
//	}

//
//   #region old,20190617备份
//   //public static string HttpPost(string url, string body, Action<HttpWebRequest> requestAction = null)
//   //{
//   //    //ServicePointManager.ServerCertificateValidationCallback = new RemoteCertificateValidationCallback(CheckValidationResult);
//   //    Encoding encoding = Encoding.UTF8;
//   //    HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
//   //    request.Method = "POST";
//   //    request.Accept = "text/html, application/xhtml+xml, */*";
//   //    request.ContentType = "application/json";
//   //    if (requestAction != null) { requestAction(request); }
//
//   //    if (!PFDataHelper.StringIsNullOrWhiteSpace(body))
//   //    {
//   //        byte[] buffer = encoding.GetBytes(body);
//   //        request.ContentLength = buffer.Length;
//   //        request.GetRequestStream().Write(buffer, 0, buffer.Length);
//   //    }
//   //    HttpWebResponse response = (HttpWebResponse)request.GetResponse();
//   //    using (StreamReader reader = new StreamReader(response.GetResponseStream(), Encoding.UTF8))
//   //    {
//   //        var r = reader.ReadToEnd();
//   //        if (response != null)
//   //        {
//   //            response.Close();
//   //        }
//   //        if (request != null)
//   //        {
//   //            request.Abort();
//   //        }
//   //        return r;
//   //    }
//   //} 
//   #endregion
//
//   /// <summary>
//   /// 
//   /// </summary>
//   /// <param name="url"></param>
//   /// <param name="body"></param>
//   /// <param name="requestAction"></param>
//   /// <param name="keepAlive"></param>
//   /// <param name="cookie">当多次post时,keepAlive应为true,且给cookie的值(测试知java开源的springCloud转发方式用此方式很慢,还会无影响)</param>
//   /// <returns></returns>
//   public static string HttpPost(string url, string body, Action<HttpPostOption> postOptionAction = null)//, Action<HttpWebRequest> requestAction = null, bool keepAlive = true, CookieContainer cookie = null)
//   {
//       var postOption = new HttpPostOption();
//       if (postOptionAction != null) { postOptionAction(postOption); }
//
//       //ServicePointManager.ServerCertificateValidationCallback = new RemoteCertificateValidationCallback(CheckValidationResult);
//       Encoding encoding = Encoding.UTF8;
//       HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
//       request.Method = "POST";
//       request.UserAgent = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.2; .NET CLR 1.1.4322; .NET CLR 2.0.50727)";
//       request.Accept = "text/html, application/xhtml+xml, */*";
//       //request.ContentType = "application/json";
//       //request.ContentType = "application/x-www-form-urlencoded; charset=UTF-8";
//       request.ContentType = "application/json;charset=UTF-8";
//       //if (requestAction != null) { requestAction(request); }
//       if (postOption != null)
//       {
//           request.KeepAlive = postOption.KeepAlive;
//           if (postOption.Cookie != null)
//           {
//               request.CookieContainer = postOption.Cookie;
//           }
//           if (postOption.Header != null)
//           {
//               foreach (var head in postOption.Header)
//               {
//                   request.Headers.Add(head.Key, head.Value);
//               }
//           }
//       }
//       if (!PFDataHelper.StringIsNullOrWhiteSpace(body))
//       {
//           byte[] buffer = encoding.GetBytes(body);
//           request.ContentLength = buffer.Length;//报错:基础连接已经关闭: 连接被意外关闭。(好像这个错误不是这句引起的)
//           Stream newStream = request.GetRequestStream();
//           newStream.Write(buffer, 0, buffer.Length);
//           newStream.Close();
//       }
//       HttpWebResponse response = (HttpWebResponse)request.GetResponse();
//       if (postOption != null && postOption.Cookie != null) { response.Cookies = postOption.Cookie.GetCookies(response.ResponseUri); }
//       using (StreamReader reader = new StreamReader(response.GetResponseStream(), encoding))
//       {
//           var r = reader.ReadToEnd();
//           if (response != null)
//           {
//               response.Close();
//           }
//           if (request != null)
//           {
//               request.Abort();
//           }
//           if (postOption != null && (!postOption.KeepAlive))
//           {
//               System.GC.Collect();
//           }
//           return r;
//       }
//   }
//   public static string HttpGet(string url)
//   {
//       //ServicePointManager.ServerCertificateValidationCallback = new RemoteCertificateValidationCallback(CheckValidationResult);
//       Encoding encoding = Encoding.UTF8;
//       HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
//       request.Method = "GET";
//       request.Accept = "text/html, application/xhtml+xml, */*";
//       request.ContentType = "application/json";
//
//       HttpWebResponse response = (HttpWebResponse)request.GetResponse();
//       using (StreamReader reader = new StreamReader(response.GetResponseStream(), Encoding.UTF8))
//       {
//           return reader.ReadToEnd();
//       }
//   }

	/// <summary>
	/// jquery不能用作data的特殊字符
	/// </summary>
	private static Map<String, String> _specDataChar = new HashMap<String, String>() {
		/**
		* 
		*/
		private static final long serialVersionUID = -8204835002925766188L;

		{
			// new KeyValuePair<string, string>(".","_"),
			// new KeyValuePair<string, string>("[","_lz_"),
			// new KeyValuePair<string, string>("]","_rz_")
			// 上面那样不够稳,因为有的字段本身就有_下划线,而不是.替换过来的(应保证字段名不会使用这些转换后的value值)
			put(".", "__p__");
			put("[", "__lz__");
			put("]", "__rz__");
		}
	};

	private static String EncodeDataChar(String s) {
		Iterator<Entry<String, String>> iter = _specDataChar.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> key = iter.next();
			s = s.replace(key.getKey(), key.getValue());
		}
//        _specDataChar.ForEach(a =>
//        {
//            s = s.Replace(a.Key, a.Value);
//        });
		return s;
	}

//   /// <summary>
//   /// jquery不能用作data的特殊字符
//   /// </summary>
//   private static List<KeyValuePair<string, string>> _specDataChar = new List<KeyValuePair<string, string>>() {
//           new KeyValuePair<string, string>(".","_"),
//           new KeyValuePair<string, string>("[","_lz_"),
//           new KeyValuePair<string, string>("]","_rz_")
//       };
//   private static string EncodeDataChar(string s)
//   {
//       _specDataChar.ForEach(a =>
//       {
//           s = s.Replace(a.Key, a.Value);
//       });
//       return s;
//   }
//   private static string DecodeDataChar(string s)
//   {
//       _specDataChar.ForEach(a =>
//       {
//           s = s.Replace(a.Value, a.Key);
//       });
//       return s;
//   }
	private static Object GetColumnSummary(SGDataTable dt, String columnName, SummaryType summaryType) {
		switch (summaryType) {
		case Average:
			// return dt.Rows.size() < 1 ? 0 : Math.Round((PFDataHelper.ColumnTotal(dt,
			// columnName) / dt.Rows.Count), DecimalPrecision);

			BigDecimal bd = SGDataHelper.ColumnTotal(dt, columnName).divide(new BigDecimal(dt.Rows.size()));
			// 设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
			bd = bd.setScale(DecimalPrecision, BigDecimal.ROUND_HALF_UP);
			return dt.Rows.size() < 1 ? 0 : bd;
		default:
			return SGDataHelper.ColumnTotal(dt, columnName);
		}
	}

//   private static object GetColumnSummary(DataTable dt, string columnName, SummaryType summaryType)
//   {
//       switch (summaryType)
//       {
//           case SummaryType.Average:
//               return dt.Rows.Count < 1 ? 0 : (PFDataHelper.ColumnTotal(dt, columnName) / dt.Rows.Count);
//           default:
//               return PFDataHelper.ColumnTotal(dt, columnName);
//       }
//   }
//
	/// <summary>
	/// var random = new Random();
	/// </summary>
	/// <param name="random"></param>
	/// <param name="length"></param>
	/// <returns></returns>
	public static String GetRandomNo(SGRef<Random> random, int length) {
		String r = "";
		for (int i = 0; i < length; i++) {
			r += String.valueOf(random.GetValue().nextInt(9));
		}
		return r;
	}

	public static PagingResult PagingStore(SGDataTable dataTable, PagingParameters p, StoreColumnCollection header,
			Boolean setWidthByHeaderWord, String xmlDataSetName) {
		if (dataTable == null) {
			return null;
		}

		if (p.getPageSize() == null) {
			p.setPageSize(dataTable.Rows.size());
		}
		SGDataTable[] all = new SGDataTable[] { dataTable };
		PFDataColumnCollection columns = dataTable.Columns;

		List<String> srcColumnNames = new ArrayList<String>();// 自定义group之后，目标head中可能有不存在的列--ben20190627
		for (PFDataColumn dataColumn : columns) {
			srcColumnNames.add(dataColumn.getKey());
		}

		StoreColumnCollection[] headerRef = new StoreColumnCollection[] { header };
		// StoreColumnCollection[] headerRef=new StoreColumnCollection[] {null};

		// Boolean isPageSql = all.Rows.size() > 0 && all.Columns["rowtotal"] != null;
		Boolean isPageSql = all[0].Rows.size() > 0
				&& SGDataHelper.ListAny(all[0].Columns, a -> "rowtotal".equals(a.getKey()));
		int total = 0;
		SGDataTable dt = null;
		if (isPageSql) {
			total = all[0].Rows.get(0).getIntColumn("rowtotal");
			dt = all[0];
		} else {
			if (p.OnlyNeedOneRow != true) {
				if (!SGDataHelper.StringIsNullOrWhiteSpace(p.getSort())) {
					try {
//                        all.DefaultView.Sort = DecodeDataChar(p.Sort);
//                        all = all.DefaultView.ToTable();
					} catch (Exception e) {
						WriteError(e);
					}
				}
				if (!SGDataHelper.StringIsNullOrWhiteSpace(p.get_filterValue())) {
					all[0] = DataTableFilter(all[0], p.get_filterValue());
				}
				total = all[0].Rows.size();// 一定要过滤完再计算total
				dt = DataPager(all[0], p.getPageIndex(), p.getPageSize());// 注意分页后的columns丢了ExtendedProperties
			} else {
				dt = DataPager(all[0], p.getPageIndex(), 1);
			}
		}
		// int total = all.Rows.Count>0&&all.Rows[0]["rowtotal"]!=null?
		// (int)all.Rows[0]["rowtotal"] : all.Rows.Count;
		if (dt != null) {
			ArrayList<Map<?, ?>> arrayList = new ArrayList<Map<?, ?>>();

			for (PFDataRow dataRow : dt.Rows) {
				Map<String, Object> dictionary = new LinkedHashMap<String, Object>();
				if (headerRef[0] == null) {
					for (PFDataColumn dataColumn : columns) {
						//// dictionary.Add(dataColumn.ColumnName.IndexOf(".") > -1 ?
						//// dataColumn.ColumnName.Replace(".", "_") : dataColumn.ColumnName
						//// , dataRow[dataColumn.ColumnName].ToString());
						// dictionary.Add(EncodeDataChar(dataColumn.ColumnName)
						// , dataRow[dataColumn.ColumnName].ToString());
						dictionary.put(EncodeDataChar(dataColumn.getKey()), dataRow.getColumn(dataColumn.getKey()));
					}
				} else {
					// SGRef<StoreColumnCollection> headerRef=new
					// SGRef<StoreColumnCollection>(header);
					(new StoreColumn() {
						{
							SetChildren(headerRef[0]);
						}
					}).EachLeaf(a -> {
						if (srcColumnNames.contains(a.data)) {
							dictionary.put(EncodeDataChar(a.data), dataRow.getColumn(a.data));
						} else {
							dictionary.put(EncodeDataChar(a.data), null);
							a.set_visible(false);
						}
					});
				}
				arrayList.add(dictionary);

			}

			if (headerRef[0] == null) {
				headerRef[0] = new StoreColumnCollection();
				// header =xmlDataSetName==null?new StoreColumnCollection(): new
				// StoreColumnCollection(xmlDataSetName);
				// var modelConfig = PFDataHelper.GetMultiModelConfig(xmlDataSetName);
				PFModelConfigCollection modelConfig = xmlDataSetName == null ? null
						: SGDataHelper.GetMultiModelConfig(xmlDataSetName);
				for (PFDataColumn dataColumn : columns) {
					StoreColumn dictionary = modelConfig == null ? new StoreColumn(dataColumn)
							: new StoreColumn(dataColumn, modelConfig.get(dataColumn.getKey()));
					// if (dataColumn.ColumnName.IndexOf(sc[0].Key) > -1)
					// {
					// dictionary.title = dataColumn.ColumnName;//前端jqueryDatables不支持.
					// dictionary.data = dataColumn.ColumnName.Replace(sc[0].Key,sc[0].Value);
					// }
					// else
					// {
					// dictionary.data = dataColumn.ColumnName;
					// }
					// if (_specDataChar.Any(a -> dataColumn.ColumnName.IndexOf(a.Key) > -1))
					if (_specDataChar.containsKey(dataColumn.getKey())) {
						dictionary.title = dataColumn.getKey();// 前端jqueryDatables不支持.
						dictionary.data = EncodeDataChar(dataColumn.getKey());
					} else {
						dictionary.data = dataColumn.getKey();
					}

					// 宽度的优先级:setWidthByHeaderWord<modelConfig<ExtendedProperties
					// PFModelConfig config = modelConfig == null ? null :
					// modelConfig[dataColumn.ColumnName];
					// dictionary.SetPropertyByModelConfig(config);

					if (dataColumn.ExtendedProperties.containsKey("title")) {
						dictionary.title = dataColumn.ExtendedProperties.get("title").toString();
					} // 20180803
					if (SGDataHelper.StringIsNullOrWhiteSpace(dictionary.title)) {
						dictionary.title = dataColumn.getKey();
					}
					if (dataColumn.ExtendedProperties != null && dataColumn.ExtendedProperties.containsKey("width")) {
						dictionary.width = dataColumn.ExtendedProperties.get("width").toString();
					} else if (setWidthByHeaderWord && SGDataHelper.StringIsNullOrWhiteSpace(dictionary.width))// 设置为中文后进入这里才有意义
					{
						dictionary.SetWidthByTitleWords();

					}

					if (dataColumn.ExtendedProperties != null
							&& dataColumn.ExtendedProperties.containsKey("dataType")) {
						dictionary.dataType = dataColumn.ExtendedProperties.get("dataType").toString();
					}
					if (dataColumn.ExtendedProperties != null && dataColumn.ExtendedProperties.containsKey("visible")) {
						dictionary
								.set_visible(Boolean.valueOf(dataColumn.ExtendedProperties.get("visible").toString()));
					}
					if (dataColumn.ExtendedProperties != null) {
						if (dataColumn.ExtendedProperties.containsKey("summary")) {
							dictionary.summary = dataColumn.ExtendedProperties.get("summary").toString();
						} else if (dataColumn.ExtendedProperties.containsKey("hasSummary") && Boolean
								.valueOf(dataColumn.ExtendedProperties.get("hasSummary").toString()) == true) {
							dictionary.summary = GetColumnSummary(all[0], dataColumn.getKey(),
									dictionary.get_summaryType());
						}
					}
					headerRef[0].add(dictionary);
				}
				if (modelConfig != null) {
					modelConfig.dispose();
					modelConfig = null;
				}
				// GC.Collect();
			} else// head不为null时
			{
				// 如果有head的情况下,visible到底以head的为准还是以xml为准?所以xml配置应该在初始化时就尽量加入--wxj20180815
				// var modelConfig = PFDataHelper.GetMultiModelConfig(xmlDataSetName);

				// SGRef<StoreColumnCollection> headerRef=new
				// SGRef<StoreColumnCollection>(header);
				StoreColumn tree = new StoreColumn() {
					{
						SetChildren(headerRef[0]);
					}
				};
				tree.EachLeaf(column -> {
					// if (column.data!=null&&column.data.IndexOf(".") > -1)
					// {
					// column.data=column.data.Replace(".", "_");
					// }
					// if (column.data != null && _specDataChar.Any(a-> .IndexOf(a.Key) > -1))
					if (column.data != null && _specDataChar.containsKey(column.data)) {
						column.data = EncodeDataChar(column.data);
					}

					//// 宽度的优先级:setWidthByHeaderWord<modelConfig<column.width
					//// PFModelConfig config = modelConfig == null ? null :
					//// modelConfig[column.data];
					//// column.SetPropertyByModelConfig(config);//先设置了中文才能计算中文字符长度
					// if (setWidthByHeaderWord &&
					//// PFDataHelper.StringIsNullOrWhiteSpace(column.width))
					// {
					// column.SetWidthByTitleWords();
					// }

					if (column.get_hasSummary()) {
						// column.summary = PFDataHelper.Thousandth(CommonFun.ColumnTotal(all,
						// column.data));
						column.summary = GetColumnSummary(all[0], column.data, column.get_summaryType());
					}
				});
			}

			int[] totalRef = new int[] { total };
			PagingResult r = new PagingResult() {
				{
					data = arrayList;
					columns = headerRef[0];
					total = totalRef[0];
				}
			};
			if (dataTable.ExtendedProperties.containsKey("exData")) {
				r.exData = dataTable.ExtendedProperties.get("exData");
			}

			// 如果清了dataTable,cache就空了
			// PFDataHelper.ClearDataTable(dataTable);
			// dataTable = null;
			// PFDataHelper.ClearDataTable(all);
			// all = null;

			return r;
			// var jsResult = new PFJsonResult();
			// jsResult.Data = r;
			// jsResult.JsonRequestBehavior = JsonRequestBehavior.AllowGet;
			// return jsResult;
			//// return Json(r, JsonRequestBehavior.AllowGet); //返回一个json字符串

		}
		return null;
	}

//
//   public static PagingResult PagingStore(DataTable dataTable, PagingParameters p, StoreColumnCollection header = null, bool setWidthByHeaderWord = true, string xmlDataSetName = null)
//   {
//       if (dataTable == null) { return null; }
//
//       if (!p.PageSize.HasValue) { p.PageSize = dataTable.Rows.Count; }
//       var all = dataTable;
//       var columns = dataTable.Columns;
//
//       List<string> srcColumnNames = new List<string>();//自定义group之后，目标head中可能有不存在的列--ben20190627
//       foreach (DataColumn dataColumn in columns)
//       {
//           srcColumnNames.Add(dataColumn.ColumnName);
//       }
//
//       bool isPageSql = all.Rows.Count > 0 && all.Columns["rowtotal"] != null;
//       int total = 0;
//       DataTable dt = null;
//       if (isPageSql)
//       {
//           total = (int)all.Rows[0]["rowtotal"];
//           dt = all;
//       }
//       else
//       {
//           if (!PFDataHelper.StringIsNullOrWhiteSpace(p.Sort))
//           {
//               //all.DefaultView.Sort = p.Sort.Replace(sc[0].Value, sc[0].Key);
//               all.DefaultView.Sort = DecodeDataChar(p.Sort);
//               all = all.DefaultView.ToTable();
//           }
//           if (!PFDataHelper.StringIsNullOrWhiteSpace(p.FilterValue))
//           {
//               all = all.DataTableFilter(p.FilterValue);
//           }
//           total = all.Rows.Count;//一定要过滤完再计算total
//           dt = all.DataPager(p.PageIndex.Value, p.PageSize.Value);//注意分页后的columns丢了ExtendedProperties
//       }
//       //int total = all.Rows.Count>0&&all.Rows[0]["rowtotal"]!=null? (int)all.Rows[0]["rowtotal"] : all.Rows.Count;
//       if (dt != null)
//       {
//           ArrayList arrayList = new ArrayList();
//           foreach (DataRow dataRow in dt.Rows)
//           {
//               Dictionary<string, object> dictionary = new Dictionary<string, object>();
//               if (header == null)
//               {
//                   foreach (DataColumn dataColumn in columns)
//                   {
//                       ////dictionary.Add(dataColumn.ColumnName.IndexOf(".") > -1 ? dataColumn.ColumnName.Replace(".", "_") : dataColumn.ColumnName
//                       ////    , dataRow[dataColumn.ColumnName].ToString());
//                       //dictionary.Add(EncodeDataChar(dataColumn.ColumnName)
//                       //    , dataRow[dataColumn.ColumnName].ToString());
//                       dictionary.Add(EncodeDataChar(dataColumn.ColumnName)
//                           , dataRow[dataColumn.ColumnName]);
//                   }
//               }
//               else
//               {
//                   (new StoreColumn { Children = header }).EachLeaf(a =>
//                   {
//                       if (srcColumnNames.Contains(a.data))
//                       {
//                           dictionary.Add(EncodeDataChar(a.data)
//      , dataRow[a.data]);
//                       }
//                       else
//                       {
//                           dictionary.Add(EncodeDataChar(a.data)
//        , null);
//                           a.visible = false;
//                       }
//                   });
//               }
//               arrayList.Add(dictionary);
//
//           }
//           if (header == null)
//           {
//               header = new StoreColumnCollection();
//               //header =xmlDataSetName==null?new StoreColumnCollection(): new StoreColumnCollection(xmlDataSetName);
//               //var modelConfig = PFDataHelper.GetMultiModelConfig(xmlDataSetName);
//               var modelConfig = xmlDataSetName == null ? null : PFDataHelper.GetMultiModelConfig(xmlDataSetName);
//               foreach (DataColumn dataColumn in columns)
//               {
//                   StoreColumn dictionary = modelConfig == null ? new StoreColumn(dataColumn) : new StoreColumn(dataColumn, modelConfig[dataColumn.ColumnName]);
//                   //if (dataColumn.ColumnName.IndexOf(sc[0].Key) > -1)
//                   //{
//                   //    dictionary.title = dataColumn.ColumnName;//前端jqueryDatables不支持.
//                   //    dictionary.data = dataColumn.ColumnName.Replace(sc[0].Key,sc[0].Value);
//                   //}
//                   //else
//                   //{
//                   //    dictionary.data = dataColumn.ColumnName;
//                   //}
//                   if (_specDataChar.Any(a => dataColumn.ColumnName.IndexOf(a.Key) > -1))
//                   {
//                       dictionary.title = dataColumn.ColumnName;//前端jqueryDatables不支持.
//                       dictionary.data = EncodeDataChar(dataColumn.ColumnName);
//                   }
//                   else
//                   {
//                       dictionary.data = dataColumn.ColumnName;
//                   }
//
//                   //宽度的优先级:setWidthByHeaderWord<modelConfig<ExtendedProperties
//                   //PFModelConfig config = modelConfig == null ? null : modelConfig[dataColumn.ColumnName];
//                   //dictionary.SetPropertyByModelConfig(config);
//
//                   if (dataColumn.ExtendedProperties.ContainsKey("title")) { dictionary.title = dataColumn.ExtendedProperties["title"].ToString(); }//20180803
//                   if (PFDataHelper.StringIsNullOrWhiteSpace(dictionary.title)) { dictionary.title = dataColumn.ColumnName; }
//                   if (dataColumn.ExtendedProperties != null && dataColumn.ExtendedProperties.Contains("width"))
//                   {
//                       dictionary.width = dataColumn.ExtendedProperties["width"].ToString();
//                   }
//                   else
//                   if (setWidthByHeaderWord && PFDataHelper.StringIsNullOrWhiteSpace(dictionary.width))//设置为中文后进入这里才有意义
//                   {
//                       dictionary.SetWidthByTitleWords();
//
//                   }
//
//                   if (dataColumn.ExtendedProperties != null && dataColumn.ExtendedProperties.Contains("dataType"))
//                   {
//                       dictionary.dataType = dataColumn.ExtendedProperties["dataType"].ToString();
//                   }
//                   if (dataColumn.ExtendedProperties != null && dataColumn.ExtendedProperties.Contains("visible"))
//                   {
//                       dictionary.visible = bool.Parse(dataColumn.ExtendedProperties["visible"].ToString());
//                   }
//                   if (dataColumn.ExtendedProperties != null)
//                   {
//                       if (dataColumn.ExtendedProperties.Contains("summary"))
//                       {
//                           dictionary.summary = dataColumn.ExtendedProperties["summary"].ToString();
//                       }
//                       else if (dataColumn.ExtendedProperties.Contains("hasSummary") && bool.Parse(dataColumn.ExtendedProperties["hasSummary"].ToString()) == true)
//                       {
//                           //dictionary.summary = PFDataHelper.Thousandth(CommonFun.ColumnTotal(all, dictionary.data));//dictionary.data已替换了特殊字符，不准确
//                           //dictionary.summary = PFDataHelper.Thousandth(CommonFun.ColumnTotal(all, dataColumn.ColumnName));
//                           //dictionary.summary = PFDataHelper.ColumnTotal(all, dataColumn.ColumnName);
//                           dictionary.summary = GetColumnSummary(all, dataColumn.ColumnName, dictionary.summaryType);
//                       }
//                   }
//                   header.Add(dictionary);
//               }
//           }
//           else//head不为null时
//           {
//               //如果有head的情况下,visible到底以head的为准还是以xml为准?所以xml配置应该在初始化时就尽量加入--wxj20180815
//               //var modelConfig = PFDataHelper.GetMultiModelConfig(xmlDataSetName);
//
//               var tree = new StoreColumn { Children = header };
//               tree.EachLeaf(column =>
//               {
//                   //if (column.data!=null&&column.data.IndexOf(".") > -1)
//                   //{
//                   //    column.data=column.data.Replace(".", "_");
//                   //}
//                   if (column.data != null && _specDataChar.Any(a => column.data.IndexOf(a.Key) > -1))
//                   {
//                       column.data = EncodeDataChar(column.data);
//                   }
//
//                   ////宽度的优先级:setWidthByHeaderWord<modelConfig<column.width
//                   ////PFModelConfig config = modelConfig == null ? null : modelConfig[column.data];
//                   ////column.SetPropertyByModelConfig(config);//先设置了中文才能计算中文字符长度
//                   //if (setWidthByHeaderWord && PFDataHelper.StringIsNullOrWhiteSpace(column.width))
//                   //{
//                   //    column.SetWidthByTitleWords();
//                   //}
//
//                   if (column.hasSummary)
//                   {
//                       //column.summary = PFDataHelper.Thousandth(CommonFun.ColumnTotal(all, column.data));
//                       column.summary = GetColumnSummary(all, column.data, column.summaryType);
//                   }
//               });
//           }
//
//           var r = new PagingResult { data = arrayList, columns = header, total = total };
//           if (dataTable.ExtendedProperties.ContainsKey("exData"))
//           {
//               r.exData = dataTable.ExtendedProperties["exData"];
//           }
//           return r;
//           //var jsResult = new PFJsonResult();
//           //jsResult.Data = r;
//           //jsResult.JsonRequestBehavior = JsonRequestBehavior.AllowGet;
//           //return jsResult;
//           ////return Json(r, JsonRequestBehavior.AllowGet); //返回一个json字符串
//
//       }
//       return null;
//   }
//   #region MD5函数
//   /// <summary>
//   /// MD5函数,需引用：using System.Security.Cryptography;
//   /// </summary>
//   /// <param name="str">原始字符串</param>
//   /// <returns>MD5结果</returns>
//   public static string MD5(string str)
//   {
//       //微软md5方法参考return FormsAuthentication.HashPasswordForStoringInConfigFile(str, "md5");
//       byte[] b = Encoding.Default.GetBytes(str);
//       b = new MD5CryptoServiceProvider().ComputeHash(b);
//       string ret = "";
//       for (int i = 0; i < b.Length; i++)
//           ret += b[i].ToString("x").PadLeft(2, '0');
//       return ret;
//   }
//   #endregion
//
	private static int WebAndExcelSizeRate = 4;// px和excel尺码的数值比 px/excel=rate

	public static Double WebWidthToExcel(String px) {
		if (StringIsNullOrWhiteSpace(px)) {
			return null;
		}
		try {
			Double r = new Double(px.replace("px", ""));
			return r / WebAndExcelSizeRate;
		} catch (Exception e) {
		}
		return null;
	}

	public static String ExcelWidthToWeb(Double d) {
		DecimalFormat df = new DecimalFormat("0.0");
		String s = df.format(d * 4);
		return s + "px";
	}
//   public static double? WebWidthToExcel(string px)
//   {
//       if (StringIsNullOrWhiteSpace(px)) { return null; }
//       double r = 0;
//       if (double.TryParse(px.Replace("px", ""), out r))
//       {
//           return r / WebAndExcelSizeRate;
//       }
//       return null;
//   }
//   public static string ExcelWidthToWeb(double d)
//   {
//       return (d * 4).ToString("0.0") + "px";
//   }
//   public static bool SendEmail(string emailFrom, string emailFromPwd, string smtpHost,
//       string[] toEmails, string mailTitle, string mailBody,
//       Action<MailMessage> mailAction = null)
//   {
//       //xuzhiquan@richinfo.cn,wxw<wxw@perfect99.com>,wxj@perfect99.com<wxj@perfect99.com>
//
//       //实例化两个必要的
//       MailMessage mail = new MailMessage();
//       SmtpClient smtp = new SmtpClient();
//
//       //发送邮箱地址
//       mail.From = new MailAddress(emailFrom);
//
//       //收件人(可以群发)
//       foreach (var i in toEmails)
//       {
//           mail.To.Add(new MailAddress(i));//benjamin 
//       }
//
//       //是否以HTML格式发送
//       mail.IsBodyHtml = true;
//       //主题的编码格式
//       mail.SubjectEncoding = Encoding.UTF8;
//       //邮件的标题
//       mail.Subject = mailTitle;
//       //内容的编码格式
//       mail.BodyEncoding = Encoding.UTF8;
//       //邮件的优先级
//       mail.Priority = MailPriority.Normal;
//       //发送内容,带一个图片标签,用于对方打开之后,回发你填写的地址信息
//       //mail.Body = @"获取打开邮件的用户IP，图片由服务器自动生成：<img src='" + Receipt + "'>";
//       mail.Body = mailBody;
//       //收件人可以在邮件里面
//       mail.Headers.Add("Disposition-Notification-To", "回执信息");
//
//       if (mailAction != null)
//       {
//           mailAction(mail);
//       }
//
//       //发件邮箱的服务器地址
//       //smtp.Host = "smtp.qq.com";
//       smtp.Host = smtpHost;// "smtp.perfect99.com";
//       smtp.DeliveryMethod = SmtpDeliveryMethod.Network;
//       smtp.Timeout = 1000000;
//       //是否为SSL加密
//       smtp.EnableSsl = true;
//       //设置端口,如果不设置的话,默认端口为25
//       smtp.Port = 25;
//       smtp.UseDefaultCredentials = true;
//       //验证发件人的凭据
//       //smtp.Credentials = new System.Net.NetworkCredential("****@163.com", "这里的密码可以是授权码");
//       smtp.Credentials = new System.Net.NetworkCredential(emailFrom, emailFromPwd);
//       //加这段之前用公司邮箱发送报错：根据验证过程，远程证书无效
//       //加上后解决问题
//       ServicePointManager.ServerCertificateValidationCallback =
//delegate (Object obj, X509Certificate certificate, X509Chain chain, SslPolicyErrors errors) { return true; };
//
//       try
//       {
//           //发送邮件
//           smtp.Send(mail);
//           smtp.Dispose();
//           return true;
//       }
//       catch (Exception e1)
//       {
//           PFDataHelper.WriteError(e1);
//           return false;
//       }
//   }
//
//   //private static List<PFListenEmailTask> _listenEmailTask;
//   //public static void ListenEmail(string hostName, string userName, string pwd,
//   //     Action<PFEmail> doAction, Func<PFEmail, bool> subjectMatch)//,bool deleteAfterRead) 
//   //{
//
//   //    if (_listenEmailTask == null) { _listenEmailTask = new List<PFListenEmailTask>(); }
//   //    var task = new PFListenEmailTask("PFTcBackupChecker",
//   //        new PFEmailManager(hostName, userName, pwd),
//   //        doAction,
//   //        subjectMatch);
//   //    _listenEmailTask.Add(task);
//   //    task.Start();
//
//   //}
//
//   //        public static string FormatMethodExecStatus(List<string> errors, MethodExecStatus execStatus)
//   //        {
//   //            if (execStatus == MethodExecStatus.Success) { return "执行成功,没有报错"; }
//   //            return string.Format(@"
//   //{0}:
//   //{1}
//   //",
//   //execStatus == MethodExecStatus.Error ? "执行报错" : "执行部分报错",
//   //string.Join("\r\n", errors)
//   //);
//   //        }
//   public static string FormatMethodExecStatus(List<string> errors, int total, out MethodExecStatus execStatus)
//   {
//       if (!errors.Any())
//       {
//           execStatus = MethodExecStatus.Success;
//           return "执行成功,没有报错";
//       }
//       else if (errors.Count < total)
//       {
//           execStatus = MethodExecStatus.PartError;
//       }
//       else
//       {
//           execStatus = MethodExecStatus.Error;
//       }
//       return string.Format(@"
//{0}:
//{1}
//",
//execStatus == MethodExecStatus.Error ? "执行报错" : "执行部分报错",
//string.Join("\r\n", errors)
//);
//   }
//

	/**
	 * 科学计数法
	 * 
	 * @param num
	 * @return
	 */
	public static String ScientificNotation(double num) {
		double bef = Math.abs(num);
		int aft = 0;
		while (bef >= 10 || (bef < 1 && bef != 0)) {
			if (bef >= 10) {
				bef = bef / 10;
				aft++;
			} else {
				bef = bef * 10;
				aft--;
			}
		}
		// return string.Concat(num >= 0 ? "" : "-", ReturnBef(bef), "E",
		// ReturnAft(aft));
		return String.join("", num >= 0 ? "" : "-", ReturnBef(bef), "E", ReturnAft(aft));
	}

	/**
	 * 获得Windows系统进程ID
	 * 
	 * @param jarNameOrMainClassFullName
	 * @return
	 */
	private static String GetWindowsSystemPID(String[] jarNameOrMainClassFullName) {

		Runtime runtime = Runtime.getRuntime();
		try {
			// _cmdStartListener.handleEvent(new CmdStartEvent(this,_springPath+" 已经启动"));
			Process _process = runtime.exec("jps -l -m");
			InputStreamReader inputReader = new InputStreamReader(_process.getInputStream());
			BufferedReader stdoutReader = new BufferedReader(inputReader);
			String line;
			List<String> lines = new ArrayList<String>();
			// System.out.println("OUTPUT");
			while ((line = stdoutReader.readLine()) != null) {
				lines.add(line);
			}
			inputReader.close();
			stdoutReader.close();
			// List<String>
			// jarNameOrMainClassFullNameList=ObjectToList(jarNameOrMainClassFullName);
			for (String i : lines) {

//	       	  if(i.indexOf("pfTransferTask-0.0.1-SNAPSHOT.jar")>-1
//	       			  ||i.indexOf("com.perfect99.pfTransferTask.PfTransferTaskApp")>-1) 
				if (SGDataHelper.ArrayAny(jarNameOrMainClassFullName, a -> i.indexOf(a) > -1)) {
					String processId = i.split(" ")[0];
					// runtime.exec("taskkill /pid "+processId+" /f");
					return processId;
					// break;
				}
			}
			_process.destroy();

		} catch (Throwable e1) {

		}
		return null;
	}

	public static SysType GetSysType() {
		try {
			if (System.getProperty("os.name").toLowerCase().contains("win")) {
				return SysType.Windows;
			} else if (System.getProperty("os.name").toLowerCase().indexOf("linux") >= 0) {
				return SysType.Linux;
			}
		} catch (Exception e) {

		}
		return SysType.Linux;
	}

	/**
	 * 获得系统进程ID
	 * 
	 * @param jarNameOrMainClassFullName
	 * @return
	 */
	public static String GetSystemPID(String[] jarNameOrMainClassFullName) {
		// if windows
		if (SysType.Windows == GetSysType()) {
			return GetWindowsSystemPID(jarNameOrMainClassFullName);
		}
		return "";
		// if linex
	}

	@Deprecated
	public static void ShutdownSpringBootForWindowsSystem(String[] jarNameOrMainClassFullName) {
		Runtime runtime = Runtime.getRuntime();
		try {
			// _cmdStartListener.handleEvent(new CmdStartEvent(this,_springPath+" 已经启动"));
			Process _process = runtime.exec("jps -l -m");
			InputStreamReader inputReader = new InputStreamReader(_process.getInputStream());
			BufferedReader stdoutReader = new BufferedReader(inputReader);
			String line;
			List<String> lines = new ArrayList<String>();
			// System.out.println("OUTPUT");
			while ((line = stdoutReader.readLine()) != null) {
//       	  if(_cmdOutListener!=null) {
//       		  _cmdOutListener.handleEvent(new CmdOutEvent(this,line));
//       	  }
				// if(_outAction!=null) {_outAction.setValue(line);}

				/*
				 * if(line.isEmpty()) { System.out.println("1111"); }
				 */

				// System.out.println(line);
				lines.add(line);
			}
			inputReader.close();
			stdoutReader.close();
			// List<String>
			// jarNameOrMainClassFullNameList=ObjectToList(jarNameOrMainClassFullName);
			for (String i : lines) {

//       	  if(i.indexOf("pfTransferTask-0.0.1-SNAPSHOT.jar")>-1
//       			  ||i.indexOf("com.perfect99.pfTransferTask.PfTransferTaskApp")>-1) 
				if (SGDataHelper.ArrayAny(jarNameOrMainClassFullName, a -> i.indexOf(a) > -1)) {
					String processId = i.split(" ")[0];
					runtime.exec("taskkill /pid " + processId + " /f");
					break;
				}
			}
			_process.destroy();
			// cutEurekaAsync();
			// stdoutReader.close();
			// Dispose();
			// _errAction.setValue(_springPath+" 已经停止");
			// System.out.println("ERROR");

			/*
			 * BufferedReader stderrReader = new BufferedReader(new
			 * InputStreamReader(_process.getErrorStream())); while ((line =
			 * stderrReader.readLine()) != null) { _errAction.setValue(line);
			 * //System.out.println(line); } stderrReader.close();
			 */

			/*
			 * int exitVal = _process.waitFor(); runtime.freeMemory();
			 */
		} catch (Throwable e1) {
//			  //cutEurekaAsync();
//		  	  _errAction.setValue(_springPath+"  "+e1.getMessage());
//	          //runtime.freeMemory();
//	          Dispose();
//	          _process.destroy();
		}
	}

	/**
	 * 
	 * @param pid PFDataHelper.PID
	 */
	public static void ShutdownSpringBootForWindowsSystemByPID(String pid) {
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec("taskkill /pid " + pid + " /f");
		} catch (Throwable e1) {

		}
	}

	/// <summary>
	/// 有效数字的处理
	/// </summary>
	/// <param name="bef">有效数字</param>
	/// <returns>三位有效数字，不足则补零</returns>
	private static String ReturnBef(double bef) {
		if (String.valueOf(bef) != null) {
			char[] arr = String.valueOf(bef).toCharArray();
			String.join("", "");
//           switch (arr.length)
//           {
//               case 1:
//               case 2: return String.concat(arr[0], ".", "00"); break;
//               case 3: return String.Concat(arr[0] + "." + arr[2] + "0"); break;
//               default: return String.Concat(arr[0] + "." + arr[2] + arr[3]); break;
//           }
			switch (arr.length) {
			case 1:
			case 2:
				return String.join("", String.valueOf(arr[0]), ".", "00");
			case 3:
				return String.join("", String.valueOf(arr[0]), ".", String.valueOf(arr[2]), "0");
			default:
				return String.join("", String.valueOf(arr[0]), ".", String.valueOf(arr[2]), String.valueOf(arr[3]));
			}
		} else
			return "000";
	}

	/// <summary>
	/// 幂的处理
	/// </summary>
	/// <param name="aft">幂数</param>
	/// <returns>三位幂数部分，不足则补零</returns>
	private static String ReturnAft(int aft) {
		if (String.valueOf(aft) != null) {
			String end;
			char[] arr = String.valueOf(Math.abs(aft)).toCharArray();
			switch (arr.length) {
			case 1:
				end = "00" + arr[0];
				break;
			case 2:
				end = "0" + arr[0] + arr[1];
				break;
			default:
				end = String.valueOf(Math.abs(aft));
				break;
			}
			// return string.Concat(aft >= 0 ? "+" : "-", end);
			return (aft >= 0 ? "+" : "-") + end;
		} else
			return "+000";
	}
//   public class CFoldPoint
//   {
//       public int X { get; set; }
//       public int Y { get; set; }
//   }
//   #region 统计算法
//   //最小二乘法直线拟合,线性回归
//   public static bool CalculateLineKB(ref List<CFoldPoint> m_FoldList, ref double k, ref double b)
//   {
//       //最小二乘法直线拟合
//       //m_FoldList为关键点(x,y)的链表
//       //拟合直线方程(Y=kX+b)，k和b为返回值
//
//       if (m_FoldList == null) return false;
//       long lCount = m_FoldList.Count;
//       if (lCount < 2) return false;
//       //CFoldPoint pFold;
//       double mX, mY, mXX, mXY, n;
//       mX = mY = mXX = mXY = 0;
//       n = lCount;
//       //POSITION pos = m_FoldList->GetHeadPosition();
//       foreach (var pFold in m_FoldList)
//       {
//           //pFold = m_FoldList->GetNext(pos);
//           mX += pFold.X;
//           mY += pFold.Y;
//           mXX += pFold.X * pFold.X;
//           mXY += pFold.X * pFold.Y;
//       }
//       if (mX * mX - mXX * n == 0) return true;
//       k = (mY * mX - mXY * n) / (mX * mX - mXX * n);
//       b = (mY - mX * k) / n;
//       return true;
//   }
//   #endregion
//   //PFDataHelper End

	private static String doSetUrlParams(String sUrl, String name, Object val) {

//    if (val instanceof Array) {
//        for (var i = 0; i < val.length; i++) {
//            sUrl += (name + '=' + val[i] + '&');
//        }
//    } else {
		sUrl += (name + '=' + val + '&');
//    }
		return sUrl;
	}

	private static String doRemoveUrlSameParam(String sUrl, String pName) {
		// var patt1 = new RegExp("([\&\?]{1})" + pName +
		// "[\\[\\]]{0,2}\=[^\&]*[\&]{0,1}", "g");
		return sUrl.replaceAll("([\\&\\?]{1})" + pName + "[\\\\[\\\\]]{0,2}\\=[^\\&]*[\\&]{0,1}", "$1");
	}

	public static String setUrlParams(String url, HashMap<String, Object> arr) {
		if (url.indexOf('?') < 0) {
			url += '?';
		} else {
			char lc = url.charAt(url.length() - 1);
			if (lc != '?' && lc != '&') {
				url += '&';
			}
		}
//
//    function setParam(sUrl, name, val) {
//        if (val instanceof Array) {
//            for (var i = 0; i < val.length; i++) {
//                sUrl += (name + '=' + val[i] + '&');
//            }
//        } else {
//            sUrl += (name + '=' + val + '&');
//        }
//        return sUrl;
//    }
//    function removeSameParam(sUrl, pName) {//移除url中已经存在的同名参数,已考虑 xx[]的情况
//        var patt1 = new RegExp("([\&\?]{1})" + pName + "[\\[\\]]{0,2}\=[^\&]*[\&]{0,1}", "g");
//        return sUrl.replace(patt1, '$1');
//    }
		// 到这里url格式为 xx? 或者 xx?xx&

		// 考虑到有数组的情况,必需全部移除再设置
		Iterator<Entry<String, Object>> iter = arr.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Object> key = iter.next();
			url = doRemoveUrlSameParam(url, key.getKey());
		}
		iter = arr.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Object> key = iter.next();
			url = doSetUrlParams(url, key.getKey(), key.getValue());
		}
//    if (arr instanceof Array) {
//        for (var i = 0; i < arr.length; i++) {//考虑到有数组的情况,必需全部移除再设置
//            url = removeSameParam(url, arr[i].name);
//        }
//        for (var i = 0; i < arr.length; i++) {
//            url = setParam(url, arr[i].name, arr[i].value);
//        }
//    } else {//object
//        for (var i in arr) {//考虑到有数组的情况,必需全部移除再设置
//            if (arr.hasOwnProperty(i)) {
//                url = removeSameParam(url, i);
//            }
//        }
//        for (var i in arr) {
//            if (arr.hasOwnProperty(i)) {
//                url = setParam(url, i, arr[i]);
//            }
//        }
//    }

		if (url.charAt(url.length() - 1) == '&') {
			url = url.substring(0, url.length() - 1);
		}
		return url;
	}

	public static void SetObjectPropertyByUpdateCollection(Object model, BaseSqlUpdateCollection update) {
		Field[] fields = model.getClass().getFields();

		for (Field i : fields) {
			String key = i.getName();
			if (update.containsKey(key)) {
				i.setAccessible(true);
				try {
					// i.set(model,update.get(key).Value);
//					if ("Qx".equals(key)) {
//						String aa = "aa";
//					}
//					i.set(model,ConvertObjectByType(update.get(key).Value,update.get(key).VType,i.getDeclaringClass()));
					i.set(model, ConvertObjectByType(update.get(key).Value, update.get(key).VType, i.getType()));

				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// base.Add(i.Key, new SqlUpdateItem { Key = i.Key, Value =
			// i.Value.GetValue(_model, null), VType = i.Value.PropertyType, PInfo = i.Value
			// });
		}
	}

	public static <T> List<List<T>> averageAssign(List<T> source, int n) {
		List<List<T>> result = new ArrayList<List<T>>();
		if (n == 0) {
			n = 1;
		}
		int remaider = source.size() % n; // (先计算出余数)
		int number = source.size() / n; // 然后是商
		int offset = 0;// 偏移量
		for (int i = 0; i < n; i++) {
			List<T> value = null;
			if (remaider > 0) {
				value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
				remaider--;
				offset++;
			} else {
				value = source.subList(i * number + offset, (i + 1) * number + offset);
			}
			result.add(value);
		}
		return result;
	}

	/**
	 * 分隔 分割
	 * 
	 * @param source
	 * @param size
	 * @return
	 */
	public static <T> List<List<T>> averageAssignBySize(List<T> source, int size) {
		if (source == null || source.size() < 1) {
			return new ArrayList<List<T>>();
		}
		if (size == 0) {
			size = 1;
		}
		int totalSize = source.size();
		int n = totalSize / size;
		int remaider = source.size() % size; // (先计算出余数)
		if (remaider > 0) {
			n += 1;
		}
		return averageAssign(source, n);
	}

	/// <summary>
	/// 自动重试
	/// 如果最终失败,会抛出错误
	/// </summary>
	/// <param name="doAction"></param>
	/// <param name="tryTimes"></param>
	public static <T, E extends Exception> void ReTry(SGActionThrowing<Object, Object, Object, Exception> doAction,
			int tryTimes) throws Exception {
		int maxTryTimes = tryTimes;
		while (tryTimes > 0) {
			try {
				doAction.go(tryTimes, null, null);
				return;
			} catch (Exception e) {
				if (tryTimes == 1) {
					throw new Exception(FormatString("尝试执行{0}次失败\r\n" + "error:{1}\r\n", maxTryTimes, e));
				}
			}
			tryTimes--;
		}
	}

	public static void ReTry(SGActionThrowing<Object, Object, Object, Exception> doAction) throws Exception {
		ReTry(doAction, 3);
	}

	public static Map<String, ?> GetClassByInterface(Class<?> interfaceCls) {

		return _applicationContext.getBeansOfType(interfaceCls);
	}

	public static <T> Map<String, T> GetClassByInterfaceT(Class<T> interfaceCls) {

		return _applicationContext.getBeansOfType(interfaceCls);
	}

	public static ApplicationContext GetApplicationContext() {

		return _applicationContext;
	}

	// 根据路径获取
	public static List<Class<?>> getClasses(File dir, String pk) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!dir.exists()) {
			return classes;
		}
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				classes.addAll(getClasses(f, pk + "." + f.getName()));
			}
			String name = f.getName();
			if (name.endsWith(".class")) {
				classes.add(Class.forName(pk + "." + name.substring(0, name.length() - 6)));
			}
		}
		return classes;
	}

	public static List<Class<?>> getClasses(Class<?> cls) throws Exception {
		String pk = cls.getPackage().getName();
		String path = pk.replace('.', '/');
		System.out.println("------------getClasses package path---------------");
		System.out.println(path);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();//在String+SWT项目的调试运行时这里变成了StartedLauncher(正常应该是AppLauncher才对的)
		//ClassLoader classloader =cls.getClassLoader();改成这样之后,在Spring项目下还是不太正常
		java.net.URL url = classloader.getResource(path);
		System.out.println("------------getClasses resource path---------------");
		System.out.println(url.getFile());
		//File f = new File(url.getFile());
		List<Class<?>> r = getClasses(new File(url.getFile()), pk);
		if (r == null || r.isEmpty()) {
			r = getClassesInJarFile(cls);
		}
		return r;
	}

	public static List<Class<?>> getClassesInJarFile(Class<?> cls) {
		try {
			List<Class<?>> classes = new ArrayList<Class<?>>();
			String pk = cls.getPackage().getName();
			String path = pk.replace('.', '/');
			String p = Thread.currentThread().getContextClassLoader().getResource(path).getPath();
			System.out.println(p);
			int idx = p.indexOf(".jar");
			p = p.substring(0, idx + 4).replace("file:/", "");
			System.out.println(p);
//			JarFile jf = new JarFile(p);
//			Enumeration<?> enu = jf.entries();
//			while (enu.hasMoreElements()) {
//			java.util.jar.JarEntry element = (java.util.jar.JarEntry) enu.nextElement();
//			                String name = element.getName();
////			                Long size = element.getSize();
////			                Long time = element.getTime();
////			                Long compressedSize = element.getCompressedSize();
//			                if(name.startsWith(path)&&name.endsWith(".class")) {
//			                	classes.add(Class.forName(name.replace(".class", "").replace("/", ".")));
//			                }
////			                System.out.print(name+"/t");
////			                System.out.print(size+"/t");
////			                System.out.print(compressedSize+"/t");
////			                System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(new Date(time)));
//			}
//			jf.close();
			try (JarFile jf = new JarFile(p);) {
				Enumeration<?> enu = jf.entries();
				while (enu.hasMoreElements()) {
					java.util.jar.JarEntry element = (java.util.jar.JarEntry) enu.nextElement();
					String name = element.getName();
//			                Long size = element.getSize();
//			                Long time = element.getTime();
//			                Long compressedSize = element.getCompressedSize();
					if (name.startsWith(path) && name.endsWith(".class")) {
						classes.add(Class.forName(name.replace(".class", "").replace("/", ".")));
					}
//			                System.out.print(name+"/t");
//			                System.out.print(size+"/t");
//			                System.out.print(compressedSize+"/t");
//			                System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(new Date(time)));
				}
				jf.close();
			} catch (Exception ex) {
				SGDataHelper.WriteError(new Throwable(), ex);
			}
			return classes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void getClassesTest(Class<?> cls) {
		try {
			String pk = cls.getPackage().getName();
			System.out.println("------------getClassesTest---------------");
			System.out.println(pk);
			String path = pk.replace('.', '/');
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			// java.net.URL url = classloader.getResource("pf/java/pfHelper/model");
			java.net.URL url = classloader.getResource(path);
			String p = null;
			System.out.println("------------getClassesTest---------------");
			p = url.getPath();
			System.out.println(p);
			System.out.println(new File(p).exists());
			System.out.println("------------getClassesTest---------------");
			p = url.getFile();
			System.out.println(p);
			System.out.println(new File(p).exists());
			System.out.println("------------getClassesTest---------------");
			url = cls.getResource("");
			p = url.getPath();
			System.out.println(p);
			System.out.println(new File(p).exists());
			System.out.println("------------getClassesTest---------------");
			URI uri = url.toURI();
			System.out.println(new File(uri).exists());

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 不使用Spring时,可以替代GetClassByInterface方法 使用方法:
	 * jdbcMap=PFDataHelper.getAllAssignedClass(BalanceJdbc.class, IPFJdbc.class);
	 * 
	 * @param packageCls
	 * @param interfaceCls
	 * @return
	 * @throws Exception
	 */
	public static List<Class<?>> getAllAssignedClass(Class<?> packageCls, Class<?> interfaceCls) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		try {
			for (Class<?> c : getClasses(packageCls)) {
				//当是Spring+swt时,swt里调用getAllAssignedClass的话,interfaceCls会是StartedClassLoader类型,造成isAssignableFrom判断错误
				//https://blog.csdn.net/qq_41109942/article/details/112739755
//				if (interfaceCls.isAssignableFrom(c) && !interfaceCls.equals(c)) {
//					classes.add(c);
//				}
				//if (ClassUtils.getClass(c.getClassLoader(),interfaceCls.getName()).isAssignableFrom(c) && !interfaceCls.equals(c)) {//ok
				if (Class.forName(interfaceCls.getName(), false, c.getClassLoader()).isAssignableFrom(c) && !interfaceCls.equals(c)) {
					
					classes.add(c);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return classes;
	}

	public static enum TrueOrStop {
		IsTrue, IsFalse, Stop
	}

	/**
	 * 猜数字在哪个范围内（常用于分批锁定sql插入时是哪一行报错)
	 * 
	 * @param start 开始数字，常为0
	 * @param next  下个数字
	 * @param range 需要锁定的范围（常常锁定到1000范围内，再自行遍历就行)
	 * @param isIn  是否在此范围内
	 * @return
	 */
	public static Pair<Integer, Integer> GuessNumber(Integer start, Function<Integer, Integer> next, Integer range,
			SGFunc<Integer, Integer, Object, TrueOrStop> isIn) {
		// boolean f=true;
		Integer n = 0;
		int guessMax = 100000;// 防止找不到一直找，默认第一步找10万次(防止死循环)
		while (true) {
			n = next.apply(start);
			TrueOrStop tos = isIn.go(start, n, null);
			if (TrueOrStop.IsTrue == tos) {
				break;
			} else if (TrueOrStop.IsFalse == tos) {
				start = n;
			} else {
				return null;
			}
			if (guessMax-- < 0) {
				return null;
			}
		}
		if (Math.abs(n - start) < range) {
			return Pair.of(start, n);
		}
		// 上面用next锁定到目标后,用1/2减小范围
		Integer m = 0;
		while (true) {
			m = (start + n) / 2;
			TrueOrStop tos = isIn.go(start, m, null);
			if (TrueOrStop.IsTrue == tos) {
				n = m;
			} else if (TrueOrStop.IsFalse == tos) {
				start = m;
			} else {
				return null;
			}
			if (Math.abs(n - start) < range) {
				return Pair.of(start, n);
			}
		}
	}

	/**
	 * 超过maxSecond强制终止线程
	 * 
	 * @param t
	 * @param maxSecond
	 */
	@SuppressWarnings("deprecation")
	public static void StopThread(Thread t, int maxSecond) {
//		PFDate now=PFDate.Now();
		long maxMillis = maxSecond * 1000;
		long now = SGDate.Now().ToCalendar().getTimeInMillis();
		boolean[] isEnd = new boolean[] { false };
		new Thread() {// 线程操作
			public void run() {
				t.interrupt();
				isEnd[0] = true;
			}
		}.start();
		while (!isEnd[0]) {
			if (SGDate.Now().ToCalendar().getTimeInMillis() - now > maxMillis) {
				t.stop();
				return;
			}
		}

	}

	/**
	 * 是否正常偏差范围
	 * 
	 * @param validMin 验证阀值(如验证月累计业绩时,单天业绩的值可以设置为validMin,如果没有概念,这个数可以设置得小一些)
	 * @return
	 */
	public static boolean IsNormalDeviation(int l, int r, int validMin) {
		if (l == r) {
			return true;
		}

		if (l < validMin && r < validMin) {
			return true;
		}

		// 这样不合理,如果l比validMin小1,r比validMin大1,应该不是false
//		if((l<validMin&&r>validMin)
//				||(l>validMin&&r<validMin)) {return false;}

		// int small =l;
		// int big=r;
		Double small = new Double(l);
		Double big = new Double(r);
		if (l > r) {
//			small=r;
//			big=l;
			small = new Double(r);
			big = new Double(l);
		}
		if ((big - small) / big > 0.05) {// 偏差大于某个百份比为异常
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 是否正常偏差范围
	 * 
	 * @param validMin 验证阀值(如验证月累计业绩时,单天业绩的值可以设置为validMin,如果没有概念,这个数可以设置得小一些)
	 * @return
	 */
	public static boolean IsNormalDeviation(BigDecimal l, BigDecimal r, int validMin) {
		return IsNormalDeviation(l == null ? 0 : l.intValue(), r == null ? 0 : r.intValue(), validMin);
	}
}
