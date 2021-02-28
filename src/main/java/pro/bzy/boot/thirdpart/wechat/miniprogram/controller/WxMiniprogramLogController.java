package pro.bzy.boot.thirdpart.wechat.miniprogram.controller;

import pro.bzy.boot.framework.utils.ExceptionCheckUtil;
import pro.bzy.boot.framework.web.domain.bean.R;
import pro.bzy.boot.thirdpart.wechat.miniprogram.domain.entity.WxMiniprogramLog;
import pro.bzy.boot.thirdpart.wechat.miniprogram.mapper.WxMiniprogramLogMapper;
import pro.bzy.boot.thirdpart.wechat.miniprogram.service.WxMiniprogramLogService;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

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
 * @since 2020-11-02
 */
@Api(tags = {"微信小程序日志"}, value="微信小程序日志")
@ApiSupport(order = 100)
@RequestMapping("/wx/wxMiniprogramLog")
@RestController
public class WxMiniprogramLogController {

	@Resource
    private WxMiniprogramLogMapper wxMiniprogramLogMapper;
    
    @Resource
    private WxMiniprogramLogService wxMiniprogramLogService;
    
    
    
    /**
     * 使用id查询数据
     * @param id
     * @return
     */
    @GetMapping("getById")
    public R<WxMiniprogramLog> getById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        return R.ofSuccess(wxMiniprogramLogService.getById(id));
    }

    
    
    /**
     * 查询数据列表
     */
    @GetMapping("getList")
    public R<List<WxMiniprogramLog>> getList(WxMiniprogramLog queryBean) {
        List<WxMiniprogramLog> wxMiniprogramLogs = wxMiniprogramLogService.list(Wrappers.<WxMiniprogramLog>lambdaQuery(queryBean));
        return R.ofSuccess(wxMiniprogramLogs);
    }
    
    
    
    /**
     * 查询数据分页
     */
    @GetMapping("getPage")
    public R<Page<WxMiniprogramLog>> getPage(int pageNo, int pageSize, WxMiniprogramLog queryBean) {
        Page<WxMiniprogramLog> page = new Page<>(pageNo, pageSize);
        wxMiniprogramLogService.page(page, Wrappers.<WxMiniprogramLog>lambdaQuery(queryBean));
        return R.ofSuccess(page);
    }
    
    
    
    /**
     * 根据id删除数据
     */
    @DeleteMapping("deleteById")
    public R<String> deleteById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        boolean flag = wxMiniprogramLogService.removeById(id);
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    /**
     * 批量删除 根据id数组
     */
    @DeleteMapping("batchDeleteByIds")
    public R<String> batchDeleteByIds(String[] ids) {
        ExceptionCheckUtil.notEmpty(ids, "批量删除的IDs 不能为空");
        
        boolean flag = wxMiniprogramLogService.removeByIds(Arrays.asList(ids));
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    /**
     * 插入一条新数据
     * @param wxMiniprogramLog 数据
     * @param bindingResult 表单校验结果
     * @return
     */
    @PostMapping("addRecord")
    public R<String> addRecord(@Validated(value= {}) @RequestBody WxMiniprogramLog wxMiniprogramLog, BindingResult bindingResult) {
        if (StringUtils.isEmpty(wxMiniprogramLog.getId()))
            wxMiniprogramLog.setId(null);
        
        boolean flag = wxMiniprogramLogService.save(wxMiniprogramLog);
        return R.ofSuccess(flag? "添加成功，ID:" + wxMiniprogramLog.getId() : "添加失败");
    }
    
    
    
    /**
     * 更新数据
     * @param updateBean 数据
     * @param bindingResult 表单校验结果
     * @return
     */
    @PostMapping("updateById")
    public R<String> updateById(@Validated(value= {}) @RequestBody WxMiniprogramLog updateBean, BindingResult bindingResult) {
        ExceptionCheckUtil.hasLength(updateBean.getId(), "ID 不能为空");
        
        boolean flag = wxMiniprogramLogService.updateById(updateBean);
        return R.ofSuccess(flag? "更新成功" : "更新失败");
    }
}