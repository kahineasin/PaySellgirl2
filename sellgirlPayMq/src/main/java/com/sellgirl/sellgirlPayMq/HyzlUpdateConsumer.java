//package com.sellgirl.sellgirlPayMq;
//
//import org.springframework.stereotype.Component;
//
//import com.sellgirl.sellgirlPayMq.producer.HyzlUpdateProduct;
//
//import pf.java.pfHelper.PFMqMessage;
//import pf.java.pfHelper.config.PFMqConfig;
//import pf.java.pfHelper.config.PFMqHelper.PFConsumerTask;
//
///*
// * 新增/修改会员基本信息(接收mq)
// * 相关文档:D:\wxj_doc\2019\12.9-12.14  代码培训\接口服务\CRM\crm接口服务\核心\完美系统对接会员MQ接口文档20190719CRM反馈.docx
// * 相关调用
// */
//@Component
//public class HyzlUpdateConsumer implements PFConsumerTask {
//
//	@Override
//	public void handle(String consumerTag, PFMqMessage message){
//        //String strMessage =message.getMessage();
////        String logMsg="\r\n [x] HyzlUpdateConsumer Received '" + strMessage + "' \r\n";
////        PFDataHelper.WriteLog(logMsg);
////        System.out.println(logMsg);		
//	}
//
//	@Override
//	public PFMqConfig GetMqConfig(PFMqConfig mqConfig) {
//		mqConfig=new HyzlUpdateProduct().GetMqConfig(mqConfig);
//		mqConfig.setTag("*");
//		return mqConfig;}
//
//}
