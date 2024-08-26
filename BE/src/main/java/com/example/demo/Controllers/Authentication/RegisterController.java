package com.example.demo.Controllers.Authentication;


import com.example.demo.Dto.UserDTO.RegisterDTO;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Mail.EmailService;
import com.example.demo.Mail.MailBody;
import com.example.demo.Repository.ForgotPasswordRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/api/auth/")
//@CrossOrigin(origins="http://localhost:5173")
@AllArgsConstructor
public class RegisterController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final int MAX_ATTEMPTS = 3; //set cho việc nhập otp giới hạn là 3 lần

    //thêm cái @Valid vi toi bo cai validation vao trong UserDto
    //trả về object ApiResponse chứ ko phải uSER NỮA
    @PostMapping("/registration")
    public ApiResponse<String> registerUserAccount(@RequestBody @Valid RegisterDTO userDto, HttpSession session) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
            userService.checkConditions(userDto);
                // Lưu thông tin người dùng vào session (nếu cần)
                session.setAttribute("userDto", userDto);
                // Lưu email vừa nhập ở form đăng ký vào session
                session.setAttribute("gmailregister", userDto.getEmail());
                // Gửi email xác thực
                String otp = otpGenerator();
                // Lưu OTP vào session
                session.setAttribute("otp", otp);
                MailBody mailBody = MailBody.builder()
                        .to(userDto.getEmail())
                        .text("Đây là OTP cho xác thực đăng kí: " + otp)
                        .subject("[OTP cho Đăng Kí]")
                        .build();
                emailService.sendSimpleMessage(mailBody);
                apiResponse.setResult("Check Mail để kiểm tra OTP");
                return apiResponse;
          //   return ResponseEntity.ok("Registration successful, check your email for OTP");
    }

    //send mail for email verification
    @PostMapping("/verifyMail1/{otpverify}")
    public ApiResponse<String> verifyEmail(@PathVariable String otpverify, HttpSession session) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        String email = (String) session.getAttribute("gmailregister");
        String otp = (String) session.getAttribute("otp");
        RegisterDTO userDto = (RegisterDTO) session.getAttribute("userDto");

        Integer attempts = (Integer) session.getAttribute("attempts");
        if (attempts == null) {
            attempts = 0;
        }

        if (otpverify.equals(otp)) {
            // Xác thực OTP thành công
            session.removeAttribute("otp");
            session.removeAttribute("attempts"); // Reset số lần thử sau khi thành công
            userService.signup(userDto);
            apiResponse.setResult("Xác Thực OTP thành công, Vui Lòng quay lại Đăng Nhập");
            return apiResponse;
        } else {
            attempts++;
            session.setAttribute("attempts", attempts);
            if (attempts >= MAX_ATTEMPTS) {
                // Gửi lại email sau khi nhập sai quá số lần cho phép
                String newOtp = otpGenerator();
                session.setAttribute("otp", newOtp);

                MailBody mailBody = MailBody.builder()
                        .to(email)
                        .text("Đây là OTP mới cho xác thực đăng kí: " + newOtp)
                        .subject("[OTP Mới cho Đăng Kí]")
                        .build();
                emailService.sendSimpleMessage(mailBody);
                session.setAttribute("attempts", 0); // Reset số lần thử

               throw new AppException(ErrorCode.TOO_MANY_ATTEMPTS);

            } else {
                throw new AppException(ErrorCode.INVALID_OTP);
            }
        }
    }


    private String otpGenerator(){
        Random random= new Random();
        int otp = random.nextInt(100_000,999_999);
        return String.valueOf(otp);
    }
}
