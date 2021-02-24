package pro.bzy.boot.framework.config.aop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import pro.bzy.boot.framework.config.aop.parent.MyAopSupport;
import pro.bzy.boot.framework.config.exceptions.FormValidatedException;

/**
 * 
 * 控制层切面增强
 * @author zhenyuan.bi
 *
 */
@Aspect
@Component
public class ControllerAop extends MyAopSupport {

    
    /**
     * 切点 切到com包下所有的controller的全部公有方法
     */
    @Pointcut("execution(* pro..*.controller..*.*(..))")
    public void aopMethod(){}
    
    
    
    /**
     * 环绕通知
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("aopMethod()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 打印切面方法信息
        Logger log = logMethodInfo(joinPoint);
        
        // 校验切面表单信息
        checkBindingResult(joinPoint);
        
        // 执行切点
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            logMethodException(log, e);
        }
        
        // 方法返回值
        return logMethodReturn(log, result);
    }
    
    
    
    /**
     * 控制层 请求的表单参数校验
     * @param joinPoint 切点
     * @return
     */
    private void checkBindingResult(ProceedingJoinPoint joinPoint) {
        // 找到方法参数中的 BindingResult 对象
        BindingResult bindingResult = null;
        for(Object arg: joinPoint.getArgs()){
            if(arg instanceof BindingResult){
                bindingResult = (BindingResult) arg;
                break;
            }
        }
        // 如果存在就查询校验结果
        if(bindingResult != null && bindingResult.hasErrors()){
            List<FieldError> errors = bindingResult.getFieldErrors();//获取字段参数不合法的错误集合
            Map<String, String> res = new HashMap<>(errors.size());
            for(FieldError error : errors){
                res.put(error.getField(), error.getDefaultMessage());
            }
            throw new FormValidatedException(res);
        }
    }



    @Override
    public JoinPointType getType() {
        return JoinPointType.CONTROlLER;
    }
}
