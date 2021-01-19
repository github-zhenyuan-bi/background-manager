package pro.bzy.boot.script.service.impl;

import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.framework.utils.FileUtil;
import pro.bzy.boot.framework.utils.PathUtil;
import pro.bzy.boot.script.domain.entity.Juben;
import pro.bzy.boot.script.domain.entity.JubenTag;
import pro.bzy.boot.script.domain.entity.Tag;
import pro.bzy.boot.script.mapper.JubenMapper;
import pro.bzy.boot.script.service.JubenService;
import pro.bzy.boot.script.service.JubenTagService;
import pro.bzy.boot.script.service.TagService;
import pro.bzy.boot.script.utils.ScriptConstant;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

    @Resource
    private TagService tagService;
    @Resource
    private JubenTagService jubenTagService;
    
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



    @Override
    public Page<Juben> getJuebnPageDataList(int pageNo, int pageSize, HttpServletRequest request) {
        // 1. 查询分页列表剧本
        Page<Juben> page = new Page<>(pageNo, pageSize);
        page(page, Wrappers.<Juben>lambdaQuery());
        List<Juben> jubens = page.getRecords();
        
        if (CollectionUtil.isNotEmpty(jubens)) {
            // 2. 查询全部的剧本标签 --> 并依据自身id构建索引map对象
            ImmutableMap<String, Tag> groupedTags = tagService.getTagsThenMapBySelfId();
            
            // 3. 查询当前jubens与标签的全部关联关系 打包成以jubenid分组的数据结果
            List<String> jubenIds = CollectionUtil.map(jubens, jb -> jb.getId());
            Map<String, List<JubenTag>> groupedJubenTags = jubenTagService.getJubenTagsGroupByJubenId(jubenIds);
            
            // 4. 组装结果数据-->将关联标签塞入对应得剧本对象属性中
            jubens.forEach(juben -> {
                List<JubenTag> thisJubenRelaToTags = groupedJubenTags.get(juben.getId());
                List<Tag> thisJubenContainTags = Lists.newArrayList();
                for (JubenTag jt : thisJubenRelaToTags) 
                    thisJubenContainTags.add(groupedTags.get(jt.getTagId()));
                juben.setTags(thisJubenContainTags);
            });
        }
        return page;
    }


}