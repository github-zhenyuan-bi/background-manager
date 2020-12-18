package pro.bzy.boot.framework.config.schedule.annoations;

import java.lang.annotation.Documented;

@Documented
public @interface SchedulingFunctionAnnoation {

    /**
     * 定时任务类型 默认系统定时任务
     * @return
     */
    String value() default "system";
}
