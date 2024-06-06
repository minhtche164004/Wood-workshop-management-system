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
//    @PostMapping("/registration")
//    ApiResponse<User> registerUserAccount(@RequestBody @Valid UserDTO userDto) {
//        ApiResponse<User> apiResponse= new ApiResponse<>();
//
//        apiResponse.setResult( userService.signup(userDto));
//        return apiResponse;
//     /*   userService.signup(userDto);
//        return ResponseEntity.ok("Registration successful");*/
//    }
//}

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
                int otp = otpGenerator();
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
    public ApiResponse<String> verifyEmail(@PathVariable Integer otpverify ,HttpSession session){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        String email = (String) session.getAttribute("gmailregister");
      int otp=(int) session.getAttribute("otp");
        RegisterDTO b = (RegisterDTO) session.getAttribute("userDto");
        if(otpverify == otp){
            // Xác thực OTP thành công
            // Xóa OTP từ session sau khi sử dụng nó
            session.removeAttribute("otp");
            userService.signup(b);
            apiResponse.setResult("Xác Thực OTP thành công , Vui Lòng quay lại Đăng Nhập");
            return apiResponse;
           // return ResponseEntity.ok("Verify otp success, Register success, please lOGIN");
        }
        else{
            throw new AppException(ErrorCode.INVALID_OTP);
        }
    }

    private Integer otpGenerator(){
        Random random= new Random();
        return random.nextInt(100_000,999_999);

    }
}
