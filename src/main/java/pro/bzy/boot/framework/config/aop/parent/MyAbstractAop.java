package pro.bzy.boot.framework.config.aop.parent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import pro.bzy.boot.framework.utils.ArgUtil;

/**
 * 我的Aop抽象
 */
public abstract class MyAbstractAop {
    
    /** 切面切到的所有控制层的日志对象 */
    protected Map<Class<?>, Logger> logs = Maps.newConcurrentMap();
    
    protected ConcurrentMap<String, String> apiDescCache = Maps.newConcurrentMap();
    
    
    /**  
     * 获取切点位置的日志对象
     * @param joinPoint 切点
     * @return
     */
    protected Logger getJoinPointLogger(ProceedingJoinPoint joinPoint) {
        Class<?> clazz = joinPoint.getTarget().getClass();
        Logger log = logs.get(clazz);
        
        log = ArgUtil.defaultValueIfNull(log, LoggerFactory.getLogger(clazz));
        return log;
    }
    
    
    
    /**
     * 打印切面信息
     * @param joinPoint 切点
     * @return
     */
    protected void logJoinPointMethodInfo(ProceedingJoinPoint joinPoint) {
        Logger log = getJoinPointLogger(joinPoint);
        log.debug("执行方法# {}", joinPoint.getSignature().getName());
        
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            Arrays.stream(args).forEach( arg -> {
                if (arg instanceof List) 
                    log.debug("参数# {}", arg.getClass().getComponentType());
                else
                    log.debug("参数# {}", arg);
            });
        }
    }
    
    
    
    
    /**
     * 获取ip
     *
     * @param request
     * @return
     */
    protected String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
