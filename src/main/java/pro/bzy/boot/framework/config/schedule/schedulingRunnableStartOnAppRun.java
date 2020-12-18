package pro.bzy.boot.framework.config.schedule;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.framework.utils.SystemConstant;
import pro.bzy.boot.framework.web.domain.entity.TimerTask;
import pro.bzy.boot.framework.web.service.TimerTaskService;


@Configuration
public class schedulingRunnableStartOnAppRun implements ApplicationRunner{

    @Resource
    private TimerTaskService timerTaskService;
    
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        runDBSchedule();
    }

    
    private void runDBSchedule() {
        // 查询全部可用得定时任务
        List<TimerTask> tasks = timerTaskService.list(
                Wrappers.<TimerTask>lambdaQuery().eq(TimerTask::getEnable, SystemConstant.ENABLE));
        
        // 逐个添加定时任务
        if (!CollectionUtil.isEmpty(tasks)) {
            tasks.forEach(task -> {
                timerTaskService.registerTimerTaskToSchedulingRunnable(task);
            });
        }
    }
}
