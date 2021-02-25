package pro.bzy.boot.framework.config.cache.redis;


import org.apache.ibatis.cache.Cache;

import pro.bzy.boot.framework.config.cache.MyAbstractCacheFactory;
import pro.bzy.boot.framework.config.cache.MyCache;
import pro.bzy.boot.framework.config.cache.annotation.CacheProperty;
import pro.bzy.boot.framework.utils.PropertiesUtil;

/**
 * 
 * redis缓存实例化工厂
 * @author user
 *
 */
public class MyRedisCacheFactory extends MyAbstractCacheFactory{

    @Override
    public MyCache getCache(String cacheId) {
        return getCache(cacheId, PropertiesUtil.getRedisExpireFormYml());
    }

    @Override
    public MyCache getCache(String cacheId, int expire) {
        return new RedisCache(cacheId, expire);
    }

    
    @Override
    public Cache getCacheForMybatis(String cacheId) {
        CacheProperty cacheProperty = getCacheTargetClassCachePropertyAnnotation(cacheId);
        
        int expire = 0;
        if (cacheProperty != null && cacheProperty.expire() != 0) {
            // 注解配置
            expire = cacheProperty.expire();
        } else {
            // yml配置
            expire = PropertiesUtil.getRedisExpireFormYml();
        }
        return getCacheForMybatis(cacheId, expire);
    }

    @Override
    public Cache getCacheForMybatis(String cacheId, int expire) {
        return new RedisCacheForMybatis(cacheId, getExtraRamdomExpire(expire));
    }

    
    
}
