package pro.bzy.boot.framework.web.service;

import pro.bzy.boot.framework.web.domain.entity.Menu;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 菜单资源 服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
public interface MenuService extends IService<Menu> {

    /** 构造一个树形菜单数据 */
    List<Menu> buildTreeMenus();
    
    /** 删除菜单后 继续删除该菜单的全部下级菜单 */
    int removeMenuThenHandleChilds(String id); 
    
    /** 根据类别查询菜单 */
    List<Menu> getByTypeThenOrder(String type);
    
    /** 根据类别查询菜单+ 资源授权对象过滤*/
    List<Menu> getByAccessorAndTypeThenOrder(String accessor, String type);
    
    /** 查询菜单分配列表 */
    List<Menu> getMenuList(Menu queryBean);
}
