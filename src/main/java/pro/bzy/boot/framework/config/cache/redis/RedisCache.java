package pro.bzy.boot.framework.config.cache.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.config.cache.MyAbstractCache;
import pro.bzy.boot.framework.config.cache.MyCahce;
import pro.bzy.boot.framework.config.exceptions.MyCacheException;
import pro.bzy.boot.framework.utils.SpringContextUtil;


@Slf4j
public class RedisCache extends MyAbstractCache implements MyCahce {
    
    private Object lock = new Object();
    private RedisTemplate<Object, Object> redisTemplate;
    @SuppressWarnings("unchecked")
    private RedisTemplate<Object, Object> getRedisTemplate(){
        if (redisTemplate == null) {
            synchronized (lock) {
                if (redisTemplate == null) {
                    redisTemplate = (RedisTemplate<Object, Object>) SpringContextUtil.getBean("redisTemplate");
                }
            }
        }
        return redisTemplate;
    }
    
    public RedisCache(String id) {
        super(id);
    }

    @Override
    public void put(Object key, Object value, int expire) throws MyCacheException {
        
        nullKeyCheck(key);
        
        getRedisTemplate().boundHashOps(getId()).put(key, value);
        if (expire > 0)
            getRedisTemplate().boundHashOps(getId()).expire(expire, TimeUnit.SECONDS);
        logPut(log, key, value, expire);
    }

    @Override
    public void put(Object key, Object value) throws MyCacheException {
        put(key, value, 0);
    }

    @Override
    public Object get(Object key) throws MyCacheException {
        Object value = getRedisTemplate().boundHashOps(getId()).get(key);
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
        Object value = getRedisTemplate().boundHashOps(getId()).delete(key);
        logRemove(log, key, value);
        return value;
    }

    @Override
    public void clear() throws MyCacheException {
        getRedisTemplate().delete(getId());
        logClear(log);
    }


    public int getSize() {
        Long size = getRedisTemplate().boundHashOps(getId()).size();
        return size == null ? 0 : size.intValue();
    }
    
}
