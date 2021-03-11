package pro.bzy.boot.framework.config.constant;

public interface JWT_constant {

    
    
    /** 传递的access_token键名 */
    String JWT_ACCESS_TOKEN_KEY = "access_token";
    /** 传递的refresh_token键名 */
    String JWT_REFRESH_TOKEN_KEY = "refresh_token";
    
    /** yml中配置的access_token过期时间键值 */
    String JWT_ACCESS_TOKEN_EXPIRE_KEY_IN_YML = "app.config.jwt.access-token-expire";
    /** yml中配置的refresh_token过期时间键值 */
    String JWT_REFRESH_TOKEN_EXPIRE_KEY_IN_YML = "app.config.jwt.refresh-token-expire";

    /** jwttoken 中存储数据 */
    String JWT_BASESTORAGE_DATAS_KEY = "jwttoken_basedata"; // token数据存储键
    String JWT_TOKEN_BASE_LOGIN_ISS_KEY = "sub";    // iss_key
    String JWT_LOGIN_FROMWHERE_KEY = "loginBackground"; // 认证用户来源 键
    String JWT_LOGIN_FROMWHERE_BACKGROUND = "background";   // 认证用户来源 - 后台
    String JWT_LOGIN_FROMWHERE_WECHAT = "wechat";   // 认证用户来源 - 微信
    String JWT_LOGIN_USERID_KEY = "userid"; // 用户id 键
    String JWT_LOGIN_USER = "user"; //  用户
    String JWT_LOGIN_USER_IP_KEY = "userip";    // 用户认证token所在ip
    String JWT_ROLE_AND_PERMISSION_KEY_SUFFIX = "_perms";
    
    int JWT_ERROR_RESPONSE_CODE = 600;
    int JWT_UNAUTHOR_RESPONSE_CODE = 601;
}
