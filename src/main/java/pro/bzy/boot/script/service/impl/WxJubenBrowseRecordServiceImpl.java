package pro.bzy.boot.script.service.impl;

import pro.bzy.boot.script.domain.entity.WxJubenBrowseRecord;
import pro.bzy.boot.script.mapper.WxJubenBrowseRecordMapper;
import pro.bzy.boot.script.service.WxJubenBrowseRecordService;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

import javax.annotation.Resource;

/**
 * VIEW 服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2021-01-19
 */
@Service
public class WxJubenBrowseRecordServiceImpl extends ServiceImpl<WxJubenBrowseRecordMapper, WxJubenBrowseRecord> implements WxJubenBrowseRecordService {

    @Resource
    private WxJubenBrowseRecordMapper wxJubenBrowseRecordMapper;

    
    @Override
    public Page<Map<String, Object>> listTopBrowseJubens(int pageNo, int pageSize) {
        Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
        this.pageMaps(page, Wrappers.<WxJubenBrowseRecord>query()
                .select("id", "juben_name as jubenName", "cover_img as coverImg", "count(openid) as jubenSubtext")
                .groupBy("id", "juben_name", "cover_img")
                .orderByDesc("count(openid)"));
        return page;
    }

    
}