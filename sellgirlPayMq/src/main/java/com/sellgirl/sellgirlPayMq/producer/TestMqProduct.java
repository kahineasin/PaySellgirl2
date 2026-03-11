package com.sellgirl.sellgirlPayMq.producer;

//import org.springframework.stereotype.Component;

import com.sellgirl.sellgirlPayMq.PFMqHelper.PFProdutTask;

import com.sellgirl.sellgirlPayMq.PFMqConfig;


//@Component
public class TestMqProduct implements PFProdutTask {

	@Override
	public PFMqConfig GetMqConfig(PFMqConfig mqConfig) {
		return mqConfig;
	}

}
