package pro.bzy.boot.framework.config.cache.redis;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.utils.SpringContextUtil;

@Slf4j
@Deprecated
public class RedisMybatis implements Cache {

    private String id;
    private RedisTemplate<Object, Object> redisTemplate;
    
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public RedisMybatis(){

    }
    public RedisMybatis(String id) {
        this.id = id;
    }

    @SuppressWarnings("unchecked")
    private RedisTemplate<Object, Object> getRedisTemplate(){
        if (redisTemplate == null) {
            synchronized (id) {
                if (redisTemplate == null) {
                    redisTemplate = (RedisTemplate<Object, Object>) SpringContextUtil.getBean("redisTemplate");
                }
            }
        }
        return redisTemplate;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) { 
        getRedisTemplate().boundHashOps(getId()).put(key, value);
        getRedisTemplate().boundHashOps(getId()).expire(10, TimeUnit.SECONDS);
        log.info("[结果放入到缓存中: " + key + "=" + value+" ]");

    }

    @Override
    public Object getObject(Object key) {
        Object value = getRedisTemplate().boundHashOps(getId()).get(key);
        if (Objects.nonNull(value)) 
            log.info("[从缓存中获取了: " + key + "=" + value+" ]");
        else
            log.info("[缓存值=null; key={}]", key);
        return value;
    }

    @Override
    public Object removeObject(Object key) {
        Object value = getRedisTemplate().boundHashOps(getId()).delete(key);
        log.info("[从缓存删除了: " + key + "=" + value+" ]");
        return value;
    }

    @Override
    public void clear() {
        getRedisTemplate().delete(getId());
        log.info("清空缓存!!!");
    }

    @Override
    public int getSize() {
        Long size = getRedisTemplate().boundHashOps(getId()).size();
        return size == null ? 0 : size.intValue();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }
}
