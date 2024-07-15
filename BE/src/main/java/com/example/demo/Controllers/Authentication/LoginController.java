package com.example.demo.Controllers.Authentication;

import com.example.demo.Config.RedisConfig;
import com.example.demo.Jwt.JwtAuthenticationResponse;
import com.example.demo.Jwt.RefreshTokenRequest;
import com.example.demo.Request.LoginRequest;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPooled;

@RestController
@RequestMapping("/api/auth/")
//@CrossOrigin(origins="http://localhost:5173")
@AllArgsConstructor
public class LoginController {
    @Autowired
    private UserService userService;
    private static final JedisPooled jedis = RedisConfig.getRedisInstance();

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody LoginRequest loginRequest){
        ApiResponse<JwtAuthenticationResponse> apiResponse= new ApiResponse<>();
        apiResponse.setResult(userService.signin(loginRequest));
        jedis.del("UserProfile");
        return apiResponse;
    }


    @PostMapping("/refreshToken")
    public ApiResponse<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        ApiResponse<JwtAuthenticationResponse> apiResponse= new ApiResponse<>();
        apiResponse.setResult(userService.refreshToken(refreshTokenRequest));
        return apiResponse;
    }




}
