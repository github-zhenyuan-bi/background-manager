package pro.bzy.boot.framework.config.yml;

import lombok.Data;

@Data
public class Shiro {

    /** 登录页地址 */
    private String loginUrl;
    
    /** 登陆成功跳转地址 */
    private String successUrl;
    
    /** 认证超时时间 */
    private Long authenTimeout;
}
