package pro.bzy.boot.framework.web.service.impl;

import pro.bzy.boot.framework.web.domain.entity.Menu;
import pro.bzy.boot.framework.web.domain.entity.Role;
import pro.bzy.boot.framework.web.domain.entity.RoleMenu;
import pro.bzy.boot.framework.web.mapper.RoleMenuMapper;
import pro.bzy.boot.framework.web.service.RoleMenuService;
import pro.bzy.boot.framework.web.service.RoleService;
import pro.bzy.boot.framework.web.service.UserRoleService;
import pro.bzy.boot.framework.utils.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

/**
 * 菜单角色关联关系 服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Resource
    private RoleService roleService;
    @Resource
    private UserRoleService userRoleService;
    
    @Override
    public Map<String, String> getMenuNamesWithRoleNames(List<Menu> menus) {
        Map<String, String> names = Maps.newLinkedHashMap();
        if (!CollectionUtil.isEmpty(menus)) {
            menus.forEach(menu -> {
                // 菜单角色关联关系
                List<RoleMenu> roleMenus = list(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getMenuId, menu.getId()));
                
                // 如果存在关联关系就查一下   关联的全部角色名称
                Set<String> roleNameSet = Sets.newHashSet();
                if (!CollectionUtil.isEmpty(roleMenus)) {
                    roleMenus.forEach(rm -> {
                        Role role = roleService.getById(rm.getRoleId());
                        if (role != null)
                            roleNameSet.add(role.getName());
                    });
                }
                String roleNames = String.join(",", roleNameSet);
                names.put(menu.getFilterExp(), "roles[" + roleNames + "]");
            });
        }
        return names;
    }

    
    
    @Override
    public void updateRoleWithMenuRelationShip(String roleId, String[] menuIds) {
        // 删除旧的绑定关系
        remove(Wrappers.<RoleMenu>lambdaUpdate().eq(RoleMenu::getRoleId, roleId));
        
        // 存储新的关系
        if (menuIds != null && menuIds.length > 0) {
            List<RoleMenu> roleMenus = Stream.<String>of(menuIds)
                    .map(menuId -> new RoleMenu(roleId, menuId)).collect(Collectors.toList());
            saveBatch(roleMenus);
        }
    }



    @Override
    public List<String> getUserPermitToAccessMenu(String accessor) {
        List<Role> roles = userRoleService.getRolesByUserId(accessor);
        List<RoleMenu> roleMenus = getAllMenuOfRole(roles);
        return CollectionUtil.map(roleMenus, rm -> rm.getMenuId());
    }



    @Override
    public List<RoleMenu> getAllMenuOfRole(List<Role> roles) {
        List<RoleMenu> roleMenus = null;
        if (CollectionUtil.isNotEmpty(roles)) {
            roleMenus = list(Wrappers.<RoleMenu>lambdaQuery().in(
                                RoleMenu::getRoleId, 
                                CollectionUtil.map(roles, role -> role.getId())));
        } else {
            roleMenus = Lists.newArrayListWithCapacity(0);
        }
        return roleMenus;
    }

}