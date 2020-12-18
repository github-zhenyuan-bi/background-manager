package pro.bzy.boot.framework.config.schedule;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;


/**
 * 动态定时任务 组件
 */
@Slf4j
@Component
public class SchedulingRunnableTaskRegistrar implements DisposableBean {

    @Autowired
    private TaskScheduler taskScheduler;
    
    
    
    /** 当前任务池 */
    private final Map<SchedulingRunnable, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>(4);


    
    /**
     * 新增定时任务
     * @param runnable
     * @param cronExpression
     */
    public void addSchedulingTask(SchedulingRunnable runnable, String cronExpression) {
        log.info("准备向定时任务池中添加新定时任务【{}】", runnable.getName());
        if (scheduledTasks.containsKey(runnable)) {
            log.warn("存在同名定时任务【{}】，现在移除旧任务", runnable.getName());
            removeSchdulingTask(runnable);
        }
        scheduledTasks.put(runnable, 
                taskScheduler.schedule(runnable, new CronTrigger(cronExpression)));
        log.info("定时任务【{}】添加成功", runnable.getName());
    }
    

    
    
    /**
     * 移除定时任务
     * @param runnableName
     */
    public void removeSchedulingTask(String runnableName) {
        for (SchedulingRunnable sr : scheduledTasks.keySet()) {
            if (sr.getName().equals(runnableName)) {
                removeSchdulingTask(sr);
                return;
            }
        }
    }
    
    
    
    /**
     * 移除定时任务
     * @param task
     */
    public void removeSchdulingTask(SchedulingRunnable runnable) {
        if (scheduledTasks.containsKey(runnable)) {
            ScheduledFuture<?> sf = this.scheduledTasks.remove(runnable);
            sf.cancel(true);
            log.info("移除定时任务【{}】成功", runnable.getName());
        } else {
            log.warn("并未发现任务池中存在任务#【{}】", runnable.getName());
        }
    }
    

    
    
    /**
     * 销毁
     */
    @Override
    public void destroy() {
        for (ScheduledFuture<?> task : this.scheduledTasks.values()) {
            task.cancel(true);
        }
        this.scheduledTasks.clear();
    }
}
