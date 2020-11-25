package pro.bzy.boot.thirdpart.wechat.wx.service;

import pro.bzy.boot.thirdpart.wechat.wx.domain.entity.WxUserinfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *  服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2020-11-02
 */
public interface WxUserinfoService extends IService<WxUserinfo> {

    /** 微信注册该系统 */
    void registOnNonexist(String openid, String session_key);
}
