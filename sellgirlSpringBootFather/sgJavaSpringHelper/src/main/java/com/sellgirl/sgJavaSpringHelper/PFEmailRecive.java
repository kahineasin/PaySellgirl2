package com.sellgirl.sgJavaSpringHelper;

//import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.search.AndTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SentDateTerm;
import javax.mail.search.SubjectTerm;
import java.io.*;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * https://blog.csdn.net/weixin_39947233/article/details/107837689
 * @author Administrator
 *
 */
public class PFEmailRecive {
    private MimeMessage msg = null;
    //private static String saveAttchPath = "";
    private StringBuffer bodytext = new StringBuffer();
    private String dateformate = "yy-MM-dd HH:mm";

    /**
     * 获取发送邮件者信息
     *
     * @return
     * @throws MessagingException
     */
    public String getFrom(Message message) throws MessagingException {
        InternetAddress[] address = (InternetAddress[]) message.getFrom();
        String from = address[0].getAddress();
        if (from == null) {
            from = "";
        }
        String personal = address[0].getPersonal();
        if (personal == null) {
            personal = "";
        }
        String fromaddr = personal + "<" + from + ">";
        return fromaddr;
    }

    /**
     * 获取邮件收件人，抄送，密送的地址和信息。根据所传递的参数不同 "to"-->收件人,"cc"-->抄送人地址,"bcc"-->密送地址
     *
     * @param type
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public String getMailAddress(String type) throws MessagingException, UnsupportedEncodingException {
        String mailaddr = "";
        String addrType = type.toUpperCase();
        InternetAddress[] address = null;

        if (addrType.equals("TO") || addrType.equals("CC") || addrType.equals("BCC")) {
            if (addrType.equals("TO")) {
                address = (InternetAddress[]) msg.getRecipients(Message.RecipientType.TO);
            }
            if (addrType.equals("CC")) {
                address = (InternetAddress[]) msg.getRecipients(Message.RecipientType.CC);
            }
            if (addrType.equals("BCC")) {
                address = (InternetAddress[]) msg.getRecipients(Message.RecipientType.BCC);
            }

            if (address != null) {
                for (int i = 0; i < address.length; i++) {
                    String mail = address[i].getAddress();
                    if (mail == null) {
                        mail = "";
                    } else {
                        mail = MimeUtility.decodeText(mail);
                    }
                    String personal = address[i].getPersonal();
                    if (personal == null) {
                        personal = "";
                    } else {
                        personal = MimeUtility.decodeText(personal);
                    }
                    String compositeto = personal + "<" + mail + ">";
                    mailaddr += "," + compositeto;
                }
                mailaddr = mailaddr.substring(1);
            }
        } else {
            throw new RuntimeException("Error email Type!");
        }
        return mailaddr;
    }

    /**
     * 获取邮件主题
     *
     * @return
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    public String getSubject() throws UnsupportedEncodingException, MessagingException {
        String subject = "";
        subject = MimeUtility.decodeText(msg.getSubject());
        if (subject == null) {
            subject = "";
        }
        return subject;
    }

    /**
     * 获取邮件发送日期
     *
     * @return
     * @throws MessagingException
     */
    public String getSendDate() throws MessagingException {
        Date sendDate = msg.getSentDate();
        SimpleDateFormat smd = new SimpleDateFormat(dateformate);
        return smd.format(sendDate);
    }

    /**
     * 获取邮件正文内容
     *
     * @return
     */
    public String getBodyText() {

        return bodytext.toString();
    }

    /**
     * 解析邮件，将得到的邮件内容保存到一个stringBuffer对象中，解析邮件 主要根据MimeType的不同执行不同的操作，一步一步的解析
     *
     * @param part
     * @throws MessagingException
     * @throws IOException
     */
    public void getMailContent(Part part) throws MessagingException, IOException {

        String contentType = part.getContentType();
        int nameindex = contentType.indexOf("name");
        boolean conname = false;
        if (nameindex != -1) {
            conname = true;
        }
        System.out.println("CONTENTTYPE:" + contentType);
        if (part.isMimeType("text/plain") && !conname) {
            bodytext.append((String) part.getContent());
        } else if (part.isMimeType("text/html") && !conname) {
            bodytext.append((String) part.getContent());
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int count = multipart.getCount();
            for (int i = 0; i < count; i++) {
                getMailContent(multipart.getBodyPart(i));
            }
        } else if (part.isMimeType("message/rfc822")) {
            getMailContent((Part) part.getContent());
        }

    }

