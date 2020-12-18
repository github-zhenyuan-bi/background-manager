package pro.bzy.boot.framework.config.shrio;


import java.util.List;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
/**
 * 认证操作类
 */
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import pro.bzy.boot.framework.utils.SystemConstant;
import pro.bzy.boot.framework.web.domain.entity.Permission;
import pro.bzy.boot.framework.web.domain.entity.Role;
import pro.bzy.boot.framework.web.domain.entity.User;
import pro.bzy.boot.framework.web.service.RolePermissionService;
import pro.bzy.boot.framework.web.service.UserRoleService;
import pro.bzy.boot.framework.web.service.UserService;

@Deprecated
public class CustomRealm extends AuthorizingRealm {

    @Lazy @Autowired
    private UserService userService;
    
    @Lazy @Autowired
    private UserRoleService userRoleService;
    
    @Lazy @Autowired
    private RolePermissionService rolePermissionService;
    
    
    private Long authenTimeout;
    
    public CustomRealm(Long authenTimeout) {
        this.authenTimeout = authenTimeout;
    }
    
    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //获取登录用户名 然后查询用户
        String username = (String) principals.getPrimaryPrincipal();
        User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        
        List<Role> roles = userRoleService.getRolesByUserId(user.getId());
        List<Permission> perms = rolePermissionService.getPermsByRole(roles);
        
        simpleAuthorizationInfo.addRoles(roles.stream().map(r -> r.getName()).collect(Collectors.toList()));
        simpleAuthorizationInfo.addStringPermissions(perms.stream().map(p -> p.getName()).collect(Collectors.toList()));
        return simpleAuthorizationInfo;
    }

    
    
    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //加这一步的目的是在Post请求的时候会先进认证，然后在到请求
        if (token.getPrincipal() == null) return null;
        
        //获取用户信息
        String username = token.getPrincipal().toString();
        User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        if (user == null) {
            throw new AuthenticationException("当前账号不存在");
        } else if (SystemConstant.IS_FORBIDDEN.equals(user.getIsForbidden())) {
            throw new AuthenticationException("该用户已被禁止");
        } else {
            //这里验证authenticationToken和simpleAuthenticationInfo的信息
            SimpleAuthenticationInfo simpleAuthenticationInfo = 
                    new SimpleAuthenticationInfo(username, user.getPassword(), getName());
            Subject subject = SecurityUtils.getSubject();
            subject.getSession().setAttribute("user", user);
            subject.getSession().setTimeout(authenTimeout);
            return simpleAuthenticationInfo;
        }
    }

}
