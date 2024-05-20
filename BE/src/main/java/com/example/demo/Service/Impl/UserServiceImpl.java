package com.example.demo.Service.Impl;

import com.example.demo.Dto.UserDTO;
import com.example.demo.Jwt.UserDetailsServiceImpl;
import com.example.demo.Dto.UserUpdateDTO;
import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Jwt.JwtAuthenticationResponse;
import com.example.demo.Jwt.RefreshTokenRequest;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Request.LoginRequest;
import com.example.demo.Service.JWTService;
import com.example.demo.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public User signup(UserDTO userDTO){
        // Lấy thời gian hiện tại
        LocalDateTime currentDateTime = LocalDateTime.now();
        // Chuyển đổi từ LocalDateTime sang java.util.Date
        Date hireDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
        String pass=passwordEncoder.encode(userDTO.getPassword());
        if(checkEmail(userDTO.getEmail())==false) {
            throw new AppException(ErrorCode.WRONG_FORMAT_EMAIL);
            //hoacj tra ve RuntimeException , deu dc , vd:  throw new RuntimeException("Mail existed");
        }
        if(checkName(userDTO.getUsername())==false){
            throw  new AppException(ErrorCode.INVALID_NAME_FORMAT);
        }
        if (!userDTO.getPassword().equals(userDTO.getCheckPass())) {
            //   return ResponseEntity.badRequest().body("Passwords do not match");
            throw new AppException(ErrorCode.NOT_MATCH_PASS);
        }
        if(checkUserbyEmail(userDTO.getEmail())==false){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Role userRole=roleRepository.findByName("USER");

        User user = new User(
                userDTO.getUsername(),
                pass,
                userDTO.getEmail(),
                userDTO.getPhoneNumber(),
                userDTO.getAddress(),
                userDTO.getFullname(),
                true,
                userDTO.getPosition(),
                hireDate,
                userRole
        );
        return  userRepository.save(user);
    }

    @Override
    public JwtAuthenticationResponse signin(LoginRequest loginRequest){
        UserDetails user;
        try {
            user = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        } catch (UsernameNotFoundException e) {
            throw new AppException(ErrorCode.WRONG_PASS_OR_EMAIL);
        }
        User a = userRepository.getUserByEmail(user.getUsername());
      if(a.getStatus()==false){
         throw new AppException(ErrorCode.UN_ACTIVE_ACCOUNT);
}
        // Kiểm tra xem mật khẩu nhập vào có khớp với mật khẩu lưu trong cơ sở dữ liệu không
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.WRONG_PASS_OR_EMAIL);
        }
        // Nếu mọi thứ đều đúng, tạo JWT token và trả về
        var jwt = jwtService.generateToken(new HashMap<>(), user);
       // var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken("");
        jwtAuthenticationResponse.setUser(user);
        return jwtAuthenticationResponse;
    }

    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
     //   User user = userRepository.findByEmail(userEmail).orElseThrow();
        UserDetails user;
        try {
            user = userDetailsService.loadUserByUsername(userEmail);
        } catch (UsernameNotFoundException e) {
            throw new AppException(ErrorCode.WRONG_PASS_OR_EMAIL);
        }

        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var jwt = jwtService.generateToken(new HashMap<>(),user);
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(refreshTokenRequest.getToken());
            jwtAuthenticationResponse.setRefreshToken("");
            return jwtAuthenticationResponse;
        }
        return null;
    }


    @Override
    public void save(UserDTO userDTO) {

        // Lấy thời gian hiện tại
        LocalDateTime currentDateTime = LocalDateTime.now();
        // Chuyển đổi từ LocalDateTime sang java.util.Date
        Date hireDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
        String pass=passwordEncoder.encode(userDTO.getPassword());
        Role userRole=roleRepository.findByName("USER");
        User user = new User(
                userDTO.getUsername(),
                pass,
                userDTO.getEmail(),
                userDTO.getPhoneNumber(),
                userDTO.getAddress(),
                userDTO.getFullname(),
                true,
                userDTO.getPosition(),
                hireDate,
                userRole
        );
        userRepository.save(user);
    }




    public Boolean checkPasswordUser(String email, String password) {
   User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return false;
        }
        return true;
    }
    public Boolean checkUserbyEmail(String email) {

        return !userRepository.findByEmail(email).isPresent();
    }
    public Boolean checkEmail(String email) {
        Pattern p = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]+@[a-zA-Z]+(\\.[a-zA-Z]+)+$");
        return p.matcher(email).find();
    }
    public Boolean checkName(String name) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9 ]+$");
        return p.matcher(name).find();
    }
    @Override
    public User getUserbyEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public List<UserUpdateDTO> GetAllUser(){
        List<User> userList = userRepository.findAll();
        List<UserUpdateDTO> userUpdateDTOS = new ArrayList<>();

        for(User user : userList){
            UserUpdateDTO userDTO = new UserUpdateDTO();
            userDTO.setFullname(user.getFullname());
            userDTO.setEmail(user.getEmail());
            userDTO.setAddress(user.getAddress());
            userDTO.setPosition(user.getPosition());
            userDTO.setRole(user.getRole().getRoleName());
            userDTO.setStatus(user.getStatus());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setUsername(user.getUsername());
            // Gán các giá trị khác tương ứng từ user sang userDTO

            userUpdateDTOS.add(userDTO);
        }

        return userUpdateDTOS;
    }


}


