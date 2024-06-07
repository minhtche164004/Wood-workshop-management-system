package com.example.demo.Service;

import com.example.demo.Dto.UserDTO.*;
import com.example.demo.Entity.User;
import com.example.demo.Jwt.JwtAuthenticationResponse;
import com.example.demo.Jwt.RefreshTokenRequest;
import com.example.demo.Request.LoginRequest;

import org.springframework.stereotype.Service;

import java.util.List;
@Service

public interface UserService {
   // void save(UserDTO userDTO);

   // User getUserbyEmail(String email);

   void checkConditions(RegisterDTO userDTO);
    List<UserDTO> FindByUsernameOrAddress(String key);
  //  UserDetailsService userDetailsService();
    User signup(RegisterDTO userDTO);
    JwtAuthenticationResponse signin(LoginRequest loginRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
List<UserDTO> GetAllUser();

    UserUpdateDTO GetUserById(int user_id);
    UserDTO FindbyId(int user_id);
    User FindbyId1(int user_id);

    UserDTO UpdateProfile(UpdateProfileDTO updateProfileDTO);

    void DeleteUserById(int userId);

    User CreateAccountForAdmin(User_Admin_DTO userDTO);

    void checkConditionsForAdmin(User_Admin_DTO userDTO);





}
