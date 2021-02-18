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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.framework.utils.SystemConstant;
import pro.bzy.boot.framework.web.controller.parent.MyAbstractController;
import pro.bzy.boot.framework.web.domain.entity.Constant;
import pro.bzy.boot.framework.web.service.ConstantService;
import pro.bzy.boot.framework.web.service.MenuService;
import pro.bzy.boot.script.domain.entity.Bulletin;
import pro.bzy.boot.script.domain.entity.BulletinTemplate;
import pro.bzy.boot.script.domain.entity.JubenCharacter;
import pro.bzy.boot.script.domain.entity.Tag;
import pro.bzy.boot.script.service.BulletinService;
import pro.bzy.boot.script.service.BulletinTemplateService;
import pro.bzy.boot.script.service.JubenCharacterService;
import pro.bzy.boot.script.service.JubenService;
import pro.bzy.boot.script.service.JubenTagService;
import pro.bzy.boot.script.service.TagService;
import pro.bzy.boot.script.utils.ScriptConstant;

@Api(tags = {"VIEW"}, value="剧本页面")
@Controller
@RequestMapping("/script")
public class ScriptPageController extends MyAbstractController {

    @Resource
    private ConstantService constantService;
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
    
    @Resource
    private BulletinService bulletinService;
    @Resource
    private BulletinTemplateService bulletinTemplateService;
    
    
    
    @ApiOperation(value="剧本管理主页")
    @GetMapping("juben/management")
    public String management(HttpServletRequest request, Map<String, Object> model) {
        // 提供一些菜单数据共视图渲染
        prepareMenuData(request, model, menuService);
        return "/script/juben/management";
    }
    

    
    @ApiOperation(value="剧本管理表单填写页面")
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
    
    
    
    @ApiOperation(value="剧本管理表单-分配标签")
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
    
    
    
    @ApiOperation(value="剧本管理表单-编辑标签")
    @GetMapping("juben/eidtTag")
    public String eidtTag(String id, HttpServletRequest request, Map<String, Object> model) {
        model.put("tag", tagService.getById(id));
        return "/script/juben/tag-add";
    }
    
    
    
    
    @ApiOperation(value="后台主页面")
    @GetMapping("business/show")
    public String businessShow(HttpServletRequest request, Map<String, Object> model) {
        prepareMenuData(request, model, menuService);
        // 为表单提供渲染数据
        return "/script/business/show";
    }
    
    
    
    
    
    
    @GetMapping("juben/showpage")
    public String showpage(HttpServletRequest request, Map<String, Object> model) {
        // 为表单提供渲染数据
        return "/script/show/showJuben";
    }
    
    
    
    @ApiOperation(value="剧本公告模板配置页面")
    @GetMapping("bulletin/bulletin-template")
    public String bulletinTemplatePage(HttpServletRequest request, Map<String, Object> model) {
        prepareMenuData(request, model, menuService);
        List<Constant> themeList = constantService
                .listByType(ScriptConstant.BULLETIN_THEME_CONSTANT_KEY);
        model.put("themeList", themeList);
        return "/script/bulletin/bulletin-template";
    }
    
    @ApiOperation(value="剧本公告推送页面")
    @GetMapping("bulletin/bulletin")
    public String bulletinSendPage(HttpServletRequest request, Map<String, Object> model) {
        prepareMenuData(request, model, menuService);
        List<Constant> themeList = constantService
                .listByType(ScriptConstant.BULLETIN_THEME_CONSTANT_KEY);
        List<BulletinTemplate> bullTemps = bulletinTemplateService.list();
        model.put("bullTemps", bullTemps);
        model.put("themeList", themeList);
        return "/script/bulletin/bulletin";
    }
    
    /*@ApiOperation(value="剧本公告模板配置页面")
    @GetMapping("bulletin/template")
    public String bulletinPage(HttpServletRequest request, Map<String, Object> model) {
        prepareMenuData(request, model, menuService);
        List<BulletinTemplate> templateList = bulletinTemplateService.list();
        model.put("templateList", templateList);
        return "/script/bulletin/bulletin-template";
    }*/
    
    
    
    @ApiOperation(value="剧本公告表单填写页面")
    @GetMapping("bulletin/setting/form/{pageName}")
    public String bulletinFormPage(@PathVariable("pageName") String pageName,
            HttpServletRequest request, Map<String, Object> model) {
        Object id = request.getParameter("id"); 
        if (!StringUtils.isEmpty(id)) {
            model.put("bullSett", bulletinTemplateService.getById(id.toString()));
        }
        List<Constant> themeList = constantService
                .listByType(ScriptConstant.BULLETIN_THEME_CONSTANT_KEY);
        List<BulletinTemplate> templateList = bulletinTemplateService
                .list(Wrappers.<BulletinTemplate>lambdaQuery().orderByDesc(BulletinTemplate::getGmtCreatetime));
        model.put("templateList", templateList);
        model.put("themeList", themeList);
        return "/script/bulletin/" + pageName;
    }
    
    
    
    @ApiOperation(value="剧本公告内容修改页")
    @GetMapping("bulletin/content/form/{pageName}")
    public String bulletinContentEditFormPage(@PathVariable("pageName") String pageName, Bulletin bulletin,
            HttpServletRequest request, Map<String, Object> model) {
        if (!StringUtils.isEmpty(bulletin.getId()))
            model.put("curBulletin", bulletinService.getById(bulletin.getId()));
        if (!StringUtils.isEmpty(bulletin.getTemplateId()))
            model.put("curTemplate", bulletinTemplateService.getById(bulletin.getTemplateId()));
        return "/script/bulletin/" + pageName;
    }
    
    
    
    @ApiOperation(value="会员卡管理")
    @GetMapping("user/rechargeCard")
    public String rechargeCard(HttpServletRequest request, Map<String, Object> model) {
        // 菜单基本数据
        prepareMenuData(request, model, menuService);
        
        return "script/user/recharge-card";
    }
}
