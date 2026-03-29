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
import org.sellgirl.sellgirlPayWeb.controller.model.JdbcHelperTest;
//import org.sellgirl.sellgirlPayWeb.controller.model.SftpUpload2;
import org.sellgirl.sellgirlPayWeb.controller.model.TestModel001;

import com.alibaba.fastjson.JSON;
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
			ConcurrentSftpUpload.main(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
