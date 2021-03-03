package pro.bzy.boot.framework.config.constant;

public interface Schedule_constant {

    /** 系统常量数据表查询到的 定时任务cron 用于渲染视图 放在model中的key */
    String SCHEDULE_CRON_CONSTANT_KEY = "constants";
    
    /** 系统常量数据表查询 定时任务cron配置常量 用的查询条件 */
    String SCHEDULE_CRON_CONSTANT_QUERY_TYPE = "schedule_cron";
    
    /** 通过反射添加注解@SchedulingFunction的方法 获取所有此类方法名称 用于渲染视图 放在model中的key */
    String SCHEDULE_TASK_METHODS_KEY = "taskMethods";
    
    /** 定时任务执行状态 - 失败 */
    String SCHEDULING_EXECUTE_FAILURE = "0";
    /** 定时任务执行状态 - 成功 */
    String SCHEDULING_EXECUTE_SUCCESS = "1";
}
