package pro.bzy.boot.framework.config.jwt;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;

import com.auth0.jwt.exceptions.TokenExpiredException;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.config.constant.JWT_constant;
import pro.bzy.boot.framework.config.shrio.ShiroFilterUtil;
import pro.bzy.boot.framework.utils.CacheUtil;
import pro.bzy.boot.framework.utils.PropertiesUtil;
import pro.bzy.boot.framework.utils.RequestAndResponseUtil;
import pro.bzy.boot.framework.web.domain.bean.R;

@Slf4j
public class JwtFilter2 extends AccessControlFilter {

    /**
     * 对拦截的请求进行jwtToken认证
     * 若请求内容中包含且验证合法的token数据 则放行允许访问资源 返回true
     * 若验证不合法token 则返回false 跳转到下面的onAccessDenied ↓
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        log.trace("onAccessDenied 方法被调用");
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        Map<String, String> nameAndValuesOfCookies = RequestAndResponseUtil.getCookiesFromRequest(httpRequest);

        // 1. 从cookie或者header中获取token
        String access_token = ShiroFilterUtil.getAccessToken(httpRequest, nameAndValuesOfCookies);
        String refresh_token = ShiroFilterUtil.getRefreshToken(httpRequest, nameAndValuesOfCookies);
        
        Map<String, Object> datas = null;
        try {
            // 2. 已获取jwtToken 验证jwtToken合法性
            ShiroFilterUtil.checkTokenNotnull(access_token, refresh_token);
                
            // 3. token过期校验
            datas = ShiroFilterUtil.getJwttokenDatasFromCache(access_token);
            
            // 4. ip验证
            checkUserIp(datas, httpRequest);
        } catch (TokenExpiredException e) {
            log.warn("【{}】已超时, 准备使用【{}】刷新", JWT_constant.JWT_ACCESS_TOKEN_KEY, JWT_constant.JWT_REFRESH_TOKEN_KEY);
            try {
                // String new_access_token = JwtUtil.refreshJwtTokenByExpiredTokenAndRefreshToken(access_token, refresh_token);
                datas = JwtUtil.refreshJwtTokenByExpiredTokenAndRefreshToken2(access_token, refresh_token);
                
                // ip验证
                checkUserIp(datas, httpRequest);
                
                String new_access_token = datas.remove("newAccessToken").toString();
                httpResponse.addHeader(JWT_constant.JWT_ACCESS_TOKEN_KEY, new_access_token);
                RequestAndResponseUtil.setCookiesAndHeaderToResponeForAccessToken(httpRequest, httpResponse, new_access_token);
                
                CacheUtil.put(new_access_token, datas, 
                        (int) PropertiesUtil.getJwtTokenExpire(JWT_constant.JWT_ACCESS_TOKEN_EXPIRE_KEY_IN_YML));
                access_token = new_access_token;
            } catch (Exception e2) {
                log.warn(e2.getMessage());
                cleanCookiesAndHeaderThenRedirectToLogin(httpRequest, httpResponse, e.getMessage());
                return false;
            }
        } catch (Exception e) {
            log.error("jwtToken认证失败, 原因：" + e.getMessage());
            cleanCookiesAndHeaderThenRedirectToLogin(httpRequest, httpResponse, e.getMessage());
            return false;
        }
        setUserDetailToRequestForRenderView(datas, httpRequest, httpResponse);
        return true;
    }

    

    /**
     * 清楚Cookie中的jwttoken信息 然后返回登录页
     * @param httpRequest
     * @param httpResponse
     * @throws IOException
     */
    private void cleanCookiesAndHeaderThenRedirectToLogin(
            HttpServletRequest httpRequest, HttpServletResponse httpResponse, String msg) throws IOException {
        // 清楚Cookie中的jwttoken信息
        RequestAndResponseUtil.clearLoginJwttokenDataFromCookies(httpRequest, httpResponse);
        // 如果ajax请求 则抛出jwttoken的异常 否则直接重定向到登录页
        if (RequestAndResponseUtil.isAjaxRequest(httpRequest)) {
            RequestAndResponseUtil.responseJson(httpResponse, 
                    JWT_constant.JWT_ERROR_RESPONSE_CODE,
                    R.builder().code(600).msg(msg).build());
        } else {
            setUrlToResponse(httpRequest, httpResponse);
            redirectToLogin(httpRequest, httpResponse);
        }
    }
    
    
    
    /**
     * 将认证前访问被拦截的资源地址记录 以便认证后恢复访问
     * @param httpRequest
     * @param httpResponse
     */
    private void setUrlToResponse(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String curAccessUrl = httpRequest.getRequestURI();
        if (curAccessUrl.startsWith("/login")) {}
        else {
            RequestAndResponseUtil.setLastAccessUrlToCookie(httpRequest, httpResponse, curAccessUrl);
        }
    }
    
    
    
    /**
     * 校验token和访问者得ip是否匹配
     * @param access_token
     * @param util
     * @param httpRequest
     */
    private void checkUserIp(Map<String, Object> cacheData, HttpServletRequest httpRequest) {
        String cur_ip = RequestAndResponseUtil.getIpAddress(httpRequest);
        Object storageIp = cacheData.get(JWT_constant.JWT_LOGIN_USER_IP_KEY);
        if (storageIp != null && !cur_ip.equals(storageIp)) {
            throw new JwtException("access_token校验异常，原因：ip异常，异地使用"+cur_ip);
        }
    }
    
    
    /**
     * 将token中的用户信息给页面渲染
     * @param access_token
     * @param httpRequest
     * @param httpResponse
     */
    private void setUserDetailToRequestForRenderView(Map<String, Object> cacheData, 
            HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        try {
            // Claims claims = JwtUtil.BASE_UTIL.decode(access_token);
            // Map<String, Object> baseDatas = JwtUtil.getBaseStorageDatasFromClaims(claims);
            //httpRequest.setAttribute(JWT_constant.JWT_BASESTORAGE_DATAS_KEY, cacheData);
            RequestAndResponseUtil.setJwttokenDatasToRequest(httpRequest, cacheData);
        } catch (Exception e) {
            log.error("从jwttoken中获取用于渲染视图得基本数据失败", e);
        }
    }
}
