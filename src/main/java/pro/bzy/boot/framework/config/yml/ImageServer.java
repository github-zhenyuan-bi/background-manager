package pro.bzy.boot.framework.config.yml;

import lombok.Data;

@Data
public class ImageServer {
    
    /** 服务地址 */
    private String server;
    
    /** 图片存储根文件夹路径 */
    private String baseStoragePath;
    
    /** 用户头像照片存储文件夹路径 */
    private String userAvatarImagePath;
    
    
    
    /** 剧本封面图片路径 */
    private String jubenCoverImagePath;
    
    /** 剧本人物图片路径 */
    private String jubenCharacterImagePath;
    
    /** 剧本通知图标 */
    private String jubenBulletinIconImagePath;
    
    /** 剧本充值卡图片 */
    private String jubenRechargeCardImgPath;
    
    
    
    
    /** 微信小程序首页轮播图存储路径 */
    private String wxMiniprogramIndexImagePath;
    
    /** 微信小程序服务模块icon存储路径 */
    private String wxMiniprogramServerModuleIconPath;
    
    /** 微信小程序合作模块icon存储路径 */
    private String wxMiniprogramCooperationIconPath;
    
    
    
    public String buildStoragePath(String path) {
        return this.baseStoragePath+path;
    }
    
    public String buildUserAvatarPath() {
        return this.baseStoragePath+this.userAvatarImagePath;
    }
    public String buildBulletinIconPath() {
        return this.baseStoragePath + this.jubenBulletinIconImagePath;
    }
    
    
    
    public String buildUserAvatarServer() {
        return this.server + this.userAvatarImagePath;
    }
    public String buildServerPath(String path) {
        return this.server + path;
    }
    
}
