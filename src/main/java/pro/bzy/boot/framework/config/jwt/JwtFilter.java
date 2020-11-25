package pro.bzy.boot.framework.config.jwt;

import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.utils.CookieUtil;
import pro.bzy.boot.framework.utils.SystemConstant;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.util.StringUtils;

import com.auth0.jwt.exceptions.TokenExpiredException;

import io.jsonwebtoken.JwtException;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义一个Filter，用来拦截所有的请求判断是否携带Token
 * isAccessAllowed()判断是否携带了有效的JwtToken
 * onAccessDenied()是没有携带JwtToken的时候进行账号密码登录，登录成功允许访问，登录失败拒绝访问
 */
@Slf4j
public class JwtFilter extends AccessControlFilter {
    
    
    /**
     * 对拦截的请求进行jwtToken认证
     * 若请求内容中包含且验证合法的token数据 则放行允许访问资源 返回true
     * 若验证不合法token 则返回false 跳转到下面的onAccessDenied ↓
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    
    
    /**
     * 在 isAccessAllowed 认证失败时 进行该方法的非token认证的补救措施
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        log.debug("onAccessDenied 方法被调用");
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        Map<String, String> nameAndValuesOfCookies = CookieUtil.getCookiesFromRequest(httpRequest);
        
        // 1. 从cookie或者header中获取token
        String access_token = getJwtTokenFromCookiesOrRequestHeader(SystemConstant.JWT_ACCESS_TOKEN_KEY, httpRequest, nameAndValuesOfCookies); 
        String refresh_token = getJwtTokenFromCookiesOrRequestHeader(SystemConstant.JWT_REFRESH_TOKEN_KEY, httpRequest, nameAndValuesOfCookies); 
      
        try {
            // 2. 已获取jwtToken 验证jwtToken合法性
            if (StringUtils.isEmpty(access_token) || StringUtils.isEmpty(refresh_token)) 
                throw new JwtException(
                        SystemConstant.JWT_ACCESS_TOKEN_KEY + "或" +
                        SystemConstant.JWT_REFRESH_TOKEN_KEY +
                        "为空，无法正常从Header或Cookies中获取，故无法认证，现准备跳转登录页");
                
            // 3. 校验access_token
            boolean is_legal_access_token = new JwtUtil().isVerify(access_token);
            if (!is_legal_access_token) 
                throw new JwtException(SystemConstant.JWT_ACCESS_TOKEN_KEY+"校验失败, 无法获取认证");
            
            // 4. shiro认证
            getSubject(servletRequest, servletResponse).login(new JwtToken(access_token));
        } catch (TokenExpiredException e) {
            log.warn("【{}】已超时, 准备使用【{}】刷新", SystemConstant.JWT_ACCESS_TOKEN_KEY, SystemConstant.JWT_REFRESH_TOKEN_KEY);
            try {
                String new_access_token = JwtUtil.refreshJwtTokenByExpiredTokenAndRefreshToken(access_token, refresh_token);
                HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
                httpResponse.addHeader(SystemConstant.JWT_ACCESS_TOKEN_KEY, new_access_token);
                CookieUtil.setCookiesToRespone(httpRequest, (HttpServletResponse) servletResponse, 
                        SystemConstant.COOKIE_JWT_ACCESS_TOKEN_EXPIRE, 
                        SystemConstant.JWT_ACCESS_TOKEN_KEY, new_access_token);
            } catch (Exception e2) {
                log.error(e.getMessage());
                redirectToLogin(servletRequest, servletResponse);
                return false;
            }
        } catch (Exception e) {
            log.error("jwtToken认证失败, 原因：" + e.getMessage());
            redirectToLogin(servletRequest, servletResponse);
            return false;
        }
        return true;
    }
    
    
    
    
    /**
     * 从请求头或者cookies中获取token
     * @param httpRequest
     * @param key
     * @return
     */
    private String getJwtTokenFromCookiesOrRequestHeader(final String key,
            HttpServletRequest httpRequest, Map<String, String> nameAndValuesOfCookies) {
        String valueOfkey = null;
        // 优先从header中获取
        valueOfkey =  httpRequest.getHeader(key);
        log.debug("从【Header】中获取【 {}】 ={}", key, valueOfkey);
        
        // 如若header中未取到 则再从cookie中获取
        if (StringUtils.isEmpty(valueOfkey)) {
            valueOfkey = nameAndValuesOfCookies.get(key);
            log.debug("从【Cookies】中获取 【{}】 ={}", key, valueOfkey);
        }
        
        return valueOfkey;
    }

}