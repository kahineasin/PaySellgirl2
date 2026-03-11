//package pf.java.pfHelper.config;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import pf.java.pfHelper.config.PFMqHelper.PFMqType;
//
//@Component
//public class PFMqConfig implements Cloneable {
//
//	@PostConstruct
//	public void beforeInit() {
//	}
//
//	@Value("${pf.mq.mqType}")
//	private String mqType;
//
//	// rabbitMq
//	@Value("${pf.mq.queueName:}")
//	private String queueName;
//	@Value("${pf.mq.host:}")
//	private String host;
//
//	// aliMq
//	@Value("${pf.mq.groupId}")
//	private String groupId;
//	@Value("${pf.mq.nameSrvAddr}")
//	private String nameSrvAddr;
//	@Value("${pf.mq.onsAddr:}")
//	private String onsAddr;
//	@Value("${pf.mq.accessKey}")
//	private String accessKey;
//	@Value("${pf.mq.secretKey}")
//	private String secretKey;
//	@Value("${pf.mq.messageModel:}")
//	private String messageModel;
//	@Value("${pf.mq.topic}")
//	private String topic;
//	@Value("${pf.mq.tag}")
//	private String tag;
//	@Value("${pf.mq.instanceName:}")
//	private String instanceName;
//
//
//	public String getTag() {
//		return tag;
//	}
//
//	public void setTag(String tag) {
//		this.tag = tag;
//	}
//
//	// public String getMqType() {
////		return mqType;
////	}
////	public void setMqType(String mqType) {
////		this.mqType = mqType;
////	}
//	public PFMqType getMqType() {
//		return PFMqType.valueOf(mqType);
//	}
//
//	public void setMqType(PFMqType mqType) {
//		this.mqType = mqType.toString();
//	}
//
//	public String getQueueName() {
//		return queueName;
//	}
//
//	public void setQueueName(String queueName) {
//		this.queueName = queueName;
//	}
//
//	public String getHost() {
//		return host;
//	}
//
//	public void setHost(String host) {
//		this.host = host;
//	}
//
//	public String getGroupId() {
//		return groupId;
//	}
//
//	public void setGroupId(String groupId) {
//		this.groupId = groupId;
//	}
//
//	public String getNameSrvAddr() {
//		return nameSrvAddr;
//	}
//
//	public void setNameSrvAddr(String nameSrvAddr) {
//		this.nameSrvAddr = nameSrvAddr;
//	}
//
//	public String getOnsAddr() {
//		return onsAddr;
//	}
//
//	public void setOnsAddr(String onsAddr) {
//		this.onsAddr = onsAddr;
//	}
//
//	public String getAccessKey() {
//		return accessKey;
//	}
//
//	public void setAccessKey(String accessKey) {
//		this.accessKey = accessKey;
//	}
//
//	public String getSecretKey() {
//		return secretKey;
//	}
//
//	public void setSecretKey(String secretKey) {
//		this.secretKey = secretKey;
//	}
//
//	public String getMessageModel() {
//		return messageModel;
//	}
//
//	public void setMessageModel(String messageModel) {
//		this.messageModel = messageModel;
//	}
//
//	public String getTopic() {
//		return topic;
//	}
//
//	public void setTopic(String topic) {
//		this.topic = topic;
//	}
//
//	public String getInstanceName() {
//		return instanceName;
//	}
//
//	public void setInstanceName(String instanceName) {
//		this.instanceName = instanceName;
//	}
//	
//	@Override
//	public Object clone() {
//		PFMqConfig stu = null;
//		try {
//			stu = (PFMqConfig) super.clone();
//		} catch (CloneNotSupportedException e) {
//			e.printStackTrace();
//		}
//		return stu;
//	}
//
//}


