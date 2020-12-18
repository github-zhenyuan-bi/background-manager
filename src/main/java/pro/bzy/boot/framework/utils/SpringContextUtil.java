package pro.bzy.boot.framework.utils;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.google.common.collect.Sets;

@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    //获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过name获取 Bean.
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    //通过class获取Bean.
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    //通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    public static Set<String> getRequestMappingUris() {
        RequestMappingHandlerMapping mapping = getBean(RequestMappingHandlerMapping.class);
        
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
        
        Set<String> urls = Sets.newHashSet();
        if (!CollectionUtil.isEmpty(handlerMethods)) {
            for (RequestMappingInfo info : handlerMethods.keySet()) {
                urls.addAll(info.getPatternsCondition().getPatterns());
            }
        }
        return urls;
    }
    
    
    /**
     * 获取spring容器中实现了某一接口得全部bean
     * @param interfaceClass
     * @return
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> interfaceClass) {
        return getApplicationContext().getBeansOfType(interfaceClass);
    }
    
}
