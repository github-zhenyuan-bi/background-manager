package pro.bzy.boot.framework.utils;

import pro.bzy.boot.framework.config.cache.MyCache;
import pro.bzy.boot.framework.config.cache.ehcache.MyEhCacheFactory;
import pro.bzy.boot.framework.config.cache.redis.MyRedisCacheFactory;
import pro.bzy.boot.framework.utils.parents.MyUtil;

public class CacheUtil implements MyUtil {

    private static final MyCache cache;
    static {
        String useCache = PropertiesUtil.get(SystemConstant.USER_CACHE);
        if (SystemConstant.CACHE_REDIS.equalsIgnoreCase(useCache)) 
            cache = new MyRedisCacheFactory().getCache("REDIS_CACHE_UTIL");
        else 
            cache = new MyEhCacheFactory().getCache("EHCACHE_UTIL");
    }
    
    
    public static final void put(Object key, Object value, int expire) {
        cache.put(key, value, expire);
    }
    
    
    public static final void put(Object key, Object value) {
        put(key, value, 0);
    }
    
    
    public static final Object get(Object key) {
        return cache.get(key);
    }
    
    
    public static final <T> T get(Object key, Class<T> clazz) {
        return cache.get(key, clazz);
    }
}
