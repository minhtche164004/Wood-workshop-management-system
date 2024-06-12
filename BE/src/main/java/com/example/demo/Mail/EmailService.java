package com.example.demo.Mail;

import com.example.demo.Controllers.Authentication.SendMailRequest;
import com.example.demo.Service.PaymentService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private PaymentService paymentService;

    @Value("${spring.mail.username}")
    private String emailSentTo;

    public void sendSimpleMessage(MailBody mailBody) {
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

    public String sendMail1(SendMailRequest sendMailRequest) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(emailSentTo);
            mimeMessageHelper.setTo(sendMailRequest.getTo());
            mimeMessageHelper.setCc(sendMailRequest.getCc());
            mimeMessageHelper.setSubject(sendMailRequest.getSubject());

            // Gọi API để lấy ảnh Base64
            String qrResponse = paymentService.getQRCodeBankingString(sendMailRequest.getAmount(), sendMailRequest.getOrderInfo());

            if (qrResponse == null || qrResponse.isEmpty()) {
                throw new RuntimeException("Invalid QR code data"); // Hoặc xử lý lỗi theo cách khác
            }

            String base64Image = qrResponse.split(",")[1];

            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            ByteArrayResource imageResource = new ByteArrayResource(imageBytes);

            mimeMessageHelper.addAttachment("QRCode.png", imageResource);

            String htmlContent = "<html><body>" + sendMailRequest.getBody() +
                    "<img src='data:image/png;base64, " + qrResponse + "' />" +
                    "</body></html>";
            mimeMessageHelper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);

            return "Email sent successfully";

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

/*  to: Đại diện cho địa chỉ email chính mà bạn muốn gửi email đến. Đây là người nhận chính và bắt buộc phải có trong email.

    cc: (Carbon Copy) Đại diện cho các địa chỉ email khác mà bạn muốn gửi một bản sao của email.
    Những người nhận trong phần cc sẽ thấy địa chỉ email của người nhận chính (to) và các địa chỉ cc khác.*/


}