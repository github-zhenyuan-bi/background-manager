package pro.bzy.boot.framework.web.controller;

import pro.bzy.boot.framework.web.domain.entity.TimerTask;
import pro.bzy.boot.framework.web.mapper.TimerTaskMapper;
import pro.bzy.boot.framework.web.service.TimerTaskService;

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
 * 定时器任务 前端控制器
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/controller.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-01
 */
@Api(tags = {"定时器任务"}, value="定时器任务")
@ApiSupport(order = 100)
@RequestMapping("/framework/timerTask")
@RestController
public class TimerTaskController {

	@Resource
    private TimerTaskMapper timerTaskMapper;
    
    @Resource
    private TimerTaskService timerTaskService;
    
    
    
    @ApiOperation(value="id查询常量")
    @GetMapping("getById")
    public R<TimerTask> getById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        return R.ofSuccess(timerTaskService.getById(id));
    }

    
    
    @ApiOperation(value="列表数据")
    @GetMapping("getList")
    public R<List<TimerTask>> getList(TimerTask queryBean) {
        List<TimerTask> timerTasks = timerTaskService.list(Wrappers.<TimerTask>lambdaQuery(queryBean));
        return R.ofSuccess(timerTasks);
    }
    
    
    
    @ApiOperation(value="分页数据")
    @GetMapping("getPage")
    public R<Page<TimerTask>> getPage(int pageNo, int pageSize, TimerTask queryBean) {
        Page<TimerTask> page = new Page<>(pageNo, pageSize);
        timerTaskService.page(page, Wrappers.<TimerTask>lambdaQuery(queryBean));
        return R.ofSuccess(page);
    }
    
    
    
    @ApiOperation(value="ID删除")
    @DeleteMapping("deleteById")
    public R<String> deleteById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        boolean flag = timerTaskService.removeById(id);
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="IDs批量删除")
    @DeleteMapping("batchDeleteByIds")
    public R<String> batchDeleteByIds(String[] ids) {
        ExceptionCheckUtil.notEmpty(ids, "批量删除的IDs 不能为空");
        
        boolean flag = timerTaskService.removeByIds(Arrays.asList(ids));
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="增加定时任务")
    @PostMapping("addRecord")
    public R<String> addRecord(@Validated(value= {}) @RequestBody TimerTask timerTask, BindingResult bindingResult) {
        if (StringUtils.isEmpty(timerTask.getId()))
            timerTask.setId(null);
        
        timerTaskService.addTimertask(timerTask);
        return R.ofSuccess("添加成功，名称:" + timerTask.getName());
    }
    
    
    
    @ApiOperation(value="ID更新")
    @PostMapping("updateById")
    public R<String> updateById(@Validated(value= {}) @RequestBody TimerTask updateBean, BindingResult bindingResult) {
        ExceptionCheckUtil.hasLength(updateBean.getId(), "ID 不能为空");
        
        timerTaskService.updateTimertask(updateBean);
        return R.ofSuccess("更新成功");
    }
    
    
    @ApiOperation(value="任务挂起")
    @PostMapping("startupOrShutdownTask")
    public R<String> startupOrShutdownTask(String id, Integer enable) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        ExceptionCheckUtil.notNull(enable, "启用状态不能为空");
        
        timerTaskService.updateForSuspendTimertask(
                TimerTask.builder().id(id).enable(enable).build());
        return R.ofSuccess("成功");
    }
}