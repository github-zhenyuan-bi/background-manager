package pro.bzy.boot.framework.config.cache.ehcache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

@Slf4j
@EnableCaching
@Configuration
public class EhCacheConfig {
    
    @Autowired
    public CacheManager manager;
    
    /**
     * 向 Ehcache 缓存池中注册缓存对象
     * @param name
     * @return
     */
    public Cache cacheRegister(String name) {
        try {
            Cache ca = new Cache(name, 5000, false, false, 0, 600);
            manager.addCache(ca);
            return ca;
        } catch (Exception e) {
            log.error("注册缓存对象[{}]失败, 原因：{}", name, e.getMessage());
        }
        return null;
    }
    
   
}
