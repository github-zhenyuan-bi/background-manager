package pro.bzy.boot.script.service.impl;

import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.framework.utils.CronUtil;
import pro.bzy.boot.framework.utils.DateUtil;
import pro.bzy.boot.script.domain.entity.BulletinTimerTaskDetail;
import pro.bzy.boot.script.mapper.BulletinTimerTaskDetailMapper;
import pro.bzy.boot.script.service.BulletinTimerTaskDetailService;
import pro.bzy.boot.script.utils.ScriptConstant;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

/**
 * VIEW 服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2021-01-25
 */
@Service
public class BulletinTimerTaskDetailServiceImpl extends ServiceImpl<BulletinTimerTaskDetailMapper, BulletinTimerTaskDetail> implements BulletinTimerTaskDetailService {

    @Resource
    private BulletinTimerTaskDetailMapper bulletinTimerTaskDetailMapper;

    
    
    @Override
    public Page<BulletinTimerTaskDetail> getPeriodPage(int pageNo, int pageSize, BulletinTimerTaskDetail queryBean) {
        Page<BulletinTimerTaskDetail> page = new Page<>(pageNo, pageSize);
        page(page, Wrappers.<BulletinTimerTaskDetail>lambdaQuery(queryBean)
                .eq(BulletinTimerTaskDetail::getSendMode, ScriptConstant.BULLETIN_SEND_MODE_PERIOD));
        
        List<BulletinTimerTaskDetail> taskDetails = page.getRecords();
        if (CollectionUtil.isNotEmpty(taskDetails)) {
            taskDetails.forEach(detail -> {
                String cron = detail.getSendCron();
                long nextExecTime = CronUtil.getNextTriggerTime(cron);
                detail.setNextTimeForCron(DateUtil.getDate(nextExecTime));
            });
        }
        return page;
    }

}