package pro.bzy.boot.framework.config.cache.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.config.cache.MyCacheSupport;
import pro.bzy.boot.framework.config.cache.MyCache;
import pro.bzy.boot.framework.config.exceptions.MyCacheException;
import pro.bzy.boot.framework.utils.SpringContextUtil;


@Slf4j
public class RedisCache extends MyCacheSupport implements MyCache {
    
    /** 缓存实例id */
    private String id;
    
    /** 缓存实例过期时间 */
    private Integer expire;
    
    
    /** 缓存实例初始化锁 */
    private Object lock = new Object();
    private Object lock2 = new Object();
    
    /** redis缓存实例 */
    private RedisTemplate<Object, Object> redisTemplate;
    
    /** redis值类型缓存 */
    private ValueOperations<Object, Object> valueOps;
    
    /** 构造器 */
    public RedisCache(String id) {
        this(id, 0);
    }
    public RedisCache(String id, int expire) {
        this.id = id;
        this.expire = expire;
        log.info("【创建缓存对象-redisCache】=> [id]: {}, [expire]: {}", id, expire);
        if (expire <= 0)
            log.warn("当前缓存对象【{}】设定超时时间小于0，无意义，实际永久存活", id);
    }
    
    public String getId() {
        return this.id;
    }
    
    
    /** 初始化 */
    @SuppressWarnings("unchecked")
    private RedisTemplate<Object, Object> getTemplate() {
        if (redisTemplate == null) {
            synchronized (lock) {
                if (redisTemplate == null) {
                    redisTemplate = (RedisTemplate<Object, Object>) SpringContextUtil.getBean("redisTemplate");
                }
            }
        }
        return redisTemplate;
    }
    
    /** 初始化 */
    private ValueOperations<Object, Object> getOps(){
        if (valueOps == null) {
            synchronized (lock2) {
                if (valueOps == null) {
                    valueOps = getTemplate().opsForValue();
                }
            }
        }
        return valueOps;
    }
    

    
    @Override
    public void put(Object key, Object value, int expire) throws MyCacheException {
        if (expire > 0)
            getOps().set(key, value, expire, TimeUnit.SECONDS);
        else
            getOps().set(key, value);
        logPut(log, key, value, expire);
    }

    @Override
    public void put(Object key, Object value) throws MyCacheException {
        put(key, value, this.expire);
    }

    @Override
    public Object get(Object key) throws MyCacheException {
        Object value = getOps().get(key);
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
        Object value = redisTemplate.delete(key);
        logRemove(log, key, value);
        return value;
    }

    @Override
    public void clear() throws MyCacheException {
        getTemplate().delete(getTemplate().keys("*"));
        logClear(log);
    }

    @Override
    public int getSize() {
        return getTemplate().keys("*").size();
    }
    
    
}
