package pro.bzy.boot.framework.config.shrio;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import com.auth0.jwt.exceptions.TokenExpiredException;

import io.jsonwebtoken.JwtException;
import lombok.NonNull;
import pro.bzy.boot.framework.config.constant.CACHE_constant;
import pro.bzy.boot.framework.config.constant.JWT_constant;
import pro.bzy.boot.framework.utils.CacheUtil;
import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.framework.utils.PropertiesUtil;
import pro.bzy.boot.framework.utils.RequestAndResponseUtil;
import pro.bzy.boot.framework.utils.SpringContextUtil;
import pro.bzy.boot.framework.web.domain.bean.R;
import pro.bzy.boot.framework.web.domain.entity.Role;
import pro.bzy.boot.framework.web.service.UserRoleService;

public final class ShiroFilterUtil {

    
    public static String gettoken(HttpServletRequest httpRequest, 
            Map<String, String> nameAndValuesOfCookies, String key) {
        
        return RequestAndResponseUtil.getJwtTokenFromCookiesOrRequestHeader(
                key, httpRequest, nameAndValuesOfCookies); 
    }
    
    
    /** 从请求中拿到access_token */
    public static String getAccessToken(HttpServletRequest httpRequest, Map<String, String> nameAndValuesOfCookies) {
        return gettoken(httpRequest, nameAndValuesOfCookies, JWT_constant.JWT_ACCESS_TOKEN_KEY);
    }
    
    
    /** 从请求中拿到refresh_token */
    public static String getRefreshToken(HttpServletRequest httpRequest, Map<String, String> nameAndValuesOfCookies) {
        return gettoken(httpRequest, nameAndValuesOfCookies, JWT_constant.JWT_REFRESH_TOKEN_KEY);
    }
    
    
    /** 检查token非空 否则抛异常 */
    public static void checkTokenNotnull(final String access_token, final String refresh_token) throws JwtException {
        if (StringUtils.isEmpty(access_token) && StringUtils.isEmpty(refresh_token)) 
            throw new JwtException(
                    JWT_constant.JWT_ACCESS_TOKEN_KEY + "且" +
                    JWT_constant.JWT_REFRESH_TOKEN_KEY +
                    "为空，无法正常从Header或Cookies中获取，故无法认证，现准备跳转登录页");
    }
    
    
    /** 从缓存中拿到token存储 */
    public static Map<String, Object> getJwttokenDatasFromCache(final String access_token) {
        @SuppressWarnings("unchecked")
        Map<String, Object> userDatas = CacheUtil.get(access_token, Map.class);
        if (Objects.isNull(userDatas))
            throw new TokenExpiredException("过期");
        return userDatas;
    }
    
    
    /** 重将access_token 放入header cookie 并将token的数据缓存到cache中 */
    public static void setResponeHeaderCookieAndCache(HttpServletRequest httpRequest, HttpServletResponse httpResponse, 
            final String access_token, final Map<String, Object> userDatas) {
        // header cookie
        RequestAndResponseUtil.setCookiesAndHeaderToResponeForAccessToken(httpRequest, httpResponse, access_token);
        // cache
        CacheUtil.put(access_token, userDatas, 
                (int) PropertiesUtil.getJwtTokenExpire(JWT_constant.JWT_ACCESS_TOKEN_EXPIRE_KEY_IN_YML));
    }
    
    
    /**
     * 处理未授权情况
     * @param httpRequest
     * @param httpResponse
     * @param msg
     * @param redirectUrl
     * @throws Exception
     */
    public static void handleUnAuthor(HttpServletRequest httpRequest, HttpServletResponse httpResponse, 
            String msg, String redirectUrl) throws Exception {
        if (RequestAndResponseUtil.isAjaxRequest(httpRequest)) {
            RequestAndResponseUtil.responseJson(httpResponse, 
                    JWT_constant.JWT_UNAUTHOR_RESPONSE_CODE,
                    R.builder().code(601).msg(msg).build());
        } else {
            WebUtils.issueRedirect(httpRequest, httpResponse, redirectUrl);
        }
    }
    
    
    /**
     * 处理未认证
     * @param httpRequest
     * @param httpResponse
     * @param msg
     * @param redirectUrl
     * @throws Exception
     */
    public static void handleUnAuthenc(HttpServletRequest httpRequest, HttpServletResponse httpResponse, 
            String msg, String redirectUrl) throws Exception {
        // 清楚Cookie中的jwttoken信息
        RequestAndResponseUtil.clearLoginJwttokenDataFromCookies(httpRequest, httpResponse);
        // 如果ajax请求 则抛出jwttoken的异常 否则直接重定向到登录页
        if (RequestAndResponseUtil.isAjaxRequest(httpRequest)) {
            RequestAndResponseUtil.responseJson(httpResponse, 
                    JWT_constant.JWT_ERROR_RESPONSE_CODE,
                    R.builder().code(600).msg(msg).build());
        } else {
            String curAccessUrl = httpRequest.getRequestURI();
            if (curAccessUrl.startsWith("/login")) {}
            else {
                RequestAndResponseUtil.setLastAccessUrlToCookie(httpRequest, httpResponse, curAccessUrl);
            }
            WebUtils.issueRedirect(httpRequest, httpResponse, redirectUrl);
        }
    }
    
    
    /**
     * 判断用户是否含有admin角色
     * @param userid
     * @return
     */
    public static boolean hasAdminRole(@NonNull String userid) {
        // 缓存查询是否用户含有admin角色
        final String KEY = CACHE_constant.USER_HAS_ADMIN_ROLE+userid;
        Boolean hasAdmin = CacheUtil.get(KEY, Boolean.class);
        if (hasAdmin != null) 
            return hasAdmin;
        UserRoleService urs = SpringContextUtil.getBean(UserRoleService.class);
        List<Role> roles = urs.getRolesByUserId(userid);
        if (CollectionUtil.isNotEmpty(roles)) {
            hasAdmin = roles.stream().anyMatch(role -> role.getName().toLowerCase().contains("admin"));
        }
        CacheUtil.put(KEY, hasAdmin, (int) PropertiesUtil.getJwtTokenExpire(JWT_constant.JWT_ACCESS_TOKEN_EXPIRE_KEY_IN_YML));
        return hasAdmin;
    }
}
