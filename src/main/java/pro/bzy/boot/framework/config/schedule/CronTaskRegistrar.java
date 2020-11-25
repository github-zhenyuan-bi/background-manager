package pro.bzy.boot.framework.config.schedule;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import pro.bzy.boot.framework.web.domain.entity.TimerTask;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.Resource;


/**
 * 动态定时任务 组件
 */
@Slf4j
@Component
public class CronTaskRegistrar implements DisposableBean {

    @Autowired
    private TaskScheduler taskScheduler;
    @Resource
    private SchedulingFunctions schedulingFunctions;
    
    
    /** 当前任务池 */
    private final Map<SchedulingRunnable, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>(4);


    /**
     * 新增定时任务
     * @param task 任务独立执行线程
     * @param cronExpression 执行周期表达式
     */
    public void addCronTask(TimerTask timerTask) {
        String taskName   = timerTask.getName();
        String methodName = timerTask.getMethodname();
        String cron       = timerTask.getCorn();
        Method method = ClassUtils.getMethod(SchedulingFunctions.class, methodName, TimerTask.class);
        addCronTask(new SchedulingRunnable(taskName, () -> {
            try {
                method.invoke(schedulingFunctions, timerTask);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                log.error("启动定时任务失败：{}", timerTask);
            }
        }), cron);
    }
    
    
    /**
     * 新增定时任务
     * @param task 任务独立执行线程
     * @param cronExpression 执行周期表达式
     */
    public void addCronTask(SchedulingRunnable task, String cronExpression) {
        log.info("准备向定时任务池中添加新任务# {}", task.getName());
        
        // 如果任务已存在则删除旧的 重新起一个新的
        if (scheduledTasks.containsKey(task)) {
            log.warn("存在同名定时任务【{}】，现在移除旧任务", task.getName());
            removeCronTask(task);
        }
        
        CronTask ct = new CronTask(task, cronExpression);
        scheduledTasks.put(task, 
                taskScheduler.schedule(ct.getRunnable(), ct.getTrigger()));
        log.info("定时任务【{}】添加完成", task.getName());
    }

    /**
     * 移除定时任务
     * @param task
     */
    public void removeCronTask(String taskName) {
        ScheduledFuture<?> sf = null;
        SchedulingRunnable task = null;
        for (SchedulingRunnable t : scheduledTasks.keySet()) {
            if (t.getName().equals(taskName)) {
                task = t;
                break;
            }
        }
        if (task != null) {
            sf = this.scheduledTasks.remove(task);
            if (sf != null) {
                log.warn("移除定时任务【{}】", task.getName());
                sf.cancel(true);
            }
        }
    }
    
    /**
     * 移除定时任务
     * @param task
     */
    @Deprecated
    public void removeCronTask(SchedulingRunnable task) {
        ScheduledFuture<?> sf = this.scheduledTasks.remove(task);
        if (sf != null) {
            log.warn("移除定时任务【{}】", task.getName());
            sf.cancel(true);
        }
    }

    
    
    @Override
    public void destroy() {
        for (ScheduledFuture<?> task : this.scheduledTasks.values()) {
            task.cancel(true);
        }
        this.scheduledTasks.clear();
    }
}
