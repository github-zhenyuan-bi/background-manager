package pro.bzy.boot.framework.config.cache.redis;

import org.apache.ibatis.cache.Cache;

import pro.bzy.boot.framework.config.cache.MyAbstractCacheFactory;
import pro.bzy.boot.framework.config.cache.MyCache;

/**
 * 
 * redis缓存实例化工厂
 * @author user
 *
 */
public class MyRedisCacheFactory extends MyAbstractCacheFactory{

    @Override
    public MyCache getCache(String cacheId) {
        return new RedisCache(cacheId);
    }

    @Override
    public Cache getCacheForMybatis(String cacheId) {
        return new RedisCacheForMybatis(cacheId);
    }

}
