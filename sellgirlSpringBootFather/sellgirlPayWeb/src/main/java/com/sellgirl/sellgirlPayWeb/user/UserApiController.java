package com.sellgirl.sellgirlPayWeb.user;

import java.text.DecimalFormat;
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
import com.sellgirl.sgJavaHelper.SGEmailSend;
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
import com.sellgirl.sgJavaMvcHelper.config.SGCookieUtils;
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
import com.sellgirl.sellgirlPayWeb.user.model.UserQuery;
//import com.sellgirl.sellgirlPayWeb.service.BalanceService;
import com.sellgirl.sellgirlPayWeb.user.service.UserService;

//@RestController
@RestController
//@RequestMapping("/User")
public class UserApiController extends  YJQueryController
{
	@Autowired private UserService userService;




	@GetMapping(value = { "/AddCookie" })
	@CrossOrigin
    public AbstractApiResult<?> AddCookie(String v)
    {
		SGCookieUtils.setCookie("aa", v);
		return AbstractApiResult.success("success");
    }


	@GetMapping(value = { "/GetCookie" })
	@CrossOrigin
    public AbstractApiResult<?> GetCookie()
    {
		String ticket=SGCookieUtils.getCookieValue("aa");
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
	
//	public enum SignType{
//		CONSECUTIVE,ONE,ZERO
//	}
//	//此版本利用前端登录信息签到. 因为同一页面的收藏功能也是前端缓存保存的
	@PostMapping(value = { "/PostSign" })
    @SGAllowAnonymous
    public AbstractApiResult<?> PostSign(String username)
    {
		if(SGDataHelper.StringIsNullOrWhiteSpace(username)) {
			return AbstractApiResult.error("签到失败,用户名为空");
		}
		UserQuery q=new UserQuery();
		q.setUserName(username);
		User user2=userService.getUser(q);
		if(null==user2) {
			return AbstractApiResult.error("签到失败,用户不存在");
		}
		
//		boolean rebegin=true;//非连续签到,需要重算
//		SignType signType=SignType.ONE;
		SGDate today=SGDate.Now().GetDayStart();
		boolean pointsEarned=false;
		int signDay=1;
		if(null!=user2.getLastSign()) {
			if(0<user2.getLastSign().compareTo(today)) {
				return AbstractApiResult.error("今天已签到");
			}
			SGDate yesterday=today.AddDays(-1);
			if(0<user2.getLastSign().compareTo(yesterday)) {//连续签到
				signDay=-1;
//				signType=SignType.CONSECUTIVE;
				if(8<user2.getSignDay()) {
					signDay=user2.getSignDay()-9;
//					signType=SignType.ZERO;
					pointsEarned=true;
				}
			}
		}
//		SGRef<Integer> days=new SGRef<Integer>(); 
		SGRef<User> u=new SGRef<User>(); 
		if(pointsEarned) {//加积分
			userService.addUserPoint(user2.getUserId(), 1);
		}
		if( userService.signDay(username,u,signDay)) {
			//更新cache
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
			HashMap<String,Object> r=new HashMap<String,Object>();
			r.put("signDay", u.GetValue().getSignDay());
			r.put("point", u.GetValue().getPoint());
			r.put("pointsEarned", pointsEarned);
			return AbstractApiResult.success(r);
		}else {
			return AbstractApiResult.error("签到失败");
		}
    }

	@PostMapping(value = { "/GenEmailCode" })
    @SGAllowAnonymous
    public AbstractApiResult<?> GenEmailCode(String email)
    { 
		double d=Math.random()*10000D;	  
		DecimalFormat df = new DecimalFormat("0");
		String s = df.format(d);
		SGEmailSend.SendMail(new String[] {email}, "欢迎注册bdbook", "bdbook注册验证码为:"+s);
//		if(SGDataHelper.StringIsNullOrWhiteSpace(username)) {
//			return AbstractApiResult.error("签到失败,用户名为空");
//		}
		SGCookieUtils.setCookie("registerEmailCode", s,600);//10分钟
		return AbstractApiResult.success();
    }
	

	@PostMapping(value = { "/api/UpgradeToResource" })
    public AbstractApiResult<?> UpgradeToResource(String inviteCode)
    {
//		if(!IsLogined()) {
//			
//		}
		if(!UserController.isInviteCodeRight(inviteCode)) {
			return AbstractApiResult.error("邀请码不正确");
		}
//		String userName=this.GetUserName();
//		if(SGDataHelper.StringIsNullOrWhiteSpace(userName)) {//应该用拦截器,不应进入这里
//			return AbstractApiResult.error("未登录");
//		}
		if(!this.userService.updateUserInvite(this.GetUserLongId(),inviteCode)){
			return AbstractApiResult.error("更新邀请码失败");
		}else {
			return AbstractApiResult.success("升级成功！页面即将刷新...",null);			
		}

    }

}