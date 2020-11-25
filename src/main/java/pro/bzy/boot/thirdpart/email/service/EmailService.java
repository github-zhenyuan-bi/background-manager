package pro.bzy.boot.thirdpart.email.service;


/**
 * 
 * 邮件服务
 * @author user
 *
 */
public interface EmailService {
    
    /** 发送普通邮件 */
    void sendSimpleEmail(String subject, String content);
    
    /** 发送html邮件 */
    void sendHtmlMail(String subject, String content);
    
    /** 发送附件邮件 */
    void sendAttachmentsMail(String subject, String content, String filePath);
}
