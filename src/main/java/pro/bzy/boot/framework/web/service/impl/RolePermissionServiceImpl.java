package pro.bzy.boot.framework.web.service.impl;

import pro.bzy.boot.framework.web.domain.entity.Permission;
import pro.bzy.boot.framework.web.domain.entity.Role;
import pro.bzy.boot.framework.web.domain.entity.RolePermission;
import pro.bzy.boot.framework.web.domain.entity.UserRole;
import pro.bzy.boot.framework.web.mapper.PermissionMapper;
import pro.bzy.boot.framework.web.mapper.RolePermissionMapper;
import pro.bzy.boot.framework.web.service.PermissionService;
import pro.bzy.boot.framework.web.service.RolePermissionService;
import pro.bzy.boot.framework.web.service.UserRoleService;
import pro.bzy.boot.framework.utils.CollectionUtil;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * 角色权限关联关系 服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

    @Resource
    private RolePermissionMapper rolePermissionMapper;
    
    @Resource
    private PermissionMapper permissionMapper;
    @Autowired
    private PermissionService permissionService;
    
    @Autowired
    private UserRoleService userRoleService;

    @Override
    public List<Permission> getPermsByRole(List<Role> roles) {
        List<Permission> perms = Lists.newArrayList();
        
        if (!CollectionUtil.isEmpty(roles)) {
            List<RolePermission> rps = rolePermissionMapper.selectList(
                    Wrappers.<RolePermission>lambdaQuery()
                        .in(RolePermission::getRoleId, 
                            roles.stream().map(r -> r.getId()).collect(Collectors.toList())));
            
            if (!CollectionUtil.isEmpty(rps)) {
                perms = permissionMapper.selectList(
                            Wrappers.<Permission>lambdaQuery()
                                .in(Permission::getId, 
                                    rps.stream().map(rp -> rp.getPermissionId()).collect(Collectors.toList())));
            }
        }
        return perms;
    }

    @Override
    public List<String> getPermsNameOfUser(String userid) {
        // 1. 查询用户的全部角色
        List<UserRole> userRoles = userRoleService.list(
                Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, userid));
        
        // 2. 根据角色查询被分配的权限id
        List<RolePermission> rolePerms = null;
        if (CollectionUtil.isNotEmpty(userRoles)) {
            rolePerms = list(Wrappers.<RolePermission>lambdaQuery()
                    .in(RolePermission::getRoleId, Lists.transform(userRoles, ur -> ur.getRoleId())));
        }
        
        // 3. 使用权限id查询全部权限的名称
        List<Permission> perms = null;
        if (CollectionUtil.isNotEmpty(rolePerms)) {
            perms = permissionService.list(Wrappers.<Permission>lambdaQuery()
                    .in(Permission::getId, Lists.transform(rolePerms, rp -> rp.getPermissionId())));
        }
        return perms == null
                ? Lists.newArrayListWithCapacity(0)
                : Lists.transform(perms, p -> p.getName());
    }

}