package pf.java.pfHelperExport;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//import org.sgGameHelper.mp3.Mp3Model;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.sellgirl.sgHelperExport.Exporter;
import com.sellgirl.sgHelperExport.ExporterOption;
import com.sellgirl.sgJavaHelper.PFDataColumn;
import com.sellgirl.sgJavaHelper.PFDataRow;
import com.sellgirl.sgJavaHelper.SGDataTable;
import com.sellgirl.sgJavaHelper.file.SGDirectory;
import com.sellgirl.sgJavaHelper.PagingParameters;
import com.sellgirl.sgJavaHelper.PagingResult;
import com.sellgirl.sgJavaHelper.StoreColumnCollection;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/**
 * Unit test for simple App.
 * 
 * 见PFExcelHelper.exportTableToExcel
 */
public class UncheckExport 
    extends TestCase
{
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
}
