package pro.bzy.boot.framework.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import pro.bzy.boot.framework.config.jwt.JwtToken;
import pro.bzy.boot.framework.config.jwt.JwtUtil;
import pro.bzy.boot.framework.config.shrio.ShiroJwtConfig;
import pro.bzy.boot.framework.utils.RequestAndResponseUtil;
import pro.bzy.boot.framework.utils.CacheUtil;
import pro.bzy.boot.framework.utils.PropertiesUtil;
import pro.bzy.boot.framework.utils.SystemConstant;
import pro.bzy.boot.framework.web.controller.parent.MyAbstractController;
import pro.bzy.boot.framework.web.domain.bean.R;
import pro.bzy.boot.framework.web.domain.entity.User;
import pro.bzy.boot.framework.web.service.UserInfoService;
import pro.bzy.boot.framework.web.service.UserService;

@Api(tags = {"后台登陆"}, value="后台登陆")
@ApiSupport(order = 1)
@Controller
public class LoginController extends MyAbstractController{
    
    
    @Resource
    private UserService userService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private ShiroJwtConfig shiroJwtConfig;
    
    
    
    @ApiOperation(value="根地址重定向至-->/login")
    @GetMapping("/")
    public String root(HttpServletRequest request, Map<String, Object> model) {
        model.putAll(getParams(request));
        return "redirect:login";
    }
    
    
    
    @ApiOperation(value="访问登陆页面")
    @GetMapping("login")
    public String login(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) {
        // 1. 清除header 和 cookies中的 jwttoken信息
        RequestAndResponseUtil.clearLoginJwttokenDataFromCookies(request, response);
        model.putAll(getParams(request));
        return goPage("login");
    }
    
    
    
    @ApiOperation(value="请求登陆系统")
    @PostMapping("login")
    public @ResponseBody R<String> loginAction(
            @Validated({}) User user, BindingResult bindingResult,
            HttpServletRequest request, HttpServletResponse response) {
        // 1. 校验用户输入的用户名和密码
        User loginUser = userService.checkUsernameAndPassword(user);
        loginUser.setUserInfo(userInfoService.getById(loginUser.getId()));
        
        // 过期时间
        final long access_token_expire = PropertiesUtil.getJwtTokenExpire(SystemConstant.JWT_ACCESS_TOKEN_EXPIRE_KEY_IN_YML);
        final long refresh_token_expire = PropertiesUtil.getJwtTokenExpire(SystemConstant.JWT_REFRESH_TOKEN_EXPIRE_KEY_IN_YML);
        
        // token包含的必要数据
        Map<String, Object> datas = new HashMap<>(2);
        datas.put(SystemConstant.JWT_LOGIN_FROMWHERE_KEY, SystemConstant.JWT_LOGIN_FROMWHERE_BACKGROUND);
        datas.put(SystemConstant.JWT_LOGIN_USER, loginUser);
        datas.put(SystemConstant.JWT_LOGIN_USERID_KEY, loginUser.getId());
        datas.put(SystemConstant.JWT_LOGIN_USER_IP_KEY, RequestAndResponseUtil.getIpAddress(request));
        
        
        // 2.1 生成认证access_token和refresh_token
        String access_token = JwtUtil.getToken(user.getUsername(), access_token_expire, datas);
        String refresh_token = JwtUtil.getToken(user.getUsername(), refresh_token_expire, datas);
        
        // 2.2 shiro登陆
        SecurityUtils.getSubject().login(new JwtToken(access_token));
        
        // 2.3 将token内容放入cookie和header
        RequestAndResponseUtil.setCookiesAndHeaderToResponeForAccessToken(request, response, access_token);
        RequestAndResponseUtil.setCookiesAndHeaderToResponeForRefreshToken(request, response, refresh_token);
        
        // 2.4 将token放入缓存
        CacheUtil.put(access_token, datas, (int) access_token_expire);
        
        String accessUrl = RequestAndResponseUtil.getLastAccessUrlToCookie(request, response);
        if (StringUtils.isEmpty(accessUrl))
            accessUrl = "/script/business/show";
        
        return R.<String>builder().code(R.SUCCESS).msg("ok").data(accessUrl).build();
    }
    
    
    @ApiOperation(value="登出系统")
    @GetMapping("logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, 
            Map<String, Object> model) {
        return "redirect:login";
    }
    
    
    
    @GetMapping("/error/{code}")
    public String bgManageModule(@PathVariable("code") String code, HttpServletRequest request, Map<String, Object> model) {    
        return "/"+code;
    } 
    
}
