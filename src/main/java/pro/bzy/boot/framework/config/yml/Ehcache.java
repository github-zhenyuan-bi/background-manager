package pro.bzy.boot.framework.config.yml;

import lombok.Data;

@Data
public class Ehcache {
        
    /** 最大缓存个数 */
    private Integer maxElementsInMemory;
    
    /** 超出部分是否缓存到磁盘 */
    private Boolean overflowToDisk;
    
    /** 是否永久有效 */
    private Boolean eternal;
    
    /** 失效前允许存活时间  单位s*/
    private Long timeToLiveSeconds;
    
    /** 失效前的允许闲置时间 单位s */
    private Long timeToIdleSeconds;
    
}
