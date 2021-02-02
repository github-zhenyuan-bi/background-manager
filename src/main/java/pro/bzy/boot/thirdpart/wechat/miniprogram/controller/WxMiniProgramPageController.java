package pro.bzy.boot.thirdpart.wechat.miniprogram.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import pro.bzy.boot.framework.web.controller.parent.MyAbstractController;
import pro.bzy.boot.framework.web.service.MenuService;

@Api(tags = {"微信小程序页面"})
@ApiSupport(order = 100)
@Controller
@RequestMapping("/wx/wxMiniprogramSetting/page")
public class WxMiniProgramPageController extends MyAbstractController {

    
    @Resource
    private MenuService menuService;
    
    @ApiOperation(value="小程序配置页面")
    @GetMapping("settingPage")
    public String loginWxMiniprogram(Map<String, Object> model, 
            HttpServletRequest request, HttpServletResponse response) {
        prepareMenuData(request, model, menuService);
        
        return "/wx/miniprogram/setting/setting";
    }
}
