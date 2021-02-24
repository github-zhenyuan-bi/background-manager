package pro.bzy.boot.framework.config.cache.ehcache;

import org.apache.ibatis.cache.Cache;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EhCacheForMybatis implements Cache {

    /** id */
    private String id;
    
    /** 缓存实例 */
    private EhCache ehCache;
    
    
    public EhCacheForMybatis(String id, EhCache ehCache) {
        this.ehCache = ehCache;
        log.info("【Cache】=> ehcacheForMybatis# id:", id);
    }
    
    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        ehCache.put(key, value);
    }

    @Override
    public Object getObject(Object key) {
        return ehCache.get(key);
    }

    @Override
    public Object removeObject(Object key) {
        return ehCache.remove(key);
    }

    @Override
    public void clear() {
        ehCache.clear();
    }

    @Override
    public int getSize() {
        return ehCache.getSize();
    }

}
