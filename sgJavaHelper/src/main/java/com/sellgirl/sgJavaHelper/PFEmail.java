package com.sellgirl.sgJavaHelper;

import java.io.UnsupportedEncodingException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

//import org.apache.commons.mail.util.MimeMessageParser;
public class PFEmail {
	private Message message;
	//private PFEmailRecive recive;
	private String from;
	private String bodyText;
	private String htmlContent;
	private String plainContent;
	private String subject;
	public PFEmail(Message message) {
		this.message=message;
		
		//使用PFEmailRecive类
		//recive=new PFEmailRecive();		
		//一定要尽早使用message里的方法,因为如果message对应的folder被释放之后,再调用就会报错了
//		 try {
//			recive.getMailContent(message);
//			 bodyText= recive.getBodyText();
//		} catch (MessagingException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		 //subject=getSubject(message);
		 

			try {
				org.apache.commons.mail.util.MimeMessageParser parser = new org.apache.commons.mail.util.MimeMessageParser((MimeMessage) message).parse();
				from = parser.getFrom(); // 获取发件人地址
//				List<Address> cc = parser.getCc();// 获取抄送人地址
//				List<Address> to = parser.getTo(); // 获取收件人地址
//				String replyTo = parser.getReplyTo();// 获取回复邮件时的收件人
				subject = parser.getSubject(); // 获取邮件主题
				 htmlContent = parser.getHtmlContent(); // 获取Html内容
				 plainContent = parser.getPlainContent(); // 获取纯文本邮件内容（注：有些邮件不支持html）
				 bodyText=htmlContent==null?plainContent:htmlContent;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public Message getMessage() {
		return message;
	}
	public String getFrom() {
		return from;
//		try {
//			return message.getFrom()[0].toString();
//		} catch (MessagingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}//From也能有多个?
//		return null;
	}
	 public  String getSubject() {
		 return subject;
//		 try {
//			return message.getSubject();
//		} catch (MessagingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//			return null;
	 }
	    /**
	     * 获取邮件主题
	     *
	     * @return
	     * @throws UnsupportedEncodingException
	     * @throws MessagingException
	     */
	    public static String getSubject(Message msg) //throws UnsupportedEncodingException, MessagingException 
	    {
	        String subject = "";
	        try {
				subject = MimeUtility.decodeText(msg.getSubject());
			} catch (UnsupportedEncodingException | MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        if (subject == null) {
	            subject = "";
	        }
	        return subject;
	    }
//	 public  String getBody() {
//		 try {
//			return message.getContent().toString();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//			return null;
//	 }
	 public  String getBody() {
		 return bodyText;
//		 try {
//			 recive.getMailContent(message);
//			 return recive.getBodyText();
////			 getMailContent(message);
////			 return getBodyText();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//			return null;
	 }
	 public  SGDate getSentDate() {
		 try {
			return new SGDate( message.getSentDate());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return null;
	 }

//	    private StringBuffer bodytext = new StringBuffer();
//	    /**
//	     * 获取邮件正文内容
//	     *
//	     * @return
//	     */
//	    public String getBodyText() {
//
//	        return bodytext.toString();
//	    }
//
//	    /**
//	     * 解析邮件，将得到的邮件内容保存到一个stringBuffer对象中，解析邮件 主要根据MimeType的不同执行不同的操作，一步一步的解析
//	     *
//	     * @param part
//	     * @throws MessagingException
//	     * @throws IOException
//	     */
//	    public void getMailContent(Part part) throws MessagingException, IOException {
//
//	        String contentType = part.getContentType();
//	        int nameindex = contentType.indexOf("name");
//	        boolean conname = false;
//	        if (nameindex != -1) {
//	            conname = true;
//	        }
//	        System.out.println("CONTENTTYPE:" + contentType);
//	        if (part.isMimeType("text/plain") && !conname) {
//	            bodytext.append((String) part.getContent());
//	        } else if (part.isMimeType("text/html") && !conname) {
//	            bodytext.append((String) part.getContent());
//	        } else if (part.isMimeType("multipart/*")) {
//	            Multipart multipart = (Multipart) part.getContent();
//	            int count = multipart.getCount();
//	            for (int i = 0; i < count; i++) {
//	                getMailContent(multipart.getBodyPart(i));
//	            }
//	        } else if (part.isMimeType("message/rfc822")) {
//	            getMailContent((Part) part.getContent());
//	        }
//
//	    }
//	    
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof PFEmail) {
				PFEmail t = (PFEmail) obj;
				//return t.getSentDate().compareTo(getSentDate() )==0&& t.getSubject().equals(getSubject());
				return t.getSentDate()!=null&&t.getSentDate().compareTo(getSentDate() )==0
						&& t.getSubject()!=null&&t.getSubject().equals(getSubject());
			}
			return false;
		}
	 
}
