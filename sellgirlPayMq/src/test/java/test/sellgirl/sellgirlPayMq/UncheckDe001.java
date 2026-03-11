package test.sellgirl.sellgirlPayMq;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.config.SGDataHelper.LocalDataType;
import com.sellgirl.sgJavaHelper.express.PFExpressHelper;
import com.sellgirl.sgJavaHelper.model.SysType;
import com.sellgirl.sgJavaHelper.model.UserOrg;
import com.sellgirl.sgJavaHelper.task.IPFTask;
import com.sellgirl.sgJavaHelper.task.PFDayTask;
import com.sellgirl.sgJavaHelper.task.PFDayTask2;
import com.sellgirl.sgJavaHelper.task.PFIntervalExactTask;
import com.sellgirl.sgJavaHelper.task.PFIntervalExactTask2;
import com.sellgirl.sgJavaHelper.task.PFIntervalTask;
import com.sellgirl.sgJavaHelper.task.PFMonthTask;
import com.sellgirl.sgJavaHelper.task.PFMonthTask2;
import com.sellgirl.sgJavaHelper.task.PFTimeTaskBase2.TimePoint;
import test.sellgirl.sellgirlPayMq.model.FlinkMessage;
//import test.sellgirl.sellgirlPayMq.model.TestTb04;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;

import javax.imageio.ImageIO;
//import javax.mail.Message;

import com.sellgirl.sgJavaHelper.sql.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.sellgirl.sgJavaHelper.*;
import com.sellgirl.sgJavaHelper.config.*;
//import test.java.pfHelper.model.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * 不要随便更改此类名,以防打包时执行了此类
 * @author Administrator
 *
 */
@SuppressWarnings(value = { "unused", "deprecation" })
public class UncheckDe001 extends TestCase {
	public static void initPFHelper() {
		//PFDataHelper.SetConfigMapper(new PFConfigTestMapper());		
		//new PFDataHelper(new PFAppConfig());
	}
	@Override
	public void setUp() {
		initPFHelper();
	}
    public void testKafkaConsumer()
    {
		 // 1.创建消费者的配置对象
        Properties properties = new Properties();
        // 2.给消费者配置对象添加参数
//        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "IP:9092");
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverId);
        
