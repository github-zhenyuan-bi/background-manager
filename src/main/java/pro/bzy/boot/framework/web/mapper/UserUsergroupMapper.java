package pro.bzy.boot.framework.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import pro.bzy.boot.framework.config.mybatis.MybatisCacheConfig;
import pro.bzy.boot.framework.web.domain.entity.UserUsergroup;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


/**
 * 用户与用户组之间的关联 Mapper 接口
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/mapper.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-06
 */
 
@Mapper
@CacheNamespace(implementation=MybatisCacheConfig.class, eviction=MybatisCacheConfig.class)
public interface UserUsergroupMapper extends BaseMapper<UserUsergroup> {

    @Select("select r.name, sum(r.num) num " + 
            "  from (select t.name, CASE t2.user_id WHEN NULL THEN 0 ELSE 1 END num " + 
            "          from T_USERGROUP t " + 
            "          left join (select uug.user_id, uug.user_group_id " + 
            "                      from t_user_usergroup uug " + 
            "                     where exists (select id " + 
            "                              from t_user u " + 
            "                             where u.deleted = 0 and u.id = uug.user_id)) t2 " + 
            "            on t.id = t2.user_group_id) r " + 
            " group by r.name")
    List<Map<String, Object>> getUserNumsGroupByUsergroup();
}