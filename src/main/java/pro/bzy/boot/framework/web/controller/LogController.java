package pro.bzy.boot.framework.web.controller;

import pro.bzy.boot.framework.web.domain.entity.Log;
import pro.bzy.boot.framework.web.mapper.LogMapper;
import pro.bzy.boot.framework.web.service.LogService;

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
import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;


/**
 * 日志记录 前端控制器
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/controller.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-01
 */
@Api(tags = {"日志记录"}, value="日志数据")
@ApiSupport(order = 100)
@RequestMapping("/framework/log")
@RestController
public class LogController {

	@Resource
    private LogMapper logMapper;
    
    @Resource
    private LogService logService;
    
    
    
    @ApiOperation(value="ID查询")
    @GetMapping("getById")
    public R<Log> getById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        return R.ofSuccess(logService.getById(id));
    }

    
    
    @ApiOperation(value="日志列表")
    @GetMapping("getList")
    public R<List<Log>> getList(Log queryBean) {
        List<Log> logs = logService.list(Wrappers.<Log>lambdaQuery(queryBean).orderByDesc(Log::getAccessTime));
        return R.ofSuccess(logs);
    }
    
    
    
    @ApiOperation(value="日志分页")
    @GetMapping("getPage")
    public R<Page<Log>> getPage(int pageNo, int pageSize, Log queryBean) {
        Page<Log> page = new Page<>(pageNo, pageSize);
        logService.page(page, Wrappers.<Log>lambdaQuery(queryBean).orderByDesc(Log::getAccessTime));
        return R.ofSuccess(page);
    }
    
    
    
    @ApiOperation(value="ID删除")
    @DeleteMapping("deleteById")
    public R<String> deleteById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        boolean flag = logService.removeById(id);
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="IDs批量删除")
    @DeleteMapping("batchDeleteByIds")
    public R<String> batchDeleteByIds(String[] ids) {
        ExceptionCheckUtil.notEmpty(ids, "批量删除的IDs 不能为空");
        
        boolean flag = logService.removeByIds(Arrays.asList(ids));
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="增加日志")
    @PostMapping("addRecord")
    public R<String> addRecord(@Validated(value= {}) Log log, BindingResult bindingResult) {
        if (StringUtils.isEmpty(log.getId()))
            log.setId(null);
        
        boolean flag = logService.save(log);
        return R.ofSuccess(flag? "添加成功，ID:" + log.getId() : "添加失败");
    }
    
    
    
    @ApiOperation(value="ID更新")
    @PostMapping("updateById")
    public R<String> updateById(@Validated(value= {}) Log updateBean, BindingResult bindingResult) {
        ExceptionCheckUtil.hasLength(updateBean.getId(), "ID 不能为空");
        
        boolean flag = logService.updateById(updateBean);
        return R.ofSuccess(flag? "更新成功" : "更新失败");
    }
}