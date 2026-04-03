package org.sellgirl.sellgirlPayWeb.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.validator.internal.util.stereotypes.ThreadSafe;
import org.sellgirl.sellgirlPayWeb.controller.model.ConcurrentSftpUpload;
import org.sellgirl.sellgirlPayWeb.controller.model.ConcurrentSftpUpload2;
import org.sellgirl.sellgirlPayWeb.controller.model.JdbcHelperTest;
//import org.sellgirl.sellgirlPayWeb.controller.model.SftpUpload2;
import org.sellgirl.sellgirlPayWeb.controller.model.TestModel001;

import com.alibaba.fastjson.JSON;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.sellgirl.sellgirlPayWeb.configuration.PFConfigMapper;
import com.sellgirl.sellgirlPayWeb.configuration.jdbc.JdbcHelper;
import com.sellgirl.sellgirlPayWeb.product.model.ResourceType;
//import com.sellgirl.sellgirlPayWeb.product.model.resourceChapCreate;
import com.sellgirl.sellgirlPayWeb.product.model.resourceCreate;
import com.sellgirl.sellgirlPayWeb.product.service.ResourceService;
import com.sellgirl.sellgirlPayWeb.projHelper.ProjHelper;
import com.sellgirl.sellgirlPayWeb.user.model.UserCreate;
import com.sellgirl.sgHelperExport.SGExcelHelper;
import com.sellgirl.sgJavaMvcHelper.HtmlHelper;
import com.sellgirl.sgJavaMvcHelper.PFGrid;

import junit.framework.TestCase;

import com.sellgirl.sgJavaHelper.DirectNode;
import com.sellgirl.sgJavaHelper.PFCmonth;
import com.sellgirl.sgJavaHelper.PFDataRow;
import com.sellgirl.sgJavaHelper.SGDataTable;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGHttpHelper;
import com.sellgirl.sgJavaHelper.PFFunc3;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.SGRequestResult;
import com.sellgirl.sgJavaHelper.SGSpeedCounter;
import com.sellgirl.sgJavaHelper.SGSqlCommandString;
import com.sellgirl.sgJavaHelper.PFSqlCommandTimeoutSecond;
import com.sellgirl.sgJavaHelper.SGSqlFieldInfo;
import com.sellgirl.sgJavaHelper.config.PFAppConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.config.SGDataHelper.LocalDataType;
import com.sellgirl.sgJavaHelper.file.SGDirectory;
import com.sellgirl.sgJavaHelper.file.SGPath;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlInsertCollection;
import com.sellgirl.sgJavaHelper.sql.SGMySqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlCreateTableCollection;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecuteBase;
import com.sellgirl.sgJavaHelper.sql.SqlCreateTableItem;
import com.sellgirl.sgJavaHelper.sql.SqlUpdateItem;
import com.sellgirl.sgJavaHelper.time.SGWaiter;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;

@SuppressWarnings("unused")
public class UncheckUpload extends TestCase {
	public static void initPFHelper() {
		//PFDataHelper.SetConfigMapper(new PFConfigTestMapper());		
		SGDataHelper.SetConfigMapper(new PFConfigMapper());		
		new SGDataHelper(new PFAppConfig());
	}

	private boolean clear=true;
	private boolean printBug=true;
	private boolean printProgress=true;
	private int maxLen=45110;//暂时的阀值
	
