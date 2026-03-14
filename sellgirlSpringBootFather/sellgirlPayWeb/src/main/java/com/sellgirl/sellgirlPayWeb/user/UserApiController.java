package com.sellgirl.sellgirlPayWeb.user;

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
import com.sellgirl.sgJavaHelper.SGAllowAnonymous;
import com.sellgirl.sgJavaHelper.SGDate;
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
import com.sellgirl.sgJavaMvcHelper.config.PFCookieUtils;
import com.sellgirl.sellgirlPayDao.DayDAO;
//import pf.springBoot.springBootSSO.controller.shares.YJQueryController;
import com.sellgirl.sellgirlPayWeb.oAuth.FormsAuth;
import com.sellgirl.sellgirlPayWeb.oAuth.LoginerBase;
//import com.sellgirl.sellgirlPayWeb.oAuth.MetabaseDataHelper;
//import pf.springBoot.springBootSSO.oAuth.MetabaseFormsAuth;
//import com.sellgirl.sellgirlPayWeb.oAuth.MetabaseUser;
import com.sellgirl.sellgirlPayWeb.oAuth.model.*;
import com.sellgirl.sellgirlPayWeb.projHelper.DES_IV;
import com.sellgirl.sellgirlPayWeb.user.model.User;
import com.sellgirl.sellgirlPayWeb.user.model.UserCreate;
//import com.sellgirl.sellgirlPayWeb.service.BalanceService;
import com.sellgirl.sellgirlPayWeb.user.service.UserService;

//@RestController
@RestController
//@RequestMapping("/User")
public class UserApiController extends  YJQueryController
{
	@Autowired private UserService userService;
//	@GetMapping(value = { "/AddUser" })
////	@PostMapping(value = { "/AddUser" })
//	@CrossOrigin
//    public AbstractApiResult<?> AddUser()
//    {
//		if(userService.addUser()) {
//    	return AbstractApiResult.success("success");
//		}else {
//	    	return AbstractApiResult.error("error");
//		}
//    }

////	@GetMapping(value = { "/AddUser" })
//	@PostMapping(value = { "/AddUser" })
//	@CrossOrigin
//    public AbstractApiResult<?> AddUser(String username,String password,String inviteCode)
//    {
//		UserCreate model=new UserCreate();
//		model.setUserName(username);
//		model.setPwd(password);
//		model.setInvitationCode(inviteCode);
//		if(userService.addUser(model)) {
//    	return AbstractApiResult.success("success");
//		}else {
//	    	return AbstractApiResult.error("error");
//		}
//    }

	@GetMapping(value = { "/AddCookie" })
	@CrossOrigin
    public AbstractApiResult<?> AddCookie(String v)
    {
		PFCookieUtils.setCookie("aa", v);
		return AbstractApiResult.success("success");
    }


	@GetMapping(value = { "/GetCookie" })
	@CrossOrigin
    public AbstractApiResult<?> GetCookie()
    {
		String ticket=PFCookieUtils.getCookieValue("aa");
		return AbstractApiResult.success(ticket);
    }
	@GetMapping(value = { "/AddCache" })
	@CrossOrigin
    public AbstractApiResult<?> AddCache(String v)
    {
        SGCaching.Set("aa", v,10);
		return AbstractApiResult.success("success");
    }


	@GetMapping(value = { "/GetCache" })
	@CrossOrigin
    public AbstractApiResult<?> GetCache()
    {
        Object data = SGCaching.Get("aa");
		return AbstractApiResult.success(data);
    }

//	//此版本需要登录再签到
//	@PostMapping(value = { "/PostSign" })
//    public AbstractApiResult<?> PostSign()
//    {
//		SystemUser user=GetSystemUser();
//		if(userService.signDay(user.UserCode)) {
//			user.signDay++;
//			user.lastSign=SGDate.Now();
//			this.SetSystemUser(user);
//			
////			HashMap<String,Object> r=new HashMap<String,Object>();
////			r.put("success", true);
//			return AbstractApiResult.success(user.signDay);
//		}else {
//			return AbstractApiResult.error("签到失败");
//		}
//    }
//	//此版本利用前端登录信息签到. 因为同一页面的收藏功能也是前端缓存保存的
	@PostMapping(value = { "/PostSign" })
    @SGAllowAnonymous
    public AbstractApiResult<?> PostSign(String username)
    {
		if(SGDataHelper.StringIsNullOrWhiteSpace(username)) {
			return AbstractApiResult.error("签到失败,用户名为空");
		}
//		SGRef<Integer> days=new SGRef<Integer>(); 
		SGRef<User> u=new SGRef<User>(); 
		if( userService.signDay(username,u)) {
			SystemUser user=GetSystemUser();
			if(null!=user&&!SGDataHelper.StringIsNullOrWhiteSpace(user.UserName)
				&&username.equals(user.UserName)
					) {
				//如果已经登录，使同步
				user.lastSign=u.GetValue().getLastSign();
				user.signDay=u.GetValue().getSignDay();
				this.SetSystemUser(user);
			}
//			user.signDay++;
//			user.lastSign=SGDate.Now();
			
//			HashMap<String,Object> r=new HashMap<String,Object>();
//			r.put("success", true);
			return AbstractApiResult.success(u.GetValue().getSignDay());
		}else {
			return AbstractApiResult.error("签到失败");
		}
    }

}