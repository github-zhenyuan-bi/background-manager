package pro.bzy.boot.framework.config.exceptions;

import pro.bzy.boot.framework.config.exceptions.parent.MyAbstractRuntimeException;

public class MyFrameworkException extends MyAbstractRuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MyFrameworkException() {
        super();
    }

    public MyFrameworkException(Integer code, String msg) {
        super(code, msg);
    }

    public MyFrameworkException(Integer code) {
        super(code);
    }

    public MyFrameworkException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MyFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyFrameworkException(String message) {
        super(message);
    }

    public MyFrameworkException(Throwable cause) {
        super(cause);
    }
    
    
    
    
}
