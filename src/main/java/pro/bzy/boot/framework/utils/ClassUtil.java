package pro.bzy.boot.framework.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Description;
import org.springframework.util.ClassUtils;

import com.google.common.collect.Maps;

import io.swagger.annotations.ApiModelProperty;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.utils.parents.MyUtil;


/**
 * 
 * 类反射处理工具
 * @author user
 *
 */
@Slf4j
public class ClassUtil extends ClassUtils implements MyUtil {

    
    /**
     * 反射获取类中得方法名称和方法上得Description注解值
     * @return
     */
    public static Map<String, String> getMethodNameWithDescription(@NonNull Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        if (CollectionUtil.isEmpty(methods)) {
            log.warn("类{}中还未添加方法", clazz.getName());
            return Maps.newHashMap();
        }
        
        // 将反射得方法数组映射为 方法名-方法注释 得map
        return CollectionUtil.collToMap(Arrays.asList(methods), 
                    method -> method.getName(), 
                    method -> {
                        Description desc = method.getAnnotation(Description.class);
                        return (desc != null)? desc.value() : "未添加方法注释！";
                    });
        
    }
    
    
    
    /**
     * 反射获取类中字段得注释 
     * 字段注释必须有 @ApiModelProperty 来声明才可以获取
     * @param clazz
     * @return
     */
    public static List<String> getFieldDescription(@NonNull final Class<?> clazz) {
        List<String> descriptions = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !"serialVersionUID".equals(field.getName()))
                .map(field -> {
                    ApiModelProperty apip = field.getDeclaredAnnotation(ApiModelProperty.class);
                    if (apip != null)
                        return field.getName() + "(" + apip.value() + ")";
                    else {
                        log.warn("类{}中得字段{}还未添加@ApiModelProperty注解来注释字段含义", clazz.getName(), field.getName());
                        return "未定义注释！";
                    }
                }).collect(Collectors.toList());
        return descriptions;
    }
    
    
    
    /**
     * 反射获取对象中字段得值
     * @param obj
     * @return
     */
    public static List<Object> getFieldValue(@NonNull final Object obj) {
        List<Object> values = Arrays.stream(obj.getClass().getDeclaredFields())
                .filter(field -> !"serialVersionUID".equals(field.getName()))
                .map(field -> {
                    field.setAccessible(true);
                    Class<?> fieldType = field.getType();
                    Object value = null;
                    try {
                        value = field.get(obj);
                        if (value != null) {
                            if (Date.class.equals(fieldType))
                                value = DateUtil.formatDate((Date) value, DateUtil.STANDARD_DATE_PATTERN);
                        } else {
                            value = "";
                        }
                    } catch (IllegalArgumentException e) {
                        log.error("字段访问异常");
                    } catch (IllegalAccessException e) {
                        log.error("字段不可访问");
                    }
                    return value;
                }).collect(Collectors.toList());
        return values;
    }
}
