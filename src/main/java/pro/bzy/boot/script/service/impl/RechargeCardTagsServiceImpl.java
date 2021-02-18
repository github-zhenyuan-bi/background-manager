package pro.bzy.boot.script.service.impl;

import pro.bzy.boot.script.domain.entity.RechargeCardTags;
import pro.bzy.boot.script.mapper.RechargeCardTagsMapper;
import pro.bzy.boot.script.service.RechargeCardTagsService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *  服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2021-02-17
 */
@Service
public class RechargeCardTagsServiceImpl extends ServiceImpl<RechargeCardTagsMapper, RechargeCardTags> implements RechargeCardTagsService {

    @Resource
    private RechargeCardTagsMapper rechargeCardTagsMapper;

}