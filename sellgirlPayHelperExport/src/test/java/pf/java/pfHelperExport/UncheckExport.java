//package pf.java.pfHelperExport;
//
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.OutputStream;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.sgGameHelper.mp3.Mp3Model;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//
//import com.sellgirl.sgHelperExport.Exporter;
//import com.sellgirl.sgHelperExport.ExporterOption;
//import com.sellgirl.sgJavaHelper.PFDataColumn;
//import com.sellgirl.sgJavaHelper.PFDataRow;
//import com.sellgirl.sgJavaHelper.PFDataTable;
//import com.sellgirl.sgJavaHelper.SGDirectory;
//import com.sellgirl.sgJavaHelper.PagingParameters;
//import com.sellgirl.sgJavaHelper.PagingResult;
//import com.sellgirl.sgJavaHelper.StoreColumnCollection;
//import com.sellgirl.sgJavaHelper.config.SGDataHelper;
//
///**
// * Unit test for simple App.
// */
//public class UncheckExport 
//    extends TestCase
//{
//    public static ArrayList<Mp3Model> getSongModels(){
//    	ArrayList<Mp3Model> r=new ArrayList<Mp3Model>();
//    	Mp3Model mp3=new Mp3Model();
//    	mp3.setFileName("001.电灯胆_邓丽欣.mp3");
//		mp3.setNoSound(true);
//    	r.add(mp3);    	
//    	mp3=new Mp3Model();
//    	mp3.setFileName("002.电灯胆_Benjamin原声.mp3");
//		mp3.setNoSound(true);
//    	r.add(mp3);
//    	mp3=new Mp3Model();
//    	mp3.setFileName("003.电灯胆_邓丽欣_伴奏.mp3");
//		mp3.setNoSound(false);
//    	r.add(mp3);
//    	mp3=new Mp3Model();
//    	mp3.setFileName("004.Lady of the Pier(Du Mei Shin).mp3");
//    	mp3.setFastComplete(true);
//    	r.add(mp3);
//    	mp3=new Mp3Model();
//    	mp3.setFileName("005.Lady of the Pier(Du Mei Shin)_ben制作消音伴奏.mp3");
//		mp3.setNoSound(false);
//    	r.add(mp3);
//    	mp3=new Mp3Model();
//    	mp3.setFileName("017.寂寞_谢容儿_伴奏.mp3");
//		mp3.setNoSound(false);
//    	r.add(mp3);
//    	mp3=new Mp3Model();
//    	mp3.setFileName("019.越长大越孤单_伴奏.mp3");
//		mp3.setNoSound(false);
//    	r.add(mp3);
////    	mp3=new Mp3Model();
////    	mp3.setFileName("");
////    	r.add(mp3);
//    	return r;
//    }
//    public void testExport()
//    {
//    	PFDataTable dt=GetDemoTable();
//    	PagingResult pagingResult = SGDataHelper.PagingStore(dt, new PagingParameters (){ },
//                null,
//                false, null);
//    	Exporter exporter = Exporter.Instance(pagingResult, new ExporterOption()
////            {
////                FileType = "xlsNoMulti",
////                Scheme = Exporter.FinancialScheme,
////                SheetTitle = GetWordCMonth(cmonthff)+hr+fgsname
////            }
//    	).FileName("总表");//这里的下载名没用到
//
//		String server = SGDataHelper.GetBaseDirectory();// AppDomain.CurrentDomain.BaseDirectory;
//
//		Path pDirPath = Paths.get(server, "log");
//		String dirPath = pDirPath.toString();
//		String logPath = Paths
//				.get(dirPath, "testExportExcel.xlsx")
//				.toString();
//		SGDirectory.EnsureFilePath(logPath);
//    	OutputStream stream;
//		try {
//			stream = new FileOutputStream(logPath, true);
//	    	exporter.WriteToStream(stream);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        assertTrue( true );
//    }
//}
