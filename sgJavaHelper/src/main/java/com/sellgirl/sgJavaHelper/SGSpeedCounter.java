package com.sellgirl.sgJavaHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.time.SGTimeSpan;

/**
 * 计时工具.
 * 用法1:
 * SGSpeedCounter speed=new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
 * System.out.println(speed.getEnSpeed(total,com.sellgirl.sgJavaHelper.SGDate.Now()));
 * 
 * 用法2(计算分段的时间):
        SGSpeedCounter speed2=new SGSpeedCounter();
        speed2.setBeginTime(beginTimeDate);
        speed2.setEndTime(beginTimeDate.AddMinutes(1));
        speed2.setBeginTime(beginTimeDate.AddMinutes(5));
        speed2.setEndTime(beginTimeDate.AddMinutes(6));
        this.PrintObject(speed2.getEnSpeed2(total)); //耗时2分钟
 * @author 1011712002
 *
 */

public class SGSpeedCounter implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3878709540857156408L;
//	public static int total = 0;
    private long beginTime = -1;
    private String beginTimeStr = "";
    private long timestampLong=0;
    public SGSpeedCounter(SGDate beginTimeDate) {
        beginTime = beginTimeDate.ToCalendar().getTimeInMillis();
        beginTimeStr = beginTimeDate.toString();
    }
    public SGSpeedCounter() {
    }
    @Deprecated
    public String getSpeed(int total,SGDate endTimeDate) {
      	timestampLong=endTimeDate.toTimestamp()-beginTime;
      	return SGDataHelper.FormatString(
                  "{0} (begin:{1} -> now:{2})",
                  getSpeed2(total),
                  beginTimeStr,
                  endTimeDate.toString());
    }
    public void setBeginTime(SGDate beginTimeDate) {
    	 beginTime = beginTimeDate.ToCalendar().getTimeInMillis();
    }
    /**
     * 
     * @param ts  如System.currentTimeMillis()
     */
    public void setBeginTime(long ts) {
   	 beginTime = ts;
   }
    //public long getBeginTime(){return beginTime;}
    public void setEndTime(SGDate endTimeDate) {
    	if(beginTime<0) {
    		return;
    	}
    	timestampLong+=endTimeDate.toTimestamp()-beginTime;
    	beginTime=-1;
   }
    /**
     * .
     * @param ts 如System.currentTimeMillis()
     */
    public void setEndTime(long ts) {
    	if(beginTime<0) {
    		return;
    	}
    	timestampLong+=ts-beginTime;
    	beginTime=-1;
   }
    @Deprecated
    public String getSpeed2(int total) {
        List<String> rList=new ArrayList<String>();
        rList.add("rows:"+SGDataHelper.ScientificNotation(total));
        if(0!=timestampLong) {
            String r2=timestampLong==0?"too quick":SGDataHelper.ScientificNotation(
                    Double.valueOf(total) * 1000 / timestampLong)
            + "条/秒";
        	rList.add("speed:"+r2);
        }
        if(0!=total) {
        	long diffPerTenMillion=total==0?-1:(timestampLong*10000000)/Long.valueOf(total);
        	SGTimeSpan ts2 =diffPerTenMillion==-1?null: SGDataHelper.GetTimeSpan(diffPerTenMillion,SGYmd.Hour | SGYmd.Minute | SGYmd.Second|SGYmd.Millisecond);
            String r3=total==0?"unknown":(ts2.toString()+ "/千万行");
        	rList.add("averageTime:"+r3);
        }
        rList.add("totalTime:"+SGDataHelper.GetTimeSpan(timestampLong,SGYmd.Hour | SGYmd.Minute | SGYmd.Second|SGYmd.Millisecond));
    	return String.join(" ", rList);
    }
    /**
     * 便于测试项目中文乱码,使用英文
     * @param total
     * @return
     */
    public String getEnSpeed2(long total) {
        List<String> rList=new ArrayList<String>();
        rList.add("rows:"+SGDataHelper.ScientificNotation(total));
        if(0!=timestampLong) {
            String r2=timestampLong==0?"too quick":SGDataHelper.ScientificNotation(
                    Double.valueOf(total) * 1000 / timestampLong)
            + "row/s";
        	rList.add("speed:"+r2);
        }
        if(0!=total) {
        	long diffPerTenMillion=total==0?-1:(timestampLong*10000000)/Long.valueOf(total);
        	SGTimeSpan ts2 =diffPerTenMillion==-1?null: SGDataHelper.GetTimeSpan(diffPerTenMillion,SGYmd.Hour | SGYmd.Minute | SGYmd.Second|SGYmd.Millisecond);
            String r3=total==0?"unknown":(ts2.toEnString()+ "/1E7Rows");
        	rList.add("averageTime:"+r3);
        }
        rList.add("totalTime:"+SGDataHelper.GetTimeSpan(timestampLong,SGYmd.Hour | SGYmd.Minute | SGYmd.Second|SGYmd.Millisecond).toEnString());
    	return String.join(" ", rList);
    }
    public String getEnSpeed(int total,SGDate endTimeDate) {
      	timestampLong=endTimeDate.toTimestamp()-beginTime;
      	return SGDataHelper.FormatString(
                  "{0} (begin:{1} -> now:{2})",
                  getEnSpeed2(total),
                  beginTimeStr,
                  endTimeDate.toString());
    }
    
    
}