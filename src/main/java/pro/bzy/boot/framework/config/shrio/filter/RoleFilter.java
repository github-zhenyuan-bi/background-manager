package pro.bzy.boot.framework.config.shrio.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;

/**
 * 
 * 自定义shiro过滤器
 * @author user
 *
 */
public class RoleFilter extends RolesAuthorizationFilter {

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws IOException {

        // 获取资源 配置 对应的角色
        final Subject subject = getSubject(request, response);
        final String[] rolesArray = (String[]) mappedValue;

        if (rolesArray == null || rolesArray.length == 0) {
            return true;
        }

        for (String roleName : rolesArray) {
            if (subject.hasRole(roleName)) {
                return true;
            }
        }

        return false;
    }
}
