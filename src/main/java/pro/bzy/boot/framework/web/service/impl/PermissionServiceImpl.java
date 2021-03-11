package pro.bzy.boot.framework.web.service.impl;

import pro.bzy.boot.framework.config.shrio.service.ShiroService;
import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.framework.web.domain.bean.PermissionSettForm;
import pro.bzy.boot.framework.web.domain.entity.Permission;
import pro.bzy.boot.framework.web.mapper.PermissionMapper;
import pro.bzy.boot.framework.web.service.PermissionService;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import freemarker.template.utility.NullArgumentException;
import lombok.NonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

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
    @Autowired
    private ShiroService shiroService;
    
    
    @Override
    public void updatePermsForMenu(@NonNull String menuId, List<PermissionSettForm> permSetts) {
        // 0. 数据校验
        checkDataBeforeSaveToDB(permSetts);
        
        // 1. 删除旧的权限配置
        remove(Wrappers.<Permission>lambdaQuery()
                .eq(Permission::getMenuId, menuId)
                .notIn(CollectionUtil.isNotEmpty(permSetts), 
                        Permission::getId, 
                        Lists.transform(permSetts, ps -> ps.getId())));
        
        // 2. 绑定新配置 存在于库则更新 否则新增
        if (CollectionUtil.isNotEmpty(permSetts)) {
            List<Permission> perms = Lists.newArrayListWithCapacity(permSetts.size());
            for (PermissionSettForm ps : permSetts) {
                String id = ps.getId();
                if (StringUtils.isEmpty(id)) {
                    Permission saveBean = new  Permission();
                    saveBean.setMenuId(menuId).setPartOfUrl(ps.getUrlExp()).setName(ps.getPerm()).setDescrip(ps.getDescrip());
                    perms.add(saveBean);
                } else {
                    update(Wrappers.<Permission>lambdaUpdate()
                            .set(Permission::getPartOfUrl, ps.getUrlExp())
                            .set(Permission::getName, ps.getPerm())
                            .set(Permission::getDescrip, ps.getDescrip())
                            .eq(Permission::getId, ps.getId()));
                }
            }
            if (CollectionUtil.isNotEmpty(perms))
                saveBatch(perms);
        }
        // 3. 更新shiroFilter
        shiroService.updateFilterChainDefinitionMap();
    }
    
    
    
    /**
     * 入库前的数据校验
     * @param permSetts
     */
    private void checkDataBeforeSaveToDB(List<PermissionSettForm> permSetts) {
        Set<String> urlExps = Sets.newHashSetWithExpectedSize(permSetts.size());
        for (PermissionSettForm perm : permSetts) {
            if (StringUtils.isEmpty(perm.getId()))
                perm.setId(null);
            
            String urlExp = perm.getUrlExp();
            String permExp = perm.getPerm();
            if (StringUtils.isEmpty(urlExp))
                throw new NullArgumentException("urlExp");
            if (StringUtils.isEmpty(permExp))
                throw new NullArgumentException("perm");
            
            if (!urlExps.add(urlExp)) {
                throw new RuntimeException("存在重复数据，同名url表达式：" + urlExp);
            } 
        }
    }

}