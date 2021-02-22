package pro.bzy.boot.framework.config.cache.ehcache;

import org.apache.ibatis.cache.Cache;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Element;
import pro.bzy.boot.framework.utils.ExceptionCheckUtil;
import pro.bzy.boot.framework.utils.SpringContextUtil;

@Slf4j
public class EhCacheMybatis implements Cache{

    /** 每一个缓存cache的唯一识别号 */
    private String id;
    
    /** 每个mapper对应一个cache */
    private net.sf.ehcache.Cache cache;
    
    
    /** 对象生成时加锁 --> 防止多个线程同时访问数据库后的缓存导致生成ID相同Cache */
    private Object lock = new Object();
    
    /** 缓存时加锁 防止重复缓存 */
    //private Lock readCacheValueLock = new ReentrantLock();
    
    
    
    /** 
     * constructor 
     * */
    public EhCacheMybatis(final String id) {
        ExceptionCheckUtil.hasText(id, "Cache instances require an ID");
        
        this.id = id;
        log.info("###### 生成ehcache缓存对象, id: {}", id);
    }
    
    
    
    /**
     * 初始化自身的缓存cache
     */
    private void initCache() {
        if (cache == null) {
            synchronized (lock) {
                if (cache == null) {
                    EhCacheConfig ehCacheConfig = SpringContextUtil.getBean(EhCacheConfig.class);
                    String cacheName = id;
                    cache = ehCacheConfig.cacheRegister(cacheName);  
                    log.info("缓存对象 {} 初始化成功", id);
                }
            }
        }
    }
    
    
    

    /**
     * 放入缓存
     */
    @Override
    public void putObject(Object arg0, Object arg1) {
        try {
            initCache();
            if (arg0 == null) return;
            
            cache.put(new Element(arg0, arg1));
        } catch (Exception e) {
            log.error("缓存失败# 键：{}，失败原因: {}", arg0, e.getMessage());
        }
    }
    
    
    
    /**
     * 抓取缓存
     */
    @Override
    public Object getObject(Object arg0) {
        try {
            initCache();
            if (arg0 == null) return null;
            
            Element element = cache.get(arg0);
            return element == null ? null : element.getObjectValue();
        } catch (Exception e) {
            log.error("获取缓存失败# 键：{}，失败原因: {}", arg0, e.getMessage());
        }
        return null;
    }
    
    
    
    /**
     * 删除缓存
     */
    @Override
    public Object removeObject(Object arg0) {
        try {
            initCache();
            if (arg0 == null) return null;
            
            return cache.removeAndReturnElement(arg0);
        } catch (Exception e) {
            log.error("删除缓存数据失败：{}",e.getMessage());
        }
        return null;
    }
    
    
    
    /**
     * 清空缓存
     */
    @Override
    public void clear() {
        initCache();
        cache.removeAll();
        log.info("当前缓存对象：{} 已清空全部缓存",  cache.getName());
    }
    
    
    
    /**
     * 获取缓存数量
     */
    @Override
    public int getSize() {
        return cache.getSize();
    }



    
    @Override
    public String getId() {
        return this.id;
    }
}
