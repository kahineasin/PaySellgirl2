package com.sellgirl.sgJavaHelper;

//import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

import com.sellgirl.sgJavaHelper.task.IPFTask; 

/// <summary>
/// 
/// </summary>
/**
 * 监听邮件的任务
 * 取代PFListenEmailTask(如果每个PFListenEmailTask都自己收邮件监听的话，那很浪费资源，应该用PFNewEmailListener一次性监听新邮件)
 * @author Administrator
 *
 */
public class PFListenNewEmailTask implements IPFTask//, IDisposable
{
    public String HashId ;
    /// <summary>
    /// 条件满足时执行的代码,如果有返回结果,会通过邮件回复
    /// </summary>
    public Consumer<PFEmail> DoConsumer ;
    //public Function<Message, Object> DoConsumer ;
    /// <summary>
    /// 可以执行的条件
    /// </summary>
    public Function<PFEmail, Boolean> SubjectMatch ;
    //public Function<Message, PFListenEmailTask, Boolean> SubjectMatch ;
    public Thread TaskThread ;
    public Boolean _running = false;

    /// <summary>
    /// 检测的过程可能非常耗时,所以允许自定义
    /// </summary>
    public int CheckMessageInterval(){
    	return 5000;
    	//return PFDataHelper.ObjectToInt(ConfigurationManager.AppSettings["CheckMessageInterval_" + HashId]) ?? PFTaskHelper.CheckMessageInterval; 
    	}
    public int EmailConnectFailWaitInterval() 
    {
    	return (1000 * 60 * 5);
    	//return PFDataHelper.ObjectToInt(ConfigurationManager.AppSettings["EmailConnectFailWaitInterval"]) ?? (1000 * 60 * 5); 
    } 

    //private PFDate _lastListenTime;
    //private PFEmailManager _emailManager;
    private PFEmailListener _emailManager;

    /// <summary>
    /// 由于邮件id大的,邮件的时间不一定是大(测试email时发现的),所以保存一天的已读邮件,和_lastListenTime结合来判断邮件是否已经读过
    /// </summary>
    //private ArrayList<PFEmail> _emailAtToday=new ArrayList<PFEmail>();
    //private PFDate _initTime;

    /// <summary>
    /// 只监听一次,常用于emailMq的生产者监听回复等情况
    /// </summary>
    private Boolean _onlyListenOnce = false;
    public PFListenNewEmailTask(String hashId,
//    		PFEmailManager emailManager,
    		PFEmailListener emailManager,
         Consumer<PFEmail> doConsumer,
         //Function<Message, Object> doConsumer,
         Function<PFEmail, Boolean> subjectMatch,
        // Function<Message, PFListenEmailTask, Boolean> subjectMatch
        Boolean onlyListenOnce//=false
        )
    {
        HashId = hashId;
        DoConsumer = doConsumer;
        SubjectMatch = subjectMatch;

        //_initTime=_lastListenTime = PFDate.Now();
        _emailManager = emailManager;

        _onlyListenOnce = onlyListenOnce;
        //一开始就读邮件的话,浪费很多性能的,还是不要了
        //ReadEmailAtYesterday();
    }

    public void Start()
    {
        if (!_running)
        {
            _running = true;
//            TaskThread =new Thread() {//线程操作
//	               public void run() {
//	            	   StartThread();
//	               }
//	        };	        
//            TaskThread.start();
            
            _emailManager.OnReceiveNew(HashId, pfEmail->{        
            	if(SubjectMatch.apply(pfEmail)) {
            		DoConsumer.accept(pfEmail);
            	}
            },
            		_onlyListenOnce);
        }
    }
    public void Stop()
    {
        if (_running)
        {
            _running = false;
//            try {
//                TaskThread.interrupt();//.destroy();//之前先释放MessageService的话,进程里仍在使用MessageService会报错,现在改为先释放Thread试试
//            }catch(Exception e) {
//            	PFDataHelper.WriteError(e);
//            }

            _emailManager.UnOnReceiveNew(HashId);
        }    
    }
	@Override
	public Boolean IsRunning() {
		return _running;
	}

//    public void Dispose()
//    {
//        if (_running)
//        {
//            try
//            {
//                _running = false;
//                TaskThread.Abort();
//            }
//            catch (Exception) { }
//        }
//    }
    
//    /// <summary>
//    /// 自然停止.让线程执行完一个循环再停止(防止Abort报错)
//    /// </summary>
//    public void NaturalStop()
//    {
//        _running = false;
//    }
    //#region 新方式,先读新邮件
    //public void StartThread(Object ps)
    //{
    //    //Message email = null;
    //    while (_running == true)
    //    {
    //        try
    //        {
    //            //try
    //            //{
    //            //    //为了检查26服务器上监听邮件失效的问题--benjamin 
    //            //    PFDataHelper.WriteLocalTxt(PFDataHelper.FormatString("监听邮件的while进行中，时间:{0}", PFDate.Now().ToString()), "PFListenEmailTask_while.txt");
    //            //}
    //            //catch (Exception) { }

