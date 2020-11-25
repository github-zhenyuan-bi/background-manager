package pro.bzy.boot.thirdpart.wechat.wx.controller;

import pro.bzy.boot.framework.utils.ExceptionCheckUtil;
import pro.bzy.boot.framework.web.domain.bean.R;
import pro.bzy.boot.thirdpart.wechat.wx.domain.entity.WxUserinfo;
import pro.bzy.boot.thirdpart.wechat.wx.mapper.WxUserinfoMapper;
import pro.bzy.boot.thirdpart.wechat.wx.service.WxUserinfoService;


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
@Api(tags = {""})
@ApiSupport(order = 100)
@RequestMapping("/wx/wxUserinfo")
@RestController
public class WxUserinfoController {

	@Resource
    private WxUserinfoMapper wxUserinfoMapper;
    
    @Resource
    private WxUserinfoService wxUserinfoService;
    
    
    
    /**
     * 使用id查询数据
     * @param id
     * @return
     */
    @GetMapping("getById")
    public R<WxUserinfo> getById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        return R.ofSuccess(wxUserinfoService.getById(id));
    }

    
    
    /**
     * 查询数据列表
     */
    @GetMapping("getList")
    public R<List<WxUserinfo>> getList(WxUserinfo queryBean) {
        List<WxUserinfo> wxUserinfos = wxUserinfoService.list(Wrappers.<WxUserinfo>lambdaQuery(queryBean));
        return R.ofSuccess(wxUserinfos);
    }
    
    
    
    /**
     * 查询数据分页
     */
    @GetMapping("getPage")
    public R<Page<WxUserinfo>> getPage(int pageNo, int pageSize, WxUserinfo queryBean) {
        Page<WxUserinfo> page = new Page<>(pageNo, pageSize);
        wxUserinfoService.page(page, Wrappers.<WxUserinfo>lambdaQuery(queryBean));
        return R.ofSuccess(page);
    }
    
    
    
    /**
     * 根据id删除数据
     */
    @DeleteMapping("deleteById")
    public R<String> deleteById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        boolean flag = wxUserinfoService.removeById(id);
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    /**
     * 批量删除 根据id数组
     */
    @DeleteMapping("batchDeleteByIds")
    public R<String> batchDeleteByIds(String[] ids) {
        ExceptionCheckUtil.notEmpty(ids, "批量删除的IDs 不能为空");
        
        boolean flag = wxUserinfoService.removeByIds(Arrays.asList(ids));
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    /**
     * 插入一条新数据
     * @param wxUserinfo 数据
     * @param bindingResult 表单校验结果
     * @return
     */
    @PostMapping("addRecord")
    public R<String> addRecord(@Validated(value= {}) @RequestBody WxUserinfo wxUserinfo, BindingResult bindingResult) {
        if (StringUtils.isEmpty(wxUserinfo.getId()))
            wxUserinfo.setId(null);
        
        boolean flag = wxUserinfoService.save(wxUserinfo);
        return R.ofSuccess(flag? "添加成功，ID:" + wxUserinfo.getId() : "添加失败");
    }
    
    
    
    /**
     * 更新数据
     * @param updateBean 数据
     * @param bindingResult 表单校验结果
     * @return
     */
    @PostMapping("updateById")
    public R<String> updateById(@Validated(value= {}) @RequestBody WxUserinfo updateBean, BindingResult bindingResult) {
        ExceptionCheckUtil.hasLength(updateBean.getId(), "ID 不能为空");
        
        boolean flag = wxUserinfoService.updateById(updateBean);
        return R.ofSuccess(flag? "更新成功" : "更新失败");
    }
}