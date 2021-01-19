package pro.bzy.boot.framework.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.lang.Nullable;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.utils.parents.MyUtil;

/**
 * 时间工具类
 */
@Slf4j
public class DateUtil implements MyUtil {

    /** 标准时间格式 年月日 时分秒 */
    public static final String STANDARD_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    /** 标准时间格式 年月日 时分秒 */
    public static final String STANDARD_DATE_PATTERN2 = "yyyyMMdd HH:mm:ss";
   
    /** 标准时间格式 年月日  */
    public static final String STANDARD_DATE_PATTERN_YMD = "yyyy-MM-dd";
    
    /** 标准时间格式 年月 */
    public static final String STANDARD_DATE_PATTERN_YM = "yyyy-MM";
   
    /** 标准时间格式 年 */
    public static final String STANDARD_DATE_PATTERN_Y = "yyyy";

    
    
    /** 格式化对象集合 */
    private static Map<String, DateFormat> formatMap = new HashMap<>(4);
    static {
        formatMap.put(STANDARD_DATE_PATTERN, new SimpleDateFormat( STANDARD_DATE_PATTERN ));
        formatMap.put(STANDARD_DATE_PATTERN_YMD, new SimpleDateFormat( STANDARD_DATE_PATTERN_YMD ));
        formatMap.put(STANDARD_DATE_PATTERN_YM, new SimpleDateFormat( STANDARD_DATE_PATTERN_YM ));
        formatMap.put(STANDARD_DATE_PATTERN_Y, new SimpleDateFormat( STANDARD_DATE_PATTERN_Y ));
        formatMap.put(STANDARD_DATE_PATTERN2, new SimpleDateFormat( STANDARD_DATE_PATTERN2 ));
    }


    /**
     * 获取当前时间
     */
    public final static Date getNow() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }
    
    
    
    public final static Date getDate(long time) {
        return new Date(time);
    }
    
    
    
    /** 
     * 获得当前年份
     * @return
     */
    public static Integer getNowYear() {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(getNow());
        return gc.get(1);
    }
    
    
    
    /**
     * 获取当前年的第一天 yyyy-MM-dd格式
     * @return
     */
    public static String getTheFirstDayOfNowYear() {
        return String.valueOf(getNowYear()) + "-01-01";
    }
    
    
    
    /**
     * 获取年的最后一天 yyyy-MM-dd格式
     * @return
     */
    public static String getTheLastDayOfNowYear() {
        return String.valueOf(getNowYear()) + "-12-31";
    }
    
    
    
    /**
     * 格式化日期 
     * @param date
     * @param dateFormat
     * @return
     */
    private final static String formatDate(
            @Nullable Date date, 
            @NonNull DateFormat dateFormat) {
        return dateFormat.format(date); 
    }
    
    
    
    /**
     * 格式化日期
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(@Nullable Date date, @Nullable String pattern) {
        return formatDate(date, formatMap.get(pattern));
    }
    
    
    public static String formateNow() {
        return formatDate(getNow(), STANDARD_DATE_PATTERN_YMD);
    }
    public static String formateNow(@Nullable String pattern) {
        return formatDate(getNow(), formatMap.get(pattern));
    }
    
    
    
    /**
     * 格式化时间 年月日 时分秒
     * @param date
     * @return
     */
    public static String YYYYMMDD_SS(@Nullable Date date) {
        return formatDate(date, STANDARD_DATE_PATTERN);
    }
    
    
    
    /**
     * 格式化时间 年月日 
     * @param date
     * @return
     */
    public static String YYYYMMDD(@Nullable Date date) {
        return formatDate(date, STANDARD_DATE_PATTERN_YMD);
    }
    
    
    
    /**
     * 格式化时间 年月
     * @param date
     * @return
     */
    public static String YYYYMM(@Nullable Date date) {
        return formatDate(date, STANDARD_DATE_PATTERN_YM);
    }
    
    
    
    /**
     * 格式化时间 年
     * @param date
     * @return
     */
    public static String YYYY(@Nullable Date date) {
        return formatDate(date, STANDARD_DATE_PATTERN_Y);
    }
    
    
    
    /**
     * 将时间字符串转成日期对象
     * @param dataStr 时间字符串
     * @param pattern 转化类型
     * @return
     */
    public static Date parse(String dataStr, String pattern) {
        try {
            DateFormat df = formatMap.get(pattern);
            ExceptionCheckUtil.notNull(df, "暂不支付此种时间类型字符串的转化：" + pattern);
            
            return df.parse(dataStr);
        } catch (ParseException e) {
            log.error("转换 时间字符串->时间 失败，原因：" + e.getMessage());
            return null;
        }
    }
    
    
    
    /**
     * 获取和指定日期 相差 diff个日子得日期
     * @param curDay
     * @param diff
     * @return
     */
    public static Date getDayDiff(Date curDay, int diff) {
        Calendar cal = Calendar.getInstance();
        if (curDay != null)
            cal.setTime(curDay);
        cal.add(Calendar.DATE, diff);
        return cal.getTime();
    }
    
    
    
    /**
     * 获取和今天 相差 diff个日子得日期
     * @param diff
     * @return
     */
    public static Date getDayDiff(int diff) {
        return getDayDiff(null, diff);
    }
    
    
    
    
}
