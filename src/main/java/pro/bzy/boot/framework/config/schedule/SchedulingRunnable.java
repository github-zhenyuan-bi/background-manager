package pro.bzy.boot.framework.config.schedule;

import java.util.Objects;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.utils.funtions.ExecutableFunction;



/**
 * 动态定时器 执行的任务内容设定
 */
@Slf4j
@AllArgsConstructor
public class SchedulingRunnable implements Runnable {
    
    /** 任务名称 */
    @NonNull
    @Getter
    private String name;
    
    /** 定时任务需要执行的方法 */
    private ExecutableFunction execFunc;
    
    
    @Override
    public void run() {
        log.info("任务#【{}】开始执行", name);
        try {
            
            Long start = System.currentTimeMillis();
            execFunc.execute();
            log.info("任务#【{}】执行完成，耗时 {}ms", name, System.currentTimeMillis()-start);
            
        } catch (Exception e) {
            log.info("任务#【{}】执行过程中发生异常，原因：", e.getMessage());
        }
    }
    
    
    
    /** 
     * 重构equals 区别每个独立的任务
     */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        if (obj instanceof SchedulingRunnable) {
            SchedulingRunnable srObj = (SchedulingRunnable) obj;
            return this.name.equals(srObj.name);
        } else {
            return false;
        }
    }
    
    public int hashCode() {
        return Objects.hash(name);
    }
}