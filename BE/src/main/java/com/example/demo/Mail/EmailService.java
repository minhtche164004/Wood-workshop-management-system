package com.example.demo.Mail;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailSentTo;
    public void sendSimpleMessage(MailBody mailBody){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("[Thông Báo Xưởng Gỗ]");
        message.setTo(mailBody.to());
        message.setFrom(emailSentTo);
        message.setText(mailBody.text());
        javaMailSender.send(message);
    }


    public String sendMail(MultipartFile[] file, String to, String[] cc, String subject, String body) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(emailSentTo);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setCc(cc);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body);

            for (int i = 0; i < file.length; i++) {
                mimeMessageHelper.addAttachment(
                        file[i].getOriginalFilename(),
                        new ByteArrayResource(file[i].getBytes()));
            }

            javaMailSender.send(mimeMessage);

            return "mail send";

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

/*  to: Đại diện cho địa chỉ email chính mà bạn muốn gửi email đến. Đây là người nhận chính và bắt buộc phải có trong email.

    cc: (Carbon Copy) Đại diện cho các địa chỉ email khác mà bạn muốn gửi một bản sao của email.
    Những người nhận trong phần cc sẽ thấy địa chỉ email của người nhận chính (to) và các địa chỉ cc khác.*/


}