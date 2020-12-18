package pro.bzy.boot.framework.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import pro.bzy.boot.framework.config.mybatis.MybatisCacheConfig;
import pro.bzy.boot.framework.web.domain.entity.UserRole;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


/**
 * 用户角色关联关系 Mapper 接口
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/mapper.java.ftl
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
 
@Mapper
@CacheNamespace(implementation= MybatisCacheConfig.class, eviction=MybatisCacheConfig.class)
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /** 查询每个角色对应得用户数量 */
    @Select("select name roleName, sum(n) userNum" + 
            "  from (select r.name, CASE ur.user_id WHEN NULL THEN 0 ELSE 1 END n " + 
            "          from t_role r" + 
            "          left join t_user_role ur on ur.role_id = r.id " + 
            "         where exists (select id from t_user u where u.deleted = 0 and u.id = ur.user_id)) tmp" + 
            " group by name")
    List<Map<String, Object>> getUserNumsGroupByRole();
}