package pro.bzy.boot.thirdpart.wechat.miniprogram.service;

import pro.bzy.boot.thirdpart.wechat.miniprogram.domain.entity.WxMiniprogramLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *  服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2020-11-02
 */
public interface WxMiniprogramLogService extends IService<WxMiniprogramLog> {

    /** 记录小程序用户访问 */
    void saveUserAccess(String openid, String session_key, String logType);
}
