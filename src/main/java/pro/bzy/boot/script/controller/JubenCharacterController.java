package pro.bzy.boot.script.controller;

import pro.bzy.boot.script.domain.entity.JubenCharacter;
import pro.bzy.boot.script.mapper.JubenCharacterMapper;
import pro.bzy.boot.script.service.JubenCharacterService;

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
 * 剧本的角色 前端控制器
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/controller.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-14
 */
@Api(tags = {"剧本的角色"})
@ApiSupport(order = 100)
@RequestMapping("/script/jubenCharacter")
@RestController
public class JubenCharacterController {

	@Resource
    private JubenCharacterMapper jubenCharacterMapper;
    
    @Resource
    private JubenCharacterService jubenCharacterService;
    
    
    
    /**
     * 使用id查询数据
     * @param id
     * @return
     */
    @GetMapping("getById")
    public R<JubenCharacter> getById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        return R.ofSuccess(jubenCharacterService.getById(id));
    }

    
    
    /**
     * 查询数据列表
     */
    @GetMapping("getList")
    public R<List<JubenCharacter>> getList(JubenCharacter queryBean) {
        List<JubenCharacter> jubenCharacters = jubenCharacterService.list(Wrappers.<JubenCharacter>lambdaQuery(queryBean));
        return R.ofSuccess(jubenCharacters);
    }
    
    
    
    /**
     * 查询数据分页
     */
    @GetMapping("getPage")
    public R<Page<JubenCharacter>> getPage(int pageNo, int pageSize, JubenCharacter queryBean) {
        Page<JubenCharacter> page = new Page<>(pageNo, pageSize);
        jubenCharacterService.page(page, Wrappers.<JubenCharacter>lambdaQuery(queryBean));
        return R.ofSuccess(page);
    }
    
    
    
    /**
     * 根据id删除数据
     */
    @DeleteMapping("deleteById")
    public R<String> deleteById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        boolean flag = jubenCharacterService.removeById(id);
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    /**
     * 批量删除 根据id数组
     */
    @DeleteMapping("batchDeleteByIds")
    public R<String> batchDeleteByIds(String[] ids) {
        ExceptionCheckUtil.notEmpty(ids, "批量删除的IDs 不能为空");
        
        boolean flag = jubenCharacterService.removeByIds(Arrays.asList(ids));
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    /**
     * 插入一条新数据
     * @param jubenCharacter 数据
     * @param bindingResult 表单校验结果
     * @return
     */
    @PostMapping("addRecord")
    public R<String> addRecord(@Validated(value= {}) @RequestBody JubenCharacter jubenCharacter, BindingResult bindingResult) {
        if (StringUtils.isEmpty(jubenCharacter.getId()))
            jubenCharacter.setId(null);
        
        boolean flag = jubenCharacterService.save(jubenCharacter);
        return R.ofSuccess(flag? "添加成功，ID:" + jubenCharacter.getId() : "添加失败");
    }
    
    
    
    @PostMapping("saveOrUpdateRecords")
    public R<String> saveOrUpdateRecords(String jubenId,
            @RequestBody List<JubenCharacter> jubenCharacters, BindingResult bindingResult) {
        jubenCharacterService.saveOrUpdateRecords(jubenId, jubenCharacters);
        return R.ofSuccess("添加成功");
    }
    
    
    /**
     * 更新数据
     * @param updateBean 数据
     * @param bindingResult 表单校验结果
     * @return
     */
    @PostMapping("updateById")
    public R<String> updateById(@Validated(value= {}) @RequestBody JubenCharacter updateBean, BindingResult bindingResult) {
        ExceptionCheckUtil.hasLength(updateBean.getId(), "ID 不能为空");
        
        boolean flag = jubenCharacterService.updateById(updateBean);
        return R.ofSuccess(flag? "更新成功" : "更新失败");
    }
    
    
    
    
    @ApiOperation(value="开放接口-剧本id查询人物数据")
    @GetMapping("public/getCharactersByJubenId")
    public R<List<JubenCharacter>> getCharactersByJubenId(String jubenId) {
        ExceptionCheckUtil.hasLength(jubenId, "剧本ID不能为空");
        
        List<JubenCharacter> jubenCharacters = jubenCharacterService.list(
                Wrappers.<JubenCharacter>lambdaQuery()
                    .eq(JubenCharacter::getJubenId, jubenId));
        return R.ofSuccess(jubenCharacters);
    }
}