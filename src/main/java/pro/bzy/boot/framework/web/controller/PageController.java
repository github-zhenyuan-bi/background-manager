package pro.bzy.boot.framework.web.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import pro.bzy.boot.framework.config.schedule.SchedulingFunctions;
import pro.bzy.boot.framework.utils.ClassUtil;
import pro.bzy.boot.framework.utils.SystemConstant;
import pro.bzy.boot.framework.web.controller.parent.MyAbstractController;
import pro.bzy.boot.framework.web.domain.entity.Menu;
import pro.bzy.boot.framework.web.domain.entity.RoleMenu;
import pro.bzy.boot.framework.web.domain.entity.TimerTask;
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

/**
 * 
 * 页面转发控制器
 * @author user
 *
 */
@Controller
public class PageController extends MyAbstractController{
    
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
  
    
    
    @GetMapping("/bgManage/{module}/{pageName}")
    public String bgManageModule(
            @PathVariable("module") String module, @PathVariable("pageName") String pageName,
            HttpServletRequest request, Map<String, Object> model) {
        
        // 进入后台管理的每个模块都需要加载一下菜单
        List<Menu> menuList = menuService.getByTypeThenOrder("bgManage");
        model.put("bgManageMenus", menuList);
        return "/bgManage/"+module+"/"+pageName;
    }
    
    
    
    
    
    /**
     * 后台管理 -> 系统管理 -> 所有的模块tab页进入
     * @param type
     * @param request
     * @param model
     * @return
     */
    @GetMapping("/framework/{module}/bgManage/system/tab")
    public String frameworkBgManagerTab(@PathVariable("module") String module, 
            HttpServletRequest request, Map<String, Object> model) {
        prepareDataForTab(module, request, model);
        return "/bgManage/system/"+module+"/"+module+"-tab" ;
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
            Object taskMethods = getObjFromSession(request, SystemConstant.SCHEDULING_TASK_METHODS_KEY);
            if (taskMethods == null) {
                taskMethods = ClassUtil.getMethodNameWithDescription(SchedulingFunctions.class);
                setValueToSession(request, SystemConstant.SCHEDULING_TASK_METHODS_KEY, taskMethods);
            }
            model.put(SystemConstant.SCHEDULING_TASK_METHODS_KEY, taskMethods);
            
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
    @GetMapping("/framework/{module}/bgManage/system/form/{type}")
    public String frameworkBgManagerForm(
            @PathVariable("module") String module, 
            @PathVariable("type") String type,
            HttpServletRequest request, Map<String, Object> model) {
        
        // 为表单提供渲染数据
        prepareDataForForm(module, type, request, model);
        
        return "/bgManage/system/"+module+"/"+ type ;
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
    
    private void prepareExtraDataForForm(String module, HttpServletRequest request, Map<String, Object> model) {
        switch (module) {
            case "timerTask":
                Object taskMethods = getObjFromSession(request, SystemConstant.SCHEDULING_TASK_METHODS_KEY);
                if (taskMethods == null) {
                    taskMethods = ClassUtil.getMethodNameWithDescription(SchedulingFunctions.class);
                    setValueToSession(request, SystemConstant.SCHEDULING_TASK_METHODS_KEY, taskMethods);
                }
                model.put(SystemConstant.SCHEDULING_TASK_METHODS_KEY, taskMethods);
                break;
            default:
                break;
        }
    }
    
    
    
    @GetMapping("/error/{code}")
    public String bgManageModule(@PathVariable("code") String code, HttpServletRequest request, Map<String, Object> model) {    
        return "/"+code;
    } 
    
    
    
    
    
    
    
    
    
    @GetMapping("/script/{module}/bgManage/form/{type}")
    public String scriptBgManagerForm(
            @PathVariable("module") String module, 
            @PathVariable("type") String type,
            HttpServletRequest request, Map<String, Object> model) {
        
        // 为表单提供渲染数据
        return "/bgManage/"+module+"/"+ type ;
    }
    
}
