package pro.bzy.boot.framework.web.service;


import java.io.File;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import pro.bzy.boot.framework.web.domain.entity.Log;

/**
 * 日志记录 服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-01
 */
public interface LogService extends IService<Log> {

    /** 日志归档处理 */
    File archiveLog(List<Log> logs);
}
