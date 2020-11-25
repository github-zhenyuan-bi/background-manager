package pro.bzy.boot.framework.config.schedule;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import pro.bzy.boot.framework.utils.CronUtil;
import pro.bzy.boot.framework.web.domain.entity.Log;
import pro.bzy.boot.framework.web.domain.entity.TimerTask;
import pro.bzy.boot.framework.web.service.LogService;
import pro.bzy.boot.script.service.JubenCharacterService;
import pro.bzy.boot.script.service.JubenService;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Component
public class SchedulingFunctions {

    @Resource
    private LogService logService;
    @Resource
    private JubenService jubenService;
    @Resource
    private JubenCharacterService jubenCharacterService;
    
    
    
    /**
     * 清除三天前的日志记录 且把该日志记录归档整理成excel
     */
    @Description("周期归档并清理系统日志")
    @Transactional(rollbackFor= {Exception.class})
    public void handleLog(TimerTask task) {
        try {
            // 1. 根据cron表达式得到上一个执行日期
            Date lastTaskExecDate = CronUtil.getLastTriggerTime(task.getCorn());
            
            LambdaQueryWrapper<Log> wrapper = Wrappers.<Log>lambdaQuery().le(Log::getAccessTime, lastTaskExecDate);
            // 2. 对将要删除的日志进行归档处理
            List<Log> willDeletedLog = logService.list(wrapper);
            logService.archiveLog(willDeletedLog);
            
            // 3. 删除上一个周期前得全部日志
            logService.remove(wrapper);
        } catch (Exception e) {
            throw new RuntimeException("定时处理系统日志异常，原因：" + e.getMessage());
        }
    }
    
    
    
    @Description("按固定周期清理不被使用的上传照片")
    public void handleUploadImg(TimerTask task) {
        try {
            // 1. 清理上传但未使用的剧本封面照片
            jubenService.clearUploadCoverImg();
            
            // 2. 清理上传但未使用的剧本人物照片
            jubenCharacterService.clearUploadCharacterImg();
        } catch (Exception e) {
            throw new RuntimeException("清理上传未使用得图片文件失败，原因：" + e.getMessage());
        }
    }
}
