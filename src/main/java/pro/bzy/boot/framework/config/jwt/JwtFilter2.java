package pro.bzy.boot.framework.config.jwt;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.util.StringUtils;

import com.auth0.jwt.exceptions.TokenExpiredException;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.utils.CacheUtil;
import pro.bzy.boot.framework.utils.PropertiesUtil;
import pro.bzy.boot.framework.utils.RequestAndResponseUtil;
import pro.bzy.boot.framework.utils.SystemConstant;
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

    @SuppressWarnings("unchecked")
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        log.debug("onAccessDenied 方法被调用");
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        Map<String, String> nameAndValuesOfCookies = RequestAndResponseUtil.getCookiesFromRequest(httpRequest);
        
        // 1. 从cookie或者header中获取token
        String access_token = RequestAndResponseUtil.getJwtTokenFromCookiesOrRequestHeader(
                SystemConstant.JWT_ACCESS_TOKEN_KEY, httpRequest, nameAndValuesOfCookies); 
        String refresh_token = RequestAndResponseUtil.getJwtTokenFromCookiesOrRequestHeader(
                SystemConstant.JWT_REFRESH_TOKEN_KEY, httpRequest, nameAndValuesOfCookies); 
        Map<String, Object> datas = null;
        try {
            // 2. 已获取jwtToken 验证jwtToken合法性
            if (StringUtils.isEmpty(access_token) && StringUtils.isEmpty(refresh_token)) 
                throw new JwtException(
                        SystemConstant.JWT_ACCESS_TOKEN_KEY + "且" +
                        SystemConstant.JWT_REFRESH_TOKEN_KEY +
                        "为空，无法正常从Header或Cookies中获取，故无法认证，现准备跳转登录页");
                
            // 3. token过期校验
            datas = CacheUtil.get(access_token, Map.class);
            if (Objects.isNull(datas))
                throw new TokenExpiredException("过期");
            
            // 4. ip验证
            checkUserIp(datas, httpRequest);
        } catch (TokenExpiredException e) {
            log.warn("【{}】已超时, 准备使用【{}】刷新", SystemConstant.JWT_ACCESS_TOKEN_KEY, SystemConstant.JWT_REFRESH_TOKEN_KEY);
            try {
                String new_access_token = JwtUtil.refreshJwtTokenByExpiredTokenAndRefreshToken(access_token, refresh_token);
                httpResponse.addHeader(SystemConstant.JWT_ACCESS_TOKEN_KEY, new_access_token);
                RequestAndResponseUtil.setCookiesAndHeaderToResponeForAccessToken(httpRequest, httpResponse, new_access_token);
                
                CacheUtil.put(new_access_token, 
                        JwtUtil.getBaseStorageDatasFromClaims(new JwtUtil().decode(new_access_token)), 
                        (int) PropertiesUtil.getJwtTokenExpire(SystemConstant.JWT_ACCESS_TOKEN_EXPIRE_KEY_IN_YML));
                access_token = new_access_token;
            } catch (Exception e2) {
                log.error(e.getMessage(), e);
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
                    SystemConstant.JWT_ERROR_RESPONSE_CODE,
                    R.builder().code(600).msg(msg).build());
        } else {
            redirectToLogin(httpRequest, httpResponse);
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
        Object storageIp = cacheData.get(SystemConstant.JWT_LOGIN_USER_IP_KEY);
        if (storageIp != null && !cur_ip.equals(storageIp)) {
            throw new JwtException("access_token校验异常，原因：ip异常，异地使用");
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
            httpRequest.setAttribute(SystemConstant.JWT_BASESTORAGE_DATAS_KEY, cacheData);
        } catch (Exception e) {
            log.error("从jwttoken中获取用于渲染视图得基本数据失败", e);
        }
    }
}
