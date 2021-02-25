package pro.bzy.boot.framework.config.cache.ehcache;

import org.apache.ibatis.cache.Cache;

import pro.bzy.boot.framework.config.cache.MyCache;


public class EhCacheForMybatis implements Cache {

    /** id */
    private String id;
    
    /** 缓存实例 */
    private MyCache ehCache;
    
    
    public EhCacheForMybatis(String id, MyCache ehCache) {
        this.id = id;
        this.ehCache = ehCache;
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
