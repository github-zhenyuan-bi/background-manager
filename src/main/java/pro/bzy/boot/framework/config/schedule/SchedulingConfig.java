package pro.bzy.boot.framework.config.schedule;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import pro.bzy.boot.framework.utils.ArgUtil;

import lombok.Data;

/**
 * 定时任务线程池
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.config.timer")
public class SchedulingConfig {
    
    private Integer threadPoolSize;
    
    private String threadPrefix;
    
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        // 定时任务执行线程池核心线程数
        taskScheduler.setPoolSize(ArgUtil.defaultValueIfNull(threadPoolSize, 4));
        taskScheduler.setRemoveOnCancelPolicy(true);
        taskScheduler.setThreadNamePrefix(ArgUtil.defaultValueIfNull(threadPrefix, "Schedule-task-"));
        return taskScheduler;
    }
}
