package pro.bzy.boot.framework.config.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import pro.bzy.boot.framework.config.interceptor.parent.MyAbstractInterceptor;
import pro.bzy.boot.framework.web.domain.bean.YmlPageConstant;
import pro.bzy.boot.framework.web.domain.entity.Constant;
import pro.bzy.boot.framework.web.mapper.LogMapper;
import pro.bzy.boot.framework.web.service.ConstantService;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 全局拦截器
 * @author zhenyuan.bi
 *
 */
@NoArgsConstructor
@Slf4j
public class BaseInterceptor extends MyAbstractInterceptor implements HandlerInterceptor{

    
    private ConstantService constantService;
    
    private YmlPageConstant ymlPageConstant;
    
    public BaseInterceptor(
            final LogMapper logMapper, 
            final ConstantService constantService,
            final YmlPageConstant ymlPageConstant) {
        super(logMapper);
        this.constantService = constantService;
        this.ymlPageConstant = ymlPageConstant;
    }
    
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.trace("请求URI# {}", request.getRequestURI());
        log.trace("请求IP# {}", request.getRemoteAddr());
        log.trace("请求方法# {}", request.getMethod());
        log.trace("请求头# {}", request.getHeaderNames());
        log.trace("请求参数# {}", request.getParameterMap());
        log.trace("请求主机# {}", request.getRemoteHost());
        
        System.err.println(request.getRequestURI() );
        // 请求日志打印
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // 再渲染页面前将常量配置的数据加载
        if (modelAndView != null) {
            Map<String, Object> model = modelAndView.getModel();
            // yml页面常量配置
            model.put("yml_constants", ymlPageConstant);
            // 数据库常量配置
            model.put("db_constants", Constant.toMap(constantService.list()));
        }
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    
}
