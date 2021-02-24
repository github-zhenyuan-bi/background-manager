package pro.bzy.boot.framework.config.cache;

import org.apache.ibatis.cache.Cache;

/**
 * 
 * 缓存对象抽象工厂
 * @author user
 *
 */
public abstract class MyAbstractCacheFactory {

    /** 获取一个缓存对象 */
    public abstract MyCache getCache(String cacheId);
    
    /** 获取一个一共给mybatis二级缓存的对象 */
    public abstract Cache getCacheForMybatis(String cacheId); 
}
