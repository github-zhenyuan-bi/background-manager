package pro.bzy.boot.framework.web.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.google.common.collect.Maps;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import pro.bzy.boot.framework.config.constant.JWT_constant;
import pro.bzy.boot.framework.config.constant.Schedule_constant;
import pro.bzy.boot.framework.config.schedule.SchedulingFunction;
import pro.bzy.boot.framework.utils.RequestAndResponseUtil;
import pro.bzy.boot.framework.utils.SpringContextUtil;
import pro.bzy.boot.framework.web.controller.parent.MyAbstractController;
import pro.bzy.boot.framework.web.domain.entity.Constant;
import pro.bzy.boot.framework.web.domain.entity.Role;
import pro.bzy.boot.framework.web.domain.entity.RoleMenu;
import pro.bzy.boot.framework.web.domain.entity.TimerTask;
import pro.bzy.boot.framework.web.domain.entity.User;
import pro.bzy.boot.framework.web.domain.entity.UserInfo;
import pro.bzy.boot.framework.web.domain.entity.UserUsergroup;
import pro.bzy.boot.framework.web.service.ConstantService;
import pro.bzy.boot.framework.web.service.MenuService;
import pro.bzy.boot.framework.web.service.RoleMenuService;
import pro.bzy.boot.framework.web.service.RoleService;
import pro.bzy.boot.framework.web.service.TimerTaskService;
import pro.bzy.boot.framework.web.service.UserInfoService;
import pro.bzy.boot.framework.web.service.UserRoleService;
import pro.bzy.boot.framework.web.service.UserService;
import pro.bzy.boot.framework.web.service.UserUsergroupService;

@Api(tags = {"框架页面"}, value="后台页面")
@ApiSupport(order = 10)
@Controller
@RequestMapping("/framework")
public class FrameworkPageController extends MyAbstractController {

    
    @Resource
    private MenuService menuService;
    @Resource
    private UserService userService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private RoleService roleService;
    @Resource
    private TimerTaskService timerTaskService;
    @Resource
    private RoleMenuService roleMenuService;
    @Resource
    private ConstantService constantService;
    @Resource
    private UserUsergroupService userUsergroupService;
    
    
    
    @ApiOperation(value="系统管理lab页面")
    @GetMapping("system/wrapper/lab")
    public String lab(HttpServletRequest request, Map<String, Object> model) {
        // 进入后台管理的每个模块都需要加载一下菜单
        prepareMenuData(request, model, menuService);
        return "/framework/wrapper";
    }
    
    
    
    @ApiOperation(value="tab模块页面")
    @GetMapping("{module}/management")
    public String management(@PathVariable("module") String module, 
            HttpServletRequest request, Map<String, Object> model) {
        prepareDataForTab(module, request, model);
        return "/framework/"+module+"/"+module+"-tab" ;
    }
    
    
   /** 
    * tab加载时需要的数据处理
    * @param type
    * @param request
    * @param model
    */
   private void prepareDataForTab(String type, HttpServletRequest request, Map<String, Object> model) {
       // 定时器tab加载时需要的数据
       if ("timerTask".equals(type)) {
           // 全部的定时任务
           List<TimerTask> tasks =  timerTaskService.list();
           model.put("tasks", tasks);
           // 定时任务可执行方法
           model.put(Schedule_constant.SCHEDULE_CRON_CONSTANT_KEY, constantService
                   .list(Wrappers.<Constant>lambdaQuery()
                           .eq(Constant::getConstType, Schedule_constant.SCHEDULE_CRON_CONSTANT_QUERY_TYPE)));
           model.put(Schedule_constant.SCHEDULE_TASK_METHODS_KEY, getTaskFuncNameWithDesc());
       }
   }
   
   
   
   
   /**
    * 打开后台管理的表单页面
    * @param module
    * @param type
    * @param request
    * @param model
    * @return
    */
   @ApiOperation(value="表单页面")
   @GetMapping("{module}/form/{pageName}")
   public String editForm(
           @PathVariable("module") String module, 
           @PathVariable("pageName") String pageName,
           HttpServletRequest request, Map<String, Object> model) {
       
       // 为表单提供渲染数据
       prepareDataForForm(module, pageName, request, model);
       
       return "/framework/"+module+"/"+ pageName ;
   }
   
