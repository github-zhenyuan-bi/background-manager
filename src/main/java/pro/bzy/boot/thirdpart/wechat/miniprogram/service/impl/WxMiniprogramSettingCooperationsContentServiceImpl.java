package pro.bzy.boot.thirdpart.wechat.miniprogram.service.impl;

import pro.bzy.boot.framework.config.exceptions.FormValidatedException;
import pro.bzy.boot.thirdpart.wechat.miniprogram.domain.entity.WxMiniprogramSettingCooperationsContent;
import pro.bzy.boot.thirdpart.wechat.miniprogram.mapper.WxMiniprogramSettingCooperationsContentMapper;
import pro.bzy.boot.thirdpart.wechat.miniprogram.service.WxMiniprogramSettingCooperationsContentService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import javax.annotation.Resource;

/**
 *  服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2021-01-19
 */
@Service
public class WxMiniprogramSettingCooperationsContentServiceImpl extends ServiceImpl<WxMiniprogramSettingCooperationsContentMapper, WxMiniprogramSettingCooperationsContent> implements WxMiniprogramSettingCooperationsContentService {

    @Resource
    private WxMiniprogramSettingCooperationsContentMapper wxMiniprogramSettingCooperationsContentMapper;

    
    
    @Override
    public List<WxMiniprogramSettingCooperationsContent> handleCoopContents(String ctKeys, String ctValues, String isSubtitles) {
        // 0. 空值校验
        if (StringUtils.isEmpty(ctKeys))
            throw new FormValidatedException("提交数据有误：合作模块内容键不能为空");
        if (StringUtils.isEmpty(ctValues))
            throw new FormValidatedException("提交数据有误：合作模块内容值不能为空");
        
        // 1. 数据处理
        String[] keysArr = ctKeys.split(",");
        String[] valsArr = ctValues.split(",");
        String[] isSubtitleArr = isSubtitles.split(",");
        if (keysArr.length != valsArr.length)
            throw new FormValidatedException("提交数据有误：合作模块内容键值数量不一致");
        
        // 2. 数据打包
        String key, value, isSubtitle;
        WxMiniprogramSettingCooperationsContent coopContent;
        List<WxMiniprogramSettingCooperationsContent> contents = Lists.newArrayListWithCapacity(keysArr.length);
        for (int i = 0; i < keysArr.length; i++) {
            key = keysArr[i];
            value = valsArr[i];
            isSubtitle = isSubtitleArr[i];
            coopContent = WxMiniprogramSettingCooperationsContent.builder()
                    .ctKey(key)
                    .ctValue(value)
                    .isSubtitle("true".equals(isSubtitle))
                    .sort(i)
                    .build();
            contents.add(coopContent);
        }
        return contents;
    }

}