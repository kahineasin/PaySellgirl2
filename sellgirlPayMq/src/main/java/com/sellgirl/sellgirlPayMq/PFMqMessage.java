package com.sellgirl.sellgirlPayMq;

import java.io.UnsupportedEncodingException;

import com.rabbitmq.client.Delivery;

import com.sellgirl.sgJavaHelper.PFEmail;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class PFMqMessage {
	private String message;

    public PFMqMessage(PFEmail email)
    {
        message = email.getBody();
    }
	public PFMqMessage(Delivery delivery) {
	   try {
		message=new String(delivery.getBody(), SGDataHelper.encoding);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public PFMqMessage(com.aliyun.openservices.ons.api.Message aliMessage) {
			try {
				message=new String(aliMessage.getBody(), SGDataHelper.encoding);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public PFMqMessage(org.apache.rocketmq.common.message.Message aliMessage) {
		try {
			message=new String(aliMessage.getBody(),SGDataHelper.encoding);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
