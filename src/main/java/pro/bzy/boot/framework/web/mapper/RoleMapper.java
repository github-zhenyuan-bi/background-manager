package pro.bzy.boot.framework.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import pro.bzy.boot.framework.config.mybatis.MybatisCacheConfig;
import pro.bzy.boot.framework.web.domain.entity.Role;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;


/**
 * 角色 Mapper 接口
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/mapper.java.ftl
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
 
@Mapper
@CacheNamespace(implementation=MybatisCacheConfig.class, eviction=MybatisCacheConfig.class)
public interface RoleMapper extends BaseMapper<Role> {

    /** 查询公共角色 */
    Role getPublicRole();
    
}