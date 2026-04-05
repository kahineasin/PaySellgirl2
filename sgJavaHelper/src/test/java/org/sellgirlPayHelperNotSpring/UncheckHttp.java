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

import org.sellgirlPayHelperNotSpring.model.SwaggerToClearHtml;

import com.sellgirl.sgJavaHelper.SGDate;
//import com.sellgirl.sgJavaHelper.SGHttpHelper;
//import com.sellgirl.sgJavaHelper.SGHttpHelper2;
import com.sellgirl.sgJavaHelper.SGHttpHelper;
import com.sellgirl.sgJavaHelper.SGRequestResult;
import com.sellgirl.sgJavaHelper.SGYmd;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.lrc.SGLrcLine;
import com.sellgirl.sgJavaHelper.lrc.SGLrcHelper;
import com.sellgirl.sgJavaHelper.time.SGTimeSpan;
/**
 * 测试输入事件，比如键盘
 */
public class UncheckHttp extends TestCase{

//	public void testHttpGet()  {
//		try {
//			SGRequestResult r=SGHttpHelper.HttpGet(
////					"http://localhost:8080/v2/api-docs?group=princess"
//					"http://localhost:8080/v2/api-docs?group=shop"
////					"http://localhost:8080/v2/api-docs"
//					, null);
//	        if(!r.success) {
//	        	throw new Exception("文档地址异常");
//	        }
//	        String swaggerJson=r.content;
//			SwaggerToClearHtml.generateHtml(swaggerJson,"docs/index.html");
////			SwaggerToClearHtml2.generateHtml(swaggerJson,"docs/index.html");
//////			GenerateApiDocTest2.generateHtml(swaggerJson);
////			GenerateApiDocTest3.generateHtml(swaggerJson);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public void testHttpGet2()  {
		try {
			SGRequestResult r=SGHttpHelper.HttpGet(
//					"http://localhost:8080/v2/api-docs?group=princess"
					"http://localhost:8080/v2/api-docs?group=shop"
//					"http://localhost:8080/v2/api-docs"
					, null);
	        if(!r.success) {
	        	throw new Exception("文档地址异常");
	        }
	        String swaggerJson=r.content;
			SwaggerToClearHtml.generateHtml(swaggerJson,"docs/index.html");
//			SwaggerToClearHtml2.generateHtml(swaggerJson,"docs/index.html");
////			GenerateApiDocTest2.generateHtml(swaggerJson);
//			GenerateApiDocTest3.generateHtml(swaggerJson);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