    /**
     * 判断邮件是否需要回执，如需回执返回true，否则返回false
     *
     * @return
     * @throws MessagingException
     */
    public boolean getReplySign() throws MessagingException {
        boolean replySign = false;
        String needreply[] = msg.getHeader("Disposition-Notification-TO");
        if (needreply != null) {
            replySign = true;
        }
        return replySign;
    }

    /**
     * 获取此邮件的message-id
     *
     * @return
     * @throws MessagingException
     */
    public String getMessageId() throws MessagingException {
        return msg.getMessageID();
    }

    /**
     * 判断此邮件是否已读，如果未读则返回false，已读返回true
     *
     * @return
     * @throws MessagingException
     */
    public boolean isNew() throws MessagingException {
        boolean isnew = false;
        Flags flags = ((Message) msg).getFlags();
        Flags.Flag[] flag = flags.getSystemFlags();
        System.out.println("flags's length:" + flag.length);
        for (int i = 0; i < flag.length; i++) {
            if (flag[i] == Flags.Flag.SEEN) {
                isnew = true;
                System.out.println("seen message .......");
                break;
            }
        }

        return isnew;
    }

    /**
     * 判断是是否包含附件
     *
     * @param part
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    public static boolean isContainAttch(Part part) throws MessagingException, IOException {
        boolean flag = false;

        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int count = multipart.getCount();
            for (int i = 0; i < count; i++) {
                BodyPart bodypart = multipart.getBodyPart(i);
                String dispostion = bodypart.getDisposition();
                if ((dispostion != null) && (dispostion.equals(Part.ATTACHMENT) || dispostion.equals(Part.INLINE))) {
                    flag = true;
                } else if (bodypart.isMimeType("multipart/*")) {
                    flag = isContainAttch(bodypart);
                } else {
                    String conType = bodypart.getContentType();
                    if (conType.toLowerCase().indexOf("appliaction") != -1) {
                        flag = true;
                    }
                    if (conType.toLowerCase().indexOf("name") != -1) {
                        flag = true;
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            flag = isContainAttch((Part) part.getContent());
        }

        return flag;
    }

    /**
     * 保存附件
     *
     * @param part
     * @throws MessagingException
     * @throws IOException
     */
    public static void saveAttchMent(Part part) throws MessagingException, IOException {
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart mpart = mp.getBodyPart(i);
                String dispostion = mpart.getDisposition();
                String contentType = mpart.getContentType();
                if ((dispostion != null) && (dispostion.equals(Part.ATTACHMENT) || dispostion.equals(Part.INLINE))) {
                    if (contentType.indexOf("application") != -1) {
                        //saveFile(decodeText(mpart.getFileName()), mpart.getInputStream());
                    }
                } else if (mpart.isMimeType("multipart/*")) {
                    saveAttchMent(mpart);
                } else {
                    //name:图片
                    if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {
                        //saveFile(decodeText(mpart.getFileName()), mpart.getInputStream());
                    }
                }
            }

        } else if (part.isMimeType("message/rfc822")) {
            saveAttchMent((Part) part.getContent());
        }
    }

    /**
     * 获取附件
     *
     * @param part
     * @throws MessagingException
     * @throws IOException
     */
    public static InputStream getFile(Part part) throws Exception {
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart mpart = mp.getBodyPart(i);
                String dispostion = mpart.getDisposition();
                String contentType = mpart.getContentType();
                if ((dispostion != null) && (dispostion.equals(Part.ATTACHMENT) || dispostion.equals(Part.INLINE))) {
                    if (contentType.indexOf("application") != -1) {
                        return mpart.getInputStream();
                    }
                } else if (mpart.isMimeType("multipart/*")) {
                    saveAttchMent(mpart);
                } else {
                    //name:图片
                    if (contentType.indexOf("application") != -1) {
                        return mpart.getInputStream();
                    }
                }
            }

        } else if (part.isMimeType("message/rfc822")) {
            saveAttchMent((Part) part.getContent());
        }
        return null;
    }

    /**
     * 文本解码
     *
     * @param encodeText 解码MimeUtility.encodeText(String text)方法编码后的文本
     * @return 解码后的文本
     * @throws UnsupportedEncodingException
     */
    public static String decodeText(String encodeText) throws UnsupportedEncodingException {
        if (encodeText == null || "".equals(encodeText)) {
            return "";
        } else {
            return MimeUtility.decodeText(encodeText);
        }
    }

    /**
     * 设置日期格式
     *
     * @param dateformate
     */
    public void setDateformate(String dateformate) {
        this.dateformate = dateformate;
    }

