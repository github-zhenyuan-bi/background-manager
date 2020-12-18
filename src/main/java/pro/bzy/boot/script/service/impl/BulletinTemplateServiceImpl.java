package pro.bzy.boot.script.service.impl;

import pro.bzy.boot.script.domain.entity.BulletinTemplate;
import pro.bzy.boot.script.mapper.BulletinTemplateMapper;
import pro.bzy.boot.script.service.BulletinTemplateService;

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
public class BulletinTemplateServiceImpl extends ServiceImpl<BulletinTemplateMapper, BulletinTemplate> implements BulletinTemplateService {

    @Resource
    private BulletinTemplateMapper bulletinTemplateMapper;

}