	/**
	 * 从爬取资源导入mysql
	 */
	public void testUpload() {
//		SftpUpload2.main(null);
		try {
//			ConcurrentSftpUpload.main(null);
			ResourceType type=ResourceType.movie;
			String[] path=new String[] {
				"D:\\github\\PaySellgirl2\\sellgirlSpringBootFather\\sellgirlPayWeb\\src\\main\\resources\\static\\resourceImg\\comic120",
				"D:\\github\\PaySellgirl2\\sellgirlSpringBootFather\\sellgirlPayWeb\\src\\main\\resources\\static\\resourceImg\\comic200",
				"D:\\github\\PaySellgirl2\\sellgirlSpringBootFather\\sellgirlPayWeb\\src\\main\\resources\\static\\resourceImg\\image120",
				"D:\\github\\PaySellgirl2\\sellgirlSpringBootFather\\sellgirlPayWeb\\src\\main\\resources\\static\\resourceImg\\image200",
				"D:\\github\\PaySellgirl2\\sellgirlSpringBootFather\\sellgirlPayWeb\\src\\main\\resources\\static\\resourceImg\\movie120",
				"D:\\github\\PaySellgirl2\\sellgirlSpringBootFather\\sellgirlPayWeb\\src\\main\\resources\\static\\resourceImg\\movie200",	
			};
			String[] path2=new String[] {
				"/root/myapp/shop/static/resourceImg/comic120",
				"/root/myapp/shop/static/resourceImg/comic200",
				"/root/myapp/shop/static/resourceImg/image120",
				"/root/myapp/shop/static/resourceImg/image200",
				"/root/myapp/shop/static/resourceImg/movie120",
				"/root/myapp/shop/static/resourceImg/movie200",
			};
			for(int i=0;path.length>i;i++) {

//				ConcurrentSftpUpload2.LOCAL_ROOT = path[i];    //全部上传完成，总耗时 167 秒 
//				ConcurrentSftpUpload2.REMOTE_ROOT =path2[i]; //
				(new ConcurrentSftpUpload2()).upload(path[i], path2[i]);
			}
//			ConcurrentSftpUpload2.LOCAL_ROOT = "D:\\cache\\html1\\shop\\static\\resourceImg\\"+type+"60";    //全部上传完成，总耗时 167 秒 
//			ConcurrentSftpUpload2.REMOTE_ROOT = "/root/myapp/shop/static/resourceImg/"+type+"60"; // 
//			ConcurrentSftpUpload2.main(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static final String TAG="ConcurrentSftpUpload";
    // 配置参数
    private static final String HOST ="156.224.19.162";// "你的服务器IP";
    private static final int PORT = 22;
    private static final String USER ="root";// "ubuntu";
//    private static final String SSH="C:\\Users\\Administrator\\.ssh\\id_rsa.pub";
  //"C:/Users/Administrator/.ssh/id_rsa";
    private static final String SSH=null;//"C:\\Users\\Administrator\\.ssh\\id_rsa";
    private static final String PASSWORD = "";

    //ok
	public void testSSH() {

        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channel = null;

        try {
            // 初始化连接
            session = jsch.getSession(USER, HOST, PORT);
            session.setPassword(PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(30000);      // 连接超时
            session.connect();

            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect(30000);          // 通道超时

            String localFile = "D:\\cache\\html1\\shop\\static\\resourceImg\\comic60\\1\\1.jpg";    //全部上传完成，总耗时 167 秒 
            String remotePath = "/root/download/aaa.jpg"; // 
            try (FileInputStream fis = new FileInputStream(localFile)) {
                channel.put(fis, remotePath);
            }
        }catch(Exception e) {
        	e.printStackTrace();
        }
	}
	public void testSSH2() {

        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channel = null;

        try {
        	jsch.addIdentity("C:\\Users\\Administrator\\.ssh\\id_rsa");
            // 初始化连接
            session = jsch.getSession(USER, HOST, PORT);
//            session.setPassword(PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(30000);      // 连接超时
            session.connect();

            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect(30000);          // 通道超时

            String localFile = "D:\\cache\\html1\\shop\\static\\resourceImg\\comic60\\1\\1.jpg";    //全部上传完成，总耗时 167 秒 
            String remotePath = "/root/download/aaa.jpg"; // 
            try (FileInputStream fis = new FileInputStream(localFile)) {
                channel.put(fis, remotePath);
            }
        }catch(Exception e) {
        	e.printStackTrace();
        }
	}

}
