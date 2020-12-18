package pro.bzy.boot.framework.config.exceptions;

import pro.bzy.boot.framework.config.exceptions.parent.MyException;

public class MyFrameworkException extends RuntimeException implements MyException {

    private static final long serialVersionUID = 1L;
    private int code;
    private String msg;
    
    
    public MyFrameworkException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }


    public MyFrameworkException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }


    public MyFrameworkException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }


    public MyFrameworkException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }


    public MyFrameworkException() {}
    
    
    public MyFrameworkException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
    
    
    
    @Override
    public int getCode() {
        // TODO Auto-generated method stub
        return code;
    }
    @Override
    public String getMsg() {
        // TODO Auto-generated method stub
        return msg;
    }
    
    
}
