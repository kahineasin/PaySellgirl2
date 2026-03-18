package com.sellgirl.sellgirlPayWeb.user;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
import com.sellgirl.sgJavaSpringHelper.config.SGDataHelper.LocalDataType;
import com.sellgirl.sgJavaHelper.model.SystemUser;
import com.sellgirl.sgJavaHelper.model.UserOrg;
import com.sellgirl.sgJavaHelper.model.UserTypeClass;
import com.sellgirl.sgJavaMvcHelper.HtmlHelperT;
import com.sellgirl.sgJavaMvcHelper.PFBaseWebController;
import com.sellgirl.sellgirlPayDao.DayDAO;
//import pf.springBoot.springBootSSO.controller.shares.YJQueryController;
import com.sellgirl.sellgirlPayWeb.oAuth.FormsAuth;
import com.sellgirl.sellgirlPayWeb.oAuth.LoginerBase;
//import com.sellgirl.sellgirlPayWeb.oAuth.MetabaseDataHelper;
//import pf.springBoot.springBootSSO.oAuth.MetabaseFormsAuth;
//import com.sellgirl.sellgirlPayWeb.oAuth.MetabaseUser;
import com.sellgirl.sellgirlPayWeb.oAuth.model.*;
import com.sellgirl.sellgirlPayWeb.projHelper.DES_IV;
import com.sellgirl.sellgirlPayWeb.user.model.PayPlan;
import com.sellgirl.sellgirlPayWeb.user.model.User;
import com.sellgirl.sellgirlPayWeb.user.model.UserCreate;
import com.sellgirl.sellgirlPayWeb.user.model.UserCreateModel;
//import com.sellgirl.sellgirlPayWeb.service.BalanceService;
import com.sellgirl.sellgirlPayWeb.user.service.UserService;

