package pro.bzy.boot.framework.web.service;

import pro.bzy.boot.framework.web.domain.entity.Permission;
import pro.bzy.boot.framework.web.domain.entity.Role;
import pro.bzy.boot.framework.web.domain.entity.RolePermission;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 角色权限关联关系 服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
public interface RolePermissionService extends IService<RolePermission> {

    List<Permission> getPermsByRole(List<Role> roles);
}