   /**
    * 为即将打开的表单页面提供渲染数据
    * @param module
    * @param type
    * @param request
    * @param model
    */
   private void prepareDataForForm(String module, String type, HttpServletRequest request, Map<String, Object> model) {
       // 根据模块 然后使用id为表单提供必要填充数据
       Object id = request.getParameter("id");
       if (!StringUtils.isEmpty(id)) {
           prepareDataByIdAndModule(module, id.toString(), model);
       }
       
       // 部分表单需要除自身数据的一些其他额外数据 
       prepareExtraDataForForm(module, request, model);
   }
   
   private void prepareDataByIdAndModule(String module, String id, Map<String, Object> model) {
       switch (module) {
           case "user":
               model.put("curUser", userService.getById((String) id));
               model.put("curUserInfo", userInfoService.getById((String) id));
               break;
           case "role":
               model.put("curUser", userService.getById((String) id));
               model.put("cboxRoleList", roleService.getRolesWithUserHasFlag((String) id));
               break;
           case "menu":
               List<RoleMenu> roleMenus = roleMenuService.list(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, id));
               model.put("curMenu", menuService.getById(id));
               model.put("roleMenus", roleMenus);
               break;
           case "timerTask":
               model.put("task", timerTaskService.getById((id.toString())));
               break;
           case "userGroup":
               List<UserUsergroup> userUserGroups = userUsergroupService.list(
                       Wrappers.<UserUsergroup>lambdaQuery().eq(UserUsergroup::getUserId, id));
               model.put("userUserGroups", userUserGroups);
               break;
           default:
               break;
       }
   }
   
   
   private Map<String, String> getTaskFuncNameWithDesc() {
       Map<String, SchedulingFunction> taskFuncs = SpringContextUtil.getBeansOfType(SchedulingFunction.class);
       Map<String, String> taskFuncNameWithDesc = Maps.newHashMap();
       if (taskFuncs != null && !taskFuncs.isEmpty()) {
           taskFuncs.forEach((name, func) -> {
               taskFuncNameWithDesc.put(name, func.getDescription());
           });
       }; 
       return taskFuncNameWithDesc;
   }
   
   
   private void prepareExtraDataForForm(String module, HttpServletRequest request, Map<String, Object> model) {
       switch (module) {
           case "timerTask":
               model.put(Schedule_constant.SCHEDULE_TASK_METHODS_KEY, getTaskFuncNameWithDesc());
               model.put(Schedule_constant.SCHEDULE_CRON_CONSTANT_KEY, constantService
                       .list(Wrappers.<Constant>lambdaQuery()
                               .eq(Constant::getConstType, Schedule_constant.SCHEDULE_CRON_CONSTANT_QUERY_TYPE)));
               break;
           default:
               break;
       }
   }
   
   
   
   @ApiOperation(value="个人信息页面")
   @GetMapping("user/personaldata")
   public String personaldata(HttpServletRequest request, Map<String, Object> model) {
       Map<String, Object> jwttokenDatas = RequestAndResponseUtil.getJwttokenStorageDatasFromRequest(request);
       Object userid = jwttokenDatas.get(JWT_constant.JWT_LOGIN_USERID_KEY);
       User user = userService.getById(userid.toString());
       UserInfo userInfo = userInfoService.getById(userid.toString());
       List<Role> userRoles = userRoleService.getRolesByUserId(userid.toString());
       model.put("current_user", user);
       model.put("current_userinfo", userInfo);
       model.put("current_userroles", userRoles);
       prepareMenuData(request, model, menuService);
       return "/framework/user/user-personaldata";
   }
   
   
   
   
   @ApiOperation(value="修改密码页面")
   @GetMapping("user/account/changePassword")
   public String changePassword(HttpServletRequest request, Map<String, Object> model) {
       Map<String, Object> jwttokenDatas = RequestAndResponseUtil.getJwttokenStorageDatasFromRequest(request);
       Object userid = jwttokenDatas.get(JWT_constant.JWT_LOGIN_USERID_KEY);
       User user = userService.getById(userid.toString());
       UserInfo userInfo = userInfoService.getById(userid.toString());
       model.put("current_user", user);
       model.put("current_userinfo", userInfo);
       prepareMenuData(request, model, menuService);
       return "/framework/user/user-changePassword";
   }
}
