package pro.bzy.boot.script.service.impl;

import pro.bzy.boot.framework.config.constant.JWT_constant;
import pro.bzy.boot.framework.utils.DateUtil;
import pro.bzy.boot.framework.utils.RequestAndResponseUtil;
import pro.bzy.boot.script.domain.entity.Bulletin;
import pro.bzy.boot.script.domain.entity.BulletinTemplate;
import pro.bzy.boot.script.mapper.BulletinMapper;
import pro.bzy.boot.script.service.BulletinService;
import pro.bzy.boot.script.service.BulletinTemplateService;
import pro.bzy.boot.script.utils.ScriptConstant;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

import javax.annotation.Resource;

/**
 *  服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2020-12-14
 */
@Service
public class BulletinServiceImpl extends ServiceImpl<BulletinMapper, Bulletin> implements BulletinService {

    @Resource
    private BulletinMapper bulletinMapper;
    @Resource
    private BulletinTemplateService bulletinTemplateService;
    
    
    @Override
    public void sendBulletin(Bulletin updateBean) {
        // 1. 查询到使用的模板
        BulletinTemplate template = bulletinTemplateService.getById(updateBean.getTemplateId());
        if (template == null)
            throw new RuntimeException("使用的模板不存在，ID:" + updateBean.getTemplateId());
        
        // 2. 提取模板中的内容
        if (StringUtils.isEmpty(updateBean.getSender()))
            throw new RuntimeException("必须选定推送方式 ---即时、定时、周期");
        Map<String, Object> datas = RequestAndResponseUtil.getJwttokenStorageDatasFromRequest();
        Bulletin bean = new Bulletin(template);
        bean.setContent(updateBean.getContent());
        bean.setSendWay(ScriptConstant.BULLETIN_SENDWAY_SHOUDONG);
        bean.setSender(datas.get(JWT_constant.JWT_LOGIN_USERID_KEY).toString());
        bean.setSendTime(DateUtil.getNow());
        
        // 3. 推送通知
        save(bean);
    }


    
    @Override
    public boolean updateBulletinForfixedTop(String bulletinId, boolean isFixedTop) {
        update(Wrappers.<Bulletin>lambdaUpdate()
                .eq(Bulletin::getId, bulletinId)
                .set(Bulletin::getIsFixedTop, isFixedTop));
        return isFixedTop;
    }

}