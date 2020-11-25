package pro.bzy.boot.framework.web.service.impl;

import pro.bzy.boot.framework.web.domain.entity.Permission;
import pro.bzy.boot.framework.web.mapper.PermissionMapper;
import pro.bzy.boot.framework.web.service.PermissionService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}