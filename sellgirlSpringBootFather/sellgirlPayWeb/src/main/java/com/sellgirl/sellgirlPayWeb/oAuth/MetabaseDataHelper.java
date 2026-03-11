package com.sellgirl.sellgirlPayWeb.oAuth;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

//import com.sellgirl.sgJavaSpringHelper.JwtHelper;
import com.sellgirl.sgJavaSpringHelper.config.SGDataHelper;
import com.sellgirl.sellgirlPayWeb.projHelper.*;


public class MetabaseDataHelper {

    public static String METABASE_URL = "http://cloud.perfect99.com:10003";
    public static String METABASE_JWT_SHARED_SECRET = "aca2bb4a215c5eafc4512c16ca60dda024e9675f677aa3ac39de85ca84ab9462";

    /// <summary>
    /// 格式如 {0}/auth/sso?jwt={1}&return_to={2}
    /// </summary>
    /// <param name="metabaseUser"></param>
    /// <param name="return_to"></param>
    /// <returns></returns>
    public static String GetSsoJwtUrl(MetabaseUser metabaseUser,String return_to )
    {
    	String url =SGDataHelper.FormatString("{0}/auth/sso?jwt={1}&return_to={2}",
            MetabaseDataHelper.METABASE_URL,
            SignUserToken(metabaseUser),
            return_to
            );

//        SGDataHelper.WriteLog(string.Format(@"
//MetabaseDataHelper.GetSsoJwtUrl :
//MetabaseDataHelper.METABASE_URL={0}
//metabaseUser={1}
//return_to={2}
//", MetabaseDataHelper.METABASE_URL, JsonConvert.SerializeObject(metabaseUser),return_to));
        return url;
    }
    public static String SignUserToken(MetabaseUser user)
    {
    	Calendar c=Calendar.getInstance();
    	c.add(Calendar.MINUTE, +10);
//    	Date expireDatePoint=c.getTime();
        //这里打包metabase所需的用户登陆信息（考虑根据当前系统的用户信息来组合user）
//    	String exp=c.getInstance().toEpochMilli();
    	long seconds=c.getTimeInMillis()/1000;

    	HashMap<String, String> payload = new HashMap<String, String>()
        {
    		{
            put( "email",user.email );
            put( "first_name",user.first_name );
            put( "last_name",user.last_name );
            //put( "exp",seconds);
            
//            put( "exp", DateTimeOffset.UtcNow.AddMinutes(10).ToUnixTimeSeconds());//到期时间
            //put( "exp",String.valueOf(exp));//到期时间 报错:java.lang.String cannot be cast to java.util.Date
            //put( "exp",c.getTime().toString());//到期时间
        	}
        };
//        
//    	HashMap<String, Object> payload = new HashMap<String, Object>()
//        {
//    		{
//            put( "email",user.email );
//            put( "first_name",user.first_name );
//            put( "last_name",user.last_name );
//            put( "exp",seconds);
//            
////            put( "exp", DateTimeOffset.UtcNow.AddMinutes(10).ToUnixTimeSeconds());//到期时间
//            //put( "exp",String.valueOf(exp));//到期时间 报错:java.lang.String cannot be cast to java.util.Date
//            //put( "exp",c.getTime().toString());//到期时间
//        	}
//        };
        //生成sso登陆用的JWT
       // return JwtHelper.CreateJWT2(payload, MetabaseDataHelper.METABASE_JWT_SHARED_SECRET);
        return JwtHelper.genToken(payload,c.getTime(), MetabaseDataHelper.METABASE_JWT_SHARED_SECRET);
    }
}
