package pro.bzy.boot.framework.web.domain.bean;

import lombok.Data;

@Data
public class PermissionSettForm {

    private String id;
    
    private String urlExp;
    
    private String perm;
    
    private String descrip;
}
