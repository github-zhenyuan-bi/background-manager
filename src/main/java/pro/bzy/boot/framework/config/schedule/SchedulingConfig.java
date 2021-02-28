package pro.bzy.boot.framework.config.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import pro.bzy.boot.framework.config.yml.Timer;
import pro.bzy.boot.framework.config.yml.YmlBean;
import pro.bzy.boot.framework.utils.ArgUtil;

import lombok.Data;

/**
 * 定时任务线程池
 */
@Data
@Configuration
public class SchedulingConfig {
    
    @Autowired
    private YmlBean ymlBean;
    
    @Bean
    public TaskScheduler taskScheduler() {
        Timer timerConfig = ymlBean.getConfig().getTimer();
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        // 定时任务执行线程池核心线程数
        taskScheduler.setPoolSize(ArgUtil.defaultValueIfNull(timerConfig.getThreadPoolSize(), 8));
        taskScheduler.setRemoveOnCancelPolicy(true);
        taskScheduler.setThreadNamePrefix(ArgUtil.defaultValueIfNull(timerConfig.getThreadPrefix(), "Schedule-task-"));
        return taskScheduler;
    }
}
