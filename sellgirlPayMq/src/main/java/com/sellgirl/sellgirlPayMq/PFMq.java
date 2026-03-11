package com.sellgirl.sellgirlPayMq;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.PropertyValueConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.sellgirl.sellgirlPayMq.PFMqHelper.PFConsumerResponseTask;
import com.sellgirl.sellgirlPayMq.PFMqHelper.PFConsumerTask;

import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.PFEmail;
import com.sellgirl.sgJavaHelper.PFEmailListener;
import com.sellgirl.sgJavaHelper.PFEmailManager;
import com.sellgirl.sgJavaHelper.SGEmailSend;
import com.sellgirl.sgJavaHelper.PFListenNewEmailTask;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class PFMq {
    private  PFMqConfig _mqConfig;
	public PFMq(PFMqConfig mqConfig) {
		_mqConfig=mqConfig;
	}
	

	public  void BuildRabbitMqConsumer(PFConsumerTask pfDeliverCallback) {

		try {
	    	//rabbitmq
		    PFMqConfig mqConfig=pfDeliverCallback.GetMqConfig(_mqConfig);
		    
	    	com.rabbitmq.client.ConnectionFactory factory = new com.rabbitmq.client.ConnectionFactory();
		    factory.setHost(mqConfig.getHost());
		    Connection connection;
			connection = factory.newConnection();
		    Channel channel = connection.createChannel();
		    String QUEUE_NAME=mqConfig.getQueueName();
		    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		    System.out.println("\r\n  [*][rabbitMq] queueName:"+QUEUE_NAME+"\r\n    Waiting for messages.\r\n");
	
		    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
		    	PFMqMessage pfMessage=new PFMqMessage(delivery);
		        String logMsg="\r\n  [x][rabbitMq] queueName:"+QUEUE_NAME+"\r\n    "+pfDeliverCallback.getClass().getSimpleName()+" Received '" + pfMessage.getMessage() + "' \r\n";
		        WriteLog(logMsg);
		    	pfDeliverCallback.handle(consumerTag,pfMessage);
		    };
		    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public  void BuildRocketMqConsumer(PFConsumerTask pfDeliverCallback) {

        try {
		    PFMqConfig mqConfig=pfDeliverCallback.GetMqConfig(_mqConfig);   
			//org.apache.rocketmq.client.consumer.DefaultMQPushConsumer consumer = new org.apache.rocketmq.client.consumer.DefaultMQPushConsumer("test-group");
	//        consumer.setNamesrvAddr("localhost:9876");
	//        consumer.setInstanceName("rmq-instance");
	//        consumer.subscribe("log-topic", "user-tag");
			org.apache.rocketmq.client.consumer.DefaultMQPushConsumer consumer = new org.apache.rocketmq.client.consumer.DefaultMQPushConsumer(mqConfig.getGroupId());
	        consumer.setNamesrvAddr(mqConfig.getNameSrvAddr());
	        
	        if(!SGDataHelper.StringIsNullOrWhiteSpace(_mqConfig.getInstanceName())) {
	        	consumer.setInstanceName(_mqConfig.getInstanceName());
	        }

	        consumer.subscribe(mqConfig.getTopic(),mqConfig.getTag());
	        
	        consumer.registerMessageListener(new org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently() {
				@Override
				public org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
	
		          for (MessageExt msg : msgs) {
				    	PFMqMessage pfMessage=new PFMqMessage(msg);
				        String logMsg="\r\n [x][rocketMq] topic:"+mqConfig.getTopic()+" tag:"+mqConfig.getTag()+"\r\n    "+pfDeliverCallback.getClass().getSimpleName()+" Received '" + pfMessage.getMessage() + "' \r\n ";
				        WriteLog(logMsg);				        
						pfDeliverCallback.handle( mqConfig.getTag(),pfMessage);
		          }
		          return org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
	        });
//	        consumer.registerMessageListener(new org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly() {
//
//				@Override
//				public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
//			          for (MessageExt msg : msgs) {
//			              System.out.println("消费者消费数据:"+new String(msg.getBody()));
//			          }
//			          return org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus.SUCCESS;
//				}
//	        });
			consumer.start();
			System.out.println("\r\n [*][rocketMq] topic:"+mqConfig.getTopic()+" tag:"+mqConfig.getTag()+"\r\n    Waiting for messages.\r\n");
		} catch (MQClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public  void BuildAliMqConsumer(PFConsumerTask pfDeliverCallback) {
		//参考:D:\eclipse_workspace\IpaasTest\src\com\mq\simple\ConsumerTest.java
        Properties properties = GetAliMqProperties();        

	    PFMqConfig mqConfig=pfDeliverCallback.GetMqConfig(_mqConfig);        
        
        Consumer consumer = ONSFactory.createConsumer(properties);
        
		consumer.subscribe(mqConfig.getTopic(), mqConfig.getTag(), new MessageListener() { 
		    public Action consume(Message message, ConsumeContext context) {
		    	PFMqMessage pfMessage=new PFMqMessage(message);
		        String logMsg="\r\n [x][aliMq] topic:"+mqConfig.getTopic()+" tag:"+mqConfig.getTag()+"\r\n    "+pfDeliverCallback.getClass().getSimpleName()+" Received '" + pfMessage.getMessage() + "' \r\n ";
		        WriteLog(logMsg);
				pfDeliverCallback.handle( mqConfig.getTag(),pfMessage);
		    	return Action.CommitMessage;
		    }
		});
        consumer.start();
	    System.out.println("\r\n [*][aliMq] topic:"+mqConfig.getTopic()+" tag:"+mqConfig.getTag()+"\r\n    Waiting for messages.\r\n");
	}

    public void BuildPFEmailMqConsumer(PFConsumerTask pfDeliverCallback)
    {
        PFMqConfig mqConfig = pfDeliverCallback.GetMqConfig(_mqConfig);
        //String producerEmailTitle = "PFEmailMq_producer_" + "会员资料表";
        //String producerEmailTitle = "PFEmailMq_producer_" + "hyzl";
        //消费方(使用系统邮箱)
        //String result = "";
        //bool success = false;
    	PFEmailListener emailListener=PFEmailListener.init(new PFEmailManager(SGEmailSend.EMAIL_OWNER_ADDR_HOST_PROPERTY, 
        		SGEmailSend.EMAIL_OWNER_ADDR, SGEmailSend.EMAIL_OWNER_ADDR_PASS));
    	PFListenNewEmailTask consumerTask = new PFListenNewEmailTask(
        		"PFEmailMqConsumerListener_" + mqConfig.getTopic(),
        		emailListener,
        email ->
        {
            //result = "{success:true}";
            PFMqMessage pfMessage = new PFMqMessage(email);
            pfDeliverCallback.handle(mqConfig.getTag(), pfMessage);
        },
        (email//, task
        ) ->
        {
            //消费方监听生产方邮件
     	   String l=("PFEmailMq_product_" + mqConfig.getTopic());
     	   String r=email.getSubject();
     	   System.out.println("l:");
     	   System.out.println(l);
     	   System.out.println("r:");
     	   System.out.println(r);
     	   System.out.println("");
     	   return l.equals(r);
            ////return email.Subject != null && email.Subject.IndexOf("TestForceUpdateHyzl_") == 0;//这里不要用>-1,否则可能把自动回复的邮件也当作是了
            //return ("PFEmailMq_product_" + mqConfig.getTopic()).equals(email.getSubject());
        },false);
        consumerTask.Start();

    }
    public void BuildPFEmailMqConsumer(PFConsumerResponseTask pfDeliverCallback)
    {
        PFMqConfig mqConfig = pfDeliverCallback.GetMqConfig(_mqConfig);
        //string producerEmailTitle = "PFEmailMq_producer_" + "会员资料表";//中文有问题--benjamin todo
        //string producerEmailTitle = "PFEmailMq_producer_" + "hyzl";
        //消费方(使用系统邮箱)
        //String result = "";
        //bool success = false;
    	PFEmailListener emailListener=PFEmailListener.init(new PFEmailManager(SGEmailSend.EMAIL_OWNER_ADDR_HOST_PROPERTY, 
        		SGEmailSend.EMAIL_OWNER_ADDR, SGEmailSend.EMAIL_OWNER_ADDR_PASS));
    	PFListenNewEmailTask consumerTask = new PFListenNewEmailTask("PFEmailMqConsumerResponseListener_" + mqConfig.getTopic(),
    			emailListener,
        email ->
        {
            //result = "{success:true}";
            PFMqMessage pfMessage = new PFMqMessage(email);
            Object r = pfDeliverCallback.handle(mqConfig.getTag(), pfMessage);
            if (r != null)
            {
                ////消费方回复邮件(暂不回复--benjamin)
                SGEmailSend.SendMail(
                    new String[] { SGEmailSend.EMAIL_OWNER_ADDR }, 
                    "PFEmailMq_consumer_Response_" + mqConfig.getTopic() + email.getBody(),
                    JSON.toJSONString(r));
            }
        },
        (email//, task
        ) ->
        {
            //消费方监听生产方邮件
     	   String l=("PFEmailMq_product_" + mqConfig.getTopic());
     	   String r=email.getSubject();
//     	   System.out.println("l:");
//     	   System.out.println(l);
//     	   System.out.println("r:");
//     	   System.out.println(r);
//     	   System.out.println("");
     	   return l.equals(r);
            ////return email.Subject != null && email.Subject.IndexOf("TestForceUpdateHyzl_") == 0;//这里不要用>-1,否则可能把自动回复的邮件也当作是了
            //return   ("PFEmailMq_product_" + mqConfig.getTopic()).equals(email.getSubject());
        },false);
        consumerTask.Start();

    }
	public  void BuildMqProducer(String message) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()
            		 ) {
        	try {
            channel.queueDeclare(_mqConfig.getQueueName(), false, false, false, null);
            channel.basicPublish("", _mqConfig.getQueueName(), null, message.getBytes("UTF-8"));
	        String logMsg="\r\n [x][rabbitMq] queueName:"+_mqConfig.getQueueName()+" \r\n    Sent '" + message + "' \r\n";
	        WriteLog(logMsg);
        	}catch(Exception e) {
        		
        	}
        } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	public  void BuildRocketMqProducer(String message) {

//		org.apache.rocketmq.client.producer.DefaultMQProducer producer = new org.apache.rocketmq.client.producer.DefaultMQProducer("test-group");
//        producer.setNamesrvAddr("localhost:9876");
//        producer.setInstanceName("rmq-instance");
		org.apache.rocketmq.client.producer.DefaultMQProducer producer = new org.apache.rocketmq.client.producer.DefaultMQProducer(_mqConfig.getGroupId());
        producer.setNamesrvAddr(_mqConfig.getNameSrvAddr());
        
        if(!SGDataHelper.StringIsNullOrWhiteSpace(_mqConfig.getInstanceName())) {
        	producer.setInstanceName(_mqConfig.getInstanceName());
        }
        
        try {
        	producer.start();
            org.apache.rocketmq.common.message.Message mmessage = new org.apache.rocketmq.common.message.Message(_mqConfig.getTopic(), _mqConfig.getTag(),message.getBytes());
            //System.out.println("生产者发送消息:"+JSON.toJSONString(user));
            org.apache.rocketmq.client.producer.SendResult sendResult= producer.send(mmessage);
            if (sendResult != null) {
    	        String logMsg="\r\n [x][rocketMq] topic:"+_mqConfig.getTopic()+" tag:"+_mqConfig.getTag()+" \r\n    Sent '" + message + "' \r\n";
    	        WriteLog(logMsg);
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
        producer.shutdown();
	}
	public  void BuildAliMqProducer(String message) {

		//参考:D:\eclipse_workspace\IpaasTest\src\com\mq\simple\ProducerTest.java
		Properties properties = GetAliMqProperties() ;
        
        Producer producer = ONSFactory.createProducer(properties);
        
        // 在发送消息前，必须调用 start 方法来启动 Producer，只需调用一次即可
        producer.start();

        Message msg = new Message( //
        		_mqConfig.getTopic(),
        		_mqConfig.getTag(),// "*"
         		 message.getBytes()
        		 );
        // 设置代表消息的业务关键属性，请尽可能全局唯一。
        // 以方便您在无法正常收到消息情况下，可通过阿里云服务器管理控制台查询消息并补发
        // 注意：不设置也不会影响消息正常收发
        String msgKey="ORDERID_" + _mqConfig.getTopic()+ SGDataHelper.ObjectToDateString(Calendar.getInstance(), "yyyyMMddHHmmss");
        msg.setKey(msgKey);
        try {
            SendResult sendResult = producer.send(msg);
            // 同步发送消息，只要不抛异常就是成功
            if (sendResult != null) {
    	        String logMsg="\r\n [x][aliMq] topic:"+_mqConfig.getTopic()+" tag:"+_mqConfig.getTag()+" \r\n    Sent '" + message + "' \r\n";
    	        WriteLog(logMsg);
            }
        }
        catch (Exception e) {
            // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
            System.out.println("\r\n"+new Date() + " TIANGONG TEST -Send mq message failed. Topic is:" + msg.getTopic()+" \r\n");
            e.printStackTrace();
        }        

        // 在应用退出前，销毁 Producer 对象
        // 注意：如果不销毁也没有问题
        producer.shutdown();
	}

	private Properties GetAliMqProperties() {
        Properties properties = new Properties();
        
        if(!SGDataHelper.StringIsNullOrWhiteSpace(_mqConfig.getGroupId())) {
            properties.put(PropertyKeyConst.GROUP_ID, _mqConfig.getGroupId());
        }
        if(!SGDataHelper.StringIsNullOrWhiteSpace(_mqConfig.getNameSrvAddr())) {
            properties.put(PropertyKeyConst.NAMESRV_ADDR, _mqConfig.getNameSrvAddr());
        }
        // 设置 TCP 接入域名（此处以公共云生产环境为例）
        if(!SGDataHelper.StringIsNullOrWhiteSpace(_mqConfig.getOnsAddr())) {
            properties.put(PropertyKeyConst.ONSAddr, _mqConfig.getOnsAddr());
        }
        // AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey,_mqConfig.getAccessKey());
        // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, _mqConfig.getSecretKey());
        //设置发送超时时间，单位毫秒
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, "3000");

        if("CLUSTERING".equals(_mqConfig.getMessageModel())) {
            properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.CLUSTERING);
        }else if("BROADCASTING".equals(_mqConfig.getMessageModel())) {
        	properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.BROADCASTING);
        }
        
        return properties;
	} 

    //参考TestSendEmailAsync()
    public void BuildPFEmailMqProducer(String message)
    {
//        var UserEmailUserName = PFDataHelper.SysEmailUserName;
//        var UserEmailPwd = PFDataHelper.SysEmailPwd;
//        var UserEmailHostName = PFDataHelper.SysEmailHostName;
//
//        ////生产方(使用User邮箱,也可以用系统邮箱吧)
//        //var rt = PFDataHelper.SendEmailAsync(UserEmailUserName, UserEmailPwd, UserEmailHostName,
//        //    new string[] { PFDataHelper.SysEmailUserName },
//        //    _mqConfig.getTopic(), message);
        SGEmailSend.SendMail(
        		new String[] { SGEmailSend.EMAIL_OWNER_ADDR },
            "PFEmailMq_product_" + _mqConfig.getTopic(), message);


        //rt.Wait();//先不测试回调
        //var resultTitle = rt.Result.Subject;
        ////Assert.IsTrue(resultTitle == "PFEmailMq_consumer_" + producerEmailTitle);
    }

    /// <summary>
    /// 异步是要等待消费者的回复
    /// </summary>
    /// <param name="message"></param>
    /// <returns></returns>
    public Thread BuildPFEmailMqProducerAsync(String message,SGRef<PFEmail> pfEmail)
    {

    	Thread rt=new Thread() {//线程操作
               public void run() {
//                   var UserEmailUserName = PFDataHelper.SysEmailUserName;
//                   var UserEmailPwd = PFDataHelper.SysEmailPwd;
//                   var UserEmailHostName = PFDataHelper.SysEmailHostName;


                   //生产方监听回复
                   //PFEmail result = null;
                   SGRef<Boolean> hasGotResult = new SGRef<Boolean>(false);
                   SGDate st = SGDate.Now();
                   //var nowStr = DateTime.Now.ToString(PFDataHelper.DateFormat);
               	PFEmailListener emailListener=PFEmailListener.init(new PFEmailManager(SGEmailSend.EMAIL_OWNER_ADDR_HOST_PROPERTY, 
                   		SGEmailSend.EMAIL_OWNER_ADDR, SGEmailSend.EMAIL_OWNER_ADDR_PASS));
               	PFListenNewEmailTask producerListenTask = new PFListenNewEmailTask("PFEmailMqProducerListener_" + _mqConfig.getTopic(),
                		   emailListener,
                   email ->
                   {
                	   pfEmail.SetValue(email);
                       hasGotResult.SetValue(true);
                   },
                   (email//, task
                   ) ->
                   {
                	   String l=("PFEmailMq_consumer_Response_" + _mqConfig.getTopic() + message);
                	   String r=email.getSubject();
                	   System.out.println("l:");
                	   System.out.println(l);
                	   System.out.println("r:");
                	   System.out.println(r);
                	   return l.equals(r);
                       //return  ("PFEmailMq_consumer_Response_" + _mqConfig.getTopic() + message).equals(email.getSubject());
                   }, true);
                   producerListenTask.Start();

                   //Thread.Sleep(2000);//不延迟的话,后面太快了,前面还没开始监听(其实没问题,因为_lastListenTime是在PFListenEmailTask初始化时就赋值了
                   ////生产方发邮件
                   BuildPFEmailMqProducer(message);
                   //PFDataHelper.SendEmail(UserEmailUserName, UserEmailPwd, UserEmailHostName,
                   //    new string[] { PFDataHelper.SysEmailUserName },
                   //    _mqConfig.getTopic(), message);

                   while (!hasGotResult.GetValue())
                   {
                	   //Object aa=(PFDate.Now().ToCalendar()-PFDate.Now().ToCalendar());
//                	   PFDataHelper.GetTimeSpan(PFDate.Now()., ymd)
                       if ((SGDate.Now().GetTotalHours()-st.GetTotalHours()) > 1)
                       {
                    	   producerListenTask.Stop();
//                           //producerListenTask.Dispose();
//                           result = new PFEmail();
//                           result.Subject = "消费者超过1小时没有响应";
                           break;
                       }
//                       Thread.Sleep(2000);
                       try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                   }
                   //producerListenTask.NaturalStop();
                   producerListenTask.Stop();
                   //return result;
               }
        };
        rt.start();
        return rt;

    }
    
	private void WriteLog(String logMsg) {
        SGDataHelper.WriteLog(logMsg);
        System.out.println(logMsg);
	}
}
