package pro.bzy.boot.framework.config.shrio.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.ShiroException;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.stereotype.Service;

import pro.bzy.boot.framework.config.shrio.service.ShiroService;
import pro.bzy.boot.framework.web.domain.entity.Menu;
import pro.bzy.boot.framework.web.service.MenuService;
import pro.bzy.boot.framework.web.service.RoleMenuService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShiroServiceImpl implements ShiroService {

    @Resource
    private MenuService menuService;
    
    @Resource
    private RoleMenuService roleMenuService;
    
    
    @Override
    public Map<String, String> loadFilterChainDefinitionMap() {
        // 全部的资源菜单列表
        List<Menu> menus = menuService.list();
        
        // 每一个菜单对应的 角色权限
        Map<String, String> filterChainDefinitionMap = roleMenuService.menuNamesWithRoleNames(menus);
        filterChainDefinitionMap.put("/bgManage/**", "authc");
        return filterChainDefinitionMap;
    }

    
    
    
    
    @Override
    public void updatePermission(ShiroFilterFactoryBean shiroFilterFactoryBean, Integer roleId,
            Boolean isRemoveSession) {
        
        synchronized (this) {
            AbstractShiroFilter shiroFilter;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            } catch (Exception e) {
                throw new ShiroException("get ShiroFilter from shiroFilterFactoryBean error!");
            }
            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();

            // 清空拦截管理器中的存储
            manager.getFilterChains().clear();
            // 清空拦截工厂中的存储,如果不清空这里,还会把之前的带进去
            //            ps:如果仅仅是更新的话,可以根据这里的 map 遍历数据修改,重新整理好权限再一起添加
            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            
            // 动态查询数据库中所有权限
            shiroFilterFactoryBean.setFilterChainDefinitionMap(loadFilterChainDefinitionMap());
            
            // 重新构建生成拦截
            Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                manager.createChain(entry.getKey(), entry.getValue());
            }
            log.info("--------------- 动态生成url权限成功！ ---------------");

            // 动态更新该角色相关联的用户shiro权限
            if(roleId != null){
                updatePermissionByRoleId(roleId,isRemoveSession);
            }
        }
    }

    @Override
    public void updatePermissionByRoleId(Integer roleId, Boolean isRemoveSession) {
        // TODO Auto-generated method stub

    }

}
