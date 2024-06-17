package com.example.demo.Controllers;


import com.example.demo.Entity.ChangePassword;
import com.example.demo.Entity.ForgotPassword;
import com.example.demo.Entity.User;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Mail.EmailService;
import com.example.demo.Mail.MailBody;
import com.example.demo.Repository.ForgotPasswordRepository;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RestController()
@RequestMapping("/api/auth/forgotPassword")
public class ForgotPasswordController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //send mail for email verification
    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email){
        User user = userRepository.findByEmail(email).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_EMAIL));
        int otp= otpGenerator();
        MailBody mailBody= MailBody.builder()
                .to(email)
                .text("Đây là OTP dành cho yêu cầu quên mật khẩu:" +otp)
                .subject("[OTP để xác thực quên mật khẩu]")
                .build();

        ForgotPassword fp= ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis()+40*1000))
                .user(user)
                .build();

        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(fp);

        return ResponseEntity.ok("Check OTP đã được gửi đến gmail!");

    }
//    @PostMapping("/verifyOtp/{otp}/{email}")
//    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email){
//        User user = userRepository.findByEmail(email).orElseThrow(()->new AppException(ErrorCode.WRONG_PASS_OR_EMAIL));
//        ForgotPassword fp= forgotPasswordRepository.findByOtpAndUser(otp,user).orElseThrow(()->new AppException(ErrorCode.INVALID_OTP));
//        if(fp.getExpirationTime().before(Date.from(Instant.now()))){
//            forgotPasswordRepository.deleteById(fp.getFpid());
//            return new ResponseEntity<>("OTP has expired!", HttpStatus.EXPECTATION_FAILED);
//        }
//        return ResponseEntity.ok("OTP verify!");
//    }
@PostMapping("/verifyOtp/{otp}/{email}")
public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email){
    User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_EMAIL));
    ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user).orElseThrow(() -> new AppException(ErrorCode.INVALID_OTP));

    if (fp.getExpirationTime().before(Date.from(Instant.now()))) { //check time xem otp đã hết hạn hay chưa
        // Xóa bản ghi OTP  nếu đã hết hạn
      forgotPasswordRepository.deleteById(fp.getFpid());

        // Tạo mã OTP mới
        int newOtp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("Đây là OTP dành cho quên mật khẩu: " + newOtp)
                .subject("[OTP dành cho quên mật khẩu]")
                .build();
//thời gian hết hạn là 40 giây
        ForgotPassword newFp = ForgotPassword.builder()
                .otp(newOtp)
                .expirationTime(new Date(System.currentTimeMillis() + 40 * 1000)) //
                .user(user)
                .build();

        // Gửi email với mã OTP mới
        emailService.sendSimpleMessage(mailBody);

        // Lưu thông tin OTP mới vào cơ sở dữ liệu
        forgotPasswordRepository.save(newFp);

        throw new AppException(ErrorCode.OTP_EXPIRED);
    }
    forgotPasswordRepository.deleteById(fp.getFpid()); //xác nhận xong thì xoá bản ghi cós trong forgotpass để có thể sử dụng cho lần sau nếu quên pass
    return ResponseEntity.ok("OTP verified!");

}

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword,
                                                        @PathVariable String email){
        if(!Objects.equals(changePassword.password(),changePassword.repeatPassword())){
            throw new AppException(ErrorCode.NOT_MATCH_PASS);
        }
        String encodedPassword = passwordEncoder.encode(changePassword.password());
        userRepository.updatePassword(email,encodedPassword);
        return ResponseEntity.ok("Password has been changed!");

    }

    private Integer otpGenerator(){
        Random random= new Random();
        return random.nextInt(100_000,999_999);
    }
}
