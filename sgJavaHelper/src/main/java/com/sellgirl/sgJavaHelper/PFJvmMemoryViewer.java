package com.sellgirl.sgJavaHelper;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.model.FileSizeUnitType;
import com.sellgirl.sgJavaHelper.sgEnum.SGFlagEnum;

public class PFJvmMemoryViewer {
	public String getMemoryInfo() {

		SGFlagEnum<FileSizeUnitType> unit = new SGFlagEnum<FileSizeUnitType>(FileSizeUnitType.GB)
				.or(FileSizeUnitType.MB).or(FileSizeUnitType.KB);
		return SGDataHelper.FormatString("JVM中的空闲内存量{0} JVM内存总量{1} JVM试图使用额最大内存量{2} 可用处理器的数目{3}",
				SGDataHelper.formatFileSize(Runtime.getRuntime().freeMemory()/1024, unit),
				SGDataHelper.formatFileSize(Runtime.getRuntime().totalMemory()/1024, unit),
				SGDataHelper.formatFileSize(Runtime.getRuntime().maxMemory()/1024, unit),
				Runtime.getRuntime().availableProcessors());
	}
}
