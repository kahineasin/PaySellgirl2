//package com.sellgirl.sellgirlPayMq;
//
//import org.springframework.stereotype.Component;
//
//import pf.java.pfHelper.PFMqMessage;
//import pf.java.pfHelper.config.PFMqConfig;
//import pf.java.pfHelper.config.PFMqHelper.PFConsumerTask;
//
//@Component
//public class TestMqConsumer2 implements PFConsumerTask {
//
//	@Override
//	public void handle(String consumerTag, PFMqMessage message){
//        //String strMessage =message.getMessage();
////        String logMsg=" [x] mq2 Received '" + strMessage + "' \r\n";
////        PFDataHelper.WriteLog(logMsg);
////        System.out.println(logMsg);		
//	}
//
//	@Override
//	public PFMqConfig GetMqConfig(PFMqConfig mqConfig) {
//		mqConfig.setQueueName("rabbitMq2");
//		return mqConfig;
//	}
//
//}
