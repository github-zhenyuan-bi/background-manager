package pro.bzy.boot.framework.config.schedule;

import pro.bzy.boot.framework.config.exceptions.MySchedulingException;
import pro.bzy.boot.framework.config.schedule.annoations.SchedulingFunctionAnnoation;
import pro.bzy.boot.framework.web.domain.entity.TimerTask;

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
     * 定时任务执行方法 任何重写该方法的都应添加注解
     * @throws MySchedulingException
     */
    @SchedulingFunctionAnnoation
    Object exec(TimerTask task) throws MySchedulingException;
    
}
