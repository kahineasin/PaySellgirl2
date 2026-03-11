package com.sellgirl.sellgirlPayMq;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.stereotype.Component;

import com.sellgirl.sgJavaHelper.PFEmail;
//import pf.java.pfHelper.PFMq;
//import pf.java.pfHelper.PFMqMessage;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
/*
 * 为了兼容普通mq和alimq
 * 此类为单例,考虑调试方便,如果有多个 GroupId的情况,建新类PFMq来实现吧
 */
//@Component //使用的类也要此特性
public class PFMqHelper {
	public  enum PFMqType{
		AliMq,RabbitMq,RocketMq, PFEmailMq,Kafka
	}
	//@FunctionalInterface
	public interface PFConsumerTask {
	    void handle(String consumerTag, PFMqMessage message) ;
	    PFMqConfig GetMqConfig(PFMqConfig mqConfig);
	    
	}
    /// <summary>
    /// 消费者会回复生产者
    /// </summary>
    public interface PFConsumerResponseTask
    {
        /// <summary>
        /// 返回null时,不会回应producer;返回数据时,把发送数据给producer(producer需要使用PFMqHelper.BuildProducerAsync()方法)
        /// </summary>
        /// <param name="consumerTag"></param>
        /// <param name="message"></param>
        /// <returns>响应producer的数据(注意:返回的对象会经过JsonConvert.SerializeObject()转换)</returns>
        Object handle(String consumerTag, PFMqMessage message);
        PFMqConfig GetMqConfig(PFMqConfig mqConfig);

    }
	public interface PFProdutTask {
	    PFMqConfig GetMqConfig(PFMqConfig mqConfig);
	    
	}
    /// <summary>
    /// 生产者需要等待消费者回复的
    /// </summary>
    public interface PFProdutResponseTask extends PFProdutTask
    {
    }


