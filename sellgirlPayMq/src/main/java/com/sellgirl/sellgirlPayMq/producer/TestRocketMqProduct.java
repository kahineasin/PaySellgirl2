package com.sellgirl.sellgirlPayMq.producer;

//import org.springframework.stereotype.Component;

import com.sellgirl.sellgirlPayMq.PFMqHelper.PFMqType;
import com.sellgirl.sellgirlPayMq.PFMqHelper.PFProdutTask;

import com.sellgirl.sellgirlPayMq.PFMqConfig;


//@Component
public class TestRocketMqProduct implements PFProdutTask {

	@Override
	public PFMqConfig GetMqConfig(PFMqConfig mqConfig) {
		mqConfig.setMqType(PFMqType.RocketMq);
//		mqConfig.setOnsAddr("http://172.0.0.1:9876");
		mqConfig.setOnsAddr(null);
		//mqConfig.setNameSrvAddr("http://mq.server.icloud.topone.local:8080/rocketmq/nsaddr4client-internal");
		//mqConfig.setNameSrvAddr(null);
		mqConfig.setNameSrvAddr("192.168.205.111:9876");//"http://172.0.0.1:9876");
		mqConfig.setGroupId("CID_PERFECT_PRS_INTERFACE_UAT");
		mqConfig.setInstanceName("CID_PERFECT_PRS_INTERFACE_UAT");//这个参数似乎可以留空的
//		mqConfig.setAccessKey("JVczMfdmcZ3MCVX4");
//		mqConfig.setSecretKey("gpgYF9JEaf4xlcKQtLKFiIhwBI5Vm8");
		mqConfig.setTopic("RocketTopic");
		mqConfig.setTag("RocketTag");
		return mqConfig;
	}

}
