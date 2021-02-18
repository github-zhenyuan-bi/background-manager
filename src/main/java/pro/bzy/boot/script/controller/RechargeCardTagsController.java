package pro.bzy.boot.script.controller;

import pro.bzy.boot.script.domain.entity.RechargeCardTags;
import pro.bzy.boot.script.mapper.RechargeCardTagsMapper;
import pro.bzy.boot.script.service.RechargeCardTagsService;

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


/**
 *  前端控制器
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/controller.java.ftl
 * @author zhenyuan.bi
 * @since 2021-02-17
 */
@Api(tags = {""})
@ApiSupport(order = 100)
@RequestMapping("/script/rechargeCardTags")
@RestController
public class RechargeCardTagsController {

	@Resource
    private RechargeCardTagsMapper rechargeCardTagsMapper;
    
    @Resource
    private RechargeCardTagsService rechargeCardTagsService;
    
    
    
    @ApiOperation(value="id查询")
    @GetMapping("getById")
    public R<RechargeCardTags> getById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        return R.ofSuccess(rechargeCardTagsService.getById(id));
    }

    
    
    @ApiOperation(value="查询数据列表")
    @GetMapping("getList")
    public R<List<RechargeCardTags>> getList(RechargeCardTags queryBean) {
        List<RechargeCardTags> rechargeCardTagss = rechargeCardTagsService.list(Wrappers.<RechargeCardTags>lambdaQuery(queryBean));
        return R.ofSuccess(rechargeCardTagss);
    }
    
    
    
    @ApiOperation(value="查询数据分页")
    @GetMapping("getPage")
    public R<Page<RechargeCardTags>> getPage(int pageNo, int pageSize, RechargeCardTags queryBean) {
        Page<RechargeCardTags> page = new Page<>(pageNo, pageSize);
        rechargeCardTagsService.page(page, Wrappers.<RechargeCardTags>lambdaQuery(queryBean));
        return R.ofSuccess(page);
    }
    
    
    
    @ApiOperation(value="id删除数据")
    @DeleteMapping("deleteById")
    public R<String> deleteById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        boolean flag = rechargeCardTagsService.removeById(id);
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="批量删除")
    @DeleteMapping("batchDeleteByIds")
    public R<String> batchDeleteByIds(String[] ids) {
        ExceptionCheckUtil.notEmpty(ids, "批量删除的IDs 不能为空");
        
        boolean flag = rechargeCardTagsService.removeByIds(Arrays.asList(ids));
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="插入一条新数据")
    @PostMapping("addRecord")
    public R<String> addRecord(@Validated(value= {FormValid.class}) @RequestBody RechargeCardTags rechargeCardTags, BindingResult bindingResult) {
        if (StringUtils.isEmpty(rechargeCardTags.getId()))
            rechargeCardTags.setId(null);
        
        boolean flag = rechargeCardTagsService.save(rechargeCardTags);
        return R.ofSuccess(flag? "添加成功，ID:" + rechargeCardTags.getId() : "添加失败");
    }
    
    
    
    @ApiOperation(value="ID更新数据")
    @PostMapping("updateById")
    public R<String> updateById(@Validated(value= {FormValid.class}) @RequestBody RechargeCardTags updateBean, BindingResult bindingResult) {
        ExceptionCheckUtil.hasLength(updateBean.getId(), "ID 不能为空");
        
        boolean flag = rechargeCardTagsService.updateById(updateBean);
        return R.ofSuccess(flag? "更新成功" : "更新失败");
    }
}