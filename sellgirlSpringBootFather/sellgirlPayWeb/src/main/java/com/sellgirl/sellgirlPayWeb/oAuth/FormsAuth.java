package com.sellgirl.sellgirlPayWeb.oAuth;

import java.util.Map;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sellgirl.sgJavaHelper.FuncAuthorityClass;
import com.sellgirl.sgJavaHelper.SGDataTable;
//import com.sellgirl.sgJavaSpringHelper.JwtHelper;
import com.sellgirl.sgJavaSpringHelper.PFCaching;
import com.sellgirl.sgJavaHelper.SGRef;
//import com.sellgirl.sgJavaSpringHelper.config.PFCookieUtils;
import com.sellgirl.sgJavaSpringHelper.config.SGDataHelper;
//import org.springframework.cache.annotation.Caching;
//import javax.cache.CacheProvider;
import com.sellgirl.sgJavaHelper.model.SystemUser;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlWhereCollection;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;
import com.sellgirl.sgJavaMvcHelper.config.PFCookieUtils;
import com.sellgirl.sellgirlPayWeb.configuration.ProjConfig;
import com.sellgirl.sellgirlPayWeb.configuration.jdbc.JdbcHelper;
import com.sellgirl.sellgirlPayWeb.oAuth.model.ActionReturnInfo;
//import pf.springBoot.springBootSSO.model.Food;
import com.sellgirl.sellgirlPayWeb.oAuth.model.BaseReturnInfoDto;
import com.sellgirl.sellgirlPayWeb.oAuth.model.UserAllInfoDto;
import com.sellgirl.sellgirlPayWeb.projHelper.JwtHelper;
import com.sellgirl.sellgirlPayWeb.user.model.User;
import com.sellgirl.sellgirlPayWeb.user.service.UserService;
import com.alibaba.fastjson.JSONObject;
//import com.report.api.ws.WsUserApi;
//import com.report.model.system.AmsRoleMenuDto;
//import com.report.model.system.RoleMenuDto;
//import com.report.model.system.UserDto;
//import com.report.model.system.UserRoleDto;
//import com.report.model.ws.t_bd_ActionDto;
//import com.report.model.ws.t_bd_AppModuleDto;
//import com.report.ws.dao.ActionMapper;
//import com.report.ws.dao.ModuleMapper;
//import com.report.ws.dto.*;
//import org.apache.dubbo.config.annotation.DubboService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import java.util.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
//import com.perfect99.right.amsweb.BaseReturnInfo;
//import com.sellgirl.sgJavaSpringHelper.FuncAuthority;
//import javax.cache.Cache;
//import javax.cache.CacheManager;
//import javax.cache.Caching;
//import javax.cache.configuration.MutableConfiguration;
//import javax.cache.spi.CachingProvider;

//import com.perfect99.right.amsweb.ActionReturnInfo;
//import com.perfect99.right.amsweb.ActionValidate;
//import com.perfect99.right.amsweb.ActionValidateSoap;


@Component
public class FormsAuth {

////	 private static PFCookieUtils _cookieUtils;
//    private static  ActionValidateSoap _service;
    private static String cookieKey="cookieKey";//相当于FormsAuthentication.FormsCookieName
//
////    @Value("${service.auth.apiUrl}")
//    public String authApiUrl;
////    @Autowired
////    RestTemplate restTemplate;
////    private static  ProjConfig projConfig;
    private static  String authApiUrl;
//	  private static UserService userService;

	@Autowired
	public FormsAuth(//PFCookieUtils cookieUtils
			ProjConfig projConfig//,UserService userService
			)
    {
		
		//_cookieUtils=cookieUtils;

//    	ActionValidate factory = new ActionValidate();
//        //根据工厂创建一个WeatherWSSoap对象
//    	ActionValidateSoap weatherWSSoap = factory.getActionValidateSoap();
//    	 _service= weatherWSSoap;
    	 
////    	 FormsAuth.projConfig= projConfig;
    	 authApiUrl=projConfig.getAuthApiUrl();
//    	 this.userService=userService;
    }
//	/**
//	 * 有调用
//	 * 改用WebLoginCheck
//	 * @param userid
//	 * @param password
//	 * @return
//	 */
//	@Deprecated
//    public static BaseReturnInfo LoginCheck(String userid,String password)
//    {
//
//    	BaseReturnInfo returnInfo = SGDataHelper.UseLocalData()
//          ? JSON.parseObject(SGDataHelper.ReadLocalJson("LoginCheck.json"), BaseReturnInfo.class) //本地方法待写-- benjamin todo
//          : _service.loginValidate(userid, password);
//    	
////		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();  
////		Client client = dcf.createClient("XXX.wsdl");  
////		/*echo: 方法名
////		 *test echo : 参数
////		*/
////		Object[] res = client.invoke("echo", "test echo");  
////		System.out.println("Echo response: " + res[0]);
//		
//		return returnInfo;
//
//    }

