package pro.bzy.boot.framework.config.interceptor.parent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.util.StringUtils;

import pro.bzy.boot.framework.utils.DateUtil;
import pro.bzy.boot.framework.utils.SystemConstant;
import pro.bzy.boot.framework.web.domain.entity.Log;
import pro.bzy.boot.framework.web.domain.entity.User;
import pro.bzy.boot.framework.web.mapper.LogMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * 我的拦截器抽象父类
 * @author zhenyuan.bi
 *
 */
@NoArgsConstructor
@AllArgsConstructor
public abstract class MyAbstractInterceptor {

    /**
     * 保存未登陆认证前的一次访问地址
     * @param request
     */
    protected void saveLastAccessUrlIfUnlogin(HttpServletRequest request) {
        request.getSession().setAttribute(SystemConstant.SAVED_URL, request.getRequestURI());
    }
    
    
    
    /**
     * 获取请求IP
     * @param request
     * @return
     */
    protected String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) 
            ip = request.getHeader("Proxy-Client-IP");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) 
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) 
            ip = request.getRemoteAddr();
        
        // 多个路由时，取第一个非unknown的ip
        final String[] arr = ip.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ip = str;
                break;
            }
        }
        return ip;
    }
    
    
    
    /**
     * 检测是否认证登陆
     * @param response
     * @return
     */
    protected boolean isAuthenticated(HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        boolean isAuthen = subject.isAuthenticated();
        return isAuthen;
    }
    
    
    @Getter 
    @Setter 
    protected LogMapper logMapper;
    
    protected void logToDB(HttpServletRequest request) {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        String accessor = user == null? null : user.getId();
        logMapper.insert(Log.builder()
                .id(null)
                .accessor(accessor)
                .accessTime(DateUtil.getNow())
                .accessModule(request.getRequestURI())
                .accessorIp(request.getRemoteAddr())
                .build());
    }
}
