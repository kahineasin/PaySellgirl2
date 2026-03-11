package com.sellgirl.sellgirlPayMq.producer;

//import org.springframework.stereotype.Component;

import com.sellgirl.sellgirlPayMq.PFMqConfig;
import com.sellgirl.sellgirlPayMq.PFMqHelper.PFMqType;
import com.sellgirl.sellgirlPayMq.PFMqHelper.PFProdutTask;

//import pf.java.pfHelper.config.PFMqConfig;


//@Component
public class HyzlUpdateProduct implements PFProdutTask {

	@Override
	public PFMqConfig GetMqConfig(PFMqConfig mqConfig) {
		//wisesystem-bns
		//mqConfig=new PFMqConfig();
		mqConfig.setGroupId("CID_PERFECT_PRS_INTERFACE_UAT");
		mqConfig.setAccessKey("JVczMfdmcZ3MCVX4");
		mqConfig.setSecretKey("gpgYF9JEaf4xlcKQtLKFiIhwBI5Vm8");
		//mqConfig.setNameSrvAddr("http://mq.server.icloud.topone.local:8080/rocketmq/nsaddr4client-internal");
		mqConfig.setNameSrvAddr(null);
//		mqConfig.setOnsAddr("http://mq.server.icloud.topone.local:8080/rocketmq/nsaddr4client-internal");
		//mqConfig.setOnsAddr("http://mq.server.icloud.topone.local/rocketmq/nsaddr4client-internet");
		mqConfig.setMqType(PFMqType.AliMq);
		mqConfig.setTopic("PERFECT_TOPIC_ISCRM_THIRD_PERFECT_UAT");
		mqConfig.setTag("OPEN_CARD/EDIT_CARD");
		//mqConfig.setTag("DOMSORDER");
		return mqConfig;
	}

}
