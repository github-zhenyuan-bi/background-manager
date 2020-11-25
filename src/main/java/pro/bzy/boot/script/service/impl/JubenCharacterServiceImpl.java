package pro.bzy.boot.script.service.impl;

import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.framework.utils.FileUtil;
import pro.bzy.boot.framework.utils.PathUtil;
import pro.bzy.boot.script.domain.entity.JubenCharacter;
import pro.bzy.boot.script.mapper.JubenCharacterMapper;
import pro.bzy.boot.script.service.JubenCharacterService;
import pro.bzy.boot.script.utils.ScriptConstant;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * 剧本的角色 服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-14
 */
@Service
public class JubenCharacterServiceImpl extends ServiceImpl<JubenCharacterMapper, JubenCharacter> implements JubenCharacterService {

    @Resource
    private JubenCharacterMapper jubenCharacterMapper;

    
    
    @Override
    @Transactional(rollbackFor=Exception.class)
    public int saveOrUpdateRecords(String jubenId, List<JubenCharacter> jubenCharacters) {
        // 1. 删除旧的人物绑定
        remove(Wrappers.<JubenCharacter>lambdaUpdate().eq(JubenCharacter::getJubenId, jubenId));
        
        // 2. 重新绑定人物关系
        if (!CollectionUtil.isEmpty(jubenCharacters)) {
            saveBatch(jubenCharacters);
            return jubenCharacters.size();
        }
            
        return 0;
    }



    @Override
    public int clearUploadCharacterImg() {
        try {
            // 1. 查询全部的在库剧本人物图片
            List<JubenCharacter> characs = list();
            List<String> jubenCharacterImgNames = (!CollectionUtil.isEmpty(characs))
                    ? characs.stream().map(charac -> new File(charac.getImg()).getName()).collect(Collectors.toList())
                    : Lists.newArrayList();
                    
             // 2. 获取剧本人物地址的全部已上传图片
            File[] CharacImgFiles = FileUtil.getFilesNameUnderDict(
                    PathUtil.getWebappResourcePath(ScriptConstant.JUBEN_UPLOAD_CHARACTER_IMG_PATH));     
            
            // 3. 删除未被使用的人物图片文件
            return FileUtil.removeNonUsedFiles(CharacImgFiles, jubenCharacterImgNames);
        } catch (Exception e) {
            throw new RuntimeException("清除剧本封面照片失败，原因：" + e.getMessage());
        }
    }

}