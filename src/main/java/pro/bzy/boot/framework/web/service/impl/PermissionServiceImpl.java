package pro.bzy.boot.framework.web.service.impl;

import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.framework.web.domain.bean.PermissionSettForm;
import pro.bzy.boot.framework.web.domain.entity.Permission;
import pro.bzy.boot.framework.web.mapper.PermissionMapper;
import pro.bzy.boot.framework.web.service.PermissionService;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;

import lombok.NonNull;

import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

/**
 * 权限 服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    
    
    @Override
    public void updatePermsForMenu(@NonNull String menuId, List<PermissionSettForm> permSetts) {
        // 1. 删除旧的权限配置
        remove(Wrappers.<Permission>lambdaQuery().eq(Permission::getMenuId, menuId));
        
        // 2. 绑定新配置
        if (CollectionUtil.isNotEmpty(permSetts)) {
            List<Permission> perms = Lists.newArrayListWithCapacity(permSetts.size());
            permSetts.forEach(ps -> {
                Permission perm = new Permission();
                perm.setMenuId(menuId)
                    .setPartOfUrl(ps.getUrlExp())
                    .setName(ps.getPerm())
                    .setDescrip(ps.getDescrip());
                perms.add(perm);
            });
            saveBatch(perms);
        }
    }

}