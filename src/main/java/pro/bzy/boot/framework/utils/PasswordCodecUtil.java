package pro.bzy.boot.framework.utils;

import org.apache.commons.codec.digest.DigestUtils;

import pro.bzy.boot.framework.utils.parents.MyUtil;
import pro.bzy.boot.framework.web.domain.entity.User;

/**
 * 
 * 密码加密工具
 * @author user
 *
 */
public class PasswordCodecUtil implements MyUtil {

    private static final String CODE_BASE = "zhe_shi_yi_ge_fei_chang_nan_de_mi_ma_+-*/";
    
    /**
     * 对密码进行二次加密
     * @param user
     * @return
     */
    public final static String sha1(User user) {
        return DigestUtils.sha1Hex(user.getUsername() + user.getPassword() + CODE_BASE);
    }
    
    
    public final static String sha1Password(String username, String orgPasswod) {
        return DigestUtils.sha1Hex(username + orgPasswod + CODE_BASE);
    }
    
    
    public static void main(String[] args) {
        System.out.println(sha1Password("jubenAdmin", "5188d6b3d9f3883a5da741c747cc2e34"));
    }
}
