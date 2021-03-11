package pro.bzy.boot.framework.config.aop.parent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import lombok.Getter;
import pro.bzy.boot.framework.utils.funtions.ExecutableFunction;


/**
 * 
 * AOP 增强类
 * @author user
 *
 */
public abstract class MyAopSupport {
    
    public enum JoinPointType {
        CONTROlLER("Controller", "###############"),
        SERVICE("Service",       "==============="),
        ANNOTATION("Annotation", "---------------")
        ;
        @Getter private final String type;
        @Getter private final String split;
        private JoinPointType(String type, String split) {
            this.type = type;
            this.split = split;
        }
    }
    
    
    
    /**
     * AOP类型实现
     * @return
     */
    abstract public JoinPointType getType();
    
    

    /** 日志对象存储器 */
    protected Map<Class<?>, Logger> logs = Maps.newConcurrentMap();
    
    
    
    /**
     * 获取切面对象得日志打印对象
     * @param joinPoint
     * @return
     */
    protected Logger getJoinPointObjectLogger(ProceedingJoinPoint joinPoint) {
        Class<?> clazz = joinPoint.getTarget().getClass();
        Logger log = logs.get(clazz);
        if (log == null) {
            log = LoggerFactory.getLogger(clazz);
            logs.put(clazz, log);
        } 
        return log;
    }
    
    
    
    /**
     * 使用切面点得日志打印对象进行切点方法得信息输出
     * @param joinPoint
     */
    protected Logger logMethodInfo(ProceedingJoinPoint joinPoint) {
        Logger log = getJoinPointObjectLogger(joinPoint);
        
        log.debug("{} AOP -> {} <- START {}", getType().split, getType().type, getType().split);
        log.debug("【METHOD_NAME 】==> | {}", joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            int size = args.length;
            for (int i = 0; i < size; i++) {
                log.debug("【ARGUMENT #{} 】==> | {}", i, args[i]);
            }
        }
        return log;
    }
    
    
    
    /**
     * 使用切面点得日志打印对象进行切点方法得执行异常信息
     * @param e
     * @throws Throwable 
     */
    protected void logMethodException(Logger log, Throwable e) throws Throwable {
        logMethodException(log, e, null);
    }
    
    
    
    /**
     * 使用切面点得日志打印对象进行切点方法得执行异常信息
     * @param e
     * @throws Throwable 
     */
    protected void logMethodException(Logger log, Throwable e, ExecutableFunction exce) throws Throwable {
        log.error("【EXCEPTIOP   】×××××", e);
        if (exce != null)
            exce.execute();
        throw e;
    }
    
    
    
    /**
     * 使用切面点得日志打印对象进行切点方法得返回结果
     * @param log
     * @param obj
     * @return
     */
    protected Object logMethodReturn(Logger log, Object obj) {
        if (obj != null) {
            int index = 0;
            if (obj.getClass() == ArrayList.class)
                for (Object item : (ArrayList<?>) obj)
                    log.debug("【RETURN row{} 】==># {}", index++, item);
            if (obj.getClass() == HashMap.class) 
                for (Map.Entry<?, ?> entry : ((HashMap<?, ?>)obj).entrySet())
                    log.debug("【RETURN row{} 】==># {}", index++, entry);
            
        } else {
            log.debug("【RETURN      】==># {}", obj);
        }
        log.debug("{} AOP -> {} <- FINISH {}", getType().split, getType().type, getType().split);
        return obj;
    }
}
