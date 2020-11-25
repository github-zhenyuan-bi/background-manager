package pro.bzy.boot.framework.utils;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.utils.funtions.ExecutableFunction;
import pro.bzy.boot.framework.utils.parents.MyUtil;

/**
 * 效率测试工具
 */
@Slf4j
public class EffeciencyUtil implements MyUtil {

    
    /**
     * 给定一个可执行方法 测试该执行方法需要的时间
     * @param executableFunction 可执行方法
     * @param overtime 超时时间
     */
    public static void run(ExecutableFunction executableFunction, Long overtime) {
        Executor executor = new Executor(1L, executableFunction);
        Timer timer = new Timer(executor, overtime);
        
        timer.start();
    }
    
    
    
    
    /**
     * 目标方法执行线程
     */
    @AllArgsConstructor
    static class Executor extends Thread {
        
        private Long times;
        private ExecutableFunction executableFunction;
        
        public void run() {
            log.debug("目标方法开始执行");
            
            try {
                for (int i = 0; i < times; i++) {
                    executableFunction.execute();
                    ExceptionCheckUtil.isTrue(!Thread.interrupted(), "执行方法已经超过最大限时，强制中断");
                }
                log.debug("目标方法执行结束");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
    
    
    
    /**
     * 目标方法计时器
     */
    @AllArgsConstructor
    static class Timer extends Thread {
        
        private Executor executor;
        private Long overtime;
        
        public void run() {
            log.debug("开始给目标方法计时");
            long start = System.currentTimeMillis();
            try {
                executor.start();
                if (overtime == null)
                    executor.join();
                else {
                    executor.join(overtime);
                    executor.interrupt();
                }
                ExceptionCheckUtil.isTrue(!executor.isInterrupted(), 
                        "执行器运行已超过最大限时 "+overtime+" ms, 被强制中断， 停止计时");
                
                log.debug("计时器计时结束：共花费：" + (System.currentTimeMillis() -start) + "ms");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
