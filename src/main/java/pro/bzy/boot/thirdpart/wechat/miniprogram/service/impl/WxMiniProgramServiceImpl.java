package pro.bzy.boot.thirdpart.wechat.miniprogram.service.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import lombok.Setter;
import pro.bzy.boot.framework.utils.ExceptionCheckUtil;
import pro.bzy.boot.framework.utils.HttpUtil;
import pro.bzy.boot.thirdpart.wechat.miniprogram.service.WxMiniProgramService;


@Service
@ConfigurationProperties(prefix = "app.config.wechat")
public class WxMiniProgramServiceImpl implements WxMiniProgramService{

    /** 小程序id */
    @Setter private String appid;
    
    /** 小程序密钥 */
    @Setter private String secret;
    
    @Override
    public String buildCode2SessionUrl(final String code) {
        ExceptionCheckUtil.hasText(appid, "小程序appid不能为空");
        ExceptionCheckUtil.hasText(secret, "小程序密钥不能为空");
        ExceptionCheckUtil.hasText(code, "小程序登陆需要的code码不能为空，code:" + code);
        
        return "https://api.weixin.qq.com/sns/jscode2session"
                + "?appid=" + appid
                + "&secret=" + secret
                + "&js_code="+ code
                + "&grant_type=authorization_code";
    }

    
    
    @Override
    public JSONObject getCode2Session(final String url) {
        try {
            String responseStr = HttpUtil.httpGet(url);
            ExceptionCheckUtil.hasText(responseStr, "使用code调取用户信息失败, 返回数据空");
            
            return JSONObject.parseObject(responseStr);
        } catch (Exception e) {
            throw new RuntimeException("使用小程序传递的code获取用户信息失败，原因：" + e.getMessage());
        }
    }

}
