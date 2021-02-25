package pro.bzy.boot.framework.config.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CacheProperty {

    /** 缓存模块的id */
    String cacheId() default "";
    
    /** 该模块的超时时间 默认0 永不超时 */
    int expire() default 0;
}
