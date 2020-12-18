package pro.bzy.boot.script.config.schedule.functions;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import pro.bzy.boot.framework.config.exceptions.MySchedulingException;
import pro.bzy.boot.framework.config.schedule.SchedulingFunction;
import pro.bzy.boot.framework.web.domain.entity.TimerTask;
import pro.bzy.boot.script.service.JubenCharacterService;
import pro.bzy.boot.script.service.JubenService;


@Component
public class ClearUploadImageFunction implements SchedulingFunction {

    @Resource
    private JubenService jubenService;
    @Resource
    private JubenCharacterService jubenCharacterService;
    
    
    @Override
    public Object exec(TimerTask task) throws MySchedulingException {
        try {
            // 1. 清理上传但未使用的剧本封面照片
            int count1 = jubenService.clearUploadCoverImg();
            
            // 2. 清理上传但未使用的剧本人物照片
            int count2 = jubenCharacterService.clearUploadCharacterImg();
            
            return "共删除" + (count1 + count2) + "张剧本相关图片";
        } catch (Exception e) {
            throw new MySchedulingException("清理上传未使用得图片文件失败，原因：" + e.getMessage());
        }
    }

    
    
    @Override
    public String getDescription() {
        return "清理上传但未被使用得照片";
    }

}
