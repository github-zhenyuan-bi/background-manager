package pro.bzy.boot.framework.web.controller;

import pro.bzy.boot.framework.web.domain.entity.Usergroup;
import pro.bzy.boot.framework.web.mapper.UserUsergroupMapper;
import pro.bzy.boot.framework.web.mapper.UsergroupMapper;
import pro.bzy.boot.framework.web.service.UsergroupService;
import pro.bzy.boot.framework.web.annoations.FormValid;
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
 * 用户组 前端控制器
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/controller.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-06
 */
@Api(tags = {"用户组"}, value="用户组")
@ApiSupport(order = 100)
@RequestMapping("/framework/usergroup")
@RestController
public class UsergroupController {

	@Resource
    private UsergroupMapper usergroupMapper;
    
    @Resource
    private UsergroupService usergroupService;
    
    @Resource
    private UserUsergroupMapper userUsergroupMapper;
    
    
    @ApiOperation(value="id查询")
    @GetMapping("getById")
    public R<Usergroup> getById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        return R.ofSuccess(usergroupService.getById(id));
    }

    
    
    @ApiOperation(value="列表数据")
    @GetMapping("getList")
    public R<List<Usergroup>> getList(Usergroup queryBean) {
        List<Usergroup> usergroups = usergroupService.list(Wrappers.<Usergroup>lambdaQuery(queryBean));
        return R.ofSuccess(usergroups);
    }
    
    
    
    @ApiOperation(value="分页数据")
    @GetMapping("getPage")
    public R<Page<Usergroup>> getPage(int pageNo, int pageSize, Usergroup queryBean) {
        Page<Usergroup> page = new Page<>(pageNo, pageSize);
        usergroupService.page(page, Wrappers.<Usergroup>lambdaQuery(queryBean));
        return R.ofSuccess(page);
    }
    
    
    
    @ApiOperation(value="ID删除")
    @DeleteMapping("deleteById")
    public R<String> deleteById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        boolean flag = usergroupService.removeById(id);
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="IDs批量删除")
    @DeleteMapping("batchDeleteByIds")
    public R<String> batchDeleteByIds(String[] ids) {
        ExceptionCheckUtil.notEmpty(ids, "批量删除的IDs 不能为空");
        
        boolean flag = usergroupService.removeByIds(Arrays.asList(ids));
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="增加用户组")
    @PostMapping("addRecord")
    public R<String> addRecord(
            @Validated(value= {FormValid.class}) 
            @RequestBody Usergroup usergroup, BindingResult bindingResult) {
        if (StringUtils.isEmpty(usergroup.getId()))
            usergroup.setId(null);
        
        boolean flag = usergroupService.save(usergroup);
        return R.ofSuccess(flag? "添加成功，ID:" + usergroup.getId() : "添加失败");
    }
    
    
    
    @ApiOperation(value="ID更新")
    @PostMapping("updateById")
    public R<String> updateById(
            @Validated(value= {FormValid.class}) 
            @RequestBody Usergroup updateBean, BindingResult bindingResult) {
        ExceptionCheckUtil.hasLength(updateBean.getId(), "ID 不能为空");
        
        boolean flag = usergroupService.updateById(updateBean);
        return R.ofSuccess(flag? "更新成功" : "更新失败");
    }
    
    
    
    
    @ApiOperation(value="对应得用户数量")
    @GetMapping("getUserNumsGroupByUsergroup")
    public R<Object> getUserNumsGroupByUsergroup() {
        Object data = userUsergroupMapper.getUserNumsGroupByUsergroup();
        return R.ofSuccess(data);
    } 
}