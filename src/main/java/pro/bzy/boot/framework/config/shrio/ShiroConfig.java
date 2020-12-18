package pro.bzy.boot.framework.config.shrio;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.Filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.google.common.collect.Maps;

import pro.bzy.boot.framework.config.shrio.filter.RoleFilter;
import pro.bzy.boot.framework.config.shrio.parent.MyShiroConfig;
import pro.bzy.boot.framework.config.shrio.service.ShiroService;
import pro.bzy.boot.framework.utils.PasswordCodecUtil;
import pro.bzy.boot.framework.web.domain.entity.User;

import lombok.Data;
import lombok.Setter;
import net.sf.ehcache.CacheManager;

/**
 * 
 * 权限认证管理
 * @author zhenyuan.bi
 *
 */
@Data
@ConfigurationProperties(prefix = "app.config.shiro")
@Deprecated
public class ShiroConfig implements MyShiroConfig{
    
    /** 登陆url */
    @Setter private String loginUrl;
    
    /** 权限验证成功url */
    @Setter private String successUrl;
    
    /** 认证过期时间 */
    @Setter private Long authenTimeout;
    
    @Autowired
    private CacheManager manager;
    
    @Resource
    private ShiroService shiroService;
    
    
    /**
     * shiro登陆认证方法 
     * @param user 用户对象 账号 密码
     */
    public void shiroLogin(User user) {
        try {
            //添加用户认证信息
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
                    user.getUsername(),
                    PasswordCodecUtil.sha1(user)
            );
            // 登录认证
            subject.login(usernamePasswordToken);
        } catch (Exception e) {
            throw new AuthenticationException("账号或密码不正确");
        }
    }
    
    
    
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl(loginUrl);
        shiroFilterFactoryBean.setUnauthorizedUrl(loginUrl);
        
        // 配置自定义角色过滤器 方便对单一资源配置多角色访问 roles[admin,user]
        LinkedHashMap<String, Filter> filtersMap = Maps.newLinkedHashMap();
        filtersMap.put("roles", new RoleFilter());
        shiroFilterFactoryBean.setFilters(filtersMap);
        
        // 资源-权限 配置Map
        // Map<String, String> filterChainDefinitionMap = shiroService.loadFilterChainDefinitionMap();
        Map<String, String> filterChainDefinitionMap = Maps.newLinkedHashMap();
        // 静态资源
        filterChainDefinitionMap.put("/assets/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/error/**", "anon");
        filterChainDefinitionMap.put("/**/favicon.ico", "anon");
        
        filterChainDefinitionMap.put("/script/juben/showpage", "anon");
        
        // 登陆注册
        filterChainDefinitionMap.put("/login**", "anon");
        filterChainDefinitionMap.put("/register**", "anon");
        // 后台管理
        filterChainDefinitionMap.put("/bgManage/**", "roles[ADMIN]");
        // 框架数据
        filterChainDefinitionMap.put("/framework/**", "roles[ADMIN]");
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        
        return shiroFilterFactoryBean;
    }
    
    //将自己的验证方式加入容器
    @Bean
    public CustomRealm myShiroRealm() {
        CustomRealm customRealm = new CustomRealm(authenTimeout);
        return customRealm;
    }
    
    
    
    
    
    
    @Bean
    public SessionDAO sessionDAO(){
        return new MemorySessionDAO();
    }
 
    @Bean
    public SimpleCookie simpleCookie(){
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName("jeesite.session.id");
        return simpleCookie;
    }

    
    
    /**
     * 权限管理，配置主要是Realm的管理认证 缓存
     * @return
     */
    @Bean
    public SecurityManager securityManager(EhCacheManager ehCacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 使用ehcache缓存
        securityManager.setCacheManager(ehCacheManager);
        securityManager.setRealm(myShiroRealm());
        return securityManager;
    }
    
    /**
     * shiro包装的ehcache缓存
     * @return
     */
    @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManager(manager);
        return em;
    }
    
    //@RequiresRoles,@RequiresPermissions用的
    //不加这个注解不生效，具体不详
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
