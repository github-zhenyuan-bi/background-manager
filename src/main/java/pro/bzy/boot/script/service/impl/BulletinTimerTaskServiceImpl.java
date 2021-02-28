package pro.bzy.boot.script.service.impl;

import pro.bzy.boot.framework.config.schedule.SchedulingRunnable;
import pro.bzy.boot.framework.config.schedule.SchedulingRunnableTaskRegistrar;
import pro.bzy.boot.framework.utils.DateUtil;
import pro.bzy.boot.framework.utils.SystemConstant;
import pro.bzy.boot.script.domain.entity.Bulletin;
import pro.bzy.boot.script.domain.entity.BulletinTemplate;
import pro.bzy.boot.script.domain.entity.BulletinTimerTask;
import pro.bzy.boot.script.mapper.BulletinTimerTaskMapper;
import pro.bzy.boot.script.service.BulletinService;
import pro.bzy.boot.script.service.BulletinTemplateService;
import pro.bzy.boot.script.service.BulletinTimerTaskService;
import pro.bzy.boot.script.utils.ScriptConstant;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 *  服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2020-12-14
 */
@Service
public class BulletinTimerTaskServiceImpl extends ServiceImpl<BulletinTimerTaskMapper, BulletinTimerTask> implements BulletinTimerTaskService {

    @Resource
    private BulletinTimerTaskMapper bulletinTimerTaskMapper;
    @Autowired
    private BulletinService bulletinService;
    @Autowired
    private BulletinTemplateService bulletinTemplateService;
    @Autowired
    private SchedulingRunnableTaskRegistrar schedulingRunnableTaskRegistrar;
    
    
    @Override
    public void addSchedulingBulletinTimerTask(BulletinTimerTask btt) {
        boolean is_sendMode_delay = ScriptConstant.BULLETIN_SEND_MODE_DELAY_CODE.equals(btt.getSendMode()) ;
        SchedulingRunnable schedulingRunnable = new SchedulingRunnable(
                buildTimingBulletinTaskName(btt),
                () -> {
                    try {
                        // 1. 使用定时任务中的模板ID查询模板
                        BulletinTemplate template = bulletinTemplateService.getById(btt.getTemplateId());
                        
                        // 2. 构造通知公告对象
                        Bulletin bulletin = new Bulletin(template);
                        bulletin.setContent(StringUtils.isEmpty(btt.getContent())
                                ? template.getContent()
                                : btt.getContent());
                        bulletin.setSendWay(ScriptConstant.BULLETIN_SENDWAY_XITONG); 
                        bulletin.setSender(btt.getId());
                        bulletin.setSendTime(DateUtil.getNow());
                        
                        // 3. 发送通知
                        bulletinService.save(bulletin);
                        
                        // 4. 更新定时任务结果
                        update(Wrappers.<BulletinTimerTask>lambdaUpdate()
                                .set(BulletinTimerTask::getSendResult, SystemConstant.SCHEDULING_EXECUTE_SUCCESS)
                                .eq(BulletinTimerTask::getId, btt.getId())
                                .set(is_sendMode_delay, 
                                        BulletinTimerTask::getEnable, false));
                    } catch (Exception e) {
                        // 4*. 更新定时任务结果 --错误--
                        e.printStackTrace();
                        update(Wrappers.<BulletinTimerTask>lambdaUpdate()
                                .set(BulletinTimerTask::getSendResult, SystemConstant.SCHEDULING_EXECUTE_FAILURE)
                                .eq(BulletinTimerTask::getId, btt.getId())
                                .set(is_sendMode_delay, 
                                        BulletinTimerTask::getEnable, false));
                    }
                });
        if (is_sendMode_delay)
            schedulingRunnableTaskRegistrar.addSchedulingTask(schedulingRunnable, btt.getSendTime());
        else 
            schedulingRunnableTaskRegistrar.addSchedulingTask(schedulingRunnable, btt.getSendCron());
    }

    
    
    /**
     * 构造一个通知公告定时任务名称
     * @param btt
     * @return
     */
    private String buildTimingBulletinTaskName(BulletinTimerTask btt) {
        boolean is_sendMode_delay = ScriptConstant.BULLETIN_SEND_MODE_DELAY_CODE.equals(btt.getSendMode()) ;
        String prefix = is_sendMode_delay
                ? ScriptConstant.BULLETIN_TIMING_TASK_NAME1
                : ScriptConstant.BULLETIN_TIMING_TASK_NAME2;
        
        return prefix + btt.getId();
    }



    @Override
    public void saveBulletinTimerTaskThenRegisterScheduling(BulletinTimerTask btt) {
        btt.setEnable(true);
        save(btt);
        
        addSchedulingBulletinTimerTask(btt);
    }



    @Override
    public int updateTimerTaskStatusForStartOrStop(BulletinTimerTask btt, boolean startOrStop) {
        // 1. 查询定时推送任务数据
        BulletinTimerTask timerTask = getById(btt.getId());
        if (timerTask.getEnable().equals(startOrStop))
            throw new RuntimeException("定时任务状态已被改变，请刷新重试！");
            
        // 2. 更新定时任务启动/暂停状态
        timerTask.setEnable(startOrStop);
        updateById(timerTask);
        
        // 3. 更新定时器
        if (startOrStop) {
            addSchedulingBulletinTimerTask(timerTask);
            return 1;
        } else {
            removeSchedulingBulletinTimerTask(timerTask);
            return 0;
        }
    }



    @Override
    public void removeTimerTaskThenCancelFromScheduling(String id) {
        // 1. 移除定时任务
        BulletinTimerTask btt = getById(id);
        removeSchedulingBulletinTimerTask(btt);
        
        // 2. 删除定时任务数据记录
        removeById(btt.getId());
    }



    @Override
    public void removeSchedulingBulletinTimerTask(BulletinTimerTask btt) {
        try {
            schedulingRunnableTaskRegistrar.removeSchedulingTask(buildTimingBulletinTaskName(btt));
        } catch (Exception e) {
            throw new RuntimeException("移除定时任务移除，该任务id:" + btt.getId());
        }
    }



    @Override
    public void updateBulletinTimerTaskThenUpdateScheduling(BulletinTimerTask btt) {
        // 1.查询历史数据
        BulletinTimerTask oldBtt = getById(btt.getId());
        
        // 2.覆盖历史数据
        oldBtt.setTemplateId(btt.getTemplateId())
                .setContent(btt.getContent())
                .setSendMode(btt.getSendMode())
                .setSendTime(btt.getSendTime())
                .setSendCron(btt.getSendCron());
        updateById(oldBtt);
        
        // 3.注册到定时器中
        addSchedulingBulletinTimerTask(oldBtt);
    }


    
}