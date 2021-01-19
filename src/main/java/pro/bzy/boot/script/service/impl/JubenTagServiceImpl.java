package pro.bzy.boot.script.service.impl;

import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.script.domain.entity.JubenTag;
import pro.bzy.boot.script.domain.entity.Tag;
import pro.bzy.boot.script.mapper.JubenTagMapper;
import pro.bzy.boot.script.service.JubenTagService;
import pro.bzy.boot.script.service.TagService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * 剧本-标签关系表 服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-08
 */
@Service
public class JubenTagServiceImpl extends ServiceImpl<JubenTagMapper, JubenTag> implements JubenTagService {

    @Resource
    private JubenTagMapper jubenTagMapper;
    @Resource
    private TagService tagService;
    
    
    
    @Override
    @Transactional(readOnly=true)
    public List<Tag> getTagsByJuben(String jubenId) {
        // 剧本与标签的关联关系
        List<JubenTag> jubenTags = list(Wrappers.<JubenTag>lambdaQuery().eq(JubenTag::getJubenId, jubenId));
        
        // 标签
        List<Tag> tags = Lists.newArrayList();
        if (!CollectionUtil.isEmpty(jubenTags)) {
            tags = jubenTags.stream()
                    .map(jt -> tagService.getById(jt.getTagId()))
                    .collect(Collectors.toList());
        }
        return tags;
    }


    
    @Override
    @Transactional(rollbackFor=Exception.class)
    public void rebindTagRelationship(String jubenId, List<JubenTag> jubenTags) {
        // 1. 删除旧的绑定关系
        remove(Wrappers.<JubenTag>lambdaUpdate().eq(JubenTag::getJubenId, jubenId));
        
        // 2. 重新绑定关联关系
        if (!CollectionUtil.isEmpty(jubenTags))
            saveBatch(jubenTags);
    }



    @Override
    @Transactional(readOnly=true)
    public Map<String, List<JubenTag>> getJubenTagsGroupByJubenId(List<String> jubenIds) {
        List<JubenTag> jubenTags = list(
                Wrappers.<JubenTag>lambdaQuery().in(JubenTag::getJubenId, jubenIds));
        Map<String, List<JubenTag>> groupedJubenTags = CollectionUtil.groupBy(jubenTags, jt -> jt.getJubenId());
        return groupedJubenTags;
    }

}