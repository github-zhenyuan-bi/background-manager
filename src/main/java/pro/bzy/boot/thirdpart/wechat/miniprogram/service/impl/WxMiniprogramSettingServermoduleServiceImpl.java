package pro.bzy.boot.thirdpart.wechat.miniprogram.service.impl;

import pro.bzy.boot.thirdpart.wechat.miniprogram.domain.entity.WxMiniprogramSettingServermodule;
import pro.bzy.boot.thirdpart.wechat.miniprogram.mapper.WxMiniprogramSettingServermoduleMapper;
import pro.bzy.boot.thirdpart.wechat.miniprogram.service.WxMiniprogramSettingServermoduleService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *  服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2021-01-19
 */
@Service
public class WxMiniprogramSettingServermoduleServiceImpl extends ServiceImpl<WxMiniprogramSettingServermoduleMapper, WxMiniprogramSettingServermodule> implements WxMiniprogramSettingServermoduleService {

    @Resource
    private WxMiniprogramSettingServermoduleMapper wxMiniprogramSettingServermoduleMapper;

}