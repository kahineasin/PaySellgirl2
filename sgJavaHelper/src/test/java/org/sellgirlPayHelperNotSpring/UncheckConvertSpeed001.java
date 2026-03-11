package org.sellgirlPayHelperNotSpring;

//import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sellgirl.sgJavaHelper.*;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

import junit.framework.TestCase;


/**
 * 不要随便更改此类名,以防打包时执行了此类
 * 
 * @author Administrator
 *
 */
@SuppressWarnings(value = { "unused",  "deprecation" })
public class UncheckConvertSpeed001 extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(UncheckConvertSpeed001.class);

//	//rows:2.00E+007 speed:4.00E+008条/秒 耗时:0时0分0秒25毫秒/千万行 总耗时:0时0分0秒50毫秒 (begin:2022-09-29 15:24:38 -> now:2022-09-29 15:24:39)
//	public void testCalendarToCalendar001() {
//		Calendar d = PFDate.Now().ToCalendar();
//		int m = 20000000;
//		int i =0;
//		
//		System.out.println("Calendar -> Calendar NORMAL");
//		PFSpeedCounter speed = new PFSpeedCounter(com.sellgirl.pfHelperNotSpring.PFDate.Now());
//		while(i<m) {
//			PFDataHelper.ConvertObjectToSqlTypeByPFType(d,PFSqlFieldType.DateTime,java.sql.Types.DATE);
//			i++;
//		}	
//		System.out.println(speed.getSpeed(m, com.sellgirl.pfHelperNotSpring.PFDate.Now()));
//	}
//	//rows:2.00E+007 speed:5.00E+009条/秒 耗时:0时0分0秒2毫秒/千万行 总耗时:0时0分0秒4毫秒 (begin:2022-09-29 15:24:39 -> now:2022-09-29 15:24:39)
//	public void testCalendarToCalendar002() {
//		Calendar d = PFDate.Now().ToCalendar();
//		int m = 20000000;
//		int i =0;
//		
//		IPFSqlFieldTypeConverter c2 = null;
//		System.out.println("Calendar -> Calendar CONVERTER");
//		PFSpeedCounter speed = new PFSpeedCounter(com.sellgirl.pfHelperNotSpring.PFDate.Now());
//		while(i<m) {			
//			if(c2==null) {
//				c2=PFDataHelper.GetObjectToSqlTypeByPFTypeConverter(d,PFSqlFieldType.DateTime,java.sql.Types.DATE);
//			}
//			c2.convert(d);
//			i++;
//		}
//		System.out.println(speed.getSpeed(m, com.sellgirl.pfHelperNotSpring.PFDate.Now()) );
//	}	
//	//rows:2.00E+007 speed:5.00E+009条/秒 耗时:0时0分0秒2毫秒/千万行 总耗时:0时0分0秒4毫秒 (begin:2022-09-29 15:24:39 -> now:2022-09-29 15:24:39)
//	public void testCalendarToCalendar003() {
//		Calendar d = PFDate.Now().ToCalendar();
//		int m = 20000000;
//		int i =0;
//		
//		IPFSqlFieldTypeConverter2 c2 = null;
//		System.out.println("Calendar -> Calendar CONVERTER INTERFACE");
//		PFSpeedCounter speed = new PFSpeedCounter(com.sellgirl.pfHelperNotSpring.PFDate.Now());
//		while(i<m) {	
//			if(c2==null) {
//				c2=PFSqlFieldTypeConverter2.CalendarToCalendar();
//			}
//			c2.convert(d);
//			i++;
//		}
//		System.out.println(speed.getSpeed(m, com.sellgirl.pfHelperNotSpring.PFDate.Now()) );
//	}
	
	//rows:2.00E+007 speed:5.46E+006条/秒 耗时:0时0分1秒830毫秒/千万行 总耗时:0时0分34秒524毫秒 (begin:2022-09-29 15:24:28 -> now:2022-09-29 15:24:32)
	public void testLongToDate001() {
		Long d = SGDate.Now().ToCalendar().getTimeInMillis();
		int m = 200000000;
		int i =0;
		
		System.out.println("long -> date NORMAL");
		SGSpeedCounter speed = new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
		while(i<m) {	
			Object tmpV=SGDataHelper.ConvertObjectToSqlTypeByPFType(d,SGSqlFieldTypeEnum.Long,java.sql.Types.DATE);
			//Object tmpV=PFDataHelper.GetObjectToSqlTypeByPFTypeConverter(d,null,java.sql.Types.DATE,"").convert(d);//测试
			//d+=1;
			i++;
		}
		System.out.println(speed.getSpeed(m, com.sellgirl.sgJavaHelper.SGDate.Now()));
	}
	//rows:2.00E+007 speed:6.40E+006条/秒 耗时:0时0分1秒562毫秒/千万行 总耗时:0时0分33秒0毫秒 (begin:2022-09-29 15:24:32 -> now:2022-09-29 15:24:35)
	public void testLongToDate002() {
		Long d = SGDate.Now().ToCalendar().getTimeInMillis();
		int m = 200000000;
		int i =0;
		
		IPFSqlFieldTypeConverter c2 = null;
		System.out.println("long -> date CONVERTER");
		SGSpeedCounter speed = new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
		while(i<m) {		
			if(c2==null) {
				c2=SGDataHelper.GetObjectToSqlTypeByPFTypeConverter(d,null,java.sql.Types.DATE,"",null

				);
			}
			Object tmpV=c2.convert(d);
			//d+=1;
			i++;
		}
		System.out.println(speed.getSpeed(m, com.sellgirl.sgJavaHelper.SGDate.Now()) );
	}
//	//rows:2.00E+007 speed:6.17E+006条/秒 耗时:0时0分1秒620毫秒/千万行 总耗时:0时0分3秒240毫秒 (begin:2022-09-29 15:24:35 -> now:2022-09-29 15:24:38)
//	public void testLongToDate003() {
//		Long d = PFDate.Now().ToCalendar().getTimeInMillis();
//		int m = 20000000;
//		int i =0;
//		
//		IPFSqlFieldTypeConverter2 c2 = null;
//		System.out.println("long -> date CONVERTER INTERFACE");
//		PFSpeedCounter speed = new PFSpeedCounter(com.sellgirl.pfHelperNotSpring.PFDate.Now());
//		while(i<m) {				
//			if(c2==null) {
//				c2=PFSqlFieldTypeConverter2.LongToCalendar();
//			}
//			Object tmpV=c2.convert(d);
//			d+=1;
//			i++;
//		}
//		System.out.println(speed.getSpeed(m, com.sellgirl.pfHelperNotSpring.PFDate.Now()) );
//	}
}
