package pro.bzy.boot.script.config.schedule.functions;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.script.domain.entity.BulletinTimerTask;
import pro.bzy.boot.script.service.BulletinTimerTaskService;

@Configuration
public class BulletinTimerTaskStarted implements ApplicationRunner {

    @Resource
    private BulletinTimerTaskService bulletinTimerTaskService;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<BulletinTimerTask> btts = bulletinTimerTaskService.list(Wrappers.<BulletinTimerTask>lambdaQuery()
                .eq(BulletinTimerTask::getEnable, true));
        
        if (CollectionUtil.isNotEmpty(btts))
            btts.forEach(btt -> bulletinTimerTaskService.registerSchedulingBulletinTimerTask(btt));
    }

}
