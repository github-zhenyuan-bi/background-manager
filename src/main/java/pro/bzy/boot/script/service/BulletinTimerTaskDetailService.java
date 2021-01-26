package pro.bzy.boot.script.service;

import pro.bzy.boot.script.domain.entity.BulletinTimerTaskDetail;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * VIEW 服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2021-01-25
 */
public interface BulletinTimerTaskDetailService extends IService<BulletinTimerTaskDetail> {

    
    /** 统计周期推送人物分页数据 */ 
    Page<BulletinTimerTaskDetail> getPeriodPage(int pageNo, int pageSize, BulletinTimerTaskDetail queryBean);
}