        // 配置序列化必须
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 配置消费者组（组名任意起名）必须
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        try (// 创建消费者对象
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties)) {
			// 注册要消费的主题（可以消费多个主题）
			ArrayList<String> topics = new ArrayList<>();
			topics.add(topic);
			kafkaConsumer.subscribe(topics);
			System.out.println("准备接收数据......");
			// 拉取数据打印
			//这种方式好像跟rabitMq之类的不一样，需要自己写定时循环,如果是这样的话，要写一个kafkaListener(类似emailListener)那样才行
			while (true) {
			    // 设置 1s 中消费⼀批数据
			    ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
			    // 打印消费到的数据
			    for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
			        System.out.println(consumerRecord);
			    }
			}
			//assertTrue( true );
		}
    }
    public void testKafkaConsumer2()
    {
		 // 1.创建消费者的配置对象
        Properties properties = new Properties();
        // 2.给消费者配置对象添加参数
//        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "IP:9092");
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverId);
        
        // 配置序列化必须
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 配置消费者组（组名任意起名）必须
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group1");
        try (// 创建消费者对象
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties)) {
			// 注册要消费的主题（可以消费多个主题）
			ArrayList<String> topics = new ArrayList<>();
			topics.add(this.topic);
			kafkaConsumer.subscribe(topics);
			System.out.println("准备接收数据......");
			// 拉取数据打印
			//这种方式好像跟rabitMq之类的不一样，需要自己写定时循环,如果是这样的话，要写一个kafkaListener(类似emailListener)那样才行
			while (true) {
			    // 设置 1s 中消费⼀批数据
			    ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
			    // 打印消费到的数据
			    for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
			        System.out.println(consumerRecord);
			    }
			}
			//assertTrue( true );
		}
    }
    /**
     * 四、指定 Offset 消费
     * 
     */
    public void testKafkaConsumer4()
    {
		 // 1.创建消费者的配置对象
        Properties properties = new Properties();
        // 2.给消费者配置对象添加参数
//        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "IP:9092");
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverId);
        
        // 配置序列化必须
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 配置消费者组（组名任意起名）必须
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        try (// 创建消费者对象
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties)) {
			// 注册要消费的主题（可以消费多个主题）
			ArrayList<String> topics = new ArrayList<>();
			topics.add(topic);
			kafkaConsumer.subscribe(topics);
			System.out.println("准备接收数据......");
	        // 指定位置进行消费
	        Set<TopicPartition> assignment = kafkaConsumer.assignment();

	        //  保证分区分配方案已经制定完毕
	        while (assignment.size() == 0){
	            kafkaConsumer.poll(Duration.ofSeconds(1));
	            assignment = kafkaConsumer.assignment();
	        }

	        // 指定消费的offset
	        for (TopicPartition topicPartition : assignment) {
	            kafkaConsumer.seek(topicPartition,6);
	        }
			// 拉取数据打印
			//这种方式好像跟rabitMq之类的不一样，需要自己写定时循环,如果是这样的话，要写一个kafkaListener(类似emailListener)那样才行
			while (true) {
			    // 设置 1s 中消费⼀批数据
			    ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
			    // 打印消费到的数据
			    for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
			        System.out.println(consumerRecord);
			    }
			}
			//assertTrue( true );
		}
    }
    /**
     * 五、指定时间消费
     */
    public void testKafkaConsumer5()
    {
		 // 1.创建消费者的配置对象
        Properties properties = new Properties();
        // 2.给消费者配置对象添加参数
//        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "IP:9092");
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverId);
        
        // 配置序列化必须
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 配置消费者组（组名任意起名）必须
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        try (// 创建消费者对象
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties)) {
			// 注册要消费的主题（可以消费多个主题）
			ArrayList<String> topics = new ArrayList<>();
			topics.add(topic);
			kafkaConsumer.subscribe(topics);
			System.out.println("准备接收数据......");
	        // 指定位置进行消费
	        Set<TopicPartition> assignment = kafkaConsumer.assignment();

	        //  保证分区分配方案已经制定完毕
	        while (assignment.size() == 0){
	            kafkaConsumer.poll(Duration.ofSeconds(1));
	            assignment = kafkaConsumer.assignment();
	        }

	        // 希望把时间转换为对应的offset
	        HashMap<TopicPartition, Long> topicPartitionLongHashMap = new HashMap<>();

	        // 封装集合存储，每个分区对应一天前的数据
	        for (TopicPartition topicPartition : assignment) {
	            topicPartitionLongHashMap.put(topicPartition,System.currentTimeMillis() - 3*24 * 3600 * 1000);
	        }
	        // 获取从 1 天前开始消费的每个分区的 offset
	        Map<TopicPartition, OffsetAndTimestamp> topicPartitionOffsetAndTimestampMap = kafkaConsumer.offsetsForTimes(topicPartitionLongHashMap);

	        // 遍历每个分区，对每个分区设置消费时间
	        for (TopicPartition topicPartition : assignment) {
	            OffsetAndTimestamp offsetAndTimestamp = topicPartitionOffsetAndTimestampMap.get(topicPartition);
	            // 根据时间指定开始消费的位置
	            kafkaConsumer.seek(topicPartition,offsetAndTimestamp.offset());
	        }
			// 拉取数据打印
			//这种方式好像跟rabitMq之类的不一样，需要自己写定时循环,如果是这样的话，要写一个kafkaListener(类似emailListener)那样才行
			while (true) {
			    // 设置 1s 中消费⼀批数据
			    ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
			    // 打印消费到的数据
			    for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
			        System.out.println(consumerRecord);
			    }
			}
			//assertTrue( true );
		}
    }
    //kafka->mysql test2.test_tb_04
    ////private String serverId="uat-cloud.perfect99.com:10015,uat-cloud.perfect99.com:10017,uat-cloud.perfect99.com:10019";
//    private String serverId="uat-cloud.perfect99.com:20011,uat-cloud.perfect99.com:20012,uat-cloud.perfect99.com:20013";
//    private String topic="maxwell2";
//    private String groupId="maxwell-d367274d-3741-059d-eb21-be6229943cd0";
    
    //kafka->mysql test2.bloodpressure_daily_t2_test
    private String serverId="uat-cloud.perfect99.com:20011,uat-cloud.perfect99.com:20012,uat-cloud.perfect99.com:20013";
