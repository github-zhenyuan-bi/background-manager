package pro.bzy.boot.framework.config.cache.ehcache;

import org.apache.ibatis.cache.Cache;

import pro.bzy.boot.framework.config.cache.MyAbstractCacheFactory;
import pro.bzy.boot.framework.config.cache.MyCache;

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

    @Override
    public MyCache getCache(String cacheId, int expire) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Cache getCacheForMybatis(String cacheId, int expire) {
        // TODO Auto-generated method stub
        return null;
    }

}
