package pro.bzy.boot.thirdpart.wechat.miniprogram.service;

import pro.bzy.boot.thirdpart.wechat.miniprogram.domain.entity.WxMiniprogramSettingIndeximg;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *  服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2021-01-19
 */
public interface WxMiniprogramSettingIndeximgService extends IService<WxMiniprogramSettingIndeximg> {

    /** 增加首页轮播图 */
    WxMiniprogramSettingIndeximg addIndexSwipperImg(String imgUrl);
    
    
    /** 调整图像顺序 */
    void adjustImageSort(int fromIndex, int toIndex);
}