    //            //if (PFDataHelper.IsDebug)
    //            //{
    //            //    PFDataHelper.WriteLog("1");
    //            //}
    //            _emailManager.Connect_Click();
    //            if (!_emailManager.IsConnect())//Connect_Click的new TcpClient(_hostName, 110) 是有可能连接失败的--benjamin20200318
    //            {
    //                Thread.Sleep(1000 * 60 * 5);//如果连接失败,等5分钟
    //                continue;
    //            }

    //            try
    //            {
    //                //为了检查26服务器上监听邮件失效的问题--benjamin todo
    //                PFDataHelper.WriteLocalTxt(PFDataHelper.FormatString("监听邮件的while的_emailManager.Connect_Click()执行成功，时间:{0}，邮件数：{1}", PFDate.Now().ToString(), _emailManager.MessageCount), "PFListenEmailTask_while_Connect_Click.txt");
    //            }
    //            catch (Exception) { }

    //            PFDate? newestMailTime = null;//记录最新邮件的时间

    //            ArrayList<Message> newEmailList = new ArrayList<Message>();
    //            for (int i = _emailManager.MessageCount; i > 0; i--)
    //            {
    //                var email = _emailManager.Retrieve_Click(i);
    //                if (email.Date <= _lastListenTime)//旧邮件
    //                {
    //                    break;
    //                }
    //                if (email==null) {
    //                    newEmailList.ForEach(a => a.Dispose());
    //                    newEmailList.Clear();
    //                    throw new Exception("读取新邮件时出错");//只要一个新邮件读不到,就重新读,这样比较保险,也不会重复消费
    //                }
    //                newEmailList.Add(email);
    //            }

    //            if (newEmailList.Any())
    //            {
    //                continue;//没有新邮件
    //            }

    //            foreach (var email in newEmailList)
    //            {
    //                try
    //                {
    //                    //为了检查26服务器上监听邮件失效的问题--benjamin todo
    //                    PFDataHelper.WriteLocalTxt(PFDataHelper.FormatString("监听邮件的while的_emailManager.Retrieve_Click()执行成功，时间:{0}，邮件数：{1}", PFDate.Now().ToString(), _emailManager.MessageCount), "PFListenEmailTask_while_Retrieve_Click.txt");
    //                }
    //                catch (Exception) { }


    //                var b = false;
    //                try
    //                {
    //                    b = SubjectMatch(email);
    //                }
    //                catch (Exception e)
    //                {
    //                    newestMailTime = null;
    //                    PFDataHelper.WriteError(new Exception(PFDataHelper.FormatString("SubjectMatch报错{0}", e)));
    //                }
    //                if (b)
    //                //if (SubjectMatch(email,this))
    //                {
    //                    //if (PFDataHelper.IsDebug)
    //                    //{
    //                    //    PFDataHelper.WriteLog("8");
    //                    //}
    //                    try
    //                    {
    //                        _emailManager.Disconnect_Click();

    //                        DoConsumer(email);
    //                        //var result=DoConsumer(email);
    //                        //if (result != null)//回复邮件
    //                        //{
    //                        //    //PFDataHelper.SendEmail(PFDataHelper.SysEmailUserName, PFDataHelper.SysEmailPwd, PFDataHelper.SysEmailHostName,
    //                        //    //    new String[] { email.From }, "PFListenEmailTask_AutoReply_" + email.Subject, JsonConvert.SerializeObject(result));
    //                        //    PFDataHelper.SendEmail(PFDataHelper.SysEmailUserName, PFDataHelper.SysEmailPwd, PFDataHelper.SysEmailHostName,
    //                        //        new String[] { email.From }, HashId+"_Reply_" + email.Subject, JsonConvert.SerializeObject(result));
    //                        //}

