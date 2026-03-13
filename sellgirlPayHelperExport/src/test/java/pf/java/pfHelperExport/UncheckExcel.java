package pf.java.pfHelperExport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import junit.framework.TestCase;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgHelperExport.Exporter;
import com.sellgirl.sgHelperExport.ExporterOption;
import com.sellgirl.sgHelperExport.SGExcelHelper;
import com.sellgirl.sgJavaHelper.PFDataRow;
import com.sellgirl.sgJavaHelper.SGDataTable;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.file.SGDirectory;
import com.sellgirl.sgJavaHelper.PFHiveSqlCreateTableCollection;
import com.sellgirl.sgJavaHelper.PFModelConfigCollection;
import com.sellgirl.sgJavaHelper.SGSqlCommandString;
import com.sellgirl.sgJavaHelper.sql.SGSqlCreateTableCollection;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;
import com.sellgirl.sgJavaHelper.time.SGTimeSpan;
import com.sellgirl.sgJavaHelper.PagingParameters;
import com.sellgirl.sgJavaHelper.PagingResult;
import com.sellgirl.sgJavaHelper.sql.SqlCreateTableItem;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import pf.java.pfHelperExport.model.DayJdbc;

public class UncheckExcel extends TestCase{

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public UncheckExcel( String testName )
    {
        super( testName );
    }

    public void testFilterExcel() {
    	String[] word=new String[] {"软件"};
    	String[] title=new String[] {"岗位名称"};
		Workbook wb1;
		try {
			String srcPath="D:\\download\\2026年春季大型综合招聘会岗位.xlsx";
			String dstPath="D:\\download\\2026年春季大型综合招聘会岗位3.xlsx";
			wb1 = SGExcelHelper.create(new FileInputStream(new File(srcPath)));
			List<Map<String, Object>> list1=SGExcelHelper.ExcelToDictList(wb1);
			for(//Map<String, Object> i:list1
					int idx=list1.size()-1;0<=idx;idx--
					) {
				Map<String, Object> i=list1.get(idx);
//				for(Entry<String,Object> j:i.entrySet()
//						) {
//					if(title.equals(j.getKey())) {
//						for(String k:)
//						SGDataHelper.ObjectToString(j.getValue()).indexOf(0) 
//					}	
//				}
				boolean match=false;
				for(//Entry<String,Object> j:i.entrySet()
						String j:title
						) {
					for(String k:word) {
						if(0<=SGDataHelper.ObjectToString(i.get(j)).indexOf(k)) {
							match=true;
							break;
						}
					}
					if(match) {
						break;
					}
				}
				if(!match) {
					list1.remove(idx);
				}
			}
			SGExcelHelper.exportTableToExcel(SGDataHelper.DictListToDataTable(list1),dstPath);
		} catch (InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
    }

	public static List<Map<String, Object>> ExcelToDictList(String path){
		try {
			Workbook wb1;
			//wb1 = new XSSFWorkbook(new FileInputStream(new File(path)));
			wb1 =SGExcelHelper.create(new FileInputStream(new File(path)));
			List<Map<String, Object>> list1=SGExcelHelper.ExcelToDictList(wb1);
			return list1;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
