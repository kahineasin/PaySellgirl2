package org.sellgirlPayHelperNotSpring;

import junit.framework.TestCase;

import org.sellgirlPayHelperNotSpring.model.GenerateApiDocTest;
import org.sellgirlPayHelperNotSpring.model.GenerateApiDocTest2;
import org.sellgirlPayHelperNotSpring.model.GenerateApiDocTest3;
import org.sellgirlPayHelperNotSpring.model.PFConfigTestMapper;
import org.sellgirlPayHelperNotSpring.model.TransferTaskTest002;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sellgirl.sgJavaHelper.AuthenticatorGenerator;
import com.sellgirl.sgJavaHelper.HostType;
import com.sellgirl.sgJavaHelper.PFEmailListener;
import com.sellgirl.sgJavaHelper.PFEmailManager;
import com.sellgirl.sgJavaHelper.SGEmailSend;
import com.sellgirl.sgJavaHelper.SGHttpHelper;
import com.sellgirl.sgJavaHelper.PFListenNewEmailTask;
import com.sellgirl.sgJavaHelper.PFSqlCommandTimeoutSecond;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.SGRequestResult;
import com.sellgirl.sgJavaHelper.config.PFAppConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlTransferItem;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 解释swagger的json,因为3方库太垃圾
 * 
 * @author Administrator
 *
 */
@SuppressWarnings(value = { "unused", "rawtypes", "serial", "deprecation" })
public class UncheckSwaggerDocs extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(UncheckSwaggerDocs.class);

//	public static void initPFHelper() {
//		PFDataHelper.SetConfigMapper(new PFConfigTestMapper());
//		new PFDataHelper(new PFAppConfig());
//	}

	public void testGenDocs()  {
		try {
			SGRequestResult r=SGHttpHelper.HttpGet(
					"http://localhost:8080/v2/api-docs?group=princess"
//					"http://localhost:8080/v2/api-docs?group=shop"
//					"http://localhost:8080/v2/api-docs"
					, null);
	        if(!r.success) {
	        	throw new Exception("文档地址异常");
	        }
	        String swaggerJson=r.content;
			GenerateApiDocTest.generateHtml(swaggerJson);
//			GenerateApiDocTest2.generateHtml(swaggerJson);
			GenerateApiDocTest3.generateHtml(swaggerJson);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
