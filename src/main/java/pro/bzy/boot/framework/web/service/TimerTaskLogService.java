package pro.bzy.boot.framework.web.service;

import pro.bzy.boot.framework.web.domain.entity.TimerTask;
import pro.bzy.boot.framework.web.domain.entity.TimerTaskLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *  服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2020-12-07
 */
public interface TimerTaskLogService extends IService<TimerTaskLog> {

    /**
     * 查询定时任务最后一次执行的日志
     * @param timeTask
     * @return
     */
    TimerTaskLog getTaskLastExecuteLog(TimerTask timeTask);
}
