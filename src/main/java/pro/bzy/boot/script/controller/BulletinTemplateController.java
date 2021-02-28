package pro.bzy.boot.script.controller;

import pro.bzy.boot.script.domain.entity.BulletinTemplate;
import pro.bzy.boot.script.mapper.BulletinTemplateMapper;
import pro.bzy.boot.script.service.BulletinTemplateService;

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
 * @since 2020-12-14
 */
@Api(tags = {"通知公告模板"}, value="通知公告模板")
@ApiSupport(order = 100)
@RequestMapping("/script/bulletinTemplate")
@RestController
public class BulletinTemplateController {

	@Resource
    private BulletinTemplateMapper bulletinTemplateMapper;
    
    @Resource
    private BulletinTemplateService bulletinTemplateService;
    
    
    
    @ApiOperation(value="id查询")
    @GetMapping("getById")
    public R<BulletinTemplate> getById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        return R.ofSuccess(bulletinTemplateService.getById(id));
    }

    
    
    @ApiOperation(value="查询数据列表")
    @GetMapping("getList")
    public R<List<BulletinTemplate>> getList(BulletinTemplate queryBean) {
        List<BulletinTemplate> bulletinTemplates = bulletinTemplateService.list();
        return R.ofSuccess(bulletinTemplates);
    }
    
    
    
    @ApiOperation(value="查询数据分页")
    @GetMapping("getPage")
    public R<Page<BulletinTemplate>> getPage(int pageNo, int pageSize, BulletinTemplate queryBean) {
        Page<BulletinTemplate> page = new Page<>(pageNo, pageSize);
        bulletinTemplateService.page(page, Wrappers.<BulletinTemplate>lambdaQuery(queryBean));
        return R.ofSuccess(page);
    }
    
    
    
    @ApiOperation(value="id删除数据")
    @DeleteMapping("deleteById")
    public R<String> deleteById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        boolean flag = bulletinTemplateService.removeById(id);
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="批量删除")
    @DeleteMapping("batchDeleteByIds")
    public R<String> batchDeleteByIds(String[] ids) {
        ExceptionCheckUtil.notEmpty(ids, "批量删除的IDs 不能为空");
        
        boolean flag = bulletinTemplateService.removeByIds(Arrays.asList(ids));
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="插入一条新数据")
    @PostMapping("addRecord")
    public R<Object> addRecord(@Validated(value= {FormValid.class}) @RequestBody BulletinTemplate bulletinTemplate, BindingResult bindingResult) {
        if (StringUtils.isEmpty(bulletinTemplate.getId()))
            bulletinTemplate.setId(null);
        
        bulletinTemplateService.save(bulletinTemplate);
        return R.ofSuccess(bulletinTemplate.getId());
    }
    
    
    
    @ApiOperation(value="ID更新数据")
    @PostMapping("updateById")
    public R<String> updateById(@Validated(value= {FormValid.class}) @RequestBody BulletinTemplate updateBean, BindingResult bindingResult) {
        ExceptionCheckUtil.hasLength(updateBean.getId(), "ID 不能为空");
        
        boolean flag = bulletinTemplateService.updateById(updateBean);
        return R.ofSuccess(flag? "更新成功" : "更新失败");
    }
}