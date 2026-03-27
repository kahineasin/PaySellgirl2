package com.sellgirl.sgJavaMvcHelper;

import java.util.Map;

import com.sellgirl.sgJavaHelper.PagingParameters;
import com.sellgirl.sgJavaMvcHelper.config.PFRequestUtils;

/**
 * 使用方法:
 * 1. 放到RestController的post方法的参数中,如 getList(PagingParameters p), 就能自动绑定
 * 2. 前端post({pageindex,pagesize,sort,...})
 */
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

