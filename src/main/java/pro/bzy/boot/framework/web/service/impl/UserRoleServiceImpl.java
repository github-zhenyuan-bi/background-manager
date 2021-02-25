package pro.bzy.boot.framework.web.service.impl;

import pro.bzy.boot.framework.web.domain.entity.Role;
import pro.bzy.boot.framework.web.domain.entity.UserRole;
import pro.bzy.boot.framework.web.mapper.RoleMapper;
import pro.bzy.boot.framework.web.mapper.UserRoleMapper;
import pro.bzy.boot.framework.web.service.RoleService;
import pro.bzy.boot.framework.web.service.UserRoleService;
import pro.bzy.boot.framework.utils.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

/**
 * 用户角色关联关系 服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Resource
    private UserRoleMapper userRoleMapper;
    
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleService roleService;
    
    @Override
    public List<Role> getRolesByUserId(String userId) {
        // 查询用户角色关联列表
        List<UserRole> userRoles = userRoleMapper.selectList(
                Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, userId));
        List<Role> roles = null;
        
        // 查询角色列表
        if (!CollectionUtil.isEmpty(userRoles)) {
            roles = roleMapper.selectList(
                    Wrappers.<Role>lambdaQuery().in(
                            Role::getId, 
                            CollectionUtil.map(userRoles, ur -> ur.getRoleId())));
        } else {
            roles = Lists.newArrayListWithCapacity(0);
        }
        return roles;
    }

    
    
    @Override
    public void bindPublicRoleForUser(final String userId) {
        if (StringUtils.isEmpty(userId))
            throw new IllegalArgumentException("需要绑定公共角色的用户ID不能为空");
        
        // 查询公共角色
        Role publicRole = roleService.getPublicRole();
        if (publicRole == null)
            throw new RuntimeException("公共角色还未创建");
        
        UserRole userRole = UserRole.builder()
                .userId(userId)
                .roleId(publicRole.getId())
                .build();
        
        // 绑定关系
        save(userRole);
    }



    @Override
    @Transactional(rollbackFor= {Exception.class})
    public void changeUserRoles(String userId, String[] roles) {
        // 1. 删除该用户得全部角色
        remove(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, userId));
        
        // 2. 重新绑定用户角色
        if (roles != null && roles.length > 0) {
            Collection<UserRole> userRoles = CollectionUtil.map(Arrays.asList(roles), 
                    roleId -> {return UserRole.builder().userId(userId).roleId(roleId).build();});
            saveBatch(userRoles);
        }
    }

}