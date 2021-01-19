package pro.bzy.boot.script.service;

import pro.bzy.boot.script.domain.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.google.common.collect.ImmutableMap;

/**
 * 剧本类型标签 服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-08
 */
public interface TagService extends IService<Tag> {

    /** 查询全部标签列表 并依据自身id建立索引结果 */
    ImmutableMap<String, Tag> getTagsThenMapBySelfId();
}
