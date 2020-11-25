package pro.bzy.boot.script.service;

import pro.bzy.boot.script.domain.entity.JubenCharacter;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 剧本的角色 服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-14
 */
public interface JubenCharacterService extends IService<JubenCharacter> {

    /** 新增或更新剧本人物 */
    int saveOrUpdateRecords(String jubenId, List<JubenCharacter> jubenCharacters);
    
    
    /** 清理上传剧本人物的图片 */
    int clearUploadCharacterImg();
}
