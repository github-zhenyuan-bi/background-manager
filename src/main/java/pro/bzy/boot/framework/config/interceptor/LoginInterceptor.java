package pro.bzy.boot.framework.config.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import pro.bzy.boot.framework.config.interceptor.parent.MyAbstractInterceptor;
import pro.bzy.boot.framework.utils.DateUtil;
import pro.bzy.boot.framework.web.domain.entity.Log;
import pro.bzy.boot.framework.web.domain.entity.User;
import pro.bzy.boot.framework.web.mapper.LogMapper;

public class LoginInterceptor extends MyAbstractInterceptor implements HandlerInterceptor {

    
    public LoginInterceptor(LogMapper logMapper) {
        super(logMapper);
    }

    
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        //User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        //String accessor = user == null? null : user.getId();
        logMapper.insert(Log.builder()
                .id(null)
                .accessor("unset")
                .accessTime(DateUtil.getNow())
                .accessModule(request.getRequestURI())
                .accessorIp(request.getRemoteAddr())
                .field1("用户登录")
                .field2(ex == null? null : ex.getMessage())
                .build());
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    
    
    
}
