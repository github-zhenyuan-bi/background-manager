package pro.bzy.boot.framework.config.exceptions;

import pro.bzy.boot.framework.config.exceptions.parent.MyAbstractRuntimeException;

public class MyAopException extends MyAbstractRuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    
    public MyAopException() {
        super();
    }

    public MyAopException(Integer code, String msg) {
        super(code, msg);
    }

    public MyAopException(Integer code) {
        super(code);
    }

    public MyAopException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MyAopException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyAopException(String message) {
        super(message);
    }

    public MyAopException(Throwable cause) {
        super(cause);
    }

    

    
}
