package pro.bzy.boot.thirdpart.wechat.miniprogram.mapper;

import pro.bzy.boot.framework.config.mybatis.MybatisCacheConfig;
import pro.bzy.boot.thirdpart.wechat.miniprogram.domain.entity.WxMiniprogramLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;



/**
 *  Mapper 接口
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/mapper.java.ftl
 * @author zhenyuan.bi
 * @since 2020-11-02
 */
 
@Mapper
@CacheNamespace(implementation=MybatisCacheConfig.class, eviction=MybatisCacheConfig.class)
public interface WxMiniprogramLogMapper extends BaseMapper<WxMiniprogramLog> {

}