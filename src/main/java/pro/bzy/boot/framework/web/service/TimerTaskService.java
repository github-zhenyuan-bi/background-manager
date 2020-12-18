package pro.bzy.boot.framework.web.service;

import pro.bzy.boot.framework.web.domain.entity.TimerTask;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 定时器任务 服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-01
 */
public interface TimerTaskService extends IService<TimerTask> {

    /** 动态添加定时任务 */
    void addTimertask(TimerTask timerTask);
    
    /** 动态修改定时任务 */
    void editTimertask(TimerTask timerTask);
    
    /** 动态启动或者挂起定时任务 */
    void suspendTimertask(TimerTask timerTask);
    
    
    /** 注册定时任务 */
    void registerTimerTaskToSchedulingRunnable(TimerTask timerTask);
}
