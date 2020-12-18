package pro.bzy.boot.framework.config.jwt;

import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.utils.RequestAndResponseUtil;
import pro.bzy.boot.framework.utils.SystemConstant;
import pro.bzy.boot.framework.web.domain.bean.R;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.util.StringUtils;

import com.auth0.jwt.exceptions.TokenExpiredException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import java.io.IOException;
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
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        Map<String, String> nameAndValuesOfCookies = RequestAndResponseUtil.getCookiesFromRequest(httpRequest);
        
        // 1. 从cookie或者header中获取token
        String access_token = RequestAndResponseUtil.getJwtTokenFromCookiesOrRequestHeader(
                SystemConstant.JWT_ACCESS_TOKEN_KEY, httpRequest, nameAndValuesOfCookies); 
        String refresh_token = RequestAndResponseUtil.getJwtTokenFromCookiesOrRequestHeader(
                SystemConstant.JWT_REFRESH_TOKEN_KEY, httpRequest, nameAndValuesOfCookies); 
      
        try {
            // 2. 已获取jwtToken 验证jwtToken合法性
            if (StringUtils.isEmpty(access_token) && StringUtils.isEmpty(refresh_token)) 
                throw new JwtException(
                        SystemConstant.JWT_ACCESS_TOKEN_KEY + "且" +
                        SystemConstant.JWT_REFRESH_TOKEN_KEY +
                        "为空，无法正常从Header或Cookies中获取，故无法认证，现准备跳转登录页");
                
            // 3. 校验access_token
            boolean is_legal_access_token = JwtUtil.BASE_UTIL.isVerify(access_token);
            if (!is_legal_access_token) 
                throw new JwtException(SystemConstant.JWT_ACCESS_TOKEN_KEY+"校验失败, 无法获取认证");
            
            // 4. ip验证
            checkUserIp(access_token, httpRequest);
            
            // 5. shiro认证
            // getSubject(servletRequest, servletResponse).login(new JwtToken(access_token));
        } catch (TokenExpiredException e) {
            log.warn("【{}】已超时, 准备使用【{}】刷新", SystemConstant.JWT_ACCESS_TOKEN_KEY, SystemConstant.JWT_REFRESH_TOKEN_KEY);
            try {
                String new_access_token = JwtUtil.refreshJwtTokenByExpiredTokenAndRefreshToken(access_token, refresh_token);
                httpResponse.addHeader(SystemConstant.JWT_ACCESS_TOKEN_KEY, new_access_token);
                RequestAndResponseUtil.setCookiesAndHeaderToResponeForAccessToken(httpRequest, httpResponse, new_access_token);
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
        setUserDetailToRequestForRenderView(access_token, httpRequest, httpResponse);
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
    private void checkUserIp(String access_token, HttpServletRequest httpRequest) {
        String cur_ip = RequestAndResponseUtil.getIpAddress(httpRequest);
        Object access_token_ip = JwtUtil.BASE_UTIL.decode(access_token).get(SystemConstant.JWT_LOGIN_USER_IP_KEY);
        if (!cur_ip.equals(access_token_ip)) {
            throw new JwtException("access_token校验异常，原因：ip异常，异地使用");
        }
    }
    
    
    
    /**
     * 将token中的用户信息给页面渲染
     * @param access_token
     * @param httpRequest
     * @param httpResponse
     */
    private void setUserDetailToRequestForRenderView(String access_token, 
            HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        try {
            Claims claims = JwtUtil.BASE_UTIL.decode(access_token);
            Map<String, Object> baseDatas = JwtUtil.getBaseStorageDatasFromClaims(claims);
            httpRequest.setAttribute(SystemConstant.JWT_BASESTORAGE_DATAS_KEY, baseDatas);
        } catch (Exception e) {
            log.error("从jwttoken中获取用于渲染视图得基本数据失败", e);
        }
    }
    
}