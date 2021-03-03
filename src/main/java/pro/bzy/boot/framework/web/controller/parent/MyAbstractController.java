package pro.bzy.boot.framework.web.controller.parent;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import pro.bzy.boot.framework.config.constant.DB_constant;
import pro.bzy.boot.framework.config.constant.JWT_constant;
import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.framework.web.domain.entity.Menu;
import pro.bzy.boot.framework.web.service.MenuService;


/**
 * 
 * 我的控制层抽象父类
 * @author user
 *
 */
public abstract class MyAbstractController {

    
    /**
     * 跳转页面
     * @param pagePath
     * @return
     */
    protected String goPage(String pagePath) {
        return pagePath;
    }
    
    
    /**
     * 跳转到管理页面
     */
    protected final String MANAGER_DIC = "bgManage/";
    protected String goManagementPage(String pagePath) {
        return goPage(MANAGER_DIC + pagePath);
    }
    
    
    
    protected String redirectPage(String pagePath) {
        return "redirect:" + pagePath;
    }
    
    
    
    /** 收集 HttpServletRequest 的全部参数为map */
    protected Map<String, Object> getParams(HttpServletRequest request) {
        // 收集全部参数
        Map<String, Object> res = new HashMap<>(request.getParameterMap().size());
        Enumeration<String> pNames = request.getParameterNames();
        while (pNames != null && pNames.hasMoreElements()) {
            String pName = pNames.nextElement();
            String pValue = request.getParameter(pName);
            res.put(pName, pValue);
        }
        return res;
    }
    
    

    /**
     * 从session中拿东西
     * @param request
     * @param key
     * @return
     */
    protected Object getObjFromSession(HttpServletRequest request, String key) {
        return request.getSession().getAttribute(key);
    }
    
    /**
     * session里放东西
     * @param request
     * @param key
     * @param value
     */
    protected void setValueToSession(HttpServletRequest request, String key, Object value) {
        request.getSession().setAttribute(key, value);
    }
    
    
    
    
    
    /**
     * 查询页面需要显示的菜单
     * @param request
     * @param model
     * @param menuService
     */
    protected void prepareMenuData(HttpServletRequest request, Map<String, Object> model
            , MenuService menuService) {
        @SuppressWarnings("unchecked")
        Map<String, Object> baseStorageDatas = (Map<String, Object>) request.getAttribute(JWT_constant.JWT_BASESTORAGE_DATAS_KEY);
        String accessor = baseStorageDatas.getOrDefault(JWT_constant.JWT_LOGIN_USERID_KEY, "").toString();
        
        List<Menu> menuList = menuService.getByAccessorAndTypeThenOrder(accessor, DB_constant.BACKGROUND_MANAGER_MENU_KEY);
        List<Menu> treeMenuList = CollectionUtil.buildTree(Menu.getDefualtRootMenu(), menuList);
        model.put("bgManageMenus", treeMenuList);
        
        if (!CollectionUtil.isEmpty(menuList)) {
            String uri = request.getRequestURI();
            Optional<Menu> curMenu = menuList.stream().filter(menuItem -> uri.equals(menuItem.getUrl())).findFirst();
            if (curMenu.isPresent())
                model.put("curMenu", curMenu.get());
        }
    }
    
    
    
}
