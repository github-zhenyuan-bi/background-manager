package pro.bzy.boot.framework.config.cache.ehcache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import pro.bzy.boot.framework.config.yml.Ehcache;
import pro.bzy.boot.framework.config.yml.YmlBean;
import pro.bzy.boot.framework.utils.SystemConstant;

@Slf4j
@EnableCaching
@Configuration
public class EhCacheConfig {
    
    @Autowired
    public CacheManager manager;
    
    @Autowired
    private YmlBean ymlBean;
    
    
    /**
     * 向 Ehcache 缓存池中注册缓存对象
     * @param name
     * @return
     */
    public Cache cacheRegister(String name) {
        try {
            Ehcache ehcache = ymlBean.getConfig().getCache().getEhcache();
            Cache ca = new Cache(name, 
                    ehcache.getMaxElementsInMemory(), 
                    ehcache.getOverflowToDisk(),
                    ehcache.getEternal(), 
                    ehcache.getTimeToLiveSeconds(), 
                    ehcache.getTimeToIdleSeconds());
            manager.addCache(ca);
            return ca;
        } catch (Exception e) {
            log.error("注册缓存对象[{}]失败, 原因：{}", name, e.getMessage());
        }
        return null;
    }
    
   
    
    /**
     * 拿接口上说明的缓存
     * @return
     */
    public Cache getApiDescOnMethodCache() {
        return manager.getCache(SystemConstant.EHCACHE_APIDESC_CACHE_NAME);
    }
}
