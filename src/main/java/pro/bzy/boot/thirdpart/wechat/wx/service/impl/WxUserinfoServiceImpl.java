package pro.bzy.boot.thirdpart.wechat.wx.service.impl;

import pro.bzy.boot.framework.utils.ExceptionCheckUtil;
import pro.bzy.boot.thirdpart.wechat.miniprogram.service.WxMiniprogramLogService;
import pro.bzy.boot.thirdpart.wechat.wx.domain.entity.WxUserinfo;
import pro.bzy.boot.thirdpart.wechat.wx.mapper.WxUserinfoMapper;
import pro.bzy.boot.thirdpart.wechat.wx.service.WxUserinfoService;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 *  服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2020-11-02
 */
@Service
public class WxUserinfoServiceImpl extends ServiceImpl<WxUserinfoMapper, WxUserinfo> implements WxUserinfoService {

    @Resource
    private WxUserinfoMapper wxUserinfoMapper;
    @Resource
    private WxMiniprogramLogService wxMiniprogramLogService;
    
    
    @Override
    @Transactional(rollbackFor=Exception.class)
    public void registOnNonexist(final String openid, final String session_key) {
        try {
            // 0. 异常校验
            ExceptionCheckUtil.hasText(openid, "注册微信用的openid不能为空");
            String logType = "登陆";
            
            // 1. 检测该用户是否已经存在
            int count = count(Wrappers.<WxUserinfo>lambdaQuery().eq(WxUserinfo::getOpenid, openid));
            
            if (count == 0) {
                // 1.1 若不存在 则注册入库
                save(WxUserinfo.builder().openid(openid).build());
                logType = "注册";
            } 
            
            // 2. 记录用户访问日志
            wxMiniprogramLogService.recordUserAccess(openid, session_key, logType);
        } catch (Exception e) {
            throw new RuntimeException("微信用户系统注册失败，原因：" + e.getMessage());
        }
    }

}