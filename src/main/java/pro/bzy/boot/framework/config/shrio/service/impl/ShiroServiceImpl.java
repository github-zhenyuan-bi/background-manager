package pro.bzy.boot.framework.config.shrio.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.ShiroException;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import pro.bzy.boot.framework.config.shrio.service.ShiroService;
import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.framework.web.domain.entity.Menu;
import pro.bzy.boot.framework.web.domain.entity.Permission;
import pro.bzy.boot.framework.web.service.MenuService;
import pro.bzy.boot.framework.web.service.PermissionService;
import pro.bzy.boot.framework.web.service.RoleMenuService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShiroServiceImpl implements ShiroService {

    @Lazy
    @Resource
    private MenuService menuService;
    
    @Lazy
    @Resource
    private RoleMenuService roleMenuService;
    
    @Lazy
    @Resource
    private PermissionService permissionService;
    
    @Lazy
    @Resource
    private ShiroFilterFactoryBean shiroFilterFactoryBean;
    
    
    @Override
    public Map<String, String> getFilterChainDefinitionMapFromDB() {
        Map<String, String> resMap = Maps.newLinkedHashMap();
        resMap.put("/assets/**", "anon");
        resMap.put("/images/**", "anon");
        resMap.put("/webjars/**", "anon");
        resMap.put("/error/**", "anon");
        resMap.put("/404**", "anon");
        resMap.put("/500**", "anon");
        resMap.put("/**/favicon.ico", "anon");
        resMap.put("/imageServer/**", "anon");
        // 登陆注册
        resMap.put("/", "anon");
        resMap.put("/login**", "anon");
        resMap.put("/register**", "anon");
        resMap.put("/logout", "logout");
        
        // 剧本相关接口
        resMap.put("/**/public/**", "anon");
        List<Menu> menus = menuService.list();
        if (CollectionUtil.isNotEmpty(menus)) {
            List<Permission> perms = permissionService.list();
            if (CollectionUtil.isNotEmpty(perms)) {
                Map<String, List<Permission>> permsOfMenu = CollectionUtil.groupBy(perms, perm -> perm.getMenuId());
                for (Menu menu : menus) {
                    List<Permission> thisPerms = permsOfMenu.get(menu.getId());
                    if (CollectionUtil.isNotEmpty(thisPerms)) {
                        for (Permission perm : thisPerms) {
                            resMap.put(menu.getFilterExp()+perm.getPartOfUrl(), perm.getName());
                        }
                    }
                }
            }
        }
        resMap.put("/**", "jwt");
        return resMap;
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
            System.err.println("原先的map"+shiroFilterFactoryBean.getFilterChainDefinitionMap());
            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            
            // 动态查询数据库中所有权限
            shiroFilterFactoryBean.setFilterChainDefinitionMap(getFilterChainDefinitionMapFromDB());
            
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





    @Override
    public void updateFilterChainDefinitionMap() {
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
            
            
            Map<String, String> chains = getFilterChainDefinitionMapFromDB();
            // 动态查询数据库中所有权限
            shiroFilterFactoryBean.setFilterChainDefinitionMap(chains);
            
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                manager.createChain(entry.getKey(), entry.getValue());
            }
        }
    }

}
