package pro.bzy.boot.framework.web.domain.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pro.bzy.boot.framework.config.constant.System_constant;

/**
 * Controller请求时的标准响应结果
 * @author zhenyuan.bi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class R<T> {
    
    /** 响应结果码 */
    private Integer code;
    
    /** 响应结果信息 */
    private String msg;
    
    /** 响应结果数据 */
    private T data;
    
    
    
    /** 请求成功码 */
    public static final Integer SUCCESS = 200;
    
    /** 请求失败码 */
    public static final Integer ERROR = 500;
    
    /** 控制层表单校验错误 */
    public static final Integer FORM_VALIDATE_ERROR = 650;
    
    /** shiro认证错误 */
    public static final Integer SHIRO_AUTHENTICATION_ERROR = 601;
    
    
    
    
    /**
     * 请求响应成功
     */
    public static <T> R<T> ofSuccess(String msg, T data) {
        return R.<T>builder().code(SUCCESS).msg(msg).data(data).build();
    }
    
    /**
     * 请求响应成功
     */
    public static <T> R<T> ofSuccess(String msg) {
        return ofSuccess(msg, null);
    }
    
    /**
     * 请求响应成功
     */
    public static <T> R<T> ofSuccess(T data) {
        return ofSuccess(System_constant.RESPONSE_MSG_OK, data);
    }
    
    
    public static  <T> R<T> ofError(int code, String msg, T data) {
        return R.<T>builder().code(code).msg(msg).data(data).build();
    }
    
    
    /**
     * 请求响应失败
     */
    public static  <T> R<T> ofError(String msg, T data) {
        return ofError(ERROR, msg, data);
    }
    
    /**
     * 请求响应失败
     */
    public static  <T> R<T> ofError(String msg) {
        return ofError(msg, null);
    }
    
    /**
     * 请求响应失败
     */
    public static  <T> R<T> ofError(Exception e) {
        return ofError(e.getMessage());
    }
}