	/**
	 * 验证密码，内部可选择调用3方sso登陆接口
	 * @param userno
	 * @param password
	 * @return
	 */
    public static BaseReturnInfoDto LoginCheckWebApi(String userno, String password) {
    	if(SGDataHelper.UseLocalData()) {
//    		return JSON.parseObject(SGDataHelper.ReadLocalJson("LoginCheck.json"), BaseReturnInfoDto.class);
    		return JSON.parseObject(SGDataHelper.ReadLocalJsonFromClassSource("LoginCheck.json"), BaseReturnInfoDto.class);
    	}
//        //chen chao系统的登陆
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("loginNo", userno);
//        jsonObject.put("password", password);
//        String data = jsonObject.toJSONString();
//        //
//        BaseReturnInfoDto baseReturnInfo = getApiData("LoginValidate", data, BaseReturnInfoDto.class);
        
        BaseReturnInfoDto baseReturnInfo=new BaseReturnInfoDto();
        User user=checkUser(userno,password,true);
        if(null!=user) {
        	baseReturnInfo.setIsSuccess(true);
        	baseReturnInfo.setUserId(user.getUserName());
        	baseReturnInfo.setUserName(user.getUserName());
        	baseReturnInfo.setUserNumber(user.getUserName());
        }else {
        	baseReturnInfo.setIsSuccess(false);
        }
//        assert baseReturnInfo != null;
//        return baseReturnInfo.isIsSuccess();
        return baseReturnInfo;
    }
//    public static BaseReturnInfoDto CheckPwdLogin(String userno, String password) {
//    	if(SGDataHelper.UseLocalData()) {
////    		return JSON.parseObject(SGDataHelper.ReadLocalJson("LoginCheck.json"), BaseReturnInfoDto.class);
//    		return JSON.parseObject(SGDataHelper.ReadLocalJsonFromClassSource("LoginCheck.json"), BaseReturnInfoDto.class);
//    	}
////        //chen chao系统的登陆
////        JSONObject jsonObject = new JSONObject();
////        jsonObject.put("loginNo", userno);
////        jsonObject.put("password", password);
////        String data = jsonObject.toJSONString();
////        //
////        BaseReturnInfoDto baseReturnInfo = getApiData("LoginValidate", data, BaseReturnInfoDto.class);
//        
//        BaseReturnInfoDto baseReturnInfo=new BaseReturnInfoDto();
//        User user=checkUser(userno,password,true);
//        if(null!=user) {
//        	baseReturnInfo.setIsSuccess(true);
//        	baseReturnInfo.setUserId(user.getUserName());
//        	baseReturnInfo.setUserName(user.getUserName());
//        	baseReturnInfo.setUserNumber(user.getUserName());
//        	
//        }else {
//        	baseReturnInfo.setIsSuccess(false);
//        }
////        assert baseReturnInfo != null;
////        return baseReturnInfo.isIsSuccess();
//        return baseReturnInfo;
//    }

