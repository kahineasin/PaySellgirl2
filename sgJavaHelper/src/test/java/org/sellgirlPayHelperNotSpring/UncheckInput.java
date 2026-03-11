package org.sellgirlPayHelperNotSpring;

import junit.framework.TestCase;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.table.TableColumn;

import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGYmd;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.lrc.SGLrcLine;
import com.sellgirl.sgJavaHelper.lrc.SGLrcHelper;
import com.sellgirl.sgJavaHelper.time.SGTimeSpan;
/**
 * 测试输入事件，比如键盘
 */
public class UncheckInput extends TestCase{
	
	private String lrc="D:\\github\\mp3\\lrc\\s\\龙珠GT主题曲_坂井泉水.lrc";
//	private String lrc="D:\\github\\mp3\\lrc\\k\\017.寂寞_谢容儿_伴奏.lrc";
	private List<String> lines;
	private Long[] times;
	private String[] oldTimes;
	private int page=30;
    private int cur=-1;
    private int curTab=0;
//    private JTextArea ta;
//    private JTextArea ta2;
    private JTextArea[] ta;
    
    long start=-1;
    private Rectangle aRect;
    ScrollPaneLayout scroll;
    javax.swing.JTabbedPane tbp;
    private boolean saving=false;
    /**
     * swing的布局太烦了，不搞了
     * @param newIdx
     */
    private void updateTA(int newIdx) {
    	if(0>newIdx||lines.size()<=newIdx) {return;}
    	if(curTab!=newIdx/page) {
        	tbp.setSelectedComponent(ta[newIdx/page]);
        	curTab=newIdx/page;
    	}
    	if(true) {//newIdx!=cur) {
    		StringBuilder sb=new StringBuilder();
//    		StringBuilder sb1=new StringBuilder();
//    		StringBuilder sb2=new StringBuilder();
    		if(0==newIdx/page) {
    			sb.append("操作方式:w上一行 s下一行 n加时间 j上个tab k下个tab q开始 p保存 e退出 "+(-1<start?"已开始":"未开始")+"\r\n");
    			sb.append("========================================\r\n");
    		}
    		int idx=0;
    		for(String i:lines) {
    			if(curTab*page<=idx&&idx<(curTab+1)*page) {
//	    			StringBuilder sb=page>idx?sb1:sb2;
	    			if(newIdx==idx) {
	    				sb.append("cur--");
	    			}
	    			if(null!=times[idx]) {
	    				sb.append(timeToString(times[idx]));
	    			}
	    			if(null!=oldTimes[idx]) {
	    				sb.append(oldTimes[idx]);
	    			}
					sb.append(i+"\r\n");
    			}
    			idx++;
    		}
//    		JTextArea ta=20<newIdx?ta2:this.ta;
//    		ta.setText(sb1.toString());
//    		ta2.setText(sb2.toString());
    		ta[newIdx/page].setText(sb.toString());
//    		aRect.x=20*newIdx;
//    		aRect.y=20*newIdx;
//    		aRect.width=100;
//    		aRect.height=20*newIdx;
//    		ta.scrollRectToVisible(aRect);
//    		ta.doLayout();
    		cur=newIdx;
    	}
    }
    private void changeTab(int newIdx) {
    	if(0>newIdx||ta.length<=newIdx) {return;}
//    	tbp.setTitleAt(newIdx, String.valueOf(newIdx));
//    	tbp.setSelectedComponent(0==newIdx?ta:ta2);
    	tbp.setSelectedComponent(ta[newIdx]);
    	curTab=newIdx;
    	if(curTab*page<=cur&&cur<(curTab+1)*page) {
    		
    	}else {
    		cur=curTab*page;
    		updateTA(cur);
    	}
	}
    private String timeToString(long t) {
		SGTimeSpan ts=SGDataHelper.GetTimeSpan(t, SGYmd.Minute | SGYmd.Second|SGYmd.Millisecond);
		int ms=ts.Millisecond/10;//显示前2位
		String r="["+(10>ts.Minute?"0":"")+ts.Minute+":"
				+(10>ts.Second?"0":"")+ts.Second+"."
				+(10>ms?"0":"")+ms+"]";
//		System.out.println(s);
		return r;
    }
	public void testLrc() throws Exception {
        //Robot robot = new Robot();
        boolean[] exit=new boolean[] {false};
        
//        // 模拟按下和释放键盘上的按键
//        robot.keyPress(KeyEvent.VK_A);
//        robot.keyRelease(KeyEvent.VK_A);
        javax.swing.JTable tb=new javax.swing.JTable();
        
        JFrame frame = new JFrame("Keyboard Listener Example");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         lines=SGDataHelper.ReadFileToLines(lrc);
//         LinkedHashMap<String, String> lrcJSON=SGLrcHelper.accessLrc(lines);
         ArrayList<SGLrcLine> lrcJSON=SGLrcHelper.accessLrc(lines);

         lines=new ArrayList<String>();
         oldTimes=new String[lrcJSON.size()];
//       Iterator<String> iter = lrcJSON.keySet().iterator();
       int i=0;
//       while (iter.hasNext()) {
//           String key = iter.next();
//           String value = lrcJSON.get(key);
//           lines.add(value);
//           oldTimes[i]=key;
////           lrcTime.add(i++, SGLrcHelper.keyToTime(key));
//           i++;
//       }

         for(SGLrcLine line:lrcJSON) {
//             String key = iter.next();
//             String value = lrcJSON.get(key);
//             lines.add(value);
//             oldTimes[i]=key;
             lines.add(line.getL());
             oldTimes[i]=SGLrcHelper.timeToString(line.getT());
//             lrcTime.add(i++, SGLrcHelper.keyToTime(key));
             i++;
         }
         
         times=new Long[lines.size()];
         
         int tabCnt=lines.size()/page;
         if(tabCnt*page<lines.size()) {
        	 tabCnt+=1;
         }
         ta=new JTextArea[tabCnt];

         KeyAdapter listener=new KeyAdapter() {
             @Override
             public void keyPressed(KeyEvent e) {
             	if(e.getKeyCode()==KeyEvent.VK_E) {
             		if(saving) {return;}
             		exit[0]=true;
             	}
             	if(e.getKeyCode()==KeyEvent.VK_Q) {
             		start=SGDate.Now().toTimestamp();            		
//             		times=new Long[lines.size()];
             		updateTA(cur);
             	}
             	if(e.getKeyCode()==KeyEvent.VK_W) {
             		updateTA(cur-1);
             	}
             	if(e.getKeyCode()==KeyEvent.VK_S) {
             		updateTA(cur+1);
             	}
             	if(e.getKeyCode()==KeyEvent.VK_J) {
             		changeTab(curTab-1);
             	}
             	if(e.getKeyCode()==KeyEvent.VK_K) {
             		changeTab(curTab+1);
             	}
             	if(-1<start&&e.getKeyCode()==KeyEvent.VK_N) {
             		long n=SGDate.Now().toTimestamp();
             		times[cur]=n-start;
             		updateTA(cur);
             	}
             	if(e.getKeyCode()==KeyEvent.VK_P) {
             		saveLrc();
             	}
//                 System.out.println("Key Pressed: " + KeyEvent.getKeyText(e.getKeyCode()));
             }
         };
         tbp=new javax.swing.JTabbedPane();
         for( i=0;tabCnt>i;i++) {
        	 JTextArea ta2=new JTextArea();
             ta2.setEditable(false);
        	 ta[i]=ta2;
        	 ta2.addKeyListener(listener);
             tbp.add(String.valueOf(i),ta2);
         }
//         ta=new JTextArea();
//         ta2=new JTextArea();
//         ta.setEditable(false);
//         ta2.setEditable(false);
//          aRect=new Rectangle(0,10,100,20);
//          scroll=new ScrollPaneLayout();
        updateTA(0); 
//        ta.setAutoscrolls(true);
//        ta.addKeyListener(listener);
//        ta2.addKeyListener(listener);
//        scroll.addLayoutComponent(ScrollPaneConstants.HORIZONTAL_SCROLLBAR, ta);
//        frame.add(ta);
//        frame.add(ta);
//        tbp.set
//        TableColumn tc=new TableColumn();
//        tc.
//        tb.addColumn(null);.add(ta);tb.add(ta2);
        frame.add(tbp);
//        frame.add(ta2);
        frame.setFocusable(true); // 确保窗口可以获取焦点
        frame.setVisible(true);
		while(!exit[0]) {
			Thread.sleep(1000);
		}
	}
	private void saveLrc() {
		saving=true;
		ArrayList<String> newLines=new ArrayList();
		int idx=0;
		for(String i:lines) {
			if(null==times[idx]) {
				newLines.add(i);				
			}else {
				newLines.add(timeToString(times[idx])+i);
			}
			idx++;
		}
		SGDataHelper.WriteLinesToFile(newLines,lrc, false);
		saving=false;
	}

}
