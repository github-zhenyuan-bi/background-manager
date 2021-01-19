package pro.bzy.boot.framework.config.schedule.functions;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import pro.bzy.boot.framework.config.exceptions.MySchedulingException;
import pro.bzy.boot.framework.config.schedule.SchedulingFunction;
import pro.bzy.boot.framework.config.schedule.annoations.SchedulingFunctionAnnoation;
import pro.bzy.boot.framework.utils.CronUtil;
import pro.bzy.boot.framework.utils.DateUtil;
import pro.bzy.boot.framework.web.domain.entity.Log;
import pro.bzy.boot.framework.web.domain.entity.TimerTask;
import pro.bzy.boot.framework.web.domain.entity.TimerTaskLog;
import pro.bzy.boot.framework.web.service.LogService;
import pro.bzy.boot.framework.web.service.TimerTaskLogService;

@Component
public class HandleSystemLogFunction implements SchedulingFunction {
    
    @Resource
    private LogService logService;
    @Resource
    private TimerTaskLogService timerTaskLogService;
    
    
    @Override
    @SchedulingFunctionAnnoation
    @Transactional(rollbackFor= {Exception.class})
    public Object exec(Object obj) throws MySchedulingException {
        try {
            TimerTask task = null;
            if (obj instanceof TimerTask)
                task = (TimerTask) obj;
            
            Date lastTaskExecDate = null;
            // 0. 获取最后一次执行的日志
            TimerTaskLog lastTaskLog = timerTaskLogService.getTaskLastExecuteLog(task);
            if (lastTaskLog != null) 
                lastTaskExecDate = lastTaskLog.getTaskEndTime();
            else
            // 1. 根据cron表达式得到上一个执行日期
                lastTaskExecDate = CronUtil.getLastTriggerTime(task.getCorn());
            
            LambdaQueryWrapper<Log> wrapper = Wrappers.<Log>lambdaQuery()
                    .between(Log::getAccessTime, lastTaskExecDate, DateUtil.getNow())
                    .orderByDesc(Log::getAccessTime);
            
            // 2. 对将要删除的日志进行归档处理
            List<Log> willDeletedLog = logService.list(wrapper);
            File excelFile = logService.archiveLog(willDeletedLog);
            
            // 3. 删除上一个周期前得全部日志
            //logService.remove(wrapper);
            
            return excelFile.getPath();
        } catch (Exception e) {
            throw new MySchedulingException("定时处理系统日志异常，原因：" + e.getMessage());
        }
    }

    
    @Override
    public String getDescription() {
        return "定期处理系统日志";
    }

}