//    /**
//     * 保存文件内容
//     *
//     * @param filename
//     * @param inputStream
//     * @throws IOException
//     */
//    private static void saveFile(String filename, InputStream inputStream) throws IOException {
//        if (filename.toLowerCase().endsWith(".rar")) {
//            UnRar unRar = new UnRar();
//            try {
//                unRar.unRarFile(inputStream, "");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            String osname = System.getProperty("os.name");
//            String storedir = saveAttchPath;
//            String sepatror = "";
//            if (osname == null) {
//                osname = "";
//            }
//
//            if (osname.toLowerCase().indexOf("win") != -1) {
//                sepatror = "//";
//                if (storedir == null || "".equals(storedir)) {
//                    storedir = "d://tmp";
//                }
//            } else {
//                sepatror = "/";
//                storedir = "/tmp";
//            }
//
//            File storefile = new File(storedir + sepatror + filename);
//            System.out.println("storefile's path:" + storefile.toString());
//
//            BufferedOutputStream bos = null;
//            BufferedInputStream bis = null;
//
//            try {
//                bos = new BufferedOutputStream(new FileOutputStream(storefile));
//                bis = new BufferedInputStream(inputStream);
//                int c;
//                while ((c = bis.read()) != -1) {
//                    bos.write(c);
//                    bos.flush();
//                }
//            } catch (FileNotFoundException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } finally {
//                bos.close();
//                bis.close();
//            }
//        }
//
//    }

    public void recive(Part part, int i) throws MessagingException, IOException {
        System.out.println("------------------START-----------------------");
        System.out.println("Message" + i + " subject:" + getSubject());
        System.out.println("Message" + i + " isNew:" + isNew());
        boolean flag = isContainAttch(part);
        System.out.println("Message" + i + " isContainAttch:" + flag);
        System.out.println("Message" + i + " replySign:" + getReplySign());
        getMailContent(part);
        System.out.println("Message" + i + " content:" + getBodyText());
        if (flag) {
            saveAttchMent(part);
        }
        System.out.println("------------------END-----------------------");
    }

    public static void main(String[] args) throws GeneralSecurityException, IOException {
        remail();
    }

    public static void remail() throws GeneralSecurityException {
        String protocol = "imap";
        boolean isSSL = true;
        String host = "mail.e-nci.com";
        int port = 993;
        String username = "username";
        String password = "password";
        //MailSSLSocketFactory sf = new MailSSLSocketFactory();
        //sf.setTrustAllHosts(true);
        Properties props = new Properties();        props.put("mail.imap.host", host);
        props.put("mail.imap.port", port);
        props.put("mail.imap.partialfetch", false);
        //不用配置ssl也可
        props.put("mail.imap.ssl.enable", isSSL);
        props.put("mail.imap.ssl.trust", "*");
        //props.put("mail.imap.ssl.socketFactory", sf);

        Session session = Session.getDefaultInstance(props);
        Store store = null;
        Folder folder = null;
        try {
            store = session.getStore(protocol);
            store.connect(username, password);
            folder = store.getFolder("INBOX"); //收件箱
            // 使用只读方式打开收件箱
            folder.open(Folder.READ_WRITE);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SearchTerm st = new AndTerm(new SentDateTerm(3, sdf.parse("2020-07-09")),
                    new SubjectTerm(""));

            Message[] messages = folder.search(st);
            for (Message message : messages) {
                System.out.println("Subject: " + message.getSubject());
                System.out.println("Content: " + message.getContentType());
                System.out.println("Content: " + message.getContent());
                System.out.println("getFrom: " + message.getFrom()[0].toString());
                System.out.println("getAllRecipients: " + message.getAllRecipients());
                System.out.println("address: " + message.getReplyTo()[0].toString());
                Date date = message.getSentDate();
                System.out.println("Date: " + date);
                //如果有附件
                if(isContainAttch(message)){
                   saveAttchMent(message);
                }
                if (message.getSubject().contains("Tigerair")) {   
                //如果是这是名字叫"Tigerair"],就进行获取
                }
            }

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (folder != null) {
                    //Folder.close()方法的boolean 类型参数表示是否在删除操作邮件后更新Folder。
                    folder.close(false);
                }
                if (store != null) {
                    store.close();
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

}

