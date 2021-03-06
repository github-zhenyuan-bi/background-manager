package pro.bzy.boot.framework.config.mybatis;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import pro.bzy.boot.framework.config.constant.DB_constant;
import pro.bzy.boot.framework.config.constant.JWT_constant;
import pro.bzy.boot.framework.utils.DateUtil;
import pro.bzy.boot.framework.utils.RequestAndResponseUtil;

@Component
public class AutoFillFiledValHandler implements MetaObjectHandler {

    
    private Object getUserId() {
        HttpServletRequest request = RequestAndResponseUtil.getRequest();
        if (request != null) {
            Map<String, Object> jwtTokenStorageDatas = RequestAndResponseUtil.getJwttokenStorageDatasFromRequest(request);
            Object userId = jwtTokenStorageDatas.get(JWT_constant.JWT_LOGIN_USERID_KEY);
            Objects.requireNonNull(userId, "执行数据操作时未查询到操作人ID");
            return userId;
        }
        return null;
    }
    
    
    /**
     * 插入数据时 自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        
        // 逻辑删除 默认未删除
        this.setFieldValByName("deleted", DB_constant.NOT_DELETED, metaObject);
        // 创建人
        this.setFieldValByName("gmtCreator", getUserId(), metaObject);
        // 创建时间
        this.setFieldValByName("gmtCreatetime", DateUtil.getNow(), metaObject);
        // 是否被禁用
        this.setFieldValByName("isForbidden", DB_constant.NOT_FORBIDDEN, metaObject);
        
    }

    
    
    /**
     * 填充数据时自动填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 其他
        this.setFieldValByName("importTime", LocalDateTime.now(), metaObject);
        // 修改人
        this.setFieldValByName("gmtModifier", getUserId(), metaObject);
    }

}
