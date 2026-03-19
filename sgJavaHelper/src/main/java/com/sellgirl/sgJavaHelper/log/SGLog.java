package com.sellgirl.sgJavaHelper.log;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.file.SGDirectory;

public class SGLog implements ISGLog{

	@Override
	public void writeException(Throwable e, String filePrev) {
		this.write(e, filePrev);
	}

	@Override
	public void write(Object e, String filePrev) {
		String server = SGDataHelper.GetBaseDirectory();// AppDomain.CurrentDomain.BaseDirectory;

		Path pDirPath = Paths.get(server, "log");
		String dirPath = pDirPath.toString();
		String logPath = Paths
				.get(dirPath, SGDataHelper.FormatString("{0}_{1}.txt", filePrev == null ? "pfError" : filePrev,
						SGDataHelper.ObjectToDateString(Calendar.getInstance(), "yyyyMMdd")))
				.toString();

		SGDirectory.EnsureExists(dirPath);


		boolean isException=e instanceof Exception;
		String eFull="";
		// TODO Auto-generated method stub
		try (FileOutputStream fos = new FileOutputStream(logPath, true);
		// OutputStreamWriter osw=new OutputStreamWriter(fos);
		OutputStreamWriter osw = new OutputStreamWriter(fos, SGDataHelper.encoding);
		BufferedWriter out = new BufferedWriter(osw);) {
			if(isException) {
				eFull=SGDataHelper.getErrorFullString((Exception)e);
			}
	String msg = SGDataHelper.FormatString("\r\ntime:[{0}]\r\n{1}\r\n", SGDate.Now().toString(),isException?eFull: e);
	out.write(msg);
	out.close();
	if (isException) {
		//LOGGER.error(msg,e);
		System.err.print(eFull);
	} else if ("pfError".equals(filePrev)) {
//		LOGGER.error(msg);
		System.err.print(msg);
		// System.err.println(msg);
	} else {
		//LOGGER.info(msg);
		System.out.print(msg);
	}
} catch (Exception e2) {
	e2.printStackTrace();
}
	}

	@Override
	public void printException(Throwable e, String tag) {
		if(null==tag) {
			System.err.println(SGDataHelper.getErrorFullString(e));
		}else {
			System.err.println(tag+"\r\n"+SGDataHelper.getErrorFullString(e));
		}
		
	}
	@Override
	public void print(Object e) {
//		System.out.println(e.toString());
		System.out.println(e);
	}

}
