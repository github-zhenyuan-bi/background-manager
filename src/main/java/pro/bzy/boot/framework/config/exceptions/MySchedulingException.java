package pro.bzy.boot.framework.config.exceptions;

import pro.bzy.boot.framework.config.exceptions.parent.MyAbstractRuntimeException;

public class MySchedulingException extends MyAbstractRuntimeException {

    private static final long serialVersionUID = 1L;

    public MySchedulingException() {
        super();
    }

    public MySchedulingException(Integer code, String msg) {
        super(code, msg);
    }

    public MySchedulingException(Integer code) {
        super(code);
    }

    public MySchedulingException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MySchedulingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MySchedulingException(String message) {
        super(message);
    }

    public MySchedulingException(Throwable cause) {
        super(cause);
    }

    
    
}
