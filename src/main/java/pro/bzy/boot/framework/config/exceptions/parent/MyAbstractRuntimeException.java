package pro.bzy.boot.framework.config.exceptions.parent;

import lombok.Getter;

public abstract class MyAbstractRuntimeException extends RuntimeException implements MyException {

    private static final long serialVersionUID = 8688845713212721495L;

    /** 异常码 */
    @Getter protected int code;
    
    /** 异常信息 */
    @Getter protected String msg;
    public static final String DEFAULT_ERR_MSG = "系统错误";

    public MyAbstractRuntimeException() {
        super();
    }

    public MyAbstractRuntimeException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MyAbstractRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyAbstractRuntimeException(String message) {
        super(message);
    }

    public MyAbstractRuntimeException(Throwable cause) {
        super(cause);
    }
    
    public MyAbstractRuntimeException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg  = msg;
    }
    
    public MyAbstractRuntimeException(Integer code) {
        super(DEFAULT_ERR_MSG);
        this.code = code;
        this.msg  = DEFAULT_ERR_MSG;
    }
}
