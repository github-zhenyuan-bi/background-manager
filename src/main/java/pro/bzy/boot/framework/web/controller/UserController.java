package pro.bzy.boot.framework.web.controller;

import pro.bzy.boot.framework.web.domain.entity.User;
import pro.bzy.boot.framework.web.mapper.UserMapper;
import pro.bzy.boot.framework.web.service.UserInfoService;
import pro.bzy.boot.framework.web.service.UserRoleService;
import pro.bzy.boot.framework.web.service.UserService;
import pro.bzy.boot.framework.web.service.UserUsergroupService;
import pro.bzy.boot.framework.web.annoations.FormValid;
import pro.bzy.boot.framework.web.domain.bean.R;
import pro.bzy.boot.framework.utils.ExceptionCheckUtil;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;


/**
 * 用户 前端控制器
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/controller.java.ftl
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
@Api(tags = {"用户"}, value="用户")
@ApiSupport(order = 100)
@RequestMapping("/framework/user")
@RestController
public class UserController {

	@Resource
    private UserMapper userMapper;
    
    @Resource
    private UserService userService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private UserUsergroupService userUsergroupService;
    
    
    
    @ApiOperation(value="id查询")
    @GetMapping("getById")
    public R<User> getById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        return R.ofSuccess(userService.getById(id));
    }

    
    
    @ApiOperation(value="列表数据")
    @GetMapping("getList")
    public R<List<User>> getList(User queryBean) {
        List<User> users = userService.list(Wrappers.<User>lambdaQuery(queryBean));
        return R.ofSuccess(users);
    }
    
    
    
    @ApiOperation(value="分页数据")
    @GetMapping("getPage")
    public R<Page<User>> getPage(int pageNo, int pageSize, String search, User queryBean) {
        // 用户分页列表
        Page<User> page = new Page<>(pageNo, pageSize);
        userService.page(page, Wrappers.<User>lambdaQuery(queryBean)
                .likeRight(!StringUtils.isEmpty(search), User::getUsername, search));
        // 每个用户对应得详情
        page.getRecords().forEach(user -> {
            user.setUserInfo(userInfoService.getById(user.getId()));
        });
        return R.ofSuccess(page);
    }
    
    
    
    @ApiOperation(value="ID删除")
    @DeleteMapping("deleteById")
    public R<String> deleteById(String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        boolean flag = userService.removeUserByIdThenHandlerSomething(id);
        return R.ofSuccess((flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="IDs批量删除")
    @DeleteMapping("batchDeleteByIds")
    public R<String> batchDeleteByIds(String[] ids) {
        ExceptionCheckUtil.notEmpty(ids, "批量删除的IDs 不能为空");
        
        boolean flag = userService.removeByIds(Arrays.asList(ids));
        return R.ofSuccess((flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="增加用户")
    @PostMapping("addRecord")
    public R<String> addRecord(
            @Validated(value= {FormValid.class}) 
            @RequestBody User user, BindingResult bindingResult) {
        userService.addUserThenHandlerSomething(user);
        return R.ofSuccess("添加成功，账号:" + user.getUsername());
    }
    
    
    
    @ApiOperation(value="ID更新")
    @PostMapping("updateById")
    public R<String> updateById(User updateBean) {
        ExceptionCheckUtil.hasLength(updateBean.getId(), "ID 不能为空");
        
        boolean flag = userService.updateById(updateBean);
        return R.ofSuccess(flag? "更新成功" : "更新失败");
    }
    
    
    
    @ApiOperation(value="更新账号密码")
    @PostMapping("updateAccountPassword")
    public R<String> updateAccountPassword(String userId, String oldPassword, String newPassword) {
        ExceptionCheckUtil.hasLength(userId, "ID 不能为空");
        ExceptionCheckUtil.hasLength(oldPassword, "旧密码不能为空");
        ExceptionCheckUtil.hasLength(newPassword, "新密码不能为空");
        
        userService.updateAccountPassword(userId, oldPassword, newPassword);
        return R.ofSuccess("更新成功");
    }
    
    
    
    @ApiOperation(value="分配角色")
    @PostMapping("allocateRoles")
    public R<String> allocateRoles(String userId, String[] roles) {
        ExceptionCheckUtil.hasLength(userId, "用户ID 不能为空");
        
        userRoleService.changeUserRoles(userId, roles);
        return R.ofSuccess("更新成功");
    } 
    
    
    
    
    @ApiOperation(value="重构用户组关系")
    @PostMapping("updateUserWithUsergroupRelationship")
    public R<String> updateUserWithUsergroupRelationship(@RequestBody Map<String, Object> params) {
        Object userId  = params.get("userId");
        Object groupIds = params.get("groupIds");
        
        ExceptionCheckUtil.notNull(userId, "用户ID不能为空");
        String[] groupIdArr = groupIds == null? null : groupIds.toString().split(",");
        userUsergroupService.updateUserWithUsergroupRelationship(userId.toString(), groupIdArr);
        return R.ofSuccess("更新成功");
    }
    
    
    
    @ApiOperation(value="重构用户组关系")
    @PostMapping("uploadAvatar")
    public R<String> uploadAvatar(String userid, MultipartFile img) {
        String rest = userService.uploadUserAvatar(userid, img);
        return R.ofSuccess("上传成功, 图片地址：" + rest);
    }
    
    
    
    
    
}