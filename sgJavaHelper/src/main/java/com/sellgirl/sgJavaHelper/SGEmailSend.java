package com.sellgirl.sgJavaHelper;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * Created by Administrator on 2017/9/29 0029.
 *
 * 系统邮箱测试通过的类型:
 * perfect99.com, 126.com
 *
 * 目标邮箱测试通过的类型:
 * perfect99.com, 126.com
 *
 *
 * https://www.cnblogs.com/007sx/p/7610709.html
 */
public class SGEmailSend {
//    private static final String EMAIL_OWNER_ADDR_HOST = "smtp.aliyun.com"; //smtp.163.com  smtp.aliyun.com  smtp.qq.com
//    private static final String EMAIL_OWNER_ADDR = "xxx@aliyun.com";
//    private static final String EMAIL_OWNER_ADDR_PASS = "xxx";
    public static  String EMAIL_OWNER_ADDR_HOST =null;// "smtp.perfect99.com"; //smtp.163.com  smtp.aliyun.com  smtp.qq.com
    /**
     * 发系统邮件时的系统邮箱.
     * 如果不设置此邮件地址,系统邮件监听的功能可能会受到影响
     */
    public static  String EMAIL_OWNER_ADDR = "wxj@perfect99.com";
    public static  String EMAIL_OWNER_ADDR_PASS = "";
    public static  Properties EMAIL_OWNER_ADDR_HOST_PROPERTY = HostType.PERFECT.getProperties();

    public static void SendMail(String[] emails,String title,  String content) 
    		//throws MessagingException 
    {
    	try {
            if(SGDataHelper.StringIsNullOrWhiteSpace(EMAIL_OWNER_ADDR)){
                return;
            }
            Properties prop = new Properties();

//            String hostStr=EMAIL_OWNER_ADDR_HOST_PROPERTY.contains("mail.host")?EMAIL_OWNER_ADDR_HOST_PROPERTY.get("mail.host"):EMAIL_OWNER_ADDR_HOST;
            //String hostStr=EMAIL_OWNER_ADDR_HOST_PROPERTY.getProperty("mail.host",EMAIL_OWNER_ADDR_HOST);
            String hostStr=SGDataHelper.StringIsNullOrWhiteSpace(EMAIL_OWNER_ADDR_HOST)?EMAIL_OWNER_ADDR_HOST_PROPERTY.getProperty("mail.host"):EMAIL_OWNER_ADDR_HOST;
            //System.out.println(hostStr);
            prop.put("mail.host",hostStr );
            prop.put("mail.transport.protocol", "smtp");
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
            mm.setSubject(title,SGDataHelper.encoding);

            //mm.setContent("您的注册验证码为:<b style=\"color:blue;\">0123</b>", "text/html;charset=utf-8");
            mm.setContent(content, "text/html;charset=utf-8");
            mm.setSentDate(SGDate.Now().ToCalendar().getTime());

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
    		SGDataHelper.WriteError(e);
    	}
    }
    /**
     * 想改地址试试,没成功
     * @param emails
     * @param title
     * @param content
     * @param from
     */
    @Deprecated
    public static void SendMail2(String[] emails,String title,  String content,String from,String from2) 
	//throws MessagingException 
{
try {
    if(SGDataHelper.StringIsNullOrWhiteSpace(EMAIL_OWNER_ADDR)){
        return;
    }
    Properties prop = new Properties();

//    String hostStr=EMAIL_OWNER_ADDR_HOST_PROPERTY.contains("mail.host")?EMAIL_OWNER_ADDR_HOST_PROPERTY.get("mail.host"):EMAIL_OWNER_ADDR_HOST;
    //String hostStr=EMAIL_OWNER_ADDR_HOST_PROPERTY.getProperty("mail.host",EMAIL_OWNER_ADDR_HOST);
    String hostStr=SGDataHelper.StringIsNullOrWhiteSpace(EMAIL_OWNER_ADDR_HOST)?EMAIL_OWNER_ADDR_HOST_PROPERTY.getProperty("mail.host"):EMAIL_OWNER_ADDR_HOST;
    //System.out.println(hostStr);
    prop.put("mail.host",hostStr );
    prop.put("mail.transport.protocol", "smtp");
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
//    mm.setFrom(new InternetAddress(EMAIL_OWNER_ADDR));
    mm.setFrom(new InternetAddress(from,from2));
    //设置收件人
    //mm.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
//    mm.setRecipient(Message.RecipientType.TO, new InternetAddress(emails[0]));
//    mm.setRecipients(Message.RecipientType.TO, PFDataHelper.ObjectToArray(PFDataHelper.ListSelect( Arrays.asList(emails), b-> {
//		try {
//			return new InternetAddress(b);
//		} catch (AddressException e) {
//			
//			e.printStackTrace();
//		}
//		return null;
//	}), Address.class));
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
	SGDataHelper.WriteError(e);
}
}

    public static void SendMailMultipart(String[] emails,String title,  String content,List<File> fileList) 
    		//throws MessagingException 
    {
    	try {

            Properties prop = new Properties();
            prop.put("mail.host", EMAIL_OWNER_ADDR_HOST);
            prop.put("mail.transport.protocol", "smtp");
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

//            //mm.setContent("您的注册验证码为:<b style=\"color:blue;\">0123</b>", "text/html;charset=utf-8");
//            mm.setContent(content, "text/html;charset=utf-8");

         // 6，设置邮件内容，混合模式
         MimeMultipart msgMultipart = new MimeMultipart("mixed");
         mm.setContent(msgMultipart);
      // 7，设置消息正文
         MimeBodyPart mbp = new MimeBodyPart();
         msgMultipart.addBodyPart(mbp);
          
         // 8，设置正文格式
         MimeMultipart bodyMultipart = new MimeMultipart("related");
         mbp.setContent(bodyMultipart);
          
         // 9，设置正文内容
         MimeBodyPart htmlPart = new MimeBodyPart();
         bodyMultipart.addBodyPart(htmlPart);
         htmlPart.setContent(content, "text/html;charset=UTF-8");

      // 10，设置附件
      if (fileList!=null&&(!fileList.isEmpty())) {
          fileList.stream().forEach(file -> {
             if (file == null || !file.exists()) {
                 return;
             }
             try {
                 //设置相关文件
                 MimeBodyPart filePart = new MimeBodyPart();
                 FileDataSource dataSource = new FileDataSource(file);
                 DataHandler dataHandler = new DataHandler(dataSource);
                 // 文件处理
                 filePart.setDataHandler(dataHandler);
                 // 附件名称
                 filePart.setFileName(file.getName());
                 // 放入正文（有先后顺序，所以在正文后面）
                 msgMultipart.addBodyPart(filePart);
             } catch (Exception e) {
                 //logger.error("send mail file error fileName={}", file.getName(), e);
             }
          });
      }


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
    		SGDataHelper.WriteError(e);
    	}
    }
    public static void main(String[] args) throws MessagingException {
        //sendMail("吸引力注册邮件", "xxx@qq.com", "您的注册验证码为:<b style=\"color:blue;\">651899</b>");
        //sendMail("吸引力", "xxx@qq.com", "spring boot 邮件测试");
        SendMail(new String[] {"wxj@perfect99.com"},"吸引力",  "spring boot 邮件测试");
//    	PFDate today=PFDate.Now();
//        String ymd=today.toString(PFDataHelper.YMDFormat);
//        SendMailMultipart(new String[] {"li@sellgirl.com"},"吸引力",  "spring boot 邮件测试",new ArrayList<File>() {/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//		{add(new File(Paths.get("log", "pfError_"+ymd+".txt").toString()));}});
    }
}
