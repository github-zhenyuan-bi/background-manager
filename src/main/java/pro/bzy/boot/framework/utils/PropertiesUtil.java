package pro.bzy.boot.framework.utils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.stream.Collectors;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.config.constant.CACHE_constant;
import pro.bzy.boot.framework.config.constant.File_constant;
import pro.bzy.boot.framework.config.constant.System_constant;
import pro.bzy.boot.framework.utils.parents.MyUtil;

/**
 * 
 */
@Slf4j
public final class PropertiesUtil implements MyUtil{
    
    public static ImmutableMap<String, String> props;
    static {
        log.debug("默认加载application主文件 和 副文件 成map");
        props = loadApplicationWithActiveProfile();
    }
    
    
    
    /**
     * 获得配置文件里的值 通过key
     * @param key
     * @return
     */
    public static String get(String key) {
        return props.get(key);
    }
    
    
    
    /**
     * 从yml配置文件里查询jwtToken的过期时间
     * @param key
     * @return
     */
    public static long getJwtTokenExpire(final String key) {
        String access_token_expire = get(key);
        if (StringUtils.hasText(access_token_expire)) {
            return Long.valueOf(access_token_expire);
        }
        return -1;
    }
    
    
    
    
    /**
     * 从yml中读取redis的超时时间
     * @return
     */
    public static int getRedisExpireFormYml() {
        // yml配置
        Object ymlRedisExpire = PropertiesUtil.get(CACHE_constant.REDIS_CACHE_EXPIRE_KEY);
        if (Objects.nonNull(ymlRedisExpire)) {
            try {
                return Integer.parseInt(ymlRedisExpire.toString());
            } catch (Exception e) {
                log.error("从yml读取【{}】的超时时间设定异常，非常规数值", CACHE_constant.REDIS_CACHE_EXPIRE_KEY);
            }
        }
        return 0;
    }
    
    
    
    /**
     * 通过统一前缀查询全部的符合要求的设定
     * @param prefix
     * @return
     */
    public static Map<String, String> getByPrefix(final String prefix) {
        Map<String, String> res = props.entrySet().stream().filter(
                ent -> ent.getKey().startsWith(prefix))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        return res;
    }
    
    
    /**
     * 刷新application主文件 和 副文件内容
     */
    public void refresh() {
        props = loadApplicationWithActiveProfile();
    }
    
    
    
    /**
     * 加载项目下的application主文件 和 副文件 成map
     * @return
     */
    public static ImmutableMap<String, String> loadApplicationWithActiveProfile() {
        Map<String, String> res = Maps.newHashMap();
        try {
            // 检测classpath下存在哪一种配置文件
            Resource resource = new ClassPathResource(File_constant.APPLICATION_YML);
            if (!resource.exists())
                resource = new ClassPathResource(File_constant.APPLICATION_PROPERTIES);
            ExceptionCheckUtil.isTrue(resource.exists(), "未检测到classpath下存在 application 文件");
            
            // 获取主文件内容
            ImmutableMap<String, String> applicationProps = loadFile(resource.getFilename());
            res.putAll(applicationProps);
            // 获取副文件内容
            if (applicationProps.containsKey(System_constant.APPLICATION_PROFILE_ACTIVE_KEY)) {
                String active = applicationProps.get(System_constant.APPLICATION_PROFILE_ACTIVE_KEY);
                String activeFileName = buildActiveFileName(resource.getFilename(), active);
                ImmutableMap<String, String> activeProps = loadFile(activeFileName);
                res.putAll(activeProps);
            }
        } catch (Exception e) {
            log.error("读取配置文件失败，原因：{}", e.getMessage());
        }
        return ImmutableMap.copyOf(res);
    }
    
    
    
    /**
     * 构造文件名
     * @param applicationFileName 
     * @param activeName
     * @return
     */
    private static String buildActiveFileName(String applicationFileName, String activeName) {
        int dotIndex = applicationFileName.lastIndexOf(StringPool.DOT);
        return applicationFileName.substring(0, dotIndex) +
                StringPool.DASH +
                activeName +
                applicationFileName.substring(dotIndex);
    }
    
    
    
    /**
     * 读取配置文件 根据文件名自动匹配 文件类型
     * @param fileName 文件名称
     * @return
     * @throws Exception
     */
    private static ImmutableMap<String, String> loadFile(@NonNull final String fileName) throws Exception {
        if (StringUtils.endsWithIgnoreCase(fileName, File_constant.YML_SUFFIX)) {
            return loadYmlFile(fileName);
        } else if (StringUtils.endsWithIgnoreCase(fileName, File_constant.PROPERTIES_SUFFIX)){
            return loadPropertiesFile(fileName);
        } else {
            throw new UnsupportedOperationException("暂时仅支持对该类型的配置文件的读取：" + fileName);
        }
    }
    
    
    
    /**
     * 读取yml文件内容成Map
     * @param fileName 文件名称
     * @return
     */
    public static ImmutableMap<String, String> loadYmlFile(@NonNull final String fileName) {
        // 加载yml文件内容
        Resource resource = new ClassPathResource(fileName);
        
        // 文件内容转bean
        YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        yamlPropertiesFactoryBean.setResources(resource);
        
        // 再转map
        return propertiesToMap(yamlPropertiesFactoryBean.getObject());
    }
    
    
    
    /**
     * 读取properties文件内容成Map
     * @param fileName 文件名称
     * @return
     * @throws IOException
     */
    public static ImmutableMap<String, String> loadPropertiesFile(@NonNull final String fileName) throws IOException {
        // 加载yml文件内容
        Resource resource = new ClassPathResource(fileName);
        
        // 文件内容转bean
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(resource);
        
        // 再转map
        return propertiesToMap(propertiesFactoryBean.getObject());
    }
    
    
    
    
    private static ImmutableMap<String, String> propertiesToMap(Properties prop) {
        return (prop == null)
                ? ImmutableMap.of()
                : Maps.fromProperties(prop);
    }
}
