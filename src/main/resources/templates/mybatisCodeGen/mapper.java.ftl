package ${package.Mapper};

import ${package.Entity}.${entity};
import ${superMapperClassPackage};

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

import pro.bzy.boot.framework.config.mybatis.MybatisCacheConfig;


/**
 * ${table.comment!} Mapper 接口
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/mapper.java.ftl
 * @author ${author}
 * @since ${date}
 */
 
<#-- mapper注解 和 缓存注解 -->
@Mapper
@CacheNamespace(implementation=MybatisCacheConfig.class, eviction=MybatisCacheConfig.class)
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

}