    //                        //if (PFDataHelper.IsDebug)
    //                        //{
    //                        //    PFDataHelper.WriteLog("9");
    //                        //}
    //                        //PFDataHelper.WriteLog(PFDataHelper.FormatString("任务{0}执行完毕,月份为:{1}", HashId, cmonth));
    //                    }
    //                    catch (Exception e)
    //                    {
    //                        PFDataHelper.WriteError(e);
    //                    }
    //                    email.Dispose();
    //                    ////_emailManager.DeleteEmail(email);
    //                    //_lastListenTime = email.Date ?? PFDate.Now();
    //                    break;
    //                }
    //                //if (PFDataHelper.IsDebug)
    //                //{
    //                //    PFDataHelper.WriteLog("10");
    //                //}
    //                email.Dispose();
    //            }
    //            //if (PFDataHelper.IsDebug)
    //            //{
    //            //    PFDataHelper.WriteLog("11");
    //            //}
    //            //不管有没有匹配到邮件,下次都应该找时间更新的邮件了
    //            if (newestMailTime != null && newestMailTime > _lastListenTime) { _lastListenTime = newestMailTime.Value; }

    //            //if (PFDataHelper.IsDebug)
    //            //{
    //            //    PFDataHelper.WriteLog("12");
    //            //}
    //            _emailManager.Disconnect_Click();
    //            //if (PFDataHelper.IsDebug)
    //            //{
    //            //    PFDataHelper.WriteLog("13");
    //            //}
    //            Thread.Sleep(CheckMessageInterval);
    //            //if (PFDataHelper.IsDebug)
    //            //{
    //            //    PFDataHelper.WriteLog("14");
    //            //}
    //            GC.Collect();//一定要有句，否则SendMobileMessage里面的所有List会使内存越来越高  

