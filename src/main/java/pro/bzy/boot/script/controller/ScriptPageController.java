package pro.bzy.boot.script.controller;

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
import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.framework.utils.SystemConstant;
import pro.bzy.boot.framework.web.controller.parent.MyAbstractController;
import pro.bzy.boot.framework.web.domain.entity.Menu;
import pro.bzy.boot.framework.web.service.MenuService;
import pro.bzy.boot.script.domain.entity.JubenCharacter;
import pro.bzy.boot.script.domain.entity.Tag;
import pro.bzy.boot.script.service.JubenCharacterService;
import pro.bzy.boot.script.service.JubenService;
import pro.bzy.boot.script.service.JubenTagService;
import pro.bzy.boot.script.service.TagService;

@Controller
@RequestMapping("/script")
public class ScriptPageController extends MyAbstractController {

    @Resource
    private JubenService jubenService;
    @Resource
    private MenuService menuService;
    @Resource
    private JubenCharacterService jubenCharacterService;
    @Resource
    private TagService tagService;
    @Resource
    private JubenTagService jubenTagService;
    
    
    
    
    /**
     * 剧本管理主页
     */
    @GetMapping("juben/management")
    public String management(HttpServletRequest request, Map<String, Object> model) {
        // 提供一些菜单数据共视图渲染
        prepareMenuData(request, model, menuService);
        return "/script/juben/management";
    }
    
    /**
     * 剧本管理表单填写页面
     * @param pageName
     * @param request
     * @param model
     * @return
     */
    @GetMapping("juben/form/{pageName}")
    public String editForm(@PathVariable("pageName") String pageName,
            HttpServletRequest request, Map<String, Object> model) {
        Object id = request.getParameter("id");
        if (!StringUtils.isEmpty(id)) {
            model.put("juben", jubenService.getById(id.toString()));
        }
        // 剧本人物界面加载人物信息
        String jubenId = request.getParameter("jubenId");
        if ("juben-characters".equals(pageName) && !StringUtils.isEmpty(jubenId)) {
            List<JubenCharacter> jubenCharacs = jubenCharacterService.list(Wrappers.<JubenCharacter>lambdaQuery()
                    .eq(JubenCharacter::getJubenId, jubenId)
                    .orderByAsc(JubenCharacter::getSex));
            if (!CollectionUtil.isEmpty(jubenCharacs)) {
                Map<String, List<JubenCharacter>> map = CollectionUtil.groupBy(jubenCharacs, jubenCharac -> {
                    return (SystemConstant.SEX_MAN.equals(jubenCharac.getSex()))
                            ? "maleGamers" : "femaleGamers";
                });
                model.putAll(map);
            }
        }
        model.putAll(getParams(request));
        // 为表单提供渲染数据
        return "/script/juben/" + pageName ;
    }
    
    
    
    /**
     * 剧本管理主页
     */
    @GetMapping("juben/juben-tag")
    public String jubenTag(HttpServletRequest request, Map<String, Object> model) {
        // 提供一些菜单数据共视图渲染
        prepareMenuData(request, model, menuService);
        return "/script/juben/juben-tag";
    }
    
    
    @GetMapping("juben/allocateTag")
    public String allocateTag(String jubenId, HttpServletRequest request, Map<String, Object> model) {
        // 全部标签
        List<Tag> tags = tagService.list();
        List<Tag> jubenTags = jubenTagService.getTagsByJuben(jubenId);
        if (!CollectionUtil.isEmpty(tags)) {
            boolean flag = CollectionUtil.isEmpty(jubenTags);
            tags.forEach(tag -> {
                tag.setJubenHas(flag
                        ? false
                        : jubenTags.stream().anyMatch(jTag -> tag.getId().equals(jTag.getId())));
            });
        }
        model.put("tags", tags);
        return "/script/juben/tag-allocate";
    }
    
    
    @GetMapping("business/show")
    public String businessShow(HttpServletRequest request, Map<String, Object> model) {
        List<Menu> menuList = menuService.getByTypeThenOrder(SystemConstant.BACKGROUND_MANAGER_MENU_KEY);
        model.put("bgManageMenus", menuList);
        Object id = request.getParameter("id");
        if (!StringUtils.isEmpty(id)) {
            model.put("curMenu", menuService.getById(id.toString()));
        }
        // 为表单提供渲染数据
        return "/script/business/show";
    }
    
    
    
    
    
    
    
    @GetMapping("juben/showpage")
    public String showpage(HttpServletRequest request, Map<String, Object> model) {
        // 为表单提供渲染数据
        return "/script/show/showJuben";
    }
}
