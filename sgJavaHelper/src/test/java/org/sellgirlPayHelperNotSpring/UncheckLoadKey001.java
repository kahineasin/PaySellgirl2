package org.sellgirlPayHelperNotSpring;

//import org.ho.yaml.Yaml;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sellgirl.sgJavaHelper.AES;
import com.sellgirl.sgJavaHelper.DirectNode;
import com.sellgirl.sgJavaHelper.IPFSqlFieldTypeConverter;
import com.sellgirl.sgJavaHelper.SGAction;
import com.sellgirl.sgJavaHelper.PFBatchHelper;
import com.sellgirl.sgJavaHelper.SGDataTable;
import com.sellgirl.sgJavaHelper.PFEnumClass;
import com.sellgirl.sgJavaHelper.PFHiveSqlCreateTableCollection;
import com.sellgirl.sgJavaHelper.PFKeyValueCollection;
import com.sellgirl.sgJavaHelper.PFLine;
import com.sellgirl.sgJavaHelper.PFModelConfig;
import com.sellgirl.sgJavaHelper.PFModelConfigCollection;
import com.sellgirl.sgJavaHelper.PFPoint;
import com.sellgirl.sgJavaHelper.SGRequestResult;
import com.sellgirl.sgJavaHelper.SGSpeedCounter;
import com.sellgirl.sgJavaHelper.SGSqlCommandString;
import com.sellgirl.sgJavaHelper.PFSqlCommandTimeoutSecond;
import com.sellgirl.sgJavaHelper.SGSqlFieldInfo;
import com.sellgirl.sgJavaHelper.PFSqlFieldType;
import com.sellgirl.sgJavaHelper.SGSqlFieldTypeEnum;
import com.sellgirl.sgJavaHelper.SGYamlHelper;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGDateRange;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.SuccessApiResult;
import com.sellgirl.sgJavaHelper.UnicodeReader;
import com.sellgirl.sgJavaHelper.config.PFAppConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.config.SGDataHelper.LocalDataType;
import com.sellgirl.sgJavaHelper.file.SGDirectory;
import com.sellgirl.sgJavaHelper.file.SGEncryptByte;
import com.sellgirl.sgJavaHelper.model.FileSizeUnitType;
import com.sellgirl.sgJavaHelper.model.SysType;
import com.sellgirl.sgJavaHelper.model.UserOrg;
import com.sellgirl.sgJavaHelper.model.UserTypeClass;
import com.sellgirl.sgJavaHelper.sgEnum.IPFEnum;
import com.sellgirl.sgJavaHelper.sgEnum.SGFlagEnum;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlCreateTableCollection;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;
import com.sellgirl.sgJavaHelper.sql.PFSqlInsertCollection;
import com.sellgirl.sgJavaHelper.sql.SGSqlTransferItem;
import com.sellgirl.sgJavaHelper.sql.PFTidbSqlExecute;
import com.sellgirl.sgJavaHelper.sql.SqlCreateTableItem;
import com.sellgirl.sgJavaHelper.sql.SqlUpdateItem;
import com.sellgirl.sgJavaHelper.task.IPFTask;
import com.sellgirl.sgJavaHelper.task.PFDayTask;
import com.sellgirl.sgJavaHelper.task.PFDayTask2;
import com.sellgirl.sgJavaHelper.task.PFIntervalExactTask;
import com.sellgirl.sgJavaHelper.task.PFIntervalExactTask2;
import com.sellgirl.sgJavaHelper.task.PFIntervalTask;
import com.sellgirl.sgJavaHelper.task.PFMonthTask;
import com.sellgirl.sgJavaHelper.task.PFMonthTask2;
import com.sellgirl.sgJavaHelper.task.PFTimeTaskBase2.TimePoint;
import com.sellgirl.sgJavaHelper.time.SGTimeSpan;

import junit.framework.TestCase;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
//import org.ho.yaml.Yaml;
import org.sellgirlPayHelperNotSpring.model.AppKey;
import org.sellgirlPayHelperNotSpring.model.ByteUtil;
import org.sellgirlPayHelperNotSpring.model.DataModelInfo;
import org.sellgirlPayHelperNotSpring.model.DefaultListDataSource;
import org.sellgirlPayHelperNotSpring.model.DefaultListDataSource2;
import org.sellgirlPayHelperNotSpring.model.HealthOrdersApiResult;
import org.sellgirlPayHelperNotSpring.model.JdbcHelperTest;
import org.sellgirlPayHelperNotSpring.model.PFConfigTestMapper;
import org.sellgirlPayHelperNotSpring.model.SellGirlProduct;
import org.sellgirlPayHelperNotSpring.model.TaskTest001;
import org.sellgirlPayHelperNotSpring.model.TaskTest002;
import org.sellgirlPayHelperNotSpring.model.TestEnum001;
import org.sellgirlPayHelperNotSpring.model.TestSerialModel;
import org.sellgirlPayHelperNotSpring.model.TestSerialModel2;
import org.sellgirlPayHelperNotSpring.model.TestSerialModel3;
import org.sellgirlPayHelperNotSpring.model.TestSerialModel4;
import org.sellgirlPayHelperNotSpring.model.TestSerialModel5;
import org.sellgirlPayHelperNotSpring.model.TransferTaskTest001;
import org.sellgirlPayHelperNotSpring.model.TransferTaskTest002;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.yaml.snakeyaml.Yaml;

