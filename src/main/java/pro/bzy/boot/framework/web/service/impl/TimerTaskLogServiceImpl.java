package pro.bzy.boot.framework.web.service.impl;

import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.framework.web.domain.entity.TimerTask;
import pro.bzy.boot.framework.web.domain.entity.TimerTaskLog;
import pro.bzy.boot.framework.web.mapper.TimerTaskLogMapper;
import pro.bzy.boot.framework.web.service.TimerTaskLogService;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *  服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2020-12-07
 */
@Service
public class TimerTaskLogServiceImpl extends ServiceImpl<TimerTaskLogMapper, TimerTaskLog> implements TimerTaskLogService {

    @Resource
    private TimerTaskLogMapper timerTaskLogMapper;

    
    
    @Override
    public TimerTaskLog getTaskLastExecuteLog(TimerTask timeTask) {
        Page<TimerTaskLog> page = new Page<>(1, 1);
        this.page(page, Wrappers.<TimerTaskLog>lambdaQuery()
                .eq(TimerTaskLog::getTaskId, timeTask.getId())
                .orderByDesc(TimerTaskLog::getTaskEndTime));
        if (CollectionUtil.isNotEmpty(page.getRecords()))
            return page.getRecords().get(0);
        return null;
    }

}