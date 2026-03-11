package com.sellgirl.sgJavaMvcHelper;

import java.util.Map;

import com.sellgirl.sgJavaHelper.PagingParameters;
import com.sellgirl.sgJavaMvcHelper.config.PFRequestUtils;

public class MvcPagingParameters extends PagingParameters{
	public MvcPagingParameters() {
		Map<String, String[]> r=PFRequestUtils.getParam();
		if(r!=null) {
			SetJavaRequestData(r);
		}
	}
	public MvcPagingParameters(boolean useParent) {
		super();
	}
}

