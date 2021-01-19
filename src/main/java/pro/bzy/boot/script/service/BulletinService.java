package pro.bzy.boot.script.service;

import pro.bzy.boot.script.domain.entity.Bulletin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *  服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2020-12-14
 */
public interface BulletinService extends IService<Bulletin> {

    /** 推送通知公告 */
    void sendBulletin(Bulletin updateBean);
    
    
    /** 通知公告置顶操作 */
    boolean fixedTopOperation(String bulletinId, boolean isFixedTop);
}
