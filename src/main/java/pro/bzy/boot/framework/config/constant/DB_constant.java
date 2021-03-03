package pro.bzy.boot.framework.config.constant;

public interface DB_constant {

    
    // ---------------------------------------
    //      数据库 字段常量
    //---------------------------------------
    /** 数据可用 1-可用 */
    Integer ENABLE = 1;  // 可以
    /** 数据可用 0-不可用 */
    Integer UNABLE = 0;  // 不可以
    
    
    /** 条件-1是 */
    Integer YES = 1;  // 是
    /** 条件-0否 */
    Integer NO  = 0;  // 否
    
    
    /** 逻辑删除 1-删除 */
    Integer IS_DELETED  = 1;   
    /** 逻辑删除 0-未删除 */
    Integer NOT_DELETED = 0;   
    
    
    /** 是否禁用数据 1-禁用 */
    Integer IS_FORBIDDEN = 1;  
    /** 是否禁用数据 0-否 */
    Integer NOT_FORBIDDEN = 0; 
    
    
    /** 性别码 1-男 */
    String SEX_MAN = "1";   
    /** 性别码 2-女 */
    String SEX_WOMEN = "2"; 
    
    
    /** 菜单资源表 顶级菜单 -- 根节点父级id -1 */
    String TREE_ROOT_ID = "-1";     
    /** 菜单资源表 查询后台管理菜单资源 --条件 */
    String BACKGROUND_MANAGER_MENU_KEY = "bgManage";
}