//    private String topic="phrs_blood_pressure_test";
    //private String topic="phrs_weight_scale";
    private String topic="phrs_we_chat_step";//"phrs_weight_index_record";    
    private String groupId="maxwell-0b7ac136-ae8e-26bc-7395-c53363149b33_111111";
    

//  private String serverId="uat-cloud.perfect99.com:20011,uat-cloud.perfect99.com:20012,uat-cloud.perfect99.com:20013";
//  private String topic="phrs_blood_pressure";//这个是正式的topic,不要随便用它生产消息
//  private String groupId="maxwell-0b7ac136-ae8e-26bc-7395-c53363149b33_111";
  //private String topic="testDateGMT";
    

    /**
     * 测试topic不存在时
     */
    public void testKafkaConsumerWhenTopicNotExist()
    {
		 // 1.创建消费者的配置对象
        Properties properties = new Properties();
        // 2.给消费者配置对象添加参数
//        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "IP:9092");
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverId);
        
        // 配置序列化必须
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 配置消费者组（组名任意起名）必须
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group1");
        try (// 创建消费者对象
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties)) {
			// 注册要消费的主题（可以消费多个主题）
			ArrayList<String> topics = new ArrayList<>();
			topics.add("1112222333");
			kafkaConsumer.subscribe(topics);
			 Map<String, List<PartitionInfo>> tmpTopic=kafkaConsumer.listTopics();
			 System.out.println("topics:"+String.join(",", tmpTopic.keySet()));
			 System.out.println("topics 是否存在:"+tmpTopic.containsKey("1112222333"));
			System.out.println("准备接收数据......");
			// 拉取数据打印
			//这种方式好像跟rabitMq之类的不一样，需要自己写定时循环,如果是这样的话，要写一个kafkaListener(类似emailListener)那样才行
			while (true) {
			    // 设置 1s 中消费⼀批数据
			    ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
			    // 打印消费到的数据
			    for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
			        System.out.println(consumerRecord);
			    }
			}
			//assertTrue( true );
		}
    }
    
    /**
     * 五、指定时间消费
     */
    public void testKafkaCheckDataType()
    {
		 // 1.创建消费者的配置对象
        Properties properties = new Properties();
        // 2.给消费者配置对象添加参数
//        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "IP:9092");
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverId);
        
        // 配置序列化必须
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 配置消费者组（组名任意起名）必须
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        try (// 创建消费者对象
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties)) {
			// 注册要消费的主题（可以消费多个主题）
			ArrayList<String> topics = new ArrayList<>();
			topics.add(topic);
			kafkaConsumer.subscribe(topics);
			System.out.println("准备接收数据......");
	        // 指定位置进行消费
	        Set<TopicPartition> assignment = kafkaConsumer.assignment();

	        //  保证分区分配方案已经制定完毕
	        while (assignment.size() == 0){
	            kafkaConsumer.poll(Duration.ofSeconds(1));
	            assignment = kafkaConsumer.assignment();
	        }

	        // 希望把时间转换为对应的offset
	        HashMap<TopicPartition, Long> topicPartitionLongHashMap = new HashMap<>();

	        // 封装集合存储，每个分区对应一天前的数据
	        for (TopicPartition topicPartition : assignment) {
	            topicPartitionLongHashMap.put(topicPartition,System.currentTimeMillis() - 3*24 * 3600 * 1000);
	        }
	        // 获取从 1 天前开始消费的每个分区的 offset
	        Map<TopicPartition, OffsetAndTimestamp> topicPartitionOffsetAndTimestampMap = kafkaConsumer.offsetsForTimes(topicPartitionLongHashMap);

	        // 遍历每个分区，对每个分区设置消费时间
	        for (TopicPartition topicPartition : assignment) {
	            OffsetAndTimestamp offsetAndTimestamp = topicPartitionOffsetAndTimestampMap.get(topicPartition);
	            // 根据时间指定开始消费的位置
	            kafkaConsumer.seek(topicPartition,offsetAndTimestamp.offset());
	        }
			// 拉取数据打印
			//这种方式好像跟rabitMq之类的不一样，需要自己写定时循环,如果是这样的话，要写一个kafkaListener(类似emailListener)那样才行
			while (true) {
			    // 设置 1s 中消费⼀批数据
			    ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
			    // 打印消费到的数据
			    for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
			        //System.out.println(consumerRecord);

			        FlinkMessage data = JSONObject.parseObject(consumerRecord.value(), FlinkMessage.class);
			        Object obj=data.getAfter().get("distance");
			        if(null!=obj) {
				        System.out.println(obj);
				        System.out.println(obj.getClass());
			        }
			    }
			}
			//assertTrue( true );
		}
    }
    
    public void testKafkaCreateTopic()
    {
		 // 1.创建消费者的配置对象
        Properties properties = new Properties();
        // 2.给消费者配置对象添加参数
//        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "IP:9092");
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverId);
        
        // 配置序列化必须
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 配置消费者组（组名任意起名）必须
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group1");
        try (// 创建消费者对象
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties)) {
			// 注册要消费的主题（可以消费多个主题）
			ArrayList<String> topics = new ArrayList<>();
			topics.add("1112222333");
			kafkaConsumer.subscribe(topics);
			 Map<String, List<PartitionInfo>> tmpTopic=kafkaConsumer.listTopics();
			 System.out.println("topics:"+String.join(",", tmpTopic.keySet()));
			 System.out.println("topics 是否存在:"+tmpTopic.containsKey("1112222333"));
			System.out.println("准备接收数据......");
			// 拉取数据打印
			//这种方式好像跟rabitMq之类的不一样，需要自己写定时循环,如果是这样的话，要写一个kafkaListener(类似emailListener)那样才行
			while (true) {
			    // 设置 1s 中消费⼀批数据
			    ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
			    // 打印消费到的数据
			    for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
			        System.out.println(consumerRecord);
			    }
			}
			//assertTrue( true );
		}
    }
