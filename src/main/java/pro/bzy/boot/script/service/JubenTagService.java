package pro.bzy.boot.script.service;

import pro.bzy.boot.script.domain.entity.JubenTag;
import pro.bzy.boot.script.domain.entity.Tag;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 剧本-标签关系表 服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-08
 */
public interface JubenTagService extends IService<JubenTag> {

    /** 使用剧本ID查询关联的标签 */
    List<Tag> getTagsByJuben(String jubenId);
    
    
    /** 查询当前jubens与标签的全部关联关系 打包成以jubenid分组的数据结果 */
    Map<String, List<JubenTag>> getJubenTagsGroupByJubenId(List<String> jubenIds);
    
    
    /** 重新绑定剧本个标签的关系 */
    void rebindTagRelationship(String jubenId, List<JubenTag> jubenTags);
}
