package pro.bzy.boot.thirdpart.wechat.miniprogram.service.impl;

import pro.bzy.boot.thirdpart.wechat.miniprogram.domain.entity.WxMiniprogramSettingIndeximg;
import pro.bzy.boot.thirdpart.wechat.miniprogram.mapper.WxMiniprogramSettingIndeximgMapper;
import pro.bzy.boot.thirdpart.wechat.miniprogram.service.WxMiniprogramSettingIndeximgService;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;

import javax.annotation.Resource;

/**
 *  服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2021-01-19
 */
@Service
public class WxMiniprogramSettingIndeximgServiceImpl extends ServiceImpl<WxMiniprogramSettingIndeximgMapper, WxMiniprogramSettingIndeximg> implements WxMiniprogramSettingIndeximgService {

    @Resource
    private WxMiniprogramSettingIndeximgMapper wxMiniprogramSettingIndeximgMapper;

    
    
    @Override
    public String addIndexSwipperImg(String imgUrl) {
        // 1. 查询当前排序最后的一个图片
        WxMiniprogramSettingIndeximg swipper = getOne(
                Wrappers.<WxMiniprogramSettingIndeximg>lambdaQuery()
                    .orderByDesc(WxMiniprogramSettingIndeximg::getSort));
        
        // 2. 计算当前图片排序 末尾增加
        int sort = 1;
        if (Objects.nonNull(swipper)) {
            Integer index = swipper.getSort();
            if (Objects.nonNull(index))
                sort = index + 1;
        }
        
        // 3. 保存
        this.save(WxMiniprogramSettingIndeximg.builder()
                .siwperImgUrl(imgUrl)
                .sort(sort)
                .build());
        
        return imgUrl;
    }

}