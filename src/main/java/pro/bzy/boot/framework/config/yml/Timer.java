package pro.bzy.boot.framework.config.yml;

import lombok.Data;

@Data
public class Timer {

    /** 线程池数量 */
    private Integer threadPoolSize;
    
    /** 线程名称前缀 */
    private String threadPrefix;
}
