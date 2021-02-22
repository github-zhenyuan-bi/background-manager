package pro.bzy.boot.framework.utils;

import pro.bzy.boot.framework.config.cache.MyAbstractCache;
import pro.bzy.boot.framework.config.cache.MyCahce;
import pro.bzy.boot.framework.config.cache.ehcache.EhCache;
import pro.bzy.boot.framework.config.cache.redis.RedisCommonCache;
import pro.bzy.boot.framework.utils.parents.MyUtil;

public class CacheUtil implements MyUtil {

    private static final MyCahce cache;
    static {
        String useCache = PropertiesUtil.get(SystemConstant.USER_CACHE);
        if (SystemConstant.CACHE_REDIS.equalsIgnoreCase(useCache)) 
            cache = MyAbstractCache.genCache("REDIS_CACHE_UTIL", RedisCommonCache.class);
        else 
            cache = MyAbstractCache.genCache("EHCACHE_UTIL", EhCache.class);
    }
    
    
    public static void put(Object key, Object value, int expire) {
        cache.put(key, value, expire);
    }
    
    
    public static void put(Object key, Object value) {
        put(key, value, 0);
    }
}
