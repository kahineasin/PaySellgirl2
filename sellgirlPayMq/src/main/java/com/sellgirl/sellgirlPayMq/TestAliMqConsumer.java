package com.sellgirl.sellgirlPayMq;
//package com.sellgirl.sellgirlPayMq;
//
//import org.springframework.stereotype.Component;
//
//import pf.java.pfHelper.PFMqMessage;
//import pf.java.pfHelper.config.PFDataHelper;
//import pf.java.pfHelper.config.PFMqConfig;
//import pf.java.pfHelper.config.PFMqHelper.PFConsumerTask;
//import pf.java.pfHelper.config.PFMqHelper.PFMqType;
//
//@Component
//public class TestAliMqConsumer implements PFConsumerTask {
//
//	@Override
//	public void handle(String consumerTag, PFMqMessage message){
//        String strMessage =message.getMessage();
//        String logMsg="\r\n [x] AliMq1 Received '" + strMessage + "' \r\n";
//        PFDataHelper.WriteLog(logMsg);
//        System.out.println(logMsg);		
//	}
//
//	@Override
//	public PFMqConfig GetMqConfig(PFMqConfig mqConfig) {
//		  //ConsumerId, "CID_I_SCRM_PERFECT_TEST"
//		  //PropertyKeyConst.GROUP_ID, "CID_I_SCRM_PERFECT_TEST"
//	      //PropertyKeyConst.AccessKey, "JVczMfdmcZ3MCVX4"
//	      //PropertyKeyConst.SecretKey, "gpgYF9JEaf4xlcKQtLKFiIhwBI5Vm8"
// 	      //NAMESRV_ADDR 172.100.2.212:9876;172.100.2.164:9876;172.100.15.35:9876

//		  //ProducerId, "PID_PERFECT_TEST"
//		  //GROUP_ID, "PID_PERFECT_TEST"
//		  //GROUP_ID, "CID_I_SCRM_PERFECT_TEST"
//	      //PropertyKeyConst.AccessKey, "JVczMfdmcZ3MCVX4"
//	      //PropertyKeyConst.SecretKey, "gpgYF9JEaf4xlcKQtLKFiIhwBI5Vm8"
// 	      //ONSAddr, "http://mq.server.icloud.topone.local:8080/rocketmq/nsaddr4broker-internal"
//		  //NAMESRV_ADDR, "172.100.2.212:9876;172.100.2.164:9876;172.100.15.35:9876"
//		  //tag,"YUNDT_CUBE_SINGLE_TAG_TIANGONG_TEST"

//		mqConfig.setMqType(PFMqType.AliMq);
//		mqConfig.setTopic("PERFECT_TOPIC_ISCRM_THIRD_PERFECT_UAT");
//		mqConfig.setTag("OPEN_CARD/EDIT_CARD");
//		return mqConfig;
//	}
//
//}
