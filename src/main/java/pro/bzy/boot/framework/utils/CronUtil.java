package pro.bzy.boot.framework.utils;

import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.TriggerBuilder;

import lombok.NonNull;
import pro.bzy.boot.framework.utils.parents.MyUtil;

/**
 * 
 * 定时任务 周期表达式工具类
 * 
 * @author user
 *
 */
public class CronUtil implements MyUtil {

    /**
     * 上次执行时间
     * @param cron
     * @return
     */
    public static Date getLastTriggerTime(@NonNull final String cron) {
        // 0. cron 表达式校验
        ExceptionCheckUtil.islegalCornExpression(cron);

        // 1. 生成cron
        CronTrigger trigger = getCronTriggerByCron(cron);
        
        // 2. 生成后四次得执行时间
        Date[] nextFour = getNextTime(trigger, 3);
        
        // 3. 计算上次得执行时间
        long l = nextFour[0].getTime() - (nextFour[2].getTime() - nextFour[1].getTime()) * 2;
        return new Date(l);
    }

    
    
    
    /**
     * 获取下次执行时间（getFireTimeAfter，也可以下下次...）
     * @param cron
     * @return
     */
    public static long getNextTriggerTime(@NonNull final String cron) {
        // 0. cron 表达式校验
        ExceptionCheckUtil.islegalCornExpression(cron);
        
        // 1. 生成cron
        CronTrigger trigger = getCronTriggerByCron(cron);
        
        // 2. 计算下次得执行时间
        return getNextTime(trigger, 1)[0].getTime();
    }
    
    
    
    /**
     * 使用cron表达式生成CronTrigger
     * @param cron
     * @return
     */
    public static CronTrigger getCronTriggerByCron(@NonNull final String cron) {
        return TriggerBuilder.newTrigger()
                .withIdentity("Caclulate Date")
                .withSchedule(CronScheduleBuilder
                        .cronSchedule(cron)).build();
    }
    
    
    
    /**
     * 获取从当前开始得后n次时间
     * @param trigger
     * @param nexts
     * @return
     */
    public static Date[] getNextTime(@NonNull final CronTrigger trigger, int nexts) {
        // 次数校验
        ExceptionCheckUtil.isTrue(nexts > 0, "nexts must great than 0 : " + nexts);
        
        // 计算后nexts次执行时间
        Date[] dates = new Date[nexts];
        Date pDate = trigger.getStartTime();
        for (int i = 0; i < nexts; i++) {
            pDate = trigger.getFireTimeAfter(pDate);
            dates[i] = pDate;
        }
        return dates;
    }
    
    
}
