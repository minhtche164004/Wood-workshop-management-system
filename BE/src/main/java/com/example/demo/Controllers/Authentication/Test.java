package com.example.demo.Controllers.Authentication;

import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
@AllArgsConstructor
//@PreAuthorize("hasRole('USER')")
public class Test {
    @Autowired
    private UserRepository userRepository;
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("user")
    public ResponseEntity<String> Testuser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        ApiResponse<User> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(userRepository.findByEmail(authentication.getName()).get());
//        return apiResponse;
        return ResponseEntity.ok("trang danh cho user");
    }
   // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
   @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("admin")
    public ResponseEntity<String> Testadmin(){
        return ResponseEntity.ok("trang danh cho admin");

    }
        /*
    Có thể truy cập vào thuôc tính Result để lấy thông tin
      User a = apiResponse.getResult();
       String role= a.getRole();
     */
}


/*
Authentication trong Spring Security chứa các thông tin cơ bản của người dùng được xác thực. Đối tượng Authentication bao gồm một số thông tin như:

Principal: Đại diện cho người dùng được xác thực. Trong trường hợp của Spring Security, Principal thường là một đối tượng UserDetails.

Credentials: Thông tin xác thực của người dùng. Đối với một UsernamePasswordAuthenticationToken, Credentials thường chứa mật khẩu.

Authorities: Danh sách các quyền (roles) của người dùng.

Authenticated: Trạng thái xác thực của người dùng (đã xác thực hay chưa).
 */