package pro.bzy.boot.script.service;

import pro.bzy.boot.script.domain.entity.Juben;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 剧本 服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2020-10-08
 */
public interface JubenService extends IService<Juben> {

    /** 上传新剧本 */
    void addJuben(Juben juben);
    
    
    /** 清理上传剧本封面的图片 */
    int clearUploadCoverImg();
    
    
    /** 查询剧本分页列表 */
    Page<Juben> getJuebnPageDataList(int pageNo, int pageSize, HttpServletRequest request);
    
    
    /** 查询剧本中最多和最少的玩家人数 */
    Map<String, Integer> getMinAndMaxGamerCountOfJuben();
    
    
    /** 查询全部的游戏时长枚举 */
    List<Object> getAllGameTime();
}