	public static User checkUser(String userName,String pwd,boolean checkPwd) {
		ISGJdbc dstJdbc=JdbcHelper.GetShop();
		try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
			SGSqlWhereCollection query =myResource.getWhereCollection();
			query.setIgnoreNullValue(false);
            query.Add("user_name",userName);
            if(checkPwd) {
            	query.Add("pwd",pwd);
            }
            String SqlString = SGDataHelper.FormatString( 
            		"select * from sg_user " +
            		"{0} " 
            		, 
            		        query.ToSql()
            		    );
            SGDataTable dt= myResource.GetDataTable(SqlString,null);
            if(null!=dt&&!dt.IsEmpty()) {
            	List<User> list= dt.ToList(User.class, (obj,row,c)->{
            		obj.setUserName(row.getStringColumn("user_name"));
            		obj.setInvitationCode(row.getStringColumn("invitation_code"));
            		obj.setSignDay(SGDataHelper.ObjectToInt(row.getColumn("sign_day")));
            		obj.setLastSign(com.sellgirl.sgJavaHelper.config.SGDataHelper.ObjectToSGDate(row.getColumn("last_sign")));
            	});
            	return list.get(0);
            }else {
            	return null;
            }
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	


//    /**
//     * 有调用
//     * 改用GetUserInfoWebApi
//     * @param userid
//     * @return
//     */
//	@Deprecated
//    public static com.perfect99.right.amsweb.UserAllInfo GetUserInfo(String userid)
//    {
//        return _service.getUserInfo(userid);
//    }
	/**
	 * 改用checkUser
	 * @param userno
	 * @return
	 */
	@Deprecated
    public static UserAllInfoDto GetUserInfoWebApi(String userno) {
        //
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("idOrNumber", userno);
        String data = jsonObject.toJSONString();
        //
        if(SGDataHelper.UseLocalData()) {
        	UserAllInfoDto user=new UserAllInfoDto();
        	user.setEMail("li@sellgirl.com");
            return user;
        }else {
	        UserAllInfoDto user = getApiData("GetUserInfo", data, UserAllInfoDto.class);
	        return user;
        }
    }

//    public static ActionReturnInfo GetAllAction(String userid)
//    {
//    	ActionReturnInfo returnInfo = SGDataHelper.UseLocalData()
//            ? JSON.parseObject(SGDataHelper.ReadLocalJson("GetAllAction.json"),ActionReturnInfo.class)
//            :_service.getAllAction(userid, "YJQ",null,null,null,null,null);
//        //var returnInfo = _service.GetAllAction(userid, "YJQ",null,null,null,null,null);
//        return returnInfo;
//    }
    //暂时用本地帐号
    public static ActionReturnInfo GetAllAction(String userid)
    {
//    	ActionReturnInfo returnInfo = SGDataHelper.UseLocalData()
//            ? JSON.parseObject(SGDataHelper.ReadLocalJson("GetAllAction.json"),ActionReturnInfo.class)
//            :_service.getAllAction(userid, "YJQ",null,null,null,null,null);
//        //var returnInfo = _service.GetAllAction(userid, "YJQ",null,null,null,null,null);

    	ActionReturnInfo returnInfo =
             JSON.parseObject(SGDataHelper.ReadLocalJson("GetAllAction.json"),ActionReturnInfo.class)
            ;
        return returnInfo;
    }

    /**
     * 前面已经验证过密码的话，可以调用此方法写用户信息到cookie
     * @param userData
     * @param exData
     * @param expireMin
     */
//    @Deprecated
    public static void SignIn(LoginerBase userData, Object exData, int expireMin)
    {
        String loginName = userData.UserCode;
        String data = JSON.toJSONString(userData);

        PFCookieUtils.setCookie( cookieKey, data);//待加密--benjamin todo
        
//        //创建一个FormsAuthenticationTicket，它包含登录名以及额外的用户数据。
//        var ticket = new FormsAuthenticationTicket(2,
//            loginName, DateTime.Now, DateTime.Now.AddDays(1), true, data);
//
//        //加密Ticket，变成一个加密的字符串。
//        var cookieValue = FormsAuthentication.Encrypt(ticket);
//
//        //根据加密结果创建登录Cookie
//        var cookie = new HttpCookie(FormsAuthentication.FormsCookieName, cookieValue)
//        {
//            HttpOnly = true,
//            Secure = FormsAuthentication.RequireSSL,
//            Domain = FormsAuthentication.CookieDomain,
//            Path = FormsAuthentication.FormsCookiePath
//        };
//        if (expireMin > 0)
//            cookie.Expires = DateTime.Now.AddMinutes(expireMin);
//
//        var context = HttpContext.Current;
//        if (context == null)
//            throw new InvalidOperationException();
//
//        //写登录Cookie
//        context.Response.Cookies.Remove(cookie.Name);
//        context.Response.Cookies.Add(cookie);

        
//        
//        //设置actions(注意actions不能放在FormsAuthenticationTicket之中,因为数据太大导致登陆失败 
//        //var allAction = _service.GetAllAction(loginName, "YJQ", null, null, null, null, null);
//        ActionReturnInfo allAction = GetAllAction(loginName);

//        //GetFuncAuthority还没写好,先注释--benjamin 
//        if (allAction != null //&&allAction.isIsSuccess()
//        		&& allAction.getActions() != null  //todo
//        		)
//        {
//        	List<String> actions =SGDataHelper.ListSelect(allAction.getActions().getActionInfo(), a -> a.getNumber());
//        	Map<String, List<String>> otherAuthority = new HashMap<String, List<String>>();
//        	SGRef<Map<String, List<String>>> rOtherAuthority=new SGRef<Map<String, List<String>>>();
//        	Map<String, FuncAuthorityClass> authority = SGDataHelper.GetFuncAuthorityClass(actions,rOtherAuthority);
//            PFCaching.Set(loginName + "_FuncAuthorities", authority);
//            PFCaching.Set(loginName + "_OtherFuncAuthorities", otherAuthority);
//        }

        PFCaching.Set(loginName, exData);

    }

//    public static void SignIn2(LoginerBase userData,
//    		Object exData, int expireMin)
//    {
//        String loginName = userData.UserCode;
//        String data = JSON.toJSONString(userData);
//
//        PFCookieUtils.setCookie( cookieKey, data);//待加密--benjamin todo
//        
////        //创建一个FormsAuthenticationTicket，它包含登录名以及额外的用户数据。
////        var ticket = new FormsAuthenticationTicket(2,
////            loginName, DateTime.Now, DateTime.Now.AddDays(1), true, data);
////
////        //加密Ticket，变成一个加密的字符串。
////        var cookieValue = FormsAuthentication.Encrypt(ticket);
////
////        //根据加密结果创建登录Cookie
////        var cookie = new HttpCookie(FormsAuthentication.FormsCookieName, cookieValue)
////        {
////            HttpOnly = true,
////            Secure = FormsAuthentication.RequireSSL,
////            Domain = FormsAuthentication.CookieDomain,
////            Path = FormsAuthentication.FormsCookiePath
////        };
////        if (expireMin > 0)
////            cookie.Expires = DateTime.Now.AddMinutes(expireMin);
////
////        var context = HttpContext.Current;
////        if (context == null)
////            throw new InvalidOperationException();
////
////        //写登录Cookie
////        context.Response.Cookies.Remove(cookie.Name);
////        context.Response.Cookies.Add(cookie);
//
//        
//        
//        //设置actions(注意actions不能放在FormsAuthenticationTicket之中,因为数据太大导致登陆失败 
//        //var allAction = _service.GetAllAction(loginName, "YJQ", null, null, null, null, null);
//        ActionReturnInfo allAction = GetAllAction(loginName);
//
////        //GetFuncAuthority还没写好,先注释--benjamin 
////        if (allAction != null //&&allAction.isIsSuccess()
////        		&& allAction.getActions() != null  //todo
////        		)
////        {
////        	List<String> actions =SGDataHelper.ListSelect(allAction.getActions().getActionInfo(), a -> a.getNumber());
////        	Map<String, List<String>> otherAuthority = new HashMap<String, List<String>>();
////        	SGRef<Map<String, List<String>>> rOtherAuthority=new SGRef<Map<String, List<String>>>();
////        	Map<String, FuncAuthorityClass> authority = SGDataHelper.GetFuncAuthorityClass(actions,rOtherAuthority);
////            PFCaching.Set(loginName + "_FuncAuthorities", authority);
////            PFCaching.Set(loginName + "_OtherFuncAuthorities", otherAuthority);
////        }
//
//        PFCaching.Set(loginName, exData);
//
//    }
    
//
    public static void SingOut()
    {
        String userId = GetUserData().UserCode;
        
        //FormsAuthentication.SignOut();
        if (!SGDataHelper.StringIsNullOrWhiteSpace(userId))
        {
            PFCookieUtils.removeCookie( cookieKey);//待加密--benjamin todo
            //var context = HttpContext.Current;
            //context.Session.RemoveAll();
            PFCookieUtils.removeCookie(userId + "_FuncAuthorities");
            PFCookieUtils.removeCookie(userId + "_OtherFuncAuthorities");
            PFCookieUtils.removeCookie(userId);
        }
        //using (var redisClient = RedisManager.GetClient())
        //{
        //    redisClient.Dispose();
        //}
    }
//
    public static LoginerBase GetUserData()
    {
//    	LoginerBase ud=new LoginerBase();
//    	ud.UserCode="1712002";
//    	ud.UserName="吴肖均";
//    	return ud;
        return GetUserDataT(LoginerBase.class);//benjamin todo
    }

//    @Autowired
//    private PFCookieUtils cookieUtils;
    public static  <T> T GetUserDataT(Class<T> cl) 
    {
        T UserData = null;
        try
        {
        	UserData=cl.newInstance();
        	
        	//String ticket=_cookieUtils.getCookieValue(cookieKey);//虽然断点看到_cookieUtils为空,但实际不报错
        	String ticket=PFCookieUtils.getCookieValue(cookieKey);//虽然断点看到_cookieUtils为空,但实际不报错
        	if(!SGDataHelper.StringIsNullOrWhiteSpace(ticket)) {
            	//List<Map> meterList= JSONArray.parseArray(paraMap.get("metersList").toString(),Map.class);
            	UserData = JSON.parseObject(ticket, cl);
        	}
    //        var context = HttpContext.Current;
        		
        	//benjamin todo
            //#region 如果用这种写法，当某Action中有两特性，第一个特性中AutoLogin执行后，第二个特性MvcMenuFilter里的用户名还是旧用户(没有实时更新)
//            var cookie = context.Request.Cookies[FormsAuthentication.FormsCookieName];
//            var ticket = FormsAuthentication.Decrypt(cookie.Value);
//            UserData = JsonConvert.DeserializeObject<T>(ticket.UserData);
            //#endregion
  
        }
        catch(Exception e)
        { }

        return UserData;
    }
    public static <T> T GetUserExData( Class<T> cl) 
    {
        T UserData =null;
        try
        {
        	UserData=cl.newInstance();
            String userCode = GetUserData().UserCode;
            if (!SGDataHelper.StringIsNullOrWhiteSpace(userCode))
            {
                Object data = PFCaching.Get(userCode);//应避免把数据保存在key=null的cache中
                if(data!=null) {
                    UserData = cl.cast(data) ;	
                }
            }
            //if (data != null) { UserData = JsonConvert.DeserializeObject<T>(data.ToString()); }                
        }
        catch(Exception e)
        { }

        return UserData;
    }
    /**
     * 有调用
     * @return
     */
    public static Map<String, FuncAuthorityClass> GetFuncAuthorities() 
    {
        String userId = GetUserData().UserCode;
        
//        CachingProvider cachingProvider = Caching.getCachingProvider();
//        
//        CacheManager cacheManager = cachingProvider.getCacheManager();
//         
//        MutableConfiguration<String, String> config = new MutableConfiguration();
//         
//        Cache<String, String> cache = cacheManager.createCache("JDKCodeNames",config);
//         
//        cache.put("JDK1.5","Tiger");
//         
//        cache.put("JDK1.6","Mustang");
//         
//        cache.put("JDK1.7","Dolphin");
//         
//        String jdk7CodeName = cache.get("JDK1.7");
        
        
        Object funcAuthorities=PFCaching.Get(userId + "_FuncAuthorities");
        if(funcAuthorities==null) {return null;}
        //return (Map<String, FuncAuthorityClass>)funcAuthorities;
        return SGDataHelper.ObjectAs(funcAuthorities);
    }
    public static Map<String, List<String>> GetOtherFuncAuthorities()
    {
    	String userId = GetUserData().UserCode;
        Object funcAuthorities =PFCaching.Get(userId + "_OtherFuncAuthorities") ;
        if(funcAuthorities==null) {return null;}
        return SGDataHelper.ObjectAs(funcAuthorities);
    }
    public static void AddFuncAuthorities(String key, FuncAuthorityClass authority)
    {
    	String userId = GetUserData().UserCode;
        Object funcAuthoritiesObj = PFCaching.Get(userId + "_FuncAuthorities")  ;
        Map<String, FuncAuthorityClass> funcAuthorities=null;
        if(funcAuthoritiesObj!=null) {
        	 //funcAuthorities = (Map<String, FuncAuthorityClass>)funcAuthoritiesObj; 
        	 funcAuthorities = SGDataHelper.ObjectAs(funcAuthoritiesObj); 
        }else {
        	 funcAuthorities = new HashMap<String, FuncAuthorityClass>();
        }

        //if (funcAuthorities == null) { funcAuthorities = new HashMap<String, FuncAuthority>(); }
        if (funcAuthorities.containsKey(key))
        {
        	//Food a=Food.Coffee.BLACK_COFFEE;
        	FuncAuthorityClass old=funcAuthorities.get(key);
        	funcAuthorities.replace(key,old.Or( authority));
            //funcAuthorities[key] |= authority;
        }else
        {
            funcAuthorities.put(key, authority);
        }
    }

    //public static ActionReturnInfo GetUserActions()
    //{
    //    ActionReturnInfo result = null;
    //    using (var redisClient = RedisManager.GetClient())
    //    {
    //        result = redisClient.Get<ActionReturnInfo>("userAction_"+ GetUserData().UserCode);
    //    }
    //    return  result;
    //}

    public static String SignUserToken(SystemUser user)
    {
    	Calendar c=Calendar.getInstance();
    	c.add(Calendar.MINUTE, +60);
//    	Date expireDatePoint=c.getTime();

//    	String exp=c.getInstance().toEpochMilli();
    	//long seconds=c.getTimeInMillis()/1000;
    	
    	HashMap<String, String> payload = new HashMap<String, String>(){/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
    		put("SysUser",JSON.toJSONString(user));
    	}};
    	return JwtHelper.genToken(payload,c.getTime(), MetabaseDataHelper.METABASE_JWT_SHARED_SECRET);
    	
    	//return JwtHelper.genTokenForObject(user,c.getTime(), MetabaseDataHelper.METABASE_JWT_SHARED_SECRET);
    	
//    	HashMap<String, String> payload = new HashMap<String, String>()
//    	//HashMap<String, Object> payload = new HashMap<String, Object>()
//        {
//    		{
//            put( "UserCode",user.UserCode );
//            put( "UserName",user.UserName );
//            put( "Email",user.Email );
//            put( "Sf",user.Sf );
//            put( "SfName",user.SfName );
//            put( "Fgs",user.Fgs );
//            put( "FgsName",user.FgsName );
//            put( "Org",user.Org );
//            put( "OrgName",user.OrgName );
//            //put( "OrgList",user.OrgList );
//            //put( "exp",seconds);
//            
////            put( "exp", DateTimeOffset.UtcNow.AddMinutes(10).ToUnixTimeSeconds());//到期时间
//            //put( "exp",String.valueOf(exp));//到期时间 报错:java.lang.String cannot be cast to java.util.Date
//            //put( "exp",c.getTime().toString());//到期时间
//        	}
//        };
//
//        //生成sso登陆用的JWT
//        return JwtHelper.genToken(payload,c.getTime(), MetabaseDataHelper.METABASE_JWT_SHARED_SECRET);
    }

