package pro.bzy.boot.thirdpart.wechat.miniprogram.controller;

import pro.bzy.boot.thirdpart.wechat.miniprogram.domain.entity.WxMiniprogramSettingIndeximg;
import pro.bzy.boot.thirdpart.wechat.miniprogram.mapper.WxMiniprogramSettingIndeximgMapper;
import pro.bzy.boot.thirdpart.wechat.miniprogram.service.WxMiniprogramSettingIndeximgService;

import pro.bzy.boot.framework.web.domain.bean.R;
import pro.bzy.boot.framework.web.annoations.FormValid;
import pro.bzy.boot.framework.utils.ExceptionCheckUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
 * @since 2021-01-19
 */
@Api(tags = {"微信首页轮播图"}, value="微信首页轮播图")
@ApiSupport(order = 100)
@RequestMapping("/wx/wxMiniprogramSettingIndeximg")
@RestController
public class WxMiniprogramSettingIndeximgController {

	@Resource
    private WxMiniprogramSettingIndeximgMapper wxMiniprogramSettingIndeximgMapper;
    
    @Resource
    private WxMiniprogramSettingIndeximgService wxMiniprogramSettingIndeximgService;
    
    
    
    @ApiOperation(value="id查询")
    @GetMapping("getById")
    public R<WxMiniprogramSettingIndeximg> getById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        return R.ofSuccess(wxMiniprogramSettingIndeximgService.getById(id));
    }

    
    
    @ApiOperation(value="查询数据列表")
    @GetMapping("getList")
    public R<List<WxMiniprogramSettingIndeximg>> getList(WxMiniprogramSettingIndeximg queryBean) {
        List<WxMiniprogramSettingIndeximg> wxMiniprogramSettingIndeximgs = wxMiniprogramSettingIndeximgService.list(
                Wrappers.<WxMiniprogramSettingIndeximg>lambdaQuery(queryBean)
                    .orderByAsc(WxMiniprogramSettingIndeximg::getSort));
        return R.ofSuccess(wxMiniprogramSettingIndeximgs);
    }
    
    
    
    @ApiOperation(value="查询数据分页")
    @GetMapping("getPage")
    public R<Page<WxMiniprogramSettingIndeximg>> getPage(int pageNo, int pageSize, WxMiniprogramSettingIndeximg queryBean) {
        Page<WxMiniprogramSettingIndeximg> page = new Page<>(pageNo, pageSize);
        wxMiniprogramSettingIndeximgService.page(page, Wrappers.<WxMiniprogramSettingIndeximg>lambdaQuery(queryBean));
        return R.ofSuccess(page);
    }
    
    
    
    @ApiOperation(value="id删除数据")
    @DeleteMapping("deleteById")
    public R<String> deleteById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        boolean flag = wxMiniprogramSettingIndeximgService.removeById(id);
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="批量删除")
    @DeleteMapping("batchDeleteByIds")
    public R<String> batchDeleteByIds(String[] ids) {
        ExceptionCheckUtil.notEmpty(ids, "批量删除的IDs 不能为空");
        
        boolean flag = wxMiniprogramSettingIndeximgService.removeByIds(Arrays.asList(ids));
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    @ApiOperation(value="插入一条新数据")
    @PostMapping("addRecord")
    public R<String> addRecord(@Validated(value= {FormValid.class}) @RequestBody WxMiniprogramSettingIndeximg wxMiniprogramSettingIndeximg, BindingResult bindingResult) {
        if (StringUtils.isEmpty(wxMiniprogramSettingIndeximg.getId()))
            wxMiniprogramSettingIndeximg.setId(null);
        
        boolean flag = wxMiniprogramSettingIndeximgService.save(wxMiniprogramSettingIndeximg);
        return R.ofSuccess(flag? "添加成功，ID:" + wxMiniprogramSettingIndeximg.getId() : "添加失败");
    }
    
    
    
    @ApiOperation(value="ID更新数据")
    @PostMapping("updateById")
    public R<String> updateById(@Validated(value= {FormValid.class}) @RequestBody WxMiniprogramSettingIndeximg updateBean, BindingResult bindingResult) {
        ExceptionCheckUtil.hasLength(updateBean.getId(), "ID 不能为空");
        
        boolean flag = wxMiniprogramSettingIndeximgService.updateById(updateBean);
        return R.ofSuccess(flag? "更新成功" : "更新失败");
    }
    
    
    
    
    @ApiOperation(value="新增一个首页轮播图")
    @PostMapping("saveAfterUpload")
    public R<Object> saveAfterUpload(HttpServletRequest request) {
        Object data = request.getAttribute("uploadedImg");
        WxMiniprogramSettingIndeximg savedBean;
        if (Objects.nonNull(data))
            savedBean = wxMiniprogramSettingIndeximgService.addIndexSwipperImg(data.toString());
        else
            throw new RuntimeException("获取轮播图片上传地址失败， 无法保存入库");
        return R.ofSuccess(savedBean);
    }
    
    
    
    @ApiOperation(value="调整图像顺序")
    @PostMapping("adjustImageSort")
    public R<Object> adjustImageSort(int fromIndex, int toIndex) {
        wxMiniprogramSettingIndeximgService.adjustImageSort(fromIndex, toIndex);
        return R.ofSuccess("顺序调整成功");
    }
}