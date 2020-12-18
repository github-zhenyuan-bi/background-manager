package pro.bzy.boot.framework.web.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import pro.bzy.boot.framework.web.domain.entity.Constant;

/**
 * 系统常量表 服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-01
 */
public interface ConstantService extends IService<Constant> {

    /** 根据类别查询常量 */
    List<Constant> listByType(String type);
}
