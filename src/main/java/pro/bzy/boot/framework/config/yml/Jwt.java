package pro.bzy.boot.framework.config.yml;

import lombok.Data;

@Data
public class Jwt {

    /** jwttoken access_token过期时长 */
    private Integer accessTokenExpire;
    
    /** jwttoken refresh_token过期时长 */
    private Integer refreshTokenExpire;
}
