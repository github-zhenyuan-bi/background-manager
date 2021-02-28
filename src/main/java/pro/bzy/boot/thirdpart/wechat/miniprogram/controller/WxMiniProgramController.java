package pro.bzy.boot.thirdpart.wechat.miniprogram.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import pro.bzy.boot.framework.config.jwt.JwtUtil;
import pro.bzy.boot.framework.utils.RequestAndResponseUtil;
import pro.bzy.boot.framework.web.controller.parent.MyAbstractController;
import pro.bzy.boot.framework.web.domain.bean.R;
import pro.bzy.boot.thirdpart.wechat.miniprogram.service.WxMiniProgramService;
import pro.bzy.boot.thirdpart.wechat.miniprogram.service.WxMiniprogramLogService;
import pro.bzy.boot.thirdpart.wechat.wx.service.WxUserinfoService;

@Api(tags = {"微信登陆"}, value="微信登陆")
@ApiSupport(order = 100)
@RestController
public class WxMiniProgramController extends MyAbstractController {

    @Resource
    private WxMiniProgramService wxMiniProgramService;
    @Resource
    private WxUserinfoService WxUserinfoService;
    @Resource
    private WxMiniprogramLogService wxMiniprogramLogService;
    
    
    @ApiOperation(value="微信小程序登陆")
    @PostMapping("/loginWxMiniprogram")
    public @ResponseBody R<Object> loginWxMiniprogram(String code, 
            HttpServletRequest request, HttpServletResponse response) {
        // 1. 调取接口获取用户信息
        JSONObject jObj = wxMiniProgramService.getCode2Session(
                wxMiniProgramService.buildCode2SessionUrl(code));
        
        // 2. 系统注册用户（若不存在） 
        String openId = jObj.getString("openid");
        String session_key = jObj.getString("session_key");
        WxUserinfoService.registOnNonexist(openId, session_key);
        
        // 3.1 生成认证access_token和refresh_token
        String access_token = JwtUtil.getToken(openId, 60*30, jObj);
        String refresh_token = JwtUtil.getToken(openId, 60*60*24, jObj);
        
        jObj.put("access_token", access_token);
        jObj.put("refresh_token", refresh_token);
        
        // 2.3 将token内容放入cookie和header
        RequestAndResponseUtil.setCookiesAndHeaderToResponeForAccessToken(request, response, access_token);
        RequestAndResponseUtil.setCookiesAndHeaderToResponeForRefreshToken(request, response, refresh_token);
        return R.<Object>builder().code(R.SUCCESS).msg("ok").data(jObj).build();
    }
    
}