	private static PFMqType _mqType=PFMqType.RabbitMq;
    private static PFMqConfig _mqConfig;
    //private static ApplicationContext _applicationContext;
//    @Autowired
//    public PFMqHelper(PFMqConfig mqConfig,ApplicationContext applicationContext) {
//    	PFMqHelper._mqConfig = mqConfig;
//    	PFMqHelper._applicationContext=applicationContext;
//    	//_mqType=PFMqType.valueOf(mqConfig.getMqType());
//    	_mqType=mqConfig.getMqType();
//    }
	public static void BuildConsumer(PFConsumerTask task) {
		 PFMqConfig mqConfig=task.GetMqConfig(PFMqConfig.class.cast(_mqConfig.clone()));
		 PFMqType mqType=mqConfig.getMqType();
		switch(mqType) {
			case RabbitMq:
				(new PFMq(mqConfig)).BuildRabbitMqConsumer(task);
				break;
			case RocketMq:
				(new PFMq(mqConfig)).BuildRocketMqConsumer(task);
				break;
			case AliMq:
				(new PFMq(mqConfig)).BuildAliMqConsumer(task);
				break;
			default:
				break;
		}
	}
    public static void BuildConsumer(PFConsumerResponseTask task)
    {
		 PFMqConfig mqConfig=task.GetMqConfig(PFMqConfig.class.cast(_mqConfig.clone()));
        PFMqType mqType = mqConfig.getMqType();
        switch (mqType)
        {
            //case PFMqType.RabbitMq:
            //	(new PFMq(mqConfig)).BuildRabbitMqConsumer(task);
            //	break;
            //case PFMqType.RocketMq:
            //	(new PFMq(mqConfig)).BuildRocketMqConsumer(task);
            //	break;
            //case PFMqType.AliMq:
            //	(new PFMq(mqConfig)).BuildAliMqConsumer(task);
            //	break;
            case PFEmailMq:
                (new PFMq(mqConfig)).BuildPFEmailMqConsumer(task);
                break;
            default:
                break;
        }
    }
	public static void BuildProducer(String message) {
		switch(_mqType) {
			case RabbitMq:
				(new PFMq(_mqConfig)).BuildMqProducer(message);
				break;
			case RocketMq:
				(new PFMq(_mqConfig)).BuildRocketMqProducer(message);
				break;
			case AliMq:
				(new PFMq(_mqConfig)).BuildAliMqProducer(message);
				break;
			default:
				break;
		}
	}
	public static void BuildProducer(String message,PFProdutTask task) {
		 PFMqConfig mqConfig=task.GetMqConfig(PFMqConfig.class.cast(_mqConfig.clone()));
		 PFMqType mqType=mqConfig.getMqType();
		switch(mqType) {
			case RabbitMq:
				(new PFMq(mqConfig)).BuildMqProducer(message);
				break;
			case RocketMq:
				(new PFMq(mqConfig)).BuildRocketMqProducer(message);
				break;
			case AliMq:
				(new PFMq(mqConfig)).BuildAliMqProducer(message);
				break;
			default:
				break;
		}
	}
    public static Thread BuildProducer(String message, PFProdutResponseTask task,SGRef<PFEmail> pfEmail)
    {
		 PFMqConfig mqConfig=task.GetMqConfig(PFMqConfig.class.cast(_mqConfig.clone()));
        PFMqType mqType = mqConfig.getMqType();
        switch (mqType)
        {
            //case RabbitMq:
            //	(new PFMq(mqConfig)).BuildMqProducer(message);
            //	break;
            //case RocketMq:
            //	(new PFMq(mqConfig)).BuildRocketMqProducer(message);
            //	break;
            //case AliMq:
            //	(new PFMq(mqConfig)).BuildAliMqProducer(message);
            //	break;
            case PFEmailMq:
                return (new PFMq(mqConfig)).BuildPFEmailMqProducerAsync(message,pfEmail);
                //break;
            default:
                break;
        }
        return null;
    }
 
//	public static void ListenMqOld() {
//	   
//		   Map<String,PFConsumerTask> res = _applicationContext.getBeansOfType(PFConsumerTask.class);
//		   Iterator<Entry<String, PFConsumerTask>> iter = res.entrySet().iterator();
//		   while(iter.hasNext()){
//			   Entry<String, PFConsumerTask> key=iter.next();
//			   PFConsumerTask tmpObj;
//				try {
//					tmpObj = PFConsumerTask.class.cast(key.getValue().getClass().newInstance());
//				    PFMqHelper.BuildConsumer(tmpObj);
//				} catch (InstantiationException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IllegalAccessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//		   }
//	}
    //Spring的方式
//	public static void ListenMq() {
//		   
//		   Map<String,PFConsumerTask> res = _applicationContext.getBeansOfType(PFConsumerTask.class);
//		   Iterator<Entry<String, PFConsumerTask>> iter = res.entrySet().iterator();
//		   while(iter.hasNext()){
//			   Entry<String, PFConsumerTask> key=iter.next();
//			   //PFConsumerTask tmpObj;
//				try {
//					   if(key.getValue() instanceof PFConsumerResponseTask) {
//						   PFMqHelper.BuildConsumer(PFConsumerResponseTask.class.cast(key.getValue()));
//					   }else 
//					   if(key.getValue() instanceof PFConsumerTask) {
//						   PFMqHelper.BuildConsumer(key.getValue());
//					   }
//					   
////					tmpObj = PFConsumerTask.class.cast(key.getValue().getClass().newInstance());
////				    PFMqHelper.BuildConsumer(tmpObj);
//				} catch (Exception e) {
//					e.printStackTrace();
//				} 
//		   }
//	}
    //非Spring方式
	public static <T extends PFConsumerTask> void ListenMq(Class<T> packageCls) {
		   
		   //Map<String,PFConsumerTask> res = _applicationContext.getBeansOfType(PFConsumerTask.class);
			List<Class<?>> jdbcCls=SGDataHelper.getAllAssignedClass(packageCls,PFConsumerTask.class);
			Map<String,PFConsumerTask> res = SGDataHelper.<Class<?>,String,PFConsumerTask>ListToMapT(jdbcCls, a->a.getSimpleName(), a->{
				try {
					return (PFConsumerTask)a.newInstance();
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return null;
			});
		   Iterator<Entry<String, PFConsumerTask>> iter = res.entrySet().iterator();
		   while(iter.hasNext()){
			   Entry<String, PFConsumerTask> key=iter.next();
			   //PFConsumerTask tmpObj;
				try {
					   if(key.getValue() instanceof PFConsumerResponseTask) {
						   PFMqHelper.BuildConsumer(PFConsumerResponseTask.class.cast(key.getValue()));
					   }else 
					   if(key.getValue() instanceof PFConsumerTask) {
						   PFMqHelper.BuildConsumer(key.getValue());
					   }
					   
//					tmpObj = PFConsumerTask.class.cast(key.getValue().getClass().newInstance());
//				    PFMqHelper.BuildConsumer(tmpObj);
				} catch (Exception e) {
					e.printStackTrace();
				} 
		   }
	}
	
	
}
