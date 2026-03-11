package com.sellgirl.sgJavaSpringHelper;

//import javax.cache.Cache;
//import javax.cache.CacheManager;
//import javax.cache.Caching;
//import javax.cache.configuration.MutableConfiguration;
//import javax.cache.spi.CachingProvider;


/**
 * 
     * Cookie是自动区分用户，自动绑定访问的用户，所以key用固定的就行了，但容量小(且必须注入)
     * Cache是服务器的cache, 所以需要用户名做key,但能存放更大的数据
 */
public class PFCaching {

    //private static readonly String appKey = ConfigurationManager.AppSettings["AppKey"];
//    @Autowired
//    private static PFAppConfig _appConfig;
//	private static Cache<String, Object> _cache;
//	public PFCaching() {
//
//        CachingProvider cachingProvider = Caching.getCachingProvider();
//        
//        CacheManager cacheManager = cachingProvider.getCacheManager();
//         
//        MutableConfiguration<String, Object> config = new MutableConfiguration();
//         
//        //Cache<String, String> cache = cacheManager.createCache("JDKCodeNames",config);
//        _cache = cacheManager.createCache(_appConfig.getAppKey(),config);
//	}
    public static Object Get(String key)
    {
    	Cache cache=CacheManager.getCacheInfo(key);
    	if(cache==null) {return null;}
    	if(cache.isExpired()) {return null;}
    	return cache.getValue();
//    	return CacheManager.getCacheInfo(key);
//    	//return _cache.get(key);
//        //return HttpContext.Current.Cache.Get(appKey + key);
    }

    public static void Set(String key, Object value)//, CacheDependency dependency)
    {

        if (value == null)//value为null时，Cache.Insert会报错
        {
            Remove(key);
        }
        else
        {
            long nowDt = System.currentTimeMillis(); //系统当前的毫秒数 
        	CacheManager.putCache(key, new Cache(key,value,(1000*60*60*24)+nowDt,false));//一天过期
        	//_cache.put(key, value);
            
            //HttpRuntime.Cache.Insert(appKey + key, value, dependency, DateTime.Now.AddDays(1), TimeSpan.Zero, CacheItemPriority.High, null);
        }
    }

    public static void Remove(String key)
    {
    	CacheManager.clearOnly(key);
//        if (_cache.containsKey(key))
//        {
//           _cache.remove(key);
//        }
    }
}
