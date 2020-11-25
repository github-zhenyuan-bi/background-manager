package pro.bzy.boot.framework.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import pro.bzy.boot.framework.config.mybatis.MybatisCacheConfig;
import pro.bzy.boot.framework.web.domain.entity.UserInfo;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户基本信息表 Mapper 接口
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/mapper.java.ftl
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
 
@Mapper
@CacheNamespace(implementation=MybatisCacheConfig.class, eviction=MybatisCacheConfig.class)
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}