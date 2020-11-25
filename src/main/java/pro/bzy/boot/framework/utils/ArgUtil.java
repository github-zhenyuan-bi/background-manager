package pro.bzy.boot.framework.utils;

import org.springframework.util.StringUtils;

import pro.bzy.boot.framework.utils.parents.MyUtil;


/**
 * 方法参数工具
 */
public class ArgUtil implements MyUtil {

    /**
     * 当参数为空时 返回给定一个默认值
     * @param arg 原参数
     * @param defaultVal 默认参数
     * @return 
     */
    public static <T> T defaultValueIfNull(T arg, T defaultVal) {
        if (arg == null)
            return defaultVal;
        
        if (arg instanceof String)
            if (StringUtils.isEmpty(arg))
                return defaultVal;
        
        return arg;
    }
}
