package pro.bzy.boot.framework.config.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.Data;
import pro.bzy.boot.framework.config.yml.ImageServer;
import pro.bzy.boot.framework.config.yml.YmlBean;

@Data
@Configuration
public class ImageServerConfig implements WebMvcConfigurer{

    @Autowired
    private YmlBean ymlBean;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        ImageServer imageServer = ymlBean.getConfig().getImageServer();
        registry
            .addResourceHandler(imageServer.getServer()+"/**")
            .addResourceLocations("file:"+imageServer.getBaseStoragePath() + "/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
    
    
    
}
