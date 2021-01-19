package pro.bzy.boot.script.service;

import pro.bzy.boot.script.domain.entity.BulletinTimerTask;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *  服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2020-12-14
 */
public interface BulletinTimerTaskService extends IService<BulletinTimerTask> {

    /** 新增定时推送任务 */
    void saveBulletinTimerTaskThenRegisterScheduling(BulletinTimerTask btt);
    
    
    /** 注册定时任务到定时器 */
    void registerSchedulingBulletinTimerTask(BulletinTimerTask btt);
    
    
    /** 启动/暂停定时任务 启动返回1 暂停返回0 */
    int startOrStopTimerTask(BulletinTimerTask btt, boolean startOrStop);
    
    
    /** 移除定时推送任务 */
    void removeTimerTaskThenCancelFromScheduling(String id);
    
    
    /** 从定时器中移除定时任务 */
    void cancelSchedulingBulletinTimerTask(BulletinTimerTask btt);
    
    
    /** 更新定时任务 */
    void updateBulletinTimerTaskThenUpdateScheduling(BulletinTimerTask btt);
}
