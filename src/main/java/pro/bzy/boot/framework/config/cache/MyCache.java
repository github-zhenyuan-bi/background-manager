package pro.bzy.boot.framework.config.cache;

import pro.bzy.boot.framework.config.exceptions.MyCacheException;

public interface MyCache {

    /** 将数据放入缓存 */
    void put(Object key, Object value, int expire) throws MyCacheException;
    
    
    /** 将数据放入缓存 */
    void put(Object key, Object value) throws MyCacheException;
    
    
    /** 从缓存中获取键对应的数据值 */
    Object get(Object key) throws MyCacheException;
    
    
    /** 从缓存中获取键对应的数据值 */
    <T> T get(Object key, Class<T> clazz) throws MyCacheException;
    
    
    /** 从缓存中删除键对应的数据值 */
    Object remove(Object key) throws MyCacheException;
    
    
    /** 清空该缓存中的全部数据 */
    void clear() throws MyCacheException;
    
    
    /** 缓存数量 */
    int getSize();
}
