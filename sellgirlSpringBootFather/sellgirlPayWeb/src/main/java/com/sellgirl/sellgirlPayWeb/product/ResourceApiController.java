package com.sellgirl.sellgirlPayWeb.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
//import com.perfect99.right.amsweb.ActionReturnInfo;
//import com.perfect99.right.amsweb.BaseReturnInfo;

import com.sellgirl.sgJavaHelper.AbstractApiResult;
import com.sellgirl.sgJavaHelper.ErrorApiResult;
import com.sellgirl.sgJavaHelper.SGAllowAnonymous;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGCaching;
import com.sellgirl.sgJavaHelper.PFDataRow;
import com.sellgirl.sgJavaHelper.SGDataTable;
//import com.sellgirl.sgJavaSpringHelper.PFJsonData;
//import com.sellgirl.sgJavaSpringHelper.PFJsonDataT;
import com.sellgirl.sgJavaHelper.PFJsonData;
import com.sellgirl.sgJavaHelper.PFJsonDataT;
import com.sellgirl.sgJavaHelper.PagingResult;
import com.sellgirl.sgJavaSpringHelper.PFObject;
//import com.sellgirl.sgJavaSpringHelper.PFObject;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaSpringHelper.config.SGDataHelper;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.sellgirl.sgJavaHelper.model.SystemUser;
import com.sellgirl.sgJavaHelper.model.UserOrg;
import com.sellgirl.sgJavaHelper.model.UserTypeClass;
import com.sellgirl.sgJavaMvcHelper.config.SGCookieUtils;
import com.sellgirl.sellgirlPayDao.DayDAO;
import com.sellgirl.sellgirlPayWeb.PayShopSwaggerAttr;
//import pf.springBoot.springBootSSO.controller.shares.YJQueryController;
import com.sellgirl.sellgirlPayWeb.oAuth.FormsAuth;
import com.sellgirl.sellgirlPayWeb.oAuth.LoginerBase;
//import com.sellgirl.sellgirlPayWeb.oAuth.MetabaseDataHelper;
//import pf.springBoot.springBootSSO.oAuth.MetabaseFormsAuth;
//import com.sellgirl.sellgirlPayWeb.oAuth.MetabaseUser;
import com.sellgirl.sellgirlPayWeb.oAuth.model.*;
import com.sellgirl.sellgirlPayWeb.product.model.ProductType;
import com.sellgirl.sellgirlPayService.product.model.ResourceType;
import com.sellgirl.sellgirlPayWeb.product.model.book;
import com.sellgirl.sellgirlPayWeb.product.model.bookQuery;
import com.sellgirl.sellgirlPayWeb.product.model.resource;
import com.sellgirl.sellgirlPayWeb.product.model.resourceQuery;
import com.sellgirl.sellgirlPayWeb.product.model.userBuyCreate;
import com.sellgirl.sellgirlPayWeb.product.service.BookService;
import com.sellgirl.sellgirlPayWeb.product.service.ResourceService;
import com.sellgirl.sellgirlPayWeb.projHelper.DES_IV;
import com.sellgirl.sellgirlPayWeb.shop.model.SuccessApiResult2;
import com.sellgirl.sellgirlPayWeb.user.YJQueryController;
import com.sellgirl.sellgirlPayWeb.user.model.UserCreate;
import com.sellgirl.sellgirlPayWeb.user.model.UserQuery;
//import com.sellgirl.sellgirlPayWeb.service.BalanceService;
import com.sellgirl.sellgirlPayWeb.user.service.UserService;
import com.sellgirl.sgJavaMvcHelper.MvcPagingParameters;
//@RestController
@RestController
//@RequestMapping("/User")
public class ResourceApiController extends  YJQueryController
{
	@Autowired private ResourceService resourceService;
	@Autowired private UserService userService;
	@PostMapping(value = { "/api/resource/list" })
    public AbstractApiResult<?> getList(
    		//String field,boolean descending ,
    		ResourceType resourceType,
    		MvcPagingParameters p
    		)
    {
		resourceQuery q=new resourceQuery();
//		if("uploadTime".equals(field)) {
//			q.sort="create_date";	
//		}else {
//			q.sort=field;
//		}
//		q.desc=descending;
//		resourceService.setResourceType(resourceType);
		List<resource> book=resourceService.GetresourceList(q,p,resourceType);
		PagingResult r=new PagingResult();
		r.data=book;
		if((null==r||book.isEmpty())&&0==p.getPageIndex()) {
			r.total=0;
		}else if((null==r||book.isEmpty())&&0<p.getPageIndex()) {
			r.total=p.getPageSize()*p.getPageIndex();
		}else {
//			r.total=p.getPageSize()*p.getPageIndex()+book.size();
			r.total=p.getPageSize()*(p.getPageIndex()+3);
		}
		return AbstractApiResult.success(r);
    }


	@PostMapping(value = { "/api/resource/unlock" })
	@PayShopSwaggerAttr
	@ApiOperation(value="扣积分解锁资源")
    @ApiResponses({
    @ApiResponse(code=200,message="成功",response=SuccessApiResult2.class),
    @ApiResponse(code=333,message="业务自定义错误",response=ErrorApiResult.class)
    })
    public AbstractApiResult<?> unlockResource(
    		ProductType resourceType,
    		long resourceId
    		)
    {
		long userId=GetUserLongId();
		SystemUser sysUser=this.GetSystemUser();
		if(1>sysUser.point) {
			return AbstractApiResult.error("积分不足，无法查看");
		}
		userBuyCreate m=new userBuyCreate();
		m.setSource_type(resourceType);
		m.setUser_id(userId);
		m.setSource_id(resourceId);
		m.setCreate_date(SGDate.Now());
		boolean b=resourceService.unlockResource(m);
		if(b) {
			userService.addUserPoint(userId, -1);
			UserQuery q=new UserQuery();
			q.setUserId(userId);
			long point=userService.getUser(q).getPoint();
			sysUser.point=point;
			this.SetSystemUser(sysUser);
			HashMap<String,Object> r=new HashMap<String,Object>();
			r.put("point", point);
			r.put("resourceLock", resourceService.GetOneResourceLock(resourceId,resourceService.productToResource(resourceType)));
			return AbstractApiResult.success("解锁成功",r);
		}else {
			return AbstractApiResult.error("解锁失败");
		}
    }
}