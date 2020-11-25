package pro.bzy.boot.framework.config.interceptor;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import pro.bzy.boot.framework.web.domain.bean.YmlPageConstant;
import pro.bzy.boot.framework.web.mapper.LogMapper;
import pro.bzy.boot.framework.web.service.ConstantService;

@Configuration
public class InterceptorRegisterConfig implements WebMvcConfigurer{

    @Resource
    private LogMapper LogMapper;
    @Resource
    private ConstantService sonstantService;
    
    @Resource
    private YmlPageConstant ymlPageConstant;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] baseExcludePathPatterns = new String[] {"/static/**", "/public/**", "/css/**", "/images/**", "/js/**", "/assets/**"};
        
        // 最基础拦截器
        registry.addInterceptor(new BaseInterceptor(LogMapper, sonstantService, ymlPageConstant))
            .addPathPatterns("/**")
            .excludePathPatterns(baseExcludePathPatterns);
        
        // 登陆拦截器
        registry.addInterceptor(new LoginInterceptor(LogMapper))
            .addPathPatterns("/loginAction**");
        
        // 框架拦截器
        registry.addInterceptor(new FrameworkInterceptor(LogMapper))
            .addPathPatterns("/framework/**");
        
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    
}
