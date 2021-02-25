package pro.bzy.boot.framework.config.yml;

import lombok.Data;

@Data
public class Cache {

    /** 觉得使用哪种缓存 */
    private String use;
    
    private Ehcache ehcache;
    
    private Redis redis;
}
