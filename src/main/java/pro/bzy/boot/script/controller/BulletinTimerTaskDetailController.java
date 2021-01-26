package pro.bzy.boot.script.controller;

import pro.bzy.boot.script.domain.entity.BulletinTimerTaskDetail;
import pro.bzy.boot.script.mapper.BulletinTimerTaskDetailMapper;
import pro.bzy.boot.script.service.BulletinTimerTaskDetailService;
import pro.bzy.boot.script.utils.ScriptConstant;
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
 * VIEW 前端控制器
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/controller.java.ftl
 * @author zhenyuan.bi
 * @since 2021-01-25
 */
@Api(tags = {"剧本定时器-视图"})
@ApiSupport(order = 100)
@RequestMapping("/script/bulletinTimerTaskDetail")
@RestController
public class BulletinTimerTaskDetailController {

	@Resource
    private BulletinTimerTaskDetailMapper bulletinTimerTaskDetailMapper;
    
    @Resource
    private BulletinTimerTaskDetailService bulletinTimerTaskDetailService;
    
    
    
    @ApiOperation(value="id查询")
    @GetMapping("getById")
    public R<BulletinTimerTaskDetail> getById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        return R.ofSuccess(bulletinTimerTaskDetailService.getById(id));
    }

    
    
    @ApiOperation(value="查询数据列表")
    @GetMapping("getList")
    public R<List<BulletinTimerTaskDetail>> getList(BulletinTimerTaskDetail queryBean) {
        List<BulletinTimerTaskDetail> bulletinTimerTaskDetails = bulletinTimerTaskDetailService.list(Wrappers.<BulletinTimerTaskDetail>lambdaQuery(queryBean));
        return R.ofSuccess(bulletinTimerTaskDetails);
    }
    
    
    
    @ApiOperation(value="查询数据分页")
    @GetMapping("getPage")
    public R<Page<BulletinTimerTaskDetail>> getPage(int pageNo, int pageSize, BulletinTimerTaskDetail queryBean) {
        Page<BulletinTimerTaskDetail> page = new Page<>(pageNo, pageSize);
        bulletinTimerTaskDetailService.page(page, Wrappers.<BulletinTimerTaskDetail>lambdaQuery(queryBean));
        return R.ofSuccess(page);
    }
    
    

    @ApiOperation(value="查询延迟推送数据分页")
    @GetMapping("getDelayPage")
    public R<Page<BulletinTimerTaskDetail>> getDelayPage(int pageNo, int pageSize, BulletinTimerTaskDetail queryBean) {
        Page<BulletinTimerTaskDetail> page = new Page<>(pageNo, pageSize);
        bulletinTimerTaskDetailService.page(page, 
                Wrappers.<BulletinTimerTaskDetail>lambdaQuery(queryBean)
                .eq(BulletinTimerTaskDetail::getSendMode, ScriptConstant.BULLETIN_SEND_MODE_DELAY)
                .orderByAsc(BulletinTimerTaskDetail::getSendMode)
                .orderByDesc(BulletinTimerTaskDetail::getSendTime));
        return R.ofSuccess(page);
    }
    
    
    @ApiOperation(value="查询周期推送数据分页")
    @GetMapping("getPeriodPage")
    public R<Page<BulletinTimerTaskDetail>> getPeriodPage(int pageNo, int pageSize, BulletinTimerTaskDetail queryBean) {
        return R.ofSuccess(bulletinTimerTaskDetailService.getPeriodPage(pageNo, pageSize, queryBean));
    }
    
    
    
    
    @ApiOperation(value="id删除数据")
    @DeleteMapping("deleteById")
    public R<String> deleteById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        boolean flag = bulletinTimerTaskDetailService.removeById(id);
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="批量删除")
    @DeleteMapping("batchDeleteByIds")
    public R<String> batchDeleteByIds(String[] ids) {
        ExceptionCheckUtil.notEmpty(ids, "批量删除的IDs 不能为空");
        
        boolean flag = bulletinTimerTaskDetailService.removeByIds(Arrays.asList(ids));
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="插入一条新数据")
    @PostMapping("addRecord")
    public R<String> addRecord(@Validated(value= {FormValid.class}) @RequestBody BulletinTimerTaskDetail bulletinTimerTaskDetail, BindingResult bindingResult) {
        if (StringUtils.isEmpty(bulletinTimerTaskDetail.getId()))
            bulletinTimerTaskDetail.setId(null);
        
        boolean flag = bulletinTimerTaskDetailService.save(bulletinTimerTaskDetail);
        return R.ofSuccess(flag? "添加成功，ID:" + bulletinTimerTaskDetail.getId() : "添加失败");
    }
    
    
    
    @ApiOperation(value="ID更新数据")
    @PostMapping("updateById")
    public R<String> updateById(@Validated(value= {FormValid.class}) @RequestBody BulletinTimerTaskDetail updateBean, BindingResult bindingResult) {
        ExceptionCheckUtil.hasLength(updateBean.getId(), "ID 不能为空");
        
        boolean flag = bulletinTimerTaskDetailService.updateById(updateBean);
        return R.ofSuccess(flag? "更新成功" : "更新失败");
    }
}