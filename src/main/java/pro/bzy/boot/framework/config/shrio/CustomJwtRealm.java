package pro.bzy.boot.framework.config.shrio;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.config.jwt.JwtToken;
import pro.bzy.boot.framework.config.jwt.JwtUtil;
import pro.bzy.boot.framework.utils.SystemConstant;

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
        return null;
    }

    //认证
    //这个token就是从过滤器中传入的jwtToken
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String jwt = (String) token.getPrincipal();
        if (jwt == null) {
            throw new NullPointerException("jwtToken认证失败, 原因： jwtToken为空");
        }
        // 判断
        JwtUtil jwtUtil = new JwtUtil();
        //jwtUtil.isVerify(jwt);
        //下面是验证这个user是否是真实存在的
        String username = (String) jwtUtil.decode(jwt).get(SystemConstant.JWT_TOKEN_BASE_LOGIN_ISS_KEY);//判断数据库中username是否存在
        log.info("用户【{}】使用token访问", username);
        return new SimpleAuthenticationInfo(jwt, jwt, "CustomJwtRealm");
        //这里返回的是类似账号密码的东西，但是jwtToken都是jwt字符串。还需要一个该Realm的类名

    }
}
