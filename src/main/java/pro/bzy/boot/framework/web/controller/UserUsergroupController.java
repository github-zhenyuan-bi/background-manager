package pro.bzy.boot.framework.web.controller;

import pro.bzy.boot.framework.web.domain.entity.UserUsergroup;
import pro.bzy.boot.framework.web.mapper.UserUsergroupMapper;
import pro.bzy.boot.framework.web.service.UserUsergroupService;

import pro.bzy.boot.framework.web.domain.bean.R;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;


/**
 * 用户与用户组之间的关联 前端控制器
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/controller.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-06
 */
@Api(tags = {"用户与用户组之间的关联"}, value="用户&用户组")
@ApiSupport(order = 100)
@RequestMapping("/framework/userUsergroup")
@RestController
public class UserUsergroupController {

	@Resource
    private UserUsergroupMapper userUsergroupMapper;
    
    @Resource
    private UserUsergroupService userUsergroupService;
    
    
    
    
    @ApiOperation(value="列表数据")
    @GetMapping("getList")
    public R<List<UserUsergroup>> getList(UserUsergroup queryBean) {
        List<UserUsergroup> userUsergroups = userUsergroupService.list(Wrappers.<UserUsergroup>lambdaQuery(queryBean));
        return R.ofSuccess(userUsergroups);
    }
    
    
    
    @ApiOperation(value="分页数据")
    @GetMapping("getPage")
    public R<Page<UserUsergroup>> getPage(int pageNo, int pageSize, UserUsergroup queryBean) {
        Page<UserUsergroup> page = new Page<>(pageNo, pageSize);
        userUsergroupService.page(page, Wrappers.<UserUsergroup>lambdaQuery(queryBean));
        return R.ofSuccess(page);
    }
    
    
    
}