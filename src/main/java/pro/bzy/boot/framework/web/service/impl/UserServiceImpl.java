package pro.bzy.boot.framework.web.service.impl;

import pro.bzy.boot.framework.utils.ExceptionCheckUtil;
import pro.bzy.boot.framework.utils.PasswordCodecUtil;
import pro.bzy.boot.framework.web.domain.entity.User;
import pro.bzy.boot.framework.web.domain.entity.UserInfo;
import pro.bzy.boot.framework.web.mapper.UserMapper;
import pro.bzy.boot.framework.web.service.UserInfoService;
import pro.bzy.boot.framework.web.service.UserRoleService;
import pro.bzy.boot.framework.web.service.UserService;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 用户 服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserInfoService userInfoService;
    
    @Resource
    private UserRoleService userRoleService;
    
    
    
    @Override
    public boolean checkUsernameAndPassword(User user) {
        // 根据用户名查询用户
        User existUser = getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
        ExceptionCheckUtil.notNull(existUser, "当前用户不存在：" + user.getUsername());
        
        // 验证用户密码
        String sha1Password = PasswordCodecUtil
                .sha1Password(user.getUsername(), user.getPassword());
        ExceptionCheckUtil.isTrue(
                sha1Password.equals(existUser.getPassword()), 
                "用户名或密码错误");
             
        return true;
    }
    
    
    
    @Override
    @Transactional(rollbackFor= {Exception.class})
    public void addUserThenHandlerSomething(User user) {
        // 1. 校验数据
        checkUserData(user);
        
        // 2. 数据处理
        handleUserData(user);
        
        // 3. 保存用户信息
        if ( StringUtils.isEmpty(user.getId()) ) user.setId(null);
        boolean addUserSuccess = save(user);
        
        if (addUserSuccess) {
            // 4. 新增用户基本信息表(UserInfo)中的一条关联数据
            userInfoService.save(UserInfo.builder().id(user.getId()).build());
            
            // 5. 给用户挂上基本角色
            userRoleService.bindPublicRoleForUser(user.getId());
        }  
    }

    
    
    /**
     * 新增用户前的数据校验
     * @param user
     */
    private void checkUserData(User user) {
        // TODO 校验数据正确
    }
    
    
    
    /** 
     * 新增用户前的数据处理
     * @param user
     */
    private void handleUserData(User user) {
        // 密码加密算法
        user.setPassword(PasswordCodecUtil.sha1(user));
    }



    @Override
    public boolean removeUserByIdThenHandlerSomething(final String id) {
        // 1. 删除ID对应用户 （当前为逻辑删除）
        return removeById(id);
    }




}