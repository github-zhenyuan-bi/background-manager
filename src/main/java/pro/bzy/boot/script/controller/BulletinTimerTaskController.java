package pro.bzy.boot.script.controller;

import pro.bzy.boot.script.domain.entity.BulletinTimerTask;
import pro.bzy.boot.script.mapper.BulletinTimerTaskMapper;
import pro.bzy.boot.script.service.BulletinTimerTaskService;

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
@Api(tags = {""})
@ApiSupport(order = 100)
@RequestMapping("/script/bulletinTimerTask")
@RestController
public class BulletinTimerTaskController {

	@Resource
    private BulletinTimerTaskMapper bulletinTimerTaskMapper;
    
    @Resource
    private BulletinTimerTaskService bulletinTimerTaskService;
    
    
    
    @ApiOperation(value="id查询")
    @GetMapping("getById")
    public R<BulletinTimerTask> getById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        return R.ofSuccess(bulletinTimerTaskService.getById(id));
    }

    
    
    @ApiOperation(value="查询数据列表")
    @GetMapping("getList")
    public R<List<BulletinTimerTask>> getList(BulletinTimerTask queryBean) {
        List<BulletinTimerTask> bulletinTimerTasks = bulletinTimerTaskService.list(Wrappers.<BulletinTimerTask>lambdaQuery(queryBean));
        return R.ofSuccess(bulletinTimerTasks);
    }
    
    
    
    @ApiOperation(value="查询数据分页")
    @GetMapping("getPage")
    public R<Page<BulletinTimerTask>> getPage(int pageNo, int pageSize, BulletinTimerTask queryBean) {
        Page<BulletinTimerTask> page = new Page<>(pageNo, pageSize);
        bulletinTimerTaskService.page(page, Wrappers.<BulletinTimerTask>lambdaQuery(queryBean));
        return R.ofSuccess(page);
    }
    
    
    
    @ApiOperation(value="id删除数据")
    @DeleteMapping("deleteById")
    public R<String> deleteById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        boolean flag = bulletinTimerTaskService.removeById(id);
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="批量删除")
    @DeleteMapping("batchDeleteByIds")
    public R<String> batchDeleteByIds(String[] ids) {
        ExceptionCheckUtil.notEmpty(ids, "批量删除的IDs 不能为空");
        
        boolean flag = bulletinTimerTaskService.removeByIds(Arrays.asList(ids));
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="插入一条新数据")
    @PostMapping("addRecord")
    public R<String> addRecord(@Validated(value= {FormValid.class}) @RequestBody BulletinTimerTask bulletinTimerTask, BindingResult bindingResult) {
        if (StringUtils.isEmpty(bulletinTimerTask.getId()))
            bulletinTimerTask.setId(null);
        
        boolean flag = bulletinTimerTaskService.save(bulletinTimerTask);
        return R.ofSuccess(flag? "添加成功，ID:" + bulletinTimerTask.getId() : "添加失败");
    }
    
    
    
    @ApiOperation(value="ID更新数据")
    @PostMapping("updateById")
    public R<String> updateById(@Validated(value= {FormValid.class}) @RequestBody BulletinTimerTask updateBean, BindingResult bindingResult) {
        ExceptionCheckUtil.hasLength(updateBean.getId(), "ID 不能为空");
        
        boolean flag = bulletinTimerTaskService.updateById(updateBean);
        return R.ofSuccess(flag? "更新成功" : "更新失败");
    }
}