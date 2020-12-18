package pro.bzy.boot.framework.config.yml;

import lombok.Data;

@Data
public class Config {

    private Log log;
    
    private Swagger2 swagger2;
    
    private Timer timer;
    
    private Shiro shiro;
    
    private Wechat wechat;
    
    private Jwt jwt;
    
    private ImageServer imageServer;
    
    private Cache cache;
   
}
