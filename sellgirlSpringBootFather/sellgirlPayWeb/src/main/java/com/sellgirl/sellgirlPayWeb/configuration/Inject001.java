//package com.sellgirl.sellgirlPayWeb.configuration;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//
//@Component
//public class Inject001 {
//    @Value("${tpf.test1}")
//    private  String test1;
//	@Value("${tpf.test2:002d}")
//    private  String test2;
//	
//    public String getTest1() {
//		return test1;
//	}
//	public void setTest1(String test1) {
//		this.test1 = test1;
//	}
//	public String getTest2() {
//		return test2;
//	}
//	public void setTest2(String test2) {
//		this.test2 = test2;
//	}
//
//    @JsonProperty
//    protected String ip;
//    
//	@Value("${tpf.test3:}")
//	public void setIp(String addr) {
//		this.ip = addr;
//	}
//}
