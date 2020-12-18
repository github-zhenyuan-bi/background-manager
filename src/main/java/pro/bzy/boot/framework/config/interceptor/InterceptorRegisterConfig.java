package pro.bzy.boot.framework.config.interceptor;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import pro.bzy.boot.framework.config.yml.YmlBean;
import pro.bzy.boot.framework.web.mapper.LogMapper;
import pro.bzy.boot.framework.web.service.ConstantService;

@Configuration
public class InterceptorRegisterConfig implements WebMvcConfigurer{

    @Resource
    private LogMapper LogMapper;
    @Resource
    private ConstantService sonstantService;
    
    @Resource
    private YmlBean ymlBean;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] baseExcludePathPatterns = new String[] {"/static/**", "/public/**", "/css/**", "/images/**", "/js/**", "/assets/**"};
        
        // 最基础拦截器
        registry.addInterceptor(new BaseInterceptor(LogMapper, sonstantService, ymlBean))
            .addPathPatterns("/**")
            .excludePathPatterns(baseExcludePathPatterns);
        
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    
}
