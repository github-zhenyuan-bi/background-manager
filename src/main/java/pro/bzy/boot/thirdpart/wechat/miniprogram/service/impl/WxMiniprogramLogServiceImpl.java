package pro.bzy.boot.thirdpart.wechat.miniprogram.service.impl;

import pro.bzy.boot.framework.utils.DateUtil;
import pro.bzy.boot.thirdpart.wechat.miniprogram.domain.entity.WxMiniprogramLog;
import pro.bzy.boot.thirdpart.wechat.miniprogram.mapper.WxMiniprogramLogMapper;
import pro.bzy.boot.thirdpart.wechat.miniprogram.service.WxMiniprogramLogService;

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
public class WxMiniprogramLogServiceImpl extends ServiceImpl<WxMiniprogramLogMapper, WxMiniprogramLog> implements WxMiniprogramLogService {

    @Resource
    private WxMiniprogramLogMapper wxMiniprogramLogMapper;

    
    @Override
    @Transactional(rollbackFor=Exception.class)
    public void recordUserAccess(String openid, String session_key, String logType) {
        save(WxMiniprogramLog.builder()
                .openid(openid)
                .sessionKey(session_key)
                .logTime(DateUtil.getNow())
                .logType(logType).build());
    }

}