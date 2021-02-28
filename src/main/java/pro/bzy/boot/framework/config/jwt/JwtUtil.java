package pro.bzy.boot.framework.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.google.common.collect.Maps;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import pro.bzy.boot.framework.utils.PropertiesUtil;
import pro.bzy.boot.framework.utils.SystemConstant;

import org.apache.commons.codec.binary.Base64;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 总的来说，工具类中有三个方法
 * 获取JwtToken，获取JwtToken中封装的信息，判断JwtToken是否存在
 * 1. encode()，参数是=签发人，存在时间，一些其他的信息=。返回值是JwtToken对应的字符串
 * 2. decode()，参数是=JwtToken=。返回值是荷载部分的键值对
 * 3. isVerify()，参数是=JwtToken=。返回值是这个JwtToken是否存在
 * */
@Slf4j
public class JwtUtil {
    
    

    // 创建默认的秘钥和算法，供无参的构造方法使用
    private static final String defaultbase64EncodedSecretKey = "bzy-proj";
    private static final SignatureAlgorithm defaultsignatureAlgorithm = SignatureAlgorithm.HS256;

    private final String base64EncodedSecretKey;
    private final SignatureAlgorithm signatureAlgorithm;

    public static final JwtUtil BASE_UTIL = new JwtUtil();
    