//@RestController
@Controller
//@RequestMapping("/User")
public class UserController 
//extends PFBaseWebController
extends YJQueryController
{
//    @Autowired
//    private BalanceService _balanceService;

/*
 * 所有系统的单点登陆控制，如果用户已在本系统登陆，直接返回token
 * 
 * 参考自项目metabasseLogin
 */
    @SGAllowAnonymous
  	@GetMapping(value = "/User/AuthSSO")
    public ModelAndView AuthSSO(String return_to,@RequestParam(required = false) String login_url)
    {
    	SGRef<SystemUser> userDataRef=new SGRef<SystemUser>();
    	Boolean isLogin= FormsAuth.IsUserLogined(userDataRef);
    	
//    	SystemUser userData = FormsAuth.GetUserExData(SystemUser.class);
//        Boolean isLogin=!(userData == null || SGDataHelper.StringIsNullOrWhiteSpace(userData.UserCode));
    	
    	if(isLogin) {//UserAuthorizeAttribute.IsLogined()
    		SystemUser userData=userDataRef.GetValue();
            String token=FormsAuth.SignUserToken(userData);
//            ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
//            mav.addObject(token);
            HashMap<String, Object> urlParam = new HashMap<String, Object>() {
				private static final long serialVersionUID = 1L;

				{
                    put("token", token);
                }
            };
            //return super.RedirectToUrl(return_to+"&token="+token);
 	       String rUrl=SGDataHelper.setUrlParams(return_to,urlParam);
 	       if(!rUrl.startsWith("http")) {rUrl="http://"+rUrl;}
           return super.RedirectToUrl(rUrl);
            //return super.RedirectToUrl(SGDataHelper.setUrlParams(return_to,urlParam));
    	}
    	if(login_url!=null) {
 	       String rUrl=login_url;
  	       if(!rUrl.startsWith("http")) {rUrl="http://"+rUrl;}
            return super.RedirectToUrl(rUrl);
    	}else {
            return super.RedirectToUrl("/User/LoginSSO?return_to="+return_to);
    	}
    	//可以考虑连同Metabase一起登陆
    }

/*
 * 登陆页面(其它系统可共用)
 * 
 * @deprecated 测试的方法
 */
    @SGAllowAnonymous
  	@GetMapping(value = "/User/LoginSSO")
    @Deprecated
    public ModelAndView LoginSSO(@RequestParam(required = false) String return_to,@RequestParam(required = false) String error )
    {
    	ViewData.clear();
    	if(error!=null) {
            ViewData.put("err",error);
    	}
        return super.View((Object)return_to);
    }

    /*
     * 登陆方法(其它系统可共用)
     * 
     * @return_to 登陆成功的去处，可空
     * @login_url 登陆失败，返回原系统的登陆页，可空
     */
    @SGAllowAnonymous
  	@GetMapping(value = "/User/GoLoginSSO")
    public ModelAndView GoLoginSSO(String userid, String password, String return_to,@RequestParam(required = false) String login_url)
    {
  		//BaseReturnInfo user = FormsAuth.LoginCheck(userid, password);
  		BaseReturnInfoDto user = FormsAuth.LoginCheckWebApi(userid, password);
        if (!user.isIsSuccess())
        {
        	if(login_url!=null) {
               HashMap<String, Object> urlParam = new HashMap<String, Object>() {
   				private static final long serialVersionUID = 1L;

   				{
                       put("error", SGDataHelper.getURLEncoderString("帐号或密码不正确(SSO)"));
                   }
               };
               //aaabbbcc
               //return super.RedirectToUrl(return_to+"&token="+token);
    	       String rUrl=SGDataHelper.setUrlParams(login_url,urlParam);
       	       if(!rUrl.startsWith("http")) {rUrl="http://"+rUrl;}
                 return super.RedirectToUrl(rUrl);
         	}else {
//                 return super.RedirectToUrl("/User/LoginSSO?return_to="+return_to);
                 //ViewData.put("err","用户名或密码错误");
//                 return View((Object)return_to,"/User/LoginSSO");
                 return super.RedirectToUrl("/User/LoginSSO?return_to="+return_to+"&error="+SGDataHelper.getURLEncoderString("用户名或密码错误"));
         	}
        }
        else
        {
            Boolean b;
            String error;
            SGRef<Boolean> rB = new SGRef<Boolean>(false);
            SGRef<String> rError = new SGRef<String>(null);
            SGRef<SystemUser> rSysUser= new SGRef<SystemUser>(null);
            
           // List<String> fgsList=
            		DoSignIn(user,rB, rError,rSysUser);
            		
            b=rB.GetValue();
            error=rError.GetValue();
            if (!b)
            {
                ViewData.put("err",error);
                return View((Object)return_to);
                //return PFJsonData.SetFault(error);
            }
//        	SystemUser userData = FormsAuth.GetUserExData(SystemUser.class);//刚登陆完不能用此方法，因为request里还没有cookie
        	SystemUser userData =rSysUser.GetValue();
        	userData.Pwd=password;
            String token=FormsAuth.SignUserToken(userData);
//          ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
//          mav.addObject(token);
	          HashMap<String, Object> urlParam = new HashMap<String, Object>() {
				private static final long serialVersionUID = 1L;

				{
	                  put("token", token);
	              }
	          };
          //return super.RedirectToUrl(return_to+"&token="+token);
	          
	       if(SGDataHelper.StringIsNullOrWhiteSpace(return_to)) {
		          return super.Json(AbstractApiResult.success("登陆成功，但没有指定return_to页面"));
	       }else {
		       String rUrl=SGDataHelper.setUrlParams(return_to,urlParam);
	 	       if(!rUrl.startsWith("http")) {rUrl="http://"+rUrl;}
	          return super.RedirectToUrl(rUrl);
	       }

        }
    }

    @SGAllowAnonymous
  	@GetMapping(value = "/User/GoLoginSSOUserId")
    public ModelAndView GoLoginSSOUserId(String userid, String return_to,@RequestParam(required = false) String login_url)
    {
  		//BaseReturnInfo user = FormsAuth.LoginCheck(userid, password);
  		//BaseReturnInfoDto user = FormsAuth.LoginCheckWebApi(userid, password);
  		BaseReturnInfoDto user =new BaseReturnInfoDto(FormsAuth.GetAllAction(new DES_IV().decode(userid)));
  		//UserAllInfoDto user =FormsAuth.GetUserInfoWebApi(userid);
        if (!user.isIsSuccess())
        {
        	if(login_url!=null) {
               HashMap<String, Object> urlParam = new HashMap<String, Object>() {
   				private static final long serialVersionUID = 1L;

   				{
                       put("error", "帐号或密码不正确(SSO)");
                   }
               };
               //return super.RedirectToUrl(return_to+"&token="+token);
    	       String rUrl=SGDataHelper.setUrlParams(login_url,urlParam);
       	       if(!rUrl.startsWith("http")) {rUrl="http://"+rUrl;}
                 return super.RedirectToUrl(rUrl);
         	}else {
//                 return super.RedirectToUrl("/User/LoginSSO?return_to="+return_to);
                 //ViewData.put("err","用户名或密码错误");
//                 return View((Object)return_to,"/User/LoginSSO");
                 return super.RedirectToUrl("/User/LoginSSO?return_to="+return_to+"&error="+SGDataHelper.getURLEncoderString("用户名或密码错误"));
         	}
        }
        else
        {
            Boolean b;
            String error;
            SGRef<Boolean> rB = new SGRef<Boolean>(false);
            SGRef<String> rError = new SGRef<String>(null);
            SGRef<SystemUser> rSysUser= new SGRef<SystemUser>(null);
            
           // List<String> fgsList=
            		DoSignIn(user,rB, rError,rSysUser);
            		
            b=rB.GetValue();
            error=rError.GetValue();
            if (!b)
            {
                ViewData.put("err",error);
                return View((Object)return_to);
                //return PFJsonData.SetFault(error);
            }
//        	SystemUser userData = FormsAuth.GetUserExData(SystemUser.class);//刚登陆完不能用此方法，因为request里还没有cookie
        	SystemUser userData =rSysUser.GetValue();
        	userData.Pwd="autologin";
            String token=FormsAuth.SignUserToken(userData);
//          ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
//          mav.addObject(token);
	          HashMap<String, Object> urlParam = new HashMap<String, Object>() {
				private static final long serialVersionUID = 1L;

				{
	                  put("token", token);
	              }
	          };
          //return super.RedirectToUrl(return_to+"&token="+token);
	          
	       if(SGDataHelper.StringIsNullOrWhiteSpace(return_to)) {
		          return super.Json(AbstractApiResult.success("登陆成功，但没有指定return_to页面"));
	       }else {
		       String rUrl=SGDataHelper.setUrlParams(return_to,urlParam);
	 	       if(!rUrl.startsWith("http")) {rUrl="http://"+rUrl;}
	          return super.RedirectToUrl(rUrl);
	       }

        }
    }

//	@SGAllowAnonymous
////  	@GetMapping(value = "/User/LogoutSSO")
//  	@GetMapping(value = "logout.html")
//    public ModelAndView LogoutSSO(String return_to) {
//	       FormsAuth.SingOut();
//	       
//	       if(SGDataHelper.StringIsNullOrWhiteSpace(return_to)) {
////	            return super.RedirectToUrl("/User/LoginSSO");
//	            return super.View("/Product/logout.html");//html中跳转
//		        //return super.Json(AbstractApiResult.success("退出成功，但没有指定return_to页面"));
//	       }else {
//	           String rUrl=return_to;
//		       if(!rUrl.startsWith("http")) {rUrl="http://"+rUrl;}		       
//	           return super.RedirectToUrl(rUrl);
//	       }
//
//    }
  //无论是@RestController还是@Controller都不影响返回页面
  	@PostMapping(value = "/User/PostLogin")
  	public ModelAndView PostloginPage(String userid,String password
//  			,
//  			HttpServletRequest request,HttpServletResponse response
  			){
      if (DoCheckLogin(userid, password).Result)//这里有报错--benjamin todo
      {
    		return super.RedirectToUrl("/");
//    	  try {
//			response.sendRedirect("/");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	  return null;
    	  
//      	//response.sendRedirect("/User/Login");
//    	  return new ModelAndView("redirect:/");
//          return new ModelAndView("redirect:/");
//          //return "redirect:/target";
          //return View("User/Login");
      }else {
    	  return View("User/Login");
      }
		
	  //return new ModelAndView("redirect:/");
	  
//      //ok
//  		return new ModelAndView("redirect:/");
  		
//	  ////ok
//  		ViewData.put("err", "密码错误");
//      return View("User/Login");
  		
//  		ModelAndView mav = new ModelAndView();
//  		mav.addObject("wvp",new WebViewPageT<Object>(this));
//  		mav.addObject("Html",new HtmlHelper());
//  		mav.setViewName("User/Login");
//  		
//  		return mav;
  	}


    

//    private List<String> DoSignIn(BaseReturnInfo user ,SGRef<Boolean>  success,SGRef<String>   error,SGRef<SystemUser> refSysUser)
    private List<String> 
    DoSignIn(BaseReturnInfoDto user ,SGRef<Boolean>  success,SGRef<String>   error,SGRef<SystemUser> refSysUser) 
    {
        //error = null;
        error.SetValue(null);
        //var userid = user.UserId;
        String userid = user.getUserNumber();
        LoginerBase userData = new LoginerBase();
        userData.UserCode=userid;
        userData.UserName=user.getUserName();

        int effectiveHours = 1;//登陆1小时
        //UserAllInfo userAllInfo = FormsAuth.GetUserInfo(userid);
        UserAllInfoDto userAllInfo = FormsAuth.GetUserInfoWebApi(userid);
        SystemUser sysUser = new SystemUser();
        refSysUser.SetValue(sysUser);
    	sysUser.UserCode=userid;
    	sysUser.UserName=user.getUserName();
    	sysUser.Email=userAllInfo.getEMail();
    			
    	List<String> fgsList = new ArrayList<String>();

        SGDataTable fgsTable=null;

        String precinct = user.getPrecinct()==null?"":user.getPrecinct();
        String orgNumber = user.getOrgExtNumber()==null? user.getOrgNumber():user.getOrgExtNumber();
        //String orgName = user.getOrgName();

        ArrayList<UserOrg> orgList = new ArrayList<UserOrg>();
        if (SGDataHelper.StringIsNullOrWhiteSpace(user.getOrgExtNumber()))
        {
        	UserOrg userOrg=new UserOrg();
        	userOrg.Org = user.getOrgNumber();
        	userOrg.OrgName = user.getOrgName();
        	userOrg.UserType = UserTypeClass.Default;
            orgList.add(userOrg);
        }else
        {
        	UserOrg userOrg=new UserOrg();
        	userOrg.Org = user.getOrgExtNumber();
        	userOrg.OrgName = user.getOrgName();
        	userOrg.UserType = UserTypeClass.Fgs;
            orgList.add(userOrg);
        }
        fgsList =Arrays.asList(precinct.split(",")).stream().filter(a->a!=orgNumber).collect(Collectors.toList());
        
        if(!SGDataHelper.UseLocalData()) {
//	        for(String fgs : fgsList)
//	        {
//	            fgsTable = _balanceService.GetSFByFgs(fgs);//benjamin
//	            //fgsTable = new BalanceService().GetSFByFgs(fgs);//benjamin 
//	            
//	            if (fgsTable != null && !fgsTable.IsEmpty())
//	            {
//	                PFDataRow row = fgsTable.getRow().get(0);
//	            	UserOrg userOrg=new UserOrg();
//	            	userOrg.Org = row.getStringColumn("fgs");
//	            	userOrg.OrgName = row.getStringColumn("fgsname");
//	            	userOrg.UserType = UserTypeClass.Fgs;
//	                orgList.add(userOrg);
//	            }             
//	        }
//	        if (orgList.size() > 0)
//	        {
//	        	if(orgList.get(0).UserType==UserTypeClass.Fgs) {
//	                fgsTable = _balanceService.GetSFByFgs(orgList.get(0).Org);//benjamin 
//	                //fgsTable =  new BalanceService().GetSFByFgs(orgList.get(0).Org);//benjamin 
//	            	PFDataRow row = fgsTable.getRow().get(0);
//	                sysUser.Sf = row.getStringColumn("sf");
//	                sysUser.SfName = row.getStringColumn("sfname");
//	                sysUser.Fgs = row.getStringColumn("fgs");
//	                sysUser.FgsName = row.getStringColumn("fgsname");
//	        	}
//	//            switch (orgList.get(0).UserType.getValue())
//	//            {
//	//                case UserTypeClass.Fgs.getValue():
//	//                    fgsTable = _balanceService.GetSFByFgs(orgList.get(0).Org);
//	//                	PFDataRow row = fgsTable.getRow().get(0);
//	//                    sysUser.Sf = row.getStringColumn("sf");
//	//                    sysUser.SfName = row.getStringColumn("sfname");
//	//                    sysUser.Fgs = row.getStringColumn("fgs");
//	//                    sysUser.FgsName = row.getStringColumn("fgsname");
//	//                    break;
//	//                default:
//	//                    break;
//	//            }
//	            sysUser.Org = orgList.get(0).Org;
//	            sysUser.OrgName = orgList.get(0).OrgName;
//	        }

        }
        sysUser.OrgList = orgList;


        if (SGDataHelper.PreventFgs() && (!SGDataHelper.StringIsNullOrWhiteSpace(sysUser.Sf)))
        {
            success.SetValue(false);
            error.SetValue("已坏");
            return new ArrayList<String>();
        }
        FormsAuth.SignIn(userData, sysUser, 60 * effectiveHours);
        success.SetValue(true);
        return fgsList;

    }
    private PFJsonData DoCheckLogin(String userid, String password)
    {
//        //OAuthClientManager manager = new OAuthClientManager();
//        //TokenInfo token = manager.LoginCheck(userid, password);
//        //if (token == null) { return View(token); }else{token.Refresh();Token = token;}
//    	BaseReturnInfo user = FormsAuth.LoginCheck(userid, password);
    	BaseReturnInfoDto user = FormsAuth.LoginCheckWebApi(userid, password);
        if (!user.isIsSuccess())
        {
            ViewData.put("err","用户名或密码错误");
            return PFJsonData.SetFault("用户名或密码错误");
        }
        else
        {
            Boolean b;
            String error;
            SGRef<Boolean> rB = new SGRef<Boolean>(false);
            SGRef<String> rError = new SGRef<String>(null);
            SGRef<SystemUser> rSysUser= new SGRef<SystemUser>(null);
            List<String> fgsList=DoSignIn(user,rB, rError,rSysUser);
            b=rB.GetValue();
            error=rError.GetValue();
            if (!b)
            {
                ViewData.put("err",error);
                return PFJsonData.SetFault(error);
            }

            return new PFJsonDataT<Object>().SetSuccess(new PFObject()
               .Add("userInfo", 
            		   new PFObject()
            		   .Add("userId", GetUserId())
            		   .Add("userName", GetUserName())
            		   .Add("sf", GetSf())
            		   .Add("sfname", GetSfName())
            		   .Add("fgs", GetFgs())
            		   .Add("fgsname", GetFgsName())
            		   .Add("fgsList", fgsList)            		   
        		   )
//               .Add("session",PFCookieUtils.getCookieMap()) //benjamin todo
            );
        }
    }
//
//  	@PostMapping(value = "/User/Logout")
//  	@ResponseBody
//    public PFJsonData Logout()
//    {
//        String UserId=GetUserId();
//        if (!SGDataHelper.StringIsNullOrWhiteSpace(UserId))
//        {
//            SGCaching.Remove(UserId);
//            //Sf = null;
//            //SfName = null;
//            //Fgs = null;
//            //FgsName = null;
//            //CurrentMonth = null;
//            //CurrentDatabase = null;
//            FormsAuth.SingOut();
//
//            rdto = PFJsonData.SetSuccess();
//        }
//        else
//        {
//            rdto = PFJsonData.SetSuccess("未登陆");
//        }
//        return rdto;
//    }
//
//    /// <summary>
//    /// 设置当前分公司(机构)
//    /// </summary>
//    /// <returns></returns>
//    [HttpPost]
//    public ActionResult SetOrg(string org,string orgName)
//    {
//        var sysUser = SystemUser ?? new SystemUser();
//        var fgsTable = _balanceService.GetSFByFgs(org);
//        if (fgsTable != null && fgsTable.Rows.Count > 0)
//        {
//            sysUser.Sf = fgsTable.Rows[0]["sf"].ToString();
//            sysUser.SfName = fgsTable.Rows[0]["sfname"].ToString();
//            sysUser.Fgs = fgsTable.Rows[0]["fgs"].ToString();
//            sysUser.FgsName = fgsTable.Rows[0]["fgsname"].ToString();
//
//        }else
//        {
//            sysUser.Sf = null;
//            sysUser.SfName = null;
//            sysUser.Fgs = null;
//            sysUser.FgsName = null;
//        }
//        sysUser.Org = org;
//        sysUser.OrgName = orgName;
//        SystemUser = sysUser;
//        rdto = JsonData.SetSuccess();
//        return Json(rdto);
//    }
//
//    public ActionResult ForgetPassword() {
//        return View();
//    }
  	
//---------------------shop 登录-------------------

	@SGAllowAnonymous
	@GetMapping(value = { "/register.html" })
    public ModelAndView Register()
    {
//  	  return View(new LoginerBase(),"Product/register");
  	ModelAndView result=new ModelAndView();
//  	result.addObject("username","cuowu");
////  	result.addObject("Model",model);
//  	result.addObject("Html", new HtmlHelperT<TModel>(ViewData));
  	result.setViewName("Product/register");
  	return result;
    }

	@SGAllowAnonymous
	@GetMapping(value = { "/logout.html" })
    public ModelAndView Logout()
    {
      String UserId=GetUserId();
      if (!SGDataHelper.StringIsNullOrWhiteSpace(UserId)) {
  		SGCaching.Remove(UserId);
  		FormsAuth.SingOut();
      }
	  	ModelAndView result=new ModelAndView();
	  	result.setViewName("Product/logout");
	  	return result;
    }

	@SGAllowAnonymous
	@GetMapping(value = { "/login.html" })
    public ModelAndView Login(String return_to)
    {
	  	ModelAndView result=new ModelAndView();
	    result.addObject("return_to",return_to);
//	    result.addObject("postAction",SGDataHelper.StringIsNullOrWhiteSpace(return_to)
//	    		?"/LoginUser":("/LoginUser?return_to="+return_to)
//	    		);
	  	result.setViewName("Product/login");
	  	return result;
    }
	@Autowired private UserService userService;
	
	public static class UserCreateDto{
		private String username;
		private String password;
		private String inviteCode;
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getInviteCode() {
			return inviteCode;
		}
		public void setInviteCode(String inviteCode) {
			this.inviteCode = inviteCode;
		}
	}
	/**
	 * 用户注册
	 * 注意用户登陆后最好刷新页面。后端跳转也便于隐藏resource页面的存在
	 * 
	 * @param username
	 * @param password
	 * @param inviteCode
	 * @return
	 */
	@SGAllowAnonymous
//	@GetMapping(value = { "/AddUser" })
	@PostMapping(value = { "/AddUser" })
//	@CrossOrigin
    public ModelAndView AddUser(String username,String password,String inviteCode)
//    public ModelAndView AddUser(@Valid @RequestBody UserCreateDto m, BindingResult bindingResult)
    {		
		String filePath = Paths.get(com.sellgirl.sgJavaHelper.config.SGDataHelper.GetBaseDirectory(), 
				com.sellgirl.sgJavaHelper.config.SGDataHelper.LocalDataType.System.toString() + "LocalData", "Txt", "shop/inviteCode.txt")
	.toString();
//		String filePath2 = Paths.get(SGDataHelper.GetBaseDirectory(), LocalDataType.System.toString() + "LocalData", "Txt", "shop","inviteCode.txt")
//	.toString();
		System.out.println("---------1----------");
		System.out.println(filePath);
//		System.out.println("---------2----------");
//		System.out.println(filePath2);
		//linux路径报错 todo
		String x=com.sellgirl.sgJavaHelper.config.SGDataHelper.ReadLocalTxt(Paths.get("shop","inviteCode.txt").toString(), 
				com.sellgirl.sgJavaHelper.config.SGDataHelper.LocalDataType.System).trim();
//		String x=SGDataHelper.ReadFileToString(filePath2).trim();
		if(null!=inviteCode&&!x.equals(inviteCode)) {
			inviteCode=null;
		}
		
		UserCreate model=new UserCreate();
//		String username=m.getUsername();String password=m.getPassword();String inviteCode=m.getInviteCode();
		model.setUserName(username);
		model.setPwd(password);
		model.setInvitationCode(inviteCode);
//		model.setUserName(m.getUsername());
//		model.setPwd(m.getPassword());
//		model.setInvitationCode(m.getInviteCode());
		SGRef<String> error=new SGRef<String>();
		if(null!=userService.getUser(username)) {
			ViewData.put("username", "用户名重复，注册失败");
	    	  return View("Product/register");
		}
		if(userService.addUser(model,error)) {

			ModelAndView r= DoLogin(username, password,null);
			if(null!=r) {
				return r;
			}
//	  		BaseReturnInfoDto userSSO = FormsAuth.LoginCheckWebApi(username, password);
//	  		if(userSSO.isIsSuccess()) {
//	  	        LoginerBase userData = new LoginerBase();
//	  	        userData.UserCode=username;
//	  	        userData.UserName=username;
//
//	  	        int effectiveHours = 1;//登陆1小时
//	  	        //UserAllInfo userAllInfo = FormsAuth.GetUserInfo(userid);
//	  	        User user = FormsAuth.checkUser(username,null,false);
//	  	        SystemUser sysUser = new SystemUser();
//	  	    	sysUser.UserCode=user.getUserName();
//	  	    	sysUser.UserName=user.getUserName();
////	  	    	sysUser.Email=user.getEmail();
//	  	    	sysUser.isInvited=!SGDataHelper.StringIsNullOrWhiteSpace(user.getInvitationCode());
//	  	        FormsAuth.SignIn(userData, sysUser, 60 * effectiveHours);
//	  	        if(sysUser.isInvited) {
////	  	    	  return View(new LoginerBase(),"Product/resource-index");
////	              return super.RedirectToUrl("/resource-index.html");
////	  	        	ViewData.put("inviteCode",user.getInvitationCode());
//	  	        	ViewData.put("username",username);
//	  	        	ViewData.put("isInvited",true);
//	  	        	ViewData.put("role","resource");
//	  	        	ViewData.put("url","/resource-index.html");
//		  	    	  return View("ProductJump/index");
//	  	        }else {
////	  	    	  return View(new LoginerBase(),"Product/index");
//	  	        	
////	              return super.RedirectToUrl("/");//这样前端没有cache,ai的页面用不了
//	  	        	
////	  	        	ViewData.put("inviteCode","");
//	  	        	ViewData.put("username",username);
//	  	        	ViewData.put("isInvited",false);
//	  	        	ViewData.put("role","normal");	  	        	
//	  	        	ViewData.put("url","/");
//	  	    	  return View("ProductJump/index");
//	  	        }
//	  		}

	  		
		}else {
//	    	return AbstractApiResult.error("error");
//	    	  return View(new LoginerBase(),"Product/register");
		}
//		UserCreateModel r=new UserCreateModel();
//		r.setUsername("error");
//		ViewData.put("storyName",storyName);
//    	ViewData.SetModel(model);
//    	ModelAndView result= GetView();
//    	ModelAndView result=new ModelAndView();
//    	result.addObject("username","cuowu");
//////    	result.addObject("Model",model);
////    	result.addObject("Html", new HtmlHelperT<TModel>(ViewData));
//    	result.setViewName("Product/register");
//    	return result;
		if(null!=error) {
			if(0<=error.GetValue().indexOf("Duplicate entry '"+username+"' for key 'user_name'")) {
		    	ViewData.put("username", "用户名重复，注册失败");
		    	  return View("Product/register");
				
			}
		}
    	ViewData.put("username", "注册失败");
  	  return View("Product/register");
    }
	

	@SGAllowAnonymous
//	@GetMapping(value = { "/AddUser" })
	@PostMapping(value = { "/LoginUser" })
//	@CrossOrigin
    public ModelAndView LoginUser(String username,String password
    		,String return_to
    		//,String inviteCode
    		)
    {
//		String x=SGDataHelper.ReadLocalTxt("shop/inviteCode.txt", LocalDataType.System).trim();
////		if(null!=inviteCode&&!x.equals(inviteCode)) {
////			inviteCode=null;
////		}
		
//		UserCreate model=new UserCreate();
////		String username=m.getUsername();String password=m.getPassword();String inviteCode=m.getInviteCode();
//		model.setUserName(username);
//		model.setPwd(password);
////		model.setInvitationCode(inviteCode);
////		model.setUserName(m.getUsername());
////		model.setPwd(m.getPassword());
////		model.setInvitationCode(m.getInviteCode());
		SGRef<String> error=new SGRef<String>();
		
		//if(userService.addUser(model,error)) {
		
			ModelAndView r= DoLogin(username, password,return_to);
			if(null!=r) {
				return r;
			}
//	  		BaseReturnInfoDto userSSO = FormsAuth.LoginCheckWebApi(username, password);
//	  		if(userSSO.isIsSuccess()) {
//	  	        LoginerBase userData = new LoginerBase();
//	  	        userData.UserCode=username;
//	  	        userData.UserName=username;
//
//	  	        int effectiveHours = 1;//登陆1小时
//	  	        User user = FormsAuth.checkUser(username,null,false);
//	  	        SystemUser sysUser = new SystemUser();
//	  	    	sysUser.UserCode=user.getUserName();
//	  	    	sysUser.UserName=user.getUserName();
////	  	    	sysUser.Email=user.getEmail();
//	  	    	sysUser.isInvited=!SGDataHelper.StringIsNullOrWhiteSpace(user.getInvitationCode());
//	  	        FormsAuth.SignIn(userData, sysUser, 60 * effectiveHours);
//	  	        if(sysUser.isInvited) {
//	  	        	ViewData.put("username",username);
//	  	        	ViewData.put("isInvited",true);
//	  	        	ViewData.put("role","resource");
//	  	        	ViewData.put("url","/resource-index.html");
//		  	    	  return View("ProductJump/index");
//	  	        }else {
////	  	    	  return View(new LoginerBase(),"Product/index");
//	  	        	
////	              return super.RedirectToUrl("/");//这样前端没有cache,ai的页面用不了
//	  	        	
////	  	        	ViewData.put("inviteCode","");
//	  	        	ViewData.put("username",username);
//	  	        	ViewData.put("isInvited",false);
//	  	        	ViewData.put("role","normal");	  	        	
//	  	        	ViewData.put("url","/");
//	  	    	  return View("ProductJump/index");
//	  	        }
//	  		}
	  		
//		}else {
////	    	return AbstractApiResult.error("error");
////	    	  return View(new LoginerBase(),"Product/register");
//		}	  		

	    	ViewData.put("username", "登陆失败");
  	  return View("Product/login");
    }
	
	private ModelAndView DoLogin(String username,String password,String return_to) {

  		BaseReturnInfoDto userSSO = FormsAuth.LoginCheckWebApi(username, password);
  		if(userSSO.isIsSuccess()) {
  	        LoginerBase userData = new LoginerBase();
  	        userData.UserCode=username;
  	        userData.UserName=username;

  	        int effectiveHours = 1;//登陆1小时
  	        User user = FormsAuth.checkUser(username,null,false);
  	      userData.UserCode=String.valueOf(user.getUserId());
  	      
  	        SystemUser sysUser = new SystemUser();
//  	    	sysUser.UserCode=user.getUserName();
  	    	sysUser.UserCode=userData.UserCode;
  	    	sysUser.UserName=user.getUserName();
//  	    	sysUser.Email=user.getEmail();
  	    	sysUser.isInvited=!SGDataHelper.StringIsNullOrWhiteSpace(user.getInvitationCode());
  	    	sysUser.signDay=user.getSignDay();
  	    	sysUser.lastSign=user.getLastSign();
  	    	SGDate now= SGDate.Now();
  	    	if(
  	    			(user.isVip1()&&null!=user.getVip1_expire()&&0>now.compareTo(user.getVip1_expire()))
  	    			||(user.isVip2()&&null!=user.getVip2_expire()&&0>now.compareTo(user.getVip2_expire()))
  	    			) {
  	    		sysUser.isVip=true;
  	    	}
  	        FormsAuth.SignIn(userData, sysUser, 60 * effectiveHours);
	        	ViewData.put("username",username);
	        	ViewData.put("isInvited",sysUser.isInvited);
	        	
	        if(!SGDataHelper.StringIsNullOrWhiteSpace(return_to)) {
	        	String urlDecode=SGDataHelper.getURLDecoderString(return_to);
	        	return this.RedirectToUrl(urlDecode);
	        }
	        else if(sysUser.isInvited) {
  	        	ViewData.put("isVip",sysUser.isVip);
  	        	ViewData.put("role","resource");
  	        	ViewData.put("url","/resource-index.html");
	  	    	  return View("ProductJump/index");
  	        }else {
  	        	ViewData.put("isVip",sysUser.isVip);
  	        	ViewData.put("role","normal");	  	        	
  	        	ViewData.put("url","/");
  	    	  return View("ProductJump/index");
  	        }
  		}
  		return null;
	}
	
	@GetMapping(value = { "/profile.html" })
    public ModelAndView Profile()
    {
//  	  return View(new LoginerBase(),"Product/register");
  	ModelAndView result=new ModelAndView();
//  	result.addObject("username","cuowu");
////  	result.addObject("Model",model);
//  	result.addObject("Html", new HtmlHelperT<TModel>(ViewData));
  	SystemUser user=GetSystemUser();
  	result.addObject("logged", null!=user&&!SGDataHelper.StringIsNullOrWhiteSpace(user.UserName));
  	result.addObject("signDay", user.signDay);
  	
  	result.addObject("signedToday", null!=user.lastSign&&user.lastSign.isToday());
  	result.addObject("isVip", user.isVip);
  	result.setViewName("Product/profile");
  	return result;
    }
	//------------------vip-----------------------

	@GetMapping(value = { "/vip.html" })
    public ModelAndView Vip()
    {
//  	  return View(new LoginerBase(),"Product/register");
  	ModelAndView result=new ModelAndView();
//  	result.addObject("username","cuowu");
////  	result.addObject("Model",model);
//  	result.addObject("Html", new HtmlHelperT<TModel>(ViewData));
  	result.setViewName("Product/vip");
  	return result;
    }

//	public enum PayPlan{
//		resource_monthly,
//		resource_yearly,
//		ebook_monthly,
//		ebook_yearly
//	}
//	
	/**
	 * 改用 Pay3Controller.PaidReturn
	 * @param plan
	 * @param amount
	 * @return
	 */
	@GetMapping(value = { "/pay.html" })
	@SGAllowAnonymous
    public ModelAndView Pay(PayPlan plan,Integer amount)
    {
//  	  return View(new LoginerBase(),"Product/register");
  	ModelAndView result=new ModelAndView();
//  	result.addObject("username","cuowu");
////  	result.addObject("Model",model);
//  	result.addObject("Html", new HtmlHelperT<TModel>(ViewData));
  	result.setViewName("Product/pay");
  	return result;
    }
}