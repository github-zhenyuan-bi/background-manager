package pro.bzy.boot.framework.config.cache.ehcache;

import org.apache.ibatis.cache.Cache;

import pro.bzy.boot.framework.config.cache.MyAbstractCacheFactory;

/**
 * 
 * ehCache工厂 生产ehcache实例
 * @author user
 *
 */
public class MyEhCacheFactory extends MyAbstractCacheFactory{

    @Override
    public EhCache getCache(String cacheId) {
        return new EhCache(cacheId);
    }

    @Override
    public Cache getCacheForMybatis(String cacheId) {
        return new EhCacheForMybatis(cacheId, getCache(cacheId));
    }

}
