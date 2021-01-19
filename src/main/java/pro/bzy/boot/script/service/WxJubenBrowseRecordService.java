package pro.bzy.boot.script.service;

import pro.bzy.boot.script.domain.entity.WxJubenBrowseRecord;

import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * VIEW 服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2021-01-19
 */
public interface WxJubenBrowseRecordService extends IService<WxJubenBrowseRecord> {

    /** 查询当下最热门的剧本列表数据 --> 按照浏览量排名 */
    Page<Map<String, Object>> listTopBrowseJubens(int pageNo, int pageSize);
}
