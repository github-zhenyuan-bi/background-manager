package pro.bzy.boot.script.mapper;

import pro.bzy.boot.script.domain.entity.JubenTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

import pro.bzy.boot.framework.config.mybatis.MybatisCacheConfig;


/**
 * 剧本-标签关系表 Mapper 接口
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/mapper.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-08
 */
 
@Mapper
@CacheNamespace(implementation=MybatisCacheConfig.class, eviction=MybatisCacheConfig.class)
public interface JubenTagMapper extends BaseMapper<JubenTag> {

}