package com.sellgirl.sellgirlPayWeb.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
//import com.perfect99.right.amsweb.ActionReturnInfo;
//import com.perfect99.right.amsweb.BaseReturnInfo;

import com.sellgirl.sgJavaHelper.AbstractApiResult;
import com.sellgirl.sgJavaHelper.SGAllowAnonymous;
import com.sellgirl.sgJavaHelper.SGCaching;
import com.sellgirl.sgJavaHelper.PFDataRow;
import com.sellgirl.sgJavaHelper.SGDataTable;
//import com.sellgirl.sgJavaSpringHelper.PFJsonData;
//import com.sellgirl.sgJavaSpringHelper.PFJsonDataT;
import com.sellgirl.sgJavaHelper.PFJsonData;
import com.sellgirl.sgJavaHelper.PFJsonDataT;
import com.sellgirl.sgJavaSpringHelper.PFObject;
//import com.sellgirl.sgJavaSpringHelper.PFObject;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaSpringHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.model.SystemUser;
import com.sellgirl.sgJavaHelper.model.UserOrg;
import com.sellgirl.sgJavaHelper.model.UserTypeClass;
import com.sellgirl.sgJavaMvcHelper.MvcPagingParameters;
import com.sellgirl.sgJavaMvcHelper.PFBaseWebController;
//import pf.springBoot.springBootSSO.controller.shares.YJQueryController;
import com.sellgirl.sellgirlPayWeb.oAuth.FormsAuth;
import com.sellgirl.sellgirlPayWeb.oAuth.LoginerBase;
//import com.sellgirl.sellgirlPayWeb.oAuth.MetabaseDataHelper;
//import pf.springBoot.springBootSSO.oAuth.MetabaseFormsAuth;
//import com.sellgirl.sellgirlPayWeb.oAuth.MetabaseUser;
import com.sellgirl.sellgirlPayWeb.oAuth.model.*;
import com.sellgirl.sellgirlPayWeb.product.model.ResourceType;
import com.sellgirl.sellgirlPayWeb.product.model.resource;
import com.sellgirl.sellgirlPayWeb.product.model.resourceQuery;
import com.sellgirl.sellgirlPayWeb.product.service.ResourceService;
import com.sellgirl.sellgirlPayWeb.projHelper.DES_IV;
//import com.sellgirl.sellgirlPayWeb.service.BalanceService;
import com.sellgirl.sellgirlPayWeb.user.YJQueryController;

//@RestController
@Controller
//@RequestMapping("/User")
public class ResourceController 
extends  YJQueryController
//extends PFBaseWebController
{
	@Autowired private ResourceService resourceService;
//    @Autowired
//    private BalanceService _balanceService;

/*
 * 所有系统的单点登陆控制，如果用户已在本系统登陆，直接返回token
 */
//    @PFAllowAnonymous
//  	@GetMapping(value = "/")
	@GetMapping(value = { "/resource-index.html" })
//	@GetMapping(value = { "/Product/index" })
    public ModelAndView Home()
    {
		MvcPagingParameters p=new MvcPagingParameters();
		p.setSort("create_date desc");
		p.setPageIndex(0);
		p.setPageSize(4);
		for(ResourceType i:ResourceType.values()) {
			resourceQuery q=new resourceQuery();
			resourceService.setResourceType(i);
			List<resource> book=resourceService.GetresourceList(q,p);	
			ViewData.put(i.name(), book);
		}
  	  return View(new LoginerBase(),"Product/resource-index");
    }
//	@GetMapping(value = { "/Product/detail?id={id}" })
//	@GetMapping(value = { "/Product/detail" })
//    public ModelAndView Detail(@PathVariable(name = "id")String id)
//    {
//  	  return View(new LoginerBase(),"Product/detail");
//    }
	@GetMapping(value = { "/resource-detail.html" })
    public ModelAndView Detail(long id,ResourceType resourceType)
    {
		resourceService.setResourceType(resourceType);
		ViewData.put("resourceType", resourceType);
  	  return View(resourceService.GetOneResource(id),"Product/resource-detail");
    }
	@GetMapping(value = { "/resource-search.html" })
    public ModelAndView Search(String q)
    {
  	  return View(new LoginerBase(),"Product/resource-search");
    }
	@GetMapping(value = { "/resource-board.html" })
    public ModelAndView Board()
    {
  	  return View(new LoginerBase(),"Product/resource-board");
    }

}