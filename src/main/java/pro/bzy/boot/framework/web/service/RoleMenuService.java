package pro.bzy.boot.framework.web.service;

import pro.bzy.boot.framework.web.domain.entity.Menu;
import pro.bzy.boot.framework.web.domain.entity.Role;
import pro.bzy.boot.framework.web.domain.entity.RoleMenu;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 菜单角色关联关系 服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
public interface RoleMenuService extends IService<RoleMenu> {

    Map<String, String> getMenuNamesWithRoleNames(List<Menu> menus);
    
    
    /** 绑定新的角色和菜单资源关系 */
    void updateRoleWithMenuAndPermRelationShip(String roleId, String[] menuIds, String[] permIds);
    
    
    /** 查询角色的全部权限菜单 */
    List<RoleMenu> getAllMenuOfRole(List<Role> roles); 
    
    
    /** 查询用户拥有访问权限的全部资源菜单 */
    List<String> getUserPermitToAccessMenu(String accessor);
}
