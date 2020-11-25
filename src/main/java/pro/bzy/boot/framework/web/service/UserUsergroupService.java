package pro.bzy.boot.framework.web.service;

import pro.bzy.boot.framework.web.domain.entity.UserUsergroup;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户与用户组之间的关联 服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-06
 */
public interface UserUsergroupService extends IService<UserUsergroup> {

    /** 更新用户与用户组的关系 */
    void updateUserWithUsergroupRelationship(String userId, String[] groupIds);
}
