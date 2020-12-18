package pro.bzy.boot.framework.web.controller;

import pro.bzy.boot.framework.web.domain.entity.Menu;
import pro.bzy.boot.framework.web.mapper.MenuMapper;
import pro.bzy.boot.framework.web.service.MenuService;
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
 * 菜单资源 前端控制器
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/controller.java.ftl
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
@Api(tags = {"菜单资源"}, value="菜单资源")
@ApiSupport(order = 100)
@RequestMapping("/framework/menu")
@RestController
public class MenuController {

	@Resource
    private MenuMapper menuMapper;
    
    @Resource
    private MenuService menuService;
    
    
    
    @ApiOperation(value="id查询菜单")
    @GetMapping("getById")
    public R<Menu> getById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        return R.ofSuccess(menuService.getById(id));
    }

    
    
    @ApiOperation(value="菜单列表")
    @GetMapping("getList")
    public R<List<Menu>> getList(Menu queryBean) {
        List<Menu> menus = menuService.list(Wrappers.<Menu>lambdaQuery(queryBean).orderByAsc(Menu::getSort));
        return R.ofSuccess(menus);
    }
    
    
    
    @ApiOperation(value="菜单分页")
    @GetMapping("getPage")
    public R<Page<Menu>> getPage(int pageNo, int pageSize, Menu queryBean) {
        Page<Menu> page = new Page<>(pageNo, pageSize);
        menuService.page(page, Wrappers.<Menu>lambdaQuery(queryBean));
        return R.ofSuccess(page);
    }
    
    
    
    @ApiOperation(value="ID删除")
    @DeleteMapping("deleteById")
    public R<String> deleteById(String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        int count = menuService.removeMenuThenHandleChilds(id);
        return R.ofSuccess("共删除当前菜单资源和子菜单共：" + count + " 条记录");
    }
    
    
    
    @ApiOperation(value="IDs批量删除")
    @DeleteMapping("batchDeleteByIds")
    public R<String> batchDeleteByIds(String[] ids) {
        ExceptionCheckUtil.notEmpty(ids, "批量删除的IDs 不能为空");
        
        boolean flag = menuService.removeByIds(Arrays.asList(ids));
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="增加菜单")
    @PostMapping("addRecord")
    public R<String> addRecord(
            @Validated(value= {FormValid.class}) @RequestBody Menu menu, 
            BindingResult bindingResult) {
        if (StringUtils.isEmpty(menu.getId()))
            menu.setId(null);
        
        boolean flag = menuService.save(menu);
        return R.ofSuccess(flag? "添加成功，ID:" + menu.getId() : "添加失败");
    }
    
    
    
    @ApiOperation(value="ID更新")
    @PostMapping("updateById")
    public R<String> updateById(
            @Validated(value= {FormValid.class}) 
            @RequestBody Menu updateBean, BindingResult bindingResult) {
        ExceptionCheckUtil.hasLength(updateBean.getId(), "ID 不能为空");
        
        boolean flag = menuService.updateById(updateBean);
        return R.ofSuccess(flag? "更新成功" : "更新失败");
    }
}