package pro.bzy.boot.framework.config.schedule;

import pro.bzy.boot.framework.config.exceptions.MySchedulingException;
import pro.bzy.boot.framework.config.schedule.annoations.SchedulingFunctionAnnoation;

/**
 * 
 * 定时任务执行方法 任何实现该接口得类都可以被用作为定时任务
 * @author user
 *
 */
public interface SchedulingFunction {

    
    /**
     * 定时任务得描述
     * @return
     */
    String getDescription();
    
    
    /**
     * 定时任务执行方法 需要对定时任务进行切面日志处理的 需要添加该注解
     * @throws MySchedulingException
     */
    @SchedulingFunctionAnnoation
    Object exec(Object task) throws MySchedulingException;
    
}
