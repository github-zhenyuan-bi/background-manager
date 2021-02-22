package pro.bzy.boot.framework.config.cache.redis;

import org.apache.ibatis.cache.Cache;

import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.config.cache.MyAbstractCache;
import pro.bzy.boot.framework.config.cache.MyCahce;
import pro.bzy.boot.framework.utils.ExceptionCheckUtil;

@Slf4j
public class RedisMybatis2 implements Cache {

    private String id;
    private MyCahce redisCache;
    public RedisMybatis2(final String id) {
        ExceptionCheckUtil.hasText(id, "Cache instances require an ID");
        
        this.id = id;
        redisCache = MyAbstractCache.genCache(id, RedisCache.class);
        log.info("###### 生成redisCache缓存对象, id: {}", id);
    }
    
    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        redisCache.put(key, value);
    }

    @Override
    public Object getObject(Object key) {
        return redisCache.get(key);
    }

    @Override
    public Object removeObject(Object key) {
        return redisCache.remove(key);
    }

    @Override
    public void clear() {
        redisCache.clear();
    }

    @Override
    public int getSize() {
        return redisCache.getSize();
    }

    
    
}
