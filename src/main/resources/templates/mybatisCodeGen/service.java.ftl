package ${package.Service};

import ${package.Entity}.${entity};
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * ${(table.comment)!} 服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author ${author}
 * @since ${date}
 */
public interface ${table.serviceName} extends IService<${entity}> {

}
