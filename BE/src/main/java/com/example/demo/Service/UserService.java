package com.example.demo.Service;

import com.example.demo.Dto.TestDTO1;
import com.example.demo.Dto.UserDTO;
import com.example.demo.Dto.UserUpdateDTO;
import com.example.demo.Entity.User;
import com.example.demo.Jwt.JwtAuthenticationResponse;
import com.example.demo.Jwt.RefreshTokenRequest;
import com.example.demo.Request.LoginRequest;

import org.springframework.stereotype.Service;

import java.util.List;
@Service

public interface UserService {
   // void save(UserDTO userDTO);

    User getUserbyEmail(String email);

   void checkConditions(UserDTO userDTO);

  //  UserDetailsService userDetailsService();
    User signup(UserDTO userDTO);
    JwtAuthenticationResponse signin(LoginRequest loginRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
List<UserUpdateDTO> GetAllUser();

    UserUpdateDTO GetUserById(int user_id);
    TestDTO1 FindbyId(int user_id);
    User FindbyId1(int user_id);
}
