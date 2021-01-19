package pro.bzy.boot.script.service.impl;

import pro.bzy.boot.script.domain.entity.Tag;
import pro.bzy.boot.script.mapper.TagMapper;
import pro.bzy.boot.script.service.TagService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

/**
 * 剧本类型标签 服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-08
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Resource
    private TagMapper tagMapper;

    
    @Override
    public ImmutableMap<String, Tag> getTagsThenMapBySelfId() {
        List<Tag> tags = list();
        ImmutableMap<String, Tag> groupedTags = Maps.uniqueIndex(tags, tag -> tag.getId());
        return groupedTags;
    }

}