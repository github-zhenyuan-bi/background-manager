package pro.bzy.boot.framework.web.controller;

import pro.bzy.boot.framework.web.domain.entity.Role;
import pro.bzy.boot.framework.web.mapper.RoleMapper;
import pro.bzy.boot.framework.web.mapper.UserRoleMapper;
import pro.bzy.boot.framework.web.service.RoleMenuService;
import pro.bzy.boot.framework.web.service.RoleService;

import pro.bzy.boot.framework.web.domain.bean.R;
import pro.bzy.boot.framework.utils.ExceptionCheckUtil;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

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
 * 角色 前端控制器
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/controller.java.ftl
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
@Api(tags = {"角色"})
@ApiSupport(order = 100)
@RequestMapping("/framework/role")
@RestController
public class RoleController {

	@Resource
    private RoleMapper roleMapper;
	@Resource
	private UserRoleMapper userRoleMapper;
	
    @Resource
    private RoleService roleService;
    @Resource
    private RoleMenuService roleMenuService;
    
    
    /**
     * 使用id查询数据
     * @param id
     * @return
     */
    @GetMapping("getById")
    public R<Role> getById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        return R.ofSuccess(roleService.getById(id));
    }

    
    
    /**
     * 查询数据列表
     */
    @GetMapping("getList")
    public R<List<Role>> getList(Role queryBean) {
        List<Role> roles = roleService.list(Wrappers.<Role>lambdaQuery(queryBean));
        return R.ofSuccess(roles);
    }
    
    
    
    /**
     * 查询数据分页
     */
    @GetMapping("getPage")
    public R<Page<Role>> getPage(int pageNo, int pageSize, Role queryBean) {
        Page<Role> page = new Page<>(pageNo, pageSize);
        roleService.page(page, Wrappers.<Role>lambdaQuery(queryBean).orderByAsc(Role::getSort));
        return R.ofSuccess(page);
    }
    
    
    
    /**
     * 根据id删除数据
     */
    @DeleteMapping("deleteById")
    public R<String> deleteById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        boolean flag = roleService.removeById(id);
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    /**
     * 批量删除 根据id数组
     */
    @DeleteMapping("batchDeleteByIds")
    public R<String> batchDeleteByIds(String[] ids) {
        ExceptionCheckUtil.notEmpty(ids, "批量删除的IDs 不能为空");
        
        boolean flag = roleService.removeByIds(Arrays.asList(ids));
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    /**
     * 插入一条新数据
     * @param role 数据
     * @param bindingResult 表单校验结果
     * @return
     */
    @PostMapping("addRecord")
    public R<String> addRecord(@Validated(value= {}) @RequestBody Role role, BindingResult bindingResult) {
        if (StringUtils.isEmpty(role.getId()))
            role.setId(null);
        
        boolean flag = roleService.save(role);
        return R.ofSuccess(flag? "添加成功，ID:" + role.getId() : "添加失败");
    }
    
    
    
    /**
     * 更新数据
     * @param updateBean 数据
     * @param bindingResult 表单校验结果
     * @return
     */
    @PostMapping("updateById")
    public R<String> updateById(@Validated(value= {}) @RequestBody Role updateBean, BindingResult bindingResult) {
        ExceptionCheckUtil.hasLength(updateBean.getId(), "ID 不能为空");
        
        boolean flag = roleService.updateById(updateBean);
        return R.ofSuccess(flag? "更新成功" : "更新失败");
    }
    
    
    
    /**
     * 查询每个角色对应得用户数量
     * @return
     */
    @GetMapping("getUserNumsGroupByRole")
    public R<Object> getUserNumsGroupByRole() {
        Object data = userRoleMapper.getUserNumsGroupByRole();
        return R.ofSuccess(data);
    } 
    
    
    
    /**
     * 重构角色菜单关系
     * @param updateBean
     * @param bindingResult
     * @return
     */
    @PostMapping("updateRoleWithMenuRelationship")
    public R<String> updateRoleWithMenuRelationship(@RequestBody Map<String, Object> params) {
        Object roleId  = params.get("roleId");
        Object menuIds = params.get("menuIds");
        
        ExceptionCheckUtil.notNull(roleId, "角色ID不能为空");
        String[] menuIdArr = menuIds == null? null : menuIds.toString().split(",");
        roleMenuService.updateRoleWithMenuRelationShip(roleId.toString(), menuIdArr);
        return R.ofSuccess("更新成功");
    }
}