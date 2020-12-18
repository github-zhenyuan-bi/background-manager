package pro.bzy.boot.script.service.impl;

import pro.bzy.boot.script.domain.entity.Bulletin;
import pro.bzy.boot.script.mapper.BulletinMapper;
import pro.bzy.boot.script.service.BulletinService;

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
public class BulletinServiceImpl extends ServiceImpl<BulletinMapper, Bulletin> implements BulletinService {

    @Resource
    private BulletinMapper bulletinMapper;

}