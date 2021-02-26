package pro.bzy.boot.framework.web.service.impl;

import pro.bzy.boot.framework.config.yml.YmlBean;
import pro.bzy.boot.framework.utils.ExceptionCheckUtil;import pro.bzy.boot.framework.utils.FileUtil;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.Image;
import java.io.File;

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
    @Resource YmlBean ymlBean;
    
    
    
    @Override
    public User checkUsernameAndPassword(User user) {
        // 根据用户名查询用户
        User existUser = getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
        ExceptionCheckUtil.notNull(existUser, "当前用户不存在：" + user.getUsername());
        
        // 验证用户密码
        String sha1Password = PasswordCodecUtil.sha1Password(user.getUsername(), user.getPassword());
        ExceptionCheckUtil.isTrue(
                sha1Password.equals(existUser.getPassword()), 
                "用户名或密码错误");
             
        return existUser;
    }
    
    
    
    @Override
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
        checkUsernameConflict(user);
    }
    
    
    
    /**
     * 用户名冲突校验
     * @param user
     */
    private void checkUsernameConflict(User user) {
        // 同名用户校验
        int sameNameUserCount = count(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
        if (sameNameUserCount > 0)
            throw new IllegalArgumentException("用户名冲突，该名称已被使用【"+user.getUsername()+"】");
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



    @Override
    public String uploadUserAvatar(String userid, MultipartFile img) {
        File uploadedImg = null;
        String thumbnailImgName = null;
        String uploadPath = ymlBean.getConfig().getImageServer().buildUserAvatarPath();
        try {
            // 1. 校验是否图片文件
            Image imgObj = FileUtil.checkImg(img);
            
            // 3. 上传图片
            uploadedImg = FileUtil.uploadImg(img, uploadPath);
            
            // 5. 生成原图的压缩图片
            thumbnailImgName = FileUtil.generateThumbnail2Directory(
                    uploadedImg, uploadPath, FileUtil.calculateImgScale(imgObj));
            
            String thumbnailImgPath = ymlBean.getConfig().getImageServer().buildUserAvatarServer() + thumbnailImgName;
            
            // 6. 上传图片地址入库
            this.update(Wrappers.<User>lambdaUpdate()
                    .set(User::getAvatar, thumbnailImgPath)
                    .eq(User::getId, userid));
            return thumbnailImgPath;
        } catch (Exception e) {
         // exp: 异常时将上传的文件删除
            if (uploadedImg != null && uploadedImg.exists()) 
                uploadedImg.delete();
            if (!StringUtils.isEmpty(thumbnailImgName)) {
                File thumbnailImg = new File(uploadPath + thumbnailImgName);
                if (thumbnailImg.exists())
                    thumbnailImg.delete();
            }
            throw new RuntimeException("上传用户头像异常，原因：" + e.getMessage());
        }
    }



    @Override
    public void updateAccountPassword(String userId, String oldPassword, String newPassword) {
        User user = getById(userId);
        
        // 1. 确认旧密码正确
        String oldPassword_code = PasswordCodecUtil.sha1Password(user.getUsername(), oldPassword);
        if (!oldPassword_code.equals(user.getPassword())) 
            throw new RuntimeException("旧密码错误");
        
        // 2. 替换新密码
        String newPassword_code = PasswordCodecUtil.sha1Password(user.getUsername(), newPassword);
        update(Wrappers.<User>lambdaUpdate().set(User::getPassword, newPassword_code).eq(User::getId, userId));
    }




}