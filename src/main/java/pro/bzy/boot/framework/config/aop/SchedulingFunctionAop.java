package pro.bzy.boot.framework.config.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pro.bzy.boot.framework.config.aop.parent.MyAopSupport;
import pro.bzy.boot.framework.config.constant.Schedule_constant;
import pro.bzy.boot.framework.config.exceptions.MyAopException;
import pro.bzy.boot.framework.utils.DateUtil;
import pro.bzy.boot.framework.web.domain.entity.TimerTask;
import pro.bzy.boot.framework.web.domain.entity.TimerTaskLog;
import pro.bzy.boot.framework.web.service.TimerTaskLogService;


/**
 * 
 * 系统定时任务执行日志记录方法
 * @author user
 *
 */
@Aspect
@Component
public class SchedulingFunctionAop extends MyAopSupport{

    @Autowired
    private TimerTaskLogService timerTaskLogService;
    
    
    /**
     * 切向所以包含注解 @SchedulingFunctionAnnoation 的类
     */
    @Pointcut("@annotation(pro.bzy.boot.framework.config.schedule.annoations.SchedulingFunctionAnnoation)")
    public void schedulingFunctionLog(){}
    
    
    
    /**
     * 切面方法 在系统定时任务执行时进行耗时统计和日志记录
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("schedulingFunctionLog()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object res = null;
        TimerTaskLog timertasklog = null;
        try {
            TimerTask timerTask = getArgOfTimerTask(joinPoint);
            timertasklog = new TimerTaskLog(timerTask);
            
            res =  joinPoint.proceed();
            
            timertasklog.setTaskEndTime(DateUtil.getNow())
                    .calculateCostTime()
                    .setExecSuccess(Schedule_constant.SCHEDULING_EXECUTE_SUCCESS)
                    .setExecResult(res.toString());
            timerTaskLogService.save(timertasklog);
        } catch (Throwable e) {
            if (timertasklog != null) {
                timertasklog.setExecSuccess(Schedule_constant.SCHEDULING_EXECUTE_FAILURE);
                timertasklog.setExecResult(e.getMessage());
                timerTaskLogService.save(timertasklog);
            }
            throw e;
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
        throw new MyAopException("切面方法中未找到TimerTask参数");
    }


    @Override
    public JoinPointType getType() {
        return JoinPointType.ANNOTATION;
    }
}
