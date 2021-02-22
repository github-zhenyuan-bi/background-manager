package pro.bzy.boot.framework.config.cache.ehcache;

import org.apache.ibatis.cache.Cache;

import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.config.cache.MyAbstractCache;
import pro.bzy.boot.framework.config.cache.MyCahce;
import pro.bzy.boot.framework.utils.ExceptionCheckUtil;

@Slf4j
public class EhCacheMybatis2 implements Cache {

    private String id;
    private MyCahce ehCache;
    
    /** 对象生成时加锁 --> 防止多个线程同时访问数据库后的缓存导致生成ID相同Cache */
    private Object lock = new Object();
    
    public EhCacheMybatis2(final String id) {
        ExceptionCheckUtil.hasText(id, "Cache instances require an ID");
        
        this.id = id;
        log.info("###### 生成ehcache缓存对象, id: {}", id);
    }
    
    public MyCahce getEhCache() {
        if (ehCache == null) {
            synchronized (lock) {
                if (ehCache == null) {
                    ehCache = MyAbstractCache.genCache(id, EhCache.class);
                    log.info("缓存对象 {} 初始化成功", getId());
                }
            }
        }
        return ehCache;
    }
    
    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        getEhCache().put(key, value);
    }

    @Override
    public Object getObject(Object key) {
        return getEhCache().get(key);
    }

    @Override
    public Object removeObject(Object key) {
        return getEhCache().remove(key);
    }

    @Override
    public void clear() {
        getEhCache().clear();
    }

    @Override
    public int getSize() {
        return getEhCache().getSize();
    }

}
