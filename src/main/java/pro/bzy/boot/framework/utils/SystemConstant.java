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
    
    String SCHEDULING_TASK_METHODS_KEY = "taskMethods";
    
    String BACKGROUND_MANAGER_MENU_KEY = "bgManage";
    
    
    
    
    
    
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
    
    
    
    
    /*
     * ---------------------------------------
     *      setting 配置常量
     *---------------------------------------/
     */
    
    String WEB_INF_PATH = "/WEB-INF";   // web项目 WEB-INF路径
     
    String APPLICATION_YML = "application.yml"; // application配置文件名称
    String APPLICATION_PROPERTIES = "application.properties";   // application配置文件名称
    
    String EHCACHE_FILE = "cache/ehcache.xml";  // 缓存 ehcache 配置文件classpath路径
    
    String SAVED_URL = "savedUrl";
    
    
    String YML_SUFFIX = "yml";
    String PROPERTIES_SUFFIX = "properties";
    
    String APPLICATION_PROFILE_ACTIVE_KEY = "spring.profiles.active";
    String USER_CACHE = "app.config.cache.use";
    String CACHE_EHCACHE = "ehcache";
    String CACHE_REDIS = "redis";
    
    String LOG_ARCHIVE_DICT = "/log/system-log/log-archive/";
    
    
    public static final String JWT_TOKEN_BASE_LOGIN_ISS_KEY = "sub";
    // token 和 refreshtoken 的key
    public static final String JWT_ACCESS_TOKEN_KEY = "access_token";
    public static final String JWT_REFRESH_TOKEN_KEY = "refresh_token";
    // token 的过期时间 10分钟
    public static final int JWT_ACCESS_TOKEN_EXPIRE = 1000*30;//1000*60*10;
    public static final int JWT_REFRESH_TOKEN_EXPIRE = JWT_ACCESS_TOKEN_EXPIRE*20;
    // cookie 存储token得到时间
    public static final int COOKIE_JWT_ACCESS_TOKEN_EXPIRE = 60*5;//60*60*24*3;
    public static final int COOKIE_JWT_REFRESH_TOKEN_EXPIRE = COOKIE_JWT_ACCESS_TOKEN_EXPIRE;
}
