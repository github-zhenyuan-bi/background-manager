package pro.bzy.boot.script.service.impl;

import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.framework.utils.FileUtil;
import pro.bzy.boot.framework.utils.PathUtil;
import pro.bzy.boot.script.domain.entity.Juben;
import pro.bzy.boot.script.mapper.JubenMapper;
import pro.bzy.boot.script.service.JubenService;
import pro.bzy.boot.script.utils.ScriptConstant;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * 剧本 服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-08
 */
@Service
public class JubenServiceImpl extends ServiceImpl<JubenMapper, Juben> implements JubenService {

    @Resource
    private JubenMapper jubenMapper;

    
    
    @Override
    public void addJuben(Juben juben) {
        // 1. 剧本数据进行验证
        checkJuben(juben);
        
        // 2. 入库
        save(juben);
    }

    
    
    /**
     * 校验剧本数据
     * @param juben
     */
    private void checkJuben(Juben juben) {
        if (StringUtils.isEmpty(juben.getName()))
            throw new IllegalArgumentException("剧本名称不能为空");
        
        // 名称唯一性检验
        int count = this.count(Wrappers.<Juben>lambdaQuery()
                .eq(Juben::getName, juben.getName())
                .ne(!StringUtils.isEmpty(juben.getId()), Juben::getId, juben.getId()));
        if (count > 0)
            throw new IllegalArgumentException("剧本名称已被使用：" + juben.getName());
    }



    @Override
    public int clearUploadCoverImg() {
        try {
            // 1. 查询全部剧本 获取剧本的封面图片地址
            List<Juben> jubens = list(Wrappers.<Juben>lambdaQuery());
            List<String> jubenCoverImgNames = (!CollectionUtil.isEmpty(jubens))
                    ? jubens.stream().map(jb -> new File(jb.getCoverImg()).getName()).collect(Collectors.toList())
                    : Lists.newArrayList();
                    
            // 2. 获取剧本封面地址的全部已上传图片
            File[] coverImgFiles = FileUtil.getFilesNameUnderDict(
                    PathUtil.getWebappResourcePath(ScriptConstant.JUBEN_UPLOAD_COVER_IMG_PATH));
            
            // 3. 移除未被使用的上传封面图
            return FileUtil.removeNonUsedFiles(coverImgFiles, jubenCoverImgNames);
        } catch (Exception e) {
            throw new RuntimeException("清除剧本封面照片失败，原因：" + e.getMessage());
        }
    }


}