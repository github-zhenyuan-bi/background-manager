package pro.bzy.boot.script.service.impl;

import pro.bzy.boot.script.domain.entity.UserRechargeRecord;
import pro.bzy.boot.script.mapper.UserRechargeRecordMapper;
import pro.bzy.boot.script.service.UserRechargeRecordService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *  服务实现类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/serviceImpl.java.ftl
 * @author zhenyuan.bi
 * @since 2021-02-17
 */
@Service
public class UserRechargeRecordServiceImpl extends ServiceImpl<UserRechargeRecordMapper, UserRechargeRecord> implements UserRechargeRecordService {

    @Resource
    private UserRechargeRecordMapper userRechargeRecordMapper;

}