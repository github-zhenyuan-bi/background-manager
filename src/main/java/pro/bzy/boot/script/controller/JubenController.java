package pro.bzy.boot.script.controller;

import pro.bzy.boot.script.domain.entity.Juben;
import pro.bzy.boot.script.domain.entity.JubenTag;
import pro.bzy.boot.script.mapper.JubenMapper;
import pro.bzy.boot.script.service.JubenService;
import pro.bzy.boot.script.service.JubenTagService;
import pro.bzy.boot.script.service.WxJubenBrowseRecordService;
import pro.bzy.boot.framework.web.domain.bean.R;
import pro.bzy.boot.framework.utils.ExceptionCheckUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;


/**
 * 剧本 前端控制器
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/controller.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-08
 */
@Api(tags = {"剧本"}, value="剧本")
@ApiSupport(order = 100)
@RequestMapping("/script/juben")
@RestController
public class JubenController {

	@Resource
    private JubenMapper jubenMapper;
    
    @Resource
    private JubenService jubenService;
    @Resource
    private JubenTagService jubenTagService;
    @Resource
    private WxJubenBrowseRecordService wxJubenBrowseRecordService;
    
    /**
     * 使用id查询数据
     * @param id
     * @return
     */
    @GetMapping("getById")
    public R<Juben> getById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        return R.ofSuccess(jubenService.getById(id));
    }

    
    
    /**
     * 查询数据列表
     */
    @GetMapping("getList")
    public R<List<Juben>> getList(Juben queryBean) {
        List<Juben> jubens = jubenService.list(Wrappers.<Juben>lambdaQuery(queryBean));
        return R.ofSuccess(jubens);
    }
    
    
    
    /**
     * 查询数据分页
     */
    @GetMapping("getPage")
    public R<Page<Juben>> getPage(int pageNo, int pageSize, Juben queryBean) {
        Page<Juben> page = new Page<>(pageNo, pageSize);
        jubenService.page(page, Wrappers.<Juben>lambdaQuery(queryBean));
        return R.ofSuccess(page);
    }
    
    
    
    /**
     * 根据id删除数据
     */
    @DeleteMapping("deleteById")
    public R<String> deleteById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        boolean flag = jubenService.removeById(id);
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    /**
     * 批量删除 根据id数组
     */
    @DeleteMapping("batchDeleteByIds")
    public R<String> batchDeleteByIds(String[] ids) {
        ExceptionCheckUtil.notEmpty(ids, "批量删除的IDs 不能为空");
        
        boolean flag = jubenService.removeByIds(Arrays.asList(ids));
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    /**
     * 插入一条新数据
     * @param juben 数据
     * @param bindingResult 表单校验结果
     * @return
     */
    @PostMapping("addRecord")
    public R<Juben> addRecord(@Validated(value= {}) @RequestBody Juben juben, BindingResult bindingResult) {
        if (StringUtils.isEmpty(juben.getId()))
            juben.setId(null);
        
        jubenService.addJuben(juben);
        return R.ofSuccess(juben);
    }
    
    
    
    /**
     * 更新数据
     * @param updateBean 数据
     * @param bindingResult 表单校验结果
     * @return
     */
    @PostMapping("updateById")
    public R<String> updateById(@Validated(value= {}) @RequestBody Juben updateBean, BindingResult bindingResult) {
        ExceptionCheckUtil.hasLength(updateBean.getId(), "ID 不能为空");
        
        boolean flag = jubenService.updateById(updateBean);
        return R.ofSuccess(flag? "更新成功" : "更新失败");
    }
    
    
    
    /**
     * 重新绑定剧本和标签的关联关系
     * @param jubenId 剧本ID
     * @param jubenTags 关联关系对象
     * @return
     */
    @PostMapping("rebindTagRelationship/{jubenId}")
    public R<String> rebindTagRelationship(@PathVariable("jubenId") String jubenId,
            @RequestBody List<JubenTag> jubenTags) {
        jubenTagService.updateTagRelationship(jubenId, jubenTags);
        return R.ofSuccess("更新成功");
    }
    
    
    
    
    
    @ApiOperation(value="开放接口-查询热门剧本")
    @GetMapping("public/getHotJubens")
    public R<Page<Map<String, Object>>> getHotJubens(int pageNo, int pageSize, Juben queryBean) {
        return R.ofSuccess(wxJubenBrowseRecordService.listTopBrowseJubens(pageNo, pageSize));
    }
    
    
    @ApiOperation(value="开放接口-查询上新剧本")
    @GetMapping("public/getNewJubens")
    public R<Page<Juben>> getNewJubens(int pageNo, int pageSize, Juben queryBean) {
        Page<Juben> page = new Page<>(pageNo, pageSize);
        jubenService.page(page, Wrappers.<Juben>lambdaQuery(queryBean)
                .orderByDesc(Juben::getGmtCreatetime));
        return R.ofSuccess(page);
    }
    
    
    @ApiOperation(value="开放接口-id查询剧本")
    @GetMapping("public/getById")
    public R<Juben> queryJubenById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        return R.ofSuccess(jubenService.getById(id));
    }
    
    @ApiOperation(value="开放接口-查询剧本分页列表")
    @GetMapping("public/getJubenDatas")
    public R<Page<Juben>> getJubenDatas(int pageNo, int pageSize, HttpServletRequest request) {
        Page<Juben> page = jubenService.getJuebnPageDataList(pageNo, pageSize, request);
        return R.ofSuccess(page);
    }
    
    
    @ApiOperation(value="开放接口-查询剧本人数上下限")
    @GetMapping("public/getMinAndMaxGamerCountOfJuben")
    public R<Object> getMinAndMaxGamerCountOfJuben() {
        return R.ofSuccess(jubenService.getMinAndMaxGamerCountOfJuben());
    }
    
    
    @ApiOperation(value="开放接口-查询剧本时长")
    @GetMapping("public/getAllGameTime")
    public R<Object> getAllGameTime() {
        return R.ofSuccess(jubenService.getAllGameTime());
    }
}