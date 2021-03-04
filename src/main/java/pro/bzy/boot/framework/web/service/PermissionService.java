package pro.bzy.boot.framework.web.service;

import pro.bzy.boot.framework.web.domain.bean.PermissionSettForm;
import pro.bzy.boot.framework.web.domain.entity.Permission;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 权限 服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
public interface PermissionService extends IService<Permission> {

    /** 为菜单资源更新权限配置信息 */
    void updatePermsForMenu(String menuId, List<PermissionSettForm> permSetts);
}
