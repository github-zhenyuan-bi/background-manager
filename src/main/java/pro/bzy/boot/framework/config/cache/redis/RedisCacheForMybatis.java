package pro.bzy.boot.framework.config.cache.redis;

import java.util.concurrent.TimeUnit;

import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.config.cache.MyCacheSupport;
import pro.bzy.boot.framework.utils.SpringContextUtil;

@Slf4j
public class RedisCacheForMybatis extends MyCacheSupport implements Cache {

    /** 缓存实例id */
    private String id;
    
    /** 缓存实例过期时间 */
    private Integer expire;
    
    /** 缓存实例初始化锁 */
    private Object lock = new Object();
    private Object lock2 = new Object();
    
    /** redis缓存实例 */
    private RedisTemplate<Object, Object> redisTemplate;

    /** hashOps缓存 */
    private BoundHashOperations<Object, Object, Object> HashOps;
    
    /** 构造器 */
    public RedisCacheForMybatis(final String id) {
        this(id, 0);
    }
    public RedisCacheForMybatis(final String id, int expire) {
        this.id = id;
        this.expire = expire;
        log.info("【创建缓存对象-redisForMyBatis】=> [id]: {}, [expire]: {}", id, expire);
        if (expire <= 0)
            log.warn("当前缓存对象【{}】设定超时时间小于1，无意义，实际永久存活", id);
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
    private BoundHashOperations<Object, Object, Object> getOps() {
        if (HashOps == null) {
            synchronized (lock2) {
                if (HashOps == null) {
                    HashOps = getTemplate().boundHashOps(id);
                }
            }
        }
        return HashOps;
    }


    @Override
    public String getId() {
        return id;
    }


    @Override
    public void putObject(Object key, Object value) {
        if (!getTemplate().hasKey(id) && expire > 0) {
            getOps().put(key, value);
            getOps().expire(expire, TimeUnit.SECONDS);
        } else {
            getOps().put(key, value);
        }
        logPut(log, key, value, expire);
    }


    @Override
    public Object getObject(Object key) {
        Object value = getOps().get(key);
        logGet(log, key, value);
        return value;
    }


    @Override
    public Object removeObject(Object key) {
        Object value = getOps().delete(key);
        logRemove(log, key, value);
        return value;
    }


    @Override
    public void clear() {
        getTemplate().delete(getId());
        logClear(log);
    }


    @Override
    public int getSize() {
        Long size = getOps().size();
        return size == null ? 0 : size.intValue();
    }
    
}