    //            //if (PFDataHelper.IsDebug)
    //            //{
    //            //    PFDataHelper.WriteLog("15");
    //            //}
    //        }
    //        catch (Exception e)
    //        {
    //            PFDataHelper.WriteError(e);
    //            Thread.Sleep(CheckMessageInterval);
    //        }
    //    }
    //} 
    //#endregion


//    private void RemoveEmailAtYesterday()
//    {
//    	_emailAtToday.removeIf(a->{
//    		try {
//				return a.getSentDate() == null || a.getSentDate().compareTo(_lastListenTime.AddDays(-2))<0;
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//    		return true;
//    	});
//        //_emailAtToday.RemoveAll(a => a.Date == null || a.Date < _lastListenTime.AddDays(-2));//保存2天的邮件比较保险
//    }
    ///// <summary>
    ///// 运行时要读昨天的邮件(之后不匹配)
    ///// </summary>
    //private void ReadEmailAtYesterday()
    //{
    //    _emailManager.Connect_Click();
    //    for (int i = _emailManager.MessageCount; i > 0; i--)
    //    {
    //        var email = _emailManager.Retrieve_Click(i);
    //        if (email != null && email.Date != null )
    //        {
    //            if(email.Date >= _lastListenTime.AddDays(-1))
    //            {
    //                _emailAtToday.Add(email);
    //            }else
    //            {
    //                break;
    //            }
    //        }else
    //        {
    //            throw new Exception("监听邮件失败,ReadEmailAtYesterday报错");
    //        }
    //    }
    //    _emailManager.Disconnect_Click();
    //}
    //#region 存在漏洞,有时MessageCount比较大时,邮件时间反而小--benjamin20200413        
//    public void StartThread()//Object ps)
//    {
//        Message email = null;
//        int step=-1;//只是为了检查报错时的位置(正常情况下是不用的,所以要检查为什么有时没报错误堆)
//        while (_running == true)
//        {
//            try
//            {
//                //try
//                //{
//                //    //为了检查26服务器上监听邮件失效的问题--benjamin 
//                //    PFDataHelper.WriteLocalTxt(PFDataHelper.FormatString("监听邮件的while进行中，时间:{0}", PFDate.Now().ToString()), "PFListenEmailTask_while.txt");
//                //}
//                //catch (Exception) { }
//
//                //if (PFDataHelper.IsDebug)
//                //{
//                //    PFDataHelper.WriteLog("1");
//                //}
//            	step=338;
//                _emailManager.Connect_Click();
//                if (!_emailManager.IsConnect())//Connect_Click的new TcpClient(_hostName, 110) 是有可能连接失败的--benjamin20200318
//                {
//                    Thread.sleep(PFDataHelper.CheckMessageInterval());
//                    //Thread.Sleep(1000 * 60 * 5);//如果连接失败,等5分钟
//                    continue;
//                }
//
//                try
//                {
//                    //为了检查26服务器上监听邮件失效的问题--benjamin todo
//                    PFDataHelper.WriteLocalTxt(PFDataHelper.FormatString("监听邮件的while的_emailManager.Connect_Click()执行成功，时间:{0}，邮件数：{1}", PFDate.Now().toString(), _emailManager.MessageCount), "PFListenEmailTask_while_Connect_Click.txt",LocalDataType.Deletable);
//                }
//                catch (Exception e) { }
//
//            	step=354;
//                PFDate newestMailTime = null;//记录最新邮件的时间
//                                                //Boolean hasMatch = false;
//                                                //PFDataHelper.WriteLog("MessageCount:" + _emailManager.MessageCount);
//                                                //if (PFDataHelper.IsDebug)
//                                                //{
//                                                //    PFDataHelper.WriteLog("2");
//                                                //    //PFDataHelper.WriteLocalTxt(PFDataHelper.FormatString("时间{0}\r\nMessageCount:{1}",PFDate.Now(), _emailManager.MessageCount), "listenEmail_Connect_Click.txt");
//                                                //}
//                Boolean isAnyReceiveFail = false;
//                for (int i = _emailManager.MessageCount; i > 0; i--)
//                {
//                	step=366;
//
//                    //if (PFDataHelper.IsDebug)
//                    //{
//                    //    PFDataHelper.WriteLog("3");
//                    //}
//                    email = _emailManager.Retrieve_Click(i);//System.OutOfMemoryException: Exception of type 'System.OutOfMemoryException' was thrown. --benjamin 
//                    //SGRef<Message> emailRef=new SGRef<Message>(null); 
//                    SGRef<PFEmail> emailRef=new SGRef<PFEmail>(null); 
//                    try
//                    {
//                        //为了检查26服务器上监听邮件失效的问题--benjamin todo
//                        PFDataHelper.WriteLocalTxt(PFDataHelper.FormatString("监听邮件的while的_emailManager.Retrieve_Click()执行成功，时间:{0}，邮件数：{1}", PFDate.Now().toString(), _emailManager.MessageCount), "PFListenEmailTask_while_Retrieve_Click.txt",LocalDataType.Deletable);
//                    }
//                    catch (Exception e) { 
//                    	
//                    }
//                	step=383;
//
//                    //if (PFDataHelper.IsDebug)
//                    //{
//                    //    PFDataHelper.WriteLog("4");
//                    //}
//                    PFEmail pfEmail=null;
//                    if (email == null)
//                    {
//                        //为了解决这个漏洞:
//                        //当第一个邮件能收到,那newestMailTime变成它的时间,但后面的都接收失败了,那么下次就会永远读不到那个失败的邮件
//                        newestMailTime = null;
//                        isAnyReceiveFail = true;
//                        continue;
//                    }//这里不用break是防止id有中间非连续的情况--benjamin20190929
//                    else {
//                        pfEmail=new PFEmail(email);
//                    	//emailRef.SetValue(email);
//                    	emailRef.SetValue(pfEmail);
//                    }
//                    
//                    PFDate emailDate=email.getSentDate()==null?null:new PFDate(email.getSentDate());
//                    if (i == _emailManager.MessageCount)
//                    {
//                        newestMailTime = emailDate;
//                    }
//                	step=409;
//
//                    //if (email.Date <= _lastListenTime)
//                    //if (email.Date != null && _lastListenTime != null &&
//                    //    email.Date.Value.AddMinutes(2) < _lastListenTime) //这里用等号似乎会有问题,有时生产者发的邮件时间反而大于消费者回复的邮件的时间(可能邮件上的时间是根据发送端的电脑时间来的,有误差),可能有更好的方法可以统一时间?--benjamin todo
//                    //为解决邮件id大但邮件时间反而小的问题
////                    if(HashId.indexOf("PFEmailMqConsumerResponseListener_")>-1&&i == _emailManager.MessageCount) {
////                    	String aa="test";
////                    }
//                    if(emailDate != null)
//                    {
//                        if (emailDate.compareTo(_lastListenTime.AddDays(-1)) <=0 ) //昨天的邮件直接不读
//                        {
//                            //email.Dispose();
//                            email=null;
//                            break;
//                        }
//                        else if (emailDate.compareTo(_lastListenTime)<=0 )//今天的邮件
//                        {
//                        	
//                            if(PFDataHelper.ListAny(_emailAtToday, a->a.equals(emailRef.GetValue())))//今天的邮件要比对
//                            {
//                                //email.Dispose();
//                                email=null;
//                                //continue;
//                                break;
//                            }
//                            else
//                            {
//                                _emailAtToday.add(pfEmail);
//                                if (emailDate.compareTo(_initTime.AddMinutes(-1))<0 )
//                                {
//                                    //email.Dispose();
//                                    email=null;
//                                    break;
//                                }
//                            }
//                        }else//新邮件(没有这句的话,新邮件会读两次--benjamin20200414
//                        {
//                            if(!PFDataHelper.ListAny(_emailAtToday,a -> a.equals(emailRef.GetValue())))
//                            {
//                                _emailAtToday.add(pfEmail);
//                            }
//                        }
//                    }else
//                    {
//                        isAnyReceiveFail = true;
//                    }
//                	step=457;
//                    //if (PFDataHelper.IsDebug)
//                    //{
//                    //    PFDataHelper.WriteLog("7");
//                    //}
//
//                	String log=PFDataHelper.FormatString(
//                			"任务{0}监听到新邮件{1}",HashId, pfEmail.getSubject());
//                	System.out.println(log);
//              	   System.out.println("");
//              	   
//                    Boolean b = false;
//                    try
//                    {
//                        //b = SubjectMatch.apply(email);
//                        b = SubjectMatch.apply(pfEmail);
//                    }
//                    catch (Exception e)
//                    {
//                        newestMailTime = null;
//                        PFDataHelper.WriteError(new Exception(PFDataHelper.FormatString("SubjectMatch报错{0}", e)));
//                    }
//                    if (b)
//                    {
//                        try
//                        {
//                            _emailManager.Disconnect_Click();
//
//                            //DoConsumer.accept(email);
//                            DoConsumer.accept(pfEmail);
//                          
//                        }
//                        catch (Exception e)
//                        {
//                            //PFDataHelper.WriteError(e);
//                            //PFDataHelper.WriteError(new Exception("PFListenEmailTask 485行",e));
//                            PFDataHelper.WriteError(new Throwable(),e);
//                        }
//                        //email.Dispose();
//                        email=null;
//                        ////_emailManager.DeleteEmail(email);
//                        //_lastListenTime = email.Date ?? PFDate.Now();
//                        if (_onlyListenOnce)
//                        {
//                            _running = false;
//                            return;
//                        }
//                        break;
//                    }
//
//                    //email.Dispose();
//                    email=null;
//                }
//                //if (PFDataHelper.IsDebug)
//                //{
//                //    PFDataHelper.WriteLog("11");
//                //}
//                //不管有没有匹配到邮件,下次都应该找时间更新的邮件了
//                if (newestMailTime != null && newestMailTime.compareTo(_lastListenTime) > 0&&(!isAnyReceiveFail)) {
//                    _lastListenTime = newestMailTime;
//                }
//
//            	step=518;
//                //if (PFDataHelper.IsDebug)
//                //{
//                //    PFDataHelper.WriteLog("12");
//                //}
//                _emailManager.Disconnect_Click();
//                //if (PFDataHelper.IsDebug)
//                //{
//                //    PFDataHelper.WriteLog("13");
//                //}
//                Thread.sleep(PFDataHelper.CheckMessageInterval());
//                //if (PFDataHelper.IsDebug)
//                //{
//                //    PFDataHelper.WriteLog("14");
//                //}
//                
//                //GC.Collect();//一定要有句，否则SendMobileMessage里面的所有List会使内存越来越高  
//
//                //if (PFDataHelper.IsDebug)
//                //{
//                //    PFDataHelper.WriteLog("15");
//                //}
//
//                RemoveEmailAtYesterday();
//            	step=542;
//            }
//            catch (Exception e)
//            {
////                PFDataHelper.WriteError(e);
////                PFDataHelper.WriteError(new Exception(
////                	PFDataHelper.FormatString("PFListenEmailTask 537行 step:{0}\r\n",String.valueOf(step)),e) );
//                PFDataHelper.WriteError(new Throwable(),new Exception(
//                    	PFDataHelper.FormatString("PFListenEmailTask 537行 step:{0}\r\n error:{1}",String.valueOf(step),e))  );
//                try {
//					Thread.sleep(PFDataHelper.CheckMessageInterval());
//				} catch (InterruptedException e1) {
//					e1.printStackTrace();
//				}
//            }
//        }
//    }
    //#endregion
}
