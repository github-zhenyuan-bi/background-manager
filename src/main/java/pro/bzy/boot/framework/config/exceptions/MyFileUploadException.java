package pro.bzy.boot.framework.config.exceptions;

import pro.bzy.boot.framework.config.exceptions.parent.MyAbstractRuntimeException;

public class MyFileUploadException extends MyAbstractRuntimeException {

    private static final long serialVersionUID = 1L;

    public MyFileUploadException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public MyFileUploadException(Integer code, String msg) {
        super(code, msg);
        // TODO Auto-generated constructor stub
    }

    public MyFileUploadException(Integer code) {
        super(code);
        // TODO Auto-generated constructor stub
    }

    public MyFileUploadException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public MyFileUploadException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public MyFileUploadException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public MyFileUploadException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
    
    

}