//    public class KafkaConsumer1<K, V> extends KafkaConsumer<K, V> {
//        @Override
//    public Map<String, List<PartitionInfo>> listTopics() {
//       // acquire();
//        try {
//            return fetcher.getAllTopicMetadata(requestTimeoutMs);
//        } finally {
//            release();
//        }
//    }
//}
    
    public void testKafkaProduct() throws InterruptedException
    {
    	Properties props = new Properties();
	//kafka服务器地址
	props.put("bootstrap.servers", serverId);
	//ack是判断请求是否为完整的条件（即判断是否成功发送）。all将会阻塞消息，这种设置性能最低，但是最可靠。
	props.put("acks", "1");
	//retries,如果请求失败，生产者会自动重试，我们指定是0次，如果启用重试，则会有重复消息的可能性。
	props.put("retries", 0);
	//producer缓存每个分区未发送消息，缓存的大小是通过batch.size()配置设定的。值较大的话将会产生更大的批。并需要更多的内存(因为每个“活跃”的分区都有一个缓冲区)
	props.put("batch.size", 16384);
	//默认缓冲区可立即发送，即便缓冲区空间没有满；但是，如果你想减少请求的数量，可以设置linger.ms大于0.这将指示生产者发送请求之前等待一段时间
	//希望更多的消息补填到未满的批中。这类似于tcp的算法，例如上面的代码段，可能100条消息在一个请求发送，因为我们设置了linger时间为1ms，然后，如果我们
	//没有填满缓冲区，这个设置将增加1ms的延迟请求以等待更多的消息。需要注意的是，在高负载下，相近的时间一般也会组成批，即使是linger.ms=0。
	//不处于高负载的情况下，如果设置比0大，以少量的延迟代价换取更少的，更有效的请求。
	props.put("linger.ms", 1);
	//buffer.memory控制生产者可用的缓存总量，如果消息发送速度比其传输到服务器的快，将会耗尽这个缓存空间。当缓存空间耗尽，其他发送调用将被阻塞，阻塞时间的阈值
	//通过max.block.ms设定，之后他将抛出一个TimeoutExecption。
	props.put("buffer.memory", 33554432);
	//key.serializer和value.serializer示例：将用户提供的key和value对象ProducerRecord转换成字节，你可以使用附带的ByteArraySerizlizaer或StringSerializer处理简单的byte和String类型.
	props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	//设置kafka的分区数量
	props.put("kafka.partitions", 12);
	
	Producer<String, String> producer = new KafkaProducer<>(props);
	for (int i = 0; i < 50; i++){
		System.out.println("key-->key"+i+"  value-->vvv"+i);
		producer.send(new ProducerRecord<String, String>(topic, "key"+i, "vvv"+i));
		Thread.sleep(1000);
	}
		  
	producer.close();

    }

