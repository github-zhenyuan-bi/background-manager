package pro.bzy.boot.framework.web.service.impl;

import pro.bzy.boot.framework.web.domain.entity.UserUsergroup;
import pro.bzy.boot.framework.web.mapper.UserUsergroupMapper;
import pro.bzy.boot.framework.web.service.UserUsergroupService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

/**
 * 用户与用户组之间的关联 服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-06
 */
@Service
public class UserUsergroupServiceImpl extends ServiceImpl<UserUsergroupMapper, UserUsergroup> implements UserUsergroupService {

    @Resource
    private UserUsergroupMapper userUsergroupMapper;

    
    
    @Override
    public void updateUserWithUsergroupRelationship(String userId, String[] groupIds) {
        // 删除旧的绑定关系
        remove(Wrappers.<UserUsergroup>lambdaUpdate().eq(UserUsergroup::getUserId, userId));
        
        // 存储新的关系
        if (groupIds != null && groupIds.length > 0) {
            List<UserUsergroup> userUsergroup = Stream.<String>of(groupIds)
                    .map(gid -> UserUsergroup.builder().userId(userId).userGroupId(gid).build())
                    .collect(Collectors.toList());
            saveBatch(userUsergroup);
        }
    }

}