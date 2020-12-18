package pro.bzy.boot.framework.web.service.impl;

import pro.bzy.boot.framework.config.exceptions.MySchedulingException;
import pro.bzy.boot.framework.config.schedule.SchedulingFunction;
import pro.bzy.boot.framework.config.schedule.SchedulingRunnable;
import pro.bzy.boot.framework.config.schedule.SchedulingRunnableTaskRegistrar;
import pro.bzy.boot.framework.utils.SpringContextUtil;
import pro.bzy.boot.framework.utils.SystemConstant;
import pro.bzy.boot.framework.web.domain.entity.TimerTask;
import pro.bzy.boot.framework.web.mapper.TimerTaskMapper;
import pro.bzy.boot.framework.web.service.TimerTaskLogService;
import pro.bzy.boot.framework.web.service.TimerTaskService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import javax.annotation.Resource;

/**
 * 定时器任务 服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-01
 */
@Service
public class TimerTaskServiceImpl extends ServiceImpl<TimerTaskMapper, TimerTask> implements TimerTaskService {

    @Resource
    private TimerTaskMapper timerTaskMapper;
    @Resource
    private TimerTaskLogService timerTaskLogService;
    @Resource
    private SchedulingRunnableTaskRegistrar schedulingRunnableTaskRegistrar;
    
    @Override
    @Transactional(rollbackFor= {Exception.class})
    public void addTimertask(TimerTask timerTask) {
        // 新增定时任务入库
        save(timerTask);
        registerTimerTaskToSchedulingRunnable(timerTask);
    }
    
    

    @Override
    @Transactional(rollbackFor= {Exception.class})
    public void editTimertask(TimerTask timerTask) {
        // 查询旧的定时任务信息
        TimerTask old = getById(timerTask.getId());
        
        // 从定时任务池种删除该任务
        schedulingRunnableTaskRegistrar.removeSchedulingTask(old.getName());
        
        // 更新定时任务入库
        updateById(timerTask);
        
        // 如果任务状态为启用 则开启定时任务
        registerTimerTaskToSchedulingRunnable(timerTask);
    }

    
    
    @Override
    public void suspendTimertask(TimerTask timerTask) {
        // 查询任务配置详情
        TimerTask task = getById(timerTask.getId());
        
        task.setEnable(timerTask.getEnable());
        if (SystemConstant.UNABLE.equals(timerTask.getEnable())) {
            schedulingRunnableTaskRegistrar.removeSchedulingTask(task.getName());
        } else {
            registerTimerTaskToSchedulingRunnable(task);
        }
        
        // 更新请用状态
        updateById(task);
    }



    @Override
    public void registerTimerTaskToSchedulingRunnable(TimerTask timerTask) {
        // 如果任务状态为启用 则开启定时任务
        if (SystemConstant.ENABLE.equals(timerTask.getEnable())) {
            SchedulingRunnable schedulingRunnable = new SchedulingRunnable(
                    timerTask.getName(), 
                    () -> {
                        Map<String, SchedulingFunction> sfs = SpringContextUtil.getBeansOfType(SchedulingFunction.class);
                        SchedulingFunction function = sfs.get(timerTask.getMethodname());
                        if (function == null)
                            throw new MySchedulingException("定时任务对应得方法类不存在：" + timerTask.getMethodname());
                        function.exec(timerTask);});
            schedulingRunnableTaskRegistrar.addSchedulingTask(schedulingRunnable, timerTask.getCorn());
        }
    }

}