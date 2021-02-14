package pro.bzy.boot.thirdpart.wechat.miniprogram.service;

import pro.bzy.boot.thirdpart.wechat.miniprogram.domain.entity.WxMiniprogramSettingCooperation;


import com.baomidou.mybatisplus.extension.service.IService;

/**
 *  服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2021-01-19
 */
public interface WxMiniprogramSettingCooperationService extends IService<WxMiniprogramSettingCooperation> {

    /** 增加一个合作模块 与其 内容 */
    void addCoop(WxMiniprogramSettingCooperation coop, String ctKeys, String ctValues, String isSubtitles);
    
    
    /** 更新一个合作模块 与其 内容 */
    void updateCoop(WxMiniprogramSettingCooperation coop, String ctKeys, String ctValues, String isSubtitles);
}
