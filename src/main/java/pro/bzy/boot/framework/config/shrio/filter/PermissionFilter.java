package pro.bzy.boot.framework.config.shrio.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.config.constant.JWT_constant;
import pro.bzy.boot.framework.config.jwt.JwtUtil;
import pro.bzy.boot.framework.config.shrio.ShiroFilterUtil;
import pro.bzy.boot.framework.utils.CacheUtil;
import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.framework.utils.RequestAndResponseUtil;
import pro.bzy.boot.framework.utils.SpringContextUtil;
import pro.bzy.boot.framework.web.service.RolePermissionService;

@Slf4j
public class PermissionFilter extends PermissionsAuthorizationFilter {

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws IOException {
        log.debug("权限验证过滤器");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        Map<String, String> nameAndValuesOfCookies = RequestAndResponseUtil.getCookiesFromRequest(httpRequest);
        
        // 1. 从cookie或者header中获取token
        String access_token = ShiroFilterUtil.getAccessToken(httpRequest, nameAndValuesOfCookies);
        String refresh_token = ShiroFilterUtil.getRefreshToken(httpRequest, nameAndValuesOfCookies);
        
        Map<String, Object> userDatas = null;
        List<String> permDatas = null;
        try {
            // 2. 已获取jwtToken 验证jwtToken合法性
            ShiroFilterUtil.checkTokenNotnull(access_token, refresh_token);
                
            // 3. 认证过期校验 若未过期则从缓存中拉取数据
            try {
                userDatas = ShiroFilterUtil.getJwttokenDatasFromCache(access_token);
            } catch (Exception e) {
                log.warn("【{}】已超时, 准备使用【{}】刷新", JWT_constant.JWT_ACCESS_TOKEN_KEY, JWT_constant.JWT_REFRESH_TOKEN_KEY);
                userDatas = JwtUtil.refreshJwtTokenByExpiredTokenAndRefreshToken2(access_token, refresh_token);
                access_token = userDatas.remove("newAccessToken").toString();
                ShiroFilterUtil.setResponeHeaderCookieAndCache(httpRequest, httpResponse, access_token, userDatas);
            }
            
            // 3.5 判断用户是否含有admin角色 含有则直接放权
            String userid = userDatas.get(JWT_constant.JWT_LOGIN_USERID_KEY).toString();
            boolean hasAdmin = ShiroFilterUtil.hasAdminRole(userid);
            if (hasAdmin) return true;
            
            // 4. 若还未查询授权信息则先查库 取得授权信息 后缓存
            permDatas = getPermsData(access_token, userid);
            
            // 5. 授权信息匹配
            isPermitted(mappedValue, permDatas);
            return true;
        } catch (UnauthorizedException e) {
            try {
                ShiroFilterUtil.handleUnAuthor(httpRequest, httpResponse, e.getMessage(), getUnauthorizedUrl());
            } catch (Exception e1) {
                log.error("授权校验失败后返回异常", e1);
            }
        } catch (Exception e) {
            try {
                ShiroFilterUtil.handleUnAuthenc(httpRequest, httpResponse, e.getMessage(), getLoginUrl());
            } catch (Exception e1) {
                log.error("授权校验失败后返回异常", e1);
            }
        }
        return false;
    }

    
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        return false;
    }

    
    /** 获取权限数据 从缓存中 若缓存没有 则查库 */
    private List<String> getPermsData(final String access_token, final String userid) {
        @SuppressWarnings("unchecked")
        List<String> permDatas = CacheUtil.get(access_token + JWT_constant.JWT_ROLE_AND_PERMISSION_KEY_SUFFIX, List.class);
        if (Objects.isNull(permDatas)) {
            RolePermissionService rps = SpringContextUtil.getBean(RolePermissionService.class);
            permDatas = rps.getPermsNameOfUser(userid);
            CacheUtil.put(access_token + JWT_constant.JWT_ROLE_AND_PERMISSION_KEY_SUFFIX, permDatas);
        } 
        return permDatas;
    }
    
    
    /** 校验是否授权 */
    private void isPermitted(Object mappedValue, List<String> permDatas) {
        String[] requiredPerms = (String[]) mappedValue;
        if (CollectionUtil.isEmpty(permDatas)) 
            throw new UnauthorizedException("未授权");
        
        if (requiredPerms != null) {
            if (requiredPerms.length == 1) {
                if (!permDatas.contains(requiredPerms[0])) {
                    throw new UnauthorizedException("未授权");
                }
            } else {
                for (String requiredPerm : requiredPerms) {
                    if (!permDatas.contains(requiredPerm)) {
                        throw new UnauthorizedException("未授权");
                    } 
                }
            }
        }
    }
    
    
    
    
}
