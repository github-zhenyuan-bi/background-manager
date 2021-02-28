package pro.bzy.boot.framework.config.exceptions;

import pro.bzy.boot.framework.config.exceptions.parent.MyAbstractRuntimeException;

import lombok.Getter;

/**
 * 
 * 表单校验异常
 * @author zhenyuan.bi
 *
 */
public class FormValidatedException extends MyAbstractRuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String BASE_ERR_MSG = "请求参数存在不合法信息";
    
    /**
     * 表单验证的 错误数据和其详细信息
     */
    @Getter private Object errDatas;


    public FormValidatedException() {
        super();
    }


    public FormValidatedException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    public FormValidatedException(String message, Throwable cause) {
        super(message, cause);
    }


    public FormValidatedException(String message) {
        this(message, null);
    }

    public FormValidatedException(Object errDatas) {
        this(BASE_ERR_MSG, errDatas);
    }

    public FormValidatedException(String message, Object errDatas) {
        super(message);
        this.errDatas = errDatas;
    }
    
    
    public FormValidatedException(Throwable cause) {
        super(cause);
    }


    @Override
    public int getCode() {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public String getMsg() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
}
