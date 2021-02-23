package pro.bzy.boot.framework.config.cache.ehcache;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import pro.bzy.boot.framework.config.cache.MyAbstractCache;
import pro.bzy.boot.framework.config.cache.MyCahce;
import pro.bzy.boot.framework.config.exceptions.MyCacheException;
import pro.bzy.boot.framework.utils.SpringContextUtil;

@Slf4j
public class EhCache extends MyAbstractCache implements MyCahce {

    private Cache cache;
    public EhCache(String cacheName) {
        super(cacheName);
        EhCacheConfig ehCacheConfig = SpringContextUtil.getBean(EhCacheConfig.class);
        cache = ehCacheConfig.cacheRegister(cacheName);  
    }
    
    
    @Override
    public void put(Object key, Object value, int expire) throws MyCacheException {
        
        nullKeyCheck(key);
        
        cache.put(new Element(key, value, false, 0, expire));
        logPut(log, key, value, expire);
    }

    @Override
    public void put(Object key, Object value) throws MyCacheException {
        put(key, value, 0);
    }

    @Override
    public Object get(Object key) throws MyCacheException {
        Object value = cache.get(key);
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
        Object value = cache.remove(key);
        logRemove(log, key, value);
        return value;
    }

    @Override
    public void clear() throws MyCacheException {
        cache.removeAll();
        logClear(log);
    }


    @Override
    public int getSize() {
        return cache.getSize();
    }


    
}
