package pro.bzy.boot.thirdpart.wechat.miniprogram.service.impl;

import pro.bzy.boot.framework.config.exceptions.FormValidatedException;
import pro.bzy.boot.thirdpart.wechat.miniprogram.domain.entity.WxMiniprogramSettingCooperation;
import pro.bzy.boot.thirdpart.wechat.miniprogram.domain.entity.WxMiniprogramSettingCooperationsContent;
import pro.bzy.boot.thirdpart.wechat.miniprogram.mapper.WxMiniprogramSettingCooperationMapper;
import pro.bzy.boot.thirdpart.wechat.miniprogram.service.WxMiniprogramSettingCooperationService;
import pro.bzy.boot.thirdpart.wechat.miniprogram.service.WxMiniprogramSettingCooperationsContentService;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

/**
 *  服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2021-01-19
 */
@Service
public class WxMiniprogramSettingCooperationServiceImpl extends ServiceImpl<WxMiniprogramSettingCooperationMapper, WxMiniprogramSettingCooperation> implements WxMiniprogramSettingCooperationService {

    @Resource
    private WxMiniprogramSettingCooperationMapper wxMiniprogramSettingCooperationMapper;
    @Resource
    private WxMiniprogramSettingCooperationsContentService coopContentService;
    
    
    @Override
    @Transactional(rollbackFor=Exception.class)
    public void addCoop(WxMiniprogramSettingCooperation coop, String ctKeys, String ctValues, String isSubtitles) {
        if (StringUtils.isEmpty(coop.getId()))
            coop.setId(null);
        
        // 1. 表单校验
        dataCheck(coop);
        coop.setEnabled(true);
        
        // 2. 内容数据处理
        List<WxMiniprogramSettingCooperationsContent> contents = coopContentService.handleCoopContents(ctKeys, ctValues, isSubtitles);
        
        // 3. 合作模块入库
        save(coop);
        
        // 4. 内容数据在处理后 入库
        contents.forEach(ct -> ct.setCoopId(coop.getId()));
        coopContentService.saveBatch(contents);
    }
    
    
    
    /**
     * 数据校验
     * @param coop
     */
    private void dataCheck(WxMiniprogramSettingCooperation coop) {
        if (StringUtils.isEmpty(coop.getCoopName()))
            throw new FormValidatedException("提交数据有误：合作模块名称不能为空");
        
        if (StringUtils.isEmpty(coop.getIcon()))
            throw new FormValidatedException("提交数据有误：合作模块图标不能为空");
    }



    @Override
    public void updateCoop(WxMiniprogramSettingCooperation coop, String ctKeys, String ctValues, String isSubtitles) {
        if (StringUtils.isEmpty(coop.getId()))
            throw new FormValidatedException("提交数据有误：更新合作模块ID不能为空");
        
        // 1. 表单校验
        dataCheck(coop);
        
        // 2. 查询原始数据
        WxMiniprogramSettingCooperation oldCoop = getById(coop.getId());
        if (Objects.isNull(oldCoop))
            throw new FormValidatedException("提交数据有误：id查询的原始数据为空");
        
        // 3. 替换原始数据
        oldCoop.setCoopName(coop.getCoopName());
        oldCoop.setIcon(coop.getIcon());
        
        // 4. 重构内容数据 + 删除历史内容 
        List<WxMiniprogramSettingCooperationsContent> contents = coopContentService.handleCoopContents(ctKeys, ctValues, isSubtitles);
        coopContentService.remove(Wrappers.<WxMiniprogramSettingCooperationsContent>lambdaUpdate()
                .eq(WxMiniprogramSettingCooperationsContent::getCoopId, coop.getId()));
        
        // 5. 更新
        updateById(oldCoop);
        contents.forEach(ct -> ct.setCoopId(coop.getId()));
        coopContentService.saveBatch(contents);
    }

}