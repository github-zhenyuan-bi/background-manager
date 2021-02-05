package pro.bzy.boot.thirdpart.wechat.miniprogram.service.impl;

import pro.bzy.boot.framework.utils.CollectionUtil;
import pro.bzy.boot.framework.utils.ExceptionCheckUtil;
import pro.bzy.boot.thirdpart.wechat.miniprogram.domain.entity.WxMiniprogramSettingIndeximg;
import pro.bzy.boot.thirdpart.wechat.miniprogram.mapper.WxMiniprogramSettingIndeximgMapper;
import pro.bzy.boot.thirdpart.wechat.miniprogram.service.WxMiniprogramSettingIndeximgService;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

/**
 *  服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2021-01-19
 */
@Service
public class WxMiniprogramSettingIndeximgServiceImpl extends ServiceImpl<WxMiniprogramSettingIndeximgMapper, WxMiniprogramSettingIndeximg> implements WxMiniprogramSettingIndeximgService {

    @Resource
    private WxMiniprogramSettingIndeximgMapper wxMiniprogramSettingIndeximgMapper;

    
    
    @Override
    public WxMiniprogramSettingIndeximg addIndexSwipperImg(String imgUrl) {
        // 1. 查询当前排序最后的一个图片
        WxMiniprogramSettingIndeximg swipper = getOne(
                Wrappers.<WxMiniprogramSettingIndeximg>lambdaQuery()
                    .last("LIMIT 1")
                    .orderByDesc(WxMiniprogramSettingIndeximg::getSort));
        
        // 2. 计算当前图片排序 末尾增加
        int sort = 500;
        if (Objects.nonNull(swipper)) {
            Integer index = swipper.getSort();
            if (Objects.nonNull(index))
                sort = index + 10;
        }
        
        // 3. 保存
        WxMiniprogramSettingIndeximg bean = WxMiniprogramSettingIndeximg.builder()
                .siwperImgUrl(imgUrl)
                .sort(sort)
                .build();
        this.save(bean);
        return bean;
    }



    @Override
    public void adjustImageSort(int fromIndex, int toIndex) {
        if (fromIndex == toIndex) return;
        
        //1. 先查出所有的图像信息
        List<WxMiniprogramSettingIndeximg> allImages = list(
                Wrappers.<WxMiniprogramSettingIndeximg>lambdaQuery()
                    .orderByAsc(WxMiniprogramSettingIndeximg::getSort));
        if (CollectionUtil.isEmpty(allImages)) return;
        
        //2. 参数异常校验
        int size = allImages.size();
        ExceptionCheckUtil.isTrue(fromIndex>=0 && fromIndex < size, "移动元素下标越界");
        ExceptionCheckUtil.isTrue(toIndex>=0 && toIndex < size, "移动元素下标越界");
        
        //3. 判断调序到 first 或者 last的极限情况 否则计算目标位置排序
        WxMiniprogramSettingIndeximg fromItem = allImages.get(fromIndex);
        WxMiniprogramSettingIndeximg toItem = allImages.get(toIndex);
        int fromSort = fromItem.getSort(), toSort = toItem.getSort();
        if (toIndex == 0) { // 移动到首位 直接比首位的排序小100
            int newSort = toSort - 10;
            fromItem.setSort(newSort);
            updateById(fromItem);
        } else if (toIndex == size - 1) { // 移动到末尾 直接比末位的排序大100
            int newSort = toSort + 10;
            fromItem.setSort(newSort);
            updateById(fromItem);
        } else if (Math.abs(toIndex - fromIndex) == 1) {    // 邻位移动 交换顺序
            fromItem.setSort(toSort);
            toItem.setSort(fromSort);
            updateById(fromItem);
            updateById(toItem);
        } else {
            // 元素后移 
            if (fromIndex < toIndex) {
                WxMiniprogramSettingIndeximg nextOfToItem = allImages.get(toIndex + 1);
                // 目标位置与目标的下一个位置之间还有空位可插入
                if (nextOfToItem.getSort() - toSort > 1) {
                    int newSort = (nextOfToItem.getSort() + toSort)/2;
                    fromItem.setSort(newSort);
                    updateById(fromItem);
                } else {
                    update(Wrappers.<WxMiniprogramSettingIndeximg>lambdaUpdate()
                            .setSql("sort = sort + 20")
                            .gt(WxMiniprogramSettingIndeximg::getSort, toSort));
                    fromItem.setSort(toSort+10);
                    updateById(fromItem);
                }
            // 元素前移
            } else {
                WxMiniprogramSettingIndeximg beforeFromItem = allImages.get(fromIndex-1);
                if (fromSort - beforeFromItem.getSort() > 1) {
                    int newSort = (beforeFromItem.getSort() + fromSort)/2;
                    fromItem.setSort(newSort);
                    updateById(fromItem);
                } else {
                    update(Wrappers.<WxMiniprogramSettingIndeximg>lambdaUpdate()
                            .setSql("sort = sort + 20")
                            .gt(WxMiniprogramSettingIndeximg::getSort, beforeFromItem.getSort()));
                    fromItem.setSort(beforeFromItem.getSort()+10);
                    updateById(fromItem);
                }
            }
        }
    }

}