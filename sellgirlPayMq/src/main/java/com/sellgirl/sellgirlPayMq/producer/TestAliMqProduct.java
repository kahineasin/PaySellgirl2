package com.sellgirl.sellgirlPayMq.producer;

////import org.springframework.stereotype.Component;

import com.sellgirl.sellgirlPayMq.PFMqHelper.PFMqType;
import com.sellgirl.sellgirlPayMq.PFMqHelper.PFProdutTask;

import com.sellgirl.sellgirlPayMq.PFMqConfig;


//@Component
public class TestAliMqProduct implements PFProdutTask {

	@Override
	public PFMqConfig GetMqConfig(PFMqConfig mqConfig) {
		mqConfig.setMqType(PFMqType.AliMq);
		mqConfig.setTopic("PERFECT_TOPIC_ISCRM_THIRD_PERFECT_UAT");
		mqConfig.setTag("OPEN_CARD/EDIT_CARD");
		return mqConfig;
	}

}
