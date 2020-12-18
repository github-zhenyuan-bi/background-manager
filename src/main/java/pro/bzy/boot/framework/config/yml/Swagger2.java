package pro.bzy.boot.framework.config.yml;

import lombok.Data;

@Data
public class Swagger2 {

    /** 是否启动swagger */
    private Boolean enable;
    
    /** 标题 */
    private String title;
    
    /** 描述 */
    private String desc;
    
    /** 基本扫描包 */
    private String basePackage;
    
    /** 服务地址 */
    private String serverUrl;
}
