package pro.bzy.boot.script.mapper;

import pro.bzy.boot.script.domain.entity.UserBalance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

import pro.bzy.boot.framework.config.mybatis.MybatisCacheConfig;


/**
 *  Mapper 接口
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/mapper.java.ftl
 * @author zhenyuan.bi
 * @since 2021-02-17
 */
 
@Mapper
@CacheNamespace(implementation=MybatisCacheConfig.class, eviction=MybatisCacheConfig.class)
public interface UserBalanceMapper extends BaseMapper<UserBalance> {

}