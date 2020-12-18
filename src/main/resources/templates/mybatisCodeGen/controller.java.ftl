package ${package.Controller};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};

import pro.bzy.boot.framework.web.domain.bean.R;
import pro.bzy.boot.framework.web.annoations.FormValid;
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

<#assign lEntity = entity?uncap_first>
<#assign serviceBean = table.serviceName?uncap_first>
<#assign onum = cfg.orderNum!100>

/**
 * ${(table.comment)!} 前端控制器
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/controller.java.ftl
 * @author ${author}
 * @since ${date}
 */
@Api(tags = {"${table.comment!}"})
@ApiSupport(order = ${onum?c})
@RequestMapping("/${(cfg.module)!}/${lEntity}")
@RestController
public class ${table.controllerName} {

	@Resource
    private ${table.mapperName} ${table.mapperName?uncap_first};
    
    @Resource
    private ${table.serviceName} ${serviceBean};
    
    
    
    @ApiOperation(value="id查询")
    @GetMapping("getById")
    public R<${entity}> getById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        return R.ofSuccess(${serviceBean}.getById(id));
    }

    
    
    @ApiOperation(value="查询数据列表")
    @GetMapping("getList")
    public R<List<${entity}>> getList(${entity} queryBean) {
        List<${entity}> ${lEntity}s = ${serviceBean}.list(Wrappers.<${entity}>lambdaQuery(queryBean));
        return R.ofSuccess(${lEntity}s);
    }
    
    
    
    @ApiOperation(value="查询数据分页")
    @GetMapping("getPage")
    public R<Page<${entity}>> getPage(int pageNo, int pageSize, ${entity} queryBean) {
        Page<${entity}> page = new Page<>(pageNo, pageSize);
        ${serviceBean}.page(page, Wrappers.<${entity}>lambdaQuery(queryBean));
        return R.ofSuccess(page);
    }
    
    
    
    @ApiOperation(value="id删除数据")
    @DeleteMapping("deleteById")
    public R<String> deleteById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        boolean flag = ${serviceBean}.removeById(id);
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="批量删除")
    @DeleteMapping("batchDeleteByIds")
    public R<String> batchDeleteByIds(String[] ids) {
        ExceptionCheckUtil.notEmpty(ids, "批量删除的IDs 不能为空");
        
        boolean flag = ${serviceBean}.removeByIds(Arrays.asList(ids));
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="插入一条新数据")
    @PostMapping("addRecord")
    public R<String> addRecord(@Validated(value= {FormValid.class}) @RequestBody ${entity} ${lEntity}, BindingResult bindingResult) {
        if (StringUtils.isEmpty(${lEntity}.getId()))
            ${lEntity}.setId(null);
        
        boolean flag = ${serviceBean}.save(${lEntity});
        return R.ofSuccess(flag? "添加成功，ID:" + ${lEntity}.getId() : "添加失败");
    }
    
    
    
    @ApiOperation(value="ID更新数据")
    @PostMapping("updateById")
    public R<String> updateById(@Validated(value= {FormValid.class}) @RequestBody ${entity} updateBean, BindingResult bindingResult) {
        ExceptionCheckUtil.hasLength(updateBean.getId(), "ID 不能为空");
        
        boolean flag = ${serviceBean}.updateById(updateBean);
        return R.ofSuccess(flag? "更新成功" : "更新失败");
    }
}