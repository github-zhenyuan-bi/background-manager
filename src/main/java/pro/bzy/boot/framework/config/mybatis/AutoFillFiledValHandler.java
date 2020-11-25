package pro.bzy.boot.framework.config.mybatis;

import java.time.LocalDateTime;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import pro.bzy.boot.framework.utils.DateUtil;
import pro.bzy.boot.framework.utils.SystemConstant;

@Component
public class AutoFillFiledValHandler implements MetaObjectHandler {

    
    /**
     * 插入数据时 自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        //User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        
        // 逻辑删除 默认未删除
        this.setFieldValByName("deleted", SystemConstant.NOT_DELETED, metaObject);
        // 创建人
        //this.setFieldValByName("gmtCreator", user.getId(), metaObject);
        // 创建时间
        this.setFieldValByName("gmtCreatetime", DateUtil.getNow(), metaObject);
        // 是否被禁用
        this.setFieldValByName("isForbidden", SystemConstant.NOT_FORBIDDEN, metaObject);
        
    }

    
    
    /**
     * 填充数据时自动填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        //User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        // 其他
        this.setFieldValByName("importTime", LocalDateTime.now(), metaObject);
        // 修改人
        //this.setFieldValByName("gmtModifier", user.getId(), metaObject);
    }

}
