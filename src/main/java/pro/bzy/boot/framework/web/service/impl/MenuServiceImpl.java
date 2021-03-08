package pro.bzy.boot.framework.web.service.impl;

import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.framework.web.domain.entity.Menu;
import pro.bzy.boot.framework.web.domain.entity.Permission;
import pro.bzy.boot.framework.web.mapper.MenuMapper;
import pro.bzy.boot.framework.web.service.MenuService;
import pro.bzy.boot.framework.web.service.PermissionService;
import pro.bzy.boot.framework.web.service.RoleMenuService;
import lombok.NonNull;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        List<String> menuIds = roleMenuService.getUserPermitToAccessMenu(accessor);
        List<Menu> menuList = list(Wrappers.<Menu>lambdaQuery()
                .in(CollectionUtil.isNotEmpty(menuIds), Menu::getId, menuIds)
                .eq(Menu::getMenuType, type)
                .orderByAsc(Menu::getSort));
        return menuList;
    }



    @Override
    public List<Menu> getMenuList(Menu queryBean) {
        List<Menu> menus = list(Wrappers.<Menu>lambdaQuery(queryBean).orderByAsc(Menu::getSort));
        if (CollectionUtil.isNotEmpty(menus)) {
            List<Permission> allPerms = permissionService.list(Wrappers.<Permission>lambdaQuery()
                    .in(Permission::getMenuId, Lists.transform(menus, menu -> menu.getId())));
            
            if (CollectionUtil.isNotEmpty(allPerms)) {
                Map<String, List<Permission>> menuOfPerms = CollectionUtil.groupBy(allPerms, perm -> perm.getMenuId());
               
                for (Menu menu : menus) 
                    menu.setPerms(menuOfPerms.get(menu.getId()));
            }
        }
        return menus;
    }

}