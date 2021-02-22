package pro.bzy.boot.framework.config.cache;

import java.lang.reflect.Constructor;
import java.util.Objects;

import org.slf4j.Logger;

import lombok.Data;
import pro.bzy.boot.framework.config.exceptions.MyCacheException;

@Data
public class MyAbstractCache {
    
    private String id;
    public MyAbstractCache(final String id) {
        this.id = id;
    }

    
    public static <T extends MyCahce> MyCahce genCache(String id, Class<T> clazz) {
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
        log.info("放入缓存：key=【{}】，value=【{}】，expire=【{}】。", key, value, expire);
    }
    
    
    protected void logGet(Logger log, Object key, Object value) {
        if (Objects.nonNull(value)) 
            log.info("从缓存中取值: key=【{}】, value=【{}】", key, value);
        else
            log.info("缓存值=null; key={}]", key);
    }
    
    
    
    protected void logRemove(Logger log, Object key, Object value) {
        log.info("从缓存删除: key=【{}】，value=【{}】", key, value);
    }
    
    
    
    protected void logClear(Logger log) {
        log.info("清空缓存!!!");
    }
    
    
    
}
