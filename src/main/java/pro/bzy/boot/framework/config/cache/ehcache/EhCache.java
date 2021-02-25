package pro.bzy.boot.framework.config.cache.ehcache;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import pro.bzy.boot.framework.config.cache.MyCacheSupport;
import pro.bzy.boot.framework.config.cache.MyCache;
import pro.bzy.boot.framework.config.exceptions.MyCacheException;
import pro.bzy.boot.framework.utils.SpringContextUtil;

@Slf4j
public class EhCache extends MyCacheSupport implements MyCache {

    
    /** 内置缓存对象 */
    private Cache cache;
    
    /** 缓存对象id */
    private String cacheId;
    
    /** 缓存实例过期时间 */
    private Integer expire;
    
    /** cache初始化锁 */
    private Object lock = new Object();
    
    
    /** 构造器 */
    public EhCache(String cacheId) {
        this.cacheId = cacheId;
    }
    
    /** 构造器 */
    public EhCache(String cacheId, int expire) {
        this.cacheId = cacheId;
        this.expire = expire;
        log.info("【创建缓存对象-ehCache】=> [id]: {}, [expire]: {}", cacheId, expire);
        if (expire <= 0)
            log.warn("当前缓存对象【{}】设定超时时间小于0，无意义，实际永久存活", cacheId);
    }
    
    
    /** 获取缓存实例 */
    private Cache getCache() {
        if (cache == null) {
            synchronized (lock) {
                if (cache == null) {
                    EhCacheConfig ehCacheConfig = SpringContextUtil.getBean(EhCacheConfig.class);
                    cache = ehCacheConfig.cacheRegister(cacheId);  
                }
            }
        }
        return cache;
    }
    
    
    @Override
    public void put(Object key, Object value, int expire) throws MyCacheException {
        nullKeyCheck(key);
        getCache().put(new Element(key, value, false, 0, expire));
        logPut(log, key, value, expire);
    }

    
    @Override
    public void put(Object key, Object value) throws MyCacheException {
        put(key, value, this.expire);
    }

    
    @Override
    public Object get(Object key) throws MyCacheException {
        Object value = getCache().get(key);
        logGet(log, key, value);
        return value;
    }

    
    @Override
    public <T> T get(Object key, Class<T> clazz) throws MyCacheException {
        Object value = get(key);
        if (value != null && clazz.equals(value.getClass())) {
            T castValue = null;
            try {
                castValue = clazz.cast(value);
                return castValue;
            } catch (Exception e) {
                throw new MyCacheException("缓存值与目标值类型不匹配");
            }
        }
        return null;
    }

    
    @Override
    public Object remove(Object key) throws MyCacheException {
        Object value = getCache().remove(key);
        logRemove(log, key, value);
        return value;
    }

    
    @Override
    public void clear() throws MyCacheException {
        getCache().removeAll();
        logClear(log);
    }


    @Override
    public int getSize() {
        return getCache().getSize();
    }


    
}
