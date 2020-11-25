package pro.bzy.boot.framework.web.service;

import pro.bzy.boot.framework.web.domain.entity.Role;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 角色 服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
public interface RoleService extends IService<Role> {

    /** 查询公共角色 */
    Role getPublicRole();
    
    /** 获取角色列表和某个用户是否拥有这些角色得标记 */
    List<Role> getRolesWithUserHasFlag(String userId);
}
