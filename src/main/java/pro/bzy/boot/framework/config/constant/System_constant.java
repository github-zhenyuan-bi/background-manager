package pro.bzy.boot.framework.config.constant;

public interface System_constant {

    /** utf8编码格式 */
    String CHARSET_UTF8 = "utf8";
    /** http 返回值类型json content-type: application/json;charset=utf-8 */
    String HTTP_CONTENT_TYPE_JSON = "application/json;charset=utf-8";
    /** http 返回值类型xml content-type: application/json;charset=utf-8 */
    String HTTP_CONTENT_TYPE_XML = "application/json;charset=utf-8";
    
    
    /** 响应消息-正常码 */
    String RESPONSE_MSG_OK = "ok";  

    /** cookie中存储未认证前访问的url */
    String COOKIE_LAST_ACCESS_URL_KEY = "lastAccessUrl";
    @Deprecated
    String SAVED_URL = "savedUrl";


    /** yml中active-profile的键 */
    String APPLICATION_PROFILE_ACTIVE_KEY = "spring.profiles.active";
    /** WEB-INF*/
    String WEB_INF_PATH = "/WEB-INF";  

}
