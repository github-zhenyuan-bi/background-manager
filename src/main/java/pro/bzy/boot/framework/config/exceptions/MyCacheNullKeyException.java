package pro.bzy.boot.framework.config.exceptions;

import pro.bzy.boot.framework.config.exceptions.parent.MyAbstractRuntimeException;

public class MyCacheNullKeyException extends MyAbstractRuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

   
    public MyCacheNullKeyException() {
        super();
    }

    public MyCacheNullKeyException(Integer code, String msg) {
        super(code, msg);
    }

    public MyCacheNullKeyException(Integer code) {
        super(code);
    }

    public MyCacheNullKeyException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MyCacheNullKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyCacheNullKeyException(String message) {
        super(message);
    }

    public MyCacheNullKeyException(Throwable cause) {
        super(cause);
    }
    
    
    
    
}
