package pro.bzy.boot.framework.web.service.impl;

import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.framework.web.domain.entity.Menu;
import pro.bzy.boot.framework.web.domain.entity.Permission;
import pro.bzy.boot.framework.web.domain.entity.Role;
import pro.bzy.boot.framework.web.domain.entity.RolePermission;
import pro.bzy.boot.framework.web.mapper.MenuMapper;
import pro.bzy.boot.framework.web.service.MenuService;
import pro.bzy.boot.framework.web.service.PermissionService;
import pro.bzy.boot.framework.web.service.RoleMenuService;
import pro.bzy.boot.framework.web.service.RolePermissionService;
import pro.bzy.boot.framework.web.service.UserRoleService;
import lombok.NonNull;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * 菜单资源 服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Resource
    private MenuMapper menuMapper;
    @Resource
    private RoleMenuService roleMenuService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Resource
    private UserRoleService userRoleService;

    @Override
    public List<Menu> buildTreeMenus() {
        // 查询全部菜单列表
        List<Menu> allMenus = menuMapper.selectList(Wrappers.emptyWrapper());
        
        Menu root = Menu.getDefualtRootMenu();
        CollectionUtil.buildTree(root, allMenus);
        
        return root.getChilds();
    }

    
    
    @Override
    public int removeMenuThenHandleChilds(@NonNull String id) {
        int count = 0;
        List<Menu> menus = list(Wrappers.<Menu>lambdaQuery()
                .select(Menu::getId)
                .eq(Menu::getId, id).or().eq(Menu::getPid, id));
        List<String> menuIds = Lists.transform(menus, menu -> menu.getId());
        // 删除菜单
        remove(Wrappers.<Menu>lambdaQuery().in(Menu::getId, menuIds));
        // 删除权限
        //permissionService.remove(Wrappers.<Permission>lambdaQuery().in(Permission::getMenuId, menuIds));
        return count;
    }



    @Override
    public List<Menu> getByTypeThenOrder(String type) {
        List<Menu> menuList = list(Wrappers.<Menu>lambdaQuery()
                .eq(Menu::getMenuType, type)
                .orderByAsc(Menu::getSort));
        return menuList;
    }



    @Override
    public List<Menu> getByAccessorAndTypeThenOrder(String accessor, String type) {
        List<Role> roles = userRoleService.getRolesByUserId(accessor);
        boolean isAdmin = roles.stream().anyMatch(role -> role.getName().toLowerCase().contains("admin"));
        if (isAdmin) {
            List<Menu> menuList = list(Wrappers.<Menu>lambdaQuery()
                    .eq(Menu::getMenuType, type)
                    .orderByAsc(Menu::getSort));
            return menuList;
        } else {
            List<String> menuIds = roleMenuService.getUserPermitToAccessMenu(accessor);
            if (CollectionUtil.isEmpty(menuIds)) {
                return Lists.newArrayListWithCapacity(0);
            } else {
                List<Menu> menuList = list(Wrappers.<Menu>lambdaQuery()
                    .in(CollectionUtil.isNotEmpty(menuIds), Menu::getId, menuIds)
                    .eq(Menu::getMenuType, type)
                    .orderByAsc(Menu::getSort));
                return menuList;
            }
        }
    }



    @Override
    public List<Menu> getMenuList(String roleId, Menu queryBean) {
        // 1. 查询全部菜单
        List<Menu> menus = list(Wrappers.<Menu>lambdaQuery(queryBean).orderByAsc(Menu::getSort));
        
        // 2. 查询菜单对应的权限信息
        if (CollectionUtil.isNotEmpty(menus)) {
            List<Permission> allPerms = permissionService.list(Wrappers.<Permission>lambdaQuery()
                    .in(Permission::getMenuId, Lists.transform(menus, menu -> menu.getId())));
            
            if (CollectionUtil.isNotEmpty(allPerms)) {
                // 3. 查询角色拥有的权限信息
                if (!StringUtils.isEmpty(roleId)) {
                    List<RolePermission> rolePerms = rolePermissionService.list(Wrappers.<RolePermission>lambdaQuery().eq(RolePermission::getRoleId, roleId));
                    if (CollectionUtil.isNotEmpty(rolePerms)) {
                        List<String> rolePermIds = Lists.transform(rolePerms, rp -> rp.getPermissionId());
                        allPerms.stream().filter(perm -> {
                            perm.setRoleHas(false);
                            for (String rpId : rolePermIds) 
                                if (rpId.equals(perm.getId())) 
                                    return true;
                            return false;
                        }).forEach(perm -> perm.setRoleHas(true));
                    }
                }
                
                // 权限进行 对应得菜单资源分组 然后塞入菜单资源对象中
                Map<String, List<Permission>> menuOfPerms = CollectionUtil.groupBy(allPerms, perm -> perm.getMenuId());
                for (Menu menu : menus) 
                    menu.setPerms(menuOfPerms.get(menu.getId()));
            }
        }
        return menus;
    }

}