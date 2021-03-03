package pro.bzy.boot.framework.config.exceptions;

import pro.bzy.boot.framework.config.exceptions.parent.MyAbstractRuntimeException;

public class MyFileThumbnailException extends MyAbstractRuntimeException {

    private static final long serialVersionUID = 1L;

    public MyFileThumbnailException() {
        super();
    }

    public MyFileThumbnailException(Integer code, String msg) {
        super(code, msg);
    }

    public MyFileThumbnailException(Integer code) {
        super(code);
    }

    public MyFileThumbnailException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MyFileThumbnailException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyFileThumbnailException(String message) {
        super(message);
    }

    public MyFileThumbnailException(Throwable cause) {
        super(cause);
    }

    
    
}
