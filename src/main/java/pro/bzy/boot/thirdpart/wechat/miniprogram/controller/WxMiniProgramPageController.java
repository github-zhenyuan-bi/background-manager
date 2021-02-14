package pro.bzy.boot.thirdpart.wechat.miniprogram.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.framework.web.controller.parent.MyAbstractController;
import pro.bzy.boot.framework.web.service.MenuService;
import pro.bzy.boot.thirdpart.wechat.miniprogram.domain.entity.WxMiniprogramSettingCooperation;
import pro.bzy.boot.thirdpart.wechat.miniprogram.domain.entity.WxMiniprogramSettingCooperationsContent;
import pro.bzy.boot.thirdpart.wechat.miniprogram.service.WxMiniprogramSettingCooperationService;
import pro.bzy.boot.thirdpart.wechat.miniprogram.service.WxMiniprogramSettingCooperationsContentService;

@Api(tags = {"微信小程序页面"}, value="微信小程序页面")
@ApiSupport(order = 100)
@Controller
@RequestMapping("/wx/wxMiniprogramSetting/page")
public class WxMiniProgramPageController extends MyAbstractController {

    
    @Resource
    private MenuService menuService;
    @Resource
    private WxMiniprogramSettingCooperationService wxMiniprogramSettingCooperationService;
    @Resource
    private WxMiniprogramSettingCooperationsContentService wxMiniprogramSettingCooperationsContentService;
    
    
    @ApiOperation(value="小程序配置页面")
    @GetMapping("settingPage")
    public String loginWxMiniprogram(Map<String, Object> model, 
            HttpServletRequest request, HttpServletResponse response) {
        prepareMenuData(request, model, menuService);
        
        List<WxMiniprogramSettingCooperation> coops = wxMiniprogramSettingCooperationService.list();
        List<WxMiniprogramSettingCooperationsContent> allContents = wxMiniprogramSettingCooperationsContentService.list(
                Wrappers.<WxMiniprogramSettingCooperationsContent>lambdaQuery()
                        .orderByAsc(WxMiniprogramSettingCooperationsContent::getSort));
        if (CollectionUtil.isNotEmpty(coops) && CollectionUtil.isNotEmpty(allContents)) {
            for (WxMiniprogramSettingCooperation coop : coops) {
                List<WxMiniprogramSettingCooperationsContent> temp = Lists.newArrayListWithCapacity(4);
                for (WxMiniprogramSettingCooperationsContent content : allContents) {
                    if (content.getCoopId().equals(coop.getId())) {
                        temp.add(content);
                    }
                }
                coop.setContents(temp);
            }
        }
        model.put("coops", coops);
        return "/wx/miniprogram/setting/setting";
    }
    
    
    @ApiOperation(value="合作模块表单页面")
    @GetMapping("cooperationFormEdit")
    public String cooperationFormEdit(Map<String, Object> model, String id,
            HttpServletRequest request, HttpServletResponse response) {
        if (!StringUtils.isEmpty(id)) {
            WxMiniprogramSettingCooperation coop = wxMiniprogramSettingCooperationService.getById(id);
            model.put("coop", coop);
            
            List<WxMiniprogramSettingCooperationsContent> coopContents = null;
            if (Objects.nonNull(coop)) {
                coopContents = wxMiniprogramSettingCooperationsContentService.list(
                        Wrappers.<WxMiniprogramSettingCooperationsContent>lambdaQuery()
                                .eq(WxMiniprogramSettingCooperationsContent::getCoopId, coop.getId())
                                .orderByAsc(WxMiniprogramSettingCooperationsContent::getSort));
                model.put("coopContents", coopContents);
            }
        }
        return "/wx/miniprogram/setting/cooperation-add";
    }
}
