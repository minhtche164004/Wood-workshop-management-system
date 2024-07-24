package com.example.demo.Mail;

import com.example.demo.Controllers.Authentication.SendMailRequest;
import com.example.demo.Dto.OrderDTO.OrderDetailWithJobStatusDTO;
import com.example.demo.Entity.Orders;
import com.example.demo.Service.PaymentService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.thymeleaf.context.Context;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

import java.util.Base64;
import java.util.List;
import java.util.Locale;

import static org.antlr.v4.runtime.misc.Utils.readFile;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private PaymentService paymentService;

    @Value("${spring.mail.username}")
    private String emailSentTo;
    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendSimpleMessage(MailBody mailBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("[Thông Báo Xưởng Gỗ]");
        message.setTo(mailBody.to());
        message.setFrom(emailSentTo);
        message.setText(mailBody.text());
        javaMailSender.send(message);
    }

    public void sendEmailFromTemplate(String name,String email, List<OrderDetailWithJobStatusDTO> orderDetails, Orders order,String link) throws MessagingException {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            helper.setFrom(new InternetAddress(emailSentTo)); // Địa chỉ email người gửi
            helper.setTo(email);
            helper.setSubject("Thông tin chi tiết đơn hàng"); // Tiêu đề email

            // Tạo context cho Thymeleaf (chỉ cần một đối tượng Context)
        Context context = new Context();
            context.setVariable("orderDetails", orderDetails); // Đưa danh sách vào context
        context.setVariable("name", name); // Đưa danh sách vào context
        context.setVariable("order", order); // Đưa danh sách vào context
        context.setVariable("link", link); // Đưa danh sách vào context

            // Xử lý template HTML bằng Thymeleaf
            String htmlContent = templateEngine.process("sendmailtemplate.html", context);

            helper.setText(htmlContent, true); // Đặt nội dung email là HTML

            mailSender.send(mimeMessage);

    }





    public String sendMail1(SendMailRequest sendMailRequest) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(emailSentTo);
            mimeMessageHelper.setTo(sendMailRequest.getTo());
            mimeMessageHelper.setCc(sendMailRequest.getCc());
            mimeMessageHelper.setSubject(sendMailRequest.getSubject());

            String qrResponse = paymentService.getQRCodeBankingString(sendMailRequest.getAmount(), sendMailRequest.getOrderInfo());

            if (qrResponse == null || qrResponse.isEmpty()) {
                throw new RuntimeException("Invalid QR code data");
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