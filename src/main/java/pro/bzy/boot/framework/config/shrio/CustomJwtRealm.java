package pro.bzy.boot.framework.config.shrio;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.config.constant.JWT_constant;
import pro.bzy.boot.framework.config.jwt.JwtToken;
import pro.bzy.boot.framework.config.jwt.JwtUtil;

@Slf4j
public class CustomJwtRealm extends AuthorizingRealm{
    


    /*
     * 多重写一个support
     * 标识这个Realm是专门用来验证JwtToken
     * 不负责验证其他的token（UsernamePasswordToken）
     * */
    @Override
    public boolean supports(AuthenticationToken token) {
        //这个token就是从过滤器中传入的jwtToken
        return token instanceof JwtToken;
    }

    
    
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.err.println(principals);
        System.err.println("授权");
        return null;
    }

    
    
    //认证
    //这个token就是从过滤器中传入的jwtToken
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String access_token = (String) token.getPrincipal();
        if (!StringUtils.hasText(access_token)) {
            throw new NullPointerException("jwtToken认证失败, 原因： jwtToken为空");
        }
        // 判断
        JwtUtil jwtUtil = new JwtUtil();
        String username = (String) jwtUtil.decode(access_token).getOrDefault(JWT_constant.JWT_TOKEN_BASE_LOGIN_ISS_KEY, "").toString();
        String fromwhere = jwtUtil.decode(access_token).getOrDefault(JWT_constant.JWT_LOGIN_FROMWHERE_KEY, "").toString();
        log.info("【{}】用户【{}】使用token访问", fromwhere, username);
        return new SimpleAuthenticationInfo(access_token, access_token, "CustomJwtRealm");
        //这里返回的是类似账号密码的东西，但是jwtToken都是jwt字符串。还需要一个该Realm的类名

    }
}