    public JwtUtil(String secretKey, SignatureAlgorithm signatureAlgorithm) {
        this.base64EncodedSecretKey = Base64.encodeBase64String(secretKey.getBytes());
        this.signatureAlgorithm = signatureAlgorithm;
    }
    public JwtUtil() {
        this(defaultbase64EncodedSecretKey, defaultsignatureAlgorithm);
    }
    
    
    
    
    /*
     * 这里就是产生jwt字符串的地方 jwt字符串包括三个部分 1. header -当前字符串的类型，一般都是“JWT”
     * -哪种算法加密，“HS256”或者其他的加密算法 所以一般都是固定的，没有什么变化 2. payload 一般有四个最常见的标准字段（下面有）
     * iat：签发时间，也就是这个jwt什么时候生成的 jti：JWT的唯一标识 iss：签发人，一般都是username或者userId exp：过期时间
     *
     */
    public String encode(String iss, long ttlMillis, Map<String, Object> claims) {
        // iss签发人，ttlMillis生存时间，claims是指还想要在jwt中存储的一些非隐私信息
        if (claims == null) {
            claims = new HashMap<>();
        }
        long nowMillis = System.currentTimeMillis();

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())// 2.这个是JWT的唯一标识，一般设置成唯一的，这个方法可以生成唯一标识
                .setIssuedAt(new Date(nowMillis))// 1. 这个地方就是以毫秒为单位，换算当前系统时间生成的iat
                .setSubject(iss)// 3. 签发人，也就是JWT是给谁的（逻辑上一般都是username或者userId）
                .signWith(signatureAlgorithm, base64EncodedSecretKey);// 这个地方是生成jwt使用的算法和秘钥
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + (ttlMillis*1000);
            Date exp = new Date(expMillis);// 4. 过期时间，这个也是使用毫秒生成的，使用当前时间+前面传入的持续时间生成
            builder.setExpiration(exp);
        } else {
            log.warn("jwtToken的过期时间小于0：" + ttlMillis);
        }
        return builder.compact();
    }
    
    

    // 相当于encode的方向，传入jwtToken生成对应的username和password等字段。Claim就是一个map
    // 也就是拿到荷载部分所有的键值对
    public Claims decode(String jwtToken) {

        // 得到 DefaultJwtParser
        return Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey(base64EncodedSecretKey)
                // 设置需要解析的 jwt
                .parseClaimsJws(jwtToken).getBody();
    }

    
    
    // 判断jwtToken是否合法
    public boolean isVerify(String jwtToken) {
        // 这个是官方的校验规则，这里只写了一个”校验算法“，可以自己加
        Algorithm algorithm = null;
        switch (signatureAlgorithm) {
        case HS256:
            algorithm = Algorithm.HMAC256(Base64.decodeBase64(base64EncodedSecretKey));
            break;
        default:
            throw new RuntimeException("不支持该算法");
        }
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(jwtToken); // 校验不通过会抛出异常
        } catch (TokenExpiredException e) {
            throw new TokenExpiredException("jwtToken已超时，orgExceptionMsg: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("jwtToken校验失败，原因：" + e.getMessage());
        }
        // 判断合法的标准：1. 头部和荷载部分没有篡改过。2. 没有过期
        return true;
    }

    public static String getToken(String key, long expiretime, Map<String, Object> map) {
        JwtUtil util = new JwtUtil();
        return util.encode(key, expiretime, map);
    }
    
    
    
    /**
     * 从claims中获取最基本必要的数据
     * @param claims
     * @return
     */
    public static Map<String, Object> getBaseStorageDatasFromClaims(Claims claims) {
        Map<String, Object> claimsDatas = Maps.newHashMapWithExpectedSize(5);
        claimsDatas.put(SystemConstant.JWT_LOGIN_FROMWHERE_KEY, claims.get(SystemConstant.JWT_LOGIN_FROMWHERE_KEY));
        claimsDatas.put(SystemConstant.JWT_LOGIN_USERID_KEY, claims.get(SystemConstant.JWT_LOGIN_USERID_KEY));
        claimsDatas.put(SystemConstant.JWT_LOGIN_USER, claims.get(SystemConstant.JWT_LOGIN_USER));
        claimsDatas.put(SystemConstant.JWT_LOGIN_USER_IP_KEY, claims.get(SystemConstant.JWT_LOGIN_USER_IP_KEY));
        return claimsDatas;
    }
    
    
    
    /**
     * 使用过期的accessToken 和 refreshToken刷新 access_token
     * @param expiredToken 过期token
     * @param refreshToken 刷新token
     * @return
     */
    public static String refreshJwtTokenByExpiredTokenAndRefreshToken(
            final String expiredToken, final String refreshToken) {
        
        // 1. refreshToken空校验
        //ExceptionCheckUtil.notNull(expiredToken, "expiredToken为空");
        //ExceptionCheckUtil.notNull(refreshToken, "refreshToken为空");
        
        // 2. 校验freshToken合法性
        JwtUtil util = new JwtUtil();
        boolean isRefreshTokenUsable = false;
        try {
            isRefreshTokenUsable = util.isVerify(refreshToken);
        } catch (TokenExpiredException e) {
            log.warn("refreshToken已过期失效，" + e.getMessage());
        } catch (Exception e) {
            log.error("refreshToken校验失败，原因: " + e.getMessage());
        }
        
        // 3. refreshToken合法后 使用refreshToken生成新的accessToken
        if (isRefreshTokenUsable) {
            Claims claims = util.decode(refreshToken);
            
            return getToken(
                    claims.getSubject(), 
                    PropertiesUtil.getJwtTokenExpire(SystemConstant.JWT_ACCESS_TOKEN_EXPIRE_KEY_IN_YML), 
                    getBaseStorageDatasFromClaims(claims));
        } else {
            throw new JwtException("使用refreshToken刷新access_token失败，freshToken不合法");
        }
    }
    
    
    public static Map<String, Object> refreshJwtTokenByExpiredTokenAndRefreshToken2(
            final String expiredToken, final String refreshToken) {
        
        // 2. 校验freshToken合法性
        JwtUtil util = new JwtUtil();
        boolean isRefreshTokenUsable = false;
        try {
            isRefreshTokenUsable = util.isVerify(refreshToken);
        } catch (TokenExpiredException e) {
            log.warn("refreshToken已过期失效，" + e.getMessage());
        } catch (Exception e) {
            log.error("refreshToken校验失败，原因: " + e.getMessage());
        }
        
        // 3. refreshToken合法后 使用refreshToken生成新的accessToken
        if (isRefreshTokenUsable) {
            Claims claims = util.decode(refreshToken);
            Map<String, Object> res = getBaseStorageDatasFromClaims(claims);
            String newAccessToken = getToken(
                    claims.getSubject(), 
                    PropertiesUtil.getJwtTokenExpire(SystemConstant.JWT_ACCESS_TOKEN_EXPIRE_KEY_IN_YML), 
                    res);
            res.put("newAccessToken", newAccessToken);
            return res;
        } else {
            throw new JwtException("使用refreshToken刷新access_token失败，freshToken不合法");
        }
    }
    
    
    
    
    

    public static void main(String[] args) {
        JwtUtil util = new JwtUtil("tom2", SignatureAlgorithm.HS256);
        // 以tom作为秘钥，以HS256加密
        Map<String, Object> map = new HashMap<>();
        map.put("username", "tom");
        map.put("password", "123456");
        map.put("age", 20);

        String jwtToken = util.encode("tom1111", 30000, map);
        String username = (String) util.decode(jwtToken).get("username");
        System.out.println(username);
        System.out.println(jwtToken);
        util.decode(jwtToken).entrySet().forEach((entry) -> {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        });
    }
}
