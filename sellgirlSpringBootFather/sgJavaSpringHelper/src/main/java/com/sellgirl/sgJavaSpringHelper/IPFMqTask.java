package com.sellgirl.sgJavaSpringHelper;

import com.rabbitmq.client.Delivery;

public interface IPFMqTask {
	public void Listen(String consumerTag, Delivery message);
}
