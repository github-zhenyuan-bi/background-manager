package pro.bzy.boot.framework.web.controller;

import pro.bzy.boot.framework.web.domain.entity.UserInfo;
import pro.bzy.boot.framework.web.mapper.UserInfoMapper;
import pro.bzy.boot.framework.web.service.UserInfoService;

import pro.bzy.boot.framework.web.domain.bean.R;
import pro.bzy.boot.framework.utils.ExceptionCheckUtil;

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
import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;


/**
 * 用户基本信息表 前端控制器
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/controller.java.ftl
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
@Api(tags = {"用户基本信息表"})
@ApiSupport(order = 100)
@RequestMapping("/framework/userInfo")
@RestController
public class UserInfoController {

	@Resource
    private UserInfoMapper userInfoMapper;
    
    @Resource
    private UserInfoService userInfoService;
    
    
    
    /**
     * 使用id查询数据
     * @param id
     * @return
     */
    @GetMapping("getById")
    public R<UserInfo> getById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        return R.ofSuccess(userInfoService.getById(id));
    }

    
    
    /**
     * 查询数据列表
     */
    @GetMapping("getList")
    public R<List<UserInfo>> getList(UserInfo queryBean) {
        List<UserInfo> userInfos = userInfoService.list(Wrappers.<UserInfo>lambdaQuery(queryBean));
        return R.ofSuccess(userInfos);
    }
    
    
    
    /**
     * 查询数据分页
     */
    @GetMapping("getPage")
    public R<Page<UserInfo>> getPage(int pageNo, int pageSize, UserInfo queryBean) {
        Page<UserInfo> page = new Page<>(pageNo, pageSize);
        userInfoService.page(page, Wrappers.<UserInfo>lambdaQuery(queryBean));
        return R.ofSuccess(page);
    }
    
    
    
    /**
     * 根据id删除数据
     */
    @DeleteMapping("deleteById")
    public R<String> deleteById(final String id) {
        ExceptionCheckUtil.hasLength(id, "ID 不能为空");
        
        boolean flag = userInfoService.removeById(id);
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    /**
     * 批量删除 根据id数组
     */
    @DeleteMapping("batchDeleteByIds")
    public R<String> batchDeleteByIds(String[] ids) {
        ExceptionCheckUtil.notEmpty(ids, "批量删除的IDs 不能为空");
        
        boolean flag = userInfoService.removeByIds(Arrays.asList(ids));
        return R.ofSuccess("删除结果：" + (flag? "删除成功" : "删除失败"));
    }
    
    
    
    /**
     * 插入一条新数据
     * @param userInfo 数据
     * @param bindingResult 表单校验结果
     * @return
     */
    @PostMapping("addRecord")
    public R<String> addRecord(@Validated(value= {}) UserInfo userInfo, BindingResult bindingResult) {
        if (StringUtils.isEmpty(userInfo.getId()))
            userInfo.setId(null);
        
        boolean flag = userInfoService.save(userInfo);
        return R.ofSuccess(flag? "添加成功，ID:" + userInfo.getId() : "添加失败");
    }
    
    
    
    /**
     * 更新数据
     * @param updateBean 数据
     * @param bindingResult 表单校验结果
     * @return
     */
    public R<String> updateById(@Validated(value= {}) UserInfo updateBean, BindingResult bindingResult) {
        ExceptionCheckUtil.hasLength(updateBean.getId(), "ID 不能为空");
        
        boolean flag = userInfoService.updateById(updateBean);
        return R.ofSuccess(flag? "更新成功" : "更新失败");
    }
}