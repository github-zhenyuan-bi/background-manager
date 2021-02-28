package pro.bzy.boot.framework.config.exceptions.parent;

/**
 * 我的异常父接口
 * @author user
 *
 */
public interface MyException {

    /** 异常码 */
    int getCode();
    
    
    /** 异常信息 */
    String getMsg();
}
