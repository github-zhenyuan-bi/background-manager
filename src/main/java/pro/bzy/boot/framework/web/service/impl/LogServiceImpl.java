package pro.bzy.boot.framework.web.service.impl;

import pro.bzy.boot.framework.utils.ClassUtil;
import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.framework.utils.DateUtil;
import pro.bzy.boot.framework.utils.ExcelUtil;
import pro.bzy.boot.framework.utils.PathUtil;
import pro.bzy.boot.framework.utils.SystemConstant;
import pro.bzy.boot.framework.web.domain.entity.Log;
import pro.bzy.boot.framework.web.mapper.LogMapper;
import pro.bzy.boot.framework.web.service.LogService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

/**
 * 日志记录 服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-01
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

    @Resource
    private LogMapper logMapper;
    
    
    @Override
    public File archiveLog(List<Log> logs) {
        try {
            // 0. 生成归档日志得名称
            String logArchiveDict = PathUtil.getWebappResourcePath(SystemConstant.LOG_ARCHIVE_DICT);
            String excelFileName = "系统日志归档(" + CollectionUtil.size(logs) + ")" + DateUtil.formateNow();
            
            // 1. 生成日志excel文档
            List<String> titles = null;
            List<List<String>> rows = null;
            if (CollectionUtil.isNotEmpty(logs)) {
                // 标题
                titles = ClassUtil.getFieldDescription(Log.class);
                // 值
                rows = CollectionUtil.map(logs, log -> 
                    CollectionUtil.map(ClassUtil.getFieldValue(log), obj -> obj.toString()));
            }
            return ExcelUtil.createExcel(logArchiveDict + excelFileName, titles, rows);
        } catch (Exception e) {
            throw new RuntimeException("日志归档异常，原因：" + e.getMessage());
        }
    }

}