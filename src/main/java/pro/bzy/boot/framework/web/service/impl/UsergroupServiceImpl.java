package pro.bzy.boot.framework.web.service.impl;

import pro.bzy.boot.framework.web.domain.entity.Usergroup;
import pro.bzy.boot.framework.web.mapper.UsergroupMapper;
import pro.bzy.boot.framework.web.service.UsergroupService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户组 服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-06
 */
@Service
public class UsergroupServiceImpl extends ServiceImpl<UsergroupMapper, Usergroup> implements UsergroupService {

    @Resource
    private UsergroupMapper usergroupMapper;

}