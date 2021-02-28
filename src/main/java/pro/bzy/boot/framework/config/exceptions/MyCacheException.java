package pro.bzy.boot.framework.config.exceptions;

import pro.bzy.boot.framework.config.exceptions.parent.MyAbstractRuntimeException;

public class MyCacheException extends MyAbstractRuntimeException {
    private static final long serialVersionUID = 1L;

   
    public MyCacheException() {
        super();
    }

    public MyCacheException(Integer code, String msg) {
        super(code, msg);
    }

    public MyCacheException(Integer code) {
        super(code);
    }

    public MyCacheException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MyCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyCacheException(String message) {
        super(message);
    }

    public MyCacheException(Throwable cause) {
        super(cause);
    }

   
    
    
}
