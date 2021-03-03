package pro.bzy.boot.framework.config.exceptions;

import pro.bzy.boot.framework.config.exceptions.parent.MyAbstractRuntimeException;

public class MyNotImageFileException extends MyAbstractRuntimeException {

    private static final long serialVersionUID = 1L;

    public MyNotImageFileException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public MyNotImageFileException(Integer code, String msg) {
        super(code, msg);
        // TODO Auto-generated constructor stub
    }

    public MyNotImageFileException(Integer code) {
        super(code);
        // TODO Auto-generated constructor stub
    }

    public MyNotImageFileException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public MyNotImageFileException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public MyNotImageFileException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public MyNotImageFileException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
    
    

}
