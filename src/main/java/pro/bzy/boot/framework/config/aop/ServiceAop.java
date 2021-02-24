package pro.bzy.boot.framework.config.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import pro.bzy.boot.framework.config.aop.parent.MyAopSupport;

@Aspect
@Component
public class ServiceAop extends MyAopSupport {

    
    /**
     * 切点 切到com包下所有的service.impl的全部公有方法
     */
    @Pointcut("execution(* pro..*.service.impl..*.*(..))")
    public void aopMethod(){}
    
    
    
    @Around("aopMethod()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 打印切面方法信息
        Logger log = logMethodInfo(joinPoint);
        Object result = joinPoint.proceed();
        return logMethodReturn(log, result);
    }



    @Override
    public JoinPointType getType() {
        return JoinPointType.SERVICE;
    }


}
