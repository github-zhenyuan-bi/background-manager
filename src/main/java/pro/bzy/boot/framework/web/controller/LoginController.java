package pro.bzy.boot.framework.web.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pro.bzy.boot.framework.config.jwt.JwtUtil;
import pro.bzy.boot.framework.config.shrio.ShiroJwtConfig;
import pro.bzy.boot.framework.utils.CookieUtil;
import pro.bzy.boot.framework.utils.SystemConstant;
import pro.bzy.boot.framework.web.controller.parent.MyAbstractController;
import pro.bzy.boot.framework.web.domain.bean.R;
import pro.bzy.boot.framework.web.domain.entity.User;
import pro.bzy.boot.framework.web.service.UserService;

@Controller
public class LoginController extends MyAbstractController{
    
    
    @Resource
    private UserService userService;
    @Resource
    private ShiroJwtConfig shiroJwtConfig;
    
    
    
    @GetMapping("/")
    public String root(HttpServletRequest request, Map<String, Object> model) {
        model.putAll(getParams(request));
        return "redirect:login";
    }
    
    
    /**
     * 进入登录页
     */
    @GetMapping("login")
    public String login(HttpServletRequest request, Map<String, Object> model) {
        model.putAll(getParams(request));
        return goPage("login");
    }
    
    
    
    @PostMapping("login")
    public @ResponseBody R<String> loginAction(
            @Validated({}) User user, BindingResult bindingResult,
            HttpServletRequest request, HttpServletResponse response) {
        // 1. 校验用户输入的用户名和密码
        boolean correct = userService.checkUsernameAndPassword(user);
        
        // 2. 如果输入正确 则认证登陆 塞入token
        if (correct) {
            // 2.1 生成认证token
            String access_token = JwtUtil.getToken(
                    user.getUsername(), 
                    SystemConstant.JWT_ACCESS_TOKEN_EXPIRE, null);
            // 2.2 生成刷新认证token
            String refresh_token = JwtUtil.getToken(
                    user.getUsername(), 
                    SystemConstant.JWT_REFRESH_TOKEN_EXPIRE, null);
            
            // 2.3 将token内容放入cookie
            CookieUtil.setCookiesToRespone(request, response, 
                    SystemConstant.COOKIE_JWT_ACCESS_TOKEN_EXPIRE, 
                    SystemConstant.JWT_ACCESS_TOKEN_KEY, access_token);
            CookieUtil.setCookiesToRespone(request, response, 
                    SystemConstant.COOKIE_JWT_REFRESH_TOKEN_EXPIRE,
                    SystemConstant.JWT_REFRESH_TOKEN_KEY, refresh_token);
            
            // 2.4 将token放入header
            response.setHeader(SystemConstant.JWT_ACCESS_TOKEN_KEY, access_token);
            response.setHeader(SystemConstant.JWT_REFRESH_TOKEN_KEY, refresh_token);
            return R.<String>builder().code(R.SUCCESS).msg("ok").data("/script/business/show").build();
        } else {
            return R.<String>builder().code(R.ERROR).msg("error").data("").build();
        }
    }
    
    
}
