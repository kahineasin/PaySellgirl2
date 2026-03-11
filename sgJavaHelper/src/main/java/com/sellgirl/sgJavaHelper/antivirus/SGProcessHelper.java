package com.sellgirl.sgJavaHelper.antivirus;

import java.util.ArrayList;
import java.util.List;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class SGProcessHelper {
	private static ISGProcessManager manager=null;
	public static void setManager(ISGProcessManager manager) {
		SGProcessHelper.manager=manager;
	}
	public static ISGProcessManager getManger() {
		return manager;
	}
	
}
