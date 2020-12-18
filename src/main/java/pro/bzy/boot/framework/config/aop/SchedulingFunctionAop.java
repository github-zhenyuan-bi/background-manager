package pro.bzy.boot.framework.config.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pro.bzy.boot.framework.config.aop.parent.MyAbstractAop;
import pro.bzy.boot.framework.utils.DateUtil;
import pro.bzy.boot.framework.utils.SystemConstant;
import pro.bzy.boot.framework.web.domain.entity.TimerTask;
import pro.bzy.boot.framework.web.domain.entity.TimerTaskLog;
import pro.bzy.boot.framework.web.service.TimerTaskLogService;

@Aspect
@Component
public class SchedulingFunctionAop extends MyAbstractAop{

    @Autowired
    private TimerTaskLogService timerTaskLogService;
    
    
    @Pointcut("@annotation(pro.bzy.boot.framework.config.schedule.annoations.SchedulingFunctionAnnoation)")
    public void schedulingFunctionLog(){}
    
    
    @Around("schedulingFunctionLog()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object res = null;
        TimerTaskLog timertasklog = null;
        try {
            TimerTask timerTask = getArgOfTimerTask(joinPoint);
            timertasklog = new TimerTaskLog(timerTask);
            res =  joinPoint.proceed();
            timertasklog.setTaskEndTime(DateUtil.getNow());
            timertasklog.calculateCostTime();
            timertasklog.setExecSuccess(SystemConstant.SCHEDULING_EXECUTE_SUCCESS);
            timertasklog.setExecResult(res.toString());
        } catch (Throwable e) {
            timertasklog.setExecSuccess(SystemConstant.SCHEDULING_EXECUTE_FAILURE);
            timertasklog.setExecResult(e.getMessage());
            throw e;
        } finally {
            timerTaskLogService.save(timertasklog);
        }
        return res;
    }
    
    
    private TimerTask getArgOfTimerTask(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                if (arg instanceof TimerTask) {
                    return (TimerTask) arg;
                }
            }
        }
        throw new RuntimeException("切面方法中未找到TimerTask参数");
    }
}
