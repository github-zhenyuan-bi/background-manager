package pro.bzy.boot.framework.utils;

import org.quartz.CronExpression;
import org.springframework.util.Assert;

import lombok.NonNull;
import pro.bzy.boot.framework.utils.parents.MyUtil;

/**
 * 异常校验类
 */
public class ExceptionCheckUtil extends Assert implements MyUtil {
    
    /**
     * 是否合法得corn表达式
     * @param cornExpression
     */
    public static void islegalCornExpression(@NonNull final String cornExpression) {
        if (!CronExpression.isValidExpression(cornExpression))
            throw new IllegalArgumentException("非标准CRON表达式：" + cornExpression);
    }
}
