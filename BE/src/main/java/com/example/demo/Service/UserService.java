package com.example.demo.Service;

import com.example.demo.Dto.UserDTO;
import com.example.demo.Dto.UserUpdateDTO;
import com.example.demo.Entity.User;
import com.example.demo.Jwt.JwtAuthenticationResponse;
import com.example.demo.Jwt.RefreshTokenRequest;
import com.example.demo.Request.LoginRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public interface UserService {
    void save(UserDTO userDTO);

    User getUserbyEmail(String email);
  //  UserDetailsService userDetailsService();
    User signup(UserDTO userDTO);
    JwtAuthenticationResponse signin(LoginRequest loginRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
List<UserUpdateDTO> GetAllUser();
}
