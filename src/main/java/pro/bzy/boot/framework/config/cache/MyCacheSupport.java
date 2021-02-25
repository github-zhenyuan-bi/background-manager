package pro.bzy.boot.framework.config.cache;

import java.lang.reflect.Constructor;
import java.util.Objects;

import org.slf4j.Logger;

import lombok.Data;
import pro.bzy.boot.framework.config.exceptions.MyCacheException;

@Data
public class MyCacheSupport {
    public static <T extends MyCache> MyCache genCache(String id, Class<T> clazz) {
        try {
            Constructor<T> cons = clazz.getConstructor(String.class);
            return cons.newInstance(id);
        } catch (Exception e) {
            throw new MyCacheException("构造缓存对象失败，原因：" + e.getMessage());
        }
    }
    
    
    
    protected void nullKeyCheck(Object key) {
        if (key == null) 
            throw new MyCacheException("缓存内容‘键’不能为空");
    }
    
    
    protected void logPut(Logger log, Object key, Object value, int expire) {
        log.debug("#####|缓      存|   key=【{}】，value=【{}】，expire=【{}】。", key, value, expire);
    }
    
    
    protected void logGet(Logger log, Object key, Object value) {
        if (Objects.nonNull(value)) 
            log.debug("#####|缓存->读|   key=【{}】, value=【{}】", key, value);
        else
            log.debug("#####|缓存->读|   value=null; key={}]", key);
    }
    
    
    
    protected void logRemove(Logger log, Object key, Object value) {
        log.debug("#####|缓存->删|   key=【{}】，value=【{}】", key, value);
    }
    
    
    
    protected void logClear(Logger log) {
        log.debug("#####|缓存->清空|");
    }
    
    
    
}
