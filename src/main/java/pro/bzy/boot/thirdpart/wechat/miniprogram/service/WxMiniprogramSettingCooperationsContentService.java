package pro.bzy.boot.thirdpart.wechat.miniprogram.service;

import pro.bzy.boot.thirdpart.wechat.miniprogram.domain.entity.WxMiniprogramSettingCooperationsContent;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 *  服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2021-01-19
 */
public interface WxMiniprogramSettingCooperationsContentService extends IService<WxMiniprogramSettingCooperationsContent> {

    
    /** 处理合作内容数据 返回内容列表 */
    List<WxMiniprogramSettingCooperationsContent> handleCoopContents(String ctKeys, String ctValues, String isSubtitles);
    
    
}
