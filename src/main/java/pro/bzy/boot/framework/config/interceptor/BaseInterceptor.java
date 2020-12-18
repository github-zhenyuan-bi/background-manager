package pro.bzy.boot.framework.config.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import pro.bzy.boot.framework.config.interceptor.parent.MyAbstractInterceptor;
import pro.bzy.boot.framework.config.yml.YmlBean;
import pro.bzy.boot.framework.utils.SystemConstant;
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
@Slf4j
@NoArgsConstructor
public class BaseInterceptor extends MyAbstractInterceptor implements HandlerInterceptor{

    /** 常量数据服务 */
    private ConstantService constantService;
    
    /** yml配置常量 */
    private YmlBean ymlBean;
    
    /** 构造器 */
    public BaseInterceptor(
            final LogMapper logMapper, 
            final ConstantService constantService,
            final YmlBean ymlBean) {
        super(logMapper);
        this.constantService = constantService;
        this.ymlBean = ymlBean;
    }
    
    
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.err.println(request.getRequestURI() );
        
        // 请求日志打印
        try {
            logToDB(request, handler);
        } catch (Exception e) {
            log.error("存储访问日志失败", e);
        }
        
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
    
    

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // 再渲染页面前将常量配置的数据加载
        if (modelAndView != null) {
            Map<String, Object> model = modelAndView.getModel();
            // yml页面常量配置
            model.put("yml_constants", ymlBean.getPageConstant());
            // 数据库常量配置
            model.put("db_constants", Constant.toMap(constantService.list()));
            // jwttoken获取基本数据
            model.put(SystemConstant.JWT_BASESTORAGE_DATAS_KEY, request.getAttribute(SystemConstant.JWT_BASESTORAGE_DATAS_KEY));
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
