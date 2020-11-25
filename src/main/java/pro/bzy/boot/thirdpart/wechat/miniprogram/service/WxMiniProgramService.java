package pro.bzy.boot.thirdpart.wechat.miniprogram.service;

import com.alibaba.fastjson.JSONObject;

public interface WxMiniProgramService {

    /** 构建请求地址 */
    String buildCode2SessionUrl(String code);
    
    
    /** 请求接口 获取用户信息 */
    JSONObject getCode2Session(String url);
}
