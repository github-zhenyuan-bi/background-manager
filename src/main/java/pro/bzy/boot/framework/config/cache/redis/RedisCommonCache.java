package pro.bzy.boot.framework.config.cache.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.config.cache.MyAbstractCache;
import pro.bzy.boot.framework.config.cache.MyCahce;
import pro.bzy.boot.framework.config.exceptions.MyCacheException;
import pro.bzy.boot.framework.utils.SpringContextUtil;

@Slf4j
public class RedisCommonCache extends MyAbstractCache implements MyCahce{

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
    
    public RedisCommonCache(String id) {
        super(id);
    }

    @Override
    public void put(Object key, Object value, int expire) throws MyCacheException {
        if (expire > 0)
            getRedisTemplate().opsForValue().set(key, value, expire, TimeUnit.MINUTES);
        else
            getRedisTemplate().opsForValue().set(key, value);
        logPut(log, key, value, expire);
    }

    @Override
    public void put(Object key, Object value) throws MyCacheException {
        put(key, value);
    }

    @Override
    public Object get(Object key) throws MyCacheException {
        Object value = getRedisTemplate().opsForValue().get(key);
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
        Object value = getRedisTemplate().delete(key);
        logRemove(log, key, value);
        return value;
    }

    @Override
    public void clear() throws MyCacheException {
        getRedisTemplate().delete(getRedisTemplate().keys("*"));
        logClear(log);
    }

    @Override
    public int getSize() {
        return getRedisTemplate().keys("*").size();
    }

}