    /// <summary>
    /// 已登陆
    /// </summary>
    /// <returns></returns>
    public static Boolean IsLogined()
    {
    	SGRef<SystemUser> userData=new SGRef<SystemUser>();
    	return IsUserLogined(userData);
//        //var userData = FormsAuth.GetUserData();
//    	SystemUser userData = FormsAuth.GetUserExData(SystemUser.class);
//        return !(userData == null || SGDataHelper.StringIsNullOrWhiteSpace(userData.UserCode));
    }
    public static Boolean IsUserLogined(SGRef<SystemUser> userData)
    {
//        //var userData = FormsAuth.GetUserData();
//    	SystemUser userData = FormsAuth.GetUserExData(SystemUser.class);
    	userData.SetValue(FormsAuth.GetUserExData(SystemUser.class));
        return !(userData == null || SGDataHelper.StringIsNullOrWhiteSpace(userData.GetValue().UserCode));
    }    
    
    
    /**
     * 获取权限系统Api总出口，ChenChao
     * 
     * 感觉可以作为通用的http请求方法 Benjamin
    *
    * @param functionUrl
    * @param data
    * @param clazz
    * @param <T>
    * @return
    */
   private static <T> T getApiData(String functionUrl, String data, Class<T> clazz) {
       try {
           String url = authApiUrl + functionUrl;
           HttpHeaders headers = new HttpHeaders();
           headers.setContentType(MediaType.APPLICATION_JSON);
           HttpEntity<String> httpEntity = new HttpEntity<>(data, headers);
           RestTemplate restTemplate = new RestTemplate();
           ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
           String bodyData = responseEntity.getBody();
           if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
               System.out.println(bodyData);
               return null;
           }
           assert bodyData != null;
           bodyData = bodyData.replace("\\\"", "\"").trim();
           if (bodyData.startsWith("\""))
               bodyData = bodyData.substring(1);
           if (bodyData.endsWith("\""))
               bodyData = bodyData.substring(0, bodyData.length() - 1);
           return JSONObject.parseObject(bodyData, clazz);
       } catch (Exception ex) {
           ex.printStackTrace();
           return null;
       }
   }

}
