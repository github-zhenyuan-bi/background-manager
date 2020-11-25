package pro.bzy.boot.framework.web.domain.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "app.page-constant")
public class YmlPageConstant {

    private String loginTitle;
    
    private String scriptName; 
}
