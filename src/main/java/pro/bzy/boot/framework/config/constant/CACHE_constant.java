package pro.bzy.boot.framework.config.constant;

public interface CACHE_constant {

    /** 缓存 ehcache 配置文件classpath路径 */ 
    String EHCACHE_FILE = "cache/ehcache.xml";  
    /** 方法上接口说明缓存组件名称  */
    String EHCACHE_APIDESC_CACHE_NAME = "api_desc_onMethod_cache";
    
    /** yml配置 使用redis或ehcache键 */
    String USER_CACHE = "app.config.cache.use";
    /** yml配置 使用redis或ehcache值 ---ehcache */
    String CACHE_EHCACHE = "ehcache";
    /** yml配置 使用redis或ehcache值 ---redis */
    String CACHE_REDIS = "redis";
    
    /** yml配置 redis内容超时键 */
    String REDIS_CACHE_EXPIRE_KEY = "app.config.cache.redis.expire";
    
    /** 日志记录模块 -- 访问资源url&名称 -- 缓存 -- 缓存键前缀 */
    String CACHE_URI_DESC_PREFIX = "uri-cache-";
    
    
    String USER_HAS_ADMIN_ROLE = "user_has_admin_role_";
}
