package pro.bzy.boot.framework.config.shrio;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Setter;
import pro.bzy.boot.framework.config.jwt.JwtDefaultSubjectFactory;
import pro.bzy.boot.framework.config.jwt.JwtFilter;
import pro.bzy.boot.framework.config.shrio.parent.MyShiroConfig;

@Configuration
@ConfigurationProperties(prefix = "app.config.shiro")
public class ShiroJwtConfig implements MyShiroConfig{
    
    /** 登陆url */
    @Setter private String loginUrl;
    
    /** 权限验证成功url */
    @Setter private String successUrl;
    
    
    
    /*
     * a. 告诉shiro不要使用默认的DefaultSubject创建对象，因为不能创建Session
     * */
    @Bean
    public SubjectFactory subjectFactory() {
        return new JwtDefaultSubjectFactory();
    }

    @Bean
    public Realm realm() {
        return new CustomJwtRealm();
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //securityManager.setCacheManager(ehCacheManager());
        securityManager.setRealm(realm());
        
        // 关闭 ShiroDAO 功能
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        // 不需要将 Shiro Session 中的东西存到任何地方（包括 Http Session 中）
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        //禁止Subject的getSession方法
        securityManager.setSubjectFactory(subjectFactory());
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager());
        shiroFilter.setLoginUrl(loginUrl);
        shiroFilter.setUnauthorizedUrl(loginUrl);
        /*
         * c. 添加jwt过滤器，并在下面注册
         * 也就是将jwtFilter注册到shiro的Filter中
         * 指定除了login和logout之外的请求都先经过jwtFilter
         * */
        Map<String, Filter> filterMap = new HashMap<>();
        //这个地方其实另外两个filter可以不设置，默认就是
        filterMap.put("anon", new AnonymousFilter());
        filterMap.put("jwt", new JwtFilter());
        filterMap.put("logout", new LogoutFilter());
        shiroFilter.setFilters(filterMap);

        // 拦截器
        Map<String, String> filterRuleMap = new LinkedHashMap<>();
        // 静态资源
        filterRuleMap.put("/assets/**", "anon");
        filterRuleMap.put("/webjars/**", "anon");
        filterRuleMap.put("/error/**", "anon");
        filterRuleMap.put("/404**", "anon");
        filterRuleMap.put("/500**", "anon");
        filterRuleMap.put("/**/favicon.ico", "anon");
        
        
        filterRuleMap.put("/script/juben/showpage", "anon");
        
        // 登陆注册
        filterRuleMap.put("/", "anon");
        filterRuleMap.put("/login**", "anon");
        filterRuleMap.put("/register**", "anon");
        filterRuleMap.put("/logout", "logout");
        
        filterRuleMap.put("/**", "jwt");
        shiroFilter.setFilterChainDefinitionMap(filterRuleMap);

        return shiroFilter;
    }
    
    
    
    
    /**
     * shiro包装的ehcache缓存
     * @return
     */
//    @Bean
//    public EhCacheManager ehCacheManager() {
//        EhCacheManager em = new EhCacheManager();
//        //em.setCacheManager(cacheManager);
//        return em;
//    }
}
