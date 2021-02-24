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
    
    /** 缓存实例初始化锁 */
    private Object lock = new Object();
    private Object lock2 = new Object();
    
    /** redis缓存实例 */
    private RedisTemplate<Object, Object> redisTemplate;

    /** hashOps缓存 */
    private BoundHashOperations<Object, Object, Object> HashOps;
    
    /** 构造器 */
    public RedisCacheForMybatis(final String id) {
        this.id = id;
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
        getOps().put(key, value);
        getOps().expire(30, TimeUnit.SECONDS);
        logPut(log, key, value, 0);
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
