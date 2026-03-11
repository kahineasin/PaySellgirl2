package org.sellgirlPayHelperNotSpring;

import junit.framework.TestCase;
import org.sellgirlPayHelperNotSpring.model.PFConfigTestMapper;
import org.sellgirlPayHelperNotSpring.model.TransferTaskTest002;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sellgirl.sgJavaHelper.AuthenticatorGenerator;
import com.sellgirl.sgJavaHelper.HostType;
import com.sellgirl.sgJavaHelper.PFEmailListener;
import com.sellgirl.sgJavaHelper.PFEmailManager;
import com.sellgirl.sgJavaHelper.SGEmailSend;
import com.sellgirl.sgJavaHelper.PFListenNewEmailTask;
import com.sellgirl.sgJavaHelper.PFSqlCommandTimeoutSecond;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGRef;
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
 * 不要随便更改此类名,以防打包时执行了此类
 * 
 * @author Administrator
 *
 */
@SuppressWarnings(value = { "unused", "rawtypes", "serial", "deprecation" })
public class UncheckEmail001 extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(UncheckEmail001.class);

//	public static void initPFHelper() {
//		PFDataHelper.SetConfigMapper(new PFConfigTestMapper());
//		new PFDataHelper(new PFAppConfig());
//	}

	public void testEmailSendAndListen() throws InterruptedException, FileNotFoundException {
		Uncheck001.initPFHelper();
		String[] emails= new String[]{"wxj@perfect99.com"};
		boolean[] b=new boolean[]{false};
		PFEmailListener emailListener = PFEmailListener.init(
				new PFEmailManager(SGEmailSend.EMAIL_OWNER_ADDR_HOST_PROPERTY, /* PFEmailSend.EMAIL_OWNER_ADDR_HOST, */
						SGEmailSend.EMAIL_OWNER_ADDR, SGEmailSend.EMAIL_OWNER_ADDR_PASS));

		PFListenNewEmailTask _emailListenTask3 = new PFListenNewEmailTask("dataMonitorEmailListener",
//				new PFEmailManager(HostType.PERFECT.getProperties(), /* PFEmailSend.EMAIL_OWNER_ADDR_HOST, */
//						PFEmailSend.EMAIL_OWNER_ADDR, PFEmailSend.EMAIL_OWNER_ADDR_PASS),
				emailListener, email -> {
			if("测试发邮件20230320".equals(email.getSubject())){
				b[0]=true;
			}
		}, (email// ,task
		) -> {
			return email.getSubject() != null && email.getSubject().indexOf("测试发邮件20230320") == 0;// 这里不要用>-1,否则可能把自动回复的邮件也当作是了
		}, false);
		_emailListenTask3.Start();


		Thread.sleep(5000);
		SGEmailSend.SendMail(emails,
				"测试发邮件20230320",  "测试发邮件20230320");
		while(!b[0]){
			Thread.sleep(2000);
		}
		System.out.println("测试通过");
	}

	public void testEmailSendAndListen2() throws InterruptedException {
		Uncheck001.initPFHelper();

    	//PFEmailSend.EMAIL_OWNER_ADDR_HOST="smtp.163.com";
		SGEmailSend.EMAIL_OWNER_ADDR_HOST="";
    	SGEmailSend.EMAIL_OWNER_ADDR="miuxiaoniao@126.com";
    	SGEmailSend.EMAIL_OWNER_ADDR_PASS=testPwd;
		SGEmailSend.EMAIL_OWNER_ADDR_HOST_PROPERTY= HostType.NETEASE2.getProperties();

		//String[] emails= new String[]{"li@sellgirl.com"};
		String[] emails= new String[]{"miuxiaoniao@126.com"};
		String title="测试发邮件20230320_2_"+ SGDate.Now().toString();
		boolean[] b=new boolean[]{false};
		PFEmailListener emailListener = PFEmailListener.init(
				new PFEmailManager(SGEmailSend.EMAIL_OWNER_ADDR_HOST_PROPERTY, /* PFEmailSend.EMAIL_OWNER_ADDR_HOST, */
						SGEmailSend.EMAIL_OWNER_ADDR, SGEmailSend.EMAIL_OWNER_ADDR_PASS));

		PFListenNewEmailTask _emailListenTask3 = new PFListenNewEmailTask("dataMonitorEmailListener",
//				new PFEmailManager(HostType.PERFECT.getProperties(), /* PFEmailSend.EMAIL_OWNER_ADDR_HOST, */
//						PFEmailSend.EMAIL_OWNER_ADDR, PFEmailSend.EMAIL_OWNER_ADDR_PASS),
				emailListener, email -> {
			if(title.equals(email.getSubject())){
				b[0]=true;
			}
		}, (email// ,task
		) -> {
			return email.getSubject() != null && email.getSubject().indexOf(title) == 0;// 这里不要用>-1,否则可能把自动回复的邮件也当作是了
		}, false);
		_emailListenTask3.Start();


		Thread.sleep(5000);
		SGEmailSend.SendMail(emails,
				title,  title);
		while(!b[0]){
			Thread.sleep(2000);
		}
		System.out.println("测试通过");
	}
	/**
	 * ok
	 * @throws InterruptedException
	 */
	public void testEmailSend() throws InterruptedException {
		Uncheck001.initPFHelper();


		String title="测试发邮件20230814_4_"+ SGDate.Now().toString();

		String[] emails= new String[]{"li@sellgirl.com"};
		SGEmailSend.SendMail(emails,
				title,  title);
		Thread.sleep(2000);
//		while(true) {
//			Thread.sleep(2000);
//		}
		//System.out.println("测试通过");
	}
	/**
	 * 实测不能改地址
	 * @throws InterruptedException
	 */
	public void testEmailSend5() throws InterruptedException {
		Uncheck001.initPFHelper();


		String title="测试发邮件20230814_4_"+ SGDate.Now().toString();

		String[] emails= new String[]{"li@sellgirl.com"};
		SGEmailSend.SendMail2(emails,
				title,  title,"li@sellgirl.com","aabb@126.com");
		Thread.sleep(2000);
//		while(true) {
//			Thread.sleep(2000);
//		}
		//System.out.println("测试通过");
	}
	private static String testPwd="";
	public void testEmailSend2() throws InterruptedException {
		Uncheck001.initPFHelper();

		//PFEmailSend.EMAIL_OWNER_ADDR_HOST="smtp.163.com";
		SGEmailSend.EMAIL_OWNER_ADDR_HOST="smtp.126.com";
		SGEmailSend.EMAIL_OWNER_ADDR="miuxiaoniao@126.com";
		SGEmailSend.EMAIL_OWNER_ADDR_PASS=testPwd;
		SGEmailSend.EMAIL_OWNER_ADDR_HOST_PROPERTY= HostType.NETEASE2.getProperties();

		String title="测试发邮件20230320_4_"+ SGDate.Now().toString();

		String[] emails= new String[]{"miuxiaoniao@126.com"};
		SGEmailSend.SendMail(emails,
				title,  title);
		Thread.sleep(2000);
		//System.out.println("测试通过");
	}

	public void testEmailSend3() throws InterruptedException {
		Uncheck001.initPFHelper();

		SGEmailSend.EMAIL_OWNER_ADDR_HOST="smtp.sellgirl.com";
		SGEmailSend.EMAIL_OWNER_ADDR="li@sellgirl.com";
		SGEmailSend.EMAIL_OWNER_ADDR_PASS="AxaPDzg4eaOh9Jwj";
		SGEmailSend.EMAIL_OWNER_ADDR_HOST_PROPERTY= HostType.SELLGIRL.getProperties();

		String title="测试发邮件20230320_4_"+ SGDate.Now().toString();

		String[] emails= new String[]{"miuxiaoniao@126.com"};
		SGEmailSend.SendMail(emails,
				title,  title);
		Thread.sleep(2000);
		//System.out.println("测试通过");
	}

	/**
	 * ok
	 */
    public static void testEmailSend4(  ) 
    		//throws MessagingException 
    {
		String[] emails= new String[]{"miuxiaoniao@126.com"};
		String title="测试发邮件20230814_1_"+ SGDate.Now().toString();
		String content=title;
		
		String EMAIL_OWNER_ADDR="li@sellgirl.com";
		String EMAIL_OWNER_ADDR_PASS="";
    	try {
//            if(PFDataHelper.StringIsNullOrWhiteSpace(EMAIL_OWNER_ADDR)){
//                return;
//            }
            Properties prop = new Properties();

//            String hostStr=EMAIL_OWNER_ADDR_HOST_PROPERTY.contains("mail.host")?EMAIL_OWNER_ADDR_HOST_PROPERTY.get("mail.host"):EMAIL_OWNER_ADDR_HOST;
            //String hostStr=EMAIL_OWNER_ADDR_HOST_PROPERTY.getProperty("mail.host",EMAIL_OWNER_ADDR_HOST);
            String hostStr="smtp.sellgirl.com";
            //System.out.println(hostStr);
            prop.put("mail.host",hostStr );
            prop.put("mail.transport.protocol", "smtp");
            prop.put("mail.smtp.host", hostStr);
            prop.put("mail.smtp.port", "25");
            prop.put("mail.smtp.auth", "true");
            //如果不加下面的这行代码 windows下正常，linux环境下发送失败，解决：http://www.cnblogs.com/Harold-Hua/p/7029117.html
            prop.setProperty("mail.smtp.ssl.enable", "true");
            //使用java发送邮件5步骤
            //1.创建sesssion
            Session session = Session.getInstance(prop);
            //开启session的调试模式，可以查看当前邮件发送状态
            //session.setDebug(true);

            //2.通过session获取Transport对象（发送邮件的核心API）
            Transport ts = session.getTransport();
            //3.通过邮件用户名密码链接，阿里云默认是开启个人邮箱pop3、smtp协议的，所以无需在阿里云邮箱里设置
            ts.connect(EMAIL_OWNER_ADDR, EMAIL_OWNER_ADDR_PASS);

            //4.创建邮件
            //创建邮件对象
            MimeMessage mm = new MimeMessage(session);
            //设置发件人
            mm.setFrom(new InternetAddress(EMAIL_OWNER_ADDR));
            //设置收件人
            //mm.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
//            mm.setRecipient(Message.RecipientType.TO, new InternetAddress(emails[0]));
//            mm.setRecipients(Message.RecipientType.TO, PFDataHelper.ObjectToArray(PFDataHelper.ListSelect( Arrays.asList(emails), b-> {
//    			try {
//    				return new InternetAddress(b);
//    			} catch (AddressException e) {
//    				
//    				e.printStackTrace();
//    			}
//    			return null;
//    		}), Address.class));
            for(int i=0;i<emails.length;i++) {
            	mm.addRecipients(Message.RecipientType.TO, emails[i]);
            }
            //mm.addRecipients(Message.RecipientType.TO, "");
            //.setr.setRecipients(Message.RecipientType.TO,email);
            //设置抄送人
            //mm.setRecipient(Message.RecipientType.CC, new InternetAddress("XXXX@qq.com"));

            //mm.setSubject("吸引力注册邮件");
            mm.setSubject(title);

            //mm.setContent("您的注册验证码为:<b style=\"color:blue;\">0123</b>", "text/html;charset=utf-8");
            mm.setContent(content, "text/html;charset=utf-8");

            // true表示开始附件模式 -----------------------------------------------------------------------
            /*MimeMessageHelper messageHelper = new MimeMessageHelper(mm, true, "utf-8");
            // 设置收件人，寄件人
            messageHelper.setTo(email);
            messageHelper.setFrom(EMAIL_OWNER_ADDR);
            messageHelper.setSubject(title);
            // true 表示启动HTML格式的邮件
            messageHelper.setText(content, true);

            FileSystemResource file1 = new FileSystemResource(new File("d:/rongke.log"));
            FileSystemResource file2 = new FileSystemResource(new File("d:/新建文本文档.txt"));
            // 添加2个附件
            messageHelper.addAttachment("rongke.log", file1);
            try {
                //附件名有中文可能出现乱码
                messageHelper.addAttachment(MimeUtility.encodeWord("新建文本文档.txt"), file2);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new MessagingException();
            }*/
            //-------------------------------------------------------------------------------------------
            //5.发送电子邮件

            ts.sendMessage(mm, mm.getAllRecipients());
    		
    	}catch(Exception e) {
    		//PFDataHelper.WriteError(new Throwable(),e);
//    		PFDataHelper.WriteError(e);
    		e.printStackTrace();
    		System.out.println("failed");
    		return;
    	}

		System.out.println("success");
    }

	public void testEmailReceive() throws InterruptedException, FileNotFoundException {
		Uncheck001.initPFHelper();

		PFEmailManager man = new PFEmailManager(SGEmailSend.EMAIL_OWNER_ADDR_HOST_PROPERTY, /* PFEmailSend.EMAIL_OWNER_ADDR_HOST, */
				SGEmailSend.EMAIL_OWNER_ADDR, SGEmailSend.EMAIL_OWNER_ADDR_PASS);

		man.Connect_Click();
		System.out.println("测试通过");
	}

	public  void testEmailReceive2()//Properties props, Authenticator authenticator, String protocol,int msgnum) { 
	{
		Uncheck001.initPFHelper();
		
		Properties props=HostType.SELLGIRL.getProperties();
		Authenticator authenticator=AuthenticatorGenerator.getAuthenticator(SGEmailSend.EMAIL_OWNER_ADDR, 
				SGEmailSend.EMAIL_OWNER_ADDR_PASS);
//		Message[] messages = null;
		Message message = null;
		Session session = Session.getDefaultInstance(props, authenticator);
		// session.setDebug(true);
		//Store store = null;
		//Folder folder = null;
		try {
			Store store = session.getStore();
			store.connect();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("failed");
			return ;
		}
		System.out.println("success");
		return ;
	}
}
