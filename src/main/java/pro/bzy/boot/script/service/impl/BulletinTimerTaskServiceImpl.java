package pro.bzy.boot.script.service.impl;

import pro.bzy.boot.script.domain.entity.BulletinTimerTask;
import pro.bzy.boot.script.mapper.BulletinTimerTaskMapper;
import pro.bzy.boot.script.service.BulletinTimerTaskService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}