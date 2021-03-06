package pro.bzy.boot.framework.config.mybatis;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.ibatis.cache.Cache;

import pro.bzy.boot.framework.config.cache.ehcache.MyEhCacheFactory;
import pro.bzy.boot.framework.config.cache.redis.MyRedisCacheFactory;
import pro.bzy.boot.framework.config.constant.CACHE_constant;
import pro.bzy.boot.framework.utils.PropertiesUtil;

/**
 * Mybatis缓存控制类
 */
public class MybatisCacheConfig implements Cache{
    
    /**
     * 实际缓存对象 由配置文件来实例化 可选 ehcache(默认) redis
     */
    private Cache cache;
    
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    
    /**
     * constructor
     * @param id
     */
    public MybatisCacheConfig(final String id) {
        String useCache = PropertiesUtil.get(CACHE_constant.USER_CACHE);
        if (CACHE_constant.CACHE_REDIS.equalsIgnoreCase(useCache)) 
            cache = new MyRedisCacheFactory().getCacheForMybatis(id);
        else 
            cache = new MyEhCacheFactory().getCacheForMybatis(id);
    }
    

    
    /**
     * 存入缓存
     */
    @Override
    public void putObject(Object arg0, Object arg1) {
        cache.putObject(arg0, arg1);
    }
    
    
    
    /**
     * 获取缓存
     */
    @Override
    public Object getObject(Object arg0) {
        return cache.getObject(arg0);
    }



    /**
     * 移除缓存
     */
    @Override
    public Object removeObject(Object arg0) {
        return cache.removeObject(arg0);
    }

    
    
    /**
     * 清空缓存
     */
    @Override
    public void clear() {
        cache.clear();
    }
    
    
    
    /**
     * 获取缓存数量
     */
    @Override
    public int getSize() {
        return cache.getSize();
    }

    
    
    @Override
    public String getId() {
        return cache.getId();
    }
    
    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }
}
