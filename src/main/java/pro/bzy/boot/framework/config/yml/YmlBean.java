package pro.bzy.boot.framework.config.yml;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class YmlBean {

    /** 配置项 */
    private Config config;
    
    /** 页面常量 */
    private PageConstant pageConstant;
}
