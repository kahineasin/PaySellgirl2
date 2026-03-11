package pf.java.pfHelperExport;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.sellgirl.sgHelperExport.Exporter;
import com.sellgirl.sgHelperExport.ExporterOption;
import com.sellgirl.sgJavaHelper.PFDataColumn;
import com.sellgirl.sgJavaHelper.PFDataRow;
import com.sellgirl.sgJavaHelper.SGDataTable;
import com.sellgirl.sgJavaHelper.PagingParameters;
import com.sellgirl.sgJavaHelper.PagingResult;
import com.sellgirl.sgJavaHelper.StoreColumnCollection;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.file.SGDirectory;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    public void testExport()
    {
    	SGDataTable dt=GetDemoTable();
    	PagingResult pagingResult = SGDataHelper.PagingStore(dt, new PagingParameters (){ },
                null,
                false, null);
    	Exporter exporter = Exporter.Instance(pagingResult, new ExporterOption()
//            {
//                FileType = "xlsNoMulti",
//                Scheme = Exporter.FinancialScheme,
//                SheetTitle = GetWordCMonth(cmonthff)+hr+fgsname
//            }
    	).FileName("总表");//这里的下载名没用到

		String server = SGDataHelper.GetBaseDirectory();// AppDomain.CurrentDomain.BaseDirectory;

		Path pDirPath = Paths.get(server, "log");
		String dirPath = pDirPath.toString();
		String logPath = Paths
				.get(dirPath, "testExportExcel.xlsx")
				.toString();
		SGDirectory.EnsureFilePath(logPath);
    	OutputStream stream;
		try {
			stream = new FileOutputStream(logPath, true);
	    	exporter.WriteToStream(stream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        assertTrue( true );
    }
    public void testExportWithColumn()
    {
    	//PFDataHelper.SetConfigMapper(new TestExportConfigMapper());
    	SGDataTable dt=GetDemoTable();
        StoreColumnCollection columns = null;
        columns=new StoreColumnCollection("yjquery") {
        	/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
        		Add("父1",a->a.SetChildren(
new StoreColumnCollection("yjquery") {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

{
	Add("0");
	Add("1");
}}
        				));
        		Add("2");
        	}
        };
        PagingResult pagingResult = SGDataHelper.PagingStore(dt, new PagingParameters (){ },
        		columns,
                false, null);
    	Exporter exporter = Exporter.Instance(pagingResult, new ExporterOption()
//            {
//                FileType = "xlsNoMulti",
//                Scheme = Exporter.FinancialScheme,
//                SheetTitle = GetWordCMonth(cmonthff)+hr+fgsname
//            }
    	).FileName("总表");//这里的下载名没用到

		String server = SGDataHelper.GetBaseDirectory();// AppDomain.CurrentDomain.BaseDirectory;

		Path pDirPath = Paths.get(server, "log");
		String dirPath = pDirPath.toString();
		String logPath = Paths
				.get(dirPath, "testExportExcel.xlsx")
				.toString();
		SGDirectory.EnsureFilePath(logPath);
    	OutputStream stream;
		try {
			stream = new FileOutputStream(logPath);
	    	exporter.WriteToStream(stream);
	    	stream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        assertTrue( true );
    }
    private static SGDataTable GetDemoTable() {

        SGDataTable t = null;
        List<PFDataRow> row = new ArrayList<PFDataRow>();// 表所有行集合
        List<PFDataColumn> col = null;// 行所有列集合
        PFDataRow r = null; // 单独一行
        PFDataColumn c = null;// 单独一列
        // 此处开始循环读数据，每次往表格中插入一行记录
        for (int i=0;i<10;i++) {
            col = new ArrayList<PFDataColumn>();
        	for (int j=0;j<3;j++) {
                String columnName = String.valueOf(j);
                Object value =i*j;
                // 初始化单元列
                c = new PFDataColumn(columnName, value);
                // 将列信息加入列集合
                col.add(c);
        	}
            // 初始化单元行
            r = new PFDataRow(col);
            // 将行信息降入行结合
            row.add(r);

        }
        // 得到数据表
        t = new SGDataTable(row);
        return t;
    }
}
