package com.sellgirl.sgJavaHelper.lrc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.SGYmd;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.time.SGTimeSpan;

public class SGLrcHelper {
	private  static final String tag="SGLrcHelper"; 
    public static float keyToTime(String key) {
//        return parseFloat(key.substr(1, 3)) * 60 + parseFloat(key.substring(4, 10));
//        return Float.parseFloat(key.substring(1, 3)) * 60 + Float.parseFloat(key.substring(4, 10));
        return Float.parseFloat(key.substring(1, 3)) * 60 + Float.parseFloat(key.substring(4, 9));
    }
    public static String timeToString(float second) {
    	long t=(long) (second*1000);
		SGTimeSpan ts=SGDataHelper.GetTimeSpan(t, SGYmd.Minute | SGYmd.Second|SGYmd.Millisecond);
		int ms=ts.Millisecond/10;//显示前2位
		String r="["+(10>ts.Minute?"0":"")+ts.Minute+":"
				+(10>ts.Second?"0":"")+ts.Second+"."
				+(10>ms?"0":"")+ms+"]";
//		System.out.println(s);
		return r;
    }
    public static boolean isTimeLine(String s) {
        byte[] b = s.getBytes();
        //格式[00:00.00]
        if(s != null //&& s !== undefined
            && s.length() > 9 && b[0] == '[' && b[3] == ':'&&b[6]=='.' && b[9] == ']'){
            return true;
        }
        return false;
    }

//    public static void setLineToTime(String line,
//                               //HashMap<String, String> lrcJSON,
//                               ArrayList<SGLrcLine> lrcJSONArray) {//为了适应压缩版的歌词(一行有多个时间标签)
////        var timeArr = [];
////        ArrayList<String> timeArr = new ArrayList<>();
//        ArrayList<Float> timeArr = new ArrayList<>();
//        while (isTimeLine(line)) {
////            timeArr.add(line.substring(0, 10));
//            timeArr.add(SGLrcHelper.keyToTime(line.substring(0, 10)));
////            line = line.substr(10, line.length - 10);
////            line = line.substring(10, line.length() - 10);
//            line = line.substring(10);
//        }
//
//        for (int i = 0; i < timeArr.size(); i++) {
//////            lrcJSON[timeArr[i]] = line;
////            lrcJSON.put(timeArr.get(i), line);
////            lrcJSONArray.push({ t: timeArr[i], l: line });
//            lrcJSONArray.add(new SGLrcLine(timeArr.get(i), line));
//        }
//    }

//    //时间默认跟随下一行,有的歌词有注意行（无时间）
    public static void setLineToTime(String line,
                               //HashMap<String, String> lrcJSON,
                               ArrayList<SGLrcLine> lrcJSONArray,
                               String noTimeLine
                               //SGRef<Float> defaultTime
                               ) {//为了适应压缩版的歌词(一行有多个时间标签)
        ArrayList<Float> timeArr = new ArrayList<>();
        while (isTimeLine(line)) {
            timeArr.add(SGLrcHelper.keyToTime(line.substring(0, 10)));
            line = line.substring(10);
        }

        for (int i = 0; i < timeArr.size(); i++) {
        	if(null!=noTimeLine) {
        		//注意这里的注音行逻辑不能简单地移动到主函数，因为被注音的行可能有多个时间，就需生成多个注音行
                lrcJSONArray.add(new SGLrcLine(timeArr.get(i), noTimeLine));	
        	}
            lrcJSONArray.add(new SGLrcLine(timeArr.get(i), line));
        }
    }
    /**
     * 处理下载好的歌词到cache, 但暂不更换到ui, 因为可能要等歌下载（异步）
     *
     * @param songUrl
     * @param data
     */
    public static //LinkedHashMap<String, String> 
    ArrayList<SGLrcLine>
    accessLrc(
        //String songUrl,
        //String data//
        
        List<String> lines
    ) {


       // String[] lrcArray = data.indexOf("\r\n") > -1 ? data.split("\r\n") : data.split("\n");// linux系统是\n换行(如github的web系统里面)

        String[] lrcArray=lines.toArray(new String[lines.size()]);
        
//        String lrcTitle = "";
//        LinkedHashMap<String, String> lrcJSON = new LinkedHashMap<>();
        ArrayList<SGLrcLine> lrcJSONArray = new ArrayList<>();
        // debugger;
//        lrcJSON['[00:00.00]'] = '';//顺序对后面$.foreach是有影响的
////      注意有的lrc,首先几句标题，下面第一行歌词就是时间0
//        lrcJSON.put("[00:00.00]", "");//顺序对后面$.foreach是有影响的

//        for (int j = 0; j < lrcArray.length; j++) {
//            //                            if (lrcArray[j].indexOf(']') === 9) {
//            if (SGLrcHelper.isTimeLine(lrcArray[j])) {
//                SGLrcHelper.setLineToTime(lrcArray[j], //lrcJSON, 
//                		lrcJSONArray);
//                
//            } else {
////                lrcTitle += (SGDataHelper.StringIsNullOrWhiteSpace(lrcTitle)? lrcArray[j]:("\r\n"+lrcArray[j]));
//                lrcJSONArray.add(new SGLrcLine(0,lrcArray[j]));
//            }
//        }

//        ArrayList<SGLrcLine> noTimeLine = new ArrayList<>();
        String noTimeLine = null;//可能有注音等无时间的行，时间跟随后一行
        boolean passTitle=false;
        for (int j = 0; j < lrcArray.length; j++) {
            //                            if (lrcArray[j].indexOf(']') === 9) {
            if (SGLrcHelper.isTimeLine(lrcArray[j])) {
//                SGLrcHelper.setLineToTime(lrcArray[j], //lrcJSON, 
//                		lrcJSONArray);
                SGLrcHelper.setLineToTime(lrcArray[j], //lrcJSON, 
                		lrcJSONArray,noTimeLine);
                if(!passTitle&& 0<lrcJSONArray.get(lrcJSONArray.size()-1).getT()) {
                	passTitle=true;
                }
                noTimeLine=null;
            } else {
            	if(passTitle) {
            		noTimeLine=lrcArray[j];
            	}else {
            		lrcJSONArray.add(new SGLrcLine(0,lrcArray[j]));
            	}
            }
        }
//        //if (lrcJSON['[00:00.00]'] === undefined && lrcJSON['[00:00:00]'] === undefined) {
//        lrcJSON['[00:00.00]'] = lrcTitle;

        
        //
        //                            var aa=$.sort(lrcJSON, function (a,b){
        //                                return parseInt(a.replace(':','').replace('.',''))-parseInt(b.replace(':','').replace('.',''));
        //                            });

        try {

            //压缩版歌词需要重新排序--benjamin 20201205
//            ArrayList<SGLrcLine> lrcJSONArraySort = new ArrayList<>(lrcJSONArray);
            Collections.sort(lrcJSONArray, new Comparator<SGLrcLine>() {
                public int compare(SGLrcLine a, SGLrcLine b) {
//                    return ((Float) (SGLrcHelper.keyToTime(a.getT()) - SGLrcHelper.keyToTime(b.getT()))).intValue();
                	int r=((Float) (a.getT() - b.getT())).intValue();
                	if(0==r&&a.getT()!=b.getT()) {
                		return a.getT()>b.getT()?1:-1;
                	}
                    return r;
                }
            });
//            lrcJSON.clear();//这里和原版不一至，如果有问题就new吧 --benjamin todo
//            for (int i = 0; i < lrcJSONArraySort.size(); i++) {
//                lrcJSON.put(lrcJSONArraySort.get(i).getT(), lrcJSONArraySort.get(i).getL());
//            }
        } catch (Throwable e) {
            SGDataHelper.getLog().printException(e, tag);
        }

//////      注意有的lrc,首先几句标题，下面第一行歌词就是时间0
////        if(lrcJSON.containsKey("[00:00.00]")) {
////        	if(!SGDataHelper.StringIsNullOrWhiteSpace(lrcTitle)) {
////        		lrcJSON.put("[00:00.00]", lrcTitle+"\r\n"+lrcJSON.get("[00:00.00]"));	
////        	}
////        }else {
////        	lrcJSON.put("[00:00.00]", lrcTitle);//顺序对后面$.foreach是有影响的
////        }
////        lrcJSONArray.add(0,new LrcLine("[00:00.00]", lrcTitle));
//        lrcJSONArray.add(0,new SGLrcLine(0, lrcTitle));
////        lrcJSONArray.push({ t: '[00:00.00]', l: lrcTitle });

//        lrcJSONCache.put(songUrl, lrcJSON);

// //歌词对应的时间数组
//        ArrayList<Float> lrcTime = new ArrayList<>();
//
//        int i = 0;
//        Iterator<String> iter = lrcJSON.keySet().iterator();
//        while (iter.hasNext()) {
//            String key = iter.next();
//            String value = lrcJSON.get(key);
//            lrcTime.add(i++, SGLrcHelper.keyToTime(key));
//        }
////        lrcTimeCache.put(songUrl, lrcTime);

        return lrcJSONArray;
    }
}
