package pro.bzy.boot.framework.web.controller;

import pro.bzy.boot.framework.web.domain.entity.Constant;
import pro.bzy.boot.framework.web.mapper.ConstantMapper;
import pro.bzy.boot.framework.web.service.ConstantService;
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
 * 系统常量表 前端控制器
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/controller.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-01
 */
@Api(tags = {"系统常量"}, value="系统常量")
@ApiSupport(order = 100)
@RequestMapping("/framework/constant")
@RestController
public class ConstantController {

	@Resource
    private ConstantMapper constantMapper;
    
    @Resource
    private ConstantService constantService;
    
    
    
    @ApiOperation(value="id查询常量")
    @GetMapping("getById")
    public R<Constant> getById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        return R.ofSuccess(constantService.getById(id));
    }

    
    
    
    @ApiOperation(value="常量列表")
    @GetMapping("getList")
    public R<List<Constant>> getList(Constant queryBean) {
        List<Constant> constants = constantService.list(Wrappers.<Constant>lambdaQuery(queryBean));
        return R.ofSuccess(constants);
    }
    
    
    
    
    @ApiOperation(value="常量分页")
    @GetMapping("getPage")
    public R<Page<Constant>> getPage(int pageNo, int pageSize, String search, Constant queryBean) {
        Page<Constant> page = new Page<>(pageNo, pageSize);
        constantService.page(page, Wrappers.<Constant>lambdaQuery(queryBean)
                .likeRight(!StringUtils.isEmpty(search), Constant::getConstType, search)
                .or(!StringUtils.isEmpty(search))
                .likeRight(!StringUtils.isEmpty(search), Constant::getConstKey, search)
                .orderByAsc(Constant::getConstType));
        return R.ofSuccess(page);
    }
    
    
    
    
    @ApiOperation(value="ID删除")
    @DeleteMapping("deleteById")
    public R<String> deleteById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        boolean flag = constantService.removeById(id);
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="IDs批量删除")
    @DeleteMapping("batchDeleteByIds")
    public R<String> batchDeleteByIds(String[] ids) {
        ExceptionCheckUtil.notEmpty(ids, "批量删除的IDs 不能为空");
        
        boolean flag = constantService.removeByIds(Arrays.asList(ids));
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    
    @ApiOperation(value="增加常量")
    @PostMapping("addRecord")
    public R<String> addRecord(
            @Validated(value= {FormValid.class}) 
            @RequestBody Constant constant, BindingResult bindingResult) {
        if (StringUtils.isEmpty(constant.getId()))
            constant.setId(null);
        
        boolean flag = constantService.save(constant);
        return R.ofSuccess(flag? "添加成功，ID:" + constant.getId() : "添加失败");
    }
    
    
    
    @ApiOperation(value="ID更新")
    @PostMapping("updateById")
    public R<String> updateById(
            @Validated(value= {FormValid.class}) 
            @RequestBody Constant updateBean, BindingResult bindingResult) {
        ExceptionCheckUtil.hasLength(updateBean.getId(), "ID 不能为空");
        
        boolean flag = constantService.updateById(updateBean);
        return R.ofSuccess(flag? "更新成功" : "更新失败");
    }
}