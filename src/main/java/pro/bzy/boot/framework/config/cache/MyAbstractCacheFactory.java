package pro.bzy.boot.framework.config.cache;

import java.util.Random;

import org.apache.ibatis.cache.Cache;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.config.cache.annotation.CacheProperty;
import pro.bzy.boot.framework.utils.ClassUtil;

/**
 * 
 * 缓存对象抽象工厂
 * @author user
 *
 */
@Slf4j
public abstract class MyAbstractCacheFactory {

    /** 获取一个缓存对象 按默认的配置过期时间 */
    public abstract MyCache getCache(String cacheId);
    
    /** 获取一个缓存对象 设定一个过期时间 单位s */
    public abstract MyCache getCache(String cacheId, int expire);
    
    /** 获取一个一共给mybatis二级缓存的对象 按默认的配置过期时间 */
    public abstract Cache getCacheForMybatis(String cacheId); 
    
    /** 获取一个一共给mybatis二级缓存的对象 设定一个过期时间 单位s */
    public abstract Cache getCacheForMybatis(String cacheId, int expire); 

    
    
    
    
    
    /** 获取目标类上的注解 */
    protected CacheProperty getCacheTargetClassCachePropertyAnnotation(String cacheId) {
        if (StringUtils.isEmpty(cacheId))
            throw new NullPointerException("缓存组件创建需要一个类的全路径名作为ID");
        
        try {
            Class<?> clazz = ClassUtil.forName(cacheId, MyAbstractCacheFactory.class.getClassLoader());
            CacheProperty cacheProperty = clazz.getDeclaredAnnotation(CacheProperty.class);
            return cacheProperty;
        } catch (ClassNotFoundException e) {
            log.error("该缓存组件的对应得类未找到：类名：" + cacheId);
        } catch (LinkageError e) {
            log.error("该缓存组件的对应得类寻找异常" + cacheId);
        }
        return null;
    }
    
    
    
    
    protected int getExtraRamdomExpire(int expire) {
        if (expire < 30) return 0;
        
        return new Random().nextInt((int) Math.sqrt(expire)) + expire;
    }
}
