package pro.bzy.boot.framework.config.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import pro.bzy.boot.framework.web.domain.bean.R;


@ControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 请求参数校验异常捕捉
     */
    @ExceptionHandler({FormValidatedException.class})
    public @ResponseBody R<Object> formValidatedException( 
            HttpServletRequest request, FormValidatedException fve) {
        return R.builder().code(R.FORM_VALIDATE_ERROR).msg(fve.getMessage()).data(fve.getErrDatas()).build();
    }
    
    
    /**
     * shiro授权异常
     * @return
     */
    @ExceptionHandler({UnauthorizedException.class})
    public @ResponseBody R<String> shiroUnauthorizedException(
            HttpServletRequest request, UnauthorizedException uae) {
        return R.ofError(601, "Unauthorized", uae.getMessage());
    }
    
    
    /**
     * shiro认证异常
     * @return
     */
    @ExceptionHandler({AuthenticationException.class})
    public @ResponseBody R<Object> shiroAuthenticationException (
            HttpServletRequest request, AuthenticationException ae) {
        return R.builder()
                .code(R.SHIRO_AUTHENTICATION_ERROR)
                .msg("error")
                .data("认证失败，原因：" + ae.getMessage()).build();
    }
    
    
    
    @ExceptionHandler({MyFrameworkException.class})
    public @ResponseBody R<Object> myFrameworkException (HttpServletRequest request, MyFrameworkException mfe) {
        return R.builder().code(mfe.getCode()).msg(mfe.getMsg()).build();
    }
    
    
    
    /**
     * 全局未知异常捕捉
     */
    @ExceptionHandler(value = Exception.class)
    public @ResponseBody R<Object> unknownExceptionHandler(
            HttpServletRequest req, Exception e) {
        e.printStackTrace();
        return R.ofError(e);
    }
}