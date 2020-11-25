package pro.bzy.boot.thirdpart.email.service.impl;

import java.io.File;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.Setter;
import pro.bzy.boot.thirdpart.email.exceptions.MyEmailException;
import pro.bzy.boot.thirdpart.email.service.EmailService;

/**
 * 
 * 邮件服务实现
 * @author user
 *
 */
@Service
@ConfigurationProperties(prefix = "spring.mail")
public class EmailServiceImpl implements EmailService {

    @Resource
    private JavaMailSender mailSender;
    
    /** 邮件发送方 */
    @Setter private String from;
    
    /** 邮件默认接受方 */
    @Setter private String to;
    
    @Override
    public void sendSimpleEmail(String subject, String content) {
        try {
            //创建SimpleMailMessage对象
            SimpleMailMessage message = new SimpleMailMessage();
            //邮件发送人
            message.setFrom(from);
            //邮件接收人
            message.setTo(to);
            //邮件主题
            message.setSubject(subject);
            //邮件内容
            message.setText(content);
            //发送邮件
            mailSender.send(message);
        } catch (Exception e) {
            throw new MyEmailException("邮件服务异常，原因：" + e.getMessage());
        }
    }

    
    
    @Override
    public void sendHtmlMail(String subject, String content) {
        //获取MimeMessage对象
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        try {
            messageHelper = new MimeMessageHelper(message, true);
            //邮件发送人
            messageHelper.setFrom(from);
            //邮件接收人
            messageHelper.setTo(to);
            //邮件主题
            message.setSubject(subject);
            //邮件内容，html格式
            messageHelper.setText(content, true);
            //发送
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new MyEmailException("邮件服务异常，原因：" + e.getMessage());
        }
    }

    
    
    
    @Override
    public void sendAttachmentsMail(String subject, String content, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new MyEmailException("邮件服务异常，原因：" + e.getMessage());
        }
    }

}
