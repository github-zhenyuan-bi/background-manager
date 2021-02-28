package pro.bzy.boot.framework.web.service.impl;

import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.framework.web.domain.entity.Menu;
import pro.bzy.boot.framework.web.mapper.MenuMapper;
import pro.bzy.boot.framework.web.service.MenuService;
import pro.bzy.boot.framework.web.service.RoleMenuService;
import lombok.NonNull;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

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
        
        // 删除id对应得菜单资源
        count += menuMapper.deleteById(id);
        
        // 删除全部子集菜单资源
        count += menuMapper.delete(Wrappers.<Menu>lambdaUpdate().eq(Menu::getPid, id));
        
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

}