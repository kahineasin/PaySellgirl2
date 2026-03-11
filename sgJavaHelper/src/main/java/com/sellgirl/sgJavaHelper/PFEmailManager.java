package com.sellgirl.sgJavaHelper;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class PFEmailManager {
	private Properties props;
	private Authenticator authenticator;
	private String protocol;
	private Store store;
	private Folder folder;
	private Boolean _isConnect;
	public int MessageCount;
    //public String _hostName ;
    public String _userName ;
    public String _pwd ;
    public PFEmailManager(Properties props,
    		//String hostName, 
    		String userName, String pwd)
    {
        //_hostName = hostName;
        _userName = userName;
        _pwd = pwd;
        this.props=props;
		//AuthenticatorGenerator.getAuthenticator("wxj@perfect99.com", "shi3KjkE48QZ3SPA")
        authenticator=AuthenticatorGenerator.getAuthenticator(userName, pwd);
    }
	public  Message Connect_Click()//Properties props, Authenticator authenticator, String protocol,int msgnum) { 
	{
//		Message[] messages = null;
		Message message = null;
		Session session = Session.getDefaultInstance(props, authenticator);
		// session.setDebug(true);
		//Store store = null;
		//Folder folder = null;
		try {
			store = protocol == null || protocol.trim().length() == 0 ? session.getStore() : session.getStore(protocol);
			store.connect();
			folder = store.getFolder("INBOX");// 获取收件箱
			folder.open(Folder.READ_ONLY); // 以只读方式打开
			_isConnect=true;
			MessageCount=folder.getMessageCount();
			//message = folder.getMessage(msgnum);
		} catch (NoSuchProviderException e) {
			_isConnect=false;
			e.printStackTrace();
		} catch (MessagingException e) {
			_isConnect=false;
			e.printStackTrace();
		}
		return message;
	}

    public Boolean IsConnect()
    {
        return _isConnect;
    }
	public  Message Retrieve_Click(//Properties props, Authenticator authenticator, String protocol,
			int msgnum) {
		try {
			return  folder.getMessage(msgnum);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    public void Disconnect_Click()
    {
        if (_isConnect)
        {
            _isConnect = false;
            try
            {
            	folder.close(true);
            	store.close();
            }
            catch (Exception err)
            {
                //CloseStream();
                //PFDataHelper.WriteError(new Throwable(),err);
                SGDataHelper.WriteError(err);
            }
        }
    }
}
