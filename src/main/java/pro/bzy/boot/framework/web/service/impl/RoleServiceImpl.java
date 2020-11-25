package pro.bzy.boot.framework.web.service.impl;

import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.framework.utils.SystemConstant;
import pro.bzy.boot.framework.web.domain.entity.Role;
import pro.bzy.boot.framework.web.domain.entity.UserRole;
import pro.bzy.boot.framework.web.mapper.RoleMapper;
import pro.bzy.boot.framework.web.mapper.UserRoleMapper;
import pro.bzy.boot.framework.web.service.RoleService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

/**
 * 角色 服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    
    
    @Override
    public Role getPublicRole() {
        return getOne(Wrappers.<Role>lambdaQuery().eq(Role::getName, "PUBLIC"));
    }



    @Override
    @Transactional(readOnly= true)
    public List<Role> getRolesWithUserHasFlag(String userId) {
        // 1. 查询全部可用的角色列表 并排序
        List<Role> allUseableRoles = list(Wrappers.<Role>lambdaQuery()
                .eq(Role::getIsForbidden, SystemConstant.NOT_FORBIDDEN)
                .orderByAsc(Role::getSort));
        
        // 2. 查询指定用户拥有的全部角色ID
        List<UserRole> userRoles = userRoleMapper.selectList(
                Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, userId));
        
        // 3. 将[1]中存在[2]中的数据里面标记userHas字段true
        if (!CollectionUtil.isEmpty(userRoles)) {
            Collection<String> roleIdsOfUser = CollectionUtil.map(userRoles, ur -> {return ur.getRoleId();});
            allUseableRoles.forEach(r -> {
                r.setUserHas(
                        roleIdsOfUser.stream().anyMatch(ru -> ru.equals(r.getId())));
            });
        } else {
            allUseableRoles.forEach(r -> r.setUserHas(false));
        }
        return allUseableRoles;
    }

    

}