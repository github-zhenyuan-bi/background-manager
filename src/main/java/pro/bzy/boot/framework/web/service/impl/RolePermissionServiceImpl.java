package pro.bzy.boot.framework.web.service.impl;

import pro.bzy.boot.framework.web.domain.entity.Permission;
import pro.bzy.boot.framework.web.domain.entity.Role;
import pro.bzy.boot.framework.web.domain.entity.RolePermission;
import pro.bzy.boot.framework.web.mapper.PermissionMapper;
import pro.bzy.boot.framework.web.mapper.RolePermissionMapper;
import pro.bzy.boot.framework.web.service.RolePermissionService;
import pro.bzy.boot.framework.utils.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;

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

}