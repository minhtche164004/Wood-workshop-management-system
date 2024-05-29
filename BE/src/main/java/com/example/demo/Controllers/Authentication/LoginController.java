package com.example.demo.Controllers.Authentication;

import com.example.demo.Jwt.JwtAuthenticationResponse;
import com.example.demo.Jwt.RefreshTokenRequest;
import com.example.demo.Request.LoginRequest;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
@AllArgsConstructor
public class LoginController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody LoginRequest loginRequest, HttpSession session){
        ApiResponse<JwtAuthenticationResponse> apiResponse= new ApiResponse<>();
        apiResponse.setResult(userService.signin(loginRequest));
       String a = apiResponse.getResult().getToken();
        session.setAttribute("token", a);
        return apiResponse;
    }



////đang lỗi , nếu cần thiết thì ko cần refresh token mà sẽ trả về trang login để bắt login lại khi token hết hạn
//@PostMapping("/refresh")
//public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
//    return  ResponseEntity.ok(userService.refreshToken(refreshTokenRequest));
//}

}
