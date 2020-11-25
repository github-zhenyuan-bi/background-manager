package pro.bzy.boot.framework.web.domain.bean;

import java.util.List;


/** 
 * 
 * 树形类
 * @author user
 *
 */
public interface TreeDomain<T> {
    
    /** 树节点id */
    String getId();
    
    /** 树节点的  父节点id */
    String getPid();
    
    /** 节点改变是否拥有孩子 */
    T setHasChild(Boolean hasChild);

    /** 节点是否拥有孩子 */
    Boolean getHasChild();
    
    /** 添加孩子 */
    T setChilds(List<T> childs);
    
    /** 获取孩子 */
    List<T> getChilds();
    
}
