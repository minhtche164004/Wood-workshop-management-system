package com.example.demo.Controllers.Authentication;

import com.example.demo.Entity.User;
import com.example.demo.Mail.EmailService;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth/")
@AllArgsConstructor
//@PreAuthorize("hasRole('USER')")
public class Test {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("user")
    public ResponseEntity<String> Testuser() {
        return ResponseEntity.ok("trang danh cho Customer");
    }
   // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
   @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("admin")
    public ResponseEntity<String> Testadmin(){
        return ResponseEntity.ok("trang danh cho admin");

    }

//    @PostMapping("/send")
//    public String sendMail(@RequestParam(value = "file", required = false) MultipartFile[] file, String to, String[] cc, String subject, String body) {
//        return emailService.sendMail(file, to, cc, subject, body);
//    }

    @PostMapping("/sendQR")
    public String sendMail(@RequestBody SendMailRequest request ){
        return emailService.sendMail1(request);
    }

}

/*
Authentication trong Spring Security chứa các thông tin cơ bản của người dùng được xác thực. Đối tượng Authentication bao gồm một số thông tin như:

Principal: Đại diện cho người dùng được xác thực. Trong trường hợp của Spring Security, Principal thường là một đối tượng UserDetails.

Credentials: Thông tin xác thực của người dùng. Đối với một UsernamePasswordAuthenticationToken, Credentials thường chứa mật khẩu.

Authorities: Danh sách các quyền (roles) của người dùng.

Authenticated: Trạng thái xác thực của người dùng (đã xác thực hay chưa).
 */