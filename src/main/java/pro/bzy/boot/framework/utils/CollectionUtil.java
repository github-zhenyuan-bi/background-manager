package pro.bzy.boot.framework.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.NonNull;
import pro.bzy.boot.framework.utils.parents.MyUtil;
import pro.bzy.boot.framework.web.domain.bean.TreeDomain;

/**
 * 集合工具类
 */
public class CollectionUtil extends CollectionUtils implements MyUtil {
    
    /**
     * 对一个Collection的集合进行分组 
     * @param coll 原Collection集合
     * @param func 分组策略 接受一个集合内元素 使用该元素构造的一个返回值作为分组键值
     * @return 分组后的Map对象
     */
    public static <T, R> Map<R, List<T>> groupBy(
            @NonNull Collection<T> coll, 
            @NonNull Function<T, R> func) {
        return coll.stream().collect(Collectors.groupingBy(func));
    }
    
    
    
    /**
     * 非空集合
     * @param coll
     * @return
     */
    public static boolean isNotEmpty(@Nullable Collection<?> coll) {
        return !isEmpty(coll);
    }
    
    
    
    /**
     * 数组不为空
     * @param objs
     * @return
     */
    public static boolean isNotEmpty(@Nullable Object[] objs) {
        return objs != null && objs.length > 0;
    }
    
    
    
    /**
     * 数不为空
     * @param objs
     * @return
     */
    public static boolean isEmpty(@Nullable Object[] objs) {
        return !isNotEmpty(objs);
    }
    
    
    
    /**
     * 向集合内填充若干个指定的相同元素
     * @param coll 非空集合对象
     * @param obj 指定元素
     * @param size 填充个数 必须大于0
     */
    public static <T> void fill(@NonNull Collection<T> coll, T obj, int size) {
        ExceptionCheckUtil.isTrue(size > 0, "Collection size must great than 0:" + size);
        
        coll.clear();
        for (int index = 0; index < size; index++)
            coll.add(obj);
    }
    
    
    
    /**
     * 映射集合为另一种集合
     * @param coll 原集合
     * @param func 映射实现
     * @return 目标集合
     */
    public static <T, R> List<R> map(Collection<T> coll, Function<T, R> func) {
        if (isEmpty(coll)) {
            return Lists.newArrayList();
        } else {
            return coll.stream().map(func).collect(Collectors.toList());
        }
    }
    
    
    
    /**
     * 集合按规则去映射为map
     * @param coll
     * @param keyFunc
     * @param valueFunc
     * @return
     */
    public static <T, K, V> Map<K, V> collToMap(Collection<T> coll, 
            Function<T, K> keyFunc, Function<T, V> valueFunc) {
        if (isEmpty(coll)) {
            return Maps.newHashMap();
        } else {
            Map<K, V> res = Maps.newHashMap();
            coll.forEach(item -> {
                res.put(keyFunc.apply(item), valueFunc.apply(item));
            });
            return res;
        }
    }
    
    
    
    /**
     * 获取集合得长度 空集合返回0
     * @param coll
     * @return
     */
    public static int size(@Nullable Collection<?> coll) {
        return isEmpty(coll)? 0 : coll.size();
    }
    
    
    
    /**
     * 将集合根基父字关系映射为树结构
     * @param root
     * @param lists
     * @return
     */
    public static <T extends TreeDomain<T>> List<T> buildTree(T root, List<T> lists) {
        addChildToCurParent(root, lists);
        return root.getChilds();
    }
    
    
    private static <T extends TreeDomain<T>> List<T> addChildToCurParent(T parent, List<T> maybeChilds) {
        // 没有孩子可分配了 就直接回归
        if (isEmpty(maybeChilds)) return null;
       
        // 当前父id
        final String id = parent.getId();
        
        // 将可分配的孩子分为 自己的孩子 和别人的孩子 两组
        Map<String, List<T>> childsGorup = groupBy(maybeChilds, child -> {
            return id.equals(child.getPid())? "childs" : "notchilds";
        });
        
        // 自己的孩子挂到自己名下
        List<T> childs = childsGorup.get("childs");
        parent.setHasChild(!isEmpty(childs)).setChilds(childs);
        
        // 如果别人的孩子为空（没有可分配的孩子了 ） 就回归
        List<T> notchilds = childsGorup.get("notchilds");
        if (isEmpty(notchilds)) return null; 
        
        // 自己的孩子继续递归 使用未分配的孩子
        if (!isEmpty(childs)) {
            for (T child : childs) {
                notchilds = addChildToCurParent(child, notchilds);
                if (isEmpty(notchilds)) return null;
            }
        }
        return notchilds;
    }
}