//    public void testKafkaProductTestTb04() throws InterruptedException
//    {
//    	Properties props = new Properties();
//	//kafka服务器地址
//	props.put("bootstrap.servers", serverId);
//	//ack是判断请求是否为完整的条件（即判断是否成功发送）。all将会阻塞消息，这种设置性能最低，但是最可靠。
//	props.put("acks", "1");
//	//retries,如果请求失败，生产者会自动重试，我们指定是0次，如果启用重试，则会有重复消息的可能性。
//	props.put("retries", 0);
//	//producer缓存每个分区未发送消息，缓存的大小是通过batch.size()配置设定的。值较大的话将会产生更大的批。并需要更多的内存(因为每个“活跃”的分区都有一个缓冲区)
//	props.put("batch.size", 16384);
//	//默认缓冲区可立即发送，即便缓冲区空间没有满；但是，如果你想减少请求的数量，可以设置linger.ms大于0.这将指示生产者发送请求之前等待一段时间
//	//希望更多的消息补填到未满的批中。这类似于tcp的算法，例如上面的代码段，可能100条消息在一个请求发送，因为我们设置了linger时间为1ms，然后，如果我们
//	//没有填满缓冲区，这个设置将增加1ms的延迟请求以等待更多的消息。需要注意的是，在高负载下，相近的时间一般也会组成批，即使是linger.ms=0。
//	//不处于高负载的情况下，如果设置比0大，以少量的延迟代价换取更少的，更有效的请求。
//	props.put("linger.ms", 1);
//	//buffer.memory控制生产者可用的缓存总量，如果消息发送速度比其传输到服务器的快，将会耗尽这个缓存空间。当缓存空间耗尽，其他发送调用将被阻塞，阻塞时间的阈值
//	//通过max.block.ms设定，之后他将抛出一个TimeoutExecption。
//	props.put("buffer.memory", 33554432);
//	//key.serializer和value.serializer示例：将用户提供的key和value对象ProducerRecord转换成字节，你可以使用附带的ByteArraySerizlizaer或StringSerializer处理简单的byte和String类型.
//	props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//	props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//	//设置kafka的分区数量
//	props.put("kafka.partitions", 12);
//	
//	Producer<String, String> producer = new KafkaProducer<>(props);
//	for (int i = 0; i < 50; i++){
//		//System.out.println("key-->key"+i+"  value-->vvv"+i);
//		TestTb04 item=new TestTb04();
//		item.setId(i);
//		item.setCol1("a"+i);
//		item.setCol2("b"+i);
//		FlinkMessage msg=new FlinkMessage();
//		msg.setOp("c");
//		msg.setAfter(JSON.parseObject(JSON.toJSONString(item)));
//		String str=JSON.toJSONString(msg);
//		System.out.println("key-->key"+i+"  value-->vvv"+str);
//		producer.send(new ProducerRecord<String, String>(topic+"2", "key"+i, str));
//		Thread.sleep(1000);
//	}
//		  
//	producer.close();
//    }

    public void testKafkaProductTestTb04() throws InterruptedException
    {
    	Properties props = new Properties();
	//kafka服务器地址
	props.put("bootstrap.servers", serverId);
	//ack是判断请求是否为完整的条件（即判断是否成功发送）。all将会阻塞消息，这种设置性能最低，但是最可靠。
	props.put("acks", "1");
	//retries,如果请求失败，生产者会自动重试，我们指定是0次，如果启用重试，则会有重复消息的可能性。
	props.put("retries", 0);
	//producer缓存每个分区未发送消息，缓存的大小是通过batch.size()配置设定的。值较大的话将会产生更大的批。并需要更多的内存(因为每个“活跃”的分区都有一个缓冲区)
	props.put("batch.size", 16384);
	//默认缓冲区可立即发送，即便缓冲区空间没有满；但是，如果你想减少请求的数量，可以设置linger.ms大于0.这将指示生产者发送请求之前等待一段时间
	//希望更多的消息补填到未满的批中。这类似于tcp的算法，例如上面的代码段，可能100条消息在一个请求发送，因为我们设置了linger时间为1ms，然后，如果我们
	//没有填满缓冲区，这个设置将增加1ms的延迟请求以等待更多的消息。需要注意的是，在高负载下，相近的时间一般也会组成批，即使是linger.ms=0。
	//不处于高负载的情况下，如果设置比0大，以少量的延迟代价换取更少的，更有效的请求。
	props.put("linger.ms", 1);
	//buffer.memory控制生产者可用的缓存总量，如果消息发送速度比其传输到服务器的快，将会耗尽这个缓存空间。当缓存空间耗尽，其他发送调用将被阻塞，阻塞时间的阈值
	//通过max.block.ms设定，之后他将抛出一个TimeoutExecption。
	props.put("buffer.memory", 33554432);
	//key.serializer和value.serializer示例：将用户提供的key和value对象ProducerRecord转换成字节，你可以使用附带的ByteArraySerizlizaer或StringSerializer处理简单的byte和String类型.
	props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	//设置kafka的分区数量
	props.put("kafka.partitions", 12);
	
	Producer<String, String> producer = new KafkaProducer<>(props);
	for (int i = 51; i < 100; i++){
		//System.out.println("key-->key"+i+"  value-->vvv"+i);
		JSONObject item=new JSONObject();
		item.put("id", i);
		item.put("col1", "a"+i);
		item.put("col2", "b"+i);
		FlinkMessage msg=new FlinkMessage();
		msg.setOp("c");
		msg.setAfter(JSON.parseObject(JSON.toJSONString(item)));
		String str=JSON.toJSONString(msg);
		System.out.println("key-->key"+i+"  value-->vvv"+str);
		producer.send(new ProducerRecord<String, String>(topic, "key"+i, str));
		Thread.sleep(1000);
	}
		  
	producer.close();
    }
    

    public void testKafkaProductTestHealthTb001() throws InterruptedException
    {
    	Properties props = new Properties();
	//kafka服务器地址
	props.put("bootstrap.servers", serverId);
	//ack是判断请求是否为完整的条件（即判断是否成功发送）。all将会阻塞消息，这种设置性能最低，但是最可靠。
	props.put("acks", "1");
	//retries,如果请求失败，生产者会自动重试，我们指定是0次，如果启用重试，则会有重复消息的可能性。
	props.put("retries", 0);
	//producer缓存每个分区未发送消息，缓存的大小是通过batch.size()配置设定的。值较大的话将会产生更大的批。并需要更多的内存(因为每个“活跃”的分区都有一个缓冲区)
	props.put("batch.size", 16384);
	//默认缓冲区可立即发送，即便缓冲区空间没有满；但是，如果你想减少请求的数量，可以设置linger.ms大于0.这将指示生产者发送请求之前等待一段时间
	//希望更多的消息补填到未满的批中。这类似于tcp的算法，例如上面的代码段，可能100条消息在一个请求发送，因为我们设置了linger时间为1ms，然后，如果我们
	//没有填满缓冲区，这个设置将增加1ms的延迟请求以等待更多的消息。需要注意的是，在高负载下，相近的时间一般也会组成批，即使是linger.ms=0。
	//不处于高负载的情况下，如果设置比0大，以少量的延迟代价换取更少的，更有效的请求。
	props.put("linger.ms", 1);
	//buffer.memory控制生产者可用的缓存总量，如果消息发送速度比其传输到服务器的快，将会耗尽这个缓存空间。当缓存空间耗尽，其他发送调用将被阻塞，阻塞时间的阈值
	//通过max.block.ms设定，之后他将抛出一个TimeoutExecption。
	props.put("buffer.memory", 33554432);
	//key.serializer和value.serializer示例：将用户提供的key和value对象ProducerRecord转换成字节，你可以使用附带的ByteArraySerizlizaer或StringSerializer处理简单的byte和String类型.
	props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	//设置kafka的分区数量
	props.put("kafka.partitions", 12);
	
	Producer<String, String> producer = new KafkaProducer<>(props);
	for (int i = 51; i < 62; i++){
//		//System.out.println("key-->key"+i+"  value-->vvv"+i);
//		JSONObject item=new JSONObject();
//		item.put("id", i);
//		item.put("col1", "a"+i);
//		item.put("col2", "b"+i);
//		FlinkMessage msg=new FlinkMessage();
//		msg.setOp("c");
//		msg.setAfter(JSON.parseObject(JSON.toJSONString(item)));
//		String str=JSON.toJSONString(msg);
		String str="{\"after\":{\"week\":39,\"clientId\":1,\"level\":2,\"year\":2022,\"measurementTime\":1663859520000,\"userNo\":1,\"deviceKey\":\"8F082A000D0F\",\"diastolicPressure\":77,\"measurementDate\":1663859520000,\"userId\":108306,\"month\":9,\"heartRate\":76,\"movementError\":0,\"deviceKeyType\":\"mac\",\"day\":22,\"systolicPressure\":111,\"hourPeriod\":3,\"associatedUserId\":108306},\"op\":\"c\",\"ts_ms\":1666075359379}";
		System.out.println("key-->key"+i+"  value-->vvv"+str);
		producer.send(new ProducerRecord<String, String>(topic, "key"+i, str));
		Thread.sleep(1000);
	}
		  
	producer.close();
    }

    public void testKafkaProductTestHealthTb002() throws InterruptedException
    {
    	Properties props = new Properties();
	//kafka服务器地址
	props.put("bootstrap.servers", serverId);
	//ack是判断请求是否为完整的条件（即判断是否成功发送）。all将会阻塞消息，这种设置性能最低，但是最可靠。
	props.put("acks", "1");
	//retries,如果请求失败，生产者会自动重试，我们指定是0次，如果启用重试，则会有重复消息的可能性。
	props.put("retries", 0);
	//producer缓存每个分区未发送消息，缓存的大小是通过batch.size()配置设定的。值较大的话将会产生更大的批。并需要更多的内存(因为每个“活跃”的分区都有一个缓冲区)
	props.put("batch.size", 16384);
	//默认缓冲区可立即发送，即便缓冲区空间没有满；但是，如果你想减少请求的数量，可以设置linger.ms大于0.这将指示生产者发送请求之前等待一段时间
	//希望更多的消息补填到未满的批中。这类似于tcp的算法，例如上面的代码段，可能100条消息在一个请求发送，因为我们设置了linger时间为1ms，然后，如果我们
	//没有填满缓冲区，这个设置将增加1ms的延迟请求以等待更多的消息。需要注意的是，在高负载下，相近的时间一般也会组成批，即使是linger.ms=0。
	//不处于高负载的情况下，如果设置比0大，以少量的延迟代价换取更少的，更有效的请求。
	props.put("linger.ms", 1);
	//buffer.memory控制生产者可用的缓存总量，如果消息发送速度比其传输到服务器的快，将会耗尽这个缓存空间。当缓存空间耗尽，其他发送调用将被阻塞，阻塞时间的阈值
	//通过max.block.ms设定，之后他将抛出一个TimeoutExecption。
	props.put("buffer.memory", 33554432);
	//key.serializer和value.serializer示例：将用户提供的key和value对象ProducerRecord转换成字节，你可以使用附带的ByteArraySerizlizaer或StringSerializer处理简单的byte和String类型.
	props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	//设置kafka的分区数量
	props.put("kafka.partitions", 12);
	
	Producer<String, String> producer = new KafkaProducer<>(props);
	SGDate date=new SGDate(2022,1,1,0,0,0);
	SGDate date2=new SGDate(2022,2,1,0,0,0);
	for (int i = 1; i <= 30; i++){
		//System.out.println("key-->key"+i+"  value-->vvv"+i);
		JSONObject item=new JSONObject();
		//item.put("id", i);
		item.put("userId", i>10?2222:1111);
		item.put("measurementDate",i>10?date2.toTimestamp(): date.toTimestamp());
		item.put("systolicPressure", i);
		item.put("diastolicPressure", 400);
		item.put("heartRate", i);
		FlinkMessage msg=new FlinkMessage();
		msg.setOp("c");
		msg.setAfter(JSON.parseObject(JSON.toJSONString(item)));
		String str=JSON.toJSONString(msg);
		//String str="{\"after\":{\"week\":39,\"clientId\":1,\"level\":2,\"year\":2022,\"measurementTime\":1663859520000,\"userNo\":1,\"deviceKey\":\"8F082A000D0F\",\"diastolicPressure\":77,\"measurementDate\":1663859520000,\"userId\":108306,\"month\":9,\"heartRate\":76,\"movementError\":0,\"deviceKeyType\":\"mac\",\"day\":22,\"systolicPressure\":111,\"hourPeriod\":3,\"associatedUserId\":108306},\"op\":\"c\",\"ts_ms\":1666075359379}";
		System.out.println("key-->key"+i+"  value-->vvv"+str);
		producer.send(new ProducerRecord<String, String>(topic, "key"+i, str));
		//Thread.sleep(1000);
	}
		  
	producer.close();
    }
    

    /**
     * 报错,Can't convert value of class java.util.HashMap to class org.apache.kafka.common.serialization.StringSerializer specified in value.serializer
     * @throws InterruptedException
     */
    public void testKafkaProductDate() throws InterruptedException
    {
    	Properties props = new Properties();
	//kafka服务器地址
	props.put("bootstrap.servers", serverId);
	//ack是判断请求是否为完整的条件（即判断是否成功发送）。all将会阻塞消息，这种设置性能最低，但是最可靠。
	props.put("acks", "1");
	//retries,如果请求失败，生产者会自动重试，我们指定是0次，如果启用重试，则会有重复消息的可能性。
	props.put("retries", 0);
	//producer缓存每个分区未发送消息，缓存的大小是通过batch.size()配置设定的。值较大的话将会产生更大的批。并需要更多的内存(因为每个“活跃”的分区都有一个缓冲区)
	props.put("batch.size", 16384);
	//默认缓冲区可立即发送，即便缓冲区空间没有满；但是，如果你想减少请求的数量，可以设置linger.ms大于0.这将指示生产者发送请求之前等待一段时间
	//希望更多的消息补填到未满的批中。这类似于tcp的算法，例如上面的代码段，可能100条消息在一个请求发送，因为我们设置了linger时间为1ms，然后，如果我们
	//没有填满缓冲区，这个设置将增加1ms的延迟请求以等待更多的消息。需要注意的是，在高负载下，相近的时间一般也会组成批，即使是linger.ms=0。
	//不处于高负载的情况下，如果设置比0大，以少量的延迟代价换取更少的，更有效的请求。
	props.put("linger.ms", 1);
	//buffer.memory控制生产者可用的缓存总量，如果消息发送速度比其传输到服务器的快，将会耗尽这个缓存空间。当缓存空间耗尽，其他发送调用将被阻塞，阻塞时间的阈值
	//通过max.block.ms设定，之后他将抛出一个TimeoutExecption。
	props.put("buffer.memory", 33554432);
	//key.serializer和value.serializer示例：将用户提供的key和value对象ProducerRecord转换成字节，你可以使用附带的ByteArraySerizlizaer或StringSerializer处理简单的byte和String类型.
	props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	//设置kafka的分区数量
	props.put("kafka.partitions", 12);
	
	Producer<String, Object> producer = new KafkaProducer<>(props);
	SGDate date=new SGDate(2022,2,2,0,0,0);
	for (int i = 0; i <= 50; i++){
		Map<String, Object> item=new HashMap<String,Object>();
		item.put("id", i);
		item.put("date",date );
//		FlinkMessage msg=new FlinkMessage();
//		msg.setOp("c");
//		msg.setAfter(JSON.parseObject(JSON.toJSONString(item)));
		String str=JSON.toJSONString(item);
		System.out.println("key-->key"+i+"  value-->vvv"+str);
		producer.send(new ProducerRecord<String, Object>(topic, "key"+i, item));
		Thread.sleep(1000);
	}
		  
	producer.close();

    }
}
