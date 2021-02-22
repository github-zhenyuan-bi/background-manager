package pro.bzy.boot.framework.config.exceptions;

import pro.bzy.boot.framework.config.exceptions.parent.MyException;

public class MyCacheNullKeyException extends RuntimeException implements MyException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

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

    public MyCacheNullKeyException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public MyCacheNullKeyException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public MyCacheNullKeyException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public MyCacheNullKeyException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public MyCacheNullKeyException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    
    
}
