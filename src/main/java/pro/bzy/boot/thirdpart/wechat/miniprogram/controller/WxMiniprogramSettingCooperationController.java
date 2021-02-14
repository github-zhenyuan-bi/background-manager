package pro.bzy.boot.thirdpart.wechat.miniprogram.controller;

import pro.bzy.boot.thirdpart.wechat.miniprogram.domain.entity.WxMiniprogramSettingCooperation;
import pro.bzy.boot.thirdpart.wechat.miniprogram.mapper.WxMiniprogramSettingCooperationMapper;
import pro.bzy.boot.thirdpart.wechat.miniprogram.service.WxMiniprogramSettingCooperationService;

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
import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;


/**
 *  前端控制器
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/controller.java.ftl
 * @author zhenyuan.bi
 * @since 2021-01-19
 */
@Api(tags = {""})
@ApiSupport(order = 100)
@RequestMapping("/wx/wxMiniprogramSettingCooperation")
@RestController
public class WxMiniprogramSettingCooperationController {

	@Resource
    private WxMiniprogramSettingCooperationMapper wxMiniprogramSettingCooperationMapper;
    
    @Resource
    private WxMiniprogramSettingCooperationService wxMiniprogramSettingCooperationService;
    
    
    
    @ApiOperation(value="id查询")
    @GetMapping("getById")
    public R<WxMiniprogramSettingCooperation> getById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        return R.ofSuccess(wxMiniprogramSettingCooperationService.getById(id));
    }

    
    
    @ApiOperation(value="查询数据列表")
    @GetMapping("getList")
    public R<List<WxMiniprogramSettingCooperation>> getList(WxMiniprogramSettingCooperation queryBean) {
        List<WxMiniprogramSettingCooperation> wxMiniprogramSettingCooperations = wxMiniprogramSettingCooperationService.list(Wrappers.<WxMiniprogramSettingCooperation>lambdaQuery(queryBean));
        return R.ofSuccess(wxMiniprogramSettingCooperations);
    }
    
    
    
    @ApiOperation(value="查询数据分页")
    @GetMapping("getPage")
    public R<Page<WxMiniprogramSettingCooperation>> getPage(int pageNo, int pageSize, WxMiniprogramSettingCooperation queryBean) {
        Page<WxMiniprogramSettingCooperation> page = new Page<>(pageNo, pageSize);
        wxMiniprogramSettingCooperationService.page(page, Wrappers.<WxMiniprogramSettingCooperation>lambdaQuery(queryBean));
        return R.ofSuccess(page);
    }
    
    
    
    @ApiOperation(value="id删除数据")
    @DeleteMapping("deleteById")
    public R<String> deleteById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        boolean flag = wxMiniprogramSettingCooperationService.removeById(id);
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="批量删除")
    @DeleteMapping("batchDeleteByIds")
    public R<String> batchDeleteByIds(String[] ids) {
        ExceptionCheckUtil.notEmpty(ids, "批量删除的IDs 不能为空");
        
        boolean flag = wxMiniprogramSettingCooperationService.removeByIds(Arrays.asList(ids));
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="插入一条新数据")
    @PostMapping("addRecord")
    public R<String> addRecord(String ctKeys, String ctValues, String isSubtitles,
            @Validated(value= {FormValid.class}) WxMiniprogramSettingCooperation coop, BindingResult bindingResult) {
        if (StringUtils.isEmpty(coop.getId()))
            coop.setId(null);
        
        wxMiniprogramSettingCooperationService.addCoop(coop, ctKeys, ctValues, isSubtitles);
        return R.ofSuccess("新增合作模块成功");
    }
    
    
    
    @ApiOperation(value="ID更新数据")
    @PostMapping("updateById")
    public R<String> updateById(String ctKeys, String ctValues, String isSubtitles,
            @Validated(value= {FormValid.class}) WxMiniprogramSettingCooperation coop, BindingResult bindingResult) {
        ExceptionCheckUtil.hasLength(coop.getId(), "ID 不能为空");
        
        wxMiniprogramSettingCooperationService.updateCoop(coop, ctKeys, ctValues, isSubtitles);
        return R.ofSuccess("更新成功");
    }
}