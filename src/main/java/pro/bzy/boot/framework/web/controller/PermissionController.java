package pro.bzy.boot.framework.web.controller;

import pro.bzy.boot.framework.web.domain.entity.Permission;
import pro.bzy.boot.framework.web.mapper.PermissionMapper;
import pro.bzy.boot.framework.web.service.PermissionService;
import pro.bzy.boot.framework.web.domain.bean.PermissionSettForm;
import pro.bzy.boot.framework.web.domain.bean.R;
import pro.bzy.boot.framework.utils.ExceptionCheckUtil;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Arrays;
import java.util.List;

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
 * 权限 前端控制器
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/controller.java.ftl
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
@Api(tags = {"权限"}, value="权限")
@ApiSupport(order = 100)
@RequestMapping("/framework/permission")
@RestController
public class PermissionController {

	@Resource
    private PermissionMapper permissionMapper;
    
    @Resource
    private PermissionService permissionService;
    
    
    
    /**
     * 使用id查询数据
     * @param id
     * @return
     */
    @GetMapping("getById")
    public R<Permission> getById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        return R.ofSuccess(permissionService.getById(id));
    }

    
    
    /**
     * 查询数据列表
     */
    @GetMapping("getList")
    public R<List<Permission>> getList(Permission queryBean) {
        List<Permission> permissions = permissionService.list(Wrappers.<Permission>lambdaQuery(queryBean));
        return R.ofSuccess(permissions);
    }
    
    
    
    /**
     * 查询数据分页
     */
    @GetMapping("getPage")
    public R<Page<Permission>> getPage(int pageNo, int pageSize, Permission queryBean) {
        Page<Permission> page = new Page<>(pageNo, pageSize);
        permissionService.page(page, Wrappers.<Permission>lambdaQuery(queryBean));
        return R.ofSuccess(page);
    }
    
    
    
    /**
     * 根据id删除数据
     */
    @DeleteMapping("deleteById")
    public R<String> deleteById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        boolean flag = permissionService.removeById(id);
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    /**
     * 批量删除 根据id数组
     */
    @DeleteMapping("batchDeleteByIds")
    public R<String> batchDeleteByIds(String[] ids) {
        ExceptionCheckUtil.notEmpty(ids, "批量删除的IDs 不能为空");
        
        boolean flag = permissionService.removeByIds(Arrays.asList(ids));
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    /**
     * 插入一条新数据
     * @param permission 数据
     * @param bindingResult 表单校验结果
     * @return
     */
    @PostMapping("addRecord")
    public R<String> addRecord(@Validated(value= {}) Permission permission, BindingResult bindingResult) {
        if (StringUtils.isEmpty(permission.getId()))
            permission.setId(null);
        
        boolean flag = permissionService.save(permission);
        return R.ofSuccess(flag? "添加成功，ID:" + permission.getId() : "添加失败");
    }
    
    
    
    /**
     * 更新数据
     * @param updateBean 数据
     * @param bindingResult 表单校验结果
     * @return
     */
    public R<String> updateById(@Validated(value= {}) Permission updateBean, BindingResult bindingResult) {
        ExceptionCheckUtil.hasLength(updateBean.getId(), "ID 不能为空");
        
        boolean flag = permissionService.updateById(updateBean);
        return R.ofSuccess(flag? "更新成功" : "更新失败");
    }
    
    
    @ApiOperation(value="更新菜单资源权限配置")
    @PostMapping("updateMenuPerms")
    public R<String> updateMenuPerms(String id, @RequestBody List<PermissionSettForm> permSetts) {
        permissionService.updatePermsForMenu(id, permSetts);
        return R.ofSuccess("更新成功");
    }
}