import javax.imageio.ImageIO;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

/**
 * 不要随便更改此类名,以防打包时执行了此类
 * 
 * @author Administrator
 *
 */
@SuppressWarnings(value = { "unused", "rawtypes", "serial", "deprecation" })
public class UncheckLoadKey001 extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(UncheckLoadKey001.class);

	private  void initPFHelper() {
		Uncheck001.initPFHelper();
		//key=PFDataHelper.ReadFileToString("D:\\github\\1\\2\\a.txt");
	}
    /**
     * 如果想加载密码可以参考下面这样.
     * @throws Exception 
	 */
	public void testAppKey() throws Exception {
		Uncheck001.initPFHelper();
		String filePath="D:\\gitee\\secretKey\\paySellgirl\\key.yml";
		File dumpFile = new File(filePath);
		
		//AppKey appKey = Yaml.loadType(dumpFile, AppKey.class);
//		AppKey appKey =SGYamlHelper.toObject( SGDataHelper.ReadFileToString(filePath), null);
		AppKey appKey =SGYamlHelper.loadTypeIgnoreUnknown(filePath, AppKey.class);
		String r=SGDataHelper.encodeBase64(appKey.getEmailPwd());
		String r2=SGDataHelper.decodeBase64(r);
		System.out.println(SGDataHelper.encodeBase64(""));
		System.out.println(r2);
		System.out.println(appKey.getEmailPwd());
	}
	public void testEncodeKey() throws FileNotFoundException {
		Uncheck001.initPFHelper();
		System.out.println(SGDataHelper.encodeBase64(""));
	}
	/**
	 * QXhhUER6ZzRlYU9oOUp3ag==
AxaPDzg4eaOh9Jwj
	 * @throws Exception 
	 */
	public void testDecodeKey() throws Exception {
		Uncheck001.initPFHelper();
//		File dumpFile = new File("D:\\gitee\\secretKey\\paySellgirl\\key.yml");
//		AppKey appKey = Yaml.loadType(dumpFile, AppKey.class);
		AppKey appKey = SGYamlHelper.loadType("D:\\gitee\\secretKey\\paySellgirl\\key.yml", AppKey.class);
		
		System.out.println(appKey.getEmailPwd());
		System.out.println(AES.AESDecryptDemo(appKey.getEmailPwd(),getKey()));
	}

	//明文//秘钥(需要使用长度为16、24或32的字节数组作为AES算法的密钥，否则就会遇到java.security.InvalidKeyException异常)
	public static String text="";    
	//private String key="";
	//private String key2="";
    public void testAESEncrypt() throws Exception {

        //加密，生成密文
        String base64Encrypted = AES.AESEncryptDemo(text,getKey());
 
        System.out.println(base64Encrypted);
        //解密，获取明文
        String text2 =  AES.AESDecryptDemo(base64Encrypted,getKey());
 
        System.out.println(text2);
    }
    public void testAESDecrypt() throws Exception {
        //解密，获取明文
        System.out.println(AES.AESDecryptDemo(text,getKey()));
    }
    public void testAESDecrypt2() throws Exception {
       //System.out.println(	getKey());
//		File dumpFile = new File("D:\\gitee\\secretKey\\paySellgirl\\key.yml");
//		Map<String, Object> appKey = (Map<String, Object>)Yaml.load(dumpFile);
		Map<String, String> appKey = SGYamlHelper.loadMap("D:\\gitee\\secretKey\\paySellgirl\\key.yml");

        //解密，获取明文
        //System.out.println(AES.AESDecryptDemo(appKey.get("").toString(),key));
        for(String k:appKey.keySet()) {
    		//if(!"san".equals(k)) {

    			try {

                	String s1=AES.AESDecryptDemo(appKey.get(k).toString(),getKey());
                    System.out.println(k+"  "+s1);
//                	String s2=AES.AESEncryptDemo(s1,key2);
//                    System.out.println(k+"  "+s2);
    			}catch(Exception e) {
                    System.out.println(k+" error "+appKey.get(k).toString());
    			}
    		//}
        }
    }
	private static int dataOfFile = 0; // 文件字节内容
    public static String getKey() throws IOException {

    	File encFile = new File("d:\\github\\1\\2\\b.jpg");
		FileInputStream fis = new FileInputStream(encFile);
		return SGEncryptByte.DecryptByteToString(fis, SGEncryptByte.DEFAULT_BUFFER_SIZE, 0x123456);

    }

}
