package pro.bzy.boot.thirdpart.wechat.miniprogram.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import pro.bzy.boot.framework.config.jwt.JwtUtil;
import pro.bzy.boot.framework.web.controller.parent.MyAbstractController;
import pro.bzy.boot.framework.web.domain.bean.R;
import pro.bzy.boot.thirdpart.wechat.miniprogram.service.WxMiniProgramService;
import pro.bzy.boot.thirdpart.wechat.miniprogram.service.WxMiniprogramLogService;
import pro.bzy.boot.thirdpart.wechat.wx.service.WxUserinfoService;

@RestController
public class WxMiniProgramController extends MyAbstractController {

    @Resource
    private WxMiniProgramService wxMiniProgramService;
    @Resource
    private WxUserinfoService WxUserinfoService;
    @Resource
    private WxMiniprogramLogService wxMiniprogramLogService;
    
    
    @PostMapping("/loginWxMiniprogram")
    public @ResponseBody R<String> loginWx(@RequestBody String code, HttpServletRequest request) {
        // 1. 调取接口获取用户信息
        JSONObject jObj = wxMiniProgramService.getCode2Session(
                wxMiniProgramService.buildCode2SessionUrl(code));
        
        // 2. 系统注册用户（若不存在） 
        String openId = jObj.getString("openid");
        String session_key = jObj.getString("session_key");
        WxUserinfoService.registOnNonexist(openId, session_key);
        
        // 3. 构造token 返回
        String token = JwtUtil.getToken(openId, 30*60*1000, jObj);
        return R.<String>builder().code(R.SUCCESS).msg("ok").data(token).build();
    }
    
}
