package pro.bzy.boot.framework.utils;

/**
 * 系统常量池
 */
public interface SystemConstant {

    /** utf8编码格式 */
    String CHARSET_UTF8 = "utf8";
    
    
    
    /** http 返回值类型json content-type: application/json;charset=utf-8 */
    String HTTP_CONTENT_TYPE_JSON = "application/json;charset=utf-8";
    
    /** http 返回值类型xml content-type: application/json;charset=utf-8 */
    String HTTP_CONTENT_TYPE_XML = "application/json;charset=utf-8";
    
    
    
    /** 符号 . */
    String DOT = ".";
    
    
    
    
    Integer DEFAULT_HANDLE_LOG_PERIOD = 3;
    
    
    String BACKGROUND_MANAGER_MENU_KEY = "bgManage";
    
    
    
    /* 定时器使用的系统常量cron表达式 */
    String SCHEDULE_CRON_CONSTANT_KEY = "constants";
    String SCHEDULE_CRON_CONSTANT_QUERY_TYPE = "schedule_cron";
    String SCHEDULE_TASK_METHODS_KEY = "taskMethods";
    
    
    /*
     * ---------------------------------------
     *      code 代码常量
     *---------------------------------------/
     */
    Integer ENABLE = 1;     // 可以
    Integer UNABLE = 0;     // 不可以
    
    Integer YES = 1;    // 是
    Integer NO = 0;     // 否
    
    Integer IS_DELETED = 1;     // 逻辑删除
    Integer NOT_DELETED = 0;    // 非逻辑删除
    
    Integer IS_FORBIDDEN = 1;   // 禁止
    Integer NOT_FORBIDDEN = 0;  // 允许
    
    String SEX_MAN = "1"; // 男
    String SEX_WOMEN = "2"; // 女
    
    
    String TREE_ROOT_ID = "-1";     // 树形结构 根id
    
    String RESPONSE_MSG_OK = "ok";  // 响应消息-正常码
    
    
    String VIEW_RENDER_PARAMS_USERID = "view_render_userid";
    String VIEW_RENDER_PARAMS_USERNAME = "view_render_username";
    
    /*
     * ---------------------------------------
     *      setting 配置常量
     *---------------------------------------/
     */
    
    String WEB_INF_PATH = "/WEB-INF";   // web项目 WEB-INF路径
     
    String APPLICATION_YML = "application.yml"; // application配置文件名称
    String APPLICATION_PROPERTIES = "application.properties";   // application配置文件名称
    
    String EHCACHE_FILE = "cache/ehcache.xml";  // 缓存 ehcache 配置文件classpath路径
    String EHCACHE_APIDESC_CACHE_NAME = "api_desc_onMethod_cache";
    
    String SAVED_URL = "savedUrl";
    
    
    String YML_SUFFIX = "yml";
    String PROPERTIES_SUFFIX = "properties";
    
    String APPLICATION_PROFILE_ACTIVE_KEY = "spring.profiles.active";
    String USER_CACHE = "app.config.cache.use";
    String CACHE_EHCACHE = "ehcache";
    String CACHE_REDIS = "redis";
    
    String LOG_ARCHIVE_DICT_KEY_IN_YML = "app.config.log.systemlog-archive-dictPath";
    String LOG_ARCHIVE_DICT = WEB_INF_PATH + "/log/system-log/log-archive/";
    
    
    String JWT_TOKEN_BASE_LOGIN_ISS_KEY = "sub";
    // token 和 refreshtoken 的key
    String JWT_ACCESS_TOKEN_KEY = "access_token";
    String JWT_REFRESH_TOKEN_KEY = "refresh_token";
    // token 的过期时间 10分钟
    //int JWT_ACCESS_TOKEN_EXPIRE = 1000*30;//1000*60*10;
    //int JWT_REFRESH_TOKEN_EXPIRE = JWT_ACCESS_TOKEN_EXPIRE*20;
    // cookie 存储token得到时间
    //int COOKIE_JWT_ACCESS_TOKEN_EXPIRE = 60*5;//60*60*24*3;
    //int COOKIE_JWT_REFRESH_TOKEN_EXPIRE = COOKIE_JWT_ACCESS_TOKEN_EXPIRE;
    String JWT_ACCESS_TOKEN_EXPIRE_KEY_IN_YML = "app.config.jwt.access-token-expire";
    String JWT_REFRESH_TOKEN_EXPIRE_KEY_IN_YML = "app.config.jwt.refresh-token-expire";

    String JWT_LOGIN_FROMWHERE_KEY = "loginBackground";
    String JWT_LOGIN_FROMWHERE_BACKGROUND = "background";
    String JWT_LOGIN_FROMWHERE_WECHAT = "wechat";
    String JWT_LOGIN_USERID_KEY = "userid";
    String JWT_LOGIN_USER = "user";
    String JWT_LOGIN_USER_IP_KEY = "userip";
    String JWT_BASESTORAGE_DATAS_KEY = "jwttoken_basedata";
    int JWT_ERROR_RESPONSE_CODE = 600;
    
    
    String SCHEDULING_EXECUTE_FAILURE = "0";
    String SCHEDULING_EXECUTE_SUCCESS = "1";
    
    
    String REDIS_CACHE_EXPIRE_KEY = "app.config.cache.redis.expire";
    
    String CACHE_URI_DESC_PREFIX = "uri-cache-";
    
    String COOKIE_LAST_ACCESS_URL_KEY = "lastAccessUrl";